package com.synthdark.casesimulatorfree.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.System;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLManager extends SQLiteOpenHelper{
    private static String DB_NAME = "CaseSimulatorDatabase";
    private static String DB_PATH;
    private static int DB_VERSION = 6;

    private final Context myContext;

    private Locale myLocale;
 
    public SQLManager(Context context, Locale locale) {
    	super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        myLocale = locale;
        DB_PATH = myContext.getDatabasePath(DB_NAME).getPath();
    }	
    
	@Override
	public void onCreate(SQLiteDatabase db) {
	 	try 
	 	{
	 		importDataBase(db);
	 		importDataBase2(db);
		} 
	 	catch (IOException e) 
		{
	 		throw new Error("Error copying database");
	 	}
	}
	
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			dropDatabaseTables(db);
			importDataBase(db);
			checkStatsRow(db);
            checkEventStickventory(db);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshDatabase() throws IOException {
        SQLiteDatabase db = this.getReadableDatabase();

		dropDatabaseTables(db);
		importDataBase(db);
		checkStatsRow(db);
        checkEventStickventory(db);
	}
	
    private void importDataBase(SQLiteDatabase db) throws IOException{
    	InputStream myInput = myContext.getAssets().open("SQL/" + myLocale.getISO3Language() + "/CaseSimulatorMainSQL.sql");
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(myInput));
        StringBuilder sqlImport = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
        	sqlImport.append(line); 
        	String endline = line.substring(line.length() - 1, line.length() - 0);
        	if (endline.equals(";")) {
        		db.execSQL(sqlImport.toString());
        		sqlImport = new StringBuilder();
        	}
        }
        reader.close();
    }
    
    private void importDataBase2(SQLiteDatabase db) throws IOException{
    	InputStream myInput = myContext.getAssets().open("SQL/CaseSimulatorUserSQL.sql");
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(myInput));
        StringBuilder sqlImport = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
        	sqlImport.append(line); 
        	String endline = line.substring(line.length() - 1, line.length() - 0);
        	if (endline.equals(";")) {
        		db.execSQL(sqlImport.toString());
        		sqlImport = new StringBuilder();
        	}
        }
        reader.close();
    }
    
    private void dropDatabaseTables(SQLiteDatabase db) throws IOException{

    	String query = "DROP TABLE 'Cases';";
    	db.execSQL(query);
    	query = "DROP TABLE 'Guns';";
    	db.execSQL(query);
        query = "DROP TABLE 'Knives';";
        db.execSQL(query);

        query = "SELECT name FROM sqlite_master WHERE type='table' AND name='EventStickers';";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount()>0){
            query = "DROP TABLE EventStickers;";
            db.execSQL(query);
        }

        cursor.close();
	}

    private void checkStatsRow(SQLiteDatabase db) throws IOException{

        String query = "SELECT Count(*) FROM 'Cases';";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int caseCount = cursor.getInt(0);

        for (int i = 1; i < (caseCount + 1); i++) {
            System.out.println(i);
            query = "SELECT * FROM Stats WHERE caseID='" + i + "'";
            cursor = db.rawQuery(query, null);

            if(cursor.getCount()<=0){
                query = "INSERT INTO 'Stats' VALUES(" + i + ",0,0,0,0,0,0,0,0);";
                db.execSQL(query);
            }
        }
        cursor.close();
    }

    private void checkEventStickventory(SQLiteDatabase db) throws IOException{

        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='EventStickventory';";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount()<=0){
            query = "CREATE TABLE `EventStickventory` (`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `evStickID` INTEGER NOT NULL);";
            db.execSQL(query);
        }
        cursor.close();
    }
        
    public void initilize() {
        this.getReadableDatabase();
    }
    
    public void openDataBase() throws SQLException {
    	SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
    }
 
    @Override
	public synchronized void close() {
        SQLiteDatabase db = this.getReadableDatabase();
    	if(db != null)
    	{
    		db.close();
    	}
    	super.close();
	}
 
	public List<List> listAllCases() {
		List<List> resultList = new ArrayList<List>();
	   	
        String query = "SELECT * FROM Cases";  
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
            	List<String> itemList = new ArrayList<String>();
            	for (int i = 0; i<4; i++) {
    		    	itemList.add(cursor.getString(i));
    	    	}
    	    	resultList.add(itemList);
            } while (cursor.moveToNext());
        }
        cursor.close();
	    return resultList;
	}
	
	public List<List> listAllGunsFromCase(int caseID) {
	   	List<List> resultList = new ArrayList<List>();
	   	
        String query = "SELECT * FROM Guns WHERE caseID='" + caseID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
		
        if (cursor.moveToFirst()) {
            do {
            	List<String> itemList = new ArrayList<String>();
    	    	for (int i = 0; i<6; i++) {
    		    	itemList.add(cursor.getString(i));
    	    	}
    	    	resultList.add(itemList);
            } while (cursor.moveToNext());
        }
        cursor.close();
	    return resultList;
	}

    public List<String> listGunWithTexture(String gunTexture) {
        List<String> resultList = new ArrayList<String>();

        String query = "SELECT * FROM Guns WHERE gunTexture = '" + gunTexture + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i<5; i++) {
                resultList.add(cursor.getString(i));
            }
        }
        cursor.close();

        return resultList;
    }

    public List<String> listGunWithID(int gunID) {
        List<String> resultList = new ArrayList<String>();

        String query = "SELECT * FROM Guns WHERE _id = '" + gunID + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i<5; i++) {
                resultList.add(cursor.getString(i));
            }
        }
        cursor.close();

        return resultList;
    }

    public List<List> listAllKnives() {
        List<List> resultList = new ArrayList<List>();

        String query = "SELECT * FROM Knives";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                List<String> itemList = new ArrayList<String>();
                for (int i = 0; i<5; i++) {
                    itemList.add(cursor.getString(i));
                }
                resultList.add(itemList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resultList;
    }

    public List<String> listKnifeWithTexture(String knifeTexture) {
        List<String> resultList = new ArrayList<String>();

        String query = "SELECT * FROM Knives WHERE knifeTexture = '" + knifeTexture + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i<5; i++) {
                resultList.add(cursor.getString(i));
            }
        }
        cursor.close();

        return resultList;
    }

    public List<String> listKnifeWithID(int knifeID) {
        List<String> resultList = new ArrayList<String>();

        String query = "SELECT * FROM Knives WHERE _id = '" + knifeID + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i<5; i++) {
                resultList.add(cursor.getString(i));
            }
        }
        cursor.close();

        return resultList;
    }

    public List<List> listAllEventStickers() {
        List<List> resultList = new ArrayList<List>();

        String query = "SELECT * FROM EventStickers";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                List<String> itemList = new ArrayList<String>();
                for (int i = 0; i<8; i++) {
                    itemList.add(cursor.getString(i));
                }
                resultList.add(itemList);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return resultList;
    }

    public void addGun(int gunID, String rarity, int caseID, String wear, int stattrak) {
        String query = "INSERT INTO `Inventory` VALUES(NULL,'" + gunID + "','" +rarity + "','" + caseID + "','" + wear + "','" + stattrak + "','0','0','0','0');";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
    }

    public void addEventSticker(int evStickID) {
        String query = "INSERT INTO `EventStickventory` VALUES(NULL,'" + evStickID + "');";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
    }

    public void removeGun(int gunID) {
        String query = "DELETE FROM `Inventory` WHERE _id = '" + gunID + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
    }

    public void removeEventSticker(int evStickID) {
        String query = "DELETE FROM `EventStickventory` WHERE _id = '" + evStickID + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
    }

    public List<List> listAllInventory() {
        List<List> resultList = new ArrayList<List>();

        String query = "SELECT * FROM Inventory";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                List<String> itemList = new ArrayList<String>();
                for (int i = 0; i<6; i++) {
                    itemList.add(cursor.getString(i));
                }
                resultList.add(itemList);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return resultList;
    }

    public List<List> listAllInventoryOld() {
        List<List> resultList = new ArrayList<List>();

        String query = "SELECT * FROM Inventory";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                List<String> itemList = new ArrayList<String>();
                for (int i = 0; i<8; i++) {
                    itemList.add(cursor.getString(i));
                }
                resultList.add(itemList);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return resultList;
    }

    public List<List> listAllEventStickventory() {
        List<List> resultList = new ArrayList<List>();

        String query = "SELECT * FROM EventStickventory";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                List<String> itemList = new ArrayList<String>();
                for (int i = 0; i<2; i++) {
                    itemList.add(cursor.getString(i));
                }
                resultList.add(itemList);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return resultList;
    }
	 
	public List<List> filterInventory(String query) {
	  List<List> resultList = new ArrayList<List>();
	   	
	  SQLiteDatabase db = this.getReadableDatabase();
	  Cursor cursor = db.rawQuery(query, null);
	  
	  if (cursor.moveToFirst()) {
	      do {
	      	List<String> itemList = new ArrayList<String>();
	      	for (int i = 0; i<6; i++) {
		    	itemList.add(cursor.getString(i));
	    	}
	    	resultList.add(itemList);
	      } while (cursor.moveToNext());
	  }
       cursor.close();

	   return resultList;
	}
	 
	public List<List> filterGuns(String query) {
	  List<List> resultList = new ArrayList<List>();
	   	
	  SQLiteDatabase db = this.getReadableDatabase();
	  Cursor cursor = db.rawQuery(query, null);
	  
	  if (cursor.moveToFirst()) {
	      do {
	      	List<String> itemList = new ArrayList<String>();
	      	for (int i = 0; i<5; i++) {
		    	itemList.add(cursor.getString(i));
	    	}
	    	resultList.add(itemList);
	      } while (cursor.moveToNext());
	  }
      cursor.close();

	  return resultList;
	}
	 
	public List<List> listAllStats() {
	  List<List> resultList = new ArrayList<List>();
	   	
	  String query = "SELECT * FROM Stats";  
	  SQLiteDatabase db = this.getReadableDatabase();
	  Cursor cursor = db.rawQuery(query, null);
	  
	  if (cursor.moveToFirst()) {
	      do {
	      	List<String> itemList = new ArrayList<String>();
	      	for (int i = 0; i<8; i++) {
		    	itemList.add(cursor.getString(i));
	    	}
	    	resultList.add(itemList);
	      } while (cursor.moveToNext());
	  }
      cursor.close();

	  return resultList;
	}
	
	public void addStat(int caseID, String type, int value) {
		String query = "UPDATE Stats SET " + type + "=" + type + "+" + value + " WHERE caseID='" + caseID + "';";
		SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
	}

    public void dropAndCreateNewInventory() {
        String query = "DROP TABLE 'Inventory';";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);

        query = "CREATE TABLE Inventory (`_id` INTEGER PRIMARY KEY AUTOINCREMENT, `gunID` INTEGER NOT NULL, `rarity` INTEGER NOT NULL, `caseID` INTEGER NOT NULL, `wear` TEXT NOT NULL, `stattrak` INTEGER NOT NULL, `stickerID1` INTEGER, `stickerID2` INTEGER, `stickerID3` INTEGER, `stickerID4` INTEGER);";
        db.execSQL(query);
    }

    public boolean checkOldInventory() {
        String query = "SELECT sql FROM sqlite_master WHERE type='table' AND name = 'Inventory';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        String s1 = cursor.getString(0);
        String s2 = "gunTexture";

        cursor.close();

        return s1.toLowerCase().contains(s2.toLowerCase());
    }
}