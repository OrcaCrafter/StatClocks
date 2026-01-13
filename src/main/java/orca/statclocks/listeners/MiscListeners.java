package orca.statclocks.listeners;

public class MiscListeners {
	
	
	public static final ListenerAdapter PLAYER_CONSUMES					= new ListenerAdapter();
	public static final ListenerAdapter PLAYER_UNDERWATER				= new ListenerAdapter();
	
	public static final ListenerAdapter DAMAGE_DEALT					= new ListenerAdapter();
	public static final ListenerAdapter DAMAGE_DEALT_DISTANCE			= new ListenerAdapter();
	
	public static final ListenerAdapter MOB_LOOT_LISTENER				= new ListenerAdapter();
	public static final ListenerAdapter BLOCK_LOOT_LISTENER				= new ListenerAdapter();
	
	public static final ListenerAdapter AXE_USE_LISTENER				= new ListenerAdapter();
	public static final ListenerAdapter SHOVEL_USE_LISTENER				= new ListenerAdapter();
	public static final ListenerAdapter HOE_USE_LISTENER				= new ListenerAdapter();
	
	public static final ListenerAdapter FISH_ITEM_LISTENER				= new ListenerAdapter();
	public static final ListenerAdapter FOOD_ON_A_STICK_LISTENER		= new ListenerAdapter();
	
	
	public static final ListenerAdapter BRUSH_USE_LISTENER				= new ListenerAdapter();
	
	public static final ListenerAdapter BLOCKS_IGNITED					= new ListenerAdapter();
	public static final ListenerAdapter MOBS_IGNITED					= new ListenerAdapter();
	
	public static final ListenerAdapter SHEARS_USE_LISTENER 			= new ListenerAdapter();
	public static final ListenerAdapter SHEARS_USE_LISTENER_ITEM 		= new ListenerAdapter();
	public static final ListenerAdapter SHEARS_USE_LISTENER_BLOCK 		= new ListenerAdapter();
	public static final ListenerAdapter SHEARS_USE_LISTENER_ENTITY 		= new ListenerAdapter();
	
	public static final ListenerAdapter INSTRUMENT_USE_LISTENER			= new ListenerAdapter();
	public static final ListenerAdapter SPYGLASS_USE_LISTENER			= new ListenerAdapter();
	
	
	public static final ListenerAdapter MACE_FALL_LISTENER				= new ListenerAdapter();
	
	public static final ListenerAdapter FIREWORK_USED_LISTENER			= new ListenerAdapter();
	
	
	//Replacements for statistics
	public static final ListenerAdapter ENTITY_KILLED_LISTENER			= new ListenerAdapter();
	public static final ListenerAdapter BLOCK_MINED_LISTENER			= new ListenerAdapter();
	public static final ListenerAdapter BLOCK_MINED_UNDERWATER_LISTENER	= new ListenerAdapter();
}
