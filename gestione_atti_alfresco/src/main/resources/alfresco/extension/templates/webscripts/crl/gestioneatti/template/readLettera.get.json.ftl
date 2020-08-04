<#escape x as jsonUtils.encodeJSONString(x)>
{  "lettera" : 
	{
		"tipoTemplate": "${lettera.typeShort}",
    	"firmatario": "<#if lettera.properties["crltemplate:firmatario"]?exists>${lettera.properties["crltemplate:firmatario"]}<#else></#if>",
    	"ufficio": "<#if lettera.properties["crltemplate:ufficio"]?exists>${lettera.properties["crltemplate:ufficio"]}<#else></#if>",
        "assessore": "<#if lettera.properties["crltemplate:assessore"]?exists>${lettera.properties["crltemplate:assessore"]}<#else></#if>",
    	"direzione": "<#if lettera.properties["crltemplate:direzione"]?exists>${lettera.properties["crltemplate:direzione"]}<#else></#if>", 	
    	"numeroTelFirmatario": "<#if lettera.properties["crltemplate:numeroTelFirmatario"]?exists>${lettera.properties["crltemplate:numeroTelFirmatario"]}<#else></#if>", 
    	"emailFirmatario": "<#if lettera.properties["crltemplate:emailFirmatario"]?exists>${lettera.properties["crltemplate:emailFirmatario"]}<#else></#if>"                    
	}
}
</#escape>


 