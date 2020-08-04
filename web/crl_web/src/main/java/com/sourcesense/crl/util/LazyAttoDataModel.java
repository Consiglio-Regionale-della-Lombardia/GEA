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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.sourcesense.crl.business.model.Atto;

/**
 * Utilità per la paginazione e l'ordinamento degli atti nelle pagine web
 * 
 * @author sourcesense
 *
 */
public class LazyAttoDataModel extends LazyDataModel<Atto> {

	private List<Atto> datasource = new ArrayList<Atto>();

	public LazyAttoDataModel(List<Atto> datasource) {
		this.datasource = datasource;
	}

	@Override
	public void setRowIndex(final int rowIndex) {
		/*
		 * The following is in ancestor (LazyDataModel): this.rowIndex = rowIndex == -1
		 * ? rowIndex : (rowIndex % pageSize);
		 */
		if (rowIndex == -1 || getPageSize() == 0) {
			super.setRowIndex(-1);
		} else
			super.setRowIndex(rowIndex % getPageSize());
	}

	@Override
	public List<Atto> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, String> filters) {
		List<Atto> data = new ArrayList<Atto>();

		for (Atto atto : datasource) {
			boolean match = true;

			for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
				try {
					String filterProperty = it.next();
					String filterValue = filters.get(filterProperty);
					String fieldValue = String.valueOf(atto.getClass().getField(filterProperty).get(atto));

					if (filterValue == null || fieldValue.startsWith(filterValue)) {
						match = true;
					} else {
						match = false;
						break;
					}
				} catch (Exception e) {
					match = false;
				}
			}

			if (match) {
				data.add(atto);
			}
		}

		if (sortField != null) {
			Collections.sort(data, new LazySorter(sortField, sortOrder));
		}

		int dataSize = data.size();
		this.setRowCount(dataSize);

		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}
}
