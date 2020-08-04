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
package com.sourcesense.crl.business.security;

import org.codehaus.jackson.map.annotate.JsonRootName;

/*
 * { "data": {
 * "ticket":"TICKET_da34b98e4a5ac05368a347b3a706246baf3e5659" } }
 */

/**
 * Fornisce il ticket alfresco per la sessione
 * 
 * @author sourcesense
 *
 */
@JsonRootName(value = "data")
public class AlfrescoSessionTicket {

	String ticket;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

}
