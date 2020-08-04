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

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.sourcesense.crl.util.JsonDateSerializer;

/**
 * Abbinamento rappresentato dalle informazioni di id atto, id atto abbinato,
 * tipo testo, data abbinamento, data disabbinamento, note, abbinato, tipo atto
 * abbinato e numero atto abbinato
 * 
 * @author sourcesense
 *
 */
@JsonRootName("abbinamento")
@JsonTypeName("abbinamento")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class Abbinamento implements Cloneable {
	private String idAtto;
	private String idAttoAbbinato;
	private String tipoTesto;
	private Date dataAbbinamento;
	private Date dataDisabbinamento;
	private String note;
	private boolean abbinato;

	private String tipoAttoAbbinato;
	private String numeroAttoAbbinato;

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

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataAbbinamento() {
		return dataAbbinamento;
	}

	public void setDataAbbinamento(Date dataAbbinamento) {
		this.dataAbbinamento = dataAbbinamento;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataDisabbinamento() {
		return dataDisabbinamento;
	}

	public void setDataDisabbinamento(Date dataDisabbinamento) {
		this.dataDisabbinamento = dataDisabbinamento;
	}

	public String getTipoTesto() {
		return tipoTesto;
	}

	public void setTipoTesto(String tipoTesto) {
		this.tipoTesto = tipoTesto;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isAbbinato() {
		return abbinato;
	}

	public void setAbbinato(boolean abbinato) {
		this.abbinato = abbinato;
	}

	public String getIdAtto() {
		return idAtto;
	}

	public void setIdAtto(String idAtto) {
		this.idAtto = idAtto;
	}

	public String getIdAttoAbbinato() {
		return idAttoAbbinato;
	}

	public void setIdAttoAbbinato(String idAttoAbbinato) {
		this.idAttoAbbinato = idAttoAbbinato;
	}

	public String getTipoAttoAbbinato() {
		return tipoAttoAbbinato;
	}

	public void setTipoAttoAbbinato(String tipoAttoAbbinato) {
		this.tipoAttoAbbinato = tipoAttoAbbinato;
	}

	public String getNumeroAttoAbbinato() {
		return numeroAttoAbbinato;
	}

	public void setNumeroAttoAbbinato(String numeroAttoAbbinato) {
		this.numeroAttoAbbinato = numeroAttoAbbinato;
	}

}
