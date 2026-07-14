package orca.statclocks.mixin.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.inventory.LoomMenu$6")
class LoomMenuMixin {
	@Shadow
	@Final
	LoomMenu field_7855;
	
	@Inject(method = "onTake", at = @At("HEAD"))
	void onTake (Player player, ItemStack itemStack, CallbackInfo ci) {
		ItemStack dye = this.field_7855.getDyeSlot().getItem();
		ItemStack pattern = this.field_7855.getPatternSlot().getItem();
		
		MiscListeners.DYE_USED_LISTENER.applyToParts(player, pattern, dye, 1);
	}
}
