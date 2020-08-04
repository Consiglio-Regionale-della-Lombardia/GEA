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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.sourcesense.crl.business.model.Commissione;
import com.sourcesense.crl.business.model.GruppoUtente;
import com.sourcesense.crl.business.model.StatoAtto;
import com.sourcesense.crl.web.ui.beans.AttoBean;
import com.sourcesense.crl.web.ui.beans.AttoSearchBean;
import com.sourcesense.crl.web.ui.beans.UserBean;

/**
 * Regole di navigazione. Contiene i flag per la configurazione della
 * navigazione da pagina web
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "navigationBean")
@RequestScoped
public class NavigationRules {

	/**
	 * Gruppi ADMINISTRATORS servizioCommissioni CommissioneN Aula Guest
	 */

	AttoBean attoBean;
	AttoSearchBean attoSearchBean;
	UserBean userBean;

	/**
	 * Inizializza le informazioni dell'utente, dell'atto e della pagina di ricerca
	 * dell'atto nel contesto web
	 */
	@PostConstruct
	protected void init() {

		FacesContext context = FacesContext.getCurrentInstance();

		userBean = (UserBean) context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{userBean}", UserBean.class)
				.getValue(context.getELContext());

		attoBean = (AttoBean) context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{attoBean}", AttoBean.class)
				.getValue(context.getELContext());

		attoSearchBean = (AttoSearchBean) context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{attoSearchBean}", AttoSearchBean.class)
				.getValue(context.getELContext());

	}

	/**
	 * Se l'utente è nel gruppo SERVIZIO_COMMISSIONI o ADMIN allora è amministratore
	 * 
	 * @return autorizzazione
	 */
	public boolean isAmministrazioneEnabled() {

		return (GruppoUtente.SERVIZIO_COMMISSIONI.equals(userBean.getUserGroupName())
				|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName()));
	}

	/**
	 * Se l'utente è nel gruppo SERVIZIO_COMMISSIONI , ADMIN o AULA allora può
	 * inserire dati MIS
	 * 
	 * @return autorizzazione
	 */
	public boolean isInsertMISEnabled() {

		return (GruppoUtente.SERVIZIO_COMMISSIONI.equals(userBean.getUserGroupName())
				|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName())
				|| GruppoUtente.AULA.equals(userBean.getUserGroupName()));
	}

	/**
	 * Se l'utente è nel gruppo SERVIZIO_COMMISSIONI , ADMIN o AULA allora può
	 * inserire dati EAC
	 * 
	 * @return autorizzazione
	 */
	public boolean isInsertEACEnabled() {

		return (GruppoUtente.SERVIZIO_COMMISSIONI.equals(userBean.getUserGroupName())
				|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName())
				|| GruppoUtente.AULA.equals(userBean.getUserGroupName()));
	}

	/**
	 * Se l'utente è nel gruppo SERVIZIO_COMMISSIONI , ADMIN o AULA allora può
	 * inserire dati degli atti
	 * 
	 * @return autorizzazione
	 */
	public boolean isInsertEnabled() {

		return (GruppoUtente.SERVIZIO_COMMISSIONI.equals(userBean.getUserGroupName())
				|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName())
				|| GruppoUtente.AULA.equals(userBean.getUserGroupName()));
	}

	/**
	 * Ritorna il sottostato della commissione consultiva
	 * 
	 * @return lo stato della commissione
	 */
	public String getSottoStatoCommissioneConsultiva() {

		Commissione comm = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		if (comm != null && comm.getRuolo().equals(Commissione.RUOLO_CONSULTIVA)) {

			return comm.getStato();

		}

		return null;
	}

	/**
	 * Se il ruolo della commissione non è di tipo RUOLO_CONSULTIVA ne
	 * RUOLO_DELIBERANTE allora ha la data di richiesta dell'iscrizione in aula
	 * 
	 * @return autorizzazione
	 */
	public boolean hasDataRichiestaIscrizioneAula() {
		Commissione comm = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		return !(comm.getRuolo().equals(Commissione.RUOLO_CONSULTIVA)
				|| comm.getRuolo().equals(Commissione.RUOLO_DELIBERANTE));
	}

	/**
	 * Se il ruolo della commissione non è di tipo ServizioCommissioni ne ADMIN
	 * allora l'EAC è disabilitato
	 * 
	 * @return autorizzazione
	 */
	public boolean isEACDisabled() {

		return !("ServizioCommissioni".equals(userBean.getUserGroupName())
				|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName()));
	}

	/**
	 * 
	 * Se il ruolo della commissione non è di tipo ServizioCommissioni ne ADMIN e
	 * l'utente non è in commissione allora l'EAC è disabilitato
	 * 
	 * @return autorizzazione
	 */
	public boolean isEACDisabledComm() {

		return !("ServizioCommissioni".equals(userBean.getUserGroupName())
				|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName())
				|| userBean.getUser().getSessionGroup().isCommissione());
	}

	/**
	 * Se il gruppo dell'utente non è di tipo CPCV ne ADMIN allora il MIS è
	 * disabilitato
	 * 
	 * @return autorizzazione
	 */
	public boolean isMISDisabled() {

		return !(GruppoUtente.CPCV.equals(userBean.getUserGroupName())
				|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName()));

	}

	/**
	 * Verifica che il gruppo dell'utente sia di tipo CPCV
	 * 
	 * @return autorizzazione
	 */
	public boolean isCPCVUser() {

		return (GruppoUtente.CPCV.equals(userBean.getUserGroupName()));

	}

	/**
	 * Verifica che il gruppo dell'utente sia di tipo ADMIN o SERVIZIO_COMMISSIONI e
	 * che lo stato dell'atto sia di tipo PRESO_CARICO_SC
	 * 
	 * @return autorizzazione
	 */
	public boolean isInviaDirettamenteAula() {
		return StatoAtto.PRESO_CARICO_SC.equals(attoBean.getStato())
				&& (GruppoUtente.ADMIN.equals(userBean.getUserGroupName())
						|| GruppoUtente.SERVIZIO_COMMISSIONI.equals(userBean.getUserGroupName()));
	}

	/**
	 * Verifica lo stato dell'atto non sia di tipo TRASMESSO_AULA,
	 * PRESO_CARICO_AULA, VOTATO_AULA ne PUBBLICATO
	 * 
	 * @return autorizzazione
	 */
	public boolean presaCaricoAulaDisabled() {

		if (attoBean.getStato().equals(StatoAtto.TRASMESSO_AULA)
				|| attoBean.getStato().equals(StatoAtto.PRESO_CARICO_AULA)
				|| attoBean.getStato().equals(StatoAtto.VOTATO_AULA)
				|| attoBean.getStato().equals(StatoAtto.PUBBLICATO))

		{
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Verifica che il gruppo dell'utente sia di tipo ADMIN o SERVIZIO_COMMISSIONI e
	 * che lo stato dell'atto sia di tipo ORG e ne PDA_UDP
	 * 
	 * @return autorizzazione
	 */
	public boolean presentazioneAssegnazioneDisabled() {
		boolean disabled;

		if (!isSessionAttoORG() && !isSessionAttoPDA_UDP()
				&& (GruppoUtente.SERVIZIO_COMMISSIONI.equals(userBean.getUserGroupName())
						|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName()))) {
			disabled = false;
		} else {
			disabled = true;
		}

		return disabled;
	}

	/**
	 * Verifica che il gruppo dell'utente sia di tipo ADMIN o SERVIZIO_COMMISSIONI e
	 * che lo stato dell'atto sia di tipo ORG e ne PDA_UDP
	 * 
	 * @return autorizzazione
	 */
	public boolean esameCommissioniDisabled() {

		boolean disabled;
		if (!isSessionAttoORG() && !isSessionAttoPDA_UDP()
				&& (attoBean.containCommissione(userBean.getUser().getSessionGroup().getNome())
						|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName()))
				&& attoBean.getLastPassaggio().getCommissioni().size() > 0) {
			disabled = false;

		} else {
			disabled = true;
		}

		return disabled;
	}

	/**
	 * Disabilitato se la commissione non esiste oppure la sessione sia di tipo
	 * PDA_UDP o ORG
	 * 
	 * @return autorizzazione
	 */
	public boolean consultazioniEPareriDisabled() {
		Commissione commissione = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		return commissione == null || isSessionAttoPDA_UDP() || isSessionAttoORG();
	}

	/**
	 * Sempre disabilitato
	 * 
	 * @return false
	 */
	public boolean collegamentiDisabled() {
		return false;
	}

	/**
	 * Abilitato se il tipo atto è PAR, INP, PRE o REL
	 * 
	 * @return autorizzazione
	 */
	public boolean isFirmatariEnabled() {

		if (attoBean.getTipoAtto().equals("PAR") || attoBean.getTipoAtto().equals("INP")
				|| attoBean.getTipoAtto().equals("PRE") || attoBean.getTipoAtto().equals("REL"))

		{
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Abilitato se il tipo atto è PDL, PLP o 03_ATTO DI INIZIATIVA POPOLARE
	 * 
	 * @return autorizzazione
	 */
	public boolean isFirmatariPopolariEnabled() {

		if (("PDL".equals(attoBean.getTipoAtto()) || "PLP".equals(attoBean.getTipoAtto()))
				&& "03_ATTO DI INIZIATIVA POPOLARE".equals(attoBean.getTipoIniziativa()))

		{
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Abilitato se il tipo atto è PDA, PLP, PRE, REF, PDL, DOC o REL
	 * 
	 * @return autorizzazione
	 */
	public boolean organismiEnabled() {

		if (attoBean.getTipoAtto().equals("PDA") || attoBean.getTipoAtto().equals("PLP")
				|| attoBean.getTipoAtto().equals("PRE") || attoBean.getTipoAtto().equals("REF")
				|| attoBean.getTipoAtto().equals("PDL") || attoBean.getTipoAtto().equals("DOC")
				|| attoBean.getTipoAtto().equals("REL"))

		{
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Abilitato se il tipo atto è PDA, PLP, PRE, REF, PDL, DOC o REL
	 * 
	 * @return autorizzazione
	 */
	public boolean emendamentiClausoleEnabled() {

		if (attoBean.getTipoAtto().equals("PDA") || attoBean.getTipoAtto().equals("PLP")
				|| attoBean.getTipoAtto().equals("PRE") || attoBean.getTipoAtto().equals("REF")
				|| attoBean.getTipoAtto().equals("REL") || attoBean.getTipoAtto().equals("PDL")
				|| attoBean.getTipoAtto().equals("DOC"))

		{
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Abilitato se il tipo atto è PDL o la commissione ha RUOLO_REFERENTE,
	 * RUOLO_COREFERENTE o RUOLO_REDIGENTE
	 * 
	 * @return autorizzazione
	 */
	public boolean isClausoleEnabled() {

		Commissione commissione = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		if (attoBean.getTipoAtto().equals("PDL") && (Commissione.RUOLO_REFERENTE.equals(commissione.getRuolo())
				|| Commissione.RUOLO_COREFERENTE.equals(commissione.getRuolo())
				|| Commissione.RUOLO_REDIGENTE.equals(commissione.getRuolo())))

		{
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Abilitato se il tipo atto è PDL o PLP
	 * 
	 * @return autorizzazione
	 */
	public boolean abbinamentiEnabled() {

		if (attoBean.getTipoAtto().equals("PDL") || attoBean.getTipoAtto().equals("PLP"))

		{
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Disabilitato se il tipo atto è PAR, REL, INP, EAC, MIS o DOC o che l'atto non
	 * sia in aula
	 * 
	 * @return autorizzazione
	 */
	public boolean esameAulaDisabled() {

		if ((GruppoUtente.AULA.equals(userBean.getUserGroupName())
				|| GruppoUtente.ADMIN.equals(userBean.getUserGroupName()))
				&& !(attoBean.getTipoAtto().equals("PAR") || attoBean.getTipoAtto().equals("REL")
						|| attoBean.getTipoAtto().equals("INP") || attoBean.getTipoAtto().equals("EAC")
						|| attoBean.getTipoAtto().equals("MIS")
						|| (attoBean.getTipoAtto().equalsIgnoreCase("DOC") && !attoBean.getAtto().isIterAula()))

		) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Box aula visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean boxAulaVisible() {

		if (attoBean.getTipoAtto().equals("PAR") || attoBean.getTipoAtto().equals("REL")
				|| attoBean.getTipoAtto().equals("INP")
				|| (attoBean.getTipoAtto().equalsIgnoreCase("DOC") && !attoBean.getAtto().isIterAula())) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Emendamenti attivati
	 * 
	 * @return autorizzazione
	 */
	public boolean emendamentiEnabled() {
		if (isSessionAttoPDL() || isSessionAttoORG() || attoBean.getTipoAtto().equals("PDA")
				|| attoBean.getTipoAtto().equals("PLP") || attoBean.getTipoAtto().equals("PRE")
				|| attoBean.getTipoAtto().equals("DOC") || attoBean.getTipoAtto().equals("REF"))

		{
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Dati atto attivati
	 * 
	 * @return autorizzazione
	 */
	public boolean datiAttoEnabled() {
		return isSessionAttoPDA_UDP() || isSessionAttoORG();
	}

	/**
	 * Rinvio e stralci attivati
	 * 
	 * @return autorizzazione
	 */
	public boolean rinvioEStralciEnabled() {
		return (!isSessionAttoDOC() && !isSessionAttoPDA_UDP() && !isSessionAttoORG());
	}

	/**
	 * Stralci e aulta attivati
	 * 
	 * @return autorizzazione
	 */
	public boolean stralciAulaEnabled() {

		if (attoBean.getTipoAtto().equals("PDA") || attoBean.getTipoAtto().equals("PLP")
				|| attoBean.getTipoAtto().equals("PRE") || attoBean.getTipoAtto().equals("REF")

		) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Pubblicazione BURL attivata
	 * 
	 * @return autorizzazione
	 */
	public boolean pubblicazioneBurlEnabled() {

		if (attoBean.getTipoAtto().equals("INP") || attoBean.getTipoAtto().equals("PAR")
				|| attoBean.getTipoAtto().equals("REL")

		) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Testo dell'atto votato attivato
	 * 
	 * @return autorizzazione
	 */
	public boolean testoAttoVotatoEnabled() {

		if (attoBean.getTipoAtto().equals("REL") || attoBean.getTipoAtto().equals("INP")

		) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * BURL attivato
	 * 
	 * @return autorizzazione
	 */
	public boolean isBURLEnabled() {

		if (attoBean.getTipoAtto().equals("REL") || attoBean.getTipoAtto().equals("INP")
				|| "Per decadenza (fine legislatura)".equals(attoBean.getTipoChiusura())
				|| "Respinto dall'Aula".equals(attoBean.getTipoChiusura())
				|| "Ritirato dai promotori".equals(attoBean.getTipoChiusura())
				|| "Abbinato ad altro atto".equals(attoBean.getTipoChiusura())
				|| "Inammissibile".equals(attoBean.getTipoChiusura())
				|| "Istruttoria conclusa".equals(attoBean.getTipoChiusura())
				|| "Irricevibile".equals(attoBean.getTipoChiusura())
				|| "Improcedibile".equals(attoBean.getTipoChiusura())) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Sempre false
	 * 
	 * @return autorizzazione
	 */
	public boolean chiusuraIterDisabled() {

		return false;

	}

	/**
	 * Esito dell'atto della commissione attivato
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoEsitoCommissioneApprovato() {
		boolean risultato = false;
		if (attoBean.getTipoAtto().equalsIgnoreCase("PDL") || attoBean.getTipoAtto().equalsIgnoreCase("PAR")
				|| attoBean.getTipoAtto().equalsIgnoreCase("PDA") || attoBean.getTipoAtto().equalsIgnoreCase("PLP")
				|| attoBean.getTipoAtto().equalsIgnoreCase("PRE") || attoBean.getTipoAtto().equalsIgnoreCase("REF")
				|| (attoBean.getTipoAtto().equalsIgnoreCase("DOC")
						&& attoBean.getTipologia().equalsIgnoreCase("PRS"))) {

			risultato = true;

		}
		return risultato;
	}

	/**
	 * Verifica se l'atto è di tipo INP, DOC ma non PRS
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoEsitoCommissioneArchiviazioneINP() {
		boolean risultato = false;
		if (attoBean.getTipoAtto().equalsIgnoreCase("INP") || (attoBean.getTipoAtto().equalsIgnoreCase("DOC")
				&& !attoBean.getTipologia().equalsIgnoreCase("PRS"))) {
			risultato = true;

		}

		return risultato;
	}

	/**
	 * Verifica se l'atto è di tipo REL
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoEsitoCommissioneApprovatoREL() {
		boolean risultato = false;
		if (attoBean.getTipoAtto().equalsIgnoreCase("REL")) {
			risultato = true;

		}

		return risultato;
	}

	/**
	 * Verifica se lo stato dell'atto è chiuso
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoChiuso() {

		return StatoAtto.CHIUSO.equals(attoBean.getStato());
	}

	/**
	 * Verifica se lo stato dell'atto è chiuso e che non sia PAR
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoPARChiuso() {

		boolean isCommissione = userBean.getUser().getSessionGroup().isCommissione();
		return !(isSessionAttoPAR() && isCommissione) && isSessionAttoChiuso();
	}

	/**
	 * Verifica se l'atto è di tipo PAR
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoPAR() {
		return attoBean.getTipoAtto().equalsIgnoreCase("PAR");
	}

	/**
	 * Verifica se l'atto è di tipo PDL
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoPDL() {
		return attoBean.getTipoAtto().equalsIgnoreCase("PDL");
	}

	/**
	 * Verifica se l'atto è di tipo INP
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoINP() {
		return attoBean.getTipoAtto().equalsIgnoreCase("INP");
	}

	/**
	 * Verifica se l'atto è di tipo DOC
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoDOC() {
		return attoBean.getTipoAtto().equalsIgnoreCase("DOC");
	}

	/**
	 * Verifica se l'atto è di tipo PRE
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoPRE() {
		return attoBean.getTipoAtto().equalsIgnoreCase("PRE");
	}

	/**
	 * Verifica se l'atto è di tipo DOC ma che non sia in aula
	 * 
	 * @return autorizzazione
	 */
	public boolean isNotSessionAttoDOCAula() {
		return attoBean.getTipoAtto().equalsIgnoreCase("DOC") && !attoBean.getAtto().isIterAula();
	}

	/**
	 * Verifica se l'atto non è di tipo PAR
	 * 
	 * @return autorizzazione
	 */
	public boolean canTransmitToAula() {

		/*
		 * if (isCommissioneConsultiva()) {
		 * 
		 * return false; }
		 */

		if (isSessionAttoPAR()) {

			return false;
		}

		return true;

	}

	/**
	 * Verifica se l'atto è di tipo PDA
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoPDA() {
		return attoBean.getTipoAtto().equalsIgnoreCase("PDA");
	}

	/**
	 * Verifica se l'atto è di tipo PAR e che l'iniziativa sia di tipo 05_ATTO DI
	 * INIZIATIVA UFFICIO PRESIDENZA
	 * 
	 * @return
	 */
	public boolean isSessionAttoPDA_UDP() {
		boolean res = attoBean.getTipoAtto().equalsIgnoreCase("PDA")
				&& ("05_ATTO DI INIZIATIVA UFFICIO PRESIDENZA".equalsIgnoreCase(attoBean.getTipoIniziativa()));

		return res;
	}

	/**
	 * Verifica se l'atto è di tipo ORG
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoORG() {

		return attoBean.getTipoAtto().equalsIgnoreCase("ORG");

	}

	/**
	 * Verifica se l'atto è di tipo PLP
	 * 
	 * @return autorizzazione
	 */
	public boolean isSessionAttoPLP() {
		return attoBean.getTipoAtto().equalsIgnoreCase("PLP");
	}

	/**
	 * Verifica se l'utente è in commissione o che il gruppo sia di tipo AULA
	 * 
	 * @return autorizzazione
	 */
	public boolean isGestioneSeduteEnabled() {

		return userBean.getUser().getSessionGroup().isCommissione()
				|| GruppoUtente.AULA.equals(userBean.getUser().getSessionGroup().getNome());
	}

	/**
	 * Verifica se l'utente è in commissione
	 * 
	 * @return autorizzazione
	 */
	public boolean gestioneSeduteConsultazioniCommissione() {

		return userBean.getUser().getSessionGroup().isCommissione();
	}

	/**
	 * Verifica che il gruppo sia di tipo AULA
	 * 
	 * @return autorizzazione
	 */
	public boolean gestioneSeduteConsultazioniAula() {
		return GruppoUtente.AULA.equals(userBean.getUser().getSessionGroup().getNome());
	}

	/**
	 * Verifica che il ruolo della commissione sia di tipo RUOLO_REFERENTE,
	 * RUOLO_DELIBERANTE, RUOLO_REDIGENTE o RUOLO_COREFERENTE
	 * 
	 * @return autorizzazione
	 */
	public boolean isCommissioneUpdateEnabled() {

		Commissione commissione = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		if (commissione != null && (Commissione.RUOLO_REFERENTE.equals(commissione.getRuolo())
				|| Commissione.RUOLO_DELIBERANTE.equals(commissione.getRuolo())
				|| Commissione.RUOLO_REDIGENTE.equals(commissione.getRuolo())
				|| Commissione.RUOLO_COREFERENTE.equals(commissione.getRuolo()))) {
			return true;
		}

		return false;

	}

	/**
	 * Verifica che il ruolo della commissione sia di tipo RUOLO_REFERENTE
	 * 
	 * @return autorizzazione
	 */
	public boolean isCommissioneReferente() {
		Commissione commissione = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		if (commissione != null && Commissione.RUOLO_REFERENTE.equals(commissione.getRuolo())) {
			return true;
		}

		return false;
	}

	/**
	 * Verifica che il ruolo della commissione sia di tipo RUOLO_CONSULTIVA
	 * 
	 * @return autorizzazione
	 */
	public boolean isCommissioneConsultiva() {
		Commissione commissione = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		if (commissione != null && Commissione.RUOLO_CONSULTIVA.equals(commissione.getRuolo())) {
			return true;
		}

		return false;
	}

	/**
	 * Verifica che il ruolo della commissione sia di tipo RUOLO_DELIBERANTE
	 * 
	 * @return autorizzazione
	 */
	public boolean isCommissioneDeliberante() {
		Commissione commissione = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		if (commissione != null && Commissione.RUOLO_DELIBERANTE.equals(commissione.getRuolo())) {
			return true;
		}

		return false;
	}

	/**
	 * Verifica che l'atto abbia la commissione deliberante
	 * 
	 * @return autorizzazione
	 */
	public boolean hasCommissioneDeliberante() {

		return attoBean.getCommissioneDeliberante() != null;

	}

	/**
	 * Verifica se l'atto è di tipo PDL, PDA, PLP, PRE, REF, REL o DOC
	 * 
	 * @return autorizzazione
	 */
	public boolean isCalendarizzazioneTipo() {

		if (attoBean.getTipoAtto().equalsIgnoreCase("PDL") || attoBean.getTipoAtto().equalsIgnoreCase("PDA")
				|| attoBean.getTipoAtto().equalsIgnoreCase("PLP") || attoBean.getTipoAtto().equalsIgnoreCase("PRE")
				|| attoBean.getTipoAtto().equalsIgnoreCase("REF") || attoBean.getTipoAtto().equalsIgnoreCase("REL")
				|| (attoBean.getTipoAtto().equalsIgnoreCase("DOC") && !attoBean.getAtto().isIterAula())

		) {
			return true;
		}

		return false;

	}

	/**
	 * Atto di tipo INP, REL o DOC e commissione con RUOLO_REFERENTE,
	 * RUOLO_COREFERENTE, RUOLO_REDIGENTE o RUOLO_DELIBERANTE
	 * 
	 * @return autorizzazione
	 */
	public boolean isRisEnabled() {

		Commissione commissione = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		if (commissione != null
				&& (Commissione.RUOLO_REFERENTE.equals(commissione.getRuolo())
						|| Commissione.RUOLO_COREFERENTE.equals(commissione.getRuolo())
						|| Commissione.RUOLO_REDIGENTE.equals(commissione.getRuolo())
						|| Commissione.RUOLO_DELIBERANTE.equals(commissione.getRuolo()))
				&& (attoBean.getTipoAtto().equalsIgnoreCase("INP") || attoBean.getTipoAtto().equalsIgnoreCase("DOC")
						|| attoBean.getTipoAtto().equalsIgnoreCase("REL"))

		) {
			return true;
		}

		return false;

	}

	/**
	 * Atto di tipo INP, REL o DOC
	 * 
	 * @return autorizzazione
	 */
	public boolean isRisTipo() {

		if (attoBean.getTipoAtto().equalsIgnoreCase("INP") || attoBean.getTipoAtto().equalsIgnoreCase("DOC")
				|| attoBean.getTipoAtto().equalsIgnoreCase("REL")) {
			return true;
		}

		return false;

	}

	/**
	 * Calendarizzazione attivata
	 * 
	 * @return autorizzazione
	 */
	public boolean isCalendarizzazioneEnabled() {

		Commissione commissione = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		if (commissione != null
				&& (Commissione.RUOLO_REFERENTE.equals(commissione.getRuolo())
						|| Commissione.RUOLO_COREFERENTE.equals(commissione.getRuolo())
						|| Commissione.RUOLO_REDIGENTE.equals(commissione.getRuolo()))
				&& (attoBean.getTipoAtto().equalsIgnoreCase("PDL") || attoBean.getTipoAtto().equalsIgnoreCase("PDA")
						|| attoBean.getTipoAtto().equalsIgnoreCase("PLP")
						|| attoBean.getTipoAtto().equalsIgnoreCase("PRE")
						|| attoBean.getTipoAtto().equalsIgnoreCase("REF")
						|| attoBean.getTipoAtto().equalsIgnoreCase("REL")
						|| (attoBean.getTipoAtto().equalsIgnoreCase("DOC") && !attoBean.getAtto().isIterAula()))

		) {
			return true;
		}

		return false;

	}

	/**
	 * Referente per la continuazione lavori
	 * 
	 * @return autorizzazione
	 */
	public boolean isContinuazioneLavoriReferente() {
		Commissione commissione = attoBean.getWorkingCommissione(userBean.getUser().getSessionGroup().getNome());

		if (commissione != null && Commissione.RUOLO_DELIBERANTE.equals(commissione.getRuolo())
				|| (commissione.getMotivazioniContinuazioneInReferente() != null
						&& !"".equals(commissione.getMotivazioniContinuazioneInReferente()))
				|| commissione.getDataSedutaContinuazioneInReferente() != null) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @return autorizzazione
	 */
	public boolean isServizioCommissioni() {

		return GruppoUtente.SERVIZIO_COMMISSIONI.equals(userBean.getUserGroupName());

	}

	/**
	 * Guest
	 * 
	 * @return autorizzazione
	 */
	public boolean isGuest() {

		if (userBean.getUser().getSessionGroup().getNome().equals(GruppoUtente.GUEST)) {
			return true;
		}

		return false;

	}

}
