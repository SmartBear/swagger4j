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

/**
 * The default SwaggerFactory
 */

public class SwaggerFactoryImpl implements SwaggerFactory {

    @Override
    public ResourceListing createResourceListing(String basePath) {
        assert basePath != null : "basePath can not be null";
        return new ResourceListingImpl(basePath);
    }

    @Override
    public ApiDeclaration createApiDeclaration(String basePath, String resourcePath) {
        assert basePath != null && resourcePath != null : "basePath and resourcePath can not be null";
        return new ApiDeclarationImpl(basePath, resourcePath);
    }

    @Override
    public SwaggerReader createSwaggerReader() {
        return new SwaggerReaderImpl();
    }

    @Override
    public SwaggerWriter createSwaggerWriter(Constants.Format format) {
        return new SwaggerWriterImpl(format);
    }
}
