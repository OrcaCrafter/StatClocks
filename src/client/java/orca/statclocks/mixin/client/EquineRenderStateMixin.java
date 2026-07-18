package orca.statclocks.mixin.client;

import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import orca.statclocks.StatClockScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EquineRenderState.class)
public abstract class EquineRenderStateMixin extends LivingEntityRenderState implements StatClockScreen.EquipmentAssigner {
	
	@Shadow
	public ItemStack saddle;
	
	@Shadow
	public ItemStack bodyArmorItem;
	
	public void statClocks$assign (ItemStack itemStack, Equippable equippable) {
		if (equippable.slot() == EquipmentSlot.BODY) {
			bodyArmorItem = itemStack;
		} else if (equippable.slot() == EquipmentSlot.SADDLE) {
			this.saddle = itemStack;
		}
	}
}
