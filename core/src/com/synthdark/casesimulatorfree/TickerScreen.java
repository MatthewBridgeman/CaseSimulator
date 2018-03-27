package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class TickerScreen implements Screen, GestureListener {

	CaseSimulatorFree game;
	InputProcessor gestureDectector;
	
	float tickerSpeed;
	float tickerFriction;
	float frictionAmount;
	boolean tickerFixer;
	boolean gunFound;
	int lastGun;
	float frictionMax;
	boolean frictionReset;
	 
	public TickerScreen(CaseSimulatorFree game){
		this.game = game;
		gestureDectector = new GestureDetector(100f, 0.0f, 0.0f, 5f, this);
     }
     
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//UPDATES OBJECTS\\
		if (tickerSpeed > 0) {	
			moveTicker();		
			tickerSpeed -= (Gdx.graphics.getDeltaTime() * tickerFriction);	
			if (tickerFriction < frictionMax) {
				tickerFriction += (Gdx.graphics.getDeltaTime() * frictionAmount);
			} else if (!frictionReset) {
				float randomNumber = game.random.nextInt(100) -50;
				tickerSpeed = (Gdx.graphics.getWidth() / 1) + randomNumber;
				frictionAmount = (Gdx.graphics.getWidth() / 13f);
				frictionMax = (Gdx.graphics.getWidth() / 6f);
				tickerFriction = (Gdx.graphics.getWidth() / 20f);
				frictionReset = true;
			} else if (tickerSpeed < (Gdx.graphics.getWidth() / 10)) {
				tickerFriction = 0;
			}
			if (tickerSpeed < 60) {
				for (int i = 0; i < game.tickerObjectList.size(); i++) {
					if (((game.tickerObjectList.get(i).gunSprite.getX() + (game.tickerObjectList.get(i).gunSprite.getWidth() / 20)) < (Gdx.graphics.getWidth() / 2)) && ((game.tickerObjectList.get(i).gunSprite.getX() + game.tickerObjectList.get(i).gunSprite.getWidth()) > (Gdx.graphics.getWidth() / 2)))
					{
						gunFound = true;
						game.tickedGunObject = game.tickerObjectList.get(i);
						
						if (game.tickedGunObject.rarity.equals("1")) {
							game.statTotalList.rarityType1 += 1;
							game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType1 += 1;
							game.myRequestHandler.addStat(game.caseList.get(game.currentCase).caseID, "rarityType1", 1);
						} else if (game.tickedGunObject.rarity.equals("2")) {
							game.statTotalList.rarityType2 += 1;
							game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType2 += 1;
							game.myRequestHandler.addStat(game.caseList.get(game.currentCase).caseID, "rarityType2", 1);							
						} else if (game.tickedGunObject.rarity.equals("3")) {
							game.statTotalList.rarityType3 += 1;
							game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType3 += 1;
							game.myRequestHandler.addStat(game.caseList.get(game.currentCase).caseID, "rarityType3", 1);							
						} else if (game.tickedGunObject.rarity.equals("4")) {
							game.statTotalList.rarityType4 += 1;
							game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType4 += 1;
							game.myRequestHandler.addStat(game.caseList.get(game.currentCase).caseID, "rarityType4", 1);							
						} else if (game.tickedGunObject.rarity.equals("5")) {
							game.statTotalList.rarityType5 += 1;
							game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType5 += 1;
							game.myRequestHandler.addStat(game.caseList.get(game.currentCase).caseID, "rarityType5", 1);							
						}
						if (game.tickedGunObject.stattrak == true) {
							game.statTotalList.stattrak += 1;
							game.statList.get(game.caseList.get(game.currentCase).caseID).stattrak += 1;
							game.myRequestHandler.addStat(game.caseList.get(game.currentCase).caseID, "stattrak", 1);							
						}
											
						Timer.schedule(new Task() {
							@Override
				            public void run() {
								game.setScreen(game.gunScreen);
						 	}
						}, 1);
													
						tickerSpeed = 0;
					}
				}
				if (!gunFound) {
					tickerSpeed = 60f;
				}
			}
		}
		
	    game.batch.begin();
	    game.appBackgroundSprite.draw(game.batch);

        game.tickerBackgroundSprite.draw(game.batch);
	    
		for (int i = 0; i < game.tickerObjectList.size(); i++) {
			game.tickerObjectList.get(i).render(game.batch);
		}

        game.appTickerBackgroundSprite.draw(game.batch);
        game.tickerForegroundSprite.draw(game.batch);

        if ((game.userPrefs.getBoolean("holiday_events", true)) && game.snow) {
            for (int i = 0; i < game.snowParticleList.size(); i++) {
                game.snowParticleList.get(i).update();
                if ((((game.snowParticleList.get(i).snowSprite.getX() + game.snowParticleList.get(i).snowSprite.getWidth()) < game.tickerForegroundSprite.getX())  ||
                        (game.snowParticleList.get(i).snowSprite.getX() > (game.tickerForegroundSprite.getX() + game.tickerForegroundSprite.getWidth()))) ||
                        (((game.snowParticleList.get(i).snowSprite.getY() + game.snowParticleList.get(i).snowSprite.getHeight()) < game.tickerForegroundSprite.getY())  ||
                                (game.snowParticleList.get(i).snowSprite.getY() > (game.tickerForegroundSprite.getY() + game.tickerForegroundSprite.getHeight())))) {
                    game.snowParticleList.get(i).render();
                }
            }
        }

		game.batch.end();
	}
	
	public void moveTicker() {
		for (int i = 0; i < game.tickerObjectList.size(); i++) {
			boolean tick = true;
			if ((game.tickerObjectList.get(i).gunSprite.getX() + (game.tickerObjectList.get(i).gunSprite.getWidth() / 2)) < (Gdx.graphics.getWidth() / 2)) {
				tick = false;
			}
			game.tickerObjectList.get(i).update(Gdx.graphics.getDeltaTime() * -tickerSpeed, 0);
			if (((game.tickerObjectList.get(i).gunSprite.getX() + (game.tickerObjectList.get(i).gunSprite.getWidth() / 2)) < (Gdx.graphics.getWidth() / 2)) && (tick == true)) {
	    		if (game.userPrefs.getBoolean("sounds", true)) {
	    			game.caseTickSound.play();
	    		}
			}
			if ((game.tickerObjectList.get(i).gunSprite.getX() + game.tickerObjectList.get(i).gunSprite.getWidth()) < 0 ) {
    			lastGun = game.RepurposeGunObject(i, tickerSpeed, lastGun, false);
			}
		}	
	}
	
	@Override
	public void resize(int width, int height) {
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(gestureDectector);
		game.myRequestHandler.sendTrackerScreenName("Ticker Screen");
		game.myRequestHandler.sendTrackerEvent("Case Opened", "Opened", game.caseList.get(game.currentCase).caseName, 1);
		
		float randomNumber = game.random.nextInt(100) -50;
		tickerSpeed = (Gdx.graphics.getWidth() / 0.6529f) + randomNumber;
		frictionAmount = (Gdx.graphics.getWidth() / 12.8f);
		tickerFriction = 0.0f;
        tickerFixer = false;
        gunFound = false;
		lastGun = 4;
		frictionMax = (Gdx.graphics.getWidth() / 4.2f);
		frictionReset = false;
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
