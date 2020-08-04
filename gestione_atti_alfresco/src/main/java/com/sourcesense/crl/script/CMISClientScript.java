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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.jscript.BaseScopableProcessorExtension;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CMISClientScript extends BaseScopableProcessorExtension {
	
	private String attiIspettiviAlfrescoURL;
	private String attiIspettiviAlfrescoUser;
	private String attiIspettiviAlfrescoPsw;
	private String attiIspettiviAlfrescoPath;
	
	private ServiceRegistry serviceRegistry;
	
	private static Log logger = LogFactory.getLog(CMISClientScript.class);
	/**
	 * Crea un nuovo documento utilizzando CMIS
	 * @param contentId ID dentro Alfresco (senza la parte workSpace://SpaceStore)
	 * @return tru se tutto è concluso bene. false se ci sono stati degli errori
	 */
	public boolean sendToAttiIspettiviRepository(String contentId) {
       
		try{
			
			SessionFactory factory = SessionFactoryImpl.newInstance();
			IdentityHashMap<String, String> param = new IdentityHashMap<String, String>();
			
			param.put(SessionParameter.USER, attiIspettiviAlfrescoUser);
			param.put(SessionParameter.PASSWORD, attiIspettiviAlfrescoPsw);
			param.put(SessionParameter.ATOMPUB_URL,  attiIspettiviAlfrescoURL+"/s/cmis");
			param.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
			
			List<Repository> repositories = factory.getRepositories(param);
			Session session = repositories.get(0).createSession();		
			Folder importFolder = (Folder) session.getObjectByPath(attiIspettiviAlfrescoPath);
			
			NodeRef contentNodeRef = new NodeRef(contentId);	
			String fileName = (String) serviceRegistry.getNodeService().getProperty(contentNodeRef, ContentModel.PROP_NAME);
			
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
			properties.put(PropertyIds.NAME, fileName);
			
			ContentReader reader = serviceRegistry.getContentService().getReader(contentNodeRef, ContentModel.PROP_CONTENT);
			ContentStream contentStream = new ContentStreamImpl(fileName, BigInteger.valueOf(reader.getSize()), "text/plain", reader.getContentInputStream()); 
			Document newDoc = importFolder.createDocument(properties, contentStream, VersioningState.MAJOR);
			
			return true; 
		    
		}catch (Exception e) { 
			logger.error(e); 
			return false;
		}
    }

	public String getAttiIspettiviAlfrescoURL() {
		return attiIspettiviAlfrescoURL;
	}


	public void setAttiIspettiviAlfrescoURL(String attiIspettiviAlfrescoURL) {
		this.attiIspettiviAlfrescoURL = attiIspettiviAlfrescoURL;
	}


	public String getAttiIspettiviAlfrescoUser() {
		return attiIspettiviAlfrescoUser;
	}


	public void setAttiIspettiviAlfrescoUser(String attiIspettiviAlfrescoUser) {
		this.attiIspettiviAlfrescoUser = attiIspettiviAlfrescoUser;
	}


	public String getAttiIspettiviAlfrescoPsw() {
		return attiIspettiviAlfrescoPsw;
	}


	public void setAttiIspettiviAlfrescoPsw(String attiIspettiviAlfrescoPsw) {
		this.attiIspettiviAlfrescoPsw = attiIspettiviAlfrescoPsw;
	}

	public String getAttiIspettiviAlfrescoPath() {
		return attiIspettiviAlfrescoPath;
	}

	public void setAttiIspettiviAlfrescoPath(String attiIspettiviAlfrescoPath) {
		this.attiIspettiviAlfrescoPath = attiIspettiviAlfrescoPath;
	}

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
		
}
