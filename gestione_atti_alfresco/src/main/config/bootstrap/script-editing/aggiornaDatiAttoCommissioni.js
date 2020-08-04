// Ridondanza delle informazioni del nodo atto sui nodi commissione

var commissioni = space.getChildAssocsByType("crlatti:commissione");


var passaggio = document.parent.parent;

var atto = passaggio.parent.parent;


var nomeAtto = atto.properties["crlatti:numeroAtto"];
if(atto.properties["crlatti:estensione"]!=null){
	nomeAtto += atto.properties["crlatti:estensione"];
}

document.properties["crlatti:nomeAttoCommissione"] = nomeAtto;
document.properties["crlatti:numeroAttoCommissione"] = atto.properties["crlatti:numeroAtto"];
document.properties["crlatti:tipoAttoCommissione"] = atto.typeShort.substring(12);
document.properties["crlatti:dataSedutaCommAttoCommissione"] = atto.properties["crlatti:dataSedutaComm"];


document.save();