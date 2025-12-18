package orca.statclocks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.components.StatClockFilterContent;
import orca.statclocks.components.StatClockPartContent;
import orca.statclocks.components.StatClockPartType;
import orca.statclocks.lists.PartTypeInfo;
import orca.statclocks.lists.StatClockPartTypes;
import orca.statclocks.loottable.CustomLootRegistries;
import orca.statclocks.loottable.DataLootItem;
import orca.statclocks.loottable.LootTableModifications;
import orca.statclocks.loottable.LootTableModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.util.tuples.Pair;

import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class StatClocksMod implements ModInitializer {
	public static final String MOD_ID = "stat-clocks";
	
	//
	//      Add items
	//
	
	public static Item STAT_CLOCK = register("stat_clock", Item::new, new Item.Properties().stacksTo(1));
	public static Item STAT_CLOCK_PART = register("stat_clock_part", Item::new, new Item.Properties().stacksTo(1));
	public static Item STAT_CLOCK_FILTER = register("stat_clock_filter", StatClockFilterItem::new, new Item.Properties().stacksTo(1)
		.component(StatClockFilterContent.FILTER_COMPONENT, StatClockFilterContent.makeEmpty()));
	public static Item NETHERITE_COUNTER = register("netherite_counter", Item::new, new Item.Properties());
	public static Item STAT_CLOCK_REMOVER = register("stat_clock_remover", Item::new, new Item.Properties().stacksTo(1));
	
	public static final ResourceKey<Registry<StatClockPartType>> PART_TYPES =
		ResourceKey.createRegistryKey((Identifier.fromNamespaceAndPath(MOD_ID, "part_types")));
	public static final Registry<StatClockPartType> PART_TYPE_REGISTRY = FabricRegistryBuilder.createSimple(PART_TYPES)
		.attribute(RegistryAttribute.SYNCED)
		.buildAndRegister();
	
	public static Item register (String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
		
		// Create the item instance.
		Item item = itemFactory.apply(settings.setId(itemKey));
		
		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);
		
		return item;
	}
	
	//
	//      Add creative mod tab
	//
	
	public static final ResourceKey<CreativeModeTab> STAT_CLOCK_PART_TAB = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(MOD_ID, "stat_clock_part"));
	public static final CreativeModeTab STAT_CLOCK_PART_GROUP = FabricItemGroup.builder()
		.icon(() -> new ItemStack(STAT_CLOCK_PART))
		.title(Component.translatable("item-group.stat_clock_part_types"))
		.build();
	
	
	//
	//      Custom tags
	//
	
	public static final TagKey<Item> STAT_CLOCKS_ITEMS = itemTag("stat_clock_items");
	
	public static final TagKey<Item> USABLE_ITEM = itemTag("usable_item");
	public static final TagKey<Item> CONSUMABLE = itemTag("consumable");
	
	public static final TagKey<Item> FISHABLE = itemTag("fishable");
	public static final TagKey<Item> FISHABLE_FISH = itemTag("fishable_fish");
	public static final TagKey<Item> FISHABLE_TREASURE = itemTag("fishable_treasure");
	public static final TagKey<Item> FISHABLE_TRASH = itemTag("fishable_trash");
	
	public static final TagKey<Item> HORSE_ARMOR = itemTag("horse_armor");
	public static final TagKey<Item> NAUTILUS_ARMOR = itemTag("nautilus_armor");
	
	public static TagKey<Item> itemTag (String name) {
		return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}
	
	public static TagKey<Block> blockTag (String name) {
		return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}
	
	public static final TagKey<EntityType<?>> IGNITABLE_MOBS = entityTag("ignitable_mobs");
	public static final TagKey<EntityType<?>> SHEARABLE_MOBS = entityTag("shearable_mobs");
	public static final TagKey<EntityType<?>> BRUSHABLE_MOBS = entityTag("brushable_mobs");
	
	public static TagKey<EntityType<?>> entityTag (String name) {
		return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}
	
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's id.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	@Override
	public void onInitialize () {
		
		LootTableModifications.ModifyLootTables();
		
		//Poke
		StatClockContent.init();
		StatClockPartContent.init();
		StatClockPartTypes.init();
		StatClockFilterContent.init();
		CustomLootRegistries.init();
		
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, STAT_CLOCK_PART_TAB, STAT_CLOCK_PART_GROUP);
		
		AddToIngredientsTab(STAT_CLOCK);
		AddToTabWithData(CreativeModeTabs.INGREDIENTS, STAT_CLOCK_PART, StatClockPartType.PART_TYPE_COMPONENT, StatClockPartTypes.EMPTY);
		AddToIngredientsTab(STAT_CLOCK_FILTER);
		AddToIngredientsTab(NETHERITE_COUNTER);
		AddToIngredientsTab(STAT_CLOCK_REMOVER);
		
		
		//Add all part types to list
		Set<Identifier> statPartKeys = StatClockPartTypes.STAT_PART_TYPES.keySet();
		
		for (Identifier id : statPartKeys) {
			Pair<StatClockPartType, PartTypeInfo> pair = StatClockPartTypes.STAT_PART_TYPES.get(id);
			StatClockPartType type = pair.getA();
			PartTypeInfo info = pair.getB();
			
			if (!info.showInCreativeInventory()) continue;
			
			AddToTabWithData(STAT_CLOCK_PART_TAB, STAT_CLOCK_PART, StatClockPartType.PART_TYPE_COMPONENT, type);
			
		}
		
		//Register commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> StatClockCommand.register(dispatcher, registryAccess));
		
		//Loot table setup
		LootTableEvents.MODIFY.register(LootTableModifier::modify);
		
		//TODO grindstone behave weirdly
	}
	
	private void AddToIngredientsTab (Item item) {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
			.register((itemGroup) -> itemGroup.accept(item));
	}
	
	private <T> void AddToTabWithData (ResourceKey<CreativeModeTab> tab, Item item, DataComponentType<T> componentType, T data) {
		ItemStack stack = new ItemStack(item);
		
		stack.set(componentType, data);
		
		ItemGroupEvents.modifyEntriesEvent(tab)
			.register((itemGroup) -> itemGroup.accept(stack));
	}
	
	public static <T> DataComponentType<T> registerComponent (String string, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, string, (unaryOperator.apply(DataComponentType.builder())).build());
	}
	
}