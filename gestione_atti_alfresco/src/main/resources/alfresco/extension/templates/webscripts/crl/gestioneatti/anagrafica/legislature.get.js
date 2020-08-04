var legislaturePath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("Legislature") + "/*";

var luceneQuery = "PATH:\""+legislaturePath+"\" AND TYPE:\"crlatti:legislaturaAnagrafica\"";
var legislatureResults = search.luceneSearch(luceneQuery);

var legislatureArrayTemp = new Array();

for(var i=0; i<legislatureResults.length; i++){
	
	legislatureArrayTemp.push(new Number(deromanize(legislatureResults[i].properties["cm:name"])));
	
}

legislatureArrayTemp.sort(function(a,b){return a-b});
legislatureArrayTemp.reverse();

var legislatureArray = new Array();


for(var i=0; i<4 && i<legislatureArrayTemp.length; i++){
	
	legislatureArray.push(romanize(legislatureArrayTemp[i]));
	
}
	


model.legislatureResults = legislatureArray;

function romanize (num) {
	if (!+num)
		return false;
	var	digits = String(+num).split(""),
		key = ["","C","CC","CCC","CD","D","DC","DCC","DCCC","CM",
		       "","X","XX","XXX","XL","L","LX","LXX","LXXX","XC",
		       "","I","II","III","IV","V","VI","VII","VIII","IX"],
		roman = "",
		i = 3;
	while (i--)
		roman = (key[+digits.pop() + (i * 10)] || "") + roman;
	return Array(+digits.join("") + 1).join("M") + roman;
}



function deromanize (str) {
	var	str = str.toUpperCase(),
		validator = /^M*(?:D?C{0,3}|C[MD])(?:L?X{0,3}|X[CL])(?:V?I{0,3}|I[XV])$/,
		token = /[MDLV]|C[MD]?|X[CL]?|I[XV]?/g,
		key = {M:1000,CM:900,D:500,CD:400,C:100,XC:90,L:50,XL:40,X:10,IX:9,V:5,IV:4,I:1},
		num = 0, m;
	if (!(str && validator.test(str)))
		return false;
	while (m = token.exec(str))
		num += key[m[0]];
	return num;
}