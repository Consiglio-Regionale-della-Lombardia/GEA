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
package com.sourcesense.crl.webscript.report.servizio_commissioni;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.sourcesense.crl.webscript.report.ReportBaseCommand;
import com.sourcesense.crl.webscript.report.util.office.DocxManager;

/**
 *
 * Generazione di report atti inviati ad organi esterni
 * V2 - Big Ok
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportAttiInviatiOrganiEsterniCommand extends ReportBaseCommand {

    /**
     * Generazione di un report di atti inviati ad organi esterni
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
            /* init json params */
            this.initLegislatura(json);
            this.initOrganismo(json);
            this.initDataAssegnazioneParereDa(json);
            this.initDataAssegnazioneParereA(json);
            /* sorting field */
            String sortField1 = "@{" + CRL_ATTI_MODEL + "}tipoAttoParere";
            String sortField2 = "@{" + CRL_ATTI_MODEL + "}numeroAttoParere";
            Map<String, ResultSet> organo2results = Maps.newHashMap();
            SearchParameters sp = new SearchParameters();
            sp.addStore(spacesStore);
            sp.setLanguage(SearchService.LANGUAGE_LUCENE);
            String query = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:" + this.legislatura + "//*\""
                    + "AND TYPE:\"crlatti:parere\" AND @crlatti\\:organismoStatutarioParere:\"" + this.organismo + "\"";
            if (!dataAssegnazioneParereDa.equals("*")
                    || !dataAssegnazioneParereA.equals("*")) {
                query += " AND @crlatti\\:dataAssegnazioneParere:["
                        + this.dataAssegnazioneParereDa + " TO "
                        + this.dataAssegnazioneParereA + " ]";
            }

            sp.setQuery(query);
            sp.addSort(sortField1, true);
            sp.addSort(sortField2, true);
            ResultSet pareriResult = this.searchService.query(sp);
            organo2results.put(this.organismo, pareriResult);
            Map<NodeRef, NodeRef> atto2parere = new HashMap<NodeRef, NodeRef>();
            LinkedListMultimap<String, NodeRef> parere2atti = this.retrieveAttiParere(organo2results, spacesStore, atto2parere); 
            XWPFDocument generatedDocument = docxManager.generateFromTemplateMap(this.retrieveLenghtMap(parere2atti), 2, false); 
            ByteArrayInputStream tempInputStream = saveTemp(generatedDocument);

            XWPFDocument finalDocument = this.fillTemplate(tempInputStream, parere2atti, atto2parere);
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
     * @param organo2atti - String organo ->  NodeRef type Atto
     * @param atto2parere - NodeRef type Atto -> NodeRef type Parere
     * @return {@link XWPFDocument} documento word del report
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public XWPFDocument fillTemplate(ByteArrayInputStream finalDocStream,
            LinkedListMultimap<String, NodeRef> organo2atti,
            Map<NodeRef, NodeRef> atto2parere) throws IOException {
        XWPFDocument document = new XWPFDocument(finalDocStream);
        int tableIndex = 0;
        List<XWPFTable> tables = document.getTables();
        for (String organo : organo2atti.keySet()) {
            for (NodeRef currentAtto : organo2atti.get(organo)) {
                XWPFTable currentTable = tables.get(tableIndex);
                Map<QName, Serializable> attoProperties = nodeService.getProperties(currentAtto);
                Map<QName, Serializable> parereProperties = nodeService.getProperties(atto2parere.get(currentAtto)); 
                QName nodeRefType = nodeService.getType(currentAtto);
                String tipoAtto = (String) nodeRefType.getLocalName();
                if (tipoAtto.length() > 4) {
                    tipoAtto = tipoAtto.substring(4);
                }

                String numeroAtto = StringUtils.EMPTY + (Integer) this.getNodeRefProperty(attoProperties, "numeroAtto")+(String) this.getNodeRefProperty(attoProperties,"estensioneAtto");
                String iniziativa = (String) this.getNodeRefProperty(attoProperties, "descrizioneIniziativa");
                String oggetto = (String) this.getNodeRefProperty(attoProperties, "oggetto");
                ArrayList<String> commReferenteList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "commReferente");
                String commReferente = this.renderList(commReferenteList);
                
                ArrayList<String> commCoReferenteList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "commCoreferente");
                String commCoReferente = this.renderList(commCoReferenteList);

                ArrayList<String> commConsultivaList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "commConsultiva");
                String commConsultiva = this.renderList(commConsultivaList); 
                ArrayList<String> pareriList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "organismiStatutari");
                String altriPareri = this.renderList(pareriList);

                Date dateAssegnazioneParere = (Date) this.getNodeRefProperty(parereProperties, "dataAssegnazioneParere");
                Date dateAssegnazioneCommissione = (Date) this.getNodeRefProperty(attoProperties, "dataAssegnazioneCommissioneReferente");

                currentTable.getRow(0).getCell(1).setText(this.checkStringEmpty(tipoAtto.toUpperCase() + " " + numeroAtto));
                currentTable.getRow(1).getCell(1).setText(this.checkStringEmpty(iniziativa));
                currentTable.getRow(2).getCell(1).setText(this.checkStringEmpty(oggetto));
                currentTable.getRow(3).getCell(1).setText(this.checkStringEmpty(commReferente));
                currentTable.getRow(3).getCell(3).setText(this.checkStringEmpty(altriPareri));
                currentTable.getRow(4).getCell(1).setText(this.checkStringEmpty(commCoReferente));
                currentTable.getRow(5).getCell(1).setText(this.checkStringEmpty(commConsultiva));
                currentTable.getRow(5).getCell(3).setText(this.checkDateEmpty(dateAssegnazioneCommissione));
                currentTable.getRow(6).getCell(1).setText(this.checkDateEmpty(dateAssegnazioneParere));

                tableIndex++;
            }
        }

        return document;
    }
}
