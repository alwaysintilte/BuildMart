package com.example.buildMart.services;

import com.example.buildMart.models.PromoCode;
import com.example.buildMart.repositories.PromoCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PromoCodeService {

    private PromoCodeRepository promoCodeRepository;

    public PromoCode findByCodeEntity(String code){
        PromoCode promoCode = promoCodeRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Promocode not found"));
        return promoCode;
    }
}
