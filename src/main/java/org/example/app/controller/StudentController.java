package org.example.app.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.example.app.dto.BookedLessonDto;
import org.example.app.dto.FreeTimeDto;
import org.example.app.dto.UserDto;
import org.example.app.security.UserPrincipal;
import org.example.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class StudentController {

    private BookedLessonsService bookedLessonsService;
    private UserService userService;
    private FreeTimeService freeTimeService;
    private PrizeService prizeService;
    private CrossTimeRangeValidationService crossTimeRangeValidationService;

    @Autowired
    public StudentController(BookedLessonsService bookedLessonsService, UserService userService,
                             FreeTimeService freeTimeService, PrizeService prizeService,
                             CrossTimeRangeValidationService crossTimeRangeValidationService) {
        this.bookedLessonsService = bookedLessonsService;
        this.userService = userService;
        this.freeTimeService = freeTimeService;
        this.prizeService = prizeService;
        this.crossTimeRangeValidationService = crossTimeRangeValidationService;
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @PostMapping("/student/bookLessons")
    public ResponseEntity bookLesson(@Valid @RequestBody BookedLessonDto bookedLessonDto,
                                     @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        crossTimeRangeValidationService.checkIfTimeRangeSlotIsUnique(bookedLessonDto,principal.getUser().getId());
        bookedLessonsService.bookLesson(bookedLessonDto, principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/student/allBookedLessons")
    public ResponseEntity<List<BookedLessonDto>> getBookedLessons(
            @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(bookedLessonsService.getByStudentId(principal.getUser().getId()));
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @DeleteMapping("/student/cancelLesson/{id}")
    public ResponseEntity<List<UserDto>> cancelLessonsBooking(@PathVariable Long id,
                                                              @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        bookedLessonsService.cancelBookedLesson(id, principal.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/approveCancelLesson/{id}")
    public ResponseEntity<List<UserDto>> approveCancelLessonOperation(@PathVariable Long id) {
        bookedLessonsService.approveCancelLessonOperation(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/student/{teacherId}/freeTime")
    public ResponseEntity<List<FreeTimeDto>> getTeacherFreeTime(@PathVariable Long teacherId) {
        return ResponseEntity.status(HttpStatus.OK).body(freeTimeService.getTeacherFreeTimes(teacherId));
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/student/allTeachers")
    public ResponseEntity<List<UserDto>> getAllTeachers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllTeachers());
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/student/{lessonId}/fullPrize")
    public ResponseEntity<Double> getLessonFullPrize(@PathVariable Long lessonId,
                                                            @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).
                body(bookedLessonsService.getFullPrizeForBookedLesson(lessonId,principal.getUser().getId()));
    }

}
