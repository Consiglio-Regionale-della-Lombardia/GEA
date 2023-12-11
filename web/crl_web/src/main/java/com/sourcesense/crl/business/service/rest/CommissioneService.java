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
import com.sourcesense.crl.business.model.Atto;
import com.sourcesense.crl.business.model.Commissione;
import com.sourcesense.crl.business.model.CommissioneConsultiva;
import com.sourcesense.crl.business.model.CommissioneReferente;
import com.sourcesense.crl.business.model.EsameCommissione;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.util.ServiceNotAvailableException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.StreamDataBodyPart;

/**
 * Servizio rest per la gestione delle commissioni. Inserimento, aggiornamento e ricerca
 * 
 * @author sourcesense
 *
 */
@Component(value = "commissioneService")
@Path("/commissioni")
public class CommissioneService {

	@Autowired
	Client client;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Aggiornamento dell'esame della commissione
	 * 
	 * @param url url
	 * @param esameCommissione esame di commissione
	 */
	public void merge(String url, EsameCommissione esameCommissione) {
		try {
			WebResource webResource = client.resource(url);

			DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			objectMapper.getSerializationConfig().setDateFormat(myDateFormat);

			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
			objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, false);
			objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

			String json = objectMapper.writeValueAsString(esameCommissione);

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

	}

	/**
	 * Rimozione dell'abbinamento
	 * 
	 * @param url url
	 */
	public void removeAbbinamento(String url) {

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}

		objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

	}

	/**
	 * Ritorna l'elenco delle commissioni per atto
	 * 
	 * @param url url
	 * @return elenco delle commissioni per atto
	 */
	public List<CommissioneReferente> retrieveCommissioniReferenteByAtto(String url) {

		List<CommissioneReferente> listCommissioniReferenti = null;

		try {
			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
			listCommissioniReferenti = objectMapper.readValue(responseMsg,
					new TypeReference<List<CommissioneReferente>>() {
					});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listCommissioniReferenti;
	}

	/**
	 * Ritorna l'elenco delle commissioni consultive per atto
	 * 
	 * @param url url
	 * @return elenco delle commissioni consultive per atto
	 */
	public List<CommissioneConsultiva> retrieveCommissioniConsultiveByAtto(String url) {

		List<CommissioneConsultiva> listCommissioniConsultive = null;

		try {
			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
			listCommissioniConsultive = objectMapper.readValue(responseMsg,
					new TypeReference<List<CommissioneConsultiva>>() {
					});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listCommissioniConsultive;
	}

	/**
	 * Ritorna l'elenco di tutte le commissioni per url
	 * 
	 * @param url url
	 * @return elenco di tutte le commissioni per url
	 */
	public List<Commissione> getAllCommissioni(String url) {
		List<Commissione> listCommissioni = null;

		try {
			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
			listCommissioni = objectMapper.readValue(responseMsg, new TypeReference<List<Commissione>>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listCommissioni;
	}

	/**
	 * Aggiornamento dell'allegato dell'atto
	 * 
	 * @param url       url
	 * @param atto      atto
	 * @param stream    contenuto dell'allegato
	 * @param allegato  allegato
	 * @param tipologia tipologia
	 * @return allegato aggiornato
	 */
	public Allegato uploadAllegato(String url, Atto atto, InputStream stream, Allegato allegato, String tipologia) {

		try {

			WebResource webResource = client.resource(url);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			FormDataMultiPart part = new FormDataMultiPart();
			String dataSeduta = (allegato.getDataSeduta() == null) ? "" : format.format(allegato.getDataSeduta());
			part.bodyPart(new StreamDataBodyPart("file", stream, allegato.getNome()));
			part.field("id", atto.getId());
			part.field("pubblico", allegato.isPubblico() + "");
			part.field("provenienza", allegato.getCommissione());
			part.field("passaggio", allegato.getPassaggio());

			part.field("dataSeduta", dataSeduta);

			part.field("tipologia", tipologia);

			ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
					.header("Accept-Charset", "UTF-8").post(ClientResponse.class, part);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
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
	 * Aggiornamento del testo dell'atto
	 * 
	 * @param url       url
	 * @param atto      atto
	 * @param stream    contenuto del testo dell'atto
	 * @param testoAtto testo dell'atto
	 * @param tipologia tipologia
	 * @return testo dell'atto aggiornato
	 */
	public TestoAtto uploadTestoAtto(String url, Atto atto, InputStream stream, TestoAtto testoAtto, String tipologia) {

		TestoAtto attoRecord = null;

		try {

			WebResource webResource = client.resource(url);
			FormDataMultiPart part = new FormDataMultiPart();
			part.bodyPart(new StreamDataBodyPart("file", stream, testoAtto.getNome()));
			part.field("id", atto.getId());
			part.field("tipologia", tipologia);
			part.field("tipologiaTesto", testoAtto.getTipologiaTesto());
			part.field("commissione", testoAtto.getCommissione());
			part.field("passaggio", testoAtto.getPassaggio());
			part.field("pubblico", "" + testoAtto.isPubblico());
			part.field("pubblicoOpendata", "" + testoAtto.isPubblicoOpendata());

			ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
					.header("Accept-Charset", "UTF-8").post(ClientResponse.class, part);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
			attoRecord = objectMapper.readValue(responseMsg, TestoAtto.class);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return attoRecord;
	}

}
