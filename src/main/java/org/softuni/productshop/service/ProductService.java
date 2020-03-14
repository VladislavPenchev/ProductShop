package org.softuni.productshop.service;

import org.softuni.productshop.domain.models.service.ProductServiceModel;

import java.util.List;

public interface ProductService {

    void addProduct(ProductServiceModel productServiceModel);

    List<ProductServiceModel> allProducts();

    ProductServiceModel details(String id);

    ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel);

    void deleteProduct(String id);

    List<ProductServiceModel> findAllByCategory(String category);

}
