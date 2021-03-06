package info.bfly.app.protocol.controller;

import com.fasterxml.jackson.databind.module.SimpleModule;
import info.bfly.api.exception.ParameterExection;
import info.bfly.api.exception.UserOperationExection;
import info.bfly.api.model.BaseResource;
import info.bfly.api.model.In;
import info.bfly.api.model.Out;
import info.bfly.api.service.ApiService;
import info.bfly.app.protocol.model.request.InvestValue;
import info.bfly.app.protocol.model.request.LoanValue;
import info.bfly.app.protocol.model.request.RequestPage;
import info.bfly.app.protocol.model.request.UserValue;
import info.bfly.app.protocol.model.response.ResponsePage;
import info.bfly.app.protocol.model.serializer.*;
import info.bfly.app.protocol.service.*;
import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.exception.*;
import info.bfly.archer.common.service.AuthService;
import info.bfly.archer.key.ResponseMsg;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserBill;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.DateUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.ImageUploadUtil;
import info.bfly.p2p.coupon.model.UserCoupon;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.loan.model.WithdrawCash;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/6 0006.
 */
@Controller
@RequestMapping("/v1.0")
@Scope(ScopeType.REQUEST)
public class UserController extends BaseResource {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ApiLoanService loanService;
    @Autowired
    private ApiInvestListService apiInvestListService;

    @Autowired
    private UserInfoSerializer userInfoSerializer;

    @Autowired
    private ApiBillListService apiBillListService;

    private String errorMessage = "";

    @Log
    Logger log;

    /**
     * ?????????????????????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/userInvest")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public Out getUserInvest(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        Out out = apiService.parseOut(request);
        InvestValue investValue = in.getFinalValue(InvestValue.class);
        User user = loadUserFromSecurityContext();
        apiInvestListService.getExample().getUser().setId(user.getId());
        apiInvestListService.setCurrentPage(investValue.getPage().getCurrentPage());
        apiInvestListService.setPageSize(investValue.getPage().getSize());
        apiInvestListService.addOrder(investValue.getPage().getOrder());
        if (investValue.getStatus() != null) {
            apiInvestListService.getExample().setStatus(investValue.getStatus());
        }
        ResponsePage page = new ResponsePage(apiInvestListService);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Invest.class, new InvestSerializer(true));
        out.setResult(page, module);
        log.info("{} {} {}", loadUserFromSecurityContext().getId(), "?????????????????????????????????", "????????????");
        return out;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/userLoanList")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public Out getUserLoanList(HttpServletRequest request) {
        Out out = apiService.parseOut(request);
        In in = apiService.parseIn(request);
        LoanValue loanValue = in.getFinalValue(LoanValue.class);
        User user = loadUserFromSecurityContext();
        loanService.getExample().getUser().setId(user.getId());
        loanService.setPageSize(loanValue.getPage().getSize());
        loanService.setCurrentPage(loanValue.getPage().getCurrentPage());
        loanService.addOrder(loanValue.getPage().getOrder());
        if (loanValue.getStatus() != null) {
            loanService.getExample().setStatus(loanValue.getStatus());
        } else {
            loanService.getExample().setStatus(loanValue.DEFALT_STATUS);
        }
        ResponsePage page = new ResponsePage(loanService);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Loan.class, new LoanSerializer());
        out.setResult(page, module);
        log.info("{} {} {}", loadUserFromSecurityContext().getId(), "????????????????????????", "????????????");
        return out;
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/userInfo")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public Out getUserInfo(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        User user = loadUserFromSecurityContext();
        Out out = apiService.parseOut(request);
        out.setResult(user, new SimpleModule().addSerializer(User.class, userInfoSerializer));
        log.info("{} {} {}", loadUserFromSecurityContext().getId(), "??????????????????????????????", "????????????");
        return out;
    }

    /**
     * ????????????
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/billList")
    @PreAuthorize("hasRole('USER')")
    public Out getBillList(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        RequestPage requestPage = in.getFinalValue(RequestPage.class);

        apiBillListService.getExample().getUser().setId(loadUserFromSecurityContext().getId());
        apiBillListService.setCurrentPage(requestPage.getCurrentPage());
        apiBillListService.setPageSize(requestPage.getSize());
        if (requestPage.getOrder() != null && requestPage.getOrder().size() != 0) {
            apiBillListService.addOrder(requestPage.getOrder());
        }

        ResponsePage page = new ResponsePage(apiBillListService);

        SimpleModule module = new SimpleModule();

        module.addSerializer(UserBill.class, new UserBillListSerializer());

        Out out = apiService.parseOut(request);

        out.setResult(page, module);


        log.info("{} {} {}", loadUserFromSecurityContext().getId(), "????????????", "????????????");
        return out;
    }


    @Autowired
    private UserService userService;

    /**
     * ?????????????????????
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getTokenCode")
    public Out getTokenCode(HttpServletRequest request) {
        In in = apiService.parseIn(request);

        UserValue userValue = in.getFinalValue(UserValue.class);
        Out out = apiService.parseOut(request);
        if (userValue.getMobileNumber() == null) {
            throw new ParameterExection("???????????????");
        }
        try {
            userService.getUserByMobileNumber(userValue.getMobileNumber());
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "??????????????????");
        } catch (UserNotFoundException e) {
            try {
                userService.sendRegisterByMobileNumberSMS(userValue.getMobileNumber());
                return out;
            } catch (AuthInfoAlreadyInColdException e2) {
                throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "???????????????????????????");
            }
        }
    }

    @Autowired
    IdGenerator idGenerator;

    /**
     * ????????????
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveUser")
    public Out saveUser(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        Out out = apiService.parseOut(request);
        UserValue userValue = in.getFinalValue(UserValue.class);
        User user = new User();
        if (userValue.getCheckData()) {
            user.setUsername(idGenerator.returnUserId("userId", "JF"));
            user.setPassword(userValue.getPassword());
            user.setMobileNumber(userValue.getMobileNumber());

            try {
                userService.registerByMobileNumber(user, userValue.getVeriCode(), userValue.getReferrer());
            } catch (NoMatchingObjectsException | AuthCodeNotMatchException e) {
                throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "???????????????");
            } catch (AuthInfoOutOfDateException | AuthInfoOutOfTimesException e) {
                throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "???????????????");
            } catch (AuthInfoAlreadyActivedException e) {
                throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "???????????????");
            }
            return out;
        } else {
            throw new UserOperationExection("????????????");
        }


    }

    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/modifyPassword")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out modifyPassword(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        UserValue userValue = in.getFinalValue(UserValue.class);

        User user = loadUserFromSecurityContext();

        if (user == null) {
            throw new UserOperationExection(ResponseMsg.OBJ_NOT_FIND);
        }

        if (userValue.getPassword() == null) {
            throw new ParameterExection("password");
        }

        if (userValue.getOldPassword() == null) {
            throw new ParameterExection("oldPassword");
        }
        // TODO ???????????????

        userService.modifyPassword(user.getId(), userValue.getPassword());

        return apiService.parseOut(request);
    }

    @Autowired
    private ApiUserCouponListService apiUserCouponListService;

    /**
     * ?????????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/getUserCoupon")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out getUserCoupon(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        Out out = apiService.parseOut(request);
        RequestPage requestPage = in.getFinalValue(RequestPage.class);
        apiUserCouponListService.getExample().setUser(loadUserFromSecurityContext());

        apiUserCouponListService.setPageSize(requestPage.getSize());
        apiUserCouponListService.setCurrentPage(requestPage.getCurrentPage());

        ResponsePage page = new ResponsePage(apiUserCouponListService);

        SimpleModule module = new SimpleModule();

        module.addSerializer(UserCoupon.class, new UserCouponSerialzer());

        out.setResult(page, module);

        return out;
    }


    @Autowired
    private ApiSinaWithdrawList apiSinaWithdrawList;


    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/getWithdrawList")
    @ResponseBody
    @PreAuthorize("hasRole('INVESTOR')")
    public Out getWithdrawList(HttpServletRequest request) {


        In in = apiService.parseIn(request);


        InvestValue investValue = in.getFinalValue(InvestValue.class);

        apiSinaWithdrawList.getExample().setUser(loadUserFromSecurityContext());

        apiSinaWithdrawList.setCurrentPage(investValue.getPage().getCurrentPage());
        apiSinaWithdrawList.setPageSize(investValue.getPage().getSize());
        apiSinaWithdrawList.addOrder(investValue.getPage().getOrder());
        if (StringUtils.isNotEmpty(investValue.getStatus())) {
            apiSinaWithdrawList.getExample().setStatus(investValue.getStatus());
        }

        Out out = apiService.parseOut(request);
        ResponsePage page = new ResponsePage(apiSinaWithdrawList);


        SimpleModule module = new SimpleModule();

        module.addSerializer(WithdrawCash.class, new WithdrawCashSerializer());

        out.setResult(page, module);

        return out;
    }

    @Autowired
    private ApiRechargeListService apiRechargeListService;

    /**
     * ????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/getRechargeList")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out getRechargeList(HttpServletRequest request) {

        In in = apiService.parseIn(request);


        InvestValue investValue = in.getFinalValue(InvestValue.class);
        apiRechargeListService.getExample().setUser(loadUserFromSecurityContext());

        apiRechargeListService.setCurrentPage(investValue.getPage().getCurrentPage());
        apiRechargeListService.setPageSize(investValue.getPage().getSize());
        apiRechargeListService.addOrder(investValue.getPage().getOrder());
        if (StringUtils.isNotEmpty(investValue.getStatus())) {
            apiRechargeListService.getExample().setStatus(investValue.getStatus());
        }

        Out out = apiService.parseOut(request);
        ResponsePage page = new ResponsePage(apiRechargeListService);


        SimpleModule module = new SimpleModule();

        module.addSerializer(Recharge.class, new RechargeSerializer());

        out.setResult(page, module);

        return out;
    }


    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/getUserRoles")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out getUserRoles(HttpServletRequest request) {


        User user = loadUserFromSecurityContext();

        List<Role> roles = user.getRoles();

        Out out = apiService.parseOut(request);
        SimpleModule module = new SimpleModule();

        module.addSerializer(Role.class, new roleSerializer());

        out.setResult(roles, module);
        return out;
    }

    @Autowired
    private AuthService authService;

    /**
     * ????????????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/findPwdToMobileCode")
    @ResponseBody
    public Out findPwdToMobileCode(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        Out out = apiService.parseOut(request);

        UserValue finalValue = in.getFinalValue(UserValue.class);
        if (finalValue.getMobileNumber() == null) {
            throw new ParameterExection("???????????????");
        }

        User user = userService.getUserByMobileNumber(finalValue.getMobileNumber());

        if (user == null) {
            throw new ParameterExection("??????????????????");
        }

        try {
            String mobile = finalValue.getMobileNumber();

            User byMobileNumberUser = userService.getUserByMobileNumber(mobile);

            userService.sendFindLoginPasswordByMobileNumberSMS(byMobileNumberUser.getId(), mobile);

            return out;
        } catch (AuthInfoAlreadyInColdException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "???????????????????????????????????????");
        } catch (Exception e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "???????????????????????????????????????");
        }
    }


    /**
     * ???????????????????????????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/checkFindPwdAuthCode")
    @ResponseBody
    public Out checkFindPwdAuthCode(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        Out out = apiService.parseOut(request);

        UserValue userValue = in.getFinalValue(UserValue.class);

        if (userValue.getMobileNumber() == null) {
            throw new ParameterExection("???????????????");
        }

        if (userValue.getVeriCode() == null) {
            throw new ParameterExection("???????????????");
        }

        String uuid = IdGenerator.randomUUID();

        String mobile = userValue.getMobileNumber();
        String authCode = userValue.getVeriCode();
        User byMobileNumberUser = userService.getUserByMobileNumber(mobile);

        try {
            authService.verifyAuthInfo(byMobileNumberUser.getId(), mobile, authCode, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE);
            authService.createAuthInfo(byMobileNumberUser.getId(), mobile, DateUtil.add(new Date(), "10"), CommonConstants.AuthInfoType.LOGIN_PASSWORD_BY_MOBILE_AUTHCODE, uuid);
            out.setResultCode(ResponseMsg.NEED_CONTINUE_REQUEST);
            out.setResult(uuid);
            return out;
        } catch (AuthInfoAlreadyInColdException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "???????????????????????????");
        } catch (AuthCodeNotMatchException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "????????????,?????????????????????");
        } catch (AuthInfoOutOfDateException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "????????????,??????????????????");
        } catch (AuthInfoAlreadyActivedException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "????????????,??????????????????????????????");
        } catch (AuthInfoOutOfTimesException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "????????????,????????????????????????");
        } catch (NoMatchingObjectsException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "????????????,????????????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/setFindPwd")
    @ResponseBody
    public Out setFindPwd(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        Out out = apiService.parseOut(request);

        UserValue userValue = in.getFinalValue(UserValue.class);

        if (userValue.getTicket() == null) {
            throw new ParameterExection("????????????");
        }

        if (userValue.getMobileNumber() == null) {
            throw new ParameterExection("???????????????");
        }

        if (userValue.getPassword() == null) {
            throw new ParameterExection("????????????");
        }

        User user = userService.getUserByMobileNumber(userValue.getMobileNumber());

        if (user == null) {
            throw new ParameterExection("???????????????");
        }

        try {
            authService.verifyAuthInfo(user.getId(), userValue.getMobileNumber(), userValue.getTicket(), CommonConstants.AuthInfoType.LOGIN_PASSWORD_BY_MOBILE_AUTHCODE);
            userService.modifyPassword(user.getId(), userValue.getPassword());
            return out;
        } catch (NoMatchingObjectsException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "????????????????????????");
        } catch (AuthInfoOutOfDateException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "?????????????????????");
        } catch (AuthInfoAlreadyActivedException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "???????????????????????????");
        } catch (AuthInfoOutOfTimesException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "?????????????????????");
        } catch (AuthCodeNotMatchException e) {
            throw new UserOperationExection(ResponseMsg.ILLEGAL_SIGN, "???????????????");
        }

    }


    /**
     * ??????????????????
     *
     * @param request
     * @param myfiles
     * @return
     */
    @RequestMapping("/steImages")
    @ResponseBody
    public Out steImages(HttpServletRequest request, @RequestParam MultipartFile[] myfiles) {
        Out out = apiService.parseOut(request);
        Map<String, String> map = new HashMap<String, String>();
        for (MultipartFile myfile : myfiles) {
            String imagePath = "";
            if (myfile.isEmpty()) {
                throw new UserOperationExection(ResponseMsg.FILE_UPLOAD_ERROR, "?????????????????????!");
            } else {
                InputStream inputStream = null;
                String name = "";
                try {
                    inputStream = myfile.getInputStream();
                    name = myfile.getName();
                    imagePath = ImageUploadUtil.upload(inputStream, name, loadUserIpSecurityContext() == null ? "" : "/bAuth/" + loadUserFromSecurityContext().getId());
                    errorMessage = "???????????????????????????";
                } catch (IOException e) {
                    throw new UserOperationExection(ResponseMsg.FILE_UPLOAD_ERROR, e.getMessage());
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
                log.info("{} {} {}", "name:" + name, "??????????????????", errorMessage);
                map.put(name, imagePath);
            }
        }
        out.setResult(map);
        return out;
    }


}
