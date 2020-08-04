// Script per l'esportazione degli atti verso il sistema di Gestione Atti di Indirizzo

attiIndirizzoExportLogger.info("Esportazione Atti verso Sistema di Gestione Atti Indirizzo avviato..."); 

var exportFolderXPathQuery = "*[@cm:name='Export']";
var exportFolderNode = companyhome.childrenByXPath(exportFolderXPathQuery)[0];

var gestioneAttiFolderXPathQuery = "*[@cm:name='Gestione Atti']";
var gestioneAttiFolderNode = exportFolderNode.childrenByXPath(gestioneAttiFolderXPathQuery)[0];

var attiIndirizzoFolderXPathQuery = "*[@cm:name='AttiIndirizzo']";
var attiIndirizzoFolderNode = gestioneAttiFolderNode.childrenByXPath(attiIndirizzoFolderXPathQuery)[0];

var xmlFileList = attiIndirizzoFolderNode.getChildAssocsByType("cm:content");

var attiInviatiXPathQuery = "*[@cm:name='AttiIndirizzoSended']";
var attiInviatiFolderNode = gestioneAttiFolderNode.childrenByXPath(attiInviatiXPathQuery)[0];

var control = false;
var count = 0;

attiIndirizzoExportLogger.info("Inizio esportazione atti. Elementi da esportare: "+xmlFileList.length);


for(var i=0; i<xmlFileList.length; i++){
	
	var xmlFile = xmlFileList[i];
	
	var xmlObject = new XML(xmlFile.content);
	
	var operazione = xmlObject.@operazione;
	var tipo_atto = xmlObject.@tipo_atto;
	var numero_atto = xmlObject.@numero_atto;
	
	var control = cmisClient.sendToAttiIspettiviRepository(xmlFile.nodeRef);
	
	if (control == true){
		
		xmlFile.move(attiInviatiFolderNode);
		attiIndirizzoExportLogger.info("Inviato file export "+xmlFile.name+": operazione "+operazione+" su atto "+tipo_atto+" "+numero_atto);
		count++;
	}
	
}


attiIndirizzoExportLogger.info("Esportazione Atti terminata. Elementi esportati con successo: "+count);








