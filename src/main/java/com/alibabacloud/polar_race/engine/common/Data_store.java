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

    public Location Append(byte[] value) {
        readWriteLock.writeLock().lock();

        Location result = new Location();
        // 判断文件大小,放不下的话新建一个文件
        if (files.size() == 0 || files.get(files.size()-1).length()+value.length > Tool.SingleFileSize) {
            last_file_location.file_no++;
            File newfile = new File(dir + "/" + Tool.DataFilePrefix + last_file_location.file_no);
            files.add(newfile);
        }
        File last_file = files.get(files.size()-1);
        result.offset = (int)last_file.length();
        result.len = value.length;
        result.file_no = last_file_location.file_no;
        // 写文件
        try {
            FileOutputStream writer = new FileOutputStream(last_file, true);
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
        byte[] result = new byte[location.len];
        byte[] bytes = new byte[Tool.SingleFileSize];
        String file_name = Tool.DataFilePrefix + location.file_no;
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(new File(dir+"//"+file_name)));
            in.read(bytes);
            for (int i=0; i<location.len; i++)
                result[i] = bytes[location.offset+i];
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
        return result;
    }
}
