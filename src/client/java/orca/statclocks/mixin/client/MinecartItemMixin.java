package orca.statclocks.mixin.client;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MinecartItem;
import orca.statclocks.StatClockScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecartItem.class)
public abstract class MinecartItemMixin extends Item implements StatClockScreen.VehicleItemAccessor {
	
	@Shadow
	@Final
	private EntityType<? extends AbstractMinecart> type;
	
	public MinecartItemMixin (Properties properties) {
		super(properties);
	}
	
	@Unique
	public EntityType<?> statClocks$getVehicleType () {
		return this.type;
	}
	
}
