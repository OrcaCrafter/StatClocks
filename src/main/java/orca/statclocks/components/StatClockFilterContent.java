package orca.statclocks.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import orca.statclocks.StatClocksMod;
import orca.statclocks.ToolTipComponent;

import java.util.List;
import java.util.Optional;


public class StatClockFilterContent implements ToolTipComponent {
	
	public static final Codec<StatClockFilterContent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				ExtraCodecs.NON_EMPTY_STRING.fieldOf("type").forGetter(StatClockFilterContent::getTypeString),
				ExtraCodecs.NON_EMPTY_STRING.fieldOf("filter").forGetter(StatClockFilterContent::getFilter)
			)
			.apply(instance, StatClockFilterContent::new)
	);
	
	public static final StreamCodec<ByteBuf, StatClockFilterContent> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, StatClockFilterContent::getTypeString,
		ByteBufCodecs.STRING_UTF8, StatClockFilterContent::getFilter,
		StatClockFilterContent::new
	);
	
	public static final DataComponentType<StatClockFilterContent> FILTER_COMPONENT = StatClocksMod.registerComponent(
		"stat_clock_filter",
		builder -> builder.persistent(CODEC).networkSynchronized(STREAM_CODEC)
	);
	
	public static StatClockFilterContent makeEmpty () {
		return new StatClockFilterContent();
	}
	
	public static void init () {
	}
	
	
	StatClockFilterType type;
	String filter;
	
	public StatClockFilterContent () {
		type = StatClockFilterType.NONE;
		filter = "none";
	}
	
	public StatClockFilterContent (String type, String filter) {
		this.type = StatClockFilterType.getFrom(type);
		this.filter = filter;
	}
	
	public StatClockFilterContent (StatClockFilterType type, String filter) {
		this.type = type;
		this.filter = filter;
	}
	
	public String getTypeString () {
		return type.getName();
	}
	
	public String getFilter () {
		return filter;
	}
	
	public void Set (Item item) {
		assert item != null;
		
		type = StatClockFilterType.ITEM;
		filter = item.toString();
	}
	
	public void Set (Block block) {
		assert block != null;
		
		type = StatClockFilterType.BLOCK;
		//Can't use toString() because of unwanted formatting
		filter = BuiltInRegistries.BLOCK.wrapAsHolder(block).getRegisteredName();
	}
	
	public void Set (EntityType<?> entity) {
		assert entity != null;
		
		type = StatClockFilterType.ENTITY;
		filter = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity).getRegisteredName();
		
	}
	
	public Item getItem () {
		Optional<Holder.Reference<Item>> check = BuiltInRegistries.ITEM.get(Identifier.read(filter).getOrThrow());
		
		return check.map(Holder.Reference::value).orElse(null);
		
	}
	
	public Block getBlock () {
		Optional<Holder.Reference<Block>> check = BuiltInRegistries.BLOCK.get(Identifier.read(filter).getOrThrow());
		
		return check.map(Holder.Reference::value).orElse(null);
	}
	
	public EntityType<?> getEntity () {
		Optional<Holder.Reference<EntityType<?>>> check = BuiltInRegistries.ENTITY_TYPE.get(Identifier.read(filter).getOrThrow());
		
		return check.map(Holder.Reference::value).orElse(null);
	}
	
	public boolean itemPassesFilter (Item filterItem) {
		return  (filterItem == getItem());
	}
	
	public boolean blockPassesFilter (Block filterBlock) {
		return  (filterBlock == getBlock());
	}
	
	public boolean entityPassesFilter (EntityType<?> filterEntity) {
		StatClocksMod.LOGGER.info("Entity: {} pass filter: {}? {}", filterEntity, getEntity(), filterEntity == getEntity());
		return  (filterEntity == getEntity());
	}
	
	public Component getItemTranslation () {
		Item item = getItem();
		if (item == null) return Component.translatable("stat-clocks.tooltip.filter_error");
		return item.getName();
	}
	
	public Component getBlockTranslation () {
		Block block = getBlock();
		if (block == null) return Component.translatable("stat-clocks.tooltip.filter_error");
		return block.getName();
	}
	
	public Component getEntityTranslation () {
		EntityType<?> entity = getEntity();
		if (entity == null) return Component.translatable("stat-clocks.tooltip.filter_error");
		return entity.getDescription();
	}
	
	public StatClockFilterType getType () {
		return type;
	}
	
	public Component getFilterTranslation () {
		
		return switch (type) {
			case ITEM -> getItemTranslation();
			case BLOCK -> getBlockTranslation();
			case ENTITY -> getEntityTranslation();
			default -> Component.empty();
		};
	}
	
	public StatClockFilterContent cloneFilterContent () {
		return new StatClockFilterContent(type.getName(), filter);
	}
	
	
	@Override
	public boolean equals (Object obj) {
		if (!(obj instanceof StatClockFilterContent compare)) return false;
		
		if (!(compare.type == this.type)) return false;
		
		return compare.filter.equals((this.filter));
	}
	
	@Override
	public void addToTooltip (ItemStack stack, Item.TooltipContext context, TooltipFlag typeFlag, List<Component> list) {
		if (type == StatClockFilterType.NONE) {
			list.add(Component.translatable("stat-clocks.tooltip.filter_use_info"));
		} else {
			list.add(Component.translatable("stat-clocks.tooltip.filter_type:" + type.getName()));
			list.add(CommonComponents.space().append(getFilterTranslation()));
		}
		
	}
}
