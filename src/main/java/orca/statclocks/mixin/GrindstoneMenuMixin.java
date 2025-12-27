package orca.statclocks.mixin;


import net.minecraft.world.inventory.*;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin extends AbstractContainerMenu {
	
	protected GrindstoneMenuMixin (@Nullable MenuType<?> menuType, int i) {
		super(menuType, i);
	}
	
	
}
