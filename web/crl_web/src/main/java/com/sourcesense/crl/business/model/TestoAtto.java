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
import org.pojomatic.annotations.AutoProperty;

/**
 * Testo dell'atto. Comprende l'url per il download
 * 
 * @author sourcesense
 *
 */
@JsonRootName("attoRecord")
@JsonTypeName("attoRecord")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class TestoAtto implements Cloneable {

	public static final String TESTO_PRESENTAZIONE_ASSEGNAZIONE = "testo_atto";
	public static final String TESTO_ESAME_AULA_VOTAZIONE = "testo_atto_votato_aula";
	public static final String TESTO_ESAME_COMMISSIONE_VOTAZIONE = "testo_atto_votato_commissione";
	public static final String TESTO_TRASMESSO_IN_AULA = "testo_trasmesso_in_aula_esame_commissioni";
	public static final String RELAZIONE_ILLUSTRATIVA_E_LA_RELAZIONE_TECNICO_FINANZIARIA = "testo_relazione_illustrativa_tecnico_finanziaria_esame_commissioni";
	public static final String SCHEDA_ISTRUTTORIA_DEL_PROGETTO_DI_LEGGE = "testo_scheda_istruttoria_progetto_legge_esame_commissioni";
	public static final String ALTRO_DOCUMENTO = "testo_altro_documento_esame_commissioni";

	private String id;
	private String nome;
	private String mimetype;
	private String descrizione;
	private String downloadUrl;
	private boolean pubblico;
	private boolean pubblicoOpendata;
	private String tipoAllegato;
	private String tipologia;
	private String tipologiaTesto;
	private String provenienza;
	private String dataSeduta;
	private String passaggio;
	private String commissione;

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public boolean isPubblico() {
		return pubblico;
	}

	public void setPubblico(boolean pubblico) {
		this.pubblico = pubblico;
	}

	public boolean isPubblicoOpendata() {
		return pubblicoOpendata;
	}

	public void setPubblicoOpendata(boolean pubblicoOpendata) {
		this.pubblicoOpendata = pubblicoOpendata;
	}

	public String getTipoAllegato() {
		return tipoAllegato;
	}

	public void setTipoAllegato(String tipoAllegato) {
		this.tipoAllegato = tipoAllegato;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getDataSeduta() {
		return dataSeduta;
	}

	public void setDataSeduta(String dataSeduta) {
		this.dataSeduta = dataSeduta;
	}

	public String getPassaggio() {
		return passaggio;
	}

	public void setPassaggio(String passaggio) {
		this.passaggio = passaggio;
	}

	public String getCommissione() {
		return commissione;
	}

	public void setCommissione(String commissione) {
		this.commissione = commissione;
	}

	public String getTipologiaTesto() {
		return tipologiaTesto;
	}

	public void setTipologiaTesto(String tipologiaTesto) {
		this.tipologiaTesto = tipologiaTesto;
	}

}
