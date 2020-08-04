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
package com.sourcesense.crl.web.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.TipoAtto;
import com.sourcesense.crl.business.service.AttoServiceManager;
import com.sourcesense.crl.web.ui.beans.AttoBean;

/**
 * Controller per amministrazione atto
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "amministrazioneAttoController")
@RequestScoped
public class AmministrazioneAttoController {

	@ManagedProperty(value = "#{attoServiceManager}")
	private AttoServiceManager attoServiceManager;

	private AttoBean attoBean;

	private String numeroAtto;

	private String tipoAtto;

	private String tipologia;

	private String legislatura;

	private String anno;

	private String estensioneAtto;

	private boolean tipologiaVisible;

	private List<TipoAtto> tipiAtto = new ArrayList<TipoAtto>();
	private Map<String, String> tipologie = new HashMap<String, String>() {

	};

	private List<String> legislature = new ArrayList<String>();

	private Map<String, String> anni = new HashMap<String, String>();

	/**
	 * Aggiunge l'atto selezionato al contesto web
	 */
	@PostConstruct
	private void initializeValues() {

		FacesContext context = FacesContext.getCurrentInstance();
		attoBean = (AttoBean) context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{attoBean}", AttoBean.class)
				.getValue(context.getELContext());

	}

	/**
	 * Aggiornamento dell'atto
	 */
	public void updateAtto() {
		Atto atto = attoBean.getAtto();
		Atto attoRet = attoServiceManager.updateAtto(atto);
		FacesContext context = FacesContext.getCurrentInstance();

		if (attoRet != null && attoRet.getError() == null) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atto aggiornato con successo", ""));

		} else if (attoRet != null && attoRet.getError() != null && !attoRet.getError().equals("")) {

			/*
			 * context.addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,
			 * attoRet.getError(), ""));
			 */
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"ATTENZIONE: non è stato possibile aggiornare l'atto", ""));

		}

	}

	/**
	 * Rimozione dell'atto
	 * 
	 * @return il valore "pretty:Home"
	 */
	public String deleteAtto() {
		Atto atto = attoBean.getAtto();
		attoServiceManager.deleteAtto(atto);

		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atto eliminato correttamente", ""));
		context.getExternalContext().getFlash().setKeepMessages(true);
		return "pretty:Home";
	}

	public AttoServiceManager getAttoServiceManager() {
		return attoServiceManager;
	}

	public void setAttoServiceManager(AttoServiceManager attoServiceManager) {
		this.attoServiceManager = attoServiceManager;
	}

}
