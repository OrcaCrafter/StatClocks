package orca.statclocks.listeners;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface DamageListener <T extends LivingEntity> {


	void onTakeDamage (T entity, DamageSource source, float damageBlocked, float damageTaken);
	
	ListenerAdapter PLAYER_DAMAGE_TAKEN_ADAPTER = new ListenerAdapter();
	ListenerAdapter HORSE_DAMAGE_TAKEN_ADAPTER = new ListenerAdapter();
	ListenerAdapter NAUTILUS_DAMAGE_TAKEN_ADAPTER = new ListenerAdapter();
	
	ListenerAdapter PLAYER_DAMAGE_BLOCKED_ADAPTER = new ListenerAdapter();
	ListenerAdapter HORSE_DAMAGE_BLOCKED_ADAPTER = new ListenerAdapter();
	ListenerAdapter WOLF_DAMAGE_BLOCKED_ADAPTER = new ListenerAdapter();
	ListenerAdapter NAUTILUS_DAMAGE_BLOCKED_ADAPTER = new ListenerAdapter();
	
	
	static void OnPlayerBlockedShield (Player player, DamageSource source, float damage) {
		ItemStack shield = player.getItemBlockingWith();
		
		if (player.isBlocking() && shield != null) {
			//Blocking with shield shouldn't contribute to other stats
			DamageListener.PLAYER_DAMAGE_BLOCKED_ADAPTER.applyToParts(player, shield, source.getEntity(), (int)damage);
		}
	};
	
	
	DamageListener<Player> PLAYER_DAMAGE_LISTENER = (Player player, DamageSource source, float damageBlocked, float damageTaken) -> {
		
		
		ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
		ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
		ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
		ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
		
		DamageListener.PLAYER_DAMAGE_BLOCKED_ADAPTER.applyToParts(player,	head,	source.getEntity(), (int)damageBlocked);
		DamageListener.PLAYER_DAMAGE_TAKEN_ADAPTER.applyToParts(player,		head,	source.getEntity(), (int)damageTaken);
		
		DamageListener.PLAYER_DAMAGE_BLOCKED_ADAPTER.applyToParts(player,	chest,	source.getEntity(), (int)damageBlocked);
		DamageListener.PLAYER_DAMAGE_TAKEN_ADAPTER.applyToParts(player,		chest,	source.getEntity(), (int)damageTaken);
		
		DamageListener.PLAYER_DAMAGE_BLOCKED_ADAPTER.applyToParts(player,	legs,	source.getEntity(), (int)damageBlocked);
		DamageListener.PLAYER_DAMAGE_TAKEN_ADAPTER.applyToParts(player,		legs,	source.getEntity(), (int)damageTaken);
		
		DamageListener.PLAYER_DAMAGE_BLOCKED_ADAPTER.applyToParts(player,	feet, 	source.getEntity(), (int)damageBlocked);
		DamageListener.PLAYER_DAMAGE_TAKEN_ADAPTER.applyToParts(player,		feet, 	source.getEntity(), (int)damageTaken);
		
		
	};
	
	DamageListener<AbstractHorse> HORSE_DAMAGE_LISTENER = (AbstractHorse horse, DamageSource source, float damageBlocked, float damageTaken) -> {
	
		ItemStack armor = horse.getBodyArmorItem();
		
		HORSE_DAMAGE_TAKEN_ADAPTER.applyToPartsNonPlayer(armor, source.getEntity(), (int)damageTaken);
		HORSE_DAMAGE_BLOCKED_ADAPTER.applyToPartsNonPlayer(armor, source.getEntity(), (int)damageBlocked);
	
	};
	
	DamageListener<AbstractNautilus> NAUTILUS_DAMAGE_LISTENER = (AbstractNautilus nautilus, DamageSource source, float damageBlocked, float damageTaken) -> {
		
		ItemStack armor = nautilus.getBodyArmorItem();
		
		NAUTILUS_DAMAGE_TAKEN_ADAPTER.applyToPartsNonPlayer(armor, source.getEntity(), (int)damageTaken);
		NAUTILUS_DAMAGE_BLOCKED_ADAPTER.applyToPartsNonPlayer(armor, source.getEntity(), (int)damageBlocked);
	};
	

}
