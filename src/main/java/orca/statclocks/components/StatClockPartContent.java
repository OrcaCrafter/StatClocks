package orca.statclocks.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.StatClocksMod;
import orca.statclocks.lists.PartTypeInfo;
import orca.statclocks.lists.StatClockPartTypes;

import java.awt.*;

public class StatClockPartContent {
	
	public static final Codec<StatClockPartContent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				StatClockPartType.CODEC.fieldOf("type").forGetter(StatClockPartContent::getType),
				ExtraCodecs.NON_NEGATIVE_INT.fieldOf("count").forGetter(StatClockPartContent::getCount)
			)
			.apply(instance, StatClockPartContent::new)
	
	);
	
	public static final StreamCodec<ByteBuf, StatClockPartContent> STREAM_CODEC = StreamCodec.composite(
		StatClockPartType.STREAM_CODEC, StatClockPartContent::getType,
		ByteBufCodecs.VAR_INT, StatClockPartContent::getCount,
		StatClockPartContent::new
	);
	
	public static final DataComponentType<StatClockPartContent> PART_COMPONENT = StatClocksMod.registerComponent(
		"stat_clock_part",
		builder -> builder.persistent(CODEC).networkSynchronized(STREAM_CODEC)
	);
	
	//Probe to register NBT components
	public static void init () {
	}
	
	private final StatClockPartType type;
	private int count;
	
	public StatClockPartContent (StatClockPartType type) {
		this(type, 0);
	}
	
	public StatClockPartContent (StatClockPartType type, int count) {
		this.type = type;
		this.count = count;
	}
	
	public StatClockPartType getType () {
		return type;
	}
	
	public int getCount () {
		return count;
	}
	
	public void incrementCount () {
		incrementCount(1);
	}
	
	public void incrementCount (int amount) {
		count += amount;
	}
	
	public void setCount (int count) {
		this.count = count;
	}
	
	public StatClockPartContent clonePartContent () {
		return new StatClockPartContent(type.clonePartType(), count);
	}
	
	public MutableComponent getComponent (ItemStack item, StatClockContent statClock) {
		
		if (StatClockPartTypes.STAT_PART_TYPES.containsKey(type.getId())) {
			PartTypeInfo info = StatClockPartTypes.STAT_PART_TYPES.get(type.getId()).getB();
			
			return info.applyFormatting(type.getComponent().append(": ").append(info.getFormatted(count)), item, statClock, this);
		}
		
		return Component.translatable("stat-clocks.tooltip.part_type_error", type.getId().toString()).withColor(Color.RED.getRGB());
	}
}
