package dbms.storage;

import dbms.Consts;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Page  {
    private String relationName;
    private String nextPageName;
    private int nextPageByte;
    private int pointerForNewTuple;

    private ArrayList<Integer> pointers;
    private byte[] data;

    public Page(byte[] page) {

        extractHeader(page);
        this.data = new byte[Consts.BLOCK_SIZE - Consts.BLOCK_HEADER_SIZE];
        this.data = Arrays.copyOfRange(page, Consts.BLOCK_HEADER_SIZE, Consts.BLOCK_SIZE);

    }

    public byte[] getData() {
        return data;
    }


    public int lastPointer() {return pointerForNewTuple;}

    public String getRelationName() {
        return relationName;
    }

    public String getNextPageName() {
        return nextPageName;
    }

    public int getNextPageByte() {
        return nextPageByte;
    }

    public ArrayList<Integer> getPointers() {
        return pointers;
    }
    public void setLastPointer(int lst) {this.pointerForNewTuple = lst;}
    public void setData(byte[] data) {this.data = data;}
    public void setPointers(ArrayList<Integer> pointer) {this.pointers = pointer;}



    private void extractHeader(byte[] page) {
        ByteBuffer wrapped = ByteBuffer.wrap(page);
        StringBuilder relationName = new StringBuilder(20);

        //change 10 to str size from relation table
        for (int i = 0; i < 10; ++i) {
            relationName.append(wrapped.getChar());
        }
        this.relationName = relationName.toString().trim();
        StringBuilder nextPageName = new StringBuilder(20);
        for (int i = 0; i < 10; ++i) {
            nextPageName.append(wrapped.getChar());
        }
        this.nextPageName = nextPageName.toString().trim();
        this.nextPageByte = wrapped.getInt();
        this.pointers = new ArrayList<>(63);
        for (int i = 0; i < 63; ++i) {
            pointers.add(wrapped.getInt());
        }
        this.pointerForNewTuple = wrapped.getInt();

    }

    @Override
    public String toString() {
        return "Page{" +
                "relationName='" + relationName + '\'' +
                ", nextPageByte=" + nextPageByte +
                ", pointers=" + pointers +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
