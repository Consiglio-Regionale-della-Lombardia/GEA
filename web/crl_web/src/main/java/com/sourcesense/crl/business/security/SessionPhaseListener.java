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
package com.sourcesense.crl.business.security;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esegue il redirect verso la pagina di login all'interno di una sessione
 * chiusa
 * 
 * @author sourcesense
 *
 */
public class SessionPhaseListener implements PhaseListener {

	private static final String homepage = "authenticate.xhtml?reason=expired";

	private static final Logger LOG = LoggerFactory.getLogger(SessionPhaseListener.class);

	/**
	 * Metodo da implementare. Attualmente non esegue operazioni
	 */
	public void afterPhase(PhaseEvent event) {
		;
	}

	/**
	 * Esegue il redirect verso la pagina di login nel caso non si è autenticati
	 */
	public void beforePhase(PhaseEvent event) {

		FacesContext fc = event.getFacesContext();

		String loginPage = (String) fc.getExternalContext().getRequestMap().

				get("web.secfilter.authenticator.showLogin");

		if (loginPage != null && !loginPage.equals("")) {

			doRedirect(fc, loginPage);

		}

	}

	/**
	 * Ritorna il phase id jsf. Attualmente di tipo RESTORE_VIEW
	 */
	public PhaseId getPhaseId() {

		return PhaseId.RESTORE_VIEW;

	}

	/**
	 * 
	 * Esegue il redirect ajax.
	 */
	public void doRedirect(FacesContext fc, String redirectPage)

			throws FacesException {

		ExternalContext ec = fc.getExternalContext();

		try {

			if (ec.isResponseCommitted()) {

				return;

			}

			if ((RequestContext.getCurrentInstance().isAjaxRequest()

					|| fc.getPartialViewContext().isPartialRequest())

					&& fc.getResponseWriter() == null

					&& fc.getRenderKit() == null) {

				ServletResponse response = (ServletResponse) ec.getResponse();

				ServletRequest request = (ServletRequest) ec.getRequest();

				response.setCharacterEncoding(request.getCharacterEncoding());

				RenderKitFactory factory = (RenderKitFactory) FactoryFinder
						.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

				RenderKit renderKit = factory.getRenderKit(fc,
						fc.getApplication().getViewHandler().calculateRenderKitId(fc));

				ResponseWriter responseWriter = renderKit.createResponseWriter(

						response.getWriter(), null, request.getCharacterEncoding());

				fc.setResponseWriter(responseWriter);

			}

			ec.redirect(ec.getRequestContextPath() +

					(redirectPage != null ? redirectPage : ""));

		} catch (IOException e) {

			LOG.error("Redirect to the specified page '" +

					redirectPage + "' failed");

			throw new FacesException(e);

		}

	}

}
