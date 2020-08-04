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
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.IoUtil;
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
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import com.sourcesense.crl.util.AttoUtil;

/**
 * Questo Web Script fa da interfaccia per la generazione dei documenti di tipo {@link LetteraCommand Lettera}
 */
public class LetteraWebScript extends AbstractWebScript {

	private ContentService contentService;
	private SearchService searchService;
	private NodeService nodeService;
	private Map<String, LetteraCommand> lettereCommandMap;

	private static Log logger = LogFactory.getLog(LetteraWebScript.class);

	/**
	 * Questo metodo in base ai parametri ricevuti in request
	 * crea  il documento {@link LetteraCommand Lettera} in formato doc.
	 * @param req WebScriptRequest
	 * @param res WebScriptResponse
	 * @throws IOException
	 */
	public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {

		OutputStream responseOutputStream = null;
		InputStream templateInputStream = null;

		ResultSet templatesResultsPlurale = null;
		ResultSet templatesResultsSingolare = null;
		ResultSet templatesResultsGenerico=null;
		ResultSet templatesResults=null;

		try { 
			String idAtto = (String) req.getParameter("idAtto");
			String tipoTemplate = (String) req.getParameter("tipoTemplate");
			String gruppo = (String) req.getParameter("gruppo"); 
			NodeRef attoNodeRef = new NodeRef(idAtto); 
			List<String> firmatariList = (List<String>) nodeService.getProperty(attoNodeRef,
					QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_FIRMATARI));

			boolean isTemplateFormaSingolare = true; 
			if (firmatariList != null && !firmatariList.isEmpty()) {
				if (firmatariList.size() > 1) { 
					templatesResultsPlurale = searchService.query(Repository.getStoreRef(),
							SearchService.LANGUAGE_LUCENE,
							"PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Templates//*\" AND TYPE:\""
									+ tipoTemplate + "\" " + "AND @cm\\:name:\"plural*\"");
					if (templatesResultsPlurale != null && templatesResultsPlurale.length() > 0) {

						isTemplateFormaSingolare = false;
					} else { 
						templatesResultsSingolare = searchService.query(Repository.getStoreRef(),
								SearchService.LANGUAGE_LUCENE,
								"PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Templates//*\" AND TYPE:\""
										+ tipoTemplate + "\"" + " AND @cm\\:name:\"singolar*\"");  
					}
				}
			}

			if (isTemplateFormaSingolare) {
				templatesResults = templatesResultsSingolare;
			} else {
				templatesResults = templatesResultsPlurale;
			}

			if (templatesResults == null || (templatesResults != null && templatesResults.length() == 0)) {  
				templatesResults=templatesResultsGenerico = searchService.query(Repository.getStoreRef(), SearchService.LANGUAGE_LUCENE,
						"PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Templates//*\" AND TYPE:\""
								+ tipoTemplate + "\"");
			}

			NodeRef templateNodeRef = templatesResults.getNodeRef(0); 
			ContentReader reader = contentService.getReader(templateNodeRef, ContentModel.PROP_CONTENT);
			templateInputStream = reader.getContentInputStream();
			byte[] templateByteArray = IOUtils.toByteArray(templateInputStream);

			byte[] documentFilledByteArray = lettereCommandMap.get(tipoTemplate).generate(templateByteArray,
					templateNodeRef, attoNodeRef, gruppo);

			String nomeLettera = tipoTemplate.split(":")[1]; 
			res.setContentType("application/ms-word");
			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			res.setHeader("Content-Disposition",
					"attachment; filename=\"" + nomeLettera + "_" + sdf.format(gc.getTime()) + ".doc\"");

			responseOutputStream = res.getOutputStream();
			responseOutputStream.write(documentFilledByteArray);

		} catch (Exception e) {
			logger.error("Exception details: " + e.getMessage(), e);
			throw new WebScriptException("Unable to generate document from template", e);
		} finally { 
			IoUtil.closeSilently(templateInputStream);
			IoUtil.closeSilently(responseOutputStream); 
			if (templatesResultsPlurale != null) {
				templatesResultsPlurale.close();
			}
			if (templatesResultsSingolare != null) {
				templatesResultsSingolare.close();
			}
			
			if (templatesResultsGenerico!=null){
				templatesResultsGenerico.close();
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

	public Map<String, LetteraCommand> getLettereCommandMap() {
		return lettereCommandMap;
	}

	public void setLettereCommandMap(Map<String, LetteraCommand> lettereCommandMap) {
		this.lettereCommandMap = lettereCommandMap;
	}

}
