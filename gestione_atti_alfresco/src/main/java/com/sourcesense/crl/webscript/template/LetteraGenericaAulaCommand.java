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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Commando che si occupa di generare una Lettera generica di tipo Aula.
 */
public class LetteraGenericaAulaCommand extends LetteraBaseCommand {

    private static Log logger = LogFactory.getLog(LetteraGenericaAulaCommand.class);

    /**
     * {@inheritDoc}
     * @return ritorna un documento  lettera generica aula .
     */
    public byte[] generate(byte[] templateByteArray, NodeRef templateNodeRef, NodeRef attoNodeRef, String gruppo) throws IOException {

        byte[] documentFilledByteArray = null;


        QName templateType = nodeService.getType(templateNodeRef);
        String nomeTemplate = templateType.getLocalName();

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ITALY); 
        HashMap<String, String> searchTerms = new HashMap<String, String>(); 
        HashMap<String, String> searchTermsFooter = new HashMap<String, String>(); 
        String firmatario = (String) nodeService.getProperty(templateNodeRef, QName.createQName(attoUtil.CRL_TEMPLATE_MODEL, PROP_FIRMATARIO));
        searchTerms.put("<firmatario>", firmatario);

        String ufficio = (String) nodeService.getProperty(templateNodeRef, QName.createQName(attoUtil.CRL_TEMPLATE_MODEL, PROP_UFFICIO));
        searchTerms.put("<ufficio>", ufficio);

        String assessore = (String) nodeService.getProperty(templateNodeRef, QName.createQName(attoUtil.CRL_TEMPLATE_MODEL, PROP_ASSESSORE));
        searchTerms.put("<assessore>", assessore);
        
        String direzione = (String) nodeService.getProperty(templateNodeRef, QName.createQName(attoUtil.CRL_TEMPLATE_MODEL, PROP_DIREZIONE));
        searchTerms.put("<direzione>", direzione);

        String numeroTelFirmatario = (String) nodeService.getProperty(templateNodeRef, QName.createQName(attoUtil.CRL_TEMPLATE_MODEL, PROP_NUMEROTELFIRMATARIO));
        searchTermsFooter.put("<numeroTelFirmatario>", numeroTelFirmatario);

        String emailFirmatario = (String) nodeService.getProperty(templateNodeRef, QName.createQName(attoUtil.CRL_TEMPLATE_MODEL, PROP_EMAILFIRMATARIO));
        searchTermsFooter.put("<emailFirmatario>", emailFirmatario); 
        String numeroAtto = ((Integer) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_NUM_ATTO)))
                + ((String) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_ESTENSIONE_ATTO)));
        String oggettoAtto = (String) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_OGGETTO_ATTO));

        String legislatura = (String) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_LEGISLATURA));

        String numeroLCR = (String) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_NUMERO_LCR_ATTO));
        String numeroDCR = (String) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_NUMERO_DCR));
        Date dataVotazioneAula = (Date) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_DATA_SEDUTA_AULA_ATTO));


        if (dataVotazioneAula != null) {

            String dataVotazioneAulaString = formatter.format(dataVotazioneAula);
            searchTerms.put("<dataLCR>", dataVotazioneAulaString.toUpperCase());
            searchTerms.put("<dataVotazione>", dataVotazioneAulaString);
        }


        String dataOdiernaString = formatter.format(new Date());
        searchTerms.put("<dataOdierna>", dataOdiernaString); 
      	String tipoAttoSigla= getTipoAttoSigla(attoNodeRef);
      	
        searchTerms.put("<tipoAttoSigla>", tipoAttoSigla);
        searchTerms.put("<numeroAtto>", numeroAtto);
        searchTerms.put("<oggettoAtto>", oggettoAtto);
        searchTerms.put("<numeroLCR>", numeroLCR);
        searchTerms.put("<numeroDCR>", numeroDCR); 

        List<String> commissioniReferenti = (List<String>) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_COMMISSIONI_REFERENTI));
        List<String> commissioniCoreferenti = (List<String>) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_COMMISSIONE_COREFERENTE));
        String commissioneRedigente = (String) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_COMMISSIONE_REDIGENTE));
        String commissioneDeliberante = (String) nodeService.getProperty(attoNodeRef, QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_COMMISSIONE_DELIBERANTE));


        List<String> listaCommissioniPrincipali = new ArrayList<String>(); 
        if (commissioniReferenti != null && commissioniReferenti.size() > 0) {

            listaCommissioniPrincipali.add(commissioniReferenti.get(0)); 
        } else { 
            if (commissioneRedigente != null && !commissioneRedigente.equals("")) {
                listaCommissioniPrincipali.add(commissioneRedigente); 
            } else {
                if (commissioneDeliberante != null && !commissioneDeliberante.equals("")) {
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

        if (listaCommissioniPrincipali.size() > 0) {
            String commissionePrincipale = listaCommissioniPrincipali.get(0);
            searchTerms.put("<commissioneReferente>", commissionePrincipale);
        } 
        String listaCommissioniPrincipaliDestString = "";

        for (int i = 0; i < listaCommissioniPrincipali.size(); i++) {

            if (i != 0) {
                listaCommissioniPrincipaliDestString += "\r\r";
            }
            listaCommissioniPrincipaliDestString += "Al Signor Presidente\rdella " + getCommissioneNumber(listaCommissioniPrincipali.get(i)) + " Commissione consiliare";
        }

        searchTerms.put("<listaCommissioniPrincipaliDest>", listaCommissioniPrincipaliDestString);



        List<NodeRef> listaCommissioniPrincipaliNodeRef = attoUtil.getCommissioniPrincipali(attoNodeRef);

        if (listaCommissioniPrincipaliNodeRef.size() > 0) {
            Date dataAssegnazioneCommissionePrincipale = (Date) nodeService.getProperty(listaCommissioniPrincipaliNodeRef.get(0), QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_DATA_ASSEGNAZIONE_COMMISSIONE));

            String dataAssegnazioneCommissionePrincipaleString = formatter.format(dataAssegnazioneCommissionePrincipale);

            searchTerms.put("<dataAssegnazioneCommissioneRef>", dataAssegnazioneCommissionePrincipaleString);

        }


        NodeRef ultimoOrganoStatutario = getLastOrganoStatutarioAssegnato(attoNodeRef);
        if (ultimoOrganoStatutario != null) {
            String ultimoOrganoStatutarioString = (String) nodeService.getProperty(ultimoOrganoStatutario, ContentModel.PROP_NAME);
            searchTerms.put("<ultimoOrganoStatutario>", ultimoOrganoStatutarioString);
        } 
        documentFilledByteArray = TemplateFiller.searchAndReplace(templateByteArray, searchTerms); 
        documentFilledByteArray = TemplateFiller.searchAndReplaceFooter(documentFilledByteArray, searchTermsFooter);

        logger.info("Generazione della lettera completata - template: " + nomeTemplate);

        return documentFilledByteArray;
    }

    private String getCommissioneNumber(String nomeCommissione) {

        return nomeCommissione.split(" ")[0];
    }
}
