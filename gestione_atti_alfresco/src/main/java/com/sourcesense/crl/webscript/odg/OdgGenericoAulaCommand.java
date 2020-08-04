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
package com.sourcesense.crl.webscript.odg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.dictionary.TypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.DynamicNamespacePrefixResolver;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * Genera i documenti che saranno i report di controllo per la Regione Lombardia. 
 * Il contenuto dei report cambia in base agli atti che si vogliono trattare
 * @author sourcesense
 *
 */
public class OdgGenericoAulaCommand extends OdgBaseCommand {

	private static Log logger = LogFactory.getLog(OdgGenericoAulaCommand.class);

	/**
	 * Gerera il documento ORDINE DEL GIORNO utilizzando un template
	 * @param templateByteArray il template in binario
	 * @param templateNodeRef nodo di Alfresco che punta dove si trova il template
	 * @param sedutaNodeRef nodo di Alfresco che contiene la seduta
	 * @param gruppo  gruppo parlamentare
	 * @return  il binario del documento che contiene l'ordine del giorno
	 */
	public byte[] generate(byte[] templateByteArray, NodeRef templateNodeRef, NodeRef sedutaNodeRef, String gruppo)
			throws IOException {

		byte[] documentFilledByteArray = null;
		HashMap<String, String> searchTerms = new HashMap<String, String>();

		NodeRef legislatura = attoUtil.getLegislaturaCorrente();

		if (legislatura != null) {
			String legislaturaString = (String) nodeService.getProperty(legislatura, ContentModel.PROP_NAME);
			searchTerms.put("numeroLegislatura", legislaturaString + "\rLEGISLATURA");
		}

		Date dataSeduta = (Date) nodeService.getProperty(sedutaNodeRef,
				QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_DATA_SEDUTA));

		if (dataSeduta != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ITALY);
			String dataSedutaString = formatter.format(dataSeduta);
			searchTerms.put("dataSeduta", dataSedutaString.toUpperCase());
		}

		String orarioInzioSeduta = (String) nodeService.getProperty(sedutaNodeRef,
				QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_ORARIO_INIZIO_SEDUTA));

		if (orarioInzioSeduta != null && !orarioInzioSeduta.equals("")) {
			searchTerms.put("orarioInizio", orarioInzioSeduta.substring(11, 16));
		}

		String orarioFineSeduta = (String) nodeService.getProperty(sedutaNodeRef,
				QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_ORARIO_FINE_SEDUTA));

		if (orarioFineSeduta != null && !orarioFineSeduta.equals("")) {
			searchTerms.put("orarioFine", orarioFineSeduta.substring(11, 16));
		}

		List<NodeRef> attiTrattati = getAttiTrattati(sedutaNodeRef);
		List<NodeRef> attiIndirizzoTrattati = getAttiIndirizzoTrattati(sedutaNodeRef);
		documentFilledByteArray = searchAndReplaceDocx(templateByteArray, searchTerms);
		documentFilledByteArray = createAttiTrattatiRowsAulaDocx(documentFilledByteArray, attiTrattati,
				attiIndirizzoTrattati);
		documentFilledByteArray = fillAttiTrattatiRowsAulaDocx(documentFilledByteArray, attiTrattati,
				attiIndirizzoTrattati);

		logger.info("Generazione del documento odg completato");

		return documentFilledByteArray;
	}

	/**
	 * Modifica il documento documentByteArray inserendo le righe dentro le tabelle del documento con il contenuto: attiTrattati e  attiIndirizzoTrattati
	 * @param documentByteArray documento in binario
	 * @param attiTrattati gli atti trattati che saranno inseriti nelle tabelle del doc.
	 * @param attiIndirizzoTrattati gli atti attiIndirizzoTrattati che saranno inseriti nelle tabelle del doc.
	 * @return il nuovo documento aggiornato e in binario
	 * @throws IOException se ci sono problemi aprendo il file oppure modificando il suo contenuto
	 */
	private byte[] createAttiTrattatiRowsAulaDocx(byte[] documentByteArray, List<NodeRef> attiTrattati,
			List<NodeRef> attiIndirizzoTrattati) throws IOException {

		XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(documentByteArray));

		List<XWPFTable> tables = document.getTables();

		XWPFTable table = tables.get(1);

		XWPFTableRow templateAttoTrattatoRow = table.getRow(3);
		XWPFTableRow templateAttoIndirizzoTrattatoRow = table.getRow(4);

		for (int i = 0; i < attiTrattati.size(); i++) {
			table.addRow(templateAttoTrattatoRow, 3);
		}

		for (int i = 0; i < attiIndirizzoTrattati.size(); i++) {
			table.addRow(templateAttoIndirizzoTrattatoRow, 4 + attiTrattati.size());
		}

		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		document.write(ostream);
		return ostream.toByteArray();

	}

	/**
	 * Metodo più importante: genera il documento con tutta l'informazione degli atti. Interroga Alfresco per ricavare i dati 
	 * che poi serviranno per sovrascrivere i parametri dentro il template da riempire. Per gli attiTrattati si inseriscono nel documento:  
	 * titoloAtto, oggettoAtto, commissioneReferente, relatoreCommissione (nel caso in cui esista). Per gli attiIndirizzoTrattati invece:
	 * titoloAtto, oggettoAtto, firmatariAttoIndirizzo.
	 * 
	 * @param documentByteArray template da completare
	 * @param attiTrattati primo insieme di atti che bisogna inserire nel documento.
	 * @param attiIndirizzoTrattati secondo insieme di atti che bisogna inserire nel documento.
	 * @return contenuto binario del file template popolato con i dati degli atti.
	 * @throws IOException se esistono problemi con la lettura del template oppure la scrittura del nuovo documento.
	 */
	private byte[] fillAttiTrattatiRowsAulaDocx(byte[] documentByteArray, List<NodeRef> attiTrattati,
			List<NodeRef> attiIndirizzoTrattati) throws IOException {

		DictionaryService dictionaryService = serviceRegistry.getDictionaryService();

		XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(documentByteArray));

		List<XWPFTable> tables = document.getTables();

		XWPFTable table = tables.get(1);

		for (int i = 0; i < attiTrattati.size(); i++) {

			XWPFTableRow row = table.getRow(3 + i);

			NodeRef attoTrattato = attiTrattati.get(i);

			HashMap<String, String> searchTerms = new HashMap<String, String>();

			String nomeAttoTrattato = (String) nodeService.getProperty(attoTrattato, ContentModel.PROP_NAME);

			TypeDefinition typeDef = dictionaryService.getType(nodeService.getType(attoTrattato));
			String tipoAttoDescrizione = typeDef.getTitle().toUpperCase();

			String oggettoAtto = (String) nodeService.getProperty(attoTrattato,
					QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_OGGETTO_ATTO));

			searchTerms.put("titoloAtto", tipoAttoDescrizione + " N." + nomeAttoTrattato);
			searchTerms.put("oggettoAtto", oggettoAtto);

			List<NodeRef> commissioniRef = attoUtil.getCommissioniPrincipali(attoTrattato);

			if (commissioniRef.size() > 0) {
				String nomeCommissioneRel = (String) nodeService.getProperty(commissioniRef.get(0),
						ContentModel.PROP_NAME);

				searchTerms.put("commissioneReferente", nomeCommissioneRel);

				NodeRef relatore = attoUtil.getRelatoreCorrente(commissioniRef.get(0));

				if (relatore != null) {

					String nomeRelatore = "Relatore Cons. "
							+ (String) nodeService.getProperty(relatore, ContentModel.PROP_NAME);

					NodeRef aula = attoUtil.getAula(attoTrattato);

					String relazioneScritta = (String) nodeService.getProperty(aula,
							QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_RELAZIONE_SCRITTA_AULA));

					if (relazioneScritta != null && relazioneScritta.equals("Sì")) {
						nomeRelatore += " con relazione scritta";
					}

					searchTerms.put("relatoreCommissione", nomeRelatore);
				} else {
					searchTerms.put("relatoreCommissione", "");
				}

			}

			searchAndReplaceParagraph(row.getCell(1), searchTerms);

		}
		int rowsAttiTrattatiNumber = attiTrattati.size() + 3;
		table.removeRow(rowsAttiTrattatiNumber);

		for (int i = 0; i < attiIndirizzoTrattati.size(); i++) {

			XWPFTableRow row = table.getRow(3 + attiTrattati.size() + i);

			NodeRef attoTrattato = attiIndirizzoTrattati.get(i);

			HashMap<String, String> searchTerms = new HashMap<String, String>();

			String numeroAttoTrattato = (String) nodeService.getProperty(attoTrattato,
					QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_NUMERO_ATTO_INDIRIZZO));
			String tipoAttoTrattato = (String) nodeService.getProperty(attoTrattato,
					QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_TIPO_ATTO_INDIRIZZO));
			String oggettoAttoTrattato = (String) nodeService.getProperty(attoTrattato,
					QName.createQName(attoUtil.CRL_ATTI_MODEL, attoUtil.PROP_OGGETTO_ATTO_INDIRIZZO));

			searchTerms.put("titoloAtto", tipoAttoTrattato + " N." + numeroAttoTrattato);
			searchTerms.put("oggettoAtto", oggettoAttoTrattato);

			String firmatariAttoTrattato = "";

			DynamicNamespacePrefixResolver namespacePrefixResolver = new DynamicNamespacePrefixResolver(null);
			namespacePrefixResolver.registerNamespace(NamespaceService.SYSTEM_MODEL_PREFIX,
					NamespaceService.SYSTEM_MODEL_1_0_URI);
			namespacePrefixResolver.registerNamespace(NamespaceService.CONTENT_MODEL_PREFIX,
					NamespaceService.CONTENT_MODEL_1_0_URI);
			namespacePrefixResolver.registerNamespace(NamespaceService.APP_MODEL_PREFIX,
					NamespaceService.APP_MODEL_1_0_URI);

			String luceneFirmatariNodePath = nodeService.getPath(attoTrattato).toPrefixString(namespacePrefixResolver);

			String firmatarioType = "{" + attoUtil.CRL_ATTI_MODEL + "}" + "firmatarioAttoIndirizzo";

			ResultSet firmatariNodes = null;
			try {

				firmatariNodes = searchService.query(attoTrattato.getStoreRef(), SearchService.LANGUAGE_LUCENE,
						"PATH:\"" + luceneFirmatariNodePath + "/cm:Firmatari/*\" AND TYPE:\"" + firmatarioType + "\"");

				for (int j = 0; j < firmatariNodes.length(); j++) {
					firmatariAttoTrattato += (String) nodeService.getProperty(firmatariNodes.getNodeRef(j),
							ContentModel.PROP_NAME);
					if (j < firmatariNodes.length() - 1) {
						firmatariAttoTrattato += ", ";
					}
				}
			} finally {
				if (firmatariNodes != null) {
					firmatariNodes.close();
				}
			}

			searchTerms.put("firmatariAttoIndirizzo", firmatariAttoTrattato);

			searchAndReplaceParagraph(row.getCell(1), searchTerms);

		}
		int rowsAttiIndirizzoTrattatiNumber = attiTrattati.size() + attiIndirizzoTrattati.size() + 3;
		table.removeRow(rowsAttiIndirizzoTrattatiNumber);

		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		document.write(ostream);
		return ostream.toByteArray();

	}

}
