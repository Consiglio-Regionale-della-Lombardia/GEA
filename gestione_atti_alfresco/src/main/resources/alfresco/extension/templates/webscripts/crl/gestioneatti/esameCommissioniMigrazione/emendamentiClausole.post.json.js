<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/commonMigrazione.js">

var atto = filterParam(json.get("atto").get("atto"));
var id = filterParam(atto.get("id"));
var statoAtto = filterParam(atto.get("stato"));
var commissioneUtente = json.get("target").get("target").get("commissione");
var passaggio = json.get("target").get("target").get("passaggio");

//selezione della commissione e del passaggio corrente
var commissioneTarget = getCommissioneTarget(json, passaggio, commissioneUtente);


//var numEmendPresentatiMaggiorEsameCommissioni = filterNumericParam(commissioneTarget.get("numEmendPresentatiMaggiorEsameCommissioni"));
//var numEmendPresentatiMinorEsameCommissioni = filterNumericParam(commissioneTarget.get("numEmendPresentatiMinorEsameCommissioni"));
//var numEmendPresentatiGiuntaEsameCommissioni = filterNumericParam(commissioneTarget.get("numEmendPresentatiGiuntaEsameCommissioni"));
//var numEmendPresentatiMistoEsameCommissioni = filterNumericParam(commissioneTarget.get("numEmendPresentatiMistoEsameCommissioni"));
//
//var numEmendApprovatiMaggiorEsameCommissioni = filterNumericParam(commissioneTarget.get("numEmendApprovatiMaggiorEsameCommissioni"));
//var numEmendApprovatiMinorEsameCommissioni = filterNumericParam(commissioneTarget.get("numEmendApprovatiMinorEsameCommissioni"));
//var numEmendApprovatiGiuntaEsameCommissioni = filterNumericParam(commissioneTarget.get("numEmendApprovatiGiuntaEsameCommissioni"));
//var numEmendApprovatiMistoEsameCommissioni = filterNumericParam(commissioneTarget.get("numEmendApprovatiMistoEsameCommissioni"));

//var nonAmmissibiliEsameCommissioni = filterNumericParam(commissioneTarget.get("nonAmmissibiliEsameCommissioni"));
//var decadutiEsameCommissioni = filterNumericParam(commissioneTarget.get("decadutiEsameCommissioni"));
//var ritiratiEsameCommissioni = filterNumericParam(commissioneTarget.get("ritiratiEsameCommissioni"));
//var respintiEsameCommissioni = filterNumericParam(commissioneTarget.get("respintiEsameCommissioni"));
//var noteEmendamentiEsameCommissioni = filterParam(commissioneTarget.get("noteEmendamentiEsameCommissioni"));

var numEmendPresentatiMaggiorEsameCommissioni = commissioneTarget.get("numEmendPresentatiMaggiorEsameCommissioni");
var numEmendPresentatiMinorEsameCommissioni = commissioneTarget.get("numEmendPresentatiMinorEsameCommissioni");
var numEmendPresentatiGiuntaEsameCommissioni = commissioneTarget.get("numEmendPresentatiGiuntaEsameCommissioni");
var numEmendPresentatiMistoEsameCommissioni = commissioneTarget.get("numEmendPresentatiMistoEsameCommissioni");

var numEmendApprovatiMaggiorEsameCommissioni = commissioneTarget.get("numEmendApprovatiMaggiorEsameCommissioni");
var numEmendApprovatiMinorEsameCommissioni = commissioneTarget.get("numEmendApprovatiMinorEsameCommissioni");
var numEmendApprovatiGiuntaEsameCommissioni = commissioneTarget.get("numEmendApprovatiGiuntaEsameCommissioni");
var numEmendApprovatiMistoEsameCommissioni = commissioneTarget.get("numEmendApprovatiMistoEsameCommissioni");

var nonAmmissibiliEsameCommissioni = commissioneTarget.get("nonAmmissibiliEsameCommissioni");
var decadutiEsameCommissioni = commissioneTarget.get("decadutiEsameCommissioni");
var ritiratiEsameCommissioni = commissioneTarget.get("ritiratiEsameCommissioni");
var respintiEsameCommissioni = commissioneTarget.get("respintiEsameCommissioni");
var noteEmendamentiEsameCommissioni = commissioneTarget.get("noteEmendamentiEsameCommissioni");

var dataPresaInCaricoProposta = filterParam(commissioneTarget.get("dataPresaInCaricoProposta"));
var dataIntesa = filterParam(commissioneTarget.get("dataIntesa"));
var esitoVotazioneIntesa = filterParam(commissioneTarget.get("esitoVotazioneIntesa"));
var noteClausolaValutativa = filterParam(commissioneTarget.get("noteClausolaValutativa"));


if(checkIsNotNull(id)){
	
	var attoNode = utils.getNodeFromString(id);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	var commissioneFolderNode = null;
	
	//cerco la commissione di riferimento dell'utente corrente
	var commissioniXPathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniXPathQuery)[0];
	
	if(checkIsNotNull(commissioneUtente)){
		var commissioneUtenteXPathQuery = "*[@cm:name=\""+commissioneUtente+"\"]";
		var commissioneUtenteResults = commissioniFolderNode.childrenByXPath(commissioneUtenteXPathQuery);
		if(commissioneUtenteResults!=null && commissioneUtenteResults.length>0){
			commissioneFolderNode = commissioneUtenteResults[0];
	
			if(numEmendPresentatiMaggiorEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendPresentatiMaggiorEsame"] = numEmendPresentatiMaggiorEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendPresentatiMaggiorEsame"] = null;
			}
			
			if(numEmendPresentatiMinorEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendPresentatiMinorEsame"] = numEmendPresentatiMinorEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendPresentatiMinorEsame"] = null;
			}
			
			if(numEmendPresentatiGiuntaEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendPresentatiGiuntaEsame"] = numEmendPresentatiGiuntaEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendPresentatiGiuntaEsame"] = null;
			}
			
			if(numEmendPresentatiMistoEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendPresentatiMistoEsame"] = numEmendPresentatiMistoEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendPresentatiMistoEsame"] = null;
			}
			

			
			
			if(numEmendApprovatiMaggiorEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendApprovatiMaggiorEsame"] = numEmendApprovatiMaggiorEsameCommissioni;			
			}else{
				commissioneFolderNode.properties["crlatti:numEmendApprovatiMaggiorEsame"] = null;		
			}
			
			if(numEmendApprovatiMinorEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendApprovatiMinorEsame"] = numEmendApprovatiMinorEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendApprovatiMinorEsame"] = null;
			}
			
			if(numEmendApprovatiGiuntaEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendApprovatiGiuntaEsame"] = numEmendApprovatiGiuntaEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendApprovatiGiuntaEsame"] = null;
			}
			
			if(numEmendApprovatiMistoEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendApprovatiMistoEsame"] = numEmendApprovatiMistoEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendApprovatiMistoEsame"] = null;
			}
			
			
			
			
			if(nonAmmissibiliEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendNonAmmissibiliEsame"] = nonAmmissibiliEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendNonAmmissibiliEsame"] = null;
			}
			
			if(decadutiEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendDecadutiEsame"] = decadutiEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendDecadutiEsame"] = null;
			}
			
			if(ritiratiEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendRitiratiEsame"] = ritiratiEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendRitiratiEsame"] = null;
			}
			
			if(respintiEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:numEmendRespintiEsame"] = respintiEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:numEmendRespintiEsame"] = null;
			}
			
			if(noteEmendamentiEsameCommissioni!="null"){
				commissioneFolderNode.properties["crlatti:noteEmendamentiEsame"] = noteEmendamentiEsameCommissioni;
			}else{
				commissioneFolderNode.properties["crlatti:noteEmendamentiEsame"] = null;
			}
			
			
			
	
			var dataPresaInCaricoPropostaParsed= null;
			if(checkIsNotNull(dataPresaInCaricoProposta)){
				var dataPresaInCaricoPropostaSplitted = dataPresaInCaricoProposta.split("-");
				dataPresaInCaricoPropostaParsed = new Date(dataPresaInCaricoPropostaSplitted[0],dataPresaInCaricoPropostaSplitted[1]-1,dataPresaInCaricoPropostaSplitted[2]);
			}
			
			var dataIntesaParsed= null;
			if(checkIsNotNull(dataIntesa)){
				var dataIntesaSplitted = dataIntesa.split("-");
				dataIntesaParsed = new Date(dataIntesaSplitted[0],dataIntesaSplitted[1]-1,dataIntesaSplitted[2]);
			}
	
			commissioneFolderNode.properties["crlatti:dataPresaInCaricoPropClausolaValutEsame"] = dataPresaInCaricoPropostaParsed;
			commissioneFolderNode.properties["crlatti:dataIntesaClausolaValutEsame"] = dataIntesaParsed;
			commissioneFolderNode.properties["crlatti:esitoVotazioneIntesaClausolaValutEsame"] = esitoVotazioneIntesa;
			commissioneFolderNode.properties["crlatti:noteClausolaValutativaEsame"] = noteClausolaValutativa;
	
			commissioneFolderNode.save();
	
		} else {
			status.code = 400;
			status.message = "commissione utente non valorizzata";
			status.redirect = true;
		}
	} 
	
	model.atto = attoNode;
	model.commissione = commissioneFolderNode;
	
} else {
	status.code = 400;
	status.message = "id atto, passaggio e commissione non valorizzato";
	status.redirect = true;
}