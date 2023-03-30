package hello.productservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class Product {

    private long id;

    private String name;

    private Integer price;

    private Integer stock;

    private Boolean open; //판매 여부
    private List<String> countries; //등록 지역

    private ItemType itemType; //상품 종류
    private String deliveryCode; //배송 방식

    public Product(){};

    public Product(String name, Integer price, Integer stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
