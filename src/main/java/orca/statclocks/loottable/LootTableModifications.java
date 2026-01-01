package orca.statclocks.loottable;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import orca.statclocks.lists.StatClockPartTypes;

import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static orca.statclocks.StatClocksMod.*;
import static orca.statclocks.components.StatClockPartType.PART_TYPE_COMPONENT;
import static orca.statclocks.loottable.DataLootItem.dataBuilder;

public class LootTableModifications {
	
	private static final Identifier ABANDONED_MINESHAFT = getIdentifier("chests/abandoned_mineshaft");
	private static final Identifier BURIED_TREASURE = getIdentifier("chests/buried_treasure");
	private static final Identifier DESERT_PYRAMID = getIdentifier("chests/desert_pyramid");
	private static final Identifier IGLOO = getIdentifier("chests/igloo_chest");
	private static final Identifier JUNGLE_TEMPLE = getIdentifier("chests/jungle_temple");
	private static final Identifier NETHER_BRIDGE = getIdentifier("chests/nether_bridge");
	private static final Identifier PILLAGER_OUTPOST = getIdentifier("chests/pillager_outpost");
	private static final Identifier SHIPWRECK_TREASURE = getIdentifier("chests/shipwreck_treasure");
	private static final Identifier SIMPLE_DUNGEON = getIdentifier("chests/simple_dungeon");
	
	private static final Identifier STRONGHOLD_ALTAR = getIdentifier("chests/stronghold_corridor");
	
//	private static final Identifier WOODLAND_MANSION = getIdentifier("chests/woodland_mansion");
//	private static final Identifier ANCIENT_CITY = getIdentifier("chests/ancient_city");
	
	private static final Identifier BASTION_GENERIC = getIdentifier("chests/bastion_other");
	private static final Identifier BASTION_BRIDGE = getIdentifier("chests/bastion_bridge");
	private static final Identifier BASTION_STABLE = getIdentifier("chests/bastion_hoglin_stable");
	private static final Identifier BASTION_TREASURE = getIdentifier("chests/bastion_treasure");
	
	private static final Identifier END_CITY_TREASURE = getIdentifier("chests/end_city_treasure");
	
	private static final Identifier FISHING = getIdentifier("gameplay/fishing");
	
//	private static final Identifier TRAIL_RUIN_RARE = getIdentifier("archaeology/trail_ruins_rare");
	
	private static final Identifier TRIAL_CHAMBER_VAULT_OMINOUS_RARE_POOL = getIdentifier("chests/trial_chambers/reward_ominous_rare");
//	private static final Identifier TRIAL_CHAMBER_VAULT_OMINOUS_UNIQUE_POOL = getIdentifier("chests/trial_chambers/reward_ominous_unique");
//	private static final Identifier TRIAL_CHAMBER_VAULT_UNIQUE_POOL = getIdentifier("chests/trial_chambers/reward_unique");
//
//	private static final Identifier TRIAL_CHAMBER_POT = getIdentifier("pots/trial_chambers/corridor");
//
//	private static final Identifier VILLAGE_WEAPONSMITH = getIdentifier("chests/village/village_weaponsmith");
//	private static final Identifier VILLAGE_SAVANNA_HOUSE = getIdentifier("chests/village/weaponsmith");
//	private static final Identifier VILLAGE_TANNER = getIdentifier("chests/village/weaponsmith");

	private static final Identifier[] COMMON_LOOT_TABLES = new Identifier[] {
		ABANDONED_MINESHAFT,
		BURIED_TREASURE,
		DESERT_PYRAMID,
		IGLOO,
		JUNGLE_TEMPLE,
		NETHER_BRIDGE,
		PILLAGER_OUTPOST,
		SHIPWRECK_TREASURE,
		SIMPLE_DUNGEON,
		STRONGHOLD_ALTAR
	};
	
	private static Identifier getIdentifier (String path) {
		return Identifier.fromNamespaceAndPath("minecraft", path);
	}
	
	public static void ModifyLootTables () {
		
		//Make most loot containers provide easy early game access to the basics
		LootTableModifier.AddModifier((tableBuilder, source) -> {
			tableBuilder.pool(
				LootPool.lootPool().setRolls(ConstantValue.exactly(1))
					.add(lootTableItem(STAT_CLOCK).setWeight(4))
					.add(lootTableItem(STAT_CLOCK_FILTER).setWeight(4))
					.add(lootTableItem(STAT_CLOCK_PART).setWeight(12))
					.add(lootTableItem(STAT_CLOCK_REMOVER).setWeight(1))
					.add(
						lootTableItem(NETHERITE_COUNTER).setWeight(24).apply(
							SetItemCountFunction.setCount(UniformGenerator.between(3, 8), false)
						)
					)
					.build()
				);
			},
			COMMON_LOOT_TABLES
		);


		LootTableModifier.AddModifier((tableBuilder, source) -> {
				tableBuilder.apply(
					new SetStatClockFunction.Builder().when(LootItemRandomChanceCondition.randomChance(0.5f))
				);
			}
		);

		//Add extra loot to the bastion treasure, fitting with the extra netherite there
		LootTableModifier.AddModifier((tableBuilder, source) -> {
			tableBuilder.pool(
				LootPool.lootPool().setRolls(UniformGenerator.between(1, 5))
					.add(lootTableItem(STAT_CLOCK).setWeight(8))
					.add(lootTableItem(STAT_CLOCK_FILTER).setWeight(4))
					.add(lootTableItem(STAT_CLOCK_PART).setWeight(12))
					.add(
						lootTableItem(NETHERITE_COUNTER).setWeight(24).apply(
							SetItemCountFunction.setCount(UniformGenerator.between(1, 5))
						)
					)
					.build()
				);
			},
			BASTION_TREASURE
		);
		
		//Add some extra loot to the other bastion chests
		LootTableModifier.AddModifier((tableBuilder, source) -> {
			tableBuilder.pool(
				LootPool.lootPool().setRolls(UniformGenerator.between(0, 2))
					.add(lootTableItem(STAT_CLOCK).setWeight(8))
					.add(lootTableItem(STAT_CLOCK_FILTER).setWeight(4))
					.add(lootTableItem(STAT_CLOCK_PART).setWeight(12))
					.add(
						(LootPoolEntryContainer.Builder<?>)
							lootTableItem(NETHERITE_COUNTER).setWeight(24).apply(
								SetItemCountFunction.setCount(UniformGenerator.between(1, 5))
							)
					)
					.build()
				);
			},
			BASTION_BRIDGE,
			BASTION_GENERIC,
			BASTION_STABLE
		);
		
		//Add elytra specific parts to the end city
		LootTableModifier.AddModifier((tableBuilder, source) -> {
			tableBuilder.pool(
				LootPool.lootPool().setRolls(ConstantValue.exactly(1))
					.add(lootTableItem(STAT_CLOCK).setWeight(4))
					.add(lootTableItem(STAT_CLOCK_FILTER).setWeight(4))
					.add(lootTableItem(STAT_CLOCK_PART).setWeight(12))
					.add(lootTableItem(STAT_CLOCK_REMOVER).setWeight(1))
					.add(
						(LootPoolEntryContainer.Builder<?>)
							lootTableItem(NETHERITE_COUNTER).setWeight(24).apply(
								SetItemCountFunction.setCount(UniformGenerator.between(3, 8), false)
							)
					)
					.add(dataBuilder(STAT_CLOCK_PART, PART_TYPE_COMPONENT, StatClockPartTypes.ROCKET_USAGE).setWeight(24))
					.build()
				);
			},
			END_CITY_TREASURE
		);
		
		//Mace specific in vaults
		LootTableModifier.AddModifier((tableBuilder, source) -> {
			tableBuilder.pool(
				LootPool.lootPool()
					.add(lootTableItem(STAT_CLOCK).setWeight(4))
					.add(lootTableItem(STAT_CLOCK_FILTER).setWeight(4))
					.add(lootTableItem(STAT_CLOCK_PART).setWeight(12))
					.add(lootTableItem(STAT_CLOCK_REMOVER).setWeight(1))
					.add(
						(LootPoolEntryContainer.Builder<?>)
							lootTableItem(NETHERITE_COUNTER).setWeight(24).apply(
								SetItemCountFunction.setCount(UniformGenerator.between(3, 8), false)
							)
					)
					.add(dataBuilder(STAT_CLOCK_PART, PART_TYPE_COMPONENT, StatClockPartTypes.FALL_ATTACK_DISTANCE).setWeight(1))
					.build()
				);
			},
			TRIAL_CHAMBER_VAULT_OMINOUS_RARE_POOL
		);
		
		//Add our loot to the table
		LootTableModifier.AddModifier((tableBuilder, source) -> {
			tableBuilder.modifyPools((builder -> {
				builder.add(
					NestedLootTable.inlineLootTable(
						LootTable.lootTable().pool(
							LootPool.lootPool().setRolls(ConstantValue.exactly(1))
								.add(dataBuilder(STAT_CLOCK_PART, PART_TYPE_COMPONENT, StatClockPartTypes.ITEMS_FISHED).setWeight(1))
								.add(dataBuilder(STAT_CLOCK_PART, PART_TYPE_COMPONENT, StatClockPartTypes.ITEMS_FISHED).setWeight(1)).build()
						).build()
					).setWeight(5)//As likely as treasure, unaffected by lure, luke of the sea or open water
				);
			}));
			},
			FISHING
		);
		
	}
	
	
}
