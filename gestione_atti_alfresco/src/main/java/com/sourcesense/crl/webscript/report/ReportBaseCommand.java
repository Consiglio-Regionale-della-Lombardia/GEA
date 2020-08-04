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
package com.sourcesense.crl.webscript.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.DynamicNamespacePrefixResolver;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.sourcesense.crl.util.AttoUtil;
import com.sourcesense.crl.webscript.report.util.JsonUtils;

/**
 * Implementazione base  che si occupa di gestire la genereziane dei report
 * in modo generico in base agli parametri in input.
 * @author sourcesense
 */
public abstract class ReportBaseCommand implements ReportCommand {

    protected ContentService contentService;
    protected SearchService searchService;
    protected NodeService nodeService;
    protected DictionaryService dictionaryService;
    protected Map<String, String> json2luceneField;
    protected static final String CRL_ATTI_MODEL = "http://www.regione.lombardia.it/content/model/atti/1.0";
    protected final static String DATA_FORMAT = "dd/MM/yyyy";
    protected static final String RUOLO_COMM_REFERENTE = "Referente";
    protected static final String RUOLO_COMM_COREFERENTE = "Co-Referente";
    protected static final String RUOLO_COMM_CONSULTIVA = "Consultiva";
    protected static final String RUOLO_COMM_REDIGENTE = "Redigente";
    protected static final String RUOLO_COMM_DELIBERANTE = "Deliberante";
    public static final String TRASMESSO_AULA = "Trasmesso ad Aula";
    public static final String PRESO_CARICO_AULA = "Preso in carico da Aula";
    public static final String VOTATO_AULA = "Votato in Aula";
    public static final String PUBBLICATO = "Pubblicato";
    public static final String ASSEGNATO_COMMISSIONE = "Assegnato a Commissione";
    public static final String PRESO_CARICO_COMMISSIONE = "Preso in carico da Commissioni";
    public static final String NOMINATO_RELATORE = "Nominato Relatore";
    public static final String VOTATO_COMMISSIONE = "Votato in Commissione";
    public static final String LAVORI_COMITATO_RISTRETTO = "Lavori Comitato Ristretto";
    public static final String CHIUSO = "Chiuso";
    public static final String RITIRATO = "Ritirato dai promotori";
    private static final String ABBINAMENTI_SPACE_NAME = "Abbinamenti";
    protected List<String> tipiAttoLucene;
    protected String ruoloCommissione;
    protected String organismo;
    protected List<String> commissioniJson;
    protected String legislatura;
    protected String dataAssegnazioneCommReferenteDa;
    protected String dataAssegnazioneCommReferenteA;
    protected String dataVotazioneCommReferenteDa;
    protected String dataVotazioneCommReferenteA;
    protected String dataRitiroDa;
    protected String dataRitiroA;
    protected String dataNominaRelatoreDa;
    protected String dataNominaRelatoreA;
    protected String dataPresentazioneDa;
    protected String dataPresentazioneA;
    protected String dataAssegnazioneParereDa;
    protected String dataAssegnazioneParereA;
    protected String dataSedutaDa;
    protected String dataSedutaA;
    protected List<String> relatoriJson;
    protected String firmatario;
    protected String tipologiaFirma;
    protected String dataConsultazioneDa;
    protected String dataConsultazioneA;
    protected static Map<String, String> tipoIniziativaDecode = new HashMap<String, String>();
    private static final String QUERY_REPLACER = "REP";
    private static final QName PROP_CODICE_GRUPPO = QName.createQName(CRL_ATTI_MODEL, "codiceGruppoConsigliereAnagrafica");
    private static final QName PROP_STATO_ATTO = QName.createQName(CRL_ATTI_MODEL, "statoAtto");
    
    private static final String GRUPPO_FIRMATARIO_QUERY_ATTIVI = 
    		"PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriAttivi//*\" " +
    		"AND TYPE:\"crlatti:consigliereAnagrafica\" AND @cm\\:name:\""+QUERY_REPLACER+"\"";
    
    private static final String GRUPPO_FIRMATARIO_QUERY_STORICI = 
    		"PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriStorici//*\" " +
    		"AND TYPE:\"crlatti:consigliereAnagrafica\" AND @cm\\:name:\""+QUERY_REPLACER+"\"";

    
    static {
        tipoIniziativaDecode.put("01_ATTO DI INIZIATIVA CONSILIARE", "Consiliare");
        tipoIniziativaDecode.put("02_ATTO DI INIZIATIVA DI GIUNTA", "Giunta");
        tipoIniziativaDecode.put("03_ATTO DI INIZIATIVA POPOLARE", "Popolare");
        tipoIniziativaDecode.put("04_ATTO DI INIZIATIVA COMMISSIONI", "Commissioni");
        tipoIniziativaDecode.put("05_ATTO DI INIZIATIVA UFFICIO PRESIDENZA", "Ufficio di Presidenza");
        tipoIniziativaDecode.put("06_ATTO DI INIZIATIVA PRESIDENTE GIUNTA", "Presidente della Giunta");
        tipoIniziativaDecode.put("07_ATTO DI INIZIATIVA AUTONOMIE LOCALI", "Consiglio delle Autonomie locali");
        tipoIniziativaDecode.put("08_ATTO DI ALTRA INIZIATIVA", "Altra Iniziativa");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract byte[] generate(byte[] templateByteArray, String json,
            StoreRef spacesStore) throws IOException;

    /**
     * Extract only valuable commissioni from the ones extracted from the
     * Relatori. For each relatore is analyzed its typer hierarchy, and when a
     * Commissione is found, its name is checked.
     *
     * @param relatoriResults lista dei relatori
     * @return List of Commissioni NodeRef
     */
    protected List<NodeRef> retrieveCommissioniFromRelatori(
            ResultSet relatoriResults) {
        List<NodeRef> commissioni = Lists.newArrayList();
        int resultLength = relatoriResults.length();
        for (int i = 0; i < resultLength; i++) {
            NodeRef relatoreNodeRef = relatoriResults.getNodeRef(i);
            NodeRef commissioneNodeRef = relatoreNodeRef;
            boolean check = false;
            while (!check) {// look for Commissione in type hierarchy
                ChildAssociationRef childAssociationRef = nodeService
                        .getPrimaryParent(commissioneNodeRef);
                commissioneNodeRef = childAssociationRef.getParentRef();
                QName nodeRefType = nodeService.getType(commissioneNodeRef);
                QName attoRefType = QName.createQName(CRL_ATTI_MODEL,
                        "commissione");
                check = dictionaryService.isSubClass(nodeRefType, attoRefType);
            }
            Map<QName, Serializable> commissioneProperties = nodeService
                    .getProperties(commissioneNodeRef);
            String commissione = (String) this.getNodeRefProperty(
                    commissioneProperties, "name");
            if (this.checkCommissione(commissione)) {
                commissioni.add(commissioneNodeRef);
            }

        }
        return commissioni;
    }

    /**
     * check if the Commissione in input is good, related to the selection of
     * Commissione good
     *
     * @param commissione commissione da controllare se è valida
     * @return
     */
    private boolean checkCommissione(String commissione) {
        return this.commissioniJson.contains(commissione);
    }

    /**
     * For each Commissione(String) in input , access to its resultSet and
     * retrieve the list of Atto NodeRef.
     *
     * @param attoChild2results - String commissione -> ResultSet
     * @param spacesStore - current space store of search
     * @param atto2child - NodeRef type Atto -> NodeRef type Commissione
     * @return una mappa con comissioni -> node ref atto
     */
    protected LinkedListMultimap<String, NodeRef> retrieveAtti(
            Map<String, ResultSet> attoChild2results, StoreRef spacesStore,
            Map<NodeRef, NodeRef> atto2child) {
        LinkedListMultimap<String, NodeRef> child2atti = LinkedListMultimap.create();
        for (String commissione : attoChild2results.keySet()) {
            ResultSet commissioneResults = attoChild2results.get(commissione);
            List<NodeRef> nodeRefList = commissioneResults.getNodeRefs();
            commissioneResults.close(); 
            sortAttiCommissione(nodeRefList);
            
            this.retrieveAttiFromList(nodeRefList, spacesStore, atto2child,
                    child2atti, commissione);
        }
        return child2atti;
    }
    
    /**
     * For each Commissione(String) in input , access to its resultSet and
     * retrieve the list of Atto NodeRef.
     *
     * @param attoChild2results - String commissione -> ResultSet
     * @param spacesStore - current space store of search
     * @param atto2child - NodeRef type Atto -> NodeRef type Commissione
     * @return
     */
    protected LinkedListMultimap<String, NodeRef> retrieveAttiLicenziati(
            Map<String, ResultSet> attoChild2results, StoreRef spacesStore,
            Map<NodeRef, NodeRef> atto2child) {
        LinkedListMultimap<String, NodeRef> child2atti = LinkedListMultimap.create();
        for (String commissione : attoChild2results.keySet()) {
            ResultSet commissioneResults = attoChild2results.get(commissione);
            List<NodeRef> nodeRefList = commissioneResults.getNodeRefs(); 
            sortAttiCommissione(nodeRefList);

            this.retrieveAttiLicenziatiFromList(nodeRefList, spacesStore, atto2child,
                    child2atti, commissione);
        }
        return child2atti;
    }
    
    /**
     * For each Commissione(String) in input , access to its resultSet and
     * retrieve the list of Atto NodeRef.
     *
     * @param attoChild2results - String commissione -> ResultSet
     * @param spacesStore - current space store of search
     * @param atto2child - NodeRef type Atto -> NodeRef type Commissione
     * @return Mappa commsione -> NodeRef atto
     */
    protected LinkedListMultimap<String, NodeRef> retrieveAttiReportAssCommissione(
            Map<String, ResultSet> attoChild2results, StoreRef spacesStore,
            Map<NodeRef, NodeRef> atto2child) {
        LinkedListMultimap<String, NodeRef> child2atti = LinkedListMultimap.create();
        for (String commissione : attoChild2results.keySet()) {
            ResultSet commissioneResults = attoChild2results.get(commissione);
            List<NodeRef> nodeRefList = commissioneResults.getNodeRefs();
            commissioneResults.close(); 
            sortAttiCommissione(nodeRefList);

            this.retrieveAttiAssCommissioneFromList(nodeRefList, spacesStore, atto2child,
                    child2atti, commissione);
        }
        return child2atti;
    }
    
    /**
     * For each Commissione(String) in input , access to its resultSet and
     * retrieve the list of Atto NodeRef.
     *
     * @param attoChild2results - String commissione -> ResultSet
     * @param spacesStore - current space store of search
     * @param atto2child - NodeRef type Atto -> NodeRef type Commissione
     * @return mappa commisione -> NodeRef AttoParere
     */
    protected LinkedListMultimap<String, NodeRef> retrieveAttiParere(
            Map<String, ResultSet> attoChild2results, StoreRef spacesStore,
            Map<NodeRef, NodeRef> atto2child) {
        LinkedListMultimap<String, NodeRef> child2atti = LinkedListMultimap.create();
        for (String commissione : attoChild2results.keySet()) {
            ResultSet commissioneResults = attoChild2results.get(commissione);
            List<NodeRef> nodeRefList = commissioneResults.getNodeRefs(); 
            sortAttiParere(nodeRefList);

            this.retrieveAttiFromList(nodeRefList, spacesStore, atto2child,
                    child2atti, commissione);
        }
        return child2atti;
    }

    /**
     * Metodo che ordina gli atti di una commissione
     * @param nodeRefList la lista degli nodi che rapresentano gli atti di una commissione
     */
    protected void sortAttiCommissione(List<NodeRef> nodeRefList) {

        Collections.sort(nodeRefList, new Comparator<NodeRef>() {
            public int compare(NodeRef node1, NodeRef node2) {

                String tipoAtto1 = (String) nodeService.getProperty(node1, QName.createQName(CRL_ATTI_MODEL, "tipoAttoCommissione"));
                String tipoAtto2 = (String) nodeService.getProperty(node2, QName.createQName(CRL_ATTI_MODEL, "tipoAttoCommissione"));

                int tipoResult = tipoAtto1.compareTo(tipoAtto2);

                if (tipoResult != 0) {
                    return tipoResult;
                }

                String numeroAtto1 = (String) nodeService.getProperty(node1, QName.createQName(CRL_ATTI_MODEL, "numeroAttoCommissione"));
                String numeroAtto2 = (String) nodeService.getProperty(node2, QName.createQName(CRL_ATTI_MODEL, "numeroAttoCommissione"));

                Integer numAtto1 = new Integer(numeroAtto1);
                Integer numAtto2 = new Integer(numeroAtto2);

                return numAtto1.compareTo(numAtto2);

            }
        });

    }


    /**
     * Metodo che ordina gli atti di una commissione per numero atto
     * @param nodeRefList la lista degli nodi che rapresentano gli atti di una commissione
     */
    protected void sortAttiPerNumeroAtto(List<NodeRef> nodeRefList) {

        Collections.sort(nodeRefList, new Comparator<NodeRef>() {
            public int compare(NodeRef node1, NodeRef node2) {

                String numeroAtto1 = (String) nodeService.getProperty(node1, QName.createQName(CRL_ATTI_MODEL, "numeroAttoCommissione"));
                String numeroAtto2 = (String) nodeService.getProperty(node2, QName.createQName(CRL_ATTI_MODEL, "numeroAttoCommissione"));

                Integer numAtto1 = new Integer(numeroAtto1);
                Integer numAtto2 = new Integer(numeroAtto2);

                return numAtto1.compareTo(numAtto2);

            }
        });

    }


    /**
     * Metodo che ordina gli atti parere
     * @param nodeRefList la lista degli nodi che rapresentano gli atti parere
     */
    protected void sortAttiParere(List<NodeRef> nodeRefList) {

        Collections.sort(nodeRefList, new Comparator<NodeRef>() {
            public int compare(NodeRef node1, NodeRef node2) {

                String tipoAtto1 = (String) nodeService.getProperty(node1, QName.createQName(CRL_ATTI_MODEL, "tipoAttoParere"));
                String tipoAtto2 = (String) nodeService.getProperty(node2, QName.createQName(CRL_ATTI_MODEL, "tipoAttoParere"));

                int tipoResult = tipoAtto1.compareTo(tipoAtto2);

                if (tipoResult != 0) {
                    return tipoResult;
                }

                String numeroAtto1 = (String) nodeService.getProperty(node1, QName.createQName(CRL_ATTI_MODEL, "numeroAttoParere"));
                String numeroAtto2 = (String) nodeService.getProperty(node2, QName.createQName(CRL_ATTI_MODEL, "numeroAttoParere"));

                Integer numAtto1 = new Integer(numeroAtto1);
                Integer numAtto2 = new Integer(numeroAtto2);

                return numAtto1.compareTo(numAtto2);

            }
        });

    }

    /**
     * Recupera le consultazioni partendo dai figli del  nodo atto
     * @param attoChild2results i nodi figli del atto
     * @param spacesStore  lo spazio alfresco utilizzato per la ricerca delle consultazioni
     * @param atto2child mappa dei nodi del atto
     * @return la lista delle consultazioni
     */
    protected ArrayListMultimap<String, NodeRef> retrieveConsultazioni(Map<String, ResultSet> attoChild2results, StoreRef spacesStore, Map<NodeRef, NodeRef> atto2child) {
        ArrayListMultimap<String, NodeRef> commissione2consultazioni = ArrayListMultimap.create();
        for (String commissione : attoChild2results.keySet()) {
            ResultSet commissioneResults = attoChild2results.get(commissione);
            List<NodeRef> consultazioniNodeRefList = commissioneResults.getNodeRefs();

            this.retrieveConsultazioniFromList(consultazioniNodeRefList, spacesStore, atto2child, commissione2consultazioni, commissione);
        }
        return commissione2consultazioni;
    }

    /**
     * Recupero delle consultazioni dalla lista di nodi facendo una query sul
     * repository nello space indicato
     * @param nodeRefList la lista di nodi da considerare
     * @param spacesStore spazio alfresco dove effeturare la ricerca
     * @param atto2child  una mappa che collega ogni atto ai suoi figli
     * @param child2atti una mappa che collega un figlio agli atti
     * @param child la stringa del nodo figlio
     */
    protected void retrieveConsultazioniFromList(List<NodeRef> nodeRefList,
            StoreRef spacesStore, Map<NodeRef, NodeRef> atto2child,
            ArrayListMultimap<String, NodeRef> child2atti, String child) {
        int resultLength = nodeRefList.size();
        for (int i = 0; i < resultLength; i++) {
            NodeRef childNodeRef = nodeRefList.get(i);
            NodeRef attoNodeRef = childNodeRef;
            boolean check = false;
            while (!check) {// look for Atto in type hierarchy
                ChildAssociationRef childAssociationRef = nodeService
                        .getPrimaryParent(attoNodeRef);
                attoNodeRef = childAssociationRef.getParentRef();
                QName nodeRefType = nodeService.getType(attoNodeRef);
                QName attoRefType = QName.createQName(CRL_ATTI_MODEL, "atto");
                check = dictionaryService.isSubClass(nodeRefType, attoRefType);
            }
            child2atti.put(child, childNodeRef);
            atto2child.put(childNodeRef, attoNodeRef);

        }
    }

    /**
     * Retrieve Atto from a NodeRef list of results from the query. For example
     * a list of Commissione or Parere. Then from each child node, it find the
     * related Atto.
     *
     * @param nodeRefList - the list of Commissione NodeRef
     * @param spacesStore - the current space store workspace://SpacesStore
     * @param atto2child - a map that relates each atto to its specif
     * childNodes( for example Commissione NodeRef)
     * @param child2atti - a map that relates a specific child NodeRef
     * @param child - the String of the child related to different Atto ( for
     * example Commissione I)
     */
    protected void retrieveAttiFromList(List<NodeRef> nodeRefList,
            StoreRef spacesStore, Map<NodeRef, NodeRef> atto2child,
            LinkedListMultimap<String, NodeRef> child2atti, String child) {
        int resultLength = nodeRefList.size();
        for (int i = 0; i < resultLength; i++) {
            NodeRef childNodeRef = nodeRefList.get(i);
            NodeRef attoNodeRef = childNodeRef;
            boolean check = false;
            while (!check) {// look for Atto in type hierarchy
                ChildAssociationRef childAssociationRef = nodeService
                        .getPrimaryParent(attoNodeRef);
                attoNodeRef = childAssociationRef.getParentRef();
                QName nodeRefType = nodeService.getType(attoNodeRef);
                QName attoRefType = QName.createQName(CRL_ATTI_MODEL, "atto");
                check = dictionaryService.isSubClass(nodeRefType, attoRefType);
            }
            child2atti.put(child, attoNodeRef);
            atto2child.put(attoNodeRef, childNodeRef);

        }
    }
    
    /**
     * Retrieve Atto from a NodeRef list of results from the query. For example
     * a list of Commissione or Parere. Then from each child node, it find the
     * related Atto.
     *
     * @param nodeRefList - the list of Commissione NodeRef
     * @param spacesStore - the current space store workspace://SpacesStore
     * @param atto2child - a map that relates each atto to its specif
     * childNodes( for example Commissione NodeRef)
     * @param child2atti - a map that relates a specific child NodeRef
     * @param child - the String of the child related to different Atto ( for
     * example Commissione I)
     */
    protected void retrieveAttiLicenziatiFromList(List<NodeRef> nodeRefList,
            StoreRef spacesStore, Map<NodeRef, NodeRef> atto2child,
            LinkedListMultimap<String, NodeRef> child2atti, String child) {
        int resultLength = nodeRefList.size();
        for (int i = 0; i < resultLength; i++) {
            NodeRef childNodeRef = nodeRefList.get(i);
            NodeRef attoNodeRef = childNodeRef;
            boolean check = false;
            while (!check) {// look for Atto in type hierarchy
                ChildAssociationRef childAssociationRef = nodeService
                        .getPrimaryParent(attoNodeRef);
                attoNodeRef = childAssociationRef.getParentRef();
                QName nodeRefType = nodeService.getType(attoNodeRef);
                QName attoRefType = QName.createQName(CRL_ATTI_MODEL, "atto");
                check = dictionaryService.isSubClass(nodeRefType, attoRefType);
            } 
            Date dataVotazione = (Date) nodeService.getProperty(attoNodeRef, AttoUtil.PROP_DATA_VOTAZIONE);
            if(dataVotazione!=null){
            	child2atti.put(child, attoNodeRef);
            	atto2child.put(attoNodeRef, childNodeRef);
            }
        }
    }
    
    /**
     * Retrieve Atto from a NodeRef list of results from the query. For example
     * a list of Commissione or Parere. Then from each child node, it find the
     * related Atto.
     *
     * @param nodeRefList - the list of Commissione NodeRef
     * @param spacesStore - the current space store workspace://SpacesStore
     * @param atto2child - a map that relates each atto to its specif
     * childNodes( for example Commissione NodeRef)
     * @param child2atti - a map that relates a specific child NodeRef
     * @param child - the String of the child related to different Atto ( for
     * example Commissione I)
     */
    protected void retrieveAttiAssCommissioneFromList(List<NodeRef> nodeRefList,
            StoreRef spacesStore, Map<NodeRef, NodeRef> atto2child,
            LinkedListMultimap<String, NodeRef> child2atti, String child) {
        int resultLength = nodeRefList.size();
        for (int i = 0; i < resultLength; i++) {
            NodeRef childNodeRef = nodeRefList.get(i);
            NodeRef attoNodeRef = childNodeRef;
            boolean check = false;
            while (!check) {// look for Atto in type hierarchy
                ChildAssociationRef childAssociationRef = nodeService
                        .getPrimaryParent(attoNodeRef);
                attoNodeRef = childAssociationRef.getParentRef();
                QName nodeRefType = nodeService.getType(attoNodeRef);
                QName attoRefType = QName.createQName(CRL_ATTI_MODEL, "atto");
                check = dictionaryService.isSubClass(nodeRefType, attoRefType);
            }
            
            String stato = (String) nodeService.getProperty(attoNodeRef, PROP_STATO_ATTO);
            if(StringUtils.isNotEmpty(stato)){
            	if(!stato.equals("Protocollato")
            			&& !stato.equals("Preso in carico da S.C.")
            			&& !stato.startsWith("Verificata ammissibilit")
            			&& !stato.equals("Proposta assegnazione")){
            		
	            	child2atti.put(child, attoNodeRef);
	                atto2child.put(attoNodeRef, childNodeRef);
	                
            	}
            }
            
        }
    }

    /**
     * Retrieve Atto using an ordered sequence of node child names ( for example
     * Relatore) and then extract from each relatore the related Atto
     *
     * @param relatore2results  un mappa che collega i relatori con i risultati
     * @param spacesStore spazio alfresco per il recupero delle info
     * @param atto2commissione mappa che collega un atto alla commissione
     * @return una mappa ordinate degli atti
     */
    protected TreeMap<String, List<NodeRef>> retrieveAttiOrdered(
            Map<String, ResultSet> relatore2results, StoreRef spacesStore,
            Map<NodeRef, NodeRef> atto2commissione) {
        TreeMap<String, List<NodeRef>> relatore2atti = Maps.newTreeMap();
        Set<String> relatori = relatore2results.keySet();
        for (String relatore : relatori) {
            ResultSet commissioneResults = relatore2results.get(relatore);
            List<NodeRef> nodeRefList = commissioneResults.getNodeRefs();
            this.retrieveAttiFromListOrdered(nodeRefList, spacesStore,
                    atto2commissione, relatore2atti, relatore);
        }
        return relatore2atti;
    }

    /**
     * Retrieve Atto from a NodeRef list of results from the query. For example
     * a list of Relatore or Parere. Then from each child node, it find the
     * related Atto. TreeMap is used to gain ordering power.
     *
     * @param nodeRefList - the list of Commissione NodeRef
     * @param spacesStore - the current space store workspace://SpacesStore
     * @param atto2child - a map that relates each atto to its specif
     * childNodes( for example Commissione NodeRef)
     * @param child2atti - a map that relates a specific child NodeRef
     * @param child - the String of the child related to different Atto ( for
     * example Commissione I)
     */
    protected void retrieveAttiFromListOrdered(List<NodeRef> nodeRefList,
            StoreRef spacesStore, Map<NodeRef, NodeRef> atto2child,
            TreeMap<String, List<NodeRef>> child2atti, String child) {
        int resultLength = nodeRefList.size();
        for (int i = 0; i < resultLength; i++) {
            NodeRef childNodeRef = nodeRefList.get(i);
            NodeRef attoNodeRef = childNodeRef;
            boolean check = false;
            while (!check) {// look for Atto in type hierarchy
                ChildAssociationRef childAssociationRef = nodeService
                        .getPrimaryParent(attoNodeRef);
                attoNodeRef = childAssociationRef.getParentRef();
                QName nodeRefType = nodeService.getType(attoNodeRef);
                QName attoRefType = QName.createQName(CRL_ATTI_MODEL, "atto");
                check = dictionaryService.isSubClass(nodeRefType, attoRefType);
            }
            if (child2atti.get(child) != null) {
                child2atti.get(child).add(attoNodeRef);
            } else {
                List<NodeRef> earlyCreatedList = new LinkedList<NodeRef>();
                earlyCreatedList.add(attoNodeRef);
                child2atti.put(child, earlyCreatedList);
            }

            atto2child.put(attoNodeRef, childNodeRef);

        }
    }

    /**
     * Return the list of Relatore for a specific Commissione
     *
     * @param commissioneNodeRef nodo di riferimento della commissione
     * @return la lista deirelatori per la commissione
     */
    public ResultSet getRelatori(NodeRef commissioneNodeRef) {
        DynamicNamespacePrefixResolver namespacePrefixResolver = new DynamicNamespacePrefixResolver(
                null);
        namespacePrefixResolver.registerNamespace(
                NamespaceService.SYSTEM_MODEL_PREFIX,
                NamespaceService.SYSTEM_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(
                NamespaceService.CONTENT_MODEL_PREFIX,
                NamespaceService.CONTENT_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(
                NamespaceService.APP_MODEL_PREFIX,
                NamespaceService.APP_MODEL_1_0_URI);

        String luceneCommissioneNodePath = nodeService.getPath(
                commissioneNodeRef).toPrefixString(namespacePrefixResolver);

        String relatoreType = "{" + CRL_ATTI_MODEL + "}" + "relatore"; 
        ResultSet relatoreNodes = searchService.query(
                commissioneNodeRef.getStoreRef(),
                SearchService.LANGUAGE_LUCENE, "PATH:\""
                + luceneCommissioneNodePath
                + "/cm:Relatori/*\" AND TYPE:\"" + relatoreType + "\"");

        return relatoreNodes;
    }

    /**
     * Metodo per decodificare il tipo iniziativa
     * @param value inziativa da decodificare
     * @return inziativa decodificata
     */
    protected String decodeTipoIniziativa(String value) {

        value = value.trim();
        String tipoIniziativa = tipoIniziativaDecode.get(value);

        if (tipoIniziativa == null || tipoIniziativa.length() < 1) {
            int idx = value.indexOf('_');
            if (idx >= 0) {
                tipoIniziativa = value.substring(idx + 1);
            } else {
                tipoIniziativa = value;
            }
        }

        return tipoIniziativa;
    }

    /**
     * Given a field and a list of values, this method returns the correct
     * syntax for a Lucene query in OR on that field. for example:
     * (field1,[value1,value2,value3] becomes: (field1:value1 OR field1:value2
     * OR field1:value3)
     *
     * @param field - field name
     * @param valueList - list of field values
     * @param phraseSearch - boolean to make phrase search or not
     * @return ritorna la query costruita per lucene in formato stringa
     */
    protected static String convertListToString(String field,
            List<String> valueList, Boolean phraseSearch) {
        String apexString = "";
        if (phraseSearch) {
            apexString = "\"";
        }
        String stringSpaced = valueList.toString().replaceAll(", ", ",");
        String stringReplaced = stringSpaced.replaceAll(",", apexString
                + " OR " + field.replaceFirst("\\:", "\\\\:") + ":"
                + apexString);
        String resultQueryString = "(" + field + ":" + apexString
                + stringReplaced.substring(1, stringReplaced.length() - 1)
                + apexString + ")";
        return resultQueryString;
    }

    /**
     * Save temporary on the fileSystem the byteArray. In this way is possible
     * to access to the tables in a right and independent manner.
     *
     * @param generatedDocument documento generato
     * @return Uno stream di byte array del documento passato nei parametri
     * @throws IOException IOException
     * @throws FileNotFoundException FileNotFoundException
     */
    protected ByteArrayInputStream saveTemp(XWPFDocument generatedDocument)
            throws IOException, FileNotFoundException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        generatedDocument.write(ostream);
        return new ByteArrayInputStream(ostream.toByteArray());
    }

    /**
     * Converte la lista di Tipo atto nel json con la sintassi alfresco
     * @param tipiAttoJson la lista dei tipo atto da convertire
     * @return la lista convertita nella sintassi alfresco
     */
    protected List<String> convertToLuceneTipiAtto(List<String> tipiAttoJson) {
        List<String> luceneTipiAttoList = new ArrayList<String>();
        for (String jsonType : tipiAttoJson) {
            String luceneValueType = "{" + CRL_ATTI_MODEL + "}" + jsonType;
            luceneTipiAttoList.add(luceneValueType);
        }
        return luceneTipiAttoList;
    }

    /**
     * Return from a map that relate a specific label to a number of results for
     * this label, the total number of results.
     *
     * @param group2atti una mappa che collega i gruppi agli atti
     * @return ritorna il count del totale dei risultati
     */
    protected int retrieveLenght(ArrayListMultimap<String, NodeRef> group2atti) {
        int count = 0;
        for (String group : group2atti.keySet()) {
            count += group2atti.get(group).size();
        }
        return count;
    }

    /**
     * Return a map that relate a specific label to a number of results for this
     * label
     *
     * @param commissione2atti una mappa che collega la commissione agli atti
     * @return una mappa con il totale dei risultati per commissione
     */
    protected Map<String, Integer> retrieveLenghtMap(
            Multimap<String, NodeRef> commissione2atti) {
        Map<String, Integer> commissione2count = new LinkedHashMap<String, Integer>();
        for (String commissione : commissione2atti.keySet()) {
            int count = commissione2atti.get(commissione).size();
            commissione2count.put(commissione, count);
        }
        return commissione2count;
    }

    /**
     * Return a map that relate a specific label to a number of results for this
     * label
     *
     * @param commissione2atti una mappa che collega la commissione agli atti
     * @return una mappa con il totale dei risultati per commissione
     */
    protected Map<String, Integer> retrieveLenghtMap(
            Map<String, List<NodeRef>> commissione2atti) {
        Map<String, Integer> commissione2count = new LinkedHashMap<String, Integer>();
        for (String commissione : commissione2atti.keySet()) {
            int count = commissione2atti.get(commissione).size();
            commissione2count.put(commissione, count);
        }
        return commissione2count;
    }

    /**
     * Return a map that relate a specific label to a number of results for this
     * label. Only elements that match the conditional rule are counted.
     *
     * @param commissione2atti una mappa che collega la commissione agli atti
     * @param doubleCheck  se effetuare un doppio controllo sullo stato del atto per conteggiarlo
     * @return una mappa con il totale dei risultati per commissione
     */
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

    /**
     * Check the validity of the Stato Atto. Implementing classes contains the
     * logic.
     *
     * @param statoAtto lo stato del atto
     * @return true con  lo stato valido
     */
    protected boolean checkStatoAtto(String statoAtto) {
        return true;
    }

    /**
     * Check the validity of the Stato Atto. Implementing classes contains the
     * logic.
     *
     * @param statoAtto lo stato del atto
     * @return true con  lo stato valido
     */
    protected boolean checkStatoAtto(String statoAtto, String tipoChiusura) {
        return true;
    }

    /**
     * Return last passaggio for a Atto NodeRef
     *
     * @param attoNodeRef il nodo dell atto
     *
     * @return ultimo passaggio di un atto
     */
    protected NodeRef getLastPassaggio(NodeRef attoNodeRef) {

        NodeRef passaggio = null;

        DynamicNamespacePrefixResolver namespacePrefixResolver = new DynamicNamespacePrefixResolver(
                null);
        namespacePrefixResolver.registerNamespace(
                NamespaceService.SYSTEM_MODEL_PREFIX,
                NamespaceService.SYSTEM_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(
                NamespaceService.CONTENT_MODEL_PREFIX,
                NamespaceService.CONTENT_MODEL_1_0_URI);
        namespacePrefixResolver.registerNamespace(
                NamespaceService.APP_MODEL_PREFIX,
                NamespaceService.APP_MODEL_1_0_URI);

        String luceneAttoNodePath = nodeService.getPath(attoNodeRef)
                .toPrefixString(namespacePrefixResolver);

        ResultSet passaggiNodes = searchService.query(
                attoNodeRef.getStoreRef(), SearchService.LANGUAGE_LUCENE,
                "PATH:\"" + luceneAttoNodePath + "/cm:Passaggi/*\"");

        int numeroPassaggio = 0;
        String nomePassaggio = "";
        int passaggioMax = 0;
        for (int i = 0; i < passaggiNodes.length(); i++) {

            nomePassaggio = (String) nodeService.getProperty(
                    passaggiNodes.getNodeRef(i), ContentModel.PROP_NAME);
            numeroPassaggio = Integer.parseInt(nomePassaggio.substring(9));

            if (numeroPassaggio > passaggioMax) {
                passaggioMax = numeroPassaggio;
                passaggio = passaggiNodes.getNodeRef(i);
            }

        }

        return passaggio;
    }

    /**
     * Return the string with the name of all the Abbinamento, comma separed
     *
     * @param currentAtto atto corrente
     * @return abbinamento1, abbinamento2, abbinamento3, etc..
     */
    protected String getAbbinamenti(NodeRef currentAtto) {
        String abbinamentiValue = StringUtils.EMPTY;
        NodeRef passaggio = this.getLastPassaggio(currentAtto);
        if (passaggio != null) {
            NodeRef abbinamentiFolderNode = nodeService.getChildByName(passaggio, ContentModel.ASSOC_CONTAINS, ABBINAMENTI_SPACE_NAME);
            Set<QName> childType = new HashSet<QName>();
            childType.add(QName.createQName(CRL_ATTI_MODEL, "abbinamento"));
            List<ChildAssociationRef> abbinamenti = nodeService.getChildAssocs(abbinamentiFolderNode, childType);
            List<String> abbinamentiList = new ArrayList<String>();
            for (Iterator<ChildAssociationRef> iterator = abbinamenti.iterator(); iterator.hasNext();) {
                ChildAssociationRef childAssociationRef = (ChildAssociationRef) iterator.next();
                NodeRef abbinamento = childAssociationRef.getChildRef();
                abbinamentiList.add(((String) nodeService.getProperty(abbinamento, ContentModel.PROP_NAME)).toUpperCase());
            }
            abbinamentiValue = renderList(abbinamentiList);
        }
        return abbinamentiValue;
    }

    /**
     * Recupera i Soggetti invitati di una consultazione
     * @param consultazione il nodo della consultazione
     * @return i Soggetti invitati
     */
    protected String getSoggettiInvitati(NodeRef consultazione) {
        String soggettiValue = StringUtils.EMPTY;
        NodeRef soggettiInvitatiFolder = nodeService.getChildByName(consultazione, ContentModel.ASSOC_CONTAINS, "SoggettiInvitati");

        if (soggettiInvitatiFolder != null) {

            Set<QName> childType = new HashSet<QName>();
            childType.add(QName.createQName(CRL_ATTI_MODEL, "soggettoInvitato"));
            List<ChildAssociationRef> soggettiInvitati = nodeService.getChildAssocs(soggettiInvitatiFolder, childType);

            List<String> soggettiList = new ArrayList<String>();
            for (Iterator<ChildAssociationRef> iterator = soggettiInvitati.iterator(); iterator.hasNext();) {
                ChildAssociationRef childAssociationRef = (ChildAssociationRef) iterator.next();
                NodeRef soggetto = childAssociationRef.getChildRef();
                soggettiList.add(((String) nodeService.getProperty(soggetto, QName.createQName(CRL_ATTI_MODEL, "descrizioneSoggettoInvitato"))));
            }
            soggettiValue = renderList(soggettiList);
        }

        return soggettiValue;
    }

    /**
     * Trasforma una lista di firmatari in una stringa separta da virgola.
     *
     * @param firmatari lista dei firmatari
     * @return una stringa con tutti i firmatari separta da virgola
     */
    protected String renderFirmatariConGruppoList(List<String> firmatari) {
        String encodedString = StringUtils.EMPTY;
        if (firmatari != null) {
            for (String firmatario : firmatari) {
            	String codiceGruppoConsiliare = getGruppoConsiliare(firmatario);
                encodedString += firmatario + " ("+codiceGruppoConsiliare+"), ";
                
            }
        }
        if (!encodedString.equals(StringUtils.EMPTY)) {
            encodedString = encodedString.substring(0,
                    encodedString.length() - 2);
        }
        return encodedString;
    }


    /**
     * Recupera il gruppo consiliare di un firmatario
     * @param firmatario per il quale recuperar il gruppo
     * @return Gruppo Consiliare del firmatario
     */
    protected String getGruppoConsiliare(String firmatario) {
		String gruppoConsiliare = StringUtils.EMPTY;
    	String luceneQuery = GRUPPO_FIRMATARIO_QUERY_ATTIVI.replaceAll(QUERY_REPLACER, firmatario);
		ResultSet firmatarioAttiviResults = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_LUCENE, luceneQuery);
		NodeRef firmatarioNodeRef = null;
		if(firmatarioAttiviResults.length()>0){ 
			firmatarioNodeRef = firmatarioAttiviResults.getNodeRef(0);
		} else { 
			luceneQuery = GRUPPO_FIRMATARIO_QUERY_STORICI.replaceAll(QUERY_REPLACER, firmatario);
			ResultSet firmatarioStoriciResults = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_LUCENE, luceneQuery);
			if(firmatarioStoriciResults.length()>0){
				firmatarioNodeRef = firmatarioStoriciResults.getNodeRef(0);
			}
		}
		if(firmatarioNodeRef!=null){
			gruppoConsiliare = (String) nodeService.getProperty(firmatarioNodeRef, PROP_CODICE_GRUPPO);
		}
		return gruppoConsiliare;
	}

	/**
     * Trasforma una lista di stringhe in un unica stringa seprata da virgola.
     *
     * @param stringList una lista di String da convertire
     * @return la lista trasformata in un unica stringa separata da virgola
     */
    protected String renderList(List<String> stringList) {
        String encodedString = StringUtils.EMPTY;
        if (stringList != null) {
            for (String singleValue : stringList) {
                encodedString += singleValue + ", ";
            }
        }
        if (!encodedString.equals(StringUtils.EMPTY)) {
            encodedString = encodedString.substring(0,
                    encodedString.length() - 2);
        }
        return encodedString;
    }

    /**
     * Trasforma una lista di stringhe in un unica stringa seprata da virgola.
     *
     * @param stringList una lista di String da convertire
     * @param toExclude il valore da exludere dalla conversione
     * @return la lista trasformata in un unica stringa separata da virgola
     */
    protected String renderWithExclusionList(List<String> stringList, String toExclude) {
        String encodedString = StringUtils.EMPTY;
        if (stringList != null) {
            for (String singleValue : stringList) {
            	if(!singleValue.equals(toExclude)){
            		encodedString += singleValue + ", ";
            	}
            }
        }
        if (!encodedString.equals(StringUtils.EMPTY)) {
            encodedString = encodedString.substring(0,
                    encodedString.length() - 2);
        }
        return encodedString;
    }

    /**
     * Return "Si" for True or "No" for False. Useful in front end rendering.
     *
     * @param booleano valore da trasformare
     * @return "Si" in caso di true e "No" in caso di false
     */
    protected String processBoolean(String booleano) {
        String result = StringUtils.EMPTY;
        if (booleano.equals("true")) {
            result = "Si";
        } else {
            result = "No";
        }
        return result;
    }

    /**
     * Init the common params : List<String> tipiAttoLucene; String
     * ruoloCommissioneLuceneField; List<String> commissioniJson;
     * @param json contentente le info per inizializzare i parametri comuni
     * @throws JSONException
     */
    protected void initCommonParams(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        initTipiAtto(json); 
        this.ruoloCommissione = JsonUtils.retieveElementFromJson(rootJson,
                "ruoloCommissione");  
        this.commissioniJson = JsonUtils.retieveArrayListFromJson(rootJson,
                "commissioni");

    }

    /**
     * Metodo per inizializzare il firmatario
     *
     * @param json con le varie info dove recuperare il firmatario
     * @throws JSONException
     */
    protected void initFirmatario(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.firmatario = JsonUtils.retieveElementFromJson(rootJson,
                "firmatario");
    }

    /**
     * Controlla se la data sia vuota in quel caso ritorna un '*'.
     * @param date data da controllare
     * @return se la data e null o vuota ritorna '*' altrimenti ritorna la data
     */
    protected String checkEmptyDate(String date) {
        String result = date;
        if (date == null || date.trim().equals("") || date.trim().equals("null")) {
            result = "*";
        }
        return result;

    }

    /**
     * Metodo per inizializzare data seduta da
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataSedutaDa(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataSedutaDa = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataSedutaDa"));
    }

    /**
     * Metodo per inizializzare data seduta a
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataSedutaA(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataSedutaA = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataSedutaA"));
    }

    /**
     * Metodo per inizializzare data assegnazione parere da
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataAssegnazioneParereDa(String json)
            throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataAssegnazioneParereDa = checkEmptyDate(JsonUtils.retieveElementFromJson(
                rootJson, "dataAssegnazioneParereDa"));
    }

    /**
     * Metodo per inizializzare data assegnazione parere a
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataAssegnazioneParereA(String json)
            throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataAssegnazioneParereA = checkEmptyDate(JsonUtils.retieveElementFromJson(
                rootJson, "dataAssegnazioneParereA"));
    }

    /**
     * Metodo per inizializzare data presentazione da
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataPresentazioneDa(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataPresentazioneDa = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataPresentazioneDa"));
    }


    /**
     * Metodo per inizializzare data presentazione a
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataPresentazioneA(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataPresentazioneA = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataPresentazioneA"));
    }


    /**
     * Metodo per inizializzare data nomina relatore da
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataNominaRelatoreDa(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataNominaRelatoreDa = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataNominaRelatoreDa"));
    }

    /**
     * Metodo per inizializzare data nomina relatore a
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataNominaRelatoreA(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataNominaRelatoreA = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataNominaRelatoreA"));
    }

    /**
     * Metodo per inizializzare data ritiro da
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataRitiroDa(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataRitiroDa = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataRitiroDa"));
    }

    /**
     * Metodo per inizializzare data ritiro a
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataRitiroA(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataRitiroA = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataRitiroA"));
    }

    /**
     * Metodo per inizializzare data assegnazione commisione referente da
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataAssegnazioneCommReferenteDa(String json)
            throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataAssegnazioneCommReferenteDa = checkEmptyDate(JsonUtils
                .retieveElementFromJson(rootJson, "dataAssegnazioneDa"));
    }

    /**
     * Metodo per inizializzare legislatura
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initLegislatura(String json)
            throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.legislatura = checkEmptyDate(JsonUtils
                .retieveElementFromJson(rootJson, "legislatura"));
    }

    /**
     * Metodo per inizializzare data assegnazione commisione referente a
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataAssegnazioneCommReferenteA(String json)
            throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataAssegnazioneCommReferenteA = checkEmptyDate(JsonUtils.retieveElementFromJson(
                rootJson, "dataAssegnazioneA"));
    }

    /**
     * Metodo per inizializzare data votazione commisione referente da
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataVotazioneCommReferenteDa(String json)
            throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataVotazioneCommReferenteDa = checkEmptyDate(JsonUtils.retieveElementFromJson(
                rootJson, "dataVotazioneCommReferenteDa"));
    }

    /**
     * Metodo per inizializzare data votazione commisione referente a
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataVotazioneCommReferenteA(String json)
            throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataVotazioneCommReferenteA = checkEmptyDate(JsonUtils.retieveElementFromJson(
                rootJson, "dataVotazioneCommReferenteA"));
    }

    /**
     * Metodo per inizializzare tipologia firma
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initTipoFirma(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.tipologiaFirma = JsonUtils.retieveElementFromJson(rootJson,
                "tipologiaFirma");
    }

    /**
     * Metodo per inizializzare Organismo
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initOrganismo(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.organismo = JsonUtils
                .retieveElementFromJson(rootJson, "organismo");
    }

    /**
     * Metodo per inizializzare i Relatori
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initRelatori(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.relatoriJson = JsonUtils.retieveArrayListFromJson(rootJson,
                "relatori");
    }

    /**
     * Metodo per inizializzare data consultazione da
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataConsultazioneDa(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataConsultazioneDa = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataConsultazioneDa"));
    }

    /**
     * Metodo per inizializzare data consultazione a
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initDataConsultazioneA(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.dataConsultazioneA = checkEmptyDate(JsonUtils.retieveElementFromJson(rootJson,
                "dataConsultazioneA"));
    }

    /**
     * Metodo per inizializzare i tipi atto
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initTipiAtto(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json); 
        List<String> tipiAttoJson = JsonUtils.retieveArrayListFromJson(
                rootJson, "tipiAtto"); 
        this.tipiAttoLucene = tipiAttoJson;
    }

    /**
     *  Metodo per inizializzare i tipi atto invertendo il case
     *  e aggiungendo un prefisso specifico
     *
     * @param json contente tutte le info passate alla generazione del report
     * @throws JSONException
     */
    protected void initTipiAttoLuceneAtto(String json) throws JSONException {
        JSONObject rootJson = new JSONObject(json);
        this.tipiAttoLucene = Lists.newArrayList(); 
        List<String> tipiAttoJson = JsonUtils.retieveArrayListFromJson(
                rootJson, "tipiAtto");
        for (String tipoAtto : tipiAttoJson) {
            String caseSensAtto = this.reverseCase(tipoAtto);
            this.tipiAttoLucene.add("crlatti:atto" + caseSensAtto);
        }
    }

    /**
     * Reverse the case of the String in input and capitalizes it
     *
     * @param tipoAtto tipologia atto
     * @return ritorna una stringa in maiuscolo del atto
     */
    private String reverseCase(String tipoAtto) {
        String lowercase = tipoAtto.toLowerCase();
        String capitalize = StringUtils.capitalize(lowercase);
        return capitalize;
    }

    /**
     * controlla se la data sia diversa da null e la formata in "dd/MM/yyyy"
     *
     * @param attributeDate la data da formattare
     * @return una data formatata o una stringa vuota in caso di null.
     */
    protected String checkDateEmpty(Date attributeDate) {
        if (attributeDate == null) {
            return StringUtils.EMPTY;
        } else {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATA_FORMAT);
            return dateFormatter.format(attributeDate);
        }

    }

    /**
     * Controlla se un testo sia null in quel caso torna una stringa vuota
     *
     * @param attribute testo da controllare
     * @return in caso di nulla un testo vuoto altrimenti lo stesso valore in input
     */
    protected String checkStringEmpty(String attribute) {
        if (attribute == null) {
            return "";
        } else {
            return attribute;
        }

    }

    /**
     * returna specific attribute value in a properties map
     *
     * @param properties mappa contentente le proprietà
     * @param attribute attributo da recuperare nella mappa
     * @return il  valore recuperato dalle proprietà.
     */
    protected Serializable getNodeRefProperty(
            Map<QName, Serializable> properties, String attribute) {
        return properties.get(QName.createQName(CRL_ATTI_MODEL, attribute));
    }

    public Map<String, String> getJson2luceneField() {
        return json2luceneField;
    }

    public void setJson2luceneField(Map<String, String> json2luceneField) {
        this.json2luceneField = json2luceneField;
    }

    public ContentService getContentService() {
        return contentService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public NodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public DictionaryService getDictionaryService() {
        return dictionaryService;
    }

    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
}
