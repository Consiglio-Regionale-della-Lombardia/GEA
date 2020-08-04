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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilità per la clonazione di liste java comprese di generics
 * 
 * @author sourcesense
 *
 */
public class CloningLists {

	/**
	 * Clonazione di una lista con i generics
	 * 
	 * @param <T> parametro generico della lista
	 * @param list lista da clonare
	 * @return lista clonata
	 */
	public static <T extends Cloneable> List<T> cloneMe(List<T> list) {
		final List<T> list2;
		if (list instanceof Cloneable) {
			list2 = forceClone(list);
		} else {
			list2 = new ArrayList<T>();
		}

		list2.clear();
		for (T t : list) {
			list2.add(forceClone(t));
		}
		return list2;
	}

	/**
	 * Clonazione di un oggetto
	 * 
	 * @param <T> tipo di oggetto
	 * @param o oggetto da clonare
	 * @return oggetto clonato
	 */
	private static <T> T forceClone(T o) {
		final Class<?> klass = o.getClass();

		final Method cloneMethod;
		try {
			cloneMethod = klass.getMethod("clone");
		} catch (NoSuchMethodException e) {
			throw new AssertionError(e);
		}

		cloneMethod.setAccessible(true);
		try {
			@SuppressWarnings("unchecked")
			final T result = (T) cloneMethod.invoke(o);
			return result;
		} catch (ClassCastException e) {
			throw new AssertionError(e);
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
