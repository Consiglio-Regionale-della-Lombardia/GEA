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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.GruppoUtente;
import com.sourcesense.crl.business.model.StatoAtto;
import com.sourcesense.crl.business.model.TipoAtto;
import com.sourcesense.crl.business.service.AttoServiceManager;
import com.sourcesense.crl.business.service.LegislaturaServiceManager;
import com.sourcesense.crl.business.service.TipoAttoServiceManager;
import com.sourcesense.crl.web.ui.beans.AttoBean;
import com.sourcesense.crl.web.ui.beans.UserBean;

/**
 * Inserimento dell'atto dalle pagine web
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "inserisciAttoController")
@ViewScoped
public class InserisciAttoController {

	@ManagedProperty(value = "#{attoServiceManager}")
	private AttoServiceManager attoServiceManager;

	@ManagedProperty(value = "#{legislaturaServiceManager}")
	private LegislaturaServiceManager legislaturaServiceManager;

	@ManagedProperty(value = "#{tipoAttoServiceManager}")
	private TipoAttoServiceManager tipoAttoServiceManager;

	private Atto atto = new Atto();

	private String numeroAtto;

	private String tipoAtto;

	private String tipologia;

	private String legislatura;

	private String anno;

	private String estensioneAtto;

	private UserBean userBean;

	private boolean tipologiaVisible;

	private List<TipoAtto> tipiAtto = new ArrayList<TipoAtto>();
	private Map<String, String> tipologie = new HashMap<String, String>() {

	};

	private List<String> legislature = new ArrayList<String>();

	private Map<String, String> anni = new HashMap<String, String>();

	/**
	 * Aggiunge i tipi di atto e le legislature al contesto web
	 */
	@PostConstruct
	private void initializeValues() {

		FacesContext context = FacesContext.getCurrentInstance();
		userBean = (UserBean) context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{userBean}", UserBean.class)
				.getValue(context.getELContext());
		setTipiAtto(tipoAttoServiceManager.retrieveAllTipoAtto(),
				GruppoUtente.AULA.equals(userBean.getUserGroupName()));
		setLegislature(legislaturaServiceManager.list());

	}

	/**
	 * Se il tipo di atto è DOC o REF viene reso visibile, altrimenti no
	 */
	public void handleTipoAttoChange() {

		if (atto.getTipoAtto() != null && !atto.getTipoAtto().equals(""))
			setTipologie(tipoAttoServiceManager.findTipologieByTipoAtto(atto.getTipoAtto()));
		else
			tipologie = new HashMap<String, String>();

		if (atto.getTipoAtto().equals("DOC") || atto.getTipoAtto().equals("REF")) {
			setTipologiaVisible(true);
		} else {
			setTipologiaVisible(false);
		}
	}

	/**
	 * Inserimento dell'atto
	 * 
	 * @return "pretty:Riepilogo_Atto" se ok altrimenti null
	 */
	public String inserisciAtto() {

		atto.setError(null);
		atto.setStato(StatoAtto.PROTOCOLLATO);

		if (GruppoUtente.AULA.equals(userBean.getUserGroupName()) && atto.getTipoAtto().equals("PDA")) {

			atto.setTipoIniziativa("05_ATTO DI INIZIATIVA UFFICIO PRESIDENZA");

		}

		Atto attoRet = attoServiceManager.persist(atto);

		if (attoRet != null && attoRet.getError() == null) {

			FacesContext context = FacesContext.getCurrentInstance();
			AttoBean attoBean = (AttoBean) context.getApplication().getExpressionFactory()
					.createValueExpression(context.getELContext(), "#{attoBean}", AttoBean.class)
					.getValue(context.getELContext());

			attoBean.setAtto(attoRet);
			attoBean.getAtto().setStato(StatoAtto.PROTOCOLLATO);

			return "pretty:Riepilogo_Atto";

		} else if (attoRet != null && attoRet.getError() != null && !attoRet.getError().equals("")) {

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"ATTENZIONE: atto già presente per la legislatura indicata", ""));
			return null;

		} else {

			return null;

		}

	}

	public AttoServiceManager getAttoServiceManager() {
		return attoServiceManager;
	}

	public void setAttoServiceManager(AttoServiceManager attoServiceManager) {
		this.attoServiceManager = attoServiceManager;
	}

	public List<TipoAtto> getTipiAtto() {
		return this.tipiAtto;
	}

	/**
	 * aggiunta dei tipi di atto
	 * 
	 * @param tipiAtto tipi di atto
	 * @param isAula se a true aggiunge anche gli atti dell'aula
	 */
	public void setTipiAtto(List<TipoAtto> tipiAtto, boolean isAula) {

		for (TipoAtto tipoAtto : tipiAtto) {

			if (tipoAtto.getCodice().equals("EAC") || tipoAtto.getCodice().equals("MIS")) {

				continue;
			}

			if (isAula && !(tipoAtto.getCodice().equals("ORG") || tipoAtto.getCodice().equals("PDA"))) {
				continue;

			} else {

				this.tipiAtto.add(tipoAtto);

			}

		}

	}

	public void setTipiAtto(List<TipoAtto> tipiAtto) {

		this.tipiAtto = tipiAtto;
	}

	public Map<String, String> getTipologie() {
		return tipologie;
	}

	public void setTipologie(Map<String, String> tipologie) {
		this.tipologie = tipologie;
	}

	public List<String> getLegislature() {
		return legislature;
	}

	public void setLegislature(List<String> legislature) {
		this.legislature = legislature;
	}

	public Map<String, String> getAnni() {
		return anni;
	}

	public void setAnni(Map<String, String> anni) {
		this.anni = anni;
	}

	public LegislaturaServiceManager getLegislaturaServiceManager() {
		return legislaturaServiceManager;
	}

	public void setLegislaturaServiceManager(LegislaturaServiceManager legislaturaServiceManager) {
		this.legislaturaServiceManager = legislaturaServiceManager;
	}

	public TipoAttoServiceManager getTipoAttoServiceManager() {
		return tipoAttoServiceManager;
	}

	public void setTipoAttoServiceManager(TipoAttoServiceManager tipoAttoServiceManager) {
		this.tipoAttoServiceManager = tipoAttoServiceManager;
	}

	public Atto getAtto() {
		return atto;
	}

	public void setAtto(Atto atto) {
		this.atto = atto;
	}

	public String getNumeroAtto() {
		return this.atto.getNumeroAtto();
	}

	public void setNumeroAtto(String numeroAtto) {
		this.atto.setNumeroAtto(numeroAtto);
	}

	public String getTipoAtto() {
		return this.atto.getTipoAtto();
	}

	public void setTipoAtto(String tipoAtto) {
		this.atto.setTipoAtto(tipoAtto);
	}

	public String getTipologia() {
		return this.atto.getTipologia();
	}

	public void setTipologia(String tipologia) {
		this.atto.setTipologia(tipologia);
		this.tipologia = tipologia;
	}

	public String getLegislatura() {
		return this.atto.getLegislatura();
	}

	public void setLegislatura(String legislatura) {
		this.atto.setLegislatura(legislatura);
	}

	public String getAnno() {
		return this.atto.getAnno();
	}

	public void setAnno(String anno) {
		this.atto.setAnno(anno);
	}

	public boolean isTipologiaVisible() {
		return tipologiaVisible;
	}

	public void setTipologiaVisible(boolean tipologiaVisible) {
		this.tipologiaVisible = tipologiaVisible;
	}

	public String getEstensioneAtto() {
		return atto.getEstensioneAtto();
	}

	public void setEstensioneAtto(String estensioneAtto) {
		this.atto.setEstensioneAtto(estensioneAtto);
	}

}
