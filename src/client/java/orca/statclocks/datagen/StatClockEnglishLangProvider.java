package orca.statclocks.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import orca.statclocks.components.StatClockPartType;

import java.util.concurrent.CompletableFuture;

import static orca.statclocks.lists.StatClockPartTypes.*;


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
		
		//Command succeeded
		translationBuilder.add("commands.addstatclock.success.single", "Added stat clock to %1$s");
		translationBuilder.add("commands.addstatclock.success.multiple", "Added %1$n stat clocks");
		
		//Command failed
		translationBuilder.add("commands.addstatclock.failed.entity", "No entity was found");
		translationBuilder.add("commands.addstatclock.failed.itemless", "%1$s is not holding any item");
		translationBuilder.add("commands.addstatclock.failed.incompatible", "%1$s cannot have a stat clock added");
		translationBuilder.add("commands.addstatclock.failed.invalid_filter", "%1$s:%2$s is not a valid filter for %3$s");
		translationBuilder.add("commands.addstatclock.failed", "Nothing Happened %1$s");
		
		
		//Add english part type terms
		translationBuilder.add("stat-clocks.tooltip.part_type_error", "Issue with part type: %1$s!");
		
		translatePartType(translationBuilder, EMPTY, "Blank Stat Clock");
		translatePartType(translationBuilder, BLOCKS_MINED, "Blocks Mined", "%1$s Mined");
		translatePartType(translationBuilder, TIMES_USED, "Times Used");
		translatePartType(translationBuilder, BLOCKS_IGNITED, "Blocks Ignited", "%1$s Ignited");
		translatePartType(translationBuilder, MOBS_IGNITED, "Mobs Ignited", "%1$s Ignited");
		translatePartType(translationBuilder, SHEARS_USED, "Times Using Shears");
		translatePartType(translationBuilder, SHEARS_USED_ITEM, "Items Sheared Off", "%1$s Sheared");
		translatePartType(translationBuilder, SHEARS_USED_BLOCK, "Blocks Sheared", "%1$s Sheared");
		translatePartType(translationBuilder, SHEARS_USED_ENTITY, "Times Using Shears", "%1$s Sheared");
		translatePartType(translationBuilder, BRUSH_USED, "Times Brushed", "%1$s Brushed Off");
		translatePartType(translationBuilder, BLOCK_LOOT_DROPPED,  "Block Loot Dropped", "%1$s Dropped");
		translatePartType(translationBuilder, DAMAGE_DEALT, "Damage Dealt", "Damage Dealt to %1$s");
		translatePartType(translationBuilder, DAMAGE_DEALT_DISTANCE, "Damage Dealt", "Damage Dealt to %1$s");
		translatePartType(translationBuilder, MOBS_KILLED, "Mobs Killed", "%1$s Killed");
		translatePartType(translationBuilder, MOB_LOOT_DROPPED, "Mob Loot Dropped", "%1$s Dropped");
		translatePartType(translationBuilder, DAMAGE_TAKEN, "Damage Taken", "Damage Taken from %1$s");
		translatePartType(translationBuilder, DAMAGE_REDUCED, "Damage Blocked", "Damage Taken from %1$s Blocked");
		translatePartType(translationBuilder, ATTACKS_BLOCKED, "Attacks Blocked", "Attacks from %1$s Blocked");
		translatePartType(translationBuilder, FALL_ATTACK_DISTANCE, "Distance Fallen Attacking", "Distance Fallen Attacking %1$s");
		translatePartType(translationBuilder, DISTANCE_WALKED, "Distance Walked", "Distance Walked on %1$s");
		translatePartType(translationBuilder, DISTANCE_CROUCHED, "Distance Crouched");
		translatePartType(translationBuilder, DISTANCE_SWAM, "Distance Swam");
		translatePartType(translationBuilder, DISTANCE_WADED, "Distance Waded");
		translatePartType(translationBuilder, TIME_UNDERWATER, "Time Underwater");
		translatePartType(translationBuilder, MINED_UNDERWATER, "Blocks Mined Underwater", "%1$s Mined Underwater");
		translatePartType(translationBuilder, ITEMS_CONSUMED, "Items Consumed", "%1$s Consumed");
		translatePartType(translationBuilder, DISTANCE_FALLEN, "Distance Fallen");
		translatePartType(translationBuilder, ITEMS_FISHED, "Times Fished", "Fished up %1$s");
		translatePartType(translationBuilder, FISH_CAUGHT, "Fish Caught");
		translatePartType(translationBuilder, TREASURE_CAUGHT, "Treasure Caught");
		translatePartType(translationBuilder, TRASH_CAUGHT, "Trash Caught");
		translatePartType(translationBuilder, MOBS_FISHED, "Mobs Fished", "%1$s Yoinked");
		translatePartType(translationBuilder, DISTANCE_FLOWN, "Distance Flown");
		translatePartType(translationBuilder, ROCKET_USAGE, "Rocket Duration Used");
		translatePartType(translationBuilder, DISTANCE_RODE, "Distance Rode", "Distance Rode on %1$s");
		translatePartType(translationBuilder, DISTANCE_FLOATED, "Distance by Happy Ghast");
		translatePartType(translationBuilder, VEHICLE_DISTANCE, "Distance Traveled");
		translatePartType(translationBuilder, TIME_PLAYED, "Time Played For");
		translatePartType(translationBuilder, TIMES_FINISHED, "Times Finished");
		
	}
	
	private void translatePartType (FabricLanguageProvider.TranslationBuilder builder, StatClockPartType type, String english, String filter) {
		
		builder.add("stat-clocks.tooltip.part_type_filtered:" + type.getId(), filter);
		
		translatePartType(builder, type, english);
	}
	
	private void translatePartType (FabricLanguageProvider.TranslationBuilder builder, StatClockPartType type, String english) {
		builder.add("stat-clocks.tooltip.part_type:" + type.getId(), english);
	}
}
