package orca.statclocks.mixin.client;

import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import orca.statclocks.StatClockScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HappyGhastRenderState.class)
public abstract class HappyGhastRenderStateMixin extends LivingEntityRenderState implements StatClockScreen.EquipmentAssigner {
	
	@Shadow
	public ItemStack bodyItem;
	
	public void statClocks$assign (ItemStack itemStack, Equippable equippable) {
		if (equippable.slot() == EquipmentSlot.BODY) {
			this.bodyItem = itemStack;
		}
	}
}
