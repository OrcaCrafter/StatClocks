package orca.statclocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.DrawItemStackOverlayCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.resources.Identifier;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.components.StatClockFilterType;
import orca.statclocks.components.StatClockPartType;
import orca.statclocks.datagen.ContextPartType;

import static orca.statclocks.StatClocksMod.MOD_ID;

public class StatClocksClient implements ClientModInitializer {
	
	public static final Identifier STAT_CLOCK_SPRITE_ID = Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/stat_clock_icon.png");
	public static final Identifier STAT_CLOCK_FILTER_SPRITE_ID = Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/stat_clock_filter_icon.png");
	
	@Override
	public void onInitializeClient () {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering
		
		
		//Add a callback to append tool tips to items from components
		ItemTooltipCallback.EVENT.register((stack, context, type, list) -> {
			
			DataComponentMap map = stack.getComponents();
			
			for (TypedDataComponent<?> component : map) {
				if (!(component.value() instanceof ToolTipComponent toolTip)) continue;
				
				toolTip.addToTooltip(stack, context, type, list);
			}
			
		});
		
		DrawItemStackOverlayCallback.EVENT.register((graphics, font, stack, i, j) -> {
			
			if (!Minecraft.getInstance().hasShiftDown()) return;
			
			if (stack.has(StatClockContent.STAT_CLOCK_COMPONENT)) {
				// renderLayer, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight
				graphics.blit(RenderPipelines.GUI_TEXTURED, STAT_CLOCK_SPRITE_ID, i + 10, j + 10, 0, 0, 6, 6, 16, 16, 16, 16);
			}
			
			StatClockPartType type = stack.get(StatClockPartType.PART_TYPE_COMPONENT);
			
			if (type != null) {
				
				if (type.filter.getType() != StatClockFilterType.NONE) {
					graphics.blit(RenderPipelines.GUI_TEXTURED, STAT_CLOCK_FILTER_SPRITE_ID, i + 10, j + 10, 0, 0, 6, 6, 16, 16, 16, 16);
				}
			}
		});
	}
}