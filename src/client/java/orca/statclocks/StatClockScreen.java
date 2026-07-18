package orca.statclocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.Equippable;
import orca.statclocks.components.StatClockContent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

//TODO item viewer screen?

public class StatClockScreen extends Screen {
	
	private static final float CAMERA_TILT_DOWN = 0.43633232F;
	private static final float ROTATION_SPEED = 2;
	
	private static final float SPIN_PAUSE_TIME = 2;
	
	private static final float RANDOM_ENTITY_INTERVAL = 2;
	
	private static final int LEFT_MOUSE = 0;
	
	private static final Component TITLE = Component.translatable("stat-clocks.view.title");
	
	private final Screen prior;
	private final ItemStack itemStack;
	private final StatClockContent content;
	private float time = 0;
	private float previewAngle = 0;
	private float pauseSpinUntil = 0;
	
	private long randomSeed;
	
	private FocusableTextWidget itemName;
	
	public StatClockScreen (Screen prior, ItemStack itemStack, StatClockContent content) {
		super(TITLE);
		this.prior = prior;
		
		if (itemStack.is(StatClocksMod.STAT_CLOCK)) {
			
			Item item = BuiltInRegistries.ITEM.getValueOrThrow(ResourceKey.create(Registries.ITEM, content.getToolTypeID()));
			
			this.itemStack = new ItemStack(item);
		} else {
			this.itemStack = itemStack;
		}
		
		this.content = content;
		
	}
	
	public StatClockPartList partList;
	
	protected void init () {
		randomSeed = new Random().nextLong();
		
		partList = new StatClockPartList(minecraft, this.width, this.height, itemStack, content, font);
		
		itemName = FocusableTextWidget.builder(itemStack.getItemName(), font).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build();
		
		addRenderableWidget(partList);
		addRenderableWidget(itemName);
	}
	
	@Override
	public void render (GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.render(graphics, mouseX, mouseY, delta);
		
		//Calculate time in seconds not ticks
		delta /= 20f;
		this.time += delta;
		
		if (time >= pauseSpinUntil) {
			previewAngle += (delta*ROTATION_SPEED) / (float)Math.TAU;
		}
		
		//Item preview
		renderItem(graphics);
		
		partList.setPosition(width/2 + StatClockPartList.SIDE_PADDING,  StatClockPartList.TOP_PADDING);
		partList.render(graphics, mouseX, mouseY, delta);
		
		itemName.setPosition((width*3)/4 - itemName.getWidth()/2, StatClockPartList.TOP_PADDING/2 - itemName.getHeight()/2);
		itemName.render(graphics, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseDragged (MouseButtonEvent mouseButtonEvent, double dx, double dy) {
		
		if (mouseButtonEvent.button() == LEFT_MOUSE && mouseButtonEvent.x() < width/2f) {
			float spinAmount = (float)(dx / (width / 2f) * Math.TAU);
			
			previewAngle -= spinAmount;
			
			pauseSpinUntil = time + SPIN_PAUSE_TIME;
			
			return true;
		}
		
		return false;
	}
	
	private void renderItem (GuiGraphics graphics) {
		
		Equippable equipment = itemStack.get(DataComponents.EQUIPPABLE);
		
		if (equipment != null && equipment.slot().getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
			this.renderHumanoidArmor(graphics, equipment);
		} else if (equipment != null && (equipment.slot().getType() == EquipmentSlot.Type.ANIMAL_ARMOR || equipment.slot().getType() == EquipmentSlot.Type.SADDLE)) {
			this.renderAnimalArmor(graphics, equipment);
		} else if (itemStack.getItem() instanceof VehicleItemAccessor vehicleItemAccessor) {
			this.renderEntity(graphics, vehicleItemAccessor.statClocks$getVehicleType(), (entity, state) -> {
				if (entity instanceof AbstractMinecart minecart && state instanceof MinecartRenderState minecartRenderState) {
					minecartRenderState.displayOffset = minecart.getDisplayOffset();
					minecartRenderState.displayBlockState = minecart.getDisplayBlockState();
				}
			});
		} else {
			renderGenericItem(graphics);
		}
	}
	
	public interface VehicleItemAccessor {
		EntityType<?> statClocks$getVehicleType ();
	}
	
	private void renderGenericItem (GuiGraphics graphics) {
		ArmorStand armorStand = EntityType.ARMOR_STAND.create(Minecraft.getInstance().level, EntitySpawnReason.EVENT);

		this.<ArmorStand, ArmorStandRenderState>renderSpecificEntity(graphics, EntityType.ARMOR_STAND, armorStand, (renderState) -> {
			
			renderState.showBasePlate = false;
			renderState.showArms = true;
			
			ItemModelResolver itemModelResolver = this.minecraft.getItemModelResolver();

			renderState.leftHandItemStack = itemStack.copy();
			itemModelResolver.updateForTopItem(renderState.leftHandItemState, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, null, null, 0);

		});
	}
	
	private void renderHumanoidArmor (GuiGraphics graphics, Equippable equippable) {
		ArmorStand armorStand = EntityType.ARMOR_STAND.create(Minecraft.getInstance().level, EntitySpawnReason.EVENT);
		
		this.<ArmorStand, ArmorStandRenderState>renderSpecificEntity(graphics, EntityType.ARMOR_STAND, armorStand, (renderState) -> {
			
			renderState.showBasePlate = false;
			renderState.showArms = true;
			
			EquipmentSlot equipmentSlot = equippable.slot();
			ItemModelResolver itemModelResolver = this.minecraft.getItemModelResolver();
			
			switch (equipmentSlot) {
				case HEAD:
					if (HumanoidArmorLayer.shouldRender(itemStack, EquipmentSlot.HEAD)) {
						renderState.headEquipment = itemStack.copy();
					} else {
						itemModelResolver.updateForTopItem(renderState.headItem, itemStack, ItemDisplayContext.HEAD, null, null, 0);
					}
					break;
				case CHEST:
					renderState.chestEquipment = itemStack.copy();
					break;
				case LEGS:
					renderState.legsEquipment = itemStack.copy();
					break;
				case FEET:
					renderState.feetEquipment = itemStack.copy();
					break;
				default:
					renderState.leftHandItemStack = itemStack.copy();
					itemModelResolver.updateForTopItem(renderState.leftHandItemState, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, null, null, 0);
			}
		});
		
	}
	
	private void renderAnimalArmor (GuiGraphics graphics, Equippable equippable) {
		if (equippable.allowedEntities().isEmpty()) return;
		
		HolderSet<EntityType<?>> entityTypes = equippable.allowedEntities().get();
		
		Random random = new Random(randomSeed * (long)(Math.floor(time * RANDOM_ENTITY_INTERVAL)));
		
		int index = random.nextInt(entityTypes.size());
		
		EntityType<?> entityType = entityTypes.get(index).value();
		Entity entity = entityType.create(Minecraft.getInstance().level, EntitySpawnReason.EVENT);
		
		this.renderSpecificEntity(graphics, (EntityType<Entity>) entityType, entity, (state) -> {
			if (state instanceof EquipmentAssigner assigner) {
				assigner.statClocks$assign(itemStack, equippable);
			}
			
			if (state instanceof VariantRandomizer randomizer) {
				randomizer.statClocks$randomizeVariant(random);
			}
		});
		
	}
	
	public interface EquipmentAssigner {
		void statClocks$assign (ItemStack itemStack, Equippable equippable);
	}
	
	public interface VariantRandomizer {
		void statClocks$randomizeVariant (Random random);
	}
	
	private <T extends Entity> void renderEntity (GuiGraphics graphics, EntityType<T> entityType, BiConsumer<Entity, EntityRenderState> consumer) {
		T entity = entityType.create(Minecraft.getInstance().level, EntitySpawnReason.EVENT);
		
		renderSpecificEntity(graphics, entityType, entity,  (state) -> {
			consumer.accept(entity, state);
		});
	}
	
	private <T extends Entity, S extends EntityRenderState> void renderSpecificEntity (GuiGraphics graphics, EntityType<T> entityType, T entity, Consumer<S> stateModifier) {
		Vector3f translate = new Vector3f(0.0F, 1.0F, 0.0F);
		Quaternionf angle = new Quaternionf().rotationXYZ(CAMERA_TILT_DOWN, previewAngle, (float) Math.PI);
		
		int scale;
		
		if (entity.getBoundingBox().getSize() >= 4) {
			scale = 10;
		} else {
			scale = 20;
		}
		
		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		EntityRenderer<? super Entity, ?> renderer = dispatcher.getRenderer(entity);
		
		S renderState = (S)renderer.createRenderState();
		
		stateModifier.accept(renderState);
		
		renderState.entityType = entityType;
		
		graphics.submitEntityRenderState(renderState, 5f * scale, translate, angle, null, 0, 0, width/2, height);
	}
	
	public boolean isPauseScreen () {
		return false;
	}
	
	public boolean isInGameUi () {
		return true;
	}
	
	@Override
	public void onClose () {
		super.onClose();
		Minecraft.getInstance().setScreen(prior);
	}
}
