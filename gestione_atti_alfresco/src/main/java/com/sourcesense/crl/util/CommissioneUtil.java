package com.sourcesense.crl.util;



import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.sourcesense.crl.job.anagrafica.DataExtractor;

public class CommissioneUtil {
/*
 * Classe che serve funzinalità di appoggio per la gestione delle commissioni
 */
	
	
	 private static Log logger = LogFactory.getLog(CommissioneUtil.class);
	/**
	 * 
	 * @param nomeLegislaturaCorrente
	 * @param committeOrder
	 * @return
	 */
	public String generaIdCommissione(String nomeCommissione, String nomeLegislaturaCorrente,int committeOrder,int identificatoreCommissione) {
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyyMMdd_HHmmss");
		Date now = new Date();
		String nowTime = fmt2.print(now.getTime());
		logger.debug("Nuovo ID Commissione per: " + nomeCommissione + ":" + nomeLegislaturaCorrente + committeOrder + identificatoreCommissione);
		//attenzione alle Giunte con campor ordine 1000 tutti uguale
		return ""+ nomeLegislaturaCorrente + committeOrder + identificatoreCommissione ;
	}
}
