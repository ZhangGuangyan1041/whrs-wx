package com.hitoo.whrswx.controller;

import com.hitoo.whrswx.service.ParseUrlService;
import com.hitoo.whrswx.util.HttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author：zhangguangyan
 * @date：Created in 2020/7/23 16:44
 * @description：功能控制
 * @ClassName:FunctionController
 * @modified By：
 * @version: 1.0.0
 */
@RestController
public class FunctionController {
    @Resource
    ParseUrlService parseUrlService;

    @PostMapping("/parseUrl")
    @CrossOrigin
    public Object parseUrl(String url) throws Exception{
        return parseUrlService.getData(url);
    }

    @PostMapping("/getWebText")
    @CrossOrigin
    public Object getWebText(String url) throws Exception {
        return parseUrlService.getWebText(url);
    }
}
