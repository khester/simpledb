package dbms.query;

public class Operation {
    private String entityName; //table1
    private int type; //fullscan
    private String predicate; // where.. somebody

    public Operation(String entityName, int type, String predicate) {
        this.entityName = entityName;
        this.type = type;
        this.predicate = predicate;
    }

    public Operation(String entityName, int type) {
        this.entityName = entityName;
        this.type = type;
    }
}
