package dev.eternalformula.arcontria.input;

public interface InputInterface {

	public void onMouseClicked(int x, int y, int button);
	
	public void onMouseReleased(int x, int y, int button);
	
	public void onMouseDrag(int x, int y);
	
	public default void onMouseHovered(int x, int y) {
	}
	
	public default void onMouseWheelScrolled(int amount) {
	}
	
	public void onKeyTyped(char key);
}
