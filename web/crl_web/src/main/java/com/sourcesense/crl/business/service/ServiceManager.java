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

import java.util.List;
import java.util.Map;

/**
 * Interfaccia implementata dai servizi CRL
 * 
 * @author sourcesense
 *
 */
public interface ServiceManager {

	/**
	 * Salvataggio dell'oggetto
	 * 
	 * @param object oggetto da persistere
	 * @return oggetto risultante
	 */
	public Object persist(Object object);

	/**
	 * Aggiornamento dell'oggetto
	 * 
	 * @param object oggetto da aggiornare
	 * @return oggetto risultante
	 */
	public Object merge(Object object);

	/**
	 * Rimozione dell'oggetto
	 * 
	 * @param object oggetto da rimuovere
	 * @return risultato della rimozione
	 */
	public boolean remove(Object object);

	/**
	 * Ritorna tutti gli oggetti disponibili
	 * 
	 * @return elenco di oggetti
	 */
	public List<Object> retrieveAll();

	/**
	 * Ritorna tutti i nomi o le descrizioni degli oggetti disponibili
	 * 
	 * @return elenco di oggetti
	 */
	public Map<String, String> findAll();

	/**
	 * Ritorna l'oggetto tramite l'id
	 * 
	 * @param id id dell'oggetto
	 * @return oggetto
	 */
	public Object findById(String id);

}
