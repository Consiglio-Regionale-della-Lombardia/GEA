<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var commissioni = space.getChildAssocsByType("crlatti:commissione");

var passaggioNode = space.parent;
var passaggiNode = passaggioNode.parent;
var attoNode = passaggiNode.parent;

// Ridondanza delle informazioni sui ruoli delle commissioni direttamente sul nodo atto
// Per consentire le ricerche lucene relative alle commissioni direttamente sui nodi atto

var commConsultivaAtto = new Array();
var commReferenteAtto = new Array();
var commCoReferenteAtto = new Array();
var commRedigenteAtto = new Array();
var commDeliberanteAtto = new Array();

var numeroEmendamentiApprovati = 0;

for (var i=0; i<commissioni.length; i++) {
	
	if(commissioni[i].properties["crlatti:ruoloCommissione"] == "Consultiva") {
		commConsultivaAtto.push(commissioni[i].name);
	}
	
	if(commissioni[i].properties["crlatti:ruoloCommissione"] == "Referente") {
		commReferenteAtto.push(commissioni[i].name);
		//attoNode.properties["crlatti:dataAssegnazioneCommissioneReferente"] = commissioni[i].properties["crlatti:dataAssegnazioneCommissione"];
	}

	if(commissioni[i].properties["crlatti:ruoloCommissione"] == "Co-Referente") {
		commCoReferenteAtto.push(commissioni[i].name);
	}
	
	if(commissioni[i].properties["crlatti:ruoloCommissione"] == "Redigente") {
		commRedigenteAtto.push(commissioni[i].name);
	}
	
	if(commissioni[i].properties["crlatti:ruoloCommissione"] == "Deliberante") {
		commDeliberanteAtto.push(commissioni[i].name);
	}
	
	
	if(commissioni[i].properties["crlatti:ruoloCommissione"] == "Referente" || commissioni[i].properties["crlatti:ruoloCommissione"] == "Co-Referente" ||
			commissioni[i].properties["crlatti:ruoloCommissione"] == "Redigente" || commissioni[i].properties["crlatti:ruoloCommissione"] == "Deliberante" ){
		
		attoNode.properties["crlatti:dataAssegnazioneCommissioneReferente"] = commissioni[i].properties["crlatti:dataAssegnazioneCommissione"];
		
		attoNode.properties["crlatti:esitoVotoComRef"] = commissioni[i].properties["crlatti:esitoVotazioneCommissione"];
		attoNode.properties["crlatti:dataSedutaComm"] = commissioni[i].properties["crlatti:dataVotazioneCommissione"];
				
		
	}
	
	
	// Controllo emendamenti in commissione
	
	var numEmendApprovatiMaggiorEsame = filterNumericParam(commissioni[i].properties["crlatti:numEmendApprovatiMaggiorEsame"]);
	var numEmendApprovatiMinorEsame = filterNumericParam(commissioni[i].properties["crlatti:numEmendApprovatiMinorEsame"]);
	var numEmendApprovatiGiuntaEsame = filterNumericParam(commissioni[i].properties["crlatti:numEmendApprovatiGiuntaEsame"]);
	var numEmendApprovatiMistoEsame = filterNumericParam(commissioni[i].properties["crlatti:numEmendApprovatiMistoEsame"]);
	
	var numeroEmendamentiApprovatiCommissione =  numEmendApprovatiMaggiorEsame + numEmendApprovatiMinorEsame + numEmendApprovatiGiuntaEsame + numEmendApprovatiMistoEsame;
	
	numeroEmendamentiApprovati += numeroEmendamentiApprovatiCommissione;
	
}


attoNode.properties["crlatti:commConsultiva"] = commConsultivaAtto;
attoNode.properties["crlatti:commReferente"] = commReferenteAtto;

if(commCoReferenteAtto.length>0) {
	attoNode.properties["crlatti:commCoreferente"] = commCoReferenteAtto;
}
if(commRedigenteAtto.length>0) {
	attoNode.properties["crlatti:commRedigente"] = commRedigenteAtto[0];
	attoNode.properties["crlatti:redigente"] = true;
}else{
	attoNode.properties["crlatti:redigente"] = false;
}

if(commDeliberanteAtto.length>0) {
	attoNode.properties["crlatti:commDeliberante"] = commDeliberanteAtto[0];
	attoNode.properties["crlatti:deliberante"] = true;
}else{
	attoNode.properties["crlatti:deliberante"] = false;
}


if(numeroEmendamentiApprovati>0){
	attoNode.properties["crlatti:emendato"] = true;
}else{
	attoNode.properties["crlatti:emendato"] = false;
}

attoNode.save();