package com.example.buildMart.services;

import com.example.buildMart.dtos.requests.CartItemPatchRequest;
import com.example.buildMart.dtos.requests.CartItemPostRequest;
import com.example.buildMart.dtos.responses.CartPromoCodeResponse;
import com.example.buildMart.dtos.responses.CartResponse;
import com.example.buildMart.exceptions.types.BadRequestException;
import com.example.buildMart.exceptions.types.ConflictException;
import com.example.buildMart.exceptions.types.NotFoundException;
import com.example.buildMart.mappers.interfaces.CartMapper;
import com.example.buildMart.models.*;
import com.example.buildMart.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CartService {

    private UserService userService;

    private CartRepository cartRepository;

    private ProductService productService;

    private CartItemRepository cartItemRepository;

    private PromoCodeService promoCodeService;

    private CartMapper cartMapper;

    public CartResponse getCart(String email){
        User user = userService.findByEmailEntity(email);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Before getting cart add at least 1 product"));;
        CartResponse cartResponse = cartMapper.toDto(cart);
        return cartResponse;
    }

    @Transactional
    public CartResponse postItem(CartItemPostRequest cartItemPostRequest, String email){
        User user = userService.findByEmailEntity(email);
        Product product = productService.findByIdEntity(cartItemPostRequest.getProductId());
        CartItem cartItem = cartItemRepository.findByCartUserIdAndProductId(user.getId(), cartItemPostRequest.getProductId()).orElse(null);
        if(cartItem != null){
            if(cartItem.getProduct().getStockQuantity() < cartItem.getQuantity() + cartItemPostRequest.getQuantity()){
                throw new ConflictException("Insufficient stock for requested quantity");
            }
            cartItem.setQuantity(cartItem.getQuantity() + cartItemPostRequest.getQuantity());
        } else {
            if(product.getStockQuantity() < cartItemPostRequest.getQuantity()){
                throw new ConflictException("Insufficient stock for requested quantity");
            }
            Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(user);
                return cartRepository.save(newCart);
            });
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemPostRequest.getQuantity());
        }
        cartItemRepository.save(cartItem);
        Cart cart = cartRepository.findByUser(user).orElse(null);
        CartResponse cartResponse = cartMapper.toDto(cart);
        return cartResponse;
    }

    @Transactional
    public CartResponse patchItem(CartItemPatchRequest cartItemPatchRequest, Long productId, String email){
        User user = userService.findByEmailEntity(email);
        CartItem cartItem = cartItemRepository.findByCartUserIdAndProductId(user.getId(), productId).orElseThrow(() -> new RuntimeException("Unnable to find specified cart item"));
        if(cartItem.getProduct().getStockQuantity() < cartItemPatchRequest.getQuantity()){
            throw new ConflictException("Insufficient stock for requested quantity");
        }
        cartItem.setQuantity(cartItemPatchRequest.getQuantity());
        cartItemRepository.save(cartItem);
        Cart cart = cartRepository.findByUser(user).orElse(null);
        CartResponse cartResponse = cartMapper.toDto(cart);
        return cartResponse;
    }

    @Transactional
    public CartResponse deleteItem(Long productId, String email){
        User user = userService.findByEmailEntity(email);
        CartItem cartItem = cartItemRepository.findByCartUserIdAndProductId(user.getId(), productId).orElseThrow(() -> new RuntimeException("Unnable to find specified cart item"));
        cartItemRepository.delete(cartItem);
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if(cart != null && cart.getItems().isEmpty()){
            cartRepository.delete(cart);
            cart = null;
        }
        CartResponse cartResponse = cartMapper.toDto(cart);
        return cartResponse;
    }

    @Transactional
    public CartPromoCodeResponse applyPromo(String promo, String email){
        User user = userService.findByEmailEntity(email);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new NotFoundException("Before applying promocode add at least 1 product"));
        if(cart.getPromoCode() != null){
            throw new ConflictException("Promocode was already applied");
        }
        PromoCode promoCode = promoCodeService.findByCodeEntity(promo);
        if(!promoCode.getIsActive()){
            throw new BadRequestException("Promocode is not active");
        }
        cart.setPromoCode(promoCode);
        cartRepository.save(cart);
        CartPromoCodeResponse cartPromoCodeResponse = cartMapper.toPromoCodeDto(cart);
        return cartPromoCodeResponse;
    }
}
