var attiEacQuery = '+TYPE:"crlatti:attoEac" + PATH:"/app:company_home/cm:CRL/*/cm:EAC/cm:X//*"';
var attoResults = search.luceneSearch(attiEacQuery);
for each (var atto in attoResults) {
    atto.properties["crlatti:legislatura"]="X";
    atto.save();
}