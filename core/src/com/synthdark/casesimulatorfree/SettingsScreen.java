package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsScreen implements Screen, GestureDetector.GestureListener {

    CaseSimulatorFree game;
    InputProcessor gestureDectector;

    List<SettingsObject> settingsList;
    Vector2 renderStart;

    public SettingsScreen(CaseSimulatorFree game){
        this.game = game;
        gestureDectector = new GestureDetector(100f, 0.0f, 0.0f, 5f, this);

        renderStart = new Vector2((Gdx.graphics.getWidth() / 40), game.inventoryTopBackgroundSprite.getY());

        settingsList = new ArrayList<SettingsObject>();
        List<String> tempSettingsList = new ArrayList<String>(Arrays.asList("sounds", "classic_sounds", "holiday_events", "event_stickers", "show_stattrak"));
        float newXpos = renderStart.x;
        float newYpos = renderStart.y - game.Arial_Bold_Large.getBounds("Settings").height - (Gdx.graphics.getWidth() / 40);
        for (int i = 0; i<tempSettingsList.size(); i++) {
            SettingsObject newSettingsObject = new SettingsObject(game, tempSettingsList.get(i), newXpos, newYpos);

            settingsList.add(newSettingsObject);
            newYpos -= newSettingsObject.bounds.y + (Gdx.graphics.getWidth() / 90);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.appBackgroundSprite.draw(game.batch);
        game.inventoryTopBackgroundSprite.draw(game.batch);

        if ((game.userPrefs.getBoolean("holiday_events", true)) && game.snow) {
            for (int i = 0; i < game.snowParticleList.size(); i++) {
                game.snowParticleList.get(i).update();
                game.snowParticleList.get(i).render();
            }
        }

        game.settingsButtonSprite.draw(game.batch);

        float newXpos = (game.settingsButtonSprite.getX() + (game.settingsButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("home")).width / 2);
        float newYpos = (game.settingsButtonSprite.getY() + (game.settingsButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("home")).height / 2);
        game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("home"), newXpos, newYpos);

        newXpos = renderStart.x;
        newYpos = renderStart.y;
        game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("settings"), newXpos, newYpos);
        for (int i = 0; i < settingsList.size(); i++) {
            settingsList.get(i).Render();
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gestureDectector);
        game.myRequestHandler.sendTrackerScreenName("Settings Screen");
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if ((x > game.settingsButtonSprite.getX()) && (x < (game.settingsButtonSprite.getX() + game.settingsButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.settingsButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.settingsButtonSprite.getY() + game.settingsButtonSprite.getHeight()))) {
            game.setScreen(game.caseScreen);
        } else 	if ((Gdx.graphics.getHeight() -y) < game.inventoryTopBackgroundSprite.getY()) {
            for (int i = 0; i < settingsList.size(); i++) {
                if ((x > settingsList.get(i).renderPos.x) && (x < (settingsList.get(i).renderPos.x + settingsList.get(i).bounds.x)) && ((Gdx.graphics.getHeight() - y) > settingsList.get(i).tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() - y) < (settingsList.get(i).tickBoxSprite.getY() + settingsList.get(i).bounds.y))) {
                    settingsList.get(i).UpdateTick();
                }
            }
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
