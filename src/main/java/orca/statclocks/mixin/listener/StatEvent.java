package orca.statclocks.mixin.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;
import orca.statclocks.listeners.StatisticsListeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class StatEvent {
	
	
	@Inject(method = "awardStat(Lnet/minecraft/stats/Stat;I)V", at = @At("RETURN"))
	private void awardStat (Stat<?> stat, int amount, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		
		StatisticsListeners.StatEvent(player, stat, amount);
	}
	
}