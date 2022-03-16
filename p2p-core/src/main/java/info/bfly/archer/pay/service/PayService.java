package info.bfly.archer.pay.service;

import info.bfly.p2p.loan.model.Recharge;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PayService {
    // FIXME:最好能抽取一个pay方法，降低耦合
    void receiveReturnS2S(HttpServletRequest request, HttpServletResponse response);

    void receiveReturnWeb(HttpServletRequest request);

    /**
     * 充值
     *
     * @param fc
     * @param recharge
     * @param bankCardNo
     */
    void recharge(FacesContext fc, Recharge recharge, String bankCardNo);
}
