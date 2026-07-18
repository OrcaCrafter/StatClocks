package orca.statclocks.mixin.client;

import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.animal.equine.Markings;
import net.minecraft.world.entity.animal.equine.Variant;
import orca.statclocks.StatClockScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(HorseRenderState.class)
public abstract class HorseRenderStateMixin extends LivingEntityRenderState implements StatClockScreen.VariantRandomizer {
	
	@Shadow
	public Variant variant;
	
	@Shadow
	public Markings markings;
	
	public void statClocks$randomizeVariant (Random random) {
		variant = Variant.values()[random.nextInt(Variant.values().length)];
		markings = Markings.values()[random.nextInt(Markings.values().length)];
	}
}
