package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.AdverLeague;
import info.bfly.archer.user.model.AdverLeagueDTO;
import info.bfly.archer.user.service.AdverService;
import info.bfly.archer.user.util.ClassUtil;
import info.bfly.archer.user.util.DozerMapperSingleton;
import info.bfly.archer.user.valide.LeagueListValidator;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.JSONUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 联盟反馈
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class LeagueList extends EntityQuery<AdverLeague> implements Serializable {
    /**
     *
     */
    private static final long   serialVersionUID = 2331475983409806529L;
    static               Logger logger           = Logger.getLogger(LeagueList.class.getName());
    @Resource
    private AdverService        adverService;
    @Resource
    private LeagueListValidator leagueListValidator;
    private Map<String, String> mapPara = new HashMap<String, String>();
    private Map<String, Object> mapData = new HashMap<String, Object>();

    private void fillPara(AdverLeague adverLeague) {
        if (null != adverLeague) {
            adverLeague.setMid(mapPara.get("mid"));
            adverLeague.setUid(mapPara.get("uid"));
            adverLeague.setStatus("1");
            adverLeague.setUserName(mapPara.get("userName"));
            adverLeague.setRegDate(new Date());
        }
    }

    /**
     * 单个对象查询
     *
     * */
    public String getleagueByid() {
        try {
            ClassUtil.convertMapValueDecord(mapPara, "utf-8");
            leagueListValidator.getleagueByid(mapPara);
            AdverLeague adverLeague = adverService.getAdverByID(Integer.parseInt(mapPara.get("id")));
            AdverLeagueDTO adverLeagueDTO = null;
            if (null != adverLeague) {
                adverLeagueDTO = new AdverLeagueDTO();
                DozerMapperSingleton.getInstance().map(adverLeague, adverLeagueDTO);
            }
            List list = new ArrayList();
            list.add(adverLeagueDTO);
            JSONUtils.saveMap(mapData, "0", "操作成功", list);
        }
        catch (Exception ex) {
            LeagueList.logger.error("getleagueByid方法执行异常" + ex.getMessage());
            JSONUtils.saveMap(mapData, "1", "getleagueByid方法执行异常" + ex.getMessage(), null);
        }
        nowStatusClearAndSetResponseValue();
        return JSONUtils.getResponseStringJSON(mapData);
    }

    @ResponseBody
    public String getleagueList() {
        try {
            ClassUtil.convertMapValueDecord(mapPara, "utf-8");
            leagueListValidator.getleagueList(mapPara);
            List alist = adverService.getAlList(mapPara.get("mid"), mapPara.get("uid"), mapPara.get("stime"), mapPara.get("eid"));
            List listDTO = null;
            if (null != alist && alist.size() > 0) {
                listDTO = new ArrayList();
                DozerMapperSingleton.listCopy(alist, listDTO, "info.bfly.archer.user.model.AdverLeagueDTO");
            }
            int total = 10;// 总页数
            mapData.put("recordTotal", total);
            JSONUtils.saveMap(mapData, "0", "操作成功", listDTO);
        }
        catch (Exception ex) {
            JSONUtils.saveMap(mapData, "1", "query方法" + ex.getMessage(), null);
        }
        nowStatusClearAndSetResponseValue();
        return JSONUtils.getResponseStringJSON(mapData);
    }

    public Map<String, Object> getMapData() {
        return mapData;
    }

    public Map<String, String> getMapPara() {
        return mapPara;
    }

    /**
     * 判断日期格式是否正确
     *
     * @param date
     * @return
     */
    public boolean isValidDate(String date) {
        String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))" + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?" + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((date != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(date);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(date);
                return match.matches();
            }
            else {
                return false;
            }
        }
        return false;
    }

    private void nowStatusClearAndSetResponseValue() {
        FacesUtil.setRequestAttribute("data", JSONUtils.getResponseStringJSON(mapData));
        mapPara.clear();
        mapData.clear();
    }

    public String save() {
        try {
            ClassUtil.convertMapValueDecord(mapPara, "utf-8");
            leagueListValidator.save(mapPara);
            AdverLeague adverLeague = new AdverLeague();
            fillPara(adverLeague);
            adverService.save(adverLeague);
            JSONUtils.saveMap(mapData, "0", "保存成功", null);
        } catch (Exception ex) {
            LeagueList.logger.error("getleagueByid方法执行异常" + ex.getMessage());
            JSONUtils.saveMap(mapData, "1", "league.save保存失败:" + ex.getMessage(), null);
        }
        nowStatusClearAndSetResponseValue();
        return "";
    }

    public void setAdverService(AdverService adverService) {
        this.adverService = adverService;
    }

    public void setMapData(Map<String, Object> mapData) {
        this.mapData = mapData;
    }

    public void setMapPara(Map<String, String> mapPara) {
        this.mapPara = mapPara;
    }
}
