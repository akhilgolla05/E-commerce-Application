package com.learnboot.dreamshopping.service.product;

import com.learnboot.dreamshopping.dto.ImageDto;
import com.learnboot.dreamshopping.dto.ProductDto;
import com.learnboot.dreamshopping.exception.AlreadyExistsException;
import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.Category;
import com.learnboot.dreamshopping.models.Image;
import com.learnboot.dreamshopping.models.Product;
import com.learnboot.dreamshopping.repository.CategoryRepository;
import com.learnboot.dreamshopping.repository.ImageRepository;
import com.learnboot.dreamshopping.repository.ProductRepository;
import com.learnboot.dreamshopping.request.AddProductRequest;
import com.learnboot.dreamshopping.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {

        if(productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistsException(request.getName()+" "+ request.getBrand()+ " already exists, You May update Instead!");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                            return categoryRepository.save(new Category(request.getCategory().getName()));
                        }
                );
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));

    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {

        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductBYId(long id) {
        return productRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, long id) {
    return productRepository.findById(id)
            .map(existingProduct-> updateExistingProduct(existingProduct,request))
            .map(productRepository::save)
            .orElseThrow(()->new ResourceNotFoundException("product Not Found"));
    }

    private Product updateExistingProduct(Product product, ProductUpdateRequest request) {
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setInventory(request.getInventory());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        product.setCategory(category);
        return productRepository.save(product);
    }

    @Override
    public void deleteProductById(long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, ()->{
                    throw new ResourceNotFoundException("Product Not Found!");
                });
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findProductByBrandAndName(brand,name);
    }

    @Override
    public long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

    //converting Product to ProductDto
    @Override
    public List<ProductDto> convertProductsToProductDto(List<Product> products) {
        return products.stream().map(this::convertToProductDto).toList();
    }

    @Override
    public ProductDto convertToProductDto(Product product) {
        ProductDto productDto =  modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos =
                images.stream().map(image->modelMapper.map(image, ImageDto.class)).toList();

        productDto.setImages(imageDtos);
        return productDto;
    }
}
