package orca.statclocks.lists;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockPartType;
import orca.statclocks.listeners.DamageListener;
import orca.statclocks.listeners.ListenerAdapter;
import orca.statclocks.listeners.MiscListeners;
import orca.statclocks.listeners.StatisticsListeners;
import oshi.util.tuples.Pair;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Predicate;

public class StatClockPartTypes {
	
	//Used linked hash map so the creative mode inventory is in a proper order
	public static final LinkedHashMap<Identifier, Pair<StatClockPartType, PartTypeInfo>> STAT_PART_TYPES = new LinkedHashMap<>();
	
	//Poke to start initializing
	public static void init () {
	}
	
	public static final StatClockPartType EMPTY = new PartTypeInfo("none").markNoListener().close();
	
	//General
	public static final StatClockPartType BLOCKS_MINED = new PartTypeInfo("blocks_mined")
		.setIncrementByDurabilityFunction(ItemStack::getDamageValue)
		.setFilterTypeBlock()
		.addListener(MiscListeners.BLOCK_MINED_LISTENER).close();
	
	
	public static final StatClockPartType TIMES_USED = new PartTypeInfo("times_used")
		.addListener(MiscListeners.FOOD_ON_A_STICK_LISTENER)
		.addListener(MiscListeners.AXE_USE_LISTENER)
		.addListener(MiscListeners.SHOVEL_USE_LISTENER)
		.addListener(MiscListeners.HOE_USE_LISTENER)
		.addListener(MiscListeners.INSTRUMENT_USE_LISTENER)
		.addListener(MiscListeners.SPYGLASS_USE_LISTENER)
		.close();
	
	public static final StatClockPartType BLOCKS_IGNITED = new PartTypeInfo("blocks_ignited")
		.addListener(MiscListeners.BLOCKS_IGNITED)
		.setFilterTypeBlock()
		.close();
	
	public static final StatClockPartType MOBS_IGNITED = new PartTypeInfo("mobs_ignited")
		.addListener(MiscListeners.MOBS_IGNITED)
		.setFilterTypeEntity(StatClocksMod.IGNITABLE_MOBS)
		.close();
	
	public static final StatClockPartType SHEARS_USED = new PartTypeInfo("shears_used")
		.addListener(MiscListeners.SHEARS_USE_LISTENER)
		.hide()
		.close();
	
	public static final StatClockPartType SHEARS_USED_ITEM = new PartTypeInfo("shears_used_item")
		.addListener(MiscListeners.SHEARS_USE_LISTENER_ITEM)
		.setFilterTypeItem(StatClocksMod.SHEARABLE_ITEMS)
		.close();
	
	public static final StatClockPartType SHEARS_USED_BLOCK = new PartTypeInfo("shears_used_block")
		.addListener(MiscListeners.SHEARS_USE_LISTENER_BLOCK)
		.setFilterTypeBlock(StatClocksMod.SHEARABLE_BLOCKS)
		.close();
	
	public static final StatClockPartType SHEARS_USED_ENTITY = new PartTypeInfo("shears_used_entity")
		.addListener(MiscListeners.SHEARS_USE_LISTENER_ENTITY)
		.setFilterTypeEntity(StatClocksMod.SHEARABLE_MOBS)
		.close();
	
	public static final StatClockPartType BRUSH_USED = new PartTypeInfo("brush_used")
		.addListener(MiscListeners.BRUSH_USE_LISTENER)
		.setFilterTypeItem(StatClocksMod.BRUSHABLE_ITEMS)
		.close();
	
	public static final StatClockPartType BLOCK_LOOT_DROPPED = new PartTypeInfo("block_loot_dropped")
		.addListener(MiscListeners.BLOCK_LOOT_LISTENER)
		.setFilterTypeItem()
		.close();
	
	public static final StatClockPartType DAMAGE_DEALT = new PartTypeInfo("damage_dealt")
		.setFormat(PartValueFormat.DAMAGE)
		.addListener(MiscListeners.DAMAGE_DEALT)
		.setFilterTypeEntity()
		.setIncrementByDurabilityFunction((stack) -> {
			
			//TODO find itemstack's attack damage
			int attackDamage = 5;
			
			return stack.getDamageValue()*attackDamage;
		})
		.close();
	
	public static final StatClockPartType DAMAGE_DEALT_DISTANCE = new PartTypeInfo("damage_dealt_distance")
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(MiscListeners.DAMAGE_DEALT_DISTANCE)
		.setFilterTypeEntity()
		.close();
	
	public static final StatClockPartType MOBS_KILLED = new PartTypeInfo("mobs_killed")
		.addListener(MiscListeners.ENTITY_KILLED_LISTENER)
		.setFilterTypeEntity()
		.close();
	
	public static final StatClockPartType MOB_LOOT_DROPPED = new PartTypeInfo("mob_loot_dropped")
		.addListener(MiscListeners.MOB_LOOT_LISTENER)
		.setFilterTypeItem()
		.close();
	
	public static final StatClockPartType DAMAGE_TAKEN = new PartTypeInfo("damage_taken")
		.setFormat(PartValueFormat.DAMAGE)
		.addListener(DamageListener.PLAYER_DAMAGE_TAKEN_ADAPTER)
		.addListener(DamageListener.HORSE_DAMAGE_TAKEN_ADAPTER)
		.addListener(DamageListener.NAUTILUS_DAMAGE_TAKEN_ADAPTER)
		.setFilterTypeEntity()
		.setFormat(PartValueFormat.DAMAGE).close();
	
	public static final StatClockPartType DAMAGE_REDUCED = new PartTypeInfo("damage_blocked")
		.setFormat(PartValueFormat.DAMAGE)
		.addListener(DamageListener.PLAYER_DAMAGE_BLOCKED_ADAPTER)
		.addListener(DamageListener.HORSE_DAMAGE_BLOCKED_ADAPTER)
		.addListener(DamageListener.WOLF_DAMAGE_BLOCKED_ADAPTER)
		.addListener(DamageListener.NAUTILUS_DAMAGE_BLOCKED_ADAPTER)
		.setFilterTypeEntity()
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				//Amount 10 times how much damage was blocked
				if (target == Stats.DAMAGE_BLOCKED_BY_SHIELD) {
					ItemStack shield = player.getActiveItem();
					//Amount is 10 times damage, which is what we want
					adapter.applyToParts(player, shield, target, amount);
				}
			})
		).close();
	
	//Shield
	public static final StatClockPartType ATTACKS_BLOCKED = new PartTypeInfo("attacks_blocked")
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				//If any damage has been blocked, increase attacks blocked by 1
				if (target == Stats.DAMAGE_BLOCKED_BY_SHIELD) {
					ItemStack shield = player.getActiveItem();
					adapter.applyToParts(player, shield, target, 1);
					
				}
				
			})
		).setFilterTypeEntity().close();
	
	//Mace
	public static final StatClockPartType FALL_ATTACK_DISTANCE = new PartTypeInfo("fall_attack_distance")
		.addListener(MiscListeners.MACE_FALL_LISTENER)
		.setFormat(PartValueFormat.DISTANCE).setFilterTypeEntity().close();
	
	//Misc armor
	public static final StatClockPartType DISTANCE_WALKED = new PartTypeInfo("distance_walked")
		.setFormat(PartValueFormat.DISTANCE)
		.setFilterTypeBlock()
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				Block standing = null;
				
				Optional<BlockPos> supportingPos = player.mainSupportingBlockPos;
				
				if (supportingPos.isPresent()) {
					standing = player.level().getBlockState(supportingPos.get()).getBlock();
				}
				
				//TODO should sprint be separate?
				if (target == Stats.WALK_ONE_CM || target == Stats.SPRINT_ONE_CM) {
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
					
					adapter.applyToParts(player, boots, standing, amount);
					adapter.applyToParts(player, legs, standing, amount);
				}
				
			})
		).close();
	
	public static final StatClockPartType DISTANCE_CROUCHED = new PartTypeInfo("distance_crouched")
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.CROUCH_ONE_CM) {
					ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
					
					adapter.applyToParts(player, leggings, target, amount);
				}
				
			})
		).close();
	
	public static final StatClockPartType DISTANCE_SWAM = new PartTypeInfo("distance_swam")
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.SWIM_ONE_CM) {
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					
					adapter.applyToParts(player, boots, target, amount);
				}
				
			})
		).close();
	
	public static final StatClockPartType DISTANCE_WADED = new PartTypeInfo("distance_waded")
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.WALK_UNDER_WATER_ONE_CM || target == Stats.WALK_ON_WATER_ONE_CM) {
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					
					adapter.applyToParts(player, boots, target, amount);
				}
				
			})
		).close();
	
	public static final StatClockPartType TIME_UNDERWATER = new PartTypeInfo("time_underwater")
		.setFormat(PartValueFormat.TIME)
		.addListener(MiscListeners.PLAYER_UNDERWATER)
		.close();
	
	public static final StatClockPartType MINED_UNDERWATER = new PartTypeInfo("mined_underwater")
		.addListener(MiscListeners.BLOCK_MINED_UNDERWATER_LISTENER).setFilterTypeBlock().close();
	
	public static final StatClockPartType ITEMS_CONSUMED = new PartTypeInfo("items_consumed")
		.addListener(MiscListeners.PLAYER_CONSUMES).setFilterTypeItem(StatClocksMod.CONSUMABLE).close();
	
	public static final StatClockPartType DISTANCE_FALLEN = new PartTypeInfo("distance_fallen")
		.setFormat(PartValueFormat.DISTANCE)
		.addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.FALL_ONE_CM) {
					ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
					
					adapter.applyToParts(player, boots, target, amount);
				}
				
			})
		).close();
	
	//Fishing Rod
	public static final StatClockPartType ITEMS_FISHED = new PartTypeInfo("items_fished")
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> target instanceof ItemStack)
		.setFilterTypeItem(StatClocksMod.FISHABLE).close();
	
	public static final StatClockPartType FISH_CAUGHT = new PartTypeInfo("fish_caught")
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> {
			if (!(target instanceof ItemStack item)) return false;
			
			return item.is(StatClocksMod.FISHABLE_FISH);
		})
		.hide().close();
	
	public static final StatClockPartType TREASURE_CAUGHT = new PartTypeInfo("treasure_caught")
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> {
			if (!(target instanceof ItemStack item)) return false;
			
			//Sort trash rods from treasure rods
			if (item.getItem() instanceof FishingRodItem) {
				return item.isEnchanted();
			}
			
			return item.is(StatClocksMod.FISHABLE_TREASURE);
		})
		.hide().close();
	
	public static final StatClockPartType TRASH_CAUGHT = new PartTypeInfo("trash_caught")
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> {
			
			if (!(target instanceof ItemStack item)) return false;
			
			//Sort trash rods from treasure rods
			if (item.getItem() instanceof FishingRodItem) {
				return !item.isEnchanted();
			}
			
			return item.is(StatClocksMod.FISHABLE_TRASH);
		})
		.hide().close();
	
	public static final StatClockPartType MOBS_FISHED = new PartTypeInfo("mobs_fished")
		.addListener(MiscListeners.FISH_ITEM_LISTENER, (Object target) -> {
			StatClocksMod.LOGGER.info(target.getClass().getSimpleName());
			
			return true;
		})
		.setFilterTypeEntity().close();
	
	
	//Elytra
	public static final StatClockPartType DISTANCE_FLOWN = new PartTypeInfo("distance_flown")
		.setFormat(PartValueFormat.DISTANCE).hide().addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.AVIATE_ONE_CM) {
					ItemStack elytra = player.getItemBySlot(EquipmentSlot.CHEST);
					adapter.applyToParts(player, elytra, target, amount);
				}
				
			})
		).close();
	public static final StatClockPartType ROCKET_USAGE = new PartTypeInfo("rocket_usage")
		.addListener(
			MiscListeners.FIREWORK_USED_LISTENER
		).close();
	
	//Saddle
	public static final StatClockPartType DISTANCE_RODE = new PartTypeInfo("distance_rode")
		.setFormat(PartValueFormat.DISTANCE)
		.setFilterTypeEntity(EntityTypeTags.CAN_EQUIP_SADDLE)
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
					adapter.applyToParts(player, harness, vehicleMob.getType(), amount);
				}
				
			})
		).close();
	
	//Harness
	public static final StatClockPartType DISTANCE_FLOATED = new PartTypeInfo("distance_floated")
		
		.setFormat(PartValueFormat.DISTANCE).addListener(
			StatisticsListeners.AddCustomListener((Player player, Identifier target, ListenerAdapter adapter, int amount) -> {
				
				if (target == Stats.HAPPY_GHAST_ONE_CM) {
					Entity vehicleEntity = player.getVehicle();
					
					//How this could happen is unknown
					if (vehicleEntity == null) return;
					
					if (!(vehicleEntity instanceof Mob vehicleMob)) return;
					
					ItemStack harness = vehicleMob.getItemBySlot(EquipmentSlot.SADDLE);
					adapter.applyToParts(player, harness, vehicleMob.getType(), amount);
				}
			})
		).close();
	
	//Minecart
	public static final StatClockPartType VEHICLE_DISTANCE = new PartTypeInfo("vehicle_distance")
		.setFormat(PartValueFormat.DISTANCE).markNoListener().hide().close();
	
	//Music disc
	public static final StatClockPartType TIME_PLAYED = new PartTypeInfo("time_played")
		.markNoListener().hide()
		.setFormat(PartValueFormat.TIME).close();
	
	public static final StatClockPartType TIMES_FINISHED = new PartTypeInfo("times_finished")
		.markNoListener().hide().close();
	
	//Container
	public static final StatClockPartType ITEMS_DEPOSITED = new PartTypeInfo("items_deposited")
		.setFilterTypeItem().close();
	
	//Banner patterns, shulker boxes, bundles and leather armor
	public static final StatClockPartType DYE_USED = new PartTypeInfo("dye_used")
		.setFilterTypeItem(StatClocksMod.DYE)
		.addListener(MiscListeners.DYE_USED_LISTENER).close();
	
	public static final StatClockPartType MINECART_FUELED = new PartTypeInfo("minecart_fueled")
		.setFilterTypeItem(ItemTags.FURNACE_MINECART_FUEL).close();
	
	public static final StatClockPartType SPYED_ON = new PartTypeInfo("spyed_on")
		.addListener(MiscListeners.SPYED_ON_LISTENER)
		.setFilterTypeEntity()
		.close();
	
	public static final StatClockPartType TIMES_SLEPT = new PartTypeInfo("times_slept")
		.markNoListener()
		.setFilterTypeEntity(StatClocksMod.CAN_SLEEP)
		.close();
	
	public static ItemStack getPriorityHandItem (Player player, Predicate<ItemStack> condition) {
		ItemStack ret = player.getMainHandItem();
		
		if (condition.test(ret)) return ret;
		
		ret = player.getOffhandItem();
		
		if (condition.test(ret)) return ret;
		
		return ItemStack.EMPTY;
	}
}
