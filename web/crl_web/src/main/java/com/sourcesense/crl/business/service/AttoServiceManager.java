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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.AttoEAC;
import com.sourcesense.crl.business.model.AttoMIS;
import com.sourcesense.crl.business.model.AttoSearch;
import com.sourcesense.crl.business.model.Collegamento;
import com.sourcesense.crl.business.model.CollegamentoAttiSindacato;
import com.sourcesense.crl.business.model.ConsultazioneParere;
import com.sourcesense.crl.business.model.Firmatario;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.business.service.rest.AttoService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Contiene le operazioni degli atti
 * 
 * @author sourcesense
 *
 */
@Service("attoServiceManager")
public class AttoServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private AttoService attoService;

	/**
	 * Ritorna l'atto completo secondo le informazioni parziali passate
	 * 
	 * @param atto atto
	 * @return elenco di atti
	 */
	public List<Atto> searchAtti(Atto atto) {
		return attoService.parametricSearch(atto,
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_atto_ricerca_avanzata", null));
	}

	/**
	 * Inizializza l'atto per la ricerca mettendo come vincolo la data di ricerca di
	 * un massimo di due mesi dalla data odierna
	 * 
	 * @return elenco di atti
	 */
	public List<Atto> initListAtti() {

		AttoSearch attoInit = new AttoSearch();
		attoInit.setDataIniziativaA(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		attoInit.setDataIniziativaDa(calendar.getTime());
		return attoService.parametricSearch(attoInit,
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_atto_ricerca_avanzata", null));

	}

	/**
	 * Rimuove il firmatario
	 * 
	 * @param firmatario firmatario
	 */
	public void removeFirmatario(Firmatario firmatario) {

		attoService.removeFirmatario(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_remove_firmatario",
				new String[] { firmatario.getId() }));
	}

	/**
	 * Rimuove l'atto
	 * 
	 * @param atto atto
	 */
	public void removeAtto(Atto atto) {

		attoService.removeFirmatario(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_remove_atto", new String[] { atto.getId() }));
	}

	/**
	 * Aggiorna il testo dell'atto
	 * 
	 * @param atto      atto
	 * @param stream    contenuto del testo dell'atto
	 * @param testoAtto testo dell'atto
	 * @return testo dell'atto aggiornato
	 */
	public TestoAtto uploadTestoAttoPresentazioneAssegnazione(Atto atto, InputStream stream, TestoAtto testoAtto) {

		return attoService.uploadTestoAtto(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_testo", new String[] { atto.getId() }),
				atto, stream, testoAtto, TestoAtto.TESTO_PRESENTAZIONE_ASSEGNAZIONE);
	}

	/**
	 * Aggiorna l'allegato
	 * 
	 * @param atto     atto
	 * @param stream   contenuto dell'allegato
	 * @param allegato allegato
	 * @return allegato aggiornato
	 */
	public Allegato uploadAllegatoNoteAllegatiPresentazioneAssegnazione(Atto atto, InputStream stream,
			Allegato allegato) {

		return attoService.uploadAllegato(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_allegato",
						new String[] { atto.getId() }),
				atto, stream, allegato, Allegato.TIPO_PRESENTAZIONE_ASSEGNAZIONE);
	}

	/**
	 * Aggiorna l'allegato dell'atto MIS
	 * 
	 * @param atto     atto
	 * @param stream   contenuto dell'allegato
	 * @param allegato allegato
	 * @return allegato aggiornato
	 */
	public Allegato uploadAllegatoMIS(AttoMIS atto, InputStream stream, Allegato allegato) {

		return attoService.uploadAllegato(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_allegato",
						new String[] { atto.getId() }),
				atto, stream, allegato, Allegato.TIPO_PRESENTAZIONE_ASSEGNAZIONE);
	}

	/**
	 * Aggiorna l'allegato dell'atto EAC
	 * 
	 * @param atto     atto
	 * @param stream   contenuto dell'allegato
	 * @param allegato allegato
	 * @return allegato aggiornato
	 */
	public Allegato uploadAllegatoEAC(AttoEAC atto, InputStream stream, Allegato allegato) {

		return attoService.uploadAllegato(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_upload_allegato",
						new String[] { atto.getId() }),
				atto, stream, allegato, Allegato.TIPO_PRESENTAZIONE_ASSEGNAZIONE);
	}

	/**
	 * Aggiorna l'allegato delle consultazioni dell'atto
	 * 
	 * @param atto     atto
	 * @param stream   contenuto dell'allegato
	 * @param allegato allegato
	 * @return allegato aggiornato
	 */
	public Allegato uploadAllegatoConsultazioni(Atto atto, InputStream stream, Allegato allegato) {

		return attoService.uploadAllegatoConsultazioni(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_upload_allegato_consultazioni", new String[] { atto.getId() }), atto, stream, allegato,
				Allegato.TIPO_CONSULTAZIONE);
	}

	/**
	 * Aggiorna l'allegato del parere dell'atto
	 * 
	 * @param atto     atto
	 * @param stream   contenuto dell'allegato
	 * @param allegato allegato
	 * @return allegato aggiornato
	 */
	public Allegato uploadAllegatoPareri(Atto atto, InputStream stream, Allegato allegato) {

		return attoService.uploadAllegatoParere(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_upload_allegato_pareri", new String[] { atto.getId() }), atto, stream, allegato,
				Allegato.TIPO_PARERE);
	}

	/**
	 * Cambia l'allegato di una presentazione
	 * 
	 * @param allegato allegato
	 * @return testo dell'atto
	 */
	public TestoAtto changeAllegatoPresentazioneAssegnazione(Allegato allegato) {

		return attoService.changeAllegato(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_change_allegato",
				new String[] { allegato.getId() }));

	}

	/**
	 * Aggiorna l'atto
	 * 
	 * @param atto atto
	 * @return atto aggiornato
	 */
	public Atto updateAtto(Atto atto) {
		return attoService.updateAtto(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_update_atto", new String[] { atto.getId() }),
				atto);

	}

	/**
	 * Rimuove l'atto
	 * 
	 * @param atto atto
	 */
	public void deleteAtto(Atto atto) {
		attoService.deleteAtto(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_delete_atto", new String[] { atto.getId() }));

	}

	/**
	 * Salvataggio del MIS
	 * 
	 * @param object atto MIS
	 * @return atto MIS aggiornato
	 */
	public AttoMIS persistMIS(Object object) {
		return attoService.createMIS(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_new_atto", null),
				(AttoMIS) object);

	}

	/**
	 * Salvataggio dell'EAC
	 * 
	 * @param object atto EAC
	 * @return atto EAC aggiornato
	 */
	public AttoEAC persistEAC(Object object) {
		return attoService.createEAC(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_new_atto", null),
				(AttoEAC) object);

	}

	@Override
	public Atto persist(Object object) {
		return attoService.create(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_new_atto", null),
				(Atto) object);

	}

	/**
	 * Ricerca dell'atto per id
	 * 
	 * @param id id dell'atto
	 * @return atto
	 */
	public Atto findById(String id) {
		return attoService
				.findById(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_atto_by_id", new String[] { id }));
	}

	/**
	 * Ricerca dell'atto EAC per id
	 * 
	 * @param id id dell'atto
	 * @return atto EAC
	 */
	public AttoEAC findEACById(String id) {
		return attoService.findEACById(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_atto_by_id", new String[] { id }));
	}

	/**
	 * Ricerca dell'atto MIS per id
	 * 
	 * @param id id dell'atto
	 * @return atto MIS
	 */
	public AttoMIS findMISById(String id) {
		return attoService.findMISById(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_atto_by_id", new String[] { id }));
	}

	/**
	 * Ricerca tutti gli atti sindacali secondo i campi
	 * 
	 * @param tipoAtto        tipo di atto
	 * @param dataCreazioneDa data creazione di inizio
	 * @param dataCreazioneA  data creazione di fine
	 * @return elenco di collegamenti agli atti del sindacato
	 */
	public List<CollegamentoAttiSindacato> findAllAttiSindacato(String tipoAtto, String dataCreazioneDa,
			String dataCreazioneA) {
		return attoService.findAllAttiSindacato(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_retrieve_atti_indirizzo", new String[] { tipoAtto, dataCreazioneDa, dataCreazioneA }));
	}

	/**
	 * Ricerca dell'atto sindacale per id
	 * 
	 * @param idAtto id dell'atto
	 * @return elenco di collegamenti agli atti del sindacato
	 */
	public List<CollegamentoAttiSindacato> findAttiSindacatoById(String idAtto) {
		return attoService.findAllAttiSindacato(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_retrieve_atti_indirizzo_atto", new String[] { idAtto }));
	}

	/**
	 * Ricerca degli atti collegati ad un atto
	 * 
	 * @param idAtto id dell'atto
	 * @return elenco di collegamenti agli atti del sindacato
	 */
	public List<Collegamento> findAttiCollegatiById(String idAtto) {
		return attoService.findCollegamentiAttoById(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_collegamenti_atto", new String[] { idAtto }));
	}

	/**
	 * Tornano i tipi degli atti disponibili
	 * 
	 * @return elenco dei tipi di atti
	 */
	public List<String> findTipoAttiSindacato() {
		return attoService.findTipoAttiSindacato(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_retrieve_tipo_atti_indirizzo", null));
	}

	/**
	 * Presa in carico dell'atto
	 * 
	 * @param atto atto
	 */
	public void presaInCaricoSC(Atto atto) {

		attoService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_presa_carico_presentazione_assegnazione", new String[] { atto.getId() }), atto);

	}

	/**
	 * Salva le informazioni generali dell'atto
	 * 
	 * @param atto atto
	 */
	public void salvaInfoGeneraliPresentazione(Atto atto) {
		attoService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_salva_info_generali_presentazione_assegnazione", new String[] { atto.getId() }), atto);
	}

	/**
	 * Salva l'ammissibiità della presentazione di un atto
	 * 
	 * @param atto atto
	 */
	public void salvaAmmissibilitaPresentazione(Atto atto) {
		attoService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_salva_ammissibilita_presentazione_assegnazione", null), atto);
	}

	/**
	 * Salva l'assegnazione della presentazione di un atto
	 * 
	 * @param atto atto
	 */
	public void salvaAssegnazionePresentazione(Atto atto) {
		attoService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_salva_assegnazione_presentazione_assegnazione", null), atto);
	}

	/**
	 * Salva le note degli allegati della presentazione di un atto
	 * 
	 * @param atto atto
	 */
	public void salvaNoteAllegatiPresentazione(Atto atto) {
		attoService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_salva_note_allegati_presentazione_assegnazione", null), atto);
	}

	/**
	 * Salva le consultazioni di un atto
	 * 
	 * @param atto atto
	 */
	public void salvaConsultazioni(Atto atto) {
		attoService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_consultazioni", null), atto);
	}

	/**
	 * Salvataggio dei pareri
	 * 
	 * @param consultazioneParere consultazione parere
	 */
	public void salvaPareri(ConsultazioneParere consultazioneParere) {
		attoService.salvaPareri(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_pareri", null),
				consultazioneParere);
	}

	/**
	 * Salva i collegamenti di un atto
	 * 
	 * @param atto atto
	 */
	public void salvaCollegamenti(Atto atto) {
		attoService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_collegamenti", null), atto);
	}

	/**
	 * Salva i collegamenti degli atti del sindacato
	 * 
	 * @param atto atto
	 */
	public void salvaCollegamentiAttiSindacato(Atto atto) {
		attoService.merge(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_collegamenti_atto_sindacato", null),
				atto);
	}

	/**
	 * Salva i relatori dell'aula
	 * 
	 * @param atto atto
	 */
	public void salvaRelatoriAula(Atto atto) {
		attoService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_relatori_aula", null), atto);
	}

	/**
	 * Operazione di chiusura dell'atto
	 * 
	 * @param atto atto
	 */
	public void chiusuraAtto(Atto atto) {
		attoService.merge(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_chiusura_atto", null), atto);
	}

	/**
	 * Ritorna il link delle leggi regionali secondo i parametri passati
	 * 
	 * @param data   data
	 * @param numero numero
	 * @return link
	 */
	public String regioniUrl(String data, String numero) {
		return urlBuilder.buildSimpleURL("leggi_regionali_link", new String[] { data, numero });
	}

	@Override
	public Atto merge(Object object) {
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

}
