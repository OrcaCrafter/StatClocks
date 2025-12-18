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
	
	static void modify (ResourceKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, HolderLookup.Provider registry) {
		//Leave data packs and modded tables alone
		if (!source.isBuiltin()) return;
		
		List<LootTableModifier> modifiers = MODIFIER_DICT.get(key.identifier());
		
		if (modifiers == null) return;
		
		for (LootTableModifier modifier : modifiers) {
			
			modifier.ModifyTable(tableBuilder, source);
			
		}
		
	}
	
	
	static void AddModifier (LootTableModifier modifier, Identifier... keys) {
		
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
	
	
	public abstract void ModifyTable (LootTable.Builder tableBuilder, LootTableSource source);
	
}
