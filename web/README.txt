Front end Regione Lombardia

MODULE crl_web

This module lets a user to follow the legal document lifecycle. Once built, you get a webapp that connects to Alfresco and depending on the user profile (gotten from LDAP),lets the user to make read/write operations related to a official document. The webapp connects to Alfresco and shows a custom UI with several sections like: home, insert new legal document, parlament sessions management and reports. The document lifecycle must be understood like a workflow with several steps. Depending on user permissions, a complete lifecycle can be finished and published.

The webapp is developed using spring. Spnego is used for user information exchange. For UI, JSF. A MVC software pattern is implemented.

MODULE crl_business

This module implements all business logic that communicates with Alfresco and creates all document operations during a document's lifecycle..... The final jar built is used in crl_web. 

MODULE gestione_atti_share

This module belongs to ALFRESCO SDK. With this module you can build up an Alfresco Share instance that can be deployed in a jetty server (as a maven lifecycle). This is not an Alfresco-all-in-one artifact, so no alfresco module is needed. In this module you can see all alfresco share personalizations for Regione Lombardia's document lifecycle. Most of personalizations can be found on share-config-custom.xml. Several types are declared in order to create this kind of documents directly from Alfresco Share. Also custom aspects from CRL content model are shown to end users. The metadata for CRL-documents on Share forms are also custom. Advanced search is also custom for Regione Lombardia.  
   
