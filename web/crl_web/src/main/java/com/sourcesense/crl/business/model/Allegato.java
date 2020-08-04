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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.sourcesense.crl.util.JsonDateSerializer;

/**
 * Allegato rappresentato con le informazioni di nome, descrizione, download
 * url, pubblico, testo, tipo allegato, id, mimetype, data seduta, tipologia,
 * provenienza, passaggio, commissione, organismo statuario e consutazione
 * 
 * @author sourcesense
 *
 */
@JsonRootName("allegato")
@JsonTypeName("allegato")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoProperty
public class Allegato implements Cloneable {

	public static final String TIPO_PRESENTAZIONE_ASSEGNAZIONE = "allegato_atto";
	public static final String TESTO_ESAME_AULA_EMENDAMENTO = "emendamento_aula";
	public static final String TIPO_ESAME_COMMISSIONE_EMENDAMENTO = "emendamento_commissione";
	public static final String TIPO_ESAME_COMMISSIONE_ALLEGATO = "allegato_commissione";
	public static final String TIPO_ESAME_AULA_ALLEGATO = "allegato_aula";
	public static final String TIPO_PARERE = "allegato_parere";
	public static final String TIPO_CONSULTAZIONE = "allegato_consultazione";
	public static final String TESTO_ESAME_COMMISSIONE_COMITATO = "testo_atto_comitato_ristretto";
	public static final String TESTO_ESAME_COMMISSIONE_CLAUSOLA = "clausola_commissione";

	private String nome;
	private String descrizione;
	private String downloadUrl;
	private boolean pubblico;
	private boolean testo;
	private String tipoAllegato;
	private String id;
	private String mimetype;
	private Date dataSeduta;
	private String tipologia;
	private String provenienza;
	private String passaggio;
	private String commissione;
	private String organismoStatutario;
	private String consultazione;

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString() {
		return Pojomatic.toString(this);
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

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSeduta() {
		return dataSeduta;
	}

	public void setDataSeduta(Date dataSeduta) {
		this.dataSeduta = dataSeduta;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
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

	public String getOrganismoStatutario() {
		return organismoStatutario;
	}

	public void setOrganismoStatutario(String organismoStatutario) {
		this.organismoStatutario = organismoStatutario;
	}

	public String getConsultazione() {
		return consultazione;
	}

	public void setConsultazione(String consultazione) {
		this.consultazione = consultazione;
	}

	public boolean isTesto() {
		return testo;
	}

	public void setTesto(boolean testo) {
		this.testo = testo;
	}

}
