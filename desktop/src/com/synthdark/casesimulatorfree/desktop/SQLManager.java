package com.synthdark.casesimulatorfree.desktop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SQLManager  {
	
    private static String DB_NAME = "CaseSimulatorDatabase";
    
	File dbFile = new File (DB_NAME);
	Connection c = null;

    Locale myLocale;

    public SQLManager (Locale locale) {
        myLocale = locale;
    }
	
    public void initilize() {
		try {
			if (dbFile.exists()) {
				openDataBase();
			} else {
				openDataBase();
				importDataBase();
				importDataBase2();
			}
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
    }
    
    private void importDataBase() throws SQLException, IOException {
	    Statement stmt = c.createStatement();
	    FileInputStream myInput = new FileInputStream("SQL/" + myLocale.getISO3Language() + "/CaseSimulatorMainSQL.sql");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(myInput));
        StringBuilder sqlImport = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
        	sqlImport.append(line);
        	String endline = line.substring(line.length() - 1, line.length() - 0);
        	if (endline.equals(";")) {
            	stmt.executeUpdate(sqlImport.toString());
        		sqlImport = new StringBuilder();
        	}
        }
        reader.close();
	    stmt.close();
    }
    
    private void importDataBase2() throws SQLException, IOException {
	    Statement stmt = c.createStatement();
	    FileInputStream myInput = new FileInputStream("SQL/CaseSimulatorUserSQL.sql");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(myInput));
        StringBuilder sqlImport = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
        	sqlImport.append(line);
        	String endline = line.substring(line.length() - 1, line.length() - 0);
        	if (endline.equals(";")) {
            	stmt.executeUpdate(sqlImport.toString());
        		sqlImport = new StringBuilder();
        	}
        }
        reader.close();
	    stmt.close();
    }
    
    public void refreshDatabase() throws SQLException, IOException {
    	dropDatabaseTables();
    	importDataBase();
    	checkStatsRow();
        checkEventStickventory();
    }
    
    private void dropDatabaseTables() throws SQLException{    	        
		Statement stmt = c.createStatement();
    	String query = "DROP TABLE Cases; DROP TABLE Guns; DROP TABLE Knives;";
		stmt.executeUpdate(query);

        query = "SELECT name FROM sqlite_master WHERE type='table' AND name='EventStickers';";
        ResultSet rs = stmt.executeQuery(query);

        if(rs.next()){
            query = "DROP TABLE EventStickers;";
            stmt.executeUpdate(query);
        }

    }
    
    private void checkStatsRow() throws IOException, SQLException{
        Statement stmt = c.createStatement();
        String query = "SELECT Count(*) FROM Cases;";
        ResultSet rs = stmt.executeQuery(query);
        int caseCount = rs.getInt(1);

    	for (int i = 1; i < (caseCount + 1); i++) {
    		stmt = c.createStatement();
	    	query = "SELECT * FROM Stats WHERE caseID='" + i + "';";
	    	rs = stmt.executeQuery(query);
	        
	    	if(!rs.next()){ 	
	        	query = "INSERT INTO 'Stats' VALUES(" + i + ",0,0,0,0,0,0,0,0);";
	        	stmt.executeUpdate(query);
	        }
    	}
	}

    private void checkEventStickventory() throws IOException, SQLException{
        Statement stmt = c.createStatement();
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='EventStickventory';";
        ResultSet rs = stmt.executeQuery(query);

        if(!rs.next()){
            query = "CREATE TABLE `EventStickventory` (`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `evStickID` INTEGER NOT NULL);";
            stmt.executeUpdate(query);
        }
    }
    
    public void openDataBase() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
    }
 
	public void close() throws SQLException {
	    c.close();
	}
    
	public List<List> listAllCases() throws SQLException {		
		List<List> resultList = new ArrayList<List>();
		
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM Cases" );
	    while ( rs.next() ) {
	    	List<String> itemList = new ArrayList<String>();
	    	for (int i = 1; i<5; i++) {
		    	itemList.add(rs.getString(i));
	    	}
	    	resultList.add(itemList);
	    }
	    
	    return resultList;
	}
	
	public List<List> listAllGunsFromCase(int caseID) throws SQLException {	   	
	   	List<List> resultList = new ArrayList<List>();
	   	
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM Guns WHERE caseID='" + caseID + "';" );
	    while ( rs.next() ) {
		   	List<String> itemList = new ArrayList<String>();
	    	for (int i = 1; i<7; i++) {
		    	itemList.add(rs.getString(i));
	    	}
	    	resultList.add(itemList);
	    }
	    return resultList;
	}

    public List<String> listGunWithTexture(String gunTexture) throws SQLException {
        List<String> resultList = new ArrayList<String>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Guns WHERE gunTexture = '" + gunTexture + "';" );
        while ( rs.next() ) {
            for (int i = 1; i<6; i++) {
                resultList.add(rs.getString(i));
            }
        }

        return resultList;
    }

    public List<String> listGunWithID(int gunID) throws SQLException {
        List<String> resultList = new ArrayList<String>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Guns WHERE _id = '" + gunID + "';" );
        while ( rs.next() ) {
            for (int i = 1; i<6; i++) {
                resultList.add(rs.getString(i));
            }
        }

        return resultList;
    }

    public List<List> listAllKnives() throws SQLException {
        List<List> resultList = new ArrayList<List>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Knives" );
        while ( rs.next() ) {
            List<String> itemList = new ArrayList<String>();
            for (int i = 1; i<6; i++) {
                itemList.add(rs.getString(i));
            }
            resultList.add(itemList);
        }

        return resultList;
    }

    public List<String> listKnifeWithTexture(String knifeTexture) throws SQLException {
        List<String> resultList = new ArrayList<String>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Knives WHERE knifeTexture = '" + knifeTexture + "';" );
        while ( rs.next() ) {
            for (int i = 1; i<6; i++) {
                resultList.add(rs.getString(i));
            }
        }

        return resultList;
    }


    public List<String> listKnifeWithID(int knifeID) throws SQLException {
        List<String> resultList = new ArrayList<String>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Knives WHERE _id = '" + knifeID + "';" );
        while ( rs.next() ) {
            for (int i = 1; i<6; i++) {
                resultList.add(rs.getString(i));
            }
        }

        return resultList;
    }

    public List<List> listAllEventStickers() throws SQLException {
        List<List> resultList = new ArrayList<List>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM EventStickers" );
        while ( rs.next() ) {
            List<String> itemList = new ArrayList<String>();
            for (int i = 1; i<9; i++) {
                itemList.add(rs.getString(i));
            }
            resultList.add(itemList);
        }

        return resultList;
    }

    public void addGun(int gunID, String rarity, int caseID, String wear, int stattrak) throws SQLException {
        Statement stmt = c.createStatement();
        String query = "INSERT INTO `Inventory` VALUES(NULL,'" + gunID + "','" +rarity + "','" + caseID + "','" + wear + "','" + stattrak + "','0','0','0','0');";
        stmt.executeUpdate(query);
    }

    public void addEventSticker(int evStickID) throws SQLException {
        Statement stmt = c.createStatement();
        String query = "INSERT INTO `EventStickventory` VALUES(NULL,'" + evStickID + "');";
        stmt.executeUpdate(query);
    }

    public void removeGun(int gunID) throws SQLException {
        Statement stmt = c.createStatement();
        String query = "DELETE FROM `Inventory` WHERE _id = '" + gunID + "';";
        stmt.executeUpdate(query);
    }

    public void removeEventSticker(int evStickID) throws SQLException {
        Statement stmt = c.createStatement();
        String query = "DELETE FROM `EventStickventory` WHERE _id = '" + evStickID + "';";
        stmt.executeUpdate(query);
    }

    public List<List> listAllInventory() throws SQLException {
        List<List> resultList = new ArrayList<List>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Inventory" );
        while ( rs.next() ) {
            List<String> itemList = new ArrayList<String>();
            for (int i = 1; i<7; i++) {
                itemList.add(rs.getString(i));
            }
            resultList.add(itemList);
        }

        return resultList;
    }

    public List<List> listAllInventoryOld() throws SQLException {
        List<List> resultList = new ArrayList<List>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Inventory" );
        while ( rs.next() ) {
            List<String> itemList = new ArrayList<String>();
            for (int i = 1; i<9; i++) {
                itemList.add(rs.getString(i));
            }
            resultList.add(itemList);
        }

        return resultList;
    }

    public List<List> listAllEventStickventory() throws SQLException {
        List<List> resultList = new ArrayList<List>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM EventStickventory" );
        while ( rs.next() ) {
            List<String> itemList = new ArrayList<String>();
            for (int i = 1; i<3; i++) {
                itemList.add(rs.getString(i));
            }
            resultList.add(itemList);
        }

        return resultList;
    }
    
	public List<List> filterInventory(String query) throws SQLException {	
		List<List> resultList = new ArrayList<List>();
		
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( query );
	    while ( rs.next() ) {
	    	List<String> itemList = new ArrayList<String>();
	    	for (int i = 1; i<7; i++) {
		    	itemList.add(rs.getString(i));
	    	}
	    	resultList.add(itemList);
	    }
	    
	    return resultList;
	}
    
	public List<List> filterGuns(String query) throws SQLException {	
		List<List> resultList = new ArrayList<List>();
		
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( query );
	    while ( rs.next() ) {
	    	List<String> itemList = new ArrayList<String>();
	    	for (int i = 1; i<6; i++) {
		    	itemList.add(rs.getString(i));
	    	}
	    	resultList.add(itemList);
	    }
	    
	    return resultList;
	}
    
	public List<List> listAllStats() throws SQLException {	
		List<List> resultList = new ArrayList<List>();
		
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM Stats" );
	    while ( rs.next() ) {
	    	List<String> itemList = new ArrayList<String>();
	    	for (int i = 1; i<9; i++) {
		    	itemList.add(rs.getString(i));
	    	}
	    	resultList.add(itemList);
	    }
	    
	    return resultList;
	}
	
	public void addStat(int caseID, String type, int value) throws SQLException {
		Statement stmt = c.createStatement();
		String query = "UPDATE Stats SET " + type + "=" + type + "+" + value + " WHERE caseID='" + caseID + "';";
		stmt.executeUpdate(query);
	}

    public void dropAndCreateNewInventory() throws SQLException {
        close();
        c = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);

        Statement stmt = c.createStatement();
        String query = "DROP TABLE Inventory;";
        stmt.executeUpdate(query);

        query = "CREATE TABLE Inventory (`_id` INTEGER PRIMARY KEY AUTOINCREMENT, `gunID` INTEGER NOT NULL, `rarity` INTEGER NOT NULL, `caseID` INTEGER NOT NULL, `wear` TEXT NOT NULL, `stattrak` INTEGER NOT NULL, `stickerID1` INTEGER, `stickerID2` INTEGER, `stickerID3` INTEGER, `stickerID4` INTEGER);";
        stmt.executeUpdate(query);
    }

    public boolean checkOldInventory() throws SQLException {
        Statement stmt = c.createStatement();
        String query = "SELECT sql FROM sqlite_master WHERE type='table' AND name = 'Inventory';";
        ResultSet rs = stmt.executeQuery(query);

        String s1 = rs.getString(1);
        String s2 = "gunTexture";
        return s1.toLowerCase().contains(s2.toLowerCase());
    }
}


