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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.GestioneAbbinamento;
import com.sourcesense.crl.business.service.rest.AbbinamentoService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Esegue il salvataggio degli abbinamenti
 * 
 * @author sourcesense
 *
 */
@Service("abbinamentoServiceManager")
public class AbbinamentoServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private AbbinamentoService abbinamentoService;

	/**
	 * Salvataggio dell'abbinamento
	 * 
	 * @param abbinamento abbinamento con l'atto
	 */
	public void salvaAbbinamento(GestioneAbbinamento abbinamento) {
		abbinamentoService.merge(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_salva_abbinamento_esame_commissioni", null),
				abbinamento);
	}
}
