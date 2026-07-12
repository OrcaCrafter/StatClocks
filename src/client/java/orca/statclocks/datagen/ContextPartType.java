package orca.statclocks.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.StatClocksMod;
import orca.statclocks.components.StatClockPartType;
import orca.statclocks.lists.StatClockPartTypes;
import org.jspecify.annotations.Nullable;

public record ContextPartType() implements SelectItemModelProperty<ResourceKey<StatClockPartType>> {
	
	public static final Codec<ResourceKey<StatClockPartType>> VALUE_CODEC = ResourceKey.codec(StatClocksMod.PART_TYPES);
	public static final SelectItemModelProperty.Type<ContextPartType, ResourceKey<StatClockPartType>> TYPE = SelectItemModelProperty.Type.create(
		MapCodec.unit(new ContextPartType()), VALUE_CODEC
	);
	
	@Override
	public @Nullable ResourceKey<StatClockPartType> get (ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
		if (!itemStack.is(StatClocksMod.STAT_CLOCK_PART)) return null;
		
		StatClockPartType type = itemStack.get(StatClockPartType.PART_TYPE_COMPONENT);
		
		return key(type == null ? StatClockPartTypes.EMPTY : type);
	}
	
	@Override
	public Codec<ResourceKey<StatClockPartType>> valueCodec () {
		return VALUE_CODEC;
	}
	
	@Override
	public SelectItemModelProperty.Type<ContextPartType, ResourceKey<StatClockPartType>> type() {
		return TYPE;
	}
	
	private static ResourceKey<StatClockPartType> key (StatClockPartType type) {
		return ResourceKey.create(StatClocksMod.PART_TYPES, type.getId());
	}
}
