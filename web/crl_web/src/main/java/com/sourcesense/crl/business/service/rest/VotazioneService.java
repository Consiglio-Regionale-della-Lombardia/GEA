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

import java.util.List;

import javax.ws.rs.Path;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcesense.crl.business.model.Votazione;
import com.sun.jersey.api.client.Client;

/**
 * Servizio rest per la gestione delle votazioni. Inserimento, aggiornamento e
 * ricerca
 * 
 * @author sourcesense
 *
 */
@Component(value = "votazioneService")
@Path("/votazioni")
public class VotazioneService {

	@Autowired
	Client client;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Non implementato
	 * 
	 * @return null
	 */
	public List<Votazione> getAllEsitoVotoAula() {
		return null;
	}

	/**
	 * Non implementato
	 * 
	 * @return null
	 */
	public List<Votazione> getAllEsitoVotoCommissioneReferente() {

		return null;
	}

}
