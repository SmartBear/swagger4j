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

import com.smartbear.swagger4j.DataType;
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
    private DataType dataType;

    ParameterImpl(String name, ParamType type) {
        this.name = name;
        this.paramType = type;
    }

    @Override
    public ParamType getParamType() {
        return paramType;
    }

    @Override
    public void setParamType(ParamType paramType) {
        assert paramType != null : "paramType can not be null";

        this.paramType = paramType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        assert name != null : "parameter name can not be null";
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isRequired() {
        return paramType == ParamType.path || required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isAllowMultiple() {
        return allowMultiple;
    }

    @Override
    public void setAllowMultiple(boolean multiple) {
        this.allowMultiple = multiple;
    }

	@Override
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	@Override
	public DataType getDataType() {
		if(this.dataType == null){
			return DataType.VOID;
		}else{
			return this.dataType;
		}
	}
}
