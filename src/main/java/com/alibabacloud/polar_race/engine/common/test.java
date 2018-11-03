package com.alibabacloud.polar_race.engine.common;


import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class test {

    public static void main(String[] args) {
//        FileInputStream in = null;
//        String path = "/home/ss/Desktop/db/src/main/java/com/alibabacloud/polar_race/engine/common/ss.txt";
//        MappedByteBuffer buff = null;
//        try {
//            buff = new RandomAccessFile(path,"rw")
//                    .getChannel()
//                    .map(FileChannel.MapMode.READ_WRITE, 0, 1000);
//        }catch (IOException e) {
//            e.printStackTrace();
////            throw new EngineException(RetCodeEnum.IO_ERROR, "");
//        }
        String dir_path = "/home/ss/Desktop/db/src/main/java/com/alibabacloud/polar_race/engine/common/temp";
//        DoorPlate dp = new DoorPlate(dr_path);
//        try{
//            dp.Init();
//        } catch (EngineException e) {
//            e.printStackTrace();
//        }
//        Location l1 = new Location();
//        l1.len = 2;
//        l1.offset = 3;
//        l1.file_no = 1;
//        dp.AddOrUpdate("sss".getBytes(), l1);
//
//        Location ll1 = dp.Find("sss".getBytes());
//        System.out.println(ll1.offset);
//
//        Location l2 = new Location();
//        l2.len = 2;
//        l2.offset = 13;
//        l2.file_no = 1231;
//        dp.AddOrUpdate("sse".getBytes(), l2);
//
//        Location ll2 = dp.Find("sse".getBytes());
//        System.out.println(ll2.file_no);
        byte[] key = {1,2,3,4};
        byte[] value = {78,90};
        EngineRace race = new EngineRace();
        try{
            race.open(dir_path);

            race.write(key, value);
            byte[] result = race.read(key);
            for (byte b : result) {
                System.out.println(b);
            }
        }catch (EngineException e) {
            e.printStackTrace();
        }


    }
}
