package com.learnboot.dreamshopping.controller;

import com.learnboot.dreamshopping.dto.ProductDto;
import com.learnboot.dreamshopping.exception.AlreadyExistsException;
import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.Product;
import com.learnboot.dreamshopping.request.AddProductRequest;
import com.learnboot.dreamshopping.request.ProductUpdateRequest;
import com.learnboot.dreamshopping.response.ApiResponse;
import com.learnboot.dreamshopping.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = productService.convertProductsToProductDto(products);
        return ResponseEntity.ok(new ApiResponse("success", productDtos));
    }

    @GetMapping("product/{id}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductBYId(id);
           ProductDto productDto =  productService.convertToProductDto(product);
            return ResponseEntity.ok(new ApiResponse("success", productDto));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductRequest product) {
        try {
            Product product1 = productService.addProduct(product);
            ProductDto productDto =  productService.convertToProductDto(product1);
            return ResponseEntity.ok(new ApiResponse("Added Successfully", productDto));
        } catch (AlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ex.getMessage(), null));
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product, @PathVariable Long id) {
        try {
            Product product1 = productService.updateProduct(product, id);
            ProductDto productDto =  productService.convertToProductDto(product1);
            return ResponseEntity.ok(new ApiResponse("Updated Successfully", productDto));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.ok(new ApiResponse(ex.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted Successfully", null));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.ok(new ApiResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("/product/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName,
                                                                @RequestParam String productName) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product Not Found", null));
            }
            List<ProductDto> productDtos = productService.convertProductsToProductDto(products);
            return ResponseEntity.ok(new ApiResponse("Fetched Successfully", productDtos));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    @GetMapping("/product/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String categoryName,
                                                                     @RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(categoryName, brandName);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product Not Found", null));
            }
            List<ProductDto> productDtos = productService.convertProductsToProductDto(products);
            return ResponseEntity.ok(new ApiResponse("fetched Successfully", productDtos));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }


    @GetMapping("/by-brand/{brandName}")
    public ResponseEntity<ApiResponse> getProductsByBrand(@PathVariable String brandName) {
        try {
            List<Product> products = productService.getProductsByBrand(brandName);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product Not Found", null));
            }
            List<ProductDto> productDtos = productService.convertProductsToProductDto(products);
            return ResponseEntity.ok(new ApiResponse("fetched Successfully", productDtos));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }


    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> getAllProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product Not Found", null));
            }
            List<ProductDto> productDtos = productService.convertProductsToProductDto(products);
            return ResponseEntity.ok(new ApiResponse("fetched Successfully", productDtos));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brandName,
                                                                   @RequestParam String productName) {
        try {
            long products = productService.countProductsByBrandAndName(brandName,productName);
            if (products == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product Not Found", null));
            }
            return ResponseEntity.ok(new ApiResponse("fetched Successfully", products));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }





}
