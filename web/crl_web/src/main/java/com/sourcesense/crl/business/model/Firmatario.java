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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.sourcesense.crl.util.JsonDateSerializer;

/**
 * Firmatario
 * 
 * @author sourcesense
 *
 */
@JsonRootName("firmatario")
@JsonTypeName("firmatario")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
@JsonIgnoreProperties({ "id_persona" })
public class Firmatario implements Cloneable, Comparable<Firmatario> {

	private String nome;
	private String gruppoConsiliare;
	private Date dataFirma;
	private Date dataRitiro;
	private boolean primoFirmatario;
	private boolean firmatarioPopolare;
	private String numeroOrdinamento;
	private String id;
	private String descrizione;
	private String cognomeNome;

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	@Override
	public int compareTo(Firmatario o) {
		return Integer.parseInt(this.numeroOrdinamento) - Integer.parseInt(o.numeroOrdinamento);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getGruppoConsiliare() {
		return gruppoConsiliare;
	}

	public void setGruppoConsiliare(String gruppoConsiliare) {
		this.gruppoConsiliare = gruppoConsiliare;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataFirma() {
		return dataFirma;
	}

	public void setDataFirma(Date dataFirma) {
		this.dataFirma = dataFirma;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataRitiro() {
		return dataRitiro;
	}

	public void setDataRitiro(Date dataRitiro) {
		this.dataRitiro = dataRitiro;
	}

	public boolean isPrimoFirmatario() {
		return primoFirmatario;
	}

	public void setPrimoFirmatario(boolean primoFirmatario) {
		this.primoFirmatario = primoFirmatario;
	}

	public boolean isFirmatarioPopolare() {
		return firmatarioPopolare;
	}

	public void setFirmatarioPopolare(boolean firmatarioPopolare) {
		this.firmatarioPopolare = firmatarioPopolare;
	}

	public String getNumeroOrdinamento() {
		return numeroOrdinamento;
	}

	public void setNumeroOrdinamento(String numeroOrdinamento) {
		this.numeroOrdinamento = numeroOrdinamento;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = cognomeNome;
	}

}
