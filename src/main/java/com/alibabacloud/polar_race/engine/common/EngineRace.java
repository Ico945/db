package com.alibabacloud.polar_race.engine.common;

import com.alibabacloud.polar_race.engine.common.AbstractEngine;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.nio.channels.FileLock;

public class EngineRace extends AbstractEngine {
	FileLock lock = null;
    private Data_store store = null;
	private DoorPlate plate = null;
	@Override
	public void open(String path) throws EngineException {
		store = new Data_store(path);
		plate = new DoorPlate(path);
		plate.Init();   // 打开mmap
	}

	@Override
	public void write(byte[] key, byte[] value) throws EngineException {
		// 需要锁
        Location location = plate.Find(key);
		location = store.Append(value, location);
		plate.AddOrUpdate(key, location);
	}

	@Override
	public byte[] read(byte[] key) throws EngineException {
		byte[] value = null;
		Location location = plate.Find(key);
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
