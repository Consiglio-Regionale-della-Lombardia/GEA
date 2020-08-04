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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.json.JSONException;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sourcesense.crl.webscript.report.ReportBaseCommand;
import com.sourcesense.crl.webscript.report.util.office.DocxManager;

/**
 * Genarazione dei report di tipologia Atti Licenziati.
 *
 * V2 - Big Ok ( scavalla pagine a 2, come comportarsi con fogli vuoti?
 *
 * @author Alessandro Benedetti
 *
 */
public class ReportAttiLicenziatiCommand extends ReportBaseCommand {

	private static Log logger = LogFactory.getLog(ReportAttiLicenziatiCommand.class);

    /**
     * Generazione di un report di Atti Licenziati
     * {@inheritDoc}
     */
    public byte[] generate(byte[] templateByteArray, String json,
            StoreRef spacesStore) throws IOException {
        ByteArrayOutputStream ostream = null;
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(
                    templateByteArray);
            DocxManager docxManager = new DocxManager(is);
            /* Init and Sort field */
            this.initLegislatura(json);
            this.initCommonParams(json);
            this.initDataVotazioneCommReferenteDa(json);
            this.initDataVotazioneCommReferenteA(json);

            String sortField1 = "@{" + CRL_ATTI_MODEL + "}numeroAtto";
            Map<String, ResultSet> commissione2results = Maps.newLinkedHashMap();
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
                        + commissione
                        + "\"";

                if (!dataVotazioneCommReferenteDa.equals("*")
                        || !dataVotazioneCommReferenteA.equals("*")) {
                    query += " AND @crlatti\\:dataVotazioneCommissione:["
                            + this.dataVotazioneCommReferenteDa
                            + " TO "
                            + this.dataVotazioneCommReferenteA + "]";
                }
                logger.info(query);
                sp.setQuery(query);
                sp.addSort(sortField1, true);
                ResultSet currentResults = this.searchService.query(sp);
                commissione2results.put(commissione, currentResults);
            }
            Map<NodeRef, NodeRef> atto2commissione = new LinkedHashMap<NodeRef, NodeRef>();
            LinkedListMultimap<String, NodeRef> commissione2atti = this.retrieveAtti(commissione2results, spacesStore, atto2commissione); 
            XWPFDocument generatedDocument = docxManager.generateFromTemplateMap(this.retrieveLenghtMapConditional(commissione2atti, false), 2, false); 
            ByteArrayInputStream tempInputStream = saveTemp(generatedDocument);

            XWPFDocument finalDocument = this.fillTemplate(tempInputStream, commissione2atti, atto2commissione, docxManager);
            ostream = new ByteArrayOutputStream();
            finalDocument.write(ostream);

        } catch (JSONException e) { 
            e.printStackTrace();
        }
        return ostream.toByteArray();

    }


    /**
     * Valorizza il template docx con i valori recuperati dalla query verso alfresco.
     * qui vanno inseriti nella table, presa dal template solo 8: tipo atto-
     * numero atto- competenza - iniziativa -firmatari- oggetto - data
     * assegnazione - esito votazione - data valutazione numero BURL - data BURL
     * - numero LR - data LR -elenco relatori -data nomina relatori
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
            Map<NodeRef, NodeRef> atto2commissione, DocxManager docxManager)
            throws IOException {
        XWPFDocument document = new XWPFDocument(finalDocStream);
        int tableIndex = 0;
        List<XWPFTable> tables = document.getTables();
        for (String commissione : commissione2atti.keySet()) {
            for (NodeRef currentAtto : commissione2atti.get(commissione)) {

                NodeRef currentCommissione = atto2commissione.get(currentAtto);

                Map<QName, Serializable> attoProperties = nodeService.getProperties(currentAtto);
                Map<QName, Serializable> commissioneProperties = nodeService.getProperties(currentCommissione);

                String statoAtto = (String) this.getNodeRefProperty(attoProperties, "statoAtto");

                if (this.checkStatoAtto(statoAtto)) {

                    XWPFTable currentTable = tables.get(tableIndex);
                    ResultSet relatori = this.getRelatori(currentCommissione); 
                    String numeroAtto = StringUtils.EMPTY + (Integer) this.getNodeRefProperty(attoProperties, "numeroAtto")+(String) this.getNodeRefProperty(attoProperties,"estensioneAtto");
                    String iniziativa = (String) this.getNodeRefProperty(attoProperties, "tipoIniziativa");
                    String oggetto = (String) this.getNodeRefProperty(attoProperties, "oggetto");
                    String numeroBurl = (String) this.getNodeRefProperty(attoProperties, "numeroPubblicazioneBURL");
                    Date dateBurl = (Date) this.getNodeRefProperty(attoProperties, "dataPubblicazioneBURL");

                    String numeroLr = (String) this.getNodeRefProperty(attoProperties, "numeroLr");
                    Date dateLr = (Date) this.getNodeRefProperty(attoProperties, "dataLr");

                    ArrayList<String> firmatariList = (ArrayList<String>) this.getNodeRefProperty(attoProperties, "firmatari");
                    String firmatari = this.renderFirmatariConGruppoList(firmatariList); 
                    String tipoAtto = (String) this.getNodeRefProperty(commissioneProperties, "tipoAttoCommissione");
                    String esitoValutazione = (String) this.getNodeRefProperty(commissioneProperties, "esitoVotazioneCommissione");
                    Date dateAssegnazioneCommissione = (Date) this.getNodeRefProperty(commissioneProperties, "dataAssegnazioneCommissione");
                    Date dateVotazioneCommissione = (Date) this.getNodeRefProperty(commissioneProperties, "dataVotazioneCommissione");
                    String ruoloCommissione = (String) this.getNodeRefProperty(commissioneProperties, "ruoloCommissione"); 

                    List<String> elencoRelatori = Lists.newArrayList();
                    List<String> elencoDateNomina = Lists.newArrayList();
                    for (int i = 0; i < relatori.length(); i++) {
                        NodeRef relatoreNodeRef = relatori.getNodeRef(i);
                        Map<QName, Serializable> relatoreProperties = nodeService.getProperties(relatoreNodeRef);
                        Date dateNomina = (Date) this.getNodeRefProperty(relatoreProperties, "dataNominaRelatore");
                        String relatore = (String) nodeService.getProperty(relatoreNodeRef, ContentModel.PROP_NAME);
                        String dateNominaString = this.checkDateEmpty(dateNomina);
                        String gruppoConsiliare = getGruppoConsiliare(relatore);
                        if(StringUtils.isNotEmpty(gruppoConsiliare)){
                        	relatore += " ("+gruppoConsiliare+")";
                        }
                        elencoRelatori.add(relatore);
                        elencoDateNomina.add(dateNominaString);
                    }

                    currentTable.getRow(0).getCell(1).setText(this.checkStringEmpty(tipoAtto.toUpperCase() + " " + numeroAtto));
                    currentTable.getRow(1).getCell(1).setText(this.checkStringEmpty(oggetto));
                    currentTable.getRow(2).getCell(1).setText(this.checkStringEmpty(ruoloCommissione));
                    currentTable.getRow(3).getCell(1).setText(this.checkStringEmpty(decodeTipoIniziativa(iniziativa)));
                    currentTable.getRow(4).getCell(1).setText(this.checkStringEmpty(firmatari));
                    currentTable.getRow(5).getCell(1).setText(this.checkDateEmpty(dateAssegnazioneCommissione));
                    currentTable.getRow(6).getCell(1).setText(this.checkStringEmpty(esitoValutazione));
                    currentTable.getRow(7).getCell(1).setText(this.checkDateEmpty(dateVotazioneCommissione));
                    currentTable.getRow(8).getCell(1).setText(this.checkStringEmpty(numeroBurl));
                    currentTable.getRow(9).getCell(1).setText(this.checkDateEmpty(dateBurl));
                    currentTable.getRow(10).getCell(1).setText(this.checkStringEmpty(numeroLr));
                    currentTable.getRow(10).getCell(3).setText(this.checkDateEmpty(dateLr));
                    docxManager.insertListInCell(currentTable.getRow(11).getCell(1), elencoRelatori);
                    docxManager.insertListInCell(currentTable.getRow(11).getCell(3), elencoDateNomina);

                    tableIndex++;
                }
            }
        }

        return document;
    }

    /**
     * {@inheritDoc}
     *
     * Controlla se lo stato sia uno dei seguenti <br/>
     * {@link ReportBaseCommand#TRASMESSO_AULA}<br/>
     * {@link ReportBaseCommand#PRESO_CARICO_AULA}<br/>
     * {@link ReportBaseCommand#VOTATO_AULA}<br/>
     * {@link ReportBaseCommand#PUBBLICATO}<br/>
     * {@link ReportBaseCommand#CHIUSO}<br/>
     *
     */
    protected boolean checkStatoAtto(String statoAtto) {
        return statoAtto.equals(TRASMESSO_AULA)
                || statoAtto.equals(PRESO_CARICO_AULA)
                || statoAtto.equals(VOTATO_AULA)
                || statoAtto.equals(PUBBLICATO)
                || statoAtto.equals(CHIUSO);
    }
}
