package orca.statclocks.lists;

import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockPartType;
import orca.statclocks.listeners.DamageListener;
import orca.statclocks.listeners.ListenerAdapter;
import orca.statclocks.listeners.MiscListeners;
import orca.statclocks.listeners.StatisticsListeners;
import oshi.util.tuples.Pair;

import java.util.LinkedHashMap;
import java.util.function.Predicate;

public class StatClockPartTypes {
	
	//Used linked hash map so the creative mode inventory is in a proper order
	public static LinkedHashMap<Identifier, Pair<StatClockPartType, PartTypeInfo>> STAT_PART_TYPES = new LinkedHashMap<>();
	
	//Poke to start initializing
	public static void init () {
	}
	
	public static final StatClockPartType EMPTY = new PartTypeInfo("none").english("Blank Stat Clock").markNoListener().close();
	
	//General
	public static final StatClockPartType BLOCKS_MINED = new PartTypeInfo("blocks_mined")
		.english("Blocks Mined").setFilterTypeBlock("%1$s Mined")
		.addCraftingResource(Items.IRON_PICKAXE)
		.addListener(
			StatisticsListeners.AddBlockMinedListener((Player player, Block target, ListenerAdapter adapter, int amount) -> adapter.applyToParts(player.getActiveItem(), target, amount))
		).close();
	
	
	public static final StatClockPartType TIMES_USED = new PartTypeInfo("times_used")
		.english("Times Used")
		.addListener(MiscListeners.FOOD_ON_A_STICK_LISTENER)
		.addListener(MiscListeners.AXE_USE_LISTENER)
		.addListener(MiscListeners.SHOVEL_USE_LISTENER)
		.addListener(MiscListeners.HOE_USE_LISTENER)
		.addListener(MiscListeners.BRUSH_USE_LISTENER)
		.addListener(MiscListeners.FLINT_AND_STEEL_USE_LISTENER)
		.addListener(MiscListeners.INSTRUMENT_USE_LISTENER)
		.addListener(MiscListeners.SPYGLASS_USE_LISTENER)
		.addListener(MiscListeners.SHEARS_USE_LISTENER)
		.addCraftingResource(Items.SHEARS).close();
	
	public static final StatClockPartType MOBS_IGNITED = new PartTypeInfo("mobs_ignited")
		.english("Mobs Ignited").setFilterTypeEntity("%1$s Ignited", StatClocksMod.IGNITABLE_MOBS).addCraftingResource(Items.GUNPOWDER)
		.addListener(MiscListeners.MOBS_IGNITED)
		.close();
	
	public static final StatClockPartType MOBS_SHEARED = new PartTypeInfo("mobs_sheared")
		.english("Mobs Sheared").setFilterTypeEntity("%1$s Sheared", StatClocksMod.SHEARABLE_MOBS)
		.addCraftingResource(ItemTags.WOOL)
		.addListener(MiscListeners.MOBS_SHEARED)
		.close();
	
	public static final StatClockPartType MOBS_BRUSHED = new PartTypeInfo("mobs_brushed")
		.english("Mobs Brushed").setFilterTypeEntity("%1$s Brushed", StatClocksMod.BRUSHABLE_MOBS)
		.addCraftingResource(Items.BRUSH)
		.addListener(MiscListeners.MOBS_BRUSHED)
		.close();
	
	public static final StatClockPartType BLOCK_LOOT_DROPPED = new PartTypeInfo("block_loot_dropped")
		.addListener(MiscListeners.BLOCK_LOOT_LISTENER)
		.english("Block Loot Dropped").setFilterTypeItem("%1$s Dropped")
		.addCraftingResource(Items.CHEST).close();
	
	public static final StatClockPartType DAMAGE_DEALT = new PartTypeInfo("damage_dealt")
		.english("Damage Dealt").setFilterTypeEntity("Damage Dealt to %1$s")
		.setFormat(PartValueFormat.DAMAGE)
		.addListener(MiscListeners.DAMAGE_DEALT)
		.addCraftingResource(Items.IRON_SWORD)
		.close();
	
	public static final StatClockPartType DAMAGE_DEALT_DISTANCE = new PartTypeInfo("damage_dealt_distance")
		.english("Damage Dealt").setFilterTypeEntity("Damage Dealt to %1$s")
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(MiscListeners.DAMAGE_DEALT_DISTANCE)
		.addCraftingResource(Items.BOW)
		.close();
	
	public static final StatClockPartType MOBS_KILLED = new PartTypeInfo("mobs_killed")
		.english("Mobs Killed").setFilterTypeEntity("%1$s Killed")
		.addListener(
			StatisticsListeners.AddEntityKilledListener((Player player, EntityType<?> target, ListenerAdapter adapter, int amount) -> adapter.applyToParts(player.getWeaponItem(), target, amount))
		)
		.addCraftingResource(Items.DIAMOND_SWORD).close();
	
	public static final StatClockPartType MOB_LOOT_DROPPED = new PartTypeInfo("mob_loot_dropped")
		.english("Mob Loot Dropped").setFilterTypeItem("%1$s Dropped")
		.addListener(MiscListeners.MOB_LOOT_LISTENER)
		.addCraftingResource(Items.BUNDLE).close();
	
	public static final StatClockPartType DAMAGE_TAKEN = new PartTypeInfo("damage_taken")
		.english("Damage Taken").setFilterTypeEntity("Damage Taken from %1$s")
		.setFormat(PartValueFormat.DAMAGE)
		.addListener(DamageListener.PLAYER_DAMAGE_TAKEN_ADAPTER)
		.addListener(DamageListener.HORSE_DAMAGE_TAKEN_ADAPTER)
		.addListener(DamageListener.WOLF_DAMAGE_TAKEN_ADAPTER)
		.addListener(DamageListener.NAUTILUS_DAMAGE_TAKEN_ADAPTER)
		.addCraftingResource(Items.IRON_HELMET).setFormat(PartValueFormat.DAMAGE).close();
	
	public static final StatClockPartType DAMAGE_REDUCED = new PartTypeInfo("damage_blocked")
		.english("Damage Blocked").setFilterTypeEntity("Damage Taken from %1$s Blocked")
		.setFormat(PartValueFormat.DAMAGE)
		.addListener(DamageListener.PLAYER_DAMAGE_BLOCKED_ADAPTER)
		.addListener(DamageListener.HORSE_DAMAGE_BLOCKED_ADAPTER)
		.addListener(DamageListener.WOLF_DAMAGE_BLOCKED_ADAPTER)
		.addListener(DamageListener.NAUTILUS_DAMAGE_BLOCKED_ADAPTER)
		.addCraftingResource(Items.GOLDEN_HELMET).addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				//Amount 10 times how much damage was blocked
				if (target == Stats.DAMAGE_BLOCKED_BY_SHIELD) {
					ItemStack shield = player.getActiveItem();
					//Amount is 10 times damage, which is what we want
					adapter.applyToParts(shield, target, amount);
				}
			})
		).close();
	
	//Shield
	public static final StatClockPartType ATTACKS_BLOCKED = new PartTypeInfo("attacks_blocked")
		.english("Attacks Blocked").setFilterTypeEntity("Attacks from %1$s Blocked")
		.addCraftingResource(Items.SHIELD)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				//If any damage has been blocked, increase attacks blocked by 1
				if (target == Stats.DAMAGE_BLOCKED_BY_SHIELD) {
					ItemStack shield = player.getActiveItem();
					adapter.applyToParts(shield, target, 1);
				
				}
				
			})
		).close();
	
	//Mace
	public static final StatClockPartType FALL_ATTACK_DISTANCE = new PartTypeInfo("fall_attack_distance")
		.english("Distance Fallen Attacking").setFilterTypeEntity("Distance Fallen Attacking %1$s")
		.addListener(MiscListeners.MACE_FALL_LISTENER)
		.addCraftingResource(Items.WIND_CHARGE)
		.setFormat(PartValueFormat.DISTANCE).close();
	
	//Misc armor
	public static final StatClockPartType DISTANCE_WALKED = new PartTypeInfo("distance_walked")
		.english("Distance Walked").addCraftingResource(Items.LEATHER_BOOTS)
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.WALK_ONE_CM) {
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
					
					adapter.applyToParts(boots, target, amount);
					adapter.applyToParts(legs, target, amount);
				}
				
			})
		).close();
	
	public static final StatClockPartType DISTANCE_CROUCHED = new PartTypeInfo("distance_crouched")
		.english("Distance Crouched").addCraftingResource(Items.ECHO_SHARD)
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.CROUCH_ONE_CM) {
					ItemStack Leggings = player.getItemBySlot(EquipmentSlot.LEGS);
					
					adapter.applyToParts(Leggings, target, amount);
				}
				
			})
		).close();
	
	public static final StatClockPartType DISTANCE_SWAM = new PartTypeInfo("distance_swam")
		.english("Distance Swam").addCraftingResource(Items.PRISMARINE_SHARD)
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.SWIM_ONE_CM) {
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					
					adapter.applyToParts(boots, target, amount);
				}
				
			})
		).close();
	
	public static final StatClockPartType DISTANCE_WADED = new PartTypeInfo("distance_waded")
		.english("Distance Waded").addCraftingResource(Items.WATER_BUCKET)
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.WALK_UNDER_WATER_ONE_CM) {
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					
					adapter.applyToParts(boots, target, amount);
				}
				
			})
		).close();
	
	public static final StatClockPartType DISTANCE_FROST_WALKED = new PartTypeInfo("distance_frost_walked")
		.english("Distance Frost Walked").addCraftingResource(Items.ICE)
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.WALK_ON_WATER_ONE_CM) {
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					
					adapter.applyToParts(boots, target, amount);
				}
				
			})
		).close();
	
	public static final StatClockPartType TIME_UNDERWATER = new PartTypeInfo("time_underwater")
		.english("Time Underwater").addCraftingResource(Items.TURTLE_SCUTE)
		.setFormat(PartValueFormat.TIME)
		.addListener(MiscListeners.PLAYER_UNDERWATER)
		.close();
	
	public static final StatClockPartType MINED_UNDERWATER = new PartTypeInfo("mined_underwater")
		.english("Blocks Mined Underwater").setFilterTypeBlock("%1$s Mined Underwater")
		.addCraftingResource(Items.HEART_OF_THE_SEA).addListener(
			StatisticsListeners.AddBlockMinedListener((Player player, Block target, ListenerAdapter adapter, int amount) -> {
				
				if (!player.isUnderWater()) return;
				
				ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
				
				adapter.applyToParts(helmet, target, amount);
			})
		).close();
	
	public static final StatClockPartType ITEMS_CONSUMED = new PartTypeInfo("items_consumed")
		.english("Items Consumed").setFilterTypeItem("%1$s Consumed", StatClocksMod.CONSUMABLE)
		.addCraftingResource(Items.BREAD).addListener(MiscListeners.PLAYER_CONSUMES).close();
	
	public static final StatClockPartType DISTANCE_FALLEN = new PartTypeInfo("distance_fallen")
		.english("Distance Fallen").addCraftingResource(Items.FEATHER)
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.FALL_ONE_CM) {
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					
					adapter.applyToParts(boots, target, amount);
				}
				
			})
		).close();
	
	//Fishing Rod
	public static final StatClockPartType ITEMS_FISHED = new PartTypeInfo("items_fished")
		.english("Times Fished").setFilterTypeItem("Fished up %1$s", StatClocksMod.FISHABLE)
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> target instanceof ItemStack)
		.addCraftingResource(ItemTags.FISHES).close();
	
	public static final StatClockPartType FISH_CAUGHT = new PartTypeInfo("fish_caught")
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> {
			if (!(target instanceof ItemStack item)) return false;
			
			return item.is(StatClocksMod.FISHABLE_FISH);
		})
		.english("Fish Caught").addCraftingResource(StatClocksMod.FISHABLE_FISH).hide().close();
	
	public static final StatClockPartType TREASURE_CAUGHT = new PartTypeInfo("treasure_caught")
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> {
			if (!(target instanceof ItemStack item)) return false;
			
			//Sort trash rods from treasure rods
			if (item.getItem() == Items.FISHING_ROD) {
				return item.isEnchanted();
			}
			
			return item.is(StatClocksMod.FISHABLE_TREASURE);
		})
		.english("Treasure Caught").addCraftingResource(StatClocksMod.FISHABLE_TREASURE).hide().close();
	
	public static final StatClockPartType TRASH_CAUGHT = new PartTypeInfo("trash_caught")
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> {
			
			if (!(target instanceof ItemStack item)) return false;
			
			//Sort trash rods from treasure rods
			if (item.getItem() == Items.FISHING_ROD) {
				return !item.isEnchanted();
			}
			
			return item.is(StatClocksMod.FISHABLE_TRASH);
		})
		.english("Trash Caught").addCraftingResource(StatClocksMod.FISHABLE_TRASH).hide().close();
	
	public static final StatClockPartType MOBS_FISHED = new PartTypeInfo("mobs_fished")
		.english("Mobs Fished").setFilterTypeEntity("%1$s Yoinked")
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> {
			StatClocksMod.LOGGER.info(target.getClass().getSimpleName());
			
			return true;
		})
		.addCraftingResource(Items.FISHING_ROD).close();
	
	
	//Elytra
	public static final StatClockPartType DISTANCE_FLOWN = new PartTypeInfo("distance_flown")
		.english("Distance Flown").setFormat(PartValueFormat.DISTANCE).hide().addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.AVIATE_ONE_CM) {
					ItemStack elytra = player.getItemBySlot(EquipmentSlot.CHEST);
					adapter.applyToParts(elytra, target, amount);
				}
				
			})
		).close();
	public static final StatClockPartType ROCKET_USAGE = new PartTypeInfo("rocket_usage")
		.english("Rocket Duration Used").addCraftingResource(Items.FIREWORK_ROCKET).addListener(
			MiscListeners.FIREWORK_USED_LISTENER
		).close();
	
	//Saddle
	public static final StatClockPartType DISTANCE_RODE = new PartTypeInfo("distance_rode")
		.english("Distance Rode").setFilterTypeEntity("Distance Rode on %1$s", EntityTypeTags.CAN_EQUIP_SADDLE)
		.addCraftingResource(Items.SADDLE).setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				//CAMELS???
				
				//HORSE_ONE_CM includes horses, donkeys, mules and CAMELS!!!
				if (target == Stats.PIG_ONE_CM || target == Stats.HORSE_ONE_CM || target == Stats.STRIDER_ONE_CM || target == Stats.NAUTILUS_ONE_CM) {
					Entity vehicleEntity = player.getVehicle();
					
					//How this could happen is unknown
					if (vehicleEntity == null) return;
					
					if (!(vehicleEntity instanceof Mob vehicleMob)) return;
					
					ItemStack harness = vehicleMob.getItemBySlot(EquipmentSlot.SADDLE);
					adapter.applyToParts(harness, vehicleMob.getType(), amount);
				}
				
			})
		).close();
	
	//Harness
	public static final StatClockPartType DISTANCE_FLOATED = new PartTypeInfo("distance_floated")
		.english("Distance by Happy Ghast").addCraftingResource(ItemTags.HARNESSES)
		.setFormat(PartValueFormat.DISTANCE).addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.HAPPY_GHAST_ONE_CM) {
					Entity vehicleEntity = player.getVehicle();
					
					//How this could happen is unknown
					if (vehicleEntity == null) return;
					
					if (!(vehicleEntity instanceof Mob vehicleMob)) return;
					
					ItemStack harness = vehicleMob.getItemBySlot(EquipmentSlot.SADDLE);
					adapter.applyToParts(harness, vehicleMob.getType(), amount);
				}
			})
		).close();
	
	//Minecart
	public static final StatClockPartType VEHICLE_DISTANCE = new PartTypeInfo("vehicle_distance")
		.english("Distance Traveled")
		.setFormat(PartValueFormat.DISTANCE).markNoListener().hide().close();
	
	//Spyglass
	public static final StatClockPartType MOBS_SPIED_ON = new PartTypeInfo("mobs_spied_on")
		.english("Mobs Spied On").setFilterTypeEntity("%1$s Spied On")
		.addCraftingResource(Items.AMETHYST_SHARD).close();
	
	//Music disc
	public static final StatClockPartType TIME_PLAYED = new PartTypeInfo("time_played")
		.english("Time Played For").markNoListener().hide()
		.setFormat(PartValueFormat.TIME).close();
	
	public static final StatClockPartType TIMES_FINISHED = new PartTypeInfo("times_finished")
		.english("Times Finished").markNoListener().hide().close();
	
	
	
	
	public static ItemStack getPriorityHandItem (Player player, Predicate<ItemStack> condition) {
		ItemStack ret = player.getMainHandItem();
		
		if (condition.test(ret)) return ret;
		
		ret = player.getOffhandItem();
		
		if (condition.test(ret)) return ret;
		
		return ItemStack.EMPTY;
	}
}
