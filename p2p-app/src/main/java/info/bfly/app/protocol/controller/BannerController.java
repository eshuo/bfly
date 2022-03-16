package info.bfly.app.protocol.controller;

import info.bfly.api.model.BaseResource;
import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@RestController
@RequestMapping("/v1.0")
@Scope(ScopeType.REQUEST)
public class BannerController extends BaseResource {


}
