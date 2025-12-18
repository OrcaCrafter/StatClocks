package orca.statclocks.listeners;

import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;

public class StatisticListenerWrapper <T> {
	
	StatEventListener<T> listener;
	ListenerAdapter adapter;
	Identifier customStatFilter;
	
	
	public StatisticListenerWrapper (StatEventListener<T> listener) {
		this.listener = listener;
		this.adapter = new ListenerAdapter();
		this.customStatFilter = null;
	}
	
	public StatisticListenerWrapper (StatEventListener<T> listener, Identifier customStatFilter) {
		this.listener = listener;
		this.adapter = new ListenerAdapter();
		this.customStatFilter = customStatFilter;
	}
	
	public StatEventListener<T> listener () {
		return listener;
	}
	
	public ListenerAdapter adapter () {
		return adapter;
	}
	
	public boolean passesFilter (Stat<T> stat) {
		
		//No filter set
		if (customStatFilter == null) return true;
		
		//Only filter custom stats
		Object value = stat.getValue();
		if (!(value instanceof Identifier identifier)) return false;
		
		return customStatFilter == identifier;
	
	}
};