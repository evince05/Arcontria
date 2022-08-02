package dev.eternalformula.arcontria.ui.charcreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.scenes.charcreator.tabs.CharacterCreatorTab;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.ui.elements.EFButton;
import dev.eternalformula.arcontria.util.Assets;

public class CCStylePicker extends UIContainer {
	
	private CharacterCreatorScene scene;
	
	private EFButton prevBtn;
	private EFButton nextBtn;
	private CCItemDisplayBox itemDisplay;
	
	// Texture Style Preview
	private TextureRegion previewTex;
	private int previewTexW;
	private int previewTexH;
	
	private BitmapFont font;
	private String styleText;
	
	private int itemId;
	private int maxItems;
	
	public CCStylePicker(CharacterCreatorScene scene, CharacterCreatorTab parent, int x, int y) {
		super(parent);
		
		this.scene = scene;
		
		this.location = new Vector2(x, y);
		
		// TextureAtlas
		TextureAtlas ccAtlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		TextureAtlas uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		
		// Font
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		
		// Skin (Container)
		setSkin(ccAtlas.findRegion("stylepickerpane"));
		
		TextureRegion dirBtns = uiAtlas.findRegion("directionbuttons");
		
		this.prevBtn = new EFButton(this, x + 20, y + 28);
		prevBtn.setSkin(new TextureRegion(dirBtns, 0, 0, 10, 10));
		prevBtn.setClickSkin(new TextureRegion(dirBtns, 20, 0, 10, 10));
		prevBtn.setClickAction(new ButtonClickAction() {

			@Override
			public void onClick(int x, int y, int button) {
				if (itemId - 1 >= 0) {
					itemId--;
					itemDisplay.setItemTexture(new TextureRegion(previewTex,
							itemId * 16, 0, 16, 16));
				}
			}
		});
		
		this.nextBtn = new EFButton(this, x + 52, y + 28);
		nextBtn.setSkin(new TextureRegion(dirBtns, 10, 0, 10, 10));
		nextBtn.setClickSkin(new TextureRegion(dirBtns, 30, 0, 10, 10));
		nextBtn.setClickAction(new ButtonClickAction() {
			@Override
			public void onClick(int x, int y, int button) {
				
				if (itemId + 1 < maxItems) {
					itemId++;
					itemDisplay.setItemTexture(new TextureRegion(previewTex,
							itemId * 16, 0, 16, 16));
				}
			}
		});
		
		this.itemDisplay = new CCItemDisplayBox(this, scene.getRGBShader());
		itemDisplay.setLocation(x + 31, y + 23);
		itemDisplay.setSkin(ccAtlas.findRegion("selectiondisplay"));
		
		addChildren(prevBtn, nextBtn, itemDisplay);
		
		this.styleText = "";
		this.itemId = 0;
	}
	
	public void setItemTextPrefix(String styleText) {
		this.styleText = styleText;
	}
	
	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
		
		if (!styleText.equals("")) {
			String text = styleText + " " + (itemId + 1);
			float textWidth = FontUtil.getWidth(font, text);
			
			font.draw(uiBatch, text, location.x + 41f - textWidth / 2f, location.y + 25f);  
		}
		
		scene.getPlayerPreview().draw(uiBatch, delta, location.x + 30, location.y + 45f);
	}
	
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}
	
	public int getCurrentId() {
		return itemId;
	}
	
	/**
	 * Sets the preview texture (to be displayed between the prev and next arrows).
	 * @param previewTexture The texture region to be displayed. (usually a spritesheet).
	 * @param texWidth The width of each individual texture in the sheet.
	 * @param texHeight The height of each individual texture in the sheet.
	 */
	
	public void setPreviewTexture(TextureRegion previewTexture, int texWidth, int texHeight) {
		this.previewTex = previewTexture;
		this.previewTexW = texWidth;
		this.previewTexH = texHeight;
		
		itemDisplay.setItemTexture(new TextureRegion(previewTexture, itemId * texWidth, 0, texWidth, texHeight));
	}
	
	public void setPreviewColor(Color color) {
		itemDisplay.setItemColor(color);
	}
}
