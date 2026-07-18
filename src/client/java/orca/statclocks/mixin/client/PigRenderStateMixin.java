package orca.statclocks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import orca.statclocks.StatClockScreen;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.Random;

@Mixin(PigRenderState.class)
public abstract class PigRenderStateMixin extends LivingEntityRenderState implements StatClockScreen.EquipmentAssigner, StatClockScreen.VariantRandomizer {
	
	@Shadow
	public ItemStack saddle;
	
	public void statClocks$assign (ItemStack itemStack, Equippable equippable) {
		if (equippable.slot() == EquipmentSlot.SADDLE) {
			this.saddle = itemStack;
		}
	}
	
	@Shadow
	@Nullable
	public PigVariant variant;
	
	public void statClocks$randomizeVariant (Random random) {
		Optional<Registry<PigVariant>> optional = Minecraft.getInstance().getConnection().registryAccess().lookup(Registries.PIG_VARIANT);
		
		assert optional.isPresent();
		
		int index = random.nextInt(optional.get().size());
		
		variant = optional.get().get(index).orElseThrow().value();
	}
}
