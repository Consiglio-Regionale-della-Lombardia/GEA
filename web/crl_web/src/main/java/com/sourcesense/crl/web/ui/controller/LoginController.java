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
package com.sourcesense.crl.web.ui.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.sourcesense.crl.business.model.User;
import com.sourcesense.crl.business.service.UserServiceManager;
import com.sourcesense.crl.util.ServiceAuthenticationException;
import com.sourcesense.crl.web.ui.beans.UserBean;

/**
 * Operazioni di login e logout dalle pagine web
 * 
 * @author sourcesense
 *
 */
@ManagedBean(name = "loginController")
@RequestScoped
public class LoginController {

	@ManagedProperty(value = "#{userServiceManager}")
	private UserServiceManager userServiceManager;

	private User user = new User();

	private String domain;

	private String username;

	private String password;

	/**
	 * Login tramite utenza e password. Ritorna una sessione in sola lettura
	 * 
	 * @return "pretty:Home" se l'autenticazione è ok altrimenti null
	 */
	public String readOnlyLogin() {

		try {

			User sessionUser = userServiceManager.authenticateReadOnly(user);

			FacesContext context = FacesContext.getCurrentInstance();
			UserBean userBean = (UserBean) context.getApplication().getExpressionFactory()
					.createValueExpression(context.getELContext(), "#{userBean}", UserBean.class)
					.getValue(context.getELContext());
			userBean.setUser(sessionUser);
			return "pretty:Home";

		} catch (ServiceAuthenticationException ex) {

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Utente e password errati ", ""));

			return null;
		}

	}

	/**
	 * Login tramite utenza e password
	 * 
	 * @return "pretty:Home" se l'autenticazione è ok altrimenti null
	 */
	public String login() {

		try {

			user.setUsername(user.getUsername() + domain);

			User sessionUser = userServiceManager.authenticate(user);

			if (sessionUser != null) {

				if (sessionUser.getSessionGroup() == null) {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Attenzione ! Utentenza non valida per la corrente legislatura ", ""));

					return null;

				}

				FacesContext context = FacesContext.getCurrentInstance();
				UserBean userBean = (UserBean) context.getApplication().getExpressionFactory()
						.createValueExpression(context.getELContext(), "#{userBean}", UserBean.class)
						.getValue(context.getELContext());
				userBean.setUser(sessionUser);
				return "pretty:Home";

			} else {

				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Utente e password errati ", ""));

				return null;

			}

		} catch (ServiceAuthenticationException ex) {

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attenzione ! Utente e password errati ", ""));

			return null;
		}

	}

	/**
	 * Logout della sessione web
	 * 
	 * @return il valore "pretty:login"
	 */
	public String logout() {
		HttpSession session = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false));
		session.removeAttribute("userBean");
		session.removeAttribute("attoBean");
		session.invalidate();
		return "pretty:login";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return user.getUsername();
	}

	public void setUsername(String username) {
		this.user.setUsername(username);
	}

	public String getPassword() {
		return user.getPassword();
	}

	public void setPassword(String password) {
		this.user.setPassword(password);
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public UserServiceManager getUserServiceManager() {
		return userServiceManager;
	}

	public void setUserServiceManager(UserServiceManager userServiceManager) {
		this.userServiceManager = userServiceManager;
	}

}
