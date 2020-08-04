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
package com.sourcesense.crl.webscript.odg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.alfresco.web.bean.repository.Repository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;


/**
 * Webscript JAVA in ALfresco che serve a generare l'ordine del giorno. Nel context del webscript si trovano i parametri con i tipi di ODG:
 * 	    <property name="odgCommandMap">
 *           <map>
 *           
 *               <entry key="crlodg:odgGenericoCommissioniDocument" value-ref="odgGenericoCommissioniCommand"/>
 *               <entry key="crlodg:odgGenericoAulaDocument" value-ref="odgGenericoAulaCommand"/>
 *           
 *           </map>
 *       </property>
 *       
 * Utilizza il layer ODGCommand perla generazione dei report.      
 * @author sourcesense
 *
 */
public class OdgWebScript extends AbstractWebScript {
	
	private ContentService contentService;
	private SearchService searchService;
	private NodeService nodeService;
	private Map<String, OdgCommand> odgCommandMap;
	
	private static Log logger = LogFactory.getLog(OdgWebScript.class);
	
	/**
	 * Metodo principale del webscript. Raccoglie dai parametri idSeduta, tipoTemplate e gruppo. Cerca il template che bisogna utilizzare per la creazione dell'ODG
	 * /app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Templates/ e genera il file Word con il report, che poi ritorna nel response direttamente.
	 */
    public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {
    	
    	OutputStream responseOutputStream = null;
    	InputStream templateInputStream = null;
    	
    	try { 
	    	String idSeduta = (String)req.getParameter("idSeduta");
    		String tipoTemplate = (String)req.getParameter("tipoTemplate");
    		String gruppo = (String)req.getParameter("gruppo");
    		
    		ResultSet templatesResults=null;
    		NodeRef templateNodeRef=null; 
			try{
	    		templatesResults = searchService.query(Repository.getStoreRef(), 
						SearchService.LANGUAGE_LUCENE, "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Templates//*\" AND TYPE:\""+tipoTemplate+"\"");
				
				templateNodeRef = templatesResults.getNodeRef(0);
			} finally {
				if (templatesResults!=null){
					templatesResults.close();
				}
			} 
			NodeRef sedutaNodeRef = new NodeRef(idSeduta); 
	    	ContentReader reader = contentService.getReader(templateNodeRef, ContentModel.PROP_CONTENT);
	    	templateInputStream = reader.getContentInputStream();
	    	byte[] templateByteArray = IOUtils.toByteArray(templateInputStream);
    		
    		
	    	byte[] documentFilledByteArray = odgCommandMap.get(tipoTemplate).generate(templateByteArray, templateNodeRef, sedutaNodeRef, gruppo);
	    	
	    	
	    	String nomeodg = tipoTemplate.split(":")[1]; 
            res.setContentType("application/ms-word");
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            res.setHeader( "Content-Disposition", "attachment; filename=\""+nomeodg+"_"+sdf.format(gc.getTime())+".docx\"" );
    		
    		responseOutputStream = res.getOutputStream();
            responseOutputStream.write(documentFilledByteArray);        
            
    	}catch(Exception e) {
    		logger.error("Exception details: "+e.getMessage());
    		throw new WebScriptException("Unable to generate document from template");
    	}finally {
    		if(templateInputStream != null) {
    			templateInputStream.close();
    			templateInputStream = null;
    		}
    		if(responseOutputStream != null) {
    			responseOutputStream.close();
        		responseOutputStream = null;
    		}
    	}
    }   
    
    
    public ContentService getContentService() {
        return contentService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }
    
    public SearchService getSearchService() {
        return searchService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }
    
    public NodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }


	public Map<String, OdgCommand> getOdgCommandMap() {
		return odgCommandMap;
	}


	public void setOdgCommandMap(Map<String, OdgCommand> odgCommandMap) {
		this.odgCommandMap = odgCommandMap;
	}
    
    
}
