//Script per la bonifica dei nodi con uno stato inconsistente del metadato tipologia a causa di un baco della web application
var luceneQuery = "TYPE:\"crlatti:testo\" AND (ISNULL:\"crlatti:tipologia\" OR @crlatti\\:tipologia:\"\" OR @crlatti\\:tipologia:\"allegato_atto\")";
var results = search.luceneSearch(luceneQuery);
for(var i=0; i<results.length; i++){
	var testoAtto = results[i];
	var nomeTesto = testoAtto.name;
	var attoTesto = testoAtto.parent.parent.name;
	var tipoAttoTesto = testoAtto.parent.parent.parent;
	var toStringTesto = "testo: "+nomeTesto+ " | Atto: "+attoTesto+ " | Tipo: "+tipoAttoTesto.name;
	
	protocolloLogger.info("Inizio processamento "+toStringTesto);
	
	testoAtto.properties["crlatti:tipologia"] = "testo_atto";
	testoAtto.save();
	
	protocolloLogger.info("Fine processamento "+toStringTesto);

}