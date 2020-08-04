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
package com.sourcesense.crl.job.anagrafica;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.jscript.BaseScopableProcessorExtension;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.AuthorityType;
import org.alfresco.service.namespace.DynamicNamespacePrefixResolver;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.web.bean.repository.Repository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alfresco.util.ISO9075;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Con questa classe si crea la legislatura corrente e si riempe con i consiglieri, siano correnti, siano storici.
 * @author sourcesense
 *
 */
public class AnagraficaImportScript extends BaseScopableProcessorExtension {

    private static Log logger = LogFactory.getLog(AnagraficaImportScript.class);

    private NodeService nodeService;
    private ContentService contentService;
    private FileFolderService fileFolderService;
    private SearchService searchService;
    private AuthorityService authorityService;
    private DataExtractor dataExtractor;
    
   
/**
 * Fa l'import dei consiglieri della legislatura corrente dentro Alfresco. Se non esiste la legislatura, viene creata. Path dove si cercano le legislature in Alfresco:
 * app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Legislature. Soltanto può esistere una legistatura corrente.
 * I consiglieri attivi si cercano dentro Alfresco: app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriAttivi
 * importa pure i gruppi dei consiliari che si trovano in Alfresco: /app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:GruppiConsiliari\
 * Si crea l'authority group con il nome: LEGISLATURA_NomeCommissione che servirà a permettere l'accesso ai dati ai consiglieri attivi.
 * Si aggiungono pure i consilgieri storici: app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriStorici
 */
    public void executeImport() {
        logger.debug("Start of method 'executeImport'");

        /* Extract data from DB */
        List<Legislature> legislatures = dataExtractor.getLegislatures();        
       
        /* Locate legislature folder node on Alfresco repository */
        ResultSet legislatureFolderNodeResultSet=null;
        ResultSet legislatureNodeResultSet=null;
        try{
        legislatureFolderNodeResultSet = searchService.query(Repository.getStoreRef(),
        		SearchService.LANGUAGE_LUCENE, "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Legislature\"");
        
        
        Legislature legislature;
        
        /* TODO posso fare una query che prende tutte i nodi legislature e fa un confronto. Così non faccio n chiamate con searchservice */
        for(int i=0; i<legislatures.size(); i++){
        	legislature = legislatures.get(i);
        	legislatureNodeResultSet = searchService.query(Repository.getStoreRef(),
                    SearchService.LANGUAGE_LUCENE, "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Legislature/cm:"+legislature.getNumber()+"\"");
        
        	
        	/* if legislature node does not exist...create it */
        	if(legislatureNodeResultSet.length() == 0) {
        		
        		FileInfo legislatureFileInfo = fileFolderService.create(legislatureFolderNodeResultSet.getNodeRef(0), legislature.getNumber(), Constant.TYPE_LEGISLATURA);
        		logger.info("Creato il nodo legislatura '" + legislature.getNumber() + "'");    
        		nodeService.setProperty(legislatureFileInfo.getNodeRef(), Constant.PROP_ID_ANAGRAFICA, legislature.getId()); 
                nodeService.setProperty(legislatureFileInfo.getNodeRef(), Constant.PROP_DATA_INIZIO_LEGISLATURA, legislature.getFrom()); 
                nodeService.setProperty(legislatureFileInfo.getNodeRef(), Constant.PROP_DATA_FINE_LEGISLATURA, legislature.getTo());  
                ContentWriter contentWriter = contentService.getWriter(legislatureFileInfo.getNodeRef(), ContentModel.PROP_CONTENT, true);
                contentWriter.setMimetype("text/plain");
                contentWriter.setEncoding("UTF-8");
                contentWriter.putContent(legislature.getNumber());
                
        	}
        }}finally{
        	if (legislatureFolderNodeResultSet!=null){
        		legislatureFolderNodeResultSet.close();
        	}
        	if (legislatureNodeResultSet!=null){
        		legislatureNodeResultSet.close();
        	}
        	
        }
   

        ResultSet currentlegislatureNodeResultSet=null;
        try{
	        currentlegislatureNodeResultSet = searchService.query(Repository.getStoreRef(),
	  				SearchService.LANGUAGE_LUCENE, 
	  				"PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Legislature/*\" AND @crlatti\\:correnteLegislatura:\"true\"");
	    
	        if(currentlegislatureNodeResultSet.length() == 1) {
	        	
	        	int idLegislaturaCorrente = (Integer) nodeService.getProperty(currentlegislatureNodeResultSet.getNodeRef(0), Constant.PROP_ID_ANAGRAFICA);
	        	String nomeLegislaturaCorrente = (String) nodeService.getProperty(currentlegislatureNodeResultSet.getNodeRef(0), ContentModel.PROP_NAME);
	        	List<Councilor> councilors = dataExtractor.getCouncilors(idLegislaturaCorrente);
	
	        	/* If no councilor exists... */
	            if (councilors.size() <= 0) {
	                logger.warn("Nessun consigliere estratto dal DB: termino");
	                return;
	            }
	            
	            logger.info("Estratti dal database anagrafica " + councilors.size() + " consiglieri per la legislatura "+nomeLegislaturaCorrente+" (corrente)");
	
	            /* Locate all councilors nodes on Alfresco repository */
	            ResultSet councilorNodesResultSet = searchService.query(Repository.getStoreRef(),
	                                                                SearchService.LANGUAGE_LUCENE,
	                                                                "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriAttivi/*\"");
	            logger.info("Trovati " + councilorNodesResultSet.length() + " consiglieri attivi nel repository");
	            
	
	            /* Delete all councilors on Alfresco repository */
	            if (councilorNodesResultSet.length() > 0) {
	                for (ChildAssociationRef councilorNodeRef : councilorNodesResultSet.getChildAssocRefs()) {
	                    fileFolderService.delete(councilorNodeRef.getChildRef());
	                    logger.info("Cancellato il nodo '" + councilorNodeRef.getQName().getLocalName() + "' dal repository");
	                }
	            }
	            
	            /* Locate all committes nodes on Alfresco repository */    
	            
	
	            /* Delete all committees on Alfresco repository */           
	            
	            /* Locate all group nodes on Alfresco repository */
	            ResultSet groupNodesResultSet = null;
		        try {
		        	groupNodesResultSet = searchService.query(Repository.getStoreRef(),
		                                                                SearchService.LANGUAGE_LUCENE,
		                                                                "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:GruppiConsiliari/*\"");
		            logger.info("Trovati " + groupNodesResultSet.length() + " gruppi consiliari nel repository");
		            
		
		            /* Delete all groups folder node on Alfresco repository */
		            if (groupNodesResultSet.length() > 0) {
		                for (ChildAssociationRef groupNodeRef : groupNodesResultSet.getChildAssocRefs()) {
		                    fileFolderService.delete(groupNodeRef.getChildRef());
		                    logger.info("Cancellato il nodo '" + groupNodeRef.getQName().getLocalName() + "' dal repository");
		                }
		            }
		        } finally {
		        	if (groupNodesResultSet!=null){
		        		groupNodesResultSet.close();
		        	}
		        }
	            
	            /* extract DB data */
	            List<Group> groups = dataExtractor.getGroups();
	            
	            
	            ResultSet groupFolderNodeResultSet = null;
	            try{
		            /* Locate groups folder node on Alfresco repository */
		            groupFolderNodeResultSet = searchService.query(Repository.getStoreRef(),
		                                                                          SearchService.LANGUAGE_LUCENE,
		                                                                          "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:GruppiConsiliari\"");
		           
		            /* Load all groups extracted from DB on Alfresco repository */
		            
		            for (Group group : groups) {
		            	
		            	String groupNodeName = group.getId() + " " + group.getCode().trim(); 
		            	groupNodeName = groupNodeName.replaceAll("[^a-zA-Z0-9]+","");
		           
		            	
		            	FileInfo groupFileInfo = fileFolderService.create(groupFolderNodeResultSet.getNodeRef(0), groupNodeName, Constant.TYPE_GRUPPO_CONSILIARE_ANAGRAFICA);
		        		logger.info("Creato il nodo gruppo '" + group.getName() + "'");    
		        		nodeService.setProperty(groupFileInfo.getNodeRef(), Constant.PROP_ID_ANAGRAFICA, group.getId()); 
		                nodeService.setProperty(groupFileInfo.getNodeRef(), Constant.PROP_NOME_GRUPPO_CONSILIARE_ANAGRAFICA, group.getName()); 
		                nodeService.setProperty(groupFileInfo.getNodeRef(), Constant.PROP_CODICE_GRUPPO_CONSILIARE_ANAGRAFICA, group.getCode());  
		                ContentWriter contentWriter = contentService.getWriter(groupFileInfo.getNodeRef(), ContentModel.PROP_CONTENT, true);
		                contentWriter.setMimetype("text/plain");
		                contentWriter.setEncoding("UTF-8");
		                contentWriter.putContent(groupNodeName);
		            }
	            } finally {
	            	if (groupFolderNodeResultSet!=null) {
	            		groupFolderNodeResultSet.close();
	            	}
	            }
	            
	            
	            /* Locate councilors folder node on Alfresco repository */
	            ResultSet councilorsFolderNodeResultSet = searchService.query(Repository.getStoreRef(),
	                                                                          SearchService.LANGUAGE_LUCENE,
	                                                                          "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriAttivi\"");
	            
	            /* Locate committes folder node on Alfresco repository */ 
	            ResultSet committesFolderNodeResultSet = searchService.query(Repository.getStoreRef(),
	                                                                          SearchService.LANGUAGE_LUCENE,
	                                                                          "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Commissioni\"");
	            
	            /* Load all councilors extracted from DB on Alfresco repository */
	           
	            Map<String, NodeRef> committesMap = new HashMap<String, NodeRef>();
	            
	            int count = 0;
	            
	            for (Councilor councilor : councilors) {
	
	            	NodeRef counciliorNodeRef = createCouncilor(councilor, councilorsFolderNodeResultSet.getNodeRef(0), nomeLegislaturaCorrente);
	            	
	            	if(counciliorNodeRef!=null){
	            		count ++;
	            	}
	                
	                /* check for committes */
	                for (int k=0; k<councilor.getCommittees().size(); k++){
	                	
	                	String committeName = councilor.getCommittees().get(k).getName();
	                	int committeOrder = councilor.getCommittees().get(k).getOrder();  
	                	
	                	/* if committe does not exist...create it and insert the NodeRef in map */
	                	if(!committesMap.containsKey(committeName)){  
	                		
	                		String luceneQueryCommissione = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Commissioni/cm:"+ISO9075.encode(committeName)+"\"";
	                		
	                		ResultSet commissioneResults = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_LUCENE, luceneQueryCommissione);
	                		NodeRef commissioneCorrente;
	                		if(commissioneResults!=null && commissioneResults.length()>0){
	                			commissioneCorrente = commissioneResults.getNodeRef(0);
	                		}else {
	                			commissioneCorrente = fileFolderService.create(committesFolderNodeResultSet.getNodeRef(0),committeName, Constant.TYPE_COMMISSIONE_ANAGRAFICA).getNodeRef();
	                		}	
	                			nodeService.setProperty(commissioneCorrente, Constant.PROP_NUMERO_ORDINAMENTO_COMMISSIONE_ANAGRAFICA, committeOrder);
		                		
		                		
	                		/* create alfresco authority (group) named as "LEGISLATURA_NomeCommissione" if does not exist */
	                		String alfrescoGroupName = nomeLegislaturaCorrente + "_" + committeName;
	                		
	                		if(authorityService.getAuthorityNodeRef("GROUP_"+alfrescoGroupName)==null){
	                			authorityService.createAuthority(AuthorityType.GROUP, alfrescoGroupName);
	                			authorityService.addAuthority(Constant.GROUP_COMMISSIONI, "GROUP_"+alfrescoGroupName);
	                			logger.info("Creata l'authority '"+alfrescoGroupName+"'"); 
	                		}
	                		

	                		committesMap.put(committeName,commissioneCorrente);
	                		logger.info("Aggiornato il nodo folder '"+commissioneCorrente+"' nel repository di Alfresco"); 
	                		if (commissioneResults!=null){
	                			commissioneResults.close();
	                		}
	                	}
	                	createCouncilor(councilor, committesMap.get(committeName), nomeLegislaturaCorrente); 
	                	
	                }        
	                
	            }
	            councilorsFolderNodeResultSet.close();
	            
	            logger.info("Inseriti " + count + " consiglieri attivi nel repository");
	            
	            
	            ResultSet historicalCouncilorNodesResultSet=null;
	            ResultSet historicalCouncilorsFolderNodeResultSet=null;
	            int historicalCount = 0;
	            
	            try{
	            	
	            
		            /* Locate all councilors nodes on Alfresco repository */
		            historicalCouncilorNodesResultSet = searchService.query(Repository.getStoreRef(),
		                                                                SearchService.LANGUAGE_LUCENE,
		                                                                "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriStorici/*\"");
		            logger.info("Trovati " + historicalCouncilorNodesResultSet.length() + " consiglieri storici nel repository");
		            
		            /* Locate councilors folder node on Alfresco repository */
		            historicalCouncilorsFolderNodeResultSet = searchService.query(Repository.getStoreRef(),
		                                                                          SearchService.LANGUAGE_LUCENE,
		                                                                          "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriStorici\"");
		            
		            
		            for (Councilor councilor : councilors) {
		                
		            	boolean exist = false;
		            	
		            	for(int j=0; j<historicalCouncilorNodesResultSet.length(); j++){
		            		
		            		int historicalCouncilorID = (Integer) nodeService.getProperty(historicalCouncilorNodesResultSet.getNodeRef(j), Constant.PROP_ID_ANAGRAFICA);
		            		if(historicalCouncilorID == councilor.getId()){
		            			exist = true;
		            		}
		            	}
		            	
		            	if(exist == false) {
		            		historicalCount ++;
		            		
		            		createCouncilor(councilor, historicalCouncilorsFolderNodeResultSet.getNodeRef(0), nomeLegislaturaCorrente);
		            		
		            	}
		            }
	            } finally {
	            	if (historicalCouncilorNodesResultSet!=null){
	            		historicalCouncilorNodesResultSet.close();
	            	}
		            if (historicalCouncilorsFolderNodeResultSet!=null){
		            	historicalCouncilorsFolderNodeResultSet.close();
		            }
	            }
	            
	            
	            
	            logger.info("Inseriti " + historicalCount + " consiglieri storici nel repository");
	            
	            
	            logger.info("Fine della sincronizzazione con il sistema di gestione anagrafica");
	        	
	        }else if(currentlegislatureNodeResultSet.length() > 1){
	        	
	        	logger.warn("Piu' di una legislatura attiva presente. Non e' stato possibile eseguire la sincronizzazione con il sistema di gestione anagrafica");
	        }else{
	        	logger.warn("Nessuna legislatura attiva presente. Non e' stato possibile eseguire la sincronizzazione con il sistema di gestione anagrafica");
	        }
        } finally {
        	if (currentlegislatureNodeResultSet!=null){
        		currentlegislatureNodeResultSet.close();
        	}
        }
        
        logger.debug("end of method 'executeImport'");
        
    }
    
    
    /**
     * Crea un cosigliere inserendolo nella cartella folderNodeRef.
     * @param councilor Struttura di dati con tutti i dati anagrafici del consigliere
     * @param folderNodeRef Cartella dove verrà creato il consigliere SE NON ESISTE ANCORA
     * @param nomeLegislaturaCorrente idem
     * @return il NodeRef del nuovo consigliere in ALfresco. Null se qualcosa va storto.
     */
    private NodeRef createCouncilor(Councilor councilor, NodeRef folderNodeRef, String nomeLegislaturaCorrente){
    	
    	NodeRef counciliorNodeRef = null;
    	
    	String councilorNodeName = councilor.getId() + " " + councilor.getFirstName() + " " + councilor.getLastName();
    	
    	String nomeFolderNode = (String) nodeService.getProperty(folderNodeRef, ContentModel.PROP_NAME); 
    	
    	DynamicNamespacePrefixResolver namespacePrefixResolver = new DynamicNamespacePrefixResolver(null);
        namespacePrefixResolver.registerNamespace(NamespaceService.SYSTEM_MODEL_PREFIX, NamespaceService.SYSTEM_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(NamespaceService.CONTENT_MODEL_PREFIX, NamespaceService.CONTENT_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(NamespaceService.APP_MODEL_PREFIX, NamespaceService.APP_MODEL_1_0_URI);

		
    	String luceneFolderNodePath = nodeService.getPath(folderNodeRef).toPrefixString(namespacePrefixResolver);				
	  	ResultSet testExistNode = searchService.query(folderNodeRef.getStoreRef(), 
  				SearchService.LANGUAGE_LUCENE, "PATH:\""+luceneFolderNodePath+"/cm:"+ISO9075.encode(councilorNodeName)+"\"");
    	
	  	if(testExistNode.length()==0){
    	
	        FileInfo councilorFileInfo = fileFolderService.create(folderNodeRef, councilorNodeName, Constant.TYPE_CONSIGLIERE_ANAGRAFICA);
	        logger.info("Creato il nodo '"+councilorFileInfo.getName()+"' nello spazio " + nomeFolderNode +" all'interno del repository di Alfresco");
	        nodeService.setProperty(councilorFileInfo.getNodeRef(), Constant.PROP_ID_ANAGRAFICA, councilor.getId()); 
	        nodeService.setProperty(councilorFileInfo.getNodeRef(), Constant.PROP_NOME_CONSIGLIERE_ANAGRAFICA, councilor.getFirstName()); 
	        nodeService.setProperty(councilorFileInfo.getNodeRef(), Constant.PROP_COGNOME_CONSIGLIERE_ANAGRAFICA, councilor.getLastName()); 
	        nodeService.setProperty(councilorFileInfo.getNodeRef(), Constant.PROP_LEGISLATURA_CONSIGLIERE_ANAGRAFICA, nomeLegislaturaCorrente); 
	        nodeService.setProperty(councilorFileInfo.getNodeRef(), Constant.PROP_GRUPPO_CONSIGLIERE_ANAGRAFICA, councilor.getGroupName()); 
	        nodeService.setProperty(councilorFileInfo.getNodeRef(), Constant.PROP_CODICE_GRUPPO_CONSIGLIERE_ANAGRAFICA, councilor.getCodeGroupName());  
	        ContentWriter contentWriter = contentService.getWriter(councilorFileInfo.getNodeRef(), ContentModel.PROP_CONTENT, true);
	        contentWriter.setMimetype("text/plain");
	        contentWriter.setEncoding("UTF-8");
	        contentWriter.putContent(councilorNodeName);
        
	        counciliorNodeRef = councilorFileInfo.getNodeRef();
	  	}
	 
	  	return counciliorNodeRef;
    }
    

    public NodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

	public ContentService getContentService() {
		return contentService;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public FileFolderService getFileFolderService() {
        return fileFolderService;
    }

    public void setFileFolderService(FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public AuthorityService getAuthorityService() {
		return authorityService;
	}

	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	public DataExtractor getDataExtractor() {
        return dataExtractor;
    }

    public void setDataExtractor(DataExtractor dataExtractor) {
        this.dataExtractor = dataExtractor;
    }
}
