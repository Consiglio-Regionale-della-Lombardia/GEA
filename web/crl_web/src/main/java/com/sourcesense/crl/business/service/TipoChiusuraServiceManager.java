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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.TipoChiusura;
import com.sourcesense.crl.business.service.rest.TipoChiusuraService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Gestisce i tipi di chiusura. Sono valori dinamici
 * 
 * @author sourcesense
 *
 */
@Service("tipoChiusuraServiceManager")
public class TipoChiusuraServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private TipoChiusuraService tipoChiusuraService;

	@Override
	public TipoChiusura persist(Object object) {

		return null;
	}

	@Override
	public TipoChiusura merge(Object object) {

		return null;
	}

	@Override
	public boolean remove(Object object) {

		return false;
	}

	@Override
	public Map<String, String> findAll() {
		Map<String, String> tipiChiusura = new HashMap<String, String>();

		List<TipoChiusura> listTipiChiusura = tipoChiusuraService.getAllTipoChiusura(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_tipi_chiusura", null));

		for (TipoChiusura tipoChiusura : listTipiChiusura) {

			tipiChiusura.put(tipoChiusura.getDescrizione(), tipoChiusura.getDescrizione());

		}
		return tipiChiusura;
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
