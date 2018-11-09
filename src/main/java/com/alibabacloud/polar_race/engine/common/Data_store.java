package com.alibabacloud.polar_race.engine.common;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Data_store {
    private String path;
    private File dir_files;
    private List<File> files;
    private File last_file;
    private Location last_file_location = new Location();

    // 初始化，判断文件夹是否存在
    public Data_store(String path) {
        this.path = path + "/DATA";
    }

    public void Init() throws EngineException {
        this.dir_files = new File(path);
        if (!dir_files.exists() || !dir_files.isDirectory()) {
            dir_files.mkdirs();
        }
        File[] files1 = dir_files.listFiles();
        files = new ArrayList<File>();
        for (File f : files1) {
            files.add(f);
        }

        // 得到最后数据文件的no
        if (files.size() > 0) {
            String sindex = files.get(files.size()-1).getName().substring(Tool.DataFilePrefixLen);
            last_file_location.file_no = Integer.parseInt(sindex);
        }
    }

    public Location Append(byte[] value) {
        Location result = new Location();

        // 添加文件的情况
        int file_count;
        if ((file_count = files.size())==0) {
            result.file_no = 1;
            result.offset = 0;
            last_file = new File(path + "/" + Tool.DataFilePrefix + result.file_no);
            files.add(last_file);
        } else if (files.get(file_count-1).length()+value.length>Tool.SingleFileSize) {
            result.file_no = file_count+1;
            result.offset = 0;
            last_file = new File(path + "/" + Tool.DataFilePrefix + result.file_no);
            files.add(last_file);
        } else {
            result.file_no = file_count;
            result.offset = (int)files.get(file_count-1).length();
            last_file = files.get(result.file_no-1);
        }
        // 默认大小
//        result.len = value.length;

        // 写文件
        try {
            RandomAccessFile writer = new RandomAccessFile(last_file, "rw");
            writer.seek(result.offset);
            writer.write(value);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(result);
        return result;
    }

    public byte[] Read(Location location){
//        String file_name = Tool.DataFilePrefix + location.file_no;
//        byte[] result = new byte[location.len];
//
//        try {
//            File file = new File(path + "/" + file_name);
//            RandomAccessFile raf = new RandomAccessFile(file, "r");
//            raf.seek(location.offset);
//            raf.read(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(result[0] + ";" + result[1] + ";" + result[2]);

        // 从List中读
        byte[] result = new byte[location.len];
        File file = files.get(location.file_no-1);
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(location.offset);
            raf.read(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}