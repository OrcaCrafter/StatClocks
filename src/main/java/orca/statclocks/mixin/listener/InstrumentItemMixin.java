package orca.statclocks.mixin.listener;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import orca.statclocks.listeners.MiscListeners;
import orca.statclocks.lists.StatClockPartTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InstrumentItem.class)
public abstract class InstrumentItemMixin extends Item {
	
	
	public InstrumentItemMixin (Properties properties) {
		super(properties);
	}
	
	@Inject(method = "use", at = @At("RETURN"))
	public void use (Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
		
		//Consume not success
		if (cir.getReturnValue() != InteractionResult.CONSUME) return;
		
		ItemStack tool = StatClockPartTypes.getPriorityHandItem(player, (item) -> item.getItem() instanceof InstrumentItem);
		
		MiscListeners.INSTRUMENT_USE_LISTENER.applyToParts(player, tool, null, 1);
		
	}
}
