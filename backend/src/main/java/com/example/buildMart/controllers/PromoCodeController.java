package com.example.buildMart.controllers;

import com.example.buildMart.models.PromoCode;
import com.example.buildMart.services.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/promocodes")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;
    @Autowired
    public PromoCodeController(PromoCodeService promoCodeService){
        this.promoCodeService = promoCodeService;
    }
    @GetMapping("/code/{code}")
    public Optional<PromoCode> findByCode(@PathVariable String code){
        return promoCodeService.findByCode(code);
    }
}
