package hello.productservice.repository;

import hello.productservice.domain.Product;

import java.util.List;

public interface ProductRepository {
    Product save(Product product);

    Product findById(Long id);
    Product findByName(String name);

    List<Product> findAll();

    void update(Long productId, Product updateproduct);

    void delete(Long productId);

}
