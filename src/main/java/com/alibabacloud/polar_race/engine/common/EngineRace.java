package com.alibabacloud.polar_race.engine.common;

import com.alibabacloud.polar_race.engine.common.AbstractEngine;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class EngineRace extends AbstractEngine {
	private Data_store store = null;
	private DoorPlate plate;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock r = lock.readLock();
    private Lock w = lock.writeLock();

	@Override
	public void open(String path) throws EngineException {
		store = new Data_store(path);
		store.Init();
		plate = new DoorPlate(path);
		plate.Init();   // 打开mmap
	}

	@Override
	public void write(byte[] key, byte[] value) throws EngineException {
	    w.lock();
		Location location = store.Append(value);
//        System.out.println(location);
        int index_plate = Tool.Hash(key, Tool.plate_num);
		plate.AddOrUpdate(key, location, index_plate);
		w.unlock();
	}

	@Override
	public byte[] read(byte[] key) throws EngineException {
		byte[] value;
        int index_plate = Tool.Hash(key, Tool.plate_num);
		Location location = plate.Find(key, index_plate);
		if (location.file_no==0)
			throw new EngineException(RetCodeEnum.NOT_FOUND, "not found");
//		System.out.println(location);
		value = store.Read(location);
		return value;
	}

	@Override
	public void range(byte[] lower, byte[] upper, AbstractVisitor visitor) throws EngineException {
	}

	@Override
	public void close() {
	}

}