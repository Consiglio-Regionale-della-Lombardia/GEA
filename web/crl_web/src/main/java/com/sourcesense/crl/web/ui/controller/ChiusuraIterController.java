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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.StatoAtto;
import com.sourcesense.crl.business.service.AttoServiceManager;
import com.sourcesense.crl.util.URLBuilder;
import com.sourcesense.crl.web.ui.beans.AttoBean;

/**
 * Gestisce le operazioni di chiusura delle sedute
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "chiusuraIterController")
@ViewScoped
public class ChiusuraIterController {

	@ManagedProperty(value = "#{attoServiceManager}")
	private AttoServiceManager attoServiceManager;

	private Atto atto = new Atto();

	private Date dataChiusura = new Date();
	private String tipoChiusura;
	private String note;
	private String numRiferimentoBurl;
	private Date dataBurl;
	private String numeroLr;
	private Date dataLr;
	private String urlLeggiRegionali;
	private String numeroDcr;
	private String numeroDgrSeguito;
	private Date dataDgrSeguito;
	private boolean dgr;
	private String numRegolamento;
	private Date dataRegolamento;

	AttoBean attoBean;

	/**
	 * Aggiunge l'atto e il dgr selezionati al contesto web 
	 */
	@PostConstruct
	protected void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));
		setAtto((Atto) attoBean.getAtto().clone());

		if (atto.getTipoChiusura() != null) {
			dgr = "Parere trasmesso alla Giunta".trim().equals(atto.getTipoChiusura().trim());
		}

	}

	/**
	 * Operazione di chiusura dell'atto
	 */
	public void chiusuraAtto() {

		this.atto.setStato(StatoAtto.CHIUSO);

		attoServiceManager.chiusuraAtto(atto);

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		attoBean.getAtto().setDataChiusura(getDataChiusura());
		attoBean.getAtto().setTipoChiusura(getTipoChiusura());
		attoBean.getAtto().setNoteChiusuraIter(getNote());
		attoBean.getAtto().setNumeroPubblicazioneBURL(getNumRiferimentoBurl());
		attoBean.getAtto().setDataPubblicazioneBURL(getDataBurl());
		attoBean.getAtto().setNumeroLr(getNumeroLr());
		attoBean.getAtto().setDataLR(getDataLr());
		attoBean.getAtto().setNumeroDgrSeguito(getNumeroDgrSeguito());
		attoBean.getAtto().setDataDgrSeguito(getDataDgrSeguito());
		attoBean.getAtto().setNumRegolamento(getNumRegolamento());
		attoBean.getAtto().setDataRegolamento(getDataRegolamento());
		attoBean.setStato(StatoAtto.CHIUSO);
		context.addMessage(null, new FacesMessage("Atto chiuso con successo", ""));
	}

	/**
	 * Operazione di cambio dgr
	 */
	public void changeDgr() {

		String tipo = atto.getTipoChiusura().trim();

		dgr = "Parere trasmesso alla Giunta".trim().equals(tipo);

	}

	/**
	 * Creazione del link della legge regionale
	 */
	public void createLeggeRegionaleLink() {

		if ("".equals(getNumeroLr()) || getDataLr() == null || getNumeroLr() == null) {

			setUrlLeggiRegionali("");

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! sono necessari la Data ed il Numero LR", ""));

		} else {

			URLBuilder urlBuilder = new URLBuilder();
			Format formatter = new SimpleDateFormat("yyyyMMdd");
			String data = formatter.format(getDataLr());
			String numero = getNumeroLr();
			try {

				if (numero.length() < 5) {
					String app = "";
					for (int i = 0; i < (5 - numero.length()); i++) {

						app += "0";
					}

					numero = app + numero;

				}

				setUrlLeggiRegionali(attoServiceManager.regioniUrl(data, numero));

			} catch (Exception e) {

			}
		}

	}

	/**
	 * Ritorna un parere negativo trasmesso alla Giunta
	 * 
	 * @return il valore "Parere negativo trasmesso alla Giunta"
	 */
	public String tipoChiusuraPar() {

		return "Parere negativo trasmesso alla Giunta";

		/*
		 * FacesContext context = FacesContext.getCurrentInstance(); attoBean =
		 * (AttoBean) context .getApplication() .getExpressionFactory()
		 * .createValueExpression(context.getELContext(), "#{attoBean}",
		 * AttoBean.class).getValue(context.getELContext());
		 * 
		 * 
		 * 
		 * if (attoBean.getTipoAtto().equals("PAR"))
		 * 
		 * { return "Respinto"; }
		 * 
		 * else { return "Rifiutato e trasmesso alla Giunta"; }
		 */

	}

	public Atto getAtto() {
		return atto;
	}

	public void setAtto(Atto atto) {
		this.atto = atto;
	}

	public Date getDataChiusura() {
		return atto.getDataChiusura();
	}

	public void setDataChiusura(Date dataChiusura) {
		this.atto.setDataChiusura(dataChiusura);
	}

	public String getTipoChiusura() {
		return atto.getTipoChiusura();
	}

	public void setTipoChiusura(String tipoChiusura) {
		this.atto.setTipoChiusura(tipoChiusura);
	}

	public String getNote() {
		return atto.getNoteChiusuraIter();
	}

	public void setNote(String note) {
		this.atto.setNoteChiusuraIter(note);
	}

	public String getNumRiferimentoBurl() {
		return atto.getNumeroPubblicazioneBURL();
	}

	public void setNumRiferimentoBurl(String numRiferimentoBurl) {
		this.atto.setNumeroPubblicazioneBURL(numRiferimentoBurl);
	}

	public Date getDataBurl() {
		return atto.getDataPubblicazioneBURL();
	}

	public void setDataBurl(Date dataBurl) {
		this.atto.setDataPubblicazioneBURL(dataBurl);
	}

	public String getNumeroLr() {
		return atto.getNumeroLr();
	}

	public void setNumeroLr(String numeroLr) {
		this.atto.setNumeroLr(numeroLr);
	}

	public Date getDataLr() {
		return atto.getDataLR();
	}

	public void setDataLr(Date dataLr) {
		this.atto.setDataLR(dataLr);
	}

	public String getNumeroDcr() {
		return atto.getNumeroDcr();
	}

	public void setNumeroDcr(String numeroDcr) {
		this.atto.setNumeroDcr(numeroDcr);
	}

	public AttoServiceManager getAttoServiceManager() {
		return attoServiceManager;
	}

	public void setAttoServiceManager(AttoServiceManager attoServiceManager) {
		this.attoServiceManager = attoServiceManager;
	}

	public String getUrlLeggiRegionali() {
		return urlLeggiRegionali;
	}

	public void setUrlLeggiRegionali(String urlLeggiRegionali) {
		this.urlLeggiRegionali = urlLeggiRegionali;
	}

	public String getNumeroDgrSeguito() {
		return this.atto.getNumeroDgrSeguito();
	}

	public void setNumeroDgrSeguito(String numeroDgrSeguito) {
		this.atto.setNumeroDgrSeguito(numeroDgrSeguito);
	}

	public Date getDataDgrSeguito() {
		return this.atto.getDataDgrSeguito();
	}

	public void setDataDgrSeguito(Date dataDgrSeguito) {
		this.atto.setDataDgrSeguito(dataDgrSeguito);
	}

	public boolean isDgr() {
		return dgr;
	}

	public void setDgr(boolean dgr) {
		this.dgr = dgr;
	}

	public String getNumRegolamento() {
		return this.atto.getNumRegolamento();
	}

	public void setNumRegolamento(String numRegolamento) {
		this.atto.setNumRegolamento(numRegolamento);
	}

	public Date getDataRegolamento() {
		return this.atto.getDataRegolamento();
	}

	public void setDataRegolamento(Date dataRegolamento) {
		this.atto.setDataRegolamento(dataRegolamento);
	}

}
