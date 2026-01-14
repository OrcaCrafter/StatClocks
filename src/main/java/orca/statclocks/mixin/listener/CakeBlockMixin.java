package orca.statclocks.mixin.listener;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public abstract class CakeBlockMixin extends Block {
	
	
	public CakeBlockMixin (Properties properties) {
		super(properties);
	}
	
	@Inject(method = "eat", at = @At("HEAD"))
	private static void eat (LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Player player, CallbackInfoReturnable<InteractionResult> cir) {
		if (!player.canEat(false)) return;
		
		ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
		MiscListeners.PLAYER_CONSUMES.applyToParts(player, helmet, Items.CAKE, 1);
		
		
	}
}
