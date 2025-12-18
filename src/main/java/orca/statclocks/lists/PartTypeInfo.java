package orca.statclocks.lists;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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


public class PartTypeInfo {
	private final String name;
	private final String modID;
	
	boolean visible = true;
	boolean noListener = false;
	
	ArrayList<Item> crafting = new ArrayList<>();
	ArrayList<TagKey<Item>> craftingTag = new ArrayList<>();
	
	PartValueFormat format = PartValueFormat.NONE;
	
	String english;
	String englishFilter;
	
	StatClockFilterType filterType = StatClockFilterType.NONE;
	
	TagKey<Item> itemTag;
	TagKey<Block> blockTag;
	TagKey<EntityType<?>> entityTag;
	
	ArrayList<Pair<ListenerAdapter, ListenerAdapter.Filter>> adapters = new ArrayList<>();
	
	
	public PartTypeInfo (String name) {
		this.name = name;
		this.modID = StatClocksMod.MOD_ID;
	}
	
	public PartTypeInfo (String name, String modID) {
		this.name = name;
		this.modID = modID;
	}
	
	public PartTypeInfo english (String english) {
		this.english = english;
		return this;
	}
	
	public PartTypeInfo setFilterTypeItem (String englishFilter) {
		this.englishFilter = englishFilter;
		filterType = StatClockFilterType.ITEM;
		return this;
	}
	
	public PartTypeInfo setFilterTypeItem (String englishFilter, TagKey<Item> itemTag) {
		this.englishFilter = englishFilter;
		filterType = StatClockFilterType.ITEM;
		
		this.itemTag = itemTag;
		
		return this;
	}
	
	public PartTypeInfo setFilterTypeBlock (String englishFilter) {
		this.englishFilter = englishFilter;
		filterType = StatClockFilterType.BLOCK;
		return this;
	}
	
	public PartTypeInfo setFilterTypeBlock (String englishFilter, TagKey<Block> blockTag) {
		this.englishFilter = englishFilter;
		filterType = StatClockFilterType.ITEM;
		
		this.blockTag = blockTag;
		
		return this;
	}
	
	public PartTypeInfo setFilterTypeEntity (String englishFilter) {
		this.englishFilter = englishFilter;
		filterType = StatClockFilterType.ENTITY;
		return this;
	}
	
	public PartTypeInfo setFilterTypeEntity (String englishFilter, TagKey<EntityType<?>> entityTag) {
		this.englishFilter = englishFilter;
		filterType = StatClockFilterType.ENTITY;
		
		this.entityTag = entityTag;
		
		return this;
	}
	
	public PartTypeInfo setFormat (PartValueFormat format) {
		this.format = format;
		return this;
	}
	
	public PartTypeInfo addCraftingResource (TagKey<Item> itemTag) {
		craftingTag.add(itemTag);
		return this;
	}
	
	public PartTypeInfo addCraftingResource (Item item) {
		crafting.add(item);
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
	
	public StatClockPartType close () {
		Identifier id = Identifier.fromNamespaceAndPath(modID, name);
		
		StatClockPartType type = new StatClockPartType(id);
		
		Registry.register(StatClocksMod.PART_TYPE_REGISTRY, id, type);
		
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
	
	
	public String getEnglish () {
		return english;
	}
	
	public String getEnglishFilter () {
		return englishFilter;
	}
	
	public Component getFormatted (int value) {
		return format.format(value);
	}
	
	public boolean isCraftable () {
		return !crafting.isEmpty() || !craftingTag.isEmpty();
	}
	
	public void makeCraftingRecipe (RecipeProvider recipeProvider, ItemStack outputItem, RecipeOutput output) {
		
		for (Item item : crafting) {
			//TODO make shaped recipes with item stack outputs possible
//			recipeProvider.shapeless(RecipeCategory.MISC, outputItem)
//				.requires(StatClocksMod.NETHERITE_COUNTER)
//				.requires(Items.GOLD_INGOT, 3)
//				.requires(item)
//				.group("craft_stat_clock_part")
//				.unlockedBy("has_resource_for_" + id, recipeProvider.has(item))
//				.save(output, "craft_stat_clock_part_" + id + "_" + RecipeProvider.getItemName(item));
			
			//Lets you change the part type
			recipeProvider.shapeless(RecipeCategory.MISC, outputItem)
				.requires(StatClocksMod.STAT_CLOCK_PART)
				.requires(item)
				.group("craft_stat_clock_part_alt")
				.unlockedBy("has_resource_for_" + name, recipeProvider.has(item))
				.save(output, "craft_stat_clock_part_alt_" + name + "_" + RecipeProvider.getItemName(item));
		}
		
		for (TagKey<Item> tag : craftingTag) {
			//TODO make shaped recipes with item stack outputs possible
//			recipeProvider.shapeless(RecipeCategory.MISC, outputItem)
//				.requires(StatClocksMod.NETHERITE_COUNTER)
//				.requires(Items.GOLD_INGOT, 3)
//				.requires(tag)
//				.group("craft_stat_clock_part")
//				.unlockedBy("has_resource_for_" + id, recipeProvider.has(tag))
//				.save(output, "craft_stat_clock_part_" + id + "_" + tag.getTranslationKey());
			
			//Lets you change the part type
			recipeProvider.shapeless(RecipeCategory.MISC, outputItem)
				.requires(StatClocksMod.STAT_CLOCK_PART)
				.requires(tag)
				.group("craft_stat_clock_part_alt")
				.unlockedBy("has_resource_for_" + name, recipeProvider.has(tag))
				.save(output, "craft_stat_clock_part_alt_" + name + "_" + tag.getTranslationKey());
		}
		
		
	}
	
	public boolean allowFilter (StatClockFilterContent filterContent) {
		StatClocksMod.LOGGER.info("Filter type: {}", filterType);
		
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
	public MutableComponent applyFormatting (MutableComponent toolTip, ItemStack item, StatClockContent statClock, StatClockPartContent statClockPartContent) {
		
		Color color;
		
		if (statClockPartContent.getType().getFilter().getType() != StatClockFilterType.NONE) {
			color = FILTERED_COLOR;
		} else {
			
			StatClockPartMapper.MapRule rule = StatClockPartMapper.GetItemMapping(item.getItem());
			
			if (rule.getDefaultParts().contains(statClockPartContent.getType())) {
				color = DEFAULT_COLOR;
			} else if (rule.getAllowedParts().contains(statClockPartContent.getType())) {
				color = ALLOWED_COLOR;
			} else {
				color = ERROR_COLOR;
			}
		}
		
		toolTip.withColor(color.getRGB());
		
		return toolTip;
	}
}
