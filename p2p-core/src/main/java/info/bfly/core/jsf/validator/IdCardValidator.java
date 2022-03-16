package info.bfly.core.jsf.validator;

import info.bfly.core.util.IDCard;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Messages;
import org.omnifaces.validator.ValueChangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

/**
 * Created by XXSun on 3/11/2017.
 */
@FacesValidator(value = "info.bfly.core.validator.IdCardValidator")
public class IdCardValidator extends ValueChangeValidator implements PartialStateHolder {
    private static final Logger log          = LoggerFactory.getLogger(IdCardValidator.class);
    public static final  String VALIDATOR_ID = "info.bfly.core.validator.IdCardValidator";

    private IDCard idCard = new IDCard();
    private boolean transientValue = false;
    private boolean initialState;

    @Override
    public void validateChangedObject(FacesContext context, UIComponent component, Object submittedValue) {

        String value = (String) submittedValue;
        String result = idCard.IDCardValidate(value);
        log.debug("validate idcard {} with {}",submittedValue,result);
        if(StringUtils.isNotEmpty(result)){
            throw new ValidatorException(Messages.createError(result));
        }

    }

    @Override
    public void markInitialState() {
        initialState = true;
    }

    @Override
    public boolean initialStateMarked() {
        return initialState;
    }

    @Override
    public void clearInitialState() {
        initialState = false;
    }

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[2];
            return values;
        }
        return null;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }

    }

    @Override
    public boolean isTransient() {
        return transientValue;
    }

    @Override
    public void setTransient(boolean newTransientValue) {
        this.transientValue = newTransientValue;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (!(otherObj instanceof IdCardValidator)) {
            return false;
        }
        IdCardValidator other = (IdCardValidator) otherObj;
        return this.equals(other);
    }
}
