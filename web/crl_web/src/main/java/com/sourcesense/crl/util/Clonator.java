/* 
 * Copyright (C) 2019 Consiglio Regionale della Lombardia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sourcesense.crl.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utlità per clonare le liste java
 * 
 * @author sourcesense
 *
 */
public class Clonator {

	/**
	 * Clonazione di una lista
	 * 
	 * @param list lista da clonare
	 * @return lista clonata
	 */
	public static List cloneList(List list) {
		List newList = new ArrayList();
		Iterator it = list.iterator();

		while (it.hasNext()) {
			newList.add(ReflectionUtils.cloneObject(it.next()));
		}

		return newList;
	}

}
