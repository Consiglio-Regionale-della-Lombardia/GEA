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

import com.sourcesense.crl.webscript.report.ReportBaseCommand;
import com.sourcesense.crl.webscript.report.util.office.DocxManager;

/**
 * Generazione di report di tipologia DCR.
 *
 * GET OK -? noteVotazione ="";//?
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportDCRCommand extends ReportBaseCommand {

    /**
     * Generazione di un report di tipologia DCR
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
            this.initTipiAttoLuceneAtto(json);
            this.initLegislatura(json);
            this.initDataSedutaDa(json);
            this.initDataSedutaA(json);
            String sortField1 = "@{" + CRL_ATTI_MODEL + "}numeroDcr";

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
            attiResults = this.searchService.query(sp); 
            XWPFDocument generatedDocument = docxManager.generateFromTemplate(
                    attiResults.length(), 3, false); 
            ByteArrayInputStream tempInputStream = saveTemp(generatedDocument);

            XWPFDocument finalDocument = this.fillTemplate(tempInputStream,
                    attiResults);
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
    public XWPFDocument fillTemplate(ByteArrayInputStream finalDocStream,
            ResultSet atti) throws IOException {
        XWPFDocument document = new XWPFDocument(finalDocStream);
        int tableIndex = 0;
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < atti.length(); i++) {
            NodeRef currentAtto = atti.getNodeRef(i);
            XWPFTable currentTable = tables.get(tableIndex);
            Map<QName, Serializable> attoProperties = nodeService
                    .getProperties(currentAtto); 
            QName nodeRefType = nodeService.getType(currentAtto);
            String tipoAtto = (String) nodeRefType.getLocalName();
            if (tipoAtto.length() > 4) {
                tipoAtto = tipoAtto.substring(4);
            }
            String numeroLcr = (String) this.getNodeRefProperty(attoProperties,
                    "numeroLcr");
            String numeroRepertorio = (String) this.getNodeRefProperty(
                    attoProperties, "numeroRepertorio");
            String noteVotazione = "";// ?
            String numeroDcr = (String) this.getNodeRefProperty(attoProperties,
                    "numeroDcr");
            Date dateSeduta = (Date) this.getNodeRefProperty(attoProperties,
                    "dataSedutaAula");
            String numeroAtto = StringUtils.EMPTY + (Integer) this.getNodeRefProperty(attoProperties, "numeroAtto")+(String) this.getNodeRefProperty(attoProperties,"estensioneAtto");
            String oggetto = (String) this.getNodeRefProperty(attoProperties,
                    "oggetto");
            String emendato = ""
                    + (Boolean) this.getNodeRefProperty(attoProperties,
                    "emendatoAulaAtto");
            emendato = this.processBoolean(emendato);

            String numeroBurl = (String) this.getNodeRefProperty(
                    attoProperties, "numeroPubblicazioneBURL");
            Date dateBurl = (Date) this.getNodeRefProperty(attoProperties,
                    "dataPubblicazioneBURL");
            String noteGenerali = (String) this.getNodeRefProperty(
                    attoProperties, "noteChiusura");

            currentTable.getRow(0).getCell(1)
                    .setText(this.checkStringEmpty(numeroDcr));
            currentTable.getRow(0).getCell(3)
                    .setText(this.checkDateEmpty(dateSeduta));
            currentTable.getRow(1).getCell(1)
                    .setText(this.checkStringEmpty(oggetto));
            currentTable
                    .getRow(2)
                    .getCell(1)
                    .setText(this.checkStringEmpty(tipoAtto.toUpperCase() + " " + numeroAtto));
            currentTable.getRow(2).getCell(3)
                    .setText(this.checkStringEmpty(emendato));
            currentTable.getRow(3).getCell(1)
                    .setText(this.checkStringEmpty(numeroLcr));
            currentTable.getRow(4).getCell(1)
                    .setText(this.checkStringEmpty(numeroRepertorio));
            currentTable.getRow(5).getCell(1)
                    .setText(this.checkStringEmpty(numeroBurl));
            currentTable.getRow(5).getCell(3)
                    .setText(this.checkDateEmpty(dateBurl));
            currentTable.getRow(6).getCell(1)
                    .setText(this.checkStringEmpty(noteVotazione));
            currentTable.getRow(7).getCell(1)
                    .setText(this.checkStringEmpty(noteGenerali));

            tableIndex++;
        }

        return document;
    }
}
