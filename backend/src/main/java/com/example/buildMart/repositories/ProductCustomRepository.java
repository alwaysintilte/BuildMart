package com.example.buildMart.repositories;

import com.example.buildMart.models.Category;
import com.example.buildMart.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductCustomRepository {
    private final MongoTemplate mongoTemplate;
    @Autowired
    public ProductCustomRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }
    public Page<Product> findAllByPage(Integer page, Integer size, String sort){
        Query query = new Query();
        Query totalQuery = new Query();
        long total = mongoTemplate.count(totalQuery, Product.class);
        addSorting(query, sort);
        Pageable pageable = addPagination(query, page, size);
        return new PageImpl<>(mongoTemplate.find(query, Product.class), pageable, total);
    }
    public List<Category> findAllCategories(){
        List<String> categoryNames = mongoTemplate.findDistinct("category", Product.class, String.class);
        List<Category> categories = categoryNames.stream().map(name -> {
            Query countQuery = new Query(Criteria.where("category").is(name));
            Long count = mongoTemplate.count(countQuery, Product.class);
            Query imageQuery = new Query(Criteria.where("category").is(name)).limit(1);
            Product product = mongoTemplate.findOne(imageQuery, Product.class);
            String image = product.getImage();
            return new Category(name, count, image);
        }).collect(Collectors.toList());
        return categories;
    }
    public List<Product> findByDiscount(){
        Query query = new Query();
        query.addCriteria(Criteria.where("discount").gt(0));
        return mongoTemplate.find(query, Product.class);
    }
    public Page<Product> findByParams(Float rating, Float minPrice, Float maxPrice, String category, Integer page, Integer size, String sort){
        Query query = new Query();
        Query totalQuery = new Query();
        if (category!=null){
            query.addCriteria(Criteria.where("category").is(category));
            totalQuery.addCriteria(Criteria.where("category").is(category));
        }
        if (rating!=null){
            query.addCriteria(Criteria.where("rating").gte(rating));
            totalQuery.addCriteria(Criteria.where("rating").gte(rating));
        }
        if(minPrice!=null || maxPrice!=null) {
            Criteria priceCriteria = Criteria.where("price");
            if(minPrice!=null){
                priceCriteria.gte(minPrice);
            }
            if(maxPrice!=null){
                priceCriteria.lte(maxPrice);
            }
            query.addCriteria(priceCriteria);
            totalQuery.addCriteria(priceCriteria);
        }
        long total = mongoTemplate.count(totalQuery, Product.class);
        addSorting(query, sort);
        Pageable pageable = addPagination(query, page, size);
        return new PageImpl<>(mongoTemplate.find(query, Product.class), pageable, total);
    }
    private void addSorting(Query query, String name){
        if(name==null || name.isEmpty()){
            return;
        }
        switch (name) {
            case "name_asc":
                query.with(Sort.by(Sort.Direction.ASC, "name"));
                break;
            case "name_desc":
                query.with(Sort.by(Sort.Direction.DESC, "name"));
                break;
            case "price_asc":
                query.with(Sort.by(Sort.Direction.ASC, "price"));
                break;
            case "price_desc":
                query.with(Sort.by(Sort.Direction.DESC, "price"));
                break;
            default:
                break;
        }
    }
    private Pageable addPagination(Query query, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);
        return pageable;
    }
}
