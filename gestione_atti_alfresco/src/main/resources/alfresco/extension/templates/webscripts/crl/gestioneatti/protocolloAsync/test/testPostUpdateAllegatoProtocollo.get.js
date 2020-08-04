var luceneQueryAtti = "TYPE:\"crlatti:allegato\" TYPE:\"crlatti:testo\" @cm\\:modified:[2015-01-01T00:00:00 TO NOW]";
var attiResults = search.luceneSearch(luceneQueryAtti);
model.attiResults = attiResults;