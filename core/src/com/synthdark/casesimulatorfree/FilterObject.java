package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class FilterObject {

	CaseSimulatorFree game;
	
	String filterType;
	int filterID;
	Sprite tickBoxSprite;
	boolean ticked = true;
	Vector2 renderPos;
	Vector2 bounds;
	
	public FilterObject(CaseSimulatorFree game, String type, int id, float xPos, float yPos) {
		this.game = game;
		filterType = type;
		filterID = id;
		renderPos = new Vector2(xPos, yPos);
		UpdateTick();

		float newXbounds = tickBoxSprite.getWidth() + game.Arial_Regular_Normal.getBounds(filterType).width + (Gdx.graphics.getWidth() / 100);
		float newYbounds = tickBoxSprite.getHeight();
		bounds = new Vector2(newXbounds, newYbounds);
	}
	
	public void UpdateTick() {
		ticked = !ticked;
		
		if (ticked) {
			tickBoxSprite = game.tickBoxTextureAtlas.createSprite("CheckedBox");
		} else {
			tickBoxSprite = game.tickBoxTextureAtlas.createSprite("UncheckedBox");
		}
		
		float imageRatio = tickBoxSprite.getWidth() / tickBoxSprite.getHeight();
		tickBoxSprite.setSize(Gdx.graphics.getWidth() / 40, (Gdx.graphics.getWidth() / imageRatio) / 40);
		tickBoxSprite.setPosition(renderPos.x,renderPos.y - (tickBoxSprite.getHeight() / 2));
	}
	
	public void UpdatePos(float xPos, float yPos) {
		renderPos.x += xPos;
		renderPos.y += yPos;
		tickBoxSprite.setPosition(renderPos.x,renderPos.y - (tickBoxSprite.getHeight() / 2));
	}
	
	public void Render() {
		tickBoxSprite.draw(game.batch);
		float newXpos = renderPos.x + tickBoxSprite.getWidth() + (Gdx.graphics.getWidth() / 100);
		float newYpos = renderPos.y + (game.Arial_Regular_Normal.getBounds(filterType).height / 2);
		if (ticked) {
			game.Arial_Regular_Normal_Blue.draw(game.batch, filterType, newXpos, newYpos);
		} else {
			game.Arial_Regular_Normal.draw(game.batch, filterType, newXpos, newYpos);
		}
	}
}
