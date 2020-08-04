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
 * Commissione referente che comprende data nomina e membri comitato ristretto
 * 
 * @author sourcesense
 *
 */
@JsonRootName("commissioneReferente")
@JsonTypeName("commissioneReferente")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class CommissioneReferente extends Commissione {

	private Date dataNomina;
	private List<Personale> membriComitatoRistretto = new ArrayList<Personale>();

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataNomina() {
		return dataNomina;
	}

	public void setDataNomina(Date dataNomina) {
		this.dataNomina = dataNomina;
	}

	public List<Personale> getMembriComitatoRistretto() {
		return this.membriComitatoRistretto;
	}

	public void setMembriComitatoRistretto(List<Personale> membriComitatoRistretto) {
		this.membriComitatoRistretto = membriComitatoRistretto;
	}

}
