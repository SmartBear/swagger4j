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

package com.smartbear.swagger4j;

import java.util.Collection;
import java.util.List;

/**
 * Holds a Swagger API Declaration - see <a href="https://github.com/wordnik/swagger-core/wiki/API-Declaration"
 * target="_new">https://github.com/wordnik/swagger-core/wiki/API-Declaration</a>
 */

public interface ApiDeclaration {

    public final static String DEFAULT_API_VERSION = "1.0";

    public SwaggerVersion getSwaggerVersion();

    public void setSwaggerVersion(SwaggerVersion swaggerVersion);

    public String getApiVersion();

    public void setApiVersion(String apiVersion);

    public String getBasePath();

    public void setBasePath(String basePath);

    public String getResourcePath();

    public void setResourcePath(String resourcedPath);

    /**
     * Gets a list of APIs for this ApiDeclarations
     *
     * @see Api
     *
     * @return a list of Api objects
     */

    public List<Api> getApis();

    /**
     * Removes the specified Api from this ApiDeclaration
     *
     * @see Api
     *
     * @param api the Api to remove
     */

    public void removeApi(Api api);

    /**
     * Adds a new API to this ApiDeclaration with the specified path
     *
     * @see Api
     *
     * @param path the path for the API to add
     * @return the created API
     */

    public Api addApi(String path);

    /**
     * Gets the API at the specified path
     *
     * @see Api
     *
     * @param path the path to the API
     * @return the API at that path, null if none available
     */

    public Api getApi(String path);

    public Collection<String> getProduces();

    public void removeProduces(String produces);

    public void addProduces(String produces);

    public Collection<String> getConsumes();

    public void removeConsumes(String consumes);

    public void addConsumes(String consumes);

    public Collection<Model> getModels();

    public Model getModel(String id);

    public void addModel(Model model);

}
