package info.bfly.core.jsf.validator;

import com.sun.faces.util.MessageFactory;
import info.bfly.archer.common.service.ValidationService;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.validator.ValueChangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

//@Component
@FacesValidator(value = "info.bfly.core.validator.AlreadyExistValidator")
public class AlreadyExistValidator extends ValueChangeValidator implements PartialStateHolder {
    private static final Logger log = LoggerFactory.getLogger(AlreadyExistValidator.class);
    public static final String VALIDATOR_ID = "info.bfly.core.validator.AlreadyExistValidator";
    // 实体
    private String             entityClass;
    // 字段名称
    private String             fieldName;
    // @Resource
    ValidationService          vdtService;
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
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[2];
            values[0] = entityClass;
            values[1] = fieldName;
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

    @Override
    public void setTransient(boolean arg0) {
    }

    @Override
    public void validateChangedObject(FacesContext context, UIComponent component, Object arg2) throws ValidatorException {
        String value = (String) arg2;
        if (StringUtils.isNotEmpty(value)) {
            try {
                boolean alreadyExist = false;
                vdtService = (ValidationService) SpringBeanUtil.getBeanByName("validationService");
                if (StringUtils.isEmpty(entityClass)) {
                    ValueExpression ve = component.getValueExpression("value");
                    String expr = ve.getExpressionString();
                    expr = expr.substring(2, expr.length() - 1);
                    String entityExpr = expr.substring(0, expr.lastIndexOf("."));
                    String filedExpr = expr.substring(expr.lastIndexOf(".") + 1, expr.length());
                    Object entity = FacesUtil.getExpressionValue("#{" + entityExpr + "}");
                    Class clazz = entity.getClass();// info.bfly.archer.user.model.User@64a26054
                    String nameString = entity.toString();
                    String namvString = nameString.substring(nameString.lastIndexOf(".") + 1, nameString.lastIndexOf("@"));
                    try {
                        alreadyExist = vdtService.isAlreadyExist(clazz, filedExpr, value);
                    }
                    catch (Exception e) {
                        try {
                            alreadyExist = vdtService.isAlreadExist(namvString, filedExpr, value);
                        }
                        catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
                else {
                    alreadyExist = vdtService.isAlreadExist(entityClass, fieldName, value);
                }
                if (alreadyExist) {
                    throw new ValidatorException(MessageFactory.getMessage(context, "info.bfly.core.validator.AlreadyExistValidator.ALREADY_EXIST", MessageFactory.getLabel(context, component), value));
                }
            }
            catch (SecurityException e) {
                log.debug(e.getMessage());
                throw new ValidatorException(MessageFactory.getMessage(context, "javax.faces.converter.NO_SUCH_FIELD", MessageFactory.getLabel(context, component), "get"
                        + StringUtils.capitalize(fieldName)));
            }
            catch (ClassNotFoundException e) {
                log.debug(e.getMessage());
                throw new ValidatorException(MessageFactory.getMessage(context, "javax.faces.converter.CLASS_NOT_FOUND", MessageFactory.getLabel(context, component), entityClass));
            }
            catch (NoSuchMethodException e) {
                log.debug(e.getMessage());
                throw new ValidatorException(MessageFactory.getMessage(context, "javax.faces.converter.NO_SUCH_FIELD", MessageFactory.getLabel(context, component), "get"
                        + StringUtils.capitalize(fieldName)));
            }
        }
    }
}
