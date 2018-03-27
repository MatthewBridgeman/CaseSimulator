package com.synthdark.casesimulatorfree.desktop;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.synthdark.casesimulatorfree.CaseSimulatorFree;
import com.synthdark.casesimulatorfree.ActivityRequestHandler;

public class DesktopLauncher implements ActivityRequestHandler {

	static DesktopLauncher application;
	static SQLManager mySQLManager;
	
	public static void main (String[] arg) {
        Locale myLocale = Locale.getDefault();
        //String myLocaleStr = myLocale.getISO3Language();
        //myLocale = new Locale("ru","RU");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		mySQLManager = new SQLManager(myLocale);
		
		if (application == null) {
            application = new DesktopLauncher();
        }
		
		new LwjglApplication(new CaseSimulatorFree(application, myLocale), config);
		config.title = "CSGO Case Simulator Free";

        //Skewed
        //config.width = 800;
        //config.height = 720;

        //S3
		config.width = 1280;
        config.height = 720;
		
		//S5
		//config.width = 1920; 
		//config.height = 1000;
	}
	
	@Override
	public void sendTrackerScreenName(String path) {
		System.out.println("Send Stat: " + path);
	}

    @Override
    public void sendTrackerEvent(String category, String action, String label, int value) {
        System.out.println("Send Stat: " + category + ", " + action + ", " + label + ", " + value);
    }

    @Override
    public void showInterstital() {
        System.out.println("Show intersitual");
    }

    @Override
	public void initializeDatabase() {
		mySQLManager.initilize();
	}

	@Override
	public void openDatabase() {
		System.out.println("openDatabase");
	}

	@Override
	public void closeDatabase() {
		System.out.println("closeDatabase");
	}

	@Override
	public List<List> listAllCases() {
		List<List> resultList = new ArrayList<List>();
		try {
			resultList = mySQLManager.listAllCases();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@Override
	public List<List> listAllGunsFromCase(int caseID) {
		List<List> resultList = new ArrayList<List>();
		try {
			resultList = mySQLManager.listAllGunsFromCase(caseID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

    @Override
    public List<String> listGunWithTexture(String gunTexture) {
        List<String> resultList = new ArrayList<String>();
        try {
            resultList = mySQLManager.listGunWithTexture(gunTexture);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<String> listGunWithID(int gunID) {
        List<String> resultList = new ArrayList<String>();
        try {
            resultList = mySQLManager.listGunWithID(gunID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    @Override
    public List<List> listAllKnives() {
        List<List> resultList = new ArrayList<List>();
        try {
            resultList = mySQLManager.listAllKnives();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<String> listKnifeWithTexture(String knifeTexture) {
        List<String> resultList = new ArrayList<String>();
        try {
            resultList = mySQLManager.listKnifeWithTexture(knifeTexture);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<String> listKnifeWithID(int knifeID) {
        List<String> resultList = new ArrayList<String>();
        try {
            resultList = mySQLManager.listKnifeWithID(knifeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<List> listAllEventStickers() {
        List<List> resultList = new ArrayList<List>();
        try {
            resultList = mySQLManager.listAllEventStickers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public void addGun(int gunID, String rarity, int caseID, String wear, int stattrak) {
        try {
            mySQLManager.addGun(gunID, rarity, caseID, wear, stattrak);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addEventSticker(int evStickID) {
        try {
            mySQLManager.addEventSticker(evStickID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeGun(int gunID) {
        try {
            mySQLManager.removeGun(gunID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeEventSticker(int evStickID) {
        try {
            mySQLManager.removeEventSticker(evStickID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<List> listAllInventory() {
        List<List> resultList = new ArrayList<List>();
        try {
            resultList = mySQLManager.listAllInventory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<List> listAllInventoryOld() {
        List<List> resultList = new ArrayList<List>();
        try {
            resultList = mySQLManager.listAllInventoryOld();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<List> listAllEventStickventory() {
        List<List> resultList = new ArrayList<List>();
        try {
            resultList = mySQLManager.listAllEventStickventory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

	@Override
	public List<List> filterInventory(String query) {
		List<List> resultList = new ArrayList<List>();
		try {
			resultList = mySQLManager.filterInventory(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public List<List> filterGuns(String query) {
		List<List> resultList = new ArrayList<List>();
		try {
			resultList = mySQLManager.filterGuns(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public List<List> listAllStats() {
		List<List> resultList = new ArrayList<List>();
		try {
			resultList = mySQLManager.listAllStats();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public void addStat(int caseID, String type, int value) {
		try {
			mySQLManager.addStat(caseID, type, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void refreshDatabase() {
		try {
				mySQLManager.refreshDatabase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}

    public void dropAndCreateNewInventory() {
        try {
            mySQLManager.dropAndCreateNewInventory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  boolean checkOldInventory() {
        boolean checkOldInventory = false;
        try {
            checkOldInventory = mySQLManager.checkOldInventory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checkOldInventory;
    }
}
