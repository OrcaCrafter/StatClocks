package orca.statclocks.mixin.listener;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.Bogged;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;

@Mixin(Bogged.class)
public abstract class BoggedMixin extends AbstractSkeleton {
	
	
	@Unique
	Player shearingPlayer;
	
	protected BoggedMixin (EntityType<? extends AbstractSkeleton> entityType, Level level) {
		super(entityType, level);
	}
	
	@WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
	void hurtAndBreak (ItemStack instance, int i, LivingEntity livingEntity, EquipmentSlot equipmentSlot, Operation<Void> original) {
		
		if (livingEntity instanceof Player player) {
			MiscListeners.SHEARS_USE_LISTENER.applyToParts(player, instance, null, 1);
			MiscListeners.SHEARS_USE_LISTENER_ENTITY.applyToParts(player, instance, this, 1);
		}
		
		original.call(instance, i, livingEntity, equipmentSlot);
	}
	
	
	@Inject(method = "mobInteract",
		at = @At(value = "INVOKE",
			shift = At.Shift.AFTER,
			target = "Lnet/minecraft/world/entity/monster/skeleton/Bogged;shear(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/sounds/SoundSource;Lnet/minecraft/world/item/ItemStack;)V"
		)
	)
	void mobInteract (Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
		shearingPlayer = player;
	}
	
	@WrapOperation(method = "spawnShearedMushrooms", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/skeleton/Bogged;dropFromShearingLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/BiConsumer;)V"))
	private void spawnShearedMushrooms (Bogged instance, ServerLevel level, ResourceKey<LootTable> lootTableKey, ItemStack shears, BiConsumer<ServerLevel, ItemStack> spawnItem, Operation<Void> original) {
		
		if (shearingPlayer != null) {
			
			BiConsumer<ServerLevel, ItemStack> countAndSpawnItem = (serverLevel, loot) -> {
				
				MiscListeners.SHEARS_USE_LISTENER_ITEM.applyToParts(shearingPlayer, shears, loot, loot.getCount());
				
				spawnItem.accept(serverLevel, loot);
			};
			
			original.call(instance, level, lootTableKey, shears, countAndSpawnItem);
			
			shearingPlayer = null;
		} else {
			original.call(instance, level, lootTableKey, shears, spawnItem);
		}
	}
}
