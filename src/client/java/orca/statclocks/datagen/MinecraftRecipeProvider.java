package orca.statclocks.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.TransmuteRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MinecraftRecipeProvider extends FabricRecipeProvider {
	public MinecraftRecipeProvider (FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected @NotNull RecipeProvider createRecipeProvider (HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes () {
				
				//Fix for dying ghast harnesses
				Ingredient harnessIngredient = this.tag(ItemTags.HARNESSES);
				Ingredient bedIngredient = this.tag(ItemTags.BEDS);
				
				for (DyeColor dyeColor : DyeColor.values()) {
					dyeOverride(dyeColor, "harness_dye", getHarnessByColor(dyeColor), harnessIngredient);
					dyeOverride(dyeColor, "bed_dye", getBedByColor(dyeColor), bedIngredient);
					
				}
			}
			
			private void dyeOverride (DyeColor dyeColor, String group, Item output, Ingredient ingredient) {
				String name = "dye_" + BuiltInRegistries.ITEM.getKey(output).getPath();
				
				DyeItem dyeItem = DyeItem.byColor(dyeColor);
				TransmuteRecipeBuilder.transmute(RecipeCategory.TOOLS, ingredient, Ingredient.of(dyeItem), output)
					.group(group)
					.unlockedBy(getHasName(dyeItem), this.has(dyeItem))
					.save(this.output, name);
			}
			
			private Item getHarnessByColor (DyeColor color) {
				//TODO switch with hashmap?
				return switch (color) {
					case WHITE -> Items.WHITE_HARNESS;
					case ORANGE -> Items.ORANGE_HARNESS;
					case MAGENTA -> Items.MAGENTA_HARNESS;
					case LIGHT_BLUE -> Items.LIGHT_BLUE_HARNESS;
					case YELLOW -> Items.YELLOW_HARNESS;
					case LIME -> Items.LIME_HARNESS;
					case PINK -> Items.PINK_HARNESS;
					case GRAY -> Items.GRAY_HARNESS;
					case LIGHT_GRAY -> Items.LIGHT_GRAY_HARNESS;
					case CYAN -> Items.CYAN_HARNESS;
					case PURPLE -> Items.PURPLE_HARNESS;
					case BLUE -> Items.BLUE_HARNESS;
					case BROWN -> Items.BROWN_HARNESS;
					case GREEN -> Items.GREEN_HARNESS;
					case RED -> Items.RED_HARNESS;
					case BLACK -> Items.BLACK_HARNESS;
				};
			}
			
			private Item getBedByColor (DyeColor color) {
				//TODO check for better method
				return switch (color) {
					case WHITE -> Items.WHITE_BED;
					case ORANGE -> Items.ORANGE_BED;
					case MAGENTA -> Items.MAGENTA_BED;
					case LIGHT_BLUE -> Items.LIGHT_BLUE_BED;
					case YELLOW -> Items.YELLOW_BED;
					case LIME -> Items.LIME_BED;
					case PINK -> Items.PINK_BED;
					case GRAY -> Items.GRAY_BED;
					case LIGHT_GRAY -> Items.LIGHT_GRAY_BED;
					case CYAN -> Items.CYAN_BED;
					case PURPLE -> Items.PURPLE_BED;
					case BLUE -> Items.BLUE_BED;
					case BROWN -> Items.BROWN_BED;
					case GREEN -> Items.GREEN_BED;
					case RED -> Items.RED_BED;
					case BLACK -> Items.BLACK_BED;
				};
			}
			
		};
	}
	
	@Override
	public @NotNull String getName () {
		return "MinecraftRecipeProvider";
	}
	
	protected Identifier getRecipeIdentifier(Identifier identifier) {
		return Identifier.withDefaultNamespace(identifier.getPath());
	}
}
