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

import com.smartbear.swagger4j.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of the ResourceListing interface
 *
 * @see ResourceListing
 */

public class ResourceListingImpl implements ResourceListing {

    private String apiVersion;
    private SwaggerVersion swaggerVersion = SwaggerVersion.DEFAULT_VERSION;
    private String basePath;
    private AuthorizationsImpl authorizations;

    private final List<ResourceListingApi> apiList = new ArrayList<ResourceListingApi>();
    private InfoImpl info;

    ResourceListingImpl(SwaggerVersion swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public SwaggerVersion getSwaggerVersion() {
        return swaggerVersion;
    }

    public void setSwaggerVersion(SwaggerVersion swaggerVersion) {
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

    @Override
    public Info getInfo() {
        if( info == null )
            info = new InfoImpl();

        return info;
    }

    public Authorizations getAuthorizations()
    {
        if( authorizations == null )
            authorizations = new AuthorizationsImpl();

        return authorizations;
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

    public Collection<Model> getApisModels() {
        List<Model> models = new ArrayList<Model>();
        for (ResourceListingApi api : getApis()) {
            models.addAll(api.getDeclaration().getModels());
        }
        return models;
    }

    public static class ResourceListingApiImpl implements ResourceListingApi {
        private String description;
        private final ApiDeclaration apiDeclaration;
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
