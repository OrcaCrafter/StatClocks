package orca.statclocks.mixin.listener;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.Level;
import orca.statclocks.listeners.MiscListeners;
import orca.statclocks.lists.StatClockPartTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpyglassItem.class)
public abstract class SpyglassItemMixin extends Item {
	
	
	public SpyglassItemMixin (Properties properties) {
		super(properties);
	}
	
	@Inject(method = "use", at = @At("RETURN"))
	public void use (Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
		
		//No filtering
		
		ItemStack tool = StatClockPartTypes.getPriorityHandItem(player, (item) -> item.getItem() instanceof SpyglassItem);
		
		MiscListeners.SPYGLASS_USE_LISTENER.applyToParts(player, tool, null, 1);
		
	}
}
