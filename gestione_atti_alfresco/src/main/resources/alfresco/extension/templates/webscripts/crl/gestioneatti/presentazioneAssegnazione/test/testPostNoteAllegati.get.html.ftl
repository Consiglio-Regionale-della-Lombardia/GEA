<html>
<head>
<script type="text/javascript">
	
	function creaDatiAtto(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/noteallegati?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function aggiornaDatiCustom(){
		var currentForm = document.forms[0];
		var id = currentForm.id.options[currentForm.id.selectedIndex].value;
		var note = currentForm.note.value;

		var attoCustom = {
		"atto" : {
			"id" : ""+id+"",
			"notePresentazioneAssegnazione" : ""+note+"",
			"linksPresentazioneAssegnazione" : [
				{
					"link":{
						"descrizione":"Yahoo",
						"indirizzo":"http://www.yahoo.it",
						"pubblico":true
					}
				},
				{
					"link":{
						"descrizione":"Google",
						"indirizzo":"http://www.google.it",
						"pubblico":true
					}
				}
			]
			}
		};
		
		creaDatiAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test modifica note e links atto</h1>
	</div>
	
	<form action="javascript:aggiornaDatiCustom()">
	<table>
		<tr>
			<td>Id:</td>
			<td>
				<select name="id">
				<#list atti as atto>
					<option value="${atto.nodeRef}">${atto.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Note:</td>
			<td><input type="text" name="note" value=""></td>
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

