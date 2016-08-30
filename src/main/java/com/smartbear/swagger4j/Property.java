package com.smartbear.swagger4j;

/**
 * @author Yann D'Isanto
 */
public interface Property extends HasDataType {

    String getDescription();

    boolean isRequired();
}
