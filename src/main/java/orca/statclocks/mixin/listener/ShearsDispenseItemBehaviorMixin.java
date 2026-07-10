package orca.statclocks.mixin.listener;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsDispenseItemBehavior.class)
public class ShearsDispenseItemBehaviorMixin {
	
	
	@Inject(
		method = "tryShearEntity",
		at = @At(
			value = "RETURN"
		),
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;shearOffAllLeashConnections(Lnet/minecraft/world/entity/player/Player;)Z"),
			to = @At(value = "RETURN")
		)
	)
	private static void shearOffAllLeashConnections (ServerLevel serverLevel, BlockPos blockPos, ItemStack shears, CallbackInfoReturnable<Boolean> cir) {
		
		MiscListeners.SHEARS_USE_LISTENER_ITEM.applyToPartsNonPlayer(shears, Items.LEAD, 1);
	
	}
}
