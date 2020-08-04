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

import com.sourcesense.crl.business.model.StatoAtto;
import com.sourcesense.crl.business.service.rest.StatoAttoService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Gestione degli stati degli atti. Possono essere dei seguenti tipi:
 * PROTOCOLLATO, PRESO_CARICO_SC, VERIFICATA_AMMISSIBILITA,
 * PROPOSTA_ASSEGNAZIONE, ASSEGNATO_COMMISSIONE, PRESO_CARICO_COMMISSIONE,
 * NOMINATO_RELATORE, VOTATO_COMMISSIONE, TRASMESSO_COMMISSIONE,
 * LAVORI_COMITATO_RISTRETTO, TRASMESSO_AULA, PRESO_CARICO_AULA, VOTATO_AULA,
 * PUBBLICATO, CHIUSO
 * 
 * @author sourcesense
 *
 */
@Service("statoAttoServiceManager")
public class StatoAttoServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private StatoAttoService statoAttoService;

	@Override
	public StatoAtto persist(Object object) {

		return null;
	}

	@Override
	public StatoAtto merge(Object object) {

		return null;
	}

	@Override
	public boolean remove(Object object) {

		return false;
	}

	@Override
	public Map<String, String> findAll() {
		Map<String, String> stati = new HashMap<String, String>();

		stati.put(StatoAtto.PROTOCOLLATO, StatoAtto.PROTOCOLLATO);
		stati.put(StatoAtto.PRESO_CARICO_SC, StatoAtto.PRESO_CARICO_SC);
		stati.put(StatoAtto.VERIFICATA_AMMISSIBILITA, StatoAtto.VERIFICATA_AMMISSIBILITA);
		stati.put(StatoAtto.PROPOSTA_ASSEGNAZIONE, StatoAtto.PROPOSTA_ASSEGNAZIONE);
		stati.put(StatoAtto.ASSEGNATO_COMMISSIONE, StatoAtto.ASSEGNATO_COMMISSIONE);
		stati.put(StatoAtto.PRESO_CARICO_COMMISSIONE, StatoAtto.PRESO_CARICO_COMMISSIONE);
		stati.put(StatoAtto.NOMINATO_RELATORE, StatoAtto.NOMINATO_RELATORE);
		stati.put(StatoAtto.VOTATO_COMMISSIONE, StatoAtto.VOTATO_COMMISSIONE);
		stati.put(StatoAtto.TRASMESSO_COMMISSIONE, StatoAtto.TRASMESSO_COMMISSIONE);
		stati.put(StatoAtto.LAVORI_COMITATO_RISTRETTO, StatoAtto.LAVORI_COMITATO_RISTRETTO);
		stati.put(StatoAtto.TRASMESSO_AULA, StatoAtto.TRASMESSO_AULA);
		stati.put(StatoAtto.PRESO_CARICO_AULA, StatoAtto.PRESO_CARICO_AULA);
		stati.put(StatoAtto.VOTATO_AULA, StatoAtto.VOTATO_AULA);
		stati.put(StatoAtto.PUBBLICATO, StatoAtto.PUBBLICATO);
		stati.put(StatoAtto.CHIUSO, StatoAtto.CHIUSO);

		return stati;
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
