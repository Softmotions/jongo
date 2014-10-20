package org.jongo.bson;

import com.mongodb.DBObject;

/**
 * @author Adamansky Anton (adamansky@gmail.com)
 */
public class DBObjectUtils {

    public static <T> T get(DBObject dbo, String property) {
        String[] parts = property.split("\\.");
        Object val = dbo.get(parts[0]);
        for (int i = 1; i < parts.length && (val instanceof DBObject); ++i) {
            val = ((DBObject) val).get(parts[i]);
        }
        return (T) val;
    }
}
