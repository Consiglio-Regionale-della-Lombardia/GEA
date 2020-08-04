<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var id = args.id;

if(checkIsNotNull(id)){
	var firmatarioNode = utils.getNodeFromString(id);
    var firmatarioName = firmatarioNode.name;
	var firmatariSpace = firmatarioNode.parent;
	firmatarioNode.remove();
	
	/*
	 * in eliminazione i firmatari devono essere gestiti manualmente 
	 * a causa di un bug di Alfresco risolto nella 4.1.1:
	 * 
	 * https://issues.alfresco.com/jira/browse/ALF-12711
	 * 
	*/
	
	var firmatari = firmatariSpace.getChildAssocsByType("crlatti:firmatario");
	var firmatariAtto = new Array(firmatari.length);
	for (var i=0; i<firmatari.length; i++) {
		firmatariAtto[i] = firmatari[i].name;
	}
	var attoNode = firmatariSpace.parent;
	attoNode.properties["crlatti:firmatari"] = firmatariAtto;
    var firmatariDeleted = attoNode.properties["crlatti:firmatariDeleted"];
    if (!firmatariDeleted){
        firmatariDeleted = [];
    }
    if (firmatariDeleted.indexOf(firmatarioName) == -1){
        firmatariDeleted.push(firmatarioName);
    }
    attoNode.properties["crlatti:firmatariDeleted"] = firmatariDeleted;
	attoNode.save();
	
	
} else {
	status.code = 400;
	status.message = "id non valorizzato";
	status.redirect = true;
}