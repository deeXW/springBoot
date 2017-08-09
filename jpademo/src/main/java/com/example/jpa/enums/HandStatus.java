package com.example.jpa.enums;

import java.io.Serializable;

/**
 * Created by chenzhengwei on 2017/4/23.
 * 调用状态
 */
public enum HandStatus implements Serializable {
    /**
     *
     */

    SUCCESS, FAILURE, UNCONNECT,ARTIFICIAL,CLOSE//人工
}
