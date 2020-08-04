<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var id = args.id;
var atto = json.get("atto")
var statoAtto = atto.get("stato");

if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);
    var attoLegislaturaName = attoNode.properties["crlatti:legislatura"];
	var attoNumero = attoNode.properties["crlatti:numeroAtto"];
	var attoTipo=attoNode.qNameType;
	attoNode.properties["crlatti:statoAtto"] = statoAtto;
	attoNode.save();
	
	
} else {
	status.code = 400;
	status.message = "id non valorizzato";
	status.redirect = true;
}