package info.bfly.archer.theme.controller;

import info.bfly.archer.config.ConfigConstants;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * SiteVars，通过JSF EL 表达式调用方法#{siteVars.siteName}，
 * 该类主要是包含了一些网站的信息，比如网站标题、名称、口号等信息。 这些信息是通过数据库存储的详细请参见 config模块。
 *
 * @author wanghm
 */
@Component
@Scope(ScopeType.SESSION)
public class SiteVars implements java.io.Serializable {
    private static final long serialVersionUID = -7089517277500127277L;
    @Log
    static Logger log;
    private final static String SITE_NAME_EL    = "#{configHome.getConfigValue('" + ConfigConstants.Website.SITE_NAME + "')}";
    private final static String SITE_SLOGAN_EL  = "#{configHome.getConfigValue('" + ConfigConstants.Website.SITE_SLOGAN + "')}";
    private final static String SITE_DNS_EL     = "#{configHome.getConfigValue('" + ConfigConstants.Website.SITE_DNS + "')}";
    static final         String FRONT_PATTERN1  = "(.*?)(p2p/loanList.htm)(.*?)";
    static final         String FRONT_PATTERN2  = "(.*?)(/loanList.htm)(.*?)";
    static final         String FRONT_PATTERN3  = "(.*?)(/loan_transfer.htm)(.*?)";
    static final         String FRONT_PATTERN4  = "(.*?)(/applyLoan.htm)(.*?)";
    static final         String FRONT_PATTERN5  = "(.*?)(/applyLoan_enterprise.htm)(.*?)";
    static final         String FRONT_PATTERN6  = "(.*?)(/index.htm)(.*?)";
    static final         String FRONT_PATTERN7  = "(.*?)(/term.htm)(.*?)";
    static final         String FRONT_PATTERN8  = "(.*?)(/p2p/loanerPersonInfo.htm)(.*?)";
    static final         String FRONT_PATTERN9  = "(.*?)(/p2p/loanerAdditionInfo.htm)(.*?)";
    static final         String FRONT_PATTERN10 = "(.*?)(/p2p/loanerAuthentication.htm)(.*?)";
    static final         String FRONT_PATTERN11 = "(.*?)(/user/myCashFlow.htm)(.*?)";
    static final         String FRONT_PATTERN12 = "(.*?)(/user/recharge.htm)(.*?)";
    static final         String FRONT_PATTERN13 = "(.*?)(/user/withdraw.htm)(.*?)";
    static final         String FRONT_PATTERN14 = "(.*?)(/user/invest/repaying.htm)(.*?)";
    static final         String FRONT_PATTERN15 = "(.*?)(/user/transfer/purchased.htm)(.*?)";
    static final         String FRONT_PATTERN16 = "(.*?)(/user/autoInvest.htm)(.*?)";
    static final         String FRONT_PATTERN17 = "(.*?)(/user/loan/repaying.htm)(.*?)";
    static final         String FRONT_PATTERN18 = "(.*?)(/user/loan/applying-p2p.htm)(.*?)";
    static final         String FRONT_PATTERN19 = "(.*?)(/user/get_investor_permission.htm)(.*?)";
    static final         String FRONT_PATTERN20 = "(.*?)(/user/accountSafe.htm)(.*?)";
    static final         String FRONT_PATTERN21 = "(.*?)(/user/messages.htm)(.*?)";
    static final         String FRONT_PATTERN22 = "(.*?)(/user/messageTypeSet.htm)(.*?)";
    static final         String FRONT_PATTERN23 = "(.*?)(/user/my_point_history.htm)(.*?)";
    static final         String FRONT_PATTERN24 = "(.*?)(/user/my_presentee.htm)(.*?)";
    static final         String FRONT_PATTERN25 = "(.*?)(/nav.htm)(.*?)";
    static final         String FRONT_PATTERN26 = "(.*?)(/tzlcList.htm)(.*?)";
    static final         String FRONT_PATTERN27 = "(.*?)(/encyclopedia.htm)(.*?)";
    static final         String FRONT_PATTERN28 = "(.*?)(/wdbklist.htm)(.*?)";
    static final         String FRONT_PATTERN29 = "(.*?)(/financial.htm)(.*?)";
    static final         String FRONT_PATTERN30 = "(.*?)(/nav_jrjc.htm)(.*?)";
    static final         String FRONT_PATTERN31 = "(.*?)(/jrjcList.htm)(.*?)";
    static final         String FRONT_PATTERN32 = "(.*?)(/center.htm)(.*?)";
    static final         String FRONT_PATTERN33 = "(.*?)(/user/userAddress.htm)(.*?)";
    static final         String FRONT_PATTERN34 = "(.*?)(/user/uPassword.htm)(.*?)";
    
    // index.htm
    private String siteDns, siteName, siteSlogan;
    private String keywords1;
    private String description;
    private String tittle3;

    public String getDescription() {
        setHeaderMeta();
        return description;
    }

    public String getKeywords1() {
        setHeaderMeta();
        return keywords1;
    }

    /**
     * 站点域名
     *
     * @return
     */
    public String getSiteDns() {
        if (siteDns == null) {
            siteDns = (String) FacesUtil.getExpressionValue(SiteVars.SITE_DNS_EL);
        }
        return siteDns;
    }

    /**
     * 网站名称
     * <p>
     * public String getSiteName(){ if(siteName == null){ siteName = (String)
     * FacesUtil.getExpressionValue(SITE_NAME_EL); } return siteName ; }
     */
    public String getSiteName() {
        if (siteName == null) {
            siteName = (String) FacesUtil.getExpressionValue(SiteVars.SITE_NAME_EL);
        }
        return siteName;
    }

    /**
     * 网站标语
     *
     * @return
     */
    public String getSiteSlogan() {
        if (siteSlogan == null) {
            siteSlogan = (String) FacesUtil.getExpressionValue(SiteVars.SITE_SLOGAN_EL);
        }
        return siteSlogan;
    }

    public String getTittle3() {
        setHeaderMeta();
        return tittle3;
    }

    public void setDescription(String description) {
        setHeaderMeta();
        this.description = description;
    }

    private String setHeaderMeta() {
        String uri = FacesUtil.getHttpServletRequest().getRequestURI();
        log.debug(uri);
    if (uri.matches(SiteVars.FRONT_PATTERN32)) {
            keywords1 = "";
            description = "";
            tittle3 = "账户中心";
        }if (uri.matches(SiteVars.FRONT_PATTERN34)) {
            keywords1 = "";
            description = "";
            tittle3 = "登陆密码修改";
        }
        return uri;
    }

    public void setKeywords1(String keywords1) {
        setHeaderMeta();
        this.keywords1 = keywords1;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setTittle3(String tittle3) {
        setHeaderMeta();
        this.tittle3 = tittle3;
    }
}
