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
import java.io.InputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcesense.crl.business.model.Lettera;
import com.sourcesense.crl.util.ServiceNotAvailableException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Servizio rest per la gestione delle lettere. Inserimento, aggiornamento e ricerca
 * 
 * @author sourcesense
 *
 */
@Component(value = "lettereNotificheService")
@Path("/lettereNotifiche")
public class LettereNotificheService {

	@Autowired
	Client client;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Aggiornamento della lettera
	 * 
	 * @param url url
	 * @param lettera lettera
	 */
	public void merge(String url, Lettera lettera) {
		try {
			WebResource webResource = client.resource(url);

			String json = objectMapper.writeValueAsString(lettera);

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");

			}

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

	}

	/**
	 * Ritorna la lettera per un determinato url
	 * 
	 * @param url url
	 * @return lettera
	 */
	public Lettera getLettera(String url) {

		Lettera lettera = null;

		try {
			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			lettera = objectMapper.readValue(responseMsg, Lettera.class);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return lettera;
	}

	/**
	 * Ritorna il contenuto della lettera
	 * 
	 * @param url url
	 * @return contenuto della lettera
	 */
	public InputStream getFile(String url) {

		InputStream responseFile = null;

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.accept(MediaType.MULTIPART_FORM_DATA).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}

		responseFile = response.getEntity(InputStream.class);

		return responseFile;

	}

}
