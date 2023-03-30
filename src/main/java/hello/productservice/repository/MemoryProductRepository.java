package hello.productservice.repository;
import hello.productservice.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository // 저장소 선언
public class MemoryProductRepository implements ProductRepository {

    private static final Map<Long, Product> store = new HashMap<>(); // 저장소, DB 역할

    private static long sequence = 0l;

    @Override
    public Product save(Product product){ // 저장됐으면 저장된(매개변수) product가 나오도록 메소드
       // product.setId(++sequence);
        product.setId(gernerateKey(store));
        store.put(product.getId(), product); // 키 : 밸류
        return product;
    }

    @Override
    public Product findById(Long id) {
        return store.get(id);
    }

    @Override
    public Product findByName(String name) {
        return store.values().stream()
                .filter(product -> product.getName().equals(name))
                .findAny().get();
    }

    @Override
    public List<Product> findAll(){ // 상품 전체 조회
        return new ArrayList<>(store.values());
    }

    // 수정 메소드
    @Override
    public void update(Long productId, Product updateproduct){ // 새로 바꿀 id 조회해서 updateproduct 로 변경
        Product product = findById(productId); // 바꿀거 선택
        product.setName(updateproduct.getName()); // updateproduct.getName() : 바꿀 내용
        product.setPrice(updateproduct.getPrice());
        product.setStock(updateproduct.getStock());
    }

    // 삭제 메소드
    @Override
    public void delete(Long productId){
        store.remove(productId); // map 리무브 삭제 메소드 이용
    }

    private Long gernerateKey(Map<Long, Product> store){
        Long num = 1l;

        while (true){
            if(store.get(num) == null){
                return num;
            }
            num++;
        }

    }
}
