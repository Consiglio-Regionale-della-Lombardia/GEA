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
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.business.service.rest.AttoRecordService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Gestisce le operazioni degli atti di tipo record
 * 
 * @author sourcesense
 *
 */
@Service("attoRecordServiceManager")
public class AttoRecordServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private AttoRecordService attoRecordService;

	/**
	 * Ritorna l'elenco dei testi di un atto
	 * 
	 * @param atto atto
	 * @return elenco di atti
	 */
	public List<TestoAtto> testiAttoByAtto(Atto atto) {

		return attoRecordService.retrieveTestiAtto(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_list_testi_atto", new String[] { atto.getId() }));
	}

	/**
	 * Ritorna l'elenco di allegati dell'atto
	 * 
	 * @param atto atto
	 * @return elenco di allegati
	 */
	public List<Allegato> allAllegatiAttoByAtto(Atto atto) {

		return attoRecordService.retrieveAllegati(urlBuilder.buildAlfrescoURL("alfresco_context_url",
				"alf_list_allegati_atto", new String[] { atto.getId(), "" }));
	}

	/**
	 * Ritorna il contenuto del file di downlaod
	 * 
	 * @param fileToDownload nome del file di downlaod
	 * @return il contenuto del file di downlaod
	 */
	public InputStream getFileById(String fileToDownload) {

		return attoRecordService
				.getFile(urlBuilder.buildAlfrescoDownloadURL("alfresco_dwnl_direct_context_url", fileToDownload, null));
	}

	/**
	 * Aggiornamento dell'allegato
	 * 
	 * @param allegato allegato
	 * @return l'allegato aggiornato
	 */
	public Allegato updateAllegato(Allegato allegato) {

		return attoRecordService.updateAllegato(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_update_allegato", null), allegato);
	}

	/**
	 * Aggiornamento dell'allegato della commissione
	 * 
	 * @param allegato allegato
	 * @return l'allegato aggiornato
	 */
	public Allegato updateAllegatoCommissione(Allegato allegato) {

		return attoRecordService.updateAllegatoCommissione(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_update_allegato", null), allegato);
	}

	/**
	 * Aggiornamento del testo dell'atto
	 * 
	 * @param testoAtto testo dell'atto
	 * @return testo dell'atto aggiornato
	 */
	public TestoAtto updateTestoAtto(TestoAtto testoAtto) {

		return attoRecordService.updateTestoAtto(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_update_testo", null), testoAtto);
	}

	/**
	 * Rimozione del file
	 * 
	 * @param idFile file
	 */
	public void deleteFile(String idFile) {

		attoRecordService.deleteFile(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_delete_file", new String[] { idFile }));

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
