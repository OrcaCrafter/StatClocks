package orca.statclocks.mixin.listener;


import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {
	
	@Inject(method = "use", at = @At("HEAD"))
	public void use (Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
		if (player.isFallFlying()) {
			ItemStack fireworkStack = player.getItemInHand(interactionHand);
			
			Fireworks fireworkData = fireworkStack.get(DataComponents.FIREWORKS);
			
			if (fireworkData == null) return;
			
			ItemStack elytra = player.getItemBySlot(EquipmentSlot.CHEST);
			
			MiscListeners.FIREWORK_USED_LISTENER.applyToParts(player, elytra, null, fireworkData.flightDuration());
		}
	}
}
