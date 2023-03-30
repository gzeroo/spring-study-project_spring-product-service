package hello.productservice.web;

import hello.productservice.domain.DeliveryCode;
import hello.productservice.domain.ItemType;
import hello.productservice.domain.Product;
import hello.productservice.repository.MemoryProductRepository;
import hello.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// 동적 HTML을 만드는 컨트롤러
@Controller
@RequestMapping("/basic/products")
@RequiredArgsConstructor // final 키워드가 들어간 객체 값 초기화 해줌 -- 아래 private final ProductRepository productRepository; 값 초기화해줌
public class BasicProductController {
    // private final MemoryProductRepository ProductRepository; // 저장소

    private final ProductService productService;

    @ModelAttribute("countries") // 사이트 들어올 때 기본 시작으로 들어가게 함
    public Map<String, String> countries(){
    Map<String, String> countries = new HashMap<>();
    countries.put("KOREA", "한국");
    countries.put("USA","미국");
    countries.put("JAPAN", "일본");
    return countries;
}

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }


    // 템플릿 html로 감 동적 -
    @GetMapping()  // tesmplates html 호출하기 위해 리턴 스트링 경로 작성 // 클라 요청 무조건 컨트롤러 받기
    // ResponseViewController 클래스 참고하기
    // index.html -> 컨트롤러 -> /basic/products 경로로 이동
    public String products(Model model){
        // 1. 레포지토리로부터 전체 상품 정보 가져오기
        List<Product> products = productService.findAll(); // 전체 상품들 가져오기

        // 2. 가져온 상품 정보를 모델에 집어넣어야 한다. > 해쉬맵
           // 키: "products", 값: products 객체

        model.addAttribute("products", products);

        // 3. return ModelAndView or viewName
        //모델에 집어넣어야 한다.

        return "/basic/products";
    }

    // 메인 화면
    @GetMapping("/{productId}") //
    public String product(@PathVariable Long productId, Model model){ // @PathVariable -> {productId} 동적 링크
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        return "/basic/product"; // product.html 불러옴
    }

    // 상품 등록
    @GetMapping("/add") // products.html 의 상품 등록 링크 받음
    public String editForm(Model model){
        model.addAttribute("product", new Product());
    return "/basic/addForm"; // html 소환
    }


    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }
    // 상품 저장 후 상품 목록으로 되돌아 가기
    /*
    @PostMapping("/add")
    public String addProductV1(@RequestParam String name,
                             @RequestParam int price,
                             @RequestParam int stock){
        productRepository.save(new Product(name,price,stock));

       return "redirect:/basic/products"; // html 소환 // redirect: - 클라에게 다시 재요청하라고 하는 것 / 클라가 다시 Get 요청한거
    }
     */

    // 상품 저장 후 상품 상세페이지로 이동
    /*
    @PostMapping("/add")
    public String addProductV2(@RequestParam String name,
                               @RequestParam int price,
                               @RequestParam int stock,
                               Model model){
        Product product = productRepository.save(new Product(name,price,stock));
        model.addAttribute("product", product);

        return "/basic/product";
    }
     */


    @PostMapping("/add")
    public String addProductV3(@ModelAttribute Product product){ // @ModelAttribute 를 쓰면 객체 넣기 가능
        // RequestParamController 클래스 참고

        productService.save(product);
        // model.addAttribute("product", product);

        return "redirect:/basic/product/{productId}";
    }

    /*
    // 상품 저장 후 상품 상세페이지로 이동
    @PostMapping("/add")
    public String addProductV4(Product product){ // Product 타입의 키가 생김 // 객체 타입으로 키가 생긴다!
        // RequestParamController 클래스 참고
        productRepository.save(product);
        //model.addAttribute("product", product);

        return "/basic/product";
        //return "redirect:/basic/product/{productId}"; // 클라한테 다시 재접속하라고 요청 ( redirect: 사용)
    }
     */

    // 상품 수정 클릭 시 내용 그대로 노출
    @GetMapping("/{productId}/edit") // @PathVariable: 경로 변수 // 경로({productId}/edit) 가 있으니까 @PathVariable 줌
    public String edit(@PathVariable Long productId, Model model){

        Product product = productService.findById(productId);
        model.addAttribute("product", product);

        return "/basic/editForm";
    }

    // 상품 수정
    @PostMapping("/{productId}/edit")
    public String editProduct(@PathVariable Long productId,
                              @ModelAttribute Product product) {
        productService.update(productId, product);
//        Product p = productRepository.findById(productId);
//
//        p.setName(product.getName());
//        p.setStock(product.getStock());
//        p.setPrice(product.getPrice());

        return "/basic/product";
    }

     @GetMapping("/{productId}/delete")
     public String delete(@PathVariable Long productId){

       productService.delete(productId);

        return "redirect:/basic/products";
    }

    @PostConstruct // 생성 이후 실행
    public void iniProduct(){ // 이니셜라이징 : 기본 테스트 세팅값
        productService.save(new Product("삼다수", 800, 20));
        productService.save(new Product("아이시스", 500, 30));
    }
}
