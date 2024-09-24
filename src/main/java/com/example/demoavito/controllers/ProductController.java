package com.example.demoavito.controllers;

import com.example.demoavito.models.Image;
import com.example.demoavito.models.Product;
import com.example.demoavito.repositories.ImageRepository;
import com.example.demoavito.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor//-внедряет зависимости lombok
public class ProductController {
    private final ProductService productService;
    private final ImageRepository imageRepository;
    @GetMapping("/")
    public String products(@RequestParam(name="title",required = false) String title,Principal principal, Model model){
        model.addAttribute("product",productService.productList(title));
        model.addAttribute("user",productService.getUserByPrincipal(principal));
        return "product";
    }
    @GetMapping("/create")
    public String pageCreate(){
        return "createProduct";
    }
    @GetMapping("/product/{id}")
    public String selectProduct(@PathVariable Long id,Model model,Principal principal) {
        Product product=productService.getProductById(id);
        model.addAttribute("product",product);
       // log.info("");
        model.addAttribute("images",product.getImages());
        model.addAttribute("user",productService.getUserByPrincipal(principal));
        return "infoProduct";
    }
    @PostMapping("/product/create")
    public String productCreate(Product product, @RequestParam("file1")MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Principal principal) throws IOException {
        productService.saveProduct(product,principal,file1,file2,file3);
        return "redirect:/";
    }
@PostMapping("/product/delete/{id}")
        public String deleteProduct(@PathVariable Long id){
    productService.deleteProduct(id);
    return "redirect:/";
        }
        @GetMapping("/product/update/{id}")
        public String updateProduct(@PathVariable Long id,Model model) {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            model.addAttribute("images", product.getImages());
            return "updateProduct";
        }

        @PostMapping("/product/update/{id}")
    public  String updateProduct(@PathVariable Long id,Product product,@RequestParam(name = "file1",required = false)MultipartFile file1,
                                 @RequestParam(name = "file2", required = false)MultipartFile file2,
                                 @RequestParam(name = "file3",required = false)MultipartFile file3,Principal principal) throws IOException {
productService.updateProduct(id,principal,product,file1,file2,file3);
            return "redirect:/product/{id}";
            }
@GetMapping("/images/delete/{id}")
    public String deleteImage(@PathVariable Long id) {
        Long productId;
    try{
        Image imageIdProduct=imageRepository.findById(id).orElse(null);
        productId=imageIdProduct.getProduct().getId();
        imageRepository.deleteById(id);

        }catch (NullPointerException e){
            return "redirect:/";
        }
    return "redirect:/product/update/"+productId;
}


        }



