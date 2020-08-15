package org.example.app.service;

import org.example.app.dto.PrizeDto;

public interface PrizeService {
    void setPrize(PrizeDto prizeDto,String email);
    PrizeDto getPrizeById(Long id);
}
