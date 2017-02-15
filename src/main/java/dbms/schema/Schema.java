package dbms.schema;

import java.util.ArrayList;

public class Schema {
    private ArrayList<Column> columns;
    private String schemaFilePath;
    private String dataFilePath;

    public Schema(ArrayList<Column> columns, String schemaFilePath) {
        this.columns = columns;
        this.schemaFilePath = schemaFilePath;
    }

    public Schema(String schemaFilePath) {
        this.columns = new ArrayList<>();
        this.schemaFilePath = schemaFilePath;
    }

    public int getSize() {
        return columns.stream().mapToInt(Column::getSize).sum();
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setSchemaFilePath(String schemaFilePath) {
        this.schemaFilePath = schemaFilePath;
    }

    public String getSchemaFilePath() {
        return schemaFilePath;
    }

    @Override
    public String toString() {
        return "Schema{" +
                "columns=" + columns +
                ", schemaFilePath='" + schemaFilePath + '\'' +
                ", dataFilePath='" + dataFilePath + '\'' +
                '}';
    }
}
