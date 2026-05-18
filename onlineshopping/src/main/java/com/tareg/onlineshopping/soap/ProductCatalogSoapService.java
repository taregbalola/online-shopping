package com.tareg.onlineshopping.soap;

import com.tareg.onlineshopping.dao.ProductDAO;
import com.tareg.onlineshopping.model.Product;
import com.tareg.onlineshopping.soap.dto.ProductSoapDto;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(
        serviceName = "ProductCatalogService",
        portName = "ProductCatalogPort",
        targetNamespace = "http://soap.onlineshopping.tareg.com/"
)
public class ProductCatalogSoapService {

    private final ProductDAO productDAO = new ProductDAO();

    @WebMethod
    @WebResult(name = "serviceStatus")
    public String ping() {
        return "SOAP ProductCatalogService is running";
    }

    @WebMethod
    @WebResult(name = "product")
    public ProductSoapDto getProductById(@WebParam(name = "id") long id) {
        Product product = productDAO.findById(id);
        return product == null ? null : map(product);
    }

    @WebMethod
    @WebResult(name = "products")
    public ProductSoapDto[] getAllProducts() {
        List<Product> products = productDAO.findAll();
        List<ProductSoapDto> result = new ArrayList<>();
        for (Product product : products) {
            result.add(map(product));
        }
        return result.toArray(new ProductSoapDto[0]);
    }
    @WebMethod
    @WebResult(name = "insertedProduct")
    public ProductSoapDto addProduct(@WebParam(name = "product") ProductSoapDto dto) {
        if (dto == null) return null;

        // Extract values from DTO and pass them into the DAO insert parameters
        Product savedProduct = productDAO.insert(
                dto.getName(),
                dto.getDescription(),
                dto.getCategory(),
                new java.math.BigDecimal(dto.getPrice()),
                dto.getStock()
        );

        // Map saved entity back to DTO (includes database-generated id and createdAt timestamp)
        return savedProduct == null ? null : map(savedProduct);
    }

    private ProductSoapDto map(Product product) {
        ProductSoapDto dto = new ProductSoapDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice().toPlainString());
        dto.setStock(product.getStock());
        dto.setCreatedAt(String.valueOf(product.getCreatedAt()));
        return dto;
    }
}

