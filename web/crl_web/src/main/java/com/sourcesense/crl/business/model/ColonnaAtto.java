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

/**
 * Tipo atto N° atto Oggetto Tipo iniziativa Firmatari Stato Tipo chiusura Com.
 * referente, co-referente, redigente o deliberante Data assegnazione
 * Commissioni consultive Relatore Data nomina relatore Abbinamenti "Esito
 * votazione commissione referente (o redigente o deliberante)" "Data votazione
 * commissione" Data scadenza "Data richiesta iscrizione all'aula" Esito
 * votazione Aula "Data votazione aula" N° DCR N° LCR BURL Data BURL N° LR Data
 * LR
 * 
 * @author sourcesense
 */
public class ColonnaAtto implements Serializable {

	private static final long serialVersionUID = 4556503563459586700L;

	private String nome;
	private String attoProperty;
	private boolean visible;

	public ColonnaAtto() {

	}

	public ColonnaAtto(String nome, String attoProperty, boolean visible) {

		this.nome = nome;
		this.visible = visible;
		this.attoProperty = attoProperty;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getAttoProperty() {
		return attoProperty;
	}

	public void setAttoProperty(String attoProperty) {
		this.attoProperty = attoProperty;
	}

}
