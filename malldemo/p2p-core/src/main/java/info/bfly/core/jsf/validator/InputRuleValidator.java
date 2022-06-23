package info.bfly.core.jsf.validator;

import info.bfly.archer.common.exception.InputRuleMatchingException;
import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.common.service.ValidationService;
import info.bfly.core.util.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Components;
import org.omnifaces.util.Messages;
import org.omnifaces.validator.ValueChangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "info.bfly.core.validator.InputRuleValidator")
public class InputRuleValidator extends ValueChangeValidator implements PartialStateHolder {
    private static final Logger log = LoggerFactory.getLogger(InputRuleValidator.class);
    public static final String VALIDATOR_ID = "info.bfly.core.validator.InputRuleValidator";
    // 规则编号
    private String ruleId;
    // 验证消息
    private String message;
    @Resource
    ValidationService vdtService;
    private boolean transientValue = false;
    private boolean initialState;

    @Override
    public void clearInitialState() {
        initialState = false;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (!(otherObj instanceof InputRuleValidator)) {
            return false;
        }
        InputRuleValidator other = (InputRuleValidator) otherObj;
        return StringUtils.equals(getRuleId(), other.getRuleId()) && StringUtils.equals(getMessage(), other.getMessage());
    }

    public String getMessage() {
        return message;
    }

    public String getRuleId() {
        return ruleId;
    }

    @Override
    public int hashCode() {
        if (StringUtils.isEmpty(getMessage()) && StringUtils.isEmpty(getRuleId())) {
            return super.hashCode();
        }
        int hashCode = 0;
        if (StringUtils.isNotEmpty(getMessage())) {
            hashCode += getMessage().hashCode();
        }
        if (StringUtils.isNotEmpty(getRuleId())) {
            hashCode += getRuleId().hashCode();
        }
        return (hashCode);
    }

    @Override
    public boolean initialStateMarked() {
        return initialState;
    }

    @Override
    public boolean isTransient() {
        return (transientValue);
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
            ruleId = (String) values[0];
            message = (String) values[1];
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[2];
            values[0] = ruleId;
            values[1] = message;
            return (values);
        }
        return null;
    }

    public void setMessage(String message) {
        clearInitialState();
        this.message = message;
    }

    public void setRuleId(String ruleId) {
        clearInitialState();
        this.ruleId = ruleId;
    }

    @Override
    public void setTransient(boolean transientValue) {
        this.transientValue = transientValue;
    }

    @Override
    public void validateChangedObject(FacesContext context, UIComponent component, Object arg2) throws ValidatorException {
        String value = (String) arg2;
        try {
            ValidationService vdtService = (ValidationService) SpringBeanUtil.getBeanByName("validationService");
            vdtService.inputRuleValidation(ruleId, value);
        } catch (NoMatchingObjectsException e) {
            log.debug(e.getMessage());
            throw new ValidatorException(Messages.createError("ruleId:{0} not found!", ruleId));
        } catch (InputRuleMatchingException e) {
            if (StringUtils.isNotEmpty(message)) {
                throw new ValidatorException(Messages.createError(message));
            } else {
                throw new ValidatorException(Messages.createError(e.getMessage(), Components.getLabel(component)));
            }
        }
    }
}
