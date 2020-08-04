<html>
<head>
<script type="text/javascript">
	
	function creaDati(seduta) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/sedute/attitrattati?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(seduta);
	}
	
	function creaDatiSeduta(){
		var currentForm = document.forms[0];
		var idSeduta = currentForm.idSeduta.value;
	
		var attoTrattato1 = currentForm.attoTrattato1.value;
		var attoTrattato2 = currentForm.attoTrattato2.value;
		
		
		var sedutaCustom = {
			
		"seduta" : {
			"idSeduta" : ""+idSeduta+"",
			"attiTrattati" : [
						{
							"atto":{
								"id":""+attoTrattato1+""
							}
						},
						{
							"atto":{
								"id":""+attoTrattato2+""
							}
						}
					]
			}
		};
		
		creaDati(JSON.stringify(sedutaCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test creazione ordine del giorno</h1>
	</div>
	
	<form action="javascript:creaDatiSeduta()">
	<table>
		<tr>
			<td>Id seduta:</td>
			
			<td>
				<select name="idSeduta">
				<#list sedute as seduta>
					<option value="${seduta.nodeRef}">${seduta.name}</option>
				</#list>
				</select>
			</td>
		
		</tr>
		
		<tr>
			<td>Atto trattato 1</td>
			<td>
				<select name="attoTrattato1">
				<#list atti as atto>
					<option value="${atto.nodeRef}">${atto.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		
		<tr>
			<td>Atto trattato 2</td>
			<td>
				<select name="attoTrattato2">
				<#list atti as atto>
					<option value="${atto.nodeRef}">${atto.name}</option>
				</#list>
				</select>
			</td>
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

