<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var attoRecord = json.get("attoRecord");

var id = attoRecord.get("id");

var nome = filterParam(attoRecord.get("nome"));
var mimetype = filterParam(attoRecord.get("mimetype"));
var tipologia = filterParam(attoRecord.get("tipologia"));
var pubblico = filterParam(attoRecord.get("pubblico"));
var pubblicoOpendata = filterParam(attoRecord.get("pubblicoOpendata"));
var provenienza = filterParam(attoRecord.get("provenienza"));

if(checkIsNotNull(id)){
	
	var attoRecordNode = utils.getNodeFromString(id);
	
	attoRecordNode.name = nome;
	attoRecordNode.properties["crlatti:tipologia"] = tipologia;
	attoRecordNode.properties["crlatti:pubblico"] = pubblico;
	if (pubblicoOpendata){
		var attoNode=attoRecordNode.parentAssocs["cm:contains"][0].parentAssocs["cm:contains"][0].parentAssocs["cm:contains"][0].parentAssocs["cm:contains"][0].parentAssocs["cm:contains"][0].parentAssocs["cm:contains"][0];
		var attiPubbliciOpendata=attoNode.childrenByXPath("*//*[@crlatti:pubblicoOpendata='true']");
		for each (attoPubblicoOpendata in attiPubbliciOpendata) {
			if (attoPubblicoOpendata.properties["crlatti:tipologiaTesto"] != null && attoPubblicoOpendata.properties["crlatti:tipologiaTesto"] == tipologia) {
				attoPubblicoOpendata.properties["crlatti:pubblicoOpendata"]=false;
				attoPubblicoOpendata.save();
			}
		}
		attoRecordNode.properties["crlatti:pubblicoOpendata"] = pubblicoOpendata;
	} else if (!pubblicoOpendata){
		attoRecordNode.properties["crlatti:pubblicoOpendata"] = pubblicoOpendata;
	}
	attoRecordNode.properties["crlatti:provenienza"] = provenienza;
	
	attoRecordNode.save();
	model.attoRecord = attoRecordNode;	
	
}else{
	status.code = 400;
	status.message = "id atto record non valorizzato";
	status.redirect = true;
}