package orca.statclocks.mixin.listener;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartFurnace;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.components.StatClockPartContent;
import orca.statclocks.lists.StatClockPartTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecartFurnace.class)
public abstract class MinecartFurnaceMixin extends AbstractMinecart {
	
	
	protected MinecartFurnaceMixin (EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	
	@Inject(method = "addFuel", at = @At(value = "RETURN"))
	void isFuel (Vec3 vec3, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) return;
		
		StatClockContent statClock = get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (statClock == null) return;
		
		StatClockPartContent part = statClock.getPart(StatClockPartTypes.MINECART_FUELED);
		
		if (part == null) return;
		
		part.incrementCount(1);
	}
}
