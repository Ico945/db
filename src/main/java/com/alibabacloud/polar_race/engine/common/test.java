package com.alibabacloud.polar_race.engine.common;


import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

public class Test {

    public static void main(String[] args) {
        EngineRace engineRace = new EngineRace();
        try {
            engineRace.open("D:/test_db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] key = {2, 2, 2, 2, 2, 2, 2, 2};
        try {
//            engineRace.write(key, key);
            byte[] result = engineRace.read(key);
            for (byte b : result)
                System.out.print(b);
            System.out.println("\n" + result.length);
        } catch (EngineException e){}

    }
}