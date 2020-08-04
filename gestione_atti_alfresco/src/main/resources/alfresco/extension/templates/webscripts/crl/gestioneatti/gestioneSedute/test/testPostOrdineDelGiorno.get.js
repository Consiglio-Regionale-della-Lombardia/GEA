var luceneQueryAtti = "TYPE:\"crlatti:atto\"";
var atti = search.luceneSearch(luceneQueryAtti);
model.atti = atti;

var luceneQueryAtti = "TYPE:\"crlatti:sedutaODG\"";
var sedute = search.luceneSearch(luceneQueryAtti);
model.sedute = sedute;