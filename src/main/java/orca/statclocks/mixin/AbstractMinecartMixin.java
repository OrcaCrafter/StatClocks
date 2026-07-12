package orca.statclocks.mixin;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import orca.statclocks.components.StatClockContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends VehicleEntity {
	
	@Unique
	StatClockContent statClock;
	
	public AbstractMinecartMixin (EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	
	public void applyImplicitComponents (DataComponentGetter dataComponentGetter) {
		super.applyImplicitComponents(dataComponentGetter);
		this.applyImplicitComponentIfPresent(dataComponentGetter, StatClockContent.STAT_CLOCK_COMPONENT);
	}
	
	public <T> boolean applyImplicitComponent (DataComponentType<T> dataComponentType, T object) {
		if (dataComponentType == StatClockContent.STAT_CLOCK_COMPONENT) {
			
			statClock = ((StatClockContent)object).cloneClockContent();
			
			return true;
		}
		
		return super.applyImplicitComponent(dataComponentType, object);
	}
	
	public <T> T get(DataComponentType<? extends T> dataComponentType) {
		if (dataComponentType == StatClockContent.STAT_CLOCK_COMPONENT) {
			return castComponentValue((DataComponentType<T>)StatClockContent.STAT_CLOCK_COMPONENT, statClock);
		}
		
		return super.get(dataComponentType);
	}
	
	@Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
	protected void addAdditionalSaveData (ValueOutput output, CallbackInfo ci) {
		if (statClock != null) {
			output.storeNullable("stat_clock", StatClockContent.CODEC, statClock);
		}
	}
	
	@Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
	protected void readAdditionalSaveData (ValueInput input, CallbackInfo ci) {
		statClock = input.read("stat_clock", StatClockContent.CODEC).orElse(null);
	}
}
