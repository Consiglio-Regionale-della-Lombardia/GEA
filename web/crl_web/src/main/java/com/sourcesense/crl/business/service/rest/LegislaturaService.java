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
package com.sourcesense.crl.business.service.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcesense.crl.business.model.Legislatura;
import com.sourcesense.crl.util.ServiceNotAvailableException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Servizio rest per la gestione delle legislature. Inserimento, aggiornamento e ricerca
 * 
 * @author sourcesense
 *
 */
@Component(value = "legislaturaService")
@Path("/legislature")
public class LegislaturaService {

	@Autowired
	Client client;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Ritorna l'elenco completo delle legislature per url
	 * 
	 * @param url url
	 * @return elenco completo delle legislature per url
	 */
	public List<Legislatura> getAllLegislatura(String url) {

		List<Legislatura> listLegislature = null;

		try {
			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
			listLegislature = objectMapper.readValue(responseMsg, new TypeReference<List<Legislatura>>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listLegislature;
	}

	/**
	 * Non implementato
	 * 
	 * @param url url
	 * @param legislatura legislatura
	 * @return null
	 */
	public Map<String, String> getAnniByLegislatura(String url, String legislatura) {

		/*
		 * WebResource webResource =
		 * client.resource(messageSource.getMessage("alfresco_context_url", null,
		 * Locale.ITALY));
		 * 
		 * ClientResponse response = webResource.queryParam("legislatura",
		 * legislatura).type(MediaType.APPLICATION_JSON) .get(ClientResponse.class);
		 * 
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */

		return null;

	}

}
