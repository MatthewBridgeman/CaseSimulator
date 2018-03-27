package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class SettingsObject {

    CaseSimulatorFree game;

    String settingsType;
    boolean ticked = true;
    Sprite tickBoxSprite;
    Vector2 renderPos;
    Vector2 bounds;

    public SettingsObject(CaseSimulatorFree game, String type, float xPos, float yPos) {
        this.game = game;
        settingsType = type;
        if ((type == "classic_sounds") || (type == "show_stattrak")) {
            ticked = !game.userPrefs.getBoolean(settingsType, false);
        } else {
            ticked = !game.userPrefs.getBoolean(settingsType, true);
        }
        renderPos = new Vector2(xPos, yPos);
        UpdateTick();

        float newXbounds = tickBoxSprite.getWidth() + game.Arial_Regular_Normal.getBounds(settingsType).width + (Gdx.graphics.getWidth() / 100);
        float newYbounds = tickBoxSprite.getHeight();
        bounds = new Vector2(newXbounds, newYbounds);
    }

    public void UpdateTick() {
        ticked = !ticked;
        game.userPrefs.putBoolean(settingsType, ticked);
        game.userPrefs.flush();
        if (settingsType == "classic_sounds") {
            game.LoadSounds();
        }

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
        float newYpos = renderPos.y + (game.Arial_Regular_Normal.getBounds(settingsType).height / 2);
        if (ticked) {
            game.Arial_Regular_Normal_Blue.draw(game.batch, game.textBundle.get(settingsType), newXpos, newYpos);
        } else {
            game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get(settingsType), newXpos, newYpos);
        }
    }

}
