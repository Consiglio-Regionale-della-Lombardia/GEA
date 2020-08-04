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

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

/**
 * Atto trattato rappresentato dalle informazioni dell'atto, previsto, discusso
 * e numero di ordinamento
 * 
 * @author sourcesense
 *
 */
@JsonRootName("attoTrattato")
@JsonTypeName("attoTrattato")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class AttoTrattato implements Cloneable, Comparable<AttoTrattato> {

	private Atto atto;
	private boolean previsto;
	private boolean discusso;
	private String numeroOrdinamento;

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int compareTo(AttoTrattato arg0) {
		String numero1 = (this.numeroOrdinamento == null || this.numeroOrdinamento.equals("")) ? "00"
				: this.numeroOrdinamento;
		String numero2 = (arg0.getNumeroOrdinamento() == null || arg0.getNumeroOrdinamento().equals("")) ? "00"
				: arg0.getNumeroOrdinamento();

		return Integer.parseInt(numero1) - Integer.parseInt(numero2);
	}

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	public Atto getAtto() {
		return atto;
	}

	public void setAtto(Atto atto) {
		this.atto = atto;
	}

	public boolean isPrevisto() {
		return previsto;
	}

	public void setPrevisto(boolean previsto) {
		this.previsto = previsto;
	}

	public boolean isDiscusso() {
		return discusso;
	}

	public void setDiscusso(boolean discusso) {
		this.discusso = discusso;
	}

	public String getNumeroOrdinamento() {
		return numeroOrdinamento;
	}

	public void setNumeroOrdinamento(String numeroOrdinamento) {
		this.numeroOrdinamento = numeroOrdinamento;
	}

}
