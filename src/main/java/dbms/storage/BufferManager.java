package dbms.storage;

import dbms.Consts;
import dbms.query.QueryPlan;
import dbms.query.QueryResult;
import dbms.schema.Column;
import dbms.schema.Row;
import dbms.schema.Schema;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.io.IOException;
import java.nio.ByteBuffer;

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
        //queryPlan

        QueryResult queryResult = new QueryResult();
        String pageID = "aaa:0";
        //fullscan

        while (true) {
            if (isBuffered(pageID)) {
                Page page = bufferTable.get(pageID);

                //queryResult.addResults(getRows(page));
                //queryResult.setSchema(getSchema(page.getRelationName()));
                if (page.getNextPageName().isEmpty()) {
                    break;
                }
                pageID = String.format("%1$s:%2$d", page.getNextPageName(), page.getNextPageByte());
            } else {
                bufferPage(pageID);
            }
        }


        //insert in pageid
        /*
        while(true) {
            if (isBuffered(pageID)) {
                Page page = bufferTable.get(pageID);
                ArrayList<Integer> pointers = new ArrayList();
                pointers = page.getPointers();
                //page.getPointers();

                int previousPointer = 0;
                for (int i = 0; i < pointers.size(); ++i)
                    if (pointers.get(i) != -1) {

                    } else {
                        previousPointer = pointers.get(i);
                        page.getData();
                    }

                if (currentPointer == pointers.size() - 1) {
                    pageID = String.format("%1$s:%2$d", page.getNextPageName(), page.getNextPageByte());
                } else {
                    break;
                }
            }
        }

*/

        if (isBuffered(pageID)) {
            Page page = bufferTable.get(pageID);
            ArrayList<String> tuples = new ArrayList<>();
            tuples.add("987654");
            tuples.add("roman");
            tuples.add(Long.toString(System.currentTimeMillis() / 1000L));
            Page dsa = makeRecord(page,tuples);

            queryResult.addResults(getRows(dsa));
            queryResult.setSchema(getSchema(dsa.getRelationName()));
        } else {
            bufferPage(pageID);
        }


            return queryResult;
    }

    //read data from page and return arraylist of rows

    private ArrayList<Row> getRows(Page page) {
        Schema schema = getSchema(page.getRelationName());
        ArrayList<Row> rows = new ArrayList<>();
        ByteBuffer wrapped = ByteBuffer.wrap(page.getData());
        int index = 0;
        while (page.getPointers().get(index)!=2) {
            ArrayList<String> values = new ArrayList<>(schema.getColumns().size());
            int beginPointer = page.getPointers().get(index);
            for (Column column: schema.getColumns()) {
                switch (column.getType()) {
                    case Consts.COLUMN_TYPE_VARCHAR:

                        char[] col = new char[column.getSize() / 2];
                        for (int j = 0; j < column.getSize() / 2; ++j) {
                            col[j] = wrapped.getChar(beginPointer);
                            beginPointer += 3;
                            System.out.print(col[j]);
                            System.out.println(" <- ITS A COLUMN");

                        }

                        values.add(String.valueOf(col).trim());
                        break;
                    case Consts.COLUMN_TYPE_INTEGER:
                        values.add(String.valueOf(wrapped.getInt(beginPointer)));
                        beginPointer+=5;
                        break;
                    case Consts.COLUMN_TYPE_DATETIME:
                        Date date = new Date();
                        date.setTime(wrapped.getInt(beginPointer) * 1000L);
                        values.add(date.toString());
                        beginPointer += 5;
                        break;
                }

            }
            index++;
            rows.add(new Row(values));
        }
        return rows;
    }



    public Page makeRecord(Page page, ArrayList<String> newTuple) throws IOException {

        /*
        1) get data.
        2) read head
        3) get metafile
        4) get next
        5) check
         */

        byte[] dataPage = page.getData();
        ArrayList<Integer> pointers = new ArrayList();
        pointers = page.getPointers();
        int currentPosition = page.lastPointer();
        for(int i = 0;i < pointers.size(); ++i) {
            if(pointers.get(i) == 2) {
                pointers.set(i, page.lastPointer());
                break;
            }
        }

        int index = 0;
        Schema schema = getSchema(page.getRelationName());
        for (Column column: schema.getColumns()) {

            String currValue = newTuple.get(index);

            switch (column.getType()) {
                case Consts.COLUMN_TYPE_INTEGER:

                    ByteBuffer.wrap(dataPage).putInt(currentPosition, Integer.parseInt(currValue));

                    currentPosition += 5;

                    break;
                case Consts.COLUMN_TYPE_VARCHAR:

                    // error
                    //char[] charArray =  currValue.toCharArray();
                    for(int i = 0;i < currValue.length(); ++i) {
                        ByteBuffer.wrap(dataPage).putChar(currentPosition, currValue.charAt(i));

                        currentPosition+=3;
                    }
                    break;
                case Consts.COLUMN_TYPE_DATETIME:
                    ByteBuffer.wrap(dataPage).putInt(currentPosition, (int) (System.currentTimeMillis() / 1000L));
                    currentPosition += 5;
                    break;

            }
            System.out.println("end of iteration");
            index++;


        }
        page.setLastPointer(currentPosition);
        page.setData(dataPage);
        page.setPointers(pointers);


        System.out.println(currentPosition);

        return page;


/*
        DataOutputStream writer =
                new DataOutputStream(new FileOutputStream(String.format("%1$s/data.data", Consts.SCHEMA_ROOT_PATH)));

        String metaFileName = "data";
        writeString(metaFileName, writer, 20);
        String nextPageFileName = "";
        writeString(nextPageFileName, writer, 20);
        writer.writeInt(4096);

        for (int i = 0; i < 63; ++i) {
            writer.writeInt(-1);
        }
        //18 bytes for a record
        newRecord(123, "123", (int) (System.currentTimeMillis() / 1000L), writer);
        for (int i = 0; i < 3778; ++i) {
            writer.writeByte(0);
        }
        writer.close();
*/
    }

    private static void writeString(String str, DataOutputStream writer, int max) throws IOException {
        int res = max - str.length() * 2;
        for (int i = 0; i < res; ++i) {
            writer.writeByte(0);
        }
        writer.writeChars(str);
    }

    private static void newRecord(int id, String str, int time, DataOutputStream writer) throws IOException {
        writer.writeInt(id);
        writeString(str, writer, 10);
        writer.writeInt(time);
    }

    public boolean isBuffered(String pageId) {
        return this.bufferTable.containsKey(pageId);
    }

    public void bufferPage(String pageId) throws IOException {
        this.bufferTable.put(pageId, diskManager.getPage(pageId));

    }

}
