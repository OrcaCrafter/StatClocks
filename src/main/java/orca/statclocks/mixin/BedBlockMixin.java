package orca.statclocks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BedBlock.class)
public class BedBlockMixin extends Block {
	
	public BedBlockMixin (Properties properties) {
		super(properties);
	}
	
	@Inject(method = "setPlacedBy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;updateNeighbourShapes(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;I)V"))
	void setPlacedBy (Level level, BlockPos footPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack, CallbackInfo ci, @Local(ordinal = 1) BlockPos headPos) {
		
		BedBlockEntity foot = level.getBlockEntity(footPos, BlockEntityType.BED).orElseThrow();
		BedBlockEntity head = level.getBlockEntity(headPos, BlockEntityType.BED).orElseThrow();
		
		head.setComponents(foot.components());
	}
	
	protected List<ItemStack> getDrops (BlockState blockState, LootParams.Builder builder) {
		List<ItemStack> drops = super.getDrops(blockState, builder);
		
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		
		if (blockEntity != null) {
			drops.forEach(itemStack -> itemStack.applyComponents(blockEntity.components()));
		}
		
		return drops;
	}
}
