package orca.statclocks.listeners;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.*;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class ListenerAdapter {
	
	public static boolean DETAIL_LOG = false;
	
	public interface Filter {
		boolean allowApplication (Object target);
	}
	
	public static Filter NO_FILTER = (Object target) -> true;
	
	List<Pair<StatClockPartType, Filter>> allowedTypes = new ArrayList<>();
	
	
	public void applyToParts (Player player, ItemStack item, Object target, int amount) {
		
		if (applyToPartsNonPlayer(item, target, amount)) {
			if (player == null) {
				StatClocksMod.LOGGER.warn("Player is null");
			} else if (!player.isLocalPlayer() && player instanceof ServerPlayer serverPlayer) {
				
				int slotIndex = -1;
				
				for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
					ItemStack check = player.getInventory().getItem(i);
					
					if (check.equals(item)) {
						slotIndex = i;
					}
				}
				
				if (slotIndex >= 0) {
					ClientboundSetPlayerInventoryPacket packet = player.getInventory().createInventoryUpdatePacket(slotIndex);
					
					serverPlayer.connection.send(packet);
					
					StatClocksMod.LOGGER.info("Sent inventory update packet for slot: {}", slotIndex);
				} else {
					StatClocksMod.LOGGER.warn("Failed to fin matching item");
				}
				
				StatClocksMod.LOGGER.warn("Player is server side");
			}
		}
		
	}
	
	public boolean applyToPartsNonPlayer (ItemStack item, Object target, int amount) {
		if (item == null || item.isEmpty()) return false;
		
		StatClockContent statClock = item.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (statClock == null) return false;
		
		ArrayList<StatClockPartContent> parts = statClock.getParts();
		
		if (DETAIL_LOG) StatClocksMod.LOGGER.info("");
		if (DETAIL_LOG) StatClocksMod.LOGGER.info("");
		if (DETAIL_LOG) StatClocksMod.LOGGER.info("Starting general application");
		
		boolean added = false;
		
		for (StatClockPartContent part : parts) {
			if (DETAIL_LOG) StatClocksMod.LOGGER.info("\tStarting part {} application", part.getType().getIdName());
			
			added = added || applyToPart(part, target, amount);
		}
		
		statClock.setParts(parts);
		
		item.set(StatClockContent.STAT_CLOCK_COMPONENT, statClock);
		
		return added;
	}
	
	private boolean applyToPart (StatClockPartContent part, Object target, int amount) {
		
		StatClockPartType partType = part.getType();
		
		boolean matches = false;
		
		for (Pair<StatClockPartType, Filter> pair : allowedTypes) {
			
			StatClockPartType allowedType = pair.getA();
			
			if (DETAIL_LOG) StatClocksMod.LOGGER.info("\t\t{} and {}", partType.getIdName(), allowedType.getIdName());
			
			if (allowedType.sameType(partType) && target != null)
				if (DETAIL_LOG) StatClocksMod.LOGGER.info("\t\t\t{} allowed? {}", target.getClass().getSimpleName(), pair.getB().allowApplication(target) ? "Allowed" : "Not Allowed");
			
			if (allowedType.sameType(partType) && pair.getB().allowApplication(target)) {
				if (DETAIL_LOG) StatClocksMod.LOGGER.info("\t\t\tPassed: {} and {}", partType.getIdName(), allowedType.getIdName());
				matches = true;
				break;
			}
		}
		
		if (!matches) return false;
		
		if (DETAIL_LOG) StatClocksMod.LOGGER.info("\tPassed to next step");
		
		StatClockFilterContent filter = partType.filter;
		StatClockFilterType filterType = filter.getType();
		
		boolean passes = switch (filterType) {
			case ITEM -> {
				if (target instanceof Item setItem) {
					yield filter.itemPassesFilter(setItem);
				} else if (target instanceof ItemStack setItem) {
					yield filter.itemPassesFilter(setItem.getItem());
				} else {
					yield false;
				}
			}
			case BLOCK -> {
				if (!(target instanceof Block targetBlock)) yield false;
				
				yield filter.blockPassesFilter(targetBlock);
			}
			case ENTITY -> {
				if (target instanceof EntityType<?> setEntity) {
					yield filter.entityPassesFilter(setEntity);
				} else if (target instanceof Entity setEntity) {
					yield filter.entityPassesFilter(setEntity.getType());
				} else {
					yield false;
				}
			}
			default -> true;
		};
		
		if (!passes) return false;
		
		if (DETAIL_LOG) StatClocksMod.LOGGER.info("\tAdded!");
		
		part.incrementCount(amount);
		
		return true;
	}
	
	public void addPartType (StatClockPartType type, Filter filter) {
		allowedTypes.add(new Pair<>(type, filter));
	}
}


