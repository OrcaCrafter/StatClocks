package orca.statclocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.components.StatClockPartContent;
import orca.statclocks.lists.PartTypeInfo;

import java.awt.*;
import java.util.List;

public class StatClockPartList extends ContainerObjectSelectionList<StatClockPartList.Entry> {
	
	public static final int TOP_PADDING = 50;
	public static final int BOTTOM_PADDING = 20;
	public static final int SIDE_PADDING = 10;
	public static final int ENTRY_HEIGHT = 25;
	
	public StatClockPartList (Minecraft minecraft, int width, int height, ItemStack stack, StatClockContent clockContent, Font font) {
		super(minecraft, width/2 - SIDE_PADDING*2, height - TOP_PADDING - BOTTOM_PADDING, TOP_PADDING, ENTRY_HEIGHT);
		
		for (StatClockPartContent part : clockContent.getParts()) {
			addEntry(new Entry(this, width, stack, part, font));
		}
		
	}
	
	static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
		
		final StatClockPartContent content;
		final FocusableTextWidget text;
		final FocusableTextWidget count;
		final StatClockPartList parent;
		
		public Entry (StatClockPartList parent, int width, ItemStack stack, StatClockPartContent content, Font font) {
			this.parent = parent;
			this.setWidth(width);
			this.content = content;
			
			PartTypeInfo info = content.getType().getInfo();
			
			Color color = info.getToolTipColor(stack, content);
			
			text = FocusableTextWidget.builder(content.getType().getText(color), font, 0).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build();
			count = FocusableTextWidget.builder(info.getFormatted(content.getCount()), font, 0).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build();
		}
		
		@Override
		public List<? extends NarratableEntry> narratables () {
			return List.of(text, count);
		}
		
		@Override
		public void renderContent (GuiGraphics guiGraphics, int i, int j, boolean bl, float f) {
			text.setPosition(parent.getX() + 30, getY() + text.getHeight()/2);
			count.setPosition(parent.getRight() - 150, getY() + text.getHeight()/2);
			
			text.renderWidget(guiGraphics, i, j, f);
			count.renderWidget(guiGraphics, i, j, f);
			
			guiGraphics.renderItem(content.getType().GetOutputItem(), parent.getX() + 8, getY());
		}
		
		@Override
		public List<? extends GuiEventListener> children () {
			return List.of(text, count);
		}
	}
}
