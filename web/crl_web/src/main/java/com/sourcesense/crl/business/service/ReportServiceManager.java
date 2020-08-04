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

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcesense.crl.business.model.Report;
import com.sourcesense.crl.business.service.rest.ReportService;
import com.sourcesense.crl.util.URLBuilder;

/**
 * Recupera e aggiorna i report
 * 
 * @author sourcesense
 *
 */
@Service("reportServiceManager")
public class ReportServiceManager implements ServiceManager {

	@Autowired
	private URLBuilder urlBuilder;

	@Autowired
	private ReportService reportService;

	/**
	 * Ritorna il contenuto binario del report
	 * 
	 * @param report report
	 * @return contenuto del report
	 */
	public InputStream getReportFile(Report report) {
		return reportService
				.getFile(urlBuilder.buildAlfrescoURL("alfresco_context_url", "alf_retrieve_report_bin", null), report);
	}

	@Override
	public Object persist(Object object) {
		return null;
	}

	@Override
	public Object merge(Object object) {
		return null;
	}

	@Override
	public boolean remove(Object object) {
		return false;
	}

	@Override
	public List<Object> retrieveAll() {
		return null;
	}

	@Override
	public Map<String, String> findAll() {
		return null;
	}

	@Override
	public Object findById(String id) {
		return null;
	}

}
