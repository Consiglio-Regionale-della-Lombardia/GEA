/*
 *  * #%L
 *  * Alfresco Repository
 *  * %%
 *  * Copyright (C) 2005 - 2016 Alfresco Software Limited
 *  * %%
 *  * This file is part of the Alfresco software. 
 *  * If the software was purchased under a paid Alfresco license, the terms of 
 *  * the paid license agreement will prevail.  Otherwise, the software is 
 *  * provided under the following open source license terms:
 *  * 
 *  * Alfresco is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  * 
 *  * Alfresco is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  * 
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 *  * #L%
 */
package com.sourcesense.crl.script;


import org.alfresco.repo.jscript.BaseScopableProcessorExtension;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.ServiceRegistry;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 * Utility functions che permettono di eseguire come sudo o come sudo e con parametri una funzione creando un job in Alfresco. 
 * @author sourcesense
 *
 */
public class CRLUtilityScript extends BaseScopableProcessorExtension {
	
	private ServiceRegistry serviceRegistry;
/**
 * Esegue una funzione come se fosse l'utente runAsUsername come un job Alfresco.
 * @param func  funzione da eseguire
 * @param runAsUsername utente che deve eseguire la funzione
 */
	public void sudo(final Function func, String runAsUsername) {
        final Context context = Context.getCurrentContext();
        final Scriptable scope = getScope();
        
        RunAsWork<Object> job = new RunAsWork<Object>(){
            @Override
            public Object doWork() throws Exception {
                func.call(context, scope, scope, new Object[]{});
                return null;
            }
        };
        
        AuthenticationUtil.runAs(job, runAsUsername);
    }
	
/**
 * 	Esegue una funzione come se fosse l'utente runAsUsername come un job Alfresco e con dei parametri somministrati come paramtro del metodo.
 * @param func funzione che verrà eseguita.
 * @param args parametri che servono alla funzione da eseguire
 * @param runAsUsername utente che eseguirà la funzione.
 * @return
 */
	public Object sudoWithArgs(final Function func, final Object[] args, String runAsUsername) {
        final Context context = Context.getCurrentContext();
        final Scriptable scope = getScope();
        
        RunAsWork<Object> job = new RunAsWork<Object>(){
            @Override
            public Object doWork() throws Exception {
           
                return func.call(context, scope, scope, args);
            }
        };
        
        return AuthenticationUtil.runAs(job, runAsUsername);
    }
	
	

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
 
 
 

	
}
