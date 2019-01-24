package com.springboot.utils.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 服务器基本信息
 * @author: chenmingjian
 * @date: 18-11-24 12:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerDescription {

    /**
     * 服务器名
     */
    private String serverName;

    /**
     * url
     */
    private String url;

    /**
     * 端口
     */
    private int port;

    /**
     * ip
     */
    private String ip;

    /**
     * 方法名
     */
    private String mothedName;

    /**
     * 请求参数
     */
    private String requestParam;

}
