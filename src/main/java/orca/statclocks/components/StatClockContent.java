package orca.statclocks.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.impl.recipe.ingredient.builtin.ComponentsIngredient;
import net.minecraft.advancements.criterion.ItemDurabilityTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.effects.ChangeItemDamage;
import orca.statclocks.StatClocksMod;
import orca.statclocks.ToolTipComponent;
import orca.statclocks.lists.StatClockPartMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StatClockContent implements ToolTipComponent {
	
	public static void init () {}
	
	public static final Codec<StatClockContent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				ExtraCodecs.NON_EMPTY_STRING.fieldOf("tool_type").forGetter(StatClockContent::getToolType),
				StatClockPartContent.CODEC.listOf().fieldOf("part_types").forGetter(StatClockContent::getParts)
			)
			.apply(instance, StatClockContent::new)
	);
	
	public static final StreamCodec<ByteBuf, StatClockContent> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, StatClockContent::getToolType,
		StatClockPartContent.STREAM_CODEC.apply(ByteBufCodecs.list()), StatClockContent::getParts,
		StatClockContent::new
	);
	
	public static final DataComponentType<StatClockContent> STAT_CLOCK_COMPONENT = StatClocksMod.registerComponent(
		"stat_clock_content",
		builder -> builder.persistent(CODEC).networkSynchronized(STREAM_CODEC)
	);
	
	public static StatClockContent DefaultStatClock (ItemStack item) {
		return ItemStatClock(item, false);
	}
	
	public static StatClockContent ItemStatClock (ItemStack item, boolean all) {
		StatClockPartMapper.MapRule rule = StatClockPartMapper.GetItemMapping(item.getItem());
		
		if (rule.isEmpty()) return null;
		
		
		if (all) {
			StatClockPartType[] defaultParts = rule.defaultPartsArray();
			StatClockPartType[] allowedParts = rule.allowedPartsArray();
			
			StatClockPartType[] ret = new StatClockPartType[defaultParts.length + allowedParts.length];
			
			System.arraycopy(defaultParts, 0, ret, 0, defaultParts.length);
			System.arraycopy(allowedParts, 0, ret, defaultParts.length, allowedParts.length);
		
			return new StatClockContent(item.getItem().toString(), ret);
		}
		
		return new StatClockContent(item.getItem().toString(), rule.defaultPartsArray());
		
	}
	
	
	public static void mergeClockInfo (ItemStack stack, StatClockContent merging) {
		StatClockContent existing = stack.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (existing == null) {
			stack.set(StatClockContent.STAT_CLOCK_COMPONENT, merging);
			return;
		}
		
		for (StatClockPartContent existingPart : existing.parts) {
			
			boolean newPart = true;
			
			//Check for an existing version of this part
			for (StatClockPartContent mergePart : merging.parts) {
				
				//If an existing part has the same type and filter, add the stats
				if (existingPart.getType().equals(mergePart.getType())) {
					
					mergePart.incrementCount(existingPart.getCount());
					newPart = false;
					
					break;
				}
			}
			
			if (newPart) {
				//If there is no existing version, add a new one
				merging.parts.add(existingPart);
			}
		}
		
		stack.set(StatClockContent.STAT_CLOCK_COMPONENT, merging);
	}
	
	String toolType;
	
	ArrayList<StatClockPartContent> parts;
	
	public StatClockContent (String toolType, StatClockPartContent... part) {
		this.toolType = toolType;
		
		this.parts = new ArrayList<>();
		this.parts.addAll(List.of(part));
	}
	
	public StatClockContent (String toolType, List<StatClockPartContent> parts) {
		this.toolType = toolType;
		
		this.parts = new ArrayList<>();
		this.parts.addAll(parts);
	}
	
	public StatClockContent (String toolType, StatClockPartType[] parts) {
		this.toolType = toolType;
		
		this.parts = new ArrayList<>();
		
		for (StatClockPartType part : parts) {
			this.parts.add(new StatClockPartContent(part));
		}
	}
	
	private String getToolType () {
		return toolType;
	}
	
	public void setParts (ArrayList<StatClockPartContent> parts) {
		this.parts = parts;
	}
	
	public ArrayList<StatClockPartContent> getParts () {
		return parts;
	}
	
	public void addPart (StatClockPartContent statClockPartContent) {
		parts.add(statClockPartContent);
	}
	
	public boolean canApplyToTool (ItemStack tool) {
	
		return (tool.getItem().toString().equals(toolType));
	}
	
	public boolean canAddPart (StatClockPartType partType, ItemStack tool) {
		//Avoid adding part to tool twice
		for (StatClockPartContent content : getParts()) {
			if (content.getType().equals(partType)) return false;
		}
		
		StatClockPartMapper.MapRule rule = StatClockPartMapper.GetItemMapping(tool.getItem());
		
		//Avoid adding disallowed part types
		boolean allowed = false;
		
		for (StatClockPartType allowedPart : rule.allowedPartsArray()) {
			if (partType.sameType(allowedPart)) {
				allowed = true;
				break;
			}
		}
		
		//Check default parts for filtered addition
		for (StatClockPartType allowedPart : rule.defaultPartsArray()) {
			if (partType.sameType(allowedPart)) {
				allowed = true;
				break;
			}
		}
		
		return allowed;
	}
	
	public StatClockPartContent getPart (StatClockPartType type) {
		
		for (StatClockPartContent part : parts) {
			if (part.getType().equals(type)) {
				return part;
			}
		}
		
		return null;
	}
	
	public StatClockContent cloneClockContent () {
		List<StatClockPartContent> partsCopy = new ArrayList<>();
		
		for (StatClockPartContent part : parts) {
			partsCopy.add(part.clonePartContent());
		}
		
		
		return new StatClockContent(toolType, partsCopy);
	}
	
	
	@Override
	public void addToTooltip (ItemStack stack, Item.TooltipContext context, TooltipFlag type, List<Component> list) {
		
		
		if (stack.is(StatClocksMod.STAT_CLOCK)) {
			Optional<Holder.Reference<Item>> toolTypeItemHolder = BuiltInRegistries.ITEM.get(Identifier.read(toolType).getOrThrow());
			
			toolTypeItemHolder.map(Holder.Reference::value).ifPresent(
				toolTypeItem ->
				list.add(Component.translatable("stat-clocks.tooltip.clock_tool_type", toolTypeItem.getName()))
			);
			
		}
		
		if (parts.isEmpty()) {
			
			list.add(Component.translatable("stat-clocks.tooltip.blank_clock"));
			
		} else {
			
			for (StatClockPartContent part : parts) {
				list.add(part.getComponent(stack, this));
			}
		}
	}
}
