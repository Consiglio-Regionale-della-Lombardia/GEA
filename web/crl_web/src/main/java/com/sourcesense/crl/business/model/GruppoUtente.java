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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * Gruppo utente
 * 
 * @author sourcesense
 *
 */
@Configurable
@XmlRootElement
@JsonRootName("gruppoUtente")
@JsonTypeName("gruppoUtente")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class GruppoUtente implements Serializable {

	public static final String SERVIZIO_COMMISSIONI = "ServizioCommissioni";
	public static final String AULA = "Aula";
	public static final String ADMIN = "CRLAdmin";
	public static final String GUEST = "CRLGuest";
	public static final String CPCV = "CPCV";

	/*
	 * Servizio commissioni Commissioni (vari ruoli) Aula CPCV Giunta per il
	 * regolamento Guest (sola lettura) Amministratore
	 */
	private List<Seduta> sedute = new ArrayList<Seduta>();
	private String nome;
	private boolean commissione;

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Seduta> getSedute() {
		return sedute;
	}

	public void setSedute(List<Seduta> sedute) {
		this.sedute = sedute;
	}

	public boolean isCommissione() {
		return commissione;
	}

	public void setCommissione(boolean commissione) {
		this.commissione = commissione;
	}

}
