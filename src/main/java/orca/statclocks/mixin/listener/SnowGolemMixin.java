package orca.statclocks.mixin.listener;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin extends AbstractGolem {
	
	protected SnowGolemMixin (EntityType<? extends AbstractGolem> entityType, Level level) {
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
	
	@WrapOperation(method = "shear", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/golem/SnowGolem;dropFromShearingLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/BiConsumer;)V"))
	void dropItemFromShearingLootTable (SnowGolem instance, ServerLevel level, ResourceKey<LootTable> resourceKey, ItemStack shears, BiConsumer<ServerLevel, ItemStack> dropItemFunction, Operation<Void> original) {
		
		BiConsumer<ServerLevel, ItemStack> countItemFunction = (serverLevel, loot) -> {
			MiscListeners.SHEARS_USE_LISTENER_ITEM.applyToPartsNonPlayer(shears, loot, loot.getCount());
			
			dropItemFunction.accept(serverLevel, loot);
		};
		
		original.call(instance, level, resourceKey, shears, countItemFunction);
	}
}
