    <import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/commonMigrazione.js">

    var atto = json.get("atto").get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var passaggio = json.get("target").get("target").get("passaggio");

// selezione del passaggio corrente
var passaggioTarget = getPassaggioTarget(json, passaggio);

var dataPresaInCarico = filterParam(passaggioTarget.get("aula").get("dataPresaInCaricoEsameAula"));
var relazioneScritta = filterParam(passaggioTarget.get("aula").get("relazioneScritta"));

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
        
    //
    if(attoNode.typeShort == "crlatti:attoOrg" || (attoNode.typeShort == "crlatti:attoPda" && attoNode.properties["crlatti:tipoIniziativa"] == "05_ATTO DI INIZIATIVA UFFICIO PRESIDENZA")) {
        if(aulaFolderNode.properties["crlatti:dataPresaInCaricoEsameAula"]==null || aulaFolderNode.properties["crlatti:dataPresaInCaricoEsameAula"]==""){
            attoNode.properties["crlatti:statoExportAttiIndirizzo"] = "CREATE";
	}
    }
    
    // presa in carico
    if(checkIsNotNull(dataPresaInCarico)) {
        var dataPresaInCaricoSplitted = dataPresaInCarico.split("-");
        var dataPresaInCaricoParsed = new Date(dataPresaInCaricoSplitted[0],dataPresaInCaricoSplitted[1]-1,dataPresaInCaricoSplitted[2]);
        aulaFolderNode.properties["crlatti:dataPresaInCaricoEsameAula"] = dataPresaInCaricoParsed;
    }
		
    aulaFolderNode.properties["crlatti:relazioneScrittaAula"] = relazioneScritta;
	
    aulaFolderNode.save();
			
    attoNode.properties["crlatti:statoAtto"] = statoAtto;
    attoNode.save();

    model.atto = attoNode;
    model.aula = aulaFolderNode;
	
} else {
    status.code = 400;
    status.message = "id atto e passaggio non valorizzato";
    status.redirect = true;
}