package com.alibabacloud.polar_race.engine.common;

public class Tool {
    static final String DataFilePrefix = "DATA_";
    static final int DataFilePrefixLen = 5;
    static final int SingleFileSize = 1024 * 1024 * 100;

    static final int ItemLength = 24;  // 28 bytes
    static final int DoorPlateMapCount = 1024 * 1024;
    static final int DoorPlateMapSize = DoorPlateMapCount * ItemLength; // doorplate内部的map大小
    static final String DoorPlateFile = "META";

    static final int plate_num = 8;

    static final int LocationLength = 3 * 4;
    static String FileName(String dir, long file_no) {
        return dir + "/" + DataFilePrefix + file_no;
    }

    static int Hash(byte[] key, int remainder) {
        int hash = 0;
        for (int i=0; i<key.length; i++) {
            hash += (key[i]+i) * 31;
        }
        return hash % remainder;
    }
}
