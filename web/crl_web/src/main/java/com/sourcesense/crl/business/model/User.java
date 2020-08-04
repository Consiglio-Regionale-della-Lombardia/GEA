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
package com.sourcesense.crl.business.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * Utenza applicativa
 * 
 * @author sourcesense
 *
 */
@Configurable
@XmlRootElement
@AutoProperty
public class User {

	private String username;
	private String password;

	private List<GruppoUtente> gruppi = new ArrayList<GruppoUtente>();

	private GruppoUtente sessionGroup = new GruppoUtente();

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<GruppoUtente> getGruppi() {
		return gruppi;
	}

	public void setGruppi(List<GruppoUtente> gruppi) {
		this.gruppi = gruppi;
	}

	public GruppoUtente getSessionGroup() {
		return sessionGroup;
	}

	public void setSessionGroup(GruppoUtente sessionGroup) {
		this.sessionGroup = sessionGroup;
	}

}
