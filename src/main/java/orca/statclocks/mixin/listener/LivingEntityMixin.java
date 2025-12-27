package orca.statclocks.mixin.listener;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.components.StatClockPartContent;
import orca.statclocks.listeners.DamageListener;
import orca.statclocks.listeners.MiscListeners;
import orca.statclocks.lists.StatClockPartTypes;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Objects;

import static orca.statclocks.listeners.DamageListener.WOLF_DAMAGE_BLOCKED_ADAPTER;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	
	@Shadow
	public abstract float getHealth ();
	
	public LivingEntityMixin (EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	
	/*
	@Shadow
	protected abstract <T> boolean applyImplicitComponentIfPresent(DataComponentGetter dataComponentGetter, DataComponentType<T> dataComponentType);
	
	@Shadow
	public float moveDist;*/
	
	@Inject(method = "tick", at = @At("HEAD"))
	public void tick (CallbackInfo ci) {
		
		Entity thisEntity = (Entity)(Object)this;
		
		StatClockContent clock = thisEntity.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (clock == null) return;
		
		StatClocksMod.LOGGER.info("IT WORKSSSSSSSSSSSSSSSSSSSS!!!!!!!!!!!!!!!!!!!!!!!");
		
		ArrayList<StatClockPartContent> parts = clock.getParts();
		
		for (StatClockPartContent part : parts) {
		
			if (part.getType() == StatClockPartTypes.VEHICLE_DISTANCE) {
				//Meters to cm
				float cm = this.moveDist * 100f;
				part.incrementCount((int)cm);
			}
		}
	
	}
	
	/*
	//Add stat clocks as a component to apply
	@Inject(method = "applyImplicitComponents", at = @At("HEAD"))
	protected void applyImplicitComponents(DataComponentGetter dataComponentGetter, CallbackInfo ci) {
		//boolean success = this.applyImplicitComponentIfPresent(dataComponentGetter, DataComponents.CUSTOM_DATA);
	}*/
	
	
	//
 	//		ARMOR
 	//
	
	@Shadow public abstract boolean isDeadOrDying ();
	@Shadow public abstract boolean isInvulnerableTo (ServerLevel serverLevel, DamageSource damageSource);
	@Shadow public abstract double getAttributeValue (Holder<Attribute> holder);
	@Shadow public abstract int getArmorValue ();
	@Shadow public abstract boolean hasEffect (Holder<MobEffect> holder);
	@Shadow public abstract @Nullable MobEffectInstance getEffect (Holder<MobEffect> holder);
	
	@Shadow
	protected float lastHurt;
	
	@Inject(method = "hurtServer", at = @At("HEAD"))
	public void hurtServer (ServerLevel serverLevel, DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
		if (this.isInvulnerableTo(serverLevel, damageSource)) return;
		if (this.isDeadOrDying()) return;
		if (damageSource.is(DamageTypeTags.IS_FIRE) && this.hasEffect(MobEffects.FIRE_RESISTANCE)) return;
		if (this.invulnerableTime > 10.0F && !damageSource.is(DamageTypeTags.BYPASSES_COOLDOWN) && damage <= this.lastHurt) return;
		
		//Shields are handled elsewhere
		
		LivingEntity thisEntity = (LivingEntity)(Object)this;
		
		if (thisEntity instanceof Wolf wolf) {
			//Wolf armor is handled differently, only blocking damage
			ItemStack armor = wolf.getBodyArmorItem();
			
			WOLF_DAMAGE_BLOCKED_ADAPTER.applyToPartsNonPlayer(armor, damageSource.getEntity(), (int)(damage * 10));
		}
		
		float damageAfterArmor		= this.getDamageAfterArmor(damageSource, damage);
		float damageAfterResistance = this.getDamageAfterResistance(damageSource, damageAfterArmor);
		float damageAfterProtection = this.getDamageAfterProtection(damageSource, damageAfterResistance);
		
		//Note the damage blocked counter only counts damage blocked by the armor, and any protection enchantments
		//The damage taken is based on how much is actually taken from the targets health
		float damageBlocked = damage - (damageAfterArmor);
		damageBlocked += damageAfterResistance - damageAfterProtection;
		
		float damageTaken = damage - damageBlocked;
		
		damageBlocked = Math.clamp(damageBlocked, 0, this.getHealth());
		damageTaken = Math.clamp(damageTaken, 0, this.getHealth());
		
		//Limit rounding issues
		damageBlocked *= 10;
		damageTaken *= 10;
		
		if (thisEntity instanceof Player player) {
			//NOTE humanoids should not be able to fake player stats
			DamageListener.PLAYER_DAMAGE_LISTENER.onTakeDamage(player, damageSource, damageBlocked, damageTaken);
		} else if (thisEntity instanceof AbstractHorse horse) {
			//Horse, donkey, mule, zombie horse, skeleton horse, camel, husk
			DamageListener.HORSE_DAMAGE_LISTENER.onTakeDamage(horse, damageSource, damageBlocked, damageTaken);
		} else if (thisEntity instanceof AbstractNautilus nautilus) {
			DamageListener.NAUTILUS_DAMAGE_LISTENER.onTakeDamage(nautilus, damageSource, damageBlocked, damageTaken);
		}
		
		ItemStack weapon = damageSource.getWeaponItem();
		
		if (damageSource.getEntity() instanceof Player player && weapon != null) {
			MiscListeners.DAMAGE_DEALT.applyToParts(player, weapon, thisEntity, (int)damageTaken);
			
			if (player.level().dimension() == thisEntity.level().dimension()) {
				
				int distanceCM = (int) (thisEntity.distanceTo(player) * 100f);
				
				MiscListeners.DAMAGE_DEALT_DISTANCE.applyToParts(player, weapon, thisEntity, distanceCM);
			}
		}
	}
	
	//Can't use this method, as calling it takes durability away from armor
	//@Shadow protected abstract float getDamageAfterArmorAbsorb (DamageSource damageSource, float damageIn);
	//Instead use this re-implementation
	@Unique
	private float getDamageAfterArmor (DamageSource damageSource, float damage) {
		int armorValue = this.getArmorValue();
		float armorToughness = (float)this.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
		
		return CombatRules.getDamageAfterAbsorb((LivingEntity)(Object)this, damage, damageSource, armorValue, armorToughness);
	}
	
	//Can't use this method, as calling it adds to the players stats
	//abstract float getDamageAfterMagicAbsorb (DamageSource damageSource, float damageIn);
	//Instead use this re-implementation
	@Unique
	private float getDamageAfterResistance (DamageSource damageSource, float damage) {
		if (damageSource.is(DamageTypeTags.BYPASSES_EFFECTS)) {
			return damage;
		} else {
			if (this.hasEffect(MobEffects.RESISTANCE) && !damageSource.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
				//Clunkier than source code because null check doesn't recognize the hasEffect method
				int i = (Objects.requireNonNull(this.getEffect(MobEffects.RESISTANCE)).getAmplifier() + 1)*5;
				
				int j = 25 - i;
				float g = damage*j;
				float h = damage;
				damage = Math.max(g/25.0F, 0.0F);
				float k = h - damage;
			}
			
			return Math.max(damage, 0.0f);
		}
	}
	
	@Unique
	private float getDamageAfterProtection (DamageSource damageSource, float damage) {
		if (!damageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
			
			if (this.level() instanceof ServerLevel serverLevel) {
				float protection = EnchantmentHelper.getDamageProtection(serverLevel, (LivingEntity)(Object)this, damageSource);
				
				if (protection > 0.0F) {
					damage = CombatRules.getDamageAfterMagicAbsorb(damage, protection);
				}
				
			}
		}
		
		return damage;
	}
	
	
	//
 	//		LOOT
 	//
	
	
	@Inject(
		method = "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;ZLnet/minecraft/resources/ResourceKey;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;ZLnet/minecraft/resources/ResourceKey;Ljava/util/function/Consumer;)V"
		),
		cancellable = true
	)
	public void dropFromLootTable (ServerLevel serverLevel, DamageSource damageSource, boolean bl, ResourceKey<LootTable> resourceKey, CallbackInfo ci) {
		ci.cancel();
		
		LivingEntity thisEntity = (LivingEntity)(Object)this;
		thisEntity.dropFromLootTable(serverLevel, damageSource, bl, resourceKey, itemStack -> this.spawnAndCount(serverLevel, damageSource, itemStack));
	}
	
	
	@Unique
	public void spawnAndCount (ServerLevel serverLevel, DamageSource source, ItemStack itemStack) {
		if (itemStack.isEmpty()) return;
		
		this.spawnAtLocation(serverLevel, itemStack);
		
		ItemStack weapon = source.getWeaponItem();
		Entity entity = source.getEntity();
		
		if (weapon == null) return;
		if (!(entity instanceof Player player)) return;
		
		MiscListeners.MOB_LOOT_LISTENER.applyToParts(player, weapon, itemStack, itemStack.getCount());
	}
}