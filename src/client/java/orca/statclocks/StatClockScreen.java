package orca.statclocks;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.components.StatClockContent;

//TODO item viewer screen?

public class StatClockScreen extends Screen {
	
	private static final Component TITLE = Component.translatable("stat-clocks.view.title");
	
	public StatClockScreen (Player player, ItemStack itemStack, InteractionHand interactionHand, StatClockContent statClock) {
		super(TITLE);
	}
	
	protected void init () {
	
	}
	
	public boolean isPauseScreen () {
		return false;
	}
	
	public boolean isInGameUi () {
		return true;
	}
}
