<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");

var tipoAtto = atto.get("tipoAtto");
var legislatura = atto.get("legislatura");
var stato = atto.get("stato");
var numeroAtto = atto.get("numeroAtto");
var numeroProtocollo = atto.get("numeroProtocollo");
var tipoIniziativa = atto.get("tipoIniziativa");
var numeroDcr = atto.get("numeroDcr");
var primoFirmatario = atto.get("primoFirmatario");
var oggetto = atto.get("oggetto");
var firmatario = atto.get("firmatario");
var dataIniziativaDa = atto.get("dataIniziativaDa");
var dataIniziativaA = atto.get("dataIniziativaA");

var luceneQuery = "PATH:\"/app:company_home/cm:CRL//*\" ";

var tipoAttoString = ""+tipoAtto+"";
var type = "crlatti:atto";

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
	}
}

luceneQuery += "AND TYPE:\""+type+"\"";

if(checkIsNotNull(legislatura)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:legislatura:\""+legislatura+"\"";
}

if(checkIsNotNull(stato)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:statoAtto:\""+stato+"\"";
}

if(checkIsNotNull(numeroAtto)){
	luceneQuery = verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:numeroAtto:\""+numeroAtto+"\"";
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
	verifyAND(luceneQuery);
	luceneQuery += "@crlatti\\:firmatari:\""+firmatario+"\"";
}


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

var attiResults = search.luceneSearch(luceneQuery);

//logica per la creazione degli oggetti da visualizzare nei risultati

var attiResultsObj = new Array();

for(var i=0; i< attiResults.length; i++){
	
	var attoResult = attiResults[i];
	var attoResultObj = new Object();
	
	attoResultObj.id = attoResult.nodeRef;
	
	var tipoAttoString = attoResult.typeShort.substring(12,15);
	attoResultObj.tipo = 	tipoAttoString.charAt(0).toUpperCase() + tipoAttoString.slice(1);
	
	attoResultObj.numeroAtto = attoResult.properties["crlatti:numeroAtto"];
	attoResultObj.oggetto = attoResult.properties["crlatti:oggetto"];
	attoResultObj.primoFirmatario = attoResult.properties["crlatti:primoFirmatario"];
	attoResultObj.stato = attoResult.properties["crlatti:statoAtto"];
	attoResultObj.elencoFirmatari = attoResult.properties["crlatti:firmatari"];
	attoResultObj.tipoIniziativa = attoResult.properties["crlatti:tipoIniziativa"];
	attoResultObj.dataPresentazione = attoResult.properties["crlatti:dataIniziativa"];
	attoResultObj.tipoChiusura = attoResult.properties["crlatti:tipoChiusura"];
	attoResultObj.commissioniConsultive = attoResult.properties["crlatti:commConsultiva"];
	
	attoResultObj.commissioniNonConsultive = attoResult.properties["crlatti:commReferente"] + "," + attoResult.properties["crlatti:commCoreferente"] + "," + 
		attoResult.properties["crlatti:commRedigente"] + "," + attoResult.properties["crlatti:commDeliberante"]

	
	var passaggioFolderNode = getLastPassaggio(attoResult);
	
	var abbinamentiFolderXpathQuery = "*[@cm:name='Abbinamenti']";
	var abbinamentiFolderNode = passaggioFolderNode.childrenByXPath(abbinamentiFolderXpathQuery)[0];
	abbinamenti = abbinamentiFolderNode.getChildAssocsByType("crlatti:abbinamento");
	
	abbinamentiString = "";
	
	for(var j=0 ; j<abbinamenti.lenght; j++){
		abbinamentiString += abbinamenti[j].name;
		if(j!=abbinamenti.length-1){
			abbinamentiString += ",";
		}
	}
	
	attoResultObj.elencoAbbinamenti = abbinamentiString;
	
	
	var commissioniFolderXpathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniFolderXpathQuery)[0];
	commissioni = commissioniFolderNode.getChildAssocsByType("crlatti:commissione");

	for(var j=0 ; j<commissioni.lenght; j++){

		var commissioneCorrente = commissioni[j];
		
		if(commissioneCorrente.properties["crlatti:ruoloCommissione"] == "Referente" || commissioneCorrente.properties["crlatti:ruoloCommissione"] == "Delibernate"
			|| commissioneCorrente.properties["crlatti:ruoloCommissione"] == "Redigente") {
			
			attoResultObj.dataAssegnazione = commissioneCorrente.properties["crlatti:dataAssegnazioneCommissione"];
			attoResultObj.esitoVotazioneCommissioneReferente = commissioneCorrente.properties["crlatti:esitoVotazioneCommissione"];
			attoResultObj.dataVotazioneCommissione = commissioneCorrente.properties["crlatti:dataVotazioneCommissione"];
			attoResultObj.dataRichiestaIscrizioneAula = commissioneCorrente.properties["crlatti:dataRichiestaIscrizioneAula"];
		
			var relatoriXPathQuery = "*[@cm:name='Relatori']";
			var relatoriCommissioneFolderNode = commissioneCorrente.childrenByXPath(relatoriXPathQuery)[0];
			
			var relatoriNode = relatoriCommissioneFolderNode.getChildAssocsByType("crlatti:relatore");
			
			attoResultObj.relatore = relatoriNode[0].name;
			attoResultObj.dataNominaRelatore = relatoriNode[0].properties["crlatti:dataNominaRelatore"];		
		}
	}
	

	attoResultObj.dataScadenza = attoResult.properties["crlatti:dataScadenza"];
	
	attoResultObj.esitoVotazioneAula = attoResult.properties["crlatti:esitoVotoAula"];
	attoResultObj.dataVotazioneAula = attoResult.properties["crlatti:dataSedutaAula"];
		
	attoResultObj.numeroPubblicazioneBURL = attoResult.properties["crlatti:numeroPubblicazioneBURL"];
	attoResultObj.dataPubblicazioneBURL = attoResult.properties["crlatti:dataPubblicazioneBURL"];
	
	attoResultObj.numeroDcr = attoResult.properties["crlatti:numeroDcr"];
	attoResultObj.numeroLcr = attoResult.properties["crlatti:numeroLcr"];
	
	attoResultObj.numeroLr = attoResult.properties["crlatti:numeroLr"];
	attoResultObj.dataLR = attoResult.properties["crlatti:dataLr"];
	
	
	var noteGeneraliFilename = "Note Generali.txt";
	var noteGeneraliXPathQuery = "*[@cm:name='"+noteGeneraliFilename+"']";
	var noteGenerali = attoResult.childrenByXPath(noteGeneraliXPathQuery)[0];
	
	attoResultObj.notePresentazioneAssegnazione = noteGenerali.content;
	
	attiResultsObj.push(attoResultObj);
	
}


model.atti = attiResults;