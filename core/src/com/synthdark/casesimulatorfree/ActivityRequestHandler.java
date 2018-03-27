package com.synthdark.casesimulatorfree;

import java.util.List;

public interface ActivityRequestHandler {
	   public void initializeDatabase();
	   public void openDatabase();
	   public void closeDatabase();
	   public List<List> listAllCases();
       public List<List> listAllGunsFromCase(int caseID);
       public List<String> listGunWithTexture(String gunTexture);
       public List<String> listGunWithID(int gunID);
	   public List<List> listAllKnives();
       public List<String> listKnifeWithTexture(String knifeTexture);
       public List<String> listKnifeWithID(int knifeID);
       public List<List> listAllEventStickers();
       public void addGun(int gunID, String rarity, int caseID, String wear, int stattrak);
       public void addEventSticker(int hatID);
	   public void removeGun(int gunID);
       public void removeEventSticker(int hatID);
       public List<List> listAllInventory();
       public List<List> listAllInventoryOld();
       public List<List> listAllEventStickventory();
	   public List<List> filterInventory(String query);
	   public List<List> filterGuns(String query);
	   public List<List> listAllStats();
	   public void addStat(int caseID, String type, int value);
	   public void refreshDatabase();
       public boolean checkOldInventory();
       public void dropAndCreateNewInventory();

	   
	   public void showInterstital();
	   public void sendTrackerScreenName(String path);
	   public void sendTrackerEvent(String category, String action, String label, int value);
}
