package orca.statclocks.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import orca.statclocks.StatClocksMod;

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
		itemModelGenerator.generateFlatItem(StatClocksMod.STAT_CLOCK_PART, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(StatClocksMod.STAT_CLOCK_FILTER, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(StatClocksMod.NETHERITE_COUNTER, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(StatClocksMod.STAT_CLOCK_REMOVER, ModelTemplates.FLAT_ITEM);
		
		
	}
}
