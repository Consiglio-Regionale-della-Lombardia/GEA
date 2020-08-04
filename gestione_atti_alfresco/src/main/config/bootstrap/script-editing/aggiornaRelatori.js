var commissioneNode = space.parent;
var commissioniNode = commissioneNode.parent;
var passaggioNode = commissioniNode.parent;
var passaggiNode =  passaggioNode.parent;
var attoNode = passaggiNode.parent;



var commRelatoriCommissioni = new Array()

// considero tutte le commissioni assegnate all'atto

var commissioniFolderNode = commissioniNode.getChildAssocsByType("crlatti:commissione");
var numeroCommissioni = commissioniFolderNode.length;

for(var i=0; i<numeroCommissioni; i++) {
	
	var commissioneTempNode = commissioniFolderNode[i];
	
	var relatoriCommissioneXPathQuery = "*[@cm:name='Relatori']";
	var relatoriCommissioneFolderNode = commissioneTempNode.childrenByXPath(relatoriCommissioneXPathQuery)[0];
	
	
	var relatoriNode = relatoriCommissioneFolderNode.getChildAssocsByType("crlatti:relatore");
	
	// considero i relatori di ogni commissione
	var numeroRelatori = relatoriNode.length;
	for(var j=0; j<numeroRelatori; j++) {
		commRelatoriCommissioni.push(relatoriNode[j].properties["cm:name"]);	
	}
}


attoNode.properties["crlatti:relatori"] = commRelatoriCommissioni;

attoNode.save();

