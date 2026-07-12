package orca.statclocks.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockPartType;

import java.util.concurrent.CompletableFuture;

import static orca.statclocks.lists.StatClockPartTypes.*;

public class StatClockItemTagProvider extends FabricTagProvider.ItemTagProvider {
	
	public StatClockItemTagProvider (FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void addTags (HolderLookup.Provider wrapperLookup) {
		
		TagAppender<Item, Item> statClockItems = valueLookupBuilder(StatClocksMod.STAT_CLOCKS_ITEMS);
		
		statClockItems.add(StatClocksMod.STAT_CLOCK);
		statClockItems.add(StatClocksMod.STAT_CLOCK_PART);
		statClockItems.add(StatClocksMod.STAT_CLOCK_FILTER);
		statClockItems.add(StatClocksMod.STAT_CLOCK_REMOVER);
		
		addPartTypeTags();
		
		valueLookupBuilder(StatClocksMod.SHEARS).add(Items.SHEARS);
		
		valueLookupBuilder(StatClocksMod.MUSIC_DISC).add(
			Items.MUSIC_DISC_13, Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_BLOCKS,
			Items.MUSIC_DISC_CHIRP, Items.MUSIC_DISC_FAR, Items.MUSIC_DISC_MALL,
			Items.MUSIC_DISC_MELLOHI, Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_STRAD,
			Items.MUSIC_DISC_WARD, Items.MUSIC_DISC_11, Items.MUSIC_DISC_CREATOR_MUSIC_BOX,
			Items.MUSIC_DISC_WAIT, Items.MUSIC_DISC_CREATOR, Items.MUSIC_DISC_PRECIPICE,
			Items.MUSIC_DISC_OTHERSIDE, Items.MUSIC_DISC_RELIC, Items.MUSIC_DISC_5,
			Items.MUSIC_DISC_PIGSTEP, Items.MUSIC_DISC_TEARS, Items.MUSIC_DISC_LAVA_CHICKEN
		);
		
		valueLookupBuilder(StatClocksMod.CONSUMABLE).add(
			Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE,
			Items.CARROT, Items.GOLDEN_CARROT,
			Items.POTATO, Items.BAKED_POTATO,
			Items.BEETROOT, Items.BEETROOT_SOUP,
			Items.BREAD, Items.COOKIE,
			Items.MUSHROOM_STEW, Items.SUSPICIOUS_STEW,
			
			Items.BEEF, Items.COOKED_BEEF,
			Items.CHICKEN, Items.COOKED_CHICKEN,
			Items.MUTTON, Items.COOKED_MUTTON,
			Items.PORKCHOP, Items.COOKED_PORKCHOP,
			Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_STEW,
			
			Items.COD, Items.COOKED_COD,
			Items.SALMON, Items.COOKED_SALMON,
			Items.PUFFERFISH, Items.TROPICAL_FISH,
			
			Items.GLOW_BERRIES, Items.SWEET_BERRIES,
			Items.PUMPKIN_PIE, Items.MELON_SLICE,
			Items.DRIED_KELP,
			
			Items.CHORUS_FRUIT,
			
			Items.ROTTEN_FLESH, Items.SPIDER_EYE,
			
			//Special case
			Items.CAKE,
			
			//Drinks
			Items.MILK_BUCKET, Items.HONEY_BOTTLE,
			Items.POTION, Items.OMINOUS_BOTTLE
		);
		
		valueLookupBuilder(StatClocksMod.HORSE_ARMOR).add(
			Items.LEATHER_HORSE_ARMOR, Items.COPPER_HORSE_ARMOR, Items.IRON_HORSE_ARMOR,
			Items.GOLDEN_HORSE_ARMOR, Items.DIAMOND_HORSE_ARMOR, Items.NETHERITE_HORSE_ARMOR
		);
		
		valueLookupBuilder(StatClocksMod.NAUTILUS_ARMOR).add(
			Items.COPPER_NAUTILUS_ARMOR, Items.IRON_NAUTILUS_ARMOR, Items.GOLDEN_NAUTILUS_ARMOR,
			Items.DIAMOND_NAUTILUS_ARMOR, Items.NETHERITE_NAUTILUS_ARMOR
		);
		
		valueLookupBuilder(StatClocksMod.BOAT).add(
			Items.OAK_BOAT,			Items.OAK_BOAT,
			Items.SPRUCE_BOAT,		Items.SPRUCE_BOAT,
			Items.BIRCH_BOAT,		Items.BIRCH_BOAT,
			Items.JUNGLE_BOAT,		Items.JUNGLE_CHEST_BOAT,
			Items.ACACIA_BOAT,		Items.ACACIA_CHEST_BOAT,
			Items.DARK_OAK_BOAT,	Items.DARK_OAK_CHEST_BOAT,
			Items.MANGROVE_BOAT,	Items.MANGROVE_CHEST_BOAT,
			Items.CHERRY_BOAT,		Items.CHERRY_CHEST_BOAT,
			Items.PALE_OAK_BOAT,	Items.PALE_OAK_CHEST_BOAT,
			Items.BAMBOO_RAFT,		Items.BAMBOO_CHEST_RAFT
		);
		
		valueLookupBuilder(StatClocksMod.MINECART).add(
			Items.MINECART, Items.CHEST_MINECART, Items.HOPPER_MINECART,
			Items.TNT_MINECART, Items.FURNACE_MINECART, Items.COMMAND_BLOCK_MINECART
		);
		
		addShearableItems();
		
		addFishingTags();
		
		addBrushableTags();
	}
	
	private void addPartTypeTags () {
		
		buildPartTags(BLOCKS_MINED)
			.addDefaultItemTags(ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.HOES)
			.addAllowedItemTags(ItemTags.SWORDS, StatClocksMod.SHEARS);
		
		buildPartTags(BLOCK_LOOT_DROPPED)
			.addAllowedItemTags(ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.HOES, ItemTags.SWORDS, StatClocksMod.SHEARS);
		
		buildPartTags(TIMES_USED)
			.addDefaultItems(Items.CARROT_ON_A_STICK, Items.WARPED_FUNGUS_ON_A_STICK, Items.GOAT_HORN, Items.SPYGLASS)
			.addDefaultItemTags(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.HOES);
		
		buildPartTags(BLOCKS_IGNITED).addDefaultItems(Items.FLINT_AND_STEEL);
		buildPartTags(MOBS_IGNITED).addDefaultItems(Items.FLINT_AND_STEEL);
		
		buildPartTags(SHEARS_USED).addDefaultItemTags(StatClocksMod.SHEARS);
		buildPartTags(SHEARS_USED_ITEM).addDefaultItemTags(StatClocksMod.SHEARS);
		buildPartTags(SHEARS_USED_BLOCK).addDefaultItemTags(StatClocksMod.SHEARS);
		buildPartTags(SHEARS_USED_ENTITY).addDefaultItemTags(StatClocksMod.SHEARS);
		
		buildPartTags(BRUSH_USED).addDefaultItems(Items.BRUSH);
		
		buildPartTags(DAMAGE_DEALT)
			.addDefaultItemTags(ItemTags.SWORDS, ItemTags.AXES, ItemTags.SPEARS)
			.addDefaultItems(Items.MACE, Items.TRIDENT, Items.BOW, Items.CROSSBOW)
			.addAllowedItemTags(ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.HOES);
		
		buildPartTags(MOBS_KILLED)
			.addDefaultItemTags(ItemTags.SWORDS, ItemTags.AXES, ItemTags.SPEARS)
			.addDefaultItems(Items.MACE, Items.TRIDENT, Items.BOW, Items.CROSSBOW)
			.addAllowedItemTags(ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.HOES);
		
		buildPartTags(MOB_LOOT_DROPPED)
			.addAllowedItemTags(ItemTags.SWORDS, ItemTags.AXES, ItemTags.SPEARS, ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.HOES)
			.addAllowedItems(Items.MACE, Items.TRIDENT, Items.BOW, Items.CROSSBOW);
		
		buildPartTags(FALL_ATTACK_DISTANCE)
			.addDefaultItems(Items.MACE);
		
		buildPartTags(DAMAGE_DEALT_DISTANCE)
			.addAllowedItems(Items.BOW, Items.CROSSBOW, Items.TRIDENT);
		
		buildPartTags(ITEMS_FISHED).addDefaultItems(Items.FISHING_ROD);
		buildPartTags(FISH_CAUGHT).addDefaultItems(Items.FISHING_ROD);
		buildPartTags(TREASURE_CAUGHT).addDefaultItems(Items.FISHING_ROD);
		buildPartTags(TRASH_CAUGHT).addDefaultItems(Items.FISHING_ROD);
		buildPartTags(MOBS_FISHED).addAllowedItems(Items.FISHING_ROD);
		
		buildPartTags(TIME_PLAYED).addDefaultItemTags(StatClocksMod.MUSIC_DISC);
		buildPartTags(TIMES_FINISHED).addDefaultItemTags(StatClocksMod.MUSIC_DISC);
		
		buildPartTags(DISTANCE_FLOWN).addDefaultItems(Items.ELYTRA);
		buildPartTags(ROCKET_USAGE).addAllowedItems(Items.ELYTRA);
		
		buildPartTags(DISTANCE_FLOATED).addDefaultItemTags(ItemTags.HARNESSES);
		buildPartTags(DISTANCE_RODE).addDefaultItems(Items.SADDLE);
		
		buildPartTags(VEHICLE_DISTANCE).addDefaultItemTags(StatClocksMod.BOAT, StatClocksMod.MINECART);
		
		buildPartTags(DAMAGE_TAKEN)
			.addDefaultItemTags(ItemTags.HEAD_ARMOR, ItemTags.CHEST_ARMOR, ItemTags.LEG_ARMOR, ItemTags.FOOT_ARMOR, StatClocksMod.HORSE_ARMOR, StatClocksMod.NAUTILUS_ARMOR);
		
		buildPartTags(DAMAGE_REDUCED)
			.addDefaultItemTags(ItemTags.HEAD_ARMOR, ItemTags.CHEST_ARMOR, ItemTags.LEG_ARMOR, ItemTags.FOOT_ARMOR, StatClocksMod.HORSE_ARMOR, StatClocksMod.NAUTILUS_ARMOR)
				.addDefaultItems(Items.WOLF_ARMOR, Items.SHIELD);
		
		buildPartTags(ATTACKS_BLOCKED).addDefaultItems(Items.SHIELD);
		
		buildPartTags(TIME_UNDERWATER).addAllowedItemTags(ItemTags.HEAD_ARMOR);
		buildPartTags(MINED_UNDERWATER).addAllowedItemTags(ItemTags.HEAD_ARMOR);
		buildPartTags(ITEMS_CONSUMED).addAllowedItemTags(ItemTags.HEAD_ARMOR);
		
		buildPartTags(DISTANCE_CROUCHED).addAllowedItemTags(ItemTags.LEG_ARMOR);
		buildPartTags(DISTANCE_WALKED).addAllowedItemTags(ItemTags.LEG_ARMOR);
		
		buildPartTags(DISTANCE_SWAM).addAllowedItemTags(ItemTags.FOOT_ARMOR);
		buildPartTags(DISTANCE_FALLEN).addAllowedItemTags(ItemTags.FOOT_ARMOR);
		buildPartTags(DISTANCE_WADED).addAllowedItemTags(ItemTags.FOOT_ARMOR);
		buildPartTags(DISTANCE_WALKED).addAllowedItemTags(ItemTags.FOOT_ARMOR);
		
		buildPartTags(ITEMS_DEPOSITED)
			.addDefaultItemTags(ItemTags.SHULKER_BOXES, ItemTags.BUNDLES)
			.addAllowedItemTags(ItemTags.CHEST_BOATS)
			.addAllowedItems(Items.HOPPER_MINECART, Items.CHEST_MINECART);
		
		STAT_PART_TYPES.forEach((id, pair) ->
			valueLookupBuilder(StatClocksMod.STAT_CLOCKABLE).addOptionalTag(pair.getB().defaultItemsTag).addOptionalTag(pair.getB().allowedItemsTag));
		
	}
	
	private void addShearableItems () {
		TagAppender<Item, Item> shearableItems = valueLookupBuilder(StatClocksMod.SHEARABLE_ITEMS);
		
		//General shearable items
		shearableItems.addTag(StatClocksMod.NAUTILUS_ARMOR);
		shearableItems.addTag(StatClocksMod.HORSE_ARMOR);
		
		shearableItems.add(Items.SADDLE);
		
		shearableItems.add(Items.LEAD);
		
		shearableItems.addOptionalTag(ItemTags.WOOL_CARPETS);
		
		//Shearable blocks
		shearableItems.add(Items.PUMPKIN_SEEDS);
		shearableItems.add(Items.HONEYCOMB);
		
		//Sheep
		shearableItems.addOptionalTag(ItemTags.WOOL);
		
		//Mooshroom and bogged
		shearableItems.add(Items.BROWN_MUSHROOM);
		shearableItems.add(Items.RED_MUSHROOM);
	}
	
	private static final Item[] FISHABLE_FISH = new Item[] {
		Items.COD, Items.SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH
	};
	
	private static final Item[] FISHABLE_TREASURE = new Item[] {
		Items.BOW, Items.ENCHANTED_BOOK, Items.NAME_TAG,
		Items.NAUTILUS_SHELL, Items.SADDLE,
		
		Items.FISHING_ROD //Enchanted
	};
	
	private static final Item[] FISHABLE_TRASH = new Item[] {
		Items.LILY_PAD, Items.BONE, Items.BOWL, Items.LEATHER, Items.LEATHER_BOOTS,
		Items.ROTTEN_FLESH, Items.POTION, Items.TRIPWIRE_HOOK, Items.STICK,
		Items.STRING, Items.INK_SAC,
		
		Items.BAMBOO, //Jungles only
		
		Items.FISHING_ROD //Unenchanted
	};
	
	private void addFishingTags () {
		//NOTE no way to read from loot tables
		TagAppender<Item, Item> fishable = valueLookupBuilder(StatClocksMod.FISHABLE);
		TagAppender<Item, Item> fish = valueLookupBuilder(StatClocksMod.FISHABLE_FISH);
		TagAppender<Item, Item> treasure = valueLookupBuilder(StatClocksMod.FISHABLE_TREASURE);
		TagAppender<Item, Item> trash = valueLookupBuilder(StatClocksMod.FISHABLE_TRASH);
		
		for (Item add : FISHABLE_FISH) {
			fishable.add(add);
			fish.add(add);
		}
		
		for (Item add : FISHABLE_TREASURE) {
			fishable.add(add);
			treasure.add(add);
		}
		
		for (Item add : FISHABLE_TRASH) {
			fishable.add(add);
			trash.add(add);
		}
		
	}
	
	
	private static final Item[] BRUSHABLE_LOOT = new Item[] {
		//Warm ocean
		Items.COAL, Items.EMERALD, Items.WHEAT,
		Items.WOODEN_HOE, Items.GOLD_NUGGET,
		Items.ANGLER_POTTERY_SHERD, Items.SHELTER_POTTERY_SHERD,
		Items.SNORT_POTTERY_SHERD,
		Items.SNIFFER_EGG, Items.IRON_AXE,
		
		//Cold ocean
		//Items.COAL, Items.EMERALD, Items.WHEAT,
		//Items.WOODEN_HOE, Items.GOLD_NUGGET,
		Items.BLADE_POTTERY_SHERD, Items.EXPLORER_POTTERY_SHERD,
		Items.MOURNER_POTTERY_SHERD, Items.PLENTY_POTTERY_SHERD,
		//Items.IRON_AXE,
		
		
		//Desert temple
		Items.ARCHER_POTTERY_SHERD, Items.MINER_POTTERY_SHERD,
		Items.PRIZE_POTTERY_SHERD, Items.SKULL_POTTERY_SHERD,
		Items.TNT, Items.DIAMOND, //Items.EMERALD
		
		//Desert well
		Items.ARMS_UP_POTTERY_SHERD, Items.BREWER_POTTERY_SHERD,
		Items.BRICK, Items.STICK, Items.SUSPICIOUS_STEW, //Items.EMERALD
		
		//Trail ruins common
		//Items.BRICK, Items.WHEAT, Items.EMERALD, Items.GOLD_NUGGET, Items.WOODEN_HOE
		Items.BLUE_DYE, Items.LIGHT_BLUE_DYE, Items.ORANGE_DYE, Items.WHITE_DYE, Items.YELLOW_DYE,
		Items.BROWN_CANDLE, Items.GREEN_CANDLE, Items.PURPLE_CANDLE, Items.RED_CANDLE,
		Items.BLUE_STAINED_GLASS_PANE, Items.LIGHT_BLUE_STAINED_GLASS_PANE,
		Items.MAGENTA_STAINED_GLASS_PANE, Items.PINK_STAINED_GLASS_PANE,
		Items.PURPLE_STAINED_GLASS_PANE, Items.RED_STAINED_GLASS_PANE,
		Items.YELLOW_STAINED_GLASS_PANE,
		Items.OAK_HANGING_SIGN, Items.SPRUCE_HANGING_SIGN,
		Items.CLAY_BALL, Items.BEETROOT_SEEDS, Items.DEAD_BUSH,
		Items.FLOWER_POT, Items.LEAD, Items.STRING,
		Items.WHEAT_SEEDS,
		
		//Trail ruins rare
		Items.BURN_POTTERY_SHERD,
		Items.DANGER_POTTERY_SHERD,
		Items.FRIEND_POTTERY_SHERD,
		Items.HEART_POTTERY_SHERD,
		Items.HEARTBREAK_POTTERY_SHERD,
		Items.HOWL_POTTERY_SHERD,
		Items.SHEAF_POTTERY_SHERD,
		
		Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
		Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
		Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,
		Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE,
		
		Items.MUSIC_DISC_RELIC
		
	};
	
	private void addBrushableTags () {
		//NOTE no way to read from loot tables
		TagAppender<Item, Item> brushable = valueLookupBuilder(StatClocksMod.BRUSHABLE_ITEMS);
		
		brushable.add(Items.ARMADILLO_SCUTE);
		
		for (Item add : BRUSHABLE_LOOT) {
			brushable.add(add);
		}
	}
	
	
	private Builder buildPartTags (StatClockPartType type) {
		return new Builder(valueLookupBuilder(type.getInfo().defaultItemsTag), valueLookupBuilder(type.getInfo().allowedItemsTag));
	}
	
	private record Builder (TagAppender<Item, Item> defaultAppender, TagAppender<Item, Item> allowedAppender) {
		
		private Builder addDefaultItems (Item... items) {
			defaultAppender.add(items);
			return this;
		}
		
		@SafeVarargs
		private Builder addDefaultItemTags (TagKey<Item>... tags) {
			for (TagKey<Item> tag : tags) {
				defaultAppender.addOptionalTag(tag);
			}
			return this;
		}
		
		private Builder addAllowedItems (Item... items) {
			allowedAppender.add(items);
			return this;
		}
		
		@SafeVarargs
		private Builder addAllowedItemTags (TagKey<Item>... tags) {
			for (TagKey<Item> tag : tags) {
				allowedAppender.addOptionalTag(tag);
			}
			return this;
		}
	}
}
