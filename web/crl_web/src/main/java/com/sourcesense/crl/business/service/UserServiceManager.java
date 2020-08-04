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
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.User;
import com.sourcesense.crl.business.service.rest.UserService;
import com.sourcesense.crl.util.ServiceAuthenticationException;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Gestisce l'utente e le operazioni di autenticazione
 * 
 * @author sourcesense
 *
 */
@Service("userServiceManager")
public class UserServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private UserService userService;

	/**
	 * Esegue l'autenticazione tramite utente e password
	 * 
	 * @param user utente
	 * @return utente completo
	 * @throws ServiceAuthenticationException
	 */
	public User authenticate(User user) throws ServiceAuthenticationException {

		urlBuilder.setAlfrescoSessionTicket(userService
				.getAuthenticationToken(urlBuilder.buildURL("alfresco_context_url", "alfresco_authentication"), user));

		User sessionUser = userService.completeAuthentication(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_gruppi_utente", null), user);
		if (sessionUser.getGruppi().get(0) == null) {

			sessionUser.setSessionGroup(null);
		} else {
			for (int i = 0; i < sessionUser.getGruppi().size(); i++) {

				String nomeGruppo = sessionUser.getGruppi().get(i).getNome();
				if (nomeGruppo.startsWith("COMM_")) {

					sessionUser.getGruppi().get(i).setCommissione(true);
					sessionUser.getGruppi().get(i).setNome(nomeGruppo.substring(nomeGruppo.indexOf("_") + 1));

				}
			}
			sessionUser.setSessionGroup(sessionUser.getGruppi().get(0));
		}

		if (urlBuilder.getAlfrescoSessionTicket() != null) {
			return sessionUser;
		} else {
			return null;
		}

	}

	/**
	 * Esegue l'autenticazione tramite utente e password per avere un accesso in
	 * sola lettura
	 * 
	 * @param user utente
	 * @return utente completo
	 * @throws ServiceAuthenticationException
	 */
	public User authenticateReadOnly(User user) throws ServiceAuthenticationException {

		user.setPassword(urlBuilder.getMessageSource().getMessage("alfresco_guest_password", null, Locale.ITALY));
		user.setUsername(urlBuilder.getMessageSource().getMessage("alfresco_guest_username", null, Locale.ITALY));

		urlBuilder.setAlfrescoSessionTicket(userService
				.getAuthenticationToken(urlBuilder.buildURL("alfresco_context_url", "alfresco_authentication"), user));

		User sessionUser = userService.completeAuthentication(
				urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_gruppi_utente", null), user);

		String nomeGruppo = sessionUser.getGruppi().get(0).getNome();
		sessionUser.setSessionGroup(sessionUser.getGruppi().get(0));
		return sessionUser;

	}

	@Override
	public User persist(Object object) {
		return null;
	}

	@Override
	public User merge(Object object) {
		return null;
	}

	@Override
	public boolean remove(Object object) {
		return false;
	}

	@Override
	public Map<String, String> findAll() {
		return null;
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
