var atto = json.get("atto");
var id = atto.get("id");
var stato = atto.get("stato");
var dataPresaInCarico = atto.get("dataPresaInCarico");

if(atto!=null
		&& id!=null
		&& stato!=null
		&& dataPresaInCarico!=null) {
	
	var attoFolderNode = utils.getNodeFromString(id);
	
	// flag per l'export verso il sistema di gestione Atti di Indirizzo
	// controllo che sia la prima volta che effettuo una presa in carico (dataPresaInCarico non deve essere già valorizzata)
	if(attoFolderNode.properties["crlatti:dataPresaInCarico"]==null || attoFolderNode.properties["crlatti:dataPresaInCarico"]==""){
		attoFolderNode.properties["crlatti:statoExportAttiIndirizzo"] = "CREATE";
	}
	
	var dataPresaInCaricoSplitted = dataPresaInCarico.split("-");
	var dataPresaInCaricoParsed = new Date(dataPresaInCaricoSplitted[0],dataPresaInCaricoSplitted[1]-1,dataPresaInCaricoSplitted[2]);

	
	attoFolderNode.properties["crlatti:statoAtto"] = stato;
	attoFolderNode.properties["crlatti:dataPresaInCarico"] = dataPresaInCaricoParsed;
	
	attoFolderNode.save();
	
} else {
	status.code = 500;
	status.message = "atto, id, stato e dataPresaInCarico sono obbligatori";
	status.redirect = true;
}