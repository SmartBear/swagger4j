package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.SwaggerVersion;

/**
 * Construct to handle change of property/element names in different Swagger versions
 */

public class Constants {

    public final static String SWAGGER_VERSION = "swaggerVersion";
    public final static String API_DOCUMENTATION = "ApiDocumentation";

    /**
     * Constants for the names used in Swagger definitions - these are not final static since they can be overridden
     * for specific versions
     */

    public String API_VERSION = "apiVersion";
    public String BASE_PATH = "basePath";
    public String PATH = "path";
    public String APIS = "apis";
    public String DESCRIPTION = "description";
    public String RESOURCE_PATH = "resourcePath";
    public String OPERATIONS = "operations";
    public String NICKNAME = "nickname";
    public String METHOD = "method";
    public String SUMMARY = "summary";
    public String NOTES = "notes";
    public String RESPONSE_CLASS = "responseClass";
    public String PARAMETERS = "parameters";
    public String NAME = "name";
    public String PARAM_TYPE = "paramType";
    public String ALLOW_MULTIPLE = "allowMultiple";
    public String REQUIRED = "required";
    public String TYPE = "type";
    public String RESPONSE_MESSAGES = "responseMessages";
    public String CODE = "code";
    public String MESSAGE = "message";
    public String PRODUCES = "produces";
    public String CONSUMES = "consumes";
    public String INFO = "info";
    public String INFO_CONTACT = "title";
    public String INFO_DESCRIPTION = "description";
    public String INFO_LICENSE = "license";
    public String INFO_LICENSE_URL = "licenseUrl";
    public String INFO_TERMSOFSERVICEURL = "termsOfServiceUrl";
    public String INFO_TITLE = "title";
    public String RESPONSE_MODEL = "responseModel";

    public static final Constants V1_1 = new V1_1Constants();
    public static final Constants V1_2 = new Constants();

    public static Constants get(SwaggerVersion version)
    {
        switch (version)
        {
            case V1_1: return V1_1;
            case V1_2: return V1_2;
        }

        throw new RuntimeException( "Unknown Swagger version: " + version );
    }

    private static class V1_1Constants extends Constants
    {
        public V1_1Constants()
        {
            METHOD = "httpMethod";
            MESSAGE = "reason";
            RESPONSE_MESSAGES = "errorResponses";
            TYPE = "dataType";
        }
    }
}
