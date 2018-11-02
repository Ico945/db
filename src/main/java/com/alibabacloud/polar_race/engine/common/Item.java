package com.alibabacloud.polar_race.engine.common;

import java.nio.MappedByteBuffer;

public class Item {
    public int key;
    public byte isUsed;
    public Location location = new Location();

    public static int serialize(MappedByteBuffer buff, int index, Item item) {
        buff.putInt(index, item.key);
        index += 4;
        buff.put(index, item.isUsed);
        index += 1;
        buff.putInt(index, item.location.file_no);
        index += 4;
        buff.putInt(index, item.location.offset);
        index += 4;
        buff.putInt(index, item.location.len);
        index += 4;
        return index;
    }
    public static Item deserialize(MappedByteBuffer buff, int index) {
        Item item = new Item();
        item.key = buff.getInt(index);
        index += 4;
        item.isUsed = buff.get(index);
        index += 1;
        item.location.file_no = buff.getInt(index);
        index += 4;
        item.location.offset = buff.getInt(index);
        index += 4;
        item.location.len = buff.getInt(index);
        index += 4;
        return item;
    }
}
