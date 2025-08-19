package org.mbc.czo.function.cart.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mbc.czo.function.cart.domain.Cart;
import org.mbc.czo.function.cart.domain.CartItem;
import org.mbc.czo.function.cart.dto.CartItemDTO;
import org.mbc.czo.function.cart.repository.CartItemRepository;
import org.mbc.czo.function.cart.repository.CartRepository;
import org.mbc.czo.function.member.domain.Member;
import org.mbc.czo.function.member.repository.MemberJpaRepository;
import org.mbc.czo.function.product.domain.Item;
import org.mbc.czo.function.product.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDTO cartItemDTO, String memail) {
        // 상품 조회
        Item item = itemRepository.findById(cartItemDTO.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

    // 회원 조회 (Optional 처리)
        Member member = memberJpaRepository.findByMemail(memail)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다: " + memail));

        // 회원의 장바구니 조회
        Cart cart = cartRepository.findByMemberMid(member.getMid());
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart); // 새로 만든 경우 저장 필요*/
        }

        // 장바구니에 같은 상품 있는지 확인
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (savedCartItem != null) {
            // 이미 있으면 수량 추가
            savedCartItem.addCount(cartItemDTO.getCount());
            return savedCartItem.getId();
        } else {
            // 없으면 새로 추가
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDTO.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }
}
