package info.bfly.pay.bean.user;

import info.bfly.pay.bean.enums.ACCOUNT_TYPE;


public class QueryBalanceEntity extends UserSinaEntity{
    private static final long serialVersionUID = -6547451386323104574L;
    /**
     * 账户类型（基本户、保证金户、存钱罐、银行账户）。默认基本户，见附录
     */
    private ACCOUNT_TYPE account_type;

    public ACCOUNT_TYPE getAccount_type() {
        return account_type;
    }

	public void setAccount_type(ACCOUNT_TYPE account_type) {
		this.account_type = account_type;
	}




}
