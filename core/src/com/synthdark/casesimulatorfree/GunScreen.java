package com.synthdark.casesimulatorfree;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class GunScreen implements Screen, GestureListener {

	CaseSimulatorFree game;
	InputProcessor gestureDectector;
	 
	public GunScreen(CaseSimulatorFree game){
		this.game = game;
		gestureDectector = new GestureDetector(100f, 0.0f, 0.0f, 5f, this);
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

		game.gunShowBackgroundSprite.draw(game.batch);
        game.tickedGunObject.render(game.batch);
		game.discardButtonSprite.draw(game.batch);
		game.keepButtonSprite.draw(game.batch);
		
		float buttonX = (game.discardButtonSprite.getX() + (game.discardButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("discard")).width / 2);
		float buttonY = (game.discardButtonSprite.getY() + (game.discardButtonSprite.getHeight()) / 2) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("discard")).height / 2);
		game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("discard"), buttonX, buttonY);
		
		buttonX = (game.keepButtonSprite.getX() + (game.keepButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("keep")).width / 2);
		game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("keep"), buttonX, buttonY);
	    
	    game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(gestureDectector);
		if (game.inventoryOpen) {
			game.myRequestHandler.sendTrackerScreenName("Trade Up: Show Gun Screen");
			game.myRequestHandler.sendTrackerEvent("New Item", "Traded for", game.tickedGunObject.gunTexture, 1);
		} else {
			game.myRequestHandler.sendTrackerScreenName("Ticker: Show Gun Screen");
			game.myRequestHandler.sendTrackerEvent("New Item", "Opened case for", game.tickedGunObject.gunTexture, 1);
		}
		
		if (game.userPrefs.getBoolean("sounds", true)) {
			game.caseDisplaySound.play();
		}
		game.tickedGunObject.showGun = true;
		
		if (game.tickedGunObject.rarity.equals("5")) {
			System.out.println("KNIFE");
            GenerateKnife();
		}
		
		float newXpos = (Gdx.graphics.getWidth() / 2) - (game.tickedGunObject.gunBackgroundSprite.getWidth() / 2);
		
		String rarityBackground = new String();
		if (game.tickedGunObject.rarity.equals("1")) {
			rarityBackground = "Blue Gun Background";
		} else if (game.tickedGunObject.rarity.equals("2")) {
			rarityBackground = "Purple Gun Background";
		} else if (game.tickedGunObject.rarity.equals("3")) {
			rarityBackground = "Pink Gun Background";
		} else if (game.tickedGunObject.rarity.equals("4")) {
			rarityBackground = "Red Gun Background";
		} else if (game.tickedGunObject.rarity.equals("5")) {
			rarityBackground = "Gold Gun Background";
		}
		
		game.tickedGunObject.gunBackgroundSprite = game.gunBackgroundAtlas.createSprite("Large " + rarityBackground);
		float imageRatio = game.tickedGunObject.gunBackgroundSprite.getWidth() / game.tickedGunObject.gunBackgroundSprite.getHeight();
		game.tickedGunObject.gunBackgroundSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
		float newYpos = (Gdx.graphics.getHeight() / 2) - (game.tickedGunObject.gunBackgroundSprite.getHeight() / 2);
		game.tickedGunObject.gunBackgroundSprite.setPosition(newXpos, newYpos);
		
		newYpos = (game.tickedGunObject.gunBackgroundSprite.getY() + game.tickedGunObject.gunBackgroundSprite.getHeight()) - (game.tickedGunObject.gunSprite.getHeight() / 1.17f);
		game.tickedGunObject.gunSprite.setPosition(newXpos, newYpos);

        newXpos = game.tickedGunObject.gunBackgroundSprite.getX() + game.tickedGunObject.gunBackgroundSprite.getWidth() - ((game.tickedGunObject.stattrakSprite.getWidth() / 4) * 3);
        newYpos = game.tickedGunObject.gunBackgroundSprite.getY() + game.tickedGunObject.gunBackgroundSprite.getHeight() - ((game.tickedGunObject.stattrakSprite.getHeight() / 4) * 3);
        game.tickedGunObject.stattrakSprite.setPosition(newXpos,newYpos);

		game.tickedGunObject.textXpos = game.tickedGunObject.gunBackgroundSprite.getX() + (game.tickedGunObject.gunBackgroundSprite.getWidth() / 20);
		game.tickedGunObject.typeTickYpos = game.tickedGunObject.gunBackgroundSprite.getY() + (game.tickedGunObject.gunBackgroundSprite.getHeight() / 4.4f);
		game.tickedGunObject.skinTickYpos = game.tickedGunObject.gunBackgroundSprite.getY() + (game.tickedGunObject.gunBackgroundSprite.getHeight() / 8.4f);
		game.tickedGunObject.typeShowYpos = game.tickedGunObject.gunBackgroundSprite.getY() + (game.tickedGunObject.gunBackgroundSprite.getHeight() / 3.2f);
		game.tickedGunObject.skinShowYpos = game.tickedGunObject.gunBackgroundSprite.getY() + (game.tickedGunObject.gunBackgroundSprite.getHeight() / 4.8f);
		game.tickedGunObject.wearShowYpos = game.tickedGunObject.gunBackgroundSprite.getY() + (game.tickedGunObject.gunBackgroundSprite.getHeight() / 9.1f);
	}

	public void GenerateKnife() {
		boolean knifeChecking = true;
		int randomNumberType = 0;

		while (knifeChecking) {
			randomNumberType = game.random.nextInt(game.knivesList.size());

			if (game.caseList.get(game.currentCase).caseName.equals(game.textBundle.get("huntsman_old")) || game.caseList.get(game.currentCase).caseName.equals(game.textBundle.get("huntsman_new"))) {
				if (game.knivesList.get(randomNumberType).knifeType.equals(game.textBundle.get("huntsman"))) {
					knifeChecking = false;
				}
			} else if (game.caseList.get(game.currentCase).caseName.equals(game.textBundle.get("breakout"))) {
                if (game.knivesList.get(randomNumberType).knifeType.equals(game.textBundle.get("butterfly"))) {
                    knifeChecking = false;
                }
            } else if ((game.caseList.get(game.currentCase).caseName.equals(game.textBundle.get("chroma"))) || (game.caseList.get(game.currentCase).caseName.equals(game.textBundle.get("chroma2")))) {
				if (game.knivesList.get(randomNumberType).knifeType.equals("Chroma")) {
					knifeChecking = false;
				}
			} else if (game.caseList.get(game.currentCase).caseName.equals(game.textBundle.get("falchion"))) {
				if (game.knivesList.get(randomNumberType).knifeType.equals(game.textBundle.get("falchion_knife"))) {
					knifeChecking = false;
				}
			} else if (game.caseList.get(game.currentCase).caseName.equals(game.textBundle.get("shadow"))) {
				if (game.knivesList.get(randomNumberType).knifeType.equals(game.textBundle.get("shadowdaggers"))) {
					knifeChecking = false;
				}
			} else {
				if (!game.knivesList.get(randomNumberType).knifeType.equals(game.textBundle.get("huntsman")) && !game.knivesList.get(randomNumberType).knifeType.equals(game.textBundle.get("butterfly")) && !game.knivesList.get(randomNumberType).knifeType.equals("Chroma") && !game.knivesList.get(randomNumberType).knifeType.equals(game.textBundle.get("falchion_knife")) && !game.knivesList.get(randomNumberType).knifeType.equals(game.textBundle.get("shadowdaggers"))) {
					knifeChecking = false;
				}
			}
		}
		
		int randomNumberKnife = game.random.nextInt(game.knivesList.get(randomNumberType).knifeList.size());
		
		game.tickedGunObject.gunSprite = game.knifeTextureAtlas.createSprite(game.knivesList.get(randomNumberType).knifeList.get(randomNumberKnife).gunTexture);
		float imageRatio = game.tickedGunObject.gunSprite.getWidth() / game.tickedGunObject.gunSprite.getHeight();
		game.tickedGunObject.gunSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);

		game.tickedGunObject.gunType = game.knivesList.get(randomNumberType).knifeList.get(randomNumberKnife).gunType;
		game.tickedGunObject.gunSkin = game.knivesList.get(randomNumberType).knifeList.get(randomNumberKnife).gunSkin;
        game.tickedGunObject.gunTexture = game.knivesList.get(randomNumberType).knifeList.get(randomNumberKnife).gunTexture;
        game.tickedGunObject.gunID = game.knivesList.get(randomNumberType).knifeList.get(randomNumberKnife).gunID;
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
		if ((x > game.discardButtonSprite.getX()) && (x < (game.discardButtonSprite.getX() + game.discardButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.discardButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.discardButtonSprite.getY() + game.discardButtonSprite.getHeight()))) {
     		game.tickedGunObject.showGun = false;
     		if (game.inventoryOpen) {
    			game.setScreen(game.inventoryScreen);
     		} else {
    			game.setScreen(game.caseScreen);
     		}
		}
		else if ((x > game.keepButtonSprite.getX()) && (x < (game.keepButtonSprite.getX() + game.keepButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.keepButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.keepButtonSprite.getY() + game.keepButtonSprite.getHeight()))) {
            game.tickedGunObject.showGun = false;
            game.inventoryScreen.tradeUp = false;

            if (game.tickedGunObject.eventSticker) {
                game.myRequestHandler.addEventSticker(game.tickedGunObject.gunID);

                game.eventStickventoryList.clear();
                List<List> eventStickventorySQLList = new ArrayList<List>();
                eventStickventorySQLList = game.myRequestHandler.listAllEventStickventory();
                for (int i = 0; i < eventStickventorySQLList.size(); i++) {
                    EventSticker newEventSticker = new EventSticker();
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
            } else {
                int stattrak;
                if (game.tickedGunObject.stattrak) {
                    stattrak = 1;
                } else {
                    stattrak = 0;
                }
				if (game.tickedGunObject.gunTexture.contains("'")){
					game.tickedGunObject.gunTexture = game.tickedGunObject.gunTexture.replace("'", "''");
					game.tickedGunObject.gunSkin = game.tickedGunObject.gunTexture.replace("'", "''");
				}/*
                if (game.tickedGunObject.gunTexture.equals("AWP - Man-o'-war")) {
                    game.tickedGunObject.gunTexture = "AWP - Man-o''-war";
                    game.tickedGunObject.gunSkin = "Man-o''-war";
                }*/
                game.myRequestHandler.addGun(game.tickedGunObject.gunID, game.tickedGunObject.rarity, game.caseList.get(game.currentCase).caseID, game.tickedGunObject.wear, stattrak);

                game.inventoryList.clear();
                List<List> inventorySQLList = new ArrayList<List>();
                if (game.filterScreen.filterAmount > 0) {
                    inventorySQLList = game.myRequestHandler.filterInventory(game.filterScreen.filterString);
                } else {
                    inventorySQLList = game.myRequestHandler.listAllInventory();
                }
                for (int i = 0; i < inventorySQLList.size(); i++) {
                    Guns newGun = new Guns();
                    List<String> itemSQLList = new ArrayList<String>();
                    newGun.rarity = inventorySQLList.get(i).get(2).toString();

                    if (newGun.rarity.equals("5")) {
                        itemSQLList = game.myRequestHandler.listKnifeWithID(Integer.parseInt(inventorySQLList.get(i).get(1).toString()));
                    } else {
                        itemSQLList = game.myRequestHandler.listGunWithID(Integer.parseInt(inventorySQLList.get(i).get(1).toString()));
                    }

                    newGun.gunID = Integer.parseInt(inventorySQLList.get(i).get(0).toString());
                    newGun.gunType = itemSQLList.get(1).toString();
                    newGun.gunSkin = itemSQLList.get(2).toString();
                    newGun.gunTexture = itemSQLList.get(3).toString();
                    newGun.caseID = Integer.parseInt(inventorySQLList.get(i).get(3).toString());
                    newGun.wear = inventorySQLList.get(i).get(4).toString();
                    if (Integer.parseInt(inventorySQLList.get(i).get(5).toString()) == 0) {
                        newGun.stattrak = false;
                    } else {
                        newGun.stattrak = true;
                    }
                    newGun.highlighted = false;
                    game.inventoryList.add(newGun);
                }
                inventorySQLList.clear();

                if (game.tickedGunObject.rarity.equals("1")) {
                    game.statList.get(0).rarityType1 += 1;
                    game.myRequestHandler.addStat(0, "rarityType1", 1);
                } else if (game.tickedGunObject.rarity.equals("2")) {
                    game.statList.get(0).rarityType2 += 1;
                    game.myRequestHandler.addStat(0, "rarityType2", 1);
                } else if (game.tickedGunObject.rarity.equals("3")) {
                    game.statList.get(0).rarityType3 += 1;
                    game.myRequestHandler.addStat(0, "rarityType3", 1);
                } else if (game.tickedGunObject.rarity.equals("4")) {
                    game.statList.get(0).rarityType4 += 1;
                    game.myRequestHandler.addStat(0, "rarityType4", 1);
                } else if (game.tickedGunObject.rarity.equals("5")) {
                    game.statList.get(0).rarityType5 += 1;
                    game.myRequestHandler.addStat(0, "rarityType5", 1);
                }
                if (game.tickedGunObject.stattrak == true) {
                    game.statList.get(0).stattrak += 1;
                    game.myRequestHandler.addStat(0, "stattrak", 1);
                }
            }
            if (game.inventoryOpen) {
                game.setScreen(game.inventoryScreen);
            } else {
                game.setScreen(game.caseScreen);
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
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
}
