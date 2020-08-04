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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.springframework.context.MessageSource;

import com.sourcesense.crl.business.model.Abbinamento;
import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.AttoEAC;
import com.sourcesense.crl.business.model.AttoMIS;
import com.sourcesense.crl.business.model.Aula;
import com.sourcesense.crl.business.model.Commissione;
import com.sourcesense.crl.business.model.Consultazione;
import com.sourcesense.crl.business.model.OrganismoStatutario;
import com.sourcesense.crl.business.model.Passaggio;
import com.sourcesense.crl.business.model.Relatore;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.business.service.AttoServiceManager;

/**
 * Atto visualizzato nelle pagine web
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "attoBean")
@SessionScoped
public class AttoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean showCommDetail;
	private Atto atto = new Atto();
	private AttoMIS attoMIS = new AttoMIS();
	private AttoEAC attoEAC = new AttoEAC();
	private String codice;
	private String numeroAtto;
	private String estensioneAtto;
	private String tipoAtto;
	private String tipologia;
	private String legislatura;
	private Date dataPresaInCarico;

	private String statoAttuale;
	private String classificazione;
	private String numeroRepertorio;
	private String urlFascicolo;
	private Date dataRepertorio;
	private Date dataIniziativa;
	private String tipoIniziativa;
	private String oggetto;
	private Date dataAssegnazione;
	private String esitoValidazione;
	private Date dataValidazione;

	private String descrizioneIniziativa;
	private String numeroDGR;
	private Date dataDGR;
	private String assegnazione;
	private String id;
	private String tipo;
	private String primoFirmatario;
	private Date dataPresentazione;
	private String stato;
	private String anno;

	private String numeroProtocollo;
	private String firmatario;
	private String tipoChiusura;
	private boolean redigente;
	private boolean deliberante;
	private String numeroLR;
	private boolean abbinamento;
	private boolean stralcio;
	private String relatore;
	private String organismoStatutario;
	private String soggettoConsultato;
	private boolean rinviato;
	private boolean sospeso;

	private Date dataAssegnazioneCommissioni;
	private Date dataLR;
	private String numeroPubblicazioneBURL;
	private Date dataPubblicazioneBURL;
	private Date dataChiusura;
	private String statoChiusura;
	private String esitoVotazione;
	private Date dataSeduta;
	private String tipoVotazione;

	private String valutazioneAmmissibilita;
	private Date dataRichiestaInformazioni;
	private Date dataRicevimentoInformazioni;
	private boolean aiutiStato;
	private boolean normaFinanziaria;
	private boolean richiestaUrgenza;
	private boolean votazioneUrgenza;
	private Date dataVotazioneUrgenza;
	private String noteAmmissibilita;
	private String noteNoteAllegatiPresentazioneAssegnazione;

	private List<Abbinamento> abbinamenti = new ArrayList<Abbinamento>();
	private List<Abbinamento> abbinamentiAttivi = new ArrayList<Abbinamento>();
	private List<Commissione> commissioni = new ArrayList<Commissione>();

	@ManagedProperty(value = "#{attoServiceManager}")
	private AttoServiceManager attoServiceManager;

	@ManagedProperty(value = "#{messageSource}")
	MessageSource messageSource;

	public int getNumeroPassaggi() {

		return this.atto.getPassaggi().size();
	}

	/**
	 * Verifica se la commissione è presente nell'atto selezionato
	 * 
	 * @param commissione commissione
	 * @returnok se la commissione è presente e non ha lo STATO_ANNULLATO
	 */
	public boolean containCommissione(String commissione) {
		for (Commissione commissioneRec : this.atto.getPassaggi().get(0).getCommissioni()) {

			if (commissioneRec.getDescrizione().equalsIgnoreCase(commissione)
					&& !commissioneRec.getStato().equals(Commissione.STATO_ANNULLATO)) {

				return true;
			}
		}

		return false;
	}

	public Commissione getCommissioneReferente() {

		Commissione commissioneRet = null;

		for (Commissione commissioneRec : getLastPassaggio().getCommissioni()) {

			if (commissioneRec.getRuolo().equals(Commissione.RUOLO_REFERENTE)
					&& !commissioneRec.getStato().equals(Commissione.STATO_ANNULLATO)) {

				commissioneRet = commissioneRec;
			}
		}

		return commissioneRet;
	}

	public Commissione getCommissioneDeliberante() {

		Commissione commissioneRet = null;

		for (Commissione commissioneRec : getLastPassaggio().getCommissioni()) {

			if (commissioneRec.getRuolo().equals(Commissione.RUOLO_DELIBERANTE)
					&& !commissioneRec.getStato().equals(Commissione.STATO_ANNULLATO)) {

				commissioneRet = commissioneRec;
			}
		}

		return commissioneRet;
	}

	public Commissione getWorkingCommissione(String descrizione) {

		Commissione commissioneRet = null;

		for (Commissione commissioneRec : getLastPassaggio().getCommissioni()) {

			if (commissioneRec.getDescrizione().equalsIgnoreCase(descrizione)
					&& !commissioneRec.getStato().equals(Commissione.STATO_ANNULLATO)) {

				commissioneRet = commissioneRec;
			}
		}

		return commissioneRet;
	}

	public Aula getWorkingAula() {

		return atto.getPassaggi().get(atto.getPassaggi().size() - 1).getAula();

	}

	public List<OrganismoStatutario> getValidOrganismiStatutari() {

		List<OrganismoStatutario> listOrg = new ArrayList<OrganismoStatutario>();

		for (OrganismoStatutario organismoRec : atto.getOrganismiStatutari()) {

			if (organismoRec.getDataAnnullo() == null) {

				listOrg.add(organismoRec);

			}
		}

		return listOrg;

	}

	public OrganismoStatutario getWorkingOrganismoStatutario(String organismo) {

		OrganismoStatutario organismoRet = null;

		for (OrganismoStatutario organismoRec : atto.getOrganismiStatutari()) {

			if (organismoRec.getDescrizione().equalsIgnoreCase(organismo)) {

				organismoRet = organismoRec;

			}
		}

		return organismoRet;

	}

	public Consultazione getWorkingConsultazione(String consultazione) {

		Consultazione consultazioneRet = null;

		for (Consultazione consultazioneRec : atto.getConsultazioni()) {

			if (consultazioneRec.getDescrizione().equalsIgnoreCase(consultazione)) {

				consultazioneRet = consultazioneRec;
			}
		}

		return consultazioneRet;
	}

	public List<Consultazione> getConsultazioni() {

		List<Consultazione> lista = atto.getConsultazioni();

		Collections.sort(lista, new Comparator<Consultazione>() {
			public int compare(Consultazione m1, Consultazione m2) {
				return m1.getDataConsultazione().compareTo(m2.getDataConsultazione());
			}
		});

		for (Consultazione consultazione : lista) {

			Commissione commAppo = getWorkingCommissione(consultazione.getCommissione());

			if (commAppo != null) {
				consultazione.setRuoloCommissione(commAppo.getRuolo());
			}
		}

		return lista;
	}

	public Passaggio getLastPassaggio() {

		return atto.getPassaggi().get(atto.getPassaggi().size() - 1);

	}

	public Passaggio getPassaggio(String pass) {

		Passaggio passaggioSelected = null;

		for (Passaggio passaggioRec : this.atto.getPassaggi()) {

			if (passaggioRec.getNome().equalsIgnoreCase(pass)) {

				passaggioSelected = passaggioRec;
			}
		}

		return passaggioSelected;

	}

	public List<String> getCommissioniAssegnate() {

		List<String> commissioni = new ArrayList<String>();
		for (Commissione commissione : getLastPassaggio().getCommissioni()) {

			commissioni.add(commissione.getDescrizione());
		}

		return commissioni;

	}

	/* Files */

	public List<TestoAtto> getTestiAttoCommissione() {
		List<TestoAtto> returnList = new ArrayList<TestoAtto>();

		for (Commissione commRec : getLastPassaggio().getCommissioni()) {
			if (!Commissione.RUOLO_CONSULTIVA.equals(commRec.getRuolo())) {
				for (TestoAtto testoAtto : commRec.getTestiAttoVotatoEsameCommissioni()) {
					testoAtto.setCommissione(commRec.getDescrizione() + " - " + commRec.getRuolo());
					returnList.add(testoAtto);
				}
			}
		}

		return returnList;
	}

	public List<TestoAtto> getTestiAttoCommissioneConsultive() {
		List<TestoAtto> returnList = new ArrayList<TestoAtto>();

		for (Commissione commRec : getLastPassaggio().getCommissioni()) {

			if (Commissione.RUOLO_CONSULTIVA.equals(commRec.getRuolo())) {
				for (TestoAtto testoAtto : commRec.getTestiAttoVotatoEsameCommissioni()) {
					testoAtto.setCommissione(commRec.getDescrizione());
					returnList.add(testoAtto);
				}
			}
		}

		return returnList;
	}

	public List<TestoAtto> getTestiAttoAula() {
		List<TestoAtto> returnList = new ArrayList<TestoAtto>();

		for (TestoAtto testoAtto : getLastPassaggio().getAula().getTestiAttoVotatoEsameAula()) {

			returnList.add(testoAtto);
		}

		return returnList;
	}

	public List<Allegato> getTestiComitatoRistretto() {

		List<Allegato> returnList = new ArrayList<Allegato>();
		for (Commissione commRec : getLastPassaggio().getCommissioni()) {

			for (Allegato allegato : commRec.getAllegatiNoteEsameCommissioni()) {
				allegato.setCommissione(commRec.getDescrizione() + " - " + commRec.getRuolo());
				returnList.add(allegato);

			}
		}
		return returnList;
	}

	public List<Allegato> getEmendamentiCommissioni() {
		List<Allegato> returnList = new ArrayList<Allegato>();
		for (Commissione commRec : getLastPassaggio().getCommissioni()) {

			for (Allegato allegato : commRec.getEmendamentiEsameCommissioni()) {
				allegato.setCommissione(commRec.getDescrizione() + " - " + commRec.getRuolo());
				returnList.add(allegato);

			}
		}
		return returnList;
	}

	public List<Allegato> getClausoleCommissioni() {
		List<Allegato> returnList = new ArrayList<Allegato>();

		for (Commissione commRec : getLastPassaggio().getCommissioni()) {

			for (Allegato allegato : commRec.getTestiClausola()) {
				allegato.setCommissione(commRec.getDescrizione() + " - " + commRec.getRuolo());
				returnList.add(allegato);

			}
		}

		return returnList;
	}

	public List<Allegato> getAllegatiCommissioni() {
		List<Allegato> returnList = new ArrayList<Allegato>();
		for (Commissione commRec : getLastPassaggio().getCommissioni()) {

			for (Allegato allegato : commRec.getAllegati()) {
				allegato.setCommissione(commRec.getDescrizione() + " - " + commRec.getRuolo());
				returnList.add(allegato);
			}
		}
		return returnList;
	}

	public List<Allegato> getEmendamentiAula() {
		List<Allegato> returnList = new ArrayList<Allegato>();
		for (Allegato allegato : getLastPassaggio().getAula().getEmendamentiEsameAula()) {

			returnList.add(allegato);

		}
		return returnList;
	}

	public List<Allegato> getAllegatiAula() {
		List<Allegato> returnList = new ArrayList<Allegato>();
		for (Allegato allegato : getLastPassaggio().getAula().getAllegatiEsameAula()) {

			returnList.add(allegato);

		}
		return returnList;
	}

	public List<Allegato> getAllegatiPareri() {
		List<Allegato> returnList = new ArrayList<Allegato>();
		for (OrganismoStatutario organismo : getAtto().getOrganismiStatutari()) {

			for (Allegato allegato : organismo.getParere().getAllegati()) {

				returnList.add(allegato);

			}

		}
		return returnList;
	}

	public List<Allegato> getAllegatiConsultazioni() {

		List<Allegato> returnList = new ArrayList<Allegato>();
		for (Consultazione consultazioneRec : getAtto().getConsultazioni()) {

			for (Allegato allegato : consultazioneRec.getAllegati()) {

				returnList.add(allegato);
			}
		}
		return returnList;
	}

	public List<String> getPassaggi() {

		List<String> passaggi = new ArrayList<String>();

		for (Passaggio passaggioRec : this.atto.getPassaggi()) {

			passaggi.add(passaggioRec.getNome());
		}

		return passaggi;

	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/* Services */
	public AttoServiceManager getAttoServiceManager() {
		return attoServiceManager;
	}

	public void setAttoServiceManager(AttoServiceManager attoServiceManager) {
		this.attoServiceManager = attoServiceManager;
	}

	/* Fields */
	public String getCodice() {
		return atto.getCodice();
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public Atto getAtto() {
		return atto;
	}

	public void setAtto(Atto atto) {

		this.atto = atto;
		for (Passaggio passaggio : this.atto.getPassaggi()) {

			for (Commissione commissione : passaggio.getCommissioni()) {

				List<Allegato> appoList = new ArrayList<Allegato>();

				for (Allegato allegato : commissione.getAllegati()) {

					if (Allegato.TESTO_ESAME_COMMISSIONE_CLAUSOLA.equals(allegato.getTipologia())) {

						commissione.getTestiClausola().add(allegato);

					} else if (Allegato.TESTO_ESAME_COMMISSIONE_COMITATO.equals(allegato.getTipologia())) {

						commissione.getAllegatiNoteEsameCommissioni().add(allegato);

					} else if (Allegato.TIPO_ESAME_COMMISSIONE_EMENDAMENTO.equals(allegato.getTipologia())) {

						commissione.getEmendamentiEsameCommissioni().add(allegato);

					} else {

						appoList.add(allegato);
					}

				}

				commissione.setAllegati(appoList);

			}

			List<Allegato> appoList = new ArrayList<Allegato>();

			for (Allegato allegato : passaggio.getAula().getAllegatiEsameAula()) {

				if (Allegato.TESTO_ESAME_AULA_EMENDAMENTO.equals(allegato.getTipologia())) {

					passaggio.getAula().getEmendamentiEsameAula().add(allegato);

				} else {

					appoList.add(allegato);
				}

			}

			passaggio.getAula().setAllegatiEsameAula(appoList);

		}

	}

	public String getRelatoreORG() {

		String relatore = "";
		for (Relatore rel : atto.getRelatori()) {

			if (rel.getDataUscita() == null) {
				relatore = rel.getDescrizione();
			}

		}

		return relatore;

	}

	public boolean isShowCommDetail() {
		return showCommDetail;
	}

	public void setShowCommDetail(boolean showCommDetail) {
		this.showCommDetail = showCommDetail;
	}

	public void visualizeCommDetail() {
		this.showCommDetail = true;
	}

	public String getNumeroAtto() {
		return atto.getNumeroAtto();
	}

	public void setNumeroAtto(String numeroAtto) {

		this.atto.setNumeroAtto(numeroAtto);
	}

	public String getEstensioneAtto() {
		return atto.getEstensioneAtto();
	}

	public void setEstensioneAtto(String estensioneAtto) {

		this.atto.setEstensioneAtto(estensioneAtto);
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
	}

	public String getUrlFascicolo() {

		String url = this.atto.getUrlFascicolo();
		if (url != null && !url.isEmpty()) {
			String context = messageSource.getMessage("host.urlFascicolo", null, Locale.ITALY);
			url = context.concat(url);
		}
		return url;
	}

	public void setUrlFascicolo(String urlFascicolo) {
		this.atto.setUrlFascicolo(urlFascicolo);
	}

	public String getLegislatura() {
		return this.atto.getLegislatura();
	}

	public void setLegislatura(String legislatura) {
		this.atto.setLegislatura(legislatura);
	}

	public Date getDataPresaInCarico() {
		return atto.getDataPresaInCarico();
	}

	public void setDataPresaInCarico(Date dataPresaInCarico) {
		this.atto.setDataPresaInCarico(dataPresaInCarico);
	}

	public String getStatoAttuale() {
		return atto.getStatoAttuale();
	}

	public void setStatoAttuale(String statoAttuale) {
		this.atto.setStatoAttuale(statoAttuale);
	}

	public String getClassificazione() {
		return atto.getClassificazione();
	}

	public void setClassificazione(String classificazione) {
		this.atto.setClassificazione(classificazione);
	}

	public String getnRepertorio() {
		return atto.getnRepertorio();
	}

	public void setDataRepertorio(Date dataRepertorio) {
		this.atto.setDataRepertorio(dataRepertorio);
	}

	public Date getDataIniziativa() {
		return atto.getDataIniziativa();
	}

	public void setDataIniziativa(Date dataIniziativa) {
		this.atto.setDataIniziativa(dataIniziativa);
	}

	public String getTipoIniziativa() {
		return atto.getTipoIniziativa();
	}

	public void setTipoIniziativa(String tipoIniziativa) {
		this.atto.setTipoIniziativa(tipoIniziativa);
	}

	public String getOggetto() {

		return atto.getOggetto();
	}

	public void setOggetto(String oggetto) {
		this.atto.setOggetto(oggetto);
	}

	public String getNumeroRepertorio() {
		return atto.getNumeroRepertorio();
	}

	public void setNumeroRepertorio(String numeroRepertorio) {
		this.atto.setNumeroRepertorio(numeroRepertorio);
	}

	public Date getDataRepertorio() {
		return atto.getDataRepertorio();
	}

	public Date getDataAssegnazione() {
		return atto.getDataAssegnazione();
	}

	public void setDataAssegnazione(Date dataAssegnazione) {
		this.atto.setDataAssegnazione(dataAssegnazione);
	}

	public String getEsitoValidazione() {
		return atto.getEsitoValidazione();
	}

	public void setEsitoValidazione(String esitoValidazione) {
		this.atto.setEsitoValidazione(esitoValidazione);
	}

	public Date getDataValidazione() {
		return atto.getDataValidazione();
	}

	public void setDataValidazione(Date dataValidazione) {
		this.atto.setDataValidazione(dataValidazione);
	}

	public String getDescrizioneIniziativa() {
		return atto.getDescrizioneIniziativa();
	}

	public void setDescrizioneIniziativa(String descrizioneIniziativa) {
		this.atto.setDescrizioneIniziativa(descrizioneIniziativa);
	}

	public String getNumeroDGR() {
		return atto.getNumeroDgr();
	}

	public void setNumeroDGR(String numeroDGR) {
		this.atto.setNumeroDgr(numeroDGR);
	}

	public Date getDataDGR() {
		return atto.getDataDgr();
	}

	public void setDataDGR(Date dataDGR) {
		this.atto.setDataDgr(dataDGR);
	}

	public String getAssegnazione() {
		return atto.getAssegnazione();
	}

	public void setAssegnazione(String assegnazione) {
		this.atto.setAssegnazione(assegnazione);
	}

	public String getId() {
		return atto.getId();
	}

	public void setId(String id) {
		this.atto.setId(id);
	}

	public String getTipo() {
		return atto.getTipo();
	}

	public void setTipo(String tipo) {
		this.atto.setTipo(tipo);
	}

	public String getPrimoFirmatario() {
		return atto.getPrimoFirmatario();
	}

	public void setPrimoFirmatario(String primoFirmatario) {
		this.atto.setPrimoFirmatario(primoFirmatario);
	}

	public Date getDataPresentazione() {
		return atto.getDataPresentazione();
	}

	public void setDataPresentazione(Date dataPresentazione) {
		this.atto.setDataPresentazione(dataPresentazione);
	}

	public String getStato() {
		return atto.getStato();
	}

	public void setStato(String stato) {
		this.atto.setStato(stato);
	}

	public String getAnno() {
		return atto.getAnno();
	}

	public void setAnno(String anno) {
		this.atto.setAnno(anno);
	}

	public String getNumeroProtocollo() {
		return atto.getNumeroProtocollo();
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.atto.setNumeroProtocollo(numeroProtocollo);
	}

	public String getFirmatario() {
		return atto.getFirmatario();
	}

	public void setFirmatario(String firmatario) {
		this.atto.setFirmatario(firmatario);
	}

	public String getTipoChiusura() {
		return atto.getTipoChiusura();
	}

	public void setTipoChiusura(String tipoChiusura) {
		this.atto.setTipoChiusura(tipoChiusura);
	}

	public boolean isRedigente() {
		return atto.isRedigente();
	}

	public void setRedigente(boolean redigente) {
		this.atto.setRedigente(redigente);
	}

	public boolean isDeliberante() {
		return atto.isDeliberante();
	}

	public void setDeliberante(boolean deliberante) {
		this.atto.setDeliberante(deliberante);
	}

	public String getNumeroLR() {
		return atto.getNumeroLr();
	}

	public void setNumeroLR(String numeroLR) {
		this.atto.setNumeroLr(numeroLR);
	}

	public boolean isAbbinamento() {
		return atto.isAbbinamento();
	}

	public void setAbbinamento(boolean abbinamento) {
		this.atto.setAbbinamento(abbinamento);
	}

	public boolean isStralcio() {
		return atto.isStralcio();
	}

	public void setStralcio(boolean stralcio) {
		this.atto.setStralcio(stralcio);
	}

	public String getOrganismoStatutario() {
		return atto.getOrganismoStatutario();
	}

	public void setOrganismoStatutario(String organismoStatutario) {
		this.atto.setOrganismoStatutario(organismoStatutario);
	}

	public String getSoggettoConsultato() {
		return atto.getSoggettoConsultato();
	}

	public void setSoggettoConsultato(String soggettoConsultato) {
		this.atto.setSoggettoConsultato(soggettoConsultato);
	}

	public boolean isRinviato() {
		return atto.isRinviato();
	}

	public void setRinviato(boolean rinviato) {
		this.atto.setRinviato(rinviato);
	}

	public boolean isSospeso() {
		return atto.isSospeso();
	}

	public void setSospeso(boolean sospeso) {
		this.atto.setSospeso(sospeso);
	}

	public Date getDataLR() {
		return atto.getDataLR();
	}

	public void setDataLR(Date dataLR) {
		this.atto.setDataLR(dataLR);
	}

	public String getNumeroPubblicazioneBURL() {
		return atto.getNumeroPubblicazioneBURL();
	}

	public void setNumeroPubblicazioneBURL(String numeroPubblicazioneBURL) {
		this.atto.setNumeroPubblicazioneBURL(numeroPubblicazioneBURL);
	}

	public Date getDataPubblicazioneBURL() {
		return atto.getDataPubblicazioneBURL();
	}

	public void setDataPubblicazioneBURL(Date dataPubblicazioneBURL) {
		this.atto.setDataPubblicazioneBURL(dataPubblicazioneBURL);
	}

	public Date getDataChiusura() {
		return atto.getDataChiusura();
	}

	public void setDataChiusura(Date dataChiusura) {
		this.atto.setDataChiusura(dataChiusura);
	}

	public String getStatoChiusura() {
		return atto.getStatoChiusura();
	}

	public void setStatoChiusura(String statoChiusura) {
		this.atto.setStatoChiusura(statoChiusura);
	}

	public String getValutazioneAmmissibilita() {
		return atto.getValutazioneAmmissibilita();
	}

	public void setValutazioneAmmissibilita(String valutazioneAmmissibilita) {
		this.atto.setValutazioneAmmissibilita(valutazioneAmmissibilita);
	}

	public Date getDataRichiestaInformazioni() {
		return atto.getDataRichiestaInformazioni();
	}

	public void setDataRichiestaInformazioni(Date dataRichiestaInformazioni) {
		this.atto.setDataRichiestaInformazioni(dataRichiestaInformazioni);
	}

	public Date getDataRicevimentoInformazioni() {
		return atto.getDataRicevimentoInformazioni();
	}

	public void setDataRicevimentoInformazioni(Date dataRicevimentoInformazioni) {
		this.atto.setDataRicevimentoInformazioni(dataRicevimentoInformazioni);
	}

	public boolean isAiutiStato() {
		return atto.isAiutiStato();
	}

	public void setAiutiStato(boolean aiutiStato) {
		this.atto.setAiutiStato(aiutiStato);
	}

	public boolean isNormaFinanziaria() {
		return atto.isNormaFinanziaria();
	}

	public void setNormaFinanziaria(boolean normaFinanziaria) {
		this.atto.setNormaFinanziaria(normaFinanziaria);
	}

	public boolean isRichiestaUrgenza() {
		return atto.isRichiestaUrgenza();
	}

	public void setRichiestaUrgenza(boolean richiestaUrgenza) {
		this.atto.setRichiestaUrgenza(richiestaUrgenza);
	}

	public boolean isVotazioneUrgenza() {
		return atto.isVotazioneUrgenza();
	}

	public void setVotazioneUrgenza(boolean votazioneUrgenza) {
		this.atto.setVotazioneUrgenza(votazioneUrgenza);
	}

	public Date getDataVotazioneUrgenza() {
		return atto.getDataVotazioneUrgenza();
	}

	public void setDataVotazioneUrgenza(Date dataVotazioneUrgenza) {
		this.atto.setDataVotazioneUrgenza(dataVotazioneUrgenza);
	}

	public String getNoteAmmissibilita() {
		return atto.getNoteAmmissibilita();
	}

	public void setNoteAmmissibilita(String noteAmmissibilita) {
		this.atto.setNoteAmmissibilita(noteAmmissibilita);
	}

	public String getNoteNoteAllegatiPresentazioneAssegnazione() {
		return atto.getNotePresentazioneAssegnazione();
	}

	public void setNoteNoteAllegatiPresentazioneAssegnazione(String noteNoteAllegatiPresentazioneAssegnazione) {
		this.atto.setNotePresentazioneAssegnazione(noteNoteAllegatiPresentazioneAssegnazione);
	}

	public Date getDataAssegnazioneCommissioni() {
		return atto.getDataAssegnazioneCommissioni();
	}

	public void setDataAssegnazioneCommissioni(Date dataAssegnazioneCommissioni) {
		this.atto.setDataAssegnazioneCommissioni(dataAssegnazioneCommissioni);
	}

	public List<Abbinamento> getAbbinamenti() {
		return getLastPassaggio().getAbbinamenti();
	}

	public List<Abbinamento> getAbbinamentiAttivi() {

		abbinamentiAttivi.clear();

		for (Abbinamento element : getAbbinamenti()) {

			if (element.getDataDisabbinamento() == null) {

				abbinamentiAttivi.add((Abbinamento) element.clone());

			}
		}

		return abbinamentiAttivi;
	}

	public List<Commissione> getCommissioni() {
		return getLastPassaggio().getCommissioni();
	}

	public AttoMIS getAttoMIS() {
		return attoMIS;
	}

	public void setAttoMIS(AttoMIS attoMIS) {
		this.attoMIS = attoMIS;
	}

	public AttoEAC getAttoEAC() {
		return attoEAC;
	}

	public void setAttoEAC(AttoEAC attoEAC) {
		this.attoEAC = attoEAC;
	}

}
