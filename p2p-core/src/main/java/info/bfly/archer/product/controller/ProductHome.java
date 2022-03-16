package info.bfly.archer.product.controller;

import info.bfly.archer.node.controller.NodeHome;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.node.model.NodeBody;
import info.bfly.archer.node.model.NodeType;
import info.bfly.archer.product.ProductConstants;
import info.bfly.archer.product.model.Product;
import info.bfly.archer.product.model.ProductPicture;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class ProductHome extends NodeHome {
    private static final long          serialVersionUID = 4691071507053326952L;
    private static final StringManager sm               = StringManager.getManager(ProductConstants.Package);

    public ProductHome() {
        setUpdateView(FacesUtil.redirect(ProductConstants.View.PRODUCT_LIST));
    }

    @Override
    public Node createInstance() {
        Product product = new Product();
        product.setNodeType(new NodeType());
        product.setNodeBody(new NodeBody());
        product.setProductPictures(new ArrayList<ProductPicture>());
        return product;
    }

    @Override
    public Class getEntityClass() {
        return Product.class;
    }

    @Override
    public String save() {
        Product p = (Product) this.getInstance();
        if (p.getProductPictures() == null || p.getProductPictures().size() == 0) {
            FacesUtil.addErrorMessage(ProductHome.sm.getString("picutureNullError"));
            return null;
        }
        for (ProductPicture pp : p.getProductPictures()) {
            pp.setProduct(p);
        }
        return super.save();
    }
}
