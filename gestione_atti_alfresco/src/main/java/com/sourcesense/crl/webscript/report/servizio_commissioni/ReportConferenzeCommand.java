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
import java.util.LinkedHashMap;
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
import com.sourcesense.crl.util.AttoUtil;
import com.sourcesense.crl.webscript.report.ReportBaseCommand;
import com.sourcesense.crl.webscript.report.util.office.DocxManager;

/**
 * Genarazione  report Conferenze
 * V2 - Big Ok
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportConferenzeCommand extends ReportBaseCommand {

    /**
     * Generazione di un report di Conferenze
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
            docxManager.setNodeService(nodeService);
            docxManager.setSearchService(searchService);
            
            /* init json params */
            this.initLegislatura(json);
            this.initCommonParams(json);
            this.initDataAssegnazioneCommReferenteDa(json);
            this.initDataAssegnazioneCommReferenteA(json);
            /* sorting fields */
            String sortField1 = "@{" + CRL_ATTI_MODEL + "}tipoAttoCommissione";
            String sortField2 = "@{" + CRL_ATTI_MODEL + "}numeroAttoCommissione";
            /* query grouped by commissione */
            Map<String, ResultSet> commissione2results = Maps.newLinkedHashMap();
            for (String commissione : this.commissioniJson) {
                SearchParameters sp = new SearchParameters();
                sp.addStore(spacesStore);
                sp.setLanguage(SearchService.LANGUAGE_LUCENE);
                String query = "PATH: \"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:" + this.legislatura + "//*\""
                        + " AND TYPE:\""
                        + "crlatti:commissione"
                        + "\" AND NOT @crlatti\\:statoCommissione:\"Annullato\" AND "
                        + convertListToString("@crlatti\\:tipoAttoCommissione",
                        this.tipiAttoLucene, true)
                        + " AND @crlatti\\:ruoloCommissione:\""
                        + this.ruoloCommissione
                        + "\" AND @cm\\:name:\""
                        + commissione   
                        + "\"";
                if (!dataAssegnazioneCommReferenteDa.equals("*")
                        || !dataAssegnazioneCommReferenteA.equals("*")) {
                    query += " AND @crlatti\\:dataAssegnazioneCommissione:["
                            + this.dataAssegnazioneCommReferenteDa + " TO "
                            + this.dataAssegnazioneCommReferenteA + " ]";
                }
                sp.setQuery(query); 
                
                sp.addSort(sortField1, true);
                sp.addSort(sortField2, true);
                ResultSet currentResults = this.searchService.query(sp);
                commissione2results.put(commissione, currentResults);
            }
            Map<NodeRef, NodeRef> atto2commissione = new LinkedHashMap<NodeRef, NodeRef>();
            LinkedListMultimap<String, NodeRef> commissione2atti = this.retrieveAtti(commissione2results, spacesStore, atto2commissione); 
            Map<String, Integer> group2count = this.retrieveLenghtMapConditional(commissione2atti, false);
            
            XWPFDocument generatedDocument = docxManager.generateFromTemplateMapConferenza2(group2count, commissione2atti, 3, atto2commissione); 
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
     * @param commissione2atti - String commissione ->  NodeRef type Atto
     * @param atto2commissione - NodeRef type Atto -> NodeRef type Commissione
     * @return {@link XWPFDocument} documento word del report
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public XWPFDocument fillTemplate(ByteArrayInputStream finalDocStream,
            LinkedListMultimap<String, NodeRef> commissione2atti,
            Map<NodeRef, NodeRef> atto2commissione) throws IOException {
        XWPFDocument document = new XWPFDocument(finalDocStream);
        int tableIndex = 0;
        List<XWPFTable> tables = document.getTables();
        for (String commissione : commissione2atti.keySet()) {
            for (NodeRef currentAtto : commissione2atti.get(commissione)) {

                Map<QName, Serializable> attoProperties = nodeService.getProperties(currentAtto);
                Map<QName, Serializable> commissioneProperties = nodeService.getProperties(atto2commissione.get(currentAtto));
                /* extracting values from Alfresco */ 
                String statoAtto = (String) this.getNodeRefProperty(attoProperties, "statoAtto");
                if (this.checkStatoAtto(statoAtto)) {
                    XWPFTable currentTable = tables.get(tableIndex);
                    String numeroAtto = StringUtils.EMPTY + (Integer) this.getNodeRefProperty(attoProperties, "numeroAtto")+(String) this.getNodeRefProperty(attoProperties,"estensioneAtto");
                    String iniziativa = (String) this.getNodeRefProperty(attoProperties, "tipoIniziativa");
                    String oggetto = (String) this.getNodeRefProperty(attoProperties, "oggetto"); 
                    String tipoAtto = (String) this.getNodeRefProperty(commissioneProperties, "tipoAttoCommissione");
                    Date dateAssegnazioneCommissione = (Date) this.getNodeRefProperty(commissioneProperties, "dataAssegnazioneCommissione"); 
                    ArrayList<String> firmatariList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "firmatari");
                    String firmatari = this.renderFirmatariConGruppoList(firmatariList);
                    
                    
                    /* writing values in the table */
                    currentTable.getRow(0).getCell(1).setText(this.checkStringEmpty(tipoAtto.toUpperCase() + " " + numeroAtto));
                    currentTable.getRow(1).getCell(1).setText(this.checkStringEmpty(oggetto));
                    currentTable.getRow(2).getCell(1).setText(this.checkStringEmpty(decodeTipoIniziativa(iniziativa)));
                    currentTable.getRow(3).getCell(1).setText(this.checkStringEmpty(firmatari));
                    currentTable.getRow(4).getCell(1).setText(this.checkDateEmpty(dateAssegnazioneCommissione));
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
     * {@link ReportBaseCommand#ASSEGNATO_COMMISSIONE}<br/>
     * {@link ReportBaseCommand#PRESO_CARICO_COMMISSIONE}<br/>
     * {@link ReportBaseCommand#VOTATO_COMMISSIONE}<br/>
     * {@link ReportBaseCommand#NOMINATO_RELATORE}<br/>
     * {@link ReportBaseCommand#LAVORI_COMITATO_RISTRETTO}<br/>
     *
     */
    protected boolean checkStatoAtto(String statoAtto) {
        return statoAtto.equals(ASSEGNATO_COMMISSIONE)||statoAtto.equals(PRESO_CARICO_COMMISSIONE)
                || statoAtto.equals(VOTATO_COMMISSIONE)
                || statoAtto.equals(NOMINATO_RELATORE)
                || statoAtto.equals(LAVORI_COMITATO_RISTRETTO);
    }


    /**
     *
     * {@inheritDoc}
     */
    @Override
    protected Map<String, Integer> retrieveLenghtMapConditional(
            LinkedListMultimap<String, NodeRef> commissione2atti, Boolean doubleCheck) {
        Map<String, Integer> commissione2count = new LinkedHashMap<String, Integer>();
        for (String commissione : commissione2atti.keySet()) {
            int count = 0;
            for (NodeRef currentAtto : commissione2atti.get(commissione)) {
                Map<QName, Serializable> attoProperties = nodeService
                        .getProperties(currentAtto);
                String statoAtto = (String) this.getNodeRefProperty(
                        attoProperties, "statoAtto");
                String tipoChiusura = (String) this.getNodeRefProperty(
                        attoProperties, "tipoChiusura");
                if (!doubleCheck) {
                    if (this.checkStatoAtto(statoAtto)) {
                        count++;
                    }else {
                    	commissione2atti.get(commissione).remove(currentAtto);
                    }
                } else {
                    if (this.checkStatoAtto(statoAtto, tipoChiusura)) {
                        count++;
                    }
                }

            }
            if (count != 0) {
                commissione2count.put(commissione, count);
            }

        }
        return commissione2count;
    }
    
}
