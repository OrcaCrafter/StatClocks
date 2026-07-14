package orca.statclocks.mixin.listener;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import orca.statclocks.listeners.MiscListeners;
import orca.statclocks.lists.StatClockPartTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.UUID;

@Mixin(SpyglassItem.class)
public abstract class SpyglassItemMixin extends Item {
	
	@Unique
	HashSet<UUID> lookedAtInCurrentUse = new HashSet<>();
	
	public SpyglassItemMixin (Properties properties) {
		super(properties);
	}
	
	@Inject(method = "use", at = @At("RETURN"))
	public void use (Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
		
		ItemStack tool = StatClockPartTypes.getPriorityHandItem(player, (item) -> item.getItem() instanceof SpyglassItem);
		
		MiscListeners.SPYGLASS_USE_LISTENER.applyToParts(player, tool, null, 1);
		
		lookedAtInCurrentUse.clear();
	}
	
	
	
	public void onUseTick (Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
		if (livingEntity instanceof ServerPlayer serverPlayer) {
			checkLookingAt(serverPlayer, itemStack);
		}
	}
	
	@Unique
	private void checkLookingAt (ServerPlayer player, ItemStack tool) {
		Vec3 rayStart = player.getEyePosition();
		Vec3 viewVector = player.getViewVector(1.0f);
		Vec3 rayEnd = rayStart.add(viewVector.x * 100.0f, viewVector.y * 100.0f, viewVector.z * 100.0f);
		
		EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
			player.level(), player, rayStart, rayEnd, new AABB(rayStart, rayEnd).inflate(1.0), entityx -> !entityx.isSpectator(), 0.0F
		);
		
		if (entityHitResult == null || entityHitResult.getType() != HitResult.Type.ENTITY) return;
		
		Entity lookingAtEntity = entityHitResult.getEntity();
		
		if (!player.hasLineOfSight(lookingAtEntity)) return;
		
		UUID uuid = lookingAtEntity.getUUID();
		
		if (lookedAtInCurrentUse.contains(uuid)) return;
		
		lookedAtInCurrentUse.add(uuid);
		
		MiscListeners.SPYED_ON_LISTENER.applyToParts(player, tool, lookingAtEntity, 1);
	}
}
