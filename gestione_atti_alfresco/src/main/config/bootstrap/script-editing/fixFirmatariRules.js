var childrenXPathQuery = "*[@cm:name='Firmatari']";
var firmatariFolderNode = document.childrenByXPath(childrenXPathQuery)[0];

firmatariFolderNode.name = "FirmatariOld";
firmatariFolderNode.save();

var firmatariSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Firmatari\"";
var firmatariSpaceTemplateNode = search.luceneSearch(firmatariSpaceTemplateQuery)[0];
var firmatariFolderNew = firmatariSpaceTemplateNode.copy(document);

for each (firmatarioEsistente in firmatariFolderNode.children){
	if (firmatarioEsistente.isDocument) {
		firmatarioEsistente.move(firmatariFolderNew);
	}
}
	
/* firmatariFolderNode.remove(); */