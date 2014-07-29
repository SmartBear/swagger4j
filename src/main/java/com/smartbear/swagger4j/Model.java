/**
 *  Copyright 2014 SmartBear Software, Inc.
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.smartbear.swagger4j.impl.PropertyImpl;

/**
 *
 * @author James Moger
 *
 */
public class Model {

	String id;

	Set<String> requiredProperties;

	Map<String, Property> properties;

	public Model(String id) {
		this.id = id;
		this.properties = new LinkedHashMap<>();
		this.requiredProperties = new TreeSet<String>();
	}

	public String getId() {
		return id;
	}

	public Property addProperty(String name, boolean required) {
		PropertyImpl property = new PropertyImpl();
		properties.put(name, property);
		if (required) {
			requiredProperties.add(name);
		}
		return property;
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

	public Collection<String> getRequiredProperties() {
		return Collections.unmodifiableCollection(requiredProperties);
	}
}
