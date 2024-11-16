package com.iyadsoft.billing_craft_backend.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO;
import com.iyadsoft.billing_craft_backend.dto.InvoiceDataDTO;
import com.iyadsoft.billing_craft_backend.dto.ProductStockCountDTO;
import com.iyadsoft.billing_craft_backend.entity.BrandName;
import com.iyadsoft.billing_craft_backend.entity.CategoryName;
import com.iyadsoft.billing_craft_backend.entity.ColorName;
import com.iyadsoft.billing_craft_backend.entity.ProductStock;
import com.iyadsoft.billing_craft_backend.entity.ProductName;
import com.iyadsoft.billing_craft_backend.entity.SupplierName;
import com.iyadsoft.billing_craft_backend.entity.Vat;
import com.iyadsoft.billing_craft_backend.repository.BrandNameRepository;
import com.iyadsoft.billing_craft_backend.repository.CategoryNameRepository;
import com.iyadsoft.billing_craft_backend.repository.ColorNameRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductNameRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductStockRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductSaleRepository;
import com.iyadsoft.billing_craft_backend.repository.SupplierNameRepository;
import com.iyadsoft.billing_craft_backend.service.ProductStockService;
import com.iyadsoft.billing_craft_backend.service.VatService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductStockRepository productRepository;
    private final ProductNameRepository productNameRepository;
    private final ColorNameRepository colorNameRepository;
    private final SupplierNameRepository supplierNameRepository;
    private final ProductSaleRepository productSaleRepository;
    private final CategoryNameRepository categoryNameRepository;
    private final BrandNameRepository brandNameRepository;
    

   
    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private VatService vatService;

    @Autowired
    ProductController(ProductStockRepository productRepository, ProductNameRepository productNameRepository,
            ColorNameRepository colorNameRepository, SupplierNameRepository supplierNameRepository,
            ProductSaleRepository productSaleRepository, CategoryNameRepository categoryNameRepository,
            BrandNameRepository brandNameRepository) {
        this.productRepository = productRepository;
        this.productNameRepository = productNameRepository;
        this.colorNameRepository = colorNameRepository;
        this.supplierNameRepository = supplierNameRepository;
        this.productSaleRepository = productSaleRepository;
        this.categoryNameRepository = categoryNameRepository;
        this.brandNameRepository = brandNameRepository;
       
    }


@PostMapping("/addProducts")
@Transactional
public ResponseEntity<List<ProductStock>> newProducts(@RequestBody List<ProductStock> newProducts) {
    List<ProductStock> savedProducts = new ArrayList<>();
    ZonedDateTime dhakaTime = ZonedDateTime.now(ZoneId.of("Asia/Dhaka"));

    for (ProductStock product : newProducts) {
        if (productRepository.existsByUsernameAndProductno(product.getUsername(), product.getProductno())) {
            throw new DuplicateEntityException("Product " + product.getProductno() + " is already exists!");
        }
        product.setTime(dhakaTime.toLocalTime());
        savedProducts.add(product);
    }
    
    savedProducts = productRepository.saveAll(savedProducts);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedProducts);
}
 
    @PostMapping("/addNewCategory")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryName categoryName) {
        if (categoryNameRepository.existsByUsernameAndCategoryItem(categoryName.getUsername(),
                categoryName.getCategoryItem())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Sorry, this category is already exists!");

        }
        CategoryName savedCategory = categoryNameRepository.save(categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @PostMapping("/addNewBrand")
    public ResponseEntity<?> saveBrand(@RequestBody BrandName brandName) {
        if (brandNameRepository.existsByUsernameAndBrandItem(brandName.getUsername(),
                brandName.getBrandItem())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Sorry, this brand is already exists!");

        }
        BrandName savedBrand = brandNameRepository.save(brandName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
    }

    @PostMapping("/addNewProduct")
    public ResponseEntity<ProductName> saveProduct(@RequestBody ProductName productName) {
        if (productNameRepository.existsByUsernameAndProductItem(productName.getUsername(),
                productName.getProductItem())) {
            throw new DuplicateEntityException("Product " + productName.getProductItem() + " is already exists !");

        }
        ProductName savedProduct = productNameRepository.save(productName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PostMapping("/addNewColor")
    public ResponseEntity<ColorName> saveColor(@RequestBody ColorName colorName) {
        if (colorNameRepository.existsByUsernameAndColorItem(colorName.getUsername(),
                colorName.getColorItem())) {
            throw new DuplicateEntityException("Color " + colorName.getColorItem() + " is already exists !");

        }
        ColorName savedColor = colorNameRepository.save(colorName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedColor);
    }

    @PostMapping("/addNewSupplier")
    public ResponseEntity<String> saveSupplier(@RequestBody SupplierName supplierName) {
        if (supplierNameRepository.existsByUsernameAndSupplierItem(supplierName.getUsername(),
                supplierName.getSupplierItem())) {
            throw new DuplicateEntityException("Supplier " + supplierName.getSupplierItem() + " is already exists !");

        }
        supplierNameRepository.save(supplierName);
        return ResponseEntity.ok("Supplier Added Successfully");
        // return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
    }

    @GetMapping("/getCategoryItem")
    public List<CategoryName> getCategoryItemByUsername(@RequestParam String username) {
        return categoryNameRepository.getCategoryItemByUsername(username);
    }

    @GetMapping("/getBrandItem")
    public List<BrandName> getBrandItemByUsername(@RequestParam String username) {
        return brandNameRepository.getBrandItemByUsername(username);
    }

    @GetMapping("/getProductItem")
    public List<ProductName> getProductsItemByUsername(@RequestParam String username) {
        return productNameRepository.getProductsItemByUsername(username);
    }

    @GetMapping("/getColorItem")
    public List<ColorName> getColorItemByUsername(@RequestParam String username) {
        return colorNameRepository.getColorItemByUsername(username);
    }

    @GetMapping("/getSupplierItem")
    public List<SupplierName> getSupplierItemByUsername(@RequestParam String username) {
        return supplierNameRepository.getSupplierItemByUsername(username);
    }

    @GetMapping("/getProductStock")
    public List<ProductStock> getProductsStockByUsername(@RequestParam String username) {
        return productRepository.getProductsStockByUsername(username);
    }

    @GetMapping("/getSingleProduct")
    public Optional<ProductStock> getSingleProduct(@RequestParam Long proId) {
        return productRepository.findById(proId);
    }

    @GetMapping("/getProductSale")
    public List<CustomerProductSaleDTO> getProductsSaleByUsername(@RequestParam String username) {
        return productSaleRepository.getProductsSaleByUsername(username);
    }

    @GetMapping("/getMonthlyProductSale")
    public List<CustomerProductSaleDTO> getMonthlyProductsSaleByUsername(@RequestParam String username) {
        return productSaleRepository.getProductsSaleByUsernameForCurrentMonth(username);
    }

    @GetMapping("/getMonthlyVendorSale")
    public List<CustomerProductSaleDTO> getMonthlyVendorSaleByUsername(@RequestParam String username) {
        return productSaleRepository.getVendorSaleByUsernameForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseProductSale")
    public List<CustomerProductSaleDTO> getDatewiseProductsSaleByUsername(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return productSaleRepository.getProductsSaleByUsernameDatewise(username, startDate, endDate);
    }

    @GetMapping("/getDatewiseVendorSale")
    public List<CustomerProductSaleDTO> getDatewiseVendorSaleByUsername(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return productSaleRepository.getVendorSaleByUsernameDatewise(username, startDate, endDate);
    }

    @GetMapping("/getInvoiceData")
    public List<InvoiceDataDTO> getInvoiceData(@RequestParam String username, String cid) {
        return productSaleRepository.getInvoiceDataByUsername(username, cid);
    }

   
    @GetMapping("/productStockSummary")
    public ResponseEntity<List<ProductStockCountDTO>> getProductCountByCategoryBrand(@RequestParam String username) {
        List<ProductStockCountDTO> productCounts = productStockService.getProductCountByUserAndGroup(username);
        return ResponseEntity.ok(productCounts);
    }

    
    @PutMapping("/vatEntry")
    public Vat saveOrUpdateVat(@RequestBody Vat vat) {
        return vatService.saveOrUpdateVat(vat);
    }

    @GetMapping("/getVatPercent")
    public Optional<Double> getPercentByUsername(@RequestParam String username) {
        return vatService.getPercentByUsername(username);
    }
}
