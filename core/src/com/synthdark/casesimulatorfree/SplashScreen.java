package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class SplashScreen implements Screen {

    CaseSimulatorFree game;
    int counter;
    String loadText;

    public SplashScreen(CaseSimulatorFree game) {
        this.game = game;
        counter = 0;
        loadText = game.textBundle.get("initialise_database");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.appBackgroundSprite.draw(game.batch);

        float newXpos = (Gdx.graphics.getWidth() / 2) - (game.Arial_Bold_Largest.getBounds(game.textBundle.get("case_simulator")).width / 2);
        float newYpos = (Gdx.graphics.getHeight() / 2) + game.Arial_Bold_Largest.getBounds(game.textBundle.get("case_simulator")).height;
        game.Arial_Bold_Largest.draw(game.batch, game.textBundle.get("case_simulator"), newXpos, newYpos);

        newXpos = (Gdx.graphics.getWidth() / 2) - (game.Arial_Regular_Normal.getBounds(loadText).width / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - game.Arial_Regular_Normal.getBounds(loadText).height;
        game.Arial_Regular_Normal.draw(game.batch, loadText, newXpos, newYpos);

        game.batch.end();

        if (counter == 1) {
            game.myRequestHandler.initializeDatabase();
            //game.myRequestHandler.refreshDatabase();
            loadText = game.textBundle.get("conversion");
        } else if (counter == 3) {
            game.ConversionCode();
            loadText = game.textBundle.get("load_skins");
        } else if (counter == 5) {
            game.Initialize();
        }
        counter++;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

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
}
