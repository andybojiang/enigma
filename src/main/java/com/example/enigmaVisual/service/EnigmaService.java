package com.example.enigmaVisual.service;

import com.example.enigmaVisual.dao.EnigmaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EnigmaService {
    private final EnigmaDao enigmadao;
    @Autowired
    public EnigmaService(@Qualifier("enigmadao") EnigmaDao enigmadao) {
        this.enigmadao = enigmadao;
    }
}
