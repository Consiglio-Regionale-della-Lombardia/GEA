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
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;

import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.Aula;
import com.sourcesense.crl.business.model.Commissione;
import com.sourcesense.crl.business.model.EsameAula;
import com.sourcesense.crl.business.model.EsameCommissione;
import com.sourcesense.crl.business.model.Link;
import com.sourcesense.crl.business.model.Passaggio;
import com.sourcesense.crl.business.model.Relatore;
import com.sourcesense.crl.business.model.StatoAtto;
import com.sourcesense.crl.business.model.Target;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.business.service.AttoRecordServiceManager;
import com.sourcesense.crl.business.service.AttoServiceManager;
import com.sourcesense.crl.business.service.AulaServiceManager;
import com.sourcesense.crl.business.service.CommissioneServiceManager;
import com.sourcesense.crl.business.service.PersonaleServiceManager;
import com.sourcesense.crl.util.CRLMessage;
import com.sourcesense.crl.util.Clonator;
import com.sourcesense.crl.web.ui.beans.AttoBean;
import com.sourcesense.crl.web.ui.beans.UserBean;

/**
 * Gestisce gli esami dell'aula
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "esameAulaController")
@ViewScoped
public class EsameAulaController {
	@ManagedProperty(value = "#{personaleServiceManager}")
	private PersonaleServiceManager personaleServiceManager;

	@ManagedProperty(value = "#{attoRecordServiceManager}")
	private AttoRecordServiceManager attoRecordServiceManager;

	@ManagedProperty(value = "#{aulaServiceManager}")
	private AulaServiceManager aulaServiceManager;

	@ManagedProperty(value = "#{commissioneServiceManager}")
	private CommissioneServiceManager commissioneServiceManager;

	@ManagedProperty(value = "#{attoServiceManager}")
	private AttoServiceManager attoServiceManager;

	private Atto atto = new Atto();
	private Passaggio passaggio;
	private String passaggioSelected;
	private Date dataPresaInCarico;
	private String relazioneScritta;
	private String esitoVotazione;
	private String quorum;
	private String tipologiaVotazione;
	private Date dataSedutaVotazione;
	private String numeroDcr;
	private String numeroLcr;
	private boolean emendato;
	private String noteVotazione;
	boolean currentFilePubblico = true;
	private String testoAttoVotatoToDelete;
	private String testoAttoToDelete;

	private List<TestoAtto> testiAttoVotatoList = new ArrayList<TestoAtto>();
	private List<Allegato> emendamentiList = new ArrayList<Allegato>();
	private List<Allegato> allegatiList = new ArrayList<Allegato>();
	private String emendamentoToDelete;
	private boolean readonly = false;

	private Integer numEmendPresentatiMaggior;
	private Integer numEmendPresentatiMinor;
	private Integer numEmendPresentatiGiunta;
	private Integer numEmendPresentatiMisto;
	private Integer numEmendPresentatiTotale = new Integer(0);
	private Integer numEmendApprovatiMaggior;
	private Integer numEmendApprovatiMinor;
	private Integer numEmendApprovatiGiunta;
	private Integer numEmendApprovatiMisto;
	private Integer numEmendApprovatiTotale = new Integer(0);
	private Integer nonAmmissibili;
	private Integer decaduti;
	private Integer ritirati;
	private Integer respinti;
	private Integer totaleNonApprovati = new Integer(0);
	private Integer noteEmendamenti;

	private Date dataSedutaRinvio;
	private Date dataTermineMassimo;
	private boolean rinvioCommBilancio;
	private String motivazioneRinvio;
	private Date dataSedutaStralcio;
	private Date dataIniziativaStralcio;
	private Date dataStralcio;
	private String articoli;
	private String noteStralcio;

	private String noteGenerali;

	private String allegatoToDelete;

	private List<Link> linksList = new ArrayList<Link>();
	private String nomeLink;
	private String linkToDelete;
	private String urlLink;
	private boolean pubblico;

	private String statoCommitVotazione = CRLMessage.COMMIT_DONE;
	private String statoCommitEmendamenti = CRLMessage.COMMIT_DONE;
	private String statoCommitRinvioEsame = CRLMessage.COMMIT_DONE;
	private String statoCommitStralci = CRLMessage.COMMIT_DONE;
	private String statoCommitNoteAllegati = CRLMessage.COMMIT_DONE;
	private String statoCommitDati = CRLMessage.COMMIT_DONE;

	private Aula aulaUser = new Aula();
	private String nomeRelatore;
	private List<Relatore> relatori = new ArrayList<Relatore>();
	private List<Relatore> relatoriList = new ArrayList<Relatore>();
	private Date dataNominaRelatore;
	private Date dataUscitaRelatore;
	private Object relatoreToDelete;

	private String oggetto;
	private String oggettoOriginale;

	private String numeroReg;

	/**
	 * Aggiunge l'atto selezionato, l'utente dell'aula, i testi dell'atto votato,
	 * gli emendamenti, gli allegati, i link e i relatori al contesto web
	 */
	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));
		setAtto((Atto) attoBean.getAtto().clone());

		aulaUser = (Aula) attoBean.getLastPassaggio().getAula().clone();
		testiAttoVotatoList = Clonator.cloneList(aulaUser.getTestiAttoVotatoEsameAula());
		emendamentiList = Clonator.cloneList(aulaUser.getEmendamentiEsameAula());
		allegatiList = Clonator.cloneList(attoBean.getAllegatiAula());
		linksList = new ArrayList<Link>(aulaUser.getLinksEsameAula());
		relatoriList = Clonator.cloneList(attoBean.getAtto().getRelatori());

		boolean original = atto.getOggettoOriginale() != null && !"".equals(atto.getOggettoOriginale());
		boolean oggNew = atto.getOggetto() != null && !"".equals(atto.getOggetto());

		if (original && oggNew) {
			setOggetto(atto.getOggetto());
			setOggettoOriginale(atto.getOggettoOriginale());

		} else {

			setOggettoOriginale(atto.getOggetto());
		}

		totaleEmendApprovati();
		totaleEmendPresentati();
		totaleNonApprovati();
		setPassaggioSelected(attoBean.getLastPassaggio().getNome());

		setRelatori(personaleServiceManager.getAllRelatori());

		UserBean userBean = ((UserBean) context.getExternalContext().getSessionMap().get("userBean"));

	}

	/**
	 * Verifica che il passaggio selezinato sia presente nei passaggi dell'atto
	 */
	public void changePassaggio() {

		Passaggio passaggioSelected = null;

		int conta = 0;
		for (Passaggio passaggioRec : this.atto.getPassaggi()) {

			conta++;

			if (passaggioRec.getNome().equalsIgnoreCase(this.passaggioSelected)) {

				passaggioSelected = passaggioRec;
				break;
			}

		}

		if (conta < this.atto.getPassaggi().size()) {

			setReadonly(true);
		} else {
			setReadonly(false);

		}

		setAulaUser(passaggioSelected.getAula());

	}

	/**
	 * Aggiornamento dello stato dei dati
	 */
	public void updateDatiHandler() {
		setStatoCommitDati(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento dello stato delle votazioni
	 */
	public void updateVotazioneHandler() {
		setStatoCommitVotazione(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento dello stato degli emendamenti
	 */
	public void updateEmendamentiHandler() {
		setStatoCommitEmendamenti(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento del rinvio esame
	 */
	public void updateRinvioEsameHandler() {
		setStatoCommitRinvioEsame(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento degli stralci
	 */
	public void updateStralciHandler() {
		setStatoCommitStralci(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento delle note degli allegati
	 */
	public void updateNoteAllegatiHandler() {
		setStatoCommitNoteAllegati(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiunge le notifiche di errore a seconda dell'operazione eseguita. Nel caso
	 * di commit dello stato della votazione, degli emendamenti, del rinvio esame,
	 * degli stralci, delle note e dei dati
	 */
	public void changeTabHandler() {

		if (statoCommitVotazione.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche a Votazione non sono state salvate ", ""));
		}

		if (statoCommitEmendamenti.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche su Emendamenti non sono state salvate ", ""));
		}

		if (statoCommitRinvioEsame.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche a Rinvio Esame non sono state salvate ", ""));
		}

		if (statoCommitStralci.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche a Stralci non sono state salvate ", ""));
		}

		if (statoCommitNoteAllegati.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche a Note e Allegati non sono state salvate ", ""));
		}

		if (statoCommitDati.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche a Dati Atto non sono state salvate ", ""));
		}
	}

	/**
	 * Presa in carico
	 */
	public void presaInCarico() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		String username = ((UserBean) context.getExternalContext().getSessionMap().get("userBean")).getUsername();

		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setAula((Aula) aulaUser.clone());

		if (atto.getStato().equals(StatoAtto.TRASMESSO_AULA)
				|| ("PDA".equals(atto.getTipoAtto())
						&& "05_ATTO DI INIZIATIVA UFFICIO PRESIDENZA".equals(atto.getTipoIniziativa()))
				|| "ORG".equals(atto.getTipoAtto()))

		{
			atto.setStato(StatoAtto.PRESO_CARICO_AULA);
		}

		Target target = new Target();
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameAula esameAula = new EsameAula();
		esameAula.setTarget(target);
		esameAula.setAtto(atto);
		aulaServiceManager.presaInCarico(esameAula);

		attoBean.getWorkingAula().setDataPresaInCaricoEsameAula(aulaUser.getDataPresaInCaricoEsameAula());
		attoBean.getWorkingAula().setRelazioneScritta(aulaUser.getRelazioneScritta());

		if (attoBean.getStato().equals(StatoAtto.TRASMESSO_AULA)
				|| ("PDA".equals(attoBean.getTipoAtto())
						&& "05_ATTO DI INIZIATIVA UFFICIO PRESIDENZA".equals(attoBean.getTipoIniziativa()))
				|| "ORG".equals(attoBean.getTipoAtto())) {
			attoBean.setStato(StatoAtto.PRESO_CARICO_AULA);
		}

		String numeroAtto = attoBean.getNumeroAtto();
		context.addMessage(null,
				new FacesMessage("Atto " + numeroAtto + " preso in carico con successo dall' utente " + username, ""));

		setStatoCommitVotazione(CRLMessage.COMMIT_DONE);
	}

	/**
	 * Aggiornamento del testo dell'atto votato
	 * 
	 * @param event evento di upload del file
	 */
	public void uploadTestoAttoVotato(FileUploadEvent event) {

		String fileName = event.getFile().getFileName();
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (!checkTestoAttoVotato(fileName)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Il file " + fileName + " è già stato allegato ", ""));
		} else {

			TestoAtto allegatoRet = new TestoAtto();
			allegatoRet.setNome(event.getFile().getFileName());
			allegatoRet.setPubblico(currentFilePubblico);
			allegatoRet.setPassaggio(attoBean.getLastPassaggio().getNome());

			try {

				allegatoRet = aulaServiceManager.uploadTestoAttoVotatoEsameAula(((AttoBean) FacesContext
						.getCurrentInstance().getExternalContext().getSessionMap().get("attoBean")).getAtto(),
						event.getFile().getInputstream(), allegatoRet);

			} catch (IOException e) {
				e.printStackTrace();
			}

			setCurrentFilePubblico(false);
			attoBean.getWorkingAula().getTestiAttoVotatoEsameAula().add(allegatoRet);
			testiAttoVotatoList.add(allegatoRet);
		}
	}

	/**
	 * Aggiornamento del testo dell'atto
	 * 
	 * @param event evento di upload del file
	 */
	public void uploadTestoAtto(FileUploadEvent event) {

		String fileName = event.getFile().getFileName();
		if (!checkTestoAtto(fileName)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Il file " + fileName + " è già stato allegato ", ""));

		} else {

			TestoAtto testoAttoRet = new TestoAtto();

			testoAttoRet.setNome(event.getFile().getFileName());
			testoAttoRet.setPubblico(currentFilePubblico);

			try {
				testoAttoRet = attoServiceManager.uploadTestoAttoPresentazioneAssegnazione(((AttoBean) FacesContext
						.getCurrentInstance().getExternalContext().getSessionMap().get("attoBean")).getAtto(),
						event.getFile().getInputstream(), testoAttoRet);

			} catch (IOException e) {
				e.printStackTrace();
			}

			FacesContext context = FacesContext.getCurrentInstance();
			AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

			attoBean.getAtto().getTestiAtto().add(testoAttoRet);

			currentFilePubblico = false;
		}
	}

	/**
	 * Verifica che il testo dell'atto selezionato sia presente nell'atto
	 * 
	 * @param fileName nome del file
	 * @return false se presente
	 */
	private boolean checkTestoAtto(String fileName) {

		for (TestoAtto element : getAtto().getTestiAtto()) {

			if (element.getNome().equals(fileName)) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Verifica che il testo dell'atto votato selezionato sia presente nell'atto
	 * 
	 * @param fileName nome del file
	 * @return false se presente
	 */
	private boolean checkTestoAttoVotato(String fileName) {

		for (TestoAtto element : testiAttoVotatoList) {

			if (fileName.equals(element.getNome())) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Rimozione del testo dell'atto votato
	 */
	public void removeTestoAttoVotato() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		for (TestoAtto element : testiAttoVotatoList) {

			if (element.getId().equals(testoAttoVotatoToDelete)) {

				attoRecordServiceManager.deleteFile(element.getId());
				testiAttoVotatoList.remove(element);
				attoBean.getLastPassaggio().getAula()
						.setTestiAttoVotatoEsameAula(Clonator.cloneList(testiAttoVotatoList));
				break;
			}
		}
	}

	/**
	 * Rimuove il testo dell'atto
	 */
	public void removeTestoAtto() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		Iterator<TestoAtto> it = getAtto().getTestiAtto().iterator();
		while (it.hasNext()) {
			TestoAtto element = it.next();
			if (element.getId().equals(testoAttoToDelete)) {
				attoRecordServiceManager.deleteFile(element.getId());
				it.remove();
				break;
			}
		}
	}

	/**
	 * Salvataggio della votazione
	 * 
	 * @return una stringa vuota
	 */
	public String salvaVotazione() {

		String navigation = "";
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		atto.setStato(StatoAtto.VOTATO_AULA);
		attoBean.setStato(StatoAtto.VOTATO_AULA);

		Target target = new Target();
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameAula esameAula = new EsameAula();
		esameAula.setTarget(target);
		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setAula((Aula) aulaUser.clone());
		esameAula.setAtto(atto);

		aulaServiceManager.salvaVotazioneEsameAula(esameAula);

		attoBean.getWorkingAula().setEsitoVotoAula(aulaUser.getEsitoVotoAula());
		attoBean.getWorkingAula().setTipologiaVotazione(aulaUser.getTipologiaVotazione());
		attoBean.getWorkingAula().setDataSedutaAula(aulaUser.getDataSedutaAula());
		attoBean.getWorkingAula().setNumeroDcr(aulaUser.getNumeroDcr());
		attoBean.getWorkingAula().setNumeroLcr(aulaUser.getNumeroLcr());
		attoBean.getWorkingAula().setEmendato(isEmendato());
		attoBean.getWorkingAula().setNoteVotazione(aulaUser.getNoteVotazione());
		attoBean.getWorkingAula().setNumeroReg(aulaUser.getNumeroReg());
		attoBean.getWorkingAula().setQuorumEsameAula(aulaUser.getQuorumEsameAula());
		setStatoCommitVotazione(CRLMessage.COMMIT_DONE);

		String message = "";

		if (aulaUser.isEmendato() && getNumEmendApprovatiTotale() == 0) {

			message = "Votazione salvata con successo \n Attenzione atto emendato senza Emendamenti Approvati";

		} else {

			message = "Votazione salvata con successo";
		}

		context.addMessage(null, new FacesMessage(message, ""));

		return navigation;
	}

	/**
	 * Aggiornamento dell'emendamento
	 * 
	 * @param event evento di upload del file
	 */
	public void uploadEmendamento(FileUploadEvent event) {
		String fileName = event.getFile().getFileName();
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (!checkEmendamenti(fileName)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Il file " + fileName + " è già stato allegato ", ""));
		} else {

			Allegato emendamentoRet = new Allegato();
			emendamentoRet.setNome(event.getFile().getFileName());
			emendamentoRet.setPubblico(currentFilePubblico);
			emendamentoRet.setPassaggio(attoBean.getLastPassaggio().getNome());

			try {
				emendamentoRet = aulaServiceManager.uploadEmendamentoEsameAula(((AttoBean) FacesContext
						.getCurrentInstance().getExternalContext().getSessionMap().get("attoBean")).getAtto(),
						event.getFile().getInputstream(), emendamentoRet);
				emendamentoRet.setPubblico(currentFilePubblico);

			} catch (IOException e) {
				e.printStackTrace();
			}

			setCurrentFilePubblico(false);

			attoBean.getWorkingAula().getEmendamentiEsameAula().add(emendamentoRet);

			emendamentiList.add(emendamentoRet);
		}
	}

	/**
	 * Verifica che l'emendamento selezionato sia presente nell'elenco degli
	 * emendamenti
	 * 
	 * @param fileName nome del file
	 * @return false se presente
	 */
	private boolean checkEmendamenti(String fileName) {

		for (Allegato element : emendamentiList) {

			if (element.getNome().equals(fileName)) {
				return false;
			}

		}

		return true;
	}

	/**
	 * Rimozione dell'emendamento
	 */
	public void removeEmendamento() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		for (Allegato element : emendamentiList) {

			if (element.getId().equals(emendamentoToDelete)) {

				attoRecordServiceManager.deleteFile(element.getId());
				emendamentiList.remove(element);
				attoBean.getLastPassaggio().getAula().setEmendamentiEsameAula(Clonator.cloneList(emendamentiList));
				break;
			}
		}
	}

	/**
	 * Aggiornamento dell'allegato
	 * 
	 * @param event evento di edit della riga
	 */
	public void updateAllegato(RowEditEvent event) {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		Allegato allegato = (Allegato) event.getObject();

		if (Allegato.TESTO_ESAME_AULA_EMENDAMENTO.equals(allegato.getTipologia())) {

			allegato.setTipoAllegato(Allegato.TESTO_ESAME_AULA_EMENDAMENTO);
			attoRecordServiceManager.updateAllegato(allegato);
			attoBean.getLastPassaggio().getAula().setEmendamentiEsameAula(Clonator.cloneList(emendamentiList));

		} else if (Allegato.TIPO_ESAME_AULA_ALLEGATO.equals(allegato.getTipologia())) {

			allegato.setTipoAllegato(Allegato.TIPO_ESAME_AULA_ALLEGATO);
			attoRecordServiceManager.updateAllegato(allegato);
			attoBean.getLastPassaggio().getAula().setAllegatiEsameAula(Clonator.cloneList(allegatiList));
		}

	}

	/**
	 * Aggiornamento del testo dell'atto
	 * 
	 * @param event evento di edit della riga
	 */
	public void updateTestoAtto(RowEditEvent event) {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		TestoAtto testoCom = (TestoAtto) event.getObject();

		if (TestoAtto.TESTO_ESAME_AULA_VOTAZIONE.equals(testoCom.getTipologia())) {

			testoCom.setTipoAllegato(TestoAtto.TESTO_ESAME_AULA_VOTAZIONE);
			attoRecordServiceManager.updateTestoAtto(testoCom);
			attoBean.getLastPassaggio().getAula().setTestiAttoVotatoEsameAula(Clonator.cloneList(testiAttoVotatoList));
		}

	}

	/**
	 * Totale degli emendamenti presentati
	 */
	public void totaleEmendPresentati() {

		numEmendPresentatiTotale = 0;

		if (getNumEmendPresentatiGiunta() != null) {
			numEmendPresentatiTotale += getNumEmendPresentatiGiunta();
		}

		if (getNumEmendPresentatiMaggior() != null) {

			numEmendPresentatiTotale += getNumEmendPresentatiMaggior();
		}
		if (getNumEmendPresentatiMinor() != null) {
			numEmendPresentatiTotale += getNumEmendPresentatiMinor();
		}
		if (getNumEmendPresentatiMisto() != null) {
			numEmendPresentatiTotale += getNumEmendPresentatiMisto();
		}

	}

	/**
	 * Totale degli emendamenti approvati
	 */
	public void totaleEmendApprovati() {

		numEmendApprovatiTotale = 0;

		if (getNumEmendApprovatiGiunta() != null) {
			numEmendApprovatiTotale += getNumEmendApprovatiGiunta();
		}

		if (getNumEmendApprovatiMaggior() != null) {

			numEmendApprovatiTotale += getNumEmendApprovatiMaggior();
		}
		if (getNumEmendApprovatiMinor() != null) {
			numEmendApprovatiTotale += getNumEmendApprovatiMinor();
		}
		if (getNumEmendApprovatiMisto() != null) {
			numEmendApprovatiTotale += getNumEmendApprovatiMisto();
		}

	}

	/**
	 * Totale degli emendamenti non approvati
	 */
	public void totaleNonApprovati() {

		totaleNonApprovati = 0;

		if (getNonAmmissibili() != null) {
			totaleNonApprovati += getNonAmmissibili();
		}

		if (getDecaduti() != null) {

			totaleNonApprovati += getDecaduti();
		}
		if (getRitirati() != null) {
			totaleNonApprovati += getRitirati();
		}
		if (getRespinti() != null) {
			totaleNonApprovati += getRespinti();
		}

	}

	/**
	 * Salvataggio degli emendamenti
	 */
	public void salvaEmendamenti() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));
		Target target = new Target();
		target.setPassaggio(attoBean.getLastPassaggio().getNome());

		EsameAula esameAula = new EsameAula();
		esameAula.setTarget(target);
		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setAula((Aula) aulaUser.clone());
		esameAula.setAtto(atto);

		aulaServiceManager.salvaEmendamentiEsameAula(esameAula);

		if (getNumEmendApprovatiTotale() > 0) {
			atto.getPassaggi().get(atto.getPassaggi().size() - 1).getAula().setEmendato(true);
			aulaUser.setEmendato(true);
			emendato = true;
			esameAula.setAtto(atto);
			attoBean.getWorkingAula().setEmendato(true);
			aulaServiceManager.salvaVotazioneEsameAula(esameAula);
		}

		attoBean.getWorkingAula()
				.setNumEmendPresentatiMaggiorEsameAula(aulaUser.getNumEmendPresentatiMaggiorEsameAula());
		attoBean.getWorkingAula().setNumEmendPresentatiMinorEsameAula(aulaUser.getNumEmendPresentatiMinorEsameAula());
		attoBean.getWorkingAula().setNumEmendPresentatiGiuntaEsameAula(aulaUser.getNumEmendPresentatiGiuntaEsameAula());
		attoBean.getWorkingAula().setNumEmendPresentatiMistoEsameAula(aulaUser.getNumEmendPresentatiMistoEsameAula());

		attoBean.getWorkingAula().setNumEmendApprovatiMaggiorEsameAula(aulaUser.getNumEmendApprovatiMaggiorEsameAula());
		attoBean.getWorkingAula().setNumEmendApprovatiMinorEsameAula(aulaUser.getNumEmendApprovatiMinorEsameAula());
		attoBean.getWorkingAula().setNumEmendApprovatiGiuntaEsameAula(aulaUser.getNumEmendApprovatiGiuntaEsameAula());
		attoBean.getWorkingAula().setNumEmendApprovatiMistoEsameAula(aulaUser.getNumEmendApprovatiMistoEsameAula());

		attoBean.getWorkingAula().setNonAmmissibiliEsameAula(aulaUser.getNonAmmissibiliEsameAula());
		attoBean.getWorkingAula().setDecadutiEsameAula(aulaUser.getDecadutiEsameAula());
		attoBean.getWorkingAula().setRitiratiEsameAula(aulaUser.getRitiratiEsameAula());
		attoBean.getWorkingAula().setRespintiEsameAula(aulaUser.getRespintiEsameAula());

		attoBean.getWorkingAula().setNoteEmendamentiEsameAula(aulaUser.getNoteEmendamentiEsameAula());

		setStatoCommitEmendamenti(CRLMessage.COMMIT_DONE);

		if (numEmendPresentatiTotale - numEmendApprovatiTotale != totaleNonApprovati) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Emendamenti salvati con successo : Attenzione dati incoerenti !", ""));

		} else {

			context.addMessage(null, new FacesMessage("Emendamenti salvati con successo", ""));
		}

	}

	/**
	 * Salvataggio del rinvio ad esame
	 * 
	 * @return il valore "pretty:Esame_Aula"
	 */
	public String salvaRinvioEsame() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setAula((Aula) aulaUser.clone());
		Target target = new Target();
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		atto.setStato(StatoAtto.ASSEGNATO_COMMISSIONE);

		EsameAula esameAula = new EsameAula();
		esameAula.setTarget(target);
		esameAula.setAtto(atto);

		Passaggio passaggio = aulaServiceManager.salvaRinvioEsameEsameAula(esameAula);

		attoBean.getWorkingAula().setDataSedutaRinvio(aulaUser.getDataSedutaRinvio());
		attoBean.getWorkingAula().setDataTermineMassimo(aulaUser.getDataTermineMassimo());
		attoBean.getWorkingAula().setMotivazioneRinvio(aulaUser.getMotivazioneRinvio());
		attoBean.getWorkingAula().setRinvioCommBilancio(aulaUser.isRinvioCommBilancio());

		attoBean.setStato(StatoAtto.ASSEGNATO_COMMISSIONE);
		attoBean.getAtto().getPassaggi().add(passaggio);
		setAtto((Atto) attoBean.getAtto().clone());
		updateStatoCommissioni(attoBean);
		setStatoCommitRinvioEsame(CRLMessage.COMMIT_DONE);
		context.addMessage(null, new FacesMessage("Rinvio Esame salvato con successo", ""));

		return "pretty:Esame_Aula";
	}

	/**
	 * Aggiornamento dello stato delle commissioni
	 * 
	 * @param attoBean atto
	 */
	public void updateStatoCommissioni(AttoBean attoBean) {

		for (Commissione commissione : atto.getPassaggi().get(atto.getPassaggi().size() - 1).getCommissioni()) {

			if (!commissione.getStato().equals(Commissione.STATO_ANNULLATO)) {

				commissione.setStato(Commissione.STATO_ASSEGNATO);
				commissione.setDataPresaInCarico(null);
				Target target = new Target();
				target.setCommissione(commissione.getDescrizione());
				target.setPassaggio(attoBean.getLastPassaggio().getNome());
				EsameCommissione esameCommissione = new EsameCommissione();
				esameCommissione.setAtto(atto);
				esameCommissione.setTarget(target);
				commissioneServiceManager.salvaPresaInCaricoEsameCommissioni(esameCommissione);

			}

		}
	}

	/**
	 * Salvataggio degli stralci
	 */
	public void salvaStralci() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setAula((Aula) aulaUser.clone());
		Target target = new Target();
		target.setPassaggio(attoBean.getLastPassaggio().getNome());

		EsameAula esameAula = new EsameAula();
		esameAula.setTarget(target);
		esameAula.setAtto(atto);

		aulaServiceManager.salvaStralciEsameAula(esameAula);

		attoBean.getWorkingAula().setDataSedutaStralcio(aulaUser.getDataSedutaStralcio());
		attoBean.getWorkingAula().setDataStralcio(aulaUser.getDataStralcio());
		attoBean.getWorkingAula().setDataIniziativaStralcio(aulaUser.getDataIniziativaStralcio());
		attoBean.getWorkingAula().setArticoli(aulaUser.getArticoli());
		attoBean.getWorkingAula().setNoteStralcio(aulaUser.getNoteStralcio());
		attoBean.getWorkingAula().setQuorumEsameAula(aulaUser.getQuorumEsameAula());

		setStatoCommitStralci(CRLMessage.COMMIT_DONE);
		context.addMessage(null, new FacesMessage("Stralci salvati con successo", ""));
	}

	/**
	 * Upload dell'allegato
	 * 
	 * @param event evento di upload del file
	 */
	public void uploadAllegato(FileUploadEvent event) {

		String fileName = event.getFile().getFileName();
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (!checkAllegato(fileName)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Il file " + fileName + " è già stato allegato ", ""));
		} else {

			Allegato allegatoRet = new Allegato();
			allegatoRet.setNome(fileName);
			allegatoRet.setPubblico(currentFilePubblico);
			allegatoRet.setPassaggio(attoBean.getLastPassaggio().getNome());
			try {
				allegatoRet = aulaServiceManager.uploadAllegatoNoteAllegatiEsameAula(((AttoBean) FacesContext
						.getCurrentInstance().getExternalContext().getSessionMap().get("attoBean")).getAtto(),
						event.getFile().getInputstream(), allegatoRet);

			} catch (IOException e) {
				e.printStackTrace();
			}
			setCurrentFilePubblico(false);
			attoBean.getWorkingAula().getAllegatiEsameAula().add(allegatoRet);

			allegatiList.add(allegatoRet);
		}
	}

	/**
	 * Verifica che il nome dell'allegato scelto sia presente nell'elenco degli allegati
	 * 
	 * @param fileName nome del file
	 * @return false se presente
	 */
	private boolean checkAllegato(String fileName) {

		for (Allegato element : allegatiList) {

			if (fileName.equals(element.getNome())) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Rimozione dell'allegato
	 */
	public void removeAllegato() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		for (Allegato element : allegatiList) {

			if (element.getId().equals(allegatoToDelete)) {

				attoRecordServiceManager.deleteFile(element.getId());
				allegatiList.remove(element);
				attoBean.getLastPassaggio().getAula().setAllegatiEsameAula(Clonator.cloneList(allegatiList));
				break;
			}
		}
	}

	/**
	 * Aggiunge il link
	 */
	public void addLink() {

		if (nomeLink != null && !nomeLink.trim().equals("")) {
			if (!checkLinks()) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Attenzione ! Link " + nomeLink + " già presente ", ""));

			} else {
				Link link = new Link();
				link.setDescrizione(nomeLink);
				link.setIndirizzo(urlLink);
				link.setPubblico(pubblico);
				linksList.add(link);

				updateNoteAllegatiHandler();
			}
		}
	}

	/**
	 * Rimozione del link
	 */
	public void removeLink() {

		for (Link element : linksList) {

			if (element.getDescrizione().equals(linkToDelete)) {

				linksList.remove(element);
				break;
			}
		}
	}

	/**
	 * Verifica che il link scelto sia presente nelle descrizioni dei lin correnti
	 * 
	 * @return false se presente
	 */
	private boolean checkLinks() {

		for (Link element : linksList) {

			if (element.getDescrizione().equals(nomeLink)) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Salvataggio delle note degli allegati
	 */
	public void salvaNoteEAllegati() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		this.aulaUser.setLinksEsameAula(linksList);
		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setAula((Aula) aulaUser.clone());

		Target target = new Target();
		target.setPassaggio(attoBean.getLastPassaggio().getNome());

		EsameAula esameAula = new EsameAula();
		esameAula.setTarget(target);
		esameAula.setAtto(atto);
		aulaServiceManager.salvaNoteAllegatiEsameAula(esameAula);

		attoBean.getWorkingAula().setNoteGeneraliEsameAula(aulaUser.getNoteGeneraliEsameAula());
		attoBean.getWorkingAula().setLinksEsameAula(linksList);

		setStatoCommitNoteAllegati(CRLMessage.COMMIT_DONE);

		context.addMessage(null, new FacesMessage("Note e Allegati salvati con successo", ""));

	}

	public Atto getAtto() {
		return atto;
	}

	public void setAtto(Atto atto) {
		this.atto = atto;
	}

	public Date getDataPresaInCarico() {
		return aulaUser.getDataPresaInCaricoEsameAula();
	}

	public void setDataPresaInCarico(Date dataPresaInCarico) {
		this.aulaUser.setDataPresaInCaricoEsameAula(dataPresaInCarico);
	}

	public String getRelazioneScritta() {
		return aulaUser.getRelazioneScritta();
	}

	public void setRelazioneScritta(String relazioneScritta) {
		this.aulaUser.setRelazioneScritta(relazioneScritta);
	}

	public String getEsitoVotazione() {
		return aulaUser.getEsitoVotoAula();
	}

	public void setEsitoVotazione(String esitoVotazione) {
		this.aulaUser.setEsitoVotoAula(esitoVotazione);
	}

	public String getTipologiaVotazione() {
		return aulaUser.getTipologiaVotazione();
	}

	public void setTipologiaVotazione(String tipologiaVotazione) {
		this.aulaUser.setTipologiaVotazione(tipologiaVotazione);
	}

	public Date getDataSedutaVotazione() {
		return aulaUser.getDataSedutaAula();
	}

	public void setDataSedutaVotazione(Date dataSedutaVotazione) {
		this.aulaUser.setDataSedutaAula(dataSedutaVotazione);
	}

	public String getNomeRelatore() {
		return nomeRelatore;
	}

	public void setNomeRelatore(String nomeRelatore) {
		this.nomeRelatore = nomeRelatore;
	}

	public Object getRelatoreToDelete() {
		return relatoreToDelete;
	}

	public void setRelatoreToDelete(Object relatoreToDelete) {
		this.relatoreToDelete = relatoreToDelete;
	}

	public List<Relatore> getRelatoriList() {
		return relatoriList;
	}

	public void setRelatoriList(List<Relatore> relatoriList) {
		this.relatoriList = relatoriList;
	}

	public List<Relatore> getRelatori() {
		return relatori;
	}

	public void setRelatori(List<Relatore> relatori) {
		this.relatori = relatori;
	}

	public Date getDataNominaRelatore() {
		return dataNominaRelatore;
	}

	public void setDataNominaRelatore(Date dataNominaRelatore) {
		this.dataNominaRelatore = dataNominaRelatore;
	}

	public Date getDataUscitaRelatore() {
		return dataUscitaRelatore;
	}

	public void setDataUscitaRelatore(Date dataUscitaRelatore) {
		this.dataUscitaRelatore = dataUscitaRelatore;
	}

	public String getNumeroDcr() {
		return aulaUser.getNumeroDcr();
	}

	public void setNumeroDcr(String numeroDcr) {
		this.aulaUser.setNumeroDcr(numeroDcr);
	}

	public String getNumeroLcr() {
		return aulaUser.getNumeroLcr();
	}

	public void setNumeroLcr(String numeroLcr) {
		this.aulaUser.setNumeroLcr(numeroLcr);
	}

	public boolean isEmendato() {
		return aulaUser.isEmendato();
	}

	public void setEmendato(boolean emendato) {
		this.aulaUser.setEmendato(emendato);
	}

	public String getNoteVotazione() {
		return aulaUser.getNoteVotazione();
	}

	public void setNoteVotazione(String noteVotazione) {
		this.aulaUser.setNoteVotazione(noteVotazione);
	}

	public String getNumeroReg() {
		return this.aulaUser.getNumeroReg();
	}

	public void setNumeroReg(String numeroReg) {
		this.aulaUser.setNumeroReg(numeroReg);
	}

	public List<TestoAtto> getTestiAttoVotatoList() {
		return testiAttoVotatoList;
	}

	public void setTestiAttoVotatoList(List<TestoAtto> testiAttoVotatoList) {
		this.testiAttoVotatoList = testiAttoVotatoList;
	}

	public List<Allegato> getEmendamentiList() {
		return emendamentiList;
	}

	public void setEmendamentiList(List<Allegato> emendamentiList) {
		this.emendamentiList = emendamentiList;
	}

	public Integer getNumEmendPresentatiMaggior() {
		return aulaUser.getNumEmendPresentatiMaggiorEsameAula();
	}

	public void setNumEmendPresentatiMaggior(Integer numEmendPresentatiMaggior) {
		this.aulaUser.setNumEmendPresentatiMaggiorEsameAula(numEmendPresentatiMaggior);
	}

	public Integer getNumEmendPresentatiMinor() {
		return aulaUser.getNumEmendPresentatiMinorEsameAula();
	}

	public void setNumEmendPresentatiMinor(Integer numEmendPresentatiMinor) {
		this.aulaUser.setNumEmendPresentatiMinorEsameAula(numEmendPresentatiMinor);
	}

	public Integer getNumEmendPresentatiGiunta() {
		return aulaUser.getNumEmendPresentatiGiuntaEsameAula();
	}

	public void setNumEmendPresentatiGiunta(Integer numEmendPresentatiGiunta) {
		this.aulaUser.setNumEmendPresentatiGiuntaEsameAula(numEmendPresentatiGiunta);
	}

	public Integer getNumEmendPresentatiMisto() {
		return aulaUser.getNumEmendPresentatiMistoEsameAula();
	}

	public void setNumEmendPresentatiMisto(Integer numEmendPresentatiMisto) {
		this.aulaUser.setNumEmendPresentatiMistoEsameAula(numEmendPresentatiMisto);
	}

	public Integer getNumEmendPresentatiTotale() {
		return numEmendPresentatiTotale;
	}

	public void setNumEmendPresentatiTotale(Integer numEmendPresentatiTotale) {
		this.numEmendPresentatiTotale = numEmendPresentatiTotale;
	}

	public Integer getNumEmendApprovatiMaggior() {
		return aulaUser.getNumEmendApprovatiMaggiorEsameAula();
	}

	public void setNumEmendApprovatiMaggior(Integer numEmendApprovatiMaggior) {
		this.aulaUser.setNumEmendApprovatiMaggiorEsameAula(numEmendApprovatiMaggior);
	}

	public Integer getNumEmendApprovatiMinor() {
		return aulaUser.getNumEmendApprovatiMinorEsameAula();
	}

	public void setNumEmendApprovatiMinor(Integer numEmendApprovatiMinor) {
		this.aulaUser.setNumEmendApprovatiMinorEsameAula(numEmendApprovatiMinor);
	}

	public Integer getNumEmendApprovatiGiunta() {
		return aulaUser.getNumEmendApprovatiGiuntaEsameAula();
	}

	public void setNumEmendApprovatiGiunta(Integer numEmendApprovatiGiunta) {
		this.aulaUser.setNumEmendApprovatiGiuntaEsameAula(numEmendApprovatiGiunta);
	}

	public Integer getNumEmendApprovatiMisto() {
		return aulaUser.getNumEmendApprovatiMistoEsameAula();
	}

	public void setNumEmendApprovatiMisto(Integer numEmendApprovatiMisto) {
		this.aulaUser.setNumEmendApprovatiMistoEsameAula(numEmendApprovatiMisto);
	}

	public Integer getNumEmendApprovatiTotale() {
		return numEmendApprovatiTotale;
	}

	public void setNumEmendApprovatiTotale(Integer numEmendApprovatiTotale) {
		this.numEmendApprovatiTotale = numEmendApprovatiTotale;
	}

	public Integer getNonAmmissibili() {
		return aulaUser.getNonAmmissibiliEsameAula();
	}

	public void setNonAmmissibili(Integer nonAmmissibili) {
		this.aulaUser.setNonAmmissibiliEsameAula(nonAmmissibili);
	}

	public Integer getDecaduti() {
		return aulaUser.getDecadutiEsameAula();
	}

	public void setDecaduti(Integer decaduti) {
		this.aulaUser.setDecadutiEsameAula(decaduti);
	}

	public Integer getRitirati() {
		return aulaUser.getRitiratiEsameAula();
	}

	public void setRitirati(Integer ritirati) {
		this.aulaUser.setRitiratiEsameAula(ritirati);
	}

	public Integer getRespinti() {
		return aulaUser.getRespintiEsameAula();
	}

	public void setRespinti(Integer respinti) {
		this.aulaUser.setRespintiEsameAula(respinti);
	}

	public Integer getTotaleNonApprovati() {
		return totaleNonApprovati;
	}

	public void setTotaleNonApprovati(Integer totaleNonApprovati) {
		this.totaleNonApprovati = totaleNonApprovati;
	}

	public String getNoteEmendamenti() {
		return aulaUser.getNoteEmendamentiEsameAula();
	}

	public void setNoteEmendamenti(String noteEmendamenti) {
		this.aulaUser.setNoteEmendamentiEsameAula(noteEmendamenti);
	}

	public Date getDataSedutaRinvio() {
		return aulaUser.getDataSedutaRinvio();
	}

	public void setDataSedutaRinvio(Date dataSedutaRinvio) {
		this.aulaUser.setDataSedutaRinvio(dataSedutaRinvio);
	}

	public Date getDataTermineMassimo() {
		return aulaUser.getDataTermineMassimo();
	}

	public void setDataTermineMassimo(Date dataTermineMassimo) {
		this.aulaUser.setDataTermineMassimo(dataTermineMassimo);
	}

	public String getMotivazioneRinvio() {
		return aulaUser.getMotivazioneRinvio();
	}

	public void setMotivazioneRinvio(String motivazione) {
		this.aulaUser.setMotivazioneRinvio(motivazione);
	}

	public Date getDataSedutaStralcio() {
		return aulaUser.getDataSedutaStralcio();
	}

	public void setDataSedutaStralcio(Date dataSedutaStralcio) {
		this.aulaUser.setDataSedutaStralcio(dataSedutaStralcio);
	}

	public Date getDataStralcio() {
		return aulaUser.getDataStralcio();
	}

	public void setDataStralcio(Date dataStralcio) {
		this.aulaUser.setDataStralcio(dataStralcio);
	}

	public Date getDataIniziativaStralcio() {
		return aulaUser.getDataIniziativaStralcio();
	}

	public void setDataIniziativaStralcio(Date dataIniziativaStralcio) {
		this.aulaUser.setDataIniziativaStralcio(dataIniziativaStralcio);
	}

	public String getArticoli() {
		return aulaUser.getArticoli();
	}

	public void setArticoli(String articoli) {
		this.aulaUser.setArticoli(articoli);
	}

	public String getNoteStralcio() {
		return aulaUser.getNoteStralcio();
	}

	public void setNoteStralcio(String noteStralcio) {
		this.aulaUser.setNoteStralcio(noteStralcio);
	}

	public String getQuorum() {
		return aulaUser.getQuorumEsameAula();
	}

	public void setQuorum(String quorum) {
		this.aulaUser.setQuorumEsameAula(quorum);
	}

	public String getNoteGenerali() {
		return aulaUser.getNoteGeneraliEsameAula();
	}

	public void setNoteGenerali(String noteGenerali) {
		this.aulaUser.setNoteGeneraliEsameAula(noteGenerali);
	}

	public List<Allegato> getAllegatiList() {
		return allegatiList;
	}

	public void setAllegatiList(List<Allegato> allegatiList) {
		this.allegatiList = allegatiList;
	}

	public List<Link> getLinksList() {
		return linksList;
	}

	public void setLinksList(List<Link> linksList) {
		this.linksList = linksList;
	}

	public String getStatoCommitVotazione() {
		return statoCommitVotazione;
	}

	public void setStatoCommitVotazione(String statoCommitVotazione) {
		this.statoCommitVotazione = statoCommitVotazione;
	}

	public String getStatoCommitEmendamenti() {
		return statoCommitEmendamenti;
	}

	public void setStatoCommitEmendamenti(String statoCommitEmendamenti) {
		this.statoCommitEmendamenti = statoCommitEmendamenti;
	}

	public String getStatoCommitRinvioEsame() {
		return statoCommitRinvioEsame;
	}

	public void setStatoCommitRinvioEsame(String statoCommitRinvioEsame) {
		this.statoCommitRinvioEsame = statoCommitRinvioEsame;
	}

	public String getStatoCommitStralci() {
		return statoCommitStralci;
	}

	public void setStatoCommitStralci(String statoCommitStralci) {
		this.statoCommitStralci = statoCommitStralci;
	}

	public String getStatoCommitNoteAllegati() {
		return statoCommitNoteAllegati;
	}

	public void setStatoCommitNoteAllegati(String statoCommitNoteAllegati) {
		this.statoCommitNoteAllegati = statoCommitNoteAllegati;
	}

	public String getStatoCommitDati() {
		return statoCommitDati;
	}

	public void setStatoCommitDati(String statoCommitDati) {
		this.statoCommitDati = statoCommitDati;
	}

	public String getTestoAttoVotatoToDelete() {
		return testoAttoVotatoToDelete;
	}

	public void setTestoAttoVotatoToDelete(String testoAttoVotatoToDelete) {
		this.testoAttoVotatoToDelete = testoAttoVotatoToDelete;
	}

	public String getTestoAttoToDelete() {
		return testoAttoToDelete;
	}

	public void setTestoAttoToDelete(String testAttoToDelete) {
		this.testoAttoToDelete = testAttoToDelete;
	}

	public String getEmendamentoToDelete() {
		return emendamentoToDelete;
	}

	public void setEmendamentoToDelete(String emendamentoToDelete) {
		this.emendamentoToDelete = emendamentoToDelete;
	}

	public String getAllegatoToDelete() {
		return allegatoToDelete;
	}

	public void setAllegatoToDelete(String allegatoToDelete) {
		this.allegatoToDelete = allegatoToDelete;
	}

	public String getNomeLink() {
		return nomeLink;
	}

	public void setNomeLink(String nomeLink) {
		this.nomeLink = nomeLink;
	}

	public String getLinkToDelete() {
		return linkToDelete;
	}

	public void setLinkToDelete(String linkToDelete) {
		this.linkToDelete = linkToDelete;
	}

	public String getUrlLink() {
		return urlLink;
	}

	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	public boolean isPubblico() {
		return pubblico;
	}

	public void setPubblico(boolean pubblico) {
		this.pubblico = pubblico;
	}

	public boolean isCurrentFilePubblico() {
		return currentFilePubblico;
	}

	public void setCurrentFilePubblico(boolean currentFilePubblico) {
		this.currentFilePubblico = currentFilePubblico;
	}

	public AulaServiceManager getAulaServiceManager() {
		return aulaServiceManager;
	}

	public void setAulaServiceManager(AulaServiceManager aulaServiceManager) {
		this.aulaServiceManager = aulaServiceManager;
	}

	public AttoServiceManager getAttoServiceManager() {
		return attoServiceManager;
	}

	public void setAttoServiceManager(AttoServiceManager attoServiceManager) {
		this.attoServiceManager = attoServiceManager;
	}

	public PersonaleServiceManager getPersonaleServiceManager() {
		return personaleServiceManager;
	}

	public void setPersonaleServiceManager(PersonaleServiceManager personaleServiceManager) {
		this.personaleServiceManager = personaleServiceManager;
	}

	public Aula getAulaUser() {
		return aulaUser;
	}

	public void setAulaUser(Aula aulaUser) {
		this.aulaUser = aulaUser;
	}

	public boolean isRinvioCommBilancio() {
		return this.aulaUser.isRinvioCommBilancio();
	}

	public void setRinvioCommBilancio(boolean rinvioCommBilancio) {
		this.aulaUser.setRinvioCommBilancio(rinvioCommBilancio);
	}

	public Passaggio getPassaggio() {
		return passaggio;
	}

	public void setPassaggio(Passaggio passaggio) {
		this.passaggio = passaggio;
	}

	public String getPassaggioSelected() {
		return passaggioSelected;
	}

	public void setPassaggioSelected(String passaggioSelected) {
		this.passaggioSelected = passaggioSelected;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public CommissioneServiceManager getCommissioneServiceManager() {
		return commissioneServiceManager;
	}

	public void setCommissioneServiceManager(CommissioneServiceManager commissioneServiceManager) {
		this.commissioneServiceManager = commissioneServiceManager;
	}

	public AttoRecordServiceManager getAttoRecordServiceManager() {
		return attoRecordServiceManager;
	}

	public void setAttoRecordServiceManager(AttoRecordServiceManager attoRecordServiceManager) {
		this.attoRecordServiceManager = attoRecordServiceManager;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getOggettoOriginale() {
		return oggettoOriginale;
	}

	public void setOggettoOriginale(String oggettoOriginale) {
		this.oggettoOriginale = oggettoOriginale;
	}

	/**
	 * Salvataggio delle informazioni generali
	 */
	public void salvaInfoGenerali() {
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		boolean original = oggettoOriginale != null && !"".equals(oggettoOriginale);
		boolean oggNew = oggetto != null && !"".equals(oggetto);

		if (original && oggNew) {

			atto.setOggetto(oggetto);
			atto.setOggettoOriginale(oggettoOriginale);
			attoBean.getAtto().setOggetto(oggetto);
			attoBean.getAtto().setOggettoOriginale(oggettoOriginale);

		} else if (original && !oggNew) {

			atto.setOggetto(oggettoOriginale);
			attoBean.getAtto().setOggetto(oggettoOriginale);

		} else {
			atto.setOggetto(oggetto);
			attoBean.getAtto().setOggetto(oggetto);

		}

		attoServiceManager.salvaInfoGeneraliPresentazione(this.atto);

		attoBean.getAtto().setClassificazione(this.atto.getClassificazione());

		attoBean.getAtto().setNumeroRepertorio(atto.getNumeroRepertorio());
		attoBean.getAtto().setDataRepertorio(this.atto.getDataRepertorio());
		attoBean.getAtto().setTipoIniziativa(atto.getTipoIniziativa());
		attoBean.getAtto().setDataIniziativa(atto.getDataIniziativa());
		attoBean.getAtto().setDescrizioneIniziativa(atto.getDescrizioneIniziativa());
		attoBean.getAtto().setNumeroDgr(atto.getNumeroDgr());
		attoBean.getAtto().setDataDgr(atto.getDataDgr());
		attoBean.getAtto().setAssegnazione(atto.getAssegnazione());
		attoBean.getAtto().setRelatori(atto.getRelatori());

		setStatoCommitDati(CRLMessage.COMMIT_DONE);

		context.addMessage(null, new FacesMessage("Informazioni Generali salvate con successo", ""));

	}

	/**
	 * Aggiunta del relatore
	 */
	public void addRelatore() {

		if (nomeRelatore != null && !nomeRelatore.trim().equals("")) {
			if (!checkRelatori()) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Attenzione ! Relatore " + nomeRelatore + " già presente ", ""));

			} else {
				Relatore relatore = new Relatore();
				relatore.setDescrizione(nomeRelatore);
				relatore.setDataNomina(dataNominaRelatore);
				relatore.setDataUscita(dataUscitaRelatore);
				relatoriList.add(relatore);

				updateDatiHandler();
			}
		}
	}

	/**
	 * Verifica che il nome del relatore scelto sia presente nell'elenco dei relatori
	 * 
	 * @return false se presente
	 */
	private boolean checkRelatori() {
		for (Relatore element : relatoriList) {
			if (element.getDescrizione().equals(nomeRelatore)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Rimozione del relatore
	 */
	public void removeRelatore() {

		for (Relatore element : relatoriList) {

			if (element.getDescrizione().equals(relatoreToDelete)) {

				relatoriList.remove(element);
				updateDatiHandler();
				break;
			}
		}
	}

	/**
	 * Conferma del relatore
	 */
	public void confermaRelatori() {

		atto.setRelatori(relatoriList);
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		attoServiceManager.salvaRelatoriAula(atto);

		attoBean.getAtto().setRelatori(Clonator.cloneList(atto.getRelatori()));

		setStatoCommitDati(CRLMessage.COMMIT_UNDONE);
		context.addMessage(null, new FacesMessage("Relatori associati all'atto. ", ""));

	}

}
