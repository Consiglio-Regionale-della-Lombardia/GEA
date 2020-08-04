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
package com.sourcesense.crl.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.springframework.beans.factory.annotation.Configurable;

import com.sourcesense.crl.util.JsonDateSerializer;
import com.sourcesense.crl.util.JsonNoteDeserializer;

/**
 * Atto rappresentato dalle informazioni di pubblico,atto proseguente ,id; ,nome
 * ,tipo ,codice ,oggetto ,oggetto originale ,primo firmatario ,data
 * presentazione ,data pubblicazione ,data seduta sc ,data iniziativa,estensione
 * atto,stato ,numero atto ,tipo atto ,tipologia ,legislatura ,anno ,error
 * ,stato attuale ,classificazione ,n repertorio ,data repertorio ,numero
 * repertorio ,url fascicolo ,descrizione iniziativa ,numero Dgr ,data Dgr
 * ,assegnazione ,data assegnazione ,esito validazione ,data validazione ,data
 * assegnazione commissioni ,numero protocollo ,tipo iniziativa ,tipo iniziativa
 * nome ,firmatario ,tipo chiusura ,redigente ,deliberante ,numero Lr
 * ,abbinamento ,stralcio ,organismo statutario ,soggetto consultato ,rinviato
 * ,sospeso ,data LR ,numero pubblicazione BURL ,data pubblicazione BURL ,data
 * chiusura ,stato chiusura ,organi ,firmatari ,elenco firmatari ,relatori
 * ,relatore ,data nomina relatore ,consultazioni ,allegati ,elenco abbinamenti
 * ,esitoVotazione commissione referente ,dataVotazione commissione ,data
 * scadenza ,data richiesta iscrizione aula ,esito votazione aula ,data
 * votazione aula ,numero Dcr ,numero Lcr ,links presentazione assegnazione
 * ,organismi statutari ,testi atto ,collegamenti ,collegamenti atti sindacato
 * ,collegamenti leggi regionali ,passaggi ,note collegamenti ,commissioni non
 * consultive ,commissioni ,consultive,data presa in carico ,valutazione
 * ammissibilità ,data richiesta informazioni ,data ricevimento informazioni
 * ,aiuti stato ,norma finanziaria ,richiesta urgenza ,votazione urgenza ,data
 * votazione urgenza ,note ammissibilita ,note presentazione,
 * assegnazione,scadenza 60 gg ,iter aula,note chiusura iter,numero Dgr seguito
 * ,data Dgr seguito ,num regolamento ,data regolamento e sedute atto
 * 
 * @author sourcesense
 *
 */
@Configurable()
@XmlRootElement(name = "atto")
@JsonRootName("atto")
@JsonTypeName("atto")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class Atto implements Cloneable, Comparable<Atto> {

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int compareTo(Atto arg0) {
		return Integer.parseInt(arg0.numeroAtto) - Integer.parseInt(this.numeroAtto);
	}

	private boolean pubblico;

	private boolean attoProseguente;

	private String id;
	private String nome;
	private String tipo;
	private String codice;
	private String oggetto;
	private String oggettoOriginale;
	private String primoFirmatario;
	private Date dataPresentazione;
	private Date dataPubblicazione;
	private Date dataSedutaSc;
	private Date dataIniziativa;

	private String estensioneAtto;

	private String stato;
	private String numeroAtto;
	private String tipoAtto;
	private String tipologia;
	private String legislatura;
	private String anno;
	private String error;

	private String statoAttuale;
	private String classificazione;
	private String nRepertorio;
	private Date dataRepertorio;
	private String numeroRepertorio;
	private String urlFascicolo;
	private String descrizioneIniziativa;
	private String numeroDgr;
	private Date dataDgr;
	private String assegnazione;
	private Date dataAssegnazione;
	private String esitoValidazione;
	private Date dataValidazione;
	private Date dataAssegnazioneCommissioni;

	private String numeroProtocollo;
	private String tipoIniziativa;
	private String tipoIniziativaNome;
	private String firmatario;
	private String tipoChiusura;

	private boolean redigente;
	private boolean deliberante;
	private String numeroLr;
	private boolean abbinamento;
	private boolean stralcio;
	private String organismoStatutario;
	private String soggettoConsultato;
	private boolean rinviato;
	private boolean sospeso;
	private Date dataLR;
	private String numeroPubblicazioneBURL;
	private Date dataPubblicazioneBURL;
	private Date dataChiusura;
	private String statoChiusura;

	private List<Organo> organi = new ArrayList<Organo>();
	private List<Firmatario> firmatari = new ArrayList<Firmatario>();
	private String elencoFirmatari;
	private List<Relatore> relatori = new ArrayList<Relatore>();
	private String relatore;
	private Date dataNominaRelatore;
	private List<Consultazione> consultazioni = new ArrayList<Consultazione>();
	private List<Allegato> allegati = new ArrayList<Allegato>();

	private String elencoAbbinamenti;
	private String esitoVotazioneCommissioneReferente;
	private Date dataVotazioneCommissione;
	private Date dataScadenza;
	private Date dataRichiestaIscrizioneAula;
	private String esitoVotazioneAula;
	private Date dataVotazioneAula;
	private String numeroDcr;
	private String numeroLcr;

	private List<Link> linksPresentazioneAssegnazione = new ArrayList<Link>();

	private List<OrganismoStatutario> organismiStatutari = new ArrayList<OrganismoStatutario>();
	private List<TestoAtto> testiAtto = new ArrayList<TestoAtto>();

	private List<Collegamento> collegamenti = new ArrayList<Collegamento>();
	private List<CollegamentoAttiSindacato> collegamentiAttiSindacato = new ArrayList<CollegamentoAttiSindacato>();
	private List<CollegamentoLeggiRegionali> collegamentiLeggiRegionali = new ArrayList<CollegamentoLeggiRegionali>();
	private List<Passaggio> passaggi = new ArrayList<Passaggio>();
	private String noteCollegamenti;
	private String commissioniNonConsultive;
	private String commissioniConsultive;

	private Date dataPresaInCarico;
	private String valutazioneAmmissibilita;
	private Date dataRichiestaInformazioni;
	private Date dataRicevimentoInformazioni;
	private boolean aiutiStato;
	private boolean normaFinanziaria;
	private boolean richiestaUrgenza;
	private boolean votazioneUrgenza;
	private Date dataVotazioneUrgenza;
	private String noteAmmissibilita;
	private String notePresentazioneAssegnazione;

	private boolean scadenza60gg;
	private boolean iterAula;

	private String noteChiusuraIter;

	private String numeroDgrSeguito;
	private Date dataDgrSeguito;

	private String numRegolamento;
	private Date dataRegolamento;

	private List<SedutaAtto> seduteAtto = new ArrayList<SedutaAtto>();

	private boolean summary;

	public Atto() {
		Passaggio passaggio = new Passaggio();
		passaggio.setNome("Passaggio1");
		passaggi.add(passaggio);
	}

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getOggetto() {

		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getOldOggetto() {
		return oggetto;
	}

	public void setOldOggetto(String oldOggetto) {
		this.oggetto = oldOggetto;
	}

	public String getOggettoOriginale() {
		return oggettoOriginale;
	}

	public void setOggettoOriginale(String oggettoOriginale) {
		this.oggettoOriginale = oggettoOriginale;
	}

	public String getPrimoFirmatario() {

		return primoFirmatario;
	}

	public void setPrimoFirmatario(String primoFirmatario) {
		this.primoFirmatario = primoFirmatario;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPubblicazione() {
		return dataPubblicazione;
	}

	public void setDataPubblicazione(Date dataPubblicazione) {
		this.dataPubblicazione = dataPubblicazione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaSc() {
		return dataSedutaSc;
	}

	public void setDataSedutaSc(Date dataSedutaSc) {
		this.dataSedutaSc = dataSedutaSc;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getNumeroAtto() {
		return numeroAtto;
	}

	public void setNumeroAtto(String numeroAtto) {
		this.numeroAtto = numeroAtto;
	}

	public String getTipoAtto() {
		return tipoAtto;
	}

	public void setTipoAtto(String tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getLegislatura() {
		return legislatura;
	}

	public void setLegislatura(String legislatura) {
		this.legislatura = legislatura;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getStatoAttuale() {
		return statoAttuale;
	}

	public void setStatoAttuale(String statoAttuale) {
		this.statoAttuale = statoAttuale;
	}

	public String getClassificazione() {
		return classificazione;
	}

	public void setClassificazione(String classificazione) {
		this.classificazione = classificazione;
	}

	public String getnRepertorio() {
		return nRepertorio;
	}

	public void setnRepertorio(String nRepertorio) {
		this.nRepertorio = nRepertorio;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRepertorio() {
		return dataRepertorio;
	}

	public void setDataRepertorio(Date dataRepertorio) {
		this.dataRepertorio = dataRepertorio;
	}

	public String getNumeroRepertorio() {
		return numeroRepertorio;
	}

	public void setNumeroRepertorio(String numeroRepertorio) {
		this.numeroRepertorio = numeroRepertorio;
	}

	public String getUrlFascicolo() {
		return urlFascicolo;
	}

	public void setUrlFascicolo(String urlFascicolo) {
		this.urlFascicolo = urlFascicolo;
	}

	public String getDescrizioneIniziativa() {
		return descrizioneIniziativa;
	}

	public void setDescrizioneIniziativa(String descrizioneIniziativa) {
		this.descrizioneIniziativa = descrizioneIniziativa;
	}

	public String getNumeroDgr() {
		return numeroDgr;
	}

	public void setNumeroDgr(String numeroDgr) {
		this.numeroDgr = numeroDgr;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataDgr() {
		return dataDgr;
	}

	public void setDataDgr(Date dataDgr) {
		this.dataDgr = dataDgr;
	}

	public String getAssegnazione() {
		return assegnazione;
	}

	public void setAssegnazione(String assegnazione) {
		this.assegnazione = assegnazione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazione() {
		return dataAssegnazione;
	}

	public void setDataAssegnazione(Date dataAssegnazione) {
		this.dataAssegnazione = dataAssegnazione;
	}

	public String getEsitoValidazione() {
		return esitoValidazione;
	}

	public void setEsitoValidazione(String esitoValidazione) {
		this.esitoValidazione = esitoValidazione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataValidazione() {
		return dataValidazione;
	}

	public void setDataValidazione(Date dataValidazione) {
		this.dataValidazione = dataValidazione;
	}

	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public String getTipoIniziativa() {
		return tipoIniziativa;
	}

	public void setTipoIniziativa(String tipoIniziativa) {
		this.tipoIniziativa = tipoIniziativa;
		this.tipoIniziativaNome = this.getTipoIniziativaNome();
	}

	public String getFirmatario() {
		return firmatario;
	}

	public void setFirmatario(String firmatario) {
		this.firmatario = firmatario;
	}

	public String getTipoChiusura() {
		return tipoChiusura;
	}

	public void setTipoChiusura(String tipoChiusura) {
		this.tipoChiusura = tipoChiusura;
	}

	public boolean isRedigente() {
		return redigente;
	}

	public void setRedigente(boolean redigente) {
		this.redigente = redigente;
	}

	public boolean isDeliberante() {
		return deliberante;
	}

	public void setDeliberante(boolean deliberante) {
		this.deliberante = deliberante;
	}

	public String getNumeroLr() {
		return numeroLr;
	}

	public void setNumeroLr(String numeroLr) {
		this.numeroLr = numeroLr;
	}

	public boolean isAbbinamento() {
		return abbinamento;
	}

	public void setAbbinamento(boolean abbinamento) {
		this.abbinamento = abbinamento;
	}

	public boolean isStralcio() {
		return stralcio;
	}

	public void setStralcio(boolean stralcio) {
		this.stralcio = stralcio;
	}

	public String getOrganismoStatutario() {
		return organismoStatutario;
	}

	public void setOrganismoStatutario(String organismoStatutario) {
		this.organismoStatutario = organismoStatutario;
	}

	public String getSoggettoConsultato() {
		return soggettoConsultato;
	}

	public void setSoggettoConsultato(String soggettoConsultato) {
		this.soggettoConsultato = soggettoConsultato;
	}

	public boolean isRinviato() {
		return rinviato;
	}

	public void setRinviato(boolean rinviato) {
		this.rinviato = rinviato;
	}

	public boolean isSospeso() {
		return sospeso;
	}

	public void setSospeso(boolean sospeso) {
		this.sospeso = sospeso;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataLR() {
		return dataLR;
	}

	public void setDataLR(Date dataLR) {
		this.dataLR = dataLR;
	}

	public String getNumeroPubblicazioneBURL() {
		return numeroPubblicazioneBURL;
	}

	public void setNumeroPubblicazioneBURL(String numeroPubblicazioneBURL) {
		this.numeroPubblicazioneBURL = numeroPubblicazioneBURL;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPubblicazioneBURL() {
		return dataPubblicazioneBURL;
	}

	public void setDataPubblicazioneBURL(Date dataPubblicazioneBURL) {
		this.dataPubblicazioneBURL = dataPubblicazioneBURL;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataChiusura() {
		return dataChiusura;
	}

	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public String getStatoChiusura() {
		return statoChiusura;
	}

	public void setStatoChiusura(String statoChiusura) {
		this.statoChiusura = statoChiusura;
	}

	public List<Organo> getOrgani() {
		return organi;
	}

	public void setOrgani(List<Organo> organi) {
		this.organi = organi;
	}

	public List<Firmatario> getFirmatari() {
		return firmatari;
	}

	public void setFirmatari(List<Firmatario> firmatari) {
		this.firmatari = firmatari;
	}

	public List<Relatore> getRelatori() {
		return relatori;
	}

	public void setRelatori(List<Relatore> relatori) {
		this.relatori = relatori;
	}

	public List<Consultazione> getConsultazioni() {
		return consultazioni;
	}

	public void setConsultazioni(List<Consultazione> consultazioni) {
		this.consultazioni = consultazioni;
	}

	public List<Allegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<Allegato> allegati) {
		this.allegati = allegati;
	}

	/*
	 * public List<Allegato> getAllegatiNotePresentazioneAssegnazione() { return
	 * allegatiNotePresentazioneAssegnazione; }
	 * 
	 * public void setAllegatiNotePresentazioneAssegnazione( List<Allegato>
	 * allegatiNotePresentazioneAssegnazione) {
	 * this.allegatiNotePresentazioneAssegnazione =
	 * allegatiNotePresentazioneAssegnazione; }
	 */

	public List<Link> getLinksPresentazioneAssegnazione() {
		return linksPresentazioneAssegnazione;
	}

	public void setLinksPresentazioneAssegnazione(List<Link> linksPresentazioneAssegnazione) {
		this.linksPresentazioneAssegnazione = linksPresentazioneAssegnazione;
	}

	public List<OrganismoStatutario> getOrganismiStatutari() {
		return organismiStatutari;
	}

	public void setOrganismiStatutari(List<OrganismoStatutario> organismiStatutari) {
		this.organismiStatutari = organismiStatutari;
	}

	public List<TestoAtto> getTestiAtto() {
		return testiAtto;
	}

	public void setTestiAtto(List<TestoAtto> testiAtto) {
		this.testiAtto = testiAtto;
	}

	public String getValutazioneAmmissibilita() {
		return valutazioneAmmissibilita;
	}

	public void setValutazioneAmmissibilita(String valutazioneAmmissibilita) {
		this.valutazioneAmmissibilita = valutazioneAmmissibilita;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRichiestaInformazioni() {
		return dataRichiestaInformazioni;
	}

	public void setDataRichiestaInformazioni(Date dataRichiestaInformazioni) {
		this.dataRichiestaInformazioni = dataRichiestaInformazioni;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRicevimentoInformazioni() {
		return dataRicevimentoInformazioni;
	}

	public void setDataRicevimentoInformazioni(Date dataRicevimentoInformazioni) {
		this.dataRicevimentoInformazioni = dataRicevimentoInformazioni;
	}

	public boolean isAiutiStato() {
		return aiutiStato;
	}

	public void setAiutiStato(boolean aiutiStato) {
		this.aiutiStato = aiutiStato;
	}

	public boolean isNormaFinanziaria() {
		return normaFinanziaria;
	}

	public void setNormaFinanziaria(boolean normaFinanziaria) {
		this.normaFinanziaria = normaFinanziaria;
	}

	public boolean isRichiestaUrgenza() {
		return richiestaUrgenza;
	}

	public void setRichiestaUrgenza(boolean richiestaUrgenza) {
		this.richiestaUrgenza = richiestaUrgenza;
	}

	public boolean isVotazioneUrgenza() {
		return votazioneUrgenza;
	}

	public void setVotazioneUrgenza(boolean votazioneUrgenza) {
		this.votazioneUrgenza = votazioneUrgenza;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataVotazioneUrgenza() {
		return dataVotazioneUrgenza;
	}

	public void setDataVotazioneUrgenza(Date dataVotazioneUrgenza) {
		this.dataVotazioneUrgenza = dataVotazioneUrgenza;
	}

	public String getNoteAmmissibilita() {
		return noteAmmissibilita;
	}

	@JsonDeserialize(using = JsonNoteDeserializer.class)
	public void setNoteAmmissibilita(String noteAmmissibilita) {
		this.noteAmmissibilita = noteAmmissibilita;
	}

	public String getNotePresentazioneAssegnazione() {
		return notePresentazioneAssegnazione;
	}

	@JsonDeserialize(using = JsonNoteDeserializer.class)
	public void setNotePresentazioneAssegnazione(String notePresentazioneAssegnazione) {
		this.notePresentazioneAssegnazione = notePresentazioneAssegnazione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPresaInCarico() {
		return dataPresaInCarico;
	}

	public void setDataPresaInCarico(Date dataPresaInCarico) {
		this.dataPresaInCarico = dataPresaInCarico;
	}

	public String getNoteChiusuraIter() {
		return noteChiusuraIter;
	}

	@JsonDeserialize(using = JsonNoteDeserializer.class)
	public void setNoteChiusuraIter(String noteChiusuraIter) {
		this.noteChiusuraIter = noteChiusuraIter;
	}

	public List<Collegamento> getCollegamenti() {
		return collegamenti;
	}

	public void setCollegamenti(List<Collegamento> collegamenti) {
		this.collegamenti = collegamenti;
	}

	public List<CollegamentoAttiSindacato> getCollegamentiAttiSindacato() {
		return collegamentiAttiSindacato;
	}

	public void setCollegamentiAttiSindacato(List<CollegamentoAttiSindacato> collegamentiAttiSindacato) {
		this.collegamentiAttiSindacato = collegamentiAttiSindacato;
	}

	public List<CollegamentoLeggiRegionali> getCollegamentiLeggiRegionali() {
		return collegamentiLeggiRegionali;
	}

	public void setCollegamentiLeggiRegionali(List<CollegamentoLeggiRegionali> collegamentiLeggiRegionali) {
		this.collegamentiLeggiRegionali = collegamentiLeggiRegionali;
	}

	public List<Passaggio> getPassaggi() {
		return passaggi;
	}

	public void setPassaggi(List<Passaggio> passaggi) {
		this.passaggi = passaggi;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataIniziativa() {
		return dataIniziativa;
	}

	public void setDataIniziativa(Date dataIniziativa) {
		this.dataIniziativa = dataIniziativa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazioneCommissioni() {
		return dataAssegnazioneCommissioni;
	}

	public void setDataAssegnazioneCommissioni(Date dataAssegnazioneCommissioni) {
		this.dataAssegnazioneCommissioni = dataAssegnazioneCommissioni;
	}

	public String getElencoFirmatari() {

		return elencoFirmatari;
	}

	public void setElencoFirmatari(String elencoFirmatari) {
		this.elencoFirmatari = elencoFirmatari;
	}

	public String getCommissioniNonConsultive() {

		return commissioniNonConsultive;
	}

	public void setCommissioniNonConsultive(String commissioniNonConsultive) {
		this.commissioniNonConsultive = commissioniNonConsultive;
	}

	public String getCommissioniConsultive() {

		return commissioniConsultive;
	}

	public void setCommissioniConsultive(String commissioniConsultive) {
		this.commissioniConsultive = commissioniConsultive;
	}

	public String getRelatore() {

		return relatore;
	}

	public void setRelatore(String relatore) {
		this.relatore = relatore;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataNominaRelatore() {
		return dataNominaRelatore;
	}

	public void setDataNominaRelatore(Date dataNominaRelatore) {
		this.dataNominaRelatore = dataNominaRelatore;
	}

	public String getElencoAbbinamenti() {
		return elencoAbbinamenti;
	}

	public void setElencoAbbinamenti(String elencoAbbinamenti) {
		this.elencoAbbinamenti = elencoAbbinamenti;
	}

	public String getEsitoVotazioneCommissioneReferente() {
		return esitoVotazioneCommissioneReferente;
	}

	public void setEsitoVotazioneCommissioneReferente(String esitoVotazioneCommissioneReferente) {
		this.esitoVotazioneCommissioneReferente = esitoVotazioneCommissioneReferente;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataVotazioneCommissione() {
		return dataVotazioneCommissione;
	}

	public void setDataVotazioneCommissione(Date dataVotazioneCommissione) {
		this.dataVotazioneCommissione = dataVotazioneCommissione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRichiestaIscrizioneAula() {
		return dataRichiestaIscrizioneAula;
	}

	public void setDataRichiestaIscrizioneAula(Date dataRichiestaIscrizioneAula) {
		this.dataRichiestaIscrizioneAula = dataRichiestaIscrizioneAula;
	}

	public String getEsitoVotazioneAula() {
		return esitoVotazioneAula;
	}

	public void setEsitoVotazioneAula(String esitoVotazioneAula) {
		this.esitoVotazioneAula = esitoVotazioneAula;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataVotazioneAula() {
		return dataVotazioneAula;
	}

	public void setDataVotazioneAula(Date dataVotazioneAula) {
		this.dataVotazioneAula = dataVotazioneAula;
	}

	public String getNumeroDcr() {
		return numeroDcr;
	}

	public void setNumeroDcr(String numeroDcr) {
		this.numeroDcr = numeroDcr;
	}

	public String getNumeroLcr() {
		return numeroLcr;
	}

	public void setNumeroLcr(String numeroLcr) {
		this.numeroLcr = numeroLcr;
	}

	public boolean isPubblico() {
		return pubblico;
	}

	public void setPubblico(boolean pubblico) {
		this.pubblico = pubblico;
	}

	public String getEstensioneAtto() {
		return estensioneAtto;
	}

	public void setEstensioneAtto(String estensioneAtto) {
		this.estensioneAtto = estensioneAtto;
	}

	public boolean isIterAula() {
		return iterAula;
	}

	public void setIterAula(boolean iterAula) {
		this.iterAula = iterAula;
	}

	public boolean isScadenza60gg() {
		return scadenza60gg;
	}

	public void setScadenza60gg(boolean scadenza60gg) {
		this.scadenza60gg = scadenza60gg;
	}

	public String getNumeroDgrSeguito() {
		return numeroDgrSeguito;
	}

	public void setNumeroDgrSeguito(String numeroDgrSeguito) {
		this.numeroDgrSeguito = numeroDgrSeguito;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataDgrSeguito() {
		return dataDgrSeguito;
	}

	public void setDataDgrSeguito(Date dataDgrSeguito) {
		this.dataDgrSeguito = dataDgrSeguito;
	}

	public List<SedutaAtto> getSeduteAtto() {

		return seduteAtto;

	}

	public void setSeduteAtto(List<SedutaAtto> seduteAtto) {
		this.seduteAtto = seduteAtto;
	}

	public String getNumRegolamento() {
		return numRegolamento;
	}

	public void setNumRegolamento(String numRegolamento) {
		this.numRegolamento = numRegolamento;
	}

	public String getNoteCollegamenti() {
		return noteCollegamenti;
	}

	public void setNoteCollegamenti(String noteCollegamenti) {
		this.noteCollegamenti = noteCollegamenti;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRegolamento() {
		return dataRegolamento;
	}

	public void setDataRegolamento(Date dataRegolamento) {
		this.dataRegolamento = dataRegolamento;
	}

	public String getTipoIniziativaNome() {
		if ("01_ATTO DI INIZIATIVA CONSILIARE".equals(tipoIniziativa)) {

			return "Consiliare";

		} else if ("03_ATTO DI INIZIATIVA POPOLARE".equals(tipoIniziativa)) {

			return "Popolare";

		} else if ("05_ATTO DI INIZIATIVA UFFICIO PRESIDENZA".equals(tipoIniziativa)) {

			return "Ufficio di Presidenza";

		} else if ("07_ATTO DI INIZIATIVA AUTONOMIE LOCALI".equals(tipoIniziativa)) {

			return "Consiglio delle Autonomie locali";

		} else if ("06_ATTO DI INIZIATIVA PRESIDENTE GIUNTA".equals(tipoIniziativa)) {

			return "Presidente della Giunta";

		} else if ("02_ATTO DI INIZIATIVA DI GIUNTA".equals(tipoIniziativa)) {

			return "Giunta";

		} else if ("04_ATTO DI INIZIATIVA COMMISSIONI".equals(tipoIniziativa)) {

			return "Commissioni";

		} else if ("08_ATTO DI ALTRA INIZIATIVA".equals(tipoIniziativa)) {

			return "Altra Iniziativa";

		}

		return null;

	}

	public void setTipoIniziativaNome(String tipoIniziativaNome) {
		this.tipoIniziativaNome = tipoIniziativaNome;
	}

	public boolean isAttoProseguente() {
		return attoProseguente;
	}

	public void setAttoProseguente(boolean attoProseguente) {
		this.attoProseguente = attoProseguente;
	}

	public boolean isSummary() {
		return summary;
	}

	public void setSummary(boolean isDetailed) {
		this.summary = isDetailed;
	}
}
