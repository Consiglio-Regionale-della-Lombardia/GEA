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
package com.sourcesense.crl.business.service;

import com.sourcesense.crl.business.model.Atto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servizio di gestione degli atti
 * 
 * @author sourcesense
 * 
 */
public class AttoService implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Atto> data;

	/**
	 * Inizializza il servizio con dati mock. Utilizzabile per i test
	 */
	private void initDummyData() {
		data = new ArrayList<Atto>();

		data.add(getDummyDatum("121", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("122", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("123", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("124", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("125", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("126", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("127", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("128", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("129", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("130", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("131", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("132", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("133", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("134", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("135", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("136", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("137", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("138", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("139", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("140", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("141", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("142", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("143", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("144", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("145", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("146", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("147", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("148", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("149", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("150", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("151", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("152", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("153", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("154", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("155", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("156", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("157", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("158", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
		data.add(getDummyDatum("159", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Pubblicato", "PDL"));
		data.add(getDummyDatum("160", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"Trasmesso dalla Commissione", "PDL"));
		data.add(getDummyDatum("161", "Approvazione piano dei trasporti pubblici in Milano", "Mario Rossi",
				"In ODG generale dell’Aula", "PDL"));
	}

	public AttoService() {
		initDummyData();
	}

	/**
	 * Ritorna un atto passandogli i parametri
	 * 
	 * @param cod             Codice atto
	 * @param oggetto         Oggetto atto
	 * @param primoFirmatario Primo firmatario
	 * @param stato           Stato
	 * @param tipo            Tipo
	 * @return L'atto valorizzato con i parametri
	 */
	private Atto getDummyDatum(String cod, String oggetto, String primoFirmatario, String stato, String tipo) {
		Atto atto = new Atto();

		atto.setCodice(cod);
		atto.setDataPresentazione(new Date());
		atto.setOggetto(oggetto);
		atto.setPrimoFirmatario(primoFirmatario);
		atto.setStato(stato);
		atto.setTipo(tipo);

		return atto;
	}

	/**
	 * Ritorna un elenco di atti paginato secondo i parametri
	 * 
	 * @param first Il valore iniziale dell'elenco
	 * @param Il    valore finale dell'elenco
	 * @return L'elenco di atti
	 */
	public ArrayList<Atto> find(int first, int pageSize) {
		if (data == null)
			return null;

		ArrayList<Atto> ret = new ArrayList<Atto>();

		int max = count();

		for (int cont = first; cont < first + pageSize && cont < max; cont++) {
			ret.add(data.get(cont));
		}

		Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "ret: {0}", ret.size());

		return ret;
	}

	/**
	 * Ritorna il numero di atti disponibili dal servizio
	 * 
	 * @return il numero di atti disponibili dal servizio
	 */
	public int count() {
		Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "count: {0}", data.size());
		return data.size();
	}

	/**
	 * Ritorna l'atto scelto secondo il codice atto
	 * 
	 * @param cod Codice atto
	 * @return L'atto
	 */
	public Atto get(String cod) {
		if (data == null)
			return null;

		for (Atto atto : data) {
			if (atto.getCodice() != null && atto.getCodice().equals(cod)) {
				return atto;
			}
		}

		return null;
	}
}
