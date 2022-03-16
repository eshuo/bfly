package info.bfly.p2p.bankcard.service;

import info.bfly.p2p.bankcard.model.BankCard;

import java.util.List;

/**
 * Filename: BankService.java <br/>
 */
public interface BankCardService {
    /**
     * 获取当前用户绑定的银行卡
     *
     * @param userId 用户id
     * @return
     */
    List<BankCard> getBankCardsByUserId(String userId);
}
