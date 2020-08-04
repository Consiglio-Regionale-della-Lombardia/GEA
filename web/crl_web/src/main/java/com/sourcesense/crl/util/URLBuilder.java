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
package com.sourcesense.crl.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.sourcesense.crl.business.security.AlfrescoSessionTicket;

/**
 * Utilità per la creazione degli url
 * 
 * @author sourcesense
 *
 */
public class URLBuilder {

	public static final String ALFRESCO_TCKT_PARAM_NAME = "alf_ticket=";
	public static final String ALFRESCO_DWNL_TCKT_PARAM_NAME = "ticket=";

	@Autowired
	MessageSource messageSource;

	AlfrescoSessionTicket alfrescoSessionTicket;

	/**
	 * Costruisce l'url di download per i documenti di Alfresco passando il nome del file
	 * 
	 * @param contextPropertyName nome del context
	 * @param fileName nome del file
	 * @param paramsValues parametri dell'url
	 * @return url di download
	 */
	public String buildAlfrescoDownloadURL(String contextPropertyName, String fileName, String[] paramsValues) {

		String url = "";
		encodeParams(paramsValues);
		url = messageSource.getMessage(contextPropertyName, null, Locale.ITALY) + fileName;

		if (url.indexOf("?") == -1) {
			url += "?" + ALFRESCO_DWNL_TCKT_PARAM_NAME + alfrescoSessionTicket.getTicket();
		} else {

			url += "&" + ALFRESCO_DWNL_TCKT_PARAM_NAME + alfrescoSessionTicket.getTicket();
		}

		return url.replaceAll(" ", "%20");
	}

	/**
	 * Costruisce l'url di download per i documenti di Alfresco passando il path del file
	 * 
	 * @param contextPropertyName nome del context
	 * @param fileName nome del file
	 * @param paramsValues parametri dell'url
	 * @return url di download
	 */
	public String buildAlfrescoURL(String contextPropertyName, String pathPropertyName, String[] paramsValues) {

		String url = "";
		encodeParams(paramsValues);
		url = messageSource.getMessage(contextPropertyName, null, Locale.ITALY)
				+ messageSource.getMessage(pathPropertyName, paramsValues, Locale.ITALY);

		url = url.trim();

		if (url.indexOf("?") == -1) {
			url += "?" + ALFRESCO_TCKT_PARAM_NAME + alfrescoSessionTicket.getTicket();
		} else {

			url += "&" + ALFRESCO_TCKT_PARAM_NAME + alfrescoSessionTicket.getTicket();
		}

		return url.replaceAll(" ", "%20");
	}

	/**
	 * Costruisce un url tramite il path passato
	 * 
	 * @param contextPropertyName nome del context
	 * @param pathPropertyName nome del path del file
	 * @return url di download
	 */
	public String buildURL(String contextPropertyName, String pathPropertyName) {

		String url = "";

		url = messageSource.getMessage(contextPropertyName, null, Locale.ITALY)
				+ messageSource.getMessage(pathPropertyName, null, Locale.ITALY);

		return url;
	}

	/**
	 * Costruisce un url tramite il path passato
	 * 
	 * @param contextPropertyName nome del context
	 * @param paramValues nome del path del file
	 * @return url di download
	 */
	public String buildSimpleURL(String contextPropertyName, String[] paramsValues) {

		String url = "";
		encodeParams(paramsValues);
		url = messageSource.getMessage(contextPropertyName, paramsValues, Locale.ITALY);

		return url.replaceAll(" ", "%20");
	}

	/**
	 * Esegue l'encoding dei parametri passati
	 * 
	 * @param paramsValues parametri da codificare
	 */
	private void encodeParams(String[] paramsValues) {

		try {
			if (paramsValues != null) {
				for (int i = 0; i < paramsValues.length; i++) {
					paramsValues[i] = URLEncoder.encode(paramsValues[i], "UTF-8");
				}
			}
		} catch (UnsupportedEncodingException uee) {

		}

	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public AlfrescoSessionTicket getAlfrescoSessionTicket() {
		return alfrescoSessionTicket;
	}

	public void setAlfrescoSessionTicket(AlfrescoSessionTicket alfrescoSessionTicket) {
		this.alfrescoSessionTicket = alfrescoSessionTicket;
	}

}
