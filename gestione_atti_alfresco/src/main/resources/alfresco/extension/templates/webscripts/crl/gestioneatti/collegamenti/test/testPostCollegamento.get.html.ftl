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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/collegamento?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function aggiornaDatiCustom(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var idAttoCollegato1 = currentForm.idAttoCollegato1.options[currentForm.idAttoCollegato1.selectedIndex].value;
		var idAttoCollegato2 = currentForm.idAttoCollegato2.options[currentForm.idAttoCollegato2.selectedIndex].value;
		var note1 = currentForm.note1.value;
		var note2 = currentForm.note2.value;

		var attoCustom = {
		
			"atto": {
				"id": ""+idAtto+"",
				"collegamenti": [
						{
							"collegamento": {
								"idAttoCollegato": ""+idAttoCollegato1+"",
								"note": ""+note1+""
							}
						},
						{
							"collegamento": {
								"idAttoCollegato": ""+idAttoCollegato2+"",
								"note": ""+note2+""
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
		<h1>Test post abbinamento atto</h1>
	</div>
	
	<form action="javascript:aggiornaDatiCustom()">
	<table>
		<tr>
			<td>
				Atto padre:
			</td>
			<td>
				<select name="idAtto">
					<#list atti as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				Atto da collegare 1:
			</td>
			<td>
				<select name="idAttoCollegato1">
					<#list atti as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Note1:</td>
			<td>
				<input name="note1" value="note coll 1"/>
			</td>
		</tr>
		<tr>
			<td>
				Atto da collegare 2:
			</td>
			<td>
				<select name="idAttoCollegato2">
					<#list atti as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
		</tr>

		<tr>
			<td>Note2:</td>
			<td>
				<input name="note2" value="note coll 2"/>
			</td>
		</tr>
		
		<tr>
			<td></td>
			<td><input type="submit" value="Post collegamenti Atto"></td>
		</tr>
		
	</table>
	</form>
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>