package info.bfly.core.jsf.validator;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import org.omnifaces.util.Components;
import org.omnifaces.util.Messages;
import org.omnifaces.validator.ValueChangeValidator;

import com.sun.faces.util.MessageFactory;

//@Component
@FacesValidator(value = "info.bfly.core.validator.NumberValidator")
public class NumberValidator extends ValueChangeValidator implements PartialStateHolder {
    public static final String VALIDATOR_ID = "info.bfly.core.validator.NumberValidator";
    // 小数精度
    private Integer precision;
    // 必须为多少的倍数
    private Double  cardinalNumber;
    private boolean initialState;

    private void checkCardinalNumber(FacesContext context, UIComponent component, Double number) {
        if (cardinalNumber != null) {
            if (cardinalNumber == 0) {
                // 除数为0，抛异常
                // TODO:国际化
                throw new ValidatorException(Messages.createError("numberValidator cardinalNumber is zero!", Components.getLabel(component)));
            }
            if (number % cardinalNumber != 0) {
                // 验证未通过
                // TODO:国际化
                throw new ValidatorException(Messages.createError("{0}必须为" + cardinalNumber + "的倍数", Components.getLabel(component)));
            }
        }
    }

    /**
     * 检查小数位数
     *
     * @param number
     */
    private void checkPrecision(FacesContext context, UIComponent component, Double number) {
        if (precision != null) {
            String numberStr;
            if (number == number.longValue()) {
                numberStr = String.valueOf(number.longValue());
            } else {
                numberStr = number.toString();
            }
            String[] ns = numberStr.split("\\.");
            if (precision == 0) {
                if (ns.length != 1) {
                    throw new ValidatorException(MessageFactory.getMessage(context, "info.bfly.core.validator.NumberValidator.NOT_A_INTEGER", MessageFactory.getLabel(context, component), precision));
                }
            } else if (ns.length == 2 && ns[1].length() > precision) {
                throw new ValidatorException(MessageFactory.getMessage(context, "info.bfly.core.validator.NumberValidator.ERROR_PRECISION", MessageFactory.getLabel(context, component), precision));
            }
        }
    }

    @Override
    public void clearInitialState() {
        initialState = false;
    }

    public Double getCardinalNumber() {
        return cardinalNumber;
    }

    public Integer getPrecision() {
        return precision;
    }

    @Override
    public boolean initialStateMarked() {
        return initialState;
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public void markInitialState() {
        initialState = true;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (state != null) {
            Object values[] = (Object[]) state;
            precision = (Integer) values[0];
            cardinalNumber = (Double) values[1];
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[2];
            values[0] = precision;
            values[1] = cardinalNumber;
            return (values);
        }
        return null;
    }

    public void setCardinalNumber(Double cardinalNumber) {
        this.cardinalNumber = cardinalNumber;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    @Override
    public void setTransient(boolean arg0) {
    }

    @Override
    public void validateChangedObject(FacesContext context, UIComponent component, Object arg2) throws ValidatorException {
        Double number = null;
        if (arg2 instanceof Long) {
            number = ((Long) arg2).doubleValue();
        } else if (arg2 instanceof Double) {
            number = (Double) arg2;
        } else {
            throw new RuntimeException(arg2.toString() + "is not a valid number");
        }
        if (number != null) {
            checkPrecision(context, component, number);
            checkCardinalNumber(context, component, number);
        }
    }
}
