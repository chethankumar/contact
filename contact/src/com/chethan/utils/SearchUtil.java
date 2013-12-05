package com.chethan.utils;

import java.util.ArrayList;

import com.chethan.objects.SimpleContact;

public class SearchUtil {

	public static ArrayList<SimpleContact> search(ArrayList<SimpleContact> contactList, String string) {
		ArrayList<SimpleContact> containsList = new ArrayList<SimpleContact>();
		ArrayList<SimpleContact> remainingList = new ArrayList<SimpleContact>();
		for (SimpleContact simpleContact : contactList) {
			for (String str : getAllPossibleStrings(string)) {
				if (simpleContact.getName().contains(str)) {
					containsList.add(simpleContact);
				} else {
					remainingList.add(simpleContact);
				}
			}
			containsList.addAll(remainingList);
		}
		return containsList;
	}

	public static ArrayList<SimpleContact> SortByPriority(ArrayList<SimpleContact> searchList,
			ArrayList<SimpleContact> callHistorySimpleContactList) {
		ArrayList<SimpleContact> containsList = new ArrayList<SimpleContact>();
		ArrayList<SimpleContact> remainingList = new ArrayList<SimpleContact>();
		for (SimpleContact simpleContact : searchList) {
			if (callHistorySimpleContactList.contains(simpleContact)) {
				containsList.add(simpleContact);
			} else {
				remainingList.add(simpleContact);
			}
		}
		containsList.addAll(remainingList);
		return containsList;
	}

	public static ArrayList<String> getAllPossibleStrings(String sourceString) {
		ArrayList<String> stringList = new ArrayList<String>();
		stringList.addAll(getCharForNumber(Integer.parseInt("" + sourceString.charAt(0))));
		if (sourceString.length() > 1) {
			for (int i = 1; i < sourceString.length(); i++) {
				for (String string : stringList) {
					string += getCharForNumber(Integer.parseInt("" + sourceString.charAt(i)));
				}
			}
		}

		return stringList;
	}

	public static ArrayList<String> getCharForNumber(Integer integer) {
		ArrayList<String> charList = new ArrayList<String>();
		switch (integer) {
		case 2:
			charList.clear();
			charList.add("a");
			charList.add("b");
			charList.add("c");
			break;
		case 3:
			charList.clear();
			charList.add("d");
			charList.add("e");
			charList.add("f");
			break;
		case 4:
			charList.clear();
			charList.add("g");
			charList.add("h");
			charList.add("i");
			break;
		case 5:
			charList.clear();
			charList.add("j");
			charList.add("k");
			charList.add("l");
			break;
		case 6:
			charList.clear();
			charList.add("m");
			charList.add("n");
			charList.add("o");
			break;
		case 7:
			charList.clear();
			charList.add("p");
			charList.add("q");
			charList.add("r");
			charList.add("s");
			break;
		case 8:
			charList.clear();
			charList.add("t");
			charList.add("u");
			charList.add("v");
			break;
		case 9:
			charList.clear();
			charList.add("w");
			charList.add("x");
			charList.add("y");
			charList.add("z");
			break;
		default:
			return charList;
		}
		return charList;
	}
}
