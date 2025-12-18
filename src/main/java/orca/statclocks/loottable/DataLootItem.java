package orca.statclocks.loottable;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.function.Consumer;

public class DataLootItem extends LootPoolSingletonContainer {
	
	public static <T> LootPoolSingletonContainer.Builder<?> dataBuilder (Item item, DataComponentType<T> componentType, T component) {
		ItemStack stack = new ItemStack(item);
		stack.set(componentType, component);
		
		return LootItem.simpleBuilder((i, j, list, list2) -> new DataLootItem(stack, i, j, list, list2));
	}
	
	ItemStack stack;
	
	protected DataLootItem (ItemStack stack, int i, int j, List<LootItemCondition> list, List<LootItemFunction> list2) {
		super(i, j, list, list2);
		this.stack = stack;
	}
	
	@Override
	public LootPoolEntryType getType () {
		return LootPoolEntries.ITEM;
	}
	
	@Override
	protected void createItemStack (Consumer<ItemStack> consumer, LootContext lootContext) {
		consumer.accept(stack);
	}
}
