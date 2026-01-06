package com.example.apiserver.controller;

import com.example.apiserver.common.Response;
import com.example.apiserver.entity.Product;
import com.example.apiserver.service.MinioService;
import com.example.apiserver.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private MinioService minioService;

    @PostMapping
    public Response<Product> createProduct(@RequestBody Product product) {
        logger.info("开始创建商品，请求数据: {}", product);
        try {
            Product createdProduct = productService.createProduct(product);
            logger.info("商品创建成功，ID: {}", createdProduct.getId());
            return Response.success(createdProduct);
        } catch (Exception e) {
            logger.error("商品创建失败: {}", e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @GetMapping
    public Response<List<Product>> getAllProducts() {
        logger.info("开始查询所有商品");
        try {
            List<Product> products = productService.getAllProducts();
            logger.info("查询所有商品成功，共找到 {} 个商品", products.size());
            return Response.success(products);
        } catch (Exception e) {
            logger.error("查询所有商品失败: {}", e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Response<Product> getProductById(@PathVariable Long id) {
        logger.info("开始根据ID查询商品，ID: {}", id);
        try {
            Product product = productService.getProductById(id);
            logger.info("根据ID查询商品成功，商品信息: {}", product);
            return Response.success(product);
        } catch (Exception e) {
            logger.error("根据ID查询商品失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public Response<List<Product>> getProductsByCategory(@PathVariable String category) {
        logger.info("开始根据分类查询商品，分类: {}", category);
        try {
            List<Product> products = productService.getProductsByCategory(category);
            logger.info("根据分类查询商品成功，分类: {}, 共找到 {} 个商品", category, products.size());
            return Response.success(products);
        } catch (Exception e) {
            logger.error("根据分类查询商品失败，分类: {}, 错误: {}", category, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public Response<List<Product>> getProductsByUserId(@PathVariable Long userId) {
        logger.info("开始根据用户ID查询商品，用户ID: {}", userId);
        try {
            List<Product> products = productService.getProductsByUserId(userId);
            logger.info("根据用户ID查询商品成功，用户ID: {}, 共找到 {} 个商品", userId, products.size());
            return Response.success(products);
        } catch (Exception e) {
            logger.error("根据用户ID查询商品失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/search")
    public Response<List<Product>> searchProducts(@RequestParam String keyword) {
        logger.info("开始搜索商品，关键词: {}", keyword);
        try {
            List<Product> products = productService.searchProducts(keyword);
            logger.info("搜索商品成功，关键词: {}, 共找到 {} 个商品", keyword, products.size());
            return Response.success(products);
        } catch (Exception e) {
            logger.error("搜索商品失败，关键词: {}, 错误: {}", keyword, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Response<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("开始更新商品，ID: {}, 更新数据: {}", id, product);
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            logger.info("商品更新成功，ID: {}", id);
            return Response.success(updatedProduct);
        } catch (Exception e) {
            logger.error("商品更新失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteProduct(@PathVariable Long id) {
        logger.info("开始删除商品，ID: {}", id);
        try {
            productService.deleteProduct(id);
            logger.info("商品删除成功，ID: {}", id);
            return Response.success();
        } catch (Exception e) {
            logger.error("商品删除失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
    
    /**
     * 上传单张图片到MinIO
     */
    @PostMapping("/upload")
    public Response<String> uploadImage(@RequestParam("file") MultipartFile file) {
        logger.info("开始上传单张图片，文件名: {}", file.getOriginalFilename());
        try {
            String imageUrl = minioService.uploadImage(file);
            logger.info("图片上传成功，URL: {}", imageUrl);
            return Response.success(imageUrl);
        } catch (Exception e) {
            logger.error("图片上传失败: {}", e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
    
    /**
     * 批量上传图片到MinIO
     */
    @PostMapping("/upload/batch")
    public Response<String[]> uploadImages(@RequestParam("files") MultipartFile[] files) {
        logger.info("开始批量上传图片，文件数量: {}", files.length);
        try {
            String[] imageUrls = minioService.uploadImages(files);
            logger.info("批量图片上传成功，URL数量: {}", imageUrls.length);
            return Response.success(imageUrls);
        } catch (Exception e) {
            logger.error("批量图片上传失败: {}", e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
}