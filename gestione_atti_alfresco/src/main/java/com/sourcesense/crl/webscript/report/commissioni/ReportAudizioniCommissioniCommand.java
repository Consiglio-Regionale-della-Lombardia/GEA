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
package com.sourcesense.crl.webscript.report.commissioni;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.json.JSONException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.sourcesense.crl.webscript.report.ReportBaseCommand;
import com.sourcesense.crl.webscript.report.util.office.DocxManager;
import java.util.LinkedHashMap;
import org.alfresco.model.ContentModel;

/**
 * Genarazione dei report di tipologia Audizioni delle commissioni.
 * V2 - Big Ok
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportAudizioniCommissioniCommand extends ReportBaseCommand {

    /**
     * Generazione di un report di Audizioni delle commissioni.
     * {@inheritDoc}
     */
    @Override
    public byte[] generate(byte[] templateByteArray, String json,
            StoreRef spacesStore) throws IOException {
        ByteArrayOutputStream ostream = null;
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(
                    templateByteArray);
            DocxManager docxManager = new DocxManager(is);
            this.initLegislatura(json);
            this.initCommonParams(json);
            this.initDataConsultazioneDa(json);
            this.initDataConsultazioneA(json);
            String sortField1 = "@{" + CRL_ATTI_MODEL + "}tipoAttoCommissione";
            String sortField2 = "@{" + CRL_ATTI_MODEL + "}numeroAttoCommissione";

            Map<String, ResultSet> commissione2results = Maps.newLinkedHashMap();

            for (String commissione : this.commissioniJson) {
                SearchParameters sp = new SearchParameters();
                sp.addStore(spacesStore);
                sp.setLanguage(SearchService.LANGUAGE_LUCENE);
                String query = "PATH: \"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:" + this.legislatura + "//*\""
                        + " AND TYPE:\""
                        + "crlatti:consultazione"
                        + "\" AND @crlatti\\:commissioneConsultazione:\""
                        + commissione + "\"";
                if (!dataConsultazioneDa.equals("*")
                        || !dataConsultazioneA.equals("*")) {
                    query += " AND @crlatti\\:dataConsultazione:["
                            + this.dataConsultazioneDa + " TO "
                            + this.dataConsultazioneA + " ]";
                }
                sp.setQuery(query);  
                ResultSet currentResults = this.searchService.query(sp);
                commissione2results.put(commissione, currentResults);
            }

            Map<NodeRef, NodeRef> atto2commissione = new LinkedHashMap<NodeRef, NodeRef>();
            ArrayListMultimap<String, NodeRef> commissione2atti = this.retrieveConsultazioni(commissione2results, spacesStore, atto2commissione); 
            XWPFDocument generatedDocument = docxManager.generateFromTemplateMap(this.retrieveLenghtMap(commissione2atti), 5, false); 
            ByteArrayInputStream tempInputStream = saveTemp(generatedDocument);

            XWPFDocument finalDocument = this.fillTemplate(tempInputStream, commissione2atti, atto2commissione);
            ostream = new ByteArrayOutputStream();
            finalDocument.write(ostream);

        } catch (JSONException e) { 
            e.printStackTrace();
        }
        return ostream.toByteArray();

    }

    /**
     * Valorizza il template docx con i valori recuperati dalla query verso alfresco.
     *
     * @param finalDocStream - docx stream del documento in generazione
     * @param commissione2consultazioni - String commissione ->  NodeRef type consultazioni
     * @param consultazione2atto - NodeRef type consultazione -> NodeRef type atto
     * @return {@link XWPFDocument} documento word del report
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public XWPFDocument fillTemplate(ByteArrayInputStream finalDocStream,
            ArrayListMultimap<String, NodeRef> commissione2consultazioni,
            Map<NodeRef, NodeRef> consultazione2atto) throws IOException {
        XWPFDocument document = new XWPFDocument(finalDocStream);
        int tableIndex = 0;
        List<XWPFTable> tables = document.getTables();
        for (String commissione : commissione2consultazioni.keySet()) {
            for (NodeRef consultazioniNode : commissione2consultazioni.get(commissione)) {
                XWPFTable currentTable = tables.get(tableIndex);
                NodeRef currentAtto = consultazione2atto.get(consultazioniNode);
                Map<QName, Serializable> attoProperties = nodeService.getProperties(currentAtto);
                Map<QName, Serializable> consultazioneProperties = nodeService.getProperties(consultazioniNode); 
                QName nodeRefType = nodeService.getType(currentAtto);
                String tipoAtto = (String) nodeRefType.getLocalName();
                if (tipoAtto.length() > 4) {
                    tipoAtto = tipoAtto.substring(4);
                }
                String numeroAtto = StringUtils.EMPTY + (Integer) this.getNodeRefProperty(attoProperties, "numeroAtto")+(String) this.getNodeRefProperty(attoProperties,"estensioneAtto");

                String oggetto = (String) this.getNodeRefProperty(attoProperties, "oggetto"); 
                Date dataConsultazione = (Date) this.getNodeRefProperty(consultazioneProperties, "dataConsultazione"); 
                String soggettiInvitati = (String) consultazioneProperties.get(ContentModel.PROP_NAME);

                StringBuffer sb = new StringBuffer();

                sb.append("Audizione in merito al ");
                sb.append(tipoAtto.toUpperCase());
                sb.append(" ");
                sb.append(numeroAtto);
                sb.append(" (");
                sb.append(oggetto); //oggetto
                sb.append("). Con: ");
                sb.append(soggettiInvitati); //soggetti consultanti
                sb.append(".");

                currentTable.getRow(0).getCell(0).setText(this.checkDateEmpty(dataConsultazione));
                currentTable.getRow(0).getCell(1).setText(this.checkStringEmpty(sb.toString()));

                tableIndex++;
            }
        }

        return document;
    }
}
