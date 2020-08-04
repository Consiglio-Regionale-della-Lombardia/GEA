<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var allegato = json.get("allegato");

var id = allegato.get("id");

var nome = filterParam(allegato.get("nome"));
var mimetype = filterParam(allegato.get("mimetype"));
var tipologia = filterParam(allegato.get("tipologia"));
var pubblico = filterParam(allegato.get("pubblico"));
var provenienza = filterParam(allegato.get("provenienza"));
var dataSeduta = filterParam(allegato.get("dataSeduta"));

if(checkIsNotNull(id)){
	
	var allegatoNode = utils.getNodeFromString(id);
	
	allegatoNode.name = nome;
	allegatoNode.properties["crlatti:tipologia"] = tipologia;
	allegatoNode.properties["crlatti:pubblico"] = pubblico;
	allegatoNode.properties["crlatti:provenienza"] = provenienza;
	
	if(allegatoNode.typeShort=="crlatti:testoComitatoRistretto"){
		
		var dataSedutaParsed = null;
		if(checkIsNotNull(dataSeduta)){
			var dataSedutaSplitted = dataSeduta.split("-");
			dataSedutaParsed = new Date(dataSedutaSplitted[0],dataSedutaSplitted[1]-1,dataSedutaSplitted[2]);
			allegatoNode.properties["crlatti:dataSedutaTestoCR"] = dataSedutaParsed;
		}
		
	}
		
	allegatoNode.save();
	model.allegato = allegatoNode;	
	
}else{
	status.code = 400;
	status.message = "id allegato non valorizzato";
	status.redirect = true;
}