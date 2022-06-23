package info.bfly.p2p.message.service;

public interface SmsTemplate {
    /*
     * 给借款人发送收款的消息提醒
     */
    void sendBorrowerReceiveRemind(String name, String mobileNumber);

    /*
     * 给借款人发送还款的消息提醒
     */
    void sendBorrowerReimburseRemind(String name, String mobileNumber);

    /*
     * 给投资人发送流标的消息提醒
     */
    void sendInvestorFlowRemind(String name, String mobileNumber, Double money);

    /*
     * 给投资人发送满标的消息提醒
     */
    void sendInvestorFullRemind(String name, String mobileNumber);

    /*
     * 给投资人发送回款的消息提醒
     */
    void sendInvestorReceiveRemind(String name, String mobileNumber);

    /*
     * 给投资人发送标的延期消息提醒
     */
    void sendInvestorUnderlyDelyRemind(String name, String mobileNumber);

    /*
     * 发送投资成功的消息
     */
    void sendInvestSuccess(String mobileNumber, Double investMoney, Double ratePercent, Double income);

    /*
     * 发送"好友币"充值成功的消息
     */
    void sendRechargeSuccess(String mobileNumber, Double money);

    /*
     * 发送提现成功的消息
     */
    void sendWithdrawSuccess(String mobileNumber, Double money);
}
