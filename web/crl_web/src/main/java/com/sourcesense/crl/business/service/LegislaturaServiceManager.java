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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.Legislatura;
import com.sourcesense.crl.business.service.rest.LegislaturaService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Interroga e interagisce sulla legislatura
 * 
 * @author sourcesense
 *
 */
@Service("legislaturaServiceManager")
public class LegislaturaServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private LegislaturaService legislaturaService;

	@Override
	public Legislatura persist(Object object) {
		return null;
	}

	@Override
	public Legislatura merge(Object object) {
		return null;
	}

	@Override
	public boolean remove(Object object) {
		return false;
	}

	/**
	 * Ritorna l'elenco delle legislature disponibili
	 * 
	 * @return elenco delle legislature
	 */
	public List<Legislatura> getAll() {

		List<Legislatura> listLegislature = legislaturaService
				.getAllLegislatura(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_legislature", null));
		return listLegislature;
	}

	@Override
	public Map<String, String> findAll() {

		Map<String, String> legislature = new HashMap<String, String>();

		List<Legislatura> listLegislature = legislaturaService
				.getAllLegislatura(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_legislature", null));

		for (Legislatura legislatura : listLegislature) {

			legislature.put(legislatura.getNome(), legislatura.getNome());

		}
		return legislature;
	}

	/**
	 * Ritorna i nomi delle legislature disponibili
	 * 
	 * @return elenco dei nomi delle legislature
	 */
	public List<String> list() {

		List<String> legislature = new ArrayList<String>();

		List<Legislatura> listLegislature = legislaturaService
				.getAllLegislatura(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_list_legislature", null));

		for (Legislatura legislatura : listLegislature) {

			legislature.add(legislatura.getNome());

		}
		return legislature;
	}

	/**
	 * Ricerca gli anni di una determinata legislatura
	 * 
	 * @param legislatura legislatura
	 * @return elenco degli anni
	 */
	public Map<String, String> findAnniByLegislatura(String legislatura) {

		return legislaturaService.getAnniByLegislatura(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "pathPropertyName", null), legislatura);
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
