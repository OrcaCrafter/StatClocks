package orca.statclocks.mixin.listener;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;

@Mixin(PumpkinBlock.class)
public abstract class PumpkinMixin extends Block {
	
	public PumpkinMixin (Properties properties) {
		super(properties);
	}
	
	@WrapOperation(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/PumpkinBlock;dropFromBlockInteractLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;Ljava/util/function/BiConsumer;)Z"))
	boolean dropFromBlockInteractLootTable (
		ServerLevel level, ResourceKey<?> lootTable, BlockState blockState,
		BlockEntity blockEntity, ItemStack stack, Entity entity, BiConsumer<ServerLevel, ItemStack> spawnItem,
		Operation<Boolean> original
	) {
		
		if (!(entity instanceof Player player)) {
			return original.call(level, lootTable, blockState, blockEntity, stack, entity, spawnItem);
		}
		
		BiConsumer<ServerLevel, ItemStack> handelItem = (serverLevelX, loot) -> {
			
			MiscListeners.SHEARS_USE_LISTENER_ITEM.applyToParts(player, stack, loot, loot.getCount());
			MiscListeners.SHEARS_USE_LISTENER_BLOCK.applyToParts(player, stack, blockState.getBlock(), 1);
			
			spawnItem.accept(serverLevelX, loot);
		};
		
		return original.call(level, lootTable, blockState, blockEntity, stack, entity, handelItem);
	}
}
