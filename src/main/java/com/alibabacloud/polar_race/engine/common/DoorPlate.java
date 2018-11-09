package com.alibabacloud.polar_race.engine.common;


import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DoorPlate {
    private String _path;
    private MappedByteBuffer[] bufs = new MappedByteBuffer[Tool.plate_num];
    public DoorPlate(String path) {
        _path = path;
    }

    public void Init() throws EngineException {
        for (int i=1; i<=Tool.plate_num; i++) {
            File file = new File(_path + "/" + Tool.DoorPlateFile + i);
            if (!file.exists()) {
                try{
                    file.createNewFile();
                } catch (IOException e) {
                    throw new EngineException(RetCodeEnum.IO_ERROR, "");
                }
            }
        }

        RandomAccessFile[] rafs = new RandomAccessFile[Tool.plate_num];
        try{
            for (int i=1; i<=Tool.plate_num; i++)
                rafs[i-1] = new RandomAccessFile(_path + "/" + Tool.DoorPlateFile+i, "rw");
        }catch(FileNotFoundException e) {
            throw new EngineException(RetCodeEnum.NOT_FOUND, _path+" file not found");
        }
        FileChannel[] fcs = new FileChannel[Tool.plate_num];
        for (int i=0; i<Tool.plate_num; i++)
            fcs[i] = rafs[i].getChannel();
        try {
            for (int i=0; i<Tool.plate_num; i++)
                bufs[i] = fcs[i].map(FileChannel.MapMode.READ_WRITE, 0, Tool.DoorPlateMapSize);
        }catch (IOException e) {
            throw new EngineException(RetCodeEnum.IO_ERROR, "");
        }
    }

    public void AddOrUpdate(byte[] key, Location location, int index_plate) {
        int index = CalcIndex(key, index_plate);
//        System.out.println(index);
        Item item = new Item();
        item.isUsed = 1;
        item.key = key;
        item.location = location;
        Item.write(bufs[index_plate], index, item);
    }

    public Location Find (byte[] key, int index_plate) throws EngineException {
//        int index = CalcIndex(key);
//        System.out.println(index);
//        Item item = Item.read(buf, index);
//        return item.location;

        int index = Tool.Hash(key, Tool.DoorPlateMapCount);
        Item item = Item.read(bufs[index_plate], index);
        while (!CompareBytes(item.key, key)) {
            if (item.isUsed == 0)
                break;
            else {
                index++;
                item = Item.read(bufs[index_plate], index);
            }
        }
        if (item.isUsed == 0)
            throw new EngineException(RetCodeEnum.NOT_FOUND, "");
        else
            return item.location;
    }


    public int CalcIndex(byte[] key, int index_plate) {
        int index = Tool.Hash(key, Tool.DoorPlateMapCount);
        while (!CanPlaceItem(index, key, index_plate)) {
            index += 1;
            index %= Tool.DoorPlateMapCount;
        }
        return index;
    }

    private boolean CanPlaceItem(int index, byte[] key, int index_plate) {
        // 当这个位置没有使用，或者使用者就是自己时可以放置新的value进去
//        index *= Tool.ItemLength;
        Item item = Item.read(bufs[index_plate], index);
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
}