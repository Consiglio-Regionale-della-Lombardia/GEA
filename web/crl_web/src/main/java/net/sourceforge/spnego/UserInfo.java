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
package net.sourceforge.spnego;

import java.util.List;

/**
 * User information from the user-store.
 * 
 * @author Darwin V. Felix
 *
 */
public interface UserInfo {

	/**
	 * Returns a list of info associated with the label.
	 * 
	 * @param label e.g. name, proxyAddresses, whenCreated
	 * @return a list of info associated with the label
	 */
	List<String> getInfo(final String label);

	/**
	 * Return a list of labels.
	 * 
	 * @return a list of labels
	 */
	List<String> getLabels();

	/**
	 * Returns true if there is info with the passed-in label.
	 * 
	 * @param label e.g. mail, memberOf, displayName
	 * @return true true if there is info with the passed-in label
	 */
	boolean hasInfo(final String label);
}
