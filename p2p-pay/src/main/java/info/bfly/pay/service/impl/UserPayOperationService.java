package info.bfly.pay.service.impl;

import info.bfly.core.annotations.ScopeType;
import info.bfly.pay.bean.BaseSinaEntity;
import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.service.AbstractPayOperationService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by XXSun on 2017/1/12.
 */
@Service
@Scope(ScopeType.REQUEST)
public class UserPayOperationService<T extends SinaInEntity, E extends BaseSinaEntity> extends AbstractPayOperationService<T,E> {

}
