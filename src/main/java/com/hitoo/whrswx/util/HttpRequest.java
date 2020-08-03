package com.hitoo.whrswx.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：zhangguangyan
 * @date：Created in 2020/5/13 10:03
 * @description：    http第三方请求
 * @ClassName:HttpRequest
 * @modified By：
 * @version:
 */
public class HttpRequest {
    static String realUrl;
    static String postParam;

    /**
     * @param url 发送请求的URL
     * @param param 请求参数为hashmap
     * @return
     */
    public static String sendGet(String url, HashMap<String,String> param){
        realUrl= url + "?";
        /*param.forEach((k,v)->{
            realUrl+=k+"="+v+"&";
        });*/

      return  sendGet(realUrl);
    }

    /**
     * 向指定URL发送GET方法的请求
     * @param url：发送请求的URL
     * @return String[result]：所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url ;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept-encoding","gzip, deflate, br");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("contentType", "UTF-8");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            connection.setRequestProperty("accept", "application/json, text/plain,*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url：发送请求的 URL，参数为url?a=xxx&b=xxx
     * @return String[result]：所代表远程资源的响应结果
     */
    public static String sendPost(String url) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);

            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 发送请求参数
            out.print(postParam);

            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }

        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    //后台请求网页内容，解决跨域问题
    public  static  HashMap<String,Object>  getWebText(String url) throws Exception {
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
                System.out.println(html);
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
                                                    /*col: 1
                                                    appid: 1
                                                    webid: 82
                                                    path: /
                                                    columnid: 46521
                                                    sourceContentType: 1
                                                    unitid: 100041
                                                    webname: 威海市人力资源和社会保障局
                                                    permissiontype: 0*/
        //String s=sendPost("http://rsj.weihai.gov.cn/module/web/jpage/dataproxy.jsp?startrecord=1&endrecord=45&perpage=15&webid=82&appid=1&columnid=46521&sourceContentType=1&unitid=100041&permissiontype=0");
        String s=sendPost("http://rsj.weihai.gov.cn/art/2020/7/23/art_46521_2359410.html");
        System.out.println(s);
    }

}
