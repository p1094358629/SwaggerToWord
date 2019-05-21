package com.word.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.word.dto.*;
import com.word.service.WordService;
import com.word.utils.StringCompareUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/1/12.
 */
@Service
public class WordServiceImpl implements WordService {

    private static RestTemplate restTemplate;
    @Value("${swaggerUrl}")
    private String swaggerUrl;

    static {
        //解决中文乱码
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
    }

    @Override
    public List<Table> tableList() {

        Map<String, Object> map = getStringObjectMap();

        List<Table> list = new LinkedList();
        //得到host，并添加上http 或 https
        String host = StringUtils.substringBefore(swaggerUrl, ":") + String.valueOf(map.get("host"));
        //解析paths
        LinkedHashMap<String, LinkedHashMap> paths = (LinkedHashMap) map.get("paths");

        if (paths != null) {
            Iterator<Map.Entry<String, LinkedHashMap>> it = paths.entrySet().iterator();
            while (it.hasNext()) {
                Table table = new Table();
                List<Request> requestList = new LinkedList<>();
                List<Response> responseList = new LinkedList<>();
                // 请求参数格式，类似于 multipart/form-data
                String requestForm = "";
                // 请求参数格式，类似于 multipart/form-data
                String responseForm = "";
                // 请求方式，类似为 get,post,delete,put 这样
                String requestType = "";
                String url; // 请求路径
                String title; // 大标题（类说明）
                String tag; // 小标题 （方法说明）
                String description; //接口描述

                Map.Entry<String, LinkedHashMap> path = it.next();
                url = path.getKey();

                LinkedHashMap<String, LinkedHashMap> value = path.getValue();
                Set<String> requestTypes = value.keySet();
                for (String str : requestTypes) {
                    requestType += str + ",";
                }

                Iterator<Map.Entry<String, LinkedHashMap>> it2 = value.entrySet().iterator();
                // 不管有几种请求方式，都只解析第一种
                Map.Entry<String, LinkedHashMap> firstRequestType = it2.next();
                LinkedHashMap content = firstRequestType.getValue();
                title = String.valueOf(((List) content.get("tags")).get(0));
                description = String.valueOf(content.get("description"));
                //请求数据类型
                List<String> consumes = (List) content.get("consumes");
                if (consumes != null && consumes.size() > 0) {
                    for (String consume : consumes) {
                        requestForm += consume + ",";
                    }
                }
                List<String> produces = (List) content.get("produces");
                if (produces != null && produces.size() > 0) {
                    for (String produce : produces) {
                        responseForm += produce + ",";
                    }
                }
                //功能说明
                tag = String.valueOf(content.get("summary"));
                //请求体
                List parameters = (ArrayList) content.get("parameters");
                if (parameters != null && parameters.size() > 0) {
                    for (int i = 0; i < parameters.size(); i++) {
                        Request request = new Request();
                        LinkedHashMap<String, Object> param = (LinkedHashMap) parameters.get(i);
                        request.setName(String.valueOf(param.get("name")));
                        //如果IBean有值说明数据类型为对象
                        request.setIBean(param.get("schema") == null ? null : getBeanFromInput(String.valueOf(param.get("schema"))));
                        //数据类型,如果type为空则存为对象名
                        request.setType(param.get("type") == null ? getBeanFromInput(String.valueOf(param.get("schema"))) : param.get("type").toString());
                        request.setRequire((Boolean) param.get("required"));
                        request.setRemark(String.valueOf(param.get("description")));
                        requestList.add(request);
                    }
                }
                //返回体
                LinkedHashMap<String, Object> responses = (LinkedHashMap) content.get("responses");
                Iterator<Map.Entry<String, Object>> it3 = responses.entrySet().iterator();
                while (it3.hasNext()) {
                    Response response = new Response();
                    Map.Entry<String, Object> entry = it3.next();
                    // 状态码 200 201 401 403 404 这样
                    String statusCode = entry.getKey();
                    LinkedHashMap<String, Object> statusCodeInfo = (LinkedHashMap) entry.getValue();
                    String statusDescription = (String) statusCodeInfo.get("description");
                    response.setName(statusCode);
                    response.setDescription(statusDescription);
                    response.setRemark(statusCodeInfo.get("schema") == null ? null : getBeanFromInput(String.valueOf(statusCodeInfo.get("schema"))));
                    responseList.add(response);
                }
                //封装Table
                table.setTitle(title);
                table.setUrl(url);
                table.setTag(tag);
                table.setDescription(description);
                table.setRequestForm(StringUtils.removeEnd(requestForm, ","));
                table.setResponseForm(StringUtils.removeEnd(responseForm, ","));
                table.setRequestType(StringUtils.removeEnd(requestType, ","));
                table.setRequestList(requestList);
                table.setResponseList(responseList);
                list.add(table);
            }
        }
        return list;
    }

    @Override
    public List<DefinitionsBean> definitionsBean() {
        Map<String, Object> map = getStringObjectMap();
        //解析definitions
        LinkedHashMap<String, LinkedHashMap> definitions = (LinkedHashMap) map.get("definitions");
        List<DefinitionsBean> definitionsBeans = new LinkedList<>();
        if (definitions != null) {
            Iterator<Map.Entry<String, LinkedHashMap>> it = definitions.entrySet().iterator();
            while (it.hasNext()) {
                DefinitionsBean definitionsBean = new DefinitionsBean();
                Map.Entry<String, LinkedHashMap> definition = it.next();
                List<String> requires = (List<String>) definition.getValue().get("required");
                //对象属性
                definitionsBean.setName(definition.getKey());
                List<BeanProp> beanProps = new ArrayList<>();
                LinkedHashMap<String, LinkedHashMap> properties = (LinkedHashMap<String, LinkedHashMap>) definition.getValue().get("properties");
                Iterator<Map.Entry<String, LinkedHashMap>> i = properties.entrySet().iterator();
                if (properties != null) {
                    while (i.hasNext()) {
                        Map.Entry<String, LinkedHashMap> next = i.next();
                        BeanProp beanProp = new BeanProp();
                        beanProp.setName(next.getKey());
                        beanProp.setDescription((String) next.getValue().get("description"));
                        //如果type为null/array,则可能是对象
                        String  type = (String) next.getValue().get("type");
                        if (type==null){
                            type = getBeanFromInput((String) next.getValue().get("$ref"));
                        }
                        beanProp.setType( type);
                        beanProp.setIBean(next.getValue().get("$ref")==null?null:type);
                        beanProp.setRequired(StringCompareUtil.isInContain(next.getKey(), requires));
                        beanProps.add(beanProp);
                    }
                }
                definitionsBean.setBeanProps(beanProps);
                definitionsBeans.add(definitionsBean);
            }
        }
        return definitionsBeans;
    }

    private Map<String, Object> getStringObjectMap() {
        String json = restTemplate.getForObject(swaggerUrl, String.class);

        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        try {
            // convert JSON string to Map
            map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (Exception e) {
            LoggerFactory.getLogger(WordService.class).error("parse error", e);
        }
        return map;
    }
    /**
     * 从schema中获取对象
     *
     * @param schema
     * @return
     */
    private String getBeanFromInput(String schema) {
        /**
         *
         *             "$ref": "#/definitions/ListMyWebMessageReq"
         *
         */
        if (schema == null) {
            return null;
        }
        String beanString = schema.substring(schema.lastIndexOf("/") + 1).replace("}", "");
        return beanString;
    }

    /**
     * 重新构建url
     *
     * @param url
     * @param requestList
     * @return etc:http://localhost:8080/rest/delete?uuid={uuid}
     */
    private String buildUrl(String url, List<Request> requestList) {
        String param = "";
        if (requestList != null && requestList.size() > 0) {
            for (Request request : requestList) {
                String name = request.getName();
                param += name + "={" + name + "}&";
            }
        }
        if (StringUtils.isNotEmpty(param)) {
            url += "?" + StringUtils.removeEnd(param, "&");
        }
        return url;

    }

    /**
     * 发送一个 Restful 请求
     *
     * @param restType "get", "head", "post", "put", "delete", "options", "patch"
     * @param url      资源地址
     * @param paramMap 参数
     * @return
     */
    private String doRestRequest(String restType, String url, Map<String, Object> paramMap) {
        Object object = null;
        try {
            switch (restType) {
                case "get":
                    object = restTemplate.getForObject(url, Object.class, paramMap);
                    break;
                case "post":
                    object = restTemplate.postForObject(url, null, Object.class, paramMap);
                    break;
                case "put":
                    restTemplate.put(url, null, paramMap);
                    break;
                case "head":
                    HttpHeaders httpHeaders = restTemplate.headForHeaders(url, paramMap);
                    return String.valueOf(httpHeaders);
                case "delete":
                    restTemplate.delete(url, paramMap);
                    break;
                case "options":
                    Set<HttpMethod> httpMethods = restTemplate.optionsForAllow(url, paramMap);
                    return String.valueOf(httpMethods);
                case "patch":
                    object = restTemplate.execute(url, HttpMethod.PATCH, null, null, paramMap);
                    break;
                case "trace":
                    object = restTemplate.execute(url, HttpMethod.TRACE, null, null, paramMap);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            // 无法使用 restTemplate 发送的请求，返回""
            // ex.printStackTrace();
            return "";
        }
        return String.valueOf(object);
    }

    /**
     * 封装post请求体
     *
     * @param list
     * @return
     */
    private Map<String, Object> ParamMap(List<Request> list) {
        Map<String, Object> map = new HashMap<>(8);
        if (list != null && list.size() > 0) {
            for (Request request : list) {
                String name = request.getName();
                String type = request.getType();
                switch (type) {
                    case "string":
                        map.put(name, "string");
                        break;
                    case "integer":
                        map.put(name, 0);
                        break;
                    case "number":
                        map.put(name, 0.0);
                        break;
                    case "boolean":
                        map.put(name, true);
                    default:
                        map.put(name, null);
                        break;
                }
            }
        }
        return map;
    }
}
