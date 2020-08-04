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

import java.lang.reflect.Method;

/**
 * Eegue la clonazione di un oggetto tramite reflection java
 * 
 * @author sourcesense
 *
 */
public class ReflectionUtils {

	/**
	 * Clonazione di un oggetto tramite reflection
	 * 
	 * @param target oggetto da clonare
	 * @return oggetto clonato
	 */
	public static Object cloneObject(Object target) {

		Object ritorno = null;
		Method metodo = null;

		try {
			metodo = target.getClass().getMethod("clone", null);

		} catch (NoSuchMethodException exc) {

		}

		if (metodo != null)
			try {
				ritorno = metodo.invoke(target, new Object[0]);
			} catch (Exception ecc) {

			}

		return ritorno;
	}
}
