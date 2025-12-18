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
		
		addFishingTags();
		
		
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
	
	private static final Item[] MISC_USABLE_ITEMS = new Item[] {
		Items.SHEARS, Items.FLINT_AND_STEEL, Items.BRUSH,
		Items.CARROT_ON_A_STICK, Items.WARPED_FUNGUS_ON_A_STICK,
		Items.GOAT_HORN, Items.SPYGLASS
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
		//TODO read from loot tables?
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
}
