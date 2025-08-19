package org.mbc.czo.function.cart.repository;

import org.mbc.czo.function.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;




public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

}
