package com.synthdark.casesimulatorfree.android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.Locale;

public class LoadDatabase extends AsyncTask<Context, Integer, String> {

    Locale myLocale;
    AndroidLauncher androidLauncher;
    SQLManager mySQLManager;

    public LoadDatabase(AndroidLauncher launcher, Locale locale) {
        System.out.println("LOAD DATABASE");
        myLocale = locale;
        androidLauncher = launcher;
    }

    @Override
    protected String doInBackground(Context... params) {
        System.out.println("DO DATABASE IN BACKGROUND");
        mySQLManager = new SQLManager(androidLauncher, myLocale);
        return null;
    }

    @Override
    protected void onPreExecute() {
        System.out.println("LOAD SPLASHSCREEN");
        androidLauncher.setSplashScreen();
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("LOAD GAME");
        super.onPostExecute(result);
        androidLauncher.executeGame(mySQLManager);
    }
}
