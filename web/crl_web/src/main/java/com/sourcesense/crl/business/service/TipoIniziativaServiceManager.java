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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.TipoIniziativa;
import com.sourcesense.crl.business.service.rest.TipoIniziativaService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Gestisce i tipi di iniziativa. Sono valori dinamici
 * 
 * @author sourcesense
 *
 */
@Service("tipoIniziativaServiceManager")
public class TipoIniziativaServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private TipoIniziativaService tipoIniziativaService;

	@Override
	public TipoIniziativa persist(Object object) {

		return null;
	}

	@Override
	public TipoIniziativa merge(Object object) {

		return null;
	}

	@Override
	public boolean remove(Object object) {

		return false;
	}

	@Override
	public Map<String, String> findAll() {
		Map<String, String> tipiIniziative = new LinkedHashMap<String, String>();

		List<TipoIniziativa> listTipiIniziative = tipoIniziativaService.getAllTipoIniziativa(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_tipi_iniziative", null));

		Collections.sort(listTipiIniziative);

		for (TipoIniziativa tipoIniziativa : listTipiIniziative) {

			tipiIniziative.put(tipoIniziativa.getDescrizione(), tipoIniziativa.getDescrizione());

		}
		return tipiIniziative;
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
