package orca.statclocks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.components.StatClockPartContent;
import orca.statclocks.lists.StatClockPartTypes;
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
	@Inject(method = "destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/Item;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"))
	public void destroy (ServerLevel serverLevel, Item item, CallbackInfo ci, @Local ItemStack itemStack) {
		
		StatClockContent statClock = this.get(StatClockContent.STAT_CLOCK_COMPONENT);
		if (statClock == null) return;
		
		itemStack.set(StatClockContent.STAT_CLOCK_COMPONENT, statClock);
		
	}
	
	public void move (MoverType moverType, Vec3 vec3) {
		
		StatClockContent statClock = this.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (statClock != null) {
			
			StatClockPartContent content = statClock.getPart(StatClockPartTypes.VEHICLE_DISTANCE);
			
			if (content != null) {
				int distance = (int)Math.round(vec3.length() * 100);
				
				content.incrementCount(distance);
			}
		}
		
		
		super.move(moverType, vec3);
	}
	
	
	//TODO pick result
}