<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var estensioneAtto= atto.get("estensioneAtto");
var classificazione = filterParam(atto.get("classificazione"));
var oggetto = filterParam(atto.get("oggetto"));
var numeroRepertorio = filterParam(atto.get("numeroRepertorio"));
var dataRepertorio = filterParam(atto.get("dataRepertorio"));
var tipoIniziativa = filterParam(atto.get("tipoIniziativa"));
var dataIniziativa = filterParam(atto.get("dataIniziativa"));
var descrizioneIniziativa = filterParam(atto.get("descrizioneIniziativa"));
var assegnazione = filterParam(atto.get("assegnazione"));
var numeroDgr = filterParam(atto.get("numeroDgr"));
var dataDgr = filterParam(atto.get("dataDgr"));
var firmatari = filterParam(atto.get("firmatari"));
var pubblico = filterParam(atto.get("pubblico"));
var oggettoOriginale = filterParam(atto.get("oggettoOriginale"));

var scadenza60gg = filterParam(atto.get("scadenza60gg"));
var iterAula = filterParam(atto.get("iterAula"));
var attoProseguente = filterParam(atto.get("attoProseguente"));
var tipologia = filterParam(atto.get("tipologia"));

// var di controllo per la creazione del file xml di export verso atti indirizzo
var exportAttiIndirizzo = false;


if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);
	attoNode.properties["crlatti:classificazione"] = classificazione;
	
	// controllo per variabile exportAttiIndirizzo
	// occorre generare l'xml  nel caso in cui cambia l'oggetto
	if(oggetto != attoNode.properties["crlatti:oggetto"]){
		exportAttiIndirizzo = true;
	}
	attoNode.properties["crlatti:estensioneAtto"]=estensioneAtto;
	attoNode.properties["crlatti:oggetto"] = oggetto;
	attoNode.properties["crlatti:numeroRepertorio"] = numeroRepertorio;
	attoNode.properties["crlatti:pubblico"] = pubblico;
	attoNode.properties["crlatti:oggettoOriginale"] = oggettoOriginale;
	
	attoNode.properties["crlatti:scadenza60gg"] = scadenza60gg;
	attoNode.properties["crlatti:iterAula"] = iterAula;
	attoNode.properties["crlatti:attoProseguente"] = attoProseguente;
        attoNode.properties["crlatti:tipologia"] = tipologia;

	
	if(checkIsNotNull(dataRepertorio)){
		var dataRepertorioSplitted = dataRepertorio.split("-");
		var dataRepertorioParsed = new Date(dataRepertorioSplitted[0],dataRepertorioSplitted[1]-1,dataRepertorioSplitted[2]);
		attoNode.properties["crlatti:dataRepertorio"] = dataRepertorioParsed;
	}
	
	attoNode.properties["crlatti:tipoIniziativa"] = tipoIniziativa;
	
	if(checkIsNotNull(dataIniziativa)){
		var dataIniziativaSplitted = dataIniziativa.split("-");
		var dataIniziativaParsed = new Date(dataIniziativaSplitted[0],dataIniziativaSplitted[1]-1,dataIniziativaSplitted[2]);
		attoNode.properties["crlatti:dataIniziativa"] = dataIniziativaParsed;
	}
	
	attoNode.properties["crlatti:descrizioneIniziativa"] = descrizioneIniziativa;
	attoNode.properties["crlatti:assegnazione"] = assegnazione;
	
	if(attoNode.typeShort!="crlatti:attoInp"){
		//campi DGR non attivi solo per INP
		attoNode.properties["crlatti:numeroDgr"] = numeroDgr;
		if(checkIsNotNull(dataDgr)){
			var dataDgrSplitted = dataDgr.split("-");
			var dataDgrParsed = new Date(dataDgrSplitted[0],dataDgrSplitted[1]-1,dataDgrSplitted[2]);
			attoNode.properties["crlatti:dataDgr"] = dataDgrParsed;
		}
	}
	attoNode.save();
	
	//gestione firmatari
	var firmatariXPathQuery = "*[@cm:name='Firmatari']";
	var firmatariFolderNode = attoNode.childrenByXPath(firmatariXPathQuery)[0];
	
	if(firmatariFolderNode!=null){
	
		var numeroFirmatari = firmatari.length();
		for (var j=0; j<numeroFirmatari; j++){
			var firmatario = firmatari.get(j).get("firmatario");
			var descrizione = filterParam(firmatario.get("descrizione"));
			var gruppoConsiliare = filterParam(firmatario.get("gruppoConsiliare"));
			var dataFirma = filterParam(firmatario.get("dataFirma"));
			var dataRitiro = filterParam(firmatario.get("dataRitiro"));
			var primoFirmatario = filterParam(firmatario.get("primoFirmatario"));
			var firmatarioPopolare = filterParam(firmatario.get("firmatarioPopolare"));
			var numeroOrdinamento = filterParam(firmatario.get("numeroOrdinamento"));
			
			//verifica l'esistenza del firmatario all'interno del folder Firmatari
			var existFirmatarioXPathQuery = "*[@cm:name=\""+descrizione+"\"]";
			var firmatarioEsistenteResults = firmatariFolderNode.childrenByXPath(existFirmatarioXPathQuery);
			var firmatarioNode = null;
			if(firmatarioEsistenteResults!=null && firmatarioEsistenteResults.length>0){
				firmatarioNode = firmatarioEsistenteResults[0];
			} else {
				firmatarioNode = firmatariFolderNode.createNode(descrizione,"crlatti:firmatario");
				// controllo per variabile exportAttiIndirizzo
				// occorre generare l'xml  nel caso in cui viene creato un nuovo firmatario
				exportAttiIndirizzo = true;
			}
			
			var dataFirmaParsed = null;
			if(checkIsNotNull(dataFirma)){
				var dataFirmaSplitted = dataFirma.split("-");
				dataFirmaParsed = new Date(dataFirmaSplitted[0],dataFirmaSplitted[1]-1,dataFirmaSplitted[2]);
			}
			
			var dataRitiroParsed = null;
			if(checkIsNotNull(dataRitiro)){
				var dataRitiroSplitted = dataRitiro.split("-");
				dataRitiroParsed = new Date(dataRitiroSplitted[0],dataRitiroSplitted[1]-1,dataRitiroSplitted[2]);
			}
			
			firmatarioNode.properties["crlatti:nomeFirmatario"] = descrizione;
			firmatarioNode.properties["crlatti:dataFirma"] = dataFirmaParsed;
			firmatarioNode.properties["crlatti:dataRitiro"] = dataRitiroParsed;
			firmatarioNode.properties["crlatti:isPrimoFirmatario"] = primoFirmatario;
            firmatarioNode.properties["crlatti:isFirmatarioPopolare"] = firmatarioPopolare;
			
			// controllo per variabile exportAttiIndirizzo
			// occorre generare l'xml  nel caso in cui cambia il gruppo consiliare di un firmatario
			if(firmatarioNode.properties["crlatti:gruppoConsiliare"] != gruppoConsiliare){
				exportAttiIndirizzo = true;
			}
			
			firmatarioNode.properties["crlatti:gruppoConsiliare"] = gruppoConsiliare;
			firmatarioNode.properties["crlatti:numeroOrdinamento"] = numeroOrdinamento;
			firmatarioNode.save();
		}
	
	
	
		//verifica firmatari da cancellare
		var firmatariNelRepository = firmatariFolderNode.getChildAssocsByType("crlatti:firmatario");
		
		//query nel repository per capire se bisogna cancellare dei firmatari
		for(var z=0; z<firmatariNelRepository.length; z++){
			var trovato = false;
			var firmatarioNelRepository = firmatariNelRepository[z];
			
			//cerco il nome del firmatario nel repo all'interno del json
			for (var q=0; q<numeroFirmatari; q++){
				var firmatario = firmatari.get(q).get("firmatario");
				var descrizione = filterParam(firmatario.get("descrizione"));
				if(""+descrizione+""==""+firmatarioNelRepository.name+""){
					trovato = true;
					break
				}
			}
			if(!trovato){
				var firmatariSpace = firmatarioNelRepository.parent;
				
				// controllo per variabile exportAttiIndirizzo
				// occorre generare l'xml  nel caso in cui viene eliminato un firmatario
				exportAttiIndirizzo = true;
				
				firmatarioNelRepository.remove();
				/*
				 * in eliminazione i firmatari devono essere gestiti manualmente 
				 * a causa di un bug di Alfresco risolto nella 4.1.1:
				 * 
				 * https://issues.alfresco.com/jira/browse/ALF-12711
				 * 
				*/
				var firmatariNelloSpazio = firmatariSpace.getChildAssocsByType("crlatti:firmatario");
				var firmatariAtto = new Array(firmatariNelloSpazio.length);
				for (var r=0; r<firmatariNelloSpazio.length; r++) {
					firmatariAtto[r] = firmatariNelloSpazio[r].name;
				}
				var attoNode = firmatariSpace.parent;
				attoNode.properties["crlatti:firmatari"] = firmatariAtto;
				attoNode.save();
			}
		}
	
	}
	
	model.atto = attoNode;
	
	if(exportAttiIndirizzo==true){
		attoNode.properties["crlatti:statoExportAttiIndirizzo"]="UPDATE";	
		attoNode.save();
	}
	
	
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}