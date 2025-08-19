package org.mbc.czo.function.cart.repository;

import org.mbc.czo.function.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberMid(String mid);
}
