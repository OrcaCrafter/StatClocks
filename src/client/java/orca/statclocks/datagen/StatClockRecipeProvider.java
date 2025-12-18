package orca.statclocks.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockPartType;
import orca.statclocks.lists.PartTypeInfo;
import orca.statclocks.lists.StatClockPartTypes;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class StatClockRecipeProvider extends FabricRecipeProvider {
	public StatClockRecipeProvider (FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected @NotNull RecipeProvider createRecipeProvider (HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes () {
				HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
				
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
				Set<Identifier> keys = StatClockPartTypes.STAT_PART_TYPES.keySet();
				
				for (Identifier key : keys) {
					Pair<StatClockPartType, PartTypeInfo> pair = StatClockPartTypes.STAT_PART_TYPES.get(key);
					
					StatClockPartType partType = pair.getA();
					PartTypeInfo info = pair.getB();
					
					if (!info.isCraftable()) continue;
					
					info.makeCraftingRecipe(this, partType.GetOutputItem(), output);
				}
				
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
		};
	}
	
	@Override
	public @NotNull String getName () {
		return "StatClockRecipeProvider";
	}
	
}
