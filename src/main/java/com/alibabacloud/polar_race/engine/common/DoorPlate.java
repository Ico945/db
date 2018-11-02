//package com.alibabacloud.polar_race.engine.common;
//
//
//import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
//import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.MappedByteBuffer;
//import java.nio.channels.FileChannel;
//
//public class DoorPlate{
//    private String _path;
//    public class Item {
//        public String key;
//        public Location location;
//        public Boolean isUsed;
//    }
//    public DoorPlate(String path) {
//        _path = path;
//    }
//
//    public void Init() throws EngineException{
//        FileInputStream in;
//        try{
//            in = new FileInputStream(_path + Tool.DoorPlateFile);
//        }catch(FileNotFoundException e) {
//            throw new EngineException(RetCodeEnum.NOT_FOUND, _path+" file not found");
//        }
//        FileChannel fc = in.getChannel();
//        MappedByteBuffer buff = null;
//        try {
//            buff = fc.map(FileChannel.MapMode.READ_WRITE, 0, Tool.DoorPlateMapSize);
//        }catch (IOException e) {
//            throw new EngineException(RetCodeEnum.IO_ERROR, "");
//        }
//
//    }
//    public Location AddOrUpdate(String key) {
//        return new Location();
//    }
//
//    public int CalcIndex(String key) {
//        int index = key.hashCode();
//        while ()
//        return index;
//    }
//}
