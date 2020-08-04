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

/**
 * Generazione di report Atti di iniziativa consiliare per consigliere
 * V2 -Big Ok
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportAttiIniziativaConsPerConsCommand extends ReportBaseCommand {

    /**
     * Generazione di un report di Atti di iniziativa consiliare per consigliere
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
            this.initFirmatario(json);
            this.initTipoFirma(json);
            this.initDataAssegnazioneCommReferenteDa(json);
            this.initDataAssegnazioneCommReferenteA(json);
            this.initDataPresentazioneDa(json);
            this.initDataPresentazioneA(json);

            String sortField1 = "@{" + CRL_ATTI_MODEL + "}numeroAtto";

            Map<String, ResultSet> firmatario2results = Maps.newHashMap();
            SearchParameters sp = new SearchParameters();
            sp.addStore(spacesStore);
            sp.setLanguage(SearchService.LANGUAGE_LUCENE);
            String query = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:" + this.legislatura + "//*\" AND TYPE:\""
                    + "crlatti:atto\" AND @crlatti\\:tipoIniziativa:\"01_ATTO DI INIZIATIVA CONSILIARE\"";
            if (this.tipologiaFirma.equals("primo")) {
                query = query + " AND @crlatti\\:primoFirmatario:\""
                        + this.firmatario + "\"";

            } else if (this.tipologiaFirma.equals("originari")) {
                query = query + " AND @crlatti\\:firmatariOriginari:\""
                        + this.firmatario + "\"";
            } else if (this.tipologiaFirma.equals("aggiunti")) {
                query = query + " AND @crlatti\\:firmatari:\""
                        + this.firmatario + "\"";
                query = query + " AND NOT @crlatti\\:firmatariOriginari:\""
                        + this.firmatario + "\"";
            }

            if (!dataAssegnazioneCommReferenteDa.equals("*")
                    || !dataAssegnazioneCommReferenteDa.equals("*")) {
                query = query
                        + " AND @crlatti\\:dataAssegnazioneCommissioneReferente:["
                        + this.dataAssegnazioneCommReferenteDa + " TO "
                        + this.dataAssegnazioneCommReferenteA + " ]";
            }
            if (!dataPresentazioneDa.equals("*")
                    || !dataPresentazioneA.equals("*")) {
                query += " AND @crlatti\\:dataIniziativa:["
                        + this.dataPresentazioneDa + " TO "
                        + this.dataPresentazioneA + " ]";
            }

            sp.setQuery(query);
            sp.addSort(sortField1, true);
            ResultSet attiResult = this.searchService.query(sp);
            firmatario2results.put(this.firmatario, attiResult);

            LinkedListMultimap<String, NodeRef> firmatario2atti = this.retrieveAtti(firmatario2results); 
            XWPFDocument generatedDocument = docxManager.generateFromTemplateMap(this.retrieveLenghtMap(firmatario2atti), 2, false); 
            ByteArrayInputStream tempInputStream = saveTemp(generatedDocument);

            XWPFDocument finalDocument = this.fillTemplate(tempInputStream, firmatario2atti);
            ostream = new ByteArrayOutputStream();
            finalDocument.write(ostream);

        } catch (JSONException e) { 
            e.printStackTrace();
        }
        return ostream.toByteArray();

    }

    /**
     * Ritonra una mappa firmatario -> atto  dal resulset della query alfresco
     * @param attoChild2results  String attofiglio -> ResultSet query alfresco
     * @return String firmatario ->  NodeRef type Atto
     */
    protected LinkedListMultimap<String, NodeRef> retrieveAtti(
            Map<String, ResultSet> attoChild2results) {
        LinkedListMultimap<String, NodeRef> child2atti = LinkedListMultimap.create();
        for (String firmatario : attoChild2results.keySet()) {
            ResultSet firmatarioResults = attoChild2results.get(firmatario);
            List<NodeRef> nodeRefList = firmatarioResults.getNodeRefs();
            for (NodeRef atto : nodeRefList) {
                child2atti.put(firmatario, atto);
            }
        }
        return child2atti;
    }

    /**
     * Valorizza il template docx con i valori recuperati dalla query verso alfresco.
     *
     * @param finalDocStream - docx stream del documento in generazione
     * @param firmatario2atti - String firmatario ->  NodeRef type Atto
     * @return {@link XWPFDocument} documento word del report
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public XWPFDocument fillTemplate(ByteArrayInputStream finalDocStream,
            LinkedListMultimap<String, NodeRef> firmatario2atti)
            throws IOException {
        XWPFDocument document = new XWPFDocument(finalDocStream);
        int tableIndex = 0;
        List<XWPFTable> tables = document.getTables();
        for (String firmatario : firmatario2atti.keySet()) {
            for (NodeRef currentAtto : firmatario2atti.get(firmatario)) {
                XWPFTable currentTable = tables.get(tableIndex);
                Map<QName, Serializable> attoProperties = nodeService.getProperties(currentAtto); 
                QName nodeRefType = nodeService.getType(currentAtto);
                String tipoAtto = (String) nodeRefType.getLocalName();
                if (tipoAtto.length() > 4) {
                    tipoAtto = tipoAtto.substring(4);
                }

                String numeroAtto = StringUtils.EMPTY + (Integer) this.getNodeRefProperty(attoProperties, "numeroAtto")+(String) this.getNodeRefProperty(attoProperties,"estensioneAtto");
                String iniziativa = (String) this.getNodeRefProperty(attoProperties, "tipoIniziativa");

                ArrayList<String> firmatariList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "firmatari");
                String firmatari = this.renderFirmatariConGruppoList(firmatariList);

                Date dateAssegnazioneCommissione = (Date) this.getNodeRefProperty(attoProperties, "dataAssegnazioneCommissioneReferente");

                ArrayList<String> commReferenteList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "commReferente");
                String commReferente = this.renderList(commReferenteList);
                
                ArrayList<String> commCoReferenteList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "commCoreferente");
                String commCoReferente = this.renderList(commCoReferenteList);

                ArrayList<String> commConsultivaList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "commConsultiva");
                String commConsultiva = this.renderList(commConsultivaList);

                String abbinamenti = this.getAbbinamenti(currentAtto);

                Date dateVotazioneCommissione = (Date) this.getNodeRefProperty(attoProperties, "dataSedutaComm");

                String numeroLcr = (String) this.getNodeRefProperty(attoProperties, "numeroLcr");
                String numeroLr = (String) this.getNodeRefProperty(attoProperties, "numeroLr");
                Date dateLr = (Date) this.getNodeRefProperty(attoProperties, "dataLr");

                currentTable.getRow(0).getCell(1).setText(this.checkStringEmpty(tipoAtto.toUpperCase() + " " + numeroAtto));
                currentTable.getRow(1).getCell(1).setText(this.checkStringEmpty(decodeTipoIniziativa(iniziativa)));
                currentTable.getRow(2).getCell(1).setText(this.checkStringEmpty(firmatari));
                currentTable.getRow(3).getCell(1).setText(this.checkDateEmpty(dateAssegnazioneCommissione));

                currentTable.getRow(3).getCell(3).setText(this.checkStringEmpty(commReferente));
                
                currentTable.getRow(4).getCell(1).setText(this.checkStringEmpty(commCoReferente));

                currentTable.getRow(5).getCell(1).setText(this.checkStringEmpty(commConsultiva));

                currentTable.getRow(6).getCell(1).setText(this.checkStringEmpty(abbinamenti));

                currentTable.getRow(7).getCell(1).setText(this.checkDateEmpty(dateVotazioneCommissione));
                currentTable.getRow(7).getCell(3).setText(this.checkStringEmpty(numeroLcr));

                currentTable.getRow(8).getCell(1).setText(this.checkStringEmpty(numeroLr));
                currentTable.getRow(8).getCell(3).setText(this.checkDateEmpty(dateLr));

                tableIndex++;
            }
        }

        return document;
    }
}
