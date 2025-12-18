package orca.statclocks.mixin.listener;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.Block.getDrops;
import static net.minecraft.world.level.block.Block.popResource;

@Mixin(Block.class)
public class BlockMixin {
	
	
	@Inject(
		method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void dropResources (BlockState blockState, Level level, BlockPos blockPos, BlockEntity blockEntity, Entity entity, ItemStack itemStack, CallbackInfo ci) {
		ci.cancel();
		
		if (level instanceof ServerLevel) {
			getDrops(blockState, (ServerLevel)level, blockPos, blockEntity, entity, itemStack).forEach(
				drop -> forEachDrop(level, blockPos, entity, drop, itemStack)
			);
			blockState.spawnAfterBreak((ServerLevel)level, blockPos, itemStack, true);
		}
	}
	
	@Unique
	private static void forEachDrop (Level level, BlockPos blockPos, Entity entity, ItemStack drop, ItemStack tool) {
		popResource(level, blockPos, drop);
		
		if (entity instanceof Player) {
			MiscListeners.BLOCK_LOOT_LISTENER.applyToParts(tool, drop, drop.getCount());
		}
	}
}
