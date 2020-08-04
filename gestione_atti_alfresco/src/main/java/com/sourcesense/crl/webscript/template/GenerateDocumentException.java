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
package com.sourcesense.crl.webscript.template;

/**
 * <p>Classe per gestione delle eccezione nella generazione dei documenti
 * @author sourcesense
 */
public class GenerateDocumentException extends RuntimeException {

	private String className;

	/**
	 *
	 * @param className nome della classe di eccezione
	 */
    public GenerateDocumentException(String className){
		setClassName(className);
	}

	/**
	 *
	 * @param className nome della classe di eccezione
	 * @param t Istanza dell eccezzione
	 */
    public GenerateDocumentException(String className, Throwable t){
    	super (t);
		setClassName(className);
		
	}

	/**
	 *
	 * @return className nome della classe di eccezione
	 */
	public String getClassName() {
		return className;
	}

	/**
	 *
	 * @param className  nome della classe di eccezione
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
}
