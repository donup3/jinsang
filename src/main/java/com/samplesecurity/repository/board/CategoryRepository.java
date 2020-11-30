package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
