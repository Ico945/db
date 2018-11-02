package com.alibabacloud.polar_race.engine.common;


public class Test {
    public static void main(String[] args) {
        Data_store data_store = new Data_store("D:/test_db/");

        Location location_read = new Location(1, 13, 4);

        byte[] value = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Location location = data_store.Append(value);

//        byte[] result = data_store.Read(location_read);
//        for (byte b : result)
//            System.out.println(b);
        System.out.println(location.file_no + "," + location.offset + "," + location.len);
    }
}
