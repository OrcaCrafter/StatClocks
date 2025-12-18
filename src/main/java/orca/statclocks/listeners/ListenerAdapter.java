package orca.statclocks.listeners;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
	
	
	public void applyToParts (ItemStack item, Object target, int amount) {
		
		if (item.isEmpty()) return;
		
		StatClockContent statClock = item.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (statClock == null) return;
		
		ArrayList<StatClockPartContent> parts = statClock.getParts();
		
		if (DETAIL_LOG) StatClocksMod.LOGGER.info("");
		if (DETAIL_LOG) StatClocksMod.LOGGER.info("");
		if (DETAIL_LOG) StatClocksMod.LOGGER.info("Starting general application");
		
		for (StatClockPartContent part : parts) {
			if (DETAIL_LOG) StatClocksMod.LOGGER.info("\tStarting part {} application", part.getType().getIdName());
			applyToPart(part, target, amount);
		}
	}
	
	private void applyToPart (StatClockPartContent part, Object target, int amount) {
		
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
		
		if (!matches) return;
		
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
		
		if (!passes) return;
		
		if (DETAIL_LOG) StatClocksMod.LOGGER.info("\tAdded!");
		
		part.incrementCount(amount);
		
	}
	
	public void addPartType (StatClockPartType type, Filter filter) {
		allowedTypes.add(new Pair<>(type, filter));
	}
}


