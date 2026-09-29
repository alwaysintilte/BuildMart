package com.example.buildMart.services;

import com.example.buildMart.models.PromoCode;
import com.example.buildMart.repositories.interfaces.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    @Autowired
    public PromoCodeService(PromoCodeRepository promoCodeRepository){
        this.promoCodeRepository = promoCodeRepository;
    }
    public Optional<PromoCode> findByCode(String code){
        return promoCodeRepository.findByCode(code);
    }
}
