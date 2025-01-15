package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.controller.DuplicateEntityException;
import com.iyadsoft.billing_craft_backend.dto.ProductDetailDTO;
import com.iyadsoft.billing_craft_backend.dto.ProductStockCountDTO;
import com.iyadsoft.billing_craft_backend.entity.Pricedrop;
import com.iyadsoft.billing_craft_backend.entity.ProductStock;
import com.iyadsoft.billing_craft_backend.repository.ProductStockRepository;

import jakarta.transaction.Transactional;

import com.iyadsoft.billing_craft_backend.repository.PricedropRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductStockService {
    private final ProductStockRepository productStockRepository;
    private final PricedropRepository pricedropRepository;

    @Transactional
    public void handlePriceDrop(String username, String supplier, String productName, String save, Double pprice, Double newPprice,
            Double newSprice) {
        // Step 1: Fetch data from ProductStock
        List<ProductStock> productStocks = productStockRepository
                .findByUsernameAndSupplierAndProductNameAndPprice(username, supplier, productName, pprice);

        for (ProductStock productStock : productStocks) {
            // Step 2: Check duplicate data in Pricedrop
            if (!pricedropRepository.existsByUsernameAndProductno(username, productStock.getProductno())) {
                if ("yes".equalsIgnoreCase(save)) {
                Pricedrop pricedrop = new Pricedrop();
                pricedrop.setUsername(username);
                pricedrop.setCategory(productStock.getCategory());
                pricedrop.setBrand(productStock.getBrand());
                pricedrop.setProductName(productStock.getProductName());
                pricedrop.setOldpprice(productStock.getPprice());
                pricedrop.setNewpprice(newPprice);
                pricedrop.setSupplier(productStock.getSupplier());
                pricedrop.setProductno(productStock.getProductno());
                pricedrop.setDate(LocalDate.now());
                pricedropRepository.save(pricedrop);
                }
                // Step 4: Update pprice and sprice in ProductStock
                productStock.setPprice(newPprice);
                productStock.setSprice(newSprice);
                productStockRepository.save(productStock);
            } else {
                throw new DuplicateEntityException(
                        "Product " + productStock.getProductno() + " is already in pricedrop!");
            }
        }
    }

    @Transactional
    public void handlePriceUp(String username, String supplier, String productName, Double pprice, Double newSprice) {
        // Step 1: Fetch data from ProductStock
        List<ProductStock> productStocks = productStockRepository
                .findByUsernameAndSupplierAndProductNameAndPprice(username, supplier, productName, pprice);

        for (ProductStock productStock : productStocks) {
            productStock.setSprice(newSprice);
            productStockRepository.save(productStock);
        }
    }

    public List<ProductStockCountDTO> getProductCountByUserAndGroup(String username) {
        LocalDate today = LocalDate.now();
        return productStockRepository.countProductByUsernameGroupByCategoryBrandProductName(username, today);
    }
    
    public List<Pricedrop> getPricedropsByUsername(String username) {
        return pricedropRepository.findByUsername(username);
    }

    public List<ProductStock> getProductsNotInSalesStock(String username, String productno) {
        return productStockRepository.findProductsNotInSalesStock(username, productno);
    }

    // public ProductStock updateProductStock(Long proId, ProductStock updatedProduct) {
    //     Optional<ProductStock> existingProductOpt = productStockRepository.findById(proId);

    //     if (existingProductOpt.isPresent()) {
    //         ProductStock existingProduct = existingProductOpt.get();
           
    //         existingProduct.setCategory(updatedProduct.getCategory());
    //         existingProduct.setBrand(updatedProduct.getBrand());
    //         existingProduct.setProductName(updatedProduct.getProductName());
    //         existingProduct.setPprice(updatedProduct.getPprice());
    //         existingProduct.setSprice(updatedProduct.getSprice());
    //         existingProduct.setColor(updatedProduct.getColor());
    //         existingProduct.setSupplier(updatedProduct.getSupplier());
    //         existingProduct.setSupplierInvoice(updatedProduct.getSupplierInvoice());
    //         existingProduct.setProductno(updatedProduct.getProductno());
    //         existingProduct.setDate(updatedProduct.getDate());
          
    //         return productStockRepository.save(existingProduct);
    //     } else {
    //         throw new RuntimeException("Product with proId " + proId + " not found.");
    //     }
       
    // }

    public ProductStock updateProductStock(Long proId, ProductStock updatedProduct) {
        Optional<ProductStock> existingProductOpt = productStockRepository.findById(proId);
    
        if (existingProductOpt.isPresent()) {
            ProductStock existingProduct = existingProductOpt.get();
    
            boolean productNoExists = productStockRepository.existsByProductnoAndProIdNot(updatedProduct.getProductno(), proId);
            if (productNoExists) {
                
                throw new RuntimeException("Product number " + updatedProduct.getProductno() + " already exists.");
            }
    
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setBrand(updatedProduct.getBrand());
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setPprice(updatedProduct.getPprice());
            existingProduct.setSprice(updatedProduct.getSprice());
            existingProduct.setColor(updatedProduct.getColor());
            existingProduct.setSupplier(updatedProduct.getSupplier());
            existingProduct.setSupplierInvoice(updatedProduct.getSupplierInvoice());
            existingProduct.setProductno(updatedProduct.getProductno());
            existingProduct.setDate(updatedProduct.getDate());
    
            return productStockRepository.save(existingProduct);
        } else {
            throw new RuntimeException("Product with proId " + proId + " not found.");
        }
    }
    

    public List<ProductDetailDTO> getAllProductOccurrences(String username, String productno) {
        return productStockRepository.findAllProductOccurrences(username, productno);
    }
}
