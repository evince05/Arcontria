package dev.eternalformula.arcontria.items;

public enum Material {
	
	APPLE("Apple", 0),
	BREAD("Bread", 1),
	COOKIE("Cookie", 2),
	RUSTY_SWORD("Rusty Sword", 3),
	ARCONTRIA("Arcontria", 4);
	
	private String name;
	private int id;
	
	Material(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

}
