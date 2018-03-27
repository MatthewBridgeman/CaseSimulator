package com.synthdark.casesimulatorfree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class FilterScreen implements Screen, GestureListener, InputProcessor {

	CaseSimulatorFree game;
	InputProcessor gestureDectector;

	List<FilterObject> caseFilterList;
	List<FilterObject> rarityFilterList;
	List<FilterObject> qualityFilterList;
	FilterObject stattrakFilter;
	List<FilterObject> pistolFilterList;
	List<FilterObject> heavyFilterList;
	List<FilterObject> subFilterList;
	List<FilterObject> rifleFilterList;
	List<FilterObject> knifeFilterList;
	boolean ticked;
	Vector2 renderStart;
	Vector2 touchDownPoint;
	int filterAmount;
	String filterString;
	
	public FilterScreen(CaseSimulatorFree game){
		this.game = game;
		gestureDectector = new GestureDetector(100f, 0.0f, 0.0f, 5f, this);
		
		renderStart = new Vector2((Gdx.graphics.getWidth() / 40), game.inventoryTopBackgroundSprite.getY());
		filterAmount = 0;
		ticked = false;
		
		//CASES\\
		caseFilterList = new ArrayList<FilterObject>();
		float newXpos = renderStart.x;
		float newYpos = renderStart.y - game.Arial_Bold_Large.getBounds(game.textBundle.get("cases")).height - (Gdx.graphics.getWidth() / 50);
		for (int i = 0; i<game.caseList.size(); i++) {
			FilterObject newFilterObject = new FilterObject(game, game.caseList.get(i).caseName, game.caseList.get(i).caseID, newXpos, newYpos);
			if (newFilterObject.renderPos.x + newFilterObject.bounds.x > Gdx.graphics.getWidth()) {
				newXpos = (Gdx.graphics.getWidth() / 40);
				newYpos -= newFilterObject.bounds.y + (Gdx.graphics.getWidth() / 100);
				newFilterObject.renderPos = new Vector2(newXpos, newYpos);
				newFilterObject.tickBoxSprite.setPosition(newXpos,newYpos - (newFilterObject.tickBoxSprite.getHeight() / 2));
			}
			caseFilterList.add(newFilterObject);
			newXpos += newFilterObject.bounds.x + (Gdx.graphics.getWidth() / 40);
		}
		
		//RARITY\\
		rarityFilterList = new ArrayList<FilterObject>();
		List<String> tempFilterList = new ArrayList<String>(Arrays.asList(game.textBundle.get("mil-spec"), game.textBundle.get("restricted"), game.textBundle.get("classified"), game.textBundle.get("covert"), game.textBundle.get("ex_rare")));
		newXpos = renderStart.x;
		newYpos = caseFilterList.get(caseFilterList.size() - 1).tickBoxSprite.getY() - game.Arial_Bold_Large.getBounds(game.textBundle.get("rarity")).height - (Gdx.graphics.getWidth() / 25);
		for (int i = 0; i<tempFilterList.size(); i++) {
			FilterObject newFilterObject = new FilterObject(game, tempFilterList.get(i), -1, newXpos, newYpos);
			if (newFilterObject.renderPos.x + newFilterObject.bounds.x > Gdx.graphics.getWidth()) {
				newXpos = (Gdx.graphics.getWidth() / 40);
				newYpos -= newFilterObject.bounds.y + (Gdx.graphics.getWidth() / 100);
				newFilterObject.renderPos = new Vector2(newXpos, newYpos);
				newFilterObject.tickBoxSprite.setPosition(newXpos,newYpos - (newFilterObject.tickBoxSprite.getHeight() / 2));
			}
			rarityFilterList.add(newFilterObject);
			newXpos += newFilterObject.bounds.x + (Gdx.graphics.getWidth() / 40);
		}
		
		//QUALITY\\
		qualityFilterList = new ArrayList<FilterObject>();
		tempFilterList = new ArrayList<String>(Arrays.asList(game.textBundle.get("battle-scarred"), game.textBundle.get("well-worn"), game.textBundle.get("field-tested"), game.textBundle.get("minimal-wear"), game.textBundle.get("factory-new")));
		newXpos = renderStart.x;
		newYpos = rarityFilterList.get(rarityFilterList.size() - 1).tickBoxSprite.getY() - game.Arial_Bold_Large.getBounds(game.textBundle.get("exterior")).height - (Gdx.graphics.getWidth() / 25);
		for (int i = 0; i<tempFilterList.size(); i++) {
			FilterObject newFilterObject = new FilterObject(game, tempFilterList.get(i), -1, newXpos, newYpos);
			if (newFilterObject.renderPos.x + newFilterObject.bounds.x > Gdx.graphics.getWidth()) {
				newXpos = (Gdx.graphics.getWidth() / 40);
				newYpos -= newFilterObject.bounds.y + (Gdx.graphics.getWidth() / 100);
				newFilterObject.renderPos = new Vector2(newXpos, newYpos);
				newFilterObject.tickBoxSprite.setPosition(newXpos,newYpos - (newFilterObject.tickBoxSprite.getHeight() / 2));
			}
			qualityFilterList.add(newFilterObject);
			newXpos += newFilterObject.bounds.x + (Gdx.graphics.getWidth() / 40);
		}
		
		//STATTRAK\\
		newXpos = renderStart.x;
		newYpos = qualityFilterList.get(qualityFilterList.size() - 1).tickBoxSprite.getY() - game.Arial_Bold_Large.getBounds(game.textBundle.get("stattrak")).height - (Gdx.graphics.getWidth() / 25);
		stattrakFilter = new FilterObject(game, game.textBundle.get("stattrak"), 0, newXpos, newYpos);

		//PISTOLS\\
		pistolFilterList = new ArrayList<FilterObject>();
		tempFilterList = new ArrayList<String>(Arrays.asList(game.textBundle.get("glock"), game.textBundle.get("p2000"), game.textBundle.get("usp-s"), game.textBundle.get("p250"), game.textBundle.get("cz75"), game.textBundle.get("dual_beret"), game.textBundle.get("tec-9"), game.textBundle.get("five-seven"), game.textBundle.get("deagle")));
		newXpos = renderStart.x;
		newYpos = stattrakFilter.tickBoxSprite.getY() - game.Arial_Bold_Large.getBounds(game.textBundle.get("pistols")).height - (Gdx.graphics.getWidth() / 25);
		for (int i = 0; i<tempFilterList.size(); i++) {
			FilterObject newFilterObject = new FilterObject(game, tempFilterList.get(i), -1, newXpos, newYpos);
			if (newFilterObject.renderPos.x + newFilterObject.bounds.x > Gdx.graphics.getWidth()) {
				newXpos = (Gdx.graphics.getWidth() / 40);
				newYpos -= newFilterObject.bounds.y + (Gdx.graphics.getWidth() / 100);
				newFilterObject.renderPos = new Vector2(newXpos, newYpos);
				newFilterObject.tickBoxSprite.setPosition(newXpos,newYpos - (newFilterObject.tickBoxSprite.getHeight() / 2));
			}
			pistolFilterList.add(newFilterObject);
			newXpos += newFilterObject.bounds.x + (Gdx.graphics.getWidth() / 40);
		}
				
		//HEAVY\\
		heavyFilterList = new ArrayList<FilterObject>();
		tempFilterList = new ArrayList<String>(Arrays.asList(game.textBundle.get("nova"), game.textBundle.get("xm1014"), game.textBundle.get("sawed-off"), game.textBundle.get("mag-7"), game.textBundle.get("m249"), game.textBundle.get("negev")));
		newXpos = renderStart.x;
		newYpos = pistolFilterList.get(pistolFilterList.size() - 1).tickBoxSprite.getY() - game.Arial_Bold_Large.getBounds(game.textBundle.get("heavy")).height - (Gdx.graphics.getWidth() / 25);
		for (int i = 0; i<tempFilterList.size(); i++) {
			FilterObject newFilterObject = new FilterObject(game, tempFilterList.get(i), -1, newXpos, newYpos);
			if (newFilterObject.renderPos.x + newFilterObject.bounds.x > Gdx.graphics.getWidth()) {
				newXpos = (Gdx.graphics.getWidth() / 40);
				newYpos -= newFilterObject.bounds.y + (Gdx.graphics.getWidth() / 100);
				newFilterObject.renderPos = new Vector2(newXpos, newYpos);
				newFilterObject.tickBoxSprite.setPosition(newXpos,newYpos - (newFilterObject.tickBoxSprite.getHeight() / 2));
			}
			heavyFilterList.add(newFilterObject);
			newXpos += newFilterObject.bounds.x + (Gdx.graphics.getWidth() / 40);
		}
		
		//SUBMACHINE\\
		subFilterList = new ArrayList<FilterObject>();
		tempFilterList = new ArrayList<String>(Arrays.asList(game.textBundle.get("mac-10"), game.textBundle.get("mp7"), game.textBundle.get("mp9"), game.textBundle.get("ump-45"), game.textBundle.get("pp-bizon"), game.textBundle.get("p90")));
		newXpos = renderStart.x;
		newYpos = heavyFilterList.get(heavyFilterList.size() - 1).tickBoxSprite.getY() - game.Arial_Bold_Large.getBounds(game.textBundle.get("submachine")).height - (Gdx.graphics.getWidth() / 25);
		for (int i = 0; i<tempFilterList.size(); i++) {
			FilterObject newFilterObject = new FilterObject(game, tempFilterList.get(i), -1, newXpos, newYpos);
			if (newFilterObject.renderPos.x + newFilterObject.bounds.x > Gdx.graphics.getWidth()) {
				newXpos = (Gdx.graphics.getWidth() / 40);
				newYpos -= newFilterObject.bounds.y + (Gdx.graphics.getWidth() / 100);
				newFilterObject.renderPos = new Vector2(newXpos, newYpos);
				newFilterObject.tickBoxSprite.setPosition(newXpos,newYpos - (newFilterObject.tickBoxSprite.getHeight() / 2));
			}
			subFilterList.add(newFilterObject);
			newXpos += newFilterObject.bounds.x + (Gdx.graphics.getWidth() / 40);
		}
		
		//RIFLES\\
		rifleFilterList = new ArrayList<FilterObject>();
		tempFilterList = new ArrayList<String>(Arrays.asList(game.textBundle.get("galil"), game.textBundle.get("famas"), game.textBundle.get("ak-47"), game.textBundle.get("m4a4"), game.textBundle.get("m4a1-s"), game.textBundle.get("ssg_08"), game.textBundle.get("aug"), game.textBundle.get("sg_553"), game.textBundle.get("awp"), game.textBundle.get("g3sg1"), game.textBundle.get("scar-20")));
		newXpos = renderStart.x;
		newYpos = subFilterList.get(subFilterList.size() - 1).tickBoxSprite.getY() - game.Arial_Bold_Large.getBounds(game.textBundle.get("rifles")).height - (Gdx.graphics.getWidth() / 25);
		for (int i = 0; i<tempFilterList.size(); i++) {
			FilterObject newFilterObject = new FilterObject(game, tempFilterList.get(i), -1, newXpos, newYpos);
			if (newFilterObject.renderPos.x + newFilterObject.bounds.x > Gdx.graphics.getWidth()) {
				newXpos = (Gdx.graphics.getWidth() / 40);
				newYpos -= newFilterObject.bounds.y + (Gdx.graphics.getWidth() / 100);
				newFilterObject.renderPos = new Vector2(newXpos, newYpos);
				newFilterObject.tickBoxSprite.setPosition(newXpos,newYpos - (newFilterObject.tickBoxSprite.getHeight() / 2));
			}
			rifleFilterList.add(newFilterObject);
			newXpos += newFilterObject.bounds.x + (Gdx.graphics.getWidth() / 40);
		}
		
		//KNIFES\\
		knifeFilterList = new ArrayList<FilterObject>();
		tempFilterList = new ArrayList<String>(Arrays.asList(game.textBundle.get("bayonet"), game.textBundle.get("flip_knife"), game.textBundle.get("gut_knife"), game.textBundle.get("huntsman"), game.textBundle.get("karambit"), game.textBundle.get("m9_bayonet"), game.textBundle.get("butterfly")));
		newXpos = renderStart.x;
		newYpos = rifleFilterList.get(rifleFilterList.size() - 1).tickBoxSprite.getY() - game.Arial_Bold_Large.getBounds(game.textBundle.get("knives")).height - (Gdx.graphics.getWidth() / 25);
		for (int i = 0; i<tempFilterList.size(); i++) {
			FilterObject newFilterObject = new FilterObject(game, tempFilterList.get(i), -1, newXpos, newYpos);
			if (newFilterObject.renderPos.x + newFilterObject.bounds.x > Gdx.graphics.getWidth()) {
				newXpos = (Gdx.graphics.getWidth() / 40);
				newYpos -= newFilterObject.bounds.y + (Gdx.graphics.getWidth() / 100);
				newFilterObject.renderPos = new Vector2(newXpos, newYpos);
				newFilterObject.tickBoxSprite.setPosition(newXpos,newYpos - (newFilterObject.tickBoxSprite.getHeight() / 2));
			}
			knifeFilterList.add(newFilterObject);
			newXpos += newFilterObject.bounds.x + (Gdx.graphics.getWidth() / 40);
		}
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

		//CASES\\
	    float newXpos = renderStart.x;
	    float newYpos = renderStart.y;
		game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("cases"), newXpos, newYpos);
		for (int i = 0; i < caseFilterList.size(); i++) {
			caseFilterList.get(i).Render();
		}
		
		//RARITY\\
		newYpos = caseFilterList.get(caseFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 50);
		game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("rarity"), newXpos, newYpos);
		for (int i = 0; i < rarityFilterList.size(); i++) {
			rarityFilterList.get(i).Render();
		}
		
		//QUALITY\\
		newYpos = rarityFilterList.get(rarityFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 50);
		game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("exterior"), newXpos, newYpos);
		for (int i = 0; i < qualityFilterList.size(); i++) {
			qualityFilterList.get(i).Render();
		}
		
		//QUALITY\\
		newYpos = qualityFilterList.get(rarityFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 50);
		game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("stattrak"), newXpos, newYpos);
		stattrakFilter.Render();
		
		//PISTOLS\\
		newYpos = stattrakFilter.tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 50);
		game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("pistols"), newXpos, newYpos);
		for (int i = 0; i < pistolFilterList.size(); i++) {
			pistolFilterList.get(i).Render();
		}
		
		//HEAVY\\
		newYpos = pistolFilterList.get(pistolFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 50);
		game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("heavy"), newXpos, newYpos);
		for (int i = 0; i < heavyFilterList.size(); i++) {
			heavyFilterList.get(i).Render();
		}
		
		//SUBMACHINE\\
		newYpos = heavyFilterList.get(heavyFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 50);
		game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("submachine"), newXpos, newYpos);
		for (int i = 0; i < subFilterList.size(); i++) {
			subFilterList.get(i).Render();
		}
		
		//RIFLES\\
		newYpos = subFilterList.get(subFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 50);
		game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("rifles"), newXpos, newYpos);
		for (int i = 0; i < rifleFilterList.size(); i++) {
			rifleFilterList.get(i).Render();
		}	
		
		//RIFLES\\
		newYpos = rifleFilterList.get(rifleFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 50);
		game.Arial_Bold_Large.draw(game.batch, game.textBundle.get("knives"), newXpos, newYpos);
		for (int i = 0; i < knifeFilterList.size(); i++) {
			knifeFilterList.get(i).Render();
		}		
	    
	    game.inventoryTopBackgroundSprite.draw(game.batch);
	    game.inventoryButtonSprite.draw(game.batch);
	    
	    newXpos = (game.inventoryButtonSprite.getX() + (game.inventoryButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("inventory")).width / 2);
		newYpos = (game.inventoryButtonSprite.getY() + (game.inventoryButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("inventory")).height / 2);
		game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("inventory"), newXpos, newYpos);
		
	    game.batch.end();	    
	}
	
	public void clearFilter() {
		filterAmount = 0;
		filterString = "";
		
		//CASES\\
		for (int i = 0; i < caseFilterList.size(); i++) {
			caseFilterList.get(i).ticked = true;
			caseFilterList.get(i).UpdateTick();
		}
		
		//RARITY\\
		for (int i = 0; i < rarityFilterList.size(); i++) {
			rarityFilterList.get(i).ticked = true;
			rarityFilterList.get(i).UpdateTick();
		}
		
		//QUALITY\\ 
		for (int i = 0; i < qualityFilterList.size(); i++) {
			qualityFilterList.get(i).ticked = true;
			qualityFilterList.get(i).UpdateTick();
		}
		
		//STATTRAK\\
		stattrakFilter.ticked = true;
		stattrakFilter.UpdateTick();
		
		//PISTOLS\\
		for (int i = 0; i < pistolFilterList.size(); i++) {
			pistolFilterList.get(i).ticked = true;
			pistolFilterList.get(i).UpdateTick();
		}
		
		//HEAVY\\
		for (int i = 0; i < heavyFilterList.size(); i++) {
			heavyFilterList.get(i).ticked = true;
			heavyFilterList.get(i).UpdateTick();
		}
		
		//SUBMACHINE\\
		for (int i = 0; i < subFilterList.size(); i++) {
			subFilterList.get(i).ticked = true;
			subFilterList.get(i).UpdateTick();
		}
		
		//RIFLES\\
		for (int i = 0; i < rifleFilterList.size(); i++) {
			rifleFilterList.get(i).ticked = true;
			rifleFilterList.get(i).UpdateTick();
		}	
		
		//RIFLES\\
		for (int i = 0; i < knifeFilterList.size(); i++) {
			knifeFilterList.get(i).ticked = true;
			knifeFilterList.get(i).UpdateTick();
		}		
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
		game.myRequestHandler.sendTrackerScreenName("Filter Screen");
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
		if ((x > game.inventoryButtonSprite.getX()) && (x < (game.inventoryButtonSprite.getX() + game.inventoryButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.inventoryButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.inventoryButtonSprite.getY() + game.inventoryButtonSprite.getHeight()))) {
			
			int gunFilterAmount = 0;
			StringBuilder gunQuery = new StringBuilder();
			String line = "";
			for (int i = 0; i < pistolFilterList.size(); i++) {
				if (pistolFilterList.get(i).ticked) {
					gunFilterAmount++;
					gunQuery.append(line);
					line = "gunType='" + pistolFilterList.get(i).filterType + "' ";
					gunQuery.append(line);
					line = "OR ";
				}
			}
			for (int i = 0; i < heavyFilterList.size(); i++) {
				if (heavyFilterList.get(i).ticked) {
					gunFilterAmount++;
					gunQuery.append(line);
					line = "gunType='" + heavyFilterList.get(i).filterType + "' ";
					gunQuery.append(line);
					line = "OR ";
				}
			}
			for (int i = 0; i < subFilterList.size(); i++) {
				if (subFilterList.get(i).ticked) {
					gunFilterAmount++;
					gunQuery.append(line);
					line = "gunType='" + subFilterList.get(i).filterType + "' ";
					gunQuery.append(line);
					line = "OR ";
				}
			}
			for (int i = 0; i < rifleFilterList.size(); i++) {
				if (rifleFilterList.get(i).ticked) {
					gunFilterAmount++;
					gunQuery.append(line);
					line = "gunType='" + rifleFilterList.get(i).filterType + "' ";
					gunQuery.append(line);
					line = "OR ";
				}
			}
			for (int i = 0; i < knifeFilterList.size(); i++) {
				if (knifeFilterList.get(i).ticked) {
					gunFilterAmount++;
					gunQuery.append(line);
					line = "gunType='" + knifeFilterList.get(i).filterType + "' ";
					gunQuery.append(line);
					line = "OR ";
				}
			}
			
			int rarityFilterAmount = 0;
			StringBuilder rarityQuery = new StringBuilder();
			line = "";
			for (int i = 0; i < rarityFilterList.size(); i++) {
				if (rarityFilterList.get(i).ticked) {
					rarityFilterAmount++;
					rarityQuery.append(line);
					if (rarityFilterList.get(i).filterType.equals("Mil-spec")) {
						line = "rarity='1' ";
					} else if (rarityFilterList.get(i).filterType.equals("Restricted")) {
						line = "rarity='2' ";
					} else if (rarityFilterList.get(i).filterType.equals("Classified")) {
						line = "rarity='3' ";
					} else if (rarityFilterList.get(i).filterType.equals("Covert")) {
						line = "rarity='4' ";
					} else if (rarityFilterList.get(i).filterType.equals("Exceedingly Rare")) {
						line = "rarity='5' ";
					}
					rarityQuery.append(line);
					line = "OR ";
				}
			}
			
			
			int qualityFilterAmount = 0;
			StringBuilder qualityQuery = new StringBuilder();
			line = "";
			for (int i = 0; i < qualityFilterList.size(); i++) {
				if (qualityFilterList.get(i).ticked) {
					qualityFilterAmount++;
					qualityQuery.append(line);
					line = "wear='" + qualityFilterList.get(i).filterType + "' ";
					qualityQuery.append(line);
					line = "OR ";
				}
			}
			
			int caseFilterAmount = 0;
			StringBuilder caseQuery = new StringBuilder();
			line = "";
			for (int i = 0; i < caseFilterList.size(); i++) {
				if (caseFilterList.get(i).ticked) {
					caseFilterAmount++;
					caseQuery.append(line);
					line = "caseID='" + caseFilterList.get(i).filterID + "' ";
					caseQuery.append(line);
					line = "OR ";
				}
			}
			

			int stattrakFilterAmount = 0;
			StringBuilder stattrakQuery = new StringBuilder();
			if (stattrakFilter.ticked) {
				stattrakFilterAmount++;
				line = "stattrak='1'";
				stattrakQuery.append(line);
			}
			
			filterAmount = rarityFilterAmount + qualityFilterAmount + gunFilterAmount + caseFilterAmount + stattrakFilterAmount;
			
			game.inventoryList.clear();
			List<List> inventorySQLList = new ArrayList<List>();
			
			if (filterAmount > 0) {
				StringBuilder sqlQuery = new StringBuilder();
				line = "SELECT * FROM Inventory WHERE (";
				if (caseFilterAmount > 0) {
					sqlQuery.append(line);
					line = "(" + caseQuery.toString() + ")";
					sqlQuery.append(line);
					line = " AND ";
				}
				if (rarityFilterAmount > 0) {
					sqlQuery.append(line);
					line = "(" + rarityQuery.toString() + ")";
					sqlQuery.append(line);
					line = " AND ";
				}
				if (qualityFilterAmount > 0) {
					sqlQuery.append(line);
					line = "(" + qualityQuery.toString() + ")";
					sqlQuery.append(line);
					line = " AND ";
				}
				if (gunFilterAmount > 0) {
					sqlQuery.append(line);
					line = "(" + gunQuery.toString() + ")";
					sqlQuery.append(line);
					line = " AND ";
				}
				if (stattrakFilterAmount > 0) {
					sqlQuery.append(line);
					line = "(" + stattrakQuery.toString() + ")";
					sqlQuery.append(line);
					line = " AND ";
				}
				line = ");";
				sqlQuery.append(line);
				filterString = sqlQuery.toString();
				
				inventorySQLList = game.myRequestHandler.filterInventory(sqlQuery.toString());
			} else {
				inventorySQLList = game.myRequestHandler.listAllInventory();
			}
            for (int i = 0; i < inventorySQLList.size(); i++) {
                Guns newGun = new Guns();
                newGun.gunID = Integer.parseInt(inventorySQLList.get(i).get(1).toString());

                List<String> itemSQLList = new ArrayList<String>();

                newGun.rarity = inventorySQLList.get(i).get(2).toString();

                if (newGun.rarity.equals("5")) {
                    itemSQLList = game.myRequestHandler.listKnifeWithID(newGun.gunID);
                } else {
                    itemSQLList = game.myRequestHandler.listGunWithID(newGun.gunID);
                }

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
			
			game.setScreen(game.inventoryScreen);
		}
		if ((Gdx.graphics.getHeight() -y) < game.inventoryTopBackgroundSprite.getY()) {	
			for (int i = 0; i < caseFilterList.size(); i++) {
				if ((x > caseFilterList.get(i).renderPos.x) && (x < (caseFilterList.get(i).renderPos.x + caseFilterList.get(i).bounds.x)) && ((Gdx.graphics.getHeight() -y) > caseFilterList.get(i).tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (caseFilterList.get(i).tickBoxSprite.getY() + caseFilterList.get(i).bounds.y))) {
					caseFilterList.get(i).UpdateTick();
				}
			}
			for (int i = 0; i < rarityFilterList.size(); i++) {
				if ((x > rarityFilterList.get(i).renderPos.x) && (x < (rarityFilterList.get(i).renderPos.x + rarityFilterList.get(i).bounds.x)) && ((Gdx.graphics.getHeight() -y) > rarityFilterList.get(i).tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (rarityFilterList.get(i).tickBoxSprite.getY() + rarityFilterList.get(i).bounds.y))) {
					rarityFilterList.get(i).UpdateTick();
				}
			}
			for (int i = 0; i < qualityFilterList.size(); i++) {
				if ((x > qualityFilterList.get(i).renderPos.x) && (x < (qualityFilterList.get(i).renderPos.x + qualityFilterList.get(i).bounds.x)) && ((Gdx.graphics.getHeight() -y) > qualityFilterList.get(i).tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (qualityFilterList.get(i).tickBoxSprite.getY() + qualityFilterList.get(i).bounds.y))) {
					qualityFilterList.get(i).UpdateTick();
				}
			}
			if ((x > stattrakFilter.renderPos.x) && (x < (stattrakFilter.renderPos.x + stattrakFilter.bounds.x)) && ((Gdx.graphics.getHeight() -y) > stattrakFilter.tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (stattrakFilter.tickBoxSprite.getY() + stattrakFilter.bounds.y))) {
				stattrakFilter.UpdateTick();
			}
			for (int i = 0; i < pistolFilterList.size(); i++) {
				if ((x > pistolFilterList.get(i).renderPos.x) && (x < (pistolFilterList.get(i).renderPos.x + pistolFilterList.get(i).bounds.x)) && ((Gdx.graphics.getHeight() -y) > pistolFilterList.get(i).tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (pistolFilterList.get(i).tickBoxSprite.getY() + pistolFilterList.get(i).bounds.y))) {
					pistolFilterList.get(i).UpdateTick();
				}
			}
			for (int i = 0; i < heavyFilterList.size(); i++) {
				if ((x > heavyFilterList.get(i).renderPos.x) && (x < (heavyFilterList.get(i).renderPos.x + heavyFilterList.get(i).bounds.x)) && ((Gdx.graphics.getHeight() -y) > heavyFilterList.get(i).tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (heavyFilterList.get(i).tickBoxSprite.getY() + heavyFilterList.get(i).bounds.y))) {
					heavyFilterList.get(i).UpdateTick();
				}
			}
			for (int i = 0; i < subFilterList.size(); i++) {
				if ((x > subFilterList.get(i).renderPos.x) && (x < (subFilterList.get(i).renderPos.x + subFilterList.get(i).bounds.x)) && ((Gdx.graphics.getHeight() -y) > subFilterList.get(i).tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (subFilterList.get(i).tickBoxSprite.getY() + subFilterList.get(i).bounds.y))) {
					subFilterList.get(i).UpdateTick();
				}
			}
			for (int i = 0; i < rifleFilterList.size(); i++) {
				if ((x > rifleFilterList.get(i).renderPos.x) && (x < (rifleFilterList.get(i).renderPos.x + rifleFilterList.get(i).bounds.x)) && ((Gdx.graphics.getHeight() -y) > rifleFilterList.get(i).tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (rifleFilterList.get(i).tickBoxSprite.getY() + rifleFilterList.get(i).bounds.y))) {
					rifleFilterList.get(i).UpdateTick();
				}
			}
			for (int i = 0; i < knifeFilterList.size(); i++) {
				if ((x > knifeFilterList.get(i).renderPos.x) && (x < (knifeFilterList.get(i).renderPos.x + knifeFilterList.get(i).bounds.x)) && ((Gdx.graphics.getHeight() -y) > knifeFilterList.get(i).tickBoxSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (knifeFilterList.get(i).tickBoxSprite.getY() + knifeFilterList.get(i).bounds.y))) {
					knifeFilterList.get(i).UpdateTick();
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
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
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
		float scrollValue = (touchDownPoint.y - screenY);
		if (scrollValue > 0) {
			if ((knifeFilterList.get(knifeFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 40)) < 0) {
				if ((knifeFilterList.get(knifeFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 40)) + scrollValue > 0) {
					scrollValue = -(knifeFilterList.get(knifeFilterList.size() - 1).tickBoxSprite.getY() - (Gdx.graphics.getWidth() / 40));
				}
				updatePos(scrollValue);
			}
		} else if (scrollValue < 0) {
			if (renderStart.y > game.inventoryTopBackgroundSprite.getY()) {
				if ((renderStart.y + scrollValue) < game.inventoryTopBackgroundSprite.getY()) {
					scrollValue = game.inventoryTopBackgroundSprite.getY() - renderStart.y;
				}
				updatePos(scrollValue);
			}
		}
		
		
		touchDownPoint = new Vector2(screenX, screenY);
		return false;
	}
	
	public void updatePos(float scrollValue) {
		renderStart.y += scrollValue;
		for (int i = 0; i < caseFilterList.size(); i++) {
			caseFilterList.get(i).UpdatePos(0, scrollValue);
		}
		for (int i = 0; i < rarityFilterList.size(); i++) {
			rarityFilterList.get(i).UpdatePos(0, scrollValue);
		}
		for (int i = 0; i < qualityFilterList.size(); i++) {
			qualityFilterList.get(i).UpdatePos(0, scrollValue);
		}
		stattrakFilter.UpdatePos(0, scrollValue);
		for (int i = 0; i < pistolFilterList.size(); i++) {
			pistolFilterList.get(i).UpdatePos(0, scrollValue);
		}
		for (int i = 0; i < heavyFilterList.size(); i++) {
			heavyFilterList.get(i).UpdatePos(0, scrollValue);
		}
		for (int i = 0; i < subFilterList.size(); i++) {
			subFilterList.get(i).UpdatePos(0, scrollValue);
		}
		for (int i = 0; i < rifleFilterList.size(); i++) {
			rifleFilterList.get(i).UpdatePos(0, scrollValue);
		}
		for (int i = 0; i < knifeFilterList.size(); i++) {
			knifeFilterList.get(i).UpdatePos(0, scrollValue);
		}
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
