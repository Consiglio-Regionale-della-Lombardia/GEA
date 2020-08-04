var sedutePath = "/app:company_home" +
"/cm:"+search.ISO9075Encode("CRL") +
"/cm:"+search.ISO9075Encode("Gestione Atti") +
"/cm:"+search.ISO9075Encode("Sedute") +"//*";

var seduteLuceneQuery = "PATH:\""+sedutePath+"\" AND TYPE:\"crlatti:sedutaODG\"";
var seduteResults = search.luceneSearch(seduteLuceneQuery);

if(seduteResults!=null && seduteResults.length > 0){
	
	for(var i=0; i<seduteResults.length; i++){
		
		var seduta = seduteResults[i];
		
		seduta.properties["crlatti:legislaturaSedutaODG"] = "X";
		seduta.save();
		
	}

}
