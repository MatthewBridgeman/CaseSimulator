package com.synthdark.casesimulatorfree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CaseScreen implements Screen, GestureListener { 

	CaseSimulatorFree game;
	InputProcessor gestureDectector;
	
	boolean totalStat;
	Matrix4 fontMatrix;
	SpriteBatch textBatch;

	public CaseScreen(CaseSimulatorFree game){
		this.game = game;
		gestureDectector = new GestureDetector(100f, 0.0f, 0.0f, 5f, this);
		totalStat = false;
		textBatch = new SpriteBatch();
		fontMatrix = new Matrix4();
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

	    game.caseBackgroundSprite.draw(game.batch);
	    game.caseForegroundSprite.draw(game.batch);
        game.inventoryButtonSprite.draw(game.batch);
        game.settingsButtonSprite.draw(game.batch);
	    game.statLeftBackgroundSprite.draw(game.batch);
	    game.statRightBackgroundSprite.draw(game.batch);
	    game.caseSprite.draw(game.batch);
	    game.turnPageSprite.draw(game.batch);

        float newXpos = (game.inventoryButtonSprite.getX() + (game.inventoryButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("inventory")).width / 2);
        float newYpos = (game.inventoryButtonSprite.getY() + (game.inventoryButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("inventory")).height / 2);
        game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("inventory"), newXpos, newYpos);

        newXpos = (game.settingsButtonSprite.getX() + (game.settingsButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("settings")).width / 2);
        newYpos = (game.settingsButtonSprite.getY() + (game.settingsButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("settings")).height / 2);
        game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("settings"), newXpos, newYpos);

        newXpos = (Gdx.graphics.getWidth() / 2) - (game.Arial_Bold_Normal.getBounds(game.caseList.get(game.currentCase).caseName).width / 2 );
		newYpos = game.caseBackgroundSprite.getY() + game.caseBackgroundSprite.getHeight() + game.Arial_Bold_Normal.getBounds(game.caseList.get(game.currentCase).caseName).height + (Gdx.graphics.getWidth() / 40);
		game.Arial_Bold_Normal.draw(game.batch, game.caseList.get(game.currentCase).caseName, newXpos, newYpos);

		//CASE AND TOTAL STATS\\
	    float xStart = (game.statLeftBackgroundSprite.getX() + (game.statLeftBackgroundSprite.getWidth() / 2));
		if (totalStat) {
			//TOTAL STATS\\
			newXpos = xStart - (game.Arial_Bold_Normal.getBounds(game.textBundle.get("total_stats")).width / 2);
			newYpos = (Gdx.graphics.getHeight() / 2) + ((game.Arial_Bold_Normal.getBounds(game.textBundle.get("total_stats")).height + ((game.Arial_Regular_Small.getBounds(game.textBundle.get("total_stats")).height + (Gdx.graphics.getWidth() / 60)) * 7)) / 2);
			game.Arial_Bold_Normal.draw(game.batch, game.textBundle.get("total_stats"), newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small.getBounds(game.textBundle.get("opened") + ": " + game.statTotalList.open).width / 2);
			newYpos -= game.Arial_Bold_Normal.getBounds(game.textBundle.get("total_stats")).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small.draw(game.batch, game.textBundle.get("opened") + ": " + game.statTotalList.open, newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Blue.getBounds(game.textBundle.get("mil-spec") + ": " + game.statTotalList.rarityType1).width / 2);
			newYpos -= game.Arial_Regular_Small.getBounds(game.textBundle.get("opened") + ": " + game.statTotalList.open).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Blue.draw(game.batch, game.textBundle.get("mil-spec") + ": " + game.statTotalList.rarityType1, newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Purple.getBounds(game.textBundle.get("restricted") + ": " + game.statTotalList.rarityType2).width / 2);
			newYpos -= game.Arial_Regular_Small_Blue.getBounds(game.textBundle.get("mil-spec") + ": " + game.statTotalList.rarityType1).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Purple.draw(game.batch, game.textBundle.get("restricted") + ": " + game.statTotalList.rarityType2, newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Pink.getBounds(game.textBundle.get("classified") + ": " + game.statTotalList.rarityType3).width / 2);
			newYpos -= game.Arial_Regular_Small_Purple.getBounds(game.textBundle.get("restricted") + ": " + game.statTotalList.rarityType2).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Pink.draw(game.batch, game.textBundle.get("classified") + ": " + game.statTotalList.rarityType3, newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Red.getBounds(game.textBundle.get("covert") + ": " + game.statTotalList.rarityType4).width / 2);
			newYpos -= game.Arial_Regular_Small_Pink.getBounds(game.textBundle.get("classified") + ": " + game.statTotalList.rarityType3).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Red.draw(game.batch, game.textBundle.get("covert") + ": " + game.statTotalList.rarityType4, newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Gold.getBounds(game.textBundle.get("ex_rare") + ": " + game.statTotalList.rarityType5).width / 2);
			newYpos -= game.Arial_Regular_Small_Red.getBounds(game.textBundle.get("covert") + ": " + game.statTotalList.rarityType4).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Gold.draw(game.batch, game.textBundle.get("ex_rare") + ": " + game.statTotalList.rarityType5, newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small.getBounds(game.textBundle.get("stattrak") + ": " + game.statTotalList.stattrak).width / 2);
			newYpos -= game.Arial_Regular_Small_Gold.getBounds(game.textBundle.get("ex_rare") + ": " + game.statTotalList.rarityType5).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small.draw(game.batch, game.textBundle.get("stattrak") + ": " + game.statTotalList.stattrak, newXpos, newYpos);
		} else {
			//CASE STATS\\		    
			newXpos = xStart - (game.Arial_Bold_Normal.getBounds(game.textBundle.get("case_stats")).width / 2);
			newYpos = (Gdx.graphics.getHeight() / 2) + ((game.Arial_Bold_Normal.getBounds(game.textBundle.get("case_stats")).height + ((game.Arial_Regular_Small.getBounds(game.textBundle.get("case_stats")).height + (Gdx.graphics.getWidth() / 60)) * 7)) / 2);
			game.Arial_Bold_Normal.draw(game.batch, game.textBundle.get("case_stats"), newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small.getBounds(game.textBundle.get("opened") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).open)).width / 2);
			newYpos -= game.Arial_Bold_Normal.getBounds(game.textBundle.get("case_stats")).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small.draw(game.batch, game.textBundle.get("opened") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).open), newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Blue.getBounds(game.textBundle.get("mil-spec") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType1)).width / 2);
			newYpos -= game.Arial_Regular_Small.getBounds(game.textBundle.get("opened") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).open)).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Blue.draw(game.batch, game.textBundle.get("mil-spec") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType1), newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Purple.getBounds(game.textBundle.get("restricted") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType2)).width / 2);
			newYpos -= game.Arial_Regular_Small_Blue.getBounds(game.textBundle.get("mil-spec") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType1)).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Purple.draw(game.batch, game.textBundle.get("restricted") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType2), newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Pink.getBounds(game.textBundle.get("classified") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType3)).width / 2);
			newYpos -= game.Arial_Regular_Small_Purple.getBounds(game.textBundle.get("restricted") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType2)).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Pink.draw(game.batch, game.textBundle.get("classified") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType3), newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Red.getBounds(game.textBundle.get("covert") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType4)).width / 2);
			newYpos -= game.Arial_Regular_Small_Pink.getBounds(game.textBundle.get("classified") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType3)).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Red.draw(game.batch, game.textBundle.get("covert") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType4), newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small_Gold.getBounds(game.textBundle.get("ex_rare") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType5)).width / 2);
			newYpos -= game.Arial_Regular_Small_Red.getBounds(game.textBundle.get("covert") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType4)).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small_Gold.draw(game.batch, game.textBundle.get("ex_rare") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType5), newXpos, newYpos);
			
			newXpos = xStart - (game.Arial_Regular_Small.getBounds(game.textBundle.get("stattrak") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).stattrak)).width / 2);
			newYpos -= game.Arial_Regular_Small_Gold.getBounds(game.textBundle.get("ex_rare") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).rarityType5)).height + (Gdx.graphics.getWidth() / 60);
			game.Arial_Regular_Small.draw(game.batch, game.textBundle.get("stattrak") + ": " + Integer.toString(game.statList.get(game.caseList.get(game.currentCase).caseID).stattrak), newXpos, newYpos);
		}
		
		//CURRENT STATS\\	
	    xStart = (game.statRightBackgroundSprite.getX() + (game.statRightBackgroundSprite.getWidth() / 2));
		newXpos = xStart - (game.Arial_Bold_Normal.getBounds(game.textBundle.get("inven_stats")).width / 2);
		newYpos = (Gdx.graphics.getHeight() / 2) + ((game.Arial_Bold_Normal.getBounds(game.textBundle.get("inven_stats")).height + ((game.Arial_Regular_Small.getBounds(game.textBundle.get("inven_stats")).height + (Gdx.graphics.getWidth() / 60)) * 7)) / 2);
		game.Arial_Bold_Normal.draw(game.batch, game.textBundle.get("inven_stats"), newXpos, newYpos);
		
		newXpos = xStart - (game.Arial_Regular_Small.getBounds(game.textBundle.get("total") + ": " + Integer.toString(game.statList.get(0).open)).width / 2);
		newYpos -= game.Arial_Bold_Normal.getBounds(game.textBundle.get("inven_stats")).height + (Gdx.graphics.getWidth() / 60);
		game.Arial_Regular_Small.draw(game.batch, game.textBundle.get("total") + ": " + Integer.toString(game.statList.get(0).open), newXpos, newYpos);
		
		newXpos = xStart - (game.Arial_Regular_Small_Blue.getBounds(game.textBundle.get("mil-spec") + ": " + Integer.toString(game.statList.get(0).rarityType1)).width / 2);
		newYpos -= game.Arial_Regular_Small.getBounds(game.textBundle.get("total") + ": " + Integer.toString(game.statList.get(0).open)).height + (Gdx.graphics.getWidth() / 60);
		game.Arial_Regular_Small_Blue.draw(game.batch, game.textBundle.get("mil-spec") + ": " + Integer.toString(game.statList.get(0).rarityType1), newXpos, newYpos);
		
		newXpos = xStart - (game.Arial_Regular_Small_Purple.getBounds(game.textBundle.get("restricted") + ": " + Integer.toString(game.statList.get(0).rarityType2)).width / 2);
		newYpos -= game.Arial_Regular_Small_Blue.getBounds(game.textBundle.get("mil-spec") + ": " + Integer.toString(game.statList.get(0).rarityType1)).height + (Gdx.graphics.getWidth() / 60);
		game.Arial_Regular_Small_Purple.draw(game.batch, game.textBundle.get("restricted") + ": " + Integer.toString(game.statList.get(0).rarityType2), newXpos, newYpos);
		
		newXpos = xStart - (game.Arial_Regular_Small_Pink.getBounds(game.textBundle.get("classified") + ": " + Integer.toString(game.statList.get(0).rarityType3)).width / 2);
		newYpos -= game.Arial_Regular_Small_Purple.getBounds(game.textBundle.get("restricted") + ": " + Integer.toString(game.statList.get(0).rarityType2)).height + (Gdx.graphics.getWidth() / 60);
		game.Arial_Regular_Small_Pink.draw(game.batch, game.textBundle.get("classified") + ": " + Integer.toString(game.statList.get(0).rarityType3), newXpos, newYpos);
		
		newXpos = xStart - (game.Arial_Regular_Small_Red.getBounds(game.textBundle.get("covert") + ": " + Integer.toString(game.statList.get(0).rarityType4)).width / 2);
		newYpos -= game.Arial_Regular_Small_Pink.getBounds(game.textBundle.get("classified") + ": " + Integer.toString(game.statList.get(0).rarityType3)).height + (Gdx.graphics.getWidth() / 60);
		game.Arial_Regular_Small_Red.draw(game.batch, game.textBundle.get("covert") + ": " + Integer.toString(game.statList.get(0).rarityType4), newXpos, newYpos);
		
		newXpos = xStart - (game.Arial_Regular_Small_Gold.getBounds(game.textBundle.get("ex_rare") + ": " + Integer.toString(game.statList.get(0).rarityType5)).width / 2);
		newYpos -= game.Arial_Regular_Small_Red.getBounds(game.textBundle.get("covert") + ": " + Integer.toString(game.statList.get(0).rarityType4)).height + (Gdx.graphics.getWidth() / 60);
		game.Arial_Regular_Small_Gold.draw(game.batch, game.textBundle.get("ex_rare") + ": " + Integer.toString(game.statList.get(0).rarityType5), newXpos, newYpos);
		
		newXpos = xStart - (game.Arial_Regular_Small.getBounds(game.textBundle.get("stattrak") + ": " + Integer.toString(game.statList.get(0).stattrak)).width / 2);
		newYpos -= game.Arial_Regular_Small_Gold.getBounds(game.textBundle.get("ex_rare") + ": " + Integer.toString(game.statList.get(0).rarityType5)).height + (Gdx.graphics.getWidth() / 60);
		game.Arial_Regular_Small.draw(game.batch, game.textBundle.get("stattrak") + ": " + Integer.toString(game.statList.get(0).stattrak), newXpos, newYpos);
		
		//CONTAINS\\
		newXpos = (Gdx.graphics.getWidth() / 2) - (game.Arial_Bold_Normal.getBounds(game.textBundle.get("contains") + ":").width / 2);
		newYpos = game.caseBackgroundSprite.getY() - (Gdx.graphics.getHeight() / 60);
		game.Arial_Bold_Normal.draw(game.batch, game.textBundle.get("contains") + ":", newXpos, newYpos);

		newXpos = 0 + (Gdx.graphics.getWidth() / 80);
		newYpos -= (Gdx.graphics.getHeight() / 20);

		int gunNumber = 0;
		
		if (!game.caseList.get(game.currentCase).caseType.equals("sticker")) {
			for (int i = 0; i < game.caseList.get(game.currentCase).gunList.size(); i++) {
				newXpos += game.Arial_Regular_Small.getBounds(game.caseList.get(game.currentCase).gunList.get(i).gunType + " | " + game.caseList.get(game.currentCase).gunList.get(i).gunSkin + "    ").width;
				if (newXpos > (Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 80))) {
					float xCalculate = newXpos - game.Arial_Regular_Small.getBounds(" - " + game.caseList.get(game.currentCase).gunList.get(i).gunType + " | " + game.caseList.get(game.currentCase).gunList.get(i).gunSkin + "    ").width;
					newXpos = (Gdx.graphics.getWidth() / 2) - (xCalculate / 2);
					
					for (int j = gunNumber; j < (i - 1); j++) {
						game.Arial_Regular_Small.draw(game.batch, game.caseList.get(game.currentCase).gunList.get(j).gunType + " | " + game.caseList.get(game.currentCase).gunList.get(j).gunSkin + "    ", newXpos, newYpos);
						newXpos += game.Arial_Regular_Small.getBounds(game.caseList.get(game.currentCase).gunList.get(j).gunType + " | " + game.caseList.get(game.currentCase).gunList.get(j).gunSkin + "    ").width;
					}
					game.Arial_Regular_Small.draw(game.batch, game.caseList.get(game.currentCase).gunList.get(i - 1).gunType + " | " + game.caseList.get(game.currentCase).gunList.get(i - 1).gunSkin, newXpos, newYpos);
					
					newYpos -= (Gdx.graphics.getHeight() / 20);
					gunNumber = i;
					i--;
					newXpos = Gdx.graphics.getWidth() / 80;	
				}
				if (i == (game.caseList.get(game.currentCase).gunList.size() - 1)) {
					float xCalculate = newXpos - game.Arial_Regular_Small.getBounds(" |     ").width;
					newXpos = (Gdx.graphics.getWidth() / 2) - (xCalculate / 2);
					for (int j = gunNumber; j < i; j++) {
						game.Arial_Regular_Small.draw(game.batch, game.caseList.get(game.currentCase).gunList.get(j).gunType + " | " + game.caseList.get(game.currentCase).gunList.get(j).gunSkin + "    ", newXpos, newYpos);
						newXpos += game.Arial_Regular_Small.getBounds(game.caseList.get(game.currentCase).gunList.get(j).gunType + " | " + game.caseList.get(game.currentCase).gunList.get(j).gunSkin + "    ").width;
					}
					if (game.caseList.get(game.currentCase).gunList.get(i).gunSkin.equals("")) {
						game.Arial_Regular_Small.draw(game.batch, game.caseList.get(game.currentCase).gunList.get(i).gunType, newXpos, newYpos);
					} else {
						game.Arial_Regular_Small.draw(game.batch, game.caseList.get(game.currentCase).gunList.get(i).gunType + " | " + game.caseList.get(game.currentCase).gunList.get(i).gunSkin, newXpos, newYpos);
					}
				}
			}
		} else {
			for (int i = 0; i < game.caseList.get(game.currentCase).gunList.size(); i++) {
				newXpos += game.Arial_Regular_Small.getBounds(game.caseList.get(game.currentCase).gunList.get(i).gunType + "    ").width;
				if (newXpos > (Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 80))) {
					float xCalculate = newXpos - game.Arial_Regular_Small.getBounds(game.caseList.get(game.currentCase).gunList.get(i).gunType + "        ").width;
					newXpos = (Gdx.graphics.getWidth() / 2) - (xCalculate / 2);
					
					for (int j = gunNumber; j < (i - 1); j++) {
						game.Arial_Regular_Small.draw(game.batch, game.caseList.get(game.currentCase).gunList.get(j).gunType + "    ", newXpos, newYpos);
						newXpos += game.Arial_Regular_Small.getBounds(game.caseList.get(game.currentCase).gunList.get(j).gunType + "    ").width;
					}
					game.Arial_Regular_Small.draw(game.batch, game.caseList.get(game.currentCase).gunList.get(i - 1).gunType, newXpos, newYpos);
					
					newYpos -= (Gdx.graphics.getHeight() / 20);
					gunNumber = i;
					i--;
					newXpos = Gdx.graphics.getWidth() / 80;	
				}
				if (i == (game.caseList.get(game.currentCase).gunList.size() - 1)) {
					float xCalculate = newXpos - game.Arial_Regular_Small.getBounds("     ").width;
					newXpos = (Gdx.graphics.getWidth() / 2) - (xCalculate / 2);
					for (int j = gunNumber; j < i; j++) {
						game.Arial_Regular_Small.draw(game.batch, game.caseList.get(game.currentCase).gunList.get(j).gunType + "    ", newXpos, newYpos);
						newXpos += game.Arial_Regular_Small.getBounds(game.caseList.get(game.currentCase).gunList.get(j).gunType + "    ").width;
					}
					game.Arial_Regular_Small.draw(game.batch, game.caseList.get(game.currentCase).gunList.get(i).gunType, newXpos, newYpos);
				}
			}
		}	    
	    game.batch.end();
	    
	    textBatch.begin();
	    fontMatrix.setToRotation(new Vector3(0,0,1), 90);
		fontMatrix.setTranslation(new Vector3(game.turnPageSprite.getX(), game.turnPageSprite.getY(), 0));
		textBatch.setTransformMatrix(fontMatrix);
		game.Arial_Regular_Smallest.draw(textBatch, game.textBundle.get("switch_view"), (game.turnPageSprite.getWidth() * 1.3f), -(game.Arial_Regular_Smallest.getBounds(game.textBundle.get("switch_view")).height * 1.5f));
		textBatch.end();
	    
	}

	@Override
	public void resize(int width, int height) {
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(gestureDectector);
		game.myRequestHandler.sendTrackerScreenName("Case Screen");
		for (int i = 0; i < game.tickerObjectList.size(); i++) {
			int lastGun = game.RepurposeGunObject(i, 0, i, true);
		}
		
		if (game.caseJustOpened && (game.sessionCaseOpened != 0) && (game.sessionCaseOpened % 10 == 0)) {
			game.myRequestHandler.showInterstital();
		}
		game.caseJustOpened = false;
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
		if ((x > game.caseSprite.getX()) && (x < (game.caseSprite.getX() + game.caseSprite.getWidth())) && ((Gdx.graphics.getHeight() - y) > game.caseSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.caseSprite.getY() + game.caseSprite.getHeight()))) {
			game.setScreen(game.tickerScreen);
			game.statTotalList.open += 1;
			game.statList.get(0).open += 1;
			game.statList.get(game.caseList.get(game.currentCase).caseID).open += 1;
			game.myRequestHandler.addStat(0, "open", 1);
			game.myRequestHandler.addStat(game.caseList.get(game.currentCase).caseID, "open", 1);
			game.sessionCaseOpened += 1;
			game.caseJustOpened = true;

			if (game.userPrefs.getBoolean("sounds", true)) {
    			game.caseOpenSound.play();
    		}
		} else if ((x > game.inventoryButtonSprite.getX()) && (x < (game.inventoryButtonSprite.getX() + game.inventoryButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.inventoryButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.inventoryButtonSprite.getY() + game.inventoryButtonSprite.getHeight()))) {
			game.inventoryOpen = true;
			game.setScreen(game.inventoryScreen);
		} else if ((x > game.settingsButtonSprite.getX()) && (x < (game.settingsButtonSprite.getX() + game.settingsButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.settingsButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.settingsButtonSprite.getY() + game.settingsButtonSprite.getHeight()))) {
            game.setScreen(game.settingsScreen);
        } else if ((x > game.statLeftBackgroundSprite.getX()) && (x < (game.statLeftBackgroundSprite.getX() + game.statLeftBackgroundSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.statLeftBackgroundSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.statLeftBackgroundSprite.getY() + game.statLeftBackgroundSprite.getHeight()))) {
			totalStat = !totalStat;
		}
		
		return false;
	}
	
	@Override
	public boolean longPress(float x, float y) {
		return false;
	}
	
	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if(Math.abs(velocityX)>Math.abs(velocityY)) {
			float imageRatio;
        	if(velocityX<-1000) {
        		game.currentCase++;
        		if (game.currentCase > (game.caseList.size() - 1)){
        			game.currentCase = 0;
        		}
        		game.caseSprite = game.caseTextureAtlas.createSprite(game.caseList.get(game.currentCase).caseTexture);
				System.out.println(game.caseList.get(game.currentCase).caseTexture);
        		if (game.caseSprite.getWidth() > game.caseSprite.getHeight()) {
        			imageRatio = game.caseSprite.getWidth() / game.caseSprite.getHeight();
	        		game.caseSprite.setSize(Gdx.graphics.getWidth() / 3f, (Gdx.graphics.getWidth() / imageRatio) / 3f);
        		} else {
        			imageRatio = game.caseSprite.getHeight() / game.caseSprite.getWidth();
	        		game.caseSprite.setSize((Gdx.graphics.getWidth() / imageRatio) / 4.1f, Gdx.graphics.getWidth() / 4.1f);
        		}
        		float newXpos = (Gdx.graphics.getWidth() / 2) - (game.caseSprite.getWidth() / 2);
        		float newYpos = (Gdx.graphics.getHeight() / 2) - (game.caseSprite.getHeight() / 2);
        		game.caseSprite.setPosition(newXpos,newYpos);
            } else if (velocityX>1000) {
            	game.currentCase--;
            	if (game.currentCase < 0){
            		game.currentCase = (game.caseList.size() - 1);
            	}
            	game.caseSprite = game.caseTextureAtlas.createSprite(game.caseList.get(game.currentCase).caseTexture);
            	if (game.caseSprite.getWidth() > game.caseSprite.getHeight()) {
        			imageRatio = game.caseSprite.getWidth() / game.caseSprite.getHeight();
	        		game.caseSprite.setSize(Gdx.graphics.getWidth() / 3f, (Gdx.graphics.getWidth() / imageRatio) / 3f);
        		} else {
        			imageRatio = game.caseSprite.getHeight() / game.caseSprite.getWidth();
	        		game.caseSprite.setSize((Gdx.graphics.getWidth() / imageRatio) / 4.1f, Gdx.graphics.getWidth() / 4.1f);
        		}
        		float newXpos = (Gdx.graphics.getWidth() / 2) - (game.caseSprite.getWidth() / 2);
        		float newYpos = (Gdx.graphics.getHeight() / 2) - (game.caseSprite.getHeight() / 2);
        		game.caseSprite.setPosition(newXpos,newYpos);
            }

    		for (int i = 0; i < game.tickerObjectList.size(); i++) {
    			int lastGun = game.RepurposeGunObject(i, 0, i, true);
    		}
		}
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