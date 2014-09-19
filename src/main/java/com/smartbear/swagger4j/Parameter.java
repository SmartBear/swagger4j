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
 * Holds a Swagger parameter defined for an API Operation - see <a href="https://github.com/wordnik/swagger-core/wiki/Parameters"
 * target="_new">https://github.com/wordnik/swagger-core/wiki/Parameters</a>
 */

public interface Parameter {
    public ParamType getParamType();

    public void setParamType(ParamType paramType);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public boolean isRequired();

    public void setRequired(boolean required);

    public String getType();

    public void setType(String type);

    public boolean isAllowMultiple();

    public void setAllowMultiple(boolean multiple);
    
    /**
     * @return a fixed list of possible values.
     */
    Collection<String> getEnumValues();
    
    void setEnumValues(Collection<String> enumValues);
    
    void setDefaultValue(String defaultValue);
    
    String getDefaultValue();

    /**
     * Parameter type - see <a href="https://github.com/wordnik/swagger-core/wiki/Parameters"
     * target="_new">https://github.com/wordnik/swagger-core/wiki/Parameters</a>
     */

    public enum ParamType {path, query, body, header, form}
}
