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
package com.sourcesense.crl.business.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.Commissione;
import com.sourcesense.crl.business.model.CommissioneConsultiva;
import com.sourcesense.crl.business.model.CommissioneReferente;
import com.sourcesense.crl.business.model.EsameCommissione;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.business.service.rest.CommissioneService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Contiene le operazioni delle commissioni
 * 
 * @author sourcesense
 *
 */
@Service("commissioneServiceManager")
public class CommissioneServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private CommissioneService commissioneService;

	@Override
	public Commissione persist(Object object) {

		return null;
	}

	@Override
	public Commissione merge(Object object) {

		return null;
	}

	@Override
	public boolean remove(Object object) {

		return false;
	}

	/**
	 * Salvataggio degli emendamenti e delle clausole nell'esame delle commissioni
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaEmendamentiClausoleEsameCommissioni(EsameCommissione esameCommissione) {
		commissioneService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_salva_emendamenti_clausole_esame_commissioni", null), esameCommissione);
	}

	/**
	 * Salvataggio della votazione dell'esame delle commissioni
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaVotazioneEsameCommissioni(EsameCommissione esameCommissione) {
		commissioneService.merge(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_votazione_esame_commissioni", null),
				esameCommissione);
	}

	/**
	 * Presa in carico dell'esame delle commissioni
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaPresaInCaricoEsameCommissioni(EsameCommissione esameCommissione) {
		commissioneService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_salva_presa_in_carico_esame_commissioni", null), esameCommissione);
	}

	/**
	 * Salvataggio dei relatori dell'esame delle commissioni
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaRelatoriEsameCommissioni(EsameCommissione esameCommissione) {
		commissioneService.merge(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_relatori_esame_commissioni", null),
				esameCommissione);
	}

	/**
	 * Salvataggio del comitato ristretto dell'esame delle commissioni
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaComitatoRistrettoEsameCommissioni(EsameCommissione esameCommissione) {
		commissioneService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_salva_comitato_ristretto_esame_commissioni", null), esameCommissione);
	}

	/**
	 * Segna la fine dei lavori dell'esame di commissione
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaFineLavoriEsameCommissioni(EsameCommissione esameCommissione) {
		commissioneService.merge(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_fine_lavori_esame_commissioni", null),
				esameCommissione);
	}

	/**
	 * Aggiorna il testo dell'atto votato all'esame della commissione
	 * 
	 * @param atto      atto
	 * @param stream    contenuto del testo dell'atto
	 * @param testoAtto testo dell'atto
	 * @return testo dell'atto
	 */
	public TestoAtto uploadTestoAttoVotatoEsameCommissioni(Atto atto, InputStream stream, TestoAtto testoAtto) {

		return commissioneService.uploadTestoAtto(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_testo_atto_commissione",
						new String[] { atto.getId() }),
				atto, stream, testoAtto, testoAtto.getTipologiaTesto());
	}

	/**
	 * Aggiorna l'emendamento dell'esame della commissione
	 * 
	 * @param atto      atto
	 * @param stream    contenuto del testo dell'atto
	 * @param testoAtto testo dell'atto
	 * @return allegato
	 */
	public Allegato uploadEmendamentoEsameCommissioni(Atto atto, InputStream stream, Allegato testoAtto) {

		return commissioneService.uploadAllegato(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_allegato_commissione",
						new String[] { atto.getId() }),
				atto, stream, testoAtto, Allegato.TIPO_ESAME_COMMISSIONE_EMENDAMENTO);
	}

	/**
	 * Aggiorna il testo del comitato ristretto
	 * 
	 * @param atto      atto
	 * @param stream    contenuto del testo dell'atto
	 * @param testoAtto testo dell'atto
	 * @return allegato
	 */
	public Allegato uploadTestoComitatoRistretto(Atto atto, InputStream stream, Allegato testoAtto) {

		return commissioneService.uploadAllegato(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_testo_comitato_ristretto",
						new String[] { atto.getId() }),
				atto, stream, testoAtto, Allegato.TESTO_ESAME_COMMISSIONE_COMITATO);
	}

	/**
	 * Aggiorna il testo della clausola dell'esame di commissione
	 * 
	 * @param atto      atto
	 * @param stream    contenuto del testo dell'atto
	 * @param testoAtto testo dell'atto
	 * @return allegato
	 */
	public Allegato uploadTestoClausolaEsameCommissioni(Atto atto, InputStream stream, Allegato testoAtto) {

		return commissioneService.uploadAllegato(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_allegato_commissione",
						new String[] { atto.getId() }),
				atto, stream, testoAtto, Allegato.TESTO_ESAME_COMMISSIONE_CLAUSOLA);
	}

	/**
	 * Aggiorna l'allegato delle note dell'esame di commissione
	 * 
	 * @param atto     atto
	 * @param stream   contenuto dell'allegato
	 * @param allegato allegato
	 * @return allegato
	 */
	public Allegato uploadAllegatoNoteAllegatiEsameCommissioni(Atto atto, InputStream stream, Allegato allegato) {

		return commissioneService.uploadAllegato(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_allegato_commissione",
						new String[] { atto.getId() }),
				atto, stream, allegato, Allegato.TIPO_ESAME_COMMISSIONE_ALLEGATO);
	}

	/**
	 * Cambio del ruolo di referente
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaCambiaRuoloInReferente(EsameCommissione esameCommissione) {
		commissioneService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_salva_continuazione_commissione_referente", null), esameCommissione);
	}

	/**
	 * Rimuove l'abbinamento
	 * 
	 * @param idAtto         id dell'atto
	 * @param idAttoAbbinato id dell'atto abbinato
	 * @param lastPassaggio  ultimo passaggio
	 */
	public void removeAbbinamento(String idAtto, String idAttoAbbinato, String lastPassaggio) {
		commissioneService.removeAbbinamento(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_delete_abbinamento_esame_commissioni", new String[] { idAtto, idAttoAbbinato, lastPassaggio }));
	}

	/**
	 * Salva le note dell'allegato dell'esame di commissione
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaNoteAllegatiEsameCommissioni(EsameCommissione esameCommissione) {
		commissioneService.merge(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_note_allegati_esame_commissioni", null),
				esameCommissione);
	}

	/**
	 * Salvataggio degli stralci
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaStralci(EsameCommissione esameCommissione) {
		commissioneService.merge(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_stralci_commissioni", null),
				esameCommissione);
	}

	/**
	 * Salvataggio della trasmissione
	 * 
	 * @param esameCommissione esame della commissione
	 */
	public void salvaTrasmissione(EsameCommissione esameCommissione) {
		commissioneService.merge(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_trasmissione_commissione", null),
				esameCommissione);
	}

	/**
	 * Ricerca le commissioni di un determinato atto
	 * 
	 * @param idAtto id dell'atto
	 * @return elenco di commissioni
	 */
	public List<Commissione> findCommissioniByAtto(String idAtto) {

		List<Commissione> commissioni = new ArrayList<Commissione>();

		List<CommissioneReferente> commissioniRef = commissioneService.retrieveCommissioniReferenteByAtto(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_new_atto", new String[] { idAtto }));

		List<CommissioneConsultiva> commissioniCons = commissioneService.retrieveCommissioniConsultiveByAtto(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_new_atto", new String[] { idAtto }));

		commissioni.addAll(commissioniCons);
		commissioni.addAll(commissioniRef);

		return commissioni;

	}

	@Override
	public Map<String, String> findAll() {
		Map<String, String> commissioni = new HashMap<String, String>();

		List<Commissione> listCommissioni = commissioneService
				.getAllCommissioni(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_commissioni", null));

		for (Commissione commissione : listCommissioni) {

			commissioni.put(commissione.getDescrizione(), commissione.getDescrizione());

		}
		return commissioni;

	}

	/**
	 * Ritorna tutte le descrizioni delle commissioni disponibili
	 * 
	 * @return l'elenco delle descrizioni delle commissioni
	 */
	public List<String> getAll() {

		List<String> commissioni = new ArrayList<String>();

		List<Commissione> listCommissioni = commissioneService
				.getAllCommissioni(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_commissioni", null));

		for (Commissione commissione : listCommissioni) {

			commissioni.add(commissione.getDescrizione());

		}
		return commissioni;

	}

	@Override
	public Object findById(String id) {

		return null;
	}

	@Override
	public List<Object> retrieveAll() {

		return null;
	}

}
