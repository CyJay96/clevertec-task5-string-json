package ru.clevertec;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;

public class JsonConverter {

    public static String toJson(Object object) {
        StringBuilder json = new StringBuilder();

        json.append("{");

        Field[] fields = object.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    json.append("\"").append(field.getName()).append("\":");

                    if (field.getType().equals(String.class) || field.getType().equals(Character.class)) {
                        json.append("\"").append(fieldValue).append("\",");
                    } else if (
                            Number.class.isAssignableFrom(field.getType()) ||
                                    field.getType().equals(Boolean.class) || field.getType().isPrimitive()
                    ) {
                        json.append(fieldValue).append(",");
                    } else if (field.getType().isArray() || Iterable.class.isAssignableFrom(field.getType())) {
                        json.append(toJsonArray(fieldValue)).append(",");
                    } else {
                        json.append(toJson(fieldValue)).append(",");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (json.lastIndexOf(",") == json.length() - 1) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("}");

        return json.toString();
    }

    private static String toJsonArray(Object object) {
        StringBuilder jsonArray = new StringBuilder();

        jsonArray.append("[");

        if (object.getClass().isArray()) {
            int arrayLength = getLength(object);
            for (int i = 0; i < arrayLength; i++) {
                Object value = get(object, i);
                appendElementToJsonArray(jsonArray, value);
            }
        } else if (Iterable.class.isAssignableFrom(object.getClass())) {
            Iterable iterableObject = (Iterable) object;
            for (Object value : iterableObject) {
                appendElementToJsonArray(jsonArray, value);
            }
        }

        if (jsonArray.lastIndexOf(",") == jsonArray.length() - 1) {
            jsonArray.deleteCharAt(jsonArray.length() - 1);
        }
        jsonArray.append("]");

        return jsonArray.toString();
    }

    private static void appendElementToJsonArray(StringBuilder jsonArray, Object value) {
        if (
                Number.class.isAssignableFrom(value.getClass()) ||
                        value.getClass().equals(Boolean.class) || value.getClass().isPrimitive()
        ) {
            jsonArray.append(value).append(",");
        } else if (
                isNotNavigationPropertyType(value.getClass()) ||
                        value.getClass().equals(String.class) || value.getClass().equals(Character.class)
        ) {
            jsonArray.append("\"").append(value).append("\",");
        } else if (value.getClass().isArray() || Iterable.class.isAssignableFrom(value.getClass())) {
            jsonArray.append(toJsonArray(value)).append(",");
        } else {
            jsonArray.append(toJson(value)).append(",");
        }
    }

    private static boolean isNotNavigationPropertyType(Class<?> classType) {
        return classType.isPrimitive() || classType.getName().startsWith("java.") || classType.equals(String.class);
    }
}
