package info.bfly.p2p.invest.service;

import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.model.InvestRefunds;

/**
 * 退款Service
 * Created by Administrator on 2017/6/5 0005.
 */
public interface InvestRefundsService {


    /**
     * 初始化退款订单
     *
     * @param invest
     * @return
     */
    InvestRefunds init(Invest invest);


    /**
     * 更新退款订单
     *
     * @param investRefunds
     */
    void updateOrSaveInvestRefunds(InvestRefunds investRefunds);


    /**
     * 处理退款成功
     */

    void refundSuccess(String investRefundId);


    /**
     * 根据订单号查找退款信息
     * @param operationOrderNo
     * @return
     */
    InvestRefunds getInvestRefundsByOperationOrderNo(String operationOrderNo);


}
