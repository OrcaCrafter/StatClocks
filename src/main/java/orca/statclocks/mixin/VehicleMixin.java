package orca.statclocks.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import orca.statclocks.components.StatClockContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VehicleEntity.class)
public abstract class VehicleMixin extends Entity {
	
	public VehicleMixin (EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	
	//Replace dropping logic with our own to add stat clock data to vehicles
	@Inject(method = "destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/Item;)V", at = @At("HEAD"), cancellable = true)
	public void destroy (ServerLevel serverLevel, Item item, CallbackInfo ci) {
		
		VehicleEntity thisVehicle = (VehicleEntity)(Object)this;
		
		StatClockContent statClock = thisVehicle.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (statClock == null) return;
		
		//Replace with our code
		ci.cancel();
		
		this.kill(serverLevel);
		if (serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)) {
			ItemStack itemStack = new ItemStack(item);
			itemStack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
			
			//Add stat clock data to dropped item
			itemStack.set(StatClockContent.STAT_CLOCK_COMPONENT, statClock);
			
			this.spawnAtLocation(serverLevel, itemStack);
		}
	}
	
}