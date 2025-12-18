package orca.statclocks.listeners;

import net.minecraft.world.entity.player.Player;

public interface StatEventListener <T> {
	
	void statEvent (Player player, T value, ListenerAdapter adapter, int amount);
	
}
