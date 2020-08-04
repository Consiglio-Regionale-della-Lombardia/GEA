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
package com.sourcesense.crl.webscript.report.aula;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sourcesense.crl.webscript.report.ReportCommand;
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

import com.sourcesense.crl.webscript.report.ReportBaseCommand;
import com.sourcesense.crl.webscript.report.util.office.DocxManager;

/**
 * Generazione di report per Atti istruttoria.
 *
 * String relazioneScritta = "";// to complete String noteGenerali = "";// to
 * complete
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportAttiIstruttoriaCommand extends ReportBaseCommand {

    /**
     * Generazione di un report di Atti Istruttoria
     * {@inheritDoc}
     */
    @Override
    public byte[] generate(byte[] templateByteArray, String json,
            StoreRef spacesStore) throws IOException {
        ByteArrayOutputStream ostream = null;
        ResultSet attiResults = null;
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(
                    templateByteArray);
            DocxManager docxManager = new DocxManager(is);
            this.initLegislatura(json);
            this.initTipiAttoLuceneAtto(json);
            this.initDataSedutaDa(json);
            this.initDataSedutaA(json);
            String sortField1 = "@{" + CRL_ATTI_MODEL + "}tipoAtto";
            String sortField2 = "@{" + CRL_ATTI_MODEL + "}numeroAtto";

            SearchParameters sp = new SearchParameters();
            sp.addStore(spacesStore);
            sp.setLanguage(SearchService.LANGUAGE_LUCENE);
            String query = "PATH: \"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:" + this.legislatura + "//*\" AND "
                    + convertListToString("TYPE", this.tipiAttoLucene, true);
            if (!dataSedutaDa.equals("*") || !dataSedutaA.equals("*")) {
                query += " AND @crlatti\\:dataSedutaAula:[" + this.dataSedutaDa
                        + " TO " + this.dataSedutaA + " ]";
            }
            sp.setQuery(query);
            sp.addSort(sortField1, true);
            sp.addSort(sortField2, true);
            attiResults = this.searchService.query(sp); 
            XWPFDocument generatedDocument = docxManager.generateFromTemplate(
                    this.retrieveLenght(attiResults), 2, false); 
            ByteArrayInputStream tempInputStream = saveTemp(generatedDocument);

            XWPFDocument finalDocument = this.fillTemplate(tempInputStream,
                    attiResults, docxManager);
            ostream = new ByteArrayOutputStream();
            finalDocument.write(ostream);

        } catch (JSONException e) { 
            e.printStackTrace();
        } finally {
        	if (attiResults!=null){
        		attiResults.close();
        	}
        }
        return ostream.toByteArray();

    }

    /**
     * Valorizza il template docx con i valori recuperati dalla query verso alfresco.
     *
     * @param finalDocStream - docx stream del documento in generazione
     * @param atti - ResultSet della query alfresco degi atti
     * @return {@link XWPFDocument} documento word del report
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public XWPFDocument fillTemplate(ByteArrayInputStream finalDocStream,
            ResultSet atti, DocxManager docxManager) throws IOException {
        XWPFDocument document = new XWPFDocument(finalDocStream);
        int tableIndex = 0;
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < atti.length(); i++) {
            /* Extract values from Alfresco */
            NodeRef currentAtto = atti.getNodeRef(i);
            Map<QName, Serializable> attoProperties = nodeService
                    .getProperties(currentAtto);
            String statoAtto = (String) this.getNodeRefProperty(attoProperties,
                    "statoAtto");
            if (this.checkStatoAtto(statoAtto)) {
                XWPFTable currentTable = tables.get(tableIndex); 
                QName nodeRefType = nodeService.getType(currentAtto);
                String tipoAtto = (String) nodeRefType.getLocalName();
                if (tipoAtto.length() > 4) {
                    tipoAtto = tipoAtto.substring(4);
                }

                String abbinamenti = this.getAbbinamenti(currentAtto);
                String numeroAtto = StringUtils.EMPTY + (Integer) this.getNodeRefProperty(attoProperties, "numeroAtto")+(String) this.getNodeRefProperty(attoProperties,"estensioneAtto");
                String oggetto = (String) this.getNodeRefProperty(
                        attoProperties, "oggetto");
                String iniziativa = (String) this.getNodeRefProperty(
                        attoProperties, "tipoIniziativa");
                String descrizioneIniziativa = (String) this
                        .getNodeRefProperty(attoProperties,
                        "descrizioneIniziativa");
                ArrayList<String> firmatariList = (ArrayList<String>) this
                        .getNodeRefProperty(attoProperties, "firmatari");
                String firmatari = this.renderFirmatariConGruppoList(firmatariList);
                ArrayList<String> commReferenteList = (ArrayList<String>) this
                        .getNodeRefProperty(attoProperties, "commReferente");
                String commReferente = "";
                if (commReferenteList != null) {
                    for (String commissioneReferenteMulti : commReferenteList) {
                        commReferente += commissioneReferenteMulti + " ";
                    }
                }

                ArrayList<String> relatoriList = (ArrayList<String>) this
                        .getNodeRefProperty(attoProperties, "relatori");

                Iterator<String> relatoriIterator = relatoriList.iterator();
                ArrayList<String> relatoriConGruppoList = new ArrayList<String>();
                while(relatoriIterator.hasNext()){
                	String relatore = relatoriIterator.next();
                	String gruppoConsiliare = getGruppoConsiliare(relatore);
                	if(StringUtils.isNotEmpty(gruppoConsiliare)){
                		relatore += " ("+gruppoConsiliare+")";
                	}
                	relatoriConGruppoList.add(relatore);
                }
                
                String relazioneScritta = "";// to complete
                String noteGenerali = "";// to complete
				/* write values in table */
                currentTable.getRow(0).getCell(1)
                        .setText(this.checkStringEmpty(tipoAtto.toUpperCase()));
                currentTable.getRow(0).getCell(2)
                        .setText(this.checkStringEmpty(numeroAtto));
                currentTable.getRow(1).getCell(1)
                        .setText(this.checkStringEmpty(abbinamenti));
                currentTable.getRow(2).getCell(1)
                        .setText(this.checkStringEmpty(oggetto));
                currentTable.getRow(3).getCell(1)
                        .setText(this.checkStringEmpty(decodeTipoIniziativa(iniziativa)));
                currentTable.getRow(4).getCell(1)
                        .setText(this.checkStringEmpty(firmatari));
                currentTable.getRow(5).getCell(1)
                        .setText(this.checkStringEmpty(descrizioneIniziativa));
                currentTable.getRow(6).getCell(1)
                        .setText(this.checkStringEmpty(commReferente));
                docxManager.insertListInCell(currentTable.getRow(7).getCell(1),
                		relatoriConGruppoList);
                currentTable.getRow(7).getCell(3)
                        .setText(this.checkStringEmpty(relazioneScritta));
                currentTable.getRow(8).getCell(1)
                        .setText(this.checkStringEmpty(noteGenerali));
                tableIndex++;
            }
        }

        return document;
    }

    /*
     * private String getRelazioneScritta(NodeRef currentAtto) { /* c'è un
     * qualche problema qui, viene fuori un nodeRef Null NodeRef aulaFolder =
     * nodeService.getChildByName(currentAtto, ContentModel.ASSOC_CONTAINS,
     * "Aula"); return (String) nodeService.getProperty(aulaFolder,
     * QName.createQName(CRL_ATTI_MODEL, "relazioneScrittaAula")); }
     */
    protected int retrieveLenght(ResultSet attiResults) {
        int count = 0;
        for (NodeRef currentAtto : attiResults.getNodeRefs()) {
            Map<QName, Serializable> attoProperties = nodeService
                    .getProperties(currentAtto);
            String statoAtto = (String) this.getNodeRefProperty(attoProperties,
                    "statoAtto");
            if (this.checkStatoAtto(statoAtto)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Check if the statoAtto is comprehended between "preso in carico e votato"
     * {@inheritDoc}
     */
    protected boolean checkStatoAtto(String statoAtto) {
        return statoAtto.equals(PRESO_CARICO_AULA);
    }
}
