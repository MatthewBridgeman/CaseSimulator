package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WeaponObject {
	
	CaseSimulatorFree game;
	
	Sprite gunSprite;
	Sprite gunBackgroundSprite;
	Sprite highlightSprite;
    Sprite stattrakSprite;
	String rarity;
	String gunType;
	String gunSkin;
    String gunTexture;

	int gunID;
    int caseID;
	boolean showGun;
	boolean render;
    boolean stattrak;
    boolean eventSticker;
	String wear;
	boolean highlighted;
	
	float textXpos;
	float typeTickYpos;
	float skinTickYpos;
	float typeShowYpos;
	float skinShowYpos;
	float wearShowYpos;
	
	
	public WeaponObject(CaseSimulatorFree game, Sprite newGunSprite, Sprite newGunBackgroundSprite, Sprite newHighlightSprite, Sprite newStattrakSprite, String newRarity, String newGunType, String newGunSkin, String newGunTexture) {
		gunSprite = newGunSprite;
		gunBackgroundSprite = newGunBackgroundSprite;
		highlightSprite = newHighlightSprite;
        stattrakSprite = newStattrakSprite;
		gunType = newGunType;
		gunSkin = newGunSkin;
		rarity = newRarity;
		gunTexture = newGunTexture;
		showGun = false;
		stattrak = false;
        eventSticker = false;
		wear = "";
		this.game = game;
		render = true;
		highlighted = false;
	}
	
	public void GenerateText() {
		textXpos = gunBackgroundSprite.getX() + (gunBackgroundSprite.getWidth() / 20);
		typeTickYpos = gunBackgroundSprite.getY() + (gunBackgroundSprite.getHeight() / 4.4f);
		skinTickYpos = gunBackgroundSprite.getY() + (gunBackgroundSprite.getHeight() / 8.4f);
		typeShowYpos = gunBackgroundSprite.getY() + (gunBackgroundSprite.getHeight() / 3.2f);
		skinShowYpos = gunBackgroundSprite.getY() + (gunBackgroundSprite.getHeight() / 4.8f);
		wearShowYpos = gunBackgroundSprite.getY() + (gunBackgroundSprite.getHeight() / 9.1f);
	}
	
	public void update(float x, float y) {
		gunSprite.setPosition(gunSprite.getX() + x, gunSprite.getY() + y);		
		gunBackgroundSprite.setPosition(gunBackgroundSprite.getX() + x, gunBackgroundSprite.getY() + y);
		highlightSprite.setPosition(highlightSprite.getX() + x, highlightSprite.getY() + y);
        stattrakSprite.setPosition(stattrakSprite.getX() + x, stattrakSprite.getY() + y);
		textXpos += x;
		typeTickYpos += y;
		skinTickYpos += y;
		typeShowYpos += y;
		skinShowYpos += y;
		wearShowYpos += y;
	}

    public void render(SpriteBatch batch) {
        if (render) {

            if (highlighted) {
                highlightSprite.draw(batch);
            }
            gunBackgroundSprite.draw(batch);
            gunSprite.draw(batch);

            if (!showGun) {
                if (!rarity.equals("5")) {
                    game.Arial_Bold_Normal.draw(batch, gunType, textXpos, typeTickYpos);
                } else {
                    float textXpos2 = (gunBackgroundSprite.getX() + (gunBackgroundSprite.getWidth()) / 2) - (game.Arial_Bold_Normal.getBounds(gunType).width / 2 );
                    game.Arial_Bold_Normal.draw(batch, gunType, textXpos2, typeTickYpos);
                }
                game.Arial_Regular_Normal.draw(batch, gunSkin, textXpos, skinTickYpos);
            } else {
                if (stattrak) {
                    game.Arial_Bold_Normal.draw(batch, game.textBundle.get("stattrak") + " " + gunType, textXpos, typeShowYpos);
                } else {
                    game.Arial_Bold_Normal.draw(batch, gunType, textXpos, typeShowYpos);
                }
                if (gunSkin.equals("")) {
                    if (!wear.equals("")) {
                        game.Arial_Regular_Normal.draw(batch, game.textBundle.get(wear), textXpos, skinShowYpos);
                    }
                } else {
                    game.Arial_Regular_Normal.draw(batch, gunSkin, textXpos, skinShowYpos);
                    if (!wear.equals("")) {
                        game.Arial_Regular_Normal.draw(batch, game.textBundle.get(wear), textXpos, wearShowYpos);
                    }
                }
            }
            if (stattrak) {
                if (game.userPrefs.getBoolean("show_stattrak", false) || showGun) {
                    stattrakSprite.draw(batch);
                }
            }
        }
    }
}
