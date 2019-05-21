package com.word.utils;


import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*********************************************************
 * 文件名称： StringCompareUtil java
 * 系统名称：区块链系统 V1.0 
 * 模块名称：com.hundsun.bcsp.base.utils  
 * 功能说明： 统一的stringutil
 * 开发人员：suqing26144 
 * 开发时间：2018/9/27 15:33 
 * 修改记录：程序版本	修改日期	修改人员	修改单号	修改说明
 *********************************************************/
public class StringCompareUtil {
     private static String rule="\n";

     private StringCompareUtil()
     {
         super();
     }
    /**
     * 忽略大小写比较objectA和objectB
     *
     * @param objectA
     * @param objectB
     * @return boolean
     */
    public static boolean equalsIgnoreCase(String objectA, String objectB) {
        return StringUtils.equalsIgnoreCase(objectA, objectB);
    }

    /**
     * 判断string是否为空
     *
     * @param object
     * @return boolean
     */
    public static boolean isEmpty(String object) {
        return StringUtils.isEmpty(object);
    }



    /**
     * 判断在集合中是否存在特定的字符串
     *
     * @param object     String
     * @param collection Collection
     * @return boolean
     */
    public static boolean isInContain(String object, Collection collection) {
        if (collection == null) {
            return false;
        }
        Iterator<String> it = collection.iterator();
        boolean status = false;
        while (it.hasNext()) {
            if (object.equals(it.next())) {
                status = true;
                break;
            }
        }
        return status;
    }
    /**
     * 判断在集合中是否存在特定的字符串
     *
     * @param object     String
     * @param collection Collection
     * @return boolean
     */
    public static boolean isInContain(Integer object, Collection collection) {
        Iterator<Integer> it = collection.iterator();
        boolean status = false;
        while (it.hasNext()) {
            if (object.compareTo(it.next()) == 0) {
                status = true;
                break;
            }
        }
        return status;
    }

    public static boolean isEquals(String a, String b) {
        return a.equals(b);
    }


    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile(rule);
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
