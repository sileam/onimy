package cn.huanzi.qch.baseadmin.util;

import cn.huanzi.qch.baseadmin.sys.chat.vo.WhatsAppChatsVO;
import cn.huanzi.qch.baseadmin.sys.chat.vo.WhatsAppMessageVO;
import cn.huanzi.qch.baseadmin.sys.chat.vo.WhatsAppUserVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class WhatsAppUtil {
    private final static String baseUrl = "https://4d6e0c494d53.inbox.platform.get.chat/api/v1/";
    private final static String TOKEN = " Token c91c67ed1952e92a751ca3cf4fd476cab6a55a35";

    public static String getToken(String url, String userName, String password) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", userName);
        jsonObject.put("password", password);
        String resultPost = postMsg(url, null, jsonObject.toString());
        log.info("post返回结果：{}", resultPost);

        return JSONObject.parseObject(resultPost).get("token").toString();
    }

    //  
    public static JSONObject createTemplateByName(String namespace, String name) throws IOException
    {
        JSONObject template = new JSONObject();
        template.put("namespace", namespace);
        template.put("name", name);
        return template;
    }

    public static JSONObject createTemplateByName(String namespace, String name, JSONObject[] components) throws IOException
    {
        JSONObject template = createTemplateByName(namespace, name);
        template.put("component", components);
        return template

    }

    public static void retrieveMediaFile(String id) throws IOException
    {


    }

    public static 

    // 代码创建模板
    // public static JSONObject requireCreateTemplate(String name, String category, String language, JSONObject[] components)
    // {
    //     return 
    // }

    /**
     * 发起post请求 没有任何body参数的情况
     *
     * @param url  请求的目标url地址
     * @param data 请求数据
     * @return 将响应结果转换成string返回
     * @throws IOException 可能出现的异常
     */
    public static String postMsg(String url, String token, String data) throws IOException {
        // 根据url地址发起post请求
        HttpPost httppost = new HttpPost(url);

        StringEntity stEntity;

        // 获取到httpclient客户端
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 设置请求的一些头部信息
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("Accept", "application/json");
            if (!Strings.isEmpty(token)) {
                httppost.addHeader("Authorization", token);
            }
            stEntity = new StringEntity(data, "UTF-8");
            httppost.setEntity(stEntity);
            // 设置请求的一些配置设置，主要设置请求超时，连接超时等参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000).setConnectionRequestTimeout(1000).setSocketTimeout(5000)
                    .build();
            httppost.setConfig(requestConfig);
            // 执行请求
            CloseableHttpResponse response = httpclient.execute(httppost);
            // 请求结果
            String resultString = "";
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
                log.info("请求状态：{}", response.getStatusLine().getStatusCode());
                // 获取请求响应结果
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 将响应内容转换为指定编码的字符串
                    resultString = EntityUtils.toString(entity, "UTF-8");
                    log.info("Response content:{}", resultString);
                    return resultString;
                }
            } else {
                log.info("请求失败！");
                return resultString;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            httpclient.close();
        }
        return null;
    }

    /**
     * body有内容的post请求方法 controller可以用bean直接接收参数
     *
     * @param url    目标地址
     * @param params 封装的参数
     * @return 将响应结果转换成string返回
     * @throws Exception 可能出现的异常
     */
    synchronized static String postData(String url, Map<String, String> params) throws Exception {
        HttpClient httpClient = new HttpClient();
        // 请求相关超时时间设置
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(10 * 1000);

        PostMethod method = new PostMethod(url);
        if (params != null) {
            // 内容编码设置
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, StandardCharsets.UTF_8);
            // 请求体信息设置
            method.setRequestBody(assembleRequestParams(params));
            // 请求头设置
            method.setRequestHeader("Content-Type", "test");
        }

        // 响应结果信息处理
        String result = "";
        try {
            httpClient.executeMethod(method);
            result = new String(method.getResponseBody(), StandardCharsets.UTF_8);
        } finally {
            // 释放连接
            method.releaseConnection();
        }
        return result;
    }

    /**
     * 组装http请求参数
     *
     * @param data 键值对
     * @return 返回一个名称-值的键值对
     */
    private synchronized static NameValuePair[] assembleRequestParams(Map<String, String> data) {
        List<NameValuePair> nameValueList = new ArrayList<>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            nameValueList.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        log.info("键值对参数：{}", ArrayUtils.toString(nameValueList.toArray(new NameValuePair[nameValueList.size()])));

        return nameValueList.toArray(new NameValuePair[nameValueList.size()]);
    }

    /**
     * get请求
     *
     * @param url 目标请求地址
     * @return 将响应结果转换成string返回
     */
    static String get(String url, String token) {
        String result = "";
        try {
            // 根据地址获取请求
            HttpGet request = new HttpGet(url);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Accept", "application/json");
            request.addHeader("Authorization", token);

            // 获取当前客户端对
            CloseableHttpClient httpclient = HttpClients.createDefault();
            // 通过请求对象获取响应对象
            HttpResponse response = httpclient.execute(request);
            // 判断请求结果状态码
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getResult(String key) {
        String url = baseUrl + key;

        // 发起get请求
        String result = get(url, TOKEN);
        log.info("get返回结果：{}", result);

        return result;
    }

    public static String post(String key, String value) throws IOException {
        return postMsg(baseUrl + key, TOKEN, value);
    }

    public static List<WhatsAppUserVO> getPersons() {
        List<WhatsAppUserVO> list = new ArrayList<>();

        String result = getResult("persons");
        JSONObject jsonObject = JSON.parseObject(result);
        int count = jsonObject.getInteger("count");
        if (count > 0) {
            JSONArray results = jsonObject.getJSONArray("results");
            for (Object o : results) {
                WhatsAppUserVO vo = new WhatsAppUserVO();
                JSONObject json = JSON.parseObject(o.toString());

                vo.setInitials(json.getString("initials"));
                vo.setWaId(json.getString("wa_id"));
                vo.setLastMessageTimestamp(json.getLong("last_message_timestamp"));
                vo.setName(json.getJSONObject("waba_payload").getJSONObject("profile").getString("name"));

                list.add(vo);
            }
        }

        return list;
    }

    public static List<WhatsAppChatsVO> getChats() {
        List<WhatsAppChatsVO> list = new ArrayList<>();

        String result = getResult("chats");
        JSONObject jsonObject = JSON.parseObject(result);
        int count = jsonObject.getInteger("count");
        if (count > 0) {
            JSONArray results = jsonObject.getJSONArray("results");
            for (Object o : results) {
                WhatsAppChatsVO vo = new WhatsAppChatsVO();
                JSONObject json = JSON.parseObject(o.toString());

                WhatsAppUserVO contact = new WhatsAppUserVO();
                JSONObject contactJson = json.getJSONObject("contact");
                contact.setWaId(contactJson.getString("wa_id"));
                contact.setName(contactJson.getJSONObject("waba_payload").getJSONObject("profile").getString("name"));
                contact.setLastMessageTimestamp(contactJson.getLong("last_message_timestamp"));
                contact.setInitials(contactJson.getString("initials"));
                vo.setContact(contact);
                vo.setNewMessage(json.getInteger("new_messages"));
                vo.setWaId(json.getString("wa_id"));
                vo.setLastMessage(praseVO(json.getJSONObject("last_message")));

                list.add(vo);
            }
        }

        return list;
    }

    public static List<WhatsAppMessageVO> getMessages(String waId, int limit, Integer offset) {
        List<WhatsAppMessageVO> list = new ArrayList<>();

        if (Strings.isEmpty(waId)) {
            waId = "48123123123";
        }

        String key = "messages/?wa_id=" + waId + "&limit=" + limit;
        if (null != offset) {
            key = key + "&offset=" + offset;
        }

        JSONObject jsonObject = JSON.parseObject(getResult(key));
        if (Objects.isNull(jsonObject)) {
            return list;
        }
        int count = jsonObject.getInteger("count");
        if (count > 0) {
            JSONArray results = jsonObject.getJSONArray("results");
            for (Object o : results) {
                list.add(praseVO(JSON.parseObject(o.toString())));
            }
        }
        Collections.sort(list);

        return list;
    }

    public static String sendMessage(String waId, String type, String text) throws IOException {
        JSONObject message = new JSONObject();

        message.put("wa_id", waId);
        message.put("type", type);
        message.put("verify_contact", "false");

        JSONObject value = new JSONObject();
        value.put("body", text);
        message.put("text", value);

        return post("messages/", message.toString());
    }

    public static String createTemplates(String name, String category, String language, String text) throws IOException {
        JSONObject template = new JSONObject();

        template.put("name", name);
        template.put("category", category);
        template.put("language", language);

        JSONArray components = new JSONArray();
        JSONObject value = new JSONObject();
        value.put("type", "BODY");
        value.put("text", text);
        components.add(value);
        template.put("components", components);

        return post("templates/", template.toString());
    }

    public static WhatsAppMessageVO praseVO(JSONObject lastMessage) {
        if (Objects.isNull(lastMessage)) {
            return null;
        }
        WhatsAppMessageVO lastMessageVo = new WhatsAppMessageVO();
        if (lastMessage.containsKey("id")) {
            lastMessageVo.setId(lastMessage.getString("id"));
        }
        lastMessageVo.setWabaPayloadFrom(lastMessage.getJSONObject("waba_payload").getString("from"));
        if (Strings.isEmpty(lastMessageVo.getWabaPayloadFrom())) {
            lastMessageVo.setWabaPayloadClass("self");
        } else {
            lastMessageVo.setWabaPayloadClass("other");
        }
        lastMessageVo.setWabaPayloadId(lastMessage.getJSONObject("waba_payload").getString("id"));
        if (lastMessage.getJSONObject("waba_payload").containsKey("text")) {
            lastMessageVo.setWabaPayloadText(lastMessage.getJSONObject("waba_payload").getJSONObject("text").getString("body"));
        } else if (lastMessage.getJSONObject("waba_payload").containsKey("image")) {
            lastMessageVo.setWabaPayloadText(lastMessage.getJSONObject("waba_payload").getJSONObject("image").getString("link"));
        }
        lastMessageVo.setWabaPayloadTimestamp(lastMessage.getJSONObject("waba_payload").getLong("timestamp"));
        lastMessageVo.setWabaPayloadType(lastMessage.getJSONObject("waba_payload").getString("type"));
        lastMessageVo.setWabaStatusesSent(lastMessage.getJSONObject("waba_statuses").getString("sent"));
        lastMessageVo.setWabaStatusesDelivered(lastMessage.getJSONObject("waba_statuses").getString("delivered"));
        lastMessageVo.setWabaStatusesRead(lastMessage.getJSONObject("waba_statuses").getString("read"));
        lastMessageVo.setContactWaId(lastMessage.getJSONObject("contact").getString("wa_id"));
        lastMessageVo.setContactWabaPayloadProfileName(lastMessage.getJSONObject("contact").getJSONObject("waba_payload").getJSONObject("profile").getString("name"));
        lastMessageVo.setContactWabaPayloadWaId(lastMessage.getJSONObject("contact").getJSONObject("waba_payload").getString("wa_id"));
        lastMessageVo.setContactInitials(lastMessage.getJSONObject("contact").getString("initials"));
        lastMessageVo.setContactLastMessageTimestamp(lastMessage.getJSONObject("contact").getString("last_message_timestamp"));
        lastMessageVo.setFromUs(lastMessage.getBoolean("from_us"));
        lastMessageVo.setReceived(lastMessage.getBoolean("received"));
        lastMessageVo.setSender(lastMessage.getString("sender"));
        lastMessageVo.setContext(lastMessage.getString("context"));
        lastMessageVo.setCustomerWaId(lastMessage.getString("customer_wa_id"));

        return lastMessageVo;
    }

    public static void main(String[] args) throws Exception {
//        getResult("persons");
//        getPersons();
//        getResult("chats");
        getChats();
//        getResult("templates/");
//        createTemplates("test_zh", "MARKETING","en", "test");
//        getMessages("855189034371", 20, null);
//        sendMessage("855189034371", "text", "Hello World!");

//        System.out.println(getToken("https://4d6e0c494d53.inbox.platform.get.chat/api/v1/auth/token/", "timi", "am112211"));
    }
}
