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

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.pojomatic.annotations.AutoProperty;
import org.springframework.beans.factory.annotation.Configurable;

import com.sourcesense.crl.util.JsonDateSerializer;

/**
 * Seduta con relativo verbale
 * 
 * @author sourcesense
 *
 */
@Configurable()
@XmlRootElement(name = "sedutaAtto")
@JsonRootName("sedutaAtto")
@JsonTypeName("sedutaAtto")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class SedutaAtto implements Comparable<SedutaAtto> {

	private String idSeduta;
	private String nomeOrgano;
	private String ruolo;
	private Date dataSeduta;
	private List<Link> links = new ArrayList<Link>();
	private String numVerbale;
	private String idVerbale;
	private boolean pubblico;
	private String downloadUrl;
	private String nomeVerbale;
	private String mimetypeVerbale;
	private boolean discusso;

	@Override
	public int compareTo(SedutaAtto o) {
		return o.getDataSeduta().compareTo(this.dataSeduta);
	}

	public String getIdSeduta() {
		return idSeduta;
	}

	public void setIdSeduta(String idSeduta) {
		this.idSeduta = idSeduta;
	}

	public String getNomeOrgano() {
		return nomeOrgano;
	}

	public void setNomeOrgano(String nomeOrgano) {
		this.nomeOrgano = nomeOrgano;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDataSeduta() {
		return dataSeduta;
	}

	public void setDataSeduta(Date dataSeduta) {
		this.dataSeduta = dataSeduta;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public String getNumVerbale() {
		return numVerbale;
	}

	public void setNumVerbale(String numVerbale) {
		this.numVerbale = numVerbale;
	}

	public boolean isDiscusso() {
		return discusso;
	}

	public void setDiscusso(boolean discusso) {
		this.discusso = discusso;
	}

	public String getIdVerbale() {
		return idVerbale;
	}

	public void setIdVerbale(String idVerbale) {
		this.idVerbale = idVerbale;
	}

	public String getNomeVerbale() {
		return nomeVerbale;
	}

	public void setNomeVerbale(String nomeVerbale) {
		this.nomeVerbale = nomeVerbale;
	}

	public String getMimetypeVerbale() {
		return mimetypeVerbale;
	}

	public void setMimetypeVerbale(String mimetypeVerbale) {
		this.mimetypeVerbale = mimetypeVerbale;
	}

	public boolean isPubblico() {
		return pubblico;
	}

	public void setPubblico(boolean pubblico) {
		this.pubblico = pubblico;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

}
