var username = person.properties.userName;
if(username=="protocollo" || username=="admin"){

	var idProtocolloAtto = "";
	var idProtocolloAllegato = "";
	var filename = "";
	var content = null;
	
	for each (field in formdata.fields) {
	  if (field.name == "idProtocolloAtto") {
		  idProtocolloAtto = field.value;
	  } else if (field.name == "idProtocolloAllegato") {
		    idProtocolloAllegato = field.value;
	  } else if (field.name == "file" && field.isFile) {
	    filename = field.filename;
	    content = field.content;
	  }
	}
	
	if(idProtocolloAtto == ""){
		status.code = 400;
		status.message = "ID protocollo atto non valorizzato";
		status.redirect = true;
	} else if(idProtocolloAllegato == ""){
		status.code = 400;
		status.message = "ID protocollo allegato non valorizzato";
		status.redirect = true;
	} else if(filename == ""){
		status.code = 400;
		status.message = "filename allegato non valorizzato";
		status.redirect = true;
	} else if(content==null){ 
		status.code = 400;
		status.message = "content allegato non valorizzato";
		status.redirect = true;
	} else {
		var attoLuceneQuery = "TYPE:\"crlatti:atto\" AND @crlatti\\:idProtocollo:\""+idProtocolloAtto+"\"";
		var attoResults = search.luceneSearch(attoLuceneQuery);
		
		if(attoResults!=null && attoResults.length>0){
			var attoFolderNode = attoResults[0];
			var allegatiFolderNode = attoFolderNode.childrenByXPath("*[@cm:name='Allegati']")[0];
			
			//verifica esistenza allegato
			var allegatoResults = allegatiFolderNode.childrenByXPath("*[@cm:name='"+filename+"']");
			var allegatoNode = null;
			if(allegatoResults!=null && allegatoResults.length>0){
				allegatoNode = allegatoResults[0];	
			} else {
			    allegatoNode = allegatiFolderNode.createNode(filename,"crlatti:allegato");
			}
			
			allegatoNode.properties["crlatti:idProtocollo"] = idProtocolloAllegato;
			allegatoNode.properties.content.write(content);
			allegatoNode.properties.content.setEncoding("UTF-8");
			allegatoNode.properties.content.guessMimetype(filename);
			allegatoNode.save();
			
			
			
			
					
		} else {
			status.code = 400;
			status.message = "Atto con idProtocollo: \""+idProtocolloAtto+"\" non trovato";
			status.redirect = true;
		}	
	}
	
} else {
	status.code = 401;
	status.message = "utenza non abilitata ad accedere a questo servizio";
	status.redirect = true;
}