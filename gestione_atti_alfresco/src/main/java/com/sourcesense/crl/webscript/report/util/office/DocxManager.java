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
package com.sourcesense.crl.webscript.report.util.office;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFactory;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;

import com.google.common.collect.LinkedListMultimap;
import com.sourcesense.crl.util.AttoUtil;
import com.sourcesense.crl.webscript.report.util.lucene.LuceneMockDocument;
import com.sun.star.beans.Pair;

/**
 * This class represents a Docx Manager to replicate and fill simple document
 * with table in input
 * 
 * @author Alessandro Benedetti
 * 
 */
public class DocxManager {
	private static Log logger = LogFactory.getLog(DocxManager.class);

	private XWPFDocument document;
	private NodeService nodeService;
	private SearchService searchService;
	protected static Map<String, String> tipoIniziativaDecode = new HashMap<String, String>();
	protected final static String DATA_FORMAT = "dd/MM/yyyy";

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	private static final String QUERY_REPLACER = "REP";

	private static final QName PROP_CODICE_GRUPPO = QName.createQName(
			AttoUtil.CRL_ATTI_MODEL, "codiceGruppoConsigliereAnagrafica");

	private static final String GRUPPO_FIRMATARIO_QUERY_ATTIVI = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriAttivi//*\" "
			+ "AND TYPE:\"crlatti:consigliereAnagrafica\" AND @cm\\:name:\""
			+ QUERY_REPLACER + "\"";

	private static final String GRUPPO_FIRMATARIO_QUERY_STORICI = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:ConsiglieriStorici//*\" "
			+ "AND TYPE:\"crlatti:consigliereAnagrafica\" AND @cm\\:name:\""
			+ QUERY_REPLACER + "\"";

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public DocxManager(InputStream is) throws IOException {
		this.document = new XWPFDocument(is);
	}

	/**
	 * Extracts the table from the input Docx ( template) and copy the table n
	 * times ( n is the size of luceneDocs in input)
	 * 
	 * @param breakBetween
	 *            TODO
	 * @param luceneDocs
	 * 
	 * @return
	 */
	public XWPFDocument generateFromTemplateMap(
			Map<String, Integer> group2count, int kpage, boolean breakBetween) {
		XWPFDocument newDoc = new XWPFDocument();
		boolean newPage = false;// new page on title
		List<XWPFTable> tables = document.getTables();
		XWPFTable tableExt = tables.get(0);// template table
		int k = 0;
		/* Copy template paragraphs */
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		int parCounter = 0;
		for (int i = 0; i < paragraphs.size(); i++) {
			XWPFParagraph currentPar = paragraphs.get(i); 
			newDoc.createParagraph();
			newDoc.setParagraph(currentPar, parCounter);
			parCounter++;// }
		}
		for (String key : group2count.keySet()) {
			if (group2count.get(key) > 0) {
				/* create key title */
				XWPFParagraph keyTitle = newDoc.createParagraph();
				XWPFRun keyRun = keyTitle.createRun();
				keyRun.setText(key.trim());
				if (k % kpage == 0 && k != 0) {
					keyTitle.setPageBreak(true);
					newPage = true;
				}
			}
			for (int j = 0; j < group2count.get(key); j++) {
				XWPFParagraph createParagraph = newDoc.createParagraph();
				if ((k % kpage == 0 && k != 0) && (!newPage)) {
					createParagraph.setPageBreak(true);

				}
				if (k % kpage == 0 && k != 0) {
					XWPFRun run = createParagraph.createRun();
					run.addBreak();
				}
				newDoc.createTable();
				newDoc.setTable(k, tableExt); 
				k++;
				newPage = false;
			}
			XWPFParagraph breakPar = newDoc.createParagraph();
			XWPFRun run = breakPar.createRun();
			run.addBreak();
		}
		return newDoc;
	}

	/**
	 * Extracts the table from the input Docx ( template) and copy the table n
	 * times ( n is the size of luceneDocs in input)
	 * 
	 * @param breakBetween
	 *            TODO
	 * @param luceneDocs
	 * 
	 * @return
	 */
	public XWPFDocument generateFromTemplateMapAssCommissioni( Map<String,Integer> group2count, int kpage,
			boolean breakBetween) {
		XWPFDocument newDoc = new XWPFDocument();
		boolean newPage = false;// new page on title
		List<XWPFTable> tables = document.getTables();
		XWPFTable tableExt = tables.get(0);//template table
		int k = 0;
		/*Copy template paragraphs*/
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		int parCounter=0;
		for (int i = 0; i < paragraphs.size(); i++) {
			XWPFParagraph currentPar = paragraphs.get(i); 
			newDoc.createParagraph();
			newDoc.setParagraph(currentPar, parCounter);
			parCounter++;// }
		}
		for (String key : group2count.keySet()) {
			if ( group2count.get(key)>0) {
				/*create key title*/
				XWPFParagraph keyTitle = newDoc.createParagraph();
				
				if (k % kpage == 0 && k != 0) {
					keyTitle.setPageBreak(true);
					newPage = true;
				}
			}
			for (int j = 0; j < group2count.get(key); j++) {
				XWPFParagraph createParagraph = newDoc.createParagraph();
				if ((k % kpage == 0 && k!=0) && (!newPage)) {
					createParagraph.setPageBreak(true);
					
				}
				if (k % kpage == 0 && k!=0) {
					XWPFRun run = createParagraph.createRun();
					run.addBreak();
				}
				newDoc.createTable();
				newDoc.setTable(k, tableExt);
				
				k++;
				newPage = false;
			}
			XWPFParagraph breakPar = newDoc.createParagraph();
			XWPFRun run = breakPar.createRun();
			run.addBreak();
		}
		return newDoc;
	}
	
	protected void sortAttiPerConferenze(List<NodeRef> nodeRefList) {

		Collections.sort(nodeRefList, new Comparator<NodeRef>() {
			public int compare(NodeRef node1, NodeRef node2) {

				String tipoAtto1 = (String) nodeService.getType(node1)
						.getLocalName();
				String tipoAtto2 = (String) nodeService.getType(node2)
						.getLocalName();

				return tipoAtto1.compareTo(tipoAtto2);

			}
		});

	}

	/**
	 * Extracts the table from the input Docx ( template) and copy the table n
	 * times ( n is the size of luceneDocs in input)
	 * 
	 * @param breakBetween
	 *            TODO
	 * @param luceneDocs
	 * 
	 * @return
	 */
	@Deprecated
	public XWPFDocument generateFromTemplateMapConferenza(
			Map<String,Integer> group2count, 
			LinkedListMultimap<String, NodeRef> commissione2atti,
			int kpage,
			Map<NodeRef, NodeRef> atto2commissione) {
		XWPFDocument newDoc = new XWPFDocument();
		boolean newPage = false;// new page on title
		List<XWPFTable> tables = document.getTables();
		XWPFTable tableExt = tables.get(0);//template table
		int k = 0;
		/*Copy template paragraphs*/
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		int parCounter=0;
		for (int i = 0; i < paragraphs.size(); i++) {
			XWPFParagraph currentPar = paragraphs.get(i);
			
			newDoc.createParagraph();
			newDoc.setParagraph(currentPar, parCounter);
			parCounter++;// }
		}
		String tipoAtto = StringUtils.EMPTY;
		Boolean tipoAttoChanged = new Boolean(false);
		
		for (String key : group2count.keySet()) { 
			List<NodeRef> listaAtti = commissione2atti.get(key);
			sortAttiPerConferenze(listaAtti);
			
			if ( group2count.get(key)>0) {
				/*create key title con commissione e tipologia di atti*/
				
				XWPFParagraph keyTitle = newDoc.createParagraph();
				XWPFRun keyRun = keyTitle.createRun();
				
				if ((k % kpage == 0 && k != 0)
						|| tipoAttoChanged) {
					keyTitle.setPageBreak(true);
					newPage = true;
				}
			}
			
			
			for (int j = 0; j < group2count.get(key); j++) {
				XWPFParagraph createParagraph = newDoc.createParagraph();
				if ((k % kpage == 0 && k!=0) && (!newPage)) {
					createParagraph.setPageBreak(true);
					
				}
				if (k % kpage == 0 && k!=0) {
					XWPFRun run = createParagraph.createRun();
					run.addBreak();
				}
				
				NodeRef attoCorrenteNodeRef = listaAtti.get(j);
				NodeRef commissioneNodeRef = atto2commissione.get(attoCorrenteNodeRef);
				String tipoAttoCorrente = (String) nodeService.getProperty(commissioneNodeRef, AttoUtil.PROP_TIPO_ATTO_COMM);
				String tipoAttoDescrizioneCorrente = getTipoAttoDescrizioneFromTipoAtto(tipoAttoCorrente);
				
				String titolo = StringUtils.EMPTY;
				String commissioneTitolo = key.trim();

				if(!tipoAttoDescrizioneCorrente.equals(tipoAtto)){
					XWPFParagraph keyTitle = newDoc.createParagraph();
					XWPFRun keyRun = keyTitle.createRun();
					titolo = tipoAttoDescrizioneCorrente + " - " + commissioneTitolo;
					keyRun.setText(titolo);
					keyRun.setBold(true);
					keyRun.setFontFamily("Cambria");
					keyRun.setFontSize(16);
					keyRun.setItalic(true);
					keyTitle.setPageBreak(true);
					
				}

				XWPFTable currentTable = new XWPFTable(tableExt.getCTTbl(),tableExt.getBody());
				newDoc.createTable();
				newDoc.setTable(k, currentTable);
				
				k++;
				newPage = true;
				
				tipoAtto = tipoAttoDescrizioneCorrente;
				
				
			}
			XWPFParagraph breakPar = newDoc.createParagraph();
			XWPFRun run = breakPar.createRun();
			run.addBreak();
		}
		return newDoc;

	}
	public XWPFDocument generateFromTemplateMapConferenza2(
			Map<String, Integer> group2count,
			LinkedListMultimap<String, NodeRef> commissione2atti, int kpage,
			Map<NodeRef, NodeRef> atto2commissione) {
		XWPFDocument newDoc = new XWPFDocument();
		int contatoreHeader=0;
		
		List<XWPFTable> tables = document.getTables();
		XWPFTable tableExt = tables.get(0);// template table
		int contaTabelleAtti = 0;
		/* Copy template paragraphs */
		List<XWPFParagraph> paragraphs = document.getParagraphs();
	
		int parCounter = 0;
		for (int i = 0; i < paragraphs.size(); i++) {
			XWPFParagraph currentPar = paragraphs.get(i);

			newDoc.createParagraph();
			newDoc.setParagraph(currentPar, parCounter);
			parCounter++;
		} 
		
		String titolo = StringUtils.EMPTY;
		
		for (String commissioneCorrente : group2count.keySet()) { 
			List<NodeRef> listaAttiCommissione = commissione2atti.get(commissioneCorrente);
			sortAttiPerConferenze(listaAttiCommissione);

			for (NodeRef attoCorrenteNodeRef :listaAttiCommissione) {

				NodeRef commissioneNodeRef = atto2commissione
						.get(attoCorrenteNodeRef);
				String tipoAttoCorrente = (String) nodeService.getProperty(
						commissioneNodeRef, AttoUtil.PROP_TIPO_ATTO_COMM);
				String tipoAttoDescrizioneCorrente = getTipoAttoDescrizioneFromTipoAtto(tipoAttoCorrente);

				String titoloCorrente=tipoAttoDescrizioneCorrente + " - " + commissioneCorrente.trim();
				if(!titoloCorrente.equals(titolo)){ 
					createNewSection(newDoc.createParagraph(), titolo, contatoreHeader, newDoc,false);
					contatoreHeader++;
					titolo=titoloCorrente;
				}         
				XWPFTable currentTable = new XWPFTable(tableExt.getCTTbl(),
						tableExt.getBody());
				newDoc.createTable();
				newDoc.setTable(contaTabelleAtti, currentTable);

				contaTabelleAtti++;         
					newDoc.createParagraph().createRun(); 

			}
			
		} 
		createNewSection(null, titolo, contatoreHeader, newDoc, true);
		newDoc.removeBodyElement(newDoc.getBodyElements().size()-1);
		return newDoc;

	}

	private static void createHeader(XWPFDocument doc, CTSectPr sectPr,
			String headerValue, int id) {
		XWPFHeader header = (XWPFHeader) doc.createRelationship(
				XWPFRelation.HEADER, XWPFFactory.getInstance(), id);  
		CTHdrFtrRef headerReference = sectPr.addNewHeaderReference(); 
		headerReference.setId(header.getPackageRelationship().getId());
		headerReference.setType(STHdrFtr.DEFAULT);
		
		CTP ctP1 = CTP.Factory.newInstance();
		
		XWPFParagraph paragraph= new XWPFParagraph(ctP1, doc);
		XWPFRun keyRun = paragraph.createRun();

		keyRun.setBold(true);
		keyRun.setFontFamily("Cambria");
		keyRun.setFontSize(16);
		keyRun.setItalic(true);
		keyRun.setText(headerValue);
		paragraph.setAlignment(ParagraphAlignment.RIGHT);
		
		header._getHdrFtr().setPArray(new CTP[]{paragraph.getCTP()});
	}

	private static CTSectPr createNewSection(XWPFParagraph par1, String titolo, int id, XWPFDocument doc, boolean lastSection) {
		

		CTSectPr sectPr;
		if(lastSection){
			sectPr=doc.getDocument().getBody().addNewSectPr();
		}
		else{
			CTPPr ppr = par1.getCTP().addNewPPr();
			sectPr = ppr.addNewSectPr();
		}     
		
		sectPr.addNewCols().setSpace(new BigInteger("708"));
		CTPageSz pgSz = sectPr.addNewPgSz();
		pgSz.setW(new BigInteger("11900"));
		pgSz.setH(new BigInteger("16840"));
		CTPageMar pgMar = sectPr.addNewPgMar();
		pgMar.setTop(new BigInteger("1417"));
		pgMar.setRight(new BigInteger("1134"));
		pgMar.setBottom(new BigInteger("1134"));
		pgMar.setLeft(new BigInteger("1134"));
		pgMar.setHeader(new BigInteger("708"));
		pgMar.setFooter(new BigInteger("708"));
		pgMar.setGutter(new BigInteger("0"));
		
		createHeader(doc, sectPr, titolo, id);
		return sectPr;
	}

	/**
	 * returna specific attribute value in a properties map
	 * 
	 * @param properties
	 * @param attribute
	 * @return
	 */
	protected Serializable getNodeRefProperty(
			Map<QName, Serializable> properties, String attribute) {
		return properties.get(QName.createQName(AttoUtil.CRL_ATTI_MODEL,
				attribute));
	}

	protected String getGruppoConsiliare(String firmatario) {
		String gruppoConsiliare = StringUtils.EMPTY;
		String luceneQuery = GRUPPO_FIRMATARIO_QUERY_ATTIVI.replaceAll(
				QUERY_REPLACER, firmatario);
		ResultSet firmatarioAttiviResults=null;
		ResultSet firmatarioStoriciResults=null;
		try{
			firmatarioAttiviResults = searchService.query(
					StoreRef.STORE_REF_WORKSPACE_SPACESSTORE,
					SearchService.LANGUAGE_LUCENE, luceneQuery);
			NodeRef firmatarioNodeRef = null;
			if (firmatarioAttiviResults.length() > 0) { 
				firmatarioNodeRef = firmatarioAttiviResults.getNodeRef(0);
			} else { 
				luceneQuery = GRUPPO_FIRMATARIO_QUERY_STORICI.replaceAll(
						QUERY_REPLACER, firmatario);
				firmatarioStoriciResults = searchService.query(
						StoreRef.STORE_REF_WORKSPACE_SPACESSTORE,
						SearchService.LANGUAGE_LUCENE, luceneQuery);
				if (firmatarioStoriciResults.length() > 0) {
					firmatarioNodeRef = firmatarioStoriciResults.getNodeRef(0);
				}
			}
			if (firmatarioNodeRef != null) {
				gruppoConsiliare = (String) nodeService.getProperty(
						firmatarioNodeRef, PROP_CODICE_GRUPPO);
			}
		} finally{
			if (firmatarioAttiviResults!=null){
				firmatarioAttiviResults.close();
			}
			if (firmatarioStoriciResults!=null){
				firmatarioStoriciResults.close();
			}
		}
		return gruppoConsiliare;
	}

	/**
	 * Encode a list of values in a single String , comma separed
	 * 
	 * @param stringList
	 * @return
	 */
	protected String renderFirmatariConGruppoList(List<String> firmatari) {
		String encodedString = StringUtils.EMPTY;
		if (firmatari != null) {
			for (String firmatario : firmatari) {
				String codiceGruppoConsiliare = getGruppoConsiliare(firmatario);
				encodedString += firmatario + " (" + codiceGruppoConsiliare
						+ "), ";

			}
		}
		if (!encodedString.equals(StringUtils.EMPTY)) {
			encodedString = encodedString.substring(0,
					encodedString.length() - 2);
		}
		return encodedString;
	}

	/**
	 * Check for null text element
	 * 
	 * @param attribute
	 * @return
	 */
	protected String checkStringEmpty(String attribute) {
		if (attribute == null) {
			return StringUtils.EMPTY;
		} else {
			return attribute;
		}

	}

	private XWPFTable fillTableConferenzePerAtto(XWPFTable table, NodeRef atto,
			Map<NodeRef, NodeRef> atto2commissione) {

		Map<QName, Serializable> attoProperties = nodeService
				.getProperties(atto);
		Map<QName, Serializable> commissioneProperties = nodeService
				.getProperties(atto2commissione.get(atto));

		String numeroAtto = StringUtils.EMPTY
				+ (Integer) this.getNodeRefProperty(attoProperties,
						"numeroAtto");
		String iniziativa = (String) this.getNodeRefProperty(attoProperties,
				"tipoIniziativa");
		String oggetto = (String) this.getNodeRefProperty(attoProperties,
				"oggetto"); 
		String tipoAtto = (String) this.getNodeRefProperty(
				commissioneProperties, "tipoAttoCommissione");
		Date dateAssegnazioneCommissione = (Date) this.getNodeRefProperty(
				commissioneProperties, "dataAssegnazioneCommissione"); 
		@SuppressWarnings("unchecked")
		ArrayList<String> firmatariList = (ArrayList<String>) this
				.getNodeRefProperty(attoProperties, "firmatari");

		String firmatari = this.renderFirmatariConGruppoList(firmatariList);

		/* writing values in the table */
		table.getRow(0)
				.getCell(1)
				.setText(
						this.checkStringEmpty(tipoAtto.toUpperCase() + " "
								+ numeroAtto));
		table.getRow(1).getCell(1).setText(this.checkStringEmpty(oggetto));
		table.getRow(2)
				.getCell(1)
				.setText(
						this.checkStringEmpty(decodeTipoIniziativa(iniziativa)));
		table.getRow(3).getCell(1).setText(this.checkStringEmpty(firmatari));
		table.getRow(4).getCell(1)
				.setText(this.checkDateEmpty(dateAssegnazioneCommissione));

		return table;
	}

	/**
	 * Check for null dates and format the result
	 * 
	 * @param attributeDate
	 * @return
	 */
	protected String checkDateEmpty(Date attributeDate) {
		if (attributeDate == null) {
			return StringUtils.EMPTY;
		} else {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(DATA_FORMAT);
			return dateFormatter.format(attributeDate);
		}

	}

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

	private Pair getTipoAttoNonUsatoInPrecedenza(String keyCommissione,
			String tipoAttoAttuale,
			LinkedListMultimap<String, NodeRef> commissione2atti,
			Map<NodeRef, NodeRef> atto2commissione) {

		String tipoAttoDaScrivere = StringUtils.EMPTY;
		List<NodeRef> attiCommissione = commissione2atti.get(keyCommissione);
		Boolean changed = new Boolean(false);
		for (NodeRef atto : attiCommissione) {
			NodeRef commissioneNodeRef = atto2commissione.get(atto);
			String tipoAttoCorrente = (String) nodeService.getProperty(
					commissioneNodeRef, AttoUtil.PROP_TIPO_ATTO_COMM);
			String tipoAttoDescrizioneCorrente = getTipoAttoDescrizioneFromTipoAtto(tipoAttoCorrente);
			if (!tipoAttoDescrizioneCorrente.equals(tipoAttoAttuale)) {
				tipoAttoDaScrivere = tipoAttoDescrizioneCorrente;
				changed = new Boolean(true);
			}
		}

		Pair tipoAttoSeDiversoPair = new Pair(tipoAttoDaScrivere, changed);
		return tipoAttoSeDiversoPair;
	}

	private String getTipoAttoDescrizioneFromTipoAtto(String tipoAtto) {
		String tipoAttoDescrizione = StringUtils.EMPTY;
		if ("Pdl".equals(tipoAtto)) {
			tipoAttoDescrizione = "Progetti di legge";
		} else if ("Doc".equals(tipoAtto)) {
			tipoAttoDescrizione = "Documenti generici";
		} else if ("Inp".equals(tipoAtto)) {
			tipoAttoDescrizione = "Petizioni popolari";
		} else if ("Par".equals(tipoAtto)) {
			tipoAttoDescrizione = "Pareri";
		} else if ("Pda".equals(tipoAtto)) {
			tipoAttoDescrizione = "Proposte atti amministrativi";
		} else if ("Plp".equals(tipoAtto)) {
			tipoAttoDescrizione = "Proposte leggi parlamentari";
		} else if ("Pre".equals(tipoAtto)) {
			tipoAttoDescrizione = "Proposte di regolamento";
		} else if ("Org".equals(tipoAtto)) {
			tipoAttoDescrizione = "Atti organizzazione interna";
		} else if ("Ref".equals(tipoAtto)) {
			tipoAttoDescrizione = "Proposte di referendum";
		} else if ("Rel".equals(tipoAtto)) {
			tipoAttoDescrizione = "Relazioni";
		} else if ("Eac".equals(tipoAtto)) {
			tipoAttoDescrizione = "Elenchi di atti comunitari";
		} else if ("Mis".equals(tipoAtto)) {
			tipoAttoDescrizione = "Missioni valutative";
		}
		return tipoAttoDescrizione;
	}

	/**
	 * fill the empty template with the input lucene documents
	 * 
	 * @param luceneDocs
	 * @return
	 */
	public XWPFDocument fillTemplate(List<LuceneMockDocument> luceneDocs,
			boolean onlyValues) {
		List<XWPFTable> tables = document.getTables();
		int tablesNumber = tables.size();
		for (int k = 0; k < tablesNumber; k++) {
			XWPFTable newTable = tables.get(k);
			int numberOfRows = newTable.getNumberOfRows();
			LuceneMockDocument luceneMockDocument = luceneDocs.get(k);
			List<String> fields = luceneMockDocument.getFields();
			List<String> values = luceneMockDocument.getValues();
			XWPFTableRow currentRow;
			String field;
			String value;

			for (int i = 0; i < numberOfRows; i++) {
				field = fields.get(i);
				value = values.get(i);
				currentRow = newTable.getRow(i);
				if (!onlyValues)
					currentRow.getCell(0).setText(field);
				if (currentRow != null && currentRow.getCell(1) != null)
					currentRow.getCell(1).setText(value);
			}
		}
		return document;
	}

	public void insertListInCell(XWPFTableCell currentCell,
			List<String> myStrings) {
		XWPFParagraph para = currentCell.getParagraphs().get(0);
		if (myStrings != null)
			for (String text : myStrings) {
				XWPFRun run = para.createRun();
				run.setText(text.trim());
				run.addBreak();
			}
	}

	public XWPFDocument generateFromTemplate(int n, int kpage,
			boolean breakBetween) {
		List<XWPFTable> tables = document.getTables();
		XWPFTable tableExt = tables.get(0);
		for (int k = 1; k < n; k++) {
			XWPFParagraph createParagraph = document.createParagraph();

			if (k % kpage == 0)
				createParagraph.setPageBreak(true);

			if (breakBetween) {
				XWPFRun run = createParagraph.createRun();
				run.addBreak();
			}
			XWPFTable currentTable = document.createTable();
			currentTable = tableExt;
			int initialTablePos = document.getTablePos(0);
			document.setTable(initialTablePos + (k + 1), currentTable);
		}

		return document;
	}

}
