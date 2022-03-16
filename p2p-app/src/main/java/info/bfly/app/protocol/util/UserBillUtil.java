package info.bfly.app.protocol.util;

public class UserBillUtil {
  /*  public static UserBillSub getUserBillSub(GsonBuilder builder,UserBill userBill) throws IllegalAccessException, InvocationTargetException{
        UserBillSub sub=new UserBillSub();
        BeanUtils.copyProperties(sub, userBill);
        String typeValue=UserBillType.getValue(UserBillType.typeNameMap,userBill.getType());
        if(typeValue!=null){
            sub.setType(typeValue);
        }
        String typeInfoValue=UserBillType.getValue(UserBillType.typeInfoNameMap, userBill.getTypeInfo());
        if(typeInfoValue!=null){
            sub.setTypeInfo(typeInfoValue);

        }
        sub.setMoneyStr(NumberUtil.getNumber(userBill.getMoney()));
        sub.setBalanceStr(NumberUtil.getNumber(userBill.getBalance()));
        sub.setFrozenMoneyStr(NumberUtil.getNumber(userBill.getFrozenMoney()));
        return sub;
    }*/
}
