<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var jsonFirmatario = filterParam(json.get("firmatario"));
/*
    concatenate +"" to force the creation of a javascript string (json.get -> java String which doesn't always behave as expected)
*/
var idAtto = filterParam(jsonFirmatario.get("idAtto")+"");
var descrizione = filterParam(jsonFirmatario.get("descrizione")+"");
var gruppoConsiliare = filterParam(jsonFirmatario.get("gruppoConsiliare")+"");
var dataFirma = filterParam(jsonFirmatario.get("dataFirma")+"");
var dataRitiro = filterParam(jsonFirmatario.get("dataRitiro")+"");
var primoFirmatario = filterParam(jsonFirmatario.get("primoFirmatario")+"");
var firmatarioPopolare = filterParam(jsonFirmatario.get("firmatarioPopolare")+"");
var firmatarioNode = null;

if(checkIsNotNull(jsonFirmatario)
		&& checkIsNotNull(idAtto)
		&& checkIsNotNull(descrizione)){
	
	var attoFolderNode = utils.getNodeFromString(idAtto);
	var children = attoFolderNode.getChildAssocsByType("cm:folder");
	var firmatariFolderNode = null;
	for each (child in children){
		if(child.name=="Firmatari"){
			firmatariFolderNode = child;
		}
	}
	
	
	//ricerca firmatario esistente
	
	for each (firmatarioEsistente in firmatariFolderNode.children){
		var nomeFirmatarioEsistente = firmatarioEsistente.name;
		var descrizioneStringa = ""+descrizione+"";
		if(nomeFirmatarioEsistente==descrizioneStringa){
			firmatarioNode = firmatarioEsistente;
		}
	}
	
	if(firmatarioNode==null){
		//creazione di un nuovo firmatario
		firmatarioNode = firmatariFolderNode.createNode(descrizione,"crlatti:firmatario");	
		firmatarioNode.content = descrizione;
	}
	
	var dataFirmaParsed = null;
	if(checkIsNotNull(dataFirma)){
		var dataFirmaSplitted = dataFirma.split("-");
		dataFirmaParsed = new Date(dataFirmaSplitted[0],dataFirmaSplitted[1]-1,dataFirmaSplitted[2]);
	}
	
	var dataRitiroParsed = null;
	if(checkIsNotNull(dataRitiro)){
		var dataRitiroSplitted = dataRitiro.split("-");
		dataRitiroParsed = new Date(dataRitiroSplitted[0],dataRitiroSplitted[1]-1,dataRitiroSplitted[2]);
	}
	
	firmatarioNode.properties["crlatti:dataFirma"] = dataFirmaParsed;
	firmatarioNode.properties["crlatti:dataRitiro"] = dataRitiroParsed;
	firmatarioNode.properties["crlatti:isPrimoFirmatario"] = primoFirmatario;
    firmatarioNode.properties["crlatti:isFirmatarioPopolare"] = firmatarioPopolare;
	firmatarioNode.properties["crlatti:gruppoConsiliare"] = gruppoConsiliare;
    firmatarioNode.properties["crlatti:isfirmatarioModified"] = true;
	firmatarioNode.save();

    var firmatariDeleted = attoFolderNode.properties["crlatti:firmatariDeleted"];
    var idx = firmatariDeleted.indexOf(descrizione);
    if (idx != -1){
        firmatariDeleted.splice(idx,1);
        attoFolderNode.properties["crlatti:firmatariDeleted"] = firmatariDeleted;
        attoFolderNode.save();
    }
	
	attoFolderNode.properties["crlatti:statoExportAttiIndirizzo"]="UPDATE";
	
} else {
	status.code = 400;
	status.message = "firmatario non valorizzato correttamente: idAtto e descrizione sono obbligatori";
	status.redirect = true;
}

model.firmatario = firmatarioNode;