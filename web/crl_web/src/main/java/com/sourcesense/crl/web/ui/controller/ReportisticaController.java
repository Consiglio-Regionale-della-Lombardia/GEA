/* 
 * Copyright (C) 2019 Consiglio Regionale della Lombardia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sourcesense.crl.web.ui.controller;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import com.sourcesense.crl.business.model.Report;
import com.sourcesense.crl.business.service.CommissioneServiceManager;
import com.sourcesense.crl.business.service.LegislaturaServiceManager;
import com.sourcesense.crl.business.service.OrganismoStatutarioServiceManager;
import com.sourcesense.crl.business.service.PersonaleServiceManager;
import com.sourcesense.crl.business.service.ReportServiceManager;
import com.sourcesense.crl.business.service.TipoAttoServiceManager;

/**
 * Crea il report visualizzato nelle pagine web
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "reportisticaController")
@ViewScoped
public class ReportisticaController implements Serializable {

	@ManagedProperty(value = "#{tipoAttoServiceManager}")
	private TipoAttoServiceManager tipoAttoServiceManager;

	@ManagedProperty(value = "#{personaleServiceManager}")
	private PersonaleServiceManager personaleServiceManager;

	@ManagedProperty(value = "#{commissioneServiceManager}")
	private CommissioneServiceManager commissioneServiceManager;

	@ManagedProperty(value = "#{organismoStatutarioServiceManager}")
	private OrganismoStatutarioServiceManager organismoStatutarioServiceManager;

	@ManagedProperty(value = "#{reportServiceManager}")
	private ReportServiceManager reportServiceManager;

	@ManagedProperty(value = "#{legislaturaServiceManager}")
	private LegislaturaServiceManager legislaturaServiceManager;

	private List<String> legislature = new ArrayList<String>();

	private TreeNode root;
	private TreeNode selectedNode;
	private StreamedContent file;
	private Report selectedReport = new Report();

	private List<String> tipiAtto;
	private Map<String, String> tipiAttoSel = new HashMap<String, String>();
	private List<String> commissioni;
	private List<String> commissioniSel = new ArrayList<String>();
	private String ruoloCommissione;
	private Date dataAssegnazioneDa;
	private Date dataAssegnazioneA;
	private Date dataVotazioneCommReferenteDa;
	private Date dataVotazioneCommReferenteA;
	private Date dataRitiroDa;
	private Date dataRitiroA;
	private Date dataNominaRelatoreDa;
	private Date dataNominaRelatoreA;
	private List<String> relatori = new ArrayList<String>();
	private List<String> relatoriSelected = new ArrayList<String>();
	private String legislatura;
	private Map<String, String> organismi = new HashMap<String, String>();
	private String organismo;
	private Date dataAssegnazioneParereDa;
	private Date dataAssegnazioneParereA;
	private List<String> firmatari = new ArrayList<String>();
	private String firmatario;
	private String tipologiaFirma;
	private Date dataPresentazioneDa;
	private Date dataPresentazioneA;
	private Date dataAssegnazioneCommReferenteDa;
	private Date dataAssegnazioneCommReferenteA;
	private Date dataSedutaDa;
	private Date dataSedutaA;
	private Date dataConsultazioneDa;
	private Date dataConsultazioneA;
	private final String ATTI_ASSE_COMM = "Atti assegnati alle commissioni";
	private final String CONFERENZE = "Conferenza (atti in istruttoria)";
	private final String ATTI_LICENZ = "Atti licenziati";
	private final String ATTI_RITIRATI = "Atti ritirati e revocati";
	private final String ATTI_RELATORE = "Elenco atti per relatore";
	private final String COMPO_COMMISSIONI = "Composizione commissioni";
	private final String ATTI_INV_ORG_EST = "Atti inviati a organi esterni";
	private final String ATTI_INIZ_CONSILIARE = "Atti di iniziativa consiliare per consigliere";
	private final String ATTI_ISTRUTTORIA_COMM = "Atti in istruttoria alle commissioni per tipo Atto";
	private final String ATTI_ASSEGNATI_COMM = "Atti assegnati alle commissioni";
	private final String ELENCO_RELATORI = "Elenco dei relatori nominati in commissione per data nomina";
	private final String ATTI_LICENZIATI_COMM = "Atti licenziati dalle commissioni";
	private final String ELENCO_AUDIZ_COMM = "Elenco Audizioni delle commissioni";
	private final String ELENCO_LCR = "Elenco LCR";
	private final String ELENCO_DCR = "Elenco DCR";
	private final String ELENCO_ATTI_ISTRUTT = "Elenco Atti Istruttoria";
	private final String ELENCO_ATTI_RINVIO = "Elenco Atti Rinviati alle Commissioni";

	/**
	 * Aggiunge tutte le commissioni, organismi, firmatari, relatori. tipi atto e
	 * legislature nel contesto web
	 */
	@PostConstruct
	public void init() {
		setCommissioniSel(commissioneServiceManager.getAll());
		setOrganismi(organismoStatutarioServiceManager.findAll());
		setFirmatari(personaleServiceManager.getAllFirmatario());
		setRelatori(personaleServiceManager.getAllRelatore());
		setTipiAttoSel(tipoAttoServiceManager.findAll());
		setLegislature(legislaturaServiceManager.list());

		root = new DefaultTreeNode("Root", null);
		TreeNode servizioCommissioni = new DefaultTreeNode("Servizio commissioni", root);

		Report repAttiAsseComm = new Report(ATTI_ASSE_COMM, "crlreport:reportAttiAssCommissioniServComm");
		TreeNode nodeAttiAsseComm = new DefaultTreeNode("lettera", repAttiAsseComm, servizioCommissioni);

		Report repConf = new Report(CONFERENZE, "crlreport:reportConferenzeServComm");
		TreeNode nodeConf = new DefaultTreeNode("lettera", repConf, servizioCommissioni);

		Report repAttiLic = new Report(ATTI_LICENZ, "crlreport:reportAttiLicenziatiServComm");
		TreeNode nodeAttiLic = new DefaultTreeNode("lettera", repAttiLic, servizioCommissioni);

		Report repAttiRit = new Report(ATTI_RITIRATI, "crlreport:reportAttiRitiratiRevocatiServComm");
		TreeNode nodeAttiRit = new DefaultTreeNode("lettera", repAttiRit, servizioCommissioni);

		Report repAttiRel = new Report(ATTI_RELATORE, "crlreport:reportAttiRelatoreServComm");
		TreeNode nodeAttiRel = new DefaultTreeNode("lettera", repAttiRel, servizioCommissioni);

		Report rep = new Report(COMPO_COMMISSIONI, "crlreport:reportComposizioneCommissioniServComm");
		TreeNode nodeLetteraTrasmissioneCommissioneReferente = new DefaultTreeNode("lettera", rep, servizioCommissioni);

		Report repAttiInvOrg = new Report(ATTI_INV_ORG_EST, "crlreport:reportAttiInviatiOrganiEsterniServComm");
		TreeNode nodeAttiInvOrg = new DefaultTreeNode("lettera", repAttiInvOrg, servizioCommissioni);

		Report repAttiInizCons = new Report(ATTI_INIZ_CONSILIARE, "crlreport:reportAttiIniziativaConsPerConsServComm");
		TreeNode nodeAttiInizCons = new DefaultTreeNode("lettera", repAttiInizCons, servizioCommissioni);
		TreeNode commissione = new DefaultTreeNode("Commissione", root);

		Report repAttiIstruttComm = new Report(ATTI_ISTRUTTORIA_COMM, "crlreport:reportAttiIstruttoriaCommissioniComm");
		TreeNode nodeAttiIstruttComm = new DefaultTreeNode("lettera", repAttiIstruttComm, commissione);

		Report repAttiAssegComm = new Report(ATTI_ASSEGNATI_COMM, "crlreport:reportAttiAssCommissioniComm");
		TreeNode nodeAttiAssegComm = new DefaultTreeNode("lettera", repAttiAssegComm, commissione);

		Report repElencoRel = new Report(ELENCO_RELATORI, "crlreport:reportRelatoriDataNominaComm");
		TreeNode nodeElencoRel = new DefaultTreeNode("lettera", repElencoRel, commissione);

		Report repAttiLiceComm = new Report(ATTI_LICENZIATI_COMM, "crlreport:reportAttiLicenziatiComm");
		TreeNode nodeAttiLiceComm = new DefaultTreeNode("lettera", repAttiLiceComm, commissione);

		Report repAudizioniCommissioniComm = new Report(ELENCO_AUDIZ_COMM, "crlreport:reportAudizioniCommissioniComm");
		TreeNode nodeAudizioniCommissioniComm = new DefaultTreeNode("lettera", repAudizioniCommissioniComm,
				commissione);
		TreeNode aula = new DefaultTreeNode("Aula", root);

		Report repElencoLcr = new Report(ELENCO_LCR, "crlreport:reportLCRAula");
		TreeNode nodeElencoLcr = new DefaultTreeNode("lettera", repElencoLcr, aula);

		Report repElencoDcr = new Report(ELENCO_DCR, "crlreport:reportDCRAula");
		TreeNode nodeElencoDcr = new DefaultTreeNode("lettera", repElencoDcr, aula);

		Report repElencoAttiIstr = new Report(ELENCO_ATTI_ISTRUTT, "crlreport:reportAttiIstruttoriaAula");
		TreeNode nodeElencoAttiIstr = new DefaultTreeNode("lettera", repElencoAttiIstr, aula);

		Report repAttiRinv = new Report(ELENCO_ATTI_RINVIO, "crlreport:reportAttiRinviatiAula");
		TreeNode nodeAttiRinv = new DefaultTreeNode("lettera", repAttiRinv, aula);

	}

	/**
	 * OK se il report si chiama Composizione commissioni
	 * 
	 * @return false se il report si chiama Composizione commissioni
	 */
	public boolean isLegislaturaVisible() {

		return (!COMPO_COMMISSIONI.equals(selectedReport.getNome()));

	}

	/**
	 * Tipo atto visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isTipiAttoVisible() {

		return (ATTI_ASSEGNATI_COMM.equals(selectedReport.getNome()) || CONFERENZE.equals(selectedReport.getNome())
				|| ATTI_LICENZ.equals(selectedReport.getNome()) || ATTI_RITIRATI.equals(selectedReport.getNome())
				|| ATTI_RELATORE.equals(selectedReport.getNome())
				|| ATTI_ISTRUTTORIA_COMM.equals(selectedReport.getNome())
				|| ATTI_ASSE_COMM.equals(selectedReport.getNome()) || ELENCO_RELATORI.equals(selectedReport.getNome())
				|| ATTI_LICENZIATI_COMM.equals(selectedReport.getNome()) || ELENCO_DCR.equals(selectedReport.getNome())
				|| ELENCO_ATTI_ISTRUTT.equals(selectedReport.getNome())
				|| ELENCO_ATTI_RINVIO.equals(selectedReport.getNome())
				|| ELENCO_AUDIZ_COMM.equals(selectedReport.getNome()));

	}

	/**
	 * Commissione visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isCommissioniVisible() {

		return (ATTI_ASSEGNATI_COMM.equals(selectedReport.getNome()) || CONFERENZE.equals(selectedReport.getNome())
				|| ATTI_LICENZ.equals(selectedReport.getNome()) || ATTI_RELATORE.equals(selectedReport.getNome())
				|| ATTI_ISTRUTTORIA_COMM.equals(selectedReport.getNome())
				|| ATTI_ASSE_COMM.equals(selectedReport.getNome()) || ELENCO_RELATORI.equals(selectedReport.getNome())
				|| ATTI_LICENZIATI_COMM.equals(selectedReport.getNome())
				|| ELENCO_AUDIZ_COMM.equals(selectedReport.getNome()))

		;

	}

	/**
	 * Data consultazione visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isDataConsultazioneVisible() {

		return ELENCO_AUDIZ_COMM.equals(selectedReport.getNome());

	}

	/**
	 * Ruolo commissione visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isRuoloCommissioneVisible() {

		return (ATTI_ASSEGNATI_COMM.equals(selectedReport.getNome()) || CONFERENZE.equals(selectedReport.getNome())
				|| ATTI_LICENZ.equals(selectedReport.getNome())
				|| ATTI_ISTRUTTORIA_COMM.equals(selectedReport.getNome())
				|| ATTI_ASSE_COMM.equals(selectedReport.getNome())
				|| ATTI_LICENZIATI_COMM.equals(selectedReport.getNome()));

	}

	/**
	 * Data assegnazione visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isDataAssegnazioneVisible() {

		return (ATTI_ASSEGNATI_COMM.equals(selectedReport.getNome()) || CONFERENZE.equals(selectedReport.getNome())
				|| ATTI_ISTRUTTORIA_COMM.equals(selectedReport.getNome())
				|| ATTI_ASSE_COMM.equals(selectedReport.getNome()))

		;
	}

	/**
	 * Data votazione commissione visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isDataVotazioneCommReferenteVisible() {

		return (ATTI_LICENZIATI_COMM.equals(selectedReport.getNome()) || ATTI_LICENZ.equals(selectedReport.getNome()))

		;

	}

	/**
	 * Data ritiro visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isDataRitiroVisible() {

		return (ATTI_RITIRATI.equals(selectedReport.getNome()))

		;

	}

	/**
	 * Data nomina relatore visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isDataNominaRelatoreVisible() {

		return (ATTI_RELATORE.equals(selectedReport.getNome()) || ELENCO_RELATORI.equals(selectedReport.getNome()))

		;

	}

	/**
	 * Relatore visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isRelatoriVisible() {

		return (ATTI_RELATORE.equals(selectedReport.getNome()) || ELENCO_RELATORI.equals(selectedReport.getNome()));

	}

	/**
	 * Organismo visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isOrganismoVisible() {

		return (ATTI_INV_ORG_EST.equals(selectedReport.getNome()))

		;
	}

	/**
	 * Data assegnazione parere da visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isDataAssegnazioneParereDaVisible() {

		return (ATTI_INV_ORG_EST.equals(selectedReport.getNome())

		)

		;

	}

	/**
	 * Firmatario visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isFirmatarioVisible() {

		return (ATTI_INIZ_CONSILIARE.equals(selectedReport.getNome()))

		;

	}

	/**
	 * Tipologia firma visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isTipologiaFirmaVisible() {

		return (ATTI_INIZ_CONSILIARE.equals(selectedReport.getNome()))

		;

	}

	/**
	 * Data presentazione visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isDataPresentazioneVisible() {

		return (ATTI_INIZ_CONSILIARE.equals(selectedReport.getNome()))

		;

	}

	/**
	 * Data assegnazione commissione referente visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isDataAssegnazioneCommReferenteVisible() {

		return (ATTI_INIZ_CONSILIARE.equals(selectedReport.getNome()))

		;

	}

	/**
	 * Data seduta visibile
	 * 
	 * @return autorizzazione
	 */
	public boolean isDataSedutaVisible() {

		return (ELENCO_LCR.equals(selectedReport.getNome()) || ELENCO_DCR.equals(selectedReport.getNome()))

		;

	}

	/**
	 * Ritorna il contenuto del report
	 * 
	 * @return il contenuto del report
	 */
	public StreamedContent getFile() {

		if (selectedReport.getTipoTemplate() != null && !selectedReport.getTipoTemplate().equals("")) {

			if (ELENCO_RELATORI.equals(selectedReport.getNome()) && selectedReport.getRelatori().size() == 0) {

				selectedReport.setRelatori(relatori);

			}

			InputStream stream = reportServiceManager.getReportFile(selectedReport);
			StreamedContent file = new DefaultStreamedContent(stream, "document", selectedReport.getNome() + ".docx");
			return file;

		} else {

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Selezionare un Report ", ""));
			return null;
		}

	}

	/**
	 * Aggiunge il report selezionato al contesto web
	 * 
	 * @param event evento di selezione del nodo
	 */
	public void onNodeSelect(NodeSelectEvent event) {

		if (event.getTreeNode().getData() instanceof Report) {

			setSelectedReport((Report) event.getTreeNode().getData());

		} else {

			if (event.getTreeNode().isExpanded()) {
				event.getTreeNode().setExpanded(false);
			} else {
				event.getTreeNode().setExpanded(true);
			}
		}

	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;

	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public Report getSelectedReport() {
		return selectedReport;
	}

	public void setSelectedReport(Report selectedReport) {
		this.selectedReport = selectedReport;
	}

	public List<String> getTipiAtto() {
		return selectedReport.getTipiAtto();
	}

	public void setTipiAtto(List<String> tipiAtto) {
		selectedReport.setTipiAtto(tipiAtto);
	}

	public List<String> getCommissioni() {
		return selectedReport.getCommissioni();
	}

	public void setCommissioni(List<String> commissioni) {
		selectedReport.setCommissioni(commissioni);
	}

	public String getRuoloCommissione() {
		return selectedReport.getRuoloCommissione();
	}

	public void setRuoloCommissione(String ruoloCommissione) {
		selectedReport.setRuoloCommissione(ruoloCommissione);
	}

	public Date getDataAssegnazioneDa() {
		return selectedReport.getDataAssegnazioneDa();
	}

	public void setDataAssegnazioneDa(Date dataAssegnazioneDa) {
		selectedReport.setDataAssegnazioneDa(dataAssegnazioneDa);
	}

	public Date getDataAssegnazioneA() {
		return selectedReport.getDataAssegnazioneA();
	}

	public void setDataAssegnazioneA(Date dataAssegnazioneA) {
		selectedReport.setDataAssegnazioneA(dataAssegnazioneA);
	}

	public Date getDataVotazioneCommReferenteDa() {
		return selectedReport.getDataVotazioneCommReferenteDa();
	}

	public void setDataVotazioneCommReferenteDa(Date dataVotazioneCommReferenteDa) {
		selectedReport.setDataVotazioneCommReferenteDa(dataVotazioneCommReferenteDa);
	}

	public Date getDataVotazioneCommReferenteA() {
		return selectedReport.getDataVotazioneCommReferenteA();
	}

	public void setDataVotazioneCommReferenteA(Date dataVotazioneCommReferenteA) {
		selectedReport.setDataVotazioneCommReferenteA(dataVotazioneCommReferenteA);
	}

	public Date getDataRitiroDa() {
		return selectedReport.getDataRitiroDa();
	}

	public void setDataRitiroDa(Date dataRitiroDa) {
		selectedReport.setDataRitiroDa(dataRitiroDa);
	}

	public Date getDataRitiroA() {
		return selectedReport.getDataRitiroA();
	}

	public void setDataRitiroA(Date dataRitiroA) {
		selectedReport.setDataRitiroA(dataRitiroA);
	}

	public Date getDataNominaRelatoreDa() {
		return selectedReport.getDataNominaRelatoreDa();
	}

	public void setDataNominaRelatoreDa(Date dataNominaRelatoreDa) {
		selectedReport.setDataNominaRelatoreDa(dataNominaRelatoreDa);
	}

	public Date getDataNominaRelatoreA() {
		return selectedReport.getDataNominaRelatoreA();
	}

	public void setDataNominaRelatoreA(Date dataNominaRelatoreA) {
		selectedReport.setDataNominaRelatoreA(dataNominaRelatoreA);
	}

	/*
	 * public List<String> getRelatori() { return selectedReport.getRelatori(); }
	 * 
	 * public void setRelatori(List<String> relatori) {
	 * selectedReport.setRelatori(relatori); }
	 */

	public String getOrganismo() {
		return selectedReport.getOrganismo();
	}

	public void setOrganismo(String organismo) {
		selectedReport.setOrganismo(organismo);
	}

	public Date getDataAssegnazioneParereDa() {
		return selectedReport.getDataAssegnazioneParereDa();
	}

	public void setDataAssegnazioneParereDa(Date dataAssegnazioneParereDa) {
		selectedReport.setDataAssegnazioneParereDa(dataAssegnazioneParereDa);
	}

	public Date getDataAssegnazioneParereA() {
		return selectedReport.getDataAssegnazioneParereA();
	}

	public void setDataAssegnazioneParereA(Date dataAssegnazioneParereA) {
		selectedReport.setDataAssegnazioneParereA(dataAssegnazioneParereA);
	}

	public String getFirmatario() {
		return selectedReport.getFirmatario();
	}

	public void setFirmatario(String firmatario) {
		selectedReport.setFirmatario(firmatario);
	}

	public String getTipologiaFirma() {
		return selectedReport.getTipologiaFirma();
	}

	public void setTipologiaFirma(String tipologiaFirma) {
		selectedReport.setTipologiaFirma(tipologiaFirma);
	}

	public Date getDataPresentazioneDa() {
		return selectedReport.getDataPresentazioneDa();
	}

	public void setDataPresentazioneDa(Date dataPresentazioneDa) {
		selectedReport.setDataPresentazioneDa(dataPresentazioneDa);
	}

	public Date getDataPresentazioneA() {
		return selectedReport.getDataPresentazioneA();
	}

	public void setDataPresentazioneA(Date dataPresentazioneA) {
		selectedReport.setDataPresentazioneA(dataPresentazioneA);
	}

	public Date getDataAssegnazioneCommReferenteDa() {
		return selectedReport.getDataAssegnazioneCommReferenteDa();
	}

	public void setDataAssegnazioneCommReferenteDa(Date dataAssegnazioneCommReferenteDa) {
		selectedReport.setDataAssegnazioneCommReferenteDa(dataAssegnazioneCommReferenteDa);
	}

	public Date getDataAssegnazioneCommReferenteA() {
		return selectedReport.getDataAssegnazioneCommReferenteA();
	}

	public void setDataAssegnazioneCommReferenteA(Date dataAssegnazioneCommReferenteA) {
		selectedReport.setDataAssegnazioneCommReferenteA(dataAssegnazioneCommReferenteA);
	}

	public Date getDataSedutaDa() {
		return selectedReport.getDataSedutaDa();
	}

	public void setDataSedutaDa(Date dataSedutaDa) {
		selectedReport.setDataSedutaDa(dataSedutaDa);
	}

	public Date getDataSedutaA() {
		return selectedReport.getDataSedutaA();
	}

	public void setDataSedutaA(Date dataSedutaA) {
		selectedReport.setDataSedutaA(dataSedutaA);
	}

	public String getLegislatura() {
		return selectedReport.getLegislatura();
	}

	public void setLegislatura(String legislatura) {
		this.selectedReport.setLegislatura(legislatura);
	}

	public Map<String, String> getOrganismi() {
		return organismi;
	}

	public void setOrganismi(Map<String, String> organismi) {
		this.organismi = organismi;
	}

	public TipoAttoServiceManager getTipoAttoServiceManager() {
		return tipoAttoServiceManager;
	}

	public void setTipoAttoServiceManager(TipoAttoServiceManager tipoAttoServiceManager) {
		this.tipoAttoServiceManager = tipoAttoServiceManager;
	}

	public PersonaleServiceManager getPersonaleServiceManager() {
		return personaleServiceManager;
	}

	public void setPersonaleServiceManager(PersonaleServiceManager personaleServiceManager) {
		this.personaleServiceManager = personaleServiceManager;
	}

	public CommissioneServiceManager getCommissioneServiceManager() {
		return commissioneServiceManager;
	}

	public void setCommissioneServiceManager(CommissioneServiceManager commissioneServiceManager) {
		this.commissioneServiceManager = commissioneServiceManager;
	}

	public OrganismoStatutarioServiceManager getOrganismoStatutarioServiceManager() {
		return organismoStatutarioServiceManager;
	}

	public void setOrganismoStatutarioServiceManager(
			OrganismoStatutarioServiceManager organismoStatutarioServiceManager) {
		this.organismoStatutarioServiceManager = organismoStatutarioServiceManager;
	}

	public Map<String, String> getTipiAttoSel() {
		return tipiAttoSel;
	}

	public void setTipiAttoSel(Map<String, String> tipiAttoSel) {
		this.tipiAttoSel = tipiAttoSel;
	}

	/*
	 * public Map<String, String> getCommissioniSel() { return commissioniSel; }
	 * 
	 * public void setCommissioniSel(Map<String, String> commissioniSel) {
	 * this.commissioniSel = commissioniSel; }
	 */

	public List<String> getCommissioniSel() {
		return commissioniSel;
	}

	public void setCommissioniSel(List<String> commissioniSel) {
		this.commissioniSel = commissioniSel;
	}

	public ReportServiceManager getReportServiceManager() {
		return reportServiceManager;
	}

	public void setReportServiceManager(ReportServiceManager reportServiceManager) {
		this.reportServiceManager = reportServiceManager;
	}

	public LegislaturaServiceManager getLegislaturaServiceManager() {
		return legislaturaServiceManager;
	}

	public void setLegislaturaServiceManager(LegislaturaServiceManager legislaturaServiceManager) {
		this.legislaturaServiceManager = legislaturaServiceManager;
	}

	public List<String> getLegislature() {
		return legislature;
	}

	public void setLegislature(List<String> legislature) {
		this.legislature = legislature;
	}

	public List<String> getRelatori() {
		return relatori;
	}

	public void setRelatori(List<String> relatori) {
		this.relatori = relatori;
	}

	public List<String> getRelatoriSelected() {
		return selectedReport.getRelatori();
	}

	public void setRelatoriSelected(List<String> relatoriSelected) {
		this.selectedReport.setRelatori(relatoriSelected);
	}

	public List<String> getFirmatari() {
		return firmatari;
	}

	public void setFirmatari(List<String> firmatari) {
		this.firmatari = firmatari;
	}

	public Date getDataConsultazioneDa() {
		return this.selectedReport.getDataConsultazioneDa();
	}

	public void setDataConsultazioneDa(Date dataConsultazioneDa) {
		this.selectedReport.setDataConsultazioneDa(dataConsultazioneDa);
	}

	public Date getDataConsultazioneA() {
		return this.selectedReport.getDataConsultazioneA();
	}

	public void setDataConsultazioneA(Date dataConsultazioneA) {
		this.selectedReport.setDataConsultazioneA(dataConsultazioneA);
	}

}
