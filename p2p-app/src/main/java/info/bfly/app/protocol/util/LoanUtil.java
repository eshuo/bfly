package info.bfly.app.protocol.util;

import info.bfly.p2p.loan.LoanConstants;

import java.util.Date;

public class LoanUtil {
/*    public static LoanSub getLoanSub(GsonBuilder builder,Loan loan,LoanCalculator loanCalculator) throws IllegalAccessException, InvocationTargetException, NoMatchingObjectsException{
        String statusValue=LoanType.getValue(LoanType.statusNameMap,loan.getStatus());
        LoanSub sub=new LoanSub();
        BeanUtils.copyProperties(sub, loan);
        sub.setJd(loanCalculator.calculateRaiseCompletedRate(loan.getId()));//进度
        sub.setLastStrTime(loanCalculator.calculateRemainTime(loan.getId()));//最后时间
        sub.setSybj(loanCalculator.calculateMoneyNeedRaised(loan.getId()));//剩余本金
        if(statusValue!=null){
            sub.setStatus(statusValue);
        }
        return sub;
    }*/
    /**
     * 根据状态获取未到期项目剩余时间
     *
     * @param expectTime
     * @param status
     * @return
     */
    public static String getRemainTime(Date expectTime, String status) {
        if (expectTime == null) {
            return "未开始";
        }
        Long time = (expectTime.getTime() - System.currentTimeMillis()) / 1000;
        if (time < 0 || !status.equals(LoanConstants.LoanStatus.RAISING)) {
            return "已到期";
        }
        long days = time / 3600 / 24;
        long hours = (time / 3600) % 24;
        long minutes = (time / 60) % 60;
        if (minutes < 1) {
            minutes = 1L;
        }
        return days + "天" + hours + "时" + minutes + "分";
    }
}
