var username = person.properties.userName;
if(username=="protocollo" || username=="admin"){
	
	var idProtocolloAllegato = "";
	var filename = "";
	var content = null;
	
	for each (field in formdata.fields) {
	  if (field.name == "idProtocolloAllegato") {
		  idProtocolloAllegato = field.value;
	  } else if (field.name == "file" && field.isFile) {
	    filename = field.filename;
	    content = field.content;
	  }
	}
	
	if(idProtocolloAllegato == ""){
		status.code = 400;
		status.message = "ID protocollo non valorizzato";
		status.redirect = true;
	} else if(filename == ""){
		status.code = 400;
		status.message = "filename allegato non valorizzato";
		status.redirect = true;
	} else if(content==null){ 
		status.code = 400;
		status.message = "binario allegato non valorizzato";
		status.redirect = true;
	} else {
		var allegatoLuceneQuery = "@crlatti\\:idProtocollo:\""+idProtocolloAllegato+"\"";
		var allegatoResults = search.luceneSearch(allegatoLuceneQuery);
		if(allegatoResults!=null && allegatoResults.length>0){
			var allegatoNode = allegatoResults[0];
			allegatoNode.properties.name=filename;
			allegatoNode.properties.content.write(content);
			allegatoNode.properties.content.setEncoding("UTF-8");
			allegatoNode.properties.content.guessMimetype(filename);
			allegatoNode.save();
		} else {
			status.code = 400;
			status.message = "Allegato con idProtocollo:\""+idProtocolloAllegato+"\" non trovato";
			status.redirect = true;
		}	
	}

} else {
	status.code = 401;
	status.message = "utenza non abilitata ad accedere a questo servizio";
	status.redirect = true;
}