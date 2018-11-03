package com.alibabacloud.polar_race.engine.common;


import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class DoorPlate{
    private String _path;
    private MappedByteBuffer buf;
    public DoorPlate(String path) {
        _path = path;
    }

    public void Init() throws EngineException{
        File file = new File(_path + "/" + Tool.DoorPlateFile);
        if (!file.exists()) {
            try{
                file.createNewFile();
            } catch (IOException e) {
                throw new EngineException(RetCodeEnum.IO_ERROR, "");
            }
        }
        RandomAccessFile raf;
        FileInputStream in;
        try{
            raf = new RandomAccessFile(_path + "/" + Tool.DoorPlateFile, "rw");
        }catch(FileNotFoundException e) {
            throw new EngineException(RetCodeEnum.NOT_FOUND, _path+" file not found");
        }
        FileChannel fc = raf.getChannel();
        try {
            buf = fc.map(FileChannel.MapMode.READ_WRITE, 0, Tool.DoorPlateMapSize);
        }catch (IOException e) {
            throw new EngineException(RetCodeEnum.IO_ERROR, "");
        }
    }
    public void AddOrUpdate(byte[] key, Location location) {
        int index = CalcIndex(key);
        Item item = new Item();
        item.isUsed = 1;
        item.key = key;
        item.location = location;
        Item.write(buf, index, item);
    }

    public Location Find(byte[] key) {
        int index = CalcIndex(key);
        Item item = Item.read(buf, index);
        return item.location;
    }


    public int CalcIndex(byte[] key) {
        int index = Hash(key);
        while (!CanPlaceItem(index, key)) {
            index += Tool.ItemLength;
            index %= Tool.DoorPlateMapCount;
        }
        return index;
    }

    private boolean CanPlaceItem(int index, byte[] key) {
        // 当这个位置没有使用，或者使用者就是自己时可以放置新的value进去
        index = Tool.ItemLength * index;
        Item item = Item.read(buf, index);
        if (item.isUsed == 0) {
            return true;
        }
        return CompareBytes(key, item.key);
    }

    private boolean CompareBytes(byte[] a, byte[]b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private int Hash(byte[] key) {
        int hash = 0;
        for (byte k : key) {
            hash += 31 * k;
        }
        return hash % Tool.DoorPlateMapCount;
    }
}

