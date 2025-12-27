package orca.statclocks.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import orca.statclocks.lists.PartTypeInfo;
import orca.statclocks.lists.StatClockPartTypes;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


public class StatClockEnglishLangProvider extends FabricLanguageProvider {
	protected StatClockEnglishLangProvider (FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		// Specifying en_us is optional, as it's the default language code
		super(dataOutput, "en_us", registryLookup);
	}
	
	@Override
	public void generateTranslations (HolderLookup.Provider wrapperLookup, FabricLanguageProvider.TranslationBuilder translationBuilder) {
		
		translationBuilder.add("item-group.stat_clock_part_types", "Stat Clock Part Types");
		
		translationBuilder.add("item.stat-clocks.stat_clock", "Stat Clock");
		translationBuilder.add("item.stat-clocks.stat_clock_part", "Stat Clock Part");
		translationBuilder.add("item.stat-clocks.stat_clock_filter", "Stat Clock Filter");
		translationBuilder.add("item.stat-clocks.netherite_counter", "Netherite Counter");
		translationBuilder.add("item.stat-clocks.stat_clock_remover", "Stat Clock Remover");
		
		translationBuilder.add("stat-clocks.view.title", "Stat Clock View");
		
		translationBuilder.add("stat-clocks.tooltip.blank_clock", "No Parts on Stat Clock");
		translationBuilder.add("stat-clocks.tooltip.clock_tool_type", "Clock is for %1$s");
		
		translationBuilder.add("stat-clocks.tooltip.filter_type:item", "Filtering for item:");
		translationBuilder.add("stat-clocks.tooltip.filter_type:block", "Filtering for block:");
		translationBuilder.add("stat-clocks.tooltip.filter_type:entity", "Filtering for entity:");
		translationBuilder.add("stat-clocks.tooltip.filter_use_info", "Use on block, item or enty to set");
		translationBuilder.add("stat-clocks.tooltip.filter_error", "Unknown issues with filter");
		
		translationBuilder.add("stat-clocks.tooltip.part_type_filter_tip_1", "Can filter based on %1$s type");
		translationBuilder.add("stat-clocks.tooltip.part_type_filter_tip_2", "Assign to a filter with an Anvil");
		
		//Units
		translationBuilder.add("stat-clocks.tooltip.unit.second",			"Second");
		translationBuilder.add("stat-clocks.tooltip.unit.second_plural",	"Seconds");
		translationBuilder.add("stat-clocks.tooltip.unit.minute",			"Minute");
		translationBuilder.add("stat-clocks.tooltip.unit.minute_plural",	"Minutes");
		translationBuilder.add("stat-clocks.tooltip.unit.hour",				"Hour");
		translationBuilder.add("stat-clocks.tooltip.unit.hour_plural",		"Hours");
		translationBuilder.add("stat-clocks.tooltip.unit.day",				"Day");
		translationBuilder.add("stat-clocks.tooltip.unit.day_plural",		"Days");
		translationBuilder.add("stat-clocks.tooltip.unit.week",				"Week");
		translationBuilder.add("stat-clocks.tooltip.unit.week_plural",		"Weeks");
		
		translationBuilder.add("stat-clocks.tooltip.unit.centimeter", "Centimeter");
		translationBuilder.add("stat-clocks.tooltip.unit.centimeter_plural", "Centimeters");
		translationBuilder.add("stat-clocks.tooltip.unit.meter", "Meter");
		translationBuilder.add("stat-clocks.tooltip.unit.meter_plural", "Meters");
		translationBuilder.add("stat-clocks.tooltip.unit.kilometer", "Kilometer");
		translationBuilder.add("stat-clocks.tooltip.unit.kilometer_plural", "Kilometers");
		
		
		//Add english part type terms
		
		translationBuilder.add("stat-clocks.tooltip.part_type_error", "Issue with part type: %1$s!");
		
		Set<Identifier> partTypeNames = StatClockPartTypes.STAT_PART_TYPES.keySet();
		for (Identifier id : partTypeNames) {
			PartTypeInfo info = StatClockPartTypes.STAT_PART_TYPES.get(id).getB();
			translationBuilder.add("stat-clocks.tooltip.part_type:" + id, info.getEnglish());
			
			if (info.isFilterable()) {
				translationBuilder.add("stat-clocks.tooltip.part_type_filtered:" + id, info.getEnglishFilter());
			}
		}
		
	}
}
