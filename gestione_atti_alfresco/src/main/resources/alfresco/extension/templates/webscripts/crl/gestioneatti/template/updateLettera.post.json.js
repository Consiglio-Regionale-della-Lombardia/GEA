<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var lettera = json.get("lettera");
var tipoTemplate = lettera.get("tipoTemplate");



if(lettera!=null
		&& tipoTemplate!=null) {
	
	var firmatario = lettera.get("firmatario");
		
	var templatesPath = "/app:company_home"+
	"/cm:"+search.ISO9075Encode("CRL")+
	"/cm:"+search.ISO9075Encode("Gestione Atti")+
	"/cm:"+search.ISO9075Encode("Templates");
	
	var luceneQuery = "PATH:\""+templatesPath+"//*\" AND TYPE:\""+tipoTemplate+"\"";
	var templateResults = search.luceneSearch(luceneQuery);
	
	var templateNode = templateResults[0];
	
	templateNode.properties["crltemplate:firmatario"] = firmatario;
	
	// Identifico tutti i template aula
	if(templateNode.isSubType("crltemplate:letteraAula")){
		
		var ufficio = lettera.get("ufficio");
		templateNode.properties["crltemplate:ufficio"] = ufficio;
		
	}
	
	// casi particolari
	if(tipoTemplate == "crltemplate:letteraTrasmissionePDAVariazioneBilancioAula") {
	
                var direzione = lettera.get("direzione");
		templateNode.properties["crltemplate:direzione"] = direzione;
		
		
	}else if(tipoTemplate == "crltemplate:letteraTrasmissioneBURLAula"){
		
		var numeroTelFirmatario = lettera.get("numeroTelFirmatario");
		var emailFirmatario = lettera.get("emailFirmatario");
		templateNode.properties["crltemplate:numeroTelFirmatario"] = numeroTelFirmatario;
		templateNode.properties["crltemplate:emailFirmatario"] = emailFirmatario;
		
	} else if(tipoTemplate == "crltemplate:letteraTrasmissionePDABilancioAula") {
            
                var assessore = lettera.get("assessore");
		templateNode.properties["crltemplate:assessore"] = assessore;
                
                var direzione = lettera.get("direzione");
		templateNode.properties["crltemplate:direzione"] = direzione;
            
        }

	
	templateNode.save();
	
} else {
	status.code = 500;
	status.message = "lettera e tipoTemplate sono obbligatori";
	status.redirect = true;
}