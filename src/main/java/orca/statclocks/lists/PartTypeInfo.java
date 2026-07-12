package orca.statclocks.lists;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.*;
import orca.statclocks.listeners.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Function;


public class PartTypeInfo {
	private final String name;
	private final String modID;
	
	boolean visible = true;
	boolean noListener = false;
	
	PartValueFormat format = PartValueFormat.NONE;
	
	StatClockFilterType filterType = StatClockFilterType.NONE;
	
	TagKey<Item> itemTag;
	TagKey<Block> blockTag;
	TagKey<EntityType<?>> entityTag;
	
	final ArrayList<Pair<ListenerAdapter, ListenerAdapter.Filter>> adapters = new ArrayList<>();
	
	public TagKey<Item> defaultItemsTag;
	public TagKey<Item> allowedItemsTag;
	
	public PartTypeInfo (String name) {
		this.name = name;
		this.modID = StatClocksMod.MOD_ID;
	}
	
	public PartTypeInfo (String name, String modID) {
		this.name = name;
		this.modID = modID;
	}
	
	public String getName () {
		return modID + ":" + name;
	}
	
	public PartTypeInfo setFilterTypeItem () {
		filterType = StatClockFilterType.ITEM;
		return this;
	}
	
	public PartTypeInfo setFilterTypeItem (TagKey<Item> itemTag) {
		filterType = StatClockFilterType.ITEM;
		
		this.itemTag = itemTag;
		
		return this;
	}
	
	public PartTypeInfo setFilterTypeBlock () {
		filterType = StatClockFilterType.BLOCK;
		return this;
	}
	
	public PartTypeInfo setFilterTypeBlock (TagKey<Block> blockTag) {
		filterType = StatClockFilterType.ITEM;
		
		this.blockTag = blockTag;
		
		return this;
	}
	
	public PartTypeInfo setFilterTypeEntity () {
		filterType = StatClockFilterType.ENTITY;
		return this;
	}
	
	public PartTypeInfo setFilterTypeEntity (TagKey<EntityType<?>> entityTag) {
		filterType = StatClockFilterType.ENTITY;
		
		this.entityTag = entityTag;
		
		return this;
	}
	
	public PartTypeInfo setFormat (PartValueFormat format) {
		this.format = format;
		return this;
	}
	
	public PartTypeInfo addListener (ListenerAdapter adapter) {
		adapters.add(new Pair<>(adapter, ListenerAdapter.NO_FILTER));
		return this;
	}
	
	public PartTypeInfo addListener (ListenerAdapter adapter, ListenerAdapter.Filter filter) {
		adapters.add(new Pair<>(adapter, filter));
		return this;
	}
	
	public PartTypeInfo markNoListener () {
		noListener = true;
		return this;
	}
	
	public PartTypeInfo hide () {
		visible = false;
		return this;
	}
	
	public Function<ItemStack, Integer> incrementFunction;
	
	public PartTypeInfo setIncrementByDurabilityFunction (Function<ItemStack, Integer> function) {
		incrementFunction = function;
		return this;
	}
	
	public StatClockPartType close () {
		Identifier id = Identifier.fromNamespaceAndPath(modID, name);
		
		StatClockPartType type = new StatClockPartType(id);
		
		Registry.register(StatClocksMod.PART_TYPE_REGISTRY, id, type);
		
		defaultItemsTag = StatClocksMod.itemTag(name + "_defaults_on");
		allowedItemsTag = StatClocksMod.itemTag(name + "_allowed_on");
		
		if (adapters.isEmpty() && !noListener) {
			StatClocksMod.LOGGER.warn("Part type: {} has no listeners", name);
		}
		
		for (Pair<ListenerAdapter, ListenerAdapter.Filter> adapter : adapters) {
			adapter.getA().addPartType(type, adapter.getB());
		}
		
		Pair<StatClockPartType, PartTypeInfo> pair = new Pair<>(type, this);
		
		assert !StatClockPartTypes.STAT_PART_TYPES.containsKey(id);
		
		StatClockPartTypes.STAT_PART_TYPES.put(id, pair);
		
		return type;
	}
	
	public Component getFormatted (int value) {
		return format.format(value);
	}
	
	public int incrementByDurability (ItemStack stack) {
		if (incrementFunction == null) return 0;
		
		return incrementFunction.apply(stack);
		
	}
	
	public boolean allowFilter (StatClockFilterContent filterContent) {
		if (filterContent.getType() == StatClockFilterType.NONE) return false;
		
		if (filterType == StatClockFilterType.NONE) return false;
		
		if (filterType != filterContent.getType()) return false;
		
		return switch (filterType) {
			case ITEM -> itemAllowed(filterContent.getItem());
			case BLOCK -> blockAllowed(filterContent.getBlock());
			case ENTITY -> entityAllowed(filterContent.getEntity());
			default -> false;
		};
	}
	
	private boolean itemAllowed (Item item) {
		if (itemTag == null) return true;
		
		return BuiltInRegistries.ITEM.wrapAsHolder(item).is(itemTag);
	}
	
	private boolean blockAllowed (Block block) {
		if (blockTag == null) return true;
		
		return BuiltInRegistries.BLOCK.wrapAsHolder(block).is(blockTag);
	}
	
	private boolean entityAllowed (EntityType<?> entity) {
		if (entityTag == null) return true;
		
		return BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity).is(entityTag);
	}
	
	public boolean isFilterable () {
		return !(filterType == StatClockFilterType.NONE);
	}
	
	public boolean showInCreativeInventory () {
		return visible;
	}
	
	public StatClockFilterType getFilterType () {
		return filterType;
	}
	
	
	public static final Color DEFAULT_COLOR = Color.white;
	public static final Color ALLOWED_COLOR = Color.gray;
	public static final Color FILTERED_COLOR = Color.green;
	public static final Color ERROR_COLOR = Color.red;
	
	@NotNull
	public Color getToolTipColor (ItemStack item, StatClockPartContent statClockPartContent) {
		Color color;
		
		if (statClockPartContent.getType().getFilter().getType() != StatClockFilterType.NONE) {
			color = FILTERED_COLOR;
		} else {
			
			if (item.is(statClockPartContent.getType().getInfo().defaultItemsTag)) {
				color = DEFAULT_COLOR;
			} else if (item.is(statClockPartContent.getType().getInfo().allowedItemsTag)) {
				color = ALLOWED_COLOR;
			} else {
				color = ERROR_COLOR;
			}
		}
		
		return color;
	}
}
