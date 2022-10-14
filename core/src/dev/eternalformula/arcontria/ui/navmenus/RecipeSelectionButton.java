package dev.eternalformula.arcontria.ui.navmenus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.items.Material;
import dev.eternalformula.arcontria.items.Recipe;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.ui.elements.EFButton;
import dev.eternalformula.arcontria.util.Assets;

public class RecipeSelectionButton extends EFButton {

	private Material material;
	
	public RecipeSelectionButton(RecipeSelectionPane pane, int x, int y) {
		super(pane, x, y);
		
		TextureAtlas uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		TextureRegion btnReg = uiAtlas.findRegion("itembox");
		
		setSkin(new TextureRegion(btnReg, 0, 0, 18, 18));
		setClickSkin(new TextureRegion(btnReg, 18, 0, 18, 18));
		buttonMode = EFButton.TOGGLE_MODE;
		
		setClickAction(new ButtonClickAction() {

			@Override
			public void onClick(int x, int y, int button) {
				pane.onRecipeSelect(Recipe.find(material.getDisplayName()));
			}
			
		});
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
		
		TextureRegion region = material.getIcon();
		uiBatch.draw(region, location.x + 1f, location.y + 1f);	
	}
}
