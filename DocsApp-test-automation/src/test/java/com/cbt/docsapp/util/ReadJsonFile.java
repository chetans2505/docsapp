package com.cbt.docsapp.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.cbt.docsapp.generics.GenericLib;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class ReadJsonFile {

	public static String readJsonKeyValue(String filePath, String Module, String elementName, String elementLocator)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		String locator = "";
		ArrayList<String> mListID;
		JsonParser parser = new JsonParser();
		Object obj = parser.parse(new FileReader(filePath));
		JsonObject jsonObject = (JsonObject) obj;
		JsonObject elementsModule = jsonObject.getAsJsonObject(Module);
		if (!elementName.equalsIgnoreCase("none")||elementName.equalsIgnoreCase("alert_accept")) {

			JsonArray mArrayId = elementsModule.getAsJsonArray(elementName);
			boolean flag = false;

			for (int i = 0; i < mArrayId.size(); i++) {

				JsonObject keyValue = (JsonObject) mArrayId.get(i);
				if (keyValue.get(elementLocator) != null) {
					flag = true;
					locator = keyValue.get(elementLocator).toString();
					// System.out.println("------"+elementLocator+"----" + locator.toString());
					break;
				}
			}

			if (flag == false) {
				System.out.println("----------" + elementLocator + "---------");
			}

		} else {
			locator = null;
		}

		return locator;
	}

}
