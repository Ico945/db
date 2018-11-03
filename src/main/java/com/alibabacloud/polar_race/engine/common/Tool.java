package com.alibabacloud.polar_race.engine.common;

public class Tool {
    static final String DataFilePrefix = "DATA_";
    static final int DataFilePrefixLen = 5;
    static final int SingleFileSize = 1024 * 1024 * 100;

    static final int DoorPlateMapSize = 1024 * 1024 * 32; // doorplate内部的map大小
    static final String DoorPlateFile = "SSSS";
    static final int ItemLength = 4 * 4 + 1;
    static final int LocationLength = 3 * 4;
    static String FileName(String dir, long file_no) {
        return dir + "/" + DataFilePrefix + file_no;
    }


}
