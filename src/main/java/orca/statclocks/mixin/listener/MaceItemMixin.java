package orca.statclocks.mixin.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MaceItem.class)
public abstract class MaceItemMixin extends Item {
	
	public MaceItemMixin (Properties properties) {
		super(properties);
	}
	
	@Inject(method = "hurtEnemy", at = @At("RETURN"))
	public void hurtEnemy(ItemStack mace, LivingEntity target, LivingEntity attacker, CallbackInfo ci) {
		
		if (MaceItem.canSmashAttack(attacker)) {
			
			double fallCM = attacker.fallDistance*100;
			
			MiscListeners.MACE_FALL_LISTENER.applyToParts(mace, target, (int)fallCM);
			
		}
		
	}
}
