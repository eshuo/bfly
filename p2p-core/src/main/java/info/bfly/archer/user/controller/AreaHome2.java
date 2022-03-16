package info.bfly.archer.user.controller;

import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class AreaHome2 extends AreaHome implements Serializable {
    private static final long serialVersionUID = -9028134079103290282L;
}
