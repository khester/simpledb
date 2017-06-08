package dbms;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        int portNumber = Consts.PORT;

        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]);
        }
        //writeTestBinaryFile();

        Server server = new Server(portNumber);

        server.start();
    }

    public static void writeTestBinaryFile() throws FileNotFoundException, IOException {
        DataOutputStream os =
                new DataOutputStream(new FileOutputStream(String.format("%1$s/aaa.data", Consts.SCHEMA_ROOT_PATH)));
        String metaFileName = "aaa";
        writeString(metaFileName, os, 20); // 20 bytes

        String nextPageFileName = "";
        writeString(nextPageFileName, os, 20); // 20 bytes

        os.writeInt(4096); // next page start byte, 4 bytes

        // 256 bytes for pointers, max 64 entities in the block
        int numOfEntities = 0;

        for (int i = 0; i < 63 - numOfEntities; ++i) { // write 244 empty bytes
            os.writeInt(2);
        }
        os.writeInt(0);

        for(int i =0;i<3796;++i) os.writeByte(0);  // offset

        os.close();
    }

    private static void writeString(String name, DataOutputStream os, int maxBytes) throws IOException{
        int shortage = maxBytes - name.length() * 2;
        for (int i = 0; i < shortage; ++i) os.writeByte(0);
        os.writeChars(name);
    }

    private static void writeEntity(int id, String name, int timestamp, DataOutputStream os) throws IOException {
        os.writeInt(id);
        writeString(name, os, 10);
        os.writeInt(timestamp);
    }
}