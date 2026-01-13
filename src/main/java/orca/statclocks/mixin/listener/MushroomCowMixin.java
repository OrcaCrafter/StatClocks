package orca.statclocks.mixin.listener;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;


@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends AbstractCow {
	
	public MushroomCowMixin (EntityType<? extends AbstractCow> entityType, Level level) {
		super(entityType, level);
	}
	
	@Unique
	Player shearingPlayer;
	
	
	@Inject(method = "mobInteract", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/world/entity/animal/cow/MushroomCow;shear(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/sounds/SoundSource;Lnet/minecraft/world/item/ItemStack;)V")
	)
	void mobInteract (Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
		shearingPlayer = player;
	}
	
	@Override
	protected void dropFromShearingLootTable (
		ServerLevel level, ResourceKey<LootTable> tableKey, ItemStack shears, BiConsumer<ServerLevel, ItemStack> spawnItem
	) {
		
		if (shearingPlayer != null) {
			
			BiConsumer<ServerLevel, ItemStack> spawnAndCountItem = (serverLevel, loot) -> {
				MiscListeners.SHEARS_USE_LISTENER_ITEM.applyToParts(shearingPlayer, shears, loot, loot.getCount());
				
				spawnItem.accept(serverLevel, loot);
			};
			
			this.dropFromLootTable(
				level,
				tableKey,
				builder -> builder.withParameter(LootContextParams.ORIGIN, this.position())
					.withParameter(LootContextParams.THIS_ENTITY, this)
					.withParameter(LootContextParams.TOOL, shears)
					.create(LootContextParamSets.SHEARING),
				spawnAndCountItem
			);
			
			shearingPlayer = null;
		} else {
			this.dropFromLootTable(
				level,
				tableKey,
				builder -> builder.withParameter(LootContextParams.ORIGIN, this.position())
					.withParameter(LootContextParams.THIS_ENTITY, this)
					.withParameter(LootContextParams.TOOL, shears)
					.create(LootContextParamSets.SHEARING),
				spawnItem
			);
		}
	}
}
