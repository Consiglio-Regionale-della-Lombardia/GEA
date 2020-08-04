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
 * Parere. Può essere obbligatorio o facoltativo
 * 
 * @author sourcesense
 *
 */
@JsonRootName("parere")
@JsonTypeName("parere")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class Parere implements Cloneable {
	private String organismoStatutario;
	private Date dataAssegnazione;
	private Date dataAnnullo;
	private boolean obbligatorio;

	private String descrizione;
	private Date dataRicezioneParere;
	private String esito;
	private Date dataRicezioneOrgano;
	private String note;
	private String commissioneDestinataria;

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

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRicezioneOrgano() {
		return dataRicezioneOrgano;
	}

	public void setDataRicezioneOrgano(Date dataRicezioneOrgano) {
		this.dataRicezioneOrgano = dataRicezioneOrgano;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAssegnazione() {
		return dataAssegnazione;
	}

	public void setDataAssegnazione(Date dataAssegnazione) {
		this.dataAssegnazione = dataAssegnazione;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAnnullo() {
		return dataAnnullo;
	}

	public void setDataAnnullo(Date dataAnnullo) {
		this.dataAnnullo = dataAnnullo;
	}

	public boolean isObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(boolean obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	public String getOrganismoStatutario() {
		return organismoStatutario;
	}

	public void setOrganismoStatutario(String organismoStatutario) {
		this.organismoStatutario = organismoStatutario;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRicezioneParere() {
		return dataRicezioneParere;
	}

	public void setDataRicezioneParere(Date dataRicezioneParere) {
		this.dataRicezioneParere = dataRicezioneParere;
	}

	public List<Allegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<Allegato> allegati) {
		this.allegati = allegati;
	}

	public String getCommissioneDestinataria() {
		return commissioneDestinataria;
	}

	public void setCommissioneDestinataria(String commissioneDestinataria) {
		this.commissioneDestinataria = commissioneDestinataria;
	}

}
