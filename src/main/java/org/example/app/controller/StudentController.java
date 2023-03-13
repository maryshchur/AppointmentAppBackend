package org.example.app.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.example.app.dto.*;
import org.example.app.security.UserPrincipal;
import org.example.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@Validated
@CrossOrigin(origins = "http://localhost:3000")
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
    @PostMapping("/student/book-lesson")
    public ResponseEntity bookLesson(@Valid @RequestBody BookedLessonDto bookedLessonDto,
                                     @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) throws Throwable {
        crossTimeRangeValidationService.checkIfTimeRangeSlotIsUnique(bookedLessonDto,principal.getUser().getId());
        bookedLessonsService.bookLesson(bookedLessonDto, principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/student/all-booked-lessons")
    public ResponseEntity<List<BookedLessonsViewDto>> getBookedLessons(
            @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(bookedLessonsService.getByStudentId(principal.getUser().getId()));
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/student/subscription")
    public ResponseEntity<Set<TeacherDto>> getSubscriptionsForCurrentStudent(@AuthenticationPrincipal @ApiIgnore UserPrincipal principal
    ) throws Throwable {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getSubscriptions(principal.getUser().getId()));

    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/student/cancel-lesson/{id}")
    public ResponseEntity cancelLessonsBooking(@PathVariable Long id,
                                                              @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) throws Throwable {
        bookedLessonsService.cancelBookedLesson(id, principal.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping ("/approve-cancel-lesson/{id}")
    public ResponseEntity approveCancelLessonOperation(@PathVariable Long id) throws Throwable {
        bookedLessonsService.approveCancelLessonOperation(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/student/{teacherId}/free-time")
    public ResponseEntity<List<AvailableTeachersHoursDto>> getTeacherFreeTime(@PathVariable Long teacherId) {
        return ResponseEntity.status(HttpStatus.OK).body(freeTimeService.getTeacherFreeTimes(teacherId));
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @GetMapping("/student/all-teachers")
    public ResponseEntity<Page<TeacherDto>> getAllTeachers(@RequestParam Optional<Integer> page,
                                                           @RequestParam Optional<Integer> pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllTeachers(page.orElseGet(() -> 1), pageSize.orElseGet(() -> 10)));
    }

//    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
//    @GetMapping("/student/{lessonId}/full-prize")
//    public ResponseEntity<Integer> getLessonFullPrize(@PathVariable Long lessonId,
//                                                      @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
//        return ResponseEntity.status(HttpStatus.OK).
//                body(bookedLessonsService.getFullPrizeForBookedLesson(lessonId,principal.getUser().getId()));
//    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "JWT")})
    @PutMapping("/profile/subscribe/{userId}")
    public ResponseEntity changeSubscription(@AuthenticationPrincipal @ApiIgnore UserPrincipal principal,
                                             @PathVariable  Long userId) throws Throwable {
        if (!principal.getUser().getId().equals(userId)) {
            userService.subscribe(principal.getUsername(), userId);
        }
        return ResponseEntity.status(HttpStatus.OK).build();

    }

}
