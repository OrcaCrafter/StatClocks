package orca.statclocks.mixin.listener;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

@Mixin(TransmuteRecipe.class)
public class TransmuteRecipeMixin {
	
	
	@WrapOperation(
		method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/TransmuteResult;apply(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;")
	)
	ItemStack copy (TransmuteResult instance, ItemStack input, Operation<ItemStack> original, @Local(argsOnly = true) CraftingInput craftingInput) {
		
		ItemStack result = original.call(instance, input);
		
		ArrayList<Item> dyes = new ArrayList<>();
		
		for (int i = 0; i < craftingInput.size(); i++) {
			ItemStack itemStack2 = craftingInput.getItem(i);
			if (!itemStack2.isEmpty()) {
				if (itemStack2.getItem() instanceof DyeItem dyeItem) {
					dyes.add(dyeItem);
				}
			}
		}
		
		for (Item dye : dyes) {
			MiscListeners.DYE_USED_LISTENER.applyToPartsNonPlayer(result, dye, 1);
		}
		
		return result;
	}
	
	
}
