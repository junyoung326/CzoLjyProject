package org.mbc.czo;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mbc.czo.function.product.constant.ItemSellStatus;
import org.mbc.czo.function.product.domain.Item;
import org.mbc.czo.function.product.domain.ItemImg;
import org.mbc.czo.function.product.dto.ItemFormDto;
import org.mbc.czo.function.product.repository.ItemImgRepository;
import org.mbc.czo.function.product.repository.ItemRepository;
import org.mbc.czo.function.product.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    // 이미지 생성
    List<MultipartFile> createMultipartFiles() throws Exception { // 가짜 MultipartFile 리스트를 만들어 반환
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0; i<5; i++) {
            String path = "C:/shop/item/";
            String imageName = "image"+i+".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            // "image/jpg" → 이건 JPEG(JPG) 이미지라는 의미
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles="ADMIN")
    void saveItem() throws Exception{
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles(); // 이미지 등록
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList); // 상품 데이터와 이미지 정보를 파라미터로 넘겨서 저장 후 상품의 아이디 값을 반환

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemNm(), item.getItemNm()); // 입력한 상품 데이터와 실제로 저장된 상품 데이터가 같은지 확인
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName()); // 상품이미지는 첫 번째 파일의 원본 이미지 파일 이름만 같은지 확인
    }

    @Test
    @DisplayName("상품 수정 테스트")
    @WithMockUser(username = "admin", roles="ADMIN")
    void updateItemTest() throws Exception {
        // 기존 상품 저장
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("기존상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("기존 상품 설명");
        itemFormDto.setPrice(5000);
        itemFormDto.setStockNumber(10);

        List<MultipartFile> originalFiles = createMultipartFiles(); // 기존 이미지
        Long itemId = itemService.saveItem(itemFormDto, originalFiles);

        // 수정할 상품 정보
        ItemFormDto updateDto = new ItemFormDto();
        updateDto.setId(itemId); // 수정할 상품 ID
        updateDto.setItemNm("수정상품");
        updateDto.setItemSellStatus(ItemSellStatus.SOLD_OUT);
        updateDto.setItemDetail("수정된 상품 설명");
        updateDto.setPrice(10000);
        updateDto.setStockNumber(0);

        List<MultipartFile> updateFiles = createMultipartFiles(); // 새로운 이미지

        // 상품 수정 실행
        Long updatedItemId = itemService.updateItem(updateDto, updateFiles);

        // 검증
        Item updatedItem = itemRepository.findById(updatedItemId).orElseThrow(EntityNotFoundException::new);
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(updatedItemId);

        assertEquals(updateDto.getItemNm(), updatedItem.getItemNm());
        assertEquals(updateDto.getItemSellStatus(), updatedItem.getItemSellStatus());
        assertEquals(updateDto.getItemDetail(), updatedItem.getItemDetail());
        assertEquals(updateDto.getPrice(), updatedItem.getPrice());
        assertEquals(updateDto.getStockNumber(), updatedItem.getStockNumber());
        assertEquals(updateFiles.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName()); // 첫 번째 이미지 이름 확인
    }

    @Test
    @DisplayName("상품 상세페이지 테스트")
        /*@WithMockUser(username = "admin", roles="ADMIN")*/
    void readOneTest() throws Exception {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> originalFiles = createMultipartFiles(); // 이미지 생성
        Long itemId = itemService.saveItem(itemFormDto, originalFiles); // 아이템 상세정보와 이미지 저장 후 반환 된 아이디 사용

        // 상품 상세 조회
        ItemFormDto detailDto = itemService.getItemDtl(itemId);

        // 검증
        assertEquals(itemFormDto.getItemNm(), detailDto.getItemNm());
        assertEquals(itemFormDto.getItemSellStatus(), detailDto.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), detailDto.getItemDetail());
        assertEquals(itemFormDto.getPrice(), detailDto.getPrice());
        assertEquals(itemFormDto.getStockNumber(), detailDto.getStockNumber());

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        assertEquals(originalFiles.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
        assertEquals(originalFiles.get(1).getOriginalFilename(), itemImgList.get(1).getOriImgName());

    }

    @Test
    @DisplayName("여러 상품 삭제 테스트")
    public void deleteMultipleItemsTest() {
        // 1. 테스트용 상품 여러 개 생성 및 저장
        Item item1 = new Item();
        item1.setItemNm("상품1");
        item1.setItemDetail("삭제 테스트용 상품1");
        item1.setPrice(1000);
        item1.setStockNumber(5);
        item1.setItemSellStatus(ItemSellStatus.SELL);

        Item item2 = new Item();
        item2.setItemNm("상품2");
        item2.setItemDetail("삭제 테스트용 상품2");
        item2.setPrice(2000);
        item2.setStockNumber(10);
        item2.setItemSellStatus(ItemSellStatus.SELL);

        // DB에 저장
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.flush(); // DB에 강제 반영

        // 2. 저장된 상품 ID 리스트 생성
        List<Long> itemIds = java.util.Arrays.asList(item1.getId(), item2.getId());

        // 3. 서비스 호출하여 리스트 단위로 삭제 (반복문 X)
        itemService.deleteItem(itemIds);

        itemRepository.flush();

        // 4. 삭제 확인 (existsById 사용)
        for (Long itemId : itemIds) {
            assertFalse(itemRepository.existsById(itemId), "상품이 삭제되지 않았습니다. ID=" + itemId);
        }
    }


}
