package com.alibabacloud.polar_race.engine.common;

import java.nio.MappedByteBuffer;

public class Item {
    public int isUsed;  // 4bytes
    public byte[] key = new byte[8]; // 8 bytes
//    public int keyLength;   // 4 bytes
    public Location location = new Location();  // 12 bytes

    public static int write(MappedByteBuffer buff, int index, Item item) {
        //  将item写入buff的指定位置
//        buff.putInt(index, item.keyLength);
//        index += 4;
        buff.putInt(index, item.isUsed);
        index += 4;
        buff.putInt(index, item.location.file_no);
        index += 4;
        buff.putInt(index, item.location.offset);
        index += 4;
        buff.putInt(index, item.location.len);
        index += 4;
        for(int i = 0; i < item.key.length; i++) {
            buff.put(index+i, item.key[i]);
        }
        return index;
    }
    public static Item read(MappedByteBuffer buff, int index) {
        //  从指定位置读取item
        Item item = new Item();
//        item.keyLength = buff.getInt(index);
//        index += 4;
        item.isUsed = buff.get(index);
        index += 4;
        item.location.file_no = buff.getInt(index);
        index += 4;
        item.location.offset = buff.getInt(index);
        index += 4;
        item.location.len = buff.getInt(index);
        index += 4;
        for(int i = 0; i < item.key.length; i++) {
            item.key[i] = buff.get(index+i);
        }
        return item;
    }
}
