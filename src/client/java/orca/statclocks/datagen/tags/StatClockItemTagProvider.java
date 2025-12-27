package orca.statclocks.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import orca.statclocks.StatClocksMod;

import java.util.concurrent.CompletableFuture;

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
		
		
		addUsableItems();
		
		addConsumable();
		
		addMobArmor();
		
		addShearableItems();
		
		addFishingTags();
		
		addBrushableTags();
	}
	
	private static final Item[] MISC_USABLE_ITEMS = new Item[] {
		Items.CARROT_ON_A_STICK, Items.WARPED_FUNGUS_ON_A_STICK,
		Items.GOAT_HORN, Items.SPYGLASS
		//Brush has its own handling
	};
	
	private void addUsableItems () {
		
		TagAppender<Item, Item> usable = valueLookupBuilder(StatClocksMod.USABLE_ITEM);
		
		usable.addOptionalTag(ItemTags.AXES);
		usable.addOptionalTag(ItemTags.SHOVELS);
		usable.addOptionalTag(ItemTags.HOES);
		
		for (Item item : MISC_USABLE_ITEMS) {
			usable.add(item);
		}
		
	}
	
	private static final Item[] CONSUMABLE = new Item[] {
		
		//Foods
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
	};
	
	private void addConsumable () {
		
		TagAppender<Item, Item> usable = valueLookupBuilder(StatClocksMod.CONSUMABLE);
		
		for (Item item : CONSUMABLE) {
			usable.add(item);
		}
	}
	
	
	private static final Item[] HORSE_ARMOR = new Item[] {
		Items.LEATHER_HORSE_ARMOR, Items.COPPER_HORSE_ARMOR, Items.IRON_HORSE_ARMOR,
		Items.GOLDEN_HORSE_ARMOR, Items.DIAMOND_HORSE_ARMOR, Items.NETHERITE_HORSE_ARMOR
	};
	
	private static final Item[] NAUTILUS_ARMOR = new Item[] {
		Items.COPPER_NAUTILUS_ARMOR, Items.IRON_NAUTILUS_ARMOR, Items.GOLDEN_NAUTILUS_ARMOR,
		Items.DIAMOND_NAUTILUS_ARMOR, Items.NETHERITE_NAUTILUS_ARMOR
	};
	
	private void addMobArmor () {
		
		TagAppender<Item, Item> horseArmor = valueLookupBuilder(StatClocksMod.HORSE_ARMOR);
		
		for (Item item : HORSE_ARMOR) {
			horseArmor.add(item);
		}
		
		TagAppender<Item, Item> nautilusArmor = valueLookupBuilder(StatClocksMod.NAUTILUS_ARMOR);
		
		for (Item item : NAUTILUS_ARMOR) {
			nautilusArmor.add(item);
		}
		
	}
	
	private void addShearableItems () {
		TagAppender<Item, Item> shearableItems = valueLookupBuilder(StatClocksMod.SHEARABLE_ITEMS);
		
		shearableItems.addTag(StatClocksMod.NAUTILUS_ARMOR);
		shearableItems.addTag(StatClocksMod.HORSE_ARMOR);
		
		shearableItems.add(Items.SADDLE);
		
		shearableItems.add(Items.LEAD);
		
		shearableItems.add(Items.PUMPKIN_SEEDS);
		
		shearableItems.addOptionalTag(ItemTags.WOOL);
		
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
}
