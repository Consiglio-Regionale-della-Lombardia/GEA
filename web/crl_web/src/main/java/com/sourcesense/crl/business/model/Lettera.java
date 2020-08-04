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

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.pojomatic.annotations.AutoProperty;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * Lettera
 * 
 * @author sourcesense
 *
 */
@Configurable()
@XmlRootElement(name = "lettera")
@JsonRootName("lettera")
@JsonTypeName("lettera")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@AutoProperty
public class Lettera implements Cloneable {

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	String idAtto;
	String tipoTemplate;
	String urlView;
	String nome;

	String firmatario;
	String ufficio;
	String direzione;
	String numeroTelFirmatario;
	String emailFirmatario;

	String assessore;

	boolean hasFirmatario;
	boolean hasUfficio;
	boolean hasDirezione;
	boolean hasNumeroTelFirmatario;
	boolean hasEmailFirmatario;
	boolean hasAssessore;

	public void setAuthorities(boolean hasFirmatario, boolean hasUfficio, boolean hasDirezione,
			boolean hasNumeroTelFirmatario, boolean hasEmailFirmatario, boolean hasAssessore) {

		this.hasFirmatario = hasFirmatario;
		this.hasUfficio = hasUfficio;
		this.hasDirezione = hasDirezione;
		this.hasNumeroTelFirmatario = hasNumeroTelFirmatario;
		this.hasEmailFirmatario = hasEmailFirmatario;
		this.hasAssessore = hasAssessore;

	}

	public String getIdAtto() {
		return idAtto;
	}

	public void setIdAtto(String idAtto) {
		this.idAtto = idAtto;
	}

	public String getTipoTemplate() {
		return tipoTemplate;
	}

	public void setTipoTemplate(String tipoTemplate) {
		this.tipoTemplate = tipoTemplate;
	}

	public String getUrlView() {
		return urlView;
	}

	public void setUrlView(String urlView) {
		this.urlView = urlView;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFirmatario() {
		return firmatario;
	}

	public void setFirmatario(String firmatario) {
		this.firmatario = firmatario;
	}

	public String getUfficio() {
		return ufficio;
	}

	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}

	public String getDirezione() {
		return direzione;
	}

	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}

	public String getNumeroTelFirmatario() {
		return numeroTelFirmatario;
	}

	public void setNumeroTelFirmatario(String numeroTelFirmatario) {
		this.numeroTelFirmatario = numeroTelFirmatario;
	}

	public String getEmailFirmatario() {
		return emailFirmatario;
	}

	public void setEmailFirmatario(String emailFirmatario) {
		this.emailFirmatario = emailFirmatario;
	}

	public boolean isHasFirmatario() {
		return hasFirmatario;
	}

	public void setHasFirmatario(boolean hasFirmatario) {
		this.hasFirmatario = hasFirmatario;
	}

	public boolean isHasUfficio() {
		return hasUfficio;
	}

	public void setHasUfficio(boolean hasUfficio) {
		this.hasUfficio = hasUfficio;
	}

	public boolean isHasDirezione() {
		return hasDirezione;
	}

	public void setHasDirezione(boolean hasDirezione) {
		this.hasDirezione = hasDirezione;
	}

	public boolean isHasNumeroTelFirmatario() {
		return hasNumeroTelFirmatario;
	}

	public void setHasNumeroTelFirmatario(boolean hasNumeroTelFirmatario) {
		this.hasNumeroTelFirmatario = hasNumeroTelFirmatario;
	}

	public boolean isHasEmailFirmatario() {
		return hasEmailFirmatario;
	}

	public void setHasEmailFirmatario(boolean hasEmailFirmatario) {
		this.hasEmailFirmatario = hasEmailFirmatario;
	}

	public String getAssessore() {
		return assessore;
	}

	public void setAssessore(String assessore) {
		this.assessore = assessore;
	}

	public boolean isHasAssessore() {
		return hasAssessore;
	}

	public void setHasAssessore(boolean hasAssessore) {
		this.hasAssessore = hasAssessore;
	}

}
