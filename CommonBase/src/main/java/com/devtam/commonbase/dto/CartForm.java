package com.devtam.commonbase.dto;

import com.devtam.commonbase.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartForm {
    private List<Cart> carts;

    // Getter and Setter
}