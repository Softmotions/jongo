package org.jongo.constraints;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Adamansky Anton (anton@adamansky.com)
 * @version $Id$
 */
public class Sort implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<SortField> fields;

    public Sort() {
        fields = new ArrayList<>();
    }

    public Sort(String fieldName, boolean ascending) {
        this();
        add(fieldName, ascending);
    }

    public Sort(String fieldName) {
        this(fieldName, true);
    }

    public Sort add(String fieldName, boolean ascending) {
        fields.add(new SortField(fieldName, ascending));
        return this;
    }

    public Sort add(String fieldName) {
        return add(fieldName, true);
    }

    public List<SortField> getFields() {
        return fields;
    }

    public class SortField implements Serializable {

        private static final long serialVersionUID = 1L;

        private String name;

        private boolean ascending;

        public SortField() {
        }

        public SortField(String name, boolean ascending) {
            this.name = name;
            this.ascending = ascending;
        }

        public boolean isAscending() {
            return ascending;
        }

        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("Sort{");
        sb.append("fields=").append(fields);
        sb.append('}');
        return sb.toString();
    }
}
