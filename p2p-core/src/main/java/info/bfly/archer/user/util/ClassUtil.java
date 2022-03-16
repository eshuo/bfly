package info.bfly.archer.user.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClassUtil {
    private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);
    /**
     * 判断该方法是否存在
     *
     * @param methods
     * @param met
     * @return
     */
    public static boolean checkMethod(Method methods[], String met) {
        if (null != methods) {
            for (Method method : methods) {
                if (met.equals(method.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void convertClassPropertDecord(Object obj, String code) {
        if (StringUtils.isBlank(code)) {
            code = "UTF-8";
        }
        if (null != obj) {
            Map map;
            try {
                map = ClassUtil.getFileValue(obj);
                map = ClassUtil.convertMapValueDecord(map, code);
                ClassUtil.setFieldValue(map, obj);
            } catch (Exception e) {
                throw new RuntimeException("参数解码异常:" + e.getMessage());
            }
        }
    }

    public static Map convertMapValueDecord(Map map, String code) throws UnsupportedEncodingException {
        Map resultMap = new HashMap();
        if (StringUtils.isBlank(code)) {
            code = "UTF-8";
        }
        if (null != map) {
            Set<String> key = map.keySet();
            String valueMap = "";
            String keyMap = "";
            for (Iterator it = key.iterator(); it.hasNext(); ) {
                keyMap = (String) it.next();
                valueMap = map.get(keyMap) == null ? null : (String) map.get(keyMap);
                if (null != valueMap) {
                    valueMap = URLDecoder.decode(valueMap, code);
                }
                resultMap.put(keyMap, valueMap);
            }
        }
        return resultMap;
    }

    /**
     * 把date 类转换成string
     *
     * @param date
     * @return
     */
    public static String fmlDate(Date date) {
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            return sdf.format(date);
        }
        return null;
    }

    // 当前的类名就是：GetFilePath
    public static String getFilePath(String fileName, int system) {
        String path = ClassUtil.class.getResource("").toString();

        if (path != null) {
            path = path.substring(system, path.indexOf("WEB-INF") + 8);// 如果是windwos将5变成6
               }
        return (path + fileName);
    }

    /**
     * 取出bean 属性和值
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<Object, Object> getFileValue(Object obj) throws Exception {
        Map<Object, Object> map = new HashMap<Object, Object>();
        if (obj instanceof Map) {
            return (Map) obj;
        }
        if (obj instanceof String) {
            return ClassUtil.strToMap((String) obj, "&");
        }
        Class<?> cls = obj.getClass();
        Method methods[] = cls.getDeclaredMethods();
        Field fields[] = cls.getDeclaredFields();
        for (Field field : fields) {
            String fldtype = field.getType().getSimpleName();
            String getMetName = ClassUtil.pareGetName(field.getName());
            String result = null;
            if (!ClassUtil.checkMethod(methods, getMetName)) {
                continue;
            }
            Method method = cls.getMethod(getMetName, (Class<?>) null);
            Object object = method.invoke(obj);
            if (null != object) {
                if (fldtype.equals("Date")) {
                    result = ClassUtil.fmlDate((Date) object);
                }
                result = String.valueOf(object);
            }
            map.put(field.getName(), result);
        }
        return map;
    }


    /**
     * 拼接某属性get 方法
     *
     * @param fldname
     * @return
     */
    public static String pareGetName(String fldname) {
        if (null == fldname || "".equals(fldname)) {
            return null;
        }
        String pro = "get" + fldname.substring(0, 1).toUpperCase() + fldname.substring(1);
        return pro;
    }

    /**
     * 拼接某属性set 方法
     *
     * @param fldname
     * @return
     */
    public static String pareSetName(String fldname) {
        if (null == fldname || "".equals(fldname)) {
            return null;
        }
        String pro = "set" + fldname.substring(0, 1).toUpperCase() + fldname.substring(1);
        return pro;
    }

    /**
     * 设置bean 属性值
     *
     * @param map
     * @param bean
     * @throws Exception
     */
    public static void setFieldValue(Map<Object, Object> map, Object bean) throws Exception {
        Class<?> cls = bean.getClass();
        Method methods[] = cls.getDeclaredMethods();
        Field fields[] = cls.getDeclaredFields();
        for (Field field : fields) {
            String fldtype = field.getType().getSimpleName();
            String fldSetName = field.getName();
            String setMethod = ClassUtil.pareSetName(fldSetName);
            if (!ClassUtil.checkMethod(methods, setMethod)) {
                continue;
            }
            Object value = map.get(fldSetName);
            Method method = cls.getMethod(setMethod, field.getType());
             if (null != value) {
                if ("String".equals(fldtype)) {
                    method.invoke(bean, (String) value);
                } else if ("Double".equals(fldtype)) {
                    method.invoke(bean, (Double) value);
                } else if ("int".equals(fldtype)) {
                    int val = Integer.valueOf((String) value);
                    method.invoke(bean, val);
                }
            }
        }
    }

    private static Map strToMap(String str, String splite) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        if (!StringUtils.isBlank(str)) {
            if (StringUtils.isBlank(splite)) {
                splite = "&";
            }
            String[] para = str.split(splite);
            String[] value = null;
            for (String s : para) {
                if (null != s) {
                    value = s.split("=");
                    if (null != value) {
                        map.put(value[0], value.length > 1 ? value[1] : null);
                    }
                }
            }
        }
        return map;
    }

    /**
     * @param field
     * @return
     */
    public Map convertFiledToMap(Field[] field) {
        Map map = new HashMap();
        if (null != field) {
            for (Field obj : field) {
                if (null != obj) {
                    try {
                        map.put(obj.getName(), obj.get(this));
                    } catch (Exception ex) {
                        log.info(ex.getMessage());
                    }
                }
            }
        }
        return map;
    }

    public String getString(Object classObject) {
        StringBuffer sb = new StringBuffer();
        if (null != classObject) {
            Field[] f = classObject.getClass().getDeclaredFields();
            try {
                for (int i = 0; i < f.length; i++) {
                    sb.append(f[i].getName() + " = " + f[i].get(this) + "\r\n");
                }
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        return sb.toString();
    }

    /**
     * @param obj
     * @param isRemoveNull 是否加入值为""的
     * @return
     */
    public Map objPropertyToMap(Object obj, Boolean isRemoveNull) {
        Map mapSource = new HashMap();
        Field[] sourceFiledArray = null;
        if (null != obj) {
            sourceFiledArray = obj.getClass().getDeclaredFields();
            mapSource = convertFiledToMap(sourceFiledArray);
        }
        return mapSource;
    }

    public void objValueCopyByPropertyName(Object source, Object des) {
        Field[] sourceFiledArray = null;
        Field[] desFiledArray = null;
        Map mapSource = null;
        if (null != source && null != des) {
            sourceFiledArray = source.getClass().getDeclaredFields();
            desFiledArray = des.getClass().getDeclaredFields();
            mapSource = convertFiledToMap(sourceFiledArray);
            if (null != mapSource) {
                for (Field obj : desFiledArray) {
                    if (mapSource.containsKey(obj.getName())) {
                        try {
                            obj.set(obj, mapSource.get(obj.getName()));
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            log.debug(e.getMessage());
                        } catch (IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            log.debug(e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
