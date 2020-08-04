/* 
 * Copyright (C) 2019 Consiglio Regionale della Lombardia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sourcesense.crl.business.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Aggiunge delle tracce all'interno dei servizi rest
 * 
 * @author sourcesense
 *
 */
@Service
@Aspect
public class BusinessLogger {

	private Logger log = LoggerFactory.getLogger(BusinessLogger.class);

	/**
	 * Aggiunge le tracce di debug ai servizi rest
	 * 
	 * @param joinPoint il punto su cui applicare le tracce
	 */
	@After("within(com.sourcesense.crl.business.service.rest.*)")
	public void restServices(JoinPoint joinPoint) {
		String trace = "Executed Rest Service : " + joinPoint.getSignature().getName() + "-"
				+ joinPoint.getSignature().getDeclaringTypeName();

		Object[] inputList = joinPoint.getArgs();

		for (Object object : inputList) {

			if (object.getClass().isAnnotationPresent(JsonRootName.class)) {

				trace += " beanIn [" + object.toString() + "] ";

			} else {

				trace += " otherIn [ " + object.getClass().getCanonicalName() + " : " + object.toString() + "]";
			}
		}

		log.info(trace);

	}

	/**
	 * Non implementato
	 */
	@Pointcut("within(@org.springframework.stereotype.Service *)")
	public void beanAnnotatedWithService() {
	}

	/**
	 * Non implementato
	 */
	@Pointcut("execution(public * *(..))")
	public void publicMethod() {

	}

	/**
	 * Non implementato
	 */
	@Pointcut("publicMethod() && beanAnnotatedWithService()")
	public void publicMethodInsideAClassMarkedWithService() {
	}

	/**
	 * Metodo di test
	 * 
	 * @param joinPoint il punto su cui elaborare il test
	 */
	@AfterReturning(pointcut = "publicMethodInsideAClassMarkedWithService()")
	public void prova(JoinPoint joinPoint) {

		String trace = "Executed Manager : " + joinPoint.getSignature().getName() + "-"
				+ joinPoint.getSignature().getDeclaringTypeName();

		Object[] inputList = joinPoint.getArgs();

		for (Object object : inputList) {

			if (object.getClass().isAnnotationPresent(JsonRootName.class)) {
				trace += " beanIn [" + object.toString() + "] ";

			} else {

				trace += " otherIn [ " + object.getClass().getCanonicalName() + " : " + object.toString() + "]";
			}
		}

		log.info(trace);

	}

}
