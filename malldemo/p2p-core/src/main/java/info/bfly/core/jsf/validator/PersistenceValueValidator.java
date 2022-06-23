package info.bfly.core.jsf.validator;

import com.sun.faces.util.MessageFactory;
import info.bfly.archer.common.service.ValidationService;
import info.bfly.core.annotations.Log;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Messages;
import org.omnifaces.validator.ValueChangeValidator;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

@Component
@FacesValidator(value = "info.bfly.core.validator.PersistenceValueValidator")
public class PersistenceValueValidator extends ValueChangeValidator implements PartialStateHolder {
    @Log
    private Logger log;
    public static final String VALIDATOR_ID = "info.bfly.core.validator.PersistenceValueValidator";
    // 实体
    private String entityClass;
    // 字段名称
    private String fieldName;
    // 实体id
    private String id;
    // 提示消息
    private String message;
    @Resource
    ValidationService vdtService;
    private boolean initialState;

    @Override
    public void clearInitialState() {
        initialState = false;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
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
            entityClass = (String) values[0];
            fieldName = (String) values[1];
            id = (String) values[2];
            message = (String) values[3];
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[4];
            values[0] = entityClass;
            values[1] = fieldName;
            values[2] = id;
            values[3] = message;
            return (values);
        }
        return null;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setTransient(boolean arg0) {
    }

    @Override
    public void validateChangedObject(FacesContext context, UIComponent component, Object arg2) throws ValidatorException {
        String value = (String) arg2;
        if (StringUtils.isEmpty(value)) {
            throw new ValidatorException(MessageFactory.getMessage(context, "javax.faces.component.UIInput.REQUIRED", MessageFactory.getLabel(context, component)));
        }
        try {
            if (!vdtService.equalsPersistenceValue(entityClass, fieldName, id, value)) {
                if (StringUtils.isNotEmpty(message)) {
                    throw new ValidatorException(Messages.createError(message));
                } else {
                    throw new ValidatorException(MessageFactory.getMessage(context, "info.bfly.core.validator.AlreadyExistValidator.PERSISTENCE_VALUE", MessageFactory.getLabel(context, component)));
                }
            }
        } catch (SecurityException e) {
            log.debug(e.getMessage());
            throw new ValidatorException(MessageFactory.getMessage(context, "javax.faces.converter.NO_SUCH_FIELD", MessageFactory.getLabel(context, component), "get"
                    + StringUtils.capitalize(fieldName)));
        } catch (ClassNotFoundException e) {
            log.debug(e.getMessage());
            throw new ValidatorException(MessageFactory.getMessage(context, "javax.faces.converter.CLASS_NOT_FOUND", MessageFactory.getLabel(context, component), entityClass));
        } catch (NoSuchMethodException e) {
            log.debug(e.getMessage());
            throw new ValidatorException(MessageFactory.getMessage(context, "javax.faces.converter.NO_SUCH_FIELD", MessageFactory.getLabel(context, component), "get"
                    + StringUtils.capitalize(fieldName)));
        }
    }
}
