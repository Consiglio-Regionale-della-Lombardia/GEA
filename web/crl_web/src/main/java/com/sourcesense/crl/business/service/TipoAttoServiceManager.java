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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.TipoAtto;
import com.sourcesense.crl.business.model.TipologiaAtto;
import com.sourcesense.crl.business.service.rest.TipoAttoService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Gestione dei tipi di atto. Sono valori dinamici
 * 
 * @author sourcesense
 *
 */
@Service("tipoAttoServiceManager")
public class TipoAttoServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private TipoAttoService tipoAttoService;

	@Override
	public TipoAtto persist(Object object) {
		return null;
	}

	@Override
	public TipoAtto merge(Object object) {
		return null;
	}

	@Override
	public boolean remove(Object object) {
		return false;
	}

	@Override
	public Map<String, String> findAll() {

		Map<String, String> tipiAtto = new LinkedHashMap<String, String>();

		List<TipoAtto> listTipiAtto = tipoAttoService
				.getAllTipoAtto(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_tipi_atto", null));
		Collections.sort(listTipiAtto);

		for (TipoAtto tipoAtto : listTipiAtto) {

			tipiAtto.put(tipoAtto.getDescrizione(), tipoAtto.getCodice());

		}
		return tipiAtto;
	}

	/**
	 * Ritorna l'elenco completo delle descrizioni dei tipi atto
	 * 
	 * @return elenco delle descrizioni dei tipi atto
	 */
	public List<String> getAll() {

		List<String> tipiAtto = new ArrayList<String>();

		List<TipoAtto> listTipiAtto = tipoAttoService
				.getAllTipoAtto(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_tipi_atto", null));

		for (TipoAtto tipoAtto : listTipiAtto) {

			tipiAtto.add(tipoAtto.getDescrizione());

		}

		Collections.sort(tipiAtto);

		return tipiAtto;
	}

	/**
	 * Ritorna l'elenco completo dei tipi atto
	 * @return elenco dei tipi atto
	 */
	public List<TipoAtto> retrieveAllTipoAtto() {

		List<TipoAtto> list = tipoAttoService
				.getAllTipoAtto(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_tipi_atto", null));
		Collections.sort(list);
		return list;

	}

	/**
	 * Ritorna l'elenco completo delle descrizioni dei tipi atto
	 * 
	 * @param tipoAtto tipo di atto
	 * @return elenco delle descrizioni dei tipi atto
	 */
	public Map<String, String> findTipologieByTipoAtto(String tipoAtto) {

		Map<String, String> tipologieAtto = new HashMap<String, String>();

		List<TipologiaAtto> listTipologieAtto = tipoAttoService.getTipologieByTipoAtto(urlBuilder.buildAlfrescoURL(
				"alfresco_context_url", "alf_tipologie_atto_by_tipo_atto", new String[] { tipoAtto }));

		for (TipologiaAtto tipologiaAtto : listTipologieAtto) {

			tipologieAtto.put(tipologiaAtto.getDescrizione(), tipologiaAtto.getDescrizione());

		}
		return tipologieAtto;

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
