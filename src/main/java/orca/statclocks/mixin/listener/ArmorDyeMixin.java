package orca.statclocks.mixin.listener;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ArmorDyeRecipe;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ArmorDyeRecipe.class)
public class ArmorDyeMixin {
	
	
	@WrapOperation(
		method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/DyedItemColor;applyDyes(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;)Lnet/minecraft/world/item/ItemStack;")
	)
	ItemStack applyDyes (ItemStack itemStack, List<DyeItem> list, Operation<ItemStack> original, @Local List<DyeItem> dyes) {
		
		for (Item dye : dyes) {
			MiscListeners.DYE_USED_LISTENER.applyToPartsNonPlayer(itemStack, dye, 1);
		}
		
		return itemStack;
	}
	
	
}
