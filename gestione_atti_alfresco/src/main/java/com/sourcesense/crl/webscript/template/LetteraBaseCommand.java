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
import java.util.ArrayList;
import java.util.List;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.DynamicNamespacePrefixResolver;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;

import com.sourcesense.crl.util.AttoUtil;


/**
 * Questo comando contiente le funzionalita base delle generazione di un documento di tipo lettera.
 */
public abstract class LetteraBaseCommand implements LetteraCommand{
	
	protected ContentService contentService;
	protected SearchService searchService;
	protected NodeService nodeService;
	protected ServiceRegistry serviceRegistry;
	protected AttoUtil attoUtil; 
	protected String PROP_FIRMATARIO = "firmatario";
	protected String PROP_UFFICIO = "ufficio";
        protected String PROP_ASSESSORE = "assessore";
	protected String PROP_DIREZIONE = "direzione";
	protected String PROP_NUMEROTELFIRMATARIO = "numeroTelFirmatario";
	protected String PROP_EMAILFIRMATARIO = "emailFirmatario";

	/**
	 * Ritorna il documento costruito in base al template e i parametri in ingresso di tipo lettera base.
	 * @param templateByteArray il template del documento da generare
	 * @param templateNodeRef il riferimento al nodo del template
	 * @param attoNodeRef il riferimento al nodo del atto
	 * @param gruppo i gruppo di appartenza al momento della richiesta
	 * @return ritorna un documento di tipo documento di lettera .
	 * @throws IOException
	 */
	public abstract byte[] generate(byte[] templateByteArray, NodeRef templateNodeRef, NodeRef attoNodeRef, String gruppo) throws IOException;

	/**
	 * Recupera il valore di un nodo.
	 * @param attoNodeRef il rifferimento del nodo di tipo atto
	 * @param nameSpaceURI il namespace associato al nodo
	 * @param localName il nome con il locale del nodo da recuperare il valore.
	 * @return il valore del nodo con il localName
	 */
	public String getStringProperty(NodeRef attoNodeRef, String nameSpaceURI, String localName){
		String value = (String) nodeService.getProperty(attoNodeRef, QName.createQName(nameSpaceURI, localName));
		
		if(value==null){
			value = "";
		}
	
		return value;
	}

	/**
	 * Recupero delle commissioni consultive dal nodo atto
	 * @param attoNodeRef il riferimento al nodo del atto
	 * @return La lista dei nodi delle commissioni consultive
	 */
	public List<NodeRef> getCommissioniConsultive(NodeRef attoNodeRef){
		
		List<NodeRef> commissioniConsultiveList = new ArrayList<NodeRef>();
		
    	DynamicNamespacePrefixResolver namespacePrefixResolver = new DynamicNamespacePrefixResolver(null);
        namespacePrefixResolver.registerNamespace(NamespaceService.SYSTEM_MODEL_PREFIX, NamespaceService.SYSTEM_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(NamespaceService.CONTENT_MODEL_PREFIX, NamespaceService.CONTENT_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(NamespaceService.APP_MODEL_PREFIX, NamespaceService.APP_MODEL_1_0_URI); 
    	NodeRef passaggioNodeRef = attoUtil.getLastPassaggio(attoNodeRef);
    	String lucenePassaggioNodePath = nodeService.getPath(passaggioNodeRef).toPrefixString(namespacePrefixResolver);
  
		
    	ResultSet commissioniConsultiveNodes = null;
    	try{ 
	    	commissioniConsultiveNodes = searchService.query(attoNodeRef.getStoreRef(),
	  				SearchService.LANGUAGE_LUCENE, "PATH:\""+lucenePassaggioNodePath+"/cm:Commissioni/*\" AND @crlatti\\:ruoloCommissione:\""+attoUtil.RUOLO_COMM_CONSULTIVA+"\"");
	    	
	    	
	    	for (int i=0; i<commissioniConsultiveNodes.length(); i++) {
	    			
	    		commissioniConsultiveList.add(commissioniConsultiveNodes.getNodeRef(i));
	    	}
    	} finally {
    		if (commissioniConsultiveNodes!=null){
    			commissioniConsultiveNodes.close();
    		}
    		
    	}
		
		return commissioniConsultiveList;
	}


	/**
	 * Recupera ultimo organo statuario assegnato.
	 * @param attoNodeRef il nodo del atto
	 * @return il nodo dell'ultimo organo statuario assegnato.
	 */
	public NodeRef getLastOrganoStatutarioAssegnato(NodeRef attoNodeRef){
		
		NodeRef lastOrgano = null;
		
		DynamicNamespacePrefixResolver namespacePrefixResolver = new DynamicNamespacePrefixResolver(null);
        namespacePrefixResolver.registerNamespace(NamespaceService.SYSTEM_MODEL_PREFIX, NamespaceService.SYSTEM_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(NamespaceService.CONTENT_MODEL_PREFIX, NamespaceService.CONTENT_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(NamespaceService.APP_MODEL_PREFIX, NamespaceService.APP_MODEL_1_0_URI); 
    	String luceneAttoNodePath = nodeService.getPath(attoNodeRef).toPrefixString(namespacePrefixResolver);
	
    		
    	String parereType = "{"+attoUtil.CRL_ATTI_MODEL+"}"+attoUtil.PARERE_TYPE;
    			
    	SearchParameters sp = new SearchParameters();
        sp.addStore(attoNodeRef.getStoreRef());
        sp.setLanguage(SearchService.LANGUAGE_LUCENE);
        sp.setQuery("PATH:\""+luceneAttoNodePath+"/cm:Pareri/*\" AND TYPE:\""+parereType+"\"");
        String field = "{"+attoUtil.CRL_ATTI_MODEL+"}"+attoUtil.PROP_DATA_ASSEGNAZIONE_PARERE;
        sp.addSort(field, false);
    
        ResultSet pareriNodes = null;
        try{
        	pareriNodes = serviceRegistry.getSearchService().query(sp);
            
            if(pareriNodes.getNodeRefs().size()>0){
            	lastOrgano = pareriNodes.getNodeRef(0); 
            }
        } finally {
        	if (pareriNodes!=null){
        		pareriNodes.close();
        	}
        }
        
		
		return lastOrgano; 
	}

	/**
	 * Metodo che recupera ultimo firmatario dal atto.
	 * @param attoNodeRef il nodo del atto
	 * @return ritorna il nodo del ultimo firmatario
	 */
	public NodeRef getLastFirmatario(NodeRef attoNodeRef){
		
		NodeRef lastFirmatario = null;
		
		DynamicNamespacePrefixResolver namespacePrefixResolver = new DynamicNamespacePrefixResolver(null);
        namespacePrefixResolver.registerNamespace(NamespaceService.SYSTEM_MODEL_PREFIX, NamespaceService.SYSTEM_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(NamespaceService.CONTENT_MODEL_PREFIX, NamespaceService.CONTENT_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(NamespaceService.APP_MODEL_PREFIX, NamespaceService.APP_MODEL_1_0_URI); 
    	String luceneAttoNodePath = nodeService.getPath(attoNodeRef).toPrefixString(namespacePrefixResolver);
	
    		
    	String firmatarioType = "{"+attoUtil.CRL_ATTI_MODEL+"}"+attoUtil.FIRMATARIO_TYPE;
    			
    	SearchParameters sp = new SearchParameters();
        sp.addStore(attoNodeRef.getStoreRef());
        sp.setLanguage(SearchService.LANGUAGE_LUCENE);
        sp.setQuery("PATH:\""+luceneAttoNodePath+"/cm:Firmatari/*\" AND TYPE:\""+firmatarioType+"\"");
        String field = "@{"+attoUtil.CRL_ATTI_MODEL+"}"+attoUtil.PROP_DATA_FIRMA_FIRMATARIO;
        sp.addSort(field, false);
    
        ResultSet firmatariNodes=null;
        try{
	        firmatariNodes = serviceRegistry.getSearchService().query(sp);
	           
	        if(firmatariNodes.getNodeRefs().size()>0){
	        	lastFirmatario = firmatariNodes.getNodeRef(0); 
	        }
        } finally {
        	if (firmatariNodes!=null){
        		firmatariNodes.close();
        	}
        }
		return lastFirmatario; 
	}

	/**
	 * Recupera la sigla della tipologia del atto.
	 * @param attoNodeRef il nodo del atto.
	 * @return il valore della sigla per tipologia atto
	 */
	protected String getTipoAttoSigla(NodeRef attoNodeRef){
		QName typeQname= nodeService.getType(attoNodeRef);
		String type= typeQname.getLocalName();
		String typeSigla= type.substring(type.length()-3, type.length());
		return typeSigla.toUpperCase();
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

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
	public AttoUtil getAttoUtil() {
		return attoUtil;
	}

	public void setAttoUtil(AttoUtil attoUtil) {
		this.attoUtil = attoUtil;
	}
    
}
