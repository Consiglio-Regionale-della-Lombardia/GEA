<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/commonMigrazione.js">

var atto = json.get("atto").get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var passaggio = json.get("target").get("target").get("passaggio");

// selezione del passaggio corrente
var passaggioTarget = getPassaggioTarget(json, passaggio);


//var numEmendPresentatiMaggiorEsame = filterNumericParam(passaggioTarget.get("aula").get("numEmendPresentatiMaggiorEsameAula"));
//var numEmendPresentatiMinorEsame = filterNumericParam(passaggioTarget.get("aula").get("numEmendPresentatiMinorEsameAula"));
//var numEmendPresentatiGiuntaEsame = filterNumericParam(passaggioTarget.get("aula").get("numEmendPresentatiGiuntaEsameAula"));
//var numEmendPresentatiMistoEsame = filterNumericParam(passaggioTarget.get("aula").get("numEmendPresentatiMistoEsameAula"));
//
//var numEmendApprovatiMaggiorEsame = filterNumericParam(passaggioTarget.get("aula").get("numEmendApprovatiMaggiorEsameAula"));
//var numEmendApprovatiMinorEsame = filterNumericParam(passaggioTarget.get("aula").get("numEmendApprovatiMinorEsameAula"));
//var numEmendApprovatiGiuntaEsame = filterNumericParam(passaggioTarget.get("aula").get("numEmendApprovatiGiuntaEsameAula"));
//var numEmendApprovatiMistoEsame = filterNumericParam(passaggioTarget.get("aula").get("numEmendApprovatiMistoEsameAula"));
//
//var nonAmmissibiliEsame = filterNumericParam(passaggioTarget.get("aula").get("nonAmmissibiliEsameAula"));
//var decadutiEsame = filterNumericParam(passaggioTarget.get("aula").get("decadutiEsameAula"));
//var ritiratiEsame = filterNumericParam(passaggioTarget.get("aula").get("ritiratiEsameAula"));
//var respintiEsame  = filterNumericParam(passaggioTarget.get("aula").get("respintiEsameAula"));
//var noteEmendamentiEsame = filterParam(passaggioTarget.get("aula").get("noteEmendamentiEsameAula"));


var numEmendPresentatiMaggiorEsame = passaggioTarget.get("aula").get("numEmendPresentatiMaggiorEsameAula");
var numEmendPresentatiMinorEsame = passaggioTarget.get("aula").get("numEmendPresentatiMinorEsameAula");
var numEmendPresentatiGiuntaEsame = passaggioTarget.get("aula").get("numEmendPresentatiGiuntaEsameAula");
var numEmendPresentatiMistoEsame = passaggioTarget.get("aula").get("numEmendPresentatiMistoEsameAula");

var numEmendApprovatiMaggiorEsame = passaggioTarget.get("aula").get("numEmendApprovatiMaggiorEsameAula");
var numEmendApprovatiMinorEsame = passaggioTarget.get("aula").get("numEmendApprovatiMinorEsameAula");
var numEmendApprovatiGiuntaEsame = passaggioTarget.get("aula").get("numEmendApprovatiGiuntaEsameAula");
var numEmendApprovatiMistoEsame = passaggioTarget.get("aula").get("numEmendApprovatiMistoEsameAula");

var nonAmmissibiliEsame =passaggioTarget.get("aula").get("nonAmmissibiliEsameAula");
var decadutiEsame = passaggioTarget.get("aula").get("decadutiEsameAula");
var ritiratiEsame = passaggioTarget.get("aula").get("ritiratiEsameAula");
var respintiEsame  = passaggioTarget.get("aula").get("respintiEsameAula");
var noteEmendamentiEsame = filterParam(passaggioTarget.get("aula").get("noteEmendamentiEsameAula"));


if(checkIsNotNull(id)
		&& checkIsNotNull(passaggio)){
	
	var attoNode = utils.getNodeFromString(id);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	var aulaXPathQuery = "*[@cm:name='Aula']";
	var aulaFolderNode = passaggioFolderNode.childrenByXPath(aulaXPathQuery)[0];

	
	if(numEmendPresentatiMaggiorEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendPresentatiMaggiorEsame"] = numEmendPresentatiMaggiorEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendPresentatiMaggiorEsame"] = null;
	}
	
	if(numEmendPresentatiMinorEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendPresentatiMinorEsame"] = numEmendPresentatiMinorEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendPresentatiMinorEsame"] = null;
	}
	
	if(numEmendPresentatiGiuntaEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendPresentatiGiuntaEsame"] = numEmendPresentatiGiuntaEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendPresentatiGiuntaEsame"] = null;
	}
	
	if(numEmendPresentatiMistoEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendPresentatiMistoEsame"] = numEmendPresentatiMistoEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendPresentatiMistoEsame"] = null;
	}
	
	
	
	
	if(numEmendApprovatiMaggiorEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendApprovatiMaggiorEsame"] = numEmendApprovatiMaggiorEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendApprovatiMaggiorEsame"] = null;
	}
	
	if(numEmendApprovatiMinorEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendApprovatiMinorEsame"] = numEmendApprovatiMinorEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendApprovatiMinorEsame"] = null;
	}
	
	if(numEmendApprovatiGiuntaEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendApprovatiGiuntaEsame"] = numEmendApprovatiGiuntaEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendApprovatiGiuntaEsame"] = null;
	}
	
	if(numEmendApprovatiMistoEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendApprovatiMistoEsame"] = numEmendApprovatiMistoEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendApprovatiMistoEsame"] = null;
	}
	


	
	if(nonAmmissibiliEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendNonAmmissibiliEsame"] = nonAmmissibiliEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendNonAmmissibiliEsame"] = null;
	}
	
	if(decadutiEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendDecadutiEsame"] = decadutiEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendDecadutiEsame"] = null;
	}
	
	if(ritiratiEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendRitiratiEsame"] = ritiratiEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendRitiratiEsame"] = null;
	}
	
	if(respintiEsame!="null"){
		aulaFolderNode.properties["crlatti:numEmendRespintiEsame"] = respintiEsame;
	}else{
		aulaFolderNode.properties["crlatti:numEmendRespintiEsame"] = null;
	}
	

	
	aulaFolderNode.properties["crlatti:noteEmendamentiEsame"] = noteEmendamentiEsame;

	aulaFolderNode.save();

	
	model.atto = attoNode;
	model.aula = aulaFolderNode;
	
} else {
	status.code = 400;
	status.message = "id atto e passaggio non valorizzato";
	status.redirect = true;
}