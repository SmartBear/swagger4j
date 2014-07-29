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
package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.DataType;
import com.smartbear.swagger4j.Items;

/**
 *
 * @author James Moger
 *
 */
public class DataTypeImpl implements DataType {

	private String type;
	private String ref;
	private String format;
	private String defaultValue;
	private String[] enumValues;
	private String minimum;
	private String maximum;
	private boolean uniqueItems;
	private Items items;

	public DataTypeImpl() {
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
	public String getRef() {
		return ref;
	}

	@Override
	public void setRef(String ref) {
		this.ref = ref;
	}

	@Override
	public String getFormat() {
		return format;
	}

	@Override
	public void setFormat(String value) {
		this.format = value;
	}

	@Override
	public String[] getAllowedValues() {
		return enumValues;
	}

	@Override
	public void setAllowedValues(String[] values) {
		if (values != null && values.length > 0) {
			this.enumValues = values;
		}
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}

	@Override
	public String getMinimum() {
		return minimum;
	}

	@Override
	public void setMinimum(String value) {
		this.minimum = value;
	}

	@Override
	public String getMaximum() {
		return maximum;
	}

	@Override
	public void setMaximum(String value) {
		this.maximum = value;
	}

	@Override
	public boolean isUniqueItems() {
		return uniqueItems;
	}

	@Override
	public void setUniqueItems(boolean value) {
		this.uniqueItems = value;
	}

	@Override
	public Items getItems() {
		return items;
	}

	@Override
	public void setItems(Items value) {
		this.items = value;
	}
}
