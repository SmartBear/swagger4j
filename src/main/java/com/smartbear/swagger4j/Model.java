package com.smartbear.swagger4j;

import java.util.List;

/**
 * @author Yann D'Isanto
 */
public interface Model extends HasDataType {

    String getId();

    String getDescription();

    List<String> getRequiredProperties();

    List<Property> getProperties();
}
