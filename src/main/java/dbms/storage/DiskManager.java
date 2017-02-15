package dbms.storage;

import dbms.Consts;
import dbms.schema.Column;
import dbms.schema.Schema;

import java.io.*;
import java.util.ArrayList;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class DiskManager {
    public static final DiskManager instance = new DiskManager();

    static DiskManager getInstance() {
        return instance;
    }

    Page getPage(String pageId) throws IOException {
        // pageID: "fileName:startByte"

        File file = new File(Consts.SCHEMA_ROOT_PATH, pageId.substring(0, pageId.indexOf(":")) + ".data");

        RandomAccessFile raf = new RandomAccessFile(file, "r");
        int startByte = Integer.parseInt(pageId.substring(pageId.indexOf(":") + 1));
        raf.seek(startByte);
        byte[] result = new byte[Consts.BLOCK_SIZE];
        raf.read(result, 0, Consts.BLOCK_SIZE);
        raf.close();
        return new Page(result);
    }

    Map<String, Schema> loadrelationTable() {
        Map<String, Schema> schemaMap = new HashMap<>();
        for (String fileName: this.getAllSchemaFilePaths()) {
            String[] column;
            ArrayList<Column> columns = new ArrayList<Column>();
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    column = line.split(";");
                    if (column.length == 2) {
                        columns.add(new Column(column[0], column[1]));
                    } else {
                        columns.add(new Column(column[0], column[1], Integer.parseInt(column[2])));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Schema schema = new Schema(columns,fileName);
            schemaMap.put(fileName, schema);
        }
        return schemaMap;
    }



    private ArrayList<String> getAllSchemaFilePaths() {
        ArrayList<String> textFiles = new ArrayList<String>();
        File dir = new File(Consts.SCHEMA_ROOT_PATH);
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".meta"))) {
                textFiles.add(file.getAbsolutePath());
            }
        }
        return textFiles;
    }




}
