package org.mbc.czo.function.product.repository;

import org.mbc.czo.function.product.domain.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    // 이미지가 잘 저장됐는지 테스트 코드를 작성하기 위한 메서드 추가
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
