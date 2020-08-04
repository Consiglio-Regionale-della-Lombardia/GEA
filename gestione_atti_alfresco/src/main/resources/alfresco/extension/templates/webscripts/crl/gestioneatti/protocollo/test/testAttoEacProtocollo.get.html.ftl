<html>
<head>
<script type="text/javascript">
	
	function creaAtto(atto) {
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
		xmlhttp.open("POST", "/alfresco/service/crl/protocollo/synch/atto?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaAttoCustom(){
		var currentForm = document.forms[0];
		var numeroAtto = currentForm.numeroAtto.value;
		var tipoAtto = currentForm.tipoAtto.options[currentForm.tipoAtto.selectedIndex].value;
		var idProtocollo = currentForm.idProtocollo.value;
		var dataAtto = currentForm.dataAtto.value;
		var note = currentForm.note.value;
		
		var attoCustom = {
		"atto" : {
			"numeroAtto" : ""+numeroAtto+"",
			"tipoAtto" : "EAC",
			"idProtocollo" : ""+idProtocollo+"",
			"dataAtto" : ""+dataAtto+"",
			"note" : ""+note+""
			}
		};
		
		creaAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Protocollo - Test creazione atto EAC</h1>
	</div>
	
	<form action="javascript:creaAttoCustom()">
	<table>
		<tr>
			<td>ID protocollo:</td>
			<td>
				<input type="text" name="idProtocollo">
			</td>
		</tr>
		<tr>
			<td>Numero atto:</td>
			<td>
				<input type="text" name="numeroAtto">
			</td>
		</tr>
		<tr>
			<td>Tipo:</td>
			<td>
				<select name="tipoAtto">
				
					<option value="EAC">EAC</option>
				
				</select>
			</td>
		</tr>
		<tr>
			<td>Data atto:</td>
			<td><input type="text" name="dataAtto" value="2012-10-13"></td>
		</tr>
		<tr>
			<td>Note:</td>
			<td><input type="text" name="note" value="Nota di prova atto"></td>
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

