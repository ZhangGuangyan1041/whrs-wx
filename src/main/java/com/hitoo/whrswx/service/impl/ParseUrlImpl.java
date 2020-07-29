package com.hitoo.whrswx.service.impl;

import com.hitoo.whrswx.controller.FunctionController;
import com.hitoo.whrswx.service.ParseUrlService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author：zhangguangyan
 * @date：Created in 2020/7/24 9:21
 * @description：解析xml
 * @ClassName:ParseUrlImpl
 * @modified By：
 * @version:
 */
@Service
public class ParseUrlImpl implements ParseUrlService {

    @Override
    public Map<String, Object> getData(String urlStr) throws Exception {
         URL url=new URL(urlStr);
        //URL url = new URL("http://rsj.weihai.gov.cn/module/web/jpage/dataproxy.jsp?startrecord=1&endrecord=45&perpage=15&webid=82&appid=1&columnid=46521&sourceContentType=1&unitid=100041&permissiontype=0");
        return bar(parse(url));
    }

    /**
     * @param url
     * @return
     * @throws DocumentException
     * @Description 通过地址url获取xml对象
     */
    private Document parse(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
    }

    /**
     * @param document
     * @return
     * @throws DocumentException
     * @Description 解析xml文本
     */
    private Map<String, Object> bar(Document document) throws DocumentException {
        Map<String, Object> map = new HashMap<>();
        Element root = document.getRootElement();
        map.put("totalrecord", root.element("totalrecord").getText());
        map.put("totalpage", root.element("totalpage").getText());

        Element recordset = root.element("recordset");
        List<String> list = new ArrayList<>();
        // iterate through child elements of root
        for (Iterator<Element> it = recordset.elementIterator("record"); it.hasNext(); ) {
            Element element = it.next();
            String result = element.getText();
            result = result.replace("href='", "href='http://rsj.weihai.gov.cn/");
            list.add(result);
           /// System.out.println(result);
        }
        map.put("recordset", list);
        return map;
    }
    //后台请求网页内容，解决跨域问题
    @Override
    public   HashMap<String,Object>  getWebText(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HashMap<String,Object> map=new HashMap<>();

        //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet(url);
        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);
            //logger.info("getWebText.ht：status="+response.getStatusLine().getStatusCode());
            map.put("status",response.getStatusLine().getStatusCode());
            //4.判断响应状态为200，进行处理
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                map.put("data",html);
                //System.out.println(html);
            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                String error=EntityUtils.toString(response.getEntity(), "utf-8");
                map.put("data",error);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return map;
    }
    public static void main(String[] args) {
//        FunctionController func=new FunctionController();
//        Document document=null;
//        try {
//            URL url=new URL("http://rsj.weihai.gov.cn/module/web/jpage/dataproxy.jsp?startrecord=1&endrecord=45&perpage=15&webid=82&appid=1&columnid=46521&sourceContentType=1&unitid=100041&permissiontype=0");
//            document=func.parse(url);
//            func.bar(document);
//        }catch (Exception e){
//
//        }

    }
}
