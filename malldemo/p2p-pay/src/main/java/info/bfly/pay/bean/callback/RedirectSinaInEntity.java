package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;

/**
 * Created by XXSun on 2017/2/16.
 */
public class RedirectSinaInEntity extends SinaInEntity {

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    @NotNull
    private String redirect_url;


}
