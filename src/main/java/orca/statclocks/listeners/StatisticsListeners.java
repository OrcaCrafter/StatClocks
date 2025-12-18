package orca.statclocks.listeners;

import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public interface StatisticsListeners {
	
	List<StatisticListenerWrapper<Identifier>>		CUSTOM_STATISTIC_LISTENERS	= new ArrayList<>();
	List<StatisticListenerWrapper<Item>>			ITEM_STATISTIC_LISTENERS	= new ArrayList<>();
	List<StatisticListenerWrapper<Block>>			BLOCK_STATISTIC_LISTENERS	= new ArrayList<>();
	List<StatisticListenerWrapper<EntityType<?>>>	ENTITY_STATISTIC_LISTENERS	= new ArrayList<>();
	
	static ListenerAdapter AddCustomListener (StatEventListener<Identifier> custom) {
		StatisticListenerWrapper<Identifier> pair = new StatisticListenerWrapper<>(custom);
		
		CUSTOM_STATISTIC_LISTENERS.add(pair);
		
		return pair.adapter();
	}
	
	static ListenerAdapter AddItemUsedListener (StatEventListener<Item> item)  {
		StatisticListenerWrapper<Item> pair = new StatisticListenerWrapper<>(item);
		
		ITEM_STATISTIC_LISTENERS.add(pair);
		
		return pair.adapter();
	}
	
	static ListenerAdapter AddBlockMinedListener (StatEventListener<Block> block) {
		StatisticListenerWrapper<Block> pair = new StatisticListenerWrapper<>(block);
		
		BLOCK_STATISTIC_LISTENERS.add(pair);
		
		return pair.adapter();
	}
	
	static ListenerAdapter AddEntityKilledListener (StatEventListener<EntityType<?>> entity) {
		StatisticListenerWrapper<EntityType<?>> pair = new StatisticListenerWrapper<>(entity);
		
		ENTITY_STATISTIC_LISTENERS.add(pair);
		
		return pair.adapter();
	}
	
	static void StatEvent (Player player, Stat<?> stat, int amount) {
		Object value = stat.getValue();
		
		if (stat.getType() == Stats.CUSTOM) {
			//Custom listeners
			assert (value instanceof Identifier);
			Identifier location = (Identifier) value;
			
			CUSTOM_STATISTIC_LISTENERS.forEach((pair) -> {
				pair.listener().statEvent(player, location, pair.adapter(), amount);
			});
			
		} else if (stat.getType() == Stats.ITEM_USED) {
			//Item used listener
			assert (value instanceof Item);
			Item item = (Item) value;
			
			ITEM_STATISTIC_LISTENERS.forEach((pair) -> {
				pair.listener().statEvent(player, item, pair.adapter(), amount);
			});
			
		} else if (stat.getType() == Stats.BLOCK_MINED) {
			//Block mined listener
			assert (value instanceof Block);
			Block block = (Block) value;
			
			BLOCK_STATISTIC_LISTENERS.forEach((pair) -> {
				pair.listener().statEvent(player, block, pair.adapter(), amount);
			});
		} else if (stat.getType() == Stats.ENTITY_KILLED) {
			//Entity killed listener
			assert (value instanceof EntityType<?>);
			EntityType<?> entityType = (EntityType<?>) value;
			
			ENTITY_STATISTIC_LISTENERS.forEach((pair) -> {
				pair.listener().statEvent(player, entityType, pair.adapter(), amount);
			});
			
		}
	}
	
}
