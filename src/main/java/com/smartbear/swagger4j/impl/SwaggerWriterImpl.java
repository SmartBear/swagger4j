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
import java.io.Writer;
import java.util.Collection;

/**
 * Default implementation of the SwaggerWriter interface
 *
 * @see SwaggerWriter
 */

public class SwaggerWriterImpl implements SwaggerWriter {

    private final SwaggerFormat format;

    public SwaggerWriterImpl(SwaggerFormat format) {
        this.format = format;
    }

    @Override
    public void writeApiDeclaration(ApiDeclaration declaration, Writer writer) throws IOException {
        SwaggerGenerator w = SwaggerGenerator.newGenerator( writer, format );

        w.addString(Utils.SWAGGER_VERSION, declaration.getSwaggerVersion().getIdentifier());
        w.addString(Utils.API_VERSION, declaration.getApiVersion());
        w.addString(Utils.BASE_PATH, declaration.getBasePath());
        w.addString(Utils.RESOURCE_PATH, declaration.getResourcePath());

        for (Api api : declaration.getApis()) {
            SwaggerGenerator aw = w.addObject(Utils.APIS);
            aw.addString(Utils.PATH, api.getPath());
            aw.addString(Utils.DESCRIPTION, api.getDescription());

            for (Operation operation : api.getOperations()) {
                SwaggerGenerator ow = aw.addObject(Utils.OPERATIONS);
                ow.addString(Utils.NICKNAME, operation.getNickName());
                ow.addString(Utils.HTTP_METHOD, operation.getMethod().name());
                ow.addString(Utils.SUMMARY, operation.getSummary());
                ow.addString(Utils.NOTES, operation.getNotes());
                ow.addString(Utils.RESPONSE_CLASS, operation.getResponseClass());

                for (Parameter parameter : operation.getParameters()) {
                    SwaggerGenerator pw = ow.addObject(Utils.PARAMETERS);
                    pw.addString(Utils.NAME, parameter.getName());
                    pw.addString(Utils.PARAM_TYPE, parameter.getParamType().name());
                    pw.addBoolean(Utils.ALLOW_MULTIPLE, parameter.isAllowMultiple());
                    pw.addString(Utils.DESCRIPTION, parameter.getDescription());
                    pw.addBoolean(Utils.REQUIRED, parameter.isRequired());
                    pw.addString(Utils.DATA_TYPE, parameter.getDataType());
                }

                for (ErrorResponse errorResponse : operation.getErrorResponses()) {
                    SwaggerGenerator ew = ow.addObject(Utils.ERROR_RESPONSES);
                    ew.addInt(Utils.CODE, errorResponse.getCode());
                    ew.addString(Utils.REASON, errorResponse.getReason());
                }

                Collection<String> produces = operation.getProduces();
                if( !produces.isEmpty())
                    ow.addArray(Utils.PRODUCES, produces.toArray(new String[produces.size()]));

                Collection<String> consumes = operation.getConsumes();
                if( !consumes.isEmpty())
                    ow.addArray(Utils.CONSUMES, consumes.toArray(new String[consumes.size()]));
            }
        }

        w.finish();
    }

    @Override
    public void writeResourceListing(ResourceListing listing, Writer writer) throws IOException {
        SwaggerGenerator w = SwaggerGenerator.newGenerator( writer, format );

        w.addString(Utils.API_VERSION, listing.getApiVersion());
        w.addString(Utils.SWAGGER_VERSION, listing.getSwaggerVersion().getIdentifier());
        w.addString(Utils.BASE_PATH, listing.getBasePath());

        for (ResourceListing.ResourceListingApi api : listing.getApis()) {
            SwaggerGenerator sw = w.addObject(Utils.APIS);
            sw.addString(Utils.DESCRIPTION, api.getDescription());
            sw.addString(Utils.PATH, api.getPath());
        }

        w.finish();
    }

    @Override
    public SwaggerFormat getFormat() {
        return format;
    }

    @Override
    public void writeSwagger(SwaggerStore store, ResourceListing resourceListing) throws IOException {
        Writer writer = store.createResource("api-docs." + format.getExtension());
        writeResourceListing(resourceListing, writer);

        for (ResourceListing.ResourceListingApi api : resourceListing.getApis()) {
            ApiDeclaration declaration = api.getDeclaration();
            String path = Utils.createFileNameFromPath(api.getPath(), format);

            writer = store.createResource(path);
            writeApiDeclaration(declaration, writer);
        }
    }
}
