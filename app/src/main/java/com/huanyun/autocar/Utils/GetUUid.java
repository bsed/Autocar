package com.huanyun.autocar.Utils;

import java.util.UUID;

/**
 * Created by admin on 2016/1/14.
 */
public class GetUUid {

    /**
     * 随机生成UUID
     * @return
     */
    public static synchronized String getUUID(){
        UUID uuid= UUID.randomUUID();
        String str = uuid.toString();
//        String uuidStr=str.replace("-", "");
        return str;
    }
}
