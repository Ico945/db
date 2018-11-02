package com.alibabacloud.polar_race.engine.common;

public class Tool {
    static final String DataFilePrefix = "DATA_";
    static final int DataFilePrefixLen = 5;
    static final int SingleFileSize = 1024*1024*100;

    static String FileName(String dir, long file_no) {
        return dir + "/" + DataFilePrefix + file_no;
    }
}
