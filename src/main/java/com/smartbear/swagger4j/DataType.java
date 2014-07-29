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

/**
 *
 * @author James Moger
 *
 */
public interface DataType {

	String getType();

	void setType(String value);

	String getRef();

	void setRef(String value);

	String getFormat();

	void setFormat(String value);

	String[] getAllowedValues();

	void setAllowedValues(String[] value);

	String getDefaultValue();

	void setDefaultValue(String value);

	String getMinimum();

	void setMinimum(String value);

	String getMaximum();

	void setMaximum(String value);

	boolean isUniqueItems();

	void setUniqueItems(boolean value);

	Items getItems();

	void setItems(Items value);

}
