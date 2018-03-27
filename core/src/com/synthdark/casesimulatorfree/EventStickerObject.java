package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EventStickerObject {

    CaseSimulatorFree game;

    Sprite eventStickerSprite;
    Sprite eventStickerBackgroundSprite;
    Sprite highlightSprite;
    String eventStickerType;
    String eventStickerSkin;
    String eventStickerTexture;
    String rarity;
    double spawnRarity;

    int eventStickerID;
    boolean render;
    boolean highlighted;

    float textXpos;
    float typeShowYpos;
    float skinShowYpos;
    float wearShowYpos;

    public EventStickerObject(CaseSimulatorFree game, Sprite newEventStickerSprite, Sprite newEventStickerBackgroundSprite, Sprite newHighlightSprite, String newEventStickerType, String newEventStickerSkin, String newEventStickerTexture, String newRarity) {
        this.game = game;
        eventStickerSprite = newEventStickerSprite;
        eventStickerBackgroundSprite = newEventStickerBackgroundSprite;
        highlightSprite = newHighlightSprite;
        eventStickerType = newEventStickerType;
        eventStickerSkin = newEventStickerSkin;
        eventStickerTexture = newEventStickerTexture;
        rarity = newRarity;
        render = true;
        highlighted = false;
    }

    public void GenerateText() {
        textXpos = eventStickerBackgroundSprite.getX() + (eventStickerBackgroundSprite.getWidth() / 20);
        typeShowYpos = eventStickerBackgroundSprite.getY() + (eventStickerBackgroundSprite.getHeight() / 3.2f);
        skinShowYpos = eventStickerBackgroundSprite.getY() + (eventStickerBackgroundSprite.getHeight() / 4.8f);
        wearShowYpos = eventStickerBackgroundSprite.getY() + (eventStickerBackgroundSprite.getHeight() / 9.1f);
    }

    public void render(SpriteBatch batch) {
        if (render) {

            if (highlighted) {
                highlightSprite.draw(batch);
            }
            eventStickerBackgroundSprite.draw(batch);
            eventStickerSprite.draw(batch);

            game.Arial_Bold_Normal.draw(batch, eventStickerType, textXpos, typeShowYpos);
            game.Arial_Regular_Normal.draw(batch, eventStickerSkin, textXpos, skinShowYpos);
        }
    }
}