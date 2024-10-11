package com.learnboot.dreamshopping.service.product;

import com.learnboot.dreamshopping.dto.ProductDto;
import com.learnboot.dreamshopping.models.Product;
import com.learnboot.dreamshopping.request.AddProductRequest;
import com.learnboot.dreamshopping.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);
    List<Product> getAllProducts();
    Product getProductBYId(long id);
    Product updateProduct(ProductUpdateRequest product, long id);
    void deleteProductById(long id);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    long countProductsByBrandAndName(String brand, String name);

    //converting Product to ProductDto
    List<ProductDto> convertProductsToProductDto(List<Product> products);

    //converting Product to ProductDto
    ProductDto convertToProductDto(Product product);
}
