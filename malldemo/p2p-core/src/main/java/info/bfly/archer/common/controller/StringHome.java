package info.bfly.archer.common.controller;

import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.GregorianCalendar;

/**
 * 一些字符串工具，在页面上用
 *
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.REQUEST)
public class StringHome {
    /**
     * 字符串切割，加上“...”
     */
    public String ellipsis(String str, int length) {
        char[] strs = str.toCharArray();
        String ellipsisStr = "...";
        if (length > strs.length) {
            length = strs.length;
            ellipsisStr = "";
        }
        char[] aimStrs = new char[length];
        System.arraycopy(strs, 0, aimStrs, 0, length);
        return String.valueOf(aimStrs) + ellipsisStr;
    }
    
    /*
     * 字符串截取
     */
    public String substring(String str,int index){
    	return str.substring(index);
    }
    public String substring(String str,int begin,int end){
    	return str.substring(begin,end);
    }



    public String getNowDateStr(){
        GregorianCalendar c= new GregorianCalendar();
        int apm = c.get(GregorianCalendar.AM_PM);
        return apm==0?"上午":"下午";
    }

}
