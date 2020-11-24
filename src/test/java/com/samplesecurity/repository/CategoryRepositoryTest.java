package com.samplesecurity.repository;

import com.samplesecurity.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void 카테고리데이터생성() {
        String[] name = new String[]{"연인", "가족", "버릇", "음식점", "자동차", "층간소음",
                "블랙박스", "회사생활", "공공장소", "중고거래", "편의점", "패스트푸드","상담원","카페","술집","쇼핑몰","기타"};
        for (String s : name) {
            Category category=new Category(s);
            categoryRepository.save(category);
        }
    }
}