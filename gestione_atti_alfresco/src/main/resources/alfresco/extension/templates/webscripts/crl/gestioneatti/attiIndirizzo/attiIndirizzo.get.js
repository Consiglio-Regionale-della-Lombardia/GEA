<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var legislaturaCorrente = getLegislaturaCorrente();

var tipo = args.tipo;

if(tipo==null 
		|| tipo==undefined
		|| tipo==""){
	status.code = 400;
	status.message = "Parametro tipo non presente nella richiesta";
	status.redirect = true;
}


// aggiunta ricerca per range di date
var creazioneDa= args.creazioneDa;

if(creazioneDa==null 
		|| creazioneDa==undefined
		|| creazioneDa==""){
	status.code = 400;
	status.message = "Parametro creazioneDa non presente nella richiesta";
	status.redirect = true;
}

var creazioneA= args.creazioneA;
if(creazioneA==null 
		|| creazioneA==undefined
		|| creazioneA==""){
	status.code = 400;
	status.message = "Parametro creazioneA non presente nella richiesta";
	status.redirect = true;
}

var gestioneAttiPath = "/app:company_home"+
"/cm:"+search.ISO9075Encode("CRL")+
"/cm:"+search.ISO9075Encode("Gestione Atti");

var legislaturaRepositoryPath = gestioneAttiPath + "/cm:"+search.ISO9075Encode(legislaturaCorrente);
var attoIndirizzoLuceneQuery = "PATH:\""+legislaturaRepositoryPath+"//*\"";
//aggiunta ricerca per range di date di creazione e ordinamento decrescente sulla data di creazione
attoIndirizzoLuceneQuery += " AND TYPE:\"crlatti:attoIndirizzo\" AND @crlatti\\:tipoAttoIndirizzo:\""+tipo+"\" AND @cm\\:created:["+creazioneDa+" TO "+creazioneA+"]";
var attoIndirizzoResults = search.luceneSearch(attoIndirizzoLuceneQuery, "@cm:created", false);

model.atti = attoIndirizzoResults;