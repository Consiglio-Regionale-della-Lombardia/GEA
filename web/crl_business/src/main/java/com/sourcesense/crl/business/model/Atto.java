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
import java.util.Date;

/**
 * Atto rappresentato tramite le informazioni di tipo, codice, oggetto, primo
 * firmatario, data presentazione e stato
 * 
 * @author sourcesense
 * 
 */
public class Atto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String tipo;
	private String codice;
	private String oggetto;
	private String primoFirmatario;
	private Date dataPresentazione;
	private String stato;

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @return the oggetto
	 */
	public String getOggetto() {
		return oggetto;
	}

	/**
	 * @param oggetto the oggetto to set
	 */
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * @return the primoFirmatario
	 */
	public String getPrimoFirmatario() {
		return primoFirmatario;
	}

	/**
	 * @param primoFirmatario the primoFirmatario to set
	 */
	public void setPrimoFirmatario(String primoFirmatario) {
		this.primoFirmatario = primoFirmatario;
	}

	/**
	 * @return the dataPresentazione
	 */
	public Date getDataPresentazione() {
		return dataPresentazione;
	}

	/**
	 * @param dataPresentazione the dataPresentazione to set
	 */
	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}
}
