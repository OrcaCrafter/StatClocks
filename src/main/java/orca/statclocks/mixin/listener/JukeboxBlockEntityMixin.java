package orca.statclocks.mixin.listener;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import orca.statclocks.components.StatClockContent;
import orca.statclocks.components.StatClockPartContent;
import orca.statclocks.lists.StatClockPartTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntity {
	
	public JukeboxBlockEntityMixin (BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
	
	@Inject(method = "tick", at = @At("RETURN"))
	private static void tick (Level level, BlockPos blockPos, BlockState blockState, JukeboxBlockEntity jukeboxBlockEntity, CallbackInfo ci) {
		
		ItemStack disc = jukeboxBlockEntity.getTheItem();
		
		JukeboxSongPlayer player = jukeboxBlockEntity.getSongPlayer();
		JukeboxSong song = player.getSong();
		
		if (song == null) return;
		if (disc == ItemStack.EMPTY) return;
		
		StatClockContent statClock = disc.get(StatClockContent.STAT_CLOCK_COMPONENT);
		
		if (statClock == null) return;
		
		StatClockPartContent timePlayed = statClock.getPart(StatClockPartTypes.TIME_PLAYED);
		StatClockPartContent timesFinished = statClock.getPart(StatClockPartTypes.TIMES_FINISHED);
		
		assert timePlayed != null;
		assert timesFinished != null;
		
		//Called every tick a song is playing
		if (jukeboxBlockEntity.getSongPlayer().isPlaying()) {
			timePlayed.incrementCount();
		}
		
		//Only called once on the last tick of playing
		if (song.hasFinished(player.getTicksSinceSongStarted())) {
			timesFinished.incrementCount();
		}
		
	}
	
	
	@Inject(method="setTheItem", at = @At("HEAD"))
	public void setTheItem (ItemStack itemStack, CallbackInfo ci, @Local(argsOnly = true) LocalRef<ItemStack> localRef) {
		//Fixes creative players keeping the same item stack reference when putting a disc in
		localRef.set(itemStack.copy());
	}
}