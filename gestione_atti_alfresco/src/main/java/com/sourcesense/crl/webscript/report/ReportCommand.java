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
package com.sourcesense.crl.webscript.report;

import java.io.IOException;

import org.alfresco.service.cmr.repository.StoreRef;

/**
 * Interface che si occupa di gestire la genereziane dei report
 * in modo generico in base agli parametri in input.
 * @author sourcesense
 */
public interface ReportCommand {
	/**
	 * Metodo che genera un documento di tipoligia report in base al template e gli paramtri in ingreso.
	 * @param templateByteArray template del report da generare
	 * @param json json con i parametri da sostituire nel template
	 * @param spacesStore lo spazio su alfresco per recuperare informazioni da inserire nel report
	 * @return il file generato dai template e i parametri in ingresso
	 * @throws IOException
	 */
	byte[] generate(byte[] templateByteArray,String json, StoreRef spacesStore)  throws IOException;
}
