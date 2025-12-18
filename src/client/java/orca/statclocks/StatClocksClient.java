package orca.statclocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;

public class StatClocksClient implements ClientModInitializer {
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
		
	}
}