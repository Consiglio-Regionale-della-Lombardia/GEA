/*
 *  * #%L
 *  * Alfresco Repository
 *  * %%
 *  * Copyright (C) 2005 - 2016 Alfresco Software Limited
 *  * %%
 *  * This file is part of the Alfresco software. 
 *  * If the software was purchased under a paid Alfresco license, the terms of 
 *  * the paid license agreement will prevail.  Otherwise, the software is 
 *  * provided under the following open source license terms:
 *  * 
 *  * Alfresco is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  * 
 *  * Alfresco is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  * 
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 *  * #L%
 */
package com.sourcesense.crl.webscript.report.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utilità per gestire il json
 */
public class JsonUtils {
	/**
	 * Recupera il valore di una proprietà del json
	 * @param root oggetto root del json
	 * @param elementName nome della proprietà da recuperare
	 * @return il valore della proprietà passata in input
	 * @throws JSONException in caso di errore nella lettura del json
	 */
	public static String retieveElementFromJson(JSONObject root, String elementName)
			throws JSONException {
		String elementValue = root.getString(elementName);
		return elementValue;
	}


	/**
	 * Recupera il valore di una proprietà del json di tipo lista
	 * @param root oggetto root del json
	 * @param arrayName nome della proprietà da recuperare
	 * @return la lista di  valori della proprietà passata in input
	 * @throws JSONException in caso di errori in lettura del json
	 */
	public static List<String> retieveArrayListFromJson(JSONObject root,
			String arrayName) throws JSONException {
		JSONArray jsonArray = root.getJSONArray(arrayName);
		List<String> tipiAttoList = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			String jsonElement = jsonArray.getString(i).trim();
			if (!jsonElement.equals("")&&jsonElement.contains(":\""))
				tipiAttoList.add(extractJsonSingleValue(jsonElement));
			else
				tipiAttoList.add(jsonElement);
		}
		return tipiAttoList;
	}

	/**
	 * Returns the value for this single json element in this form :
	 * "tipoAtto":"PDL"
	 * 
	 * @param jsonElement elemento json da trasformare
	 * @return ritorna il valore nel seguente formato "tipoAtto":"PDL"
	 */
	public static String extractJsonSingleValue(String jsonElement) {
		int indexFieldValueSeparator = jsonElement.indexOf(":");
		int indexValueEnd = jsonElement.lastIndexOf("\"");
		return jsonElement.substring(indexFieldValueSeparator + 2,
				indexValueEnd);
	}
}
