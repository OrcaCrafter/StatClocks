package orca.statclocks.lists;

import net.minecraft.network.chat.Component;

public interface PartValueFormat {
	
	PartValueFormat NONE = value -> Component.empty().append(Integer.toString(value));
	
	PartValueFormat DAMAGE = value -> {
		
		//Damage points to hearts???
		float valueAdjusted = value/10f;
		String formatString = "%.1f";
		
		return Component.empty().append(String.format(formatString, valueAdjusted));
	};
	
	PartValueFormat DISTANCE = value -> {
		
		//cm to m
		float valueAdjusted = value/100f;
		String suffix = "stat-clocks.tooltip.unit.meter";
		String formatString = "%.0f";
		
		if (valueAdjusted != 1) suffix = suffix + "_plural";
		
		return Component.empty()
			.append(String.format(formatString, valueAdjusted))
			.append(" ")
			.append(Component.translatable(suffix));
	};
	
	PartValueFormat TIME = value -> {
		
		//Ticks to seconds
		float valueAdjusted = value/20f;
		String suffix = "stat-clocks.tooltip.unit.second";
		String formatString = "%.2f";
		
		if (valueAdjusted != 1) suffix = suffix + "_plural";
		
		return Component.empty()
			.append(String.format(formatString, valueAdjusted))
			.append(" ")
			.append(Component.translatable(suffix));
	};
	
	
	public Component format (int value);
	
}
