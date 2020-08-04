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
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.sourcesense.crl.util.JsonDateSerializer;

/**
 * Consultazione di un atto in una seduta
 * 
 * @author sourcesense
 *
 */
@JsonRootName("consultazione")
@JsonTypeName("consultazione")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class Consultazione implements Cloneable {

	private String numeroAtto;
	private String tipoAtto;
	private String descrizione;
	private Date dataConsultazione;
	private boolean prevista;
	private boolean discussa;
	private Date dataSeduta;
	private String note;
	private String idAtto;
	private String commissione;
	private String ruoloCommissione;
	private String soggetti;

	private List<SoggettoInvitato> soggettiInvitati = new ArrayList<SoggettoInvitato>();
	private List<Allegato> allegati = new ArrayList<Allegato>();

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataConsultazione() {
		return dataConsultazione;
	}

	public void setDataConsultazione(Date dataConsultazione) {
		this.dataConsultazione = dataConsultazione;
	}

	public boolean isPrevista() {
		return prevista;
	}

	public void setPrevista(boolean prevista) {
		this.prevista = prevista;
	}

	public boolean isDiscussa() {
		return discussa;
	}

	public void setDiscussa(boolean discussa) {
		this.discussa = discussa;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSeduta() {
		return dataSeduta;
	}

	public void setDataSeduta(Date dataSeduta) {
		this.dataSeduta = dataSeduta;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<SoggettoInvitato> getSoggettiInvitati() {
		return soggettiInvitati;
	}

	public void setSoggettiInvitati(List<SoggettoInvitato> soggettiInvitati) {
		this.soggettiInvitati = soggettiInvitati;
	}

	public List<Allegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<Allegato> allegati) {
		this.allegati = allegati;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public String getIdAtto() {
		return idAtto;
	}

	public void setIdAtto(String idAtto) {
		this.idAtto = idAtto;
	}

	public String getCommissione() {
		return commissione;
	}

	public void setCommissione(String commissione) {
		this.commissione = commissione;
	}

	public String getSoggetti() {

		StringBuilder soggettiStr = new StringBuilder("");

		for (SoggettoInvitato soggetto : soggettiInvitati) {

			soggettiStr.append(soggetto.getDescrizione() + " ");
		}

		return soggettiStr.toString();

	}

	public void setSoggetti(String soggetti) {
		this.soggetti = soggetti;
	}

	public String getRuoloCommissione() {
		return ruoloCommissione;
	}

	public void setRuoloCommissione(String ruoloCommissione) {
		this.ruoloCommissione = ruoloCommissione;
	}

}
