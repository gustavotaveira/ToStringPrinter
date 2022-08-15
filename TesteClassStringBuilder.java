package br.com.ebix.rebp.condominio.seguro.bean;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


public class TesteClassStringBuilder {

    private StringBuilder aJson = new StringBuilder();

    public TesteClassStringBuilder(Object o) {
        super();
        aJson.ensureCapacity(256);
    }

    /**
     * This append method prepares the a Key and Value element in the JSON for input of fieldName
     * and fieldValue
     * 
     * @param fieldName  - Json Key 
     * @param fieldValue - Json value
     * @return this -- returns Custom toString Builder
     */
    public TesteClassStringBuilder append(final String fieldName, final Object fieldValue) {
        aJson.append(aJson.length() == 0 ? "" : ",").append("\"").append(fieldName).append("\": ").append(new ValueSerializer(fieldValue).serialize());
        return this;
    }


    /**
     * Gives to a String in the JSON format
     * @return String - In the format of Json
     */
    public String build() {
        return toString();
    }

    /**
     * Builds a String representation of the object
     */
    @Override
    public String toString() {
        return "{" + aJson.toString() + "}";
    }

    class ValueSerializer {

        Object fieldValue;

        ValueSerializer(Object iFieldValue) {
            fieldValue = iFieldValue;
        }

        public StringBuilder serialize() {
            StringBuilder aValue = new StringBuilder();
            if (fieldValue == null) {
                aValue.append("null");
            } else if (fieldValue instanceof Number) {
                aValue.append(fieldValue);
            } else if (fieldValue instanceof CharSequence || fieldValue instanceof Enum) {
                aValue.append("\"").append(fieldValue).append("\"");
            } else if (fieldValue instanceof Collection<?> || fieldValue instanceof Object[]) {
                aValue.append(new LinearCollection(fieldValue).serialize());
            } else if (fieldValue instanceof Map<?, ?>) {
                aValue.append(new MappedCollection(fieldValue).serialize());
            } else {
                aValue.append(String.valueOf(fieldValue));
            }
            return aValue;
        }
    }

    class LinearCollection {
        Collection<?> items;
        public LinearCollection(Object objArray) {
            if (objArray == null) {
                items = null;
            } else if (objArray instanceof Object[]) {
                items = Arrays.asList((Object[]) objArray);
            } else if (objArray instanceof Collection<?>) {
                this.items = (Collection<?>) objArray;
            } else {
                items = null;
            }
        }

        public StringBuilder serialize() {
            if (items == null) {
                return null;
            }
            StringBuilder aStr = new StringBuilder();
            if (items.isEmpty()) {
                aStr.append("[]");
                return aStr;
            }
            aStr.append("[");
            for (Object item : items) {
                aStr.append(new ValueSerializer(item).serialize());
                aStr.append(",");
            }
            aStr.deleteCharAt(aStr.length() - 1);
            aStr.append("]");
            return aStr;
        }
    }

    class MappedCollection {

        Map<?, ?> aMap;

        public MappedCollection(Object iMap) {
            if(iMap ==null) {
                this.aMap = null;
            }else if(iMap instanceof Map<?,?> ) {
                this.aMap = (Map<?, ?>) iMap;
            } else {
                this.aMap = null;
            }
        }

        public StringBuilder serialize() {
            if (aMap == null) {
                return null;
            }
            StringBuilder aStr = new StringBuilder();
            if (aMap.entrySet().isEmpty()) {
                aStr.append("{}");
                return aStr;
            }
            aStr.append("{");
            for (Map.Entry<?, ?> aEntry : aMap.entrySet()) {
                aStr.append("\"").append(aEntry.getKey()).append("\": ");
                aStr.append(new ValueSerializer(aEntry.getValue()).serialize());
                aStr.append(",");
            }
            aStr.deleteCharAt(aStr.length() - 1);
            aStr.append("}");
            return aStr;
        }
    }
}
