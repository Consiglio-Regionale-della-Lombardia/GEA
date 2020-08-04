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

/**
 * Errore di servizio non disponibile
 * 
 * @author sourcesense
 *
 */
public class ServiceNotAvailableException extends RuntimeException {

	private String serviceName;

	public ServiceNotAvailableException() {

	}

	public ServiceNotAvailableException(String serviceName) {

		setServiceName(serviceName);
	}

	public ServiceNotAvailableException(String serviceName, Throwable t) {
		super(t);
		setServiceName(serviceName);

	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
