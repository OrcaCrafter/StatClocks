package orca.statclocks.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockPartType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static orca.statclocks.lists.StatClockPartTypes.*;

public class StatClockRecipeProvider extends FabricRecipeProvider {
	public StatClockRecipeProvider (FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected @NotNull RecipeProvider createRecipeProvider (HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes () {
				//Deconstruct misc parts
				shapeless(RecipeCategory.MISC, StatClocksMod.NETHERITE_COUNTER, 4)
					.requires(StatClocksMod.STAT_CLOCKS_ITEMS)
					.requires(StatClocksMod.STAT_CLOCKS_ITEMS)
					.requires(StatClocksMod.STAT_CLOCKS_ITEMS)
					.requires(StatClocksMod.STAT_CLOCKS_ITEMS)
					.unlockedBy("has_misc_stat_clock_item", has(StatClocksMod.STAT_CLOCKS_ITEMS))
					.save(output, "deconstruct_stat_clocks");
				
				//Craft counter
				shaped(RecipeCategory.MISC, StatClocksMod.NETHERITE_COUNTER, 8)
					.pattern(" N ")
					.pattern("GRG")
					.pattern(" G ")
					.define('N', Items.NETHERITE_INGOT)
					.define('R', Items.REDSTONE)
					.define('G', Items.GOLD_INGOT)
					.unlockedBy(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
					.save(output, "craft_netherite_counter");
				
				//Craft stat clock
				shaped(RecipeCategory.MISC, StatClocksMod.STAT_CLOCK, 1)
					.pattern("GGG")
					.pattern("NNN")
					.pattern("GRG")
					.define('G', Items.GOLD_INGOT)
					.define('N', StatClocksMod.NETHERITE_COUNTER)
					.define('R', Items.REDSTONE)
					.unlockedBy(getHasName(StatClocksMod.NETHERITE_COUNTER), has(StatClocksMod.NETHERITE_COUNTER))
					.save(output, "craft_stat_clock");
				
				//Reset stat clock
				shapeless(RecipeCategory.MISC, StatClocksMod.STAT_CLOCK)
					.requires(StatClocksMod.STAT_CLOCK)
					.unlockedBy(getHasName(StatClocksMod.STAT_CLOCK), has(StatClocksMod.STAT_CLOCK))
					.save(output, "reset_stat_clock");
				
				
				//Craft blank part
				shaped(RecipeCategory.MISC, StatClocksMod.STAT_CLOCK_PART)
					.pattern(" G ")
					.pattern("G G")
					.pattern(" N ")
					.define('G', Items.GOLD_INGOT)
					.define('N', StatClocksMod.NETHERITE_COUNTER)
					.group("craft_stat_clock_part")
					.unlockedBy(getHasName(StatClocksMod.NETHERITE_COUNTER), has(StatClocksMod.NETHERITE_COUNTER))
					.save(output, "craft_stat_clock_part");
				
				
				//Craft stat part
				craftPart(BLOCKS_MINED, output, Items.IRON_PICKAXE);
				craftPart(TIMES_USED, output, Items.SHEARS);
				craftPart(BLOCKS_IGNITED, output, Items.FLINT_AND_STEEL);
				craftPart(MOBS_IGNITED, output, Items.FIRE_CHARGE);
				craftPart(SHEARS_USED, output, ItemTags.WOOL);
				craftPart(SHEARS_USED_ITEM, output, ItemTags.WOOL);
				craftPart(SHEARS_USED_BLOCK, output, ItemTags.WOOL);
				craftPart(SHEARS_USED_ENTITY, output, ItemTags.WOOL);
				craftPart(BRUSH_USED, output, Items.BRUSH);
				craftPart(BLOCK_LOOT_DROPPED, output, Items.CHEST);
				craftPart(DAMAGE_DEALT, output, Items.IRON_SWORD);
				craftPart(DAMAGE_DEALT_DISTANCE, output, Items.BOW);
				craftPart(MOBS_KILLED, output, Items.DIAMOND_SWORD);
				craftPart(MOB_LOOT_DROPPED, output, Items.BUNDLE);
				craftPart(DAMAGE_TAKEN, output, Items.IRON_HELMET);
				craftPart(DAMAGE_REDUCED, output, Items.GOLDEN_HELMET);
				craftPart(ATTACKS_BLOCKED, output, Items.SHIELD);
				craftPart(FALL_ATTACK_DISTANCE, output, Items.WIND_CHARGE);
				craftPart(DISTANCE_WALKED, output, Items.LEATHER_BOOTS);
				craftPart(DISTANCE_CROUCHED, output, Items.ECHO_SHARD);
				craftPart(DISTANCE_SWAM, output, Items.PRISMARINE_SHARD);
				craftPart(DISTANCE_WADED, output, Items.WATER_BUCKET);
				craftPart(TIME_UNDERWATER, output, Items.TURTLE_SCUTE);
				craftPart(MINED_UNDERWATER, output, Items.HEART_OF_THE_SEA);
				craftPart(ITEMS_CONSUMED, output, Items.BREAD);
				craftPart(DISTANCE_FALLEN, output, Items.FEATHER);
				craftPart(ITEMS_FISHED, output, ItemTags.FISHES);
				craftPart(FISH_CAUGHT, output, StatClocksMod.FISHABLE_FISH);
				craftPart(TREASURE_CAUGHT, output, StatClocksMod.FISHABLE_TREASURE);
				craftPart(TRASH_CAUGHT, output, StatClocksMod.FISHABLE_TRASH);
				craftPart(MOBS_FISHED, output, Items.FISHING_ROD);
				craftPart(ROCKET_USAGE, output, Items.FIREWORK_ROCKET);
				craftPart(DISTANCE_RODE, output, Items.SADDLE);
				craftPart(DISTANCE_FLOATED, output, ItemTags.HARNESSES);
				
				//Reset stat clock part
				shapeless(RecipeCategory.MISC, StatClocksMod.STAT_CLOCK_PART)
					.requires(StatClocksMod.STAT_CLOCK_PART)
					.unlockedBy(getHasName(StatClocksMod.STAT_CLOCK_PART), has(StatClocksMod.STAT_CLOCK_PART))
					.save(output, "reset_stat_clock_part");
				
				//Craft stat clock filter
				shaped(RecipeCategory.MISC, StatClocksMod.STAT_CLOCK_FILTER)
					.pattern("NNN")
					.pattern("NWN")
					.pattern("NCN")
					.define('N', Items.IRON_NUGGET)
					.define('W', ItemTags.WOOL)
					.define('C', StatClocksMod.NETHERITE_COUNTER)
					.unlockedBy(getHasName(StatClocksMod.NETHERITE_COUNTER), has(StatClocksMod.NETHERITE_COUNTER))
					.save(output, "craft_stat_clock_filter");
				
				//Reset stat clock filter
				shapeless(RecipeCategory.MISC, StatClocksMod.STAT_CLOCK_FILTER)
					.requires(StatClocksMod.STAT_CLOCK_FILTER)
					.unlockedBy(getHasName(StatClocksMod.STAT_CLOCK_FILTER), has(StatClocksMod.STAT_CLOCK_FILTER))
					.save(output, "reset_stat_clock_filter");
				
				//Craft stat clock remover
				shaped(RecipeCategory.MISC, StatClocksMod.STAT_CLOCK_REMOVER)
					.pattern("I ")
					.pattern("CI")
					.define('I', Items.IRON_INGOT)
					.define('C', StatClocksMod.NETHERITE_COUNTER)
					.unlockedBy(getHasName(StatClocksMod.STAT_CLOCK), has(StatClocksMod.STAT_CLOCK))
					.save(output, "craft_stat_clock_remover");
				
			}
			
			private void craftPart (StatClockPartType partType, RecipeOutput output, ItemLike item) {
				//TODO make shaped recipes with item stack outputs possible
//			recipeProvider.shapeless(RecipeCategory.MISC, outputItem)
//				.requires(StatClocksMod.NETHERITE_COUNTER)
//				.requires(Items.GOLD_INGOT, 3)
//				.requires(item)
//				.group("craft_stat_clock_part")
//				.unlockedBy("has_resource_for_" + id, recipeProvider.has(item))
//				.save(output, "craft_stat_clock_part_" + id + "_" + RecipeProvider.getItemName(item));
				
				//Lets you change the part type
				shapeless(RecipeCategory.MISC, partType.GetOutputItem())
					.requires(StatClocksMod.STAT_CLOCK_PART)
					.requires(item)
					.group("craft_stat_clock_part_alt")
					.unlockedBy("has_resource_for_" + partType.getId().getPath(), has(item))
					.save(output, "craft_stat_clock_part_alt_" + partType.getId().getPath() + "_" + RecipeProvider.getItemName(item));
			}
			
			private void craftPart (StatClockPartType partType, RecipeOutput output, TagKey<Item> tag) {
				//TODO make shaped recipes with item stack outputs possible
//				recipeProvider.shapeless(RecipeCategory.MISC, outputItem)
//				.requires(StatClocksMod.NETHERITE_COUNTER)
//				.requires(Items.GOLD_INGOT, 3)
//				.requires(tag)
//				.group("craft_stat_clock_part")
//				.unlockedBy("has_resource_for_" + id, recipeProvider.has(tag))
//				.save(output, "craft_stat_clock_part_" + id + "_" + tag.getTranslationKey());
				
				//Lets you change the part type
				shapeless(RecipeCategory.MISC, partType.GetOutputItem())
					.requires(StatClocksMod.STAT_CLOCK_PART)
					.requires(tag)
					.group("craft_stat_clock_part_alt")
					.unlockedBy("has_resource_for_" + partType.getId().getPath(), has(tag))
					.save(output, "craft_stat_clock_part_alt_" + partType.getId().getPath() + "_" + tag.getTranslationKey());
			}
		};
	}
	
	@Override
	public @NotNull String getName () {
		return "StatClockRecipeProvider";
	}
	
}
