package com.hitoo.whrswx.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: whrs-wx
 * @description: 通过url解析返回的xml文件
 * @author: 张广岩
 * @create: 2020-07-24 09:20
 **/
@Service
public interface ParseUrlService {
    Map<String,Object> getData(String url) throws Exception;
    HashMap<String,Object> getWebText(String url) throws Exception;
}
