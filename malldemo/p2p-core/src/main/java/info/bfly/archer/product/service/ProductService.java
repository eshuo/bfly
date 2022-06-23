package info.bfly.archer.product.service;

import info.bfly.archer.product.model.Product;
import info.bfly.archer.product.model.ProductPicture;

public interface ProductService {
    ProductPicture addProductPicture(String productId, String picture);

    void deleteProductPicture(ProductPicture productPicture);

    void deleteProductPicture(String productPictureId);

    void save(Product product);

    void saveProductPicture(ProductPicture pp);
}
