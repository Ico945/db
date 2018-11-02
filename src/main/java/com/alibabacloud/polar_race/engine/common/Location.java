package com.alibabacloud.polar_race.engine.common;

public class Location {
    int file_no = 0;
    int offset = 0;
    int len = 0;

    public Location() {}

    public Location(int file_no, int offset, int len) {
        this.file_no = file_no;
        this.offset = offset;
        this.len = len;
    }
}
