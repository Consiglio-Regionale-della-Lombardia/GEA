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
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.sourcesense.crl.webscript.template.LetteraCommand;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
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
 * Web service alfresco che si occupa di generare i vari {@link ReportCommand Report} in base a i parametri ricevuti.
 * @author sourcesense
 */
public class ReportWebScript extends AbstractWebScript {
    private ContentService contentService;
    private SearchService searchService;
    private NodeService nodeService;
    private DictionaryService dictionaryService;
    private Map<String, ReportCommand> reportCommandMap;

    private static Log logger = LogFactory.getLog(ReportWebScript.class);

	/**
	 *
	 * Generazione di un report in base alle info che arrivano in ingresso in formato json.
	 * Report Json example	 *
	 * {"tipoTemplate":"crlreport:reportAttiAssCommissioniServComm",
	 * 	    "legislatura":"IX",
	 *     "nome":"Atti assegnati alle commissioni","tipiAtto":["PAR","REF","PDA","PDL","PRE"],
	 *      "commissioni":[],"ruoloCommissione":"Referente","dataAssegnazioneDa":1355958000000,"dataAssegnazioneA":1355958000000,
	 *      "dataVotazioneCommReferenteDa":null,"dataVotazioneCommReferenteA":null,"dataRitiroDa":null,"dataRitiroA":null,
	 *      "dataNominaRelatoreDa":null,"dataNominaRelatoreA":null,"relatori":null,"organismo":null,"dataAssegnazioneParereDa":null,
	 *      "dataAssegnazioneParereA":null,"firmatario":null,"tipologiaFirma":null,"dataPresentazioneDa":null,"dataPresentazioneA":null,
	 *      "dataAssegnazioneCommReferenteDa":null,"dataAssegnazioneCommReferenteA":null,"dataSedutaDa":null,"dataSedutaA":null
	 *  }
	 *
	 * @param req WebScriptRequest
	 * @param res WebScriptResponse
	 * @throws IOException
	 */
    public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {
        long startTs = System.currentTimeMillis();

        AuthenticationUtil.setRunAsUserSystem();

        OutputStream responseOutputStream = null;
        InputStream templateInputStream = null;

        try {


            String json = IOUtils.toString(req.getContent().getReader());
            JSONObject jsonObj = new JSONObject(json);
            String tipoTemplate = (String) jsonObj.get("tipoTemplate");

            ResultSet templatesResults = null;
            NodeRef templateNodeRef = null;
            try {
                templatesResults = searchService.query(Repository.getStoreRef(),

                        SearchService.LANGUAGE_LUCENE, "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Reports//*\" AND TYPE:\"" + tipoTemplate + "\"");

                templateNodeRef = templatesResults.getNodeRef(0);
            } finally {
                templatesResults.close();
            }
            ContentReader reader = contentService.getReader(templateNodeRef, ContentModel.PROP_CONTENT);
            templateInputStream = reader.getContentInputStream();
            byte[] templateByteArray = IOUtils.toByteArray(templateInputStream);

            StoreRef searchStoreRef = null;
            List<StoreRef> stores = nodeService.getStores();
            for (StoreRef store : stores) {
                if (store.toString().equals("workspace://SpacesStore"))
                    searchStoreRef = store;
            }



            byte[] documentFilledByteArray = reportCommandMap.get(tipoTemplate).generate(templateByteArray, json, searchStoreRef);

            long duration = System.currentTimeMillis() - startTs;
            logger.info("Report " + tipoTemplate + " finished in " + duration + "ms");

            String nomeLettera = tipoTemplate.split(":")[1];
            res.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            res.setHeader("Content-Disposition", "attachment; filename=\"" + nomeLettera + "_" + sdf.format(gc.getTime()) + ".docx\"");

            responseOutputStream = res.getOutputStream();
            responseOutputStream.write(documentFilledByteArray);

        } catch (Exception e) {
            logger.error("Unable to generate document from template", e);
            throw new WebScriptException("Unable to generate document from template");
        } finally {
            if (templateInputStream != null) {
                templateInputStream.close();
                templateInputStream = null;
            }
            if (responseOutputStream != null) {
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

    public Map<String, ReportCommand> getReportCommandMap() {
        return reportCommandMap;
    }

    public void setReportCommandMap(Map<String, ReportCommand> reportCommandMap) {
        this.reportCommandMap = reportCommandMap;
    }

    public DictionaryService getDictionaryService() {
        return dictionaryService;
    }

    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
}
