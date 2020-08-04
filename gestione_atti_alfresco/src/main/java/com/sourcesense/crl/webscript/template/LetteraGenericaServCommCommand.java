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
package com.sourcesense.crl.webscript.template;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.dictionary.TypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sourcesense.crl.util.AttoUtil;

/**
 * {@inheritDoc}
 * @return ritorna un documento  lettera generica servizio commissioni .
 */
public class LetteraGenericaServCommCommand extends LetteraBaseCommand{

	private static Log logger = LogFactory.getLog(LetteraGenericaServCommCommand.class);
	/**
	 * {@inheritDoc}
	 * @return ritorna un documento  lettera generica commissioni .
	 */
	public byte[] generate(byte[] templateByteArray, NodeRef templateNodeRef, NodeRef attoNodeRef, String gruppo) {
		
		byte[] documentFilledByteArray = null; 
    	HashMap<String, String> searchTerms = new HashMap<String, String>(); 
    	String firmatario = (String) nodeService.getProperty(templateNodeRef, QName.createQName(AttoUtil.CRL_TEMPLATE_MODEL, PROP_FIRMATARIO));	
		searchTerms.put("<firmatario>", firmatario);
		
		QName templateType  =  nodeService.getType(templateNodeRef);
		String nomeTemplate = templateType.getLocalName(); 
    	String numeroAtto = ((Integer) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_NUM_ATTO))) + 
    			((String) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_ESTENSIONE_ATTO)));
    	String oggettoAtto = (String) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_OGGETTO_ATTO));
    	String iniziativa = (String) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_TIPO_INIZIATIVA));
    	String descrizioneIniziativa = (String) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_DESCRIZIONE_INIZIATIVA));
    	String numeroRepertorio = (String) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_NUMERO_REPERTORIO));
    	String numeroDgr = (String) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_NUMERO_DGR));
    	Date dataDgr = (Date) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_DATA_DGR));
    	Date dataRitiro = (Date) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_DATA_CHIUSURA_ATTO));
    	
    	if(dataDgr != null) {
    		SimpleDateFormat dataDgrFormatter = new SimpleDateFormat("dd/MM/yy");
    		String dataDgrString = dataDgrFormatter.format(dataDgr);
    		searchTerms.put("<dataDGR>", dataDgrString);
    	}
    	
    	if(dataRitiro != null) {
    		SimpleDateFormat dataRitiroFormatter = new SimpleDateFormat("dd/MM/yy");
    		String dataRitiroString = dataRitiroFormatter.format(dataRitiro);
    		searchTerms.put("<dataRitiroAtto>", dataRitiroString);
    	}
    		
    	DictionaryService dictionaryService = serviceRegistry.getDictionaryService();		
		TypeDefinition typeDef = dictionaryService.getType(nodeService.getType(attoNodeRef));
		String tipoAttoDescrizione = typeDef.getTitle().toLowerCase(); 
		String tipoAttoSigla= getTipoAttoSigla(attoNodeRef);
		
        searchTerms.put("<tipoAttoSigla>", tipoAttoSigla);
		searchTerms.put("<numeroAtto>", numeroAtto);
		searchTerms.put("<oggettoAtto>", oggettoAtto);
		searchTerms.put("<numeroRepertorio>", numeroRepertorio);
		searchTerms.put("<tipoAttoDescrizione>", tipoAttoDescrizione);
		searchTerms.put("<numeroDGR>", numeroDgr); 
    	List<String> firmatariList = (List<String>) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_FIRMATARI));
    	String firmatari = StringUtils.EMPTY;
    	
    	if(firmatariList!=null) {
	    	for(int i=0; i<firmatariList.size(); i++){	
	    		firmatari += firmatariList.get(i);
	    		if(i<firmatariList.size()-1){
	    			firmatari += ", ";
	    		}
	    	}
    	}
    	
    	searchTerms.put("<firmatariAtto>", firmatari);    	
    	
    	NodeRef ultimoFirmatario = getLastFirmatario(attoNodeRef);
    	if(ultimoFirmatario!=null) {
    		String ultimoFirmatarioString = (String) nodeService.getProperty(ultimoFirmatario, ContentModel.PROP_NAME);
    		searchTerms.put("<ultimoFirmatarioAtto>", ultimoFirmatarioString);
    	}
    	

    	if(iniziativa!=null && iniziativa!=StringUtils.EMPTY){
	    	if(iniziativa.equals(AttoUtil.INIZIATIVA_CONSILIARE)){
	    		
	    		descrizioneIniziativa = "di iniziativa dei Consiglieri regionali: ";
	        	
	        	if(firmatariList!=null) {
	    	    	for(int i=0; i<firmatariList.size(); i++){	
	    	    		descrizioneIniziativa += firmatariList.get(i);
	    	    		if(i<firmatariList.size()-1){
	    	    			descrizioneIniziativa += ", ";
	    	    		}
	    	    	}
	        	}
	        	
	        	searchTerms.put("<iniziativa>", iniziativa.substring(8));
	        	
	        }else if(iniziativa.equals(AttoUtil.INIZIATIVA_PRESIDENTE_GIUNTA)){
	        	
	        	iniziativa = "DI INIZIATIVA: PRESIDENTE DELLA GIUNTA REGIONALE";
	        	searchTerms.put("<iniziativa>", iniziativa);
	        	
	        }else{
	        	
	        	iniziativa = iniziativa.replaceAll("INIZIATIVA", "INIZIATIVA:");
	        	searchTerms.put("<iniziativa>", iniziativa.substring(8));
	        }
	    	
	    	
	    	searchTerms.put("<iniziativaMinuscolo>", iniziativa.substring(8).toLowerCase());
	    	searchTerms.put("<descrizioneIniziativa>", descrizioneIniziativa);
    	}
    	
		
	 	List<String> commissioniReferenti = (List<String>) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_COMMISSIONI_REFERENTI));
    	List<String> commissioniConsultive = (List<String>) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_COMMISSIONI_CONSULTIVE));
    	List<String> commissioniCoreferenti = (List<String>) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_COMMISSIONE_COREFERENTE));
    	String commissioneRedigente = (String) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_COMMISSIONE_REDIGENTE));
    	String commissioneDeliberante = (String) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_COMMISSIONE_DELIBERANTE));
    	List<String> organismiStatutari = (List<String>) nodeService.getProperty(attoNodeRef, QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_ORGANISMI_STATUTARI));
  
    	
    	List<String> listaCommissioniPrincipali = new ArrayList<String>(); 
    	if(commissioniReferenti!=null && commissioniReferenti.size()>0) {
    		
    		listaCommissioniPrincipali.add(commissioniReferenti.get(0)); 
    	}else { 
    		if(commissioneRedigente!=null && !commissioneRedigente.equals(StringUtils.EMPTY)) {
    			listaCommissioniPrincipali.add(commissioneRedigente); 
    		}else {
    			if(commissioneDeliberante!=null && !commissioneDeliberante.equals(StringUtils.EMPTY)) {
    				listaCommissioniPrincipali.add(commissioneDeliberante);
    			}
    		}
    		
    	}
    	
    	if(commissioniCoreferenti != null && commissioniCoreferenti.size() > 0){
        	Iterator<String> i = commissioniCoreferenti.iterator();
        	while(i.hasNext()){
        		listaCommissioniPrincipali.add(i.next());
        	}
        } 
    	String listaCommissioniPrincipaliDestString = StringUtils.EMPTY;
    	
    	for(int i=0; i<listaCommissioniPrincipali.size(); i++) {
    		
    		if(i!=0){
    			listaCommissioniPrincipaliDestString += "\r\r";
    		}
    		listaCommissioniPrincipaliDestString += "Al Signor Presidente\rdella Commissione consiliare "+getCommissioneNumber(listaCommissioniPrincipali.get(i));
    	}
    					
    	searchTerms.put("<listaCommissioniPrincipaliDest>", listaCommissioniPrincipaliDestString); 
    	String listaCommissioniConsultiveDestString = StringUtils.EMPTY;
    	String listaCommissioniConsultiveInvitiString = "Commissione consiliare "; 
    	if(commissioniConsultive!=null && commissioniConsultive.size()>0) {
    		
    		for(int i=0; i<commissioniConsultive.size(); i++) {
    			
    			if(i!=0){
    				listaCommissioniConsultiveDestString += "\r\r";
        		}
    			
    			listaCommissioniConsultiveDestString += "Al Signor Presidente\rdella Commissione consiliare "+getCommissioneNumber(commissioniConsultive.get(i));
    			listaCommissioniConsultiveInvitiString += getCommissioneNumber(commissioniConsultive.get(i));
    			
    			if(i<commissioniConsultive.size()-1){
    				listaCommissioniConsultiveInvitiString += ", ";
    			}
        	}	
    	}
    	
    	searchTerms.put("<listaCommissioniConsultiveDest>", listaCommissioniConsultiveDestString);
    	searchTerms.put("<listaCommissioniConsultiveInviti>", listaCommissioniConsultiveInvitiString); 
    	String listaOrganismiStatutariDestString = StringUtils.EMPTY;	
    	String listaOrganismiStatutariInvitiString = StringUtils.EMPTY; 
    	if(organismiStatutari!=null && organismiStatutari.size()>0) {
    		
    		for(int i=0; i<organismiStatutari.size(); i++) {
        		
    			if(i!=0){
    				listaOrganismiStatutariDestString += "\r\r";
        		}
    			
    			listaOrganismiStatutariDestString += "Al Signor Presidente\rdel "+organismiStatutari.get(i);
    			listaOrganismiStatutariInvitiString += organismiStatutari.get(i);
    			
    			if(i<organismiStatutari.size()-1){
    				listaOrganismiStatutariInvitiString += ", ";
    			}
    	
    		}
    	}
    	
    	searchTerms.put("<listaOrganismiStatutariDest>", listaOrganismiStatutariDestString);
    	searchTerms.put("<listaOrganismiStatutariInviti>", listaOrganismiStatutariInvitiString);
    	
    	
    	List<NodeRef> listaCommissioniPrincipaliNodeRef = attoUtil.getCommissioniPrincipali(attoNodeRef);
    	
    	if(listaCommissioniPrincipaliNodeRef.size() > 0){
    		Date dataAssegnazioneCommissionePrincipale = (Date) nodeService.getProperty(listaCommissioniPrincipaliNodeRef.get(0), QName.createQName(AttoUtil.CRL_ATTI_MODEL, AttoUtil.PROP_DATA_ASSEGNAZIONE_COMMISSIONE));

    		if(dataAssegnazioneCommissionePrincipale!=null){
    			SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yy", Locale.ITALY);
            	String dataAssegnazioneCommissionePrincipaleString = formatter.format(dataAssegnazioneCommissionePrincipale);
            	
            	searchTerms.put("<dataAssegnazioneCommissioneRef>", dataAssegnazioneCommissionePrincipaleString);
    		}
        
        	
    	}

        	
    	NodeRef ultimoOrganoStatutario = getLastOrganoStatutarioAssegnato(attoNodeRef);
    	if(ultimoOrganoStatutario!=null) {
    		String ultimoOrganoStatutarioString = (String) nodeService.getProperty(ultimoOrganoStatutario, ContentModel.PROP_NAME);
    		searchTerms.put("<ultimoOrganoStatutario>", ultimoOrganoStatutarioString);
    	} 
		documentFilledByteArray = TemplateFiller.searchAndReplace(templateByteArray, searchTerms);
		
		logger.info("Generazione della lettera completata - template: "+nomeTemplate);
		
		return documentFilledByteArray;
	}
	
	
	private String getCommissioneNumber(String nomeCommissione){
		
		return nomeCommissione.split(" ")[0];
	}
	
}
