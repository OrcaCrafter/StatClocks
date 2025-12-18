package orca.statclocks.mixin.listener;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Consumable.class)
public abstract class ConsumableMixin {
	
	
	@Inject(method = "onConsume", at = @At("HEAD"))
	public void onConsume (Level level, LivingEntity livingEntity, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
		
		if (livingEntity instanceof Player player) {
			ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
			MiscListeners.PLAYER_CONSUMES.applyToParts(helmet, itemStack, 1);
		}
	}
}
