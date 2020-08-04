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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.Consultazione;
import com.sourcesense.crl.business.model.ConsultazioneParere;
import com.sourcesense.crl.business.model.OrganismoStatutario;
import com.sourcesense.crl.business.model.SoggettoInvitato;
import com.sourcesense.crl.business.model.Target;
import com.sourcesense.crl.business.service.AttoRecordServiceManager;
import com.sourcesense.crl.business.service.AttoServiceManager;
import com.sourcesense.crl.business.service.CommissioneServiceManager;
import com.sourcesense.crl.util.CRLMessage;
import com.sourcesense.crl.util.Clonator;
import com.sourcesense.crl.web.ui.beans.AttoBean;

/**
 * Gestisce le consultazioni e i pareri degli atti
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "consultazioniPareriController")
@ViewScoped
public class ConsultazioniPareriController {

	@ManagedProperty(value = "#{attoServiceManager}")
	private AttoServiceManager attoServiceManager;

	@ManagedProperty(value = "#{attoRecordServiceManager}")
	private AttoRecordServiceManager attoRecordServiceManager;

	@ManagedProperty(value = "#{commissioneServiceManager}")
	private CommissioneServiceManager commissioneServiceManager;

	private Atto atto = new Atto();

	private List<OrganismoStatutario> organismiList = new ArrayList<OrganismoStatutario>();
	private List<String> commissioni = new ArrayList<String>();
	private List<String> commissioniDestinatarie = new ArrayList<String>();
	private String commissioneSelected;

	private OrganismoStatutario organismoSelected = new OrganismoStatutario();
	private String descrizioneOrganismoSelected;

	private Date dataRicezioneParere;
	private Date dataRicezioneOrgano;
	private String esito;
	private String noteParere;
	private String allegatoParereToDelete;
	private boolean currentFilePubblico;
	private String commissioneDestinataria;

	private List<Consultazione> consultazioniList = new ArrayList<Consultazione>();
	private Consultazione consultazioneSelected;
	private String soggettoConsultato;
	private String consultazioneToDelete;
	private String allegatoConsultazioneToDelete;

	private String descrizioneConsultazioneSelected;

	private Date dataSedutaConsultazione;
	private boolean prevista;
	private boolean discussa;
	private Date dataConsultazione;
	private String noteConsultazione;

	private List<SoggettoInvitato> soggettiInvitatiList = new ArrayList<SoggettoInvitato>();
	private String nomeSoggettoInvitato;
	private boolean intervenuto;
	private String soggettoInvitatoToDelete;

	private static final Pattern soggettoPattern = Pattern
			.compile("(.*[\\\"\\*\\\\\\>\\<\\?\\/\\:\\|]+.*)|(.*[\\.]?.*[\\.]+$)|(.*[ ]+$)");

	private String statoCommitPareri = CRLMessage.COMMIT_DONE;
	private String statoCommitConsultazioni = CRLMessage.COMMIT_DONE;

	/**
	 * Aggiunge l'atto selezionato, gli organismi, le commissioni destinatarie, le
	 * consultazioni e le commissioni al contesto web
	 */
	@PostConstruct
	protected void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));
		setAtto((Atto) attoBean.getAtto().clone());

		setOrganismiList(new ArrayList<OrganismoStatutario>(Clonator.cloneList(atto.getOrganismiStatutari())));

		setCommissioniDestinatarie(commissioneServiceManager.getAll());

		if (!organismiList.isEmpty()) {

			setOrganismoSelected(organismiList.get(0));
			setDescrizioneOrganismoSelected(organismiList.get(0).getDescrizione());
			showParereDetail();
		}

		setConsultazioniList(new ArrayList<Consultazione>(Clonator.cloneList(atto.getConsultazioni())));

		if (!consultazioniList.isEmpty()) {
			setDescrizioneConsultazioneSelected(consultazioniList.get(0).getDescrizione());
			setCommissioneSelected(consultazioniList.get(0).getCommissione());
			showConsultazioneDetail();
		}

		commissioni = attoBean.getCommissioniAssegnate();
	}

	/**
	 * Aggiornamento dei pareri
	 */
	public void updatePareriHandler() {
		setStatoCommitPareri(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento delle consultazioni
	 */
	public void updateConsultazioniHandler() {
		setStatoCommitConsultazioni(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiunge le notifiche di errore a seconda dell'operazione eseguita. Nel caso
	 * di commit dello stato dei pareri aggiunge il messaggio "Attenzione ! Le
	 * modifiche ai Pareri non sono state salvate " mentre nel caso di commit delle
	 * consultazioni "Attenzione ! Le modifiche alle Consultazioni non sono state
	 * salvate "
	 * 
	 */
	public void changeTabHandler() {

		if (statoCommitPareri.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche ai Pareri non sono state salvate ", ""));
		}

		if (statoCommitConsultazioni.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche alle Consultazioni non sono state salvate ", ""));
		}
	}

	/**
	 * Mostra i dettagli del parere
	 */
	public void showParereDetail() {

		setOrganismoSelected(findOrganismo(descrizioneOrganismoSelected));

		if (organismoSelected != null) {
			setDataRicezioneParere(organismoSelected.getParere().getDataRicezioneParere());
			setDataRicezioneOrgano(organismoSelected.getParere().getDataRicezioneOrgano());
			setEsito(organismoSelected.getParere().getEsito());
			setNoteParere(organismoSelected.getParere().getNote());
			setCommissioneDestinataria(organismoSelected.getParere().getCommissioneDestinataria());

		} else {
			setDataRicezioneParere(null);
			setDataRicezioneOrgano(null);
			setEsito("");
			setNoteParere("");

		}
	}

	/**
	 * Trova l'organismo secondo la descrizione
	 * 
	 * @param descrizione descrizione
	 * @return organismo statutario
	 */
	private OrganismoStatutario findOrganismo(String descrizione) {

		for (OrganismoStatutario element : organismiList) {
			if (element.getDescrizione().equals(descrizione)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Aggiorna l'allegato del parere
	 * 
	 * @param event evento di upload
	 */
	public void uploadAllegatoParere(FileUploadEvent event) {

		String fileName = event.getFile().getFileName();

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (attoBean.getWorkingOrganismoStatutario(descrizioneOrganismoSelected) == null) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Nessun organismo Statutario selezionato ", ""));

		} else {

			if (!checkAllegatoParere(fileName)) {

				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Attenzione ! Il file " + fileName + " è già stato allegato ", ""));
			} else {

				Allegato allegatoParereRet = new Allegato();
				allegatoParereRet.setNome(fileName);
				allegatoParereRet.setOrganismoStatutario(descrizioneOrganismoSelected);
				allegatoParereRet.setPubblico(currentFilePubblico);

				try {
					allegatoParereRet = attoServiceManager
							.uploadAllegatoPareri(
									((AttoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
											.get("attoBean")).getAtto(),
									event.getFile().getInputstream(), allegatoParereRet);

				} catch (IOException e) {
					e.printStackTrace();
				}

				setCurrentFilePubblico(false);
				attoBean.getWorkingOrganismoStatutario(descrizioneOrganismoSelected).getParere().getAllegati()
						.add(allegatoParereRet);

			}
		}
	}

	/**
	 * Verifica che il nome del file sia presente nell'elenco degli allegati del
	 * parere
	 * 
	 * @param fileName nome del file
	 * @return false se presente
	 */
	private boolean checkAllegatoParere(String fileName) {

		for (Allegato element : organismoSelected.getParere().getAllegati()) {

			if (fileName.equals(element.getNome())) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Rimuove l'allegato del parere
	 */
	public void removeAllegatoParere() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		for (Allegato element : organismoSelected.getParere().getAllegati()) {

			if (element.getId().equals(allegatoParereToDelete)) {

				attoRecordServiceManager.deleteFile(element.getId());
				organismoSelected.getParere().getAllegati().remove(element);
				for (OrganismoStatutario organ : attoBean.getAtto().getOrganismiStatutari()) {

					if (organ.getDescrizione().equals(organismoSelected.getDescrizione())) {
						organ.getParere().setAllegati(Clonator.cloneList(organismoSelected.getParere().getAllegati()));
						break;
					}

				}
				break;
			}
		}
	}

	/**
	 * Salvataggio del parere
	 */
	public void salvaParere() {

		organismoSelected.getParere().setDataRicezioneOrgano(getDataRicezioneOrgano());
		organismoSelected.getParere().setDataRicezioneParere(getDataRicezioneParere());
		organismoSelected.getParere().setEsito(getEsito());
		organismoSelected.getParere().setNote(getNoteParere());
		organismoSelected.getParere().setCommissioneDestinataria(getCommissioneDestinataria());

		atto.setOrganismiStatutari(organismiList);

		Target target = new Target();
		target.setOrganismoStatutario(descrizioneOrganismoSelected);

		ConsultazioneParere consultazioneParere = new ConsultazioneParere();
		consultazioneParere.setAtto(atto);
		consultazioneParere.setTarget(target);
		attoServiceManager.salvaPareri(consultazioneParere);

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		attoBean.getAtto().setOrganismiStatutari(organismiList);

		setStatoCommitPareri(CRLMessage.COMMIT_DONE);
		context.addMessage(null, new FacesMessage("Pareri salvati con successo", ""));
	}

	/**
	 * Dettaglio della consultazione
	 */
	public void showConsultazioneDetail() {
		setConsultazioneSelected(findConsultazione());
		setCommissioneSelected(consultazioneSelected.getCommissione());

		if (consultazioneSelected != null) {
			setDataSedutaConsultazione(consultazioneSelected.getDataSeduta());
			setPrevista(consultazioneSelected.isPrevista());
			setDiscussa(consultazioneSelected.isDiscussa());
			setNoteConsultazione(consultazioneSelected.getNote());
			setSoggettiInvitatiList(consultazioneSelected.getSoggettiInvitati());

		}

		else {
			setDataSedutaConsultazione(null);
			setDataConsultazione(null);
			setPrevista(false);
			setDiscussa(false);
			setNoteConsultazione("");
			setSoggettiInvitatiList(new ArrayList<SoggettoInvitato>());

		}
	}

	/**
	 * Preleva le informazioni della consultazione corrente
	 * 
	 * @return consultazione
	 */
	private Consultazione findConsultazione() {
		if (descrizioneConsultazioneSelected != null) {
			for (Consultazione element : consultazioniList) {
				if (element.getDescrizione().equals(descrizioneConsultazioneSelected)) {
					return element;
				}
			}
		}
		return null;
	}

	/**
	 * Verifica se il soggetto è formattato nel modo corretto
	 * 
	 * @param soggetto soggetto
	 * @return true se il soggetto non è formattato correttamente
	 */
	private boolean isValidSoggetto(String soggetto) {
		return !soggettoPattern.matcher(soggetto).matches();
	}

	/**
	 * Aggiunta della consultazione
	 */
	public void addConsultazione() {

		if (soggettoConsultato != null && !soggettoConsultato.trim().equals("")) {
			if (isValidSoggetto(soggettoConsultato)) {

				if (!checkConsultazioni()) {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Attenzione ! Soggetto consultato " + soggettoConsultato + " già presente ", ""));

				} else {
					Consultazione consultazione = new Consultazione();
					consultazione.setDescrizione(soggettoConsultato);
					consultazione.setDataConsultazione(dataConsultazione);
					consultazione.setPrevista(true);
					consultazioniList.add(consultazione);

					setDescrizioneConsultazioneSelected(soggettoConsultato);
					showConsultazioneDetail();
					updateConsultazioniHandler();
					setSoggettoConsultato("");
					setDataConsultazione(null);
				}
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! I caratteri \" | * < > \\ ? / :"
								+ " non sono consentiti. Il Soggetto invitato non può terminare con un punto o uno spazio",
								""));
			}
		}
	}

	/**
	 * Rimozione della consultazione
	 */
	public void removeConsultazione() {

		for (Consultazione element : consultazioniList) {

			if (element.getDescrizione().equals(consultazioneToDelete)) {

				consultazioniList.remove(element);

				if (!consultazioniList.isEmpty()) {
					setDescrizioneConsultazioneSelected(consultazioniList.get(0).getDescrizione());
					showConsultazioneDetail();
				} else {

					setDataSedutaConsultazione(null);
					setPrevista(false);
					setDiscussa(false);
					setNoteConsultazione("");
					setSoggettiInvitatiList(new ArrayList<SoggettoInvitato>());
					setCommissioneDestinataria(null);

					setConsultazioneSelected(null);

				}

				updateConsultazioniHandler();
				break;
			}
		}
	}

	/**
	 * Verifica se il soggetto consultato è presente nell'elenco delle descrizioni
	 * delle consultazioni
	 * 
	 * @return false se presente
	 */
	private boolean checkConsultazioni() {

		for (Consultazione element : consultazioniList) {

			if (soggettoConsultato.equals(element.getDescrizione())) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Aggiunta del soggetto invitato
	 */
	public void addSoggettoInvitato() {

		if (consultazioneSelected != null) {
			if (nomeSoggettoInvitato != null && !nomeSoggettoInvitato.trim().equals("")) {
				if (isValidSoggetto(nomeSoggettoInvitato)) {
					if (!checkSoggettiInvitati()) {
						FacesContext context = FacesContext.getCurrentInstance();
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Attenzione ! Soggetto invitato " + nomeSoggettoInvitato + " già presente ", ""));

					} else {
						SoggettoInvitato soggetto = new SoggettoInvitato();
						soggetto.setDescrizione(nomeSoggettoInvitato);
						soggetto.setIntervenuto(intervenuto);
						soggettiInvitatiList.add(soggetto);
						setNomeSoggettoInvitato("");
						setIntervenuto(false);
						updateConsultazioniHandler();
					}
				} else {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! I caratteri \" | * < > \\ ? / :"
									+ " non sono consentiti. Il Soggetto invitato non può terminare con un punto o uno spazio",
									""));
				}
			}
		}

		else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! salvare prima la consultazione ", ""));
		}
	}

	/**
	 * Rimozione del soggetto invitato
	 */
	public void removeSoggettoInvitato() {

		for (SoggettoInvitato element : soggettiInvitatiList) {

			if (element.getDescrizione().equals(soggettoInvitatoToDelete)) {

				soggettiInvitatiList.remove(element);
				updateConsultazioniHandler();
				break;
			}
		}
	}

	/**
	 * Verifica se il nome del soggetto invitato è presente nell'elenco delle
	 * descrizioni dei soggetti invitati
	 * 
	 * @return false se presente
	 */
	private boolean checkSoggettiInvitati() {

		for (SoggettoInvitato element : soggettiInvitatiList) {

			if (nomeSoggettoInvitato.equals(element.getDescrizione())) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Salvataggio della consultazione
	 */
	public void salvaConsultazione() {

		if (consultazioneSelected != null) {
			consultazioneSelected.setDataSeduta(getDataSedutaConsultazione());
			consultazioneSelected.setPrevista(isPrevista());
			consultazioneSelected.setDiscussa(isDiscussa());
			consultazioneSelected.setNote(getNoteConsultazione());
			consultazioneSelected.setCommissione(commissioneSelected);
			atto.setConsultazioni(getConsultazioniList());
			attoServiceManager.salvaConsultazioni(atto);
			FacesContext context = FacesContext.getCurrentInstance();
			AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

			Collections.sort(getConsultazioniList(), new Comparator<Consultazione>() {
				public int compare(Consultazione m1, Consultazione m2) {
					return m1.getDataConsultazione().compareTo(m2.getDataConsultazione());
				}
			});

			attoBean.getAtto().setConsultazioni(getConsultazioniList());

			setStatoCommitConsultazioni(CRLMessage.COMMIT_DONE);
			context.addMessage(null, new FacesMessage("Consultazioni salvate con successo", ""));
		}

		else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! definire prima la consultazione ", ""));
		}
	}

	/**
	 * Aggiornamento dell'allegato della consultazione
	 * 
	 * @param event evento di upload del file
	 */
	public void uploadAllegatoConsultazione(FileUploadEvent event) {

		String fileName = event.getFile().getFileName();
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (attoBean.getWorkingConsultazione(descrizioneConsultazioneSelected) == null) {

			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! salvare prima la consultazione ", ""));

		} else {

			if (!checkAllegatoConsultazione(fileName)) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Attenzione ! Il file " + fileName + " è già stato allegato ", ""));

			} else {

				Allegato allegatoConsultazioneRet = new Allegato();
				allegatoConsultazioneRet.setNome(fileName);
				allegatoConsultazioneRet.setConsultazione(descrizioneConsultazioneSelected);
				allegatoConsultazioneRet.setPubblico(currentFilePubblico);

				try {
					allegatoConsultazioneRet = attoServiceManager.uploadAllegatoConsultazioni(
							((AttoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
									.get("attoBean")).getAtto(),
							event.getFile().getInputstream(), allegatoConsultazioneRet);

				} catch (IOException e) {
					e.printStackTrace();
				}

				setCurrentFilePubblico(false);
				attoBean.getWorkingConsultazione(descrizioneConsultazioneSelected).getAllegati()
						.add(allegatoConsultazioneRet);

			}
		}
	}

	/**
	 * Verifica se il nome del file consultato è presente nell'elenco delle
	 * consultazioni selezionate
	 * 
	 * @param fileName nome del file
	 * @return false se presente
	 */
	private boolean checkAllegatoConsultazione(String fileName) {

		for (Allegato element : consultazioneSelected.getAllegati()) {

			if (fileName.equals(element.getNome())) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Rimozione dell'allegato della consultazione
	 */
	public void removeAllegatoConsultazione() {

		for (Allegato element : consultazioneSelected.getAllegati()) {

			if (element.getId().equals(allegatoConsultazioneToDelete)) {

				consultazioneSelected.getAllegati().remove(element);
				break;
			}
		}
	}

	/**
	 * Totale dei soggetti invitati
	 * 
	 * @return totale dei soggetti invitati
	 */
	public int totSoggettiInvitati() {

		return soggettiInvitatiList.size();
	}

	/**
	 * Totale dei soggetti presenti
	 * 
	 * @return totale dei soggetti invitati
	 */
	public int totSogettiPresenti() {

		int totSogettiPresenti = 0;

		for (SoggettoInvitato element : soggettiInvitatiList) {

			if (element.isIntervenuto()) {

				totSogettiPresenti++;

			}

		}
		return totSogettiPresenti;
	}

	public Atto getAtto() {
		return atto;
	}

	public void setAtto(Atto atto) {
		this.atto = atto;
	}

	public List<OrganismoStatutario> getOrganismiList() {
		return organismiList;
	}

	public void setOrganismiList(List<OrganismoStatutario> organismiList) {

		for (OrganismoStatutario organismoStatutario : organismiList) {

			if (organismoStatutario.getDataAnnullo() == null) {

				this.organismiList.add(organismoStatutario);
			}
		}

	}

	public Date getDataRicezioneParere() {
		return dataRicezioneParere;
	}

	public void setDataRicezioneParere(Date dataRicezioneParere) {
		this.dataRicezioneParere = dataRicezioneParere;
	}

	public Date getDataRicezioneOrgano() {
		return dataRicezioneOrgano;
	}

	public void setDataRicezioneOrgano(Date dataRicezioneOrgano) {
		this.dataRicezioneOrgano = dataRicezioneOrgano;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public AttoServiceManager getAttoServiceManager() {
		return attoServiceManager;
	}

	public void setAttoServiceManager(AttoServiceManager attoServiceManager) {
		this.attoServiceManager = attoServiceManager;
	}

	public String getNoteParere() {
		return noteParere;
	}

	public void setNoteParere(String noteParere) {
		this.noteParere = noteParere;
	}

	public String getDescrizioneOrganismoSelected() {
		return descrizioneOrganismoSelected;
	}

	public void setDescrizioneOrganismoSelected(String descrizioneOrganismoSelected) {
		this.descrizioneOrganismoSelected = descrizioneOrganismoSelected;
	}

	public String getAllegatoParereToDelete() {
		return allegatoParereToDelete;
	}

	public void setAllegatoParereToDelete(String allegatoParereToDelete) {
		this.allegatoParereToDelete = allegatoParereToDelete;
	}

	public String getStatoCommitPareri() {
		return statoCommitPareri;
	}

	public void setStatoCommitPareri(String statoCommitPareri) {
		this.statoCommitPareri = statoCommitPareri;
	}

	public String getStatoCommitConsultazioni() {
		return statoCommitConsultazioni;
	}

	public void setStatoCommitConsultazioni(String statoCommitConsultazioni) {
		this.statoCommitConsultazioni = statoCommitConsultazioni;
	}

	public List<Consultazione> getConsultazioniList() {
		return consultazioniList;
	}

	public void setConsultazioniList(List<Consultazione> consultazioniList) {
		this.consultazioniList = consultazioniList;
	}

	public Date getDataSedutaConsultazione() {
		return dataSedutaConsultazione;
	}

	public void setDataSedutaConsultazione(Date dataSedutaConsultazione) {
		this.dataSedutaConsultazione = dataSedutaConsultazione;
	}

	public boolean isPrevista() {
		return prevista;
	}

	public void setPrevista(boolean prevista) {
		this.prevista = prevista;
	}

	public boolean isDiscussa() {
		return discussa;
	}

	public void setDiscussa(boolean discussa) {
		this.discussa = discussa;
	}

	public Date getDataConsultazione() {
		return dataConsultazione;
	}

	public void setDataConsultazione(Date dataConsultazione) {
		this.dataConsultazione = dataConsultazione;
	}

	public String getNoteConsultazione() {
		return noteConsultazione;
	}

	public void setNoteConsultazione(String noteConsultazione) {
		this.noteConsultazione = noteConsultazione;
	}

	public List<SoggettoInvitato> getSoggettiInvitatiList() {
		return soggettiInvitatiList;
	}

	public void setSoggettiInvitatiList(List<SoggettoInvitato> soggettiInvitatiList) {
		this.soggettiInvitatiList = soggettiInvitatiList;
	}

	public Consultazione getConsultazioneSelected() {
		return consultazioneSelected;
	}

	public void setConsultazioneSelected(Consultazione consultazioneSelected) {
		this.consultazioneSelected = consultazioneSelected;
	}

	public String getDescrizioneConsultazioneSelected() {
		return descrizioneConsultazioneSelected;
	}

	public void setDescrizioneConsultazioneSelected(String descrizioneConsultazioneSelected) {
		this.descrizioneConsultazioneSelected = descrizioneConsultazioneSelected;
	}

	public String getSoggettoConsultato() {
		return soggettoConsultato;
	}

	public void setSoggettoConsultato(String soggettoConsultato) {
		this.soggettoConsultato = soggettoConsultato;
	}

	public String getConsultazioneToDelete() {
		return consultazioneToDelete;
	}

	public void setConsultazioneToDelete(String consultazioneToDelete) {
		this.consultazioneToDelete = consultazioneToDelete;
	}

	public String getNomeSoggettoInvitato() {
		return nomeSoggettoInvitato;
	}

	public void setNomeSoggettoInvitato(String nomeSoggettoInvitato) {
		this.nomeSoggettoInvitato = nomeSoggettoInvitato;
	}

	public String getSoggettoInvitatoToDelete() {
		return soggettoInvitatoToDelete;
	}

	public void setSoggettoInvitatoToDelete(String soggettoInvitatoToDelete) {
		this.soggettoInvitatoToDelete = soggettoInvitatoToDelete;
	}

	public boolean isIntervenuto() {
		return intervenuto;
	}

	public void setIntervenuto(boolean intervenuto) {
		this.intervenuto = intervenuto;
	}

	public boolean isCurrentFilePubblico() {
		return currentFilePubblico;
	}

	public void setCurrentFilePubblico(boolean currentFilePubblico) {
		this.currentFilePubblico = currentFilePubblico;
	}

	public String getAllegatoConsultazioneToDelete() {
		return allegatoConsultazioneToDelete;
	}

	public void setAllegatoConsultazioneToDelete(String allegatoConsultazioneToDelete) {
		this.allegatoConsultazioneToDelete = allegatoConsultazioneToDelete;
	}

	public OrganismoStatutario getOrganismoSelected() {
		return organismoSelected;
	}

	public void setOrganismoSelected(OrganismoStatutario organismoSelected) {
		this.organismoSelected = organismoSelected;
	}

	public List<String> getCommissioni() {
		return commissioni;
	}

	public void setCommissioni(List<String> commissioni) {
		this.commissioni = commissioni;
	}

	public String getCommissioneSelected() {
		return commissioneSelected;
	}

	public void setCommissioneSelected(String commissioneSelected) {
		this.commissioneSelected = commissioneSelected;
	}

	public String getCommissioneDestinataria() {
		return commissioneDestinataria;
	}

	public void setCommissioneDestinataria(String commissioneDestinataria) {
		this.commissioneDestinataria = commissioneDestinataria;
	}

	public AttoRecordServiceManager getAttoRecordServiceManager() {
		return attoRecordServiceManager;
	}

	public void setAttoRecordServiceManager(AttoRecordServiceManager attoRecordServiceManager) {
		this.attoRecordServiceManager = attoRecordServiceManager;
	}

	public List<String> getCommissioniDestinatarie() {
		return commissioniDestinatarie;
	}

	public void setCommissioniDestinatarie(List<String> commissioniDestinatarie) {
		this.commissioniDestinatarie = commissioniDestinatarie;
	}

	public CommissioneServiceManager getCommissioneServiceManager() {
		return commissioneServiceManager;
	}

	public void setCommissioneServiceManager(CommissioneServiceManager commissioneServiceManager) {
		this.commissioneServiceManager = commissioneServiceManager;
	}

}
