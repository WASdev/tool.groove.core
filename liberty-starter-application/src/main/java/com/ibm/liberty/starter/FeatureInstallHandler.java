/*******************************************************************************
 * Copyright (c) 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.ibm.liberty.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.liberty.starter.api.v1.model.internal.Services;
import com.ibm.liberty.starter.api.v1.model.registration.Service;

public class FeatureInstallHandler {
	
	private static final Logger log = Logger.getLogger(FeatureInstallHandler.class.getName());

    private final ServiceConnector serviceConnector;
    
    private HashMap<String, String> installFeatures = new HashMap<String, String>();
	private List<String> listOfFeatures;
	
    public FeatureInstallHandler(Services services, ServiceConnector serviceConnector) {
        this.serviceConnector = serviceConnector;
        setServices(services);
    }
    
    private void setServices(Services services) {
        for (Service service : services.getServices()) {
            String features = serviceConnector.getFeaturesToInstall(service);
            if(features != null && !features.trim().isEmpty()){
            	installFeatures.put(service.getId(), features);
            }
        }
    }
    
    /**
     * Returns a list of features, which are required by the services, that must be installed during Liberty installation 
     */
    public List<String> getFeaturesToInstall(){
    	if(listOfFeatures == null){
    		listOfFeatures = new ArrayList<String>();
    		for(String key : installFeatures.keySet()){
    			String features = installFeatures.get(key);
    			if(features.split(",").length > 0){
    				for(String feature : features.split(",")){
    					if(!listOfFeatures.contains(feature)){
    						listOfFeatures.add(feature);
    					}
    				}
    			}
    		}
    	}
    	log.fine("Features to install : " + listOfFeatures);
    	return listOfFeatures;
    }
}