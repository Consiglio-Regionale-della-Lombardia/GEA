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

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import com.sourcesense.crl.business.model.Lettera;
import com.sourcesense.crl.business.service.LettereNotificheServiceManager;
import com.sourcesense.crl.web.ui.beans.AttoBean;
import com.sourcesense.crl.web.ui.beans.UserBean;

/**
 * Gestione delle lettere e delle notifiche dalle pagine web
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "lettereNotificheController")
@ViewScoped
public class LettereNotificheController implements Serializable {

	private TreeNode root;

	private TreeNode selectedNode;
	private Lettera letteraSelected = new Lettera();
	private StreamedContent file;

	private String direzione;
	private String assessore;
	private String emailFirmatario;
	private String firmatario;
	private String numeroTelFirmatario;
	private String ufficio;

	@ManagedProperty(value = "#{lettereNotificheServiceManager}")
	private LettereNotificheServiceManager lettereNotificheServiceManager;

	/**
	 * Aggiunge una lettera di default ./lettere/default.xhtml
	 */
	@PostConstruct
	public void init() {

		letteraSelected.setUrlView("./lettere/default.xhtml");

		root = new DefaultTreeNode("root", null);
		TreeNode nodeServComm = new DefaultTreeNode("Servizio Commissioni", root);

		TreeNode nodeServCommPDL = new DefaultTreeNode("PDL", nodeServComm);

		Lettera letteraTrasmissioneCommissioneReferente = new Lettera();
		letteraTrasmissioneCommissioneReferente
				.setTipoTemplate("crltemplate:letteraTrasmissioneCommissioneReferenteServComm");
		letteraTrasmissioneCommissioneReferente
				.setUrlView("./lettere/servizioCommissioni/pdl/letteraTrasmissioneCommissioneReferente.xhtml");
		letteraTrasmissioneCommissioneReferente.setNome("Lettera Trasmissione Commissione Referente");
		letteraTrasmissioneCommissioneReferente.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissioneCommissioneReferente = new DefaultTreeNode("lettera",
				letteraTrasmissioneCommissioneReferente, nodeServCommPDL);

		Lettera letteraTrasmissioneCommissioneReferenteConsultiva = new Lettera();
		letteraTrasmissioneCommissioneReferenteConsultiva
				.setTipoTemplate("crltemplate:letteraTrasmissioneCommissioneReferenteConsultivaServComm");
		letteraTrasmissioneCommissioneReferenteConsultiva.setUrlView(
				"./lettere/servizioCommissioni/pdl/letteraTrasmissioneCommissioneReferenteConsultiva.xhtml");
		letteraTrasmissioneCommissioneReferenteConsultiva
				.setNome("Lettera Trasmissione Commissione Referente e Consultiva");
		letteraTrasmissioneCommissioneReferenteConsultiva.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissioneCommissioneReferenteConsultiva = new DefaultTreeNode("lettera",
				letteraTrasmissioneCommissioneReferenteConsultiva, nodeServCommPDL);

		Lettera letteraTrasmissioneCommissioneReferenteConsultivaOrganismoStatutario = new Lettera();
		letteraTrasmissioneCommissioneReferenteConsultivaOrganismoStatutario
				.setTipoTemplate("crltemplate:letteraTrasmissioneCommissioneReferenteConsultivaOrganismoServComm");
		letteraTrasmissioneCommissioneReferenteConsultivaOrganismoStatutario.setUrlView(
				"./lettere/servizioCommissioni/pdl/letteraTrasmissioneCommissioneReferenteConsultivaOrganismoStatutario.xhtml");
		letteraTrasmissioneCommissioneReferenteConsultivaOrganismoStatutario
				.setNome("Lettera Trasmissione Commissione Referente, Consultiva e Organismo Statutario");
		letteraTrasmissioneCommissioneReferenteConsultivaOrganismoStatutario.setAuthorities(true, false, false, false,
				false, false);
		TreeNode nodeLetteraTrasmissioneCommissioneReferenteConsultivaOrganismoStatutario = new DefaultTreeNode(
				"lettera", letteraTrasmissioneCommissioneReferenteConsultivaOrganismoStatutario, nodeServCommPDL);

		Lettera letteraRettificaAssegnazione = new Lettera();
		letteraRettificaAssegnazione.setTipoTemplate("crltemplate:letteraRettificaAssegnazioneServComm");
		letteraRettificaAssegnazione.setUrlView("./lettere/servizioCommissioni/pdl/letteraRettificaAssegnazione.xhtml");
		letteraRettificaAssegnazione.setNome("Lettera Rettifica Assegnazione");
		letteraRettificaAssegnazione.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraRettificaAssegnazione = new DefaultTreeNode("lettera", letteraRettificaAssegnazione,
				nodeServCommPDL);

		Lettera letteraIntegrazioneCPOServComm = new Lettera();
		letteraIntegrazioneCPOServComm.setTipoTemplate("crltemplate:letteraIntegrazioneCPOServComm");
		letteraIntegrazioneCPOServComm
				.setUrlView("./lettere/servizioCommissioni/pdl/letteraIntegrazioneCPOServComm.xhtml");
		letteraIntegrazioneCPOServComm.setNome("Lettera integrazione richiesta parere Organo statutario");
		letteraIntegrazioneCPOServComm.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraIntegrazioneCPOServComm = new DefaultTreeNode("lettera", letteraIntegrazioneCPOServComm,
				nodeServCommPDL);

		Lettera letteraRitiro = new Lettera();
		letteraRitiro.setTipoTemplate("crltemplate:letteraRitiroServComm");
		letteraRitiro.setUrlView("./lettere/servizioCommissioni/pdl/letteraRitiro.xhtml");
		letteraRitiro.setNome("Lettera Ritiro");
		letteraRitiro.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraRitiro = new DefaultTreeNode("lettera", letteraRitiro, nodeServCommPDL);

		Lettera letteraAggiuntaFirma = new Lettera();
		letteraAggiuntaFirma.setTipoTemplate("crltemplate:letteraAggiuntaFirmaServComm");
		letteraAggiuntaFirma.setUrlView("./lettere/servizioCommissioni/pdl/letteraAggiuntaFirma.xhtml");
		letteraAggiuntaFirma.setNome("Lettera aggiunta firma");
		letteraAggiuntaFirma.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraAggiuntaFirma = new DefaultTreeNode("lettera", letteraAggiuntaFirma, nodeServCommPDL);

		Lettera letteraRitiroFirma = new Lettera();
		letteraRitiroFirma.setTipoTemplate("crltemplate:letteraRitiroFirmaServComm");
		letteraRitiroFirma.setUrlView("./lettere/servizioCommissioni/pdl/letteraRitiroFirma.xhtml");
		letteraRitiroFirma.setNome("Lettera ritiro firma");
		letteraRitiroFirma.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraRitiroFirma = new DefaultTreeNode("lettera", letteraRitiroFirma, nodeServCommPDL);

		Lettera letteraAssestamentoBilancio = new Lettera();
		letteraAssestamentoBilancio.setTipoTemplate("crltemplate:letteraAssestamentoBilancioServComm");
		letteraAssestamentoBilancio.setUrlView("./lettere/servizioCommissioni/pdl/letteraAssestamentoBilancio.xhtml");
		letteraAssestamentoBilancio.setNome("Lettera assestamento bilancio");
		TreeNode nodeLetteraAssestamentoBilancio = new DefaultTreeNode("lettera", letteraAssestamentoBilancio,
				nodeServCommPDL);

		Lettera letteraBilancioProgrammatico = new Lettera();
		letteraBilancioProgrammatico.setTipoTemplate("crltemplate:letteraBilancioProgrammaticoServComm");
		letteraBilancioProgrammatico.setUrlView("./lettere/servizioCommissioni/pdl/letteraBilancioProgrammatico.xhtml");
		letteraBilancioProgrammatico.setNome("Lettera bilancio programmatico");
		letteraBilancioProgrammatico.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraBilancioProgrammatico = new DefaultTreeNode("lettera", letteraBilancioProgrammatico,
				nodeServCommPDL);

		Lettera letteraIniziativaPresidenteGiuntaFusioneDistacchiComuni = new Lettera();
		letteraIniziativaPresidenteGiuntaFusioneDistacchiComuni
				.setTipoTemplate("crltemplate:letteraIniziativaPresidenteGiuntaFusioneDistaccoComuniServComm");
		letteraIniziativaPresidenteGiuntaFusioneDistacchiComuni.setUrlView(
				"./lettere/servizioCommissioni/pdl/letteraIniziativaPresidenteGiuntaFusioneDistacchiComuni.xhtml");
		letteraIniziativaPresidenteGiuntaFusioneDistacchiComuni.setNome("Lettera di fusione o distacco comuni");
		letteraIniziativaPresidenteGiuntaFusioneDistacchiComuni.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraIniziativaPresidenteGiuntaFusioneDistacchiComuni = new DefaultTreeNode("lettera",
				letteraIniziativaPresidenteGiuntaFusioneDistacchiComuni, nodeServCommPDL);

		Lettera letteraIniziativaPopolare = new Lettera();
		letteraIniziativaPopolare.setTipoTemplate("crltemplate:letteraIniziativaPopolareServComm");
		letteraIniziativaPopolare.setUrlView("./lettere/servizioCommissioni/pdl/letteraIniziativaPopolare.xhtml");
		letteraIniziativaPopolare.setNome("Lettera Iniziativa Popolare");
		letteraIniziativaPopolare.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraIniziativaPopolare = new DefaultTreeNode("lettera", letteraIniziativaPopolare,
				nodeServCommPDL);

		Lettera letteraRendicontoGenerale = new Lettera();
		letteraRendicontoGenerale.setTipoTemplate("crltemplate:letteraRendicontoGeneraleServComm");
		letteraRendicontoGenerale.setUrlView("./lettere/servizioCommissioni/pdl/letteraRendicontoGenerale.xhtml");
		letteraRendicontoGenerale.setNome("Lettera Rendiconto Generale");
		letteraRendicontoGenerale.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraRendicontoGenerale = new DefaultTreeNode("lettera", letteraRendicontoGenerale,
				nodeServCommPDL);

		Lettera letteraLeggeFinanziaria = new Lettera();
		letteraLeggeFinanziaria.setTipoTemplate("crltemplate:letteraLeggeFinanziariaServComm");
		letteraLeggeFinanziaria.setUrlView("./lettere/servizioCommissioni/pdl/letteraRettificaAssegnazione.xhtml");
		letteraLeggeFinanziaria.setNome("Lettera Legge Finanziaria");
		letteraLeggeFinanziaria.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraLeggeFinanziaria = new DefaultTreeNode("lettera", letteraLeggeFinanziaria, nodeServCommPDL);

		TreeNode nodeServCommDOC = new DefaultTreeNode("DOC", nodeServComm);

		Lettera letteraDOCassegnazionePSR = new Lettera();
		letteraDOCassegnazionePSR.setTipoTemplate("crltemplate:letteraDOCAssegnazionePSRServComm");
		letteraDOCassegnazionePSR.setUrlView("./lettere/servizioCommissioni/doc/letteraDOCassegnazionePSR.xhtml");
		letteraDOCassegnazionePSR.setNome("Lettera DOC assegnazione PSR");
		letteraDOCassegnazionePSR.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraDOCassegnazionePSR = new DefaultTreeNode("lettera", letteraDOCassegnazionePSR,
				nodeServCommDOC);

		Lettera letteraDOCassegnazioneDSA = new Lettera();
		letteraDOCassegnazioneDSA.setTipoTemplate("crltemplate:letteraDOCAssegnazioneDSAServComm");
		letteraDOCassegnazioneDSA.setUrlView("./lettere/servizioCommissioni/doc/letteraDOCassegnazioneDSA.xhtml");
		letteraDOCassegnazioneDSA.setNome("Lettera DOC assegnazione DSA");
		letteraDOCassegnazioneDSA.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraDOCassegnazioneDSA = new DefaultTreeNode("lettera", letteraDOCassegnazioneDSA,
				nodeServCommDOC);

		Lettera letteraAssegnazioneProgrammaLavoroCommissioneEuropea = new Lettera();
		letteraAssegnazioneProgrammaLavoroCommissioneEuropea
				.setTipoTemplate("crltemplate:letteraAssegnazioneProgrammaLavoroCommEuropeaServComm");
		letteraAssegnazioneProgrammaLavoroCommissioneEuropea.setUrlView(
				"./lettere/servizioCommissioni/doc/letteraAssegnazioneProgrammaLavoroCommissioneEuropea.xhtml");
		letteraAssegnazioneProgrammaLavoroCommissioneEuropea
				.setNome("Lettera Assegnazione Programma Lavoro Commissione Europea");
		letteraAssegnazioneProgrammaLavoroCommissioneEuropea.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraAssegnazioneProgrammaLavoroCommissioneEuropea = new DefaultTreeNode("lettera",
				letteraAssegnazioneProgrammaLavoroCommissioneEuropea, nodeServCommDOC);

		TreeNode nodeServCommPAR = new DefaultTreeNode("PAR", nodeServComm);

		Lettera letteraParereCommissione = new Lettera();
		letteraParereCommissione.setTipoTemplate("crltemplate:letteraParereCommissioneServComm");
		letteraParereCommissione.setUrlView("./lettere/servizioCommissioni/par/letteraParereCommissione.xhtml");
		letteraParereCommissione.setNome("Lettera Parere Commissione");
		letteraParereCommissione.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraParereCommissione = new DefaultTreeNode("lettera", letteraParereCommissione,
				nodeServCommPAR);

		Lettera letteraIntesaCommissioneGiunta = new Lettera();
		letteraIntesaCommissioneGiunta.setTipoTemplate("crltemplate:letteraIntesaCommissioneGiuntaServComm");
		letteraIntesaCommissioneGiunta
				.setUrlView("./lettere/servizioCommissioni/par/letteraIntesaCommissioneGiunta.xhtml");
		letteraIntesaCommissioneGiunta.setNome("Lettera Intesa Commissione Giunta");
		letteraIntesaCommissioneGiunta.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraIntesaCommissioneGiunta = new DefaultTreeNode("lettera", letteraIntesaCommissioneGiunta,
				nodeServCommPAR);

		Lettera letteraParereCommissioneSospensionepausaestiva = new Lettera();
		letteraParereCommissioneSospensionepausaestiva
				.setTipoTemplate("crltemplate:letteraParereCommissioneConSospPausaEstivaServComm");
		letteraParereCommissioneSospensionepausaestiva
				.setUrlView("./lettere/servizioCommissioni/par/letteraParereCommissioneSospensionepausaestiva.xhtml");
		letteraParereCommissioneSospensionepausaestiva
				.setNome("Lettera Parere Commissione con sospensione pausa estiva");
		letteraParereCommissioneSospensionepausaestiva.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraParereCommissioneSospensionepausaestiva = new DefaultTreeNode("lettera",
				letteraParereCommissioneSospensionepausaestiva, nodeServCommPAR);

		Lettera letteraIntesaCommissioneGiuntasospensionepausaestiva = new Lettera();
		letteraIntesaCommissioneGiuntasospensionepausaestiva
				.setTipoTemplate("crltemplate:letteraIntesaCommissioneGiuntaConSospPausaEstivaServComm");
		letteraIntesaCommissioneGiuntasospensionepausaestiva.setUrlView(
				"./lettere/servizioCommissioni/par/letteraIntesaCommissioneGiuntasospensionepausaestiva.xhtml");
		letteraIntesaCommissioneGiuntasospensionepausaestiva
				.setNome("Lettera Intesa Commissione Giunta con sospensione pausa estiva");
		letteraIntesaCommissioneGiuntasospensionepausaestiva.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraIntesaCommissioneGiuntasospensionepausaestiva = new DefaultTreeNode("lettera",
				letteraIntesaCommissioneGiuntasospensionepausaestiva, nodeServCommPAR);

		Lettera letteraParerePropostaRegolamento = new Lettera();
		letteraParerePropostaRegolamento.setTipoTemplate("crltemplate:letteraParerePropostaDiRegolamentoServComm");
		letteraParerePropostaRegolamento
				.setUrlView("./lettere/servizioCommissioni/par/letteraParerePropostaRegolamento.xhtml");
		letteraParerePropostaRegolamento.setNome("Lettera Parere Proposta di Regolamento");
		letteraParerePropostaRegolamento.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraParerePropostaRegolamento = new DefaultTreeNode("lettera", letteraParerePropostaRegolamento,
				nodeServCommPAR);

		TreeNode nodeServCommPDA = new DefaultTreeNode("PDA", nodeServComm);

		Lettera letteraAssegnazionesededeliberante = new Lettera();
		letteraAssegnazionesededeliberante
				.setTipoTemplate("crltemplate:letteraAssegnazioneInSedeDeliberantePDAServComm");
		letteraAssegnazionesededeliberante
				.setUrlView("./lettere/servizioCommissioni/pda/letteraAssegnazionesededeliberante.xhtml");
		letteraAssegnazionesededeliberante.setNome("Lettera Assegnazione in sede deliberante");
		letteraAssegnazionesededeliberante.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraAssegnazionesededeliberante = new DefaultTreeNode("lettera",
				letteraAssegnazionesededeliberante, nodeServCommPDA);

		Lettera letteraAssegnazionesedereferente = new Lettera();
		letteraAssegnazionesedereferente.setTipoTemplate("crltemplate:letteraAssegnazioneInSedeReferentePDAServComm");
		letteraAssegnazionesedereferente
				.setUrlView("./lettere/servizioCommissioni/pda/letteraAssegnazionesedereferente.xhtml");
		letteraAssegnazionesedereferente.setNome("Lettera Assegnazione in sede referente");
		letteraAssegnazionesedereferente.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraAssegnazionesedereferente = new DefaultTreeNode("lettera", letteraAssegnazionesedereferente,
				nodeServCommPDA);

		TreeNode nodeServCommPLP = new DefaultTreeNode("PLP", nodeServComm);

		Lettera letteraAssegnazioneistruttoria = new Lettera();
		letteraAssegnazioneistruttoria.setTipoTemplate("crltemplate:letteraAssegnazionePerIstruttoriaPLPServComm");
		letteraAssegnazioneistruttoria
				.setUrlView("./lettere/servizioCommissioni/plp/letteraAssegnazioneistruttoria.xhtml");
		letteraAssegnazioneistruttoria.setNome("Lettera Assegnazione per istruttoria");
		letteraAssegnazioneistruttoria.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraAssegnazioneistruttoria = new DefaultTreeNode("lettera", letteraAssegnazioneistruttoria,
				nodeServCommPLP);

		TreeNode nodeServCommREL = new DefaultTreeNode("REL", nodeServComm);

		Lettera letteraRelazionepresentatagiunta = new Lettera();
		letteraRelazionepresentatagiunta.setTipoTemplate("crltemplate:letteraRelazionePresentataDallaGiuntaServComm");
		letteraRelazionepresentatagiunta
				.setUrlView("./lettere/servizioCommissioni/rel/letteraRelazionepresentatagiunta.xhtml");
		letteraRelazionepresentatagiunta.setNome("Lettera Relazione presentata dalla giunta");
		letteraRelazionepresentatagiunta.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraRelazionepresentatagiunta = new DefaultTreeNode("lettera", letteraRelazionepresentatagiunta,
				nodeServCommREL);

		TreeNode nodeServCommPRE = new DefaultTreeNode("PRE", nodeServComm);

		Lettera letteraAssegnazioneistruttoriaPRE = new Lettera();
		letteraAssegnazioneistruttoriaPRE.setTipoTemplate("crltemplate:letteraAssegnazionePerIstruttoriaPREServComm");
		letteraAssegnazioneistruttoriaPRE
				.setUrlView("./lettere/servizioCommissioni/pre/letteraAssegnazioneistruttoriaPRE.xhtml");
		letteraAssegnazioneistruttoriaPRE.setNome("Lettera Assegnazione per istruttoria");
		letteraAssegnazioneistruttoriaPRE.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraAssegnazioneistruttoriaPRE = new DefaultTreeNode("lettera",
				letteraAssegnazioneistruttoriaPRE, nodeServCommPRE);

		TreeNode nodeServCommREF = new DefaultTreeNode("REF", nodeServComm);

		Lettera letteraAssegnazioneistruttoriaREF = new Lettera();
		letteraAssegnazioneistruttoriaREF.setTipoTemplate("crltemplate:letteraAssegnazionePerIstruttoriaREFServComm");
		letteraAssegnazioneistruttoriaREF
				.setUrlView("./lettere/servizioCommissioni/pre/letteraAssegnazioneistruttoriaREF.xhtml");
		letteraAssegnazioneistruttoriaREF.setNome("Lettera Assegnazione per istruttoria");
		letteraAssegnazioneistruttoriaREF.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraAssegnazioneistruttoriaREF = new DefaultTreeNode("lettera",
				letteraAssegnazioneistruttoriaREF, nodeServCommREF);

		TreeNode nodeServCommINP = new DefaultTreeNode("INP", nodeServComm);

		Lettera letteraAssegnazioneistruttoriaINP = new Lettera();
		letteraAssegnazioneistruttoriaINP.setTipoTemplate("crltemplate:letteraAssegnazionePerIstruttoriaINPServComm");
		letteraAssegnazioneistruttoriaINP
				.setUrlView("./lettere/servizioCommissioni/pre/letteraAssegnazioneistruttoriaREF.xhtml");
		letteraAssegnazioneistruttoriaINP.setNome("Lettera Assegnazione per istruttoria");
		letteraAssegnazioneistruttoriaINP.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraAssegnazioneistruttoriaINP = new DefaultTreeNode("lettera",
				letteraAssegnazioneistruttoriaINP, nodeServCommINP);
		TreeNode nodeComm = new DefaultTreeNode("Commissioni", root);

		/*
		 * Lettera letteraCommissioni = new Lettera();
		 * letteraCommissioni.setTipoTemplate("crltemplate:letteraCommissioni");
		 * letteraCommissioni
		 * .setUrlView("./lettere/commissioni/letteraCommissioni.xhtml");
		 * letteraCommissioni.setNome("Lettera Commissioni");
		 * letteraCommissioni.setAuthorities(true, false, false, false, false); TreeNode
		 * nodeLetteraCommissioni = new DefaultTreeNode("lettera", letteraCommissioni,
		 * nodeComm);
		 */

		Lettera letteraTrasmissionePDL = new Lettera();
		letteraTrasmissionePDL.setTipoTemplate("crltemplate:letteraTrasmissionePDLCommissioni");
		letteraTrasmissionePDL.setUrlView("./lettere/commissioni/letteraTrasmissionePDL.xhtml");
		letteraTrasmissionePDL.setNome("Lettera di trasmissione PDL");
		letteraTrasmissionePDL.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissionePDL = new DefaultTreeNode("lettera", letteraTrasmissionePDL, nodeComm);

		Lettera letteraTrasmissionePDA = new Lettera();
		letteraTrasmissionePDA.setTipoTemplate("crltemplate:letteraTrasmissionePDACommissioni");
		letteraTrasmissionePDA.setUrlView("./lettere/commissioni/letteraTrasmissionePDA.xhtml");
		letteraTrasmissionePDA.setNome("Lettera di Trasmissione PDA");
		letteraTrasmissionePDA.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissionePDA = new DefaultTreeNode("lettera", letteraTrasmissionePDA, nodeComm);

		Lettera letteraTrasmissionePDAurgenza = new Lettera();
		letteraTrasmissionePDAurgenza.setTipoTemplate("crltemplate:letteraTrasmissionePDAUrgenzaCommissioni");
		letteraTrasmissionePDAurgenza.setUrlView("./lettere/commissioni/letteraTrasmissionePDAurgenza.xhtml");
		letteraTrasmissionePDAurgenza.setNome("Lettera di Trasmissione con richiesta di trattazione urgente");
		letteraTrasmissionePDAurgenza.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissionePDAurgenza = new DefaultTreeNode("lettera", letteraTrasmissionePDAurgenza,
				nodeComm);

		Lettera letteraTrasmissionePAR = new Lettera();
		letteraTrasmissionePAR.setTipoTemplate("crltemplate:letteraTrasmissionePARCommissioni");
		letteraTrasmissionePAR.setUrlView("./lettere/commissioni/letteraTrasmissionePAR.xhtml");
		letteraTrasmissionePAR.setNome("Lettera di Trasmissione PAR");
		letteraTrasmissionePAR.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLletteraTrasmissionePAR = new DefaultTreeNode("lettera", letteraTrasmissionePAR, nodeComm);

		Lettera letteraTrasmissionePLP = new Lettera();
		letteraTrasmissionePLP.setTipoTemplate("crltemplate:letteraTrasmissionePLPCommissioni");
		letteraTrasmissionePLP.setUrlView("./lettere/commissioni/letteraTrasmissionePLP.xhtml");
		letteraTrasmissionePLP.setNome("Lettera di Trasmissione PLP");
		letteraTrasmissionePLP.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissionePLP = new DefaultTreeNode("lettera", letteraTrasmissionePLP, nodeComm);

		Lettera letteraPresaAttoREL = new Lettera();
		letteraPresaAttoREL.setTipoTemplate("crltemplate:letteraPresaAttoRELCommissioni");
		letteraPresaAttoREL.setUrlView("./lettere/commissioni/letteraPresaAttoREL.xhtml");
		letteraPresaAttoREL.setNome("Lettera di Presa Atto REL");
		letteraPresaAttoREL.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraPresaAttoREL = new DefaultTreeNode("lettera", letteraPresaAttoREL, nodeComm);

		Lettera letteraTrasmissioneREF = new Lettera();
		letteraTrasmissioneREF.setTipoTemplate("crltemplate:letteraTrasmissioneREFCommissioni");
		letteraTrasmissioneREF.setUrlView("./lettere/commissioni/letteraTrasmissioneREF.xhtml");
		letteraTrasmissioneREF.setNome("Lettera di Trasmissione REF");
		letteraTrasmissioneREF.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissioneREF = new DefaultTreeNode("lettera", letteraTrasmissioneREF, nodeComm);

		Lettera letteraTrasmissionePareresedeconsultiva = new Lettera();
		letteraTrasmissionePareresedeconsultiva
				.setTipoTemplate("crltemplate:letteraTrasmissioneParereDaSedeConsultivaCommissioni");
		letteraTrasmissionePareresedeconsultiva
				.setUrlView("./lettere/commissioni/letteraTrasmissionePareresedeconsultiva.xhtml");
		letteraTrasmissionePareresedeconsultiva.setNome("Lettera di Trasmissione Parere da sede consultiva");
		letteraTrasmissionePareresedeconsultiva.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissionePareresedeconsultiva = new DefaultTreeNode("lettera",
				letteraTrasmissionePareresedeconsultiva, nodeComm);

		Lettera letteraTrasmissioneAulaleggeriordinonormativo = new Lettera();
		letteraTrasmissioneAulaleggeriordinonormativo
				.setTipoTemplate("crltemplate:letteraTrasmissioneAulaLeggeRiordinoCommissioni");
		letteraTrasmissioneAulaleggeriordinonormativo
				.setUrlView("./lettere/commissioni/letteraTrasmissioneAulaleggeriordinonormativo.xhtml");
		letteraTrasmissioneAulaleggeriordinonormativo
				.setNome("Lettera di Trasmissione ad Aula legge di riordino (normativo)");
		letteraTrasmissioneAulaleggeriordinonormativo.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissioneAulaleggeriordinonormativo = new DefaultTreeNode("lettera",
				letteraTrasmissioneAulaleggeriordinonormativo, nodeComm);

		Lettera letteraTrasmissioneAulatestounicocompilativo = new Lettera();
		letteraTrasmissioneAulatestounicocompilativo.setTipoTemplate("crltemplate:letteraTrasmissioneAulaTestoUnico");
		letteraTrasmissioneAulatestounicocompilativo
				.setUrlView("./lettere/commissioni/letteraTrasmissioneAulatestounicocompilativo.xhtml");
		letteraTrasmissioneAulatestounicocompilativo
				.setNome("Lettera di Trasmissione in Aula testo unico (compilativo)");
		letteraTrasmissioneAulatestounicocompilativo.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissioneAulatestounicocompilativo = new DefaultTreeNode("lettera",
				letteraTrasmissioneAulatestounicocompilativo, nodeComm);

		Lettera letteratrasmissioneDOCRISDSA = new Lettera();
		letteratrasmissioneDOCRISDSA.setTipoTemplate("crltemplate:letteraTrasmissioneDocRisDsaCommissioni");
		letteratrasmissioneDOCRISDSA.setUrlView("./lettere/commissioni/letteratrasmissioneDOCRISDSA.xhtml");
		letteratrasmissioneDOCRISDSA.setNome("Lettera di trasmissione RIS su DOC - DSA");
		letteratrasmissioneDOCRISDSA.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteratrasmissioneDOCRISDSA = new DefaultTreeNode("lettera", letteratrasmissioneDOCRISDSA,
				nodeComm);

		Lettera letteratrasmissioneDOCRISProgrammaEuropeo = new Lettera();
		letteratrasmissioneDOCRISProgrammaEuropeo
				.setTipoTemplate("crltemplate:letteraTrasmissioneDocRisProgrammaEuropeoCommissioni");
		letteratrasmissioneDOCRISProgrammaEuropeo
				.setUrlView("./lettere/commissioni/letteratrasmissioneDOCRISProgrammaEuropeo.xhtml");
		letteratrasmissioneDOCRISProgrammaEuropeo.setNome("Lettera di trasmissione RIS su DOC - Programma europeo");
		letteratrasmissioneDOCRISProgrammaEuropeo.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteratrasmissioneDOCRISProgrammaEuropeo = new DefaultTreeNode("lettera",
				letteratrasmissioneDOCRISProgrammaEuropeo, nodeComm);

		Lettera letteratrasmissioneparereDOC = new Lettera();
		letteratrasmissioneparereDOC.setTipoTemplate("crltemplate:letteraTrasmissioneParereSuDOCCommissioni");
		letteratrasmissioneparereDOC.setUrlView("./lettere/commissioni/letteratrasmissioneparereDOC.xhtml");
		letteratrasmissioneparereDOC.setNome("Lettera di trasmissione parere su DOC");
		letteratrasmissioneparereDOC.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteratrasmissioneparereDOC = new DefaultTreeNode("lettera", letteratrasmissioneparereDOC,
				nodeComm);

		Lettera letterarichiestaparereNormafinanziaria = new Lettera();
		letterarichiestaparereNormafinanziaria
				.setTipoTemplate("crltemplate:letteraRichiestaParereNormaFinanziariaCommissioni");
		letterarichiestaparereNormafinanziaria
				.setUrlView("./lettere/commissioni/letterarichiestaparereNormafinanziaria.xhtml");
		letterarichiestaparereNormafinanziaria.setNome("Lettera di richiesta parere su Norma finanziaria");
		letterarichiestaparereNormafinanziaria.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetterarichiestaparereNormafinanziaria = new DefaultTreeNode("lettera",
				letterarichiestaparereNormafinanziaria, nodeComm);

		Lettera letteratrasmissioneparereNormafinanziaria = new Lettera();
		letteratrasmissioneparereNormafinanziaria
				.setTipoTemplate("crltemplate:letteraTrasmissioneParereSuNormaFinanziariaCommissioni");
		letteratrasmissioneparereNormafinanziaria
				.setUrlView("./lettere/commissioni/letteratrasmissioneparereNormafinanziaria.xhtml");
		letteratrasmissioneparereNormafinanziaria.setNome("Lettera di trasmissione parere su Norma finanziaria");
		letteratrasmissioneparereNormafinanziaria.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteratrasmissioneparereNormafinanziaria = new DefaultTreeNode("lettera",
				letteratrasmissioneparereNormafinanziaria, nodeComm);

		Lettera letteraTrasmissioneRIS = new Lettera();
		letteraTrasmissioneRIS.setTipoTemplate("crltemplate:letteraTrasmissioneRISCommissioni");
		letteraTrasmissioneRIS.setUrlView("./lettere/commissioni/letteraTrasmissioneRIS.xhtml");
		letteraTrasmissioneRIS.setNome("Lettera di Trasmissione RIS");
		letteraTrasmissioneRIS.setAuthorities(true, false, false, false, false, false);
		TreeNode nodeLetteraTrasmissioneRIS = new DefaultTreeNode("lettera", letteraTrasmissioneRIS, nodeComm);
		TreeNode nodeAula = new DefaultTreeNode("Aula", root);

		/*
		 * Lettera letteraAula = new Lettera();
		 * letteraAula.setTipoTemplate("crltemplate:letteraAula");
		 * letteraAula.setUrlView("./lettere/aula/letteraAula.xhtml");
		 * letteraAula.setNome("Lettera Aula"); letteraAula.setAuthorities(true, true,
		 * false, false, false); TreeNode nodeLetteraAula = new
		 * DefaultTreeNode("lettera", letteraAula, nodeAula);
		 */

		Lettera letteraTrasmissioneLCR = new Lettera();
		letteraTrasmissioneLCR.setTipoTemplate("crltemplate:letteraTrasmissioneLCRAula");
		letteraTrasmissioneLCR.setUrlView("./lettere/aula/letteraTrasmissioneLCR.xhtml");
		letteraTrasmissioneLCR.setNome("Lettera di Trasmissione LCR");
		letteraTrasmissioneLCR.setAuthorities(true, true, false, false, false, false);
		TreeNode nodeLetteraTrasmissioneLCR = new DefaultTreeNode("lettera", letteraTrasmissioneLCR, nodeAula);

		Lettera letteraTrasmissionePDAAula = new Lettera();
		letteraTrasmissionePDAAula.setTipoTemplate("crltemplate:letteraTrasmissionePDAAula");
		letteraTrasmissionePDAAula.setUrlView("./lettere/aula/letteraTrasmissionePDAAula.xhtml");
		letteraTrasmissionePDAAula.setNome("Lettera di Trasmissione PDA");
		letteraTrasmissionePDAAula.setAuthorities(true, true, false, false, false, false);
		TreeNode nodeLetteraTrasmissionePDAAula = new DefaultTreeNode("lettera", letteraTrasmissionePDAAula, nodeAula);

		Lettera letteraTrasmissionePDABilancio = new Lettera();
		letteraTrasmissionePDABilancio.setTipoTemplate("crltemplate:letteraTrasmissionePDABilancioAula");
		letteraTrasmissionePDABilancio.setUrlView("./lettere/aula/letteraTrasmissionePDABilancio.xhtml");
		letteraTrasmissionePDABilancio.setNome("Lettera di Trasmissione PDA Bilancio");
		letteraTrasmissionePDABilancio.setAuthorities(true, true, true, false, false, true);
		TreeNode nodeLetteraTrasmissionePDABilancio = new DefaultTreeNode("lettera", letteraTrasmissionePDABilancio,
				nodeAula);

		Lettera letteraTrasmissionePDAVariazioneBilancio = new Lettera();
		letteraTrasmissionePDAVariazioneBilancio
				.setTipoTemplate("crltemplate:letteraTrasmissionePDAVariazioneBilancioAula");
		letteraTrasmissionePDAVariazioneBilancio
				.setUrlView("./lettere/aula/letteraTrasmissionePDAVariazioneBilancio.xhtml");
		letteraTrasmissionePDAVariazioneBilancio.setNome("Lettera di Trasmissione PDA Variazione Bilancio");
		letteraTrasmissionePDAVariazioneBilancio.setAuthorities(true, true, true, false, false, false);
		TreeNode nodeLetteraTrasmissionePDAVariazioneBilancio = new DefaultTreeNode("lettera",
				letteraTrasmissionePDAVariazioneBilancio, nodeAula);

		Lettera letteraTrasmissioneREFAula = new Lettera();
		letteraTrasmissioneREFAula.setTipoTemplate("crltemplate:letteraTrasmissioneREFAula");
		letteraTrasmissioneREFAula.setUrlView("./lettere/aula/letteraTrasmissioneREFAula.xhtml");
		letteraTrasmissioneREFAula.setNome("Lettera di Trasmissione REF");
		letteraTrasmissioneREFAula.setAuthorities(true, true, false, false, false, false);
		TreeNode nodeLetteraTrasmissioneREFAula = new DefaultTreeNode("lettera", letteraTrasmissioneREFAula, nodeAula);

		Lettera letteraTrasmissioneORG = new Lettera();
		letteraTrasmissioneORG.setTipoTemplate("crltemplate:letteraTrasmissioneORGAula");
		letteraTrasmissioneORG.setUrlView("./lettere/aula/letteraTrasmissioneORG.xhtml");
		letteraTrasmissioneORG.setNome("Lettera di Trasmissione ORG");
		letteraTrasmissioneORG.setAuthorities(true, true, false, false, false, false);
		TreeNode nodeLetteraTrasmissioneORG = new DefaultTreeNode("lettera", letteraTrasmissioneORG, nodeAula);

		Lettera letteraConvalidaORG = new Lettera();
		letteraConvalidaORG.setTipoTemplate("crltemplate:letteraConvalidaORGAula");
		letteraConvalidaORG.setUrlView("./lettere/aula/letteraConvalidaORG.xhtml");
		letteraConvalidaORG.setNome("Lettera di Convalida ORG");
		letteraConvalidaORG.setAuthorities(true, true, false, false, false, false);
		TreeNode nodeLetteraConvalidaORG = new DefaultTreeNode("lettera", letteraConvalidaORG, nodeAula);

		Lettera letteraTrasmissioneBURL = new Lettera();
		letteraTrasmissioneBURL.setTipoTemplate("crltemplate:letteraTrasmissioneBURLAula");
		letteraTrasmissioneBURL.setUrlView("./lettere/aula/letteraTrasmissioneBURL.xhtml");
		letteraTrasmissioneBURL.setNome("Lettera di Trasmissione BURL");
		letteraTrasmissioneBURL.setAuthorities(true, true, false, true, true, false);
		TreeNode nodeLetteraTrasmissioneBURL = new DefaultTreeNode("lettera", letteraTrasmissioneBURL, nodeAula);

	}

	public TreeNode getRoot() {
		return root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;

	}

	/**
	 * Ritorna il contenuto della lettera
	 * 
	 * @return contenuto della lettera
	 */
	public StreamedContent getFile() {

		if (letteraSelected.getTipoTemplate() != null && !letteraSelected.equals("")) {

			FacesContext context = FacesContext.getCurrentInstance();
			AttoBean attoBean = ((AttoBean) context.getExternalContext().getSessionMap().get("attoBean"));

			UserBean userBean = ((UserBean) context.getExternalContext().getSessionMap().get("userBean"));

			InputStream stream = lettereNotificheServiceManager.getLetteraFile(letteraSelected,
					attoBean.getAtto().getId(), userBean.getUser().getSessionGroup().getNome());
			StreamedContent file = new DefaultStreamedContent(stream, "document", letteraSelected.getNome() + ".doc");
			return file;
		} else {

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Selezionare una lettera ", ""));
			return null;
		}
	}

	/**
	 * Aggiunge le informazioni delle lettere, della direzione, email firmatario,
	 * numero di telefono e ufficio dalla lettera selezionata nel contesto web
	 * 
	 * @param event evento di selezione del nodo
	 */
	public void onNodeSelect(NodeSelectEvent event) {

		if (event.getTreeNode().getData() instanceof Lettera) {

			Lettera letteraRet = lettereNotificheServiceManager.getLettera((Lettera) event.getTreeNode().getData());

			setLetteraSelected((Lettera) event.getTreeNode().getData());
			getLetteraSelected().setDirezione(letteraRet.getDirezione());
			getLetteraSelected().setEmailFirmatario(letteraRet.getEmailFirmatario());
			getLetteraSelected().setFirmatario(letteraRet.getFirmatario());
			getLetteraSelected().setNumeroTelFirmatario(letteraRet.getNumeroTelFirmatario());
			getLetteraSelected().setUfficio(letteraRet.getUfficio());

			setDirezione(letteraRet.getDirezione());
			setEmailFirmatario(letteraRet.getEmailFirmatario());
			setFirmatario(letteraRet.getFirmatario());
			setNumeroTelFirmatario(letteraRet.getNumeroTelFirmatario());
			setUfficio(letteraRet.getUfficio());

		} else {

			if (event.getTreeNode().isExpanded()) {
				event.getTreeNode().setExpanded(false);
			} else {
				event.getTreeNode().setExpanded(true);
			}

		}

	}

	/**
	 * Salvataggio della lettera
	 */
	public void salvaLettera() {

		if (letteraSelected.getTipoTemplate() != null && !letteraSelected.equals("")) {

			letteraSelected.setDirezione(this.getDirezione());
			letteraSelected.setEmailFirmatario(this.getEmailFirmatario());
			letteraSelected.setFirmatario(this.getFirmatario());
			letteraSelected.setNumeroTelFirmatario(this.getNumeroTelFirmatario());
			letteraSelected.setUfficio(this.getUfficio());
			letteraSelected.setAssessore(this.getAssessore());

			lettereNotificheServiceManager.updateLettera(letteraSelected);

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Lettera " + letteraSelected.getNome() + " salvata con successo", ""));

		} else {

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Selezionare una lettera ", ""));

		}
	}

	/**
	 * Salvataggio del firmatario
	 * 
	 * @param actionEvent evento ajax JSF
	 */
	public void saveFirmatario(AjaxBehaviorEvent actionEvent) {
		actionEvent.getSource();

	}

	public LettereNotificheServiceManager getLettereNotificheServiceManager() {
		return lettereNotificheServiceManager;
	}

	public void setLettereNotificheServiceManager(LettereNotificheServiceManager lettereNotificheServiceManager) {
		this.lettereNotificheServiceManager = lettereNotificheServiceManager;
	}

	public Lettera getLetteraSelected() {
		return letteraSelected;
	}

	public void setLetteraSelected(Lettera letteraSelected) {
		this.letteraSelected = letteraSelected;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public String getDirezione() {
		return direzione;
	}

	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}

	public String getEmailFirmatario() {
		return emailFirmatario;
	}

	public void setEmailFirmatario(String emailFirmatario) {
		this.emailFirmatario = emailFirmatario;
	}

	public String getFirmatario() {
		return firmatario;
	}

	public void setFirmatario(String firmatario) {
		this.firmatario = firmatario;
	}

	public String getNumeroTelFirmatario() {
		return numeroTelFirmatario;
	}

	public void setNumeroTelFirmatario(String numeroTelFirmatario) {
		this.numeroTelFirmatario = numeroTelFirmatario;
	}

	public String getUfficio() {
		return ufficio;
	}

	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public String getAssessore() {
		return assessore;
	}

	public void setAssessore(String assessore) {
		this.assessore = assessore;
	}

}
