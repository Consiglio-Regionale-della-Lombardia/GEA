{
    List: [
    <#list atto as atti>
       {
        "Atto" :
           {
            "Legislatura": "${atti.properties["crlatti:legislatura"]}",
            "Numero atto": "${atti.properties["crlatti:numeroAtto"]}",
            "Stato atto": "${atti.properties["crlatti:statoAtto"]}",
            "NodeRef": "${atti.nodeRef}"
           }
       }
   ]
   <#if atti_has_next>,</#if>
   </#list>
}