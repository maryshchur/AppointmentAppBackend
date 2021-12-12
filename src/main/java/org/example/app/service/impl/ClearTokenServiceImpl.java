package org.example.app.service.impl;

import org.example.app.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class ClearTokenServiceImpl {

    private VerificationTokenRepository tokenRepository;

    @Autowired
    public ClearTokenServiceImpl(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Scheduled(cron="0 0 0 * * ?")
    //(cron = @midnight)
    @Transactional
    public void purgeExpired() {
        System.out.println("TRIGERRRRRR!!!!!");
        //tokenRepository.deleteAllByExpiryDateIsLessThanEqual(new Date(System.currentTimeMillis()));
        // tokenRepository.deleteById(4L);
        System.out.println(new Timestamp(System.currentTimeMillis()));
        tokenRepository.deleteAllByExpiryDate(new Timestamp(System.currentTimeMillis()));

    }
}
