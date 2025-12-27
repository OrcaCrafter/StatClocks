package orca.statclocks.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import orca.statclocks.StatClocksMod;

import java.util.concurrent.CompletableFuture;


public class StatClockEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	
	public StatClockEntityTagProvider (FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}
	
	static final EntityType<?>[] IGNITABLE_MOBS = new EntityType<?>[] {
		EntityType.CREEPER
	};
	
	static final EntityType<?>[] SHEARABLE_MOBS = new EntityType<?>[] {
		EntityType.SHEEP, EntityType.MOOSHROOM, EntityType.BOGGED
	};
	
	@Override
	protected void addTags (HolderLookup.Provider wrapperLookup) {
		
		addAll(StatClocksMod.IGNITABLE_MOBS, IGNITABLE_MOBS);
		
		addAll(StatClocksMod.SHEARABLE_MOBS, SHEARABLE_MOBS);
	}
	
	private void addAll (TagKey<EntityType<?>> tag, EntityType<?>... mobs) {
		
		TagAppender<EntityType<?>, EntityType<?>> builder = valueLookupBuilder(tag);
		
		for (EntityType<?> mob : mobs) {
			builder.add(mob);
		}
	}
}
