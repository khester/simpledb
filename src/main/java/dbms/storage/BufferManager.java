package dbms.storage;

import dbms.Consts;
import dbms.query.QueryPlan;
import dbms.query.QueryResult;
import dbms.schema.Column;
import dbms.schema.Row;
import dbms.schema.Schema;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.ByteBuffer;

import java.util.HashMap;
import java.util.Date;

public class BufferManager {
    private static final BufferManager instance = new BufferManager();
    private HashMap<String, Page> bufferTable = new HashMap<>();
    private DiskManager diskManager = DiskManager.getInstance();
    private Map<String, Schema> schemas;
    public static BufferManager getInstance() {
        return instance;
    }

    public void loadSchemas() {
        this.schemas = diskManager.loadrelationTable();
    }


    private Schema getSchema(String metaFileName) {
        return schemas.get(String.format("%1$s/%2$s.meta", Consts.SCHEMA_ROOT_PATH, metaFileName));
    }



    public QueryResult executeQuery(QueryPlan queryPlan) throws IOException {
        QueryResult queryResult = new QueryResult();
        String pageID = "data:0";
        while (true) {
            if (isBuffered(pageID)) {
                Page page = bufferTable.get(pageID);
                queryResult.addResults(getRows(page));
                queryResult.setSchema(getSchema(page.getRelationName()));
                if (page.getNextPageName().isEmpty()) {
                    break;
                }
                pageID = String.format("%1$s:%2$d", page.getNextPageName(), page.getNextPageByte());
            } else {
                bufferPage(pageID);
            }
        }

        return queryResult;
    }


    private ArrayList<Row> getRows(Page page) {
        Schema schema = getSchema(page.getRelationName());
        ArrayList<Row> rows = new ArrayList<>();
        ByteBuffer wrapped = ByteBuffer.wrap(page.getData());
        for (int i = 0; i < page.getPointers().size(); ++i) {
            ArrayList<String> values = new ArrayList<>(schema.getColumns().size());
            int beginPointer = page.getPointers().get(i);
            for (Column column: schema.getColumns()) {
                switch (column.getType()) {
                    case Consts.COLUMN_TYPE_VARCHAR:
                        char[] col = new char[column.getSize() / 2];
                        for (int j = 0; j < column.getSize() / 2; ++j) {
                            col[j] = wrapped.getChar(2 * j + beginPointer);
                        }
                        values.add(String.valueOf(col).trim());

                    case Consts.COLUMN_TYPE_INTEGER:
                        values.add(String.valueOf(wrapped.getInt(beginPointer)));

                    case Consts.COLUMN_TYPE_DATETIME:
                        Date date = new Date();
                        date.setTime(wrapped.getInt(beginPointer) * 1000L);
                        values.add(date.toString());
                }
                beginPointer += column.getSize();
            }

            rows.add(new Row(values));
        }
        return rows;
    }

    public boolean isBuffered(String pageId) {
        return this.bufferTable.containsKey(pageId);
    }

    public void bufferPage(String pageId) throws IOException {
        this.bufferTable.put(pageId, diskManager.getPage(pageId));
    }

}
