package info.bfly.core.jsf.converter;

import info.bfly.archer.common.controller.EntityBase;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Id;

import org.hibernate.proxy.HibernateProxy;

@FacesConverter("archer.EntityConverter")
public class EntityConverter extends EntityBase implements Converter {
    private static final String ID_PREFIX = "entityConverter_";

    public Object getAnnotadedWithId(Object object) {
        return getAnnotadedWithId(object, object.getClass());
    }



    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null) {
            return getFromViewMap(context, component, value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object != null && !"".equals(object)) {
            String id;
            id = getId(object);
            if (id == null) {
                id = "";
            }
            id = id.trim();
            putInViewMap(id, context, component, object);
            return id;
        }
        return null;
    }

    public Object getFromViewMap(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.trim().isEmpty()) {
            Map objectsFromClass = (Map) context.getViewRoot().getViewMap().get(EntityConverter.ID_PREFIX + component.getId());
            if (objectsFromClass != null) {
                return objectsFromClass.get(value);
            }
        }
        return null;
    }

    /**
     * Get object ID
     *
     * @param Object object
     * @return String
     */
    public String getId(Object object) {
        try {
            if (object instanceof HibernateProxy) {
                return ((HibernateProxy) object).getHibernateLazyInitializer().getIdentifier().toString();
            }
            Object id = getAnnotadedWithId(object);
            if (id != null) {
                return id.toString();
            } else {
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void putInViewMap(String id, FacesContext context, UIComponent component, Object object) {
        if (object != null) {
            Map objectsFromClass = (Map) context.getViewRoot().getViewMap().get(EntityConverter.ID_PREFIX + component.getId());
            if (objectsFromClass == null) {
                objectsFromClass = new HashMap();
                context.getViewRoot().getViewMap().put(EntityConverter.ID_PREFIX + component.getId(), objectsFromClass);
            }
            objectsFromClass.put(id, object);
        }
    }
}
