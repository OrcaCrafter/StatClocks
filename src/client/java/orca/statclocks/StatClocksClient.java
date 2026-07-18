package orca.statclocks;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.DrawItemStackOverlayCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.components.StatClockFilterType;
import orca.statclocks.components.StatClockPartType;
import orca.statclocks.datagen.ContextPartType;
import org.lwjgl.glfw.GLFW;

import static orca.statclocks.StatClocksMod.MOD_ID;

public class StatClocksClient implements ClientModInitializer {
	
	public static final Identifier STAT_CLOCK_SPRITE_ID = Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/stat_clock_icon.png");
	public static final Identifier STAT_CLOCK_FILTER_SPRITE_ID = Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/stat_clock_filter_icon.png");
	
	public static KeyMapping openStatScreenBind;
	
	@Override
	public void onInitializeClient () {
		
		SelectItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MOD_ID, "stat_clock_part_type"), ContextPartType.TYPE);
		
		openStatScreenBind = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.stat-clocks.open_stat_viewer", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, KeyMapping.Category.MISC
		));
		
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			if (client.screen != null) return;
			
			while (openStatScreenBind.consumeClick()) {
				ItemStack held = client.player.getItemHeldByArm(HumanoidArm.RIGHT);
				
				StatClockContent clock = held.get(StatClockContent.STAT_CLOCK_COMPONENT);
				
				if (clock == null) {
					client.player.displayClientMessage(Component.translatable("stat-clocks.view.failed"), true);
					return;
				}
				
				client.setScreen(new StatClockScreen(null, held, clock));
			}
		});
		
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