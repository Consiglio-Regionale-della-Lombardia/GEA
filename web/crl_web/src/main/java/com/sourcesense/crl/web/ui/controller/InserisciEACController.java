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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.sourcesense.crl.business.model.AttoEAC;
import com.sourcesense.crl.business.model.CollegamentoAttiSindacato;
import com.sourcesense.crl.business.model.StatoAtto;
import com.sourcesense.crl.business.service.AttoRecordServiceManager;
import com.sourcesense.crl.business.service.AttoServiceManager;
import com.sourcesense.crl.business.service.LegislaturaServiceManager;
import com.sourcesense.crl.business.service.TipoAttoServiceManager;
import com.sourcesense.crl.util.Clonator;
import com.sourcesense.crl.web.ui.beans.AttoBean;

/**
 * Inserimento dell' EAC dalle pagine web
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "inserisciEACController")
@ViewScoped
public class InserisciEACController {

	@ManagedProperty(value = "#{attoServiceManager}")
	private AttoServiceManager attoServiceManager;

	@ManagedProperty(value = "#{legislaturaServiceManager}")
	private LegislaturaServiceManager legislaturaServiceManager;

	@ManagedProperty(value = "#{tipoAttoServiceManager}")
	private TipoAttoServiceManager tipoAttoServiceManager;

	@ManagedProperty(value = "#{attoRecordServiceManager}")
	private AttoRecordServiceManager attoRecordServiceManager;

	/*
	 * private Map<String, String> tipiAttoSindacato = new HashMap<String,
	 * String>(); private Map<String, String> numeriAttoSindacato = new
	 * HashMap<String, String>();
	 */

	private List<String> tipiAttoSindacato = new ArrayList<String>();
	private String tipoAttoSindacato;
	private String annoCreazione;
	private String idAttoSindacato;
	private List<CollegamentoAttiSindacato> numeriAttoSindacato = new ArrayList<CollegamentoAttiSindacato>();
	private List<CollegamentoAttiSindacato> collegamentiAttiSindacato = new ArrayList<CollegamentoAttiSindacato>();
	private List<CollegamentoAttiSindacato> attiSindacato = new ArrayList<CollegamentoAttiSindacato>();

	private AttoEAC atto = new AttoEAC();

	private String numeroAtto;

	private String noteAtto;

	private Date dataAtto;

	private String legislatura;

	private boolean currentFilePubblico;

	private List<Allegato> allegatiEAC = new ArrayList<Allegato>();

	private List<String> legislature = new ArrayList<String>();

	private String allegatoEACToDelete;
	private String numeroAttoSindacato;
	private String descrizioneAttoSindacato;
	private String attoSindacatoToDelete;

	/**
	 * Aggiunge i tipi di atto del sindacato e le legislature al contesto web
	 */
	@PostConstruct
	private void initializeValues() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (attoBean.getAttoEAC() != null) {

			if (attoBean.getAttoEAC().getId() != null && !"".equals(attoBean.getAttoEAC().getId())) {

				atto = (AttoEAC) attoBean.getAttoEAC().clone();

				setCollegamentiAttiSindacato(attoServiceManager.findAttiSindacatoById(atto.getId()));

				attoBean.setAttoEAC(null);
			}
		}
		setTipiAttoSindacato(attoServiceManager.findTipoAttiSindacato());
		setLegislature(legislaturaServiceManager.list());
	}

	/**
	 * Inserimento dell'atto
	 */
	public void inserisciAtto() {

		atto.setTipoAtto("EAC");
		atto.setStato(StatoAtto.EAC);

		AttoEAC attoRet = attoServiceManager.persistEAC(atto);
		FacesContext context = FacesContext.getCurrentInstance();

		if (attoRet != null && attoRet.getError() == null) {

			this.atto = attoRet;

			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Atto EAC inserito con successo", ""));

		} else if (attoRet != null && attoRet.getError() != null && !attoRet.getError().equals("")) {

			/*
			 * context.addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,
			 * attoRet.getError(), ""));
			 */
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"ATTENZIONE: atto già presente per la legislatura indicata", ""));

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
				allegatoRet = attoServiceManager.uploadAllegatoEAC(atto, event.getFile().getInputstream(), allegatoRet);

			} catch (IOException e) {
				e.printStackTrace();
			}

			atto.getAllegati().add(allegatoRet);
		}
	}

	/**
	 * Verifica che il parere non sia già presente nell'elenco degli allegati EAC
	 * 
	 * @param fileName nome del file
	 * @return false se presente
	 */
	private boolean checkAllegatoParere(String fileName) {

		for (Allegato element : allegatiEAC) {

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

		for (Allegato element : getAllegatiEAC()) {

			if (element.getId().equals(allegatoEACToDelete)) {

				attoRecordServiceManager.deleteFile(element.getId());
				getAllegatiEAC().remove(element);
				attoBean.getAttoEAC().setAllegati(Clonator.cloneList(getAllegatiEAC()));
				break;
			}
		}
	}

	/**
	 * Aggiunta del collegamento degli atti del sindacato
	 */
	public void addCollegamentoAttoSindacato() {

		if (!"".equals(idAttoSindacato)) {
			if (!checkCollegamentiAttiSindacati()) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Atto già collegato ", ""));

			} else {

				for (CollegamentoAttiSindacato collegamento : attiSindacato) {

					if (collegamento.getIdAtto().equals(idAttoSindacato)) {

						collegamento.setDescrizione(descrizioneAttoSindacato);
						atto.getCollegamentiAttiSindacato().add(collegamento);
						break;
					}

				}
				attoServiceManager.salvaCollegamentiAttiSindacato(atto);

			}
		}
	}

	/**
	 * Ricerca gli atti del sindacato per anno di creazione e aggiorna il numero
	 * degli atti del sindacato
	 */
	public void handleAttoSindacatoChange() {
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(annoCreazione), 0, 1);
		Date dataCreazioneDa = c.getTime();
		c.set(Integer.parseInt(annoCreazione), 11, 31);
		Date dataCreazioneA = c.getTime();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		setAttiSindacato(attoServiceManager.findAllAttiSindacato(tipoAttoSindacato, formatter.format(dataCreazioneDa),
				formatter.format(dataCreazioneA)));
		getNumeriAttoSindacato().clear();
		setNumeriAttoSindacato(attiSindacato);
		/*
		 * for (CollegamentoAttiSindacato collegamento : attiSindacato) {
		 * 
		 * if (collegamento.getTipoAtto().equals(tipoAttoSindacato)) {
		 * 
		 * getNumeriAttoSindacato().add(collegamento);
		 * 
		 * }
		 * 
		 * }
		 */

	}

	/**
	 * Rimozione del collegamento degli atti del sindacato
	 */
	public void removeCollegamentoAttoSindacato() {

		for (CollegamentoAttiSindacato element : atto.getCollegamentiAttiSindacato()) {

			if (element.getNumeroAtto().equals(attoSindacatoToDelete)) {

				atto.getCollegamentiAttiSindacato().remove(element);
				break;
			}
		}

		attoServiceManager.salvaCollegamentiAttiSindacato(atto);
	}

	/**
	 * Verifica che il collegamento scelto non sia già presente nell'elenco degli
	 * atti del sindacato
	 * 
	 * @return false se presente
	 */
	private boolean checkCollegamentiAttiSindacati() {

		for (CollegamentoAttiSindacato element : collegamentiAttiSindacato) {

			if (element.getNumeroAtto().equals(numeroAttoSindacato)) {

				return false;
			}
		}
		return true;
	}

	public AttoServiceManager getAttoServiceManager() {
		return attoServiceManager;
	}

	public void setAttoServiceManager(AttoServiceManager attoServiceManager) {
		this.attoServiceManager = attoServiceManager;
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

	public AttoEAC getAtto() {
		return atto;
	}

	public void setAtto(AttoEAC atto) {
		this.atto = atto;
	}

	public String getNumeroAtto() {
		return this.atto.getNumeroAtto();
	}

	public void setNumeroAtto(String numeroAtto) {
		this.atto.setNumeroAtto(numeroAtto);
	}

	public String getNoteAtto() {
		return this.atto.getNote();
	}

	public void setNoteAtto(String noteAtto) {
		this.atto.setNote(noteAtto);
	}

	public Date getDataAtto() {
		return this.atto.getDataAtto();
	}

	public void setDataAtto(Date dataAtto) {
		this.atto.setDataAtto(dataAtto);
	}

	public List<Allegato> getAllegatiEAC() {
		return atto.getAllegati();
	}

	public void setAllegatiEAC(List<Allegato> allegatiEAC) {
		this.atto.setAllegati(allegatiEAC);
	}

	public String getAllegatoEACToDelete() {
		return allegatoEACToDelete;
	}

	public void setAllegatoEACToDelete(String allegatoEACToDelete) {
		this.allegatoEACToDelete = allegatoEACToDelete;
	}

	public List<CollegamentoAttiSindacato> getCollegamentiAttiSindacato() {
		return atto.getCollegamentiAttiSindacato();
	}

	public void setCollegamentiAttiSindacato(List<CollegamentoAttiSindacato> collegamentiAttiSindacato) {
		this.atto.setCollegamentiAttiSindacato(collegamentiAttiSindacato);
	}

	public String getTipoAttoSindacato() {
		return tipoAttoSindacato;
	}

	public void setTipoAttoSindacato(String tipoAttoSindacato) {
		this.tipoAttoSindacato = tipoAttoSindacato;
	}

	public String getNumeroAttoSindacato() {
		return numeroAttoSindacato;
	}

	public void setNumeroAttoSindacato(String numeroAttoSindacato) {
		this.numeroAttoSindacato = numeroAttoSindacato;
	}

	public String getDescrizioneAttoSindacato() {
		return descrizioneAttoSindacato;
	}

	public void setDescrizioneAttoSindacato(String descrizioneAttoSindacato) {
		this.descrizioneAttoSindacato = descrizioneAttoSindacato;
	}

	public String getAttoSindacatoToDelete() {
		return attoSindacatoToDelete;
	}

	public void setAttoSindacatoToDelete(String attoSindacatoToDelete) {
		this.attoSindacatoToDelete = attoSindacatoToDelete;
	}

	/*
	 * public Map<String, String> getTipiAttoSindacato() { return tipiAttoSindacato;
	 * }
	 * 
	 * 
	 * public void setTipiAttoSindacato(Map<String, String> tipiAttoSindacato) {
	 * this.tipiAttoSindacato = tipiAttoSindacato; }
	 * 
	 * 
	 * public Map<String, String> getNumeriAttoSindacato() { return
	 * numeriAttoSindacato; }
	 * 
	 * 
	 * public void setNumeriAttoSindacato(Map<String, String> numeriAttoSindacato) {
	 * this.numeriAttoSindacato = numeriAttoSindacato; }
	 */

	public boolean isCurrentFilePubblico() {
		return currentFilePubblico;
	}

	public List<String> getTipiAttoSindacato() {
		return tipiAttoSindacato;
	}

	public void setTipiAttoSindacato(List<String> tipiAttoSindacato) {
		this.tipiAttoSindacato = tipiAttoSindacato;
	}

	public List<CollegamentoAttiSindacato> getNumeriAttoSindacato() {
		return numeriAttoSindacato;
	}

	public void setNumeriAttoSindacato(List<CollegamentoAttiSindacato> numeriAttoSindacato) {
		this.numeriAttoSindacato = numeriAttoSindacato;
	}

	public List<CollegamentoAttiSindacato> getAttiSindacato() {
		return attiSindacato;
	}

	public void setAttiSindacato(List<CollegamentoAttiSindacato> attiSindacato) {
		this.attiSindacato = attiSindacato;
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

	public AttoRecordServiceManager getAttoRecordServiceManager() {
		return attoRecordServiceManager;
	}

	public void setAttoRecordServiceManager(AttoRecordServiceManager attoRecordServiceManager) {
		this.attoRecordServiceManager = attoRecordServiceManager;
	}

	public String getIdAttoSindacato() {
		return idAttoSindacato;
	}

	public void setIdAttoSindacato(String idAttoSindacato) {
		this.idAttoSindacato = idAttoSindacato;
	}

	public String getAnnoCreazione() {
		return annoCreazione;
	}

	public void setAnnoCreazione(String annoCreazione) {
		this.annoCreazione = annoCreazione;
	}

	public String getLegislatura() {
		return this.atto.getLegislatura();
	}

	public void setLegislatura(String legislatura) {
		this.atto.setLegislatura(legislatura);
	}

}
