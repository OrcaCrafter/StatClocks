package orca.statclocks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.NautilusRenderState;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import orca.statclocks.StatClockScreen;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.Random;

@Mixin(NautilusRenderState.class)
public abstract class NautilusRenderStateMixin extends LivingEntityRenderState implements StatClockScreen.EquipmentAssigner, StatClockScreen.VariantRandomizer {
	
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
	
	@Shadow
	@Nullable
	public ZombieNautilusVariant variant;
	
	public void statClocks$randomizeVariant (Random random) {
		Optional<Registry<ZombieNautilusVariant>> optional = Minecraft.getInstance().getConnection().registryAccess().lookup(Registries.ZOMBIE_NAUTILUS_VARIANT);
		
		assert optional.isPresent();
		
		int index = random.nextInt(optional.get().size());
		
		variant = optional.get().get(index).orElseThrow().value();
	}
}
