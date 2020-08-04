<html>
<head>
<script type="text/javascript">
	
	function creaSeduta(atto) {
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				document.getElementById("myDiv").innerHTML = xmlhttp.responseText;
			}
		}
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/seduta?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaSedutaCustom(){
		var currentForm = document.forms[0];
		var dataSeduta = currentForm.dataSeduta.value;
		var numVerbale = currentForm.numVerbale.value;
		var note = currentForm.note.value;
		var provenienza = currentForm.provenienza.value;
		
		
		var sedutaCustom = {
		"target" : {
			"provenienza": ""+provenienza+""
		},
		
		"seduta" : {
			"dataSeduta" : ""+dataSeduta+"",
			"numVerbale" : ""+numVerbale+"",
			"note" : ""+note+"",
			"links" : [
						{
							"link":{
								"descrizione":"Yahoo",
								"indirizzo":"http://www.yahoo.it"
							}
						},
						{
							"link":{
								"descrizione":"Google",
								"indirizzo":"http://www.google.it"
							}
						}
					]	
			}
		};
		
		creaSeduta(JSON.stringify(sedutaCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test creazione seduta</h1>
	</div>
	
	<form action="javascript:creaSedutaCustom()">
	<table>
		<tr>
			<td>Provenienza (Aula o Commissione XXX):</td>
			<td><input type="text" name="provenienza"></td>
		</tr>
		
		<tr>
			<td>Data seduta:</td>
			<td><input type="text" name="dataSeduta"></td>
		</tr>
		
		<tr>
			<td>Numero Verbale:</td>
			<td><input type="text" name="numVerbale"></td>
		</tr>
		
		<tr>
			<td>Note:</td>
			<td><input type="text" name="note"></td>
		</tr>
		
		<tr>
		<td></td>
		<td><input type="submit"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

