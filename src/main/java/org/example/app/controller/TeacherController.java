package org.example.app.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.example.app.dto.*;
import org.example.app.security.UserPrincipal;
import org.example.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class TeacherController {
    private UserService userService;
    private PrizeService prizeService;
    private FreeTimeService freeTimeService;
    private CrossTimeRangeValidationService crossTimeRangeValidationService;
    private BookedLessonsService bookedLessonsService;

    @Autowired
    public TeacherController(UserService userService, PrizeService prizeService, FreeTimeService freeTimeService,
                             CrossTimeRangeValidationService crossTimeRangeValidationService, BookedLessonsService bookedLessonsService) {
        this.userService = userService;
        this.prizeService = prizeService;
        this.freeTimeService = freeTimeService;
        this.crossTimeRangeValidationService = crossTimeRangeValidationService;
        this.bookedLessonsService = bookedLessonsService;
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @PostMapping("/teacher/set-prize")
    public ResponseEntity setPrize(@Valid @RequestBody PrizeDto prizeDto,
                                   @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) throws Throwable {
        prizeService.setPrize(prizeDto, principal.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @PostMapping("/teacher/set-free-time")
    public ResponseEntity setFreeTime(@Valid @RequestBody FreeTimeDto[] freeTimeDto,
                                      @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) throws Throwable {
        for(FreeTimeDto freeTime : freeTimeDto) {
            crossTimeRangeValidationService.checkIfTimeRangeSlotIsUnique(freeTime, principal.getUsername());
            freeTimeService.save(freeTime, principal.getUsername());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Should be Get coz this endpoint is used by html form tag which support only get method
    @GetMapping("/approve-booking/{lessonId}")
    public ResponseEntity approveBooking(@PathVariable Long lessonId) throws Throwable {
        bookedLessonsService.approveLessonsBooking(lessonId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Should be Get coz this endpoint is used by html form tag which support only get method
    @GetMapping ("/decline-booking/{lessonId}")
    public ResponseEntity declineBooking(@PathVariable Long lessonId) {
        bookedLessonsService.declineLessonsBooking(lessonId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/teacher/upcoming-lessons")
    public ResponseEntity<List<BookedLessonsViewDto>> getAllUpcomingApprovedLessons(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(bookedLessonsService.getLessonsByTeacherId(principal.getUser().getId(),true));
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/teacher/booking-request")
    public ResponseEntity<List<BookedLessonsViewDto>> getAllLessonsBookingRequests(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(bookedLessonsService.getLessonsByTeacherId(principal.getUser().getId(),false));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/teacher/free-hours")
    public ResponseEntity<List<AvailableTeachersHoursDto>> getAllFreeHours(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(freeTimeService.getTeacherFreeTimes(principal.getUser().getId()));
    }

//    @PreAuthorize("hasRole('ROLE_TEACHER')")
//    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
//    @GetMapping("/teacher/prize")
//    public ResponseEntity<PrizeDto> getPrize(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
//        return ResponseEntity.status(HttpStatus.OK).body(prizeService.getPrizeById(principal.getUser().getPrize().getId()));
//    }
}



