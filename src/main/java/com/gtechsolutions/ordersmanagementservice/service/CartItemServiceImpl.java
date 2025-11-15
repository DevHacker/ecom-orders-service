package com.gtechsolutions.ordersmanagementservice.service;

import com.gtechsolutions.ordersmanagementservice.client.ProductServiceClient;
import com.gtechsolutions.ordersmanagementservice.client.UserServiceClient;
import com.gtechsolutions.ordersmanagementservice.dtos.CartItemRequest;
import com.gtechsolutions.ordersmanagementservice.dtos.ProductDTO;
import com.gtechsolutions.ordersmanagementservice.dtos.UserDTO;
import com.gtechsolutions.ordersmanagementservice.entity.CartItem;
import com.gtechsolutions.ordersmanagementservice.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    @Override
    public String addToCart(String userId, CartItemRequest cartItemRequest) {
        UserDTO foundUser = userServiceClient.searchUser(userId);
        if(foundUser == null) {
            log.info("User not found");
            return "User with the given id is not found";
        }

        ProductDTO foundProduct = productServiceClient.searchProduct(cartItemRequest.getProdcutId());
        if(foundProduct == null || foundProduct.getStockQuantity() < cartItemRequest.getQuantity()){
            log.info("Product not found or out of stock");
            return "Product not found or out of stock";
        }

        CartItem cartItemFromDB = cartItemRepository.findCartItemByUserIdAndProductId(userId, cartItemRequest.getProdcutId());

        if(cartItemFromDB != null){
            cartItemFromDB.setQuantity(cartItemFromDB.getQuantity() + cartItemRequest.getQuantity());
            log.info("Product already in cart - Quantity is :: {}", cartItemFromDB.getQuantity());
            cartItemFromDB.setPrice((foundProduct.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity()))).add(cartItemFromDB.getPrice()));
            cartItemRepository.save(cartItemFromDB);
        }else{
            CartItem cartItem = new CartItem();
            cartItem.setProductId(cartItemRequest.getProdcutId());
            cartItem.setUserId(userId);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(foundProduct.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItemRepository.save(cartItem);
        }

        return "Product successfully added to cart";

    }


    @Override
    @Transactional
    public Boolean removeFromCart(String userId, Long productId) {
       /* return cartItemRepository.findById(Long.parseLong(userId))
                .flatMap(user -> productRepository.findById(productId)
                        .map(product -> cartItemRepository.findCartItemByUser_IdAndProduct_Id(user.getId(), product.getId()))
                        .map(foundItem -> {
                            cartItemRepository.deleteCartItemByUser_IdAndProduct_Id(user.getId(), productId);
                            return true;
                        })
                )
                .orElse(false);*/
        CartItem foundItem = cartItemRepository.findCartItemByUserIdAndProductId(userId, productId);

        if (foundItem != null) {
            cartItemRepository.delete(foundItem);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<CartItem> fetchUserCart(String userId) {
       /* return userRepository.findById(Long.parseLong(userId))
                .map(user -> cartItemRepository.findCartItemsByUser_Id(user.getId()))
                .orElseGet(List::of);*/

        return cartItemRepository.findCartItemsByUserId(userId);
    }

    @Override
    public void deleteCart(String userId) {
        /*userRepository.findById(userId)
                .ifPresent(cartItemRepository::deleteCartItemsByUser);*/
        cartItemRepository.deleteAll(cartItemRepository.findCartItemsByUserId(userId));
    }

}
