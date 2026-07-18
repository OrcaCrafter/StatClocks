package orca.statclocks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import orca.statclocks.StatClockScreen;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.Random;

@Mixin(WolfRenderState.class)
public abstract class WolfRenderStateMixin extends LivingEntityRenderState implements StatClockScreen.EquipmentAssigner, StatClockScreen.VariantRandomizer {
	
	@Shadow
	public ItemStack bodyArmorItem;
	
	
	public void statClocks$assign (ItemStack itemStack, Equippable equippable) {
		if (equippable.slot() == EquipmentSlot.BODY) {
			this.bodyArmorItem = itemStack;
		}
	}
	
	@Shadow
	public Identifier texture;
	
	@Shadow
	@Nullable
	public DyeColor collarColor;
	
	public void statClocks$randomizeVariant (Random random) {
		Optional<Registry<WolfVariant>> optional = Minecraft.getInstance().getConnection().registryAccess().lookup(Registries.WOLF_VARIANT);
		
		assert optional.isPresent();
		
		int index = random.nextInt(optional.get().size());
		
		WolfVariant variant = optional.get().get(index).orElseThrow().value();
		
		texture = variant.assetInfo().tame().texturePath();
		collarColor = DyeColor.values()[random.nextInt(DyeColor.values().length)];
	}
}
