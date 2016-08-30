/**
 * Copyright 2013 SmartBear Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.Parameter;

/**
 * Default implementation of the Parameter interface
 *
 * @see Parameter
 */

public class ParameterImpl implements Parameter {
    private ParamType paramType;
    private String name;
    private String description;
    private boolean required;
    private String type;
    private boolean allowMultiple;

    ParameterImpl(String name, ParamType type) {
        this.name = name;
        this.paramType = type;
    }

    public ParamType getParamType() {
        return paramType;
    }

    public void setParamType(ParamType paramType) {
        assert paramType != null : "paramType can not be null";

        this.paramType = paramType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        assert name != null : "parameter name can not be null";
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return paramType == ParamType.path || required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAllowMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(boolean multiple) {
        this.allowMultiple = multiple;
    }
}
