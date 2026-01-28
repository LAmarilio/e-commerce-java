package com.leonardo.ecommerce_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.ecommerce_api.dto.ProductRequestDTO;
import com.leonardo.ecommerce_api.dto.ProductResponseDTO;
import com.leonardo.ecommerce_api.model.Product;
import com.leonardo.ecommerce_api.repository.ProductRepository;

import jakarta.persistence.OptimisticLockException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    private void verifyName(String name) {
        if (productRepository.existsByNameIgnoreCase(name)) {
            throw new RuntimeException("Um produto com este nome já está cadastrado!");
        }
    }

    private void verifyNameForUpdate(ProductRequestDTO dto, UUID id) {
        if (productRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
            throw new RuntimeException("Já existe outro produto com este nome!");
        }
    }

    private void verifyPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O preço do produto não pode ser menor ou igual a zero!");
        }
    }

    private void verifyStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new RuntimeException("O estoque deste produto não pode ser um numero negativo!");
        }
    }

    private void checkerRules(ProductRequestDTO dto) {
        verifyName(dto.getName());
        verifyPrice(dto.getPrice());
        verifyStockQuantity(dto.getStockQuantity());
    }

    private void checkerRulesForUpdate(ProductRequestDTO dto, UUID id) {
        verifyNameForUpdate(dto, id);
        verifyPrice(dto.getPrice());
        verifyStockQuantity(dto.getStockQuantity());
    }

    private String formatString(String string) {
        string = string.trim().toLowerCase();
        String firstLetter = string.substring(0, 1).toUpperCase();
        return firstLetter + string.substring(1);
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCreatedAt(product.getCreatedAt());
        return dto;
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        checkerRules(dto);

        Product product = new Product();
        product.setName(formatString(dto.getName()));
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());

        Product saved = productRepository.save(product);

        return toResponseDTO(saved);
    }

    public Optional<ProductResponseDTO> getProductById(UUID id) {
        return productRepository.findById(id).map(this::toResponseDTO);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO dto) {
        return productRepository.findById(id)
                .map(product -> {
                    checkerRulesForUpdate(dto, id);
                    product.setName(formatString(dto.getName()));
                    product.setDescription(dto.getDescription());
                    product.setPrice(dto.getPrice());
                    product.setStockQuantity(dto.getStockQuantity());

                    Product updated = productRepository.save(product);
                    return toResponseDTO(updated);
                })
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
    }

    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void restock(UUID id, int quantityReStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        product.setStockQuantity(product.getStockQuantity() + quantityReStock);
        productRepository.save(product);
    }

    @Transactional
    public void consumeStock(UUID id, int quantityConsumed) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

            if (product.getStockQuantity() < quantityConsumed) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - quantityConsumed);
            productRepository.save(product);
        } catch (OptimisticLockException e) {
            throw new RuntimeException(
                    "Conflito de concorrência: o estoque foi alterado por outro processo, tente novamente.");
        }
    }

    public List<ProductResponseDTO> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ProductResponseDTO> getProductsByPriceBetween(BigDecimal min, BigDecimal max) {
        return productRepository.findByPriceBetween(min, max)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
}