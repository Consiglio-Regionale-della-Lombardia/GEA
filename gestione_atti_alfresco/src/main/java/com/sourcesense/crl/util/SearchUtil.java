package com.sourcesense.crl.util;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.cmr.search.SearchParameters.SortDefinition;

public class SearchUtil {
    
	private SearchService searchService;

    /**
     * Wrapper method, see all the definitions of methods named 'buildSearchParameters' for deeper customizations
     * 
     * @param query
     */
    public SearchParameters buildSearchParameters(String query) {
        return this.buildSearchParameters(query, SearchService.LANGUAGE_FTS_ALFRESCO);
    }

    public SearchParameters buildSearchParameters(String query, String language) {
        return this.buildSearchParameters(query, language, StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
    }

    public SearchParameters buildSearchParameters(String query, String language, StoreRef storeRef) {
        return this.buildSearchParameters(query, language, storeRef, -1, 0);
    }

    public SearchParameters buildSearchParameters(String query, String language, StoreRef storeRef, int maxItems, int skipCount) {
        return this.buildSearchParameters(query, language, storeRef, maxItems, skipCount, null);
    }

    public SearchParameters buildSearchParameters(String query, String language, StoreRef storeRef, int maxItems, int skipCount, SortDefinition sortDefinition) {

        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setLanguage(language);
        searchParameters.addStore(storeRef);
        searchParameters.setQuery(query);
        searchParameters.setMaxItems(maxItems);
        searchParameters.setSkipCount(skipCount);
        
        if (sortDefinition != null) {
            searchParameters.addSort(sortDefinition);
        }

        return searchParameters;
    }

    /**
     * Get list nodeRefs by Alfresco FTS query
     * 
     * @param query
     */
    public List<NodeRef> getNodeRefsByQuery(String query) {

        List<NodeRef> nodeRefs = new ArrayList<NodeRef>();
        
        ResultSet resultSetQuery = this.searchService.query(this.buildSearchParameters(query));

        if (!resultSetQuery.getNodeRefs().isEmpty()) {
            nodeRefs = resultSetQuery.getNodeRefs();
        }

        return nodeRefs;
        
    }

    /**
     * Get list nodeRefs by SearchParameters. 
     * <p>HINT: use buildSearchParameters for an easy creation or customization of searchParameters</p>
     * 
     * @param searchParameters
     */
    public List<NodeRef> getNodeRefsBySearchParameters(SearchParameters searchParameters) {

        List<NodeRef> nodeRefs = new ArrayList<NodeRef>();
        
        ResultSet resultSetQuery = this.searchService.query(searchParameters);

        if (!resultSetQuery.getNodeRefs().isEmpty()) {
            nodeRefs = resultSetQuery.getNodeRefs();
        }

        return nodeRefs;
        
    }

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

}
