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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;

import com.sourcesense.crl.business.model.Abbinamento;
import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.Commissione;
import com.sourcesense.crl.business.model.Componente;
import com.sourcesense.crl.business.model.EsameCommissione;
import com.sourcesense.crl.business.model.GestioneAbbinamento;
import com.sourcesense.crl.business.model.Link;
import com.sourcesense.crl.business.model.Passaggio;
import com.sourcesense.crl.business.model.Relatore;
import com.sourcesense.crl.business.model.StatoAtto;
import com.sourcesense.crl.business.model.Target;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.business.service.AbbinamentoServiceManager;
import com.sourcesense.crl.business.service.AttoRecordServiceManager;
import com.sourcesense.crl.business.service.AttoServiceManager;
import com.sourcesense.crl.business.service.CommissioneServiceManager;
import com.sourcesense.crl.business.service.PersonaleServiceManager;
import com.sourcesense.crl.util.CRLMessage;
import com.sourcesense.crl.util.Clonator;
import com.sourcesense.crl.util.DateUtils;
import com.sourcesense.crl.web.ui.beans.AttoBean;
import com.sourcesense.crl.web.ui.beans.UserBean;

/**
 * Gestisce gli esami delle commissioni
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "esameCommissioniController")
@ViewScoped
public class EsameCommissioniController {

	@ManagedProperty(value = "#{personaleServiceManager}")
	private PersonaleServiceManager personaleServiceManager;

	@ManagedProperty(value = "#{attoServiceManager}")
	private AttoServiceManager attoServiceManager;

	@ManagedProperty(value = "#{commissioneServiceManager}")
	private CommissioneServiceManager commissioneServiceManager;

	@ManagedProperty(value = "#{abbinamentoServiceManager}")
	private AbbinamentoServiceManager abbinamentoServiceManager;

	@ManagedProperty(value = "#{attoRecordServiceManager}")
	private AttoRecordServiceManager attoRecordServiceManager;

	private Atto atto = new Atto();

	private boolean readonly = false;

	private List<Commissione> commissioniList = new ArrayList<Commissione>();
	private Commissione commissioneUser = new Commissione();
	private Passaggio passaggio;
	private String passaggioSelected;
	private List<Relatore> relatori = new ArrayList<Relatore>();
	private List<Relatore> membriComitato = new ArrayList<Relatore>();
	private String relatoreToDelete;
	private Date dataNominaRelatore;
	private Date dataUscitaRelatore;
	private String nomeRelatore;

	private boolean currentFilePubblico = true;

	private boolean currentFilePubblicoOpendata = false;

	private String tipologiaTesto;

	private String testoTabella;

	private Date currentDataSeduta;

	private String componenteToDelete;
	private Date dataNominaComponente;
	private boolean coordinatore;
	private Date dataUscitaComponente;
	private String nomeComponente;

	private Date dataPresaInCarico;
	private String materia;
	private String messaggioGiorniScadenza;
	private String messaggioGiorniScadenzaColor;
	private boolean presenzaComitatoRistretto;
	private Date dataIstituzioneComitato;
	private Date dataFineLavori;

	private List<Relatore> relatoriList = new ArrayList<Relatore>();
	private List<Componente> membriComitatoList = new ArrayList<Componente>();
	private String tipoComitato;
	private String testoComitatoToDelete;

	private List<Abbinamento> abbinamentiList = new ArrayList<Abbinamento>();
	private String abbinamentoToDelete;
	private String idAbbinamentoSelected;
	private Abbinamento abbinamentoSelected;

	private String tipoTesto;
	private Date dataAbbinamento;
	private Date dataDisabbinamento;
	private String noteAbbinamento;
	private String oggettoAttoCorrente;
	private boolean attoProseguente;

	private String esitoVotazione;
	private String quorum;
	private Date dataSedutaRegistrazioneVotazione;
	private Date dataCalendarizzazione;
	private Date dataRis;
	private String numeroRis;

	private List<TestoAtto> testiAttoVotatoList = new ArrayList<TestoAtto>();
	private List<Allegato> testiComitatoRistrettoList = new ArrayList<Allegato>();
	private List<Allegato> emendamentiList = new ArrayList<Allegato>();
	private List<Allegato> testiClausolaList = new ArrayList<Allegato>();
	private List<Allegato> allegatiList = new ArrayList<Allegato>();

	private String testoAttoVotatoToDelete;
	private Date dataSedutaContinuazioneLavori;
	private String motivazioni;
	private Date dataTrasmissione;
	private Date dataRichiestaIscrizione;
	private boolean passaggioDiretto;
	private String emendamentoToDelete;
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
	private Integer numEmendApprovatiCommissione;
	private Integer numEmendPresentatiCommissione;

	private Integer nonAmmissibili;
	private Integer decaduti;
	private Integer ritirati;
	private Integer respinti;
	private Integer totaleNonApprovati = new Integer(0);
	private String noteEmendamenti;
	private Date dataPresaInCaricoProposta;
	private Date dataIntesa;
	private String esitoVotazioneIntesa;
	private String noteClausolaValutativa;

	private String testoClausolaToDelete;
	private String noteGenerali;

	private List<Link> linksList = new ArrayList<Link>();

	private String allegatoToDelete;
	private String linkToDelete;

	private String nomeLink;
	private String urlLink;
	private boolean pubblico;

	private Date dataSedutaStralcio;
	private Date dataIniziativaStralcio;
	private Date dataStralcio;
	private String articoli;
	private String noteStralcio;
	private String quorumStralcio;

	private Date dataAssegnazione;
	private boolean sospensioneFeriale;
	private Date dataInterruzione;
	private Date dataRicezioneIntegrazioni;
	private Date dataScadenza;

	private Date dataDcr;
	private String numeroDcr;

	private List<String> esitiVotazione = new ArrayList<String>();
	private List<String> quorumVotazione = new ArrayList<String>();

	private String statoCommitRelatori = CRLMessage.COMMIT_DONE;
	private String statoCommitComitatoRistretto = CRLMessage.COMMIT_DONE;
	private String statoCommitPresaInCarico = CRLMessage.COMMIT_DONE;
	private String statoCommitFineLavori = CRLMessage.COMMIT_DONE;
	private String statoCommitAbbinamentieDisabbinamenti = CRLMessage.COMMIT_DONE;
	private String statoCommitOggettoAttoCorrente = CRLMessage.COMMIT_DONE;
	private String statoCommitRegistrazioneVotazione = CRLMessage.COMMIT_DONE;
	private String statoCommitContinuazioneLavori = CRLMessage.COMMIT_DONE;
	private String statoCommitTrasmissione = CRLMessage.COMMIT_DONE;
	private String statoCommitEmendamentiClausole = CRLMessage.COMMIT_DONE;
	private String statoCommitNote = CRLMessage.COMMIT_DONE;
	private String statoCommitStralci = CRLMessage.COMMIT_DONE;

	/**
	 * Aggiunge l'atto selezionato, i relatori, i membri del comitato, le
	 * commissioni, i valori della commissione utente, il passaggio e i dati al
	 * contesto web
	 */
	@PostConstruct
	protected void init() {
		setRelatori(personaleServiceManager.getAllRelatori());
		setMembriComitato(personaleServiceManager.getAllMembriComitato());
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));
		UserBean userBean = ((UserBean) context.getExternalContext().getSessionMap().get("userBean"));
		setAtto((Atto) attoBean.getAtto().clone());
		setCommissioniList(Clonator.cloneList(attoBean.getLastPassaggio().getCommissioni()));

		setValoriCommissioneUtente(attoBean, userBean);
		setPassaggioSelected(attoBean.getLastPassaggio().getNome());
		loadData(attoBean.getLastPassaggio(), attoBean);

	}

	/**
	 * Aggiornamento dei valori della commissine utente
	 * 
	 * @param attoBean atto
	 * @param userBean utente
	 */
	private void setValoriCommissioneUtente(AttoBean attoBean, UserBean userBean) {
		Commissione commTemp = findCommissione(userBean.getUser().getSessionGroup().getNome());
		esitiVotazione.clear();
		quorumVotazione.clear();

		if (!commTemp.getRuolo().equals(Commissione.RUOLO_CONSULTIVA)) {

			String tipo = attoBean.getTipoAtto();

			if ("PDL".equals(tipo) || "PDA".equals(tipo) || "PLP".equals(tipo) || "PRE".equals(tipo)
					|| "REF".equals(tipo) || "DOC".equals(tipo)) {

				esitiVotazione.add("Approvato");
				esitiVotazione.add("Respinto");
				esitiVotazione.add("Proposta di non passaggio all'esame");

			} else if ("PAR".equals(tipo)) {

				esitiVotazione.add("Parere favorevole");
				esitiVotazione.add("Parere favorevole con osservazioni");
				esitiVotazione.add("Parere negativo");
				esitiVotazione.add("Parere negativo con osservazioni");
				esitiVotazione.add("Intesa espressa senza condizioni");
				esitiVotazione.add("Intesa espressa con condizioni");
				esitiVotazione.add("Mancata intesa");

			} else if ("INP".equals(tipo)) {

				esitiVotazione.add("Archiviazione");

			} else if ("REL".equals(tipo)) {

				esitiVotazione.add("Presa d'atto");
				esitiVotazione.add("Approvato");

			}

		} else {

			esitiVotazione.add("Parere favorevole");
			esitiVotazione.add("Parere favorevole con osservazioni");
			esitiVotazione.add("Parere negativo");
			esitiVotazione.add("Parere negativo con osservazioni");

		}

		if (commTemp.getRuolo().equals(Commissione.RUOLO_DELIBERANTE)) {

			quorumVotazione.add("Palese per alzata di mano");
			quorumVotazione.add("Palese per appello nominale");
			quorumVotazione.add("Segreta");

			esitiVotazione.clear();
			esitiVotazione.add("Approvato");
			esitiVotazione.add("Approvato non passaggio all'esame");
			esitiVotazione.add("Respinto");

		} else {

			quorumVotazione.add("Maggioranza");
			quorumVotazione.add("Unanimità");

		}

		if (commTemp == null) {

			commTemp = attoBean.getCommissioneReferente();

			if (commTemp == null) {

				commTemp = attoBean.getCommissioneDeliberante();

				if (commTemp == null) {
					commTemp = new Commissione();
				}

			}

		}

		setCommissioneUser(commTemp);

	}

	/**
	 * Verifica se il ruolo dell'utente della commissione può cambiare lo stato.
	 * Deve essere di tipo RUOLO_REFERENTE, RUOLO_REDIGENTE, RUOLO_DELIBERANTE o
	 * RUOLO_COREFERENTE
	 * 
	 * @return true se verificato
	 */
	private boolean canChangeStatoAtto() {

		return (commissioneUser.getRuolo().equals(Commissione.RUOLO_REFERENTE)
				|| commissioneUser.getRuolo().equalsIgnoreCase(Commissione.RUOLO_REDIGENTE)
				|| commissioneUser.getRuolo().equalsIgnoreCase(Commissione.RUOLO_DELIBERANTE)
				|| commissioneUser.getRuolo().equalsIgnoreCase(Commissione.RUOLO_COREFERENTE));

	}

	/**
	 * Esegue il cambio di passaggio
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
		setCommissioniList(Clonator.cloneList(passaggioSelected.getCommissioni()));

		FacesContext context = FacesContext.getCurrentInstance();
		UserBean userBean = ((UserBean) context.getExternalContext().getSessionMap().get("userBean"));

		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));
		Commissione commTemp = findCommissione(userBean.getUser().getSessionGroup().getNome());
		if (commTemp == null) {

			commTemp = attoBean.getCommissioneReferente();

			if (commTemp == null) {

				commTemp = attoBean.getCommissioneDeliberante();

				if (commTemp == null) {
					commTemp = new Commissione();
				}

			}

		}

		setCommissioneUser(commTemp);

		loadData(passaggioSelected, attoBean);
	}

	/**
	 * Ricerca la commissione per nome tra quelle presenti
	 * 
	 * @param nome nome della commissione
	 * @return commissione
	 */
	private Commissione findCommissione(String nome) {

		for (Commissione element : commissioniList) {
			if (element.getDescrizione().equals(nome)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Caricamento dei dati
	 * 
	 * @param passaggioIn passaggio
	 * @param attoBean    atto
	 */
	private void loadData(Passaggio passaggioIn, AttoBean attoBean) {
		setDataPresaInCarico(commissioneUser.getDataPresaInCarico());
		setMateria(commissioneUser.getMateria());
		setDataScadenza(commissioneUser.getDataScadenza());
		setPresenzaComitatoRistretto(commissioneUser.isPresenzaComitatoRistretto());
		setDataIstituzioneComitato(commissioneUser.getDataIstituzioneComitato());
		setDataFineLavori(commissioneUser.getDataFineLavoriComitato());
		setDataTrasmissione(commissioneUser.getDataTrasmissione());
		setDataRichiestaIscrizione(commissioneUser.getDataTrasmissione());
		setPassaggioDiretto(commissioneUser.isPassaggioDirettoInAula());

		totaleEmendApprovati();
		totaleEmendPresentati();
		totaleNonApprovati();

		abbinamentiList = Clonator.cloneList(passaggioIn.getAbbinamenti());
		relatoriList = Clonator.cloneList(commissioneUser.getRelatori());
		membriComitatoList = Clonator.cloneList(commissioneUser.getComitatoRistretto().getComponenti());
		testiComitatoRistrettoList = Clonator.cloneList(commissioneUser.getAllegatiNoteEsameCommissioni());
		testiAttoVotatoList = Clonator.cloneList(commissioneUser.getTestiAttoVotatoEsameCommissioni());
		emendamentiList = Clonator.cloneList(commissioneUser.getEmendamentiEsameCommissioni());
		testiClausolaList = Clonator.cloneList(commissioneUser.getTestiClausola());
		allegatiList = Clonator.cloneList(attoBean.getAllegatiCommissioni());
		linksList = Clonator.cloneList(commissioneUser.getLinksNoteEsameCommissione());

		if (commissioneUser.getDataScadenza() == null && commissioneUser.getDataInterruzione() == null) {

			commissioneUser.setDataScadenza(DateUtils.getDataScadenzaPar(getDataAssegnazione(),
					attoBean.getAtto().isScadenza60gg(), isSospensioneFeriale()));

		}

		confrontaDataScadenza();

	}

	/**
	 * Aggiornamneto dei relatori
	 */
	public void updateRelatoriHandler() {
		setStatoCommitRelatori(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento del comitato ristretto
	 */
	public void updateComitatoRistrettoHandler() {
		setStatoCommitComitatoRistretto(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento della presa in carico
	 */
	public void updatePresaInCaricoHandler() {
		setStatoCommitPresaInCarico(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento della fine lavori
	 */
	public void updateFineLavoriHandler() {
		setStatoCommitFineLavori(CRLMessage.COMMIT_DONE);
	}

	/**
	 * Aggiornamento degli abbinamenti e disabbinamenti
	 */
	public void updateAbbinamentiHandler() {
		setStatoCommitAbbinamentieDisabbinamenti(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento dell'atto corrente
	 */
	public void updateOggettoAttoCorrenteHandler() {
		setStatoCommitOggettoAttoCorrente(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento della registrazione della votazione
	 */
	public void updateRegistrazioneVotazioneHandler() {
		setStatoCommitRegistrazioneVotazione(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento della continuazione lavori
	 */
	public void updateContinuazioneLavoriHandler() {
		setStatoCommitContinuazioneLavori(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento della trasmissione
	 */
	public void updateTrasmissioneHandler() {
		setStatoCommitTrasmissione(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento delle clausole degli emendamenti
	 */
	public void updateEmendamentiClausoleHandler() {
		setStatoCommitEmendamentiClausole(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento delle note
	 */
	public void updateNoteHandler() {
		setStatoCommitNote(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiornamento degli stralci
	 */
	public void updateStralciHandler() {
		setStatoCommitStralci(CRLMessage.COMMIT_UNDONE);
	}

	/**
	 * Aggiunge le notifiche di errore a seconda dell'operazione eseguita
	 */
	public void changeTabHandler() {

		if (statoCommitRelatori.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche ai Relatori non sono state salvate ", ""));
		}

		if (statoCommitComitatoRistretto.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche al Comitato Ristretto non sono state salvate ", ""));
		}

		if (statoCommitPresaInCarico.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche della Presa in Carico non sono state salvate ", ""));
		}

		if (statoCommitFineLavori.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche relative alla Fine dei Lavori non sono state salvate ", ""));
		}

		if (statoCommitAbbinamentieDisabbinamenti.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche di Abbinamenti e Disabbinamenti non sono state salvate ", ""));
		}

		if (statoCommitOggettoAttoCorrente.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche all'Oggetto dell'Atto corrente non sono state salvate ", ""));
		}

		if (statoCommitRegistrazioneVotazione.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche della Registrazione Votazione non sono state salvate ", ""));
		}

		if (statoCommitContinuazioneLavori.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche sulla Continuazione dei lavori in referente non sono state salvate ",
					""));
		}

		if (statoCommitTrasmissione.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche sulla Trasmissione non sono state salvate ", ""));
		}

		if (statoCommitEmendamentiClausole.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche di Emendamenti e Clausole non sono state salvate ", ""));
		}

		if (statoCommitNote.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche alle Note ed Allegati non sono state salvate ", ""));
		}

		if (statoCommitStralci.equals(CRLMessage.COMMIT_UNDONE)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Le modifiche a Stralci non sono state salvate ", ""));
		}

	}

	/**
	 * Aggiunge la data scadenza alla commissione
	 */
	public void scadenzaPar() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (getDataInterruzione() != null && getDataRicezioneIntegrazioni() == null) {

			commissioneUser.setDataScadenza(null);

		} else if (getDataInterruzione() != null && getDataRicezioneIntegrazioni() != null) {

			commissioneUser.setDataScadenza(DateUtils
					.generateDataScadenzaParDgrInterruzione(getDataRicezioneIntegrazioni(), isSospensioneFeriale()));

		} else {

			commissioneUser.setDataScadenza(DateUtils.getDataScadenzaPar(getDataAssegnazione(),
					attoBean.getAtto().isScadenza60gg(), isSospensioneFeriale()));

		}

		confrontaDataScadenza();

		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(getCommissioniList());

		Target target = new Target();
		target.setCommissione(commissioneUser.getDescrizione());
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameCommissione esameCommissione = new EsameCommissione();
		esameCommissione.setAtto(atto);
		esameCommissione.setTarget(target);

		commissioneServiceManager.salvaPresaInCaricoEsameCommissioni(esameCommissione);

		context.addMessage(null, new FacesMessage("Scadenza Parere Aggiornata", ""));

	}

	/**
	 * Presa in carico dell'esame della commissione
	 */
	public void presaInCarico() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		commissioneUser.setDataPresaInCarico(getDataPresaInCarico());
		commissioneUser.setMateria(materia);

		if (commissioneUser.getStato().equals(Commissione.STATO_ASSEGNATO)) {
			commissioneUser.setStato(Commissione.STATO_IN_CARICO);
		}
		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(getCommissioniList());

		if (canChangeStatoAtto() && atto.getStato().equals(StatoAtto.ASSEGNATO_COMMISSIONE)) {
			atto.setStato(StatoAtto.PRESO_CARICO_COMMISSIONE);
		}

		Target target = new Target();
		target.setCommissione(commissioneUser.getDescrizione());
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameCommissione esameCommissione = new EsameCommissione();
		esameCommissione.setAtto(atto);
		esameCommissione.setTarget(target);

		commissioneServiceManager.salvaPresaInCaricoEsameCommissioni(esameCommissione);

		UserBean userBean = ((UserBean) context.getExternalContext().getSessionMap().get("userBean"));

		attoBean.getLastPassaggio().setCommissioni(commissioniList);

		if (canChangeStatoAtto() && attoBean.getStato().equals(StatoAtto.ASSEGNATO_COMMISSIONE)) {
			attoBean.setStato(StatoAtto.PRESO_CARICO_COMMISSIONE);
		}

		String numeroAtto = attoBean.getNumeroAtto();
		setStatoCommitPresaInCarico(CRLMessage.COMMIT_DONE);
		context.addMessage(null, new FacesMessage(
				"Atto " + numeroAtto + " preso in carico con successo dall' utente " + userBean.getUser().getUsername(),
				""));
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

				updateRelatoriHandler();
			}
		}
	}

	/**
	 * Rimozione del relatore
	 */
	public void removeRelatore() {

		for (Relatore element : relatoriList) {

			if (element.getDescrizione().equals(relatoreToDelete)) {

				relatoriList.remove(element);
				updateRelatoriHandler();
				break;
			}
		}
	}

	/**
	 * Verifica che il nome del relatore sia presente nell'elenco dei relatori
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
	 * Conferma del relatore
	 */
	public void confermaRelatori() {

		commissioneUser.setRelatori(relatoriList);

		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(commissioniList);

		if (checkStatiRelatori()) {

			commissioneUser.setStato(Commissione.STATO_NOMINATO_RELATORE);
		} else {

			commissioneUser.setStato(Commissione.STATO_IN_CARICO);
		}

		if (canChangeStatoAtto()) {

			if (checkStatiRelatori()) {
				atto.setStato(StatoAtto.NOMINATO_RELATORE);
			} else {
				atto.setStato(StatoAtto.PRESO_CARICO_COMMISSIONE);
			}

		}
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		Target target = new Target();
		target.setCommissione(commissioneUser.getDescrizione());
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameCommissione esameCommissione = new EsameCommissione();
		esameCommissione.setAtto(atto);
		esameCommissione.setTarget(target);

		commissioneServiceManager.salvaRelatoriEsameCommissioni(esameCommissione);

		attoBean.getLastPassaggio().setCommissioni(Clonator.cloneList(commissioniList));

		if (canChangeStatoAtto()) {
			if (checkStatiRelatori()) {
				attoBean.setStato(StatoAtto.NOMINATO_RELATORE);
			} else {
				attoBean.setStato(StatoAtto.PRESO_CARICO_COMMISSIONE);

			}
		}

		setStatoCommitRelatori(CRLMessage.COMMIT_DONE);
		context.addMessage(null, new FacesMessage("Relatori salvati con successo", ""));
	}

	/**
	 * Verifica che tutti i relatori abbiano la data di uscita
	 * 
	 * @return true se rimane almeno un relatore in aula
	 */
	private boolean checkStatiRelatori() {

		int annullo = 0;

		for (Relatore relatore : relatoriList) {

			if (relatore.getDataUscita() != null) {
				annullo++;

			}
		}

		return annullo < relatoriList.size();

	}

	/**
	 * Verifica che tutti i relatori abbiano la data di uscita
	 * 
	 * @return true se rimane almeno un relatore in aula
	 */
	private boolean isNominatoRelatore() {

		int usciti = 0;

		for (Relatore element : relatoriList) {

			if (element.getDataUscita() != null) {

				usciti++;
			}
		}
		return (usciti < relatoriList.size());
	}

	/**
	 * Aggiunge il componente ai membri del comitato
	 */
	public void addComponente() {

		if (nomeComponente != null && !nomeComponente.trim().equals("")) {
			if (!checkComponenti()) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Attenzione ! Componente " + nomeComponente + " già presente ", ""));

			} else if (coordinatore && checkCoordinatore()) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Attenzione ! Coordinatore già selezionato ", ""));

			} else {
				Componente componente = new Componente();
				componente.setDescrizione(nomeComponente);
				componente.setDataNomina(dataNominaComponente);
				componente.setDataUscita(dataUscitaComponente);
				componente.setCoordinatore(coordinatore);
				membriComitatoList.add(componente);

				updateComitatoRistrettoHandler();
			}
		}
	}

	/**
	 * Rimuove il componente dai membri del comitato
	 */
	public void removeComponente() {

		for (Componente element : membriComitatoList) {

			if (element.getDescrizione().equals(componenteToDelete)) {

				membriComitatoList.remove(element);
				updateComitatoRistrettoHandler();
				break;
			}
		}
	}

	/**
	 * Verifica che il nome del componente scelto sia presente nell'elenco dei
	 * membri del comitato
	 * 
	 * @return false se presente
	 */
	private boolean checkComponenti() {

		for (Componente element : membriComitatoList) {

			if (element.getDescrizione().equals(nomeComponente)) {

				return false;
			}
		}
		return true;
	}

	/**
	 * Verifica che tra i membri del comitato ci sia almeno un coordinatore
	 * 
	 * @return true se esiste almeno un coordinatore
	 */
	private boolean checkCoordinatore() {

		for (Componente element : membriComitatoList) {

			if (element.isCoordinatore()) {

				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica che tra i membri del comitato almeno uno non abbia data di uscita
	 * 
	 * @param attoBean
	 * @return true se almeno un membro è attivo
	 */
	private boolean checkOneMembroAttivo(AttoBean attoBean) {

		for (Componente element : membriComitatoList) {

			if (element.getDataUscita() == null) {

				return true;
			}
		}
		/*
		 * if (!ritorno && attoBean.getWorkingCommissione(commissioneUser.getDescrizione
		 * ()).getComitatoRistretto().getComponenti().size() ==
		 * membriComitatoList.size() && membriComitatoList.size() > 0) { ritorno = true;
		 * }
		 */

		return false;
	}

	/**
	 * Conferma del comitato ristretto
	 */
	public void confermaComitatoRistretto() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		boolean isOneMembroAttivo = checkOneMembroAttivo(attoBean);

		commissioneUser.getComitatoRistretto().setComponenti(membriComitatoList);
		commissioneUser.setDataIstituzioneComitato(getDataIstituzioneComitato());

		Target target = new Target();
		target.setCommissione(commissioneUser.getDescrizione());
		target.setPassaggio(attoBean.getLastPassaggio().getNome());

		boolean statoChange = atto.getStato().equals(StatoAtto.ASSEGNATO_COMMISSIONE)
				|| atto.getStato().equals(StatoAtto.PRESO_CARICO_COMMISSIONE)
				|| atto.getStato().equals(StatoAtto.NOMINATO_RELATORE);

		if (canChangeStatoAtto() && isOneMembroAttivo) {

			if (statoChange) {
				atto.setStato(StatoAtto.LAVORI_COMITATO_RISTRETTO);
			}

			commissioneUser.setStato(Commissione.STATO_COMITATO_RISTRETTO);

		}

		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(commissioniList);

		EsameCommissione esameCommissione = new EsameCommissione();
		esameCommissione.setAtto(atto);
		esameCommissione.setTarget(target);
		commissioneServiceManager.salvaComitatoRistrettoEsameCommissioni(esameCommissione);

		if (canChangeStatoAtto() && isNominatoRelatore()) {
			if (statoChange) {
				attoBean.setStato(StatoAtto.LAVORI_COMITATO_RISTRETTO);
			}
		}
		attoBean.getLastPassaggio().setCommissioni(Clonator.cloneList(commissioniList));

		setStatoCommitComitatoRistretto(CRLMessage.COMMIT_DONE);
		context.addMessage(null, new FacesMessage("Comitato ristretto salvato con successo", ""));

	}

	/**
	 * Upload del testo del comitato ristretto
	 * 
	 * @param event evento di upload del file
	 */
	public void uploadTestoComitatoRistretto(FileUploadEvent event) {

		String fileName = event.getFile().getFileName();
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (!checkTestoComitato(fileName)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Il file " + fileName + " è già stato allegato ", ""));

		} else {

			Allegato allegatoRet = new Allegato();
			allegatoRet.setNome(event.getFile().getFileName());
			allegatoRet.setPubblico(currentFilePubblico);
			allegatoRet.setDataSeduta(currentDataSeduta);
			allegatoRet.setCommissione(commissioneUser.getDescrizione());
			allegatoRet.setPassaggio(attoBean.getLastPassaggio().getNome());

			try {

				Allegato allegatoAlf = commissioneServiceManager.uploadTestoComitatoRistretto(((AttoBean) FacesContext
						.getCurrentInstance().getExternalContext().getSessionMap().get("attoBean")).getAtto(),
						event.getFile().getInputstream(), allegatoRet);

				allegatoRet.setId(allegatoAlf.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}

			setCurrentDataSeduta(null);
			setCurrentFilePubblico(false);

			attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).getAllegatiNoteEsameCommissioni()
					.add(allegatoRet);
			testiComitatoRistrettoList.add(allegatoRet);
		}
	}

	/**
	 * Non implementato. Ritorna sempre ok
	 * 
	 * @param fileName nome del file
	 * @return true
	 */
	private boolean checkTestoComitato(String fileName) {

		return true;
	}

	/**
	 * Rimozione del testo del comitato
	 */
	public void removeTestoComitato() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		for (Allegato element : testiComitatoRistrettoList) {

			if (element.getId().equals(testoComitatoToDelete)) {
				attoRecordServiceManager.deleteFile(element.getId());
				testiComitatoRistrettoList.remove(element);
				attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
						.setAllegatiNoteEsameCommissioni(Clonator.cloneList(testiComitatoRistrettoList));
				break;
			}
		}
	}

	/**
	 * Aggiornamento dell'allegato
	 * 
	 * @param event evento di modifica della riga
	 */
	public void updateAllegato(RowEditEvent event) {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		Allegato allegato = (Allegato) event.getObject();
		if (Allegato.TESTO_ESAME_COMMISSIONE_CLAUSOLA.equals(allegato.getTipologia())) {

			allegato.setTipoAllegato(Allegato.TESTO_ESAME_COMMISSIONE_CLAUSOLA);
			attoRecordServiceManager.updateAllegatoCommissione(allegato);
			attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
					.setTestiClausola(Clonator.cloneList(testiClausolaList));
		} else if (Allegato.TESTO_ESAME_COMMISSIONE_COMITATO.equals(allegato.getTipologia())) {

			allegato.setTipoAllegato(Allegato.TESTO_ESAME_COMMISSIONE_COMITATO);
			attoRecordServiceManager.updateAllegato(allegato);
			attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
					.setAllegatiNoteEsameCommissioni(Clonator.cloneList(testiComitatoRistrettoList));
		} else if (Allegato.TIPO_ESAME_COMMISSIONE_EMENDAMENTO.equals(allegato.getTipologia())) {

			allegato.setTipoAllegato(Allegato.TIPO_ESAME_COMMISSIONE_EMENDAMENTO);
			attoRecordServiceManager.updateAllegatoCommissione(allegato);
			attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
					.setEmendamentiEsameCommissioni(Clonator.cloneList(emendamentiList));
		} else if (Allegato.TIPO_ESAME_COMMISSIONE_ALLEGATO.equals(allegato.getTipologia())) {

			allegato.setTipoAllegato(Allegato.TIPO_ESAME_COMMISSIONE_ALLEGATO);
			attoRecordServiceManager.updateAllegato(allegato);
			attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
					.setAllegati(Clonator.cloneList(allegatiList));

		}

	}

	/**
	 * Conferma della fine dei lavori
	 */
	public void confermaFineLavori() {
		commissioneUser.setDataFineLavoriComitato(getDataFineLavori());
		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(getCommissioniList());
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		Target target = new Target();
		target.setCommissione(commissioneUser.getDescrizione());
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameCommissione esameCommissione = new EsameCommissione();
		esameCommissione.setAtto(atto);
		esameCommissione.setTarget(target);
		commissioneServiceManager.salvaFineLavoriEsameCommissioni(esameCommissione);

		attoBean.getLastPassaggio().setCommissioni(Clonator.cloneList(getCommissioniList()));

		setStatoCommitPresaInCarico(CRLMessage.COMMIT_DONE);
		context.addMessage(null, new FacesMessage("Relatori e Comitati Ristretti salvati con successo", ""));
	}

	/**
	 * Aggiunta dell'abbinamento
	 * 
	 * @param idAbbinamento id abbinamento
	 * @param tipoAtto      tipo di atto
	 */
	public void addAbbinamento(String idAbbinamento, String tipoAtto) {

		if (!idAbbinamento.trim().equals("")) {

			FacesContext context = FacesContext.getCurrentInstance();
			AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

			if (!checkAbbinamenti(idAbbinamento)) {
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Atto già abbinato ", ""));

			} else if (attoBean.getAtto().getId().equals(idAbbinamento)) {

				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Operazione non possibile ", ""));

			} else {

				Atto attoDaAbbinare = attoServiceManager.findById(idAbbinamento);

				Abbinamento abbinamento = new Abbinamento();
				abbinamento.setIdAtto(atto.getId());
				abbinamento.setIdAttoAbbinato(idAbbinamento);
				abbinamento.setAbbinato(true);
				abbinamento.setNumeroAttoAbbinato(attoDaAbbinare.getNumeroAtto());
				abbinamento.setTipoAttoAbbinato(attoDaAbbinare.getTipoAtto());

				abbinamentiList.add(abbinamento);
				setIdAbbinamentoSelected(idAbbinamento);
				showAbbinamentoDetail();
				updateAbbinamentiHandler();

			}
		}
	}

	/**
	 * Rimozione dell'abbinamento
	 */
	public void removeAbbinamento() {

		for (Abbinamento element : abbinamentiList) {

			if (element.getIdAttoAbbinato().equals(abbinamentoToDelete)) {

				FacesContext context = FacesContext.getCurrentInstance();
				AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));
				commissioneServiceManager.removeAbbinamento(atto.getId(), element.getIdAttoAbbinato(),
						attoBean.getLastPassaggio().getNome());
				abbinamentiList.remove(element);

				for (Abbinamento abbinamento : attoBean.getAbbinamenti()) {

					if (abbinamento.getIdAttoAbbinato().equals(abbinamentoToDelete)) {

						attoBean.getAbbinamenti().remove(abbinamento);
						break;
					}

				}

				updateAbbinamentiHandler();
				element.setAbbinato(false);
				context.addMessage(null, new FacesMessage("Abbinamento cancellato con successo", ""));
				break;
			}
		}
	}

	/**
	 * Verifica che la'bbinamento scelto sia presente nell'elenco degli abbinamenti
	 * 
	 * @param idAbbinamento id abbinamento
	 * @return false se presente
	 */
	private boolean checkAbbinamenti(String idAbbinamento) {

		for (Abbinamento element : abbinamentiList) {

			if (element.getIdAttoAbbinato().equals(idAbbinamento)) {

				return false;
			}
		}
		return true;
	}

	/**
	 * Mostra il dettaglio dell'abbinamento nella pagina web
	 */
	public void showAbbinamentoDetail() {

		setAbbinamentoSelected(findAbbinamento(idAbbinamentoSelected));

		if (abbinamentoSelected != null) {
			Atto attoDaAbbinare = attoServiceManager.findById(abbinamentoSelected.getIdAttoAbbinato());

			setTipoTesto(abbinamentoSelected.getTipoTesto());
			setDataAbbinamento(abbinamentoSelected.getDataAbbinamento());
			setDataDisabbinamento(abbinamentoSelected.getDataDisabbinamento());
			setNoteAbbinamento(abbinamentoSelected.getNote());
		}

		else {
			setTipoTesto("");
			setDataAbbinamento(null);
			setDataDisabbinamento(null);
			setNoteAbbinamento("");
		}
	}

	/**
	 * Ricerca l'abbinamento per id
	 * 
	 * @param id id dell'abbinamento
	 * @return abbinamento
	 */
	private Abbinamento findAbbinamento(String id) {

		for (Abbinamento element : abbinamentiList) {

			if (element.getIdAttoAbbinato().equals(id)) {

				return element;

			}

		}

		return null;
	}

	/**
	 * Salvataggio dell'abbinamento
	 */
	public void salvaAbbinamentoDisabbinamento() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (dataAbbinamento == null && dataDisabbinamento != null) {

			setStatoCommitAbbinamentieDisabbinamenti(CRLMessage.COMMIT_DONE);
			context.addMessage(null, new FacesMessage("Impossibile disabbinare un atto non ancora abbinato", ""));

		}

		else if (dataAbbinamento != null && dataDisabbinamento != null) {

			abbinamentoSelected.setTipoTesto(tipoTesto);
			abbinamentoSelected.setDataAbbinamento(dataAbbinamento);
			abbinamentoSelected.setDataDisabbinamento(dataDisabbinamento);
			abbinamentoSelected.setNote(noteAbbinamento);

			GestioneAbbinamento gestioneAbbinamento = new GestioneAbbinamento();
			Target target = new Target();
			target.setPassaggio(attoBean.getLastPassaggio().getNome());
			gestioneAbbinamento.setAbbinamento(abbinamentoSelected);
			gestioneAbbinamento.setTarget(target);
			abbinamentoServiceManager.salvaAbbinamento(gestioneAbbinamento);

			mergeAbbinamento(abbinamentoSelected, attoBean.getAbbinamenti());

			setStatoCommitAbbinamentieDisabbinamenti(CRLMessage.COMMIT_DONE);
			context.addMessage(null, new FacesMessage("Disabbinamento salvato con successo", ""));

		}

		else if (dataAbbinamento != null && dataDisabbinamento == null) {

			abbinamentoSelected.setTipoTesto(tipoTesto);
			abbinamentoSelected.setDataAbbinamento(dataAbbinamento);
			abbinamentoSelected.setNote(noteAbbinamento);
			abbinamentoSelected.setDataDisabbinamento(null);

			GestioneAbbinamento gestioneAbbinamento = new GestioneAbbinamento();
			Target target = new Target();
			target.setPassaggio(attoBean.getLastPassaggio().getNome());
			gestioneAbbinamento.setAbbinamento(abbinamentoSelected);
			gestioneAbbinamento.setTarget(target);
			abbinamentoServiceManager.salvaAbbinamento(gestioneAbbinamento);

			mergeAbbinamento(abbinamentoSelected, attoBean.getLastPassaggio().getAbbinamenti());

			setStatoCommitAbbinamentieDisabbinamenti(CRLMessage.COMMIT_DONE);
			context.addMessage(null, new FacesMessage("Abbinamento salvato con successo", ""));

		}

	}

	/**
	 * Aggiornamento dell'abbinamento
	 * 
	 * @param abbinamento        abbinamento
	 * @param abbinamentiSession sessione dell'abbinamento
	 */
	private void mergeAbbinamento(Abbinamento abbinamento, List<Abbinamento> abbinamentiSession) {

		for (Abbinamento element : abbinamentiSession) {

			if (element.getIdAttoAbbinato().equals(abbinamento.getIdAttoAbbinato())) {

				element.setTipoTesto(abbinamento.getTipoTesto());
				element.setDataAbbinamento(abbinamento.getDataAbbinamento());
				element.setDataDisabbinamento(abbinamento.getDataDisabbinamento());
				element.setNote(abbinamento.getNote());

				return;
			}
		}

		abbinamentiSession.add(abbinamento);
	}

	/**
	 * Salvataggio dell'oggetto
	 */
	public void salvaOggetto() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (this.oggettoAttoCorrente != null && !"".equals(this.oggettoAttoCorrente)) {
			this.atto.setOggettoOriginale(atto.getOggetto());
			this.atto.setOggetto(oggettoAttoCorrente);
		}
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		attoServiceManager.salvaInfoGeneraliPresentazione(atto);
		attoBean.getAtto().setOggetto(atto.getOggetto());
		attoBean.getAtto().setOggettoOriginale(atto.getOggettoOriginale());
		attoBean.getAtto().setAttoProseguente(atto.isAttoProseguente());

		setStatoCommitOggettoAttoCorrente(CRLMessage.COMMIT_DONE);

		context.addMessage(null, new FacesMessage("Oggetto Atto corrente salvato con successo", ""));

	}

	/**
	 * Ritorna un messaggio differente a seconda della data di registrazione della
	 * votazione e della data di scadenza
	 */
	public void confrontaDataScadenza() {

		setMessaggioGiorniScadenza("");

		if (getDataScadenza() != null && getDataSedutaRegistrazioneVotazione() == null) {

			long scadenzaLong = getDataScadenza().getTime();
			long todayLong = (new Date()).getTime();
			long msDifferenza = (scadenzaLong - todayLong);
			int giorniDifferenza = (int) (msDifferenza / 86400000);
			if (giorniDifferenza == 0) {

				setMessaggioGiorniScadenza("Attenzione! La scadenza è prevista per oggi");
				setMessaggioGiorniScadenzaColor("red");
			} else if (giorniDifferenza > 1) {

				setMessaggioGiorniScadenza("Mancano " + giorniDifferenza + " giorni alla scadenza.");
				setMessaggioGiorniScadenzaColor("green");
			} else {

				setMessaggioGiorniScadenza("Attenzione! Ritardo di " + (-giorniDifferenza) + " giorni.");
				setMessaggioGiorniScadenzaColor("red");
			}

		} else if (getDataScadenza() != null && getDataSedutaRegistrazioneVotazione() != null) {

			long todayLong = getDataSedutaRegistrazioneVotazione().getTime();
			long scadenzaLong = getDataScadenza().getTime();
			long msDifferenza = (scadenzaLong - todayLong);
			int giorniDifferenza = (int) (msDifferenza / 86400000);

			if (giorniDifferenza < 0) {
				setMessaggioGiorniScadenza("Ritardo maturato : " + (-giorniDifferenza) + " giorni.");
				setMessaggioGiorniScadenzaColor("red");

			}
		}

	}

	/**
	 * Registrazione della votazione
	 * 
	 * @return "pretty:Chiusura_Iter" se l'utente ha il ruolo di RUOLO_DELIBERANTE e
	 *         l'atto è di tipo PDA, altrimenti null
	 */
	public String registraVotazione() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		String stato = atto.getStato();
		if (checkAnnullaCommissione()) {

			commissioneUser.setStato(Commissione.STATO_IN_CARICO);
			commissioneUser.setDataSedutaCommissione(null);

			if (checkLavoriComitatoRistretto() && canChangeStatoAtto()) {

				stato = StatoAtto.LAVORI_COMITATO_RISTRETTO;

			} else if (checkNominatoRelatore() && canChangeStatoAtto()) {
				stato = StatoAtto.NOMINATO_RELATORE;

			} else if (canChangeStatoAtto()) {
				stato = StatoAtto.PRESO_CARICO_COMMISSIONE;

			}
		} else {
			commissioneUser.setStato(Commissione.STATO_VOTATO);

			if (canChangeStatoAtto()) {
				stato = StatoAtto.VOTATO_COMMISSIONE;
			}
		}

		atto.setStato(stato);
		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(getCommissioniList());
		Target target = new Target();
		target.setCommissione(commissioneUser.getDescrizione());
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameCommissione esameCommissione = new EsameCommissione();
		esameCommissione.setAtto(atto);
		esameCommissione.setTarget(target);
		commissioneServiceManager.salvaVotazioneEsameCommissioni(esameCommissione);

		if (canChangeStatoAtto()) {
			attoBean.getAtto().setStato(stato);
		}

		attoBean.getLastPassaggio().setCommissioni(commissioniList);
		setStatoCommitRegistrazioneVotazione(CRLMessage.COMMIT_DONE);
		confrontaDataScadenza();
		context.addMessage(null, new FacesMessage("Registrazione Votazione salvata con successo", ""));
		if (Commissione.RUOLO_DELIBERANTE.equals(commissioneUser.getRuolo()) && atto.getTipoAtto().equals("PDA")) {
			return "pretty:Chiusura_Iter";
		} else {
			return null;
		}

	}

	/**
	 * Se non esiste il quorum, nessun esito di votazione e nessuna data di
	 * registrazione della votazione, la commissione viene annullata
	 * 
	 * @return true nel caso di annullamento
	 */
	private boolean checkAnnullaCommissione() {

		if (("".equals(getQuorum()) || getQuorum() == null) && ("".equals(getEsitoVotazione()) || getQuorum() == null)
				&& getDataSedutaRegistrazioneVotazione() == null) {
			return true;
		}

		return false;

	}

	/**
	 * Verifica che almeno un componente dei membri del comitato non sia uscito
	 * dall'aula
	 * 
	 * @return true se almeno un membro è presente in aula
	 */
	private boolean checkLavoriComitatoRistretto() {

		for (Componente componente : this.getMembriComitatoList()) {

			if (componente.getDataUscita() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica che almeno un relatore non sia uscito dall'aula
	 * 
	 * @return true se almeno un membro è presente in aula
	 */
	private boolean checkNominatoRelatore() {
		for (Relatore relatore : getRelatoriList()) {

			if (relatore.getDataUscita() == null) {
				return true;
			}
		}
		return false;

	}

	/**
	 * Upload del testo dell'atto votato
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

			TestoAtto testoVotatoRet = new TestoAtto();
			testoVotatoRet.setNome(event.getFile().getFileName());
			testoVotatoRet.setPubblico(currentFilePubblico);
			testoVotatoRet.setPubblicoOpendata(currentFilePubblicoOpendata);
			testoVotatoRet.setCommissione(commissioneUser.getDescrizione());
			testoVotatoRet.setPassaggio(attoBean.getLastPassaggio().getNome());
			testoVotatoRet.setTipologiaTesto(tipologiaTesto);

			try {

				testoVotatoRet = commissioneServiceManager.uploadTestoAttoVotatoEsameCommissioni(
						((AttoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
								.get("attoBean")).getAtto(),
						event.getFile().getInputstream(), testoVotatoRet);

			} catch (IOException e) {
				e.printStackTrace();
			}

			setCurrentFilePubblico(false);

			attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).getTestiAttoVotatoEsameCommissioni()
					.add(testoVotatoRet);

			testiAttoVotatoList.add(testoVotatoRet);
		}
	}

	public void handleChangeTipologiaTesto(AjaxBehaviorEvent event) {

        UIComponent component = event.getComponent(); // The component that triggered the event
        if (component instanceof org.primefaces.component.selectonemenu.SelectOneMenu) {
            SelectOneMenu selectOneMenu = (SelectOneMenu) component; 
            String selectedTipologiaTesto = (String) selectOneMenu.getValue(); // The newly selected value

			setTipologiaTesto(selectedTipologiaTesto);

		}
    }

	/**
	 * Verifica che il testo scelto sia presente nell'elenco dei testi degli atti
	 * votati
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
				attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
						.setTestiAttoVotatoEsameCommissioni(Clonator.cloneList(testiAttoVotatoList));

				break;
			}
		}
	}

	/**
	 * Aggiornamento del testo dell'atto votato
	 * 
	 * @param event evento di modifica della riga
	 */
	public void updateTestoAttoVotato(RowEditEvent event) {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		TestoAtto allegato = (TestoAtto) event.getObject();

		if (allegato.isPubblicoOpendata()) {
			
			for (TestoAtto testoAtto : testiAttoVotatoList) {
				if (!(testoAtto.getNome().equals(allegato.getNome())) && testoAtto.getTipologiaTesto().equals(allegato.getTipologiaTesto())) {
					testoAtto.setPubblicoOpendata(false);
				}
			}

		}

		allegato.setTipoAllegato(allegato.getTipologiaTesto());
		attoRecordServiceManager.updateTestoAtto(allegato);
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setTestiAttoVotatoEsameCommissioni(Clonator.cloneList(testiAttoVotatoList));

	}

	/**
	 * Assegnazione del ruolo di referente
	 */
	public void cambiaRuoloInReferente() {

		FacesContext context = FacesContext.getCurrentInstance();

		if (getDataSedutaContinuazioneLavori() != null) {
			AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

			commissioneUser.setRuolo(Commissione.RUOLO_REFERENTE);

			Target target = new Target();
			target.setCommissione(commissioneUser.getDescrizione());
			target.setPassaggio(attoBean.getLastPassaggio().getNome());

			EsameCommissione esameCommissione = new EsameCommissione();

			for (Commissione commissioneRec : atto.getPassaggi().get(atto.getPassaggi().size() - 1).getCommissioni()) {

				if (commissioneRec.getDescrizione().equals(commissioneUser.getDescrizione())) {

					commissioneRec.setRuolo(Commissione.RUOLO_REFERENTE);
					commissioneRec.setMotivazioniContinuazioneInReferente(getMotivazioni());
					commissioneRec.setDataSedutaContinuazioneInReferente(getDataSedutaContinuazioneLavori());
				}
			}

			atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(getCommissioniList());
			esameCommissione.setAtto(atto);
			esameCommissione.setTarget(target);

			commissioneServiceManager.salvaCambiaRuoloInReferente(esameCommissione);

			attoBean.getLastPassaggio().setCommissioni(commissioniList);
			setStatoCommitContinuazioneLavori(CRLMessage.COMMIT_DONE);
			context.addMessage(null,
					new FacesMessage("Continuazione dei lavori in referente salvata con successo", ""));
		} else {

			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Inserire la Data seduta", ""));

		}
	}

	/**
	 * Conferma della trasmissione
	 * 
	 * @return "pretty:Chiusura_Iter" se lo stato può essere cambiato altrimenti ""
	 */
	public String confermaTrasmissione() {

		String risultato = "";

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (isPassaggioDiretto() && ((commissioneUser.getEsitoVotazione() != null
				&& !commissioneUser.getEsitoVotazione().trim().equals(""))
				|| (commissioneUser.getQuorumEsameCommissioni() != null
						&& !commissioneUser.getQuorumEsameCommissioni().trim().equals(""))
				|| (commissioneUser.getDataSedutaCommissione() != null))) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Presenza di estremi della votazione (art. 23 comma 9)", ""));

		} else {

			if (atto.getTipoAtto().equals("INP") || atto.getTipoAtto().equals("PAR") || atto.getTipoAtto().equals("REL")
					|| (atto.getTipoAtto().equals("DOC") && !atto.isIterAula())
					|| commissioneUser.getRuolo().equals(Commissione.RUOLO_DELIBERANTE)) {

				if (canChangeStatoAtto()) {
					risultato = "pretty:Chiusura_Iter";
				}

			}
			if (canChangeStatoAtto() && risultato.equals("")) {
				attoBean.setStato(StatoAtto.TRASMESSO_AULA);
				atto.setStato(StatoAtto.TRASMESSO_AULA);
				commissioneUser.setStato(Commissione.STATO_TRASMESSO);

			} else if (!canChangeStatoAtto()) {

				commissioneUser.setStato(Commissione.STATO_TRASMESSO);

			}

			commissioneUser.setDataTrasmissione(getDataTrasmissione());
			commissioneUser.setPassaggioDirettoInAula(isPassaggioDiretto());
			commissioneUser.setDataRichiestaIscrizioneAula(getDataRichiestaIscrizione());

			atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(getCommissioniList());

			attoBean.getLastPassaggio().setCommissioni(Clonator.cloneList(getCommissioniList()));

			Target target = new Target();
			target.setCommissione(commissioneUser.getDescrizione());
			target.setPassaggio(attoBean.getLastPassaggio().getNome());
			EsameCommissione esameCommissione = new EsameCommissione();
			esameCommissione.setAtto(atto);
			esameCommissione.setTarget(target);

			commissioneServiceManager.salvaTrasmissione(esameCommissione);

			setStatoCommitTrasmissione(CRLMessage.COMMIT_DONE);
			context.addMessage(null, new FacesMessage("Trasmissione salvata con successo", ""));

		}

		return risultato;
	}

	/**
	 * Ritorna il messaggio a seconda del tipo di atto. Se è un parere oppure se il
	 * ruolo della commissione è RUOLO_CONSULTIVA ritorna "Testo del parere
	 * espresso". Altrimenti "Testo dell'atto votato"
	 * 
	 * @return il testo del parere espresso dell'atto votato
	 */
	public String testoParereEspressoAttoVotato() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = (AttoBean) context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{attoBean}", AttoBean.class)
				.getValue(context.getELContext());

		if (attoBean.getTipoAtto().equals("PAR"))

		{
			return "Testo del parere espresso";
		}

		else if (Commissione.RUOLO_CONSULTIVA.equals(commissioneUser.getRuolo()))

		{
			return "Testo del parere espresso";
		} else {
			return "Testo dell'atto votato";
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
		if (getNumEmendPresentatiCommissione() != null) {
			numEmendPresentatiTotale += getNumEmendPresentatiCommissione();
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
		if (getNumEmendApprovatiCommissione() != null) {
			numEmendApprovatiTotale += getNumEmendApprovatiCommissione();
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

			Allegato allegatoRet = new Allegato();

			try {

				allegatoRet.setNome(event.getFile().getFileName());
				allegatoRet.setPubblico(currentFilePubblico);
				allegatoRet.setCommissione(commissioneUser.getDescrizione());
				allegatoRet.setPassaggio(attoBean.getLastPassaggio().getNome());

				allegatoRet = commissioneServiceManager.uploadEmendamentoEsameCommissioni(((AttoBean) FacesContext
						.getCurrentInstance().getExternalContext().getSessionMap().get("attoBean")).getAtto(),
						event.getFile().getInputstream(), allegatoRet);

			} catch (IOException e) {
				e.printStackTrace();
			}

			setCurrentFilePubblico(false);

			attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).getEmendamentiEsameCommissioni()
					.add(allegatoRet);

			emendamentiList.add(allegatoRet);
		}
	}

	/**
	 * Non implementato. Ritorna sempre ok
	 * 
	 * @param fileName
	 * @return true
	 */
	private boolean checkEmendamenti(String fileName) {

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
				attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
						.setEmendamentiEsameCommissioni(Clonator.cloneList(emendamentiList));

				break;
			}
		}
	}

	/**
	 * Aggiornamento del testo della clausola
	 * 
	 * @param event evento di upload del file
	 */
	public void uploadTestoClausola(FileUploadEvent event) {
		String fileName = event.getFile().getFileName();
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		if (!checkTestiClausola(fileName)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Attenzione ! Il file " + fileName + " è già stato allegato ", ""));
		} else {

			Allegato allegatoRet = new Allegato();

			try {

				allegatoRet.setNome(event.getFile().getFileName());
				allegatoRet.setPubblico(currentFilePubblico);
				allegatoRet.setDataSeduta(currentDataSeduta);
				allegatoRet.setCommissione(commissioneUser.getDescrizione());
				allegatoRet.setPassaggio(attoBean.getLastPassaggio().getNome());

				allegatoRet = commissioneServiceManager.uploadTestoClausolaEsameCommissioni(((AttoBean) FacesContext
						.getCurrentInstance().getExternalContext().getSessionMap().get("attoBean")).getAtto(),
						event.getFile().getInputstream(), allegatoRet);

			} catch (IOException e) {
				e.printStackTrace();
			}

			setCurrentFilePubblico(false);

			attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).getTestiClausola().add(allegatoRet);

			testiClausolaList.add(allegatoRet);
		}
	}

	/**
	 * Verifica se il testo selezionato è presente nell'elenco dei testi
	 * 
	 * @param fileName nome del file
	 * @return false se presente
	 */
	private boolean checkTestiClausola(String fileName) {

		for (Allegato element : testiClausolaList) {

			if (fileName.equals(element.getNome())) {

				return false;
			}

		}

		return true;
	}

	/**
	 * Rimozione del testo della clausola
	 */
	public void removeTestoClausola() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		for (Allegato element : testiClausolaList) {

			if (element.getId().equals(testoClausolaToDelete)) {

				attoRecordServiceManager.deleteFile(element.getId());
				testiClausolaList.remove(element);
				attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
						.setTestiClausola(Clonator.cloneList(testiClausolaList));
				break;
			}
		}
	}

	/**
	 * Salvataggio della clausola dell'emendamento
	 */
	public void salvaEmendamentiClausole() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(getCommissioniList());

		Target target = new Target();
		target.setCommissione(commissioneUser.getDescrizione());
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameCommissione esameCommissione = new EsameCommissione();
		esameCommissione.setAtto(atto);
		esameCommissione.setTarget(target);
		commissioneServiceManager.salvaEmendamentiClausoleEsameCommissioni(esameCommissione);

		attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).setNumEmendPresentatiMaggiorEsameCommissioni(
				commissioneUser.getNumEmendPresentatiMaggiorEsameCommissioni());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).setNumEmendPresentatiMinorEsameCommissioni(
				commissioneUser.getNumEmendPresentatiMinorEsameCommissioni());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).setNumEmendPresentatiGiuntaEsameCommissioni(
				commissioneUser.getNumEmendPresentatiGiuntaEsameCommissioni());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).setNumEmendPresentatiMistoEsameCommissioni(
				commissioneUser.getNumEmendPresentatiMistoEsameCommissioni());

		attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).setNumEmendApprovatiMaggiorEsameCommissioni(
				commissioneUser.getNumEmendApprovatiMaggiorEsameCommissioni());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setNumEmendApprovatiMinorEsameCommissioni(commissioneUser.getNumEmendApprovatiMinorEsameCommissioni());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).setNumEmendApprovatiGiuntaEsameCommissioni(
				commissioneUser.getNumEmendApprovatiGiuntaEsameCommissioni());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setNumEmendApprovatiMistoEsameCommissioni(commissioneUser.getNumEmendApprovatiMistoEsameCommissioni());

		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setNonAmmissibiliEsameCommissioni(commissioneUser.getNonAmmissibiliEsameCommissioni());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setDecadutiEsameCommissioni(commissioneUser.getDecadutiEsameCommissioni());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setRitiratiEsameCommissioni(commissioneUser.getRitiratiEsameCommissioni());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setRespintiEsameCommissioni(commissioneUser.getRespintiEsameCommissioni());

		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setNoteEmendamentiEsameCommissioni(commissioneUser.getNoteEmendamentiEsameCommissioni());

		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setDataPresaInCaricoProposta(commissioneUser.getDataPresaInCaricoProposta());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).setDataIntesa(commissioneUser.getDataIntesa());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setEsitoVotazioneIntesa(commissioneUser.getEsitoVotazioneIntesa());
		attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
				.setNoteClausolaValutativa(commissioneUser.getNoteClausolaValutativa());

		setStatoCommitEmendamentiClausole(CRLMessage.COMMIT_DONE);

		if (numEmendPresentatiTotale - numEmendApprovatiTotale != totaleNonApprovati) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Emendamenti e Clausole salvati con successo : Attenzione dati incoerenti !", ""));

		} else {

			context.addMessage(null, new FacesMessage("Emendamenti e Clausole salvati con successo", ""));
		}
	}

	/**
	 * Aggiornamento dell'allegato
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

			try {

				allegatoRet.setNome(event.getFile().getFileName());
				allegatoRet.setPubblico(currentFilePubblico);
				allegatoRet.setDataSeduta(currentDataSeduta);
				allegatoRet.setCommissione(commissioneUser.getDescrizione());
				allegatoRet.setPassaggio(attoBean.getLastPassaggio().getNome());

				allegatoRet = commissioneServiceManager.uploadAllegatoNoteAllegatiEsameCommissioni(
						((AttoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
								.get("attoBean")).getAtto(),
						event.getFile().getInputstream(), allegatoRet);

			} catch (IOException e) {
				e.printStackTrace();
			}

			setCurrentFilePubblico(false);
			attoBean.getWorkingCommissione(commissioneUser.getDescrizione()).getAllegati().add(allegatoRet);
			allegatiList.add(allegatoRet);
		}
	}

	/**
	 * Verifica che l'allegato scelto sia presente nell'elenco degli allegati
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
				attoBean.getWorkingCommissione(commissioneUser.getDescrizione())
						.setAllegati(Clonator.cloneList(allegatiList));
				break;
			}
		}
	}

	/**
	 * Aggiunta del link
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

				updateNoteHandler();
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
	 * Verifica che il link scelto non sia presente nell'elenco dei link
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
	 * Salvataggio delle note
	 */
	public void salvaNoteEAllegati() {

		this.commissioneUser.setLinksNoteEsameCommissione(linksList);
		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));
		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(getCommissioniList());
		Target target = new Target();
		target.setCommissione(commissioneUser.getDescrizione());
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameCommissione esameCommissione = new EsameCommissione();
		esameCommissione.setAtto(atto);
		esameCommissione.setTarget(target);

		commissioneServiceManager.salvaNoteAllegatiEsameCommissioni(esameCommissione);

		attoBean.getLastPassaggio().setCommissioni(Clonator.cloneList(getCommissioniList()));

		setStatoCommitNote(CRLMessage.COMMIT_DONE);

		context.addMessage(null, new FacesMessage("Note e Allegati salvati con successo", ""));

	}

	/**
	 * Salvataggio degli stralci
	 */
	public void salvaStralci() {

		FacesContext context = FacesContext.getCurrentInstance();
		AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));
		atto.getPassaggi().get(atto.getPassaggi().size() - 1).setCommissioni(getCommissioniList());
		Target target = new Target();
		target.setCommissione(commissioneUser.getDescrizione());
		target.setPassaggio(attoBean.getLastPassaggio().getNome());
		EsameCommissione esameCommissione = new EsameCommissione();
		esameCommissione.setAtto(atto);
		esameCommissione.setTarget(target);

		commissioneServiceManager.salvaStralci(esameCommissione);

		attoBean.getLastPassaggio().setCommissioni(Clonator.cloneList(getCommissioniList()));

		setStatoCommitStralci(CRLMessage.COMMIT_DONE);
		context.addMessage(null, new FacesMessage("Stralci salvati con successo", ""));
	}

	public Atto getAtto() {
		return atto;
	}

	public void setAtto(Atto atto) {
		this.atto = atto;
	}

	public Date getDataPresaInCarico() {
		return dataPresaInCarico;
	}

	public void setDataPresaInCarico(Date dataPresaInCarico) {
		this.dataPresaInCarico = dataPresaInCarico;
	}

	public String getMateria() {
		return this.materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public Date getDataScadenza() {
		return commissioneUser.getDataScadenza();
	}

	public void setDataScadenza(Date dataScadenza) {
		this.commissioneUser.setDataScadenza(dataScadenza);
	}

	public Date getDataFineLavori() {
		return dataFineLavori;
	}

	public void setDataFineLavori(Date dataFineLavori) {
		this.dataFineLavori = dataFineLavori;
	}

	public String getTipoTesto() {
		return tipoTesto;
	}

	public void setTipoTesto(String tipoTesto) {
		this.tipoTesto = tipoTesto;
	}

	public Date getDataAbbinamento() {
		return dataAbbinamento;
	}

	public void setDataAbbinamento(Date dataAbbinamento) {
		this.dataAbbinamento = dataAbbinamento;
	}

	public Date getDataDisabbinamento() {
		return dataDisabbinamento;
	}

	public void setDataDisabbinamento(Date dataDisabbinamento) {
		this.dataDisabbinamento = dataDisabbinamento;
	}

	public String getNoteAbbinamento() {
		return noteAbbinamento;
	}

	public void setNoteAbbinamento(String noteAbbinamento) {
		this.noteAbbinamento = noteAbbinamento;
	}

	public String getOggettoAttoCorrente() {

		return (atto.getOggettoOriginale() != null && !"".equals(atto.getOggettoOriginale())) ? atto.getOggetto() : "";
	}

	public void setOggettoAttoCorrente(String oggettoAttoCorrente) {

		this.oggettoAttoCorrente = oggettoAttoCorrente;

	}

	public boolean isAttoProseguente() {
		return this.atto.isAttoProseguente();
	}

	public void setAttoProseguente(boolean attoProseguente) {
		this.atto.setAttoProseguente(attoProseguente);
	}

	public String getEsitoVotazione() {
		return commissioneUser.getEsitoVotazione();
	}

	public void setEsitoVotazione(String esitoVotazione) {
		this.commissioneUser.setEsitoVotazione(esitoVotazione);
	}

	public String getQuorum() {
		return commissioneUser.getQuorumEsameCommissioni();
	}

	public void setQuorum(String quorum) {
		this.commissioneUser.setQuorumEsameCommissioni(quorum);
	}

	public Date getDataSedutaRegistrazioneVotazione() {
		return commissioneUser.getDataSedutaCommissione();
	}

	public void setDataSedutaRegistrazioneVotazione(Date dataSedutaRegistrazioneVotazione) {
		this.commissioneUser.setDataSedutaCommissione(dataSedutaRegistrazioneVotazione);
	}

	public Date getDataCalendarizzazione() {
		return this.commissioneUser.getDataCalendarizzazione();
	}

	public void setDataCalendarizzazione(Date dataCalendarizzazione) {
		this.commissioneUser.setDataCalendarizzazione(dataCalendarizzazione);

	}

	public Date getDataRis() {
		return this.commissioneUser.getDataRis();
	}

	public void setDataRis(Date dataRis) {
		this.commissioneUser.setDataRis(dataRis);
	}

	public String getNumeroRis() {
		return this.commissioneUser.getNumeroRis();
	}

	public void setNumeroRis(String numeroRis) {
		this.commissioneUser.setNumeroRis(numeroRis);
	}

	public Date getDataSedutaContinuazioneLavori() {
		return commissioneUser.getDataSedutaContinuazioneInReferente();
	}

	public void setDataSedutaContinuazioneLavori(Date dataSedutaContinuazioneLavori) {
		this.commissioneUser.setDataSedutaContinuazioneInReferente(dataSedutaContinuazioneLavori);
	}

	public String getMotivazioni() {
		return commissioneUser.getMotivazioniContinuazioneInReferente();
	}

	public void setMotivazioni(String motivazioni) {
		this.commissioneUser.setMotivazioniContinuazioneInReferente(motivazioni);
	}

	public Date getDataTrasmissione() {
		return dataTrasmissione;
	}

	public void setDataTrasmissione(Date dataTrasmissione) {
		this.dataTrasmissione = dataTrasmissione;
	}

	public Date getDataRichiestaIscrizione() {
		return dataRichiestaIscrizione;
	}

	public void setDataRichiestaIscrizione(Date dataRichiestaIscrizione) {
		this.dataRichiestaIscrizione = dataRichiestaIscrizione;
	}

	public boolean isPassaggioDiretto() {
		return passaggioDiretto;
	}

	public void setPassaggioDiretto(boolean passaggioDiretto) {
		this.passaggioDiretto = passaggioDiretto;
	}

	public Integer getNumEmendPresentatiMaggior() {
		return commissioneUser.getNumEmendPresentatiMaggiorEsameCommissioni();
	}

	public void setNumEmendPresentatiMaggior(Integer numEmendPresentatiMaggior) {
		this.commissioneUser.setNumEmendPresentatiMaggiorEsameCommissioni(numEmendPresentatiMaggior);
	}

	public Integer getNumEmendPresentatiMinor() {
		return commissioneUser.getNumEmendPresentatiMinorEsameCommissioni();
	}

	public void setNumEmendPresentatiMinor(Integer numEmendPresentatiMinor) {
		this.commissioneUser.setNumEmendPresentatiMinorEsameCommissioni(numEmendPresentatiMinor);
	}

	public Integer getNumEmendPresentatiGiunta() {
		return commissioneUser.getNumEmendPresentatiGiuntaEsameCommissioni();
	}

	public void setNumEmendPresentatiGiunta(Integer numEmendPresentatiGiunta) {
		this.commissioneUser.setNumEmendPresentatiGiuntaEsameCommissioni(numEmendPresentatiGiunta);
	}

	public Integer getNumEmendPresentatiMisto() {
		return commissioneUser.getNumEmendPresentatiMistoEsameCommissioni();
	}

	public void setNumEmendPresentatiMisto(Integer numEmendPresentatiMisto) {
		this.commissioneUser.setNumEmendPresentatiMistoEsameCommissioni(numEmendPresentatiMisto);
	}

	public Integer getNumEmendPresentatiTotale() {
		return numEmendPresentatiTotale;
	}

	public void setNumEmendPresentatiTotale(Integer numEmendPresentatiTotale) {
		this.numEmendPresentatiTotale = numEmendPresentatiTotale;
	}

	public Integer getNumEmendApprovatiMaggior() {
		return commissioneUser.getNumEmendApprovatiMaggiorEsameCommissioni();
	}

	public void setNumEmendApprovatiMaggior(Integer numEmendApprovatiMaggior) {
		this.commissioneUser.setNumEmendApprovatiMaggiorEsameCommissioni(numEmendApprovatiMaggior);
	}

	public Integer getNumEmendApprovatiMinor() {
		return commissioneUser.getNumEmendApprovatiMinorEsameCommissioni();
	}

	public void setNumEmendApprovatiMinor(Integer numEmendApprovatiMinor) {
		this.commissioneUser.setNumEmendApprovatiMinorEsameCommissioni(numEmendApprovatiMinor);
	}

	public Integer getNumEmendApprovatiGiunta() {
		return commissioneUser.getNumEmendApprovatiGiuntaEsameCommissioni();
	}

	public void setNumEmendApprovatiGiunta(Integer numEmendApprovatiGiunta) {
		this.commissioneUser.setNumEmendApprovatiGiuntaEsameCommissioni(numEmendApprovatiGiunta);
	}

	public Integer getNumEmendApprovatiMisto() {
		return commissioneUser.getNumEmendApprovatiMistoEsameCommissioni();
	}

	public void setNumEmendApprovatiMisto(Integer numEmendApprovatiMisto) {
		this.commissioneUser.setNumEmendApprovatiMistoEsameCommissioni(numEmendApprovatiMisto);
	}

	public Integer getNumEmendApprovatiTotale() {
		return numEmendApprovatiTotale;
	}

	public void setNumEmendApprovatiTotale(Integer numEmendApprovatiTotale) {
		this.numEmendApprovatiTotale = numEmendApprovatiTotale;
	}

	public Integer getDecaduti() {
		return commissioneUser.getDecadutiEsameCommissioni();
	}

	public void setDecaduti(Integer decaduti) {
		this.commissioneUser.setDecadutiEsameCommissioni(decaduti);
	}

	public Integer getRitirati() {
		return commissioneUser.getRitiratiEsameCommissioni();
	}

	public void setRitirati(Integer ritirati) {
		this.commissioneUser.setRitiratiEsameCommissioni(ritirati);
	}

	public Integer getRespinti() {
		return commissioneUser.getRespintiEsameCommissioni();
	}

	public void setRespinti(Integer respinti) {
		this.commissioneUser.setRespintiEsameCommissioni(respinti);
	}

	public Integer getTotaleNonApprovati() {
		return totaleNonApprovati;
	}

	public void setTotaleNonApprovati(Integer totaleNonApprovati) {
		this.totaleNonApprovati = totaleNonApprovati;
	}

	public String getNoteEmendamenti() {
		return commissioneUser.getNoteEmendamentiEsameCommissioni();
	}

	public void setNoteEmendamenti(String noteEmendamenti) {
		this.commissioneUser.setNoteEmendamentiEsameCommissioni(noteEmendamenti);
	}

	public Date getDataPresaInCaricoProposta() {
		return commissioneUser.getDataPresaInCaricoProposta();
	}

	public void setDataPresaInCaricoProposta(Date dataPresaInCaricoProposta) {
		this.commissioneUser.setDataPresaInCaricoProposta(dataPresaInCaricoProposta);
	}

	public Date getDataIntesa() {
		return commissioneUser.getDataIntesa();
	}

	public void setDataIntesa(Date dataIntesa) {
		this.commissioneUser.setDataIntesa(dataIntesa);
	}

	public Date getDataDcr() {
		return this.commissioneUser.getDataDcr();
	}

	public void setDataDcr(Date dataDcr) {
		this.commissioneUser.setDataDcr(dataDcr);
	}

	public String getNumeroDcr() {
		return this.commissioneUser.getNumeroDcr();
	}

	public void setNumeroDcr(String numeroDcr) {
		this.commissioneUser.setNumeroDcr(numeroDcr);
	}

	public String getEsitoVotazioneIntesa() {
		return commissioneUser.getEsitoVotazioneIntesa();
	}

	public void setEsitoVotazioneIntesa(String esitoVotazioneIntesa) {
		this.commissioneUser.setEsitoVotazioneIntesa(esitoVotazioneIntesa);
	}

	public String getNoteClausolaValutativa() {
		return commissioneUser.getNoteClausolaValutativa();
	}

	public void setNoteClausolaValutativa(String noteClausolaValutativa) {
		this.commissioneUser.setNoteClausolaValutativa(noteClausolaValutativa);
	}

	public String getNoteGenerali() {
		return commissioneUser.getNoteGeneraliEsameCommissione();
	}

	public void setNoteGenerali(String noteGenerali) {
		this.commissioneUser.setNoteGeneraliEsameCommissione(noteGenerali);
	}

	/*
	 * public Map<String, String> getRelatori() { return relatori; }
	 * 
	 * public void setRelatori(Map<String, String> relatori) { this.relatori =
	 * relatori; }
	 * 
	 * public Map<String, String> getMembriComitato() { return membriComitato; }
	 * 
	 * public void setMembriComitato(Map<String, String> membriComitato) {
	 * this.membriComitato = membriComitato; }
	 */

	public List<Relatore> getRelatoriList() {
		return relatoriList;
	}

	public List<Relatore> getRelatori() {
		return relatori;
	}

	public void setRelatori(List<Relatore> relatori) {
		this.relatori = relatori;
	}

	public List<Relatore> getMembriComitato() {
		return membriComitato;
	}

	public void setMembriComitato(List<Relatore> membriComitato) {
		this.membriComitato = membriComitato;
	}

	public void setRelatoriList(List<Relatore> relatoriList) {
		this.relatoriList = relatoriList;
	}

	public List<Componente> getMembriComitatoList() {
		return membriComitatoList;
	}

	public void setMembriComitatoList(List<Componente> membriComitatoList) {
		this.membriComitatoList = membriComitatoList;
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

	public PersonaleServiceManager getPersonaleServiceManager() {
		return personaleServiceManager;
	}

	public void setPersonaleServiceManager(PersonaleServiceManager personaleServiceManager) {
		this.personaleServiceManager = personaleServiceManager;
	}

	public String getStatoCommitPresaInCarico() {
		return statoCommitPresaInCarico;
	}

	public void setStatoCommitPresaInCarico(String statoCommitPresaInCarico) {
		this.statoCommitPresaInCarico = statoCommitPresaInCarico;
	}

	public String getStatoCommitEmendamentiClausole() {
		return statoCommitEmendamentiClausole;
	}

	public void setStatoCommitEmendamentiClausole(String statoCommitEmendamentiClausole) {
		this.statoCommitEmendamentiClausole = statoCommitEmendamentiClausole;
	}

	public String getStatoCommitNote() {
		return statoCommitNote;
	}

	public void setStatoCommitNote(String statoCommitNote) {
		this.statoCommitNote = statoCommitNote;
	}

	public AttoServiceManager getAttoServiceManager() {
		return attoServiceManager;
	}

	public void setAttoServiceManager(AttoServiceManager attoServiceManager) {
		this.attoServiceManager = attoServiceManager;
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

	public String getLinkToDelete() {
		return linkToDelete;
	}

	public void setLinkToDelete(String linkToDelete) {
		this.linkToDelete = linkToDelete;
	}

	public String getTestoAttoVotatoToDelete() {
		return testoAttoVotatoToDelete;
	}

	public void setTestoAttoVotatoToDelete(String testoAttoVotatoToDelete) {
		this.testoAttoVotatoToDelete = testoAttoVotatoToDelete;
	}

	public String getEmendamentoToDelete() {
		return emendamentoToDelete;
	}

	public void setEmendamentoToDelete(String emendamentoToDelete) {
		this.emendamentoToDelete = emendamentoToDelete;
	}

	public String getTestoClausolaToDelete() {
		return testoClausolaToDelete;
	}

	public void setTestoClausolaToDelete(String testoClausolaToDelete) {
		this.testoClausolaToDelete = testoClausolaToDelete;
	}

	public String getRelatoreToDelete() {
		return relatoreToDelete;
	}

	public void setRelatoreToDelete(String relatoreToDelete) {
		this.relatoreToDelete = relatoreToDelete;
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

	public String getNomeRelatore() {
		return nomeRelatore;
	}

	public void setNomeRelatore(String nomeRelatore) {
		this.nomeRelatore = nomeRelatore;
	}

	public List<Allegato> getTestiComitatoRistrettoList() {
		return testiComitatoRistrettoList;
	}

	public void setTestiComitatoRistrettoList(List<Allegato> testiComitatoRistrettoList) {
		this.testiComitatoRistrettoList = testiComitatoRistrettoList;
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

	public List<Allegato> getTestiClausolaList() {
		return testiClausolaList;
	}

	public void setTestiClausolaList(List<Allegato> testiClausolaList) {
		this.testiClausolaList = testiClausolaList;
	}

	public String getComponenteToDelete() {
		return componenteToDelete;
	}

	public void setComponenteToDelete(String componenteToDelete) {
		this.componenteToDelete = componenteToDelete;
	}

	public Date getDataNominaComponente() {
		return dataNominaComponente;
	}

	public void setDataNominaComponente(Date dataNominaComponente) {
		this.dataNominaComponente = dataNominaComponente;
	}

	public boolean isCoordinatore() {
		return coordinatore;
	}

	public void setCoordinatore(boolean coordinatore) {
		this.coordinatore = coordinatore;
	}

	public Date getDataUscitaComponente() {
		return dataUscitaComponente;
	}

	public void setDataUscitaComponente(Date dataUscitaComponente) {
		this.dataUscitaComponente = dataUscitaComponente;
	}

	public String getNomeComponente() {
		return nomeComponente;
	}

	public void setNomeComponente(String nomeComponente) {
		this.nomeComponente = nomeComponente;
	}

	public boolean isPresenzaComitatoRistretto() {
		return presenzaComitatoRistretto;
	}

	public void setPresenzaComitatoRistretto(boolean presenzaComitatoRistretto) {
		this.presenzaComitatoRistretto = presenzaComitatoRistretto;
	}

	public Date getDataIstituzioneComitato() {
		return dataIstituzioneComitato;
	}

	public void setDataIstituzioneComitato(Date dataIstituzioneComitato) {
		this.dataIstituzioneComitato = dataIstituzioneComitato;
	}

	public List<Abbinamento> getAbbinamentiList() {
		return abbinamentiList;
	}

	public void setAbbinamentiList(List<Abbinamento> abbinamentiList) {
		this.abbinamentiList = abbinamentiList;
	}

	public Integer getNonAmmissibili() {
		return commissioneUser.getNonAmmissibiliEsameCommissioni();
	}

	public void setNonAmmissibili(Integer nonAmmissibili) {
		this.commissioneUser.setNonAmmissibiliEsameCommissioni(nonAmmissibili);
	}

	public String getAbbinamentoToDelete() {
		return abbinamentoToDelete;
	}

	public void setAbbinamentoToDelete(String abbinamentoToDelete) {
		this.abbinamentoToDelete = abbinamentoToDelete;
	}

	public String getIdAbbinamentoSelected() {
		return idAbbinamentoSelected;
	}

	public void setIdAbbinamentoSelected(String idAbbinamentoSelected) {
		this.idAbbinamentoSelected = idAbbinamentoSelected;
	}

	public Abbinamento getAbbinamentoSelected() {
		return abbinamentoSelected;
	}

	public void setAbbinamentoSelected(Abbinamento abbinamentoSelected) {
		this.abbinamentoSelected = abbinamentoSelected;
	}

	public String getStatoCommitRelatori() {
		return statoCommitRelatori;
	}

	public void setStatoCommitRelatori(String statoCommitRelatori) {
		this.statoCommitRelatori = statoCommitRelatori;
	}

	public String getStatoCommitComitatoRistretto() {
		return statoCommitComitatoRistretto;
	}

	public void setStatoCommitComitatoRistretto(String statoCommitComitatoRistretto) {
		this.statoCommitComitatoRistretto = statoCommitComitatoRistretto;
	}

	public String getStatoCommitRegistrazioneVotazione() {
		return statoCommitRegistrazioneVotazione;
	}

	public void setStatoCommitRegistrazioneVotazione(String statoCommitRegistrazioneVotazione) {
		this.statoCommitRegistrazioneVotazione = statoCommitRegistrazioneVotazione;
	}

	public String getStatoCommitContinuazioneLavori() {
		return statoCommitContinuazioneLavori;
	}

	public void setStatoCommitContinuazioneLavori(String statoCommitContinuazioneLavori) {
		this.statoCommitContinuazioneLavori = statoCommitContinuazioneLavori;
	}

	public String getStatoCommitTrasmissione() {
		return statoCommitTrasmissione;
	}

	public void setStatoCommitTrasmissione(String statoCommitTrasmissione) {
		this.statoCommitTrasmissione = statoCommitTrasmissione;
	}

	public String getMessaggioGiorniScadenza() {
		return messaggioGiorniScadenza;
	}

	public void setMessaggioGiorniScadenza(String messaggioGiorniScadenza) {
		this.messaggioGiorniScadenza = messaggioGiorniScadenza;
	}

	public String getTestoComitatoToDelete() {
		return testoComitatoToDelete;
	}

	public void setTestoComitatoToDelete(String testoComitatoToDelete) {
		this.testoComitatoToDelete = testoComitatoToDelete;
	}

	public String getStatoCommitAbbinamentieDisabbinamenti() {
		return statoCommitAbbinamentieDisabbinamenti;
	}

	public void setStatoCommitAbbinamentieDisabbinamenti(String statoCommitAbbinamentieDisabbinamenti) {
		this.statoCommitAbbinamentieDisabbinamenti = statoCommitAbbinamentieDisabbinamenti;
	}

	public String getStatoCommitOggettoAttoCorrente() {
		return statoCommitOggettoAttoCorrente;
	}

	public void setStatoCommitOggettoAttoCorrente(String statoCommitOggettoAttoCorrente) {
		this.statoCommitOggettoAttoCorrente = statoCommitOggettoAttoCorrente;
	}

	public Commissione getCommissioneUser() {
		return commissioneUser;
	}

	public void setCommissioneUser(Commissione commissioneUser) {
		this.commissioneUser = commissioneUser;
	}

	public List<Commissione> getCommissioniList() {
		return commissioniList;
	}

	public void setCommissioniList(List<Commissione> commissioniList) {
		this.commissioniList = commissioniList;
	}

	public CommissioneServiceManager getCommissioneServiceManager() {
		return commissioneServiceManager;
	}

	public void setCommissioneServiceManager(CommissioneServiceManager commissioneServiceManager) {
		this.commissioneServiceManager = commissioneServiceManager;
	}

	public String getStatoCommitFineLavori() {
		return statoCommitFineLavori;
	}

	public void setStatoCommitFineLavori(String statoCommitFineLavori) {
		this.statoCommitFineLavori = statoCommitFineLavori;
	}

	public AbbinamentoServiceManager getAbbinamentoServiceManager() {
		return abbinamentoServiceManager;
	}

	public void setAbbinamentoServiceManager(AbbinamentoServiceManager abbinamentoServiceManager) {
		this.abbinamentoServiceManager = abbinamentoServiceManager;
	}

	public Date getCurrentDataSeduta() {
		return currentDataSeduta;
	}

	public void setCurrentDataSeduta(Date currentDataSeduta) {
		this.currentDataSeduta = currentDataSeduta;
	}

	public Passaggio getPassaggio() {
		return passaggio;
	}

	public void setPassaggio(Passaggio passaggio) {
		this.passaggio = passaggio;
	}

	public boolean isCurrentFilePubblico() {
		return currentFilePubblico;
	}

	public void setCurrentFilePubblico(boolean currentFilePubblico) {
		this.currentFilePubblico = currentFilePubblico;
	}

	public String getTipologiaTesto() {
		return tipologiaTesto;
	}

	public void setTipologiaTesto(String tipologiaTesto) {
		this.tipologiaTesto = tipologiaTesto;
	}

	public boolean isCurrentFilePubblicoOpendata() {
		return currentFilePubblicoOpendata;
	}

	public void setCurrentFilePubblicoOpendata(boolean currentFilePubblicoOpendata) {
		this.currentFilePubblicoOpendata = currentFilePubblicoOpendata;
	}

	public Date getDataSedutaStralcio() {
		return this.commissioneUser.getDataSedutaStralcio();
	}

	public void setDataSedutaStralcio(Date dataSedutaStralcio) {
		this.commissioneUser.setDataSedutaStralcio(dataSedutaStralcio);
	}

	public Date getDataIniziativaStralcio() {
		return this.commissioneUser.getDataIniziativaStralcio();
	}

	public void setDataIniziativaStralcio(Date dataIniziativaStralcio) {
		this.commissioneUser.setDataIniziativaStralcio(dataIniziativaStralcio);
	}

	public Date getDataStralcio() {
		return this.commissioneUser.getDataStralcio();
	}

	public void setDataStralcio(Date dataStralcio) {
		this.commissioneUser.setDataStralcio(dataStralcio);
	}

	public String getArticoli() {
		return this.commissioneUser.getArticoli();
	}

	public void setArticoli(String articoli) {
		this.commissioneUser.setArticoli(articoli);
	}

	public String getNoteStralcio() {
		return this.commissioneUser.getNoteStralcio();
	}

	public void setNoteStralcio(String noteStralcio) {
		this.commissioneUser.setNoteStralcio(noteStralcio);
	}

	public String getQuorumStralcio() {
		return this.commissioneUser.getQuorumStralcio();
	}

	public void setQuorumStralcio(String quorumStralcio) {
		this.commissioneUser.setQuorumStralcio(quorumStralcio);
	}

	public String getTipoComitato() {
		return this.commissioneUser.getComitatoRistretto().getTipologia();
	}

	public void setTipoComitato(String tipoComitato) {
		this.commissioneUser.getComitatoRistretto().setTipologia(tipoComitato);
	}

	public Date getDataAssegnazione() {
		return this.commissioneUser.getDataAssegnazione();
	}

	public void setDataAssegnazione(Date dataAssegnazione) {
		this.commissioneUser.setDataAssegnazione(dataAssegnazione);
	}

	public boolean isSospensioneFeriale() {
		return this.commissioneUser.isSospensioneFeriale();
	}

	public void setSospensioneFeriale(boolean sospensioneFeriale) {
		this.commissioneUser.setSospensioneFeriale(sospensioneFeriale);
	}

	public Date getDataInterruzione() {
		return this.commissioneUser.getDataInterruzione();
	}

	public void setDataInterruzione(Date dataInterruzione) {
		this.commissioneUser.setDataInterruzione(dataInterruzione);
	}

	public Date getDataRicezioneIntegrazioni() {
		return this.commissioneUser.getDataRicezioneIntegrazioni();
	}

	public void setDataRicezioneIntegrazioni(Date dataRicezioneIntegrazioni) {
		this.commissioneUser.setDataRicezioneIntegrazioni(dataRicezioneIntegrazioni);
	}

	public String getStatoCommitStralci() {
		return statoCommitStralci;
	}

	public void setStatoCommitStralci(String statoCommitStralci) {
		this.statoCommitStralci = statoCommitStralci;
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

	public Integer getNumEmendApprovatiCommissione() {
		return numEmendApprovatiCommissione;
	}

	public void setNumEmendApprovatiCommissione(Integer numEmendApprovatiCommissione) {
		this.numEmendApprovatiCommissione = numEmendApprovatiCommissione;
	}

	public Integer getNumEmendPresentatiCommissione() {
		return numEmendPresentatiCommissione;
	}

	public void setNumEmendPresentatiCommissione(Integer numEmendPresentatiCommissione) {
		this.numEmendPresentatiCommissione = numEmendPresentatiCommissione;
	}

	public AttoRecordServiceManager getAttoRecordServiceManager() {
		return attoRecordServiceManager;
	}

	public void setAttoRecordServiceManager(AttoRecordServiceManager attoRecordServiceManager) {
		this.attoRecordServiceManager = attoRecordServiceManager;
	}

	public String getMessaggioGiorniScadenzaColor() {
		return messaggioGiorniScadenzaColor;
	}

	public void setMessaggioGiorniScadenzaColor(String messaggioGiorniScadenzaColor) {
		this.messaggioGiorniScadenzaColor = messaggioGiorniScadenzaColor;
	}

	public List<String> getEsitiVotazione() {
		return esitiVotazione;
	}

	public void setEsitiVotazione(List<String> esitiVotazione) {

		this.esitiVotazione = esitiVotazione;
	}

	public List<String> getQuorumVotazione() {
		return quorumVotazione;
	}

	public void setQuorumVotazione(List<String> quorumVotazione) {
		this.quorumVotazione = quorumVotazione;
	}

}
