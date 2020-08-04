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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utilità per le date
 * 
 * @author sourcesense
 *
 */
public class DateUtils {

	/**
	 * Genera una data di scadenza secondo il formato dd/MM/yyyy a partire dal
	 * 01/08/
	 * 
	 * @param dateIn             data da modificare
	 * @param sospensioneFeriale specifica se considerare o no le ferie
	 * @return data generata
	 */
	public static Date generateDataScadenzaParDgrInterruzione(Date dateIn, boolean sospensioneFeriale) {

		try {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(dateIn);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			if (sospensioneFeriale) {

				Calendar calendarAppo = new GregorianCalendar();
				calendarAppo.setTime(dateIn);

				Date dataStart = sdf.parse("01/08/" + calendar.get(Calendar.YEAR));

				int contaMargin = 0;

				while (calendarAppo.getTime().before(dataStart)) {
					calendarAppo.add(Calendar.DAY_OF_YEAR, 1);
					contaMargin++;
				}

				if (contaMargin >= 15) {

					calendar.add(Calendar.DAY_OF_YEAR, 16);

				} else if (contaMargin == 0) {

					Date dataCalc = sdf.parse("01/10/" + calendar.get(Calendar.YEAR));
					calendar.setTime(dataCalc);

				} else {

					calendar.add(Calendar.DAY_OF_YEAR, 61);
				}

			} else {

				calendar.add(Calendar.DAY_OF_YEAR, 16);

			}

			return calendar.getTime();

		} catch (Exception e) {

		}

		return null;

	}

	/**
	 * Generazione dela data di scadenza a seconda del tipo
	 * 
	 * @param dataPresaInCarico  data da modificare
	 * @param isRegolamento      specifica se la data appartiene ad un regolamento
	 * @param sospensioneFeriale specifica se considerare o no le ferie
	 * @return data generata
	 */
	public static Date getDataScadenzaPar(Date dataPresaInCarico, boolean isRegolamento, boolean sospensioneFeriale) {
		if (isRegolamento) {
			return generateDataScadenzaParRegolamento(dataPresaInCarico, sospensioneFeriale);
		} else {
			return generateDataScadenzaParDgr(dataPresaInCarico, sospensioneFeriale);
		}
	}

	/**
	 * Se la data di scadenza è per un dgr viene aggiunto un ritardo di 30 giorni
	 * 
	 * @param dateIn             data da modificare
	 * @param sospensioneFeriale specifica se considerare o no le ferie
	 * @return data generata
	 */
	private static Date generateDataScadenzaParRegolamento(Date dateIn, boolean sospensioneFeriale) {
		return generateDataScadenzaPar(60, dateIn, sospensioneFeriale);
	}

	/**
	 * Se la data di scadenza è per un regolamento viene aggiunto un ritardo di 60
	 * giorni
	 * 
	 * @param dateIn             data da modificare
	 * @param sospensioneFeriale specifica se considerare o no le ferie
	 * @return data generata
	 */
	private static Date generateDataScadenzaParDgr(Date dateIn, boolean sospensioneFeriale) {
		return generateDataScadenzaPar(30, dateIn, sospensioneFeriale);
	}

	/**
	 * Aggiunge un ritardo in ordine di giorni alla data
	 * 
	 * @param DELAY ritardo in giorni della data da modificare
	 * @param dateIn data da modificare
	 * @param sospensioneFeriale specifica se considerare o no le ferie
	 * @return data generata
	 */
	private static Date generateDataScadenzaPar(int DELAY, Date dateIn, boolean sospensioneFeriale) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dateIn);
		if (sospensioneFeriale) {
			final Date PRIMO_AGOSTO = primoAgostoDate(dateIn);
			final Date SEDICI_SETTEMBRE = sediciSettembreDate(dateIn);
			final boolean before = dateIn.before(PRIMO_AGOSTO);
			final boolean between = !before && dateIn.before(SEDICI_SETTEMBRE);
			final boolean after = !before && !between;
			if (before) {
				final int delta = numDaysBeetwen(dateIn, PRIMO_AGOSTO);
				if (delta > DELAY) {
					calendar.add(Calendar.DAY_OF_YEAR, DELAY);
				} else if (delta > 0 && delta <= DELAY) {
					calendar.add(Calendar.DAY_OF_YEAR, DELAY + 46);
				} else if (delta == 0) {
					calendar.add(Calendar.DAY_OF_YEAR, DELAY);
				}
			} else if (between) {
				calendar.setTime(SEDICI_SETTEMBRE);
				calendar.add(Calendar.DAY_OF_YEAR, DELAY);
			} else {
				calendar.add(Calendar.DAY_OF_YEAR, DELAY);
			}
		} else {
			calendar.add(Calendar.DAY_OF_YEAR, DELAY);
		}
		return calendar.getTime();
	}

	/**
	 * Ritorna il numero di giorni tra le due date selezionate
	 * 
	 * @param dateIn data da modificare
	 * @param startDate data di partenza da cui partire con la modifica
	 * @return intervallo di giorni tra le due date
	 */
	private static int numDaysBeetwen(Date dateIn, Date startDate) {
		int numDays = 0;
		Calendar calendarDateIn = new GregorianCalendar();
		calendarDateIn.setTime(dateIn);

		while (calendarDateIn.getTime().before(startDate)) {
			calendarDateIn.add(Calendar.DAY_OF_YEAR, 1);
			numDays++;
		}

		return numDays;
	}

	/**
	 * Aggiunge il primo agosto alla data selezionata
	 * 
	 * @param dateIn data da modificare
	 * @return data aggiornata
	 */
	private static Date primoAgostoDate(Date dateIn) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dateIn);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dataPrimoAgosto;
		try {
			dataPrimoAgosto = sdf.parse("01/08/" + calendar.get(Calendar.YEAR));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unparsable Date");
		}
		return dataPrimoAgosto;
	}

	/**
	 * Aggiunge il sedici settembre alla data selezionata
	 * 
	 * @param dateIn data da modificare
	 * @return data aggiornata
	 */
	private static Date sediciSettembreDate(Date dateIn) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dateIn);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dataSediciSettembre;
		try {
			dataSediciSettembre = sdf.parse("16/09/" + calendar.get(Calendar.YEAR));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unparsable Date");
		}
		return dataSediciSettembre;
	}
}
