// prendo document


// faccio una ricerca di tutti gli atti che non hanno 

if(document.properties["crlatti:chiusuraLegislatura"] == true){
	
	var legislaturaAttiPath = "/app:company_home"+
	"/cm:"+search.ISO9075Encode("CRL")+
	"/cm:"+search.ISO9075Encode("Gestione Atti")+
	"/cm:"+search.ISO9075Encode(document.name);


	var attiList = search.luceneSearch("PATH:\""+legislaturaAttiPath+"//*\" AND TYPE:\"crlatti:atto\"")


	for(var i=0; i<attiList.length; i++){
		var attoTemp = attiList[i];
		if(attoTemp.properties["crlatti:statoAtto"]!="Chiuso"){
			attoTemp.properties["crlatti:statoAtto"] = "Chiuso";
			attoTemp.properties["crlatti:tipoChiusura"] = "Per decadenza (fine legislatura)";
			attoTemp.save();
		}
	}

}
