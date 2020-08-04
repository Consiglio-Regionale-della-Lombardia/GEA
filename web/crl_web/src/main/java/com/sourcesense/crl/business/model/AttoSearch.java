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
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.sourcesense.crl.util.JsonDateSerializer;

/**
 * Atto usato per la ricerca che aggiunge i campi di ricerca numero DGR, data
 * assegnazione a, data assegnazione da, data DGR, numero atto da, numero atto
 * a, data pubblicazione da, data pubblicazione a, data seduta sc da, data
 * seduta sc a, data seduta commissione da, data seduta commissione a, data
 * seduta aula da, data seduta aula a, data iniziativa da, data iniziativa a,
 * data chiusura da, data chiusura a, data LR, relatore, numero Dcr, esito
 * commissione referente, esito voto aula, commissione user, commissione 1,
 * commissione 2. commissione 3, gruppo firmatario, gruppo primo firmatario,
 * numero Lcr, stati utente, gruppo utente, emendato, emendato aula, ruolo
 * utente e tipo working list
 * 
 * @author sourcesense
 *
 */
@JsonRootName("atto")
@JsonTypeName("atto")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class AttoSearch extends Atto {

	private String numeroDGR;
	private Date dataAssegnazioneA;
	private Date dataAssegnazioneDa;
	private Date dataDGR;

	private String numeroAttoDa;
	private String numeroAttoA;
	private Date dataPubblicazioneDa;
	private Date dataPubblicazioneA;
	private Date dataSedutaSCDa;
	private Date dataSedutaSCA;
	private Date dataSedutaCommissioneDa;
	private Date dataSedutaCommissioneA;
	private Date dataSedutaAulaDa;
	private Date dataSedutaAulaA;
	private Date dataIniziativaDa;
	private Date dataIniziativaA;
	private Date dataChiusuraDa;
	private Date dataChiusuraA;
	private Date dataLR;
	private String relatore;
	private String numeroDcr;
	private String esitoVotoCommissioneReferente;
	private String esitoVotoAula;
	private String commissioneUser;
	private String commissione1;
	private String commissione2;
	private String commissione3;
	private String ruoloCommissione1;
	private String ruoloCommissione2;
	private String ruoloCommissione3;
	private String gruppoFirmatario;
	private String gruppoPrimoFirmatario;
	private String numeroLcr;
	private List<StatoAtto> statiUtente = new ArrayList<StatoAtto>();
	private String gruppoUtente;
	private boolean emendato;
	private boolean emendatoAula;

	private String ruoloUtente;
	private String tipoWorkingList;

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPubblicazioneDa() {
		return dataPubblicazioneDa;

	}

	public void setDataPubblicazioneDa(Date dataPubblicazioneDa) {
		this.dataPubblicazioneDa = dataPubblicazioneDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataPubblicazioneA() {
		return dataPubblicazioneA;
	}

	public void setDataPubblicazioneA(Date dataPubblicazioneA) {
		this.dataPubblicazioneA = dataPubblicazioneA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaSCDa() {
		return dataSedutaSCDa;
	}

	public void setDataSedutaSCDa(Date dataSedutaSCDa) {
		this.dataSedutaSCDa = dataSedutaSCDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaSCA() {
		return dataSedutaSCA;
	}

	public void setDataSedutaSCA(Date dataSedutaSCA) {
		this.dataSedutaSCA = dataSedutaSCA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaCommissioneDa() {
		return dataSedutaCommissioneDa;
	}

	public void setDataSedutaCommissioneDa(Date dataSedutaCommissioneDa) {
		this.dataSedutaCommissioneDa = dataSedutaCommissioneDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaCommissioneA() {
		return dataSedutaCommissioneA;
	}

	public void setDataSedutaCommissioneA(Date dataSedutaCommissioneA) {
		this.dataSedutaCommissioneA = dataSedutaCommissioneA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaAulaDa() {
		return dataSedutaAulaDa;
	}

	public void setDataSedutaAulaDa(Date dataSedutaAulaDa) {
		this.dataSedutaAulaDa = dataSedutaAulaDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSedutaAulaA() {
		return dataSedutaAulaA;
	}

	public void setDataSedutaAulaA(Date dataSedutaAulaA) {
		this.dataSedutaAulaA = dataSedutaAulaA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataIniziativaDa() {
		return dataIniziativaDa;
	}

	public void setDataIniziativaDa(Date dataIniziativaDa) {
		this.dataIniziativaDa = dataIniziativaDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataIniziativaA() {
		return dataIniziativaA;
	}

	public void setDataIniziativaA(Date dataIniziativaA) {
		this.dataIniziativaA = dataIniziativaA;
	}

	public String getRelatore() {
		return relatore;
	}

	public void setRelatore(String relatore) {
		this.relatore = relatore;
	}

	public String getNumeroDcr() {
		return numeroDcr;
	}

	public void setNumeroDcr(String numeroDcr) {
		this.numeroDcr = numeroDcr;
	}

	public String getEsitoVotoCommissioneReferente() {
		return esitoVotoCommissioneReferente;
	}

	public void setEsitoVotoCommissioneReferente(String esitoVotoCommissioneReferente) {
		this.esitoVotoCommissioneReferente = esitoVotoCommissioneReferente;
	}

	public String getEsitoVotoAula() {
		return esitoVotoAula;
	}

	public void setEsitoVotoAula(String esitoVotoAula) {
		this.esitoVotoAula = esitoVotoAula;
	}

	public String getCommissione1() {
		return commissione1;
	}

	public void setCommissione1(String commissione1) {
		this.commissione1 = commissione1;
	}

	public String getCommissione2() {
		return commissione2;
	}

	public void setCommissione2(String commissione2) {
		this.commissione2 = commissione2;
	}

	public String getCommissione3() {
		return commissione3;
	}

	public void setCommissione3(String commissione3) {
		this.commissione3 = commissione3;
	}

	public String getRuoloCommissione1() {
		return ruoloCommissione1;
	}

	public void setRuoloCommissione1(String ruoloCommissione1) {
		this.ruoloCommissione1 = ruoloCommissione1;
	}

	public String getRuoloCommissione2() {
		return ruoloCommissione2;
	}

	public void setRuoloCommissione2(String ruoloCommissione2) {
		this.ruoloCommissione2 = ruoloCommissione2;
	}

	public String getRuoloCommissione3() {
		return ruoloCommissione3;
	}

	public void setRuoloCommissione3(String ruoloCommissione3) {
		this.ruoloCommissione3 = ruoloCommissione3;
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

	public List<StatoAtto> getStatiUtente() {
		return statiUtente;
	}

	public void setStatiUtente(List<StatoAtto> statiUtente) {
		this.statiUtente = statiUtente;
	}

	public String getGruppoUtente() {
		return gruppoUtente;
	}

	public void setGruppoUtente(String gruppoUtente) {
		this.gruppoUtente = gruppoUtente;
	}

	public String getNumeroAttoDa() {
		return numeroAttoDa;
	}

	public void setNumeroAttoDa(String numeroAttoDa) {
		this.numeroAttoDa = numeroAttoDa;
	}

	public String getNumeroAttoA() {
		return numeroAttoA;
	}

	public void setNumeroAttoA(String numeroAttoA) {
		this.numeroAttoA = numeroAttoA;
	}

	public String getNumeroDGR() {
		return numeroDGR;
	}

	public void setNumeroDGR(String numeroDGR) {
		this.numeroDGR = numeroDGR;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazioneA() {
		return dataAssegnazioneA;
	}

	public void setDataAssegnazioneA(Date dataAssegnazioneA) {
		this.dataAssegnazioneA = dataAssegnazioneA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazioneDa() {
		return dataAssegnazioneDa;
	}

	public void setDataAssegnazioneDa(Date dataAssegnazioneDa) {
		this.dataAssegnazioneDa = dataAssegnazioneDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataDGR() {
		return dataDGR;
	}

	public void setDataDGR(Date dataDGR) {
		this.dataDGR = dataDGR;
	}

	public boolean isEmendatoAula() {
		return emendatoAula;
	}

	public void setEmendatoAula(boolean emendatoAula) {
		this.emendatoAula = emendatoAula;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataChiusuraDa() {
		return dataChiusuraDa;
	}

	public void setDataChiusuraDa(Date dataChiusuraDa) {
		this.dataChiusuraDa = dataChiusuraDa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataChiusuraA() {
		return dataChiusuraA;
	}

	public void setDataChiusuraA(Date dataChiusuraA) {
		this.dataChiusuraA = dataChiusuraA;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataLR() {
		return dataLR;
	}

	public void setDataLR(Date dataLR) {
		this.dataLR = dataLR;
	}

	public String getCommissioneUser() {
		return commissioneUser;
	}

	public void setCommissioneUser(String commissioneUser) {
		this.commissioneUser = commissioneUser;
	}

	public String getGruppoFirmatario() {
		return gruppoFirmatario;
	}

	public void setGruppoFirmatario(String gruppoFirmatario) {
		this.gruppoFirmatario = gruppoFirmatario;
	}

	public String getGruppoPrimoFirmatario() {
		return gruppoPrimoFirmatario;
	}

	public void setGruppoPrimoFirmatario(String gruppoPrimoFirmatario) {
		this.gruppoPrimoFirmatario = gruppoPrimoFirmatario;
	}

	public String getRuoloUtente() {
		return ruoloUtente;
	}

	public void setRuoloUtente(String ruoloUtente) {
		this.ruoloUtente = ruoloUtente;
	}

	public String getTipoWorkingList() {
		return tipoWorkingList;
	}

	public void setTipoWorkingList(String tipoWorkingList) {
		this.tipoWorkingList = tipoWorkingList;
	}

}
