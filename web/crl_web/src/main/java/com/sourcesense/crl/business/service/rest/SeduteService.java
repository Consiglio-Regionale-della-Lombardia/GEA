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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.sourcesense.crl.business.model.GestioneSedute;
import com.sourcesense.crl.business.model.Seduta;
import com.sourcesense.crl.util.ServiceNotAvailableException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.StreamDataBodyPart;

/**
 * Servizio rest per la gestione delle sedute. Inserimento, aggiornamento e
 * ricerca
 * 
 * @author sourcesense
 *
 */
@Component(value = "seduteService")
@Path("/sedute")
public class SeduteService {

	@Autowired
	Client client;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Ritorna l'elenco delle sedute per un determinato gruppo
	 * 
	 * @param url         url
	 * @param param       gruppo
	 * @param legislatura legislatura
	 * @return elenco delle sedute
	 */
	public List<Seduta> findByGroup(String url, String param, String legislatura) {

		List<Seduta> sedute = null;
		try {
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource.queryParam("provenienza", param)
					.queryParam("legislatura", legislatura).queryParam("summary", "true")
					.accept(MediaType.APPLICATION_JSON + ";charset=utf-8").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
			sedute = objectMapper.readValue(responseMsg, new TypeReference<List<Seduta>>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return sedute;
	}

	/**
	 * Ritorna la seduta secondo una determinata data
	 * 
	 * @param url         url
	 * @param param       gruppo
	 * @param dataSeduta  data della seduta
	 * @param legislatura legislatura
	 * @return seduta
	 */
	public Seduta findByDate(String url, String param, String dataSeduta, String legislatura) {

		Seduta seduta = null;
		try {
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource.queryParam("provenienza", param).queryParam("dataSeduta", dataSeduta)
					.queryParam("legislatura", legislatura).accept(MediaType.APPLICATION_JSON + ";charset=utf-8")
					.get(ClientResponse.class);

			if (response.getStatus() == 404) {
				return null;
			} else if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
			seduta = objectMapper.readValue(responseMsg, new TypeReference<Seduta>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return seduta;
	}

	/**
	 * Creazione della seduta
	 * 
	 * @param url            url
	 * @param gestioneSedute gestione delle sedute
	 * @return seduta
	 */
	public Seduta create(String url, GestioneSedute gestioneSedute) {

		Seduta seduta = null;

		try {
			WebResource webResource = client.resource(url);

			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
			String json = objectMapper.writeValueAsString(gestioneSedute);

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=utf-8")
					.post(ClientResponse.class, json);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

			String responseMsg = response.getEntity(String.class);
			seduta = objectMapper.readValue(responseMsg, Seduta.class);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

		return seduta;

	}

	/**
	 * Rimozione della seduta
	 * 
	 * @param url url
	 */
	public void delete(String url) {

		WebResource webResource = client.resource(url);

		DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=utf-8")
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}

		objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

	}

	/**
	 * Aggiornamento della seduta
	 * 
	 * @param url    url
	 * @param seduta seduta
	 */
	public void mergeSeduta(String url, Seduta seduta) {
		try {
			WebResource webResource = client.resource(url);

			String json = objectMapper.writeValueAsString(seduta);

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=utf-8")
					.post(ClientResponse.class, json);

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
	 * Aggiornamento della gestione della seduta
	 * 
	 * @param url            url
	 * @param gestioneSedute gestione della seduta
	 * @return seduta
	 */
	public Seduta merge(String url, GestioneSedute gestioneSedute) {

		Seduta seduta = null;

		try {
			WebResource webResource = client.resource(url);

			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);

			String json = objectMapper.writeValueAsString(gestioneSedute);

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=utf-8")
					.post(ClientResponse.class, json);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

			String responseMsg = response.getEntity(String.class);
			seduta = objectMapper.readValue(responseMsg, Seduta.class);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

		return seduta;

	}

	/**
	 * Ritorna l'allegato della seduta
	 * 
	 * @param url url
	 * @return contenuto dell'allegato
	 */
	public InputStream getFile(String url) {

		InputStream responseFile = null;

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.type(MediaType.TEXT_PLAIN + ";charset=utf-8").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}

		responseFile = response.getEntity(InputStream.class);

		return responseFile;

	}

	/**
	 * Aggiornamento dell'odg di una seduta
	 * 
	 * @param url      url
	 * @param seduta   seduta
	 * @param stream   contenuto dell'odg
	 * @param allegato allegato
	 * @return allegato
	 */
	public Allegato uploadOdg(String url, Seduta seduta, InputStream stream, Allegato allegato) {

		try {

			WebResource webResource = client.resource(url);
			FormDataMultiPart part = new FormDataMultiPart();
			part.bodyPart(new StreamDataBodyPart("file", stream, allegato.getNome()));
			part.field("id", seduta.getIdSeduta());
			part.field("tipologia", "odg");
			part.field("pubblico", "" + allegato.isPubblico());
			ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
					.header("Accept-Charset", "UTF-8").post(ClientResponse.class, part);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			allegato = objectMapper.readValue(responseMsg, Allegato.class);

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
	 * Aggiornamento del verbale di una seduta
	 * 
	 * @param url      url
	 * @param seduta   seduta
	 * @param stream   contenuto dell'allegato
	 * @param allegato allegato
	 * @return allegato aggiornato
	 */
	public Allegato uploadVerbale(String url, Seduta seduta, InputStream stream, Allegato allegato) {

		try {

			WebResource webResource = client.resource(url);
			FormDataMultiPart part = new FormDataMultiPart();
			part.bodyPart(new StreamDataBodyPart("file", stream, allegato.getNome()));
			part.field("id", seduta.getIdSeduta());
			part.field("tipologia", "verbale");
			part.field("pubblico", "" + allegato.isPubblico());
			ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
					.header("Accept-Charset", "UTF-8").post(ClientResponse.class, part);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			allegato = objectMapper.readValue(responseMsg, Allegato.class);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return allegato;
	}

}
