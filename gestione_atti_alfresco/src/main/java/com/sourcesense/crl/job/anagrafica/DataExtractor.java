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
package com.sourcesense.crl.job.anagrafica;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Classe che permette di estrarre da Database tutti i dati che riguardano una legistatura.
 * @author sourcesense
 *
 */
public class DataExtractor {

    private static Log logger = LogFactory.getLog(DataExtractor.class);
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Trova l'elenco di tutte le legislature sul DB. Crea una lista di BEAN Legislatura
     * @return elento di BEAN Legislatura
     */
    public List<Legislature> getLegislatures() {
        Connection conn = null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(Constant.QUERY_LEGISLATURE);
            rs = ps.executeQuery();
            Legislature legislature = null;
            List<Legislature> legislatures = new ArrayList<Legislature>();
            while (rs.next()) {
                legislature = new Legislature();
                legislature.setId(rs.getInt(Constant.COLUMN_LEGISLATURE_ID));
                legislature.setNumber(rs.getString(Constant.COLUMN_LEGISLATURE_NUMBER));
                legislature.setFrom(rs.getDate(Constant.COLUMN_LEGISLATURE_FROM));
                legislature.setTo(rs.getDate(Constant.COLUMN_LEGISLATURE_TO));
                legislatures.add(legislature);
            }
            return legislatures;
        } catch (SQLException e) {
            logger.error("Cannot get current legislature id", e);
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { 
                }
            }
            if ( rs!=null){
            	try {
	            	rs.close();
	            } catch (SQLException e) { 
	            }
            }
           if (ps!=null){
        	   try {
	        	   ps.close();
	           } catch (SQLException e) { 
	           }
           }
        }
    }

    /**
     * Ottiene i dati da DB della legislatura corrente.
     * @return Bean Legislature
     */
    public Legislature getCurrentLegislature() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(Constant.QUERY_CURRENT_LEGISLATURE);
            rs = ps.executeQuery();
            Legislature legislature = null;
            if (rs.next()) {
                legislature = new Legislature();
                legislature.setId(rs.getInt(Constant.COLUMN_LEGISLATURE_ID));
                legislature.setNumber(rs.getString(Constant.COLUMN_LEGISLATURE_NUMBER));
                legislature.setFrom(rs.getDate(Constant.COLUMN_LEGISLATURE_FROM));
                legislature.setTo(rs.getDate(Constant.COLUMN_LEGISLATURE_TO));
            }
            return legislature;
        } catch (SQLException e) {
            logger.error("Cannot get current legislature id", e);
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { 
                }
            }
            if ( rs!=null){
            	try {
	            	rs.close();
	            } catch (SQLException e) { 
	            }
            }
           if (ps!=null){
        	   try {
	        	   ps.close();
	           } catch (SQLException e) { 
	           }
           }
        }
    }

    /**
     * Ottiene la lista di tutti i consiglieri che appartengono ad una legislatura, 
     * la quale viene identificata con l'id che arriva come parametro. 
     * @param idCurrentLegislature ID nel DB della legislatura della quale si volgiono sapere i consiglieri.
     * @return lista con i Bean che rappresentano tutti i consiglieri della legislatura corrente
     */
    public List<Councilor> getCouncilors(int idCurrentLegislature) {
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs= null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(Constant.QUERY_ALL_COUNCILORS_FILTERED_BY_LEGISLATURE);
            ps.setInt(1, idCurrentLegislature);
            ps.setInt(2, idCurrentLegislature);
            List<Councilor> councilors = new ArrayList<Councilor>();
            rs = ps.executeQuery();
            while (rs.next()) {
                Councilor councilor = new Councilor();
                councilor.setId(rs.getInt(Constant.COLUMN_ID_PERSONA));
                councilor.setFirstName(rs.getString(Constant.COLUMN_NOME));
                councilor.setLastName(rs.getString(Constant.COLUMN_COGNOME));
                councilor.setGroupName(rs.getString(Constant.COLUMN_NOME_GRUPPO));
                councilor.setCodeGroupName(rs.getString(Constant.COLUMN_CODICE_GRUPPO));
                councilor.setLegislatureNumber(rs.getString(Constant.COLUMN_NUMERO_LEGISLATURA));
                String committes = rs.getString(Constant.COLUMN_ORGANI);
                if (committes != null && !"".equals(committes) && !"#".equals(committes)) {
                    StringTokenizer st = new StringTokenizer(committes, "#");
                    while (st.hasMoreTokens()) {
                        String comm = st.nextToken();
                        Committee c = new Committee();

                        String[] strArray = comm.split("!");
                        if (strArray.length > 0) {
                            String name = strArray[0];
                            if (name != null) {
                                name = name.replace('"', '\'');
                            }
                            c.setName(name.trim());
                            if (strArray.length > 1) {
                                c.setOrder(Integer.valueOf(comm.split("!")[1]));
                            } else {
                                c.setOrder(1000);
                            }
                        }

                        councilor.addCommittee(c);
                    }
                }
                councilors.add(councilor);
            }
            return councilors;
        } catch (SQLException e) {
            logger.error("Cannot get all councilors of current legislature", e);
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { 
                }
            }
            if ( rs!=null){
            	try {
	            	rs.close();
	            } catch (SQLException e) { 
	            }
            }
           if (ps!=null){
        	   try {
	        	   ps.close();
	           } catch (SQLException e) { 
	           }
           }
        }
    }
/**
 * Ottiene l'elenco di tutti i gruppi sul DB che sono attivi. 
 * @return lista con tutti i Bean Group di tutti i gruppi attivi.
 */
    public List<Group> getGroups() {
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(Constant.QUERY_CURRENT_GRUPPI_CONSILIARI);
            rs = ps.executeQuery();
            Group group = null;
            List<Group> groups = new ArrayList<Group>();
            while (rs.next()) {
                group = new Group();
                group.setId(rs.getInt(Constant.COLUMN_GRUPPI_CONSILIARI_ID));
                group.setName(rs.getString(Constant.COLUMN_GRUPPI_CONSILIARI_NOME));
                group.setCode(rs.getString(Constant.COLUMN_GRUPPI_CONSILIARI_CODICE));
                groups.add(group);
            }
            rs.close();
            ps.close();
            return groups;
        } catch (SQLException e) {
            logger.error("Cannot get current legislature id", e);
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { 
                }
            }
            if ( rs!=null){
            	try {
	            	rs.close();
	            } catch (SQLException e) { 
	            }
            }
            if (ps!=null){
            	try {
            		ps.close();
            	} catch (SQLException e) { 
            	}
            }	
        }
    }
}
