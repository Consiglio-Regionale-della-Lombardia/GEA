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

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.sourcesense.crl.util.JsonDateSerializer;

/**
 * Atto MIS che aggiunge le informazioni di numero repertorio, oggetto, data
 * iniziativa commissione, data proposta commissione, commissione competente,
 * esito voto intesa, data intesa, data risposta comitato, data approvazione
 * progetto, data approvazione udp. data trasmissione udp, numero atto udp,data
 * scadenza mv, data esame rapporto finale, data trasmissione commissioni, note,
 * istituto incaricato, relatore 1 e relatore 2
 * 
 * @author sourcesense
 *
 */
public class AttoMIS extends Atto implements Cloneable {

	private String numeroRepertorio;
	private String oggetto;
	private Date dataIniziativaComitato;
	private Date dataPropostaCommissione;
	private String commissioneCompetente;
	private String esitoVotoIntesa;
	private Date dataIntesa;
	private Date dataRispostaComitato;
	private Date dataApprovazioneProgetto;
	private Date dataApprovazioneUdP;
	private Date dataTrasmissioneUdP;
	private String numeroAttoUdp;
	private Date dataScadenzaMV;
	private Date dataEsameRapportoFinale;
	private Date dataTrasmissioneCommissioni;
	private String note;
	private String istitutoIncaricato;
	private String relatore1;
	private String relatore2;

	public String getNumeroRepertorio() {
		return numeroRepertorio;
	}

	public void setNumeroRepertorio(String numeroRepertorio) {
		this.numeroRepertorio = numeroRepertorio;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataIniziativaComitato() {
		return dataIniziativaComitato;
	}

	public void setDataIniziativaComitato(Date dataIniziativaComitato) {
		this.dataIniziativaComitato = dataIniziativaComitato;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPropostaCommissione() {
		return dataPropostaCommissione;
	}

	public void setDataPropostaCommissione(Date dataPropostaCommissione) {
		this.dataPropostaCommissione = dataPropostaCommissione;
	}

	public String getCommissioneCompetente() {
		return commissioneCompetente;
	}

	public void setCommissioneCompetente(String commissioneCompetente) {
		this.commissioneCompetente = commissioneCompetente;
	}

	public String getEsitoVotoIntesa() {
		return esitoVotoIntesa;
	}

	public void setEsitoVotoIntesa(String esitoVotoIntesa) {
		this.esitoVotoIntesa = esitoVotoIntesa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataIntesa() {
		return dataIntesa;
	}

	public void setDataIntesa(Date dataIntesa) {
		this.dataIntesa = dataIntesa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRispostaComitato() {
		return dataRispostaComitato;
	}

	public void setDataRispostaComitato(Date dataRispostaComitato) {
		this.dataRispostaComitato = dataRispostaComitato;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataApprovazioneProgetto() {
		return dataApprovazioneProgetto;
	}

	public void setDataApprovazioneProgetto(Date dataApprovazioneProgetto) {
		this.dataApprovazioneProgetto = dataApprovazioneProgetto;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataApprovazioneUdP() {
		return dataApprovazioneUdP;
	}

	public void setDataApprovazioneUdP(Date dataApprovazioneUdP) {
		this.dataApprovazioneUdP = dataApprovazioneUdP;
	}

	public String getNumeroAttoUdp() {
		return numeroAttoUdp;
	}

	public void setNumeroAttoUdp(String numeroAttoUdp) {
		this.numeroAttoUdp = numeroAttoUdp;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataScadenzaMV() {
		return dataScadenzaMV;
	}

	public void setDataScadenzaMV(Date dataScadenzaMV) {
		this.dataScadenzaMV = dataScadenzaMV;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataEsameRapportoFinale() {
		return dataEsameRapportoFinale;
	}

	public void setDataEsameRapportoFinale(Date dataEsameRapportoFinale) {
		this.dataEsameRapportoFinale = dataEsameRapportoFinale;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataTrasmissioneCommissioni() {
		return dataTrasmissioneCommissioni;
	}

	public void setDataTrasmissioneCommissioni(Date dataTrasmissioneCommissioni) {
		this.dataTrasmissioneCommissioni = dataTrasmissioneCommissioni;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getIstitutoIncaricato() {
		return istitutoIncaricato;
	}

	public void setIstitutoIncaricato(String istitutoIncaricato) {
		this.istitutoIncaricato = istitutoIncaricato;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataTrasmissioneUdP() {
		return dataTrasmissioneUdP;
	}

	public void setDataTrasmissioneUdP(Date dataTrasmissioneUdP) {
		this.dataTrasmissioneUdP = dataTrasmissioneUdP;
	}

	public String getRelatore1() {
		return relatore1;
	}

	public void setRelatore1(String relatore1) {
		this.relatore1 = relatore1;
	}

	public String getRelatore2() {
		return relatore2;
	}

	public void setRelatore2(String relatore2) {
		this.relatore2 = relatore2;
	}

}
