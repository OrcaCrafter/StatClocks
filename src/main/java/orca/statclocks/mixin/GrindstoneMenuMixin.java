package orca.statclocks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.components.StatClockContent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin extends AbstractContainerMenu {
	
	protected GrindstoneMenuMixin (@Nullable MenuType<?> menuType, int i) {
		super(menuType, i);
	}
	
	@WrapOperation(method = "computeResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;hasAnyEnchantments(Lnet/minecraft/world/item/ItemStack;)Z"))
	boolean hasAnyEnchantments (ItemStack itemStack, Operation<Boolean> original) {
		return original.call(itemStack) || itemStack.get(StatClockContent.STAT_CLOCK_COMPONENT) != null;
	}
	
	@Inject(method = "removeNonCursesFrom", at = @At("TAIL"))
	private void removeNonCursesFrom (ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
		itemStack.remove(StatClockContent.STAT_CLOCK_COMPONENT);
	}
}
