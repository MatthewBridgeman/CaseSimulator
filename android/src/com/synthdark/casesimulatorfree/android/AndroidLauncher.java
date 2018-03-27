package com.synthdark.casesimulatorfree.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.synthdark.casesimulatorfree.ActivityRequestHandler;
import com.synthdark.casesimulatorfree.CaseSimulatorFree;

public class AndroidLauncher extends AndroidApplication  implements ActivityRequestHandler {

    Locale myLocale;

    SQLManager mySQLManager;
    CaseSimulatorFree caseSim;

    protected AdView adView;
    protected View gameView;
    protected boolean firstAd;
    boolean adsOff;
    private InterstitialAd interstitialAd;
    RelativeLayout layout;

    public enum TrackerName {
        APP_TRACKER,
    }
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    Tracker appTracker;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        myLocale = Locale.getDefault();
        //myLocale = new Locale("ru","RU");

        //Google play bugfix for ads: https://groups.google.com/forum/#!topic/google-admob-ads-sdk/_x12qmjWI7M
        try {
            Class.forName("android.os.AsyncTask");
        } catch (Throwable ignore) {
        }

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        LoadDatabase loadDatabase = new LoadDatabase(this, myLocale);
        loadDatabase.execute();

        //mySQLManager = new SQLManager(this, myLocale);

        //appTracker = getTracker(TrackerName.APP_TRACKER);
        //appTracker.enableAdvertisingIdCollection(true);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;

        adsOff = false;//isAppInstalled("com.synthdark.casesimulatorpro.android");

        caseSim = new CaseSimulatorFree(this, myLocale);

        if (adsOff) {
            initialize(caseSim, config);
        } else {
            layout = new RelativeLayout(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(params);

            layout.addView(createGameView(config));
            layout.addView(createAdView());

            startAdvertising(adView);
            createIntersitual();
        }

        System.out.println("DOING STAFF");
    }

    public void setSplashScreen() {
        setContentView(R.layout.splashscreen);
    }

    public void executeGame(SQLManager manager) {
        mySQLManager = manager;
        caseSim.initializeGame();
        setContentView(layout);
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    @Override
    public void onResume() {
        super.onResume();
        //GoogleAnalytics.getInstance(this).reportActivityStart(this);
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //GoogleAnalytics.getInstance(this).reportActivityStop(this);
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void createIntersitual() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-8850101842420714/5132126988");
        AdRequest interstitialRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(interstitialRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed( ) {
                AdRequest interstitialRequest = new AdRequest.Builder().build();
                interstitialAd.loadAd(interstitialRequest);
            }
        });
    }

    private AdView createAdView() {
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-8850101842420714/8179144181");
        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        adView.setLayoutParams(adParams);
        adView.setBackgroundColor(Color.BLACK);

        return adView;
    }

    private View createGameView(AndroidApplicationConfiguration config) {
        gameView = initializeForView(caseSim, config);
        return gameView;
    }

    private void startAdvertising(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void showInterstital() {
        if (!adsOff) {
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (interstitialAd.isLoaded()) {
                            interstitialAd.show();
                        } else {
                            AdRequest interstitialRequest = new AdRequest.Builder().build();
                            interstitialAd.loadAd(interstitialRequest);
                            interstitialAd.show();
                        }
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(R.xml.app_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    @Override
    public void sendTrackerScreenName(String path) {
        Log.i("CaseSimulator", path);

        //appTracker.setScreenName(path);
        //appTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public void sendTrackerEvent(String category, String action, String label, int value) {
        Log.i("CaseSimulator", category + ", " + action + ", " + label + ", " + value);

       // appTracker.send(new HitBuilders.EventBuilder()
        //        .setCategory(category)
        //        .setAction(action)
       //         .setLabel(label)
       //         .setValue(value)
       //         .build());
    }

    @Override
    public void initializeDatabase() {
        mySQLManager.initilize();
    }

    @Override
    public void openDatabase() {
        mySQLManager.openDataBase();
    }

    @Override
    public void closeDatabase() {
        mySQLManager.close();
    }

    @Override
    public List<List> listAllCases() {
        return mySQLManager.listAllCases();
    }

    @Override
    public List<List>  listAllGunsFromCase(int caseID) {
        return mySQLManager.listAllGunsFromCase(caseID);
    }
    @Override
    public List<String> listGunWithTexture(String gunTexture) {
        return mySQLManager.listGunWithTexture(gunTexture);
    }

    @Override
    public List<String> listGunWithID(int gunID) {
        return mySQLManager.listGunWithID(gunID);
    }

    @Override
    public List<List> listAllKnives() {
        return mySQLManager.listAllKnives();
    }

    @Override
    public List<String> listKnifeWithTexture(String knifeTexture) {
        return mySQLManager.listKnifeWithTexture(knifeTexture);
    }

    @Override
    public List<String> listKnifeWithID(int knifeID) {
        return mySQLManager.listKnifeWithID(knifeID);
    }

    @Override
    public List<List> listAllEventStickers() {
        return mySQLManager.listAllEventStickers();
    }


    @Override
    public void addGun(int gunID, String rarity, int caseID, String wear, int stattrak) {
        mySQLManager.addGun(gunID, rarity, caseID, wear, stattrak);
    }

    @Override
    public void addEventSticker(int evStickID) {
        mySQLManager.addEventSticker(evStickID);
    }


    @Override
    public void removeGun(int gunID) {
        mySQLManager.removeGun(gunID);
    }

    @Override
    public void removeEventSticker(int evStickID) {
        mySQLManager.removeEventSticker(evStickID);
    }

    @Override
    public List<List> listAllInventory() {
        return mySQLManager.listAllInventory();
    }

    @Override
    public List<List> listAllInventoryOld() {
        return mySQLManager.listAllInventoryOld();
    }

    @Override
    public List<List> listAllEventStickventory() {
        return mySQLManager.listAllEventStickventory();
    }


    @Override
    public List<List> filterInventory(String query) {
        return mySQLManager.filterInventory(query);
    }

    @Override
    public List<List> filterGuns(String query) {
        return mySQLManager.filterGuns(query);
    }

    @Override
    public List<List> listAllStats() {
        return mySQLManager.listAllStats();
    }

    public void addStat(int caseID, String type, int value) {
        mySQLManager.addStat(caseID, type, value);
    }

    @Override
    public void refreshDatabase() {
        try {
            mySQLManager.refreshDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dropAndCreateNewInventory() {
        mySQLManager.dropAndCreateNewInventory();
    }

    public  boolean checkOldInventory() {
        return mySQLManager.checkOldInventory();
    }
}