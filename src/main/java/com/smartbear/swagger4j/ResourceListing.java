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

import java.util.List;

/**
 * Holds a Swagger Resource Listing - see <a href="https://github.com/wordnik/swagger-core/wiki/Resource-Listing"
 * target="_new">https://github.com/wordnik/swagger-core/wiki/Resource-Listing</a>
 */

public interface ResourceListing {

    public SwaggerVersion getSwaggerVersion();

    public void setSwaggerVersion(SwaggerVersion swaggerVersion);

    public String getApiVersion();

    public void setApiVersion(String apiVersion);

    public String getBasePath();

    public void setBasePath(String basePath);

    public List<ResourceListingApi> getApis();

    public void removeApi(ResourceListingApi api);

    public ResourceListingApi addApi(ApiDeclaration apiDeclaration, String path);

    /**
     * A reference to a Swagger API-Declaration contained within a Resource-Listing
     */

    public interface ResourceListingApi {

        public String getDescription();

        public void setDescription(String description);

        public ApiDeclaration getDeclaration();

        public String getPath();

        public void setPath(String path);
    }
}
