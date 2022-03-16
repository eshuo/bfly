package info.bfly.archer.product.service.impl;

import info.bfly.archer.product.model.Product;
import info.bfly.archer.product.model.ProductPicture;
import info.bfly.archer.product.service.ProductService;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.ImageUploadUtil;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;

    @Override
    public ProductPicture addProductPicture(String productId, String picture) {
        ProductPicture pp = new ProductPicture();
        pp.setId(IdGenerator.randomUUID());
        pp.setPicture(picture);
        Product p = new Product();
        p.setId(productId);
        pp.setProduct(p);
        // ht.save(pp);
        return pp;
    }

    @Override
    public void deleteProductPicture(ProductPicture productPicture) {
        ImageUploadUtil.delete(FacesUtil.getAppRealPath() + productPicture.getPicture());
        ProductPicture pp = ht.get(ProductPicture.class, productPicture.getId());
        if (pp != null) {
            ht.delete(pp);
        }
    }

    @Override
    public void deleteProductPicture(String productPictureId) {
        ProductPicture pp = ht.get(ProductPicture.class, productPictureId);
        deleteProductPicture(pp);
    }

    @Override
    @Transactional
    public void save(Product product) {
        ht.merge(product);
    }

    @Override
    public void saveProductPicture(ProductPicture pp) {
        ht.save(pp);
    }
}
