package com.synthdark.casesimulatorfree;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class InventoryScreen implements Screen, GestureListener, InputProcessor {

	CaseSimulatorFree game;
	InputProcessor gestureDectector;
	
	List<WeaponObject> inventoryObjectList;
	
	Vector2 touchDownPoint;
	int loadSize;
	int invenTop;
	float renderStart;
	boolean tradeUp;
	int highlightNumber;
	boolean initialTradeFilter;
	boolean delete;
	
	public InventoryScreen(CaseSimulatorFree game){
		this.game = game;
		gestureDectector = new GestureDetector(100f, 0.0f, 0.0f, 5f, this);
		inventoryObjectList = new ArrayList<WeaponObject>();
		invenTop = 0;
		loadSize = 0;
		touchDownPoint = new Vector2();
		tradeUp = false;
		initialTradeFilter = false;
		highlightNumber = 0;
		delete = false;
		renderStart = game.inventoryTopBackgroundSprite.getY() - game.gunBackgroundSize.y;
		LoadInventoryObjects();
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

		for(int i = 0; i< inventoryObjectList.size(); i++) {
			inventoryObjectList.get(i).render(game.batch);
		}

	    game.inventoryTopBackgroundSprite.draw(game.batch);
	    game.inventoryBottomBackgroundSprite.draw(game.batch);
        game.inventoryButtonSprite.draw(game.batch);

        float newXpos;
        float newYpos;

        if (game.userPrefs.getBoolean("event_stickers", true)) {
            game.settingsButtonSprite.draw(game.batch);

            newXpos = (game.settingsButtonSprite.getX() + (game.settingsButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("event_stickers")).width / 2);
            newYpos = (game.settingsButtonSprite.getY() + (game.settingsButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("event_stickers")).height / 2);
            game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("event_stickers"), newXpos, newYpos);
        }

		newXpos = (game.inventoryButtonSprite.getX() + (game.inventoryButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("home")).width / 2);
		newYpos = (game.inventoryButtonSprite.getY() + (game.inventoryButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("home")).height / 2);
		game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("home"), newXpos, newYpos);
		if (!delete) {
			if (tradeUp && (highlightNumber == 10)) {
			    game.filterButtonSprite.draw(game.batch);
				newXpos = (game.filterButtonSprite.getX() + (game.filterButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("trade_in")).width / 2);
				newYpos = (game.filterButtonSprite.getY() + (game.filterButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("trade_in")).height / 2);
				game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("trade_in"), newXpos, newYpos);
			} else if (!tradeUp) {
			    game.filterButtonSprite.draw(game.batch);
				newXpos = (game.filterButtonSprite.getX() + (game.filterButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("filter")).width / 2);
				newYpos = (game.filterButtonSprite.getY() + (game.filterButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("filter")).height / 2);
				game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("filter"), newXpos, newYpos);
			}
			
			game.tradeUpButtonSprite.draw(game.batch);	
			if (tradeUp) {
				newXpos = (game.tradeUpButtonSprite.getX() + (game.tradeUpButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("cancel")).width / 2);
				newYpos = (game.tradeUpButtonSprite.getY() + (game.tradeUpButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("cancel")).height / 2);
				game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("cancel"), newXpos, newYpos);
			} else {
				newXpos = (game.tradeUpButtonSprite.getX() + (game.tradeUpButtonSprite.getWidth()) / 2) - (game.Arial_Regular_Normal.getBounds(game.textBundle.get("trade_up")).width / 2);
				newYpos = (game.tradeUpButtonSprite.getY() + (game.tradeUpButtonSprite.getHeight()) / 1.9f) + (game.Arial_Regular_Normal.getBounds(game.textBundle.get("trade_up")).height / 2);
				game.Arial_Regular_Normal.draw(game.batch, game.textBundle.get("trade_up"), newXpos, newYpos);
			}
		}
		
		if (!tradeUp) {
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
		}
		
		game.batch.end();
	}

	public void LoadInventoryObjects() {
		for(int i = invenTop; i < (invenTop + loadSize); i++) {
			String rarityBackground = new String();
			if (game.inventoryList.get(i).rarity.equals("1")) {
				rarityBackground = "Large Blue Gun Background";
			} else if (game.inventoryList.get(i).rarity.equals("2")) {
				rarityBackground = "Large Purple Gun Background";
			} else if (game.inventoryList.get(i).rarity.equals("3")) {
				rarityBackground = "Large Pink Gun Background";
			} else if (game.inventoryList.get(i).rarity.equals("4")) {
				rarityBackground = "Large Red Gun Background";
			} else if (game.inventoryList.get(i).rarity.equals("5")) {
				rarityBackground = "Large Gold Gun Background";
			}

			Sprite gunBackgroundSprite = new Sprite();
			gunBackgroundSprite = game.gunBackgroundAtlas.createSprite(rarityBackground);
			float imageRatio = gunBackgroundSprite.getWidth() / gunBackgroundSprite.getHeight();
			gunBackgroundSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);

			Texture highlightTexture = new Texture(Gdx.files.internal("Gun Backgrounds/Highlight.png"));
			Sprite highlightSprite = new Sprite(highlightTexture);
			imageRatio = highlightSprite.getWidth() / highlightSprite.getHeight();
			highlightSprite.setSize(Gdx.graphics.getWidth() / 4.5f, (Gdx.graphics.getWidth() / imageRatio) / 4.5f);
			float highlightX = gunBackgroundSprite.getX() + (gunBackgroundSprite.getWidth() / 2);
			float highlightY = gunBackgroundSprite.getY() + (gunBackgroundSprite.getHeight() / 2);
			highlightSprite.setCenter(highlightX, highlightY);

			Sprite gunSprite = new Sprite();
			if (rarityBackground.equals("Large Gold Gun Background")) {
				gunSprite = game.knifeTextureAtlas.createSprite(game.inventoryList.get(i).gunTexture);
				imageRatio = gunSprite.getWidth() / gunSprite.getHeight();
				gunSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
			} else {
				gunSprite = game.gunTextureAtlas.createSprite(game.inventoryList.get(i).gunTexture);
				imageRatio = gunSprite.getWidth() / gunSprite.getHeight();
				gunSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
			}
			
			Texture stattrakTexture = new Texture(Gdx.files.internal("Textures/Stattrak.png"));
			Sprite stattrakSprite = new Sprite(stattrakTexture);
			imageRatio = stattrakSprite.getWidth() / stattrakSprite.getHeight();
			stattrakSprite.setSize(Gdx.graphics.getWidth() / 13f, (Gdx.graphics.getWidth() / imageRatio) / 13f);

			String type = game.inventoryList.get(i).gunType;
			String skin = game.inventoryList.get(i).gunSkin;
			String rarity = game.inventoryList.get(i).rarity;
			String texture = game.inventoryList.get(i).gunTexture;
			String wear = game.inventoryList.get(i).wear;

			boolean stattrak = game.inventoryList.get(i).stattrak;
			
			WeaponObject newWeapon = new WeaponObject(game, gunSprite, gunBackgroundSprite, highlightSprite, stattrakSprite, rarity, type, skin, texture);
			newWeapon.wear = wear;
			newWeapon.stattrak = stattrak;
			newWeapon.gunID = game.inventoryList.get(i).gunID;
			newWeapon.caseID = game.inventoryList.get(i).caseID;
			newWeapon.GenerateText();
			newWeapon.showGun = true;
			newWeapon.highlighted = game.inventoryList.get(i).highlighted;
			inventoryObjectList.add(newWeapon);
		}
		
		if (inventoryObjectList.size() > 0) {
			renderStart = game.inventoryTopBackgroundSprite.getY() - inventoryObjectList.get(0).highlightSprite.getHeight();
		}
		
		PositionInventory();
	}
	
	public void RepurposeInventoryObjects() {
		int maxInvenRender = game.maxInven;

		if ((invenTop + (game.maxInven - 1)) >= game.inventoryList.size()) {
			maxInvenRender = game.inventoryList.size() - invenTop;
		}
		
		for(int i = 0; i < inventoryObjectList.size(); i++) {
			if (i < maxInvenRender) {	
				String rarityBackground = new String();
				if (game.inventoryList.get(invenTop + i).rarity.equals("1")) {
					rarityBackground = "Large Blue Gun Background";
				} else if (game.inventoryList.get(invenTop + i).rarity.equals("2")) {
					rarityBackground = "Large Purple Gun Background";
				} else if (game.inventoryList.get(invenTop + i).rarity.equals("3")) {
					rarityBackground = "Large Pink Gun Background";
				} else if (game.inventoryList.get(invenTop + i).rarity.equals("4")) {
					rarityBackground = "Large Red Gun Background";
				} else if (game.inventoryList.get(invenTop + i).rarity.equals("5")) {
					rarityBackground = "Large Gold Gun Background";
				}
				
				Sprite gunBackgroundSprite = new Sprite();
				gunBackgroundSprite = game.gunBackgroundAtlas.createSprite(rarityBackground);
				float imageRatio = gunBackgroundSprite.getWidth() / gunBackgroundSprite.getHeight();
				gunBackgroundSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);

				Sprite gunSprite = new Sprite();
				if (rarityBackground.equals("Large Gold Gun Background")) {
					gunSprite = game.knifeTextureAtlas.createSprite(game.inventoryList.get(invenTop + i).gunTexture);
					imageRatio = gunSprite.getWidth() / gunSprite.getHeight();
					gunSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
				} else {
					gunSprite = game.gunTextureAtlas.createSprite(game.inventoryList.get(invenTop + i).gunTexture);
					imageRatio = gunSprite.getWidth() / gunSprite.getHeight();
					gunSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
				}

				inventoryObjectList.get(i).gunSprite = gunSprite;
				inventoryObjectList.get(i).gunBackgroundSprite = gunBackgroundSprite;
				inventoryObjectList.get(i).gunType = game.inventoryList.get(invenTop + i).gunType;
				inventoryObjectList.get(i).gunSkin = game.inventoryList.get(invenTop + i).gunSkin;
				inventoryObjectList.get(i).rarity = game.inventoryList.get(invenTop + i).rarity;
				inventoryObjectList.get(i).gunTexture = game.inventoryList.get(invenTop + i).gunTexture;
				inventoryObjectList.get(i).wear = game.inventoryList.get(invenTop + i).wear;
				inventoryObjectList.get(i).gunID = game.inventoryList.get(invenTop + i).gunID;
				inventoryObjectList.get(i).caseID = game.inventoryList.get(invenTop + i).caseID;;
				inventoryObjectList.get(i).render = true;
				inventoryObjectList.get(i).highlighted = game.inventoryList.get(invenTop + i).highlighted;
				inventoryObjectList.get(i).stattrak = game.inventoryList.get(invenTop + i).stattrak;
			} else {
				inventoryObjectList.get(i).render = false;
			}
		}
	}

	public void PositionInventory() {

		float newXpos = (Gdx.graphics.getWidth() / 40);
		float newYpos = renderStart;
		float newYpos2 = renderStart;
		
		Vector2 invenCount = new Vector2(1,1);
		
		for(int i = 0; i < inventoryObjectList.size(); i++) {
			if (invenCount.y < (game.maxInvenGrid.y + 1)) {
				inventoryObjectList.get(i).gunBackgroundSprite.setPosition(newXpos,newYpos);
				newYpos2 = (inventoryObjectList.get(i).gunBackgroundSprite.getY() + inventoryObjectList.get(i).gunBackgroundSprite.getHeight()) - (inventoryObjectList.get(i).gunSprite.getHeight() / 1.17f);
				inventoryObjectList.get(i).gunSprite.setPosition(newXpos,newYpos2);
				float highlightX = inventoryObjectList.get(i).gunBackgroundSprite.getX() + (inventoryObjectList.get(i).gunBackgroundSprite.getWidth() / 2);
				float highlightY = inventoryObjectList.get(i).gunBackgroundSprite.getY() + (inventoryObjectList.get(i).gunBackgroundSprite.getHeight() / 2);
				inventoryObjectList.get(i).highlightSprite.setCenter(highlightX, highlightY);
				float newXpos2 = inventoryObjectList.get(i).gunBackgroundSprite.getX() + inventoryObjectList.get(i).gunBackgroundSprite.getWidth() - ((inventoryObjectList.get(i).stattrakSprite.getWidth() / 4) * 3);
				newYpos2 = inventoryObjectList.get(i).gunBackgroundSprite.getY() + inventoryObjectList.get(i).gunBackgroundSprite.getHeight() - ((inventoryObjectList.get(i).stattrakSprite.getHeight() / 4) * 3);
				inventoryObjectList.get(i).stattrakSprite.setPosition(newXpos2,newYpos2);
				inventoryObjectList.get(i).GenerateText();
	
				newXpos += inventoryObjectList.get(i).gunBackgroundSprite.getWidth() + (Gdx.graphics.getWidth() / 40);
				invenCount.x++;
				
				if (invenCount.x > game.maxInvenGrid.x) {
					newXpos = (Gdx.graphics.getWidth() / 40);
					newYpos -= inventoryObjectList.get(i).gunBackgroundSprite.getHeight() + (Gdx.graphics.getWidth() / 40);
					invenCount.y++;
					invenCount.x = 1;
				}
			}
		}
	}

    public void ResetInventory() {

        for (int i = 0; i < inventoryObjectList.size(); i++) {
            inventoryObjectList.get(i).highlighted = false;
        }
        for (int i = 0; i < game.inventoryList.size(); i++) {
            game.inventoryList.get(i).highlighted = false;
        }
        highlightNumber = 0;

        game.inventoryList.clear();
        List<List> inventorySQLList = new ArrayList<List>();
        inventorySQLList = game.myRequestHandler.listAllInventory();
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



        initialTradeFilter = false;
        RepurposeInventoryObjects();
        PositionInventory();

        float newXpos = (Gdx.graphics.getWidth() / 2) - (game.deleteButtonSprite.getWidth() / 2);
        float newYpos = (Gdx.graphics.getWidth() / 80);
        game.deleteButtonSprite.setPosition(newXpos,newYpos);
    }
	
	public void TradeUp() {
		int tradeRarity =  Integer.parseInt(game.inventoryList.get(0).rarity);	
		List<Guns> tradeUpList = new ArrayList<Guns>();
		for (int i = 0; i<game.inventoryList.size(); i++) {
			if (game.inventoryList.get(i).highlighted) {
				game.myRequestHandler.removeGun(game.inventoryList.get(i).gunID);

				List<Guns> tradeGunList = new ArrayList<Guns>();
				for (int j = 0; j < game.caseList.size(); j++) {
					if (game.caseList.get(j).caseID == game.inventoryList.get(i).caseID) {
						if (tradeRarity == 1) {
							tradeGunList = game.caseList.get(j).gun2List;
						} else if (tradeRarity == 2) {
							tradeGunList = game.caseList.get(j).gun3List;
						} else if (tradeRarity == 3) {
							tradeGunList = game.caseList.get(j).gun4List;
						} else if (tradeRarity == 4) {
							tradeGunList = game.caseList.get(j).gun4List;
						}
					}
				}
				int randomGun = game.random.nextInt(tradeGunList.size());
				double randomWear = (game.random.nextInt(1000) / 10.0);
				
				Guns newGun = new Guns();
				
				String wear = "";
				if (!((game.inventoryList.get(i).caseID > 12) && (game.inventoryList.get(i).caseID < 20))) {
					if (randomWear <= 20) {
						newGun.wear = "battle-scarred";
					}
					else if (randomWear <= 40) {
						newGun.wear = "well-worn";
					}
					else if (randomWear <= 60) {
						newGun.wear = "field-tested";
					}
					else if (randomWear <= 80) {
						newGun.wear = "minimal-wear";
					}
					else {
						newGun.wear = "factory-new";
					}
				}
                newGun.caseID = game.inventoryList.get(i).caseID;
                newGun.gunID = tradeGunList.get(randomGun).gunID;
                newGun.gunType = tradeGunList.get(randomGun).gunType;
                newGun.gunType = tradeGunList.get(randomGun).gunType;
				newGun.gunSkin = tradeGunList.get(randomGun).gunSkin;
				newGun.gunTexture = tradeGunList.get(randomGun).gunTexture;
				newGun.rarity = tradeGunList.get(randomGun).rarity;
				newGun.stattrak = false;
				newGun.highlighted = false;
				
				tradeUpList.add(newGun);
				
				if (tradeRarity == 1) {
					game.statList.get(0).rarityType1 -= 1;
					game.myRequestHandler.addStat(0, "rarityType1", -1);
				} else if (tradeRarity == 2) {
					game.statList.get(0).rarityType2 -= 1;
					game.myRequestHandler.addStat(0, "rarityType2", -1);							
				} else if (tradeRarity == 3) {
					game.statList.get(0).rarityType3 -= 1;
					game.myRequestHandler.addStat(0, "rarityType3", -1);								
				} else if (tradeRarity == 4) {
					game.statList.get(0).rarityType4 -= 1;
					game.myRequestHandler.addStat(0, "rarityType4", -1);								
				} else if (tradeRarity == 5) {
					game.statList.get(0).rarityType5 -= 1;
					game.myRequestHandler.addStat(0, "rarityType5", -1);								
				}

				if (game.inventoryList.get(i).stattrak == true) {
					game.statList.get(0).stattrak -= 1;
					game.myRequestHandler.addStat(0, "stattrak", -1);							
				}
			}
		}

		int randomGun = game.random.nextInt(tradeUpList.size());
		
		Sprite gunBackgroundSprite = new Sprite();
		String rarityBackground = new String();
		if (tradeUpList.get(randomGun).rarity.equals("1")) {
			rarityBackground = "Large Blue Gun Background";
		} else if (tradeUpList.get(randomGun).rarity.equals("2")) {
			rarityBackground = "Large Purple Gun Background";
		} else if (tradeUpList.get(randomGun).rarity.equals("3")) {
			rarityBackground = "Large Pink Gun Background";
		} else if (tradeUpList.get(randomGun).rarity.equals("4")) {
			rarityBackground = "Large Red Gun Background";
		}
		
		gunBackgroundSprite = game.gunBackgroundAtlas.createSprite(rarityBackground);
		float imageRatio = gunBackgroundSprite.getWidth() / gunBackgroundSprite.getHeight();
		gunBackgroundSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
		
		Texture highlightTexture = new Texture(Gdx.files.internal("Gun Backgrounds/Highlight.png"));
		Sprite highlightSprite = new Sprite(highlightTexture);
		imageRatio = highlightSprite.getWidth() / highlightSprite.getHeight();
		highlightSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
				
		Sprite gunSprite = new Sprite();
		gunSprite = game.gunTextureAtlas.createSprite(tradeUpList.get(randomGun).gunTexture);
		imageRatio = gunSprite.getWidth() / gunSprite.getHeight();
		gunSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
		
		Texture stattrakTexture = new Texture(Gdx.files.internal("Textures/Stattrak.png"));
		Sprite stattrakSprite = new Sprite(stattrakTexture);
		imageRatio = stattrakSprite.getWidth() / stattrakSprite.getHeight();
		stattrakSprite.setSize(Gdx.graphics.getWidth() / 4f, (Gdx.graphics.getWidth() / imageRatio) / 4f);
		
		WeaponObject newWeapon = new WeaponObject(game, gunSprite, gunBackgroundSprite, highlightSprite, stattrakSprite, tradeUpList.get(randomGun).rarity, tradeUpList.get(randomGun).gunType, tradeUpList.get(randomGun).gunSkin, tradeUpList.get(randomGun).gunTexture);
		newWeapon.wear = tradeUpList.get(randomGun).wear;
		newWeapon.stattrak = false;
        newWeapon.gunID = tradeUpList.get(randomGun).gunID;
        newWeapon.caseID = tradeUpList.get(randomGun).caseID;
		newWeapon.GenerateText();
				
		initialTradeFilter = false;
		game.tickedGunObject = newWeapon;
		tradeUp = false;
		delete = false;
		inventoryObjectList.clear();
		ResetInventory();
		game.filterScreen.clearFilter();
		game.setScreen(game.gunScreen); 
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
		game.myRequestHandler.sendTrackerScreenName("Inventory Screen");
	
		invenTop = 0;
		loadSize = 0;
		highlightNumber = 0;
        
		if ((game.inventoryList.size() - invenTop) > game.maxInven) {
			loadSize = game.maxInven;
		} else {
			loadSize = game.inventoryList.size() - invenTop;
		}
		
		LoadInventoryObjects();
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
			for (int i = 0; i < inventoryObjectList.size(); i++) {
				if ((x > inventoryObjectList.get(i).gunBackgroundSprite.getX()) && (x < (inventoryObjectList.get(i).gunBackgroundSprite.getX() + inventoryObjectList.get(i).gunBackgroundSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > inventoryObjectList.get(i).gunBackgroundSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (inventoryObjectList.get(i).gunBackgroundSprite.getY() + inventoryObjectList.get(i).gunBackgroundSprite.getHeight()))) {
					if (inventoryObjectList.get(i).render) {
						if (tradeUp) {
							if (!inventoryObjectList.get(i).rarity.equals("5") && !inventoryObjectList.get(i).rarity.equals("4") && !inventoryObjectList.get(i).stattrak && !((inventoryObjectList.get(i).caseID > 12) && (inventoryObjectList.get(i).caseID < 20))) {
								if (highlightNumber < 11) {
									boolean initialTradeHighlight = false;
									if (!initialTradeFilter) {
										initialTradeFilter = true;
										game.filterScreen.clearFilter();
										
										StringBuilder sqlQuery = new StringBuilder();
										sqlQuery.append("SELECT * FROM Inventory WHERE ");
										
										String line = "rarity='" + inventoryObjectList.get(i).rarity + "' ";
										sqlQuery.append(line);
										game.inventoryList.clear();
										List<List> inventorySQLList = new ArrayList<List>();
										inventorySQLList = game.myRequestHandler.filterInventory(sqlQuery.toString());
										for (int j = 0; j<inventorySQLList.size(); j++) {
											if (!((Integer.parseInt(inventorySQLList.get(j).get(3).toString()) > 12) && (Integer.parseInt(inventorySQLList.get(j).get(3).toString()) < 20))) {
												if (Integer.parseInt(inventorySQLList.get(j).get(5).toString()) == 0) {
                                                    Guns newGun = new Guns();
                                                    List<String> itemSQLList = new ArrayList<String>();
                                                    newGun.rarity = inventorySQLList.get(j).get(2).toString();

                                                    if (newGun.rarity.equals("5")) {
                                                        itemSQLList = game.myRequestHandler.listKnifeWithID(Integer.parseInt(inventorySQLList.get(j).get(1).toString()));
                                                    } else {
                                                        itemSQLList = game.myRequestHandler.listGunWithID(Integer.parseInt(inventorySQLList.get(j).get(1).toString()));
                                                    }

                                                    newGun.gunID = Integer.parseInt(inventorySQLList.get(j).get(0).toString());
                                                    newGun.gunType = itemSQLList.get(1).toString();
                                                    newGun.gunSkin = itemSQLList.get(2).toString();
                                                    newGun.gunTexture = itemSQLList.get(3).toString();
                                                    newGun.caseID = Integer.parseInt(inventorySQLList.get(j).get(3).toString());
                                                    newGun.wear = inventorySQLList.get(j).get(4).toString();
                                                    newGun.stattrak = false;
                                                    if (inventoryObjectList.get(i).gunID == newGun.gunID) {
                                                        newGun.highlighted = true;
                                                    } else {
                                                        newGun.highlighted = false;
                                                    }
                                                    initialTradeHighlight = true;
                                                    game.inventoryList.add(newGun);
												}
											}
										}
										highlightNumber++;
										
										invenTop = 0;
										loadSize = 0;
								        
										if ((game.inventoryList.size() - invenTop) > game.maxInven) {
											loadSize = game.maxInven;
										} else {
											loadSize = game.inventoryList.size() - invenTop;
										}										
										
										inventoryObjectList.clear();
										LoadInventoryObjects();
										PositionInventory();
									}

									if (highlightNumber != 10) {
										if (!initialTradeHighlight) {
											game.inventoryList.get(invenTop + i).highlighted = !game.inventoryList.get(invenTop + i).highlighted;
											inventoryObjectList.get(i).highlighted = !inventoryObjectList.get(i).highlighted;
											if (inventoryObjectList.get(i).highlighted) {
												highlightNumber++;
											} else {
												highlightNumber--;
											}
										}
									} else {
										if (inventoryObjectList.get(i).highlighted) {
											game.inventoryList.get(invenTop + i).highlighted = false;
											inventoryObjectList.get(i).highlighted = false;
											highlightNumber--;
										}
									}
									if (highlightNumber == 0) {
										game.filterScreen.clearFilter();
										
										ResetInventory();
										
										invenTop = 0;
										loadSize = 0;
										highlightNumber = 0;
								        
										if ((game.inventoryList.size() - invenTop) > game.maxInven) {
											loadSize = game.maxInven;
										} else {
											loadSize = game.inventoryList.size() - invenTop;
										}		
										initialTradeFilter = false;
										inventoryObjectList.clear();
										LoadInventoryObjects();
										PositionInventory();
									} 
								}
							}
						} else if (delete) {
							if (highlightNumber == 0) {
								float newXpos = ((Gdx.graphics.getWidth() / 10) * 6.35f)  - (game.cancelDeleteButtonSprite.getWidth() / 2);
								float newYpos = (Gdx.graphics.getWidth() / 80);
								game.cancelDeleteButtonSprite.setPosition(newXpos,newYpos);							
							}
							game.inventoryList.get(invenTop + i).highlighted = !game.inventoryList.get(invenTop + i).highlighted;
							inventoryObjectList.get(i).highlighted = !inventoryObjectList.get(i).highlighted;
							if (inventoryObjectList.get(i).highlighted) {
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
            tradeUp = false;
            delete = false;
            if (inventoryObjectList.size() > 0) {
                renderStart = game.inventoryTopBackgroundSprite.getY() - inventoryObjectList.get(0).highlightSprite.getHeight();
            }
            inventoryObjectList.clear();
            ResetInventory();
            game.filterScreen.clearFilter();
            game.setScreen(game.caseScreen);
        } else if ((x > game.settingsButtonSprite.getX()) && (x < (game.settingsButtonSprite.getX() + game.settingsButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.settingsButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.settingsButtonSprite.getY() + game.settingsButtonSprite.getHeight()))) {
            if (game.userPrefs.getBoolean("Hats", true)) {
                tradeUp = false;
                delete = false;
                if (inventoryObjectList.size() > 0) {
                    renderStart = game.inventoryTopBackgroundSprite.getY() - inventoryObjectList.get(0).highlightSprite.getHeight();
                }
                inventoryObjectList.clear();
                game.setScreen(game.eventStickventoryScreen);
            }
        } else if ((x > game.filterButtonSprite.getX()) && (x < (game.filterButtonSprite.getX() + game.filterButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.filterButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.filterButtonSprite.getY() + game.filterButtonSprite.getHeight()))) {
			if (!delete) {
				if (tradeUp && (highlightNumber == 10)) {
					TradeUp();
				} else if (!tradeUp) {
					inventoryObjectList.clear();
					game.setScreen(game.filterScreen);
				}
			}
		} else if ((x > game.tradeUpButtonSprite.getX()) && (x < (game.tradeUpButtonSprite.getX() + game.tradeUpButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.tradeUpButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.tradeUpButtonSprite.getY() + game.tradeUpButtonSprite.getHeight()))) {
			if (!delete) {
				tradeUp = !tradeUp;
				if (!tradeUp) {
					game.filterScreen.clearFilter();
											
					ResetInventory();
					
					invenTop = 0;
					loadSize = 0;
					highlightNumber = 0;
			        
					if ((game.inventoryList.size() - invenTop) > game.maxInven) {
						loadSize = game.maxInven;
					} else {
						loadSize = game.inventoryList.size() - invenTop;
					}		
					
					inventoryObjectList.clear();
					LoadInventoryObjects();
					PositionInventory();
				}
			}
		} else if ((x > game.deleteButtonSprite.getX()) && (x < (game.deleteButtonSprite.getX() + game.deleteButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.deleteButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.deleteButtonSprite.getY() + game.deleteButtonSprite.getHeight()))) {
			if (!tradeUp) {
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
						
						for (int i = 0; i<game.inventoryList.size(); i++) {
							if (game.inventoryList.get(i).highlighted) {
								game.myRequestHandler.removeGun(game.inventoryList.get(i).gunID);
								
								if (game.inventoryList.get(i).rarity.equals("1")) {
									game.statList.get(0).rarityType1 -= 1;
									game.myRequestHandler.addStat(0, "rarityType1", -1);
								} else if (game.inventoryList.get(i).rarity.equals("2")) {
									game.statList.get(0).rarityType2 -= 1;
									game.myRequestHandler.addStat(0, "rarityType2", -1);							
								} else if (game.inventoryList.get(i).rarity.equals("3")) {
									game.statList.get(0).rarityType3 -= 1;
									game.myRequestHandler.addStat(0, "rarityType3", -1);								
								} else if (game.inventoryList.get(i).rarity.equals("4")) {
									game.statList.get(0).rarityType4 -= 1;
									game.myRequestHandler.addStat(0, "rarityType4", -1);								
								} else if (game.inventoryList.get(i).rarity.equals("5")) {
									game.statList.get(0).rarityType5 -= 1;
									game.myRequestHandler.addStat(0, "rarityType5", -1);								
								}
								if (game.inventoryList.get(i).stattrak == true) {
									game.statList.get(0).stattrak -= 1;
									game.myRequestHandler.addStat(0, "stattrak", -1);							
								}
							}
						}
						ResetInventory();
						
						invenTop = 0;
						loadSize = 0;
						highlightNumber = 0;
				        
						if ((game.inventoryList.size() - invenTop) > game.maxInven) {
							loadSize = game.maxInven;
						} else {
							loadSize = game.inventoryList.size() - invenTop;
						}		
						
						inventoryObjectList.clear();
						LoadInventoryObjects();
						PositionInventory();
					}
				}
			}
		} else if ((x > game.cancelDeleteButtonSprite.getX()) && (x < (game.cancelDeleteButtonSprite.getX() + game.cancelDeleteButtonSprite.getWidth())) && ((Gdx.graphics.getHeight() -y) > game.cancelDeleteButtonSprite.getY()) && ((Gdx.graphics.getHeight() -y) < (game.cancelDeleteButtonSprite.getY() + game.cancelDeleteButtonSprite.getHeight()))) {
			if (!tradeUp) {
				delete = false;
				highlightNumber = 0;
				float newXpos = (Gdx.graphics.getWidth() / 2) - (game.deleteButtonSprite.getWidth() / 2);
				float newYpos = (Gdx.graphics.getWidth() / 80);
				game.deleteButtonSprite.setPosition(newXpos,newYpos);
				for (int i = 0; i<game.inventoryList.size(); i++) {
					if (game.inventoryList.get(i).highlighted) {
						game.inventoryList.get(i).highlighted = false;
					}
				}
				for (int i = 0; i<inventoryObjectList.size(); i++) {
					if (inventoryObjectList.get(i).highlighted) {
						inventoryObjectList.get(i).highlighted = false;
					}
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
		if (inventoryObjectList.size() > 0) {
			if ((touchDownPoint.y - screenY) > 0) {
				if ((inventoryObjectList.get(loadSize - 1).highlightSprite.getY() - (Gdx.graphics.getWidth() / 40)) < game.filterButtonSprite.getHeight()) {
					renderStart += (touchDownPoint.y - screenY);
					
					if ((renderStart - (Gdx.graphics.getWidth() / 40) - ((inventoryObjectList.get(0).highlightSprite.getHeight() + (Gdx.graphics.getWidth() / 40)) * (game.maxInvenGrid.y - 1))) > game.filterButtonSprite.getHeight()) {
						renderStart = ((inventoryObjectList.get(0).highlightSprite.getHeight() + (Gdx.graphics.getWidth() / 40)) * (game.maxInvenGrid.y - 1)) + (Gdx.graphics.getWidth() / 40) + game.filterButtonSprite.getHeight();
					}
					if ((invenTop + game.maxInven) < game.inventoryList.size()) {			
						if (inventoryObjectList.get(0).highlightSprite.getY() > game.inventoryTopBackgroundSprite.getY()) {
							
							invenTop += game.maxInvenGrid.x;
							renderStart = inventoryObjectList.get((int) game.maxInvenGrid.x).highlightSprite.getY();
							RepurposeInventoryObjects();
						}
					}
				}
			} else if ((touchDownPoint.y - screenY) < 0) {
				if (((inventoryObjectList.get(0).highlightSprite.getY() + inventoryObjectList.get(0).highlightSprite.getHeight()) > game.inventoryTopBackgroundSprite.getY()) || (invenTop != 0)) {
					renderStart += (touchDownPoint.y - screenY);
					
					if ((renderStart + inventoryObjectList.get(0).highlightSprite.getHeight()) < game.inventoryTopBackgroundSprite.getY()) {
						renderStart = game.inventoryTopBackgroundSprite.getY() - inventoryObjectList.get(0).highlightSprite.getHeight();
					}
					
					if (invenTop != 0) {
						if ((inventoryObjectList.get(loadSize - 1).highlightSprite.getY() + inventoryObjectList.get(loadSize - 1).highlightSprite.getHeight()) < 0) {
							
							invenTop -= game.maxInvenGrid.x;
							renderStart = inventoryObjectList.get(0).highlightSprite.getY() + inventoryObjectList.get(0).highlightSprite.getHeight() + (Gdx.graphics.getWidth() / 40);
							RepurposeInventoryObjects();
						}
					}
				}
			}	
			
			PositionInventory();
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
