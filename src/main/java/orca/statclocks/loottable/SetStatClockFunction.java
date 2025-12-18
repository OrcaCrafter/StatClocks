package orca.statclocks.loottable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import orca.statclocks.components.StatClockContent;

import java.util.List;

import static orca.statclocks.loottable.CustomLootRegistries.SET_STAT_CLOCK;

public class SetStatClockFunction extends LootItemConditionalFunction {
	public static final MapCodec<SetStatClockFunction> CODEC = RecordCodecBuilder.mapCodec(
		instance -> commonFields(instance)
			.and(
				instance.group(
					Codec.STRING.fieldOf("filter").<SetStatClockFunction>forGetter(function -> function.filter)
				).t1()
			)
			.apply(instance, SetStatClockFunction::new)
	);
	
	String filter;
	
	SetStatClockFunction (List<LootItemCondition> list, String filter) {
		super(list);
		this.filter = filter;
	}
	
	@Override
	public LootItemFunctionType<SetStatClockFunction> getType () {
		return SET_STAT_CLOCK;
	}
	
	@Override
	public ItemStack run (ItemStack itemStack, LootContext lootContext) {
		
		itemStack = itemStack.copy();
		
		DataComponentType<?> type = BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(Identifier.parse(filter));
		
		if (type != null) {
			
			if (!itemStack.has(type)) return itemStack;
		
		}
		
		StatClockContent content = StatClockContent.DefaultStatClock(itemStack);
		
		itemStack.set(StatClockContent.STAT_CLOCK_COMPONENT, content);
		
		//TODO add extra stat clock
		
		return itemStack;
	}
	
	public static class Builder extends LootItemConditionalFunction.Builder<SetStatClockFunction.Builder> {
		
		String filter;
		
		public Builder (String filter) {
			this.filter = filter;
		}
		
		protected SetStatClockFunction.Builder getThis () {
			return this;
		}
		
		@Override
		public LootItemFunction build () {
			return new SetStatClockFunction(this.getConditions(), filter);
		}
	}
}
