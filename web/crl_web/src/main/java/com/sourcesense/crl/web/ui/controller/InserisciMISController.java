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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.AttoMIS;
import com.sourcesense.crl.business.model.Relatore;
import com.sourcesense.crl.business.model.StatoAtto;
import com.sourcesense.crl.business.service.AttoRecordServiceManager;
import com.sourcesense.crl.business.service.AttoServiceManager;
import com.sourcesense.crl.business.service.CommissioneServiceManager;
import com.sourcesense.crl.business.service.LegislaturaServiceManager;
import com.sourcesense.crl.business.service.PersonaleServiceManager;
import com.sourcesense.crl.web.ui.beans.AttoBean;

/**
 * Inserimento del MIS dalle pagine web
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "inserisciMISController")
@ViewScoped
public class InserisciMISController {

	@ManagedProperty(value = "#{attoServiceManager}")
	private AttoServiceManager attoServiceManager;

	@ManagedProperty(value = "#{commissioneServiceManager}")
	private CommissioneServiceManager commissioneServiceManager;

	@ManagedProperty(value = "#{legislaturaServiceManager}")
	private LegislaturaServiceManager legislaturaServiceManager;

	@ManagedProperty(value = "#{attoRecordServiceManager}")
	private AttoRecordServiceManager attoRecordServiceManager;

	@ManagedProperty(value = "#{personaleServiceManager}")
	private PersonaleServiceManager personaleServiceManager;

	private AttoMIS atto = new AttoMIS();

	private String numeroRepertorio;
	private String oggetto;
	private Date dataIniziativaComitato;
	private Date dataPropostaCommissione;
	private String commissioneCompetente;
	private String esitoVotoIntesa;
	private Date dataIntesa;
	private Date dataRispostaComitato;
	private Date dataApprovazioneProgetto;
	private Date dataApprovazioneUdP;
	private Date dataTrasmissioneUdP;
	private String istitutoIncaricato;
	private String numeroAttoUdp;
	private Date dataScadenzaMV;
	private Date dataEsameRapportoFinale;
	private Date dataTrasmissioneCommissioni;
	private String note;
	private String legislatura;
	private boolean checkAttoChiuso = false;

	private String allegatoMISToDelete;
	private boolean currentFilePubblico;

	private List<Allegato> allegatiMIS = new ArrayList<Allegato>();
	private List<String> commissioni = new ArrayList<String>();

	private List<String> legislature = new ArrayList<String>();

	private List<Relatore> relatoriList = new ArrayList<Relatore>();
	private String relatore1;
	private String relatore2;

	/**
	 * Aggiunge le commissioni, le legislature e i relatori al contesto web
	 */
	@PostConstruct
	private void initializeValues() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (attoBean.getAttoMIS() != null) {

			atto = (AttoMIS) attoBean.getAttoMIS().clone();
			attoBean.setAttoMIS(null);
		}

		setCommissioni(commissioneServiceManager.getAll());
		setLegislature(legislaturaServiceManager.list());
		setRelatoriList(personaleServiceManager.getAllRelatori());

	}

	/**
	 * Inserimento dell'atto
	 */
	public void inserisciAtto() {

		atto.setTipoAtto("MIS");
		atto.setNumeroAtto(atto.getNumeroRepertorio());

		AttoMIS attoRet = attoServiceManager.persistMIS(atto);
		FacesContext context = FacesContext.getCurrentInstance();

		if (attoRet != null && attoRet.getError() == null) {

			this.atto = attoRet;
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Atto MIS inserito con successo", ""));

		} else if (attoRet != null && attoRet.getError() != null && !attoRet.getError().equals("")) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"ATTENZIONE: atto già presente per la legislatura indicata", ""));
			/*
			 * context.addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,
			 * attoRet.getError(), ""));
			 */

		}
	}

	/**
	 * Upload dell'allegato parere
	 * 
	 * @param event evento di upload del file
	 */
	public void uploadAllegatoParere(FileUploadEvent event) {
		String fileName = event.getFile().getFileName();

		if (!checkAllegatoParere(fileName)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Il file " + fileName + " è già stato allegato ", ""));
		} else {

			Allegato allegatoRet = new Allegato();
			allegatoRet.setNome(event.getFile().getFileName());
			allegatoRet.setPubblico(currentFilePubblico);

			try {
				allegatoRet = attoServiceManager.uploadAllegatoMIS(atto, event.getFile().getInputstream(), allegatoRet);

			} catch (IOException e) {
				e.printStackTrace();
			}

			atto.getAllegati().add(allegatoRet);
		}
	}

	/**
	 * Verifica che il parere non sia già presente nell'elenco degli allegati MIS
	 * 
	 * @param fileName nome del file
	 * @return false se presente
	 */
	private boolean checkAllegatoParere(String fileName) {

		for (Allegato element : allegatiMIS) {

			if (fileName.equals(element.getDescrizione())) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Rimozione dell'allegato parere
	 */
	public void removeAllegatoParere() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		for (Allegato element : getAllegatiMIS()) {

			if (element.getId().equals(allegatoMISToDelete)) {

				attoRecordServiceManager.deleteFile(element.getId());
				getAllegatiMIS().remove(element);
				break;
			}
		}
	}

	public AttoServiceManager getAttoServiceManager() {
		return attoServiceManager;
	}

	public void setAttoServiceManager(AttoServiceManager attoServiceManager) {
		this.attoServiceManager = attoServiceManager;
	}

	public AttoMIS getAtto() {
		return atto;
	}

	public void setAtto(AttoMIS atto) {
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

	public String getAnno() {
		return this.atto.getAnno();
	}

	public void setAnno(String anno) {
		this.atto.setAnno(anno);
	}

	public String getaAllegatoMISToDelete() {
		return allegatoMISToDelete;
	}

	public void setAllegatoMISToDelete(String allegatoMISToDelete) {
		this.allegatoMISToDelete = allegatoMISToDelete;
	}

	public CommissioneServiceManager getCommissioneServiceManager() {
		return commissioneServiceManager;
	}

	public void setCommissioneServiceManager(CommissioneServiceManager commissioneServiceManager) {
		this.commissioneServiceManager = commissioneServiceManager;
	}

	public String getNumeroRepertorio() {
		return atto.getNumeroRepertorio();
	}

	public void setNumeroRepertorio(String numeroRepertorio) {
		this.atto.setNumeroRepertorio(numeroRepertorio);
	}

	public String getOggetto() {
		return atto.getOggetto();
	}

	public void setOggetto(String oggetto) {
		this.atto.setOggetto(oggetto);
	}

	public Date getDataIniziativaComitato() {
		return atto.getDataIniziativaComitato();
	}

	public void setDataIniziativaComitato(Date dataIniziativaComitato) {
		this.atto.setDataIniziativaComitato(dataIniziativaComitato);
	}

	public Date getDataPropostaCommissione() {
		return atto.getDataPropostaCommissione();
	}

	public void setDataPropostaCommissione(Date dataPropostaCommissione) {
		this.atto.setDataPropostaCommissione(dataPropostaCommissione);
	}

	public String getCommissioneCompetente() {
		return atto.getCommissioneCompetente();
	}

	public void setCommissioneCompetente(String commissioneCompetente) {
		this.atto.setCommissioneCompetente(commissioneCompetente);
	}

	public String getEsitoVotoIntesa() {
		return atto.getEsitoVotoIntesa();
	}

	public void setEsitoVotoIntesa(String esitoVotoIntesa) {
		this.atto.setEsitoVotoIntesa(esitoVotoIntesa);
	}

	public Date getDataIntesa() {
		return atto.getDataIntesa();
	}

	public void setDataIntesa(Date dataIntesa) {
		this.atto.setDataIntesa(dataIntesa);
	}

	public Date getDataRispostaComitato() {
		return atto.getDataRispostaComitato();
	}

	public void setDataRispostaComitato(Date dataRispostaComitato) {
		this.atto.setDataRispostaComitato(dataRispostaComitato);
	}

	public Date getDataApprovazioneProgetto() {
		return atto.getDataApprovazioneProgetto();
	}

	public void setDataApprovazioneProgetto(Date dataApprovazioneProgetto) {
		this.atto.setDataApprovazioneProgetto(dataApprovazioneProgetto);
	}

	public Date getDataApprovazioneUdP() {
		return atto.getDataApprovazioneUdP();
	}

	public void setDataApprovazioneUdP(Date dataApprovazioneUdP) {
		this.atto.setDataApprovazioneUdP(dataApprovazioneUdP);
	}

	public String getIstitutoIncaricato() {
		return atto.getIstitutoIncaricato();
	}

	public void setIstitutoIncaricato(String istitutoIncaricato) {
		this.atto.setIstitutoIncaricato(istitutoIncaricato);
	}

	public String getNumeroAttoUdp() {
		return atto.getNumeroAttoUdp();
	}

	public void setNumeroAttoUdp(String numeroAttoUdp) {
		this.atto.setNumeroAttoUdp(numeroAttoUdp);
	}

	public Date getDataScadenzaMV() {
		return atto.getDataScadenzaMV();
	}

	public void setDataScadenzaMV(Date dataScadenzaMV) {
		this.atto.setDataScadenzaMV(dataScadenzaMV);
	}

	public Date getDataEsameRapportoFinale() {
		return atto.getDataEsameRapportoFinale();
	}

	public void setDataEsameRapportoFinale(Date dataEsameRapportoFinale) {
		this.atto.setDataEsameRapportoFinale(dataEsameRapportoFinale);
	}

	public Date getDataTrasmissioneCommissioni() {
		return atto.getDataTrasmissioneCommissioni();
	}

	public void setDataTrasmissioneCommissioni(Date dataTrasmissioneCommissioni) {
		this.atto.setDataTrasmissioneCommissioni(dataTrasmissioneCommissioni);
	}

	public String getNote() {
		return atto.getNote();
	}

	public void setNote(String note) {
		this.atto.setNote(note);
	}

	public List<Allegato> getAllegatiMIS() {
		return atto.getAllegati();
	}

	public void setAllegatiMIS(List<Allegato> allegatiMIS) {
		this.atto.setAllegati(allegatiMIS);
	}

	public String getAllegatoMISToDelete() {
		return allegatoMISToDelete;
	}

	public List<String> getCommissioni() {
		return commissioni;
	}

	public void setCommissioni(List<String> commissioni) {
		this.commissioni = commissioni;
	}

	public boolean isCurrentFilePubblico() {
		return currentFilePubblico;
	}

	public void setCurrentFilePubblico(boolean currentFilePubblico) {
		this.currentFilePubblico = currentFilePubblico;
	}

	public List<String> getLegislature() {
		return legislature;
	}

	public void setLegislature(List<String> legislature) {
		this.legislature = legislature;
	}

	public LegislaturaServiceManager getLegislaturaServiceManager() {
		return legislaturaServiceManager;
	}

	public void setLegislaturaServiceManager(LegislaturaServiceManager legislaturaServiceManager) {
		this.legislaturaServiceManager = legislaturaServiceManager;
	}

	public String getLegislatura() {
		return this.atto.getLegislatura();
	}

	public void setLegislatura(String legislatura) {
		this.atto.setLegislatura(legislatura);
	}

	public AttoRecordServiceManager getAttoRecordServiceManager() {
		return attoRecordServiceManager;
	}

	public void setAttoRecordServiceManager(AttoRecordServiceManager attoRecordServiceManager) {
		this.attoRecordServiceManager = attoRecordServiceManager;
	}

	public PersonaleServiceManager getPersonaleServiceManager() {
		return personaleServiceManager;
	}

	public void setPersonaleServiceManager(PersonaleServiceManager personaleServiceManager) {
		this.personaleServiceManager = personaleServiceManager;
	}

	public List<Relatore> getRelatoriList() {
		return relatoriList;
	}

	public void setRelatoriList(List<Relatore> relatoriList) {
		this.relatoriList = relatoriList;
	}

	public String getRelatore1() {
		return atto.getRelatore1();
	}

	public void setRelatore1(String relatore1) {
		this.atto.setRelatore1(relatore1);
	}

	public String getRelatore2() {
		return atto.getRelatore2();
	}

	public void setRelatore2(String relatore2) {
		this.atto.setRelatore2(relatore2);
	}

	public Date getDataTrasmissioneUdP() {
		return atto.getDataTrasmissioneUdP();
	}

	public void setDataTrasmissioneUdP(Date dataTrasmissioneUdP) {
		this.atto.setDataTrasmissioneUdP(dataTrasmissioneUdP);
	}

	public boolean isCheckAttoChiuso() {
		return checkAttoChiuso;
	}

	public void setCheckAttoChiuso(boolean checkAttoChiuso) {
		this.checkAttoChiuso = checkAttoChiuso;
		if (checkAttoChiuso)
			atto.setStato("Chiuso");
		else
			atto.setStato(StatoAtto.MIS);

	}

}
