package orca.statclocks.mixin.listener;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import orca.statclocks.StatClocksMod;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushableBlockEntity.class)
public class BrushableBlockEntityMixin {
	
	@Shadow
	private ItemStack item;
	
	@Inject(method = "dropContent", at = @At("HEAD"))
	private void dropContent (ServerLevel serverLevel, LivingEntity livingEntity, ItemStack brush, CallbackInfo ci) {
		if (!(livingEntity instanceof Player player)) return;
		
		StatClocksMod.LOGGER.info("Brush: {} found: {}", brush, this.item);
		
		if (!this.item.isEmpty()) {
			MiscListeners.BRUSH_USE_LISTENER.applyToParts(player, brush, this.item, 1);
		}
		
	
	}

}
