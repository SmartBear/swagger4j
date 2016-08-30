/*
 * Copyright 2014 Yann D'Isanto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.DataType;
import com.smartbear.swagger4j.Model;
import com.smartbear.swagger4j.Property;

import java.util.Collections;
import java.util.List;

/**
 * @author Yann D'Isanto
 */
public class ModelImpl implements Model {

    private String id;

    private final DataType dataType;

    private String description;

    private List<String> requiredProperties;

    private List<Property> properties;

    public ModelImpl(String id, String description) {
        this.id = id;
        this.description = description;
        dataType = new ModelDataType(id);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<String> getRequiredProperties() {
        return requiredProperties;
    }

    public void setRequiredProperties(List<String> requiredProperties) {
        this.requiredProperties = requiredProperties;
    }

    @Override
    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getName() {
        return getId();
    }

    public DataType getDataType() {
        return dataType;
    }

    public List<String> getEnumValues() {
        return Collections.emptyList();
    }

    public Object getDefaultValue() {
        return null;
    }

    public Number getMinimum() {
        return null;
    }

    public Number getMaximum() {
        return null;
    }

}
