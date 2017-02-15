package dbms.schema;

import dbms.Consts;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public class Column {
    private String name;
    private int type;
    private int size;

    public Column(String name, String type) {
        this.name = name;
        this.type = typeToInt(type);
        this.size = 4;
    }

    public Column(String name, String type, int size) {
        this.name = name;
        this.type = typeToInt(type);
        this.size = size;

    }

    private int typeToInt(String type) {
        switch (type) {
            case "varchar":
                return Consts.COLUMN_TYPE_VARCHAR;
            case "datetime":
                return Consts.COLUMN_TYPE_DATETIME;
            case "int":
                return Consts.COLUMN_TYPE_INTEGER;
        }
        return 0;
    }



    private String intToType(int type) {
        switch (type) {
            case Consts.COLUMN_TYPE_VARCHAR:
                return "varchar";
            case Consts.COLUMN_TYPE_DATETIME:
                return "datetime";
            case Consts.COLUMN_TYPE_INTEGER:
                return "int";

        }
        return null;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type=" + intToType(type) +
                ", size=" + size +
                '}';
    }
}
