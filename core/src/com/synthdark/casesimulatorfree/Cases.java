package com.synthdark.casesimulatorfree;

import java.util.ArrayList;
import java.util.List;

import com.synthdark.casesimulatorfree.Guns;

public class Cases {
	int caseID;
	String caseName;
	String caseTexture;
	String caseType;
	List<Guns> gunList;
	List<Guns> gun1List;
	List<Guns> gun2List;
	List<Guns> gun3List;
	List<Guns> gun4List;
	List<Guns> gun5List;
	
	public void GenerateRarityLists() {
		
		gun1List = new ArrayList<Guns>();
		gun2List = new ArrayList<Guns>();
		gun3List = new ArrayList<Guns>();
		gun4List = new ArrayList<Guns>();
		gun5List = new ArrayList<Guns>();
		
		for (int i = 0; i < gunList.size(); i++) {
			if (gunList.get(i).rarity.equals("1")) {
				gun1List.add(gunList.get(i));
			}
			if (gunList.get(i).rarity.equals("2")) {
				gun2List.add(gunList.get(i));
			}
			if (gunList.get(i).rarity.equals("3")) {
				gun3List.add(gunList.get(i));
			}
			if (gunList.get(i).rarity.equals("4")) {
				gun4List.add(gunList.get(i));
			}
			if (gunList.get(i).rarity.equals("5")) {
				gun5List.add(gunList.get(i));
			}
		}
	}
	
	public void dispose() {
		gunList.clear();
		gun1List.clear();
		gun2List.clear();
		gun3List.clear();
		gun4List.clear();
		gun5List.clear();
	}
}
