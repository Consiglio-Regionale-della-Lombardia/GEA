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
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import com.sourcesense.crl.webscript.report.ReportBaseCommand;
import com.sourcesense.crl.webscript.report.util.office.DocxManager;

/**
 * Generazione dei report Composizione commissioni
 * V2 - Ok, no result with anagrafica?
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportComposizioneCommissioniCommand extends ReportBaseCommand {

    /**
     * Generazione di un report di Composizione commissioni
     * {@inheritDoc}
     */
    @Override
    public byte[] generate(byte[] templateByteArray, String json,
            StoreRef spacesStore) throws IOException {
        ByteArrayOutputStream ostream = null;
        ByteArrayInputStream is = new ByteArrayInputStream(templateByteArray);
        DocxManager docxManager = new DocxManager(is);
        
        String sortField1 = "@{" + CRL_ATTI_MODEL + "}numeroOrdinamentoCommissioneAnagrafica";        
        
        SearchParameters sp = new SearchParameters();
        sp.addStore(spacesStore);
        sp.setLanguage(SearchService.LANGUAGE_LUCENE);
        /* query from anagrafica */
        String query = "PATH: \"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Commissioni/*\"";
        sp.setQuery(query);
        sp.addSort(sortField1, true);
        ResultSet commissioniResult = this.searchService.query(sp); 
        XWPFDocument generatedDocument = docxManager.generateFromTemplate(
                commissioniResult.length(), 2, false); 
        ByteArrayInputStream tempInputStream = saveTemp(generatedDocument);
        
        XWPFDocument finalDocument = this.fillTemplate(tempInputStream, commissioniResult, docxManager);
        ostream = new ByteArrayOutputStream();
        finalDocument.write(ostream);
        
        return ostream.toByteArray();
        
    }

    /**
     * Valorizza il template docx con i valori recuperati dalla query verso alfresco.
     *
     * @param finalDocStream - docx stream del documento in generazione
     * @param commissioniResult - result set commissioni query alfresco
     * @return {@link XWPFDocument} documento word del report
     * @throws IOException
     */
    public XWPFDocument fillTemplate(ByteArrayInputStream finalDocStream,
            ResultSet commissioniResult, DocxManager docxManager)
            throws IOException {
        XWPFDocument document = new XWPFDocument(finalDocStream);
        int tableIndex = 0;
        List<XWPFTable> tables = document.getTables();
        for (NodeRef currentCommissione : commissioniResult.getNodeRefs()) {
            XWPFTable currentTable = tables.get(tableIndex); 
            String nomeCommissione = (String) nodeService.getProperty(currentCommissione, ContentModel.PROP_NAME);
            List<ChildAssociationRef> consiglieriAssList = nodeService.getChildAssocs(currentCommissione);
            List<String> consiglieri = new ArrayList<String>();
            List<String> partiti = new ArrayList<String>();
            String currentConsigliere = "";
            for (ChildAssociationRef consigliereAss : consiglieriAssList) {
                NodeRef consigliereRef = consigliereAss.getChildRef();
                Map<QName, Serializable> consigliereProperties = nodeService.getProperties(consigliereRef);
                String nomeConsigliere = (String) this.getNodeRefProperty(consigliereProperties, "nomeConsigliereAnagrafica");
                String cognomeConsigliere = (String) this.getNodeRefProperty(consigliereProperties, "cognomeConsigliereAnagrafica");
                String codice = (String) this.getNodeRefProperty(consigliereProperties, "codiceGruppoConsigliereAnagrafica");
                currentConsigliere = cognomeConsigliere + " " + nomeConsigliere;
                consiglieri.add(currentConsigliere);
                partiti.add(codice);
                
            }
            
            currentTable.getRow(0).getCell(0).setText(this.checkStringEmpty(nomeCommissione));
            docxManager.insertListInCell(currentTable.getRow(1).getCell(1), consiglieri);
            docxManager.insertListInCell(currentTable.getRow(1).getCell(2), partiti);
            
            tableIndex++;
        }
        
        return document;
    }
}
