var luceneQueryAtti = "TYPE:\"crlatti:atto\"";
var attiResults = search.luceneSearch(luceneQueryAtti);
model.atti = attiResults;


var luceneQueryLettere = "TYPE:\"crltemplate:lettera\"";
var lettereResults = search.luceneSearch(luceneQueryLettere);
model.lettere = lettereResults;