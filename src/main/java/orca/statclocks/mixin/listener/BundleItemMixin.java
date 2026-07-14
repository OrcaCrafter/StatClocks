package orca.statclocks.mixin.listener;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin extends Item {
	
	public BundleItemMixin (Properties properties) {
		super(properties);
	}
	
}
