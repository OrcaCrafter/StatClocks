package orca.statclocks.lists;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.Equippable;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockPartType;

import java.util.ArrayList;

public class StatClockPartMapper {
	
	interface RuleMapper {
		void addRules (Item item, MapRule rule);
	}
	
	public static class MapRule {
		private final ArrayList<StatClockPartType> defaultParts;
		private final ArrayList<StatClockPartType> allowedParts;
		
		public MapRule () {
			defaultParts = new ArrayList<>();
			allowedParts = new ArrayList<>();
		}
		
		public ArrayList<StatClockPartType> getDefaultParts () {
			return defaultParts;
		}
		
		public ArrayList<StatClockPartType> getAllowedParts () {
			return allowedParts;
		}
		
		public StatClockPartType[] defaultPartsArray () {
			StatClockPartType[] ret = new StatClockPartType[defaultParts.size()];
			return defaultParts.toArray(ret);
		}
		
		public StatClockPartType[] allowedPartsArray () {
			StatClockPartType[] ret = new StatClockPartType[allowedParts.size()];
			return allowedParts.toArray(ret);
		}
		
		public boolean isEmpty () {
			return defaultParts.isEmpty() && allowedParts.isEmpty();
		}
	}
	
	static final ArrayList<RuleMapper> MAPPERS = new ArrayList<>();
	
	static {
		
		//Block breaking rule
		MAPPERS.add((Item item, MapRule rule) -> {
			if (isInTags(item, ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.HOES)) {
				rule.defaultParts.add(StatClockPartTypes.BLOCKS_MINED);
				rule.allowedParts.add(StatClockPartTypes.BLOCK_LOOT_DROPPED);
			}
			
			if (isInTag(item, ItemTags.SWORDS) || item instanceof ShearsItem) {
				rule.allowedParts.add(StatClockPartTypes.BLOCKS_MINED);
				rule.allowedParts.add(StatClockPartTypes.BLOCK_LOOT_DROPPED);
			}
		});
		
		//Item used rule
		MAPPERS.add((Item item, MapRule rule) -> {
			if (isInTag(item, StatClocksMod.USABLE_ITEM)) {
				rule.defaultParts.add(StatClockPartTypes.TIMES_USED);
			}
			
			if (item instanceof FlintAndSteelItem) {
				rule.defaultParts.add(StatClockPartTypes.BLOCKS_IGNITED);
				rule.defaultParts.add(StatClockPartTypes.MOBS_IGNITED);
			}
			
			if (item instanceof ShearsItem) {
				rule.defaultParts.add(StatClockPartTypes.SHEARS_USED);
				rule.allowedParts.add(StatClockPartTypes.SHEARS_USED_ITEM);
				rule.allowedParts.add(StatClockPartTypes.SHEARS_USED_BLOCK);
				rule.allowedParts.add(StatClockPartTypes.SHEARS_USED_ENTITY);
			}
			
			if (item instanceof BrushItem) {
				rule.defaultParts.add(StatClockPartTypes.BRUSH_USED);
			}
		});
		
		//Damage dealt rule
		MAPPERS.add((Item item, MapRule rule) -> {
			//Regular combat items, and damage dealing tools
			if (isInTags(item, ItemTags.SWORDS, ItemTags.AXES, ItemTags.SPEARS) || item instanceof MaceItem) {
				rule.defaultParts.add(StatClockPartTypes.DAMAGE_DEALT);
				rule.defaultParts.add(StatClockPartTypes.MOBS_KILLED);
				
				rule.allowedParts.add(StatClockPartTypes.MOB_LOOT_DROPPED);
			} else if (isInTags(item, ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.HOES)) {
				rule.allowedParts.add(StatClockPartTypes.DAMAGE_DEALT);
				rule.allowedParts.add(StatClockPartTypes.MOBS_KILLED);
				
				rule.allowedParts.add(StatClockPartTypes.MOB_LOOT_DROPPED);
			}
		});
		
		//Mace rule
		MAPPERS.add((Item item, MapRule rule) -> {
			if (item instanceof MaceItem) {
				rule.defaultParts.add(StatClockPartTypes.FALL_ATTACK_DISTANCE);
			}
		});
		
		//Trident rule
		MAPPERS.add((Item item, MapRule rule) -> {
			if (item instanceof TridentItem) {
				rule.defaultParts.add(StatClockPartTypes.DAMAGE_DEALT);
				rule.defaultParts.add(StatClockPartTypes.MOBS_KILLED);
				
				rule.allowedParts.add(StatClockPartTypes.DAMAGE_DEALT_DISTANCE);
				rule.allowedParts.add(StatClockPartTypes.MOB_LOOT_DROPPED);
			}
		});
		
		//Bows
		MAPPERS.add((Item item, MapRule rule) -> {
			//Regular combat items, and damage dealing tools
			if (item instanceof  BowItem || item instanceof CrossbowItem) {
				rule.defaultParts.add(StatClockPartTypes.DAMAGE_DEALT);
				rule.defaultParts.add(StatClockPartTypes.MOBS_KILLED);
				
				rule.allowedParts.add(StatClockPartTypes.DAMAGE_DEALT_DISTANCE);
				rule.allowedParts.add(StatClockPartTypes.MOB_LOOT_DROPPED);
			}
		});
		
		//Fishing rod
		MAPPERS.add((Item item, MapRule rule) -> {
			if (item instanceof FishingRodItem) {
				rule.defaultParts.add(StatClockPartTypes.ITEMS_FISHED);
				
				rule.defaultParts.add(StatClockPartTypes.FISH_CAUGHT);
				rule.defaultParts.add(StatClockPartTypes.TREASURE_CAUGHT);
				rule.defaultParts.add(StatClockPartTypes.TRASH_CAUGHT);
				
				rule.allowedParts.add(StatClockPartTypes.MOBS_FISHED);
				
			}
		});
		
		//Music disc
		MAPPERS.add((Item item, MapRule rule) -> {
			if (item.components().get(DataComponents.JUKEBOX_PLAYABLE) != null) {
				rule.defaultParts.add(StatClockPartTypes.TIME_PLAYED);
				rule.defaultParts.add(StatClockPartTypes.TIMES_FINISHED);
			}
		});
		
		//Elytra
		MAPPERS.add((Item item, MapRule rule) -> {
			if (item.components().get(DataComponents.GLIDER) != null) {
				rule.defaultParts.add(StatClockPartTypes.DISTANCE_FLOWN);
				rule.allowedParts.add(StatClockPartTypes.ROCKET_USAGE);
			}
		});
		
		
		//Mount equipment
		MAPPERS.add((Item item, MapRule rule) -> {
			if (isInTag(item, ItemTags.HARNESSES)) {
				rule.defaultParts.add(StatClockPartTypes.DISTANCE_FLOATED);
			} else if (isEquippableSlot(item, EquipmentSlot.SADDLE)) {
				rule.defaultParts.add(StatClockPartTypes.DISTANCE_RODE);
			}
		});
		
		//Placeable vehicles
		MAPPERS.add((Item item, MapRule rule) -> {
			if (item instanceof BoatItem || item instanceof MinecartItem) {
				
				rule.defaultParts.add(StatClockPartTypes.VEHICLE_DISTANCE);
				
			}
		});
		
		//Armor general
		MAPPERS.add((Item item, MapRule rule) -> {
			boolean humanArmor = isInTags(item, ItemTags.HEAD_ARMOR, ItemTags.CHEST_ARMOR, ItemTags.LEG_ARMOR, ItemTags.FOOT_ARMOR);
			boolean mobArmor = isInTags(item, StatClocksMod.HORSE_ARMOR, StatClocksMod.NAUTILUS_ARMOR);
			
			if (humanArmor || mobArmor) {
				rule.defaultParts.add(StatClockPartTypes.DAMAGE_TAKEN);
				rule.defaultParts.add(StatClockPartTypes.DAMAGE_REDUCED);
			}
			
			if (item == Items.WOLF_ARMOR) {
				rule.defaultParts.add(StatClockPartTypes.DAMAGE_REDUCED);
			}
		});
		
		//Shield
		MAPPERS.add((Item item, MapRule rule) -> {
			if (item instanceof ShieldItem) {
				rule.defaultParts.add(StatClockPartTypes.DAMAGE_REDUCED);
				rule.defaultParts.add(StatClockPartTypes.ATTACKS_BLOCKED);
			}
		});
		
		//Armor slot specific
		MAPPERS.add((Item item, MapRule rule) -> {
			if (isInTag(item, ItemTags.HEAD_ARMOR)) {
				rule.allowedParts.add(StatClockPartTypes.TIME_UNDERWATER);		// Respiration & turtle shell
				rule.allowedParts.add(StatClockPartTypes.MINED_UNDERWATER);		// Aqua Affinity
				
				rule.allowedParts.add(StatClockPartTypes.ITEMS_CONSUMED);
			} else if (isInTag(item, ItemTags.LEG_ARMOR)) {
				rule.allowedParts.add(StatClockPartTypes.DISTANCE_CROUCHED);	// Swift sneak
				rule.allowedParts.add(StatClockPartTypes.DISTANCE_WALKED);		// Because
			} else if (isInTag(item, ItemTags.FOOT_ARMOR)) {
				rule.allowedParts.add(StatClockPartTypes.DISTANCE_SWAM);		// Depth Strider
				rule.allowedParts.add(StatClockPartTypes.DISTANCE_FALLEN);		// Feather Falling
				rule.allowedParts.add(StatClockPartTypes.DISTANCE_WADED);		// Depth strider
				rule.allowedParts.add(StatClockPartTypes.DISTANCE_WALKED);		// Soul speed, frost walker & depth strider
			}
		});
	}
	
	private static <T> boolean isEquippableSlot (Item item, EquipmentSlot slot) {
		Equippable equippable = item.components().get(DataComponents.EQUIPPABLE);
		
		if (equippable == null) return false;
		
		return equippable.slot() == slot;
	}
	
	private static boolean isInTag (Item item, TagKey<Item> tag) {
		return BuiltInRegistries.ITEM.wrapAsHolder(item).is(tag);
	}
	
	@SafeVarargs
	private static boolean isInTags (Item item, TagKey<Item>... tags) {
		for (TagKey<Item> tag : tags) {
			if (BuiltInRegistries.ITEM.wrapAsHolder(item).is(tag))
				return true;
		}
		
		return false;
	}
	
	private static boolean isItem (Item item, Item... items) {
		for (Item check : items) {
			if (item == check)
				return true;
		}
		
		return false;
	}
	
	
	public static MapRule GetItemMapping (Item item) {
		
		MapRule ret = new MapRule();
		
		for (RuleMapper mapper : MAPPERS) {
			mapper.addRules(item, ret);
		}
		
		return ret;
	}
	
}
