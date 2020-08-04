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
import java.util.List;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

/**
 * Collegamento agli atti del sindacato
 * 
 * @author sourcesense
 *
 */
@JsonRootName("collegamentoAttiSindacato")
@JsonTypeName("collegamentoAttiSindacato")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class CollegamentoAttiSindacato implements Cloneable, Comparable<CollegamentoAttiSindacato> {

	private String idAtto;
	private String descrizione;
	private String tipoAtto;
	private String numeroAtto;
	private String link;
	private String oggettoAtto;
	private String numeroOrdinamento;
	private String firmatariList;
	private List<Firmatario> firmatari = new ArrayList<Firmatario>();

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int compareTo(CollegamentoAttiSindacato arg0) {

		if (arg0.numeroOrdinamento == null || arg0.numeroOrdinamento.equals("")) {
			arg0.numeroOrdinamento = "0";
		}

		if (this.numeroOrdinamento == null || this.numeroOrdinamento.equals("")) {
			this.numeroOrdinamento = "0";
		}

		return Integer.parseInt(this.numeroOrdinamento) - Integer.parseInt(arg0.numeroOrdinamento);

	}

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getTipoAtto() {
		return tipoAtto;
	}

	public void setTipoAtto(String tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	public String getNumeroAtto() {
		return numeroAtto;
	}

	public void setNumeroAtto(String numeroAtto) {
		this.numeroAtto = numeroAtto;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getOggettoAtto() {
		return oggettoAtto;
	}

	public void setOggettoAtto(String oggettoAtto) {
		this.oggettoAtto = oggettoAtto;
	}

	public List<Firmatario> getFirmatari() {
		return firmatari;
	}

	public void setFirmatari(List<Firmatario> firmatari) {
		this.firmatari = firmatari;
	}

	public String getIdAtto() {
		return idAtto;
	}

	public void setIdAtto(String idAtto) {
		this.idAtto = idAtto;
	}

	public String getNumeroOrdinamento() {
		return numeroOrdinamento;
	}

	public void setNumeroOrdinamento(String numeroOrdinamento) {
		this.numeroOrdinamento = numeroOrdinamento;
	}

	public String getFirmatariList() {

		firmatariList = "";

		for (Firmatario firmatario : getFirmatari()) {
			if (firmatario.getDescrizione() != null) {
				firmatariList += firmatario.getDescrizione() + "-" + firmatario.getGruppoConsiliare() + "\n";
			}
		}
		return firmatariList;
	}

	public void setFirmatariList(String firmatariList) {
		this.firmatariList = firmatariList;
	}

}
