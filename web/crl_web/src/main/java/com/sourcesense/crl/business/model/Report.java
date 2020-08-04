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
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.sourcesense.crl.util.JsonDateSerializer;

/**
 * Report di una legislatura
 * 
 * @author sourcesense
 *
 */
public class Report {

	private String tipoTemplate;
	private String nome;
	private String legislatura;

	private List<String> tipiAtto;
	private List<String> commissioni;
	private String ruoloCommissione;
	private Date dataAssegnazioneDa;
	private Date dataAssegnazioneA;
	private Date dataVotazioneCommReferenteDa;
	private Date dataVotazioneCommReferenteA;
	private Date dataRitiroDa;
	private Date dataRitiroA;
	private Date dataNominaRelatoreDa;
	private Date dataNominaRelatoreA;
	private List<String> relatori;
	private String organismo;
	private Date dataAssegnazioneParereDa;
	private Date dataAssegnazioneParereA;
	private String firmatario;
	private String tipologiaFirma;
	private Date dataPresentazioneDa;
	private Date dataPresentazioneA;
	private Date dataAssegnazioneCommReferenteDa;
	private Date dataAssegnazioneCommReferenteA;
	private Date dataSedutaDa;
	private Date dataSedutaA;
	private Date dataConsultazioneDa;
	private Date dataConsultazioneA;

	public Report() {

	}

	public Report(String nome, String tipoTemplate) {

		this.nome = nome;
		this.tipoTemplate = tipoTemplate;

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<String> getTipiAtto() {
		return tipiAtto;
	}

	public void setTipiAtto(List<String> tipiAtto) {
		this.tipiAtto = tipiAtto;
	}

	public List<String> getCommissioni() {
		return commissioni;
	}

	public void setCommissioni(List<String> commissioni) {
		this.commissioni = commissioni;
	}

	public String getRuoloCommissione() {
		return ruoloCommissione;
	}

	public void setRuoloCommissione(String ruoloCommissione) {
		this.ruoloCommissione = ruoloCommissione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazioneDa() {
		return dataAssegnazioneDa;
	}

	public void setDataAssegnazioneDa(Date dataAssegnazioneDa) {
		this.dataAssegnazioneDa = dataAssegnazioneDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazioneA() {
		return dataAssegnazioneA;
	}

	public void setDataAssegnazioneA(Date dataAssegnazioneA) {
		this.dataAssegnazioneA = dataAssegnazioneA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataVotazioneCommReferenteDa() {
		return dataVotazioneCommReferenteDa;
	}

	public void setDataVotazioneCommReferenteDa(Date dataVotazioneCommReferenteDa) {
		this.dataVotazioneCommReferenteDa = dataVotazioneCommReferenteDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataVotazioneCommReferenteA() {
		return dataVotazioneCommReferenteA;
	}

	public void setDataVotazioneCommReferenteA(Date dataVotazioneCommReferenteA) {
		this.dataVotazioneCommReferenteA = dataVotazioneCommReferenteA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRitiroDa() {
		return dataRitiroDa;
	}

	public void setDataRitiroDa(Date dataRitiroDa) {
		this.dataRitiroDa = dataRitiroDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRitiroA() {
		return dataRitiroA;
	}

	public void setDataRitiroA(Date dataRitiroA) {
		this.dataRitiroA = dataRitiroA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataNominaRelatoreDa() {
		return dataNominaRelatoreDa;
	}

	public void setDataNominaRelatoreDa(Date dataNominaRelatoreDa) {
		this.dataNominaRelatoreDa = dataNominaRelatoreDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataNominaRelatoreA() {
		return dataNominaRelatoreA;
	}

	public void setDataNominaRelatoreA(Date dataNominaRelatoreA) {
		this.dataNominaRelatoreA = dataNominaRelatoreA;
	}

	public List<String> getRelatori() {
		return relatori;
	}

	public void setRelatori(List<String> relatori) {
		this.relatori = relatori;
	}

	public String getOrganismo() {
		return organismo;
	}

	public void setOrganismo(String organismo) {
		this.organismo = organismo;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazioneParereDa() {
		return dataAssegnazioneParereDa;
	}

	public void setDataAssegnazioneParereDa(Date dataAssegnazioneParereDa) {
		this.dataAssegnazioneParereDa = dataAssegnazioneParereDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazioneParereA() {
		return dataAssegnazioneParereA;
	}

	public void setDataAssegnazioneParereA(Date dataAssegnazioneParereA) {
		this.dataAssegnazioneParereA = dataAssegnazioneParereA;
	}

	public String getFirmatario() {
		return firmatario;
	}

	public void setFirmatario(String firmatario) {
		this.firmatario = firmatario;
	}

	public String getTipologiaFirma() {
		return tipologiaFirma;
	}

	public void setTipologiaFirma(String tipologiaFirma) {
		this.tipologiaFirma = tipologiaFirma;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPresentazioneDa() {
		return dataPresentazioneDa;
	}

	public void setDataPresentazioneDa(Date dataPresentazioneDa) {
		this.dataPresentazioneDa = dataPresentazioneDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPresentazioneA() {
		return dataPresentazioneA;
	}

	public void setDataPresentazioneA(Date dataPresentazioneA) {
		this.dataPresentazioneA = dataPresentazioneA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazioneCommReferenteDa() {
		return dataAssegnazioneCommReferenteDa;
	}

	public void setDataAssegnazioneCommReferenteDa(Date dataAssegnazioneCommReferenteDa) {
		this.dataAssegnazioneCommReferenteDa = dataAssegnazioneCommReferenteDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazioneCommReferenteA() {
		return dataAssegnazioneCommReferenteA;
	}

	public void setDataAssegnazioneCommReferenteA(Date dataAssegnazioneCommReferenteA) {
		this.dataAssegnazioneCommReferenteA = dataAssegnazioneCommReferenteA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaDa() {
		return dataSedutaDa;
	}

	public void setDataSedutaDa(Date dataSedutaDa) {
		this.dataSedutaDa = dataSedutaDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaA() {
		return dataSedutaA;
	}

	public void setDataSedutaA(Date dataSedutaA) {
		this.dataSedutaA = dataSedutaA;
	}

	public String getTipoTemplate() {
		return tipoTemplate;
	}

	public void setTipoTemplate(String tipoTemplate) {
		this.tipoTemplate = tipoTemplate;
	}

	public String getLegislatura() {
		return legislatura;
	}

	public void setLegislatura(String legislatura) {
		this.legislatura = legislatura;
	}

	public Date getDataConsultazioneDa() {
		return dataConsultazioneDa;
	}

	public void setDataConsultazioneDa(Date dataConsultazioneDa) {
		this.dataConsultazioneDa = dataConsultazioneDa;
	}

	public Date getDataConsultazioneA() {
		return dataConsultazioneA;
	}

	public void setDataConsultazioneA(Date dataConsultazioneA) {
		this.dataConsultazioneA = dataConsultazioneA;
	}

}
