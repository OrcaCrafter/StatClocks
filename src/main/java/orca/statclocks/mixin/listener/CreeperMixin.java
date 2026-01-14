package orca.statclocks.mixin.listener;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Creeper.class)
public class CreeperMixin {
	
	@Inject(method = "mobInteract", at = @At("HEAD"))
	protected void mobInteract (Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
		ItemStack tool = player.getItemInHand(interactionHand);
		
		//CREEPER_IGNITERS does include fire charges by default, but fire charges can't have stat clocks,
		//so no unintended behavior is expected.
		if (tool.is(ItemTags.CREEPER_IGNITERS)) {
			MiscListeners.MOBS_IGNITED.applyToParts(player, tool, this, 1);
		}
		
	}
}
