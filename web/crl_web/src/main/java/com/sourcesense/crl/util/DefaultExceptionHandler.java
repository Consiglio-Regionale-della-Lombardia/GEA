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
package com.sourcesense.crl.util;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sourcesense.crl.business.security.SessionPhaseListener;

/**
 * Gestore web per i messaggi di errore. Esegue il redirect verso specifiche
 * pagine
 * 
 * @author sourcesense
 *
 */
public class DefaultExceptionHandler extends ExceptionHandlerWrapper {

	private static final String errorpage = "/exception.xhtml";

	private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	/** key for session scoped message detail */

	public static final String MESSAGE_DETAIL_KEY = "ip.client.jsftoolkit.messageDetail";

	private ExceptionHandler wrapped;

	public DefaultExceptionHandler(ExceptionHandler wrapped) {

		this.wrapped = wrapped;

	}

	@Override
	public Throwable getRootCause(Throwable t) {
		return super.getRootCause(t);
	}

	@Override
	public ExceptionHandler getWrapped() {

		return this.wrapped;

	}

	@Override
	public void handle() throws FacesException {

		FacesContext fc = FacesContext.getCurrentInstance();

		if (fc.isProjectStage(ProjectStage.Development)) {

			getWrapped().handle();

		} else {

			for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {

				ExceptionQueuedEvent event = i.next();
				ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
				String redirectPage = null;
				Throwable t = context.getException();

				if (t instanceof AbortProcessingException) {

					LOG.error("An unexpected exception has occurred by event listener(s)", t);
					redirectPage = errorpage;
					fc.getExternalContext().getSessionMap().put(DefaultExceptionHandler.MESSAGE_DETAIL_KEY,
							t.getLocalizedMessage());

				} else if (t instanceof ViewExpiredException) {

					if (LOG.isDebugEnabled()) {

						LOG.debug("View '" + ((ViewExpiredException) t).getViewId() + "' is expired", t);

					}

					HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);

					if (session != null) {
						session.invalidate();
					}

				} else if (t.getCause().getCause().getCause() instanceof ServiceNotAvailableException) {

					String errorMsg = ((ServiceNotAvailableException) t.getCause().getCause().getCause())
							.getServiceName();

					LOG.error(errorMsg, t.getCause().getCause().getCause());
					redirectPage = errorpage + "?error=" + errorMsg;

				} else if (t.getCause().getCause().getCause() instanceof ServiceAuthenticationException) {

					FacesContext jsfcontext = FacesContext.getCurrentInstance();
					jsfcontext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Attenzione ! Utente e password errati ", ""));

				} else {

					String messageKey = handleUnexpected(fc, t);

					redirectPage = errorpage + messageKey;

					fc.getExternalContext().getSessionMap().put(DefaultExceptionHandler.MESSAGE_DETAIL_KEY,
							t.getLocalizedMessage());

				}

				SessionPhaseListener spl = new SessionPhaseListener();
				spl.doRedirect(fc, redirectPage);
				break;

			}

		}
	}

	/**
	 * Nel caso di errore inaspettato viene aggiunto un log di errore
	 * 
	 * @param facesContext contesto web JSF
	 * @param t            eccezione da gestire
	 * @return nome della classe che gestirà l'errore
	 */
	protected String handleUnexpected(FacesContext facesContext, final Throwable t) {

		LOG.error("An unexpected internal error has occurred", t);

		return "jsftoolkit.exception.UncheckedException";

	}

}
