package dev.eternalformula.arcontria.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.scenes.SceneManager;
import dev.eternalformula.arcontria.util.Strings;

public class InputHandler implements InputProcessor {

	private SceneManager mgr;
	
	public boolean isMouseDown;
	public boolean wasMouseDownLast;
	
	private Vector2 lastMouseLocation;
	private Vector2 lastClickLocation;
	
	public InputHandler(SceneManager sceneManager) {
		this.mgr = sceneManager;
		Gdx.input.setInputProcessor(this);
		
		this.isMouseDown = false;
		this.wasMouseDownLast = false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		
		if (mgr.getCurrentScene() != null) {
			mgr.getCurrentScene().onKeyTyped(character);
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		isMouseDown = true;
		
		Vector2 coords = unprojectCoordsToUI(screenX, screenY);
		lastClickLocation = coords;
		
		if (mgr.getCurrentScene() != null) {
			mgr.getCurrentScene().onMouseClicked((int) coords.x, (int) coords.y, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		isMouseDown = false;
		
		if (mgr.getCurrentScene() != null) {
			Vector2 coords = unprojectCoordsToUI(screenX, screenY);
			mgr.getCurrentScene().onMouseReleased((int) coords.x, (int) coords.y, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector2 coords = unprojectCoordsToUI(screenX, screenY);
		
		if (mgr.getCurrentScene() != null) {
			mgr.getCurrentScene().onMouseHovered((int) coords.x, (int) coords.y);
		}
		
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		if (mgr.getCurrentScene() != null) {
			mgr.getCurrentScene().onMouseWheelScrolled((int) amountY);
		}
		return false;
	}
	
	public void update(float delta) {
		Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		
		if ((isMouseDown && wasMouseDownLast) && (!mousePos.equals(lastMouseLocation))) {
			mouseDragged();
		}
		
		wasMouseDownLast = isMouseDown;
		lastMouseLocation = new Vector2(Gdx.input.getX(), Gdx.input.getY());
	}
	
	public void mouseDragged() {
		if (mgr.getCurrentScene() != null) {
			Vector2 localCoords = unprojectCoordsToUI(Gdx.input.getX(), Gdx.input.getY());
			mgr.getCurrentScene().onMouseDrag((int) localCoords.x, (int) localCoords.y);
		}
	}
	
	public Vector2 unprojectCoordsToGame(int x, int y) {
		return mgr.getViewportHandler().getGameViewport().unproject(new Vector2(x, y));
	}
	
	public Vector2 unprojectCoordsToUI(int x, int y) {
		return mgr.getViewportHandler().getUIViewport().unproject(new Vector2(x, y));
	}
	
	public Vector2 getLastClickLocation() {
		return lastClickLocation;
	}

}
