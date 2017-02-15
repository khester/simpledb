package dbms.schema;

import java.util.ArrayList;

public class Row {
    private ArrayList<String> data;

    public Row(ArrayList<String> data) {
        this.data = data;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Row{" +
                "data=" + data +
                '}';
    }
}
