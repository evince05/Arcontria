package dev.eternalformula.arcontria.ui.charcreator;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.ui.elements.EFCheckBox;
import dev.eternalformula.arcontria.util.Assets;

public class CCGenderPicker extends UIContainer {
	
	private EFCheckBox maleBox;
	private EFCheckBox femaleBox;
	private EFCheckBox otherBox;
	
	private BitmapFont font;
	
	private int selectedBox;
	
	public CCGenderPicker(UIContainer parent, int x, int y) {
		super(parent);
		
		this.location = new Vector2(x, y);
		
		// UIAtlas
		TextureAtlas uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		TextureRegion checkBox = new TextureRegion(uiAtlas.findRegion("checkbox"), 0, 0, 8, 8);
		TextureRegion checkedCheckBox = new TextureRegion(uiAtlas.findRegion("checkbox"), 8, 0, 8, 8);
		
		// Font
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		
		this.maleBox = new EFCheckBox(this);
		maleBox.setLocation(x, y);
		maleBox.setSkin(checkBox);
		maleBox.setCheckedSkin(checkedCheckBox);
		maleBox.setClickAction(new ButtonClickAction() {

			@Override
			public void onClick(int x, int y, int button) {
				selectGender(0);
			}
		});
		
		this.femaleBox = new EFCheckBox(this);
		femaleBox.setLocation(x + 38, y);
		femaleBox.setSkin(checkBox);
		femaleBox.setCheckedSkin(checkedCheckBox);
		femaleBox.setClickAction(new ButtonClickAction() {

			@Override
			public void onClick(int x, int y, int button) {
				selectGender(1);
			}
		});
		
		this.otherBox = new EFCheckBox(this);
		otherBox.setLocation(x + 88, y);
		otherBox.setSkin(checkBox);
		otherBox.setCheckedSkin(checkedCheckBox);
		otherBox.setClickAction(new ButtonClickAction() {

			@Override
			public void onClick(int x, int y, int button) {
				selectGender(2);
			}
		});
		
		addChildren(maleBox, femaleBox, otherBox);
		
		// No box is selected
		this.selectedBox = -1;
	}
	
	/**
	 * Returns the selected gender. <br>
	 * -1 = none selected, 0 = male, 1 = female, 2 = other
	 */
	
	public int getSelectedGender() {
		return selectedBox; 
	}
	
	private void selectGender(int gender) {
		
		for (int i = 0; i < children.size(); i++) {
			UIElement e = children.get(i);
			if (e instanceof EFCheckBox) {
				EFCheckBox checkBox = (EFCheckBox) e;
				if (i == gender) {
					checkBox.setValue(true);
				}
				else {
					checkBox.setValue(false);
				}
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
		
		font.draw(uiBatch, "Male", location.x + 10, location.y + 14);
		font.draw(uiBatch, "Female", location.x + 48, location.y + 14);
		font.draw(uiBatch, "Other", location.x + 98, location.y + 14);
	}

}
