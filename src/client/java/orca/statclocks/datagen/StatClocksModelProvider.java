package orca.statclocks.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.resources.ResourceKey;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockPartType;
import orca.statclocks.lists.StatClockPartTypes;

import java.util.ArrayList;

public class StatClocksModelProvider extends FabricModelProvider {
	
	
	public StatClocksModelProvider (FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generateBlockStateModels (BlockModelGenerators blockModelGenerator) {
	
	}
	
	@Override
	public void generateItemModels (ItemModelGenerators itemModelGenerator) {
		itemModelGenerator.generateFlatItem(StatClocksMod.STAT_CLOCK, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(StatClocksMod.STAT_CLOCK_FILTER, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(StatClocksMod.NETHERITE_COUNTER, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(StatClocksMod.STAT_CLOCK_REMOVER, ModelTemplates.FLAT_ITEM);
		
		ItemModel.Unbaked partBase = ItemModelUtils.plainModel(itemModelGenerator.createFlatItemModel(StatClocksMod.STAT_CLOCK_PART, ModelTemplates.FLAT_ITEM));
		
		ArrayList<SelectItemModel.SwitchCase<ResourceKey<StatClockPartType>>> cases = new ArrayList<>();
		
		StatClockPartTypes.STAT_PART_TYPES.forEach((id, pair) -> {
			StatClockPartType type = pair.getA();
			
			ItemModel.Unbaked partLayer = ItemModelUtils.plainModel(itemModelGenerator.createFlatItemModel(StatClocksMod.STAT_CLOCK_PART, "_icons/" + id.getPath(), ModelTemplates.FLAT_ITEM));
			
			cases.add(ItemModelUtils.when(key(type), partLayer));
		});
		
		itemModelGenerator.itemModelOutput.accept(
			StatClocksMod.STAT_CLOCK_PART,
			ItemModelUtils.composite(
				partBase,
				ItemModelUtils.select(
					new ContextPartType(), partBase, cases
				)
			)
		);
	}
	
	private static ResourceKey<StatClockPartType> key (StatClockPartType type) {
		return ResourceKey.create(StatClocksMod.PART_TYPES, type.getId());
	}
}
