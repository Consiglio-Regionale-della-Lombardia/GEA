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
package com.sourcesense.crl.behaviour;

import com.sourcesense.crl.util.AttoUtil;
import com.sourcesense.crl.util.OpenDataCommand;
import com.sourcesense.crl.webservice.opendata.client.UpsertOpenData;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.executer.MailActionExecuter;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ActionService;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.*;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceException;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.web.bean.repository.Repository;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;
import java.util.Map.Entry;

/**
 * Behaviour creato in Alfresco per gestire gli eventi degli atti prima di cancellare un documento/atto in Alfresco e quando si aggiornano le proprietà di un documento/atto in Alfresco
 * @author sourcesense
 * @see https://docs.alfresco.com/5.2/references/dev-extension-points-behaviors.html
 */

public class AttoFolderBehaviour implements NodeServicePolicies.BeforeDeleteNodePolicy,
        NodeServicePolicies.OnUpdatePropertiesPolicy, NodeServicePolicies.OnCreateNodePolicy, NodeServicePolicies.OnDeleteNodePolicy {

    /* Aspect names */
    public static final QName ATTI_INDIRIZZO_ASPECT = QName
            .createQName("http://www.regione.lombardia.it/content/model/atti/1.0", "attiIndirizzoAspect");
    public static final QName PUBBLICABILE_ASPECT = QName
            .createQName("http://www.regione.lombardia.it/content/model/atti/1.0", "pubblicabile");
    private static final String ATTO_CANCELLATO = "cancellato";
    private static Log logger = LogFactory.getLog(AttoFolderBehaviour.class);
    private PolicyComponent policyComponent;

    private Behaviour onUpdateProperties;
    private Behaviour onCreateNode;
    private Behaviour beforeDeleteNode;
    private Behaviour onDeleteNode;
    private ContentService contentService;

    private FileFolderService fileFolderService;
    private NodeService nodeService;
    private SearchService searchService;
    private DictionaryService dictionaryService;
    private ActionService actionService;
    private String openDataAdminMailAddress;
    private String dataSeparator;
    private String listSeparator;
    private OpenDataCommand openDataCommand;
    private String ambiente;
    private String[] modelProperties;
    private String openDataDateFormat;
    private UpsertOpenData upsertOpenData;
    private NamespaceService namespaceService;
    private String[] tipoAtto;
    private String privateToken;

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public void setPrivateToken(String privateToken) {
        this.privateToken = privateToken;
    }

    public void setOpenDataCommand(OpenDataCommand openDataCommand) {
        this.openDataCommand = openDataCommand;
    }

    public void setTipoAtto(String[] tipoAtto) {
        this.tipoAtto = tipoAtto;
    }

    public void setUpsertOpenData(UpsertOpenData upsertOpenData) {
        this.upsertOpenData = upsertOpenData;
    }

    public void setPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponent = policyComponent;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setFileFolderService(FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    public void setNamespaceService(NamespaceService namespaceService) {
        this.namespaceService = namespaceService;
    }

    public void setOpenDataAdminMailAddress(String openDataAdminMailAddress) {
        this.openDataAdminMailAddress = openDataAdminMailAddress;
    }

    public void setDataSeparator(String dataSeparator) {
        this.dataSeparator = dataSeparator;
    }

    public void setModelProperties(String[] modelPropertiesString) {
        this.modelProperties = modelPropertiesString;
    }

    public void setOpenDataDateFormat(String openDataDateFormat) {
        this.openDataDateFormat = openDataDateFormat;
    }

    public void setListSeparator(String listSeparator) {
        this.listSeparator = listSeparator;
    }

    /**
     * Metodo che inizializza i behaviour che rimarranno in ascolto dentro Alfresco Repository
     * 
     * Tre behaviour: beforeDeleteNode,onUpdateProperties, onCreateNode
     */
    public void init() {
        this.beforeDeleteNode = new JavaBehaviour(this, "beforeDeleteNode", NotificationFrequency.EVERY_EVENT);
        this.policyComponent.bindClassBehaviour(NodeServicePolicies.BeforeDeleteNodePolicy.QNAME, ATTI_INDIRIZZO_ASPECT,
                this.beforeDeleteNode);
 
        onUpdateProperties = new JavaBehaviour(this, "onUpdateProperties",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);
        onCreateNode = new JavaBehaviour(this, "onCreateNode", Behaviour.NotificationFrequency.TRANSACTION_COMMIT);
        for (int i = 0; i < tipoAtto.length; i++) {
            QName tipoAttoQName = QName.createQName(AttoUtil.CRL_ATTI_MODEL, tipoAtto[i]);
            this.policyComponent.bindClassBehaviour(NodeServicePolicies.OnUpdatePropertiesPolicy.QNAME, tipoAttoQName,
                    onUpdateProperties);
            this.policyComponent.bindClassBehaviour(NodeServicePolicies.OnCreateNodePolicy.QNAME, tipoAttoQName,
                    onCreateNode);
        }
        this.policyComponent.bindClassBehaviour(NodeServicePolicies.OnUpdatePropertiesPolicy.QNAME, PUBBLICABILE_ASPECT,
                onUpdateProperties);
        onDeleteNode = new JavaBehaviour(this, "onDeleteNode", NotificationFrequency.TRANSACTION_COMMIT);
        this.policyComponent.bindClassBehaviour(NodeServicePolicies.OnDeleteNodePolicy.QNAME, PUBBLICABILE_ASPECT, this.onDeleteNode);
    }

    /**
     * Metodo che si esegue quando si attiva il behaviour beforeDeleteNode. 
     * Si genera un report in xml e si piazza: /app:company_home/cm:Export/cm:Gestione_x0020_Atti/cm:AttiIndirizzo
     * @param NodeRef con il nodo sul quale si è avviata l'azione di delete.
     * 
     */
    public void beforeDeleteNode(NodeRef nodeRef) {

        String CRL_ATTI_MODEL = "http://www.regione.lombardia.it/content/model/atti/1.0";
        String TYPE_LEGISLATURA = "legislaturaAnagrafica";

        String PROP_OGGETTO_ATTO = "oggetto";
        String PROP_LEGISLATURA = "legislatura";
        String PROP_ID_ANAGRAFICA = "idAnagrafica";

        String path = nodeService.getPath(nodeRef).toPrefixString(namespaceService);

        if (path == null || (!path.startsWith("/app:company_home/cm:CRL/cm:Gestione_x0020_Atti"))) {
            return;
        }

        String numeroAtto = (String) nodeService.getProperty(nodeRef, ContentModel.PROP_NAME);
        String idAtto = (String) nodeService.getProperty(nodeRef, ContentModel.PROP_NODE_UUID);
 
 
 
        String numeroLegislatura = (String) nodeService.getProperty(nodeRef,
                QName.createQName(CRL_ATTI_MODEL, PROP_LEGISLATURA));
 
        NodeRef legislatura = null;
        String idLegislatura = "";

        String legislaturaType = "{" + CRL_ATTI_MODEL + "}" + TYPE_LEGISLATURA;

        StoreRef storeRef = new StoreRef("workspace://SpacesStore");
 
        ResultSet legislatureNodes = null;
        try {
            legislatureNodes = searchService.query(storeRef, SearchService.LANGUAGE_LUCENE,
                    "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Legislature/cm:"
                            + numeroLegislatura + "\"");

            if (legislatureNodes.length() > 0) {
                legislatura = legislatureNodes.getNodeRef(0);
                idLegislatura = (String) nodeService
                        .getProperty(legislatura, QName.createQName(CRL_ATTI_MODEL, PROP_ID_ANAGRAFICA)).toString();
            }
        } finally {
            if (legislatureNodes != null) {
                legislatureNodes.close();
            }
        }

        String tipoAtto = nodeService.getType(nodeRef).getLocalName().substring(4).toUpperCase();
        String oggettoAtto = (String) nodeService.getProperty(nodeRef,
                QName.createQName(CRL_ATTI_MODEL, PROP_OGGETTO_ATTO));

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        ResultSet exportFolderNodeResultSet = null;
        try {

            docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("atto");
            doc.appendChild(rootElement);
            doc.getInputEncoding();

            Attr attr1 = doc.createAttribute("id_atto");
            attr1.setValue(idAtto);
            rootElement.setAttributeNode(attr1);

            Attr attr2 = doc.createAttribute("id_legislatura");
            attr2.setValue(idLegislatura);
            rootElement.setAttributeNode(attr2);

            Attr attr3 = doc.createAttribute("tipo_atto");
            attr3.setValue(tipoAtto);
            rootElement.setAttributeNode(attr3);

            Attr attr4 = doc.createAttribute("numero_atto");
            attr4.setValue(numeroAtto);
            rootElement.setAttributeNode(attr4);

            Attr attr5 = doc.createAttribute("oggetto_atto");
            attr5.setValue(oggettoAtto);
            rootElement.setAttributeNode(attr5);

            Attr attr6 = doc.createAttribute("operazione");
            attr6.setValue("DELETE");
            rootElement.setAttributeNode(attr6);

            StringWriter output = new StringWriter();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(output));

            String xmlString = output.toString();

            Calendar calendar = new GregorianCalendar();

            int anno = calendar.get(Calendar.YEAR);
            int mese = calendar.get(Calendar.MONTH) + 1;
            int giorno = calendar.get(Calendar.DAY_OF_MONTH);
            int ora = calendar.get(Calendar.HOUR_OF_DAY);
            int minuto = calendar.get(Calendar.MINUTE);
            int secondo = calendar.get(Calendar.SECOND);

            String nomeFileExport = "export_atto_" + numeroAtto + "_" + anno + "-" + mese + "-" + giorno + "_" + ora
                    + "-" + minuto + "-" + secondo + ".xml";

            exportFolderNodeResultSet = searchService.query(Repository.getStoreRef(), SearchService.LANGUAGE_LUCENE,
                    "PATH:\"/app:company_home/cm:Export/cm:Gestione_x0020_Atti/cm:AttiIndirizzo\"");

            FileInfo xmlFileInfo = fileFolderService.create(exportFolderNodeResultSet.getNodeRef(0), nomeFileExport,
                    ContentModel.TYPE_CONTENT);

            ContentWriter contentWriter = contentService.getWriter(xmlFileInfo.getNodeRef(), ContentModel.PROP_CONTENT,
                    true);
            contentWriter.setMimetype("text/plain");
            contentWriter.setEncoding("UTF-8");
            contentWriter.putContent(xmlString);

            logger.info("Creato file export " + nomeFileExport + ": operazione DELETE su atto " + tipoAtto + " "
                    + numeroAtto);

        } catch (Exception e) {
            logger.info("Errore nella creazione del file xml di export: operazione DELETE su atto " + tipoAtto + " "
                    + numeroAtto);
        } finally {
            if (exportFolderNodeResultSet != null) {
                exportFolderNodeResultSet.close();
            }
        }

    }

    /**
     * Crea la struttura di dati da inviare come messaggio SOAP al WS Opendata.
     * @param childRef nodo che in base alla proprietà 
     * @return Stringa con tutto il contenuto del messaggio da inviare al WS Opendata.
     */
    private String generateOdAtto(NodeRef childRef) {
        StringBuilder odAtto = new StringBuilder("");
        int n = modelProperties.length;
        for (int i = 0; i < n; i++) {
            String property = modelProperties[i];
            QName propertyQName;
            String[] qnameStringSplitted = property.split(":");
            String prefix = qnameStringSplitted[0];
            String localName = qnameStringSplitted[1];
            Serializable val;
            try {
                propertyQName = QName.createQName(prefix, localName, namespaceService);
            } catch (NamespaceException e) {
                propertyQName = QName.createQName(prefix, localName);
            }
            switch (propertyQName.toString()) {
                case "{openDataCommand}getTipoAtto": {
                    val = openDataCommand.getTipoAtto(childRef);
                    break;
                }
                case "{openDataCommand}getIdAtto": {
                    val = openDataCommand.getIdAtto(childRef);
                    break;
                }
                case "{openDataCommand}getNumeroAtto": {
                    val = openDataCommand.getNumeroAtto(childRef);
                    break;
                }
                case "{openDataCommand}tipoIniziativa": {
                    val = openDataCommand.getTipoIniziativa(childRef);
                    break;
                }
                case "{openDataCommand}linkTestoAttoComReferente": {
                    val = openDataCommand.getLinkTestoAttoComReferente(childRef);
                    break;
                }
                case "{openDataCommand}linkTestoRelazioneIllustrativa": {
                    val = openDataCommand.getLinkTestoRelazioneIllustrativa(childRef);
                    break;
                }
                case "{openDataCommand}linkTestoSchedaIstruttoria": {
                    val = openDataCommand.getLinkTestoSchedaIstruttoria(childRef);
                    break;
                }
                case "{openDataCommand}getLinkVotoFinaleAula": {
                    val = openDataCommand.getLinkVotoFinaleAula(childRef);
                    break;
                }
                case "{openDataCommand}linkAtto": {
                    val = openDataCommand.getLinkAtto(childRef);
                    break;
                }
                case "{openDataCommand}getPrimoFirmatario": {
                    val = openDataCommand.getPrimoFirmatario(childRef);
                    break;
                }
                case "{openDataCommand}getTuttiFirmatari": {
                    val = openDataCommand.getTuttiFirmatari(childRef);
                    break;
                }
                case "{openDataCommand}getPrimoPromotore": {
                    val = openDataCommand.getPrimoPromotore(childRef);
                    break;
                }
                case "{openDataCommand}getTuttiPromotori": {
                    val = openDataCommand.getTuttiPromotori(childRef);
                    break;
                }
                case "{openDataCommand}getRelatore": {
                    val = openDataCommand.getRelatore(childRef);
                    break;
                }
                case "{openDataCommand}getDataNominaRelatore": {
                    val = openDataCommand.getDataNominaRelatore(childRef);
                    if (val != null)
                        val = DateFormatUtils.format((Date) val, openDataDateFormat);
                    break;
                }
                case "{openDataCommand}getAbbinamenti": {
                    val = openDataCommand.getAbbinamenti(childRef);
                    break;
                }
                case "{openDataCommand}getDataVotazioneCommissioneReferente": {
                    val = openDataCommand.getDataVotazioneCommissioneReferente(childRef);
                    if (val != null)
                        val = DateFormatUtils.format((Date) val, openDataDateFormat);
                    break;
                }

                case "{openDataCommand}getEsitoVotazioneCommissioneReferente": {
                    val = openDataCommand.getEsitoVotazioneCommissioneReferente(childRef);
                    break;
                }
                case "{openDataCommand}getEsitoVotazioneAula": {
                    val = openDataCommand.getEsitoVotazioneAula(childRef);
                    break;
                }
                case "{openDataCommand}getDataVotazioneAula": {
                    val = openDataCommand.getDataVotazioneAula(childRef);
                    if (val != null)
                        val = DateFormatUtils.format((Date) val, openDataDateFormat);
                    break;
                }
                case "{openDataCommand}getNumeroDcrPassaggioAula": {
                    val = openDataCommand.getNumeroDcrPassaggioAula(childRef);
                    break;
                }

                default: {
                    val = nodeService.getProperty(childRef, propertyQName);
                    if (val instanceof Date) {
                        val = DateFormatUtils.format((Date) val, openDataDateFormat);
                    } else if (val instanceof ArrayList) {
                        StringBuilder tmpVal = new StringBuilder("");
                        int z = ((ArrayList) val).size();
                        for (int j = 0; j < z; j++) {
                            ((ArrayList) val).get(j);
                            tmpVal.append(((ArrayList) val).get(j).toString());
                            if (j < z - 1) {
                                tmpVal.append(listSeparator);
                            }
                        }
                        val = tmpVal.toString();
                    }
                }
            }
            if (val == null) {
                val = "";
            }
            odAtto.append(val);
            if (i < n - 1) {
                odAtto.append(dataSeparator);
            }
        }
        return odAtto.toString();
    }

    /**
     * Metodo che si esegue quando si attiva il behaviour onCreateNode. 
     * Crea una richiesta per il Web Service di OPENDATA per la notifica della modifica dell'atto. 
     * Se va in errore, si notifica tramite mail l'esito negativo 
     * @param ChildAssociationRef Nodo figlio con il quale creare la struttura di dati da inviare al Web Service.
     * 
     */
    @Override
    public void onCreateNode(ChildAssociationRef childAssocRef) {
        String odAtto = "";
        try {
            odAtto = generateOdAtto(childAssocRef.getChildRef());
            logger.debug("odAtto: " + odAtto);
            String response = upsertOpenData.getUpsertOpenDataSoap().upsertATTO(odAtto, privateToken, ambiente);
            logger.info("Il webservice ha restituito " + response);
        } catch (Exception e) {
            try {
                logger.error("Impossibile notificare ad OpenData l'avvenuta modifica dell'atto ", e);
                String idAtto = openDataCommand.getIdAtto(childAssocRef.getChildRef());
                notifyOpenDataAdmin("Mancata creazione atto " + idAtto, null, getEmailTemplateNodeRef());
            } catch (OpenDataAdminNotificationException e1) {
 
                logger.error("Impossibile inviare mail di notifica", e);
            }
        }
    }

    /**
     * Metodo che si esegue quando si attiva il behaviour onUpdateProperties. 
     * Crea una richiesta per il Web Service di OPENDATA per la notifica dell'aggiornamento dell'atto.Se mancano dei dati nell'atto, non si invia la richiesta al WS.
     * Se va in errore, si notifica tramite mail l'esito negativo. Se lo stato del atto è CANCELLATO, si invia la richiesta al WS per la cancellazione.
     * @param nodeRef Nodo con il quale creare la struttura di dati da inviare al Web Service.
     * @param Map<QName, Serializable> before: le proprietà prima dell'action update
     * @param Map<QName, Serializable> after: le proprietà dopo l'action update
     * 
     */
    
    @Override
    public void onUpdateProperties(NodeRef nodeRef, Map<QName, Serializable> before, Map<QName, Serializable> after) {

        if ((after.containsKey(AttoUtil.PROP_STATO_ATTO_QNAME)
                && !((String) after.get(AttoUtil.PROP_STATO_ATTO_QNAME)).equalsIgnoreCase(ATTO_CANCELLATO))
                || !after.containsKey(AttoUtil.PROP_STATO_ATTO_QNAME)
                || (after.containsKey(AttoUtil.PROP_PUBBLICO_OPENDATA_QNAME) && ((boolean) after
                .get(AttoUtil.PROP_PUBBLICO_OPENDATA_QNAME) || (before
                .containsKey(AttoUtil.PROP_PUBBLICO_OPENDATA_QNAME)
                && ((boolean) after.get(AttoUtil.PROP_PUBBLICO_OPENDATA_QNAME) != (boolean) before
                .get(AttoUtil.PROP_PUBBLICO_OPENDATA_QNAME)))))) {
            NodeRef attoNodeRef = new NodeRef(nodeRef.toString());
            attoNodeRef = retrieveAttoNodeRefFromChild(attoNodeRef);
            if (attoNodeRef != null && ((after.containsKey(AttoUtil.PROP_PUBBLICO_OPENDATA_QNAME)
                    || (dictionaryService.isSubClass(nodeService.getType(nodeRef), AttoUtil.TYPE_ATTO))) && !nodeService.getType(attoNodeRef).equals(AttoUtil.TYPE_ATTO_EAC))) {
                try {
                	logger.debug("Accesso a  numero atto da model:" + AttoUtil.PROP_NUMERO_ATTO_QNAME);
                	logger.debug("Inizio struttura Alfter");
                	
                	for (Iterator iterator = after.entrySet().iterator(); iterator.hasNext();) {
						Entry<QName,Serializable> ent = (Entry<QName,Serializable>)iterator.next();
                		 
						logger.debug(((QName)ent.getKey()).getLocalName() + "\n");
					}
                	logger.debug("Mappatura proprietà AttoUtil: " + after.get(AttoUtil.PROP_LEGISLATURA_QNAME) + "; Numero atto:" + after.containsKey(AttoUtil.PROP_NUMERO_ATTO_QNAME) + "; Numero atto:" + after.get(AttoUtil.PROP_NUMERO_ATTO_QNAME)
                	+ "; oggetto: " + after.get(AttoUtil.PROP_OGGETTO_ATTO_QNAME) + "; Data iniziativa " + after.get(AttoUtil.PROP_DATA_INIZIATIVA_ATTO_QNAME) + "; " + after.get(AttoUtil.PROP_STATO_ATTO_QNAME)
                	+"; Referenti commissione: " + after.get(AttoUtil.PROP_COMMISSIONI_REFERENTI_QNAME) + "; Data Assegnazione:" + after.get(AttoUtil.PROP_DATA_ASSEGNAZIONE_COMMISSIONI_REFERENTI_QNAME));
                    
                    boolean hasAspectTestoTipizzabile = nodeService.hasAspect(nodeRef, AttoUtil.ASPECT_TESTO_TIPIZZABILE);
                    
                    if (hasAspectTestoTipizzabile) {
                        logger.debug("+++ hasAspectTestoTipizzabile: " + hasAspectTestoTipizzabile + " --> " + nodeRef.toString());
                    }

                    if (
                            (after.containsKey(AttoUtil.PROP_LEGISLATURA_QNAME) && after.get(AttoUtil.PROP_LEGISLATURA_QNAME) != null && !((String) after.get(AttoUtil.PROP_LEGISLATURA_QNAME)).isEmpty()
                                    && after.containsKey(AttoUtil.PROP_NUMERO_ATTO_QNAME) && (after.get(AttoUtil.PROP_NUMERO_ATTO_QNAME)) != null
                                    && after.containsKey(AttoUtil.PROP_OGGETTO_ATTO_QNAME) && after.get(AttoUtil.PROP_OGGETTO_ATTO_QNAME) != null && !((String) after.get(AttoUtil.PROP_OGGETTO_ATTO_QNAME)).isEmpty()
                                    && after.containsKey(AttoUtil.PROP_DATA_INIZIATIVA_ATTO_QNAME) && after.get(AttoUtil.PROP_DATA_INIZIATIVA_ATTO_QNAME) != null 
                                    && after.containsKey(AttoUtil.PROP_STATO_ATTO_QNAME) && after.get(AttoUtil.PROP_STATO_ATTO_QNAME) != null && !((String) after.get(AttoUtil.PROP_STATO_ATTO_QNAME)).isEmpty()
                                    && after.containsKey(AttoUtil.PROP_COMMISSIONI_REFERENTI_QNAME) && after.get(AttoUtil.PROP_COMMISSIONI_REFERENTI_QNAME) != null && !((ArrayList)after.get(AttoUtil.PROP_COMMISSIONI_REFERENTI_QNAME)).isEmpty()
                                    && after.containsKey(AttoUtil.PROP_DATA_ASSEGNAZIONE_COMMISSIONI_REFERENTI_QNAME) && after.get(AttoUtil.PROP_DATA_ASSEGNAZIONE_COMMISSIONI_REFERENTI_QNAME) != null ) || hasAspectTestoTipizzabile

                    ) {
                        logger.info("Notifica aggiornamento dell'atto " + nodeService.getType(attoNodeRef) + " "
                                + nodeService.getProperty(attoNodeRef, AttoUtil.PROP_NUMERO_ATTO_QNAME));
                        String odAtto = "";
                        odAtto = generateOdAtto(attoNodeRef);
                        logger.debug("odAtto: " + odAtto);
                        String response = upsertOpenData.getUpsertOpenDataSoap().upsertATTO(odAtto, privateToken, ambiente);
                        logger.info("Il webservice ha restituito " + response);
                    } else {
                        logger.info("Troppe poche informazioni per fare la chiamata ad opendata");
                    }
                } catch (Exception e) {
                    try {
                        logger.error("Impossibile notificare ad OpenData l'avvenuta modifica dell'atto ", e);
                        String idAtto = openDataCommand.getIdAtto(attoNodeRef);
                        notifyOpenDataAdmin("Mancato aggiornamento atto " + idAtto, null, getEmailTemplateNodeRef());
                    } catch (OpenDataAdminNotificationException e1) {
 
                        logger.error("Impossibile inviare mail di notifica", e);
                    }
                }
            }
        } else { // cancellazione atto
            String idAtto = "";
            try {
                logger.info("Notifica cancellazione dell'atto " + nodeService.getType(nodeRef) + " "
                        + nodeService.getProperty(nodeRef, AttoUtil.PROP_NUMERO_ATTO_QNAME));
                idAtto = openDataCommand.getIdAtto(nodeRef);
                logger.debug("idAtto: " + idAtto);
                String response = upsertOpenData.getUpsertOpenDataSoap().deleteATTO(idAtto, privateToken, ambiente);
                logger.info("Il webservice ha restituito " + response);
            } catch (Exception e) {
                try {
                    logger.error("Impossibile notificare ad OpenData l'avvenuta cancellazione dell'atto ", e);
                    notifyOpenDataAdmin("Mancato cancellazione atto " + idAtto, null, getEmailTemplateNodeRef());
                } catch (OpenDataAdminNotificationException e1) {
 
                    logger.error("Impossibile inviare mail di notifica", e);
                }
            }
        }
    }

    /**
     * Invio della notifica di attività tramite mail realizzata impiegando il WS Opendata. 
     * @param subjectText Titolo della mail
     * @param model parametri per il template
     * @param templateNodeRef il template per la mail.
     */
    protected void notifyOpenDataAdmin(String subjectText, Map<String, Serializable> model, NodeRef templateNodeRef) {
 
        Action mail = actionService.createAction(MailActionExecuter.NAME);

        mail.setParameterValue(MailActionExecuter.PARAM_TO, openDataAdminMailAddress);
        mail.setParameterValue(MailActionExecuter.PARAM_SUBJECT, subjectText);
        mail.setParameterValue(MailActionExecuter.PARAM_TEMPLATE, templateNodeRef);
        mail.setParameterValue(MailActionExecuter.PARAM_TEMPLATE_MODEL, (Serializable) model);

        actionService.executeAction(mail, null);
    }

    /**
     * Trova il template che si vuole impiegare per la mail correlata con l'attività. Fisicamente si trova in Alfresco nel path: 
     * app:company_home/app:dictionary/app:email_templates/cm:opendata/cm:open-data-notify-email.html.ftl
     * @return NodeRef che punta al nodo di Alfresco che ha il template 
     * @throws OpenDataAdminNotificationException
     */
    private NodeRef getEmailTemplateNodeRef() throws OpenDataAdminNotificationException {
        List<NodeRef> nodeRefs = searchService.selectNodes(
                nodeService.getRootNode(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE),
                "app:company_home/app:dictionary/app:email_templates/cm:opendata/cm:open-data-notify-email.html.ftl",
                null, this.namespaceService, false);

        if (nodeRefs.size() == 1) {
 
            NodeRef base = nodeRefs.get(0);
 
 
            return base;
        } else {
            throw new OpenDataAdminNotificationException("Cannot find the email template!");
        }
    }

/**
 * Partendo da un Nodo figlio con tipo TYPE_ATTO, si ricava l'atto che si trova nel nodo padre
 * @param child il nodo figlio dal quale si parte
 * @return il NodeRef con il padre di child che rappresenta l'atto.
 */
    private NodeRef retrieveAttoNodeRefFromChild(NodeRef child) {
        NodeRef attoNodeRef = child;
        while ((attoNodeRef != null)
                && nodeService.exists(attoNodeRef) &&
                (!dictionaryService.isSubClass(nodeService.getType(attoNodeRef), AttoUtil.TYPE_ATTO))) {
            attoNodeRef = nodeService.getPrimaryParent(attoNodeRef).getParentRef();
        }
        return attoNodeRef;
    }

    /**
     * Metodo che si esegue quando si attiva il behaviour onDeleteNode. Se il nodo che arriva come parametro non è un atto, si cerca l'atto nei figli del proprio nodo.
     * Si preprara il messaggio da inviare al WS Opendata con la cancellazione dell'atto. Si notifica tramite mail l'aggiornamento dell'atto
     * @param ChildAssociationRef nodo sul quale accade il behaviour.
     * @param boolean:non utilizzato.
     */
    @Override
    public void onDeleteNode(ChildAssociationRef childAssociationRef, boolean b) {
        NodeRef attoNodeRef;
        if (nodeService.exists(childAssociationRef.getChildRef()) && dictionaryService.isSubClass(nodeService.getType(childAssociationRef.getChildRef()), AttoUtil.TYPE_ATTO)) {
            attoNodeRef = childAssociationRef.getChildRef();
        } else {
            attoNodeRef = retrieveAttoNodeRefFromChild(childAssociationRef.getParentRef());
        }
        if (attoNodeRef != null && nodeService.exists(attoNodeRef)) {
            try {
                logger.info("Notifica aggiornamento dell'atto " + nodeService.getType(attoNodeRef) + " "
                        + nodeService.getProperty(attoNodeRef, AttoUtil.PROP_NUMERO_ATTO_QNAME));
                String odAtto = "";
                odAtto = generateOdAtto(attoNodeRef);
                logger.debug("odAtto: " + odAtto);
                String response = upsertOpenData.getUpsertOpenDataSoap().upsertATTO(odAtto, privateToken, ambiente);
                logger.info("Il webservice ha restituito " + response);
            } catch (Exception e) {
                try {
                    logger.error("Impossibile notificare ad OpenData l'avvenuta modifica dell'atto ", e);
                    String idAtto = openDataCommand.getIdAtto(attoNodeRef);
                    notifyOpenDataAdmin("Mancato aggiornamento atto " + idAtto, null, getEmailTemplateNodeRef());
                } catch (OpenDataAdminNotificationException e1) {
 
                    logger.error("Impossibile inviare mail di notifica", e);
                }
            }
        }
    }
}
