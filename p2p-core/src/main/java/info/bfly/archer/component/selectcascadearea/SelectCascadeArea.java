package info.bfly.archer.component.selectcascadearea;

import javax.faces.component.UIComponentBase;

import org.springframework.stereotype.Component;

/**
 *
 *
 * Description:
 *
 * @version: 1.0 Create at: 2014-1-21 下午6:41:53
 *
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-21 wangzhi 1.0
 */
@Component(value = "selectCascadeArea")
public class SelectCascadeArea extends UIComponentBase {
    @Override
    public String getFamily() {
        return "info.bfly.archer.component.SelectCascadeArea";
    }
}
