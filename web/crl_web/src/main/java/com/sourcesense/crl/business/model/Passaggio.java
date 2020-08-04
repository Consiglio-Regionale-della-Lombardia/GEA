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

/**
 * Passaggio in aula delle commissioni con relativi abbinamenti
 * 
 * @author sourcesense
 *
 */
@JsonRootName("passaggio")
@JsonTypeName("passaggio")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class Passaggio {

	private String nome;
	private List<Commissione> commissioni = new ArrayList<Commissione>();
	private List<Abbinamento> abbinamenti = new ArrayList<Abbinamento>();
	private Aula aula = new Aula();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Commissione> getCommissioni() {
		return commissioni;
	}

	public void setCommissioni(List<Commissione> commissioni) {
		this.commissioni = commissioni;
	}

	public List<Abbinamento> getAbbinamenti() {
		return abbinamenti;
	}

	public void setAbbinamenti(List<Abbinamento> abbinamenti) {
		this.abbinamenti = abbinamenti;
	}

	public Aula getAula() {
		return aula;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

}
