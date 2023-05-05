{
    <#if status.message?has_content>
        "message": "${status.message}"
    <#else>
        "message": "Errore. Verificare che il json body in input sia ben formattato in json e che il Content-Type della chiamata sia in application/json"
    </#if>
}