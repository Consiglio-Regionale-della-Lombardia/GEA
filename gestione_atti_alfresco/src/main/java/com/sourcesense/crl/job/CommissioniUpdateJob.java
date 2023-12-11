package com.sourcesense.crl.job;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sourcesense.crl.util.AttoUtil;
import com.sourcesense.crl.util.SearchUtil;

public class CommissioniUpdateJob implements Job {

    private static Log logger = LogFactory.getLog(CommissioniUpdateJob.class);

	private ServiceRegistry serviceRegistry = null;
	private NodeService nodeService = null;
    private SearchUtil searchUtil = null;
	
	private Boolean jobEnabled = Boolean.FALSE;
    private String commissioneBefore = StringUtils.EMPTY;
    private String commissioneAfter = StringUtils.EMPTY;

    private final String SERVICE_REGISTRY = "serviceRegistry";

    private final String PASSAGGI_SPACE_NAME = "Passaggi";
    private final String COMMISSIONI_SPACE_NAME = "Commissioni";
    private final String COMMISSIONI_ANNULLATE_SPACE_NAME = "CommissioniAnnullate";
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        AuthenticationUtil.setAdminUserAsFullyAuthenticatedUser();

        this.retrieveJobData(context);

        if (!this.jobEnabled) {
            logger.info("Job Disabilitato.");
            return;
        } else {
            logger.info("Commissione before: " + this.commissioneBefore);
            logger.info("Commissione after: " + this.commissioneAfter);
        }

        try {

            String queryAttiDaBonificare = this.buildQueryAtti();

            List<NodeRef> nodeRefsAtti = this.searchUtil.getNodeRefsByQuery(queryAttiDaBonificare);

            if (nodeRefsAtti.isEmpty()) {
                logger.info("Nessun atto da aggiornare con commissione: " + commissioneBefore);
                return;
            } else {
                logger.info("Trovati " + nodeRefsAtti.size() + " atti da aggiornare");
            }

            for (NodeRef nodeAtto : nodeRefsAtti) {

                this.replacePropertiesAtto(nodeAtto);

                this.renameCommissioni(nodeAtto);

            }
                    
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }

    }

    private void renameCommissioni(NodeRef nodeAtto) throws Exception {

        NodeRef folderPassaggi = this.nodeService.getChildByName(nodeAtto, ContentModel.ASSOC_CONTAINS, PASSAGGI_SPACE_NAME);

        if (folderPassaggi == null || !this.nodeService.exists(folderPassaggi)) {
            logger.debug("L'atto " + this.nodeService.getProperty(nodeAtto, ContentModel.PROP_NAME) + " non presenta Passaggi");
            return;
        }

        List<ChildAssociationRef> listaPassaggi = this.nodeService.getChildAssocs(folderPassaggi);

        if (listaPassaggi.isEmpty()) {
            logger.debug("L'atto " + this.nodeService.getProperty(nodeAtto, ContentModel.PROP_NAME) + " non presenta passaggi nella folder Passaggi");
            return;
        }

        for (ChildAssociationRef passaggio : listaPassaggi) {

            this.renameCommissioneDefault(passaggio);

            this.renameCommissioneAnnullata(passaggio);

        }

    }

    private void renameCommissioneAnnullata(ChildAssociationRef passaggio) {

        NodeRef folderCommissioniAnnullate = this.nodeService.getChildByName(passaggio.getChildRef(), ContentModel.ASSOC_CONTAINS, COMMISSIONI_ANNULLATE_SPACE_NAME);
        List<ChildAssociationRef> commissioneAnnullate = this.nodeService.getChildAssocs(folderCommissioniAnnullate);

        if (commissioneAnnullate.isEmpty()) {
            return;
        }

        for (ChildAssociationRef commissioneAnnullata : commissioneAnnullate) {

            String[] nameCommissioneAnnullata = this.nodeService.getProperty(commissioneAnnullata.getChildRef(), ContentModel.PROP_NAME).toString().split("#");
            
            if (nameCommissioneAnnullata[0].equals(this.commissioneBefore)) {
                this.nodeService.setProperty(commissioneAnnullata.getChildRef(), ContentModel.PROP_NAME, this.commissioneAfter + "#" + nameCommissioneAnnullata[1]);
                logger.debug("Aggiornato nome commissione annullata");
                break;
            }
        }
    }

    private void renameCommissioneDefault(ChildAssociationRef passaggio) {

        NodeRef folderCommissioni = this.nodeService.getChildByName(passaggio.getChildRef(), ContentModel.ASSOC_CONTAINS, COMMISSIONI_SPACE_NAME);
        List<ChildAssociationRef> commissioniDefault = this.nodeService.getChildAssocs(folderCommissioni);

        if (commissioniDefault.isEmpty()) {
            return;
        }

        for (ChildAssociationRef commissioneDefault : commissioniDefault) {
            
            if (this.nodeService.getProperty(commissioneDefault.getChildRef(), ContentModel.PROP_NAME).toString().equals(this.commissioneBefore)) {
                this.nodeService.setProperty(commissioneDefault.getChildRef(), ContentModel.PROP_NAME, this.commissioneAfter );
                logger.debug("Aggiornato nome commissione");
                break;
            }
        }
    }

    private void replacePropertiesAtto(NodeRef nodeAtto) throws Exception {

        List<String> commReferenti = (ArrayList<String>) this.nodeService.getProperty(nodeAtto, AttoUtil.PROP_COMMISSIONI_REFERENTI_QNAME);
        List<String> commConsultive = (ArrayList<String>) this.nodeService.getProperty(nodeAtto, AttoUtil.PROP_COMMISSIONI_CONSULTIVE_QNAME);
        List<String> commCoreferenti = (ArrayList<String>) this.nodeService.getProperty(nodeAtto, AttoUtil.PROP_COMMISSIONE_COREFERENTE_QNAME);
        String commDeliberante = (String) this.nodeService.getProperty(nodeAtto, AttoUtil.PROP_COMMISSIONE_DELIBERANTE_QNAME);
        String commRedigente = (String) this.nodeService.getProperty(nodeAtto, AttoUtil.PROP_COMMISSIONE_REDIGENTE_QNAME);

        this.replacePropertyList(nodeAtto, commReferenti, AttoUtil.PROP_COMMISSIONI_REFERENTI_QNAME);
        this.replacePropertyList(nodeAtto, commConsultive, AttoUtil.PROP_COMMISSIONI_CONSULTIVE_QNAME);
        this.replacePropertyList(nodeAtto, commCoreferenti, AttoUtil.PROP_COMMISSIONE_COREFERENTE_QNAME);

        this.replacePropertyString(nodeAtto, commDeliberante, AttoUtil.PROP_COMMISSIONE_DELIBERANTE_QNAME);
        this.replacePropertyString(nodeAtto, commRedigente, AttoUtil.PROP_COMMISSIONE_REDIGENTE_QNAME);

        logger.debug("Aggiornate properties atto " + this.nodeService.getProperty(nodeAtto, ContentModel.PROP_NAME));

    }

    private void replacePropertyList(NodeRef nodeAtto, List<String> propertyValues, QName propertyQName) {

        if (propertyValues == null) return;

        if (propertyValues.contains(this.commissioneBefore)) {

            List<String> listValuesToAdd = propertyValues;
            listValuesToAdd.remove(this.commissioneBefore);
            listValuesToAdd.add(this.commissioneAfter);

            this.nodeService.setProperty(nodeAtto, propertyQName, (Serializable) listValuesToAdd);
        }

    }

    private void replacePropertyString(NodeRef nodeAtto, String propertyValue, QName propertyQName) {

        if (propertyValue != null && propertyValue.equals(this.commissioneBefore)) {
            this.nodeService.setProperty(nodeAtto, propertyQName, this.commissioneAfter);
        }

    }

    private String buildQueryAtti() throws Exception {

        String query = "TYPE:\"crlatti:atto\" AND crlatti:legislatura:\"" + this.getCurrentLegislatura() + "\"" +
            " AND (crlatti:commReferente:\"" + this.commissioneBefore + "\"" + 
            " OR crlatti:commConsultiva:\"" + this.commissioneBefore + "\"" +
            " OR crlatti:commCoreferente:\"" + this.commissioneBefore + "\"" +
            " OR crlatti:commDeliberante:\"" + this.commissioneBefore + "\"" +
            " OR crlatti:commRedigente:\"" + this.commissioneBefore + "\")";

        logger.debug("queryAttidaBonificare ---> " + query);

        return query;

    }

    private String getCurrentLegislatura() throws Exception {

        String queryLegislaturaCorrente = "TYPE:\"crlatti:legislaturaAnagrafica\" AND crlatti:correnteLegislatura:true";
        List<NodeRef> legislatureCorrenti = this.searchUtil.getNodeRefsByQuery(queryLegislaturaCorrente);
        
        if (legislatureCorrenti.size() != 1) {
            throw new Exception("Nessuna legislatura corrente oppure più di una legislatura corrente abilitata");
        }

        return (String) this.nodeService.getProperty(legislatureCorrenti.get(0), ContentModel.PROP_NAME);

    }

    private void retrieveJobData(JobExecutionContext context) {
        JobDataMap jobData = context.getJobDetail().getJobDataMap();
        
        if (jobData.containsKey(SERVICE_REGISTRY)) {
            this.setServiceRegistry((ServiceRegistry) jobData.get(SERVICE_REGISTRY));
        }
        
        this.configureJobFromDynamicProperties();
	}

    private void configureJobFromDynamicProperties() {
        String queryConfig = "PATH:\"/app:company_home/cm:Configurazioni/cm:commissioni_update.properties\"";
        List<NodeRef> listConfig = this.searchUtil.getNodeRefsByQuery(queryConfig);
        boolean configFound = false;

        if (!listConfig.isEmpty()) {
            for (NodeRef nodeConfig : listConfig) {
                if (this.nodeService.getProperty(nodeConfig, ContentModel.PROP_NAME).toString().equals("commissioni_update.properties")) {
                    
                    ContentReader contentReader = this.serviceRegistry.getContentService().getReader(nodeConfig, ContentModel.PROP_CONTENT);
                    Properties props = new Properties();
                    
                    try (InputStream inputStreamProps = contentReader.getContentInputStream()){
                        
                        props.load(inputStreamProps);
                        this.jobEnabled = Boolean.parseBoolean(props.getProperty("jobEnabled"));
                        this.commissioneBefore = props.getProperty("commissioneBefore");
                        this.commissioneAfter = props.getProperty("commissioneAfter");
                        
                    } catch (Exception e) {
                        logger.error(e);
                        e.printStackTrace();
                    }

                    configFound = true;
                    break;
                }
            }
        }
        
        if (!configFound) {
            throw new RuntimeException("Impossibile trovare il file configurazione");
        }
        if (StringUtils.isBlank(this.commissioneBefore) || StringUtils.isBlank(this.commissioneAfter)) {
            throw new RuntimeException("Properties 'commissioneBefore' e 'commissioneAfter' non settate correttamente");
        }
    }

    public void setSearchUtil(SearchUtil searchUtil) {
        this.searchUtil = searchUtil;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {

		this.serviceRegistry = serviceRegistry;
		this.nodeService = this.serviceRegistry.getNodeService();
	
    }
}