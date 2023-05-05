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

function getLabelGruppoCommissione(gruppo) {

	var labelSplittedLength = gruppo.properties['cm:authorityDisplayName'].split("_").length;

	var label = gruppo.properties['cm:authorityDisplayName'].split("_")[labelSplittedLength - 1];

	return "COMM_" + label;

}

function getLabelGruppoDefault(gruppo) {
	var labelGroup = "";
	// * I gruppi ALFRESCO_ADMINISTRATORS e EMAIL_CONTRIBUTORS non hanno di default la property 'cm:authorityDisplayName'
	if (gruppo.properties['cm:authorityDisplayName'] == null) {
		labelGroup = gruppo.properties['cm:authorityName'].substring(6);
	} else {
		labelGroup = gruppo.properties['cm:authorityDisplayName'];
	}
	return labelGroup;
}

function getCommissioneGroups(gruppi){
	
	var gruppiCommissione = new Array();
	
	for(var i=0; i< gruppi.length; i++){

		var gruppo = groups.getGroup(gruppi[i].properties["cm:authorityName"].substring(6));
		if(gruppo.getParentGroups().length>0){
			if(gruppo.getParentGroups()[0].getShortName() == "Commissioni"){
				gruppiCommissione.push(getLabelGruppoCommissione(gruppi[i]));
			}else{
				gruppiCommissione.push(getLabelGruppoDefault(gruppi[i]));
			}
		}else{
			if (gruppo.getShortName()!="Commissioni"){
				gruppiCommissione.push(getLabelGruppoDefault(gruppi[i]));
			}
		}
	}
	return gruppiCommissione;
}

