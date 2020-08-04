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

import java.io.IOException;

import org.alfresco.service.cmr.repository.NodeRef;

/**
 * Interfaccia che fa da base per tutti i tipi documento Lettera che si occupa di generare
 * un documento di tipologia lettera in base al template che riceve come parametro.
 */
public interface LetteraCommand {

	/**
	 * Ritorna il documento costruito in base al template e i parametri in ingresso.
	 * @param templateByteArray il template del documento da generare
	 * @param templateNodeRef il riferimento al nodo del template
	 * @param attoNodeRef il riferimento al nodo del atto
	 * @param gruppo i gruppo di appartenza al momento della richiesta
	 * @return documento generato in base al template passato come parametro.
	 * @throws IOException
	 */
	byte[] generate(byte[] templateByteArray, NodeRef templateNodeRef, NodeRef attoNodeRef, String gruppo)  throws IOException;
}
