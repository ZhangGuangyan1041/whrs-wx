package com.hitoo.whrswx.controller;

import com.hitoo.whrswx.service.ParseUrlService;
import com.hitoo.whrswx.util.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public Object parseUrl(@RequestParam("url") String url) throws Exception{
        return parseUrlService.getData(url);
    }

    @PostMapping("/getWebText")
    public Object getWebText(@RequestParam("url") String url) throws Exception {
        return parseUrlService.getWebText(url);
    }
    @GetMapping("/")
    public Object index()throws  Exception{
        return "空白页";
    }
}
