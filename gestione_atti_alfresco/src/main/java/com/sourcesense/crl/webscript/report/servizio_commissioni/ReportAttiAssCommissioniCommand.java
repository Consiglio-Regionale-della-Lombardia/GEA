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

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.sourcesense.crl.webscript.report.ReportBaseCommand;
import com.sourcesense.crl.webscript.report.util.office.DocxManager;
import java.util.LinkedHashMap;

/**
 * Generazione report  Atti assegnati alle commissioni
 * V2 Big - OK
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportAttiAssCommissioniCommand extends ReportBaseCommand {

    /**
     * Generazione di un report di Atti assegnati alle commissioni
     * {@inheritDoc}
     */
    @Override
    public byte[] generate(byte[] templateByteArray, String json,
            StoreRef spacesStore) throws IOException {
        ByteArrayOutputStream ostream = null;
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(templateByteArray);
            DocxManager docxManager = new DocxManager(is);
            /* init Json params */
            this.initCommonParams(json);
            this.initLegislatura(json);
            this.initDataAssegnazioneCommReferenteDa(json);
            this.initDataAssegnazioneCommReferenteA(json);
            /* define sorting fields */
            String sortField1 = "@{" + CRL_ATTI_MODEL + "}tipoAttoCommissione";
            String sortField2 = "@{" + CRL_ATTI_MODEL + "}numeroAttoCommissione";
            Map<String, ResultSet> commissione2results = Maps.newLinkedHashMap();
            /* execute guery grouped by Commissione */
            for (String commissione : this.commissioniJson) {
                SearchParameters sp = new SearchParameters();
                sp.addStore(spacesStore);
                sp.setLanguage(SearchService.LANGUAGE_LUCENE);
                String query = "PATH: \"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:" + this.legislatura + "//*\""
                        + " AND TYPE:\""
                        + "crlatti:commissione"
                        + "\" AND "
                        + convertListToString("@crlatti\\:tipoAttoCommissione",
                        this.tipiAttoLucene, true)
                        + " AND @crlatti\\:ruoloCommissione:\""
                        + this.ruoloCommissione
                        + "\" AND @cm\\:name:\""
                        + commissione + "\"";
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
            LinkedListMultimap<String, NodeRef> commissione2atti = this.retrieveAttiReportAssCommissione(commissione2results, spacesStore, atto2commissione); 
            XWPFDocument generatedDocument = docxManager.generateFromTemplateMapAssCommissioni(this.retrieveLenghtMapConditional(commissione2atti, false), 3, false); 
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

                String statoAtto = (String) this.getNodeRefProperty(attoProperties, "statoAtto");

                if (this.checkStatoAtto(statoAtto)) {

                    XWPFTable currentTable = tables.get(tableIndex);
                    /* value extraction from Alfresco */ 
                    String numeroAtto = StringUtils.EMPTY + (Integer) this.getNodeRefProperty(attoProperties, "numeroAtto")+(String) this.getNodeRefProperty(attoProperties,"estensioneAtto");
                    String iniziativa = (String) this.getNodeRefProperty(attoProperties, "tipoIniziativa");
                    String oggetto = (String) this.getNodeRefProperty(attoProperties, "oggetto");
                    ArrayList<String> commReferenteList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "commReferente");
                    String commReferente = this.renderList(commReferenteList);
                    
                    ArrayList<String> commCoReferenteList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "commCoreferente");
                    String commCoReferente = this.renderList(commCoReferenteList);

                    ArrayList<String> firmatariList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "firmatari");
                    String firmatari = this.renderFirmatariConGruppoList(firmatariList);
                    String numeroDgr = (String) this.getNodeRefProperty(attoProperties, "numeroDgr"); 
                    String tipoAtto = (String) this.getNodeRefProperty(commissioneProperties, "tipoAttoCommissione");
                    Date dateAssegnazioneCommissione = (Date) this.getNodeRefProperty(commissioneProperties, "dataAssegnazioneCommissione");
                    Date dateVotazioneCommissione = (Date) this.getNodeRefProperty(commissioneProperties, "dataVotazioneCommissione");
                    String ruoloCommissione = (String) this.getNodeRefProperty(commissioneProperties, "ruoloCommissione");

                    /* values writing in table */
                    currentTable.getRow(0).getCell(1).setText(this.checkStringEmpty(tipoAtto.toUpperCase() + " " + numeroAtto));
                    currentTable.getRow(1).getCell(1).setText(this.checkStringEmpty(ruoloCommissione));
                    currentTable.getRow(2).getCell(1).setText(this.checkStringEmpty(decodeTipoIniziativa(iniziativa)));
                    currentTable.getRow(3).getCell(1).setText(this.checkStringEmpty(oggetto));
                    currentTable.getRow(4).getCell(1).setText(this.checkStringEmpty(firmatari));
                    currentTable.getRow(5).getCell(1).setText(this.checkStringEmpty(numeroDgr));
                    currentTable.getRow(6).getCell(1).setText(this.checkDateEmpty(dateAssegnazioneCommissione));
                    currentTable.getRow(7).getCell(1).setText(this.checkDateEmpty(dateVotazioneCommissione));
                    currentTable.getRow(8).getCell(1).setText(this.checkStringEmpty(commReferente));
                    currentTable.getRow(9).getCell(1).setText(this.checkStringEmpty(commCoReferente));
                    tableIndex++;
                }
            }
        }

        return document;
    }

    /**
     *{@inheritDoc}
     * ritorna sempre true
     */
    protected boolean checkStatoAtto(String statoAtto) {
       /* return statoAtto.equals(ASSEGNATO_COMMISSIONE)
                || statoAtto.equals(PRESO_CARICO_COMMISSIONE)
                || statoAtto.equals(VOTATO_COMMISSIONE)
                || statoAtto.equals(NOMINATO_RELATORE)
                || statoAtto.equals(LAVORI_COMITATO_RISTRETTO);*/
    return true;
    }
}
