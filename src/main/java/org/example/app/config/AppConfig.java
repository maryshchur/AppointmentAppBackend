package org.example.app.config;

import org.example.app.dto.AvailableTeachersHoursDto;
import org.example.app.dto.BookedLessonsViewDto;
import org.example.app.dto.UserDto;
import org.example.app.entities.BookedLesson;
import org.example.app.entities.FreeTime;
import org.example.app.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableScheduling
public class AppConfig {

    @Value("${spring.mail.user}")
    private String user;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${ENDPOINT_URL}")
    private String endpointUrl;


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setUsername(user);
        mailSender.setPassword(password);
        mailSender.setPort(587);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.debug", "true");

        return mailSender;

    }

    @Bean
    public LinkDiscoverers discovers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));

    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(User.class, UserDto.class).setPostConverter(context ->
        {
            UserDto userDto = context.getDestination();
            userDto.setImage(endpointUrl + context.getSource().getImageUrl());
            return userDto;
        });
        modelMapper.typeMap(FreeTime.class, AvailableTeachersHoursDto.class).setPreConverter(context ->
        {
            AvailableTeachersHoursDto dto = context.getDestination();
            dto.setStartDate(context.getSource().getDate()+"T"+context.getSource().getTimeFrom());
            dto.setEndDate(context.getSource().getDate()+"T"+context.getSource().getTimeTo());
            return dto;
        });
        modelMapper.typeMap(BookedLesson.class, BookedLessonsViewDto.class).setPreConverter(context ->
        {
            BookedLessonsViewDto dto = context.getDestination();
            dto.setStartDate(context.getSource().getDate()+"T"+context.getSource().getTimeFrom());
            dto.setEndDate(context.getSource().getDate()+"T"+context.getSource().getTimeTo());
            dto.setTitle("Lessons with "+context.getSource().getStudent().getFirstName()+" "+context.getSource().getStudent().getLastName());
            return dto;
        });
        return modelMapper;
    }
}
