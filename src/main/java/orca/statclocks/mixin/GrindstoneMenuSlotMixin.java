package orca.statclocks.mixin;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.components.StatClockContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = { "net/minecraft/world/inventory/GrindstoneMenu$2", "net/minecraft/world/inventory/GrindstoneMenu$3" })
public abstract class GrindstoneMenuSlotMixin {
	
	
	@WrapOperation(method = "mayPlace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"))
	boolean mayPlace (ItemStack instance, Operation<Boolean> original) {
		return original.call(instance) || instance.get(StatClockContent.STAT_CLOCK_COMPONENT) != null;
	}
	
}
