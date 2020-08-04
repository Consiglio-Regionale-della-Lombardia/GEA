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
 * Stato dell'atto. Può essere dei seguenti tipi: Protocollato, Preso in carico
 * da S.C., Verificata ammissibilità, Proposta assegnazione, Preso in carico da
 * Commissioni, Nominato Relatore, Votato in Commissione, Trasmesso da
 * Commissione, Lavori Comitato Ristretto, Trasmesso ad Aula, Preso in carico da
 * Aula, Votato in Aula, Pubblicato, Chiuso, eac o mis
 * 
 * @author sourcesense
 *
 */
@JsonRootName("statoAtto")
@JsonTypeName("statoAtto")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class StatoAtto {

	public static final String PROTOCOLLATO = "Protocollato";
	public static final String PRESO_CARICO_SC = "Preso in carico da S.C.";
	public static final String VERIFICATA_AMMISSIBILITA = "Verificata ammissibilità";
	public static final String PROPOSTA_ASSEGNAZIONE = "Proposta assegnazione";

	public static final String ASSEGNATO_COMMISSIONE = "Assegnato a Commissione";
	public static final String PRESO_CARICO_COMMISSIONE = "Preso in carico da Commissioni";
	public static final String NOMINATO_RELATORE = "Nominato Relatore";
	public static final String VOTATO_COMMISSIONE = "Votato in Commissione";
	public static final String TRASMESSO_COMMISSIONE = "Trasmesso da Commissione";
	public static final String LAVORI_COMITATO_RISTRETTO = "Lavori Comitato Ristretto";

	public static final String TRASMESSO_AULA = "Trasmesso ad Aula";
	public static final String PRESO_CARICO_AULA = "Preso in carico da Aula";
	public static final String VOTATO_AULA = "Votato in Aula";
	public static final String PUBBLICATO = "Pubblicato";
	public static final String CHIUSO = "Chiuso";

	public static final String EAC = "eac";
	public static final String MIS = "mis";

	private String descrizione;

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	public StatoAtto() {

	}

	public StatoAtto(String descrizione) {

		this.descrizione = descrizione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
