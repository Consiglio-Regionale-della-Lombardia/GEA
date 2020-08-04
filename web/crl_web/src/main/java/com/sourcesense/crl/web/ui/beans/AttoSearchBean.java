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
package com.sourcesense.crl.web.ui.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.sourcesense.crl.business.model.AttoSearch;

/**
 * Ricerca dell'atto nelle pagine web
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "attoSearchBean")
@SessionScoped
public class AttoSearchBean extends AttoSearch implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean firstSaerch = true;

	public AttoSearchBean() {
		super();
		setTipoWorkingList("inlavorazione");
	}

	public boolean isFirstSaerch() {
		return firstSaerch;
	}

	public void setFirstSaerch(boolean firstSaerch) {
		this.firstSaerch = firstSaerch;
	}
}
