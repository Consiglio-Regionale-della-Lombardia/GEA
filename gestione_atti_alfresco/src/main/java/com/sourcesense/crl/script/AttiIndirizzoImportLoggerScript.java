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
package com.sourcesense.crl.script;

import org.alfresco.repo.jscript.BaseScopableProcessorExtension;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Personalizzazione per log AttiIndirizzoImport 
 * @author sourcesense
 *
 */
public class AttiIndirizzoImportLoggerScript extends BaseScopableProcessorExtension {

	
	private static Log logger = LogFactory.getLog(AttiIndirizzoImportLoggerScript.class);

	
	public void info(String logString) {
        
		logger.info(logString);
				 
    }
	
	public void debug(String logString) {
	        
		logger.debug(logString);			
	        
	}
	
	public void error(String logString) {
        
		logger.error(logString);			
	        
	}
	
	public void fatal(String logString) {
        
		logger.fatal(logString);			
	        
	}
	
	public void trace(String logString) {
        
		logger.trace(logString);			
	        
	}
	
	public void warn(String logString) {
        
		logger.warn(logString);			
	        
	}
	
	
	
}
