//Script per la bonifica dei nodi con uno stato inconsistente del metadato tipologia a causa di un baco dello script protocollo
var luceneQuery = "TYPE:\"crlatti:allegato\" AND (ISNULL:\"crlatti:tipologia\" OR @crlatti\\:tipologia:\"\") AND @cm\\:creator:\"protocollo\" AND PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:X/*/*/*/*/cm:Allegati/*\"";

var results = search.luceneSearch(luceneQuery);
for(var i=0; i<results.length; i++){
	var testoAtto = results[i];
	var nomeTesto = testoAtto.name;
	var attoTesto = testoAtto.parent.parent.name;
	var tipoAttoTesto = testoAtto.parent.parent.parent;
	var toStringTesto = "Allegato: "+nomeTesto+ " | Atto: "+attoTesto+ " | Tipo: "+tipoAttoTesto.name;
	
	protocolloLogger.info("Inizio processamento "+toStringTesto);
	
	testoAtto.properties["crlatti:tipologia"] = "allegato_atto";
	testoAtto.save();
	
	protocolloLogger.info("Fine processamento "+toStringTesto);

}