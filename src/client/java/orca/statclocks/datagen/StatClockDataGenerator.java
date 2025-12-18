package orca.statclocks.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import orca.statclocks.datagen.tags.StatClockItemTagProvider;

public class StatClockDataGenerator implements DataGeneratorEntrypoint {
	
	@Override
	public void onInitializeDataGenerator (FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		
		pack.addProvider(StatClockItemTagProvider::new);
		
		pack.addProvider(StatClockRecipeProvider::new);
		
		pack.addProvider(StatClockEnglishLangProvider::new);
		
		pack.addProvider(StatClocksModelProvider::new);
		
		
	}
}
