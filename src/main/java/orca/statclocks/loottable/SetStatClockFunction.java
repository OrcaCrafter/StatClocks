package orca.statclocks.loottable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.components.StatClockPartContent;
import orca.statclocks.lists.StatClockPartTypes;

import java.util.List;

import static orca.statclocks.loottable.CustomLootRegistries.SET_STAT_CLOCK;

public class SetStatClockFunction extends LootItemConditionalFunction {
	public static final MapCodec<SetStatClockFunction> CODEC = RecordCodecBuilder.mapCodec(
		instance -> commonFields(instance)
			.and(
				instance.group(
					Codec.STRING.fieldOf("filter").forGetter(function -> function.filter)
				).and(
					Codec.STRING.fieldOf("filter_type").forGetter(function -> function.filterType.toString())
				)
			)
			.apply(instance, SetStatClockFunction::new)
	);

	public enum FilterType {
		NO_FILTER,
		DATA_COMPONENT,
		ITEM_TAG,
		ITEM
	}

	final String filter;
	final FilterType filterType;
	
	SetStatClockFunction (List<LootItemCondition> list, String filter, String filterType) {
		super(list);
		this.filter = filter;
		this.filterType = FilterType.valueOf(filterType);
	}
	
	@Override
	public LootItemFunctionType<SetStatClockFunction> getType () {
		return SET_STAT_CLOCK;
	}
	
	@Override
	public ItemStack run (ItemStack itemStack, LootContext lootContext) {
		
		itemStack = itemStack.copy();

		boolean passes = switch (filterType) {
			case DATA_COMPONENT -> {

				DataComponentType<?> type = BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(Identifier.parse(filter));

				if (type == null) yield false;

				yield itemStack.has(type);
			}
			case ITEM_TAG -> {
				TagKey<Item> itemTag = TagKey.create(Registries.ITEM, Identifier.parse(filter));

				yield itemStack.is(itemTag);
			}
			case ITEM -> {
				Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(filter));

				yield itemStack.is(item);
			}
			case NO_FILTER -> true;
		};

		if (!passes) return itemStack;
		
		StatClockContent content = StatClockContent.DefaultStatClock(itemStack);

		if (content == null) return itemStack;

		if (itemStack.isDamaged()) {
			content.incrementByDurability(itemStack);
		}

		itemStack.set(StatClockContent.STAT_CLOCK_COMPONENT, content);
		
		//TODO add extra stat clock
		
		return itemStack;
	}
	
	public static class Builder extends LootItemConditionalFunction.Builder<SetStatClockFunction.Builder> {
		
		final String filter;
		final FilterType filterType;

		public Builder (DataComponentType<?> componentType) {
			Identifier id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(componentType);

			assert id != null;

			filter = id.toString();
			filterType = FilterType.DATA_COMPONENT;
		}

		public Builder (TagKey<Item> itemTag) {
			this(itemTag.location(), FilterType.ITEM_TAG);
		}

		public Builder (Item item) {
			this(BuiltInRegistries.ITEM.getKey(item), FilterType.ITEM);
		}

		public Builder () {
			filter = "";
			filterType = FilterType.NO_FILTER;
		}

		public Builder (Identifier filter, FilterType filterType) {
			this.filter = filter.toString();
			this.filterType = filterType;
		}
		
		protected SetStatClockFunction.Builder getThis () {
			return this;
		}
		
		@Override
		public LootItemFunction build () {
			return new SetStatClockFunction(this.getConditions(), filter, filterType.toString());
		}
	}
}
