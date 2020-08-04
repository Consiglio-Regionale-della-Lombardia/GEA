<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var gruppi = new Array();

// Controllare che il gruppo dell'utente sia relativo alla legislatura corrente

var legislaturaCorrente = getLegislaturaCorrente();

if(legislaturaCorrente != null){
	
	var gruppiUtente =  people.getContainerGroups(person);
	
	// N.B. Nella gestione dei gruppi, gli utenti possono avere solo un gruppo associato
	// GRUPPI da gestire
	
//	-> Commissioni hanno COMM_ prefisso
//	-> ServizioCommissioni
//	-> Aula
//	-> CPCV
//	-> Administrator
//	-> guest
//	
//	Servizio commissioni
//	Commissioni (vari ruoli) Giunta per il regolamento
//	Aula
//	CPCV
//	Guest (sola lettura)
//	Amministratore
//	
	
	var commissioneGroups = getCommissioneGroups(gruppiUtente);
	

}


model.gruppi = commissioneGroups;


function getCommissioneGroups(gruppi){
	
	var gruppiCommissione = new Array();
	
	for(var i=0; i< gruppi.length; i++){

		var gruppo = groups.getGroup(gruppi[i].properties["cm:authorityName"].substring(6));
		if(gruppo.getParentGroups().length>0){
			if(gruppo.getParentGroups()[0].getShortName() == "Commissioni"){
				var gruppoToken = gruppi[i].properties['cm:authorityName'].split("_");
				gruppiCommissione.push("COMM_" + gruppoToken[2]);
			}else{
				gruppiCommissione.push(gruppi[i].properties['cm:authorityName'].substring(6));
			}
		}else{
			if (gruppo.getShortName()!="Commissioni"){
				gruppiCommissione.push(gruppi[i].properties['cm:authorityName'].substring(6));
			}
		}
	}
	return gruppiCommissione;
}

