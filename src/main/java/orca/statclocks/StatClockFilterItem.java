package orca.statclocks;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import orca.statclocks.components.StatClockFilterContent;
import org.jetbrains.annotations.NotNull;


public class StatClockFilterItem extends Item {
	public StatClockFilterItem (Properties properties) {
		super(properties);
	}
	
	
	@Override
	public InteractionResult use (Level level, Player player, InteractionHand interactionHand) {
		return super.use(level, player, interactionHand);
	}
	
	@Override
	public @NotNull InteractionResult useOn (UseOnContext useOnContext) {
		//Used on block
		
		Block block = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos()).getBlock();
		
		ItemStack filterItem = useOnContext.getItemInHand();
		
		StatClockFilterContent filter = StatClockFilterContent.makeEmpty();
		
		filter.Set(block);
		
		filterItem.set(StatClockFilterContent.FILTER_COMPONENT, filter);
		
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public @NotNull InteractionResult interactLivingEntity (ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
		//Used on entity
		
		ItemStack filterItem = player.getItemInHand(interactionHand);
		
		StatClockFilterContent filter = StatClockFilterContent.makeEmpty();
		
		filter.Set(livingEntity.getType());
		
		filterItem.set(StatClockFilterContent.FILTER_COMPONENT, filter);
		
		return InteractionResult.SUCCESS;
	}
	
	
	@Override
	public boolean overrideStackedOnOther (ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
		//Clicked on item in inventory
		if (clickAction != ClickAction.SECONDARY) return false;
		
		ItemStack readStack = slot.getItem();
		
		if (readStack.is(Items.AIR)) return false;
		
		StatClockFilterContent filter = StatClockFilterContent.makeEmpty();
		
		filter.Set(readStack.getItem());
		
		itemStack.set(StatClockFilterContent.FILTER_COMPONENT, filter);
		
		return true;
	}
	
}
