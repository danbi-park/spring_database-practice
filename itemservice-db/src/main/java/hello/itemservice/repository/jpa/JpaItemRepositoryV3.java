package hello.itemservice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.*;

@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        // 동적 쿼리 리팩토링
        return query.select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice)) // null 이면 무시됨, ","을 쓰면 "and"조건
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName){
        if (StringUtils.hasText(itemName)){
            return  item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if(maxPrice != null){
            return item.price.loe(maxPrice);
        }
        return null;
    }

    public List<Item> findAllOld(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        // 동적쿼리
        QItem item = QItem.item; // 아래 처럼 static imported = item 처럼 써도 됨
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(itemName)){
            builder.and(item.itemName.like("%" + itemName + "%"));
        }
        if(maxPrice != null){
            builder.and(item.price.loe(maxPrice));
        }

        return query.select(item) // -> static imported
                .from(item)
                .where(builder)
                .fetch();
    }


}
