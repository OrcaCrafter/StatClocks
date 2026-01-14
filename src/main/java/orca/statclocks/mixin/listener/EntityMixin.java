package orca.statclocks.mixin.listener;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.advancements.criterion.PlayerInteractTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import orca.statclocks.listeners.MiscListeners;
import orca.statclocks.lists.StatClockPartTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	
	@Unique
	Player shearingPlayer;
	@Unique
	ItemStack shears;
	
	@WrapOperation(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;shearOffAllLeashConnections(Lnet/minecraft/world/entity/player/Player;)Z"))
	boolean shearOffAllLeashConnections (Entity instance, Player player, Operation<Boolean> original) {
		
		boolean sheared = original.call(instance, player);
		
		if (sheared) {
			ItemStack shears = StatClockPartTypes.getPriorityHandItem(player, tool -> tool.is(Items.SHEARS));
			
			MiscListeners.SHEARS_USE_LISTENER.applyToParts(player, shears, null, 1);
			MiscListeners.SHEARS_USE_LISTENER_ENTITY.applyToParts(player, shears, instance, 1);
		}
		
		return sheared;
	}
	
	@Inject(method = "dropAllLeashConnections", at = @At("HEAD"))
	void dropAllLeashConnectionsHEAD (Player player, CallbackInfoReturnable<Boolean> cir) {
		shearingPlayer = player;
		shears = StatClockPartTypes.getPriorityHandItem(player, tool -> tool.is(Items.SHEARS));
	}
	
	@WrapOperation(method = "dropAllLeashConnections", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Leashable;dropLeash()V"))
	void dropLeash (Leashable instance, Operation<Void> original) {
		original.call(instance);
		
		if (shearingPlayer != null) {
			MiscListeners.SHEARS_USE_LISTENER_ITEM.applyToParts(shearingPlayer, shears, Items.LEAD, 1);
		}
	}
	
	@Inject(method = "dropAllLeashConnections", at = @At("RETURN"))
	void dropAllLeashConnectionsRETURN (Player player, CallbackInfoReturnable<Boolean> cir) {
		shearingPlayer = null;
		shears = null;
	}
	
	
	@WrapOperation(method = "attemptToShearEquipment",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/criterion/PlayerInteractTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;)V"))
	void attemptToShearEquipment (PlayerInteractTrigger instance, ServerPlayer player, ItemStack drop, Entity entity, Operation<Void> original) {
		original.call(instance, player, drop, entity);
		
		ItemStack shears = StatClockPartTypes.getPriorityHandItem(player, tool -> tool.is(Items.SHEARS));
		
		MiscListeners.SHEARS_USE_LISTENER.applyToParts(player, shears, null, 1);
		MiscListeners.SHEARS_USE_LISTENER_ENTITY.applyToParts(player, shears, entity, 1);
		MiscListeners.SHEARS_USE_LISTENER_ITEM.applyToParts(player, shears, drop, drop.getCount());
		
	}
}
