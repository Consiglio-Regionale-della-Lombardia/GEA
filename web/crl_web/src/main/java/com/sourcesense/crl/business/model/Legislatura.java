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
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

/**
 * Legislatura
 * 
 * @author sourcesense
 *
 */
@JsonRootName("legislatura")
@JsonTypeName("legislatura")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class Legislatura {

	private String nome;
	private boolean corrente;
	private Date dataInizioLegislatura;
	private Date dataFineLegislatura;

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

	public boolean isCorrente() {
		return corrente;
	}

	public void setCorrente(boolean corrente) {
		this.corrente = corrente;
	}

	public Date getDataInizioLegislatura() {
		return dataInizioLegislatura;
	}

	public void setDataInizioLegislatura(Date dataInizioLegislatura) {
		this.dataInizioLegislatura = dataInizioLegislatura;
	}

	public Date getDataFineLegislatura() {
		return dataFineLegislatura;
	}

	public void setDataFineLegislatura(Date dataFineLegislatura) {
		this.dataFineLegislatura = dataFineLegislatura;
	}

}
