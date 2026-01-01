package orca.statclocks.loottable;

import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface LootTableModifier {
	
	HashMap<Identifier, ArrayList<LootTableModifier>> MODIFIER_DICT = new HashMap<>();
	List<LootTableModifier> GLOBAL_MODIFIERS = new ArrayList<>();

	static void modify (ResourceKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, HolderLookup.Provider registry) {
		//Leave data packs and modded tables alone
		if (!source.isBuiltin()) return;

		for (LootTableModifier modifier : GLOBAL_MODIFIERS) {
			modifier.ModifyTable(tableBuilder, source);
		}
		
		List<LootTableModifier> modifiers = MODIFIER_DICT.get(key.identifier());
		
		if (modifiers == null) return;
		
		for (LootTableModifier modifier : modifiers) {
			
			modifier.ModifyTable(tableBuilder, source);
			
		}
		
	}
	
	
	static void AddModifier (LootTableModifier modifier, Identifier... keys) {

		if (keys.length == 0) {
			GLOBAL_MODIFIERS.add(modifier);
		}

		for (Identifier key : keys) {
			
			ArrayList<LootTableModifier> modifiers;
			
			if (MODIFIER_DICT.containsKey(key)) {
				modifiers = MODIFIER_DICT.get(key);
			} else {
				modifiers = new ArrayList<>();
				MODIFIER_DICT.put(key, modifiers);
			}
			
			modifiers.add(modifier);
			
		}
		
	}
	
	
	void ModifyTable (LootTable.Builder tableBuilder, LootTableSource source);
	
}
