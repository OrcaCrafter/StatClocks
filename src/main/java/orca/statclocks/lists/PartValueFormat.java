package orca.statclocks.lists;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public interface PartValueFormat {
	
	PartValueFormat NONE = value -> Component.empty().append(Integer.toString(value));
	
	PartValueFormat DAMAGE = value -> {
		
		//Damage points to hearts???
		float valueAdjusted = value/10f;
		String formatString = "%.1f";
		
		return Component.empty().append(String.format(formatString, valueAdjusted));
	};
	
	record FormatEntry (int minValue, float scalar, String suffix, String format) {
		
		private static final String LOCAL_PREFIX = "stat-clocks.tooltip.unit.";
		
		public FormatEntry (int minValue, float scalar, String suffix, String format) {
			this.minValue = minValue;
			this.scalar = scalar;
			this.suffix = LOCAL_PREFIX + suffix;
			this.format = format;
		}
	
	}
	
	static FormatEntry findFormat (int value, FormatEntry[] entries, FormatEntry def) {
		
		for (int i = entries.length - 1; i >= 0; i --) {
			FormatEntry entry = entries[i];
			
			if (entry.minValue <= value) {
				return entry;
			}
		}
		
		return def;
	}
	
	FormatEntry[] DISTANCE_FORMATS = new FormatEntry[] {
		new FormatEntry(0, 1,  "centimeter", "%.0f"),
		new FormatEntry(100, 1f/100f,  "meter", "%.2f"),
		new FormatEntry(100000, 1f/100000,  "kilometer", "%.1f")
	};
	
	PartValueFormat DISTANCE = value -> {
		
		FormatEntry formatEntry = findFormat(value, DISTANCE_FORMATS, DISTANCE_FORMATS[0]);
		
		float valueAdjusted = (float)value * formatEntry.scalar;
		String suffix = formatEntry.suffix;
		
		if (valueAdjusted != 1) suffix = suffix + "_plural";
		
		return Component.empty()
			.append(String.format(formatEntry.format, valueAdjusted))
			.append(" ")
			.append(Component.translatable(suffix));
	};
	
	int TPS = 20;// Ticks per second
	int SPM = 60;// Seconds per minute
	int MPH = 60;// minutes per hour
	int HPD = 24;// hours per day
	int DPW = 7; // days per week
	
	int TPM = TPS*SPM;// ticks per minute = ticks per second * seconds per minute
	int TPH = TPM*MPH;// ticks per hour  = ticks per minute * minutes per hour
	int TPD = TPH*HPD;// ticks per day = ticks per hour * hours per day
	int TPW = TPD*DPW;// ticks per week = ticks per day * days per week
	
	FormatEntry[] TIME_FORMATS = new FormatEntry[] {
		new FormatEntry(0, 1f/TPS,  "second", "%.2f"),
		new FormatEntry(TPM, 1f/TPM,  "minute", "%.1f"),
		new FormatEntry(TPH,1f/TPH,  "hour", "%.1f"),
		new FormatEntry(TPD, 1f/TPD,  "day", "%.1f"),
		new FormatEntry(TPW, 1f/TPW,  "week", "%.1f")
	};
	
	PartValueFormat TIME = value -> {
		
		//Ticks to seconds
		FormatEntry formatEntry = findFormat(value, TIME_FORMATS, TIME_FORMATS[0]);
		
		float valueAdjusted = (float)value * formatEntry.scalar;
		String suffix = formatEntry.suffix;
		
		if (valueAdjusted != 1) suffix = suffix + "_plural";
		
		return Component.empty()
			.append(String.format(formatEntry.format, valueAdjusted))
			.append(" ")
			.append(Component.translatable(suffix));
	};
	
	
	public Component format (int value);
	
}
