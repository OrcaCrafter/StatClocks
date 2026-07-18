package orca.statclocks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.StatClockScreen;
import orca.statclocks.StatClocksClient;
import orca.statclocks.components.StatClockContent;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {
	
	@Shadow
	@Nullable
	protected Slot hoveredSlot;
	
	protected AbstractContainerScreenMixin (Component component) {
		super(component);
	}
	
	@Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
	void keyPressed (KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
		if (!StatClocksClient.openStatScreenBind.matches(keyEvent)) return;
			
		Slot slot = this.hoveredSlot;
		
		if (slot == null) return;
		
		ItemStack item = slot.getItem();
		
		StatClockContent content = item.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (content == null) return;
		
		Minecraft.getInstance().setScreen(new StatClockScreen(this, item, content));
		
		cir.cancel();
		
	}
}
