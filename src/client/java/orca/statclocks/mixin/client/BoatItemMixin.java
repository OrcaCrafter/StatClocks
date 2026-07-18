package orca.statclocks.mixin.client;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import orca.statclocks.StatClockScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BoatItem.class)
public abstract class BoatItemMixin extends Item implements StatClockScreen.VehicleItemAccessor {
	
	@Shadow
	@Final
	private EntityType<? extends AbstractBoat> entityType;
	
	public BoatItemMixin (Properties properties) {
		super(properties);
	}
	
	@Unique
	public EntityType<?> statClocks$getVehicleType () {
		return this.entityType;
	}
}
