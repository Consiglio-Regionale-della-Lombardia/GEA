var firmatari = space.getChildAssocsByType("crlatti:firmatario");
var firmatariAtto = new Array();
var firmatariOriginari = new Array();
var primoFirmatarioAtto = "";

var gruppoFirmatari = new Array();
var gruppoPrimoFirmatario = "";

var firmatariPath = "/app:company_home" +
"/cm:"+search.ISO9075Encode("CRL") +
"/cm:"+search.ISO9075Encode("Gestione Atti") +
"/cm:"+search.ISO9075Encode("Anagrafica") +
"/cm:"+search.ISO9075Encode("ConsiglieriAttivi") +"/*";


firmatari.sort(function(a, b){
	
	var indiceA = a.properties["crlatti:numeroOrdinamento"]; 
	var indiceB = b.properties["crlatti:numeroOrdinamento"]; 
	 
	if (indiceA < indiceB) {
		return -1 
	}
	if (indiceA > indiceB) {
		return 1
	}
	  
	return 0
	
})


for (var i=0; i<firmatari.length; i++) {
	
	if(firmatari[i].properties["crlatti:isPrimoFirmatario"]==true && (firmatari[i].properties["crlatti:dataRitiro"]==null || firmatari[i].properties["crlatti:dataRitiro"]==undefined ) ){
		primoFirmatarioAtto = firmatari[i].name;
		
		// per ridondare il gruppo del firmatario sull'atto (ricerca) lo prelevo dall'anagrafica
		
		var consigliereLuceneQuery = "PATH:\""+firmatariPath+"\" AND TYPE:\"crlatti:consigliereAnagrafica\" AND @cm\\:name:\""+firmatari[i].name+"\"";
		var consigliereResults = search.luceneSearch(consigliereLuceneQuery);
		if(consigliereResults!=null && consigliereResults.length==1){
			consigliereAnagraficaNode = consigliereResults[0];
			gruppoPrimoFirmatario = consigliereAnagraficaNode.properties["crlatti:codiceGruppoConsigliereAnagrafica"];
		} 
		
		
	}else{
		if(firmatari[i].properties["crlatti:dataRitiro"]==null || firmatari[i].properties["crlatti:dataRitiro"]==undefined ){
			firmatariOriginari.push(firmatari[i].name);
		}
	}
	
	if(firmatari[i].properties["crlatti:dataRitiro"]==null || firmatari[i].properties["crlatti:dataRitiro"]==undefined ){
		firmatariAtto.push(firmatari[i].name);
		
		var consigliereLuceneQuery = "PATH:\""+firmatariPath+"\" AND TYPE:\"crlatti:consigliereAnagrafica\" AND @cm\\:name:\""+firmatari[i].name+"\"";
		var consigliereResults = search.luceneSearch(consigliereLuceneQuery);
		if(consigliereResults!=null && consigliereResults.length==1){
			consigliereAnagraficaNode = consigliereResults[0];
			gruppoFirmatari.push(consigliereAnagraficaNode.properties["crlatti:codiceGruppoConsigliereAnagrafica"]);
		} 
	}

	
}

var attoNode = space.parent;
attoNode.properties["crlatti:primoFirmatario"] = primoFirmatarioAtto;
attoNode.properties["crlatti:firmatariOriginari"] = firmatariOriginari;
attoNode.properties["crlatti:firmatari"] = firmatariAtto;
attoNode.properties["crlatti:gruppoFirmatari"] = gruppoFirmatari;
attoNode.properties["crlatti:gruppoPrimoFirmatario"] = gruppoPrimoFirmatario;

attoNode.save();