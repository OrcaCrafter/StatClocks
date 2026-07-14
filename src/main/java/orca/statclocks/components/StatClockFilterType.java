package orca.statclocks.components;

public enum StatClockFilterType {
	ITEM, BLOCK, ENTITY, PLAYER, NONE;
	
	public static StatClockFilterType getFrom (String value) {
		return switch (value) {
			case "item" -> ITEM;
			case "block" -> BLOCK;
			case "entity" -> ENTITY;
			case "player" -> PLAYER;
			default -> NONE;
		};
	}
	
	public String getName () {
		return switch (this) {
			case ITEM -> "item";
			case BLOCK -> "block";
			case ENTITY -> "entity";
			case PLAYER -> "player";
			default -> "none";
		};
	}
}
