package com.smartbear.swagger4j;

/**
 *
 * @author Yann D'Isanto
 */
public interface DataType {

    String getType();

    String getRef();

    String getFormat();

    boolean isArray();

    boolean isPrimitive();

    boolean isComplex();

    boolean isVoid();

    boolean isRef();

    DataType VOID = new DataType() {

        @Override
        public String toString() {
            return getType();
        }

        @Override
        public String getType() {
            return "void";
        }

        @Override
        public String getRef() {
            return null;
        }

        @Override
        public String getFormat() {
            return null;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }

        public boolean isComplex() {
            return false;
        }

        @Override
        public boolean isVoid() {
            return true;
        }

        @Override
        public boolean isRef() {
            return false;
        }
    };

}
