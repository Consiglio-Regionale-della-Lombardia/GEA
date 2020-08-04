<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");


var relatori = filterParam(atto.get("relatori"));


if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);
	
	// gestione passaggi
	var relatoriXPathQuery = "*[@cm:name='RelatoriAtto']";
	var relatoriFolderNode = attoNode.childrenByXPath(relatoriXPathQuery)[0];
	
	
	var numeroRelatori = relatori.length();
	//relatori
	
	for(var k=0; k<numeroRelatori; k++) {
		
		var relatore = relatori.get(k).get("relatore");
	
		var descrizione = filterParam(relatore.get("descrizione"));
		var dataNomina = filterParam(relatore.get("dataNomina"));
		var dataUscita = filterParam(relatore.get("dataUscita"));
		
		//verifica l'esistenza di un relatore nel repository
		var existRelatoreXPathQuery = "*[@cm:name=\""+descrizione+"\"]";
		var relatoreEsistenteResults = relatoriFolderNode.childrenByXPath(existRelatoreXPathQuery);
		var relatoreNode = null;
		if(relatoreEsistenteResults!=null && relatoreEsistenteResults.length>0){
			relatoreNode = relatoreEsistenteResults[0];
		} else {
			relatoreNode = relatoriFolderNode.createNode(descrizione,"crlatti:relatore");
			relatoreNode.content = descrizione;
		}
		
		var dataNominaParsed = null;
		if(checkIsNotNull(dataNomina)){
			var dataNominaSplitted = dataNomina.split("-");
			dataNominaParsed = new Date(dataNominaSplitted[0],dataNominaSplitted[1]-1,dataNominaSplitted[2]);
		}
		
		var dataUscitaParsed = null;
		if(checkIsNotNull(dataUscita)){
			var dataUscitaSplitted = dataUscita.split("-");
			dataUscitaParsed = new Date(dataUscitaSplitted[0],dataUscitaSplitted[1]-1,dataUscitaSplitted[2]);
		}
		
		relatoreNode.properties["crlatti:dataNominaRelatore"] = dataNominaParsed;
		relatoreNode.properties["crlatti:dataUscitaRelatore"] = dataUscitaParsed;
		relatoreNode.save();
		
	}	
	
	//verifica relatori da cancellare
	var relatoriNelRepository = relatoriFolderNode.getChildAssocsByType("crlatti:relatore");
	
		
	//query nel repository per capire se bisogna cancellare alcuni relatori
	for(var z=0; z<relatoriNelRepository.length; z++){
		var trovato = false;
		var relatoreNelRepository = relatoriNelRepository[z];
		
		//cerco il nome del relatore nel repo all'interno del json
		for (var q=0; q<relatori.length(); q++){
			var relatore = relatori.get(q).get("relatore");
			var descrizione = filterParam(relatore.get("descrizione"));
			if(""+descrizione+""==""+relatoreNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			var relatoriSpace = relatoreNelRepository.parent;
			relatoreNelRepository.remove();
			
			
		}
	}
	

	attoNode.properties["crlatti:statoAtto"] = statoAtto;
	attoNode.save();


	model.atto = attoNode;
	model.relatori = relatoriFolderNode.getChildAssocsByType("crlatti:relatore");
	
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}