package orca.statclocks.mixin.listener;

import net.minecraft.advancements.criterion.FishingRodHookedTrigger;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import orca.statclocks.listeners.MiscListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(FishingRodHookedTrigger.class)
public abstract class FishingRodHookedTriggerMixin extends SimpleCriterionTrigger<FishingRodHookedTrigger.TriggerInstance> {
	
	@Inject(method = "trigger", at = @At("HEAD"))
	public void trigger (ServerPlayer player, ItemStack rod, FishingHook hook, Collection<ItemStack> lootCollection, CallbackInfo ci) {
		
		//Should only be zero or one item, but this is easier and safer
		for (ItemStack loot : lootCollection) {
			MiscListeners.FISH_ITEM_LISTENER.applyToParts(rod, loot, 1);
		}
		
		Entity caught = hook.getHookedIn();
		
		//Caught an entity
		if (caught != null) {
			MiscListeners.FISH_ITEM_LISTENER.applyToParts(rod, caught, 1);
		}
		
	}

}
