package orca.statclocks.mixin.listener;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends Avatar {
	
	protected PlayerMixin (EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	public void tick (CallbackInfo ci) {
		
		if (isUnderWater()) {
			
			ItemStack helmet = getItemBySlot(EquipmentSlot.HEAD);
			MiscListeners.PLAYER_UNDERWATER.applyToParts((Player)(Object)this, helmet, null, 1);
		}
	
	}
	
	@Inject(method="killedEntity", at = @At("TAIL"))
	public void killedEntity (ServerLevel serverLevel, LivingEntity target, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		ItemStack weapon = damageSource.getWeaponItem();
		
		if (weapon == null ||weapon.isEmpty()) return;
		
		MiscListeners.ENTITY_KILLED_LISTENER.applyToParts((Player)(Object)this, weapon, target, 1);
	}
	
}
