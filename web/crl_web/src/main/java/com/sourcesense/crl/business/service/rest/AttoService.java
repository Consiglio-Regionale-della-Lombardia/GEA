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
import java.util.ArrayList;
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
import com.sourcesense.crl.business.model.AttoEAC;
import com.sourcesense.crl.business.model.AttoMIS;
import com.sourcesense.crl.business.model.Collegamento;
import com.sourcesense.crl.business.model.CollegamentoAttiSindacato;
import com.sourcesense.crl.business.model.ConsultazioneParere;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.util.ServiceNotAvailableException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.StreamDataBodyPart;

/**
 * Servizio rest per la gestione degli atti. Inserimento, aggiornamento e
 * ricerca
 * 
 * @author sourcesense
 *
 */
@Component(value = "attoService")
@Path("/atti")
public class AttoService {

	@Autowired
	Client client;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Creazione dell'atto
	 * 
	 * @param url  url
	 * @param atto atto
	 * @return atto creato
	 */
	public Atto create(String url, Atto atto) {

		Atto attoRet = null;

		WebResource webResource = client.resource(url);
		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
		try {
			String json = objectMapper.writeValueAsString(atto);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() == 500) {

				atto.setError("Attenzione : l'atto " + atto.getNumeroAtto() + " è già presente");

			} else if (response.getStatus() != 200) {

				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");

			} else {

				String responseMsg = response.getEntity(String.class);
				attoRet = objectMapper.readValue(responseMsg, Atto.class);
				atto.setId(attoRet.getId());
				atto.setNumeroAtto(attoRet.getNumeroAtto());
				atto.setPubblico(attoRet.isPubblico());
			}

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return atto;
	}

	/**
	 * Creazione dell'atto MIS
	 * 
	 * @param url  url
	 * @param atto atto
	 * @return atto creato
	 */
	public AttoMIS createMIS(String url, AttoMIS atto) {

		AttoMIS attoRet = null;

		WebResource webResource = client.resource(url);
		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
		try {
			String json = objectMapper.writeValueAsString(atto);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() == 500) {
				atto.setError("Attenzione : l'atto " + atto.getNumeroAtto() + " è già presente");

			} else if (response.getStatus() != 200) {

				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");

			} else {

				String responseMsg = response.getEntity(String.class);
				atto.setError(null);
				attoRet = objectMapper.readValue(responseMsg, AttoMIS.class);
				atto.setId(attoRet.getId());
			}
		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return atto;
	}

	/**
	 * Aggiornamento dell'atto
	 * 
	 * @param url  url
	 * @param atto atto
	 * @return atto aggiornato
	 */
	public Atto updateAtto(String url, Atto atto) {

		Atto attoRet = null;

		WebResource webResource = client.resource(url);

		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
		try {
			String json = objectMapper.writeValueAsString(atto);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

			if (response.getStatus() == 500) {
				atto.setError("Attenzione : l'atto " + atto.getNumeroAtto() + " è già presente");

			} else if (response.getStatus() != 200) {

				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");

			} else {

				String responseMsg = response.getEntity(String.class);
				atto.setError(null);
			}
		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return atto;
	}

	/**
	 * Rimozione dell'atto
	 * 
	 * @param url url
	 */
	public void deleteAtto(String url) {

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}
	}

	/**
	 * Creazione dell'atto EAC
	 * 
	 * @param url  url
	 * @param atto atto
	 * @return atto creato
	 */
	public AttoEAC createEAC(String url, AttoEAC atto) {

		AttoEAC attoRet = null;

		WebResource webResource = client.resource(url);
		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
		try {
			String json = objectMapper.writeValueAsString(atto);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() == 500) {
				atto.setError("Attenzione : l'atto " + atto.getNumeroAtto() + " è già presente");

			} else if (response.getStatus() != 200) {

				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");

			} else {

				String responseMsg = response.getEntity(String.class);
				atto.setError(null);
				attoRet = objectMapper.readValue(responseMsg, AttoEAC.class);
				atto.setId(attoRet.getId());
			}
		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return atto;
	}

	/**
	 * Non implementato
	 * 
	 * @param atto atto
	 * @return true
	 */
	public boolean update(Atto atto) {
		return true;
	}

	/**
	 * Ricerca dell'atto tramite url
	 * 
	 * @param url id
	 * @return atto
	 */
	public Atto findById(String url) {
		Atto atto = null;

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}
		String responseMsg = response.getEntity(String.class);

		try {

			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
			atto = objectMapper.readValue(responseMsg, Atto.class);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

		return atto;
	}

	/**
	 * Ricerca dell'atto EAC tramite url
	 * 
	 * @param url id
	 * @return atto EAC
	 */
	public AttoEAC findEACById(String url) {
		AttoEAC atto = null;

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}
		String responseMsg = response.getEntity(String.class);

		try {
			atto = objectMapper.readValue(responseMsg, AttoEAC.class);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

		return atto;
	}

	/**
	 * Ricerca dell'atto MIS tramite url
	 * 
	 * @param url id
	 * @return atto MIS
	 */
	public AttoMIS findMISById(String url) {
		AttoMIS atto = null;

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}
		String responseMsg = response.getEntity(String.class);

		try {

			atto = objectMapper.readValue(responseMsg, AttoMIS.class);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

		return atto;
	}

	/**
	 * Non implementato
	 * 
	 * @return null
	 */
	public ArrayList<Atto> findAll() {
		return null;
	}

	/**
	 * Aggiornamento dell'allegato
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
			FormDataMultiPart part = new FormDataMultiPart();
			part.bodyPart(new StreamDataBodyPart("file", stream, allegato.getNome()));
			part.field("id", atto.getId());
			part.field("tipologia", tipologia);
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
	 * Aggiornamento dell'allegato del parere
	 * 
	 * @param url       url
	 * @param atto      atto
	 * @param stream    contenuto dell'allegato
	 * @param allegato  allegato
	 * @param tipologia tipologia
	 * @return allegato aggiornato
	 */
	public Allegato uploadAllegatoParere(String url, Atto atto, InputStream stream, Allegato allegato,
			String tipologia) {

		try {

			WebResource webResource = client.resource(url);
			FormDataMultiPart part = new FormDataMultiPart();
			part.bodyPart(new StreamDataBodyPart("file", stream, allegato.getNome()));
			part.field("id", atto.getId());
			part.field("tipologia", tipologia);
			part.field("organismoStatutario", allegato.getOrganismoStatutario());
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
	 * Aggiornamento dell'allegato della consultazione
	 * 
	 * @param url       url
	 * @param atto      atto
	 * @param stream    contenuto dell'allegato
	 * @param allegato  allegato
	 * @param tipologia tipologia
	 * @return allegato aggiornato
	 */
	public Allegato uploadAllegatoConsultazioni(String url, Atto atto, InputStream stream, Allegato allegato,
			String tipologia) {

		try {

			WebResource webResource = client.resource(url);
			FormDataMultiPart part = new FormDataMultiPart();
			part.bodyPart(new StreamDataBodyPart("file", stream, allegato.getNome()));
			part.field("id", atto.getId());
			part.field("tipologia", tipologia);
			part.field("consultazione", allegato.getConsultazione());
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
	 * Rimozione del firmatario
	 * 
	 * @param url url
	 */
	public void removeFirmatario(String url) {

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}

	}

	/**
	 * Rimozione dell'atto
	 * 
	 * @param url url
	 */
	public void removeAtto(String url) {

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new ServiceNotAvailableException(
					"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
		}

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
			part.field("pubblico", "" + testoAtto.isPubblico());

			ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
					.header("Accept-Charset", "UTF-8").post(ClientResponse.class, part);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
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

	/**
	 * Modifica dell'allegato
	 * 
	 * @param url url
	 * @return testo dell'atto
	 */
	public TestoAtto changeAllegato(String url) {

		TestoAtto attoRecord = null;

		try {

			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.post(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);
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

	/**
	 * Ricerca del tipo atti del sindacato
	 * 
	 * @param url url
	 * @return elenco dei tipi di atto
	 */
	public List<String> findTipoAttiSindacato(String url) {
		List<String> listTipi = null;

		try {
			WebResource webResource = client.resource(url);
			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);

			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
			listTipi = objectMapper.readValue(responseMsg, new TypeReference<List<String>>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listTipi;

	}

	/**
	 * Ritorna tutti gli atti del sindacato secondo l'url
	 * 
	 * @param url url
	 * @return elenco dei collegamenti degli atti del sindacato
	 */
	public List<CollegamentoAttiSindacato> findAllAttiSindacato(String url) {
		List<CollegamentoAttiSindacato> listAtti = null;

		try {
			WebResource webResource = client.resource(url);
			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);

			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
			listAtti = objectMapper.readValue(responseMsg, new TypeReference<List<CollegamentoAttiSindacato>>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listAtti;

	}

	/**
	 * Ritorna il collegamento ad un atto tramite url
	 * 
	 * @param url url
	 * @return elenco dei collegamenti
	 */
	public List<Collegamento> findCollegamentiAttoById(String url) {
		List<Collegamento> listAtti = null;

		try {
			WebResource webResource = client.resource(url);
			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);

			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
			listAtti = objectMapper.readValue(responseMsg, new TypeReference<List<Collegamento>>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listAtti;

	}

	/**
	 * Ritorna l'elenco degli atti tramite url
	 * 
	 * @param atto atto
	 * @param url  url
	 * @return elenco degli atti
	 */
	public List<Atto> parametricSearch(Atto atto, String url) {
		List<Atto> listAtti = null;

		try {
			WebResource webResource = client.resource(url);
			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
			String json = objectMapper.writeValueAsString(atto);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);

			objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
			listAtti = objectMapper.readValue(responseMsg, new TypeReference<List<Atto>>() {
			});

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}
		return listAtti;

	}

	/**
	 * Aggiornamento dell'atto
	 * 
	 * @param url  url
	 * @param atto atto
	 */
	public void merge(String url, Atto atto) {

		try {
			WebResource webResource = client.resource(url);
			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
			String json = objectMapper.writeValueAsString(atto);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() != 200) {
				throw new ServiceNotAvailableException(
						"Errore - " + response.getStatus() + ": Alfresco non raggiungibile ");
			}

			String responseMsg = response.getEntity(String.class);

		} catch (JsonMappingException e) {

			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (JsonParseException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);

		} catch (IOException e) {
			throw new ServiceNotAvailableException(this.getClass().getSimpleName(), e);
		}

	}

	/**
	 * Salvataggio dei pareri
	 * 
	 * @param url url
	 * @param consultazioneParere consultazione parere
	 */
	public void salvaPareri(String url, ConsultazioneParere consultazioneParere) {

		try {
			WebResource webResource = client.resource(url);

			DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			objectMapper.getSerializationConfig().setDateFormat(myDateFormat);

			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
			objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, false);
			objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

			String json = objectMapper.writeValueAsString(consultazioneParere);
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

}
