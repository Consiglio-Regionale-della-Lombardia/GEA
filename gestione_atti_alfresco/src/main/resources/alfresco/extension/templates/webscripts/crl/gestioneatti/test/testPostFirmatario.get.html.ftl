<html>
<head>
<script type="text/javascript">
	function creaFirmatario(firmatario) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/firmatario?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(firmatario);
	}
	
	function creaFirmatarioCustom(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var firmatario = currentForm.firmatario.options[currentForm.firmatario.selectedIndex].value;
		var gruppoConsiliare = currentForm.gruppoConsiliare.value;
		var dataFirma = currentForm.dataFirma.value;
		var dataRitiro = currentForm.dataRitiro.value;
		var primoFirmatario = currentForm.primoFirmatario.value;
		
		var firmatarioCustom = {
		"firmatario" : {
			"idAtto" : ""+idAtto+"",
			"descrizione" : ""+firmatario+"",
			"gruppoConsiliare" : ""+gruppoConsiliare+"",
			"dataFirma" : ""+dataFirma+"",
			"dataRitiro" : ""+dataRitiro+"",
			"primoFirmatario" : ""+primoFirmatario+""
			}
		};
		
		creaFirmatario(JSON.stringify(firmatarioCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test creazione modifica firmatario</h1>
	</div>
	
	<form action="javascript:creaFirmatarioCustom()">
	<table>
		<tr>
			<td>Tipo:</td>
			<td>
				<select name="idAtto">
				<#list atti as atto>
					<option value="${atto.nodeRef}">${atto.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Firmatario:</td>
			<td>
				<select name="firmatario">
				<#list firmatari as firmatario>
					<option value="${firmatario.name}">${firmatario.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Gruppo consiliare:</td>
			<td>
				<input type="text" name="gruppoConsiliare">
			</td>
		</tr>
		<tr>
			<td>Primo firmatario:</td>
			<td>
				<input type="text" name="primoFirmatario">
			</td>
		</tr>
		<tr>
			<td>Data firma:</td>
			<td>
				<input type="text" name="dataFirma">
			</td>
		</tr>
		<tr>
			<td>Data ritiro:</td>
			<td>
				<input type="text" name="dataRitiro">
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

