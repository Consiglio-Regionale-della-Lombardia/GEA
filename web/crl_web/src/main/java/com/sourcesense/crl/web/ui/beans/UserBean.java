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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.sourcesense.crl.business.model.ColonnaAtto;
import com.sourcesense.crl.business.model.GruppoUtente;
import com.sourcesense.crl.business.model.User;

/**
 * Rappresenta l'utente tramite le informazioni di username, password, session
 * token e colonne
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 7726122743894534255L;

	private User user = new User();

	private String username;

	private String password;

	private String sessionToken;

	private List<ColonnaAtto> colonneTotali = new ArrayList<ColonnaAtto>();

	private List<ColonnaAtto> colonneUser = new ArrayList<ColonnaAtto>();

	/**
	 * Aggiunge le colonne tabellari visualizzabili nel profilo utente. Per default
	 * vengono aggiunte: Oggetto, Data presentazione, Stato, Primo firmatario,
	 * Firmatari, Tipo iniziativa, Tipo chiusura, Com. referente, co-referente,
	 * redigente o deliberante,Data assegnazione, Commissioni consultive, Relatore,
	 * Data nomina relatore, Abbinamenti, Esito votazione Commissione referente (o
	 * redigente o deliberante), Data votazione Commissione, Data scadenza, Data
	 * richiesta iscrizione Aula, Esito votazione Aula, Data votazione Aula, N° DCR,
	 * N° LCR, BURL, Data BURL, N° LR, Data LR, Note generali
	 */
	@PostConstruct
	public void init() {

		colonneTotali.add(new ColonnaAtto("Oggetto", "oggetto", true));
		colonneTotali.add(new ColonnaAtto("Data presentazione", "dataPresentazione", true));
		colonneTotali.add(new ColonnaAtto("Stato", "stato", true));
		colonneTotali.add(new ColonnaAtto("Primo firmatario", "primoFirmatario", false));
		colonneTotali.add(new ColonnaAtto("Firmatari", "elencoFirmatari", false));
		colonneTotali.add(new ColonnaAtto("Tipo iniziativa", "tipoIniziativaNome", false));
		colonneTotali.add(new ColonnaAtto("Tipo chiusura", "tipoChiusura", false));
		colonneTotali.add(new ColonnaAtto("Com. referente, co-referente, redigente o deliberante",
				"commissioniNonConsultive", false));
		colonneTotali.add(new ColonnaAtto("Data assegnazione", "dataAssegnazione", false));
		colonneTotali.add(new ColonnaAtto("Commissioni consultive", "commissioniConsultive", false));
		colonneTotali.add(new ColonnaAtto("Relatore", "relatore", false));
		colonneTotali.add(new ColonnaAtto("Data nomina relatore", "dataNominaRelatore", false));
		colonneTotali.add(new ColonnaAtto("Abbinamenti", "elencoAbbinamenti", false));
		colonneTotali.add(new ColonnaAtto("Esito votazione Commissione referente (o redigente o deliberante)",
				"esitoVotazioneCommissioneReferente", false));
		colonneTotali.add(new ColonnaAtto("Data votazione Commissione", "dataVotazioneCommissione", false));
		colonneTotali.add(new ColonnaAtto("Data scadenza", "dataScadenza", false));
		colonneTotali.add(new ColonnaAtto("Data richiesta iscrizione Aula", "dataRichiestaIscrizioneAula", false));
		colonneTotali.add(new ColonnaAtto("Esito votazione Aula", "esitoVotazioneAula", false));
		colonneTotali.add(new ColonnaAtto("Data votazione Aula", "dataVotazioneAula", false));
		colonneTotali.add(new ColonnaAtto("N° DCR", "numeroDcr", false));
		colonneTotali.add(new ColonnaAtto("N° LCR", "numeroLcr", false));
		colonneTotali.add(new ColonnaAtto("BURL", "numeroPubblicazioneBURL", false));
		colonneTotali.add(new ColonnaAtto("Data BURL", "dataPubblicazioneBURL", false));
		colonneTotali.add(new ColonnaAtto("N° LR", "numeroLr", false));
		colonneTotali.add(new ColonnaAtto("Data LR", "dataLR", false));
		colonneTotali.add(new ColonnaAtto("Note generali", "notePresentazioneAssegnazione", false));
		refreshColonneUser();

	}

	public String getUserGroupName() {

		return user.getSessionGroup().getNome();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return user.getUsername();
	}

	public void setUsername(String username) {
		this.user.setUsername(username);
	}

	public String getPassword() {
		return user.getPassword();
	}

	public void setPassword(String password) {
		this.user.setPassword(password);
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public List<ColonnaAtto> getColonneTotali() {
		return colonneTotali;
	}

	public void setColonneTotali(List<ColonnaAtto> colonneTotali) {
		this.colonneTotali = colonneTotali;
	}

	public List<ColonnaAtto> getColonneUser() {
		return colonneUser;
	}

	public void setColonneUser(List<ColonnaAtto> colonneUser) {
		this.colonneUser = colonneUser;
	}

	/**
	 * Refresh delle colonne utente
	 */
	public void refreshColonneUser() {

		colonneUser.clear();

		for (ColonnaAtto colonnaAtto : colonneTotali) {
			if (colonnaAtto.isVisible()) {
				colonneUser.add(colonnaAtto);
			}
		}
	}

	/**
	 * Aggiunge il gruppo all'utente
	 * 
	 * @param event evento JSF
	 */
	public void handleChangeSessionGroup(final AjaxBehaviorEvent event) {
		SelectOneMenu selectOneMenu = ((SelectOneMenu) event.getSource());
		String selectedItem = (String) selectOneMenu.getSubmittedValue();
		for (GruppoUtente gruppoUtente : user.getGruppi()) {
			if (gruppoUtente.toString().equalsIgnoreCase(selectedItem)) {
				user.setSessionGroup(gruppoUtente);
				return;
			}
		}
	}

	/**
	 * Refresh delle colonne utente
	 * 
	 * @return il valore "pretty:Home"
	 */
	public String refreshSearch() {
		refreshColonneUser();
		return "pretty:Home";

	}

}
