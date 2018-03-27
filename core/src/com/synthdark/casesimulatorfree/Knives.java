package com.synthdark.casesimulatorfree;

import java.util.List;

import com.synthdark.casesimulatorfree.Guns;

public class Knives {
	String knifeType;
	List<Guns> knifeList;

	public void dispose() {
		knifeList.clear();
	}
}
