package orca.statclocks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.StatClocksMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WanderingTrader.class)
public class WanderingTraderMixin {
	
	@WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
	boolean overwriteInteract (ItemStack instance, Item item, Operation<Boolean> original) {
		//Allows for setting villagers as a filter mob
		//This wrap target may break if https://bugs.mojang.com/browse/MC/issues/MC-215831 is patched
		return original.call(instance, item) || instance.is(StatClocksMod.STAT_CLOCK_FILTER);
	}
	
}
