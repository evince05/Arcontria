package dev.eternalformula.arcontria.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UIContainer extends UIElement {
	
	// The element which the mouse has just clicked on.
	public UIElement focusedElement;
	
	protected List<UIElement> children;
	
	/**
	 * Base UIContainer (no parent) constructor.
	 */
	
	public UIContainer() {
		super();
		this.children = new ArrayList<UIElement>();
	}
	
	/**
	 * Child UIContainer (has parent) constructor.
	 */
	
	public UIContainer(UIContainer parent) {
		super(parent);
		this.children = new ArrayList<UIElement>();
	}
	
	public List<UIElement> getChildren() {
		return children;
	}
	
	public void addChild(UIElement e) {
		children.add(e);
	}
	
	public void addChildren(UIElement... elements) {
		for (UIElement e : elements) {
			children.add(e);
		}
	}
	
	@Override
	public void onKeyTyped(char key) {
		children.forEach(e -> {
			e.onKeyTyped(key);
		});
	}

	@Override
	public void onMouseClicked(int x, int y, int button) {
		
		children.forEach(e -> {
			if (e.isActive()) {
				e.onMouseClicked(x, y, button);
			}
			
		});
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
		children.forEach(e -> {
			if (e.isActive()) {
				e.onMouseReleased(x, y, button);
			}
			
		});
	}

	/**
	 * Note: if input is messed up, check the e.isActive().
	 */
	@Override
	public void onMouseHovered(int x, int y) {
		children.forEach(e -> {
			if (e.isActive()) {
				e.onMouseHovered(x, y);
			}
			
		});
	}
	
	@Override
	public void onMouseDrag(int x, int y) {
		children.forEach(e -> {
			if (e.isActive()) {
				e.onMouseDrag(x, y);
			}
			
		});
	}

	@Override
	public void update(float delta) {
		for (UIElement e : children) {
			e.update(delta);
		}
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		if (visible) {
			
			if (skin != null) {
				uiBatch.draw(skin, location.x, location.y);
			}
			for (UIElement e : children) {
				if (e.isVisible()) {
					e.draw(uiBatch, delta);
				}
			}
		}
	}
	
	protected void drawChildren(SpriteBatch uiBatch, float delta) {
		for (UIElement e : children) {
			e.draw(uiBatch, delta);
		}
	}
	
	public boolean isFocusedOnElement(UIElement e) {
		return focusedElement != null && focusedElement.equals(e);
	}
	
	public boolean hasParent() {
		return container != null;
	}
	
	public UIContainer getParent() {
		return container;
	}

}
