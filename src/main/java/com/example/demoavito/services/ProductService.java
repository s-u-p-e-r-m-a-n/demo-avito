package com.example.demoavito.services;

import com.example.demoavito.models.Image;
import com.example.demoavito.models.Product;
import com.example.demoavito.models.User;
import com.example.demoavito.repositories.ProductRepository;
import com.example.demoavito.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public List<Product> productList(String title) {
        if (title != null)
            return productRepository.findByTitle(title);

        return productRepository.findAll();
    }

public void updateProduct(Long id, Principal principal,Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
product.setUser(getUserByPrincipal(principal));
LocalDateTime createLocalDateTime=LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String formattedDateTime = createLocalDateTime.format(formatter);
    createLocalDateTime = LocalDateTime.parse(formattedDateTime, formatter);

    Product products = productRepository.findById(id).orElse(null);
    products.setPrice(product.getPrice());
    products.setCity(product.getCity());
    products.setDescription(product.getDescription());
    products.setUser(product.getUser());
    products.setTitle(product.getTitle());
    products.setDateOfCreate(createLocalDateTime);
    Image image1;
    Image image2;
    Image image3;
    if(file1.getSize()!=0){
        image1=toImageEntity(file1);
        image1.setPreviewImage(true);
        products.addImageToProduct(image1);
    }
    if(file2.getSize()!=0){
        image2=toImageEntity(file2);
        products.addImageToProduct(image2);
    }
    if(file3.getSize()!=0){
        image3=toImageEntity(file3);
        products.addImageToProduct(image3);
    }

        Product productFromDB = productRepository.save(products);
        productFromDB.setPreviewImage(productFromDB.getImages().get(0).getId());

    productRepository.save(products);

}

    public User getUserByPrincipal(Principal principal) {
        if(principal==null) return new User();
      return   userRepository.findByEmail(principal.getName());
    }

    public void saveProduct(Product product, Principal principal,MultipartFile file1,MultipartFile file2,MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if(file1.getSize()!=0){
            image1=toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if(file2.getSize()!=0){
            image2=toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if(file3.getSize()!=0){
            image3=toImageEntity(file3);
            product.addImageToProduct(image3);
        }
        log.info("Сохранение нового продукта. Title:{},Author:{}", product.getTitle(),product.getUser());
        /* Создаем объект Product и сохраняем его в базу,далее получаем его из базы и
       записываем в поле setPreviewImage id его первой фотографии */
        if(product.getImages().size()!=0){
        Product productFromDB=productRepository.save(product);
            productFromDB.setPreviewImage(productFromDB.getImages().get(0).getId());
        }
        productRepository.save(product);

    }
private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
image.setName(file.getName());
image.setOriginalFileName(file.getOriginalFilename());
image.setContentType(file.getContentType());
image.setSize(file.getSize());
image.setBytes(file.getBytes());

return image;

}
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

}
