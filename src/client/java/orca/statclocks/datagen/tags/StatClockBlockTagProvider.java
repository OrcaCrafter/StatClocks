package orca.statclocks.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import orca.statclocks.StatClocksMod;

import java.util.concurrent.CompletableFuture;


public class StatClockBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	
	public StatClockBlockTagProvider (FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}
	
	
	static final Block[] SHEARABLE_BLOCKS = new Block[] {
		Blocks.PUMPKIN,
		Blocks.CAVE_VINES_PLANT
	};
	
	@Override
	protected void addTags (HolderLookup.Provider wrapperLookup) {
		
		TagAppender<Block, Block> builder = valueLookupBuilder(StatClocksMod.SHEARABLE_BLOCKS);
		
		for (Block block : SHEARABLE_BLOCKS) {
			builder.add(block);
		}
		
		
	}
}
