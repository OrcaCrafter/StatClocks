package orca.statclocks.loottable;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import static orca.statclocks.StatClocksMod.MOD_ID;

public class CustomLootRegistries {
	public static final LootItemFunctionType<SetStatClockFunction> SET_STAT_CLOCK = Registry.register(
		BuiltInRegistries.LOOT_FUNCTION_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "set_stat_clock"),
		new LootItemFunctionType<>(SetStatClockFunction.CODEC)
	);
	
	
	public static void init () {};
	
}
