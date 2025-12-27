package orca.statclocks;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import orca.statclocks.components.*;
import orca.statclocks.lists.PartTypeInfo;

import java.util.Collection;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class StatClockCommand {
	private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(
		object -> Component.translatableEscape("commands.addstatclock.failed.entity", object)
	);
	private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType(
		object -> Component.translatableEscape("commands.addstatclock.failed.itemless", object)
	);
	private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(
		object -> Component.translatableEscape("commands.addstatclock.failed.incompatible", object)
	);
	private static final Dynamic3CommandExceptionType ERROR_INVALID_FILTER = new Dynamic3CommandExceptionType(
		(a, b, c) -> {
			return Component.translatableEscape("commands.addstatclock.failed.invalid_filter", a, b, c);
		}
	);
	private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(Component.translatable("commands.addstatclock.failed"));
	
	
	public static void register (CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
		//Add stat clock command
		
		commandDispatcher.register(
			literal("addstatclock")
				.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(
					literal("default")
						.then(
							argument("targets", EntityArgument.entities())
								.executes(
									context -> addStatClockParts(
										context.getSource(), EntityArgument.getEntities(context, "targets"), false
									)
								)
						)
						.executes(
							context -> addStatClockParts(
								context.getSource(), ImmutableList.of(context.getSource().getEntityOrException()), true
							)
						)
				)
				.then(
					literal("all")
						.then(
							argument("targets", EntityArgument.entities())
								.executes(
									context -> addStatClockParts(
										context.getSource(), EntityArgument.getEntities(context, "targets"), true
									)
								)
						)
						.executes(
							context -> addStatClockParts(
								context.getSource(), ImmutableList.of(context.getSource().getEntityOrException()), true
							)
						)
				)
				.then(
					literal("single")
						.then(
							argument("targets", EntityArgument.entities())
								.then(
									argument("part", ResourceArgument.resource(commandBuildContext, StatClocksMod.PART_TYPES))
										.executes(
											context -> addPartToStatClock(
												context.getSource(),
												EntityArgument.getEntities(context,"targets"),
												ResourceArgument.getResource(context, "part", StatClocksMod.PART_TYPES).value(),
												StatClockFilterType.NONE,
												null
											)
										)
										.then(
											literal("filter")
												.then(
													literal("item")
														.then(
															argument("filter_item", ResourceArgument.resource(commandBuildContext, Registries.ITEM))
																.executes(
																	context -> addPartToStatClockItem(
																		context.getSource(),
																		EntityArgument.getEntities(context, "targets"),
																		ResourceArgument.getResource(context, "part", StatClocksMod.PART_TYPES).value(),
																		ResourceArgument.getResource(context, "filter_item", Registries.ITEM).value()
																	)
																)
														)
												)
												.then(
													literal("block")
														.then(
															argument("filter_block", ResourceArgument.resource(commandBuildContext, Registries.BLOCK))
																.executes(
																	context -> addPartToStatClockBlock(
																		context.getSource(),
																		EntityArgument.getEntities(context, "targets"),
																		ResourceArgument.getResource(context, "part", StatClocksMod.PART_TYPES).value(),
																		ResourceArgument.getResource(context, "filter_block", Registries.BLOCK).value()
																	)
																)
														)
												)
												.then(
													literal("entity")
														.then(
															argument("filter_entity", ResourceArgument.resource(commandBuildContext, Registries.ENTITY_TYPE))
																.executes(
																	context -> addPartToStatClockEntity(
																		context.getSource(),
																		EntityArgument.getEntities(context, "targets"),
																		ResourceArgument.getResource(context, "part", StatClocksMod.PART_TYPES).value(),
																		ResourceArgument.getResource(context, "filter_entity", Registries.ENTITY_TYPE).value()
																	)
																)
														)
												)
										)
								)
						)
						.then(
							argument("part", ResourceArgument.resource(commandBuildContext, StatClocksMod.PART_TYPES))
								.executes(
									context -> addPartToStatClock(
										context.getSource(),
										ImmutableList.of(context.getSource().getEntityOrException()),
										ResourceArgument.getResource(context, "part", StatClocksMod.PART_TYPES).value(),
										StatClockFilterType.NONE,
										null
									)
								)
								.then(
									literal("filter")
										.then(
											literal("item")
												.then(
													argument("filter_item", ResourceArgument.resource(commandBuildContext, Registries.ITEM))
														.executes(
															context -> addPartToStatClockItem(
																context.getSource(),
																ImmutableList.of(context.getSource().getEntityOrException()),
																ResourceArgument.getResource(context, "part", StatClocksMod.PART_TYPES).value(),
																ResourceArgument.getResource(context, "filter_item", Registries.ITEM).value()
															)
														)
												)
										)
										.then(
											literal("block")
												.then(
													argument("filter_block", ResourceArgument.resource(commandBuildContext, Registries.BLOCK))
														.executes(
															context -> addPartToStatClockBlock(
																context.getSource(),
																ImmutableList.of(context.getSource().getEntityOrException()),
																ResourceArgument.getResource(context, "part", StatClocksMod.PART_TYPES).value(),
																ResourceArgument.getResource(context, "filter_block", Registries.BLOCK).value()
															)
														)
												)
										)
										.then(
											literal("entity")
												.then(
													argument("filter_entity", ResourceArgument.resource(commandBuildContext, Registries.ENTITY_TYPE))
														.executes(
															context -> addPartToStatClockEntity(
																context.getSource(),
																ImmutableList.of(context.getSource().getEntityOrException()),
																ResourceArgument.getResource(context, "part", StatClocksMod.PART_TYPES).value(),
																ResourceArgument.getResource(context, "filter_entity", Registries.ENTITY_TYPE).value()
															)
														)
												)
										)
								)
						)
				)
		);
	}
	
	
	private static int addStatClockParts (CommandSourceStack commandSourceStack, Collection<? extends Entity> targets, boolean all) throws CommandSyntaxException {
		
		int appliedTo = 0;
		
		for (Entity entity : targets) {
			if (entity instanceof LivingEntity livingEntity) {
				ItemStack itemStack = livingEntity.getMainHandItem();
				
				if (!itemStack.isEmpty()) {
					
					StatClockContent add = StatClockContent.ItemStatClock(itemStack, all);
					
					if (add == null) throw ERROR_INCOMPATIBLE.create(itemStack.getHoverName().getString());
					
					StatClockContent.mergeClockInfo(itemStack, add);
					
					appliedTo ++;
				} else if (targets.size() == 1) {
					throw ERROR_NO_ITEM.create(livingEntity.getName().getString());
				}
			} else if (targets.size() == 1) {
				throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
			}
		}
		
		if (appliedTo == 0) {
			throw ERROR_NOTHING_HAPPENED.create();
		} else {
			if (targets.size() == 1) {
				commandSourceStack.sendSuccess(
					() -> Component.translatable(
						"commands.addstatclock.success.single", (targets.iterator().next()).getDisplayName()
					),
					true
				);
			} else {
				commandSourceStack.sendSuccess(
					() -> Component.translatable("commands.addstatclock.success.multiple", targets.size()), true
				);
			}
			
			return appliedTo;
		}
	}
	
	
	private static int addPartToStatClockItem (CommandSourceStack commandSourceStack, Collection<? extends Entity> targets, StatClockPartType type, Item item) throws CommandSyntaxException {
		return addPartToStatClock(commandSourceStack, targets, type, StatClockFilterType.ITEM, BuiltInRegistries.ITEM.wrapAsHolder(item).getRegisteredName());
	}
	
	private static int addPartToStatClockBlock (CommandSourceStack commandSourceStack, Collection<? extends Entity> targets, StatClockPartType type, Block block) throws CommandSyntaxException {
		return addPartToStatClock(commandSourceStack, targets, type, StatClockFilterType.BLOCK, BuiltInRegistries.BLOCK.wrapAsHolder(block).getRegisteredName());
	}
	
	private static int addPartToStatClockEntity (CommandSourceStack commandSourceStack, Collection<? extends Entity> targets, StatClockPartType type, EntityType<?> entityType) throws CommandSyntaxException {
		return addPartToStatClock(commandSourceStack, targets, type, StatClockFilterType.ENTITY, BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType).getRegisteredName());
	}
	
	private static int addPartToStatClock (CommandSourceStack commandSourceStack, Collection<? extends Entity> targets, StatClockPartType type, StatClockFilterType filterType, String filter) throws CommandSyntaxException {
		//Avoid modifying all instance of this part type
		type = type.clonePartType();
		
		PartTypeInfo typeInfo = type.getInfo();
		
		if (filterType != StatClockFilterType.NONE) {
			StatClockFilterContent filterContent = new StatClockFilterContent(filterType, filter);
			
			if (!typeInfo.allowFilter(filterContent)) throw ERROR_INVALID_FILTER.create(filterType.getName(), filter, typeInfo.getName());
			
			type.setFilter(filterContent);
		}
		
		StatClockPartContent addPart = new StatClockPartContent(type);
		
		int appliedTo = 0;
		
		for (Entity entity : targets) {
			if (entity instanceof LivingEntity livingEntity) {
				ItemStack itemStack = livingEntity.getMainHandItem();
				
				if (!itemStack.isEmpty()) {
					
					StatClockContent statClock = itemStack.get(StatClockContent.STAT_CLOCK_COMPONENT);
					
					if (statClock == null) throw ERROR_INCOMPATIBLE.create(itemStack.getHoverName().getString());
					
					if (!statClock.canAddPart(type, itemStack)) throw ERROR_INCOMPATIBLE.create(itemStack.getHoverName().getString());
					
					statClock.addPart(addPart.clonePartContent());
					
					appliedTo ++;
				} else if (targets.size() == 1) {
					throw ERROR_NO_ITEM.create(livingEntity.getName().getString());
				}
			} else if (targets.size() == 1) {
				throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
			}
		}
		
		if (appliedTo == 0) {
			throw ERROR_NOTHING_HAPPENED.create();
		} else {
			if (targets.size() == 1) {
				commandSourceStack.sendSuccess(
					() -> Component.translatable(
						"commands.addstatclock.success.single", (targets.iterator().next()).getDisplayName()
					),
					true
				);
			} else {
				commandSourceStack.sendSuccess(
					() -> Component.translatable("commands.addstatclock.success.multiple", targets.size()), true
				);
			}
			
			return appliedTo;
		}
	}
}
