package hello.itemservice.repository.v2;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static hello.itemservice.domain.QItem.item;


/* 복잡한 쿼리를 담당하는 레포, QueryDSL 사용 */
@Repository
public class ItemQueryRepositoryV2 {
    private final JPAQueryFactory query;

    public ItemQueryRepositoryV2(EntityManager em){
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond){
        return query.select(item)
                .from(item)
                .where(likeItemName(cond.getItemName()), maxPrice(cond.getMaxPrice())) // null 이면 무시됨, ","을 쓰면 "and"조건
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
}
