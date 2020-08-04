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
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.util.ServiceNotAvailableException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Servizio rest per la gestione degli atti record
 * 
 * @author sourcesense
 *
 */
@Component(value = "attoRecordService")
@Path("/attiRecord")
public class AttoRecordService {

	@Autowired
	Client client;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Ritorna l'elenco dei testi dell'atto per un determinato url
	 * 
	 * @param url url
	 * @return elenco dei testi dell'atto
	 */
	public List<TestoAtto> retrieveTestiAtto(String url) {
		List<TestoAtto> listTestiAtto = null;

		try {
			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
			listTestiAtto = objectMapper.readValue(responseMsg, new TypeReference<List<TestoAtto>>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listTestiAtto;

	}

	/**
	 * Rimuove l'url
	 * 
	 * @param url url
	 */
	public void deleteFile(String url) {

		WebResource webResource = client.resource(url);
		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		if (response.getStatus() != 200) {

			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");

		}

	}

	/**
	 * Ritorna l'elenco degli allegati per un determinato url
	 * 
	 * @param url url
	 * @return elenco degli allegati
	 */
	public List<Allegato> retrieveAllegati(String url) {
		List<Allegato> listAllegati = null;

		try {
			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);

			listAllegati = objectMapper.readValue(responseMsg, new TypeReference<List<Allegato>>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listAllegati;

	}

	/**
	 * Ritorna il contenuto di un url
	 * 
	 * @param url url
	 * @return contenuto dell'url
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

	/**
	 * Aggiornamento dell'allegato della commissione
	 * 
	 * @param url      url
	 * @param allegato allegato
	 * @return allegato risultante
	 */
	public Allegato updateAllegatoCommissione(String url, Allegato allegato) {

		WebResource webResource = client.resource(url);
		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);

		try {
			String json = objectMapper.writeValueAsString(allegato);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() != 200) {

				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");

			}
			/*
			 * objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE,true);
			 * String responseMsg = response.getEntity(String.class);
			 * 
			 * allegatoRet = objectMapper.readValue(responseMsg, Allegato.class);
			 * allegato.setId(allegatoRet.getId());
			 */
		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

		return allegato;
	}

	/**
	 * Aggiornamento dell'allegato per un determinato url
	 * 
	 * @param url      url
	 * @param allegato allegato
	 * @return allegato risultante
	 */
	public Allegato updateAllegato(String url, Allegato allegato) {

		Allegato allegatoRet = null;

		WebResource webResource = client.resource(url);
		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);

		try {
			String json = objectMapper.writeValueAsString(allegato);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() != 200) {

				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");

			}
			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
			String responseMsg = response.getEntity(String.class);
			allegatoRet = objectMapper.readValue(responseMsg, Allegato.class);
			allegato.setId(allegatoRet.getId());

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

		return allegato;
	}

	/**
	 * Aggiornamento del testo dell'atto per un determinato url
	 * 
	 * @param url       url
	 * @param testoAtto testo dell'atto
	 * @return testo dell'atto risultante
	 */
	public TestoAtto updateTestoAtto(String url, TestoAtto testoAtto) {

		TestoAtto testoAttoRet = null;

		WebResource webResource = client.resource(url);
		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);

		try {
			String json = objectMapper.writeValueAsString(testoAtto);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() != 200) {

				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");

			}

			String responseMsg = response.getEntity(String.class);
			testoAttoRet = objectMapper.readValue(responseMsg, TestoAtto.class);
			testoAtto.setId(testoAttoRet.getId());

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

		return testoAtto;
	}

}
