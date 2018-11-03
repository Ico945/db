package com.alibabacloud.polar_race.engine.common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Data_store {
    private File dir;
    private List<File> files;
    private Location last_file_location = new Location();
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // 初始化，判断文件夹是否存在
    public Data_store(String dir_) {
        this.dir = new File(dir_);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
            System.out.println("文件夹新建成功");
        }
        File[] files1 = dir.listFiles();
        files = new ArrayList<File>();
        for (File f : files1)
            files.add(f);

        // 得到最后数据文件的no
        if (files.size() > 0) {
            String sindex = files.get(files.size()-1).getName().substring(Tool.DataFilePrefixLen);
            last_file_location.file_no = Integer.parseInt(sindex);
        }
    }

    public Location Append(byte[] value, Location location) {
        Location result = new Location();

        readWriteLock.writeLock().lock();
        File last_file;

        // 添加文件的情况
        if (location.file_no == 0) {
            // 需要新建文件
            int file_count;
            if ((file_count = files.size())==0) {
                location.file_no++;
            } else if (files.get(file_count-1).length()+value.length>Tool.SingleFileSize) {
                location.file_no = file_count+1;
            } else {
                location.file_no = file_count;
                location.offset = (int)files.get(file_count-1).length();
            }
            last_file = new File(dir + "/" + Tool.DataFilePrefix + location.file_no);
            if (!files.contains(last_file))
                files.add(last_file);

            result.offset = 0;
            result.len = value.length;
            result.file_no = location.file_no;
        } else {
            // 修改
            last_file = new File(dir + "/" + Tool.DataFilePrefix + location.file_no);
            result = location;
        }

        // 写文件
        try {
            RandomAccessFile writer = new RandomAccessFile(last_file, "rw");
            writer.seek(location.offset);
            writer.write(value);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return result;
    }

    public byte[] Read(Location location) {
        readWriteLock.readLock().lock();
        String file_name = Tool.DataFilePrefix + location.file_no;
        byte[] result = new byte[location.len];

        try {
            File file = new File(dir + "/" + file_name);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(location.offset);
            raf.read(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
        return result;
    }
}
