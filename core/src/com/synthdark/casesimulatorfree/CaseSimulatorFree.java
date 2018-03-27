package com.synthdark.casesimulatorfree;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.text.ParseException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.I18NBundle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import sun.rmi.runtime.Log;

public class CaseSimulatorFree extends Game {

    ActivityRequestHandler myRequestHandler;
    Preferences userPrefs;

    CaseScreen caseScreen;
    TickerScreen tickerScreen;
    GunScreen gunScreen;
    InventoryScreen inventoryScreen;
    EventStickventoryScreen eventStickventoryScreen;
    FilterScreen filterScreen;
    SettingsScreen settingsScreen;
    SplashScreen splashScreen;

    I18NBundle textBundle;
    Locale myLocale;

    /////FONTS\\\\\
    //Coloured Stats
    BitmapFont Arial_Regular_Small_Blue;
    BitmapFont Arial_Regular_Small_Purple;
    BitmapFont Arial_Regular_Small_Pink;
    BitmapFont Arial_Regular_Small_Red;
    BitmapFont Arial_Regular_Small_Gold;

    //Other Text
    BitmapFont Arial_Regular_Smallest;
    BitmapFont Arial_Regular_Small;
    BitmapFont Arial_Regular_Normal;
    BitmapFont Arial_Regular_Normal_Blue;
    BitmapFont Arial_Bold_Normal;
    BitmapFont Arial_Bold_Large;
    BitmapFont Arial_Bold_Largest;
    
    //Other Languages Characters
    static final String ENGLISH_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "1234567890.,:;_¡!¿?\"'+-*/|()[]={}";
    static final String RUSSIAN_CHARACTERS = "�?БВГДЕ�?ЖЗИЙКЛМ�?ОПРСТУФХЦЧШЩЪЫЬЭЮЯ" + "абвгдеёжзийклмнопр�?туфхцчшщъыь�?ю�?";

    List<Cases> caseList;
    List<Knives> knivesList;
    List<WeaponObject> tickerObjectList;
    List<Guns> inventoryList;
    List<EventSticker> eventStickventoryList;
    List<Stats> statList;
    List<EventSticker> eventStickerList;
    List<EventSticker> dropableEventStickerList;
    List<SnowParticle> snowParticleList;
    Stats statTotalList;
    WeaponObject tickedGunObject;

    SpriteBatch batch;
    TextureAtlas caseTextureAtlas;
    TextureAtlas gunTextureAtlas;
    TextureAtlas gunBackgroundAtlas;
    TextureAtlas knifeTextureAtlas;
    TextureAtlas tickBoxTextureAtlas;
    TextureAtlas eventStickerTextureAtlas;
    TextureAtlas snowTextureAtlas;
    Sprite appBackgroundSprite;
    Sprite caseBackgroundSprite;
    Sprite caseForegroundSprite;
    Sprite appTickerBackgroundSprite;
    Sprite tickerBackgroundSprite;
    Sprite tickerForegroundSprite;
    Sprite inventoryTopBackgroundSprite;
    Sprite inventoryBottomBackgroundSprite;
    Sprite inventoryButtonSprite;
    Sprite settingsButtonSprite;
    Sprite filterButtonSprite;
    Sprite tradeUpButtonSprite;
    Sprite deleteButtonSprite;
    Sprite cancelDeleteButtonSprite;
    Sprite keepButtonSprite;
    Sprite discardButtonSprite;
    Sprite caseSprite;
    Sprite statLeftBackgroundSprite;
    Sprite statRightBackgroundSprite;
    Sprite gunShowBackgroundSprite;
    Sprite turnPageSprite;

    Sound caseOpenSound;
    Sound caseTickSound;
    Sound caseDisplaySound;

    Random random;
    Vector3 centerScreen;
    int currentCase;
    boolean inventoryOpen;
    Vector2 maxInvenGrid;
    int maxInven;
    Vector2 gunBackgroundSize;

    boolean snow;

    int sessionCaseOpened;
    boolean caseJustOpened;

    public CaseSimulatorFree(ActivityRequestHandler handler, Locale locale){
        myLocale = locale;
        myRequestHandler = handler;
    }

    @Override
    public void create() {
        centerScreen = new Vector3(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        random = new Random();
        batch = new SpriteBatch();
        caseList = new ArrayList<Cases>();
        knivesList = new ArrayList<Knives>();
        eventStickerList = new ArrayList<EventSticker>();
        tickerObjectList = new ArrayList<WeaponObject>();
        inventoryList = new ArrayList<Guns>();
        eventStickventoryList = new ArrayList<EventSticker>();
        dropableEventStickerList = new ArrayList<EventSticker>();
        statList = new ArrayList<Stats>();
        statTotalList = new Stats();
        currentCase = 0;
        inventoryOpen = false;
        sessionCaseOpened = 0;
        caseJustOpened = false;
        snow = false;

        //LOAD PREFERENCES\\
        userPrefs = Gdx.app.getPreferences("UserPreferences");

        //I18n\\
        FileHandle textBundleFile = Gdx.files.internal("I18n/CaseSimulator");
        textBundle = I18NBundle.createBundle(textBundleFile, myLocale);
        if (!userPrefs.getString("Database", "nothing").equals(myLocale.getISO3Language())) {
            userPrefs.putString("Database", myLocale.getISO3Language());
            userPrefs.flush();
            // myRequestHandler.refreshDatabase();
        }

        //GENERATE FONTS\\
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Arial-Regular.ttf"));
        String CHARACTERS = ENGLISH_CHARACTERS;
        int fontSizeSmallest = 40;
        int fontSizeSmall = 35;
        int fontSizeNormal = 30;
        int fontSizeLarge = 20;
        int fontSizeLargest = 10;
        if (myLocale.getISO3Language().equals("rus")) {
            CHARACTERS += RUSSIAN_CHARACTERS;
            fontSizeSmallest = 50;
            fontSizeSmall = 45;
            fontSizeNormal = 40;
            fontSizeLarge = 30;
        }

        //Stats
        Arial_Regular_Small_Blue = generator.generateFont((Gdx.graphics.getHeight() / fontSizeSmall), CHARACTERS, false);
        Arial_Regular_Small_Blue.setColor(0.2f, 0.2f, 1.0f, 1.0f);
        Arial_Regular_Small_Purple = generator.generateFont((Gdx.graphics.getHeight() / fontSizeSmall), CHARACTERS, false);
        Arial_Regular_Small_Purple.setColor(0.4f, 0.1f, 1.0f, 1.0f);
        Arial_Regular_Small_Pink = generator.generateFont((Gdx.graphics.getHeight() / fontSizeSmall), CHARACTERS, false);
        Arial_Regular_Small_Pink.setColor(0.9f, 0.0f, 1.0f, 0.9f);
        Arial_Regular_Small_Red = generator.generateFont((Gdx.graphics.getHeight() / fontSizeSmall), CHARACTERS, false);
        Arial_Regular_Small_Red.setColor(1.0f, 0.2f, 0.0f, 1.0f);
        Arial_Regular_Small_Gold = generator.generateFont((Gdx.graphics.getHeight() / fontSizeSmall), CHARACTERS, false);
        Arial_Regular_Small_Gold.setColor(1.0f, 0.7f, 0.0f, 1.0f);

        //Other Text
        Arial_Regular_Small = generator.generateFont((Gdx.graphics.getHeight() / fontSizeSmall), CHARACTERS, false);
        Arial_Regular_Small.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        Arial_Regular_Smallest = generator.generateFont((Gdx.graphics.getHeight() / fontSizeSmallest), CHARACTERS, false);
        Arial_Regular_Smallest.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        Arial_Regular_Normal = generator.generateFont((Gdx.graphics.getHeight() / fontSizeNormal), CHARACTERS, false);
        Arial_Regular_Normal.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        Arial_Regular_Normal_Blue = generator.generateFont((Gdx.graphics.getHeight() / fontSizeNormal), CHARACTERS, false);
        Arial_Regular_Normal_Blue.setColor(0.1f, 0.9f, 1.0f, 1.0f);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Arial-Bold.ttf"));
        Arial_Bold_Normal = generator.generateFont((Gdx.graphics.getHeight() / fontSizeNormal), CHARACTERS, false);
        Arial_Bold_Normal.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        Arial_Bold_Large = generator.generateFont((Gdx.graphics.getHeight() / fontSizeLarge), CHARACTERS, false);
        Arial_Bold_Large.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        Arial_Bold_Largest = generator.generateFont((Gdx.graphics.getHeight() / fontSizeLargest), CHARACTERS, false);
        Arial_Bold_Largest.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        generator.dispose();

        //TEXTURES\\
        Texture appBackgroundTexture = new Texture(Gdx.files.internal("Textures/App Background.png"));

        //SPRITES\\
        appBackgroundSprite = new Sprite(appBackgroundTexture);
        appBackgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        splashScreen = new SplashScreen(this);
        setScreen(splashScreen);
    }

    public void Initialize() {
        float newXpos;
        float newYpos;

        //LOAD DATE
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH);

        //LOAD SOUNDS\\
        LoadSounds();

        //POPULATE CASE AND GUN LIST\\
        List<List> caseSQLList = new ArrayList<List>();
        caseSQLList = myRequestHandler.listAllCases();
        for (int i = 0; i < caseSQLList.size(); i++) {
            Cases newCase = new Cases();
            newCase.caseID = Integer.parseInt(caseSQLList.get(i).get(0).toString());
            newCase.caseName = caseSQLList.get(i).get(1).toString();
            newCase.caseTexture = caseSQLList.get(i).get(2).toString();
            newCase.caseType = caseSQLList.get(i).get(3).toString();

            List<Guns> gunList = new ArrayList<Guns>();
            List<List> gunSQLList = new ArrayList<List>();
            gunSQLList = myRequestHandler.listAllGunsFromCase(newCase.caseID);
            for (int j = 0; j < gunSQLList.size(); j++) {
                Guns newGun = new Guns();
                newGun.gunID = Integer.parseInt(gunSQLList.get(j).get(0).toString());
                newGun.gunType = gunSQLList.get(j).get(1).toString();
                newGun.gunSkin = gunSQLList.get(j).get(2).toString();
                newGun.gunTexture = gunSQLList.get(j).get(3).toString();
                newGun.rarity = gunSQLList.get(j).get(4).toString();
                gunList.add(newGun);
            }
            newCase.gunList = gunList;

            caseList.add(newCase);
            caseList.get(i).GenerateRarityLists();
        }
        caseSQLList.clear();

        //POPULATE KNIFE LIST\\
        List<List> knifeSQLList = new ArrayList<List>();
        knifeSQLList = myRequestHandler.listAllKnives();
        String knifeID = "0";
        List<Guns> knifeList = new ArrayList<Guns>();

        for (int i = 0; i < knifeSQLList.size(); i++) {
            if (knifeID.equals("0")) {
                knifeID = knifeSQLList.get(i).get(4).toString();
            } else if (!knifeID.equals(knifeSQLList.get(i).get(4).toString())) {
                Knives newKnives = new Knives();
                newKnives.knifeType = knifeList.get(1).gunType;
                if (knifeID.equals("8")) {
                    newKnives.knifeType = "Chroma";
                }
                newKnives.knifeList = knifeList;
                knivesList.add(newKnives);
                knifeList = new ArrayList<Guns>();
                knifeID = knifeSQLList.get(i).get(4).toString();
            }
            Guns newKnife = new Guns();
            newKnife.gunID = Integer.parseInt(knifeSQLList.get(i).get(0).toString());
            newKnife.gunType = knifeSQLList.get(i).get(1).toString();
            newKnife.gunSkin = knifeSQLList.get(i).get(2).toString();
            newKnife.gunTexture = knifeSQLList.get(i).get(3).toString();
            knifeList.add(newKnife);
        }
        Knives newKnives = new Knives();
        newKnives.knifeType = knifeList.get(1).gunType;
        if (knifeID.equals("8")) {
            newKnives.knifeType = "Chroma";
        }
        newKnives.knifeList = knifeList;
        knivesList.add(newKnives);
        knifeSQLList.clear();

        //POPULATE EVENT STICKER LIST\\
        List<List> eventStickersSQLList = new ArrayList<List>();
        eventStickersSQLList = myRequestHandler.listAllEventStickers();
        for (int j = 0; j < eventStickersSQLList.size(); j++) {
            EventSticker newEventSticker = new EventSticker();
            newEventSticker.eventStickerID = Integer.parseInt(eventStickersSQLList.get(j).get(0).toString());
            newEventSticker.eventStickerType = eventStickersSQLList.get(j).get(1).toString();
            newEventSticker.eventStickerSkin = eventStickersSQLList.get(j).get(2).toString();
            newEventSticker.eventStickerTexture = eventStickersSQLList.get(j).get(3).toString();
            newEventSticker.rarity = eventStickersSQLList.get(j).get(4).toString();
            newEventSticker.spawnRarity = Double.parseDouble(eventStickersSQLList.get(j).get(5).toString());
            newEventSticker.startDate = eventStickersSQLList.get(j).get(6).toString();
            newEventSticker.endDate = eventStickersSQLList.get(j).get(7).toString();
            eventStickerList.add(newEventSticker);
        }
        eventStickersSQLList.clear();

        //POPULATE DROPABLE STICKER LIST
        for (int i = 0; i < eventStickerList.size(); i++) {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            String startDateStr = eventStickerList.get(i).startDate;
            String endDateStr = eventStickerList.get(i).endDate;
            try {
                startDate.setTime(sdf.parse(startDateStr));
                endDate.setTime(sdf.parse(endDateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ((today.after(startDate)) && (today.before(endDate))) {
                dropableEventStickerList.add(eventStickerList.get(i));

                if (userPrefs.getInteger(eventStickerList.get(i).eventStickerTexture, -1) == -1) {
                    int randomCase = random.nextInt(caseList.size() - 1);
                    userPrefs.putInteger(eventStickerList.get(i).eventStickerTexture, randomCase);
                    userPrefs.flush();
                }
            }
        }

        /*
        //LOAD ALL ITEMS INTO INVENTORY
        for (int i = 0; i < caseList.size(); i++) {
            for (int j = 0; j < caseList.get(i).gunList.size(); j++) {
                if (!caseList.get(i).gunList.get(j).rarity.equals("5")) {
                    myRequestHandler.addGun(caseList.get(i).gunList.get(j).gunID, caseList.get(i).gunList.get(j).rarity, caseList.get(i).caseID, "Factory New", 0);
                    if (caseList.get(i).gunList.get(j).rarity.equals("1")) {
                        myRequestHandler.addStat(0, "rarityType1", 1);
                        myRequestHandler.addStat(caseList.get(i).caseID, "rarityType1", 1);
                    } else if (caseList.get(i).gunList.get(j).rarity.equals("2")) {
                        myRequestHandler.addStat(0, "rarityType2", 1);
                        myRequestHandler.addStat(caseList.get(i).caseID, "rarityType2", 1);
                    } else if (caseList.get(i).gunList.get(j).rarity.equals("3")) {
                        myRequestHandler.addStat(0, "rarityType3", 1);
                        myRequestHandler.addStat(caseList.get(i).caseID, "rarityType3", 1);
                    } else if (caseList.get(i).gunList.get(j).rarity.equals("4")) {
                        myRequestHandler.addStat(0, "rarityType4", 1);
                        myRequestHandler.addStat(caseList.get(i).caseID, "rarityType4", 1);
                    }
                    myRequestHandler.addStat(0, "open", 1);
                    myRequestHandler.addStat(caseList.get(i).caseID, "open", 1);
                }
            }
        }
        for (int i = 0; i < knivesList.size(); i++) {
            for (int j = 0; j < knivesList.get(i).knifeList.size(); j++) {
                myRequestHandler.addGun(knivesList.get(i).knifeList.get(j).gunID, "5", 1, "Factory New", 0);

                myRequestHandler.addStat(0, "rarityType5", 1);
                myRequestHandler.addStat(1, "rarityType5", 1);
                myRequestHandler.addStat(0, "open", 1);
                myRequestHandler.addStat(1, "open", 1);
            }
        }
        for (int i = 0; i < eventStickerList.size(); i++) {
            myRequestHandler.addEventSticker(eventStickerList.get(i).eventStickerID);
        }
        */

        //POPULATE INVENTORY LIST\\
        List<List> inventorySQLList = new ArrayList<List>();
        inventorySQLList = myRequestHandler.listAllInventory();
        for (int i = 0; i < inventorySQLList.size(); i++) {
            Guns newGun = new Guns();
            List<String> itemSQLList = new ArrayList<String>();
            newGun.rarity = inventorySQLList.get(i).get(2).toString();

            if (newGun.rarity.equals("5")) {
                itemSQLList = myRequestHandler.listKnifeWithID(Integer.parseInt(inventorySQLList.get(i).get(1).toString()));
            } else {
                itemSQLList = myRequestHandler.listGunWithID(Integer.parseInt(inventorySQLList.get(i).get(1).toString()));
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
            inventoryList.add(newGun);
        }
        inventorySQLList.clear();

        //POPULATE EVENTSTICKVENTORY LIST\\
        List<List> eventStickventorySQLList = new ArrayList<List>();
        eventStickventorySQLList = myRequestHandler.listAllEventStickventory();
        for (int i = 0; i < eventStickventorySQLList.size(); i++) {
            EventSticker newEventSticker = new EventSticker();
            newEventSticker.eventStickvenID = Integer.parseInt(eventStickventorySQLList.get(i).get(0).toString());
            newEventSticker.eventStickerID = Integer.parseInt(eventStickventorySQLList.get(i).get(1).toString());
            newEventSticker.eventStickerType = eventStickerList.get(newEventSticker.eventStickerID - 1).eventStickerType;
            newEventSticker.eventStickerSkin = eventStickerList.get(newEventSticker.eventStickerID - 1).eventStickerSkin;
            newEventSticker.eventStickerTexture = eventStickerList.get(newEventSticker.eventStickerID - 1).eventStickerTexture;
            newEventSticker.rarity = eventStickerList.get(newEventSticker.eventStickerID - 1).rarity;
            newEventSticker.spawnRarity = eventStickerList.get(newEventSticker.eventStickerID - 1).spawnRarity;

            newEventSticker.highlighted = false;
            eventStickventoryList.add(newEventSticker);
        }
        eventStickventorySQLList.clear();

        //POPULATE STAT LIST\\
        List<List> statQLList = new ArrayList<List>();
        statQLList = myRequestHandler.listAllStats();
        for (int i = 0; i < statQLList.size(); i++) {
            Stats newStat = new Stats();
            newStat.caseID = Integer.parseInt(statQLList.get(i).get(0).toString());
            newStat.open = Integer.parseInt(statQLList.get(i).get(1).toString());
            newStat.rarityType1 = Integer.parseInt(statQLList.get(i).get(2).toString());
            newStat.rarityType2 = Integer.parseInt(statQLList.get(i).get(3).toString());
            newStat.rarityType3 = Integer.parseInt(statQLList.get(i).get(4).toString());
            newStat.rarityType4 = Integer.parseInt(statQLList.get(i).get(5).toString());
            newStat.rarityType5 = Integer.parseInt(statQLList.get(i).get(6).toString());
            newStat.stattrak = Integer.parseInt(statQLList.get(i).get(7).toString());
            statList.add(newStat);

            if (i > 0) {
                statTotalList.open += Integer.parseInt(statQLList.get(i).get(1).toString());
                statTotalList.rarityType1 += Integer.parseInt(statQLList.get(i).get(2).toString());
                statTotalList.rarityType2 += Integer.parseInt(statQLList.get(i).get(3).toString());
                statTotalList.rarityType3 += Integer.parseInt(statQLList.get(i).get(4).toString());
                statTotalList.rarityType4 += Integer.parseInt(statQLList.get(i).get(5).toString());
                statTotalList.rarityType5 += Integer.parseInt(statQLList.get(i).get(6).toString());
                statTotalList.stattrak += Integer.parseInt(statQLList.get(i).get(7).toString());
            }
        }
        statQLList.clear();

        //TEXTURES\\
        Texture caseBackgroundTexture = new Texture(Gdx.files.internal("Textures/Case Background.png"));
        Texture caseForegroundTexture = new Texture(Gdx.files.internal("Textures/Case Foreground.png"));
        Texture appTickerBackgroundTexture = new Texture(Gdx.files.internal("Textures/App Ticker Background.png"));
        Texture tickerBackgroundTexture = new Texture(Gdx.files.internal("Textures/Ticker Background.png"));
        Texture tickerForegroundTexture = new Texture(Gdx.files.internal("Textures/Ticker Foreground.png"));
        Texture inventoryTopBackgroundTexture = new Texture(Gdx.files.internal("Textures/Inventory Top Background.png"));
        Texture inventoryBottomBackgroundTexture = new Texture(Gdx.files.internal("Textures/Inventory Bottom Background.png"));
        Texture rectangleButtonTexture = new Texture(Gdx.files.internal("Textures/RectangleButton.png"));
        Texture roundButtonTexture = new Texture(Gdx.files.internal("Textures/RoundButton.png"));
        Texture turnPageTexture = new Texture(Gdx.files.internal("Textures/TurnPage.png"));

        //TEXTURE ATLAS\\
        caseTextureAtlas = new TextureAtlas(Gdx.files.internal("Cases/Cases.pack"));
        gunTextureAtlas = new TextureAtlas(Gdx.files.internal("Guns/Guns.pack"));
        gunBackgroundAtlas = new TextureAtlas(Gdx.files.internal("Gun Backgrounds/Gun Backgrounds.pack"));
        knifeTextureAtlas = new TextureAtlas(Gdx.files.internal("Knives/Knives.pack"));
        tickBoxTextureAtlas = new TextureAtlas(Gdx.files.internal("TickBox/TickBox.pack"));
        eventStickerTextureAtlas = new TextureAtlas(Gdx.files.internal("Event Stickers/Event Stickers.pack"));
        snowTextureAtlas = new TextureAtlas(Gdx.files.internal("Snow/Snow.pack"));

        //SPRITES\\

        caseBackgroundSprite = new Sprite(caseBackgroundTexture);
        float imageRatio = caseBackgroundSprite.getWidth() / caseBackgroundSprite.getHeight();
        caseBackgroundSprite.setSize(Gdx.graphics.getWidth() / 2.6f, (Gdx.graphics.getWidth() / imageRatio) / 2.4f);
        newXpos = (Gdx.graphics.getWidth() / 2) - (caseBackgroundSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - (caseBackgroundSprite.getHeight() / 2);
        caseBackgroundSprite.setPosition(newXpos, newYpos);

        caseForegroundSprite = new Sprite(caseForegroundTexture);
        imageRatio = caseForegroundSprite.getWidth() / caseForegroundSprite.getHeight();
        caseForegroundSprite.setSize(Gdx.graphics.getWidth() / 2.6f, (Gdx.graphics.getWidth() / imageRatio) / 2.6f);
        newXpos = (Gdx.graphics.getWidth() / 2) - (caseForegroundSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - (caseForegroundSprite.getHeight() / 2);
        caseForegroundSprite.setPosition(newXpos, newYpos);

        appTickerBackgroundSprite = new Sprite(appTickerBackgroundTexture);
        appTickerBackgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        tickerBackgroundSprite = new Sprite(tickerBackgroundTexture);
        imageRatio = tickerBackgroundSprite.getWidth() / tickerBackgroundSprite.getHeight();
        tickerBackgroundSprite.setSize(Gdx.graphics.getWidth() / 1.43f, (Gdx.graphics.getWidth() / imageRatio) / 1.43f);
        newXpos = (Gdx.graphics.getWidth() / 2) - (tickerBackgroundSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - (tickerBackgroundSprite.getHeight() / 2);
        tickerBackgroundSprite.setPosition(newXpos, newYpos);

        tickerForegroundSprite = new Sprite(tickerForegroundTexture);
        imageRatio = tickerForegroundSprite.getWidth() / tickerForegroundSprite.getHeight();
        tickerForegroundSprite.setSize(Gdx.graphics.getWidth() / 1.43f, (Gdx.graphics.getWidth() / imageRatio) / 1.43f);
        newXpos = (Gdx.graphics.getWidth() / 2) - (tickerForegroundSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - (tickerForegroundSprite.getHeight() / 2);
        tickerForegroundSprite.setPosition(newXpos, newYpos);

        caseSprite = caseTextureAtlas.createSprite(caseList.get(currentCase).caseTexture);
        if (caseSprite.getWidth() > caseSprite.getHeight()) {
            imageRatio = caseSprite.getWidth() / caseSprite.getHeight();
            caseSprite.setSize(Gdx.graphics.getWidth() / 3f, (Gdx.graphics.getWidth() / imageRatio) / 3f);
        } else {
            imageRatio = caseSprite.getHeight() / caseSprite.getWidth();
            caseSprite.setSize((Gdx.graphics.getWidth() / imageRatio) / 4.1f, Gdx.graphics.getWidth() / 4.1f);
        }
        newXpos = (Gdx.graphics.getWidth() / 2) - (caseSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - (caseSprite.getHeight() / 2);
        caseSprite.setPosition(newXpos, newYpos);

        inventoryTopBackgroundSprite = new Sprite(inventoryTopBackgroundTexture);
        imageRatio = inventoryTopBackgroundSprite.getWidth() / inventoryTopBackgroundSprite.getHeight();
        inventoryTopBackgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() / imageRatio);
        newYpos = Gdx.graphics.getHeight() - inventoryTopBackgroundSprite.getHeight();
        inventoryTopBackgroundSprite.setPosition(0, newYpos);

        inventoryBottomBackgroundSprite = new Sprite(inventoryBottomBackgroundTexture);
        imageRatio = inventoryBottomBackgroundSprite.getWidth() / inventoryBottomBackgroundSprite.getHeight();
        inventoryBottomBackgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() / imageRatio);
        inventoryBottomBackgroundSprite.setPosition(0, 0);

        inventoryButtonSprite = new Sprite(rectangleButtonTexture);
        imageRatio = inventoryButtonSprite.getWidth() / inventoryButtonSprite.getHeight();
        inventoryButtonSprite.setSize(Gdx.graphics.getWidth() / 6, (Gdx.graphics.getWidth() / imageRatio) / 4);
        newXpos = (Gdx.graphics.getWidth() / 80);
        newYpos = Gdx.graphics.getHeight() - inventoryButtonSprite.getHeight() - (Gdx.graphics.getWidth() / 80);
        inventoryButtonSprite.setPosition(newXpos, newYpos);

        settingsButtonSprite = new Sprite(rectangleButtonTexture);
        imageRatio = settingsButtonSprite.getWidth() / settingsButtonSprite.getHeight();
        settingsButtonSprite.setSize(Gdx.graphics.getWidth() / 6, (Gdx.graphics.getWidth() / imageRatio) / 4);
        newXpos = Gdx.graphics.getWidth() - settingsButtonSprite.getWidth() - (Gdx.graphics.getWidth() / 80);
        newYpos = Gdx.graphics.getHeight() - settingsButtonSprite.getHeight() - (Gdx.graphics.getWidth() / 80);
        settingsButtonSprite.setPosition(newXpos, newYpos);

        filterButtonSprite = new Sprite(rectangleButtonTexture);
        imageRatio = filterButtonSprite.getWidth() / filterButtonSprite.getHeight();
        filterButtonSprite.setSize(Gdx.graphics.getWidth() / 6, (Gdx.graphics.getWidth() / imageRatio) / 4);
        newXpos = (Gdx.graphics.getWidth() / 80);
        newYpos = (Gdx.graphics.getWidth() / 80);
        filterButtonSprite.setPosition(newXpos, newYpos);

        tradeUpButtonSprite = new Sprite(rectangleButtonTexture);
        imageRatio = tradeUpButtonSprite.getWidth() / tradeUpButtonSprite.getHeight();
        tradeUpButtonSprite.setSize(Gdx.graphics.getWidth() / 6, (Gdx.graphics.getWidth() / imageRatio) / 4);
        newXpos = Gdx.graphics.getWidth() - tradeUpButtonSprite.getWidth() - (Gdx.graphics.getWidth() / 80);
        newYpos = (Gdx.graphics.getWidth() / 80);
        tradeUpButtonSprite.setPosition(newXpos, newYpos);

        deleteButtonSprite = new Sprite(rectangleButtonTexture);
        imageRatio = deleteButtonSprite.getWidth() / deleteButtonSprite.getHeight();
        deleteButtonSprite.setSize(Gdx.graphics.getWidth() / 6, (Gdx.graphics.getWidth() / imageRatio) / 4);
        newXpos = (Gdx.graphics.getWidth() / 2) - (deleteButtonSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getWidth() / 80);
        deleteButtonSprite.setPosition(newXpos, newYpos);

        cancelDeleteButtonSprite = new Sprite(rectangleButtonTexture);
        imageRatio = cancelDeleteButtonSprite.getWidth() / cancelDeleteButtonSprite.getHeight();
        cancelDeleteButtonSprite.setSize(Gdx.graphics.getWidth() / 6, (Gdx.graphics.getWidth() / imageRatio) / 4);
        newXpos = ((Gdx.graphics.getWidth() / 10) * 6.35f) - (deleteButtonSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getWidth() / 80);
        cancelDeleteButtonSprite.setPosition(newXpos, newYpos);

        discardButtonSprite = new Sprite(roundButtonTexture);
        imageRatio = discardButtonSprite.getWidth() / discardButtonSprite.getHeight();
        discardButtonSprite.setSize(Gdx.graphics.getWidth() / 7, (Gdx.graphics.getWidth() / imageRatio) / 7);
        newXpos = (caseBackgroundSprite.getX() / 2) - (discardButtonSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - (discardButtonSprite.getHeight() / 2);
        discardButtonSprite.setPosition(newXpos, newYpos);

        keepButtonSprite = new Sprite(roundButtonTexture);
        imageRatio = keepButtonSprite.getWidth() / keepButtonSprite.getHeight();
        keepButtonSprite.setSize(Gdx.graphics.getWidth() / 7, (Gdx.graphics.getWidth() / imageRatio) / 7);
        newXpos += (caseBackgroundSprite.getX() + caseBackgroundSprite.getWidth());
        newYpos = (Gdx.graphics.getHeight() / 2) - (keepButtonSprite.getHeight() / 2);
        keepButtonSprite.setPosition(newXpos, newYpos);

        statLeftBackgroundSprite = new Sprite(caseBackgroundTexture);
        imageRatio = statLeftBackgroundSprite.getWidth() / statLeftBackgroundSprite.getHeight();
        statLeftBackgroundSprite.setSize(Gdx.graphics.getWidth() / 5.6f, (Gdx.graphics.getWidth() / imageRatio) / 2.8f);
        newXpos = (caseBackgroundSprite.getX() / 2) - (statLeftBackgroundSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - (statLeftBackgroundSprite.getHeight() / 2);
        statLeftBackgroundSprite.setPosition(newXpos, newYpos);

        statRightBackgroundSprite = new Sprite(caseBackgroundTexture);
        imageRatio = statRightBackgroundSprite.getWidth() / statRightBackgroundSprite.getHeight();
        statRightBackgroundSprite.setSize(Gdx.graphics.getWidth() / 5.6f, (Gdx.graphics.getWidth() / imageRatio) / 2.8f);
        newXpos = (Gdx.graphics.getWidth() - ((Gdx.graphics.getWidth() - (caseBackgroundSprite.getX() + caseBackgroundSprite.getWidth())) / 2)) - (statRightBackgroundSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - (statRightBackgroundSprite.getHeight() / 2);
        statRightBackgroundSprite.setPosition(newXpos, newYpos);

        gunShowBackgroundSprite = new Sprite(caseBackgroundTexture);
        imageRatio = gunShowBackgroundSprite.getWidth() / gunShowBackgroundSprite.getHeight();
        gunShowBackgroundSprite.setSize(Gdx.graphics.getWidth() / 3.7f, (Gdx.graphics.getWidth() / imageRatio) / 2.4f);
        newXpos = (Gdx.graphics.getWidth() / 2) - (gunShowBackgroundSprite.getWidth() / 2);
        newYpos = (Gdx.graphics.getHeight() / 2) - (gunShowBackgroundSprite.getHeight() / 2);
        gunShowBackgroundSprite.setPosition(newXpos, newYpos);

        turnPageSprite = new Sprite(turnPageTexture);
        imageRatio = turnPageSprite.getWidth() / turnPageSprite.getHeight();
        turnPageSprite.setSize(Gdx.graphics.getWidth() / 50f, (Gdx.graphics.getWidth() / imageRatio) / 50f);
        newXpos = (statLeftBackgroundSprite.getX() + statLeftBackgroundSprite.getWidth()) - (turnPageSprite.getWidth() / 2);
        newYpos = statLeftBackgroundSprite.getY() + (statLeftBackgroundSprite.getHeight() / 10);
        turnPageSprite.setPosition(newXpos, newYpos);

        //GENERATE INITIAL TICKER OBJECTS\\
        newXpos = 0;
        for (int j = 0; j < 5; j++) {
            float randomNumber = (random.nextInt(1000) / 10);
            if (randomNumber <= 78.8) {  //78.8%
                newXpos = GenerateNewGunObject(newXpos, caseList.get(currentCase).gun1List);
            } else if (randomNumber <= 95.8) {  //17%
                newXpos = GenerateNewGunObject(newXpos, caseList.get(currentCase).gun2List);
            } else if (randomNumber <= 98.6) {  //2.8%
                newXpos = GenerateNewGunObject(newXpos, caseList.get(currentCase).gun3List);
            } else if (randomNumber <= 99.6) {  //1.0%
                if (caseList.get(currentCase).gun4List.size() == 0) {
                    newXpos = GenerateNewGunObject(newXpos, caseList.get(currentCase).gun3List);
                } else {
                    newXpos = GenerateNewGunObject(newXpos, caseList.get(currentCase).gun4List);
                }
            } else {  //0.4%
                if (caseList.get(currentCase).gun5List.size() == 0) {
                    if (caseList.get(currentCase).gun4List.size() == 0) {
                        newXpos = GenerateNewGunObject(newXpos, caseList.get(currentCase).gun3List);
                    } else {
                        newXpos = GenerateNewGunObject(newXpos, caseList.get(currentCase).gun4List);
                    }
                } else {
                    newXpos = GenerateNewGunObject(newXpos, caseList.get(currentCase).gun5List);
                }
            }
        }

        //GENERATE INVEN MAXES\\
        float imageRatioTemp1 = gunBackgroundAtlas.findRegion("Large Gold Gun Background").originalWidth;
        float imageRatioTemp2 = gunBackgroundAtlas.findRegion("Large Gold Gun Background").originalHeight;
        imageRatio = imageRatioTemp1 / imageRatioTemp2;
        gunBackgroundSize = new Vector2(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);

        maxInvenGrid = new Vector2(0, 0);
        newXpos = (Gdx.graphics.getWidth() / 40);
        newYpos = inventoryTopBackgroundSprite.getY() + inventoryBottomBackgroundSprite.getHeight();

        float calcXpos = newXpos;
        while ((calcXpos + gunBackgroundSize.x) < Gdx.graphics.getWidth()) {
            calcXpos += gunBackgroundSize.x + (Gdx.graphics.getWidth() / 40);
            maxInvenGrid.x++;
        }
        float calcYpos = newYpos;
        while ((calcYpos - gunBackgroundSize.y) > 0) {
            calcYpos -= gunBackgroundSize.y - (Gdx.graphics.getWidth() / 40);
            maxInvenGrid.y++;
        }
        maxInvenGrid.y++;

        maxInven = (int) (maxInvenGrid.x * maxInvenGrid.y);

        /////GENERATE HOLIDAY EFFECTS\\\\\
        //GENERATE SNOW
        Calendar snowStartDate = Calendar.getInstance();
        Calendar snowEndDate = Calendar.getInstance();
        String snowStartDateStr = "11 12 2014";
        String snowEndDateStr = "30 12 2014";
        try {
            snowStartDate.setTime(sdf.parse(snowStartDateStr));
            snowEndDate.setTime(sdf.parse(snowEndDateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ((today.after(snowStartDate)) && (today.before(snowEndDate))) {
            snow = true;
            snowParticleList = new ArrayList<SnowParticle>();
            for (int i = 0; i < 100; i++) {
                SnowParticle newSnowParticle = new SnowParticle(this);
                snowParticleList.add(newSnowParticle);
            }
        }

        //START SCREENS\\
        caseScreen = new CaseScreen(this);
        tickerScreen = new TickerScreen(this);
        gunScreen = new GunScreen(this);
        inventoryScreen = new InventoryScreen(this);
        eventStickventoryScreen = new EventStickventoryScreen(this);
        filterScreen = new FilterScreen(this);
        settingsScreen = new SettingsScreen(this);

        setScreen(caseScreen);
    }

    public void ConversionCode() {
        //JSON INVENTORY CONVERSION\\
        Gson gson = new Gson();
        if (Gdx.files.local("Inventory.json").exists()) {
            FileHandle file = Gdx.files.local("Inventory.json");
            BufferedReader json = new BufferedReader(file.reader());
            Type collectionType = new TypeToken<List<Guns>>() {
            }.getType();
            List<Guns> inventoryConversionList = gson.fromJson(json, collectionType);

            for (int i = 0; i < inventoryConversionList.size(); i++) {
                if (inventoryConversionList.get(i).gunTexture.equals("Let's Roll-oll")) {
                    inventoryConversionList.get(i).gunType = "Lets Roll-oll";
                    inventoryConversionList.get(i).gunTexture = "Lets Roll-oll";
                }
                String sqlQuery = "SELECT * FROM Guns WHERE gunTexture='" + inventoryConversionList.get(i).gunTexture + "';";
                if (inventoryConversionList.get(i).rarity.equals("Gold Gun Background")) {
                    sqlQuery = "SELECT * FROM Knives WHERE knifeTexture='" + inventoryConversionList.get(i).gunTexture + "';";
                }
                List<List> inventoryConversionSQLList = myRequestHandler.filterGuns(sqlQuery);

                String gunType = inventoryConversionSQLList.get(0).get(1).toString();
                String gunSkin = inventoryConversionSQLList.get(0).get(2).toString();
                String gunTexture = inventoryConversionSQLList.get(0).get(3).toString();
                String rarity = inventoryConversionSQLList.get(0).get(4).toString();
                int caseID = Integer.parseInt(inventoryConversionSQLList.get(0).get(5).toString());
                if (inventoryConversionList.get(i).rarity.equals("Gold Gun Background")) {
                    rarity = "5";
                    caseID = 1;
                    if (inventoryConversionList.get(i).gunType.equals("Huntsman Knife")) {
                        caseID = 8;
                    }
                    if (inventoryConversionList.get(i).gunType.equals("Butterfly Knife")) {
                        caseID = 9;
                    }
                }
                String wear = inventoryConversionList.get(i).wear;
                int stattrak = 0;
                if (inventoryConversionList.get(i).stattrak) {
                    stattrak = 1;
                }

                List<String> gunSQLList = new ArrayList<String>();
                gunSQLList = myRequestHandler.listGunWithTexture(gunTexture);

                myRequestHandler.addGun(Integer.parseInt(gunSQLList.get(0).toString()), rarity, caseID, wear, stattrak);

                myRequestHandler.addStat(0, "open", 1);
                myRequestHandler.addStat(caseID, "open", 1);

                if (rarity.equals("1")) {
                    myRequestHandler.addStat(0, "rarityType1", 1);
                    myRequestHandler.addStat(caseID, "rarityType1", 1);
                } else if (rarity.equals("2")) {
                    myRequestHandler.addStat(0, "rarityType2", 1);
                    myRequestHandler.addStat(caseID, "rarityType2", 1);
                } else if (rarity.equals("3")) {
                    myRequestHandler.addStat(0, "rarityType3", 1);
                    myRequestHandler.addStat(caseID, "rarityType3", 1);
                } else if (rarity.equals("4")) {
                    myRequestHandler.addStat(0, "rarityType4", 1);
                    myRequestHandler.addStat(caseID, "rarityType4", 1);
                } else if (rarity.equals("5")) {
                    myRequestHandler.addStat(0, "rarityType5", 1);
                    myRequestHandler.addStat(caseID, "rarityType5", 1);
                }
                if (stattrak == 1) {
                    myRequestHandler.addStat(0, "stattrak", 1);
                    myRequestHandler.addStat(caseID, "stattrak", 1);
                }
                inventoryConversionSQLList.clear();
                gunSQLList.clear();
            }
            try {
                json.close();
                Gdx.files.local("Inventory.json").delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //JSON PRO INVENTORY CONVERSION\\
        String proPath = Gdx.files.getLocalStoragePath();
        proPath = proPath.replace("com.synthdark.casesimulatorfree.android", "com.synthdark.casesimulatorpro.android");
        File proInven = new File(proPath + "Inventory.json");
        if (proInven.exists() && !Gdx.files.local("READ.yes").exists()) {
            File proRead = new File(proPath + "READ.yes");
            if (proRead.exists()) {
                FileHandle file = new FileHandle(proPath + "Inventory.json");
                BufferedReader json = new BufferedReader(file.reader());
                Type collectionType = new TypeToken<List<Guns>>() {
                }.getType();
                List<Guns> inventoryConversionList = gson.fromJson(json, collectionType);

                for (int i = 0; i < inventoryConversionList.size(); i++) {
                    if (inventoryConversionList.get(i).gunTexture.equals("Let's Roll-oll")) {
                        inventoryConversionList.get(i).gunType = "Lets Roll-oll";
                        inventoryConversionList.get(i).gunTexture = "Lets Roll-oll";
                    }
                    String sqlQuery = "SELECT * FROM Guns WHERE gunTexture='" + inventoryConversionList.get(i).gunTexture + "';";
                    if (inventoryConversionList.get(i).rarity.equals("Gold Gun Background")) {
                        sqlQuery = "SELECT * FROM Knives WHERE knifeTexture='" + inventoryConversionList.get(i).gunTexture + "';";
                    }
                    List<List> inventoryConversionSQLList = myRequestHandler.filterGuns(sqlQuery);

                    String gunType = inventoryConversionSQLList.get(0).get(1).toString();
                    String gunSkin = inventoryConversionSQLList.get(0).get(2).toString();
                    String gunTexture = inventoryConversionSQLList.get(0).get(3).toString();
                    String rarity = inventoryConversionSQLList.get(0).get(4).toString();
                    int caseID = Integer.parseInt(inventoryConversionSQLList.get(0).get(5).toString());
                    if (inventoryConversionList.get(i).rarity.equals("Gold Gun Background")) {
                        rarity = "5";
                        caseID = 1;
                        if (inventoryConversionList.get(i).gunType.equals("Huntsman Knife")) {
                            caseID = 8;
                        }
                        if (inventoryConversionList.get(i).gunType.equals("Butterfly Knife")) {
                            caseID = 9;
                        }
                    }
                    String wear = inventoryConversionList.get(i).wear;
                    int stattrak = 0;
                    if (inventoryConversionList.get(i).stattrak) {
                        stattrak = 1;
                    }

                    List<String> gunSQLList = new ArrayList<String>();
                    gunSQLList = myRequestHandler.listGunWithTexture(gunTexture);

                    myRequestHandler.addGun(Integer.parseInt(gunSQLList.get(0).toString()), rarity, caseID, wear, stattrak);

                    myRequestHandler.addStat(0, "open", 1);
                    myRequestHandler.addStat(caseID, "open", 1);

                    if (rarity.equals("1")) {
                        myRequestHandler.addStat(0, "rarityType1", 1);
                        myRequestHandler.addStat(caseID, "rarityType1", 1);
                    } else if (rarity.equals("2")) {
                        myRequestHandler.addStat(0, "rarityType2", 1);
                        myRequestHandler.addStat(caseID, "rarityType2", 1);
                    } else if (rarity.equals("3")) {
                        myRequestHandler.addStat(0, "rarityType3", 1);
                        myRequestHandler.addStat(caseID, "rarityType3", 1);
                    } else if (rarity.equals("4")) {
                        myRequestHandler.addStat(0, "rarityType4", 1);
                        myRequestHandler.addStat(caseID, "rarityType4", 1);
                    } else if (rarity.equals("5")) {
                        myRequestHandler.addStat(0, "rarityType5", 1);
                        myRequestHandler.addStat(caseID, "rarityType5", 1);
                    }
                    if (stattrak == 1) {
                        myRequestHandler.addStat(0, "stattrak", 1);
                        myRequestHandler.addStat(caseID, "stattrak", 1);
                    }
                    inventoryConversionSQLList.clear();
                    gunSQLList.clear();
                }
                try {
                    json.close();
                    Gdx.files.local("READ.yes").file().createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //SQL OLD TO NEW INVENTORY CONVERSION FIX
        if (myRequestHandler.checkOldInventory()) {
            List<List> inventorySQLList = new ArrayList<List>();
            inventorySQLList = myRequestHandler.listAllInventoryOld();
            for (int i = 0; i < inventorySQLList.size(); i++) {
                Guns newGun = new Guns();
                newGun.gunID = Integer.parseInt(inventorySQLList.get(i).get(0).toString());

                newGun.gunType = inventorySQLList.get(i).get(1).toString();
                newGun.gunSkin = inventorySQLList.get(i).get(2).toString();
                newGun.gunTexture = inventorySQLList.get(i).get(3).toString();
                newGun.rarity = inventorySQLList.get(i).get(4).toString();
                newGun.caseID = Integer.parseInt(inventorySQLList.get(i).get(5).toString());

                newGun.wear = inventorySQLList.get(i).get(6).toString();
                if (newGun.wear.equals("Battle-Scarred")) {
                    newGun.wear = "battle-scarred";
                    System.out.println("switched");
                } else if (newGun.wear.equals("Well-Worn")) {
                    newGun.wear = "well-worn";
                    System.out.println("switched");
                } else if (newGun.wear.equals("Field-Tested")) {
                    newGun.wear = "field-tested";
                    System.out.println("switched");
                } else if (newGun.wear.equals("Minimal Wear")) {
                    newGun.wear = "minimal-wear";
                    System.out.println("switched");
                } else if (newGun.wear.equals("Factory New")) {
                    newGun.wear = "factory-new";
                    System.out.println("switched");
                }

                if (Integer.parseInt(inventorySQLList.get(i).get(7).toString()) == 0) {
                    newGun.stattrak = false;
                } else {
                    newGun.stattrak = true;
                }
                newGun.highlighted = false;
                inventoryList.add(newGun);
            }
            inventorySQLList.clear();

            myRequestHandler.dropAndCreateNewInventory();

            for (int i = 0; i < inventoryList.size(); i++) {
                List<String> itemSQLList = new ArrayList<String>();

                if (inventoryList.get(i).rarity.equals("5")) {
                    itemSQLList = myRequestHandler.listKnifeWithTexture(inventoryList.get(i).gunTexture);
                } else {
                    itemSQLList = myRequestHandler.listGunWithTexture(inventoryList.get(i).gunTexture);
                }

                int stattrak;
                if (inventoryList.get(i).stattrak) {
                    stattrak = 1;
                } else {
                    stattrak = 0;
                }
                myRequestHandler.addGun(Integer.parseInt(itemSQLList.get(0).toString()), inventoryList.get(i).rarity, inventoryList.get(i).caseID, inventoryList.get(i).wear, stattrak);
            }
            inventoryList.clear();
        }
    }

    public void LoadSounds() {
        if (!userPrefs.getBoolean("Classic Sounds", false)) {
            caseOpenSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/New/caseOpenSound.wav"));
            caseTickSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/New/caseTickSound.wav"));
            caseDisplaySound = Gdx.audio.newSound(Gdx.files.internal("Sounds/New/caseDisplaySound.wav"));
        } else {
            caseOpenSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Old/caseOpenSound.wav"));
            caseTickSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Old/caseTickSound.wav"));
            caseDisplaySound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Old/caseDisplaySound.wav"));
        }
    }

    public float GenerateNewGunObject(float newXpos, List<Guns> gunList) {

        Sprite gunBackgroundSprite = new Sprite();
        Sprite gunSprite = new Sprite();

        Texture highlightTexture = new Texture(Gdx.files.internal("Gun Backgrounds/Highlight.png"));
        Sprite highlightSprite = new Sprite(highlightTexture);
        float imageRatio = highlightSprite.getWidth() / highlightSprite.getHeight();
        highlightSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);


        Texture stattrakTexture = new Texture(Gdx.files.internal("Textures/Stattrak.png"));
        Sprite stattrakSprite = new Sprite(stattrakTexture);
        imageRatio = stattrakSprite.getWidth() / stattrakSprite.getHeight();
        stattrakSprite.setSize(Gdx.graphics.getWidth() / 15f, (Gdx.graphics.getWidth() / imageRatio) / 15f);

        WeaponObject newWeapon = new WeaponObject(this, gunSprite, gunBackgroundSprite, highlightSprite, stattrakSprite, "", "", "", "");
        tickerObjectList.add(newWeapon);

        newXpos += gunSprite.getWidth() + (Gdx.graphics.getWidth() / 40);

        return newXpos;
    }

    public int RepurposeGunObject(int gunItem, float tickerSpeed, int lastGun, boolean newSet) {
        float newXpos = 0;
        if (newSet) {
            if (lastGun != 0) {
                newXpos = (tickerObjectList.get(lastGun - 1).gunSprite.getX() + tickerObjectList.get(lastGun - 1).gunSprite.getWidth()) + (Gdx.graphics.getWidth() / 40);
            }
        } else {
            newXpos = (tickerObjectList.get(lastGun).gunSprite.getX() + tickerObjectList.get(lastGun).gunSprite.getWidth()) + (Gdx.graphics.getWidth() / 40);
            if (lastGun == 4) {
                newXpos -= Gdx.graphics.getDeltaTime() * tickerSpeed;
            }
        }

        double randomEventSticker = (random.nextInt(1000) / 10.0);

        List<EventSticker> tempEventStickerList = new ArrayList<EventSticker>();
        for (int i = 0; i < dropableEventStickerList.size(); i++) {
            if (randomEventSticker <= dropableEventStickerList.get(i).spawnRarity) {
                if (currentCase == userPrefs.getInteger(dropableEventStickerList.get(i).eventStickerTexture, -1)) {
                    tempEventStickerList.add(dropableEventStickerList.get(i));
                }
            }
        }

        if ((userPrefs.getBoolean("event_stickers", true)) && (tempEventStickerList.size() > 0)) {
            int tempRandomEventSticker = (random.nextInt(tempEventStickerList.size()));

            String rarityBackground = new String();
            if (eventStickerList.get(tempRandomEventSticker).rarity.equals("1")) {
                rarityBackground = "Blue Gun Background";
            } else if (eventStickerList.get(tempRandomEventSticker).rarity.equals("2")) {
                rarityBackground = "Purple Gun Background";
            } else if (eventStickerList.get(tempRandomEventSticker).rarity.equals("3")) {
                rarityBackground = "Pink Gun Background";
            } else if (eventStickerList.get(tempRandomEventSticker).rarity.equals("4")) {
                rarityBackground = "Red Gun Background";
            } else if (eventStickerList.get(tempRandomEventSticker).rarity.equals("5")) {
                rarityBackground = "Gold Gun Background";
            }

            tickerObjectList.get(gunItem).gunBackgroundSprite = gunBackgroundAtlas.createSprite(rarityBackground);
            float imageRatio = tickerObjectList.get(gunItem).gunBackgroundSprite.getWidth() / tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight();
            tickerObjectList.get(gunItem).gunBackgroundSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
            float newYpos = (Gdx.graphics.getHeight() / 2) - (tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight() / 2);
            tickerObjectList.get(gunItem).gunBackgroundSprite.setPosition(newXpos, newYpos);

            float highlightX = tickerObjectList.get(gunItem).gunBackgroundSprite.getX() + (tickerObjectList.get(gunItem).gunBackgroundSprite.getWidth() / 2);
            float highlightY = tickerObjectList.get(gunItem).gunBackgroundSprite.getY() + (tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight() / 2);
            tickerObjectList.get(gunItem).highlightSprite.setCenter(highlightX, highlightY);

            float stattrackX = tickerObjectList.get(gunItem).gunBackgroundSprite.getX() + tickerObjectList.get(gunItem).gunBackgroundSprite.getWidth() - ((tickerObjectList.get(gunItem).stattrakSprite.getWidth() / 4) * 3);
            float stattrackY = tickerObjectList.get(gunItem).gunBackgroundSprite.getY() + tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight() - ((tickerObjectList.get(gunItem).stattrakSprite.getHeight() / 4) * 3);
            tickerObjectList.get(gunItem).stattrakSprite.setPosition(stattrackX, stattrackY);

            tickerObjectList.get(gunItem).gunSprite = eventStickerTextureAtlas.createSprite(tempEventStickerList.get(tempRandomEventSticker).eventStickerTexture);
            imageRatio = tickerObjectList.get(gunItem).gunSprite.getWidth() / tickerObjectList.get(gunItem).gunSprite.getHeight();
            tickerObjectList.get(gunItem).gunSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
            newYpos = (tickerObjectList.get(gunItem).gunBackgroundSprite.getY() + tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight()) - (tickerObjectList.get(gunItem).gunSprite.getHeight() / 1.17f);
            tickerObjectList.get(gunItem).gunSprite.setPosition(newXpos, newYpos);

            tickerObjectList.get(gunItem).gunID = tempEventStickerList.get(tempRandomEventSticker).eventStickerID;
            tickerObjectList.get(gunItem).gunType = tempEventStickerList.get(tempRandomEventSticker).eventStickerType;
            tickerObjectList.get(gunItem).gunSkin = tempEventStickerList.get(tempRandomEventSticker).eventStickerSkin;
            tickerObjectList.get(gunItem).rarity = tempEventStickerList.get(tempRandomEventSticker).rarity;
            tickerObjectList.get(gunItem).gunTexture = tempEventStickerList.get(tempRandomEventSticker).eventStickerTexture;
            tickerObjectList.get(gunItem).wear = "";
            tickerObjectList.get(gunItem).stattrak = false;
            tickerObjectList.get(gunItem).eventSticker = true;
            tickerObjectList.get(gunItem).GenerateText();
        } else {
            int maxRanNum = 1000;
            if (caseList.get(currentCase).gun5List.size() == 0) {
                maxRanNum = 996;
            }
            if (caseList.get(currentCase).gun4List.size() == 0) {
                maxRanNum = 986;
            }
            if (caseList.get(currentCase).gun3List.size() == 0) {
                maxRanNum = 958;
            }
            if (caseList.get(currentCase).gun2List.size() == 0) {
                maxRanNum = 788;
            }


            double randomNumber = (random.nextInt(maxRanNum) / 10.0);
            List<Guns> gunList = new ArrayList<Guns>();

            if ((randomNumber <= 78.8) && (caseList.get(currentCase).gun1List.size() != 0)) {  //78.8%
                gunList = caseList.get(currentCase).gun1List;
            } else if ((randomNumber <= 95.8) && (caseList.get(currentCase).gun2List.size() != 0)) {  //17%
                gunList = caseList.get(currentCase).gun2List;
            } else if ((randomNumber <= 98.6) && (caseList.get(currentCase).gun3List.size() != 0)) {  //2.8%
                gunList = caseList.get(currentCase).gun3List;
            } else if ((randomNumber <= 99.6) && (caseList.get(currentCase).gun4List.size() != 0)) {  //1.0%
                gunList = caseList.get(currentCase).gun4List;
            } else if (caseList.get(currentCase).gun5List.size() != 0) {  //0.4%
                gunList = caseList.get(currentCase).gun5List;
            }



/*            if ((randomNumber <= 78.8) && (caseList.get(currentCase).gun1List.size() != 0)) {  //78.8%
                gunList = caseList.get(currentCase).gun1List;
            } else if ((randomNumber <= 95.8) && (caseList.get(currentCase).gun2List.size() != 0)) {  //17%
                gunList = caseList.get(currentCase).gun2List;
            } else if ((randomNumber <= 98.6) && (caseList.get(currentCase).gun3List.size() != 0)) {  //2.8%
                gunList = caseList.get(currentCase).gun3List;
            } else if ((randomNumber <= 99.6) && (caseList.get(currentCase).gun4List.size() != 0)) {  //1.0%

                if (caseList.get(currentCase).gun4List.size() == 0) {
                    gunList = caseList.get(currentCase).gun3List;
                } else {
                    gunList = caseList.get(currentCase).gun4List;
                }

            } else if (caseList.get(currentCase).gun5List.size() != 0) {  //0.4%

                if (caseList.get(currentCase).gun5List.size() == 0) {
                    if (caseList.get(currentCase).gun4List.size() == 0) {
                        if (caseList.get(currentCase).gun3List.size() == 0) {
                            gunList = caseList.get(currentCase).gun2List;
                        } else {
                            gunList = caseList.get(currentCase).gun3List;
                        }
                    } else {
                        gunList = caseList.get(currentCase).gun4List;
                    }
                } else {
                    gunList = caseList.get(currentCase).gun5List;
                }

            }
            */

            int randomNumber2 = random.nextInt(gunList.size());
            double randomStattrak = (random.nextInt(1000) / 10.0);
            double randomWear = (random.nextInt(1000) / 10.0);


            boolean stattrak = false;
            //if (!((caseList.get(currentCase).caseID > 12) && (caseList.get(currentCase).caseID < 20))) {
            if ((caseList.get(currentCase).caseType.equals("case")) || (caseList.get(currentCase).caseType.equals("esport"))) {
                if (randomStattrak <= 4) {
                    stattrak = true;
                }
            }

            String wear = "";
            //if (!((caseList.get(currentCase).caseID > 12) && (caseList.get(currentCase).caseID < 20))) {
            if ((caseList.get(currentCase).caseType.equals("case")) || (caseList.get(currentCase).caseType.equals("esport"))) {
                if (randomWear <= 11) {  //11%
                    wear = "battle-scarred";
                } else if (randomWear <= 18.5) {  //7.5%
                    wear = "well-worn";
                } else if (randomWear <= 67) {  //48.5%
                    wear = "field-tested";
                } else if (randomWear <= 97) {  //30%
                    wear = "minimal-wear";
                } else {  //3%
                    wear = "factory-new";
                }
            }

            String rarityBackground = new String();
            if (gunList.get(randomNumber2).rarity.equals("1")) {
                rarityBackground = "Blue Gun Background";
            } else if (gunList.get(randomNumber2).rarity.equals("2")) {
                rarityBackground = "Purple Gun Background";
            } else if (gunList.get(randomNumber2).rarity.equals("3")) {
                rarityBackground = "Pink Gun Background";
            } else if (gunList.get(randomNumber2).rarity.equals("4")) {
                rarityBackground = "Red Gun Background";
            } else if (gunList.get(randomNumber2).rarity.equals("5")) {
                rarityBackground = "Gold Gun Background";
            }

            tickerObjectList.get(gunItem).gunBackgroundSprite = gunBackgroundAtlas.createSprite(rarityBackground);
            float imageRatio = tickerObjectList.get(gunItem).gunBackgroundSprite.getWidth() / tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight();
            tickerObjectList.get(gunItem).gunBackgroundSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
            float newYpos = (Gdx.graphics.getHeight() / 2) - (tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight() / 2);
            tickerObjectList.get(gunItem).gunBackgroundSprite.setPosition(newXpos, newYpos);

            float highlightX = tickerObjectList.get(gunItem).gunBackgroundSprite.getX() + (tickerObjectList.get(gunItem).gunBackgroundSprite.getWidth() / 2);
            float highlightY = tickerObjectList.get(gunItem).gunBackgroundSprite.getY() + (tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight() / 2);
            tickerObjectList.get(gunItem).highlightSprite.setCenter(highlightX, highlightY);

            float stattrackX = tickerObjectList.get(gunItem).gunBackgroundSprite.getX() + tickerObjectList.get(gunItem).gunBackgroundSprite.getWidth() - ((tickerObjectList.get(gunItem).stattrakSprite.getWidth() / 4) * 3);
            float stattrackY = tickerObjectList.get(gunItem).gunBackgroundSprite.getY() + tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight() - ((tickerObjectList.get(gunItem).stattrakSprite.getHeight() / 4) * 3);
            tickerObjectList.get(gunItem).stattrakSprite.setPosition(stattrackX, stattrackY);

            tickerObjectList.get(gunItem).gunSprite = gunTextureAtlas.createSprite(gunList.get(randomNumber2).gunTexture);
            System.out.println(gunList.get(randomNumber2).gunTexture);
            imageRatio = tickerObjectList.get(gunItem).gunSprite.getWidth() / tickerObjectList.get(gunItem).gunSprite.getHeight();
            tickerObjectList.get(gunItem).gunSprite.setSize(Gdx.graphics.getWidth() / 4.7f, (Gdx.graphics.getWidth() / imageRatio) / 4.7f);
            newYpos = (tickerObjectList.get(gunItem).gunBackgroundSprite.getY() + tickerObjectList.get(gunItem).gunBackgroundSprite.getHeight()) - (tickerObjectList.get(gunItem).gunSprite.getHeight() / 1.17f);
            tickerObjectList.get(gunItem).gunSprite.setPosition(newXpos, newYpos);

            float newXpos2 = tickerObjectList.get(gunItem).gunBackgroundSprite.getX() + tickerObjectList.get(gunItem).gunBackgroundSprite.getWidth() - ((tickerObjectList.get(gunItem).stattrakSprite.getWidth() / 4) * 3);
            tickerObjectList.get(gunItem).stattrakSprite.setPosition(newXpos2, tickerObjectList.get(gunItem).stattrakSprite.getY());

            tickerObjectList.get(gunItem).gunID = gunList.get(randomNumber2).gunID;
            tickerObjectList.get(gunItem).gunType = gunList.get(randomNumber2).gunType;
            tickerObjectList.get(gunItem).gunSkin = gunList.get(randomNumber2).gunSkin;
            tickerObjectList.get(gunItem).rarity = gunList.get(randomNumber2).rarity;
            tickerObjectList.get(gunItem).gunTexture = gunList.get(randomNumber2).gunTexture;
            tickerObjectList.get(gunItem).wear = wear;
            tickerObjectList.get(gunItem).stattrak = stattrak;
            tickerObjectList.get(gunItem).eventSticker = false;
            tickerObjectList.get(gunItem).GenerateText();
        }

        lastGun++;
        if (lastGun > 4) {
            lastGun = 0;
        }

        return lastGun;
    }

    public void dispose() {
        batch.dispose();

        Arial_Regular_Small.dispose();
        Arial_Regular_Small_Blue.dispose();
        Arial_Regular_Small_Purple.dispose();
        Arial_Regular_Small_Pink.dispose();
        Arial_Regular_Small_Red.dispose();
        Arial_Regular_Small_Gold.dispose();
        Arial_Regular_Normal.dispose();
        Arial_Regular_Normal_Blue.dispose();
        Arial_Bold_Normal.dispose();
        Arial_Bold_Large.dispose();

        caseOpenSound.dispose();
        caseTickSound.dispose();
        caseDisplaySound.dispose();

        knifeTextureAtlas.dispose();
        gunTextureAtlas.dispose();
        gunBackgroundAtlas.dispose();
        caseTextureAtlas.dispose();

        for (int i = 0; i < caseList.size(); i++) {
            caseList.get(i).dispose();
        }
        for (int i = 0; i < knivesList.size(); i++) {
            knivesList.get(i).dispose();
        }
        caseList.clear();
        knivesList.clear();
        tickerObjectList.clear();
        inventoryList.clear();
        statList.clear();

        caseScreen.dispose();
        tickerScreen.dispose();
        gunScreen.dispose();
        inventoryScreen.dispose();
        filterScreen.dispose();
        settingsScreen.dispose();

    }
}
	
