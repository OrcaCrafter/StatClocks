package orca.statclocks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public interface ToolTipComponent {
	
	public void addToTooltip (ItemStack stack, Item.TooltipContext context, TooltipFlag type, List<Component> list);
	
}
