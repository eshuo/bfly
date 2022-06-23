package info.bfly.archer.product.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.node.controller.NodeList;
import info.bfly.archer.product.ProductConstants;
import info.bfly.archer.product.model.Product;
import info.bfly.core.annotations.ScopeType;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 产品查询
 *
 */
@Component
@Scope(ScopeType.REQUEST)
public class ProductList extends NodeList {
    private static final long serialVersionUID = -1350682013319140386L;

    @Override
    public Class getEntityClass() {
        return Product.class;
    }

    @Override
    public String getNodeTypeId() {
        return ProductConstants.NodeType;
    }

    @Override
    public void init() {
        setCountHql("SELECT count(product) FROM Product product join product.categoryTerms term ");
        setHql("SELECT product FROM Product product join product.categoryTerms term");
        final String[] RESTRICTIONS = {"product.id like #{productList.example.id}", "product.title like #{productList.example.title}", "product.nodeType.id = #{productList.example.nodeType.id}",
                "product in elements(term.nodes) and term.id = #{productList.example.categoryTerms[0].id}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        addOrder("product.updateTime", EntityQuery.DIR_DESC);
    }
}
