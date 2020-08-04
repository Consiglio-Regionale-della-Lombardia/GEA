<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

// Ridondanza delle informazioni sull'aula per consentire le ricerche lucene sul nodo atto

var passaggioNode = document.parent;
var passaggiNode = passaggioNode.parent;
var attoNode = passaggiNode.parent;


var numEmendApprovatiMaggiorEsame = filterNumericParam(document.properties["crlatti:numEmendApprovatiMaggiorEsame"]);
var numEmendApprovatiMinorEsame = filterNumericParam(document.properties["crlatti:numEmendApprovatiMinorEsame"]);
var numEmendApprovatiGiuntaEsame = filterNumericParam(document.properties["crlatti:numEmendApprovatiGiuntaEsame"]);
var numEmendApprovatiMistoEsame = filterNumericParam(document.properties["crlatti:numEmendApprovatiMistoEsame"]);

var numeroEmendamentiApprovati =  numEmendApprovatiMaggiorEsame + numEmendApprovatiMinorEsame + numEmendApprovatiGiuntaEsame + numEmendApprovatiMistoEsame;


if(numeroEmendamentiApprovati>0){
	attoNode.properties["crlatti:emendatoAulaAtto"] = true;
}else{
	attoNode.properties["crlatti:emendatoAulaAtto"] = false;
}

attoNode.properties["crlatti:numeroLcr"] = document.properties["crlatti:numeroLcrPassaggioAula"];
attoNode.properties["crlatti:esitoVotoAula"] = document.properties["crlatti:esitoVotoPassaggioAula"];
attoNode.properties["crlatti:dataSedutaAula"] = document.properties["crlatti:dataSedutaPassaggioAula"];
attoNode.properties["crlatti:numeroDcr"] = document.properties["crlatti:numeroDcrPassaggioAula"];


attoNode.save();



