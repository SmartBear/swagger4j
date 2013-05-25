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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Default implementation of the ApiDeclaration interface
 *
 * @see ApiDeclaration
 */

public class ApiDeclarationImpl implements ApiDeclaration {
    private static Logger log = Logger.getLogger(ApiDeclarationImpl.class.getName());

    private String apiVersion = Constants.DEFAULT_API_VERSION;
    private String basePath;
    private String swaggerVersion = Constants.SWAGGER_VERSION;
    private String resourcePath;
    private ArrayList<Api> apiList = new ArrayList<Api>();

    ApiDeclarationImpl( String basePath, String resourcePath ) {
        assert basePath != null && resourcePath != null : "basePath and resourcePath must not be null";

        this.basePath = basePath;
        this.resourcePath = resourcePath;
    }

    @Override
    public String getSwaggerVersion() {
        return swaggerVersion;
    }

    @Override
    public void setSwaggerVersion(String swaggerVersion) {
        assert swaggerVersion != null : "swaggerVersion can not be null";

        this.swaggerVersion = swaggerVersion;
    }

    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public void setApiVersion(String apiVersion) {
        assert apiVersion != null : "apiVersion can not be null";

        this.apiVersion = apiVersion;
    }

    @Override
    public String getBasePath() {
        return basePath;
    }

    @Override
    public void setBasePath(String basePath) {
        assert basePath != null : "basePath can not be null";

        this.basePath = basePath;
    }

    @Override
    public String getResourcePath() {
        return resourcePath;
    }

    @Override
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public List<Api> getApis() {
        return Collections.unmodifiableList(apiList);
    }

    @Override
    public void removeApi(Api api) {
        assert api != null : "api can not be null";

        synchronized (apiList) {
            apiList.remove(api);
        }
    }

    @Override
    public Api addApi(String path) {
        assert path != null : "Can not add api with null path";
        assert getApi(path) == null : "Api already exists at path [" + path + "]";

        synchronized (apiList) {
            ApiImpl api = new ApiImpl(path);
            apiList.add(api);
            return api;
        }
    }

    @Override
    public Api getApi(String path) {
        assert path != null : "api path can not be null";

        synchronized (apiList) {
            for (Api api : apiList)
                if (api.getPath().equals(path))
                    return api;

            return null;
        }
    }
}
