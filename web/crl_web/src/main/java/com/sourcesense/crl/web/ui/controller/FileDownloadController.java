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
package com.sourcesense.crl.web.ui.controller;

import java.io.InputStream;
import java.net.URLEncoder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.sourcesense.crl.business.model.Allegato;
import com.sourcesense.crl.business.model.TestoAtto;
import com.sourcesense.crl.business.service.AttoRecordServiceManager;

/**
 * Gestisce i download dei documenti
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "fileDownloadController")
@ViewScoped
public class FileDownloadController {

	@ManagedProperty(value = "#{attoRecordServiceManager}")
	private AttoRecordServiceManager attoRecordServiceManager;

	private String fileId;
	private String fileName;
	private String fileMimetype;
	private StreamedContent file;

	/**
	 * Aggiornamento dell'allegato o del testo
	 * 
	 * @param event evento di modifica della riga
	 */
	public void fileUpdate(RowEditEvent event) {

		if (event.getObject() instanceof Allegato) {

			attoRecordServiceManager.updateAllegato((Allegato) event.getObject());

		} else if (event.getObject() instanceof TestoAtto) {

			attoRecordServiceManager.updateTestoAtto((TestoAtto) event.getObject());

		}

	}

	public StreamedContent getFile() {
		String fileToDownload = fileId.replaceAll(":/", "") + "/" + URLEncoder.encode(fileName);

		InputStream stream = attoRecordServiceManager.getFileById(fileToDownload);
		file = new DefaultStreamedContent(stream, fileMimetype, fileName);
		return file;
	}

	public AttoRecordServiceManager getAttoRecordServiceManager() {
		return attoRecordServiceManager;
	}

	public void setAttoRecordServiceManager(AttoRecordServiceManager attoRecordServiceManager) {
		this.attoRecordServiceManager = attoRecordServiceManager;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileMimetype() {
		return fileMimetype;
	}

	public void setFileMimetype(String fileMimetype) {
		this.fileMimetype = fileMimetype;
	}

}
