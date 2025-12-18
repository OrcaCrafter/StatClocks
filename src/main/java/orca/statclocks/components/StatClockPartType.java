package orca.statclocks.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import orca.statclocks.StatClocksMod;
import orca.statclocks.ToolTipComponent;
import orca.statclocks.lists.PartTypeInfo;
import orca.statclocks.lists.StatClockPartTypes;
import oshi.util.tuples.Pair;

import java.util.List;

public class StatClockPartType implements ToolTipComponent {
	
	public static final Codec<StatClockPartType> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				ExtraCodecs.NON_EMPTY_STRING.fieldOf("id").forGetter(StatClockPartType::getIdName),
				StatClockFilterContent.CODEC.fieldOf("filter").forGetter(StatClockPartType::getFilter)
			)
			.apply(instance, StatClockPartType::new)
	);
	
	public static final StreamCodec<ByteBuf, StatClockPartType> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, StatClockPartType::getIdName,
		StatClockFilterContent.STREAM_CODEC, StatClockPartType::getFilter,
		StatClockPartType::new
	);
	
	public static final DataComponentType<StatClockPartType> PART_TYPE_COMPONENT = StatClocksMod.registerComponent(
		"stat_clock_part_type",
		builder -> builder.persistent(CODEC).networkSynchronized(STREAM_CODEC)
	);
	
	//Non static
	
	private final Identifier id;
	public StatClockFilterContent filter;
	
	public StatClockPartType (Identifier id) {
		this.id = id;
		filter = StatClockFilterContent.makeEmpty();
	}
	
	public StatClockPartType (String name) {
		this.id = Identifier.read(name).getOrThrow();
		filter = StatClockFilterContent.makeEmpty();
	}
	
	public StatClockPartType (String name, StatClockFilterContent filter) {
		this.id = Identifier.read(name).getOrThrow();
		this.filter = filter;
	}
	
	public String getIdName () {
		return id.toString();
	}
	
	public Identifier getId () {
		return id;
	}
	
	public StatClockFilterContent getFilter () {
		return filter;
	}
	
	public void setFilter (StatClockFilterContent set) {
		filter = set;
	}
	
	public ItemStack GetOutputItem () {
		ItemStack ret = new ItemStack(StatClocksMod.STAT_CLOCK_PART);
		
		ret.set(StatClockPartType.PART_TYPE_COMPONENT, this);
		
		return ret;
	}
	
	public StatClockPartType clonePartType () {
		return new StatClockPartType(id.toString(), filter.cloneFilterContent());
	}
	
	public boolean sameType (StatClockPartType check) {
		return this.id.equals(check.id);
	}
	
	@Override
	public boolean equals (Object obj) {
		if (!(obj instanceof StatClockPartType compare)) return false;
		
		if (!id.equals(compare.id)) return false;
		
		return filter.equals(compare.filter);
	}
	
	public MutableComponent getComponent () {
		if (filter.getType() == StatClockFilterType.NONE) {
			return Component.translatable("stat-clocks.tooltip.part_type:" + id);
		} else {
			return Component.translatable("stat-clocks.tooltip.part_type_filtered:" + id, filter.getFilterTranslation().getString());
		}
	}
	
	public PartTypeInfo getInfo () {
		return StatClockPartTypes.STAT_PART_TYPES.get(this.id).getB();
	}
	
	@Override
	public void addToTooltip (ItemStack stack, Item.TooltipContext context, TooltipFlag type, List<Component> list) {
		list.add(getComponent());
		
		if (filter.getType() != StatClockFilterType.NONE) return;
		
		Pair<StatClockPartType, PartTypeInfo> pair = StatClockPartTypes.STAT_PART_TYPES.get(this.id);
		
		if (pair == null) return;
		StatClockFilterType filterType = pair.getB().getFilterType();
		
		if (filterType == StatClockFilterType.NONE) return;
		
		list.add(Component.empty().append(Component.translatable("stat-clocks.tooltip.part_type_filter_tip_1", filterType.getName())).withStyle(ChatFormatting.GRAY));
		list.add(Component.empty().append(Component.translatable("stat-clocks.tooltip.part_type_filter_tip_2")).withStyle(ChatFormatting.GRAY));
	}
}
