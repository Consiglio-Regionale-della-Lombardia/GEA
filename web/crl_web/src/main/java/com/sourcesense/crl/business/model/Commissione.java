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

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.sourcesense.crl.util.JsonDateSerializer;
import com.sourcesense.crl.util.JsonIntegerDeserializer;
import com.sourcesense.crl.util.JsonNoteDeserializer;

/**
 * Commissione con stati e ruoli
 * 
 * @author sourcesense
 *
 */
@JsonRootName("commissione")
@JsonTypeName("commissione")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class Commissione implements Cloneable {

	public static final String STATO_PROPOSTO = "Proposto";
	public static final String STATO_ASSEGNATO = "Assegnato";
	public static final String STATO_IN_CARICO = "In Carico";
	public static final String STATO_ANNULLATO = "Annullato";
	public static final String STATO_VOTATO = "Votato";
	public static final String STATO_NOMINATO_RELATORE = "Nominato Relatore";
	public static final String STATO_COMITATO_RISTRETTO = "Lavori Comitato Ristretto";
	public static final String STATO_TRASMESSO = "Trasmesso";
	public static final String STATO_VERIFICATA_AMMISSIBILITA = "Verificata Ammissibilità";

	public static final String RUOLO_REFERENTE = "Referente";
	public static final String RUOLO_COREFERENTE = "Co-Referente";
	public static final String RUOLO_CONSULTIVA = "Consultiva";
	public static final String RUOLO_REDIGENTE = "Redigente";
	public static final String RUOLO_DELIBERANTE = "Deliberante";

	private String commissione;
	private String descrizione;
	private String nome;
	private String ruolo;
	private String stato;

	private String esitoVotazione;
	private String materia;
	private String esitoVotoCommissioneReferente;
	private String quorumEsameCommissioni;
	private String motivazioniContinuazioneInReferente;
	private String noteEmendamentiEsameCommissioni;
	private String noteGeneraliEsameCommissione;
	private String esitoVotazioneIntesa;
	private String noteClausolaValutativa;

	private Date dataAssegnazione;
	private Date dataPresaInCarico;
	private Date dataVotazione;
	private Date dataTrasmissione;
	private Date dataNomina;
	private Date dataFineLavoriComitato;
	private Date dataScadenzaEsameCommissioni;
	private Date dataProposta;
	private Date dataAnnullo;
	private Date dataScadenza;
	private Date dataSedutaCommissione;
	private Date dataSedutaContinuazioneInReferente;
	private Date dataIstituzioneComitato;
	private Date dataRichiestaIscrizioneAula;
	private Date dataPresaInCaricoProposta;
	private Date dataIntesa;

	private List<Relatore> relatori = new ArrayList<Relatore>();
	private List<Link> linksNoteEsameCommissione = new ArrayList<Link>();
	private List<TestoAtto> testiAttoVotatoEsameCommissioni = new ArrayList<TestoAtto>();
	private List<Allegato> emendamentiEsameCommissioni = new ArrayList<Allegato>();
	private List<Allegato> allegatiNoteEsameCommissioni = new ArrayList<Allegato>();
	private List<Allegato> testiClausola = new ArrayList<Allegato>();
	private List<Allegato> allegati = new ArrayList<Allegato>();

	private boolean annullata = false;
	private boolean presenzaComitatoRistretto;
	private boolean passaggioDirettoInAula;

	private Integer numEmendPresentatiMaggiorEsameCommissioni;
	private Integer numEmendPresentatiMinorEsameCommissioni;
	private Integer numEmendPresentatiGiuntaEsameCommissioni;
	private Integer numEmendPresentatiMistoEsameCommissioni;
	private Integer numEmendApprovatiMaggiorEsameCommissioni;
	private Integer numEmendApprovatiMinorEsameCommissioni;
	private Integer numEmendApprovatiGiuntaEsameCommissioni;
	private Integer numEmendApprovatiMistoEsameCommissioni;
	private Integer nonAmmissibiliEsameCommissioni;
	private Integer decadutiEsameCommissioni;
	private Integer ritiratiEsameCommissioni;
	private Integer respintiEsameCommissioni;

	private Date dataSedutaStralcio;
	private Date dataIniziativaStralcio;
	private Date dataStralcio;
	private String articoli;
	private String noteStralcio;
	private String quorumStralcio;

	private boolean sospensioneFeriale;
	private Date dataInterruzione;
	private Date dataRicezioneIntegrazioni;

	private Date dataCalendarizzazione;

	private Date dataDcr;
	private String numeroDcr;

	private Date dataRis;
	private String numeroRis;

	private ComitatoRistretto comitatoRistretto = new ComitatoRistretto();

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Allegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<Allegato> allegati) {
		this.allegati = allegati;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataScadenzaEsameCommissioni() {
		return dataScadenzaEsameCommissioni;
	}

	public void setDataScadenzaEsameCommissioni(Date dataScadenzaEsameCommissioni) {
		this.dataScadenzaEsameCommissioni = dataScadenzaEsameCommissioni;
	}

	public List<TestoAtto> getTestiAttoVotatoEsameCommissioni() {
		return testiAttoVotatoEsameCommissioni;
	}

	public void setTestiAttoVotatoEsameCommissioni(List<TestoAtto> testiAttoVotatoEsameCommissioni) {
		this.testiAttoVotatoEsameCommissioni = testiAttoVotatoEsameCommissioni;
	}

	public List<Allegato> getEmendamentiEsameCommissioni() {
		return emendamentiEsameCommissioni;
	}

	public void setEmendamentiEsameCommissioni(List<Allegato> emendamentiEsameCommissioni) {
		this.emendamentiEsameCommissioni = emendamentiEsameCommissioni;
	}

	public String getEsitoVotoCommissioneReferente() {
		return esitoVotoCommissioneReferente;
	}

	public void setEsitoVotoCommissioneReferente(String esitoVotoCommissioneReferente) {
		this.esitoVotoCommissioneReferente = esitoVotoCommissioneReferente;
	}

	public String getQuorumEsameCommissioni() {
		return quorumEsameCommissioni;
	}

	public void setQuorumEsameCommissioni(String quorumEsameCommissioni) {
		this.quorumEsameCommissioni = quorumEsameCommissioni;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaCommissione() {
		return dataSedutaCommissione;
	}

	public void setDataSedutaCommissione(Date dataSedutaCommissione) {
		this.dataSedutaCommissione = dataSedutaCommissione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaContinuazioneInReferente() {
		return dataSedutaContinuazioneInReferente;
	}

	public void setDataSedutaContinuazioneInReferente(Date dataSedutaContinuazioneInReferente) {
		this.dataSedutaContinuazioneInReferente = dataSedutaContinuazioneInReferente;
	}

	public String getMotivazioniContinuazioneInReferente() {
		return motivazioniContinuazioneInReferente;
	}

	public void setMotivazioniContinuazioneInReferente(String motivazioniContinuazioneInReferente) {
		this.motivazioniContinuazioneInReferente = motivazioniContinuazioneInReferente;
	}

	public Integer getNumEmendPresentatiMaggiorEsameCommissioni() {
		return numEmendPresentatiMaggiorEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendPresentatiMaggiorEsameCommissioni(Integer numEmendPresentatiMaggiorEsameCommissioni) {
		this.numEmendPresentatiMaggiorEsameCommissioni = numEmendPresentatiMaggiorEsameCommissioni;
	}

	public Integer getNumEmendPresentatiMinorEsameCommissioni() {
		return numEmendPresentatiMinorEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendPresentatiMinorEsameCommissioni(Integer numEmendPresentatiMinorEsameCommissioni) {
		this.numEmendPresentatiMinorEsameCommissioni = numEmendPresentatiMinorEsameCommissioni;
	}

	public Integer getNumEmendPresentatiGiuntaEsameCommissioni() {
		return numEmendPresentatiGiuntaEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendPresentatiGiuntaEsameCommissioni(Integer numEmendPresentatiGiuntaEsameCommissioni) {
		this.numEmendPresentatiGiuntaEsameCommissioni = numEmendPresentatiGiuntaEsameCommissioni;
	}

	public Integer getNumEmendPresentatiMistoEsameCommissioni() {
		return numEmendPresentatiMistoEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendPresentatiMistoEsameCommissioni(Integer numEmendPresentatiMistoEsameCommissioni) {
		this.numEmendPresentatiMistoEsameCommissioni = numEmendPresentatiMistoEsameCommissioni;
	}

	public Integer getNumEmendApprovatiMaggiorEsameCommissioni() {
		return numEmendApprovatiMaggiorEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendApprovatiMaggiorEsameCommissioni(Integer numEmendApprovatiMaggiorEsameCommissioni) {
		this.numEmendApprovatiMaggiorEsameCommissioni = numEmendApprovatiMaggiorEsameCommissioni;
	}

	public Integer getNumEmendApprovatiMinorEsameCommissioni() {
		return numEmendApprovatiMinorEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendApprovatiMinorEsameCommissioni(Integer numEmendApprovatiMinorEsameCommissioni) {
		this.numEmendApprovatiMinorEsameCommissioni = numEmendApprovatiMinorEsameCommissioni;
	}

	public Integer getNumEmendApprovatiGiuntaEsameCommissioni() {
		return numEmendApprovatiGiuntaEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendApprovatiGiuntaEsameCommissioni(Integer numEmendApprovatiGiuntaEsameCommissioni) {
		this.numEmendApprovatiGiuntaEsameCommissioni = numEmendApprovatiGiuntaEsameCommissioni;
	}

	public Integer getNumEmendApprovatiMistoEsameCommissioni() {
		return numEmendApprovatiMistoEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendApprovatiMistoEsameCommissioni(Integer numEmendApprovatiMistoEsameCommissioni) {
		this.numEmendApprovatiMistoEsameCommissioni = numEmendApprovatiMistoEsameCommissioni;
	}

	public Integer getNonAmmissibiliEsameCommissioni() {
		return nonAmmissibiliEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNonAmmissibiliEsameCommissioni(Integer nonAmmissibiliEsameCommissioni) {
		this.nonAmmissibiliEsameCommissioni = nonAmmissibiliEsameCommissioni;
	}

	public Integer getDecadutiEsameCommissioni() {
		return decadutiEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setDecadutiEsameCommissioni(Integer decadutiEsameCommissioni) {
		this.decadutiEsameCommissioni = decadutiEsameCommissioni;
	}

	public Integer getRitiratiEsameCommissioni() {
		return ritiratiEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setRitiratiEsameCommissioni(Integer ritiratiEsameCommissioni) {
		this.ritiratiEsameCommissioni = ritiratiEsameCommissioni;
	}

	public Integer getRespintiEsameCommissioni() {
		return respintiEsameCommissioni;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setRespintiEsameCommissioni(Integer respintiEsameCommissioni) {
		this.respintiEsameCommissioni = respintiEsameCommissioni;
	}

	public String getNoteEmendamentiEsameCommissioni() {
		return noteEmendamentiEsameCommissioni;
	}

	public void setNoteEmendamentiEsameCommissioni(String noteEmendamentiEsameCommissioni) {
		this.noteEmendamentiEsameCommissioni = noteEmendamentiEsameCommissioni;
	}

	public List<Allegato> getAllegatiNoteEsameCommissioni() {
		return allegatiNoteEsameCommissioni;
	}

	public void setAllegatiNoteEsameCommissioni(List<Allegato> allegatiNoteEsameCommissioni) {
		this.allegatiNoteEsameCommissioni = allegatiNoteEsameCommissioni;
	}

	public String getCommissione() {
		return commissione;
	}

	public void setCommissione(String commissione) {
		this.commissione = commissione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazione() {
		return dataAssegnazione;
	}

	public void setDataAssegnazione(Date dataAssegnazione) {
		this.dataAssegnazione = dataAssegnazione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPresaInCarico() {
		return dataPresaInCarico;
	}

	public void setDataPresaInCarico(Date dataPresaInCarico) {
		this.dataPresaInCarico = dataPresaInCarico;
	}

	/*
	 * public String getTipoVotazione() { return tipoVotazione; }
	 * 
	 * public void setTipoVotazione(String tipoVotazione) { this.tipoVotazione =
	 * tipoVotazione; }
	 */

	public String getEsitoVotazione() {
		return esitoVotazione;
	}

	public void setEsitoVotazione(String esitoVotazione) {
		this.esitoVotazione = esitoVotazione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataVotazione() {
		return dataVotazione;
	}

	public void setDataVotazione(Date dataVotazione) {
		this.dataVotazione = dataVotazione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataTrasmissione() {
		return dataTrasmissione;
	}

	public void setDataTrasmissione(Date dataTrasmissione) {
		this.dataTrasmissione = dataTrasmissione;
	}

	public List<Relatore> getRelatori() {
		return relatori;
	}

	public void setRelatori(List<Relatore> relatori) {
		this.relatori = relatori;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataProposta() {
		return dataProposta;
	}

	public void setDataProposta(Date dataProposta) {
		this.dataProposta = dataProposta;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAnnullo() {
		return dataAnnullo;
	}

	public void setDataAnnullo(Date dataAnnullo) {
		this.dataAnnullo = dataAnnullo;
	}

	public boolean isAnnullata() {
		return annullata;
	}

	public void setAnnullata(boolean annullata) {
		this.annullata = annullata;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataNomina() {
		return dataNomina;
	}

	public void setDataNomina(Date dataNomina) {
		this.dataNomina = dataNomina;
	}

	public ComitatoRistretto getComitatoRistretto() {
		return comitatoRistretto;
	}

	public void setComitatoRistretto(ComitatoRistretto comitatoRistretto) {
		this.comitatoRistretto = comitatoRistretto;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public boolean isPresenzaComitatoRistretto() {
		return presenzaComitatoRistretto;
	}

	public void setPresenzaComitatoRistretto(boolean presenzaComitatoRistretto) {
		this.presenzaComitatoRistretto = presenzaComitatoRistretto;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataIstituzioneComitato() {
		return dataIstituzioneComitato;
	}

	public void setDataIstituzioneComitato(Date dataIstituzioneComitato) {
		this.dataIstituzioneComitato = dataIstituzioneComitato;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataFineLavoriComitato() {
		return dataFineLavoriComitato;
	}

	public void setDataFineLavoriComitato(Date dataFineLavoriComitato) {
		this.dataFineLavoriComitato = dataFineLavoriComitato;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRichiestaIscrizioneAula() {
		return dataRichiestaIscrizioneAula;
	}

	public void setDataRichiestaIscrizioneAula(Date dataRichiestaIscrizioneAula) {
		this.dataRichiestaIscrizioneAula = dataRichiestaIscrizioneAula;
	}

	public boolean isPassaggioDirettoInAula() {
		return passaggioDirettoInAula;
	}

	public void setPassaggioDirettoInAula(boolean passaggioDirettoInAula) {
		this.passaggioDirettoInAula = passaggioDirettoInAula;
	}

	public List<Link> getLinksNoteEsameCommissione() {
		return linksNoteEsameCommissione;
	}

	public void setLinksNoteEsameCommissione(List<Link> linksNoteEsameCommissione) {
		this.linksNoteEsameCommissione = linksNoteEsameCommissione;
	}

	public String getNoteGeneraliEsameCommissione() {
		return noteGeneraliEsameCommissione;
	}

	@JsonDeserialize(using = JsonNoteDeserializer.class)
	public void setNoteGeneraliEsameCommissione(String noteGeneraliEsameCommissione) {
		this.noteGeneraliEsameCommissione = noteGeneraliEsameCommissione;
	}

	public List<Allegato> getTestiClausola() {
		return testiClausola;
	}

	public void setTestiClausola(List<Allegato> testiClausola) {
		this.testiClausola = testiClausola;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPresaInCaricoProposta() {
		return dataPresaInCaricoProposta;
	}

	public void setDataPresaInCaricoProposta(Date dataPresaInCaricoProposta) {
		this.dataPresaInCaricoProposta = dataPresaInCaricoProposta;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataIntesa() {
		return dataIntesa;
	}

	public void setDataIntesa(Date dataIntesa) {
		this.dataIntesa = dataIntesa;
	}

	public String getEsitoVotazioneIntesa() {
		return esitoVotazioneIntesa;
	}

	public void setEsitoVotazioneIntesa(String esitoVotazioneIntesa) {
		this.esitoVotazioneIntesa = esitoVotazioneIntesa;
	}

	public String getNoteClausolaValutativa() {
		return noteClausolaValutativa;
	}

	@JsonDeserialize(using = JsonNoteDeserializer.class)
	public void setNoteClausolaValutativa(String noteClausolaValutativa) {
		this.noteClausolaValutativa = noteClausolaValutativa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaStralcio() {
		return dataSedutaStralcio;
	}

	public void setDataSedutaStralcio(Date dataSedutaStralcio) {
		this.dataSedutaStralcio = dataSedutaStralcio;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataIniziativaStralcio() {
		return dataIniziativaStralcio;
	}

	public void setDataIniziativaStralcio(Date dataIniziativaStralcio) {
		this.dataIniziativaStralcio = dataIniziativaStralcio;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataStralcio() {
		return dataStralcio;
	}

	public void setDataStralcio(Date dataStralcio) {
		this.dataStralcio = dataStralcio;
	}

	public String getArticoli() {
		return articoli;
	}

	public void setArticoli(String articoli) {
		this.articoli = articoli;
	}

	public String getNoteStralcio() {
		return noteStralcio;
	}

	public void setNoteStralcio(String noteStralcio) {
		this.noteStralcio = noteStralcio;
	}

	public String getQuorumStralcio() {
		return quorumStralcio;
	}

	public void setQuorumStralcio(String quorumStralcio) {
		this.quorumStralcio = quorumStralcio;
	}

	public boolean isSospensioneFeriale() {
		return sospensioneFeriale;
	}

	public void setSospensioneFeriale(boolean sospensioneFeriale) {
		this.sospensioneFeriale = sospensioneFeriale;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataInterruzione() {
		return dataInterruzione;
	}

	public void setDataInterruzione(Date dataInterruzione) {
		this.dataInterruzione = dataInterruzione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRicezioneIntegrazioni() {
		return dataRicezioneIntegrazioni;
	}

	public void setDataRicezioneIntegrazioni(Date dataRicezioneIntegrazioni) {
		this.dataRicezioneIntegrazioni = dataRicezioneIntegrazioni;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRis() {
		return dataRis;
	}

	public void setDataRis(Date dataRis) {
		this.dataRis = dataRis;
	}

	public String getNumeroRis() {
		return numeroRis;
	}

	public void setNumeroRis(String numeroRis) {
		this.numeroRis = numeroRis;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataDcr() {
		return dataDcr;
	}

	public void setDataDcr(Date dataDcr) {
		this.dataDcr = dataDcr;
	}

	public String getNumeroDcr() {
		return numeroDcr;
	}

	public void setNumeroDcr(String numeroDcr) {
		this.numeroDcr = numeroDcr;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataCalendarizzazione() {
		return dataCalendarizzazione;
	}

	public void setDataCalendarizzazione(Date dataCalendarizzazione) {
		this.dataCalendarizzazione = dataCalendarizzazione;
	}

	public List<Relatore> getValidRelatori() {

		List<Relatore> list = new ArrayList<Relatore>();

		for (Relatore relatore : relatori) {
			if (relatore.getDataUscita() == null) {
				list.add(relatore);
			}
		}

		return list;

	}

}
