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
import javax.faces.bean.ViewScoped;

import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.service.AttoServiceManager;

/**
 * Manager rappresentato dalle informazioni del codice, dell'atto e della spunta
 * per visualizzare i i dettagli delle commissioni
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "mgmtBean")
@ViewScoped
public class ManagerBean implements Serializable {

	private boolean showCommDetail;

	private String code;

	private Atto atto;

	private AttoServiceManager as = new AttoServiceManager();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Atto getAtto() {
		return atto;
	}

	public void setAtto(Atto atto) {
		this.atto = atto;
	}

	public boolean isShowCommDetail() {
		return showCommDetail;
	}

	public void setShowCommDetail(boolean showCommDetail) {
		this.showCommDetail = showCommDetail;
	}

	/**
	 * Verifica se i dettagli delle commissioni sono visualizzabili
	 */
	public void visualizeCommDetail() {
		this.showCommDetail = true;
	}

}
