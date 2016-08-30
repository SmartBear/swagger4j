package com.smartbear.swagger4j;

import java.util.List;

/**
 * @author Yann D'Isanto
 */
public interface HasDataType {

    String getName();

    DataType getDataType();

    Object getDefaultValue();

    /**
     * @return a fixed list of possible values.
     */
    List<String> getEnumValues();

    /**
     * @return the minimum valid value for the type, inclusive.
     */
    Number getMinimum();

    /**
     * @return the maximum valid value for the type, inclusive.
     */
    Number getMaximum();

}
