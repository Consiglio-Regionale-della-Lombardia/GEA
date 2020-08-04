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
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sourcesense.crl.web.ui.beans.UserBean;

/**
 * Filtro web per il login
 * 
 * @author sourcesense
 *
 */
public class LoginFilter implements Filter {

	private FilterConfig filterConfig;
	private static final String loginpage = "/authenticate.xhtml";
	private static final String homepage = "/home";

	/**
	 * Inizializza il filtro aggiungendo la configurazione di default
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	/**
	 * Ripulisce eliminando il filtro di configurazione
	 */
	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	/**
	 * Esegue il redirect verso la pagina di login
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		httpRequest.setCharacterEncoding("UTF-8");
		Principal principal = httpRequest.getUserPrincipal();

		String username = principal != null ? principal.getName() : "";

		String path = httpRequest.getServletPath();

		UserBean userBean = (UserBean) session.getAttribute("userBean");

		if (userBean != null) {
			if ((path.equals("/") || path.equals(loginpage))) {
				httpResponse.sendRedirect(httpRequest.getContextPath() + homepage);
			} else {
				filterChain.doFilter(request, response);
			}
		} else if (!path.equals("/") && (!path.equals(loginpage)) && (filterConfig == null)
				&& (!httpRequest.getServletPath().contains("/javax.faces.resource"))
				&& (!httpRequest.getServletPath().contains("/resources/"))) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + loginpage);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

}