package com.alibabacloud.polar_race.engine.common;

public class Tool {
    static final String DataFilePrefix = "DATA_";
    static final int DataFilePrefixLen = 5;
    static final int SingleFileSize = 1024 * 1024 * 100;

    static final int ItemLength = 24;  // 28 bytes
    static final int DoorPlateMapCount = 1024 * 1024;
    static final int DoorPlateMapSize = DoorPlateMapCount * ItemLength; // doorplate内部的map大小
    static final String DoorPlateFile = "SSSS";

    static final int LocationLength = 3 * 4;
    static String FileName(String dir, long file_no) {
        return dir + "/" + DataFilePrefix + file_no;
    }


}
