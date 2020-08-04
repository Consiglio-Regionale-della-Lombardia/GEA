<html>
<head>
<script type="text/javascript">
	function creaDati(atto) {
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				document.getElementById("myDiv").innerHTML = "status: "+xmlhttp.status;
			}
		}
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/consultazioni?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaDatiCustom(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.value;
		var cons1 = currentForm.cons1.value;
		var dataCons1 = currentForm.dataCons1.value;
		var cons2 = currentForm.cons2.value;
		var dataCons2 = currentForm.dataCons2.value;
		
		
		var attoCustom = {
			
			"atto": {
				"id": ""+idAtto+"",
				"consultazioni": [
					{
						"consultazione": {
							"descrizione" : ""+cons1+"",
							"dataConsultazione" : ""+dataCons1+""
						}
					},
					{
						"consultazione": {
							"descrizione" : ""+cons2+"",
							"dataConsultazione" : ""+dataCons2+""
						}
					}
				]
				 
			}
		};
		
		
	
		
		creaDati(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test creazione modifica consultazioni</h1>
	</div>
	
	<form action="javascript:creaDatiCustom()">
	<table>
		<tr>
			<td>Atto:</td>
			<td>
				<select name="idAtto">
					<#list atti as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
		</tr>
	
		
		<tr>
			<td>Consultazione 1</td>
			<td>
				<input type="text" name="cons1">
			</td>
		</tr>
		<tr>
			<td>Data Consultazione 1</td>
			<td>
				<input type="text" name="dataCons1">
			</td>
		</tr>
		<tr>
			<td>Consultazione 2</td>
			<td>
				<input type="text" name="cons2">
			</td>
		</tr>
		<tr>
			<td>Data Consultazione 2</td>
			<td>
				<input type="text" name="dataCons2">
			</td>
		</tr>
			<tr>
			<td></td>
			<td><input type="submit" value="salva"></td>
		</tr>
	
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

