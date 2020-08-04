/*
 *  * #%L
 *  * Alfresco Repository
 *  * %%
 *  * Copyright (C) 2005 - 2016 Alfresco Software Limited
 *  * %%
 *  * This file is part of the Alfresco software. 
 *  * If the software was purchased under a paid Alfresco license, the terms of 
 *  * the paid license agreement will prevail.  Otherwise, the software is 
 *  * provided under the following open source license terms:
 *  * 
 *  * Alfresco is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  * 
 *  * Alfresco is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  * 
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 *  * #L%
 */
package com.sourcesense.crl.webscript.report.model;

import java.util.Date;

import org.alfresco.service.cmr.repository.NodeRef;

/**
 * Modello del atto
 */
public class Atto implements Comparable<Atto> {
	private Date dataNomina;
	private String tipoAtto;
	private String numeroAtto;
	private String consigliere;
	
	private NodeRef attoNodeRef;

	public Atto(Date dataNomina, String tipoAtto, String numeroAtto,
			String consigliere, NodeRef attoNodeRef) {
		super();
		this.dataNomina = dataNomina;
		this.tipoAtto = tipoAtto;
		this.numeroAtto = numeroAtto;
		this.consigliere = consigliere;
		this.attoNodeRef = attoNodeRef;
	}


	public Atto() {
	}


	public String getTipoAtto() {
		return tipoAtto;
	}

	public void setTipoAtto(String tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	public String getNumeroAtto() {
		return numeroAtto;
	}

	public void setNumeroAtto(String numeroAtto) {
		this.numeroAtto = numeroAtto;
	}

	public String getConsigliere() {
		return consigliere;
	}

	public void setConsigliere(String consigliere) {
		this.consigliere = consigliere;
	}

	public NodeRef getAttoNodeRef() {
		return attoNodeRef;
	}

	public void setAttoNodeRef(NodeRef attoNodeRef) {
		this.attoNodeRef = attoNodeRef;
	}

	public Date getDataNomina() {
		return dataNomina;
	}

	public void setDataNomina(Date dataNomina) {
		this.dataNomina = dataNomina;
	}


	@Override
	public int compareTo(Atto o) {
		int result=0;
		result=this.getDataNomina().compareTo(o.getDataNomina());
		if(result==0){
			result=this.getConsigliere().compareTo(o.getConsigliere());
		}
		if(result==0){
			result=this.getTipoAtto().compareTo(o.getTipoAtto());
		}
		if(result==0){
			result=this.getNumeroAtto().compareTo(o.getNumeroAtto());
		}
		return result;
	}

	
	

}
