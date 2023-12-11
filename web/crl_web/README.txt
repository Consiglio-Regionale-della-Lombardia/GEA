For buildind this module:

First, you need to install the jar produced in the project: crl_business. 
Then, using a maven lifecycle, you can:

mvn clean install -Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2 -> generates the war file that must be deployed to connect to Alfresco.

mvn clean --> cancel all target files.
