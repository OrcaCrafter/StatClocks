package orca.statclocks.mixin.listener;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodOnAStickItem.class)
public abstract class FoodOnAStickItemMixin extends Item {
	
	@Final @Shadow private int consumeItemDamage;
	
	public FoodOnAStickItemMixin (Properties properties) {
		super(properties);
	}
	
	@WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndConvertOnBreak(ILnet/minecraft/world/level/ItemLike;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
	public ItemStack onFoodOnAStickUsedProper (ItemStack itemInHand, int damage, ItemLike replacement, LivingEntity entity, EquipmentSlot hand, Operation<ItemStack> original) {
		
		MiscListeners.FOOD_ON_A_STICK_LISTENER.applyToParts(itemInHand, entity.getVehicle(), 1);
		
		ItemStack ret = itemInHand.hurtAndConvertOnBreak(this.consumeItemDamage, Items.FISHING_ROD, entity, hand);
		
		//If the item on stick breaks, it becomes a fishing rod.
		//Typically, the fishing rod keeps the stat clock
		//NOTE if making parts drop when a tool is broke, this would need to be updated
		if (ret.is(Items.FISHING_ROD)) {
			ret.remove(StatClockContent.STAT_CLOCK_COMPONENT);
		}
		
		return ret;
	}
}
