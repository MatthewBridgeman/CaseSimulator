package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class EventStickventoryScreen implements Screen, GestureDetector.GestureListener, InputProcessor {

    CaseSimulatorFree game;
    InputProcessor gestureDectector;

    List<EventStickerObject> eventStickventoryObjectList;

    Vector2 touchDownPoint;
    int loadSize;
    int invenTop;
    float renderStart;
    int highlightNumber;
    boolean delete;

    public EventStickventoryScreen(CaseSimulatorFree game){
        this.game = game;
        gestureDectector = new GestureDetector(100f, 0.0f, 0.0f, 5f, this);
        eventStickventoryObjectList = new ArrayList<EventStickerObject>();
        invenTop = 0;
        loadSize = 0;
        touchDownPoint = new Vector2();
        highlightNumber = 0;
        delete = false;
        renderStart = game.inventoryTopBackgroundSprite.getY() - game.gunBackgroundSize.y;
        LoadEventStickventoryObjects();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.appBackgroundSprite.draw(game.batch);

        if ((game.userPrefs.getBoolean("holiday_events", true)) && game.snow) {
            for (int i = 0; i < game.snowParticleList.size(); i++) {
                game.snowParticleList.get(i).update();
                game.snowParticleList.get(i).render();
            }
        }

        for(int i = 0; i< eventStickventoryObjectList.size(); i++) {
            eventStickventoryObjectList.get(i).render(game.batch);
        }

        game.inventoryTopBackgroundSprite.draw(game.batch);
        game.inventoryBottomBackgroundSprite.draw(game.batch);
        game.inventoryButtonSprite.draw(game.batch);
        game.settingsButtonSprite.draw(game.batch);

        float newXpos = (game.settingsButtonSprite.getX() + (game.settingsButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("inventory")).width / 2);
        float newYpos = (game.settingsButtonSprite.getY() + (game.settingsButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("inventory")).height / 2);
        game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("inventory"), newXpos, newYpos);

        newXpos = (game.inventoryButtonSprite.getX() + (game.inventoryButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("home")).width / 2);
        newYpos = (game.inventoryButtonSprite.getY() + (game.inventoryButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("home")).height / 2);
        game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("home"), newXpos, newYpos);

        if (!delete) {
            game.deleteButtonSprite.draw(game.batch);
            newXpos = (game.deleteButtonSprite.getX() + (game.deleteButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("delete")).width / 2);
            newYpos = (game.deleteButtonSprite.getY() + (game.deleteButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("delete")).height / 2);
            game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("delete"), newXpos, newYpos);
        } else {
            if (highlightNumber > 0) {
                game.deleteButtonSprite.draw(game.batch);
                newXpos = (game.deleteButtonSprite.getX() + (game.deleteButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("confirm")).width / 2);
                newYpos = (game.deleteButtonSprite.getY() + (game.deleteButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("confirm")).height / 2);
                game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("confirm"), newXpos, newYpos);
            }
            game.cancelDeleteButtonSprite.draw(game.batch);
            newXpos = (game.cancelDeleteButtonSprite.getX() + (game.cancelDeleteButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("cancel")).width / 2);
            newYpos = (game.cancelDeleteButtonSprite.getY() + (game.cancelDeleteButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("cancel")).height / 2);
            game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("cancel"), newXpos, newYpos);
        }

        game.batch.end();
    }


    public void LoadEventStickventoryObjects() {
        for(int i = invenTop; i < (invenTop + loadSize); i++) {
            String rarityBackground = new String();
            if (game.eventStickventoryList.get(invenTop + i).rarity.equals("1")) {
                rarityBackground = "Large Blue Gun Background";
            } else if (game.eventStickventoryList.get(invenTop + i).rarity.equals("2")) {
                rarityBackground = "Large Purple Gun Background";
            } else if (game.eventStickventoryList.get(invenTop + i).rarity.equals("3")) {
                rarityBackground = "Large Pink Gun Background";
            } else if (game.eventStickventoryList.get(invenTop + i).rarity.equals("4")) {
                rarityBackground = "Large Red Gun Background";
            } else if (game.eventStickventoryList.get(invenTop + i).rarity.equals("5")) {
                rarityBackground = "Large Gold Gun Background";
            }

            Sprite eventStickerBackgroundSprite = new Sprite();
            eventStickerBackgroundSprite = game.gunBackgroundAtlas.createSprite(rarityBackground);
            float imageRatio = eventStickerBackgroundSprite.getWidth() / eventStickerBackgroundSprite.getHeight();
            eventStickerBackgroundSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);

            Texture highlightTexture = new Texture(Gdx.files.internal("Gun Backgrounds/Highlight.png"));
            Sprite highlightSprite = new Sprite(highlightTexture);
            imageRatio = highlightSprite.getWidth() / highlightSprite.getHeight();
            highlightSprite.setSize(Gdx.graphics.getWidth() / 4.5f, (Gdx.graphics.getWidth() / imageRatio) / 4.5f);
            float highlightX = eventStickerBackgroundSprite.getX() + (eventStickerBackgroundSprite.getWidth() / 2);
            float highlightY = eventStickerBackgroundSprite.getY() + (eventStickerBackgroundSprite.getHeight() / 2);
            highlightSprite.setCenter(highlightX, highlightY);

            Sprite eventStickerSprite = new Sprite();
            eventStickerSprite = game.eventStickerTextureAtlas.createSprite(game.eventStickventoryList.get(i).eventStickerTexture);
            imageRatio = eventStickerSprite.getWidth() / eventStickerSprite.getHeight();
            eventStickerSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);

            String type = game.eventStickventoryList.get(i).eventStickerType;
            String skin = game.eventStickventoryList.get(i).eventStickerSkin;
            String texture = game.eventStickventoryList.get(i).eventStickerTexture;
            String rarity = game.eventStickventoryList.get(i).rarity;

            EventStickerObject newEventSticker = new EventStickerObject(game, eventStickerSprite, eventStickerBackgroundSprite, highlightSprite, type, skin, texture, rarity);
            newEventSticker.eventStickerID = game.eventStickventoryList.get(i).eventStickerID;
            newEventSticker.highlighted = game.eventStickventoryList.get(i).highlighted;
            eventStickventoryObjectList.add(newEventSticker);
        }

        if (eventStickventoryObjectList.size() > 0) {
            renderStart = game.inventoryTopBackgroundSprite.getY() - eventStickventoryObjectList.get(0).highlightSprite.getHeight();
        }

        PositionEventStickventory();
    }

    public void RepurposeEventStickventoryObjects() {
        int maxInvenRender = game.maxInven;

        if ((invenTop + (game.maxInven - 1)) >= game.eventStickventoryList.size()) {
            maxInvenRender = game.eventStickventoryList.size() - invenTop;
        }

        for(int i = 0; i < eventStickventoryObjectList.size(); i++) {
            if (i < maxInvenRender) {

                String rarityBackground = new String();
                if (game.eventStickventoryList.get(invenTop + i).rarity.equals("1")) {
                    rarityBackground = "Large Blue Gun Background";
                } else if (game.eventStickventoryList.get(invenTop + i).rarity.equals("2")) {
                    rarityBackground = "Large Purple Gun Background";
                } else if (game.eventStickventoryList.get(invenTop + i).rarity.equals("3")) {
                    rarityBackground = "Large Pink Gun Background";
                } else if (game.eventStickventoryList.get(invenTop + i).rarity.equals("4")) {
                    rarityBackground = "Large Red Gun Background";
                } else if (game.eventStickventoryList.get(invenTop + i).rarity.equals("5")) {
                    rarityBackground = "Large Gold Gun Background";
                }

                Sprite eventStickerBackgroundSprite = new Sprite();
                eventStickerBackgroundSprite = game.gunBackgroundAtlas.createSprite(rarityBackground);
                float imageRatio = eventStickerBackgroundSprite.getWidth() / eventStickerBackgroundSprite.getHeight();
                eventStickerBackgroundSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);

                Sprite eventStickerSprite = new Sprite();
                eventStickerSprite = game.eventStickerTextureAtlas.createSprite(game.eventStickventoryList.get(invenTop + i).eventStickerTexture);
                imageRatio = eventStickerSprite.getWidth() / eventStickerSprite.getHeight();
                eventStickerSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);

                eventStickventoryObjectList.get(i).eventStickerSprite = eventStickerSprite;
                eventStickventoryObjectList.get(i).eventStickerBackgroundSprite = eventStickerBackgroundSprite;
                eventStickventoryObjectList.get(i).eventStickerType = game.eventStickventoryList.get(invenTop + i).eventStickerType;
                eventStickventoryObjectList.get(i).eventStickerSkin = game.eventStickventoryList.get(invenTop + i).eventStickerSkin;
                eventStickventoryObjectList.get(i).eventStickerTexture = game.eventStickventoryList.get(invenTop + i).eventStickerTexture;
                eventStickventoryObjectList.get(i).rarity = game.eventStickventoryList.get(invenTop + i).rarity;
                eventStickventoryObjectList.get(i).eventStickerID = game.eventStickventoryList.get(invenTop + i).eventStickerID;;
                eventStickventoryObjectList.get(i).render = true;
                eventStickventoryObjectList.get(i).highlighted = game.eventStickventoryList.get(invenTop + i).highlighted;
            } else {
                eventStickventoryObjectList.get(i).render = false;
            }
        }
    }

    public void PositionEventStickventory() {

        float newXpos = (Gdx.graphics.getWidth() / 40);
        float newYpos = renderStart;
        float newYpos2 = renderStart;

        Vector2 invenCount = new Vector2(1,1);

        for(int i = 0; i < eventStickventoryObjectList.size(); i++) {
            if (invenCount.y < (game.maxInvenGrid.y + 1)) {
                eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.setPosition(newXpos,newYpos);

                newYpos2 = (eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getY() + eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getHeight()) - (eventStickventoryObjectList.get(i).eventStickerSprite.getHeight() / 1.17f);
                eventStickventoryObjectList.get(i).eventStickerSprite.setPosition(newXpos,newYpos2);

                float highlightX = eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getX() + (eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getWidth() / 2);
                float highlightY = eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getY() + (eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getHeight() / 2);
                eventStickventoryObjectList.get(i).highlightSprite.setCenter(highlightX, highlightY);

                eventStickventoryObjectList.get(i).GenerateText();

                newXpos += eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getWidth() + (Gdx.graphics.getWidth() / 40);
                invenCount.x++;

                if (invenCount.x > game.maxInvenGrid.x) {
                    newXpos = (Gdx.graphics.getWidth() / 40);
                    newYpos -= eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getHeight() + (Gdx.graphics.getWidth() / 40);
                    invenCount.y++;
                    invenCount.x = 1;
                }
            }
        }
    }

    public void ResetEventStickventory() {

        for (int i = 0; i < eventStickventoryObjectList.size(); i++) {
            eventStickventoryObjectList.get(i).highlighted = false;
        }
        for (int i = 0; i < game.eventStickventoryList.size(); i++) {
            game.eventStickventoryList.get(i).highlighted = false;
        }
        highlightNumber = 0;

        game.eventStickventoryList.clear();
        List<List> eventStickventorySQLList = new ArrayList<List>();
        eventStickventorySQLList = game.myRequestHandler.listAllEventStickventory();
        for (int i = 0; i<eventStickventorySQLList.size(); i++) {
            EventSticker newEventSticker  = new EventSticker();
            newEventSticker.eventStickvenID = Integer.parseInt(eventStickventorySQLList.get(i).get(0).toString());
            newEventSticker.eventStickerID = Integer.parseInt(eventStickventorySQLList.get(i).get(1).toString());
            newEventSticker.eventStickerType = game.eventStickerList.get(newEventSticker.eventStickerID - 1).eventStickerType;
            newEventSticker.eventStickerSkin = game.eventStickerList.get(newEventSticker.eventStickerID - 1).eventStickerSkin;
            newEventSticker.eventStickerTexture = game.eventStickerList.get(newEventSticker.eventStickerID - 1).eventStickerTexture;
            newEventSticker.rarity = game.eventStickerList.get(newEventSticker.eventStickerID - 1).rarity;
            newEventSticker.spawnRarity = game.eventStickerList.get(newEventSticker.eventStickerID - 1).spawnRarity;
            newEventSticker.startDate = game.eventStickerList.get(newEventSticker.eventStickerID - 1).startDate;
            newEventSticker.endDate = game.eventStickerList.get(newEventSticker.eventStickerID - 1).endDate;

            newEventSticker.highlighted = false;
            game.eventStickventoryList.add(newEventSticker);
        }
        RepurposeEventStickventoryObjects();
        PositionEventStickventory();

        float newXpos = (Gdx.graphics.getWidth() / 2) - (game.deleteButtonSprite.getWidth() / 2);
        float newYpos = (Gdx.graphics.getWidth() / 80);
        game.deleteButtonSprite.setPosition(newXpos,newYpos);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(100f, 0.0f, 0.0f, 5f, this));
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        game.myRequestHandler.sendTrackerScreenName("Hatventory Screen");

        invenTop = 0;
        loadSize = 0;
        highlightNumber = 0;

        if ((game.eventStickventoryList.size() - invenTop) > game.maxInven) {
            loadSize = game.maxInven;
        } else {
            loadSize = game.eventStickventoryList.size() - invenTop;
        }

        LoadEventStickventoryObjects();
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
        if (((Gdx.graphics.getHeight() -y) < game.inventoryTopBackgroundSprite.getY()) && ((Gdx.graphics.getHeight() -y) > (game.inventoryBottomBackgroundSprite.getY() + game.inventoryBottomBackgroundSprite.getHeight()))) {
            for (int i = 0; i < eventStickventoryObjectList.size(); i++) {
                if ((x > eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getX()) && (x < (eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getX() + eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getY() + eventStickventoryObjectList.get(i).eventStickerBackgroundSprite.getHeight()))) {
                    if (eventStickventoryObjectList.get(i).render) {
                        if (delete) {
                            if (highlightNumber == 0) {
                                float newXpos = ((Gdx.graphics.getWidth() / 10) * 6.35f)  - (game.cancelDeleteButtonSprite.getWidth() / 2);
                                float newYpos = (Gdx.graphics.getWidth() / 80);
                                game.cancelDeleteButtonSprite.setPosition(newXpos,newYpos);
                            }
                            game.eventStickventoryList.get(invenTop + i).highlighted = !game.eventStickventoryList.get(invenTop + i).highlighted;
                            eventStickventoryObjectList.get(i).highlighted = !eventStickventoryObjectList.get(i).highlighted;
                            if (eventStickventoryObjectList.get(i).highlighted) {
                                highlightNumber++;
                            } else {
                                highlightNumber--;
                            }
                            if (highlightNumber == 0) {
                                float newXpos = (Gdx.graphics.getWidth() / 2) - (game.cancelDeleteButtonSprite.getWidth() / 2);
                                float newYpos = (Gdx.graphics.getWidth() / 80);
                                game.cancelDeleteButtonSprite.setPosition(newXpos,newYpos);
                            }
                        }
                    }
                }
            }
        } else if ((x > game.inventoryButtonSprite.getX()) && (x < (game.inventoryButtonSprite.getX() + game.inventoryButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.inventoryButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.inventoryButtonSprite.getY() + game.inventoryButtonSprite.getHeight()))) {
            game.inventoryOpen = false;
            delete = false;
            if (eventStickventoryObjectList.size() > 0) {
                renderStart = game.inventoryTopBackgroundSprite.getY() - eventStickventoryObjectList.get(0).highlightSprite.getHeight();
            }
            eventStickventoryObjectList.clear();
            game.setScreen(game.caseScreen);
        } else if ((x > game.settingsButtonSprite.getX()) && (x < (game.settingsButtonSprite.getX() + game.settingsButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.settingsButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.settingsButtonSprite.getY() + game.settingsButtonSprite.getHeight()))) {
            delete = false;
            if (eventStickventoryObjectList.size() > 0) {
                renderStart = game.inventoryTopBackgroundSprite.getY() - eventStickventoryObjectList.get(0).highlightSprite.getHeight();
            }
            eventStickventoryObjectList.clear();
            game.setScreen(game.inventoryScreen);
        } else if ((x > game.deleteButtonSprite.getX()) && (x < (game.deleteButtonSprite.getX() + game.deleteButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.deleteButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.deleteButtonSprite.getY() + game.deleteButtonSprite.getHeight()))) {
            if (!delete) {
                delete = true;
                float newXpos = ((Gdx.graphics.getWidth() / 10) * 3.65f) - (game.deleteButtonSprite.getWidth() / 2);
                float newYpos = (Gdx.graphics.getWidth() / 80);
                game.deleteButtonSprite.setPosition(newXpos,newYpos);

                newXpos = (Gdx.graphics.getWidth() / 2) - (game.cancelDeleteButtonSprite.getWidth() / 2);
                newYpos = (Gdx.graphics.getWidth() / 80);
                game.cancelDeleteButtonSprite.setPosition(newXpos,newYpos);
            } else if (delete) {
                if (highlightNumber > 0) {
                    delete = false;

                    float newXpos = (Gdx.graphics.getWidth() / 2) - (game.deleteButtonSprite.getWidth() / 2);
                    float newYpos = (Gdx.graphics.getWidth() / 80);
                    game.deleteButtonSprite.setPosition(newXpos,newYpos);

                    for (int i = 0; i<game.eventStickventoryList.size(); i++) {
                        if (game.eventStickventoryList.get(i).highlighted) {
                            game.myRequestHandler.removeEventSticker(game.eventStickventoryList.get(i).eventStickvenID);
                        }
                    }
                    ResetEventStickventory();

                    invenTop = 0;
                    loadSize = 0;
                    highlightNumber = 0;

                    if ((game.eventStickventoryList.size() - invenTop) > game.maxInven) {
                        loadSize = game.maxInven;
                    } else {
                        loadSize = game.eventStickventoryList.size() - invenTop;
                    }

                    eventStickventoryObjectList.clear();
                    LoadEventStickventoryObjects();
                    PositionEventStickventory();
                }
            }
        } else if ((x > game.cancelDeleteButtonSprite.getX()) && (x < (game.cancelDeleteButtonSprite.getX() + game.cancelDeleteButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.cancelDeleteButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.cancelDeleteButtonSprite.getY() + game.cancelDeleteButtonSprite.getHeight()))) {
            delete = false;
            highlightNumber = 0;
            float newXpos = (Gdx.graphics.getWidth() / 2) - (game.deleteButtonSprite.getWidth() / 2);
            float newYpos = (Gdx.graphics.getWidth() / 80);
            game.deleteButtonSprite.setPosition(newXpos,newYpos);
            for (int i = 0; i < game.eventStickventoryList.size(); i++) {
                if (game.eventStickventoryList.get(i).highlighted) {
                    game.eventStickventoryList.get(i).highlighted = false;
                }
            }
            for (int i = 0; i < eventStickventoryObjectList.size(); i++) {
                if (eventStickventoryObjectList.get(i).highlighted) {
                    eventStickventoryObjectList.get(i).highlighted = false;
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
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchDownPoint = new Vector2(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (eventStickventoryObjectList.size() > 0) {
            if ((touchDownPoint.y - screenY) > 0) {
                if ((eventStickventoryObjectList.get(loadSize - 1).highlightSprite.getY() - (Gdx.graphics.getWidth() / 40)) < game.filterButtonSprite.getHeight()) {
                    renderStart += (touchDownPoint.y - screenY);

                    if ((renderStart - (Gdx.graphics.getWidth() / 40) - ((eventStickventoryObjectList.get(0).highlightSprite.getHeight() + (Gdx.graphics.getWidth() / 40)) * (game.maxInvenGrid.y - 1))) > game.filterButtonSprite.getHeight()) {
                        renderStart = ((eventStickventoryObjectList.get(0).highlightSprite.getHeight() + (Gdx.graphics.getWidth() / 40)) * (game.maxInvenGrid.y - 1)) + (Gdx.graphics.getWidth() / 40) + game.filterButtonSprite.getHeight();
                    }
                    if ((invenTop + game.maxInven) < game.eventStickventoryList.size()) {
                        if (eventStickventoryObjectList.get(0).highlightSprite.getY() > game.inventoryTopBackgroundSprite.getY()) {

                            invenTop += game.maxInvenGrid.x;
                            renderStart = eventStickventoryObjectList.get((int) game.maxInvenGrid.x).highlightSprite.getY();
                            RepurposeEventStickventoryObjects();
                        }
                    }
                }
            } else if ((touchDownPoint.y - screenY) < 0) {
                if (((eventStickventoryObjectList.get(0).highlightSprite.getY() + eventStickventoryObjectList.get(0).highlightSprite.getHeight()) > game.inventoryTopBackgroundSprite.getY()) || (invenTop != 0)) {
                    renderStart += (touchDownPoint.y - screenY);

                    if ((renderStart + eventStickventoryObjectList.get(0).highlightSprite.getHeight()) < game.inventoryTopBackgroundSprite.getY()) {
                        renderStart = game.inventoryTopBackgroundSprite.getY() - eventStickventoryObjectList.get(0).highlightSprite.getHeight();
                    }

                    if (invenTop != 0) {
                        if ((eventStickventoryObjectList.get(loadSize - 1).highlightSprite.getY() + eventStickventoryObjectList.get(loadSize - 1).highlightSprite.getHeight()) < 0) {

                            invenTop -= game.maxInvenGrid.x;
                            renderStart = eventStickventoryObjectList.get(0).highlightSprite.getY() + eventStickventoryObjectList.get(0).highlightSprite.getHeight() + (Gdx.graphics.getWidth() / 40);
                            RepurposeEventStickventoryObjects();
                        }
                    }
                }
            }

            PositionEventStickventory();
            touchDownPoint = new Vector2(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
