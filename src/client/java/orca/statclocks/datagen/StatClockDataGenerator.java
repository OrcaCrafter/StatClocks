package orca.statclocks.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import orca.statclocks.StatClocksMod;
import orca.statclocks.datagen.tags.StatClockBlockTagProvider;
import orca.statclocks.datagen.tags.StatClockEntityTagProvider;
import orca.statclocks.datagen.tags.StatClockItemTagProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class StatClockDataGenerator implements DataGeneratorEntrypoint {
	
	@Override
	public void onInitializeDataGenerator (FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		
		pack.addProvider(StatClockItemTagProvider::new);
		pack.addProvider(StatClockBlockTagProvider::new);
		pack.addProvider(StatClockEntityTagProvider::new);
		
		pack.addProvider(StatClockRecipeProvider::new);
		
		pack.addProvider(StatClockEnglishLangProvider::new);
		
		pack.addProvider(StatClocksModelProvider::new);
		
		String workingDir = System.getProperty("user.dir");
		File projectRoot = new File(workingDir).getParentFile().getParentFile();
		
		File guiIcons = new File(projectRoot.getAbsolutePath() + "/src/main/resources/assets/stat-clocks/textures/gui/stat_clock_part_icons");
		File itemIcons = new File(projectRoot.getAbsolutePath() + "/src/main/resources/assets/stat-clocks/textures/item/stat_clock_part_icons");
		
		
		for (File imageFile : Objects.requireNonNull(guiIcons.listFiles())) {
			
			try {
				StatClocksMod.LOGGER.info("Reading file: {}", imageFile.getAbsolutePath());
				BufferedImage image = ImageIO.read(imageFile);
				
				BufferedImage drawnImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
				
				Graphics2D g2d = drawnImage.createGraphics();
				
				g2d.drawImage(image, 6, 4, null);
				
				g2d.dispose();
				
				File drawnImageFile = new File(itemIcons.getAbsoluteFile() + "/" + imageFile.getName());
				
				StatClocksMod.LOGGER.info("Writing to file: {}", drawnImageFile.getAbsolutePath());
				boolean success = ImageIO.write(drawnImage, "PNG", drawnImageFile);
				
				if (!success) {
					StatClocksMod.LOGGER.warn("Failed to write image file: {}", drawnImageFile.getAbsolutePath());
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}
		
	}
}
