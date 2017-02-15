package dbms.query;

import java.util.ArrayList;

public class QueryPlan {
    private ArrayList<Operation> operations;
    private String query;

    public QueryPlan(String query) {
        this.query = query;
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    private void addOperation(Operation operation) {
        this.operations.add(operation);
    }
}
