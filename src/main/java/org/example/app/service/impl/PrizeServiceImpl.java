package org.example.app.service.impl;

import org.example.app.dto.PrizeDto;
import org.example.app.dto.UserDto;
import org.example.app.entities.Prize;
import org.example.app.entities.User;
import org.example.app.exeptions.NotFoundException;
import org.example.app.repository.PrizeRepository;
import org.example.app.service.PrizeService;
import org.example.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class PrizeServiceImpl implements PrizeService {

    private UserService userService;
    private PrizeRepository prizeRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public PrizeServiceImpl(UserService userService, PrizeRepository prizeRepository) {
        this.userService = userService;
        this.prizeRepository = prizeRepository;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Override
    public void setPrize(PrizeDto prizeDto, String email) {
        UserDto userDto = userService.getUserByEmail(email);
        if (userDto.getPrize().getId() != null) {
            prizeDto.setId(userDto.getPrize().getId());
            prizeRepository.save(modelMapper.map(prizeDto, Prize.class));
        } else {
            userDto.setPrize(prizeRepository.save(modelMapper.map(prizeDto, Prize.class)));
            userService.saveOrUpdate(modelMapper.map(userDto, User.class));
        }
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Override
    public PrizeDto getPrizeById(Long id) {
        return modelMapper.map(prizeRepository.findById(id).orElseThrow(() -> new NotFoundException("Prize was not found")), PrizeDto.class);
    }

}
