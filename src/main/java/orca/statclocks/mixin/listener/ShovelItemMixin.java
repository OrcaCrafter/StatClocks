package orca.statclocks.mixin.listener;


import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import orca.statclocks.listeners.MiscListeners;
import orca.statclocks.lists.StatClockPartTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends Item {
	
	
	public ShovelItemMixin (Properties properties) {
		super(properties);
	}
	
	@Inject(method = "useOn", at = @At("RETURN"))
	public void useOn (UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
		
		if (cir.getReturnValue() != InteractionResult.SUCCESS) return;
		
		Player player = useOnContext.getPlayer();
		
		if (player == null) return;
		
		ItemStack tool = StatClockPartTypes.getPriorityHandItem(player, (item) -> item.getItem() instanceof ShovelItem);
		
		MiscListeners.SHOVEL_USE_LISTENER.applyToParts(player, tool, null, 1);
		
	}
}
