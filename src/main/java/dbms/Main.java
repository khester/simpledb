package dbms;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        int portNumber = Consts.PORT;

        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]);
        }
        makeRecord();
        Server server = new Server(portNumber);
        server.start();
    }

    public static void makeRecord() throws IOException {
        DataOutputStream writer =
                new DataOutputStream(new FileOutputStream(String.format("%1$s/data.data", Consts.SCHEMA_ROOT_PATH)));

        String metaFileName = "data";
        writeString(metaFileName, writer, 20);
        String nextPageFileName = "";
        writeString(nextPageFileName, writer, 20);
        writer.writeInt(4096);
        writer.writeInt(0);
        for (int i = 0; i < 63; ++i) {
            writer.writeInt(0);
        }
        newRecord(123, "123", (int) (System.currentTimeMillis() / 1000L), writer);
        for (int i = 0; i < 3778; ++i) {
            writer.writeByte(0);
        }
        writer.close();
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
}