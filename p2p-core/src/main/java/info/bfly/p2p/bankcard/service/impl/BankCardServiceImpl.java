package info.bfly.p2p.bankcard.service.impl;

import info.bfly.p2p.bankcard.model.BankCard;
import info.bfly.p2p.bankcard.service.BankCardService;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XXSun on 2/24/2017.
 */
@Service
public class BankCardServiceImpl implements BankCardService{
    @Resource
    HibernateTemplate ht;


    @Override
    public List<BankCard> getBankCardsByUserId(String userId) {
        List<?> bankcards = ht.findByNamedQuery("BankCard.getBankCardListByUserId", userId);
        return (List<BankCard>) bankcards;
    }
}
