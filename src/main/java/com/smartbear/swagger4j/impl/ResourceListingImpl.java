/**
 *  Copyright 2013 SmartBear Software, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.ApiDeclaration;
import com.smartbear.swagger4j.Constants;
import com.smartbear.swagger4j.ResourceListing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Default implementation of the ResourceListing interface
 *
 * @see ResourceListing
 */

public class ResourceListingImpl implements ResourceListing {

    private String apiVersion;
    private String swaggerVersion = Constants.SWAGGER_VERSION;
    private String basePath;

    private ArrayList<ResourceListingApi> apiList = new ArrayList<ResourceListingApi>();

    ResourceListingImpl(String basePath) {
        this.basePath = basePath;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getSwaggerVersion() {
        return swaggerVersion;
    }

    public void setSwaggerVersion(String swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public List<ResourceListingApi> getApis() {
        return Collections.unmodifiableList(apiList);
    }

    @Override
    public void removeApi(ResourceListingApi api) {

        if (apiList.contains(api)) {
            apiList.remove(api);
        }
    }

    @Override
    public ResourceListingApi addApi(ApiDeclaration apiDeclaration, String path) {
        assert apiDeclaration != null : "apiDeclaration can not be null";
        assert getApi(apiDeclaration.getResourcePath()) == null : "Can not add API to Resource Listing; path already exists";

        synchronized (apiList) {
            ResourceListingApiImpl api = new ResourceListingApiImpl(apiDeclaration, path);
            apiList.add(api);
            return api;
        }
    }

    private ResourceListingApi getApi(String path) {
        synchronized (apiList) {
            for (ResourceListingApi api : apiList) {
                if (api.getPath().equals(path))
                    return api;
            }

            return null;
        }
    }

    public class ResourceListingApiImpl implements ResourceListingApi {
        private String description;
        private ApiDeclaration apiDeclaration;
        private String path;

        ResourceListingApiImpl(ApiDeclaration apiDeclaration, String path) {
            this.apiDeclaration = apiDeclaration;
            this.path = path;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public ApiDeclaration getDeclaration() {
            return apiDeclaration;
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public void setPath(String path) {
            assert path != null : "path can not be null";
            this.path = path;
        }
    }
}
