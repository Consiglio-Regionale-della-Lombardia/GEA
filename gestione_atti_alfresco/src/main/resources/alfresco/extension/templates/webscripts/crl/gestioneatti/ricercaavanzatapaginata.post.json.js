<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

function creaLuceneQueryCommissioniRuoli(luceneQuery, commissione, ruoloCommissione, workingList){
	if(checkIsNotNull(commissione)){
		var commissioneString = ""+commissione+"";
		if(checkIsNotNull(ruoloCommissione)){
			var ruoloCommissioneString = ""+ruoloCommissione+"";
			if(ruoloCommissioneString=="Referente"){
				luceneQuery = verifyAND(luceneQuery);
				luceneQuery += "@crlatti\\:commReferente:\""+commissioneString+"\"";
			} else if(ruoloCommissioneString=="Co-Referente"){
				luceneQuery = verifyAND(luceneQuery);
				luceneQuery += "@crlatti\\:commCoreferente:\""+commissioneString+"\"";
			} else if(ruoloCommissioneString=="Consultiva"){
				luceneQuery = verifyAND(luceneQuery);
				luceneQuery += "@crlatti\\:commConsultiva:\""+commissioneString+"\"";
			} else if(ruoloCommissioneString=="Redigente"){
				luceneQuery = verifyAND(luceneQuery);
				luceneQuery += "@crlatti\\:commRedigente:\""+commissioneString+"\"";
			} else if(ruoloCommissioneString=="Deliberante"){
				luceneQuery = verifyAND(luceneQuery);
				luceneQuery += "@crlatti\\:commDeliberante:\""+commissioneString+"\"";
			}
		} else {
			luceneQuery = verifyAND(luceneQuery);
			luceneQuery += " ( " +
					"@crlatti\\:commReferente:\""+commissioneString+"\" OR "+
					"@crlatti\\:commCoreferente:\""+commissioneString+"\" OR "+
					"@crlatti\\:commConsultiva:\""+commissioneString+"\" OR "+
					"@crlatti\\:commRedigente:\""+commissioneString+"\" OR "+
					"@crlatti\\:commDeliberante:\""+commissioneString+"\" ";
                                        // SCRL-124
                                        if(workingList == "inlavorazione") {
                                            luceneQuery += "OR (TYPE:\"crlatti:attoMis\" AND @crlatti\\:commissioneCompetenteMis:\""+commissioneString+"\" AND ISNOTNULL:\"crlatti:dataPropostaCommissioneMis\" AND ISNULL:\"crlatti:dataIntesaMis\") ";
                                        }
					luceneQuery += ") ";
		}
	}
	return luceneQuery;
}

var atto = json.get("atto");

var isHome = getObj(atto, "isHome");

var tipoAtto = getObj(atto, "tipoAtto");
var legislatura = getObj(atto, "legislatura");
var stato = getObj(atto, "stato");

var numeroAttoDa = getObj(atto, "numeroAttoDa");
var numeroAttoA = getObj(atto, "numeroAttoA");
var estensioneAtto=getObj(atto, "estensioneAtto");

var numeroProtocollo = getObj(atto, "numeroProtocollo");
var tipoIniziativa = getObj(atto, "tipoIniziativa");
var numeroDcr = getObj(atto, "numeroDcr");
var primoFirmatario = getObj(atto, "primoFirmatario");
var gruppoPrimoFirmatario = getObj(atto, "gruppoPrimoFirmatario");
var oggetto = getObj(atto, "oggetto");
var firmatario = getObj(atto, "firmatario");
var gruppoFirmatario = getObj(atto, "gruppoFirmatario");
var dataIniziativaDa = getObj(atto, "dataIniziativaDa");
var dataIniziativaA = getObj(atto, "dataIniziativaA");

var dataAssegnazioneDa = getObj(atto, "dataAssegnazioneDa");
var dataAssegnazioneA = getObj(atto, "dataAssegnazioneA");

var dataChiusuraDa = getObj(atto, "dataChiusuraDa");
var dataChiusuraA = getObj(atto, "dataChiusuraA");

var dataLr = getObj(atto, "dataLR");

var commissione1 = getObj(atto, "commissione1");
var commissione2 = getObj(atto, "commissione2");
var commissione3 = getObj(atto, "commissione3");
var commissioneUser = getObj(atto, "commissioneUser");

var ruoloCommissione1 = getObj(atto, "ruoloCommissione1");
var ruoloCommissione2 = getObj(atto, "ruoloCommissione2");
var ruoloCommissione3 = getObj(atto, "ruoloCommissione3");

// pannello sinistra
var tipoChiusura = getObj(atto, "tipoChiusura");
var esitoVotoComRef = getObj(atto, "esitoVotoCommissioneReferente");
var esitoVotoAula = getObj(atto, "esitoVotoAula");

var redigente = getObj(atto, "redigente");
var deliberante = getObj(atto, "deliberante");
var numeroLcr = getObj(atto, "numeroLcr");
var numeroLr = getObj(atto, "numeroLr");
var anno = getObj(atto, "anno");
var abbinamento = getObj(atto, "abbinamento");
var stralcio = getObj(atto, "stralcio");

// pannello destra
var dataPubblicazioneDa = getObj(atto, "dataPubblicazioneDa");
var dataPubblicazioneA = getObj(atto, "dataPubblicazioneA");

var dataSedutaScDa = getObj(atto, "dataSedutaSCDa");
var dataSedutaScA = getObj(atto, "dataSedutaSCA");

var dataSedutaCommDa = getObj(atto, "dataSedutaCommissioneDa");
var dataSedutaCommA = getObj(atto, "dataSedutaCommissioneA");

var dataSedutaAulaDa = getObj(atto, "dataSedutaAulaDa");
var dataSedutaAulaA = getObj(atto, "dataSedutaAulaA");

var relatore = getObj(atto, "relatore");
var organismoStatutario = getObj(atto, "organismoStatutario");
var soggettoConsultato = getObj(atto, "soggettoConsultato");
var emendato = getObj(atto, "emendato");
var emendatoAula = getObj(atto, "emendatoAula");
var sospeso = getObj(atto, "sospeso");
var numeroDgr = getObj(atto, "numeroDGR");
var dataDgr = getObj(atto, "dataDGR");

var numeroFascicolo = getObj(atto, "numeroRepertorio");
var rinviato = getObj(atto, "rinviato");

/* Impaginazione ricerca */
var startFrom = parseInt(getObj(atto, "start"));
var limit = parseInt(getObj(atto, "limit"));
var orderby = getObj(atto, "orderby");
var pubblico = getObj(atto, "pubblico");

var luceneQuery = " ";

var tipoAttoString = ""+tipoAtto+"";
var type = "crlatti:atto";

var statiUtente = getObj(atto, "statiUtente");

var ruoloUtente = getObj(atto, "ruoloUtente");
var tipoWorkingList = getObj(atto, "tipoWorkingList");

// atto -> statiUtente -> StatoAtto -> descrizione
// statoAtto = statiUtente
// ruoliCommissione = singolo metadato
// numeroRepertorio = numeroFascicolo
// emendatoAula da ridondare
// emendato in commissione

if(checkIsNotNull(tipoAttoString)){
	if(tipoAttoString == "PDL") {
		type = "crlatti:attoPdl";	
	} else if(tipoAttoString == "INP") {
		type = "crlatti:attoInp";	
	} else if(tipoAttoString == "PAR") {
		type = "crlatti:attoPar";	
	} else if(tipoAttoString == "PDA") {
		type = "crlatti:attoPda";	
	} else if(tipoAttoString == "PLP") {
		type = "crlatti:attoPlp";	
	} else if(tipoAttoString == "PRE") {
		type = "crlatti:attoPre";	
	} else if(tipoAttoString == "REF") {
		type = "crlatti:attoRef";	
	} else if(tipoAttoString == "REL") {
		type = "crlatti:attoRel";	
	} else if(tipoAttoString == "EAC") {
		type = "crlatti:attoEac";
	} else if(tipoAttoString == "REF") {
		type = "crlatti:attoRef";	
	} else if(tipoAttoString == "DOC") {
		type = "crlatti:attoDoc";	
	} else if(tipoAttoString == "ORG") {
		type = "crlatti:attoOrg";	
	} else if(tipoAttoString == "MIS") {
		type = "crlatti:attoMis";	
	}
}

luceneQuery += " TYPE:\""+type+"\"";

if(type=="crlatti:atto"){
	
	if(checkIsNotNull(legislatura)){
		luceneQuery = verifyAND(luceneQuery);
		luceneQuery += "( @crlatti\\:legislatura:\""+legislatura+"\" OR @crlatti\\:legislatura:\"nd\" )";
	}
}
else{
	
	if(checkIsNotNull(legislatura)){
		luceneQuery = verifyAND(luceneQuery);
		luceneQuery += "@crlatti\\:legislatura:\""+legislatura+"\"";
	}
}


if(checkIsNotNull(stato)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:statoAtto:\""+stato+"\"";
}

if(checkIsNotNull(numeroFascicolo)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroRepertorio:\""+numeroFascicolo+"\"";
}

// numeroAtto range
if(checkIsNotNull(numeroAttoDa) && checkIsNotNull(numeroAttoA)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroAtto:["+numeroAttoDa+" TO "+numeroAttoA+"]";
} else if(checkIsNotNull(numeroAttoDa)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroAtto:["+numeroAttoDa+" TO MAX]";
} else if(checkIsNotNull(numeroAttoA)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroAtto:[MIN TO "+numeroAttoA+"]";
}

if (checkIsNotNull(estensioneAtto)){
    luceneQuery = verifyAND(luceneQuery);
    luceneQuery += "@crlatti\\:estensioneAtto:\""+estensioneAtto+"\"";
}

if (estensioneAtto==""){
    luceneQuery+=" -@crlatti\\:estensioneAtto:\"bis\"";
    luceneQuery+=" -@crlatti\\:estensioneAtto:\"ter\"";
    luceneQuery+=" -@crlatti\\:estensioneAtto:\"quater\"";
    luceneQuery+=" -@crlatti\\:estensioneAtto:\"quintus\"";
    luceneQuery+=" -@crlatti\\:estensioneAtto:\"quinquies\"";
    luceneQuery+=" -@crlatti\\:estensioneAtto:\"sextus\"";
}

if(checkIsNotNull(numeroProtocollo)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroProtocollo:\""+numeroProtocollo+"\"";
}

if(checkIsNotNull(tipoIniziativa)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:tipoIniziativa:\""+tipoIniziativa+"\"";
}

if(checkIsNotNull(numeroDcr)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroDcr:\""+numeroDcr+"\"";
}

if(checkIsNotNull(primoFirmatario)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:primoFirmatario:\""+primoFirmatario+"\"";
}

if(checkIsNotNull(oggetto)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:oggetto:\""+oggetto+"\"";
}

if(checkIsNotNull(firmatario)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:firmatari:\""+firmatario+"\"";
}

if(checkIsNotNull(gruppoFirmatario)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:gruppoFirmatari:\""+gruppoFirmatario+"\"";
}

if(checkIsNotNull(gruppoPrimoFirmatario)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:gruppoPrimoFirmatario:\""+gruppoPrimoFirmatario+"\"";
}


// SCRL-124
// commissioni e ruoli
luceneQuery = creaLuceneQueryCommissioniRuoli(luceneQuery,commissioneUser,null,tipoWorkingList);
luceneQuery = creaLuceneQueryCommissioniRuoli(luceneQuery,commissione1,ruoloCommissione1,null);
luceneQuery = creaLuceneQueryCommissioniRuoli(luceneQuery,commissione2,ruoloCommissione2,null);
luceneQuery = creaLuceneQueryCommissioniRuoli(luceneQuery,commissione3,ruoloCommissione3,null);


// dataIniziativa
if(checkIsNotNull(dataIniziativaDa)
		&& checkIsNotNull(dataIniziativaA)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataIniziativa:["+dataIniziativaDa+"T00:00:00 TO "+dataIniziativaA+"T00:00:00]";
} else if(checkIsNotNull(dataIniziativaDa)
		&& (dataIniziativaA==null || dataIniziativaA==undefined || dataIniziativaA=="")) {
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataIniziativa:["+dataIniziativaDa+"T00:00:00 TO MAX]";
} else if(checkIsNotNull(dataIniziativaA)
		&& (dataIniziativaDa==null || dataIniziativaDa==undefined || dataIniziativaDa=="")){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataIniziativa:[MIN TO "+dataIniziativaA+"T00:00:00]";
}

// dataChiusura
if(checkIsNotNull(dataChiusuraDa)
		&& checkIsNotNull(dataChiusuraA)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataChiusura:["+dataChiusuraDa+"T00:00:00 TO "+dataChiusuraA+"T00:00:00]";
} else if(checkIsNotNull(dataChiusuraDa)
		&& (dataChiusuraA==null || dataChiusuraA==undefined || dataChiusuraA=="")) {
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataChiusura:["+dataChiusuraDa+"T00:00:00 TO MAX]";
} else if(checkIsNotNull(dataChiusuraA)
		&& (dataChiusuraDa==null || dataChiusuraDa==undefined || dataChiusuraDa=="")){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataChiusura:[MIN TO "+dataChiusuraA+"T00:00:00]";
}

// pannello sinistro
if(checkIsNotNull(tipoChiusura)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:tipoChiusura:\""+tipoChiusura+"\"";
}

if(checkIsNotNull(esitoVotoComRef)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:esitoVotoComRef:\""+esitoVotoComRef+"\"";
}

if(checkIsNotNull(esitoVotoAula)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:esitoVotoAula:\""+esitoVotoAula+"\"";
}

if(checkIsNotNull(redigente)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:redigente:\""+redigente+"\"";
}

if(checkIsNotNull(deliberante)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:deliberante:\""+deliberante+"\"";
}

if(checkIsNotNull(numeroLcr)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroLcr:\""+numeroLcr+"\"";
}

if(checkIsNotNull(numeroLr)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroLr:\""+numeroLr+"\"";
}

if(checkIsNotNull(anno)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:anno:\""+anno+"\"";
}

if(checkIsNotNull(abbinamento)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:abbinamento:\""+abbinamento+"\"";
}

if(checkIsNotNull(stralcio)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:stralcio:\""+stralcio+"\"";
}

// pannello destro

// dataPubblicazione
if(checkIsNotNull(dataPubblicazioneDa)
		&& checkIsNotNull(dataPubblicazioneA)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataPubblicazione:["+dataPubblicazioneDa+"T00:00:00 TO "+dataPubblicazioneA+"T00:00:00]";
} else if(checkIsNotNull(dataPubblicazioneDa)
		&& (dataPubblicazioneA==null || dataPubblicazioneA==undefined || dataPubblicazioneA=="")) {
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataPubblicazione:["+dataPubblicazioneDa+"T00:00:00 TO MAX]";
} else if(checkIsNotNull(dataPubblicazioneA)
		&& (dataPubblicazioneDa==null || dataPubblicazioneDa==undefined || dataPubblicazioneDa=="")){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataPubblicazione:[MIN TO "+dataPubblicazioneA+"T00:00:00]";
}

// dataSedutaSc
if(checkIsNotNull(dataSedutaScDa)
		&& checkIsNotNull(dataSedutaScA)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataSedutaSc:["+dataSedutaScDa+"T00:00:00 TO "+dataSedutaScA+"T00:00:00]";
} else if(checkIsNotNull(dataSedutaScDa)
		&& (dataSedutaScA==null || dataSedutaScA==undefined || dataSedutaScA=="")) {
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataSedutaSc:["+dataSedutaScDa+"T00:00:00 TO MAX]";
} else if(checkIsNotNull(dataSedutaScA)
		&& (dataSedutaScDa==null || dataSedutaScDa==undefined || dataSedutaScDa=="")){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataSedutaSc:[MIN TO "+dataSedutaScA+"T00:00:00]";
}

// dataSedutaComm
if(checkIsNotNull(dataSedutaCommDa)
		&& checkIsNotNull(dataSedutaCommA)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataSedutaComm:["+dataSedutaCommDa+"T00:00:00 TO "+dataSedutaCommA+"T00:00:00]";
} else if(checkIsNotNull(dataSedutaCommDa)
		&& (dataSedutaCommA==null || dataSedutaCommA==undefined || dataSedutaCommA=="")) {
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataSedutaComm:["+dataSedutaCommDa+"T00:00:00 TO MAX]";
} else if(checkIsNotNull(dataSedutaCommA)
		&& (dataSedutaCommDa==null || dataSedutaCommDa==undefined || dataSedutaCommDa=="")){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataSedutaComm:[MIN TO "+dataSedutaCommA+"T00:00:00]";
}

// dataSedutaAula
if(checkIsNotNull(dataSedutaAulaDa)
		&& checkIsNotNull(dataSedutaAulaA)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataSedutaAula:["+dataSedutaAulaDa+"T00:00:00 TO "+dataSedutaAulaA+"T00:00:00]";
} else if(checkIsNotNull(dataSedutaAulaDa)
		&& (dataSedutaAulaA==null || dataSedutaAulaA==undefined || dataSedutaAulaA=="")) {
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataSedutaAula:["+dataSedutaAulaDa+"T00:00:00 TO MAX]";
} else if(checkIsNotNull(dataSedutaAulaA)
		&& (dataSedutaAulaDa==null || dataSedutaAulaDa==undefined || dataSedutaAulaDa=="")){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataSedutaAula:[MIN TO "+dataSedutaAulaA+"T00:00:00]";
}

// dataAssegnazioneCommissioneReferente
if(checkIsNotNull(dataAssegnazioneDa)
		&& checkIsNotNull(dataAssegnazioneA)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataAssegnazioneCommissioneReferente:["+dataAssegnazioneDa+"T00:00:00 TO "+dataAssegnazioneA+"T00:00:00]";
} else if(checkIsNotNull(dataAssegnazioneDa)
		&& (dataAssegnazioneA==null || dataAssegnazioneA==undefined || dataAssegnazioneA=="")) {
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataAssegnazioneCommissioneReferente:["+dataAssegnazioneDa+"T00:00:00 TO MAX]";
} else if(checkIsNotNull(dataAssegnazioneA)
		&& (dataAssegnazioneDa==null || dataAssegnazioneDa==undefined || dataAssegnazioneDa=="")){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataAssegnazioneCommissioneReferente:[MIN TO "+dataAssegnazioneA+"T00:00:00]";
}

if(checkIsNotNull(relatore)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:relatori:\""+relatore+"\"";
}

if(checkIsNotNull(organismoStatutario)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:organismiStatutari:\""+organismoStatutario+"\"";
}

if(checkIsNotNull(soggettoConsultato)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:soggettoConsultato:\""+soggettoConsultato+"\"";
}

if(checkIsNotNull(emendato)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:emendato:\""+emendato+"\"";
}

if(checkIsNotNull(rinviato)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:rinviato:\""+rinviato+"\"";
}

if(checkIsNotNull(emendatoAula)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:emendatoAulaAtto:\""+emendatoAula+"\"";
}

if(checkIsNotNull(sospeso)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:sospeso:\""+sospeso+"\"";
}

if(checkIsNotNull(numeroDgr)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroDgr:\""+numeroDgr+"\"";
}

if(checkIsNotNull(dataDgr)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataDgr:["+dataDgr+"T00:00:00 TO "+dataDgr+"T00:00:00]";
}

if(checkIsNotNull(dataLr)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:dataLr:["+dataLr+"T00:00:00 TO "+dataLr+"T00:00:00]";
}

// SCRL-124
if(ruoloUtente != "Aula" && (tipoWorkingList == "inlavorazione" || tipoWorkingList == "lavorato")) {
    luceneQuery = verifyAND(luceneQuery);
    luceneQuery += "NOT (TYPE:\"crlatti:attoOrg\" OR (TYPE:\"crlatti:attoPda\" AND @crlatti\\:tipoIniziativa:\"05_ATTO DI INIZIATIVA UFFICIO PRESIDENZA\"))";
}
         
// statiUtente - condizioni in OR per Lucene
if(checkIsNotNull(statiUtente)){
	var numeroStatiUtente = statiUtente.length();
	if(numeroStatiUtente>0){
		luceneQuery = verifyAND(luceneQuery);
		luceneQuery += " ( ";
		for (var j=0; j<numeroStatiUtente; j++){
			var statoAtto = statiUtente.get(j).get("statoAtto");
			if(checkIsNotNull(statoAtto)){
				var descrizioneStatoAtto = statoAtto.get("descrizione");
				if(checkIsNotNull(descrizioneStatoAtto)){
					// luceneQuery = verifyOR(luceneQuery);
					luceneQuery += "@crlatti\\:statoAtto:\""+descrizioneStatoAtto+"\"";
					if(j<numeroStatiUtente-1){
						luceneQuery = verifyOR(luceneQuery);
					}
					
				}
			}
		}
                
                // SCRL-124
                if(ruoloUtente == "Aula" && tipoWorkingList == "inlavorazione") {
                    luceneQuery = verifyOR(luceneQuery);
                    luceneQuery += "(TYPE:\"crlatti:attoOrg\" AND @crlatti\\:statoAtto:\"Protocollato\")";
                    luceneQuery = verifyOR(luceneQuery); 
                    luceneQuery += "(TYPE:\"crlatti:attoPda\" AND @crlatti\\:tipoIniziativa:\"05_ATTO DI INIZIATIVA UFFICIO PRESIDENZA\" AND @crlatti\\:statoAtto:\"Protocollato\")";
                }
                
		luceneQuery += " ) ";
	}
}

if(isHome!=null && isHome!=undefined && isHome!=""){
	if(isHome){
		// condizione di recenti modifiche negli ultimi 7 giorni
		var dataOggi = new Date();
		dataOggi.setDate(-7);

		var anno = dataOggi.getFullYear();
		var mese = dataOggi.getMonth()+2;
		var giorno = dataOggi.getDate();

		if(mese<10){
			mese = "0"+mese; 
		}

		if(giorno<10){
			giorno = "0"+giorno;
		}

		var dataPartenzaQuery = anno+"\\-"+mese+"\\-"+giorno+"T00:00:00 TO NOW";
		var ultimeModificheLuceneQuery = " AND @cm\\:modified:["+dataPartenzaQuery+"]";

		luceneQuery = luceneQuery + ultimeModificheLuceneQuery;
	}
	
}

/* Ricerca per atto pubblico */ 
if ( checkIsNotNull(pubblico) ) {
	if (pubblico)  {
		luceneQuery = luceneQuery + " AND @crlatti\\:pubblico:true";
	}	
}

/* add PATH */

/* Rimozione Query
 * 
 * luceneQuery = luceneQuery + ' AND PATH:"//app:company_home/cm:CRL//*"';
*/

protocolloLogger.info(luceneQuery);

var start = new Date().getTime();
protocolloLogger.info("Ricerca - Lucene Query start:"+start);

/* var attiResults = search.luceneSearch(luceneQuery); */

/* Inizio Impaginazione rirerca */
var sort;
if(checkIsNotNull(orderby)){
	sort = {
		 column: orderby,
		 ascending: false
		};
}
else {
	sort = {
		 column: "@{http://www.regione.lombardia.it/content/model/atti/1.0}numeroAtto",  // numero
																							// atto
		 ascending: false
		};
}

if(checkIsNull(limit)){
	limit = 10000;
}

if(checkIsNull(startFrom)){
	startFrom = 0;
}

var paging = {
 maxItems: limit,
 skipCount: startFrom
};

var def = {
 query: luceneQuery,
 store: "workspace://SpacesStore",
 language: "lucene",
 sort: [sort]
};

/* Fine Impaginazione rirerca */

/* esegue la ricerca */
var attiResults = search.query(def);



var end = new Date().getTime();
var time = end - start;
protocolloLogger.info("Ricerca - Lucene Query eseguita in: "+time);



// logica per la creazione degli oggetti da visualizzare nei risultati
var startDb = new Date().getTime();
protocolloLogger.info("Ricerca - Database - start:"+startDb);




var commissioniAbbinamentiDuration = 0;

var attiResultsCleaned= new Array();

for(var i=0; i< attiResults.length; i++){

    /*
	 * A quick way to retrieve only documents under this folder a lucene path
	 * query is much slower when the document to filter are few as in this case
	 */
    if (attiResults[i].displayPath.startsWith("/Company Home/CRL")){
    		attiResultsCleaned.push(attiResults[i]);
    }
    
}

model.total = attiResultsCleaned.length;
attiResultsCleaned=attiResultsCleaned.slice(startFrom, startFrom+limit<=model.total+1?startFrom+limit:model.total);
var attiResultsObj = new Array();

for(var i=0; i< attiResultsCleaned.length; i++){
	var attoResult = attiResultsCleaned[i];
	var attoResultObj = new Object();
	
	attoResultObj.id = attoResult.nodeRef.toString();
	

	attoResultObj.tipo =  attoResult.typeShort.substring(12,15);
	
	
	attoResultObj.name = attoResult.name;
	attoResultObj.numeroAtto = attoResult.properties["crlatti:numeroAtto"];
	attoResultObj.oggetto = attoResult.properties["crlatti:oggetto"];
	attoResultObj.primoFirmatario = attoResult.properties["crlatti:primoFirmatario"];
	attoResultObj.stato = attoResult.properties["crlatti:statoAtto"];
	attoResultObj.tipoIniziativa = attoResult.properties["crlatti:tipoIniziativa"];
	attoResultObj.dataPresentazione = attoResult.properties["crlatti:dataIniziativa"];
	attoResultObj.tipoChiusura = attoResult.properties["crlatti:tipoChiusura"];
	attoResultObj.estensioneAtto = attoResult.properties["crlatti:estensioneAtto"];
	
		
	var elencoFirmatariArray = attoResult.properties["crlatti:firmatari"];
	var elencoFirmatariString = "";
	
	if(elencoFirmatariArray!=null) {
		for(var j=0; j<elencoFirmatariArray.length; j++){
			elencoFirmatariString += elencoFirmatariArray[j];
			if(j!=elencoFirmatariArray.length-1){
				elencoFirmatariString += ", ";
			}
		}
	}
	
	attoResultObj.elencoFirmatari = elencoFirmatariString;
	
	
	var commissioniConsultiveArray = attoResult.properties["crlatti:commConsultiva"];
	var commissioniConsultiveString = "";
	
	if(commissioniConsultiveArray!=null) {
		for(var j=0; j<commissioniConsultiveArray.length; j++){
			commissioniConsultiveString += commissioniConsultiveArray[j];
			if(j!=commissioniConsultiveArray.length-1){
				commissioniConsultiveString += ", ";
			}
		}
	}
	
	attoResultObj.commissioniConsultive = commissioniConsultiveString;
	
	var commissioniReferentiArray = attoResult.properties["crlatti:commReferente"];
	var commissioniReferentiString = "";
        
        if(commissioniReferentiArray == null) {
            commissioniReferentiArray = new Array();
        }
        
        if(checkIsNotNull(attoResult.properties["crlatti:commCoreferente"])) {
            commissioniReferentiArray.push(attoResult.properties["crlatti:commCoreferente"]);
        }
        
        if(checkIsNotNull(attoResult.properties["crlatti:commRedigente"])) {
            commissioniReferentiArray.push(attoResult.properties["crlatti:commRedigente"]);
        }
        
        if(checkIsNotNull(attoResult.properties["crlatti:commDeliberante"])) {
            commissioniReferentiArray.push(attoResult.properties["crlatti:commDeliberante"]);
        }
	
	if (commissioniReferentiArray!=null){
		for(var j=0; j<commissioniReferentiArray.length; j++){
			commissioniReferentiString += commissioniReferentiArray[j];
			if(j!=commissioniReferentiArray.length-1){
				commissioniReferentiString += ", ";
			}
		}
	}
        
        attoResultObj.commissioniNonConsultive = commissioniReferentiString;

    var startTsCommissioniAbbinamenti = Date.now();
	if(attoResult.typeShort != "crlatti:attoMis" && attoResult.typeShort != "crlatti:attoEac") {
		// MG
		var passaggioFolderNode = getLastPassaggio(attoResult);

		var abbinamentiFolderXpathQuery = "*[@cm:name='Abbinamenti']";
		var abbinamentiFolderNode = passaggioFolderNode.childrenByXPath(abbinamentiFolderXpathQuery)[0];
		var abbinamenti = abbinamentiFolderNode.getChildAssocsByType("crlatti:abbinamento");
		
		var abbinamentiString = "";
		
		for(var j=0 ; j<abbinamenti.length; j++){
			abbinamentiString += abbinamenti[j].name.toUpperCase();
			if(j!=abbinamenti.length-1){
				abbinamentiString += ", ";
			}
		}
		
		attoResultObj.elencoAbbinamenti = abbinamentiString;
		
		// MG

		var commissioniFolderXpathQuery = "*[@cm:name='Commissioni']";
		var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniFolderXpathQuery)[0];
		var commissioni = commissioniFolderNode.getChildAssocsByType("crlatti:commissione");

		for(var j=0 ; j<commissioni.length; j++){

			var commissioneCorrente = commissioni[j];
			
			if(commissioneCorrente.properties["crlatti:ruoloCommissione"] == "Referente" || commissioneCorrente.properties["crlatti:ruoloCommissione"] == "Delibernate"
				|| commissioneCorrente.properties["crlatti:ruoloCommissione"] == "Redigente") {
				
				attoResultObj.dataAssegnazione = commissioneCorrente.properties["crlatti:dataAssegnazioneCommissione"];
				attoResultObj.esitoVotazioneCommissioneReferente = commissioneCorrente.properties["crlatti:esitoVotazioneCommissione"];
				attoResultObj.dataVotazioneCommissione = commissioneCorrente.properties["crlatti:dataVotazioneCommissione"];
				attoResultObj.dataRichiestaIscrizioneAula = commissioneCorrente.properties["crlatti:dataRichiestaIscrizioneAula"];

                // MG
				var relatoriXPathQuery = "*[@cm:name='Relatori']";
				var relatoriCommissioneFolderNode = commissioneCorrente.childrenByXPath(relatoriXPathQuery)[0];
				
				var relatoriNode = relatoriCommissioneFolderNode.getChildAssocsByType("crlatti:relatore");
				
                                if(relatoriNode != null && relatoriNode.length > 0) {
                                    attoResultObj.relatore = relatoriNode[0].name;
                                    attoResultObj.dataNominaRelatore = relatoriNode[0].properties["crlatti:dataNominaRelatore"];	
                                }
			}
		}

		
	}
    commissioniAbbinamentiDuration += Date.now()-startTsCommissioniAbbinamenti;
	

	attoResultObj.dataScadenza = attoResult.properties["crlatti:dataScadenza"];
	
	attoResultObj.esitoVotazioneAula = attoResult.properties["crlatti:esitoVotoAula"];
	attoResultObj.dataVotazioneAula = attoResult.properties["crlatti:dataSedutaAula"];
		
	attoResultObj.numeroPubblicazioneBURL = attoResult.properties["crlatti:numeroPubblicazioneBURL"];
	attoResultObj.dataPubblicazioneBURL = attoResult.properties["crlatti:dataPubblicazioneBURL"];
	
	attoResultObj.numeroDcr = attoResult.properties["crlatti:numeroDcr"];
	attoResultObj.numeroLcr = attoResult.properties["crlatti:numeroLcr"];
	
	attoResultObj.numeroLr = attoResult.properties["crlatti:numeroLr"];
	attoResultObj.dataLR = attoResult.properties["crlatti:dataLr"];
	
	attoResultObj.pubblico = attoResult.properties["crlatti:pubblico"];
	
	// MG
	var noteGeneraliFilename = "Note Generali.txt";
	var noteGeneraliXPathQuery = "*[@cm:name='"+noteGeneraliFilename+"']";
	var noteGenerali = attoResult.childrenByXPath(noteGeneraliXPathQuery)[0];
	
	if(noteGenerali!=null){
		attoResultObj.notePresentazioneAssegnazione = noteGenerali.content;
	}
	
	attiResultsObj.push(attoResultObj);
	
}

var endDb = new Date().getTime();
var timeDb = endDb - startDb;
protocolloLogger.info("Ricerca - Database - eseguita in :"+timeDb);
logger.log("output commissioni e abbinamenti: "+commissioniAbbinamentiDuration+"ms");
model.atti = attiResultsObj;