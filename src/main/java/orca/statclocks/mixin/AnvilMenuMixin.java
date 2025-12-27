package orca.statclocks.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.*;
import orca.statclocks.lists.PartTypeInfo;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
	
	
	@Shadow @Final private DataSlot cost;
	@Shadow @Nullable private String itemName;
	
	public AnvilMenuMixin (@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition) {
		super(menuType, i, inventory, containerLevelAccess, itemCombinerMenuSlotDefinition);
	}
	
	@Inject(method = "createResult", at = @At("RETURN"))
	private void createResult (CallbackInfo ci) {
		ItemStack a = this.inputSlots.getItem(0);
		ItemStack b = this.inputSlots.getItem(1);
		
		if (a.isEmpty()) return;
		if (b.isEmpty()) return;
		
		if (b.is(StatClocksMod.STAT_CLOCK)) {
			statClockAddition(a, b);
		} else if (b.is(StatClocksMod.STAT_CLOCK_PART)) {
			addPart(a, b);
		} else if (a.is(StatClocksMod.STAT_CLOCK_PART) && b.is(StatClocksMod.STAT_CLOCK_FILTER)) {
			//Apply filter to stat clock part
			assignPartFilter(a, b);
		} else if (!a.is(StatClocksMod.STAT_CLOCK) && b.is(StatClocksMod.STAT_CLOCK_REMOVER)) {
			removeStatClock(a);
		}
	}
	
	@Unique
	private void statClockAddition (ItemStack tool, ItemStack clock) {
		StatClockContent statClockContent = clock.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (statClockContent == null) {
			//Adding a blank clock to a tool
			statClockContent = StatClockContent.DefaultStatClock(tool);
			
			//Failed to create, likely not a tool
			if (statClockContent == null) return;
		} else {
			
			if (!statClockContent.canApplyToTool(tool)) return;
			
			//Adding an existing clock to a tool
			statClockContent = statClockContent.cloneClockContent();
		}
		
		ItemStack result = tool.copy();
		
		StatClockContent.mergeClockInfo(result, statClockContent);
		
		boolean renaming = renaming(tool, result);
		
		this.resultSlots.setItem(0, result);
		this.cost.set(renaming ? 2 : 1);
		
	}
	
	@Unique
	private void addPart (ItemStack tool, ItemStack part) {
		StatClockContent statClockContent = tool.get(StatClockContent.STAT_CLOCK_COMPONENT);
		StatClockPartType partType = part.get(StatClockPartType.PART_TYPE_COMPONENT);
		
		//Cannot add a part to a clockless tool
		if (statClockContent == null) return;
		if (partType == null) return;
		statClockContent = statClockContent.cloneClockContent();
		partType = partType.clonePartType();
		
		if (!statClockContent.canAddPart(partType, tool)) return;
		
		ItemStack result = tool.copy();
		statClockContent = statClockContent.cloneClockContent();
		
		statClockContent.addPart(new StatClockPartContent(partType));
		
		result.set(StatClockContent.STAT_CLOCK_COMPONENT, statClockContent);
		
		boolean renaming = renaming(tool, result);
		
		this.resultSlots.setItem(0, result);
		this.cost.set(renaming ? 2 : 1);
	}
	
	@Unique
	private void assignPartFilter (ItemStack part, ItemStack filter) {
		ItemStack result = part.copy();
		
		StatClockPartType partType = part.get(StatClockPartType.PART_TYPE_COMPONENT);
		StatClockFilterContent filterContent = filter.get(StatClockFilterContent.FILTER_COMPONENT);
		
		//Can't handle a part without knowing its type
		if (partType == null || filterContent == null) return;
		partType = partType.clonePartType();
		filterContent = filterContent.cloneFilterContent();
		
		//Only add to one without a filter
		//TODO decide if overwriting filters should be allowed
		//if (partType.filter.getType() != StatClockFilterType.NONE) return;
		
		//Don't add a blank filter
		if (filterContent.getType() == StatClockFilterType.NONE) return;
		
		//Assign filter only if valid for the part type
		PartTypeInfo info = partType.getInfo();
		
		//Make sure the part can have that filter
		if (!info.allowFilter(filterContent)) return;
		
		partType = partType.clonePartType();
		filterContent = filterContent.cloneFilterContent();
		
		partType.setFilter(filterContent);
		
		result.set(StatClockPartType.PART_TYPE_COMPONENT, partType);
		
		boolean renaming = renaming(part, result);
		
		this.resultSlots.setItem(0, result);
		this.cost.set(renaming ? 2 : 1);
	}
	
	@Unique
	private void removeStatClock (ItemStack tool) {
		StatClockContent statClock = tool.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (statClock == null) return;
		
		ItemStack result = new ItemStack(StatClocksMod.STAT_CLOCK);
		
		result.set(StatClockContent.STAT_CLOCK_COMPONENT, statClock.cloneClockContent());
		
		this.resultSlots.setItem(0, result);
		this.cost.set(1);
	}
	
	@Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
	private void onTake (Player player, ItemStack itemStack, CallbackInfo ci) {
		//If removing a stat clock that was separated from an item, run our own code
		//to handle the event
		if (this.inputSlots.getItem(1).is(StatClocksMod.STAT_CLOCK_REMOVER)) {
			onTakeClockRemoval();
			
			ci.cancel();
		}
	}
	
	@Unique
	private void onTakeClockRemoval () {
		//Remove levels
		if (!player.hasInfiniteMaterials()) {
			player.giveExperienceLevels(-this.cost.get());
		}
		
		//Clear stat clock from base item
		
		//TODO decide if remover tool should be consumed, damaged or reusable
		//this.inputSlots.setItem(1, ItemStack.EMPTY);
		
		ItemStack tool = this.inputSlots.getItem(0);
		
		//Remove the clock
		tool.remove(StatClockContent.STAT_CLOCK_COMPONENT);
		
		//Reset cost value
		this.cost.set(0);
		
		//Rename the item
		if (player instanceof ServerPlayer serverPlayer
			&& !StringUtil.isBlank(this.itemName)
			&& !this.inputSlots.getItem(0).getHoverName().getString().equals(this.itemName)) {
			serverPlayer.getTextFilter().processStreamMessage(this.itemName);
		}
		
		//Damage anvil
		this.access.execute((level, blockPos) -> {
			BlockState blockState = level.getBlockState(blockPos);
			if (!player.hasInfiniteMaterials() && blockState.is(BlockTags.ANVIL) && player.getRandom().nextFloat() < 0.12F) {
				BlockState blockState2 = AnvilBlock.damage(blockState);
				if (blockState2 == null) {
					level.removeBlock(blockPos, false);
					level.levelEvent(1029, blockPos, 0);
				} else {
					level.setBlock(blockPos, blockState2, 2);
					level.levelEvent(1030, blockPos, 0);
				}
			} else {
				level.levelEvent(1030, blockPos, 0);
			}
		});
		
	}
	
	@Unique
	private boolean renaming (ItemStack input, ItemStack output) {
		
		//Check if renaming the item
		if (this.itemName != null && !StringUtil.isBlank(this.itemName)) {
			
			boolean difference = !this.itemName.equals(input.getHoverName().getString());
			
			if (difference) {
				output.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName));
			}
			
			return difference;
		}
		
		//If the field is blank, remove the custom id
		if (input.has(DataComponents.CUSTOM_NAME)) {
			output.remove(DataComponents.CUSTOM_NAME);
			
			return true;
		}
		
		return false;
	}
	
}