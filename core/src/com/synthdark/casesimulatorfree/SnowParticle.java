package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SnowParticle {

    CaseSimulatorFree game;

    Sprite snowSprite;
    float fallRate;
    float xMovement;
    float directionChangeTime = 1000;
    long lastDirectionChangeTime;
    float imageRatio;

    public SnowParticle(CaseSimulatorFree game){
        this.game = game;
        fallRate = -1;
        xMovement = ((game.random.nextFloat() * 5) - 2.5f);
        lastDirectionChangeTime = System.currentTimeMillis();

        snowSprite = game.snowTextureAtlas.createSprite("Snowflake" + (game.random.nextInt(4) + 1));
        imageRatio = snowSprite.getWidth() / snowSprite.getHeight();
        int snowSize = 45 + (game.random.nextInt(40) - 20);
        snowSprite.setSize(Gdx.graphics.getWidth() / snowSize, (Gdx.graphics.getWidth() / imageRatio) / snowSize);
        float newXpos = game.random.nextInt(Gdx.graphics.getWidth());
        float newYpos = game.random.nextInt(Gdx.graphics.getHeight());
        snowSprite.setPosition(newXpos,newYpos);
    }

    public void update() {
        if (System.currentTimeMillis() - lastDirectionChangeTime >= directionChangeTime) {
            xMovement += ((game.random.nextFloat() * 1) - 0.5f);
            lastDirectionChangeTime = System.currentTimeMillis();
        }
        snowSprite.translate(xMovement, fallRate);

        if (((snowSprite.getX() + snowSprite.getWidth()) < 0) || (snowSprite.getX() > Gdx.graphics.getWidth()) || ((snowSprite.getY() + snowSprite.getHeight()) < 0)) {
            xMovement = ((game.random.nextFloat() * 5) - 2.5f);
            lastDirectionChangeTime = System.currentTimeMillis();

            int snowSize = 45 + (game.random.nextInt(40) - 20);
            snowSprite.setSize(Gdx.graphics.getWidth() / snowSize, (Gdx.graphics.getWidth() / imageRatio) / snowSize);
            float newXpos = game.random.nextInt(Gdx.graphics.getWidth());
            float newYpos = Gdx.graphics.getHeight();
            snowSprite.setPosition(newXpos,newYpos);
        }
    }

    public void render() {
        snowSprite.draw(game.batch);
    }
}
