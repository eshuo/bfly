package info.bfly.p2p.trusteeship.exception;

/**
 * Description: 第三方资金托管时候,需要推进
 */
public class TrusteeshipFormException extends Exception {
    private static final long serialVersionUID = 6156071238261755639L;

    private final String form;

    public String getForm() {
        return form;
    }

    public TrusteeshipFormException(String form) {
        this.form =form;
    }

}
