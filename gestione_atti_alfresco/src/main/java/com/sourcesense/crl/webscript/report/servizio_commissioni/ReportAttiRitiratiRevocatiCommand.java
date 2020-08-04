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
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.sourcesense.crl.webscript.report.ReportBaseCommand;
import com.sourcesense.crl.webscript.report.util.office.DocxManager;

/**
 * Generazione report Atti ritirati e revocati
 * V2 - Big OK
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportAttiRitiratiRevocatiCommand extends ReportBaseCommand {

    /**
     * Generazione di un report di Atti ritirati e revocati
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
            this.initTipiAttoLuceneAtto(json);
            this.initDataRitiroDa(json);
            this.initDataRitiroA(json);

            /* sorting field */
            String sortField1 = "@{" + CRL_ATTI_MODEL + "}numeroAtto";
            Map<String, ResultSet> tipoAtto2results = Maps.newHashMap();
            for (String tipoAtto : this.tipiAttoLucene) {
                SearchParameters sp = new SearchParameters();
                sp.addStore(spacesStore);
                sp.setLanguage(SearchService.LANGUAGE_LUCENE);
                String query = "PATH: \"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:" + this.legislatura + "//*\""
                        + " AND TYPE:\""
                        + tipoAtto
                        + "\"";
                if (!dataRitiroDa.equals("*")
                        || !dataRitiroA.equals("*")) {
                    query += " AND @crlatti\\:dataChiusura:["
                            + this.dataRitiroDa + " TO " + this.dataRitiroA + " ]";
                }
                sp.setQuery(query);
                sp.addSort(sortField1, true);
                ResultSet attiResult = this.searchService.query(sp);
                tipoAtto2results.put(tipoAtto, attiResult);
            }
            LinkedListMultimap<String, NodeRef> tipoAtto2atti = this
                    .retrieveAtti(tipoAtto2results); 
            XWPFDocument generatedDocument = docxManager
                    .generateFromTemplateMap(
                    this.retrieveLenghtMapConditional(tipoAtto2atti, true), 3, false); 
            ByteArrayInputStream tempInputStream = saveTemp(generatedDocument);

            XWPFDocument finalDocument = this.fillTemplate(tempInputStream,
                    tipoAtto2atti);
            ostream = new ByteArrayOutputStream();
            finalDocument.write(ostream);

        } catch (JSONException e) { 
            e.printStackTrace();
        }
        return ostream.toByteArray();

    }

    /**
     * Recupera gli atti aggregati per tipo atto dal resulset di ricerca
     * @param tipoAtto2results  String tipo atto -> ResulSet ricerca alfresco
     * @return mappa String tipo atto -> list NodeRef type Atto
     */
    private LinkedListMultimap<String, NodeRef> retrieveAtti(
            Map<String, ResultSet> tipoAtto2results) {
        LinkedListMultimap<String, NodeRef> tipo2atti = LinkedListMultimap.create();
        for (String tipo : tipoAtto2results.keySet()) {
            ResultSet resultSet = tipoAtto2results.get(tipo);
            for (NodeRef atto : resultSet.getNodeRefs()) {
                tipo2atti.put(this.convertAttoType(tipo), atto);
            }
        }
        return tipo2atti;
    }

    /**
     * the substring for the Atto type : crlatti:attoPdl -> Pdl
     *
     * @param tipo tipo atto
     * @return the substring for the Atto type : crlatti:attoPdl -> Pdl
     */
    private String convertAttoType(String tipo) {
        return tipo.substring(12);
    }

    /**
     * Valorizza il template docx con i valori recuperati dalla query verso alfresco.
     *
     * @param finalDocStream - docx stream del documento in generazione
     * @param tipoAtto2atti - String tipoatto ->  NodeRef type Atto
     * @return {@link XWPFDocument} documento word del report
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public XWPFDocument fillTemplate(ByteArrayInputStream finalDocStream,
            LinkedListMultimap<String, NodeRef> tipoAtto2atti) throws IOException {
        XWPFDocument document = new XWPFDocument(finalDocStream);
        int tableIndex = 0;
        List<XWPFTable> tables = document.getTables();
        for (String tipoAttoString : tipoAtto2atti.keySet()) {
            List<NodeRef> attiList = tipoAtto2atti.get(tipoAttoString);
            for (NodeRef currentAtto : attiList) {
                Map<QName, Serializable> attoProperties = nodeService
                        .getProperties(currentAtto);
                String statoAtto = (String) this.getNodeRefProperty(
                        attoProperties, "statoAtto");
                String tipoChiusura = (String) this.getNodeRefProperty(
                        attoProperties, "tipoChiusura");
                if (this.checkStatoAtto(statoAtto, tipoChiusura)) {
                    QName nodeRefType = nodeService.getType(currentAtto);
                    String tipoAtto = (String) nodeRefType.getLocalName();
                    if (tipoAtto.length() > 4) {
                        tipoAtto = tipoAtto.substring(4);
                    }
                    XWPFTable currentTable = tables.get(tableIndex); 
                    String numeroAtto = StringUtils.EMPTY + (Integer) this.getNodeRefProperty(attoProperties, "numeroAtto")+(String) this.getNodeRefProperty(attoProperties,"estensioneAtto");
                    String iniziativa = (String) this.getNodeRefProperty(
                            attoProperties, "tipoIniziativa");
                    String oggetto = (String) this.getNodeRefProperty(
                            attoProperties, "oggetto");
                    Date datePresentazione = (Date) this.getNodeRefProperty(
                            attoProperties, "dataIniziativa");
                    Date dateRevoca = (Date) this.getNodeRefProperty(
                            attoProperties, "dataChiusura"); 
                    ArrayList<String> firmatariList = (ArrayList<String>) this
                            .getNodeRefProperty(attoProperties, "firmatari");
                    String firmatari = this.renderFirmatariConGruppoList(firmatariList);
                    currentTable
                            .getRow(0)
                            .getCell(1)
                            .setText(
                            this.checkStringEmpty(tipoAtto.toUpperCase() + " "
                            + numeroAtto));
                    currentTable.getRow(1).getCell(1)
                            .setText(this.checkStringEmpty(decodeTipoIniziativa(iniziativa)));
                    currentTable.getRow(2).getCell(1)
                            .setText(this.checkStringEmpty(firmatari));
                    currentTable.getRow(3).getCell(1)
                            .setText(this.checkStringEmpty(oggetto));
                    currentTable.getRow(4).getCell(1)
                            .setText(this.checkDateEmpty(datePresentazione));
                    currentTable.getRow(5).getCell(1)
                            .setText(this.checkDateEmpty(dateRevoca));
                    tableIndex++;
                }
            }
        }

        return document;
    }

    /**
     * {@inheritDoc}
     *
     * Controlla se lo stato dell'atto sia uno dei seguenti <br/>
     * {@link ReportBaseCommand#CHIUSO}<br/>
     * {@link ReportBaseCommand#RITIRATO}<br/>
     *
     */
    protected boolean checkStatoAtto(String statoAtto, String tipoChiusura) {
        return statoAtto.equals(CHIUSO) && tipoChiusura.equals(RITIRATO);
    }
}
