package orca.statclocks.mixin.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.StriderRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import orca.statclocks.StatClockScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StriderRenderState.class)
public abstract class StriderRenderStateMixin extends LivingEntityRenderState implements StatClockScreen.EquipmentAssigner {
	
	@Shadow
	public ItemStack saddle;
	
	public void statClocks$assign (ItemStack itemStack, Equippable equippable) {
		if (equippable.slot() == EquipmentSlot.SADDLE) {
			this.saddle = itemStack;
		}
	}
}
