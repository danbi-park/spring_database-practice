package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;
//JPA 매핑 정보로 DDL도 가능한데 그 때 컬럼 길이를 length로 사용할 수 있음
@Data
@Entity //JPA가 사용하는 객체라는 뜻
//@Table(name = "item") // 객체와 이름이 같으면
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB에서 값을 증가시키는 전략
    private Long id;

    // 컬럼명과 필드명이 다를 때 지정
    @Column(name = "item_name", length = 10) // 참고로 스프링부트와 통합하여 사용하면 item_name <-> itemName 명명규칙이 호환된다. 지금 이 라인도 굳이 필요없음
    private String itemName;
    private Integer price;
    private Integer quantity;

    // JPA는 public, protected의 기본 생성자가 필수임 -> 프록시 기술 등을 사용하기 편해짐
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
