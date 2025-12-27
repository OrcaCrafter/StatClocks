package orca.statclocks.mixin.listener;


import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import orca.statclocks.listeners.MiscListeners;
import orca.statclocks.lists.StatClockPartTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
public abstract class HoeItemMixin extends Item {
	
	
	public HoeItemMixin (Properties properties) {
		super(properties);
	}
	
	@Inject(method = "useOn", at = @At("RETURN"))
	public void useOn (UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
		
		if (cir.getReturnValue() != InteractionResult.SUCCESS) return;
		
		Player player = useOnContext.getPlayer();
		
		if (player == null) return;
		
		ItemStack tool = StatClockPartTypes.getPriorityHandItem(player, (item) -> item.getItem() instanceof HoeItem);
		
		MiscListeners.HOE_USE_LISTENER.applyToParts(player, tool, null, 1);
		
	}
}
