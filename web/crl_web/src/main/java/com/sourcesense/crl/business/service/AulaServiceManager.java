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
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.EsameAula;
import com.sourcesense.crl.business.model.Passaggio;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.business.service.rest.AulaService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Contiene le operazioni delle aule
 * 
 * @author sourcesense
 *
 */
@Service("aulaServiceManager")
public class AulaServiceManager implements ServiceManager {

	@Autowired
	private AulaService aulaService;

	@Autowired
	private URLBuilder urlBuilder;

	/**
	 * Presa in carico dell'esame dell'aula
	 * 
	 * @param esameAula esame dell'aula
	 */
	public void presaInCarico(EsameAula esameAula) {

		aulaService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_presa_carico_aula",
				new String[] { esameAula.getAtto().getId() }), esameAula);

	}

	/**
	 * Salvataggio della votazione dell'esame in aula
	 * 
	 * @param esameAula esame dell'aula
	 */
	public void salvaVotazioneEsameAula(EsameAula esameAula) {
		aulaService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_votazione_esame_aula", null),
				esameAula);
	}

	/**
	 * Aggiornamento del testo dell'atto votato all'esame dell'aula
	 * 
	 * @param atto      atto
	 * @param stream    contenuto del testo dell'atto
	 * @param testoAtto testo dell'atto
	 * @return testo dell'atto aggiornato
	 */
	public TestoAtto uploadTestoAttoVotatoEsameAula(Atto atto, InputStream stream, TestoAtto testoAtto) {

		return aulaService
				.uploadTestoAtto(
						urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_testo_atto_aula",
								new String[] { atto.getId() }),
						atto, stream, testoAtto, TestoAtto.TESTO_ESAME_AULA_VOTAZIONE);
	}

	/**
	 * Aggiornamento delle note degli allegati all'esame dell'aula
	 * 
	 * @param atto     atto
	 * @param stream   contenuto dell'allegato
	 * @param allegato allegato
	 * @return allegato aggiornato
	 */
	public Allegato uploadAllegatoNoteAllegatiEsameAula(Atto atto, InputStream stream, Allegato allegato) {

		return aulaService
				.uploadAllegato(
						urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_allegato_aula",
								new String[] { atto.getId() }),
						atto, stream, allegato, Allegato.TIPO_ESAME_AULA_ALLEGATO);
	}

	/**
	 * Aggiornamento dell'emendamento dell'esame dell'aula
	 * 
	 * @param atto     atto
	 * @param stream   contenuto dell'allegato
	 * @param allegato allegato
	 * @return allegato aggiornato
	 */
	public Allegato uploadEmendamentoEsameAula(Atto atto, InputStream stream, Allegato allegato) {

		return aulaService
				.uploadAllegato(
						urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_emendamento_aula",
								new String[] { atto.getId() }),
						atto, stream, allegato, Allegato.TESTO_ESAME_AULA_EMENDAMENTO);
	}

	/**
	 * Salvataggio dell'esame dell'aula
	 * 
	 * @param esameAula esame dell'aula
	 */
	public void salvaEmendamentiEsameAula(EsameAula esameAula) {
		aulaService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_emendamenti_esame_aula", null),
				esameAula);
	}

	/**
	 * Salvataggio del rinvio dell'esame in aula
	 * 
	 * @param esameAula esame dell'aula
	 * @return passaggio
	 */
	public Passaggio salvaRinvioEsameEsameAula(EsameAula esameAula) {
		return aulaService.rinvioEsame(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_rinvio_esame_esame_aula", null),
				esameAula);
	}

	/**
	 * Salvataggio degli stralci dell'esame in aula
	 * 
	 * @param esameAula esame dell'aula
	 */
	public void salvaStralciEsameAula(EsameAula esameAula) {
		aulaService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_stralci_esame_aula", null),
				esameAula);
	}

	/**
	 * Salvataggio delle note degli allegati dell'esame in aula
	 * 
	 * @param esameAula esame dell'aula
	 */
	public void salvaNoteAllegatiEsameAula(EsameAula esameAula) {
		aulaService.merge(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_note_allegati_esame_aula", null),
				esameAula);
	}

	@Override
	public Object persist(Object object) {
		return null;
	}

	@Override
	public Object merge(Object object) {
		return null;
	}

	@Override
	public boolean remove(Object object) {
		return false;
	}

	@Override
	public List<Object> retrieveAll() {
		return null;
	}

	@Override
	public Map<String, String> findAll() {
		return null;
	}

	@Override
	public Object findById(String id) {
		return null;
	}

}
