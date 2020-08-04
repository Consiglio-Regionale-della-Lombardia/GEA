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

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.sourcesense.crl.util.JsonDateSerializer;
import com.sourcesense.crl.util.JsonIntegerDeserializer;
import com.sourcesense.crl.util.JsonNoteDeserializer;

/**
 * Aula che comprende le informazioni di data presa in carico esame aula,
 * relazione scritta, esito voto aula, tipologia votazione, data seduta aula,
 * numero Dcr, numero Lcr, emendato,note votazione, testi atto votato esame
 * aula, emendamenti esame aula, allegati esame aula, numero degli emendamenti
 * presentati di tipo maggioranza, minoranza e giunta, numero degli emendamenti
 * approvati di tipo maggioranza, minoranza e giunta, non ammissibili, decaduti,
 * ritirati, respinti, totale non approvati in esame aula, note emendamenti,
 * data seduta rinvio
 * 
 * @author sourcesense
 *
 */
public class Aula implements Cloneable {

	public final static String ESITO_VOTO_APPROVATO = "Approvato";
	public final static String ESITO_VOTO_APPROVATO_NON_PASSAGGIO = "Approvato non passaggio all' esame";
	public final static String ESITO_VOTO_RESPINTO = "Respinto dall'Aula";

	public final static String TIPO_VOTO_PALESE_ALZATA_MANO = "Palese per alzata di mano";
	public final static String TIPO_VOTO_PALESE_APPELLO_NOMINALE = "Palese per appello nominale";
	public final static String TIPO_VOTO_SEGRETA = "Segreta";

	private Date dataPresaInCaricoEsameAula;
	private String relazioneScritta;
	private String esitoVotoAula;
	private String tipologiaVotazione;
	private Date dataSedutaAula;
	private String numeroDcr;
	private String numeroLcr;
	private boolean emendato;
	private String noteVotazione;

	private List<TestoAtto> testiAttoVotatoEsameAula = new ArrayList<TestoAtto>();
	private List<Allegato> emendamentiEsameAula = new ArrayList<Allegato>();
	private List<Allegato> allegatiEsameAula = new ArrayList<Allegato>();

	private Integer numEmendPresentatiMaggiorEsameAula;
	private Integer numEmendPresentatiMinorEsameAula;
	private Integer numEmendPresentatiGiuntaEsameAula;
	private Integer numEmendPresentatiMistoEsameAula;
	private Integer numEmendApprovatiMaggiorEsameAula;
	private Integer numEmendApprovatiMinorEsameAula;
	private Integer numEmendApprovatiGiuntaEsameAula;
	private Integer numEmendApprovatiMistoEsameAula;
	private Integer nonAmmissibiliEsameAula;
	private Integer decadutiEsameAula;
	private Integer ritiratiEsameAula;
	private Integer respintiEsameAula;
	private Integer totaleNonApprovatiEsameAula;
	private String noteEmendamentiEsameAula;

	private Date dataSedutaRinvio;
	private Date dataTermineMassimo;
	private String motivazioneRinvio;

	private Date dataSedutaStralcio;
	private Date dataStralcio;
	private Date dataIniziativaStralcio;

	private String articoli;
	private String noteStralcio;
	private String quorumEsameAula;

	private String numeroReg;

	private List<Link> linksEsameAula = new ArrayList<Link>();

	private String noteGeneraliEsameAula;

	private boolean rinvioCommBilancio;

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaAula() {
		return dataSedutaAula;
	}

	public void setDataSedutaAula(Date dataSedutaAula) {
		this.dataSedutaAula = dataSedutaAula;
	}

	public String getRelazioneScritta() {
		return relazioneScritta;
	}

	public void setRelazioneScritta(String relazioneScritta) {
		this.relazioneScritta = relazioneScritta;
	}

	public String getEsitoVotoAula() {
		return esitoVotoAula;
	}

	public void setEsitoVotoAula(String esitoVotoAula) {
		this.esitoVotoAula = esitoVotoAula;
	}

	public String getTipologiaVotazione() {
		return tipologiaVotazione;
	}

	public void setTipologiaVotazione(String tipologiaVotazione) {
		this.tipologiaVotazione = tipologiaVotazione;
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

	public boolean isEmendato() {
		return emendato;
	}

	public void setEmendato(boolean emendato) {
		this.emendato = emendato;
	}

	public String getNoteVotazione() {
		return noteVotazione;
	}

	@JsonDeserialize(using = JsonNoteDeserializer.class)
	public void setNoteVotazione(String noteVotazione) {
		this.noteVotazione = noteVotazione;
	}

	public List<TestoAtto> getTestiAttoVotatoEsameAula() {
		return testiAttoVotatoEsameAula;
	}

	public void setTestiAttoVotatoEsameAula(List<TestoAtto> testiAttoVotatoEsameAula) {
		this.testiAttoVotatoEsameAula = testiAttoVotatoEsameAula;
	}

	public List<Allegato> getEmendamentiEsameAula() {
		return emendamentiEsameAula;
	}

	public void setEmendamentiEsameAula(List<Allegato> emendamentiEsameAula) {
		this.emendamentiEsameAula = emendamentiEsameAula;
	}

	public Integer getNumEmendPresentatiMaggiorEsameAula() {
		return numEmendPresentatiMaggiorEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendPresentatiMaggiorEsameAula(Integer numEmendPresentatiMaggiorEsameAula) {
		this.numEmendPresentatiMaggiorEsameAula = numEmendPresentatiMaggiorEsameAula;
	}

	public Integer getNumEmendPresentatiMinorEsameAula() {
		return numEmendPresentatiMinorEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendPresentatiMinorEsameAula(Integer numEmendPresentatiMinorEsameAula) {
		this.numEmendPresentatiMinorEsameAula = numEmendPresentatiMinorEsameAula;
	}

	public Integer getNumEmendPresentatiGiuntaEsameAula() {
		return numEmendPresentatiGiuntaEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendPresentatiGiuntaEsameAula(Integer numEmendPresentatiGiuntaEsameAula) {
		this.numEmendPresentatiGiuntaEsameAula = numEmendPresentatiGiuntaEsameAula;
	}

	public Integer getNumEmendPresentatiMistoEsameAula() {
		return numEmendPresentatiMistoEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendPresentatiMistoEsameAula(Integer numEmendPresentatiMistoEsameAula) {
		this.numEmendPresentatiMistoEsameAula = numEmendPresentatiMistoEsameAula;
	}

	public Integer getNumEmendApprovatiMaggiorEsameAula() {
		return numEmendApprovatiMaggiorEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendApprovatiMaggiorEsameAula(Integer numEmendApprovatiMaggiorEsameAula) {
		this.numEmendApprovatiMaggiorEsameAula = numEmendApprovatiMaggiorEsameAula;
	}

	public Integer getNumEmendApprovatiMinorEsameAula() {
		return numEmendApprovatiMinorEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendApprovatiMinorEsameAula(Integer numEmendApprovatiMinorEsameAula) {
		this.numEmendApprovatiMinorEsameAula = numEmendApprovatiMinorEsameAula;
	}

	public Integer getNumEmendApprovatiGiuntaEsameAula() {
		return numEmendApprovatiGiuntaEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendApprovatiGiuntaEsameAula(Integer numEmendApprovatiGiuntaEsameAula) {
		this.numEmendApprovatiGiuntaEsameAula = numEmendApprovatiGiuntaEsameAula;
	}

	public Integer getNumEmendApprovatiMistoEsameAula() {
		return numEmendApprovatiMistoEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNumEmendApprovatiMistoEsameAula(Integer numEmendApprovatiMistoEsameAula) {
		this.numEmendApprovatiMistoEsameAula = numEmendApprovatiMistoEsameAula;
	}

	public Integer getNonAmmissibiliEsameAula() {
		return nonAmmissibiliEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setNonAmmissibiliEsameAula(Integer nonAmmissibiliEsameAula) {
		this.nonAmmissibiliEsameAula = nonAmmissibiliEsameAula;
	}

	public Integer getDecadutiEsameAula() {
		return decadutiEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setDecadutiEsameAula(Integer decadutiEsameAula) {
		this.decadutiEsameAula = decadutiEsameAula;
	}

	public Integer getRitiratiEsameAula() {
		return ritiratiEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setRitiratiEsameAula(Integer ritiratiEsameAula) {
		this.ritiratiEsameAula = ritiratiEsameAula;
	}

	public Integer getRespintiEsameAula() {
		return respintiEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setRespintiEsameAula(Integer respintiEsameAula) {
		this.respintiEsameAula = respintiEsameAula;
	}

	public Integer getTotaleNonApprovatiEsameAula() {
		return totaleNonApprovatiEsameAula;
	}

	@JsonDeserialize(using = JsonIntegerDeserializer.class)
	public void setTotaleNonApprovatiEsameAula(Integer totaleNonApprovatiEsameAula) {
		this.totaleNonApprovatiEsameAula = totaleNonApprovatiEsameAula;
	}

	public String getNoteEmendamentiEsameAula() {
		return noteEmendamentiEsameAula;
	}

	@JsonDeserialize(using = JsonNoteDeserializer.class)
	public void setNoteEmendamentiEsameAula(String noteEmendamentiEsameAula) {
		this.noteEmendamentiEsameAula = noteEmendamentiEsameAula;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaRinvio() {
		return dataSedutaRinvio;
	}

	public void setDataSedutaRinvio(Date dataSedutaRinvio) {
		this.dataSedutaRinvio = dataSedutaRinvio;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataTermineMassimo() {
		return dataTermineMassimo;
	}

	public void setDataTermineMassimo(Date dataTermineMassimo) {
		this.dataTermineMassimo = dataTermineMassimo;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaStralcio() {
		return dataSedutaStralcio;
	}

	public void setDataSedutaStralcio(Date dataSedutaStralcio) {
		this.dataSedutaStralcio = dataSedutaStralcio;
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

	@JsonDeserialize(using = JsonNoteDeserializer.class)
	public void setNoteStralcio(String noteStralcio) {
		this.noteStralcio = noteStralcio;
	}

	public String getQuorumEsameAula() {
		return quorumEsameAula;
	}

	public void setQuorumEsameAula(String quorumEsameAula) {
		this.quorumEsameAula = quorumEsameAula;
	}

	public List<Allegato> getAllegatiEsameAula() {
		return allegatiEsameAula;
	}

	public void setAllegatiEsameAula(List<Allegato> allegatiEsameAula) {
		this.allegatiEsameAula = allegatiEsameAula;
	}

	public List<Link> getLinksEsameAula() {
		return linksEsameAula;
	}

	public void setLinksEsameAula(List<Link> linksEsameAula) {
		this.linksEsameAula = linksEsameAula;
	}

	public String getMotivazioneRinvio() {
		return motivazioneRinvio;
	}

	public void setMotivazioneRinvio(String motivazioneRinvio) {
		this.motivazioneRinvio = motivazioneRinvio;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPresaInCaricoEsameAula() {
		return dataPresaInCaricoEsameAula;
	}

	public void setDataPresaInCaricoEsameAula(Date dataPresaInCaricoEsameAula) {
		this.dataPresaInCaricoEsameAula = dataPresaInCaricoEsameAula;
	}

	public Date getDataIniziativaStralcio() {
		return dataIniziativaStralcio;
	}

	public void setDataIniziativaStralcio(Date dataIniziativaStralcio) {
		this.dataIniziativaStralcio = dataIniziativaStralcio;
	}

	public String getNoteGeneraliEsameAula() {
		return noteGeneraliEsameAula;
	}

	@JsonDeserialize(using = JsonNoteDeserializer.class)
	public void setNoteGeneraliEsameAula(String noteGeneraliEsameAula) {
		this.noteGeneraliEsameAula = noteGeneraliEsameAula;
	}

	public String getNumeroReg() {
		return numeroReg;
	}

	public void setNumeroReg(String numeroReg) {
		this.numeroReg = numeroReg;
	}

	public boolean isRinvioCommBilancio() {
		return rinvioCommBilancio;
	}

	public void setRinvioCommBilancio(boolean rinvioCommBilancio) {
		this.rinvioCommBilancio = rinvioCommBilancio;
	}

}
