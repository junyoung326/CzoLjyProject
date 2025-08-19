package org.mbc.czo.function.cart.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mbc.czo.function.cart.domain.Order;
import org.mbc.czo.function.cart.domain.OrderItem;
import org.mbc.czo.function.cart.dto.OrderDTO;
import org.mbc.czo.function.cart.repository.OrderRepository;
import org.mbc.czo.function.member.domain.Member;
import org.mbc.czo.function.member.repository.MemberJpaRepository;
import org.mbc.czo.function.product.domain.Item;
import org.mbc.czo.function.product.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDTO orderDTO, String memail) {
        //상품조회

        Item item = itemRepository.findById(orderDTO.getItem_id())
                .orElseThrow(EntityNotFoundException::new);

        //회원 조회
        Member member = memberJpaRepository.findByMemail(memail)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다: " + memail));

        //주문 상품 리스트 생성
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem =
                OrderItem.createOrderItem(item, orderDTO.getCount());
        orderItemList.add(orderItem);

        //주문 생성
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();

    }

}
