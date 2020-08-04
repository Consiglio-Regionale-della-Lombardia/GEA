var commissione1Label = "I Commissione - Programmazione e bilancio";
var commissione1NewLabel = "I Commissione permanente - Programmazione e bilancio";

var commissione2Label = "II Commissione - Affari istituzionali";
var commissione2NewLabel = "II Commissione permanente - Affari istituzionali";

var commissione3Label = "III Commissione - Sanità e politiche sociali";
var commissione3NewLabel = "III Commissione permanente - Sanità e politiche sociali";

var commissione4Label = "IV Commissione - Attività produttive e occupazione";
var commissione4NewLabel = "IV Commissione permanente - Attività produttive e occupazione";

var commissione5Label = "V Commissione - Territorio e infrastrutture";
var commissione5NewLabel = "V Commissione permanente - Territorio e infrastrutture";

var commissione6Label = "VI Commissione - Ambiente e protezione civile";
var commissione6NewLabel = "VI Commissione permanente - Ambiente e protezione civile";

var commissione7Label = "VII Commissione - Cultura, istruzione, formazione, comunicazione e sport";
var commissione7NewLabel = "VII Commissione permanente - Cultura, istruzione, formazione, comunicazione e sport";

var commissione8Label = "VIII Commissione - Agricoltura, montagna, foreste e parchi";
var commissione8NewLabel = "VIII Commissione permanente - Agricoltura, montagna, foreste e parchi";

var gestioneSedutePath = "/app:company_home"+
"/cm:"+search.ISO9075Encode("CRL")+
"/cm:"+search.ISO9075Encode("Gestione Atti")+
"/cm:"+search.ISO9075Encode("Sedute")+"/*";

var luceneQuery = "PATH:\""+gestioneSedutePath+"\"";
var provenienze = search.luceneSearch(luceneQuery);
for(var i=0; i<provenienze.length; i++){
	var provenienza = provenienze[i];
	if(provenienza.name==commissione1Label){
		provenienza.name=commissione1NewLabel;
	} else if(provenienza.name==commissione2Label){
		provenienza.name=commissione2NewLabel;
	} else if(provenienza.name==commissione3Label){
		provenienza.name=commissione3NewLabel;
	} else if(provenienza.name==commissione4Label){
		provenienza.name=commissione4NewLabel;
	} else if(provenienza.name==commissione5Label){
		provenienza.name=commissione5NewLabel;
	} else if(provenienza.name==commissione6Label){
		provenienza.name=commissione6NewLabel;
	} else if(provenienza.name==commissione7Label){
		provenienza.name=commissione7NewLabel;
	} else if(provenienza.name==commissione8Label){
		provenienza.name=commissione8NewLabel;
	}
	provenienza.save();
}