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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/consultazione/soggettiinvitati?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaDatiCustom(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.value;
		var consultazione = currentForm.consultazione.value;
		var sogg1 = currentForm.sogg1.value;
		var intSogg1 = currentForm.intSogg1.value;
		var sogg2 = currentForm.sogg2.value;
		var intSogg2 = currentForm.intSogg2.value;
		
		
		var attoCustom = {
			"target": {
				"consultazione" : ""+consultazione+""
			},
			
			"atto": {
				"id": ""+idAtto+"",
				"consultazioni": [
					{
						"consultazione": {
							"descrizione" : ""+consultazione+"",
							"soggettiInvitati": [
								{
									"soggettoInvitato":
										{
											"descrizione": ""+sogg1+"",
											"intervenuto": ""+intSogg1+""
										}
								},
								{
									"soggettoInvitato":
										{
											"descrizione": ""+sogg2+"",
											"intervenuto": ""+intSogg2+""
										}
								}
									
							]
						}
					},
					{
					
						"consultazione": {
							"descrizione" : "test"
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
			<td>Consultazione</td>
			<td>
				<input type="text" name="consultazione">
			</td>
		</tr>
		<tr>
			<td>Soggetto Invitato 1</td>
			<td>
				<input type="text" name="sogg1">
			</td>
		</tr>
		<tr>
			<td>Soggetto Invitato 1 Intervenuto:</td>
			<td>
				<select name="intSogg1">
					<option value="true">SI</option>
					<option value="false">NO</option>
				</select>
			</td>
		</tr>
		
		<tr>
			<td>Soggetto Invitato 2</td>
			<td>
				<input type="text" name="sogg2">
			</td>
		</tr>
		<tr>
			<td>Soggetto Invitato 2 Intervenuto:</td>
			<td>
				<select name="intSogg2">
					<option value="true">SI</option>
					<option value="false">NO</option>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Salva"></td>
		</tr>
	
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

