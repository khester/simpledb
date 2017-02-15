package dbms.query;

import dbms.schema.Row;
import dbms.schema.Schema;

import java.util.ArrayList;

public class QueryResult {
    private Schema schema;
    private ArrayList<Row> results = new ArrayList<>();
    private int rowsNumber = 0;

    public void setResults(ArrayList<Row> results) {
        this.results = results;
        this.rowsNumber = results.size();
    }

    public void addResults(ArrayList<Row> rows) {
        if (this.results == null) {
            this.results = rows;
        } else {
            this.results.addAll(rows);
        }
        rowsNumber += rows.size();
    }

    public ArrayList<Row> getResults() {
        return results;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public int getRowsNumber() {
        return rowsNumber;
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "schema=" + schema +
                ", results=" + results +
                ", rowsNumber=" + rowsNumber +
                '}';
    }


}
