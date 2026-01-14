package orca.statclocks.mixin.listener;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Armadillo.class)
public class ArmadilloMixin {
	
	
	@Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
	void mobInteract (Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
		
		
		if (!player.isLocalPlayer()) {
			MiscListeners.BRUSH_USE_LISTENER.applyToParts(player, player.getItemInHand(interactionHand), Items.ARMADILLO_SCUTE, 1);
		}
		
	}
}
