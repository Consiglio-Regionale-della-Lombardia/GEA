<html>
<head>
<script type="text/javascript">
	function creaEmendamentiAula(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/esameaula/emendamenti?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	


	function creaEmendamenti(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var passaggio = currentForm.passaggio.value;
		
		
		var numEmendPresentatiMaggiorEsame = currentForm.numEmendPresentatiMaggiorEsame.value;
		var numEmendPresentatiMinorEsame = currentForm.numEmendPresentatiMinorEsame.value;
		var numEmendPresentatiGiuntaEsame = currentForm.numEmendPresentatiGiuntaEsame.value;
		var numEmendPresentatiMistoEsame = currentForm.numEmendPresentatiMistoEsame.value;

		var numEmendApprovatiMaggiorEsame = currentForm.numEmendApprovatiMaggiorEsame.value;
		var numEmendApprovatiMinorEsame = currentForm.numEmendApprovatiMinorEsame.value;
		var numEmendApprovatiGiuntaEsame = currentForm.numEmendApprovatiGiuntaEsame.value;
		var numEmendApprovatiMistoEsame = currentForm.numEmendApprovatiMistoEsame.value;

		var nonAmmissibiliEsame = currentForm.nonAmmissibiliEsame.value;
		var decadutiEsame = currentForm.decadutiEsame.value;
		var ritiratiEsame = currentForm.ritiratiEsame.value;
		var respintiEsame = currentForm.respintiEsame.value;
		var noteEmendamentiEsame = currentForm.noteEmendamentiEsame.value;
		
		
		var attoCustom = {	
			"target":{
				"passaggio": ""+passaggio+""
			},
			"atto": {
				"id": ""+idAtto+"",
				"stato": "emendamenti aula",
				"passaggi": [
					{
						
							"nome" : "Passaggio1",
							"aula":
									{
							 			
										"numEmendPresentatiMaggiorEsameAula": ""+numEmendPresentatiMaggiorEsame+"",
										"numEmendPresentatiMinorEsameAula": ""+numEmendPresentatiMinorEsame+"",
										"numEmendPresentatiGiuntaEsameAula": ""+numEmendPresentatiGiuntaEsame+"",
										"numEmendPresentatiMistoEsameAula": ""+numEmendPresentatiMistoEsame+"",
										
										"numEmendApprovatiMaggiorEsameAula": ""+numEmendApprovatiMaggiorEsame+"",
										"numEmendApprovatiMinorEsameAula": ""+numEmendApprovatiMinorEsame+"",
										"numEmendApprovatiGiuntaEsameAula": ""+numEmendApprovatiGiuntaEsame+"",
										"numEmendApprovatiMistoEsameAula": ""+numEmendApprovatiMistoEsame+"",
										
										"nonAmmissibiliEsameAula": ""+nonAmmissibiliEsame+"",
										"decadutiEsameAula": ""+decadutiEsame+"",
										"ritiratiEsameAula": ""+ritiratiEsame+"",
										"respintiEsameAula": ""+respintiEsame+"",
										"noteEmendamentiEsameAula": ""+noteEmendamentiEsame+""
										
										
									}
							
						
					}
				]
		
			}
		};
		
	
		
		creaEmendamentiAula(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test emendamenti clausole atto</h1>
	</div>
	
	<form action="javascript:creaEmendamenti()">
	<table>
		<tr>
			<td>
				Atto:
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
			<td>Passaggio</td>
			<td>
				<input type="text" name="passaggio"/>
			</td>
		</tr>
			
		<tr>
			<td>Num Emend Presentati Maggior</td>
			<td>
				<input type="text" name="numEmendPresentatiMaggiorEsame"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Presentati Minor</td>
			<td>
				<input type="text" name="numEmendPresentatiMinorEsame"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Presentati Giunta</td>
			<td>
				<input type="text" name="numEmendPresentatiGiuntaEsame"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Presentati Misto</td>
			<td>
				<input type="text" name="numEmendPresentatiMistoEsame"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Approvati Maggior</td>
			<td>
				<input type="text" name="numEmendApprovatiMaggiorEsame"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Approvati Minor</td>
			<td>
				<input type="text" name="numEmendApprovatiMinorEsame"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Approvati Giunta</td>
			<td>
				<input type="text" name="numEmendApprovatiGiuntaEsame"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Approvati Misto</td>
			<td>
				<input type="text" name="numEmendApprovatiMistoEsame"/>
			</td>
		</tr>
		<tr>
			<td>Non ammissibili</td>
			<td>
				<input type="text" name="nonAmmissibiliEsame"/>
			</td>
		</tr>
		<tr>
			<td>decaduti</td>
			<td>
				<input type="text" name="decadutiEsame"/>
			</td>
		</tr>
		<tr>
			<td>ritirati</td>
			<td>
				<input type="text" name="ritiratiEsame"/>
			</td>
		</tr>
		<tr>
			<td>respinti</td>
			<td>
				<input type="text" name="respintiEsame"/>
			</td>
		</tr>
		<tr>
			<td>note emendamenti</td>
			<td>
				<input type="text" name="noteEmendamentiEsame"/>
			</td>
		</tr>
	
			<td></td>
			<td><input type="submit" value="emendamenti"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

