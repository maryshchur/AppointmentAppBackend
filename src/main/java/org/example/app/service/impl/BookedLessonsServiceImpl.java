package org.example.app.service.impl;

import org.example.app.controller.StudentController;
import org.example.app.controller.TeacherController;
import org.example.app.dto.BookedLessonDto;
import org.example.app.dto.FreeTimeDto;
import org.example.app.dto.UserDto;
import org.example.app.entities.BookedLesson;
import org.example.app.entities.FreeTime;
import org.example.app.entities.User;
import org.example.app.exeptions.NotFoundException;
import org.example.app.repository.BookedLessonRepository;
import org.example.app.repository.FreeTimeRepository;
import org.example.app.service.BookedLessonsService;
import org.example.app.service.FreeTimeService;
import org.example.app.service.UserService;
import org.example.app.util.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookedLessonsServiceImpl implements BookedLessonsService {
    private BookedLessonRepository bookedLessonRepository;
    private FreeTimeService freeTimeService;
    private FreeTimeRepository freeTimeRepository;
    private UserService userService;
    private final EmailSender emailSender;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public BookedLessonsServiceImpl(FreeTimeRepository freeTimeRepository, FreeTimeService freeTimeService, EmailSender emailSender, BookedLessonRepository bookedLessonRepository, UserService userService) {
        this.freeTimeRepository = freeTimeRepository;
        this.freeTimeService = freeTimeService;
        this.emailSender = emailSender;
        this.bookedLessonRepository = bookedLessonRepository;
        this.userService = userService;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void bookLesson(BookedLessonDto bookedLessonDto, String email) {
        UserDto teacher = userService.getUserByEmail(bookedLessonDto.getTeacher().getEmail());
        UserDto student = userService.getUserByEmail(email);

        bookedLessonDto.setTeacher(modelMapper.map(teacher, User.class));
        BookedLesson bookedLesson = modelMapper.map(bookedLessonDto, BookedLesson.class);
        bookedLesson.setStudent(modelMapper.map(student, User.class));
        bookedLesson.setAproved(false);
        sendEmailForApproving(bookedLessonRepository.save(bookedLesson));
    }

    private void sendEmailForApproving(BookedLesson bookedLesson) {
        String text = String.format("You have just received new lesson's request by %s %s on %s at %s-%s",
                bookedLesson.getStudent().getFirstName(), bookedLesson.getStudent().getLastName(), bookedLesson.getDate(), bookedLesson.getTimeFrom(), bookedLesson.getTimeTo());
        emailSender.sendEmail("Lesson's request", text +
                        "<form method=\"GET\" action=\"" + linkTo(methodOn(TeacherController.class).approveBooking(bookedLesson.getId())) + "\">" +
                        "<input type=\"submit\" value=\"Approve\"/></form>" +
                        "<form method=\"GET\" action=\"" + linkTo(methodOn(TeacherController.class).declineBooking(bookedLesson.getId())) + "\">" +
                        "<input type=\"submit\" value=\"Decline\"/> </form>\"",
                bookedLesson.getTeacher().getEmail());
    }

    @Transactional
    public void approveLessonsBooking(Long lessonId) {
        BookedLesson bookedLesson = findById(lessonId);
        bookedLesson.setAproved(true);
        changeFreeTime(bookedLesson, userService.getUserByEmail(bookedLesson.getTeacher().getEmail()));
        bookedLessonRepository.save(bookedLesson);

    }

    public void declineLessonsBooking(Long lessonId) {
        bookedLessonRepository.deleteById(lessonId);
    }

    private void changeFreeTime(BookedLesson bookedLesson, UserDto teacher) {
        FreeTimeDto freeTimeDto = getBookedTimeRange(bookedLesson, teacher);
        if (!freeTimeDto.getTimeFrom().equals(bookedLesson.getTimeFrom())) {
            freeTimeService.save(new FreeTimeDto(bookedLesson.getDate(), freeTimeDto.getTimeFrom(),
                    bookedLesson.getTimeFrom()), teacher.getEmail());
        }
        if (!freeTimeDto.getTimeTo().equals(bookedLesson.getTimeTo())) {
            freeTimeService.save(new FreeTimeDto(bookedLesson.getDate(), bookedLesson.getTimeTo(),
                    freeTimeDto.getTimeTo()), teacher.getEmail());
        }
        freeTimeService.delete(freeTimeDto.getId());
    }

    private FreeTimeDto getBookedTimeRange(BookedLesson bookedLesson, UserDto teacher) {
        List<FreeTimeDto> allFreeHoursInThatDay = freeTimeService.getTeacherFreeTimeByData(teacher.getId(), bookedLesson.getDate());
        return allFreeHoursInThatDay.stream().sorted(Comparator.comparing(FreeTimeDto::getTimeFrom)).filter(
                x -> (!bookedLesson.getTimeTo().after(x.getTimeTo()) ||
                        bookedLesson.getTimeTo().equals(x.getTimeTo()))).findFirst().get();
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void cancelBookedLesson(Long lessonId, Long studentId) {
        sendEmailForCancelingBookedLesson( getLessonsByStudentIdAndTeacherId(lessonId,studentId));
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public Double getFullPrizeForBookedLesson(Long lessonId, Long studentId){
        BookedLesson bookedLesson = getLessonsByStudentIdAndTeacherId(lessonId,studentId);
        Long lessonDuration = getDateDiff(bookedLesson.getTimeFrom(),bookedLesson.getTimeTo(),TimeUnit.MINUTES);
        return (lessonDuration.doubleValue()*bookedLesson.getTeacher().getPrize().getPrize())
                /bookedLesson.getTeacher().getPrize().getAmountOfTime();
    }
    private long getDateDiff(Time date1, Time date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
    private BookedLesson getLessonsByStudentIdAndTeacherId(Long lessonId, Long studentId){
        BookedLesson bookedLesson = findById(lessonId);
        if (bookedLesson.getStudent().getId() != studentId) {
            throw new NotFoundException(String.format(
                    "Student with id %s does not have booked lessons with id %s", studentId, lessonId));
        }
        return bookedLesson;
    }

    private void sendEmailForCancelingBookedLesson(BookedLesson bookedLesson) {
        String text = String.format("You have just send request for canceling lesson with teacher %s %s on %s at %s-%s",
                bookedLesson.getTeacher().getFirstName(), bookedLesson.getTeacher().getLastName(),
                bookedLesson.getDate(), bookedLesson.getTimeFrom(), bookedLesson.getTimeTo());
        emailSender.sendEmail("Cancel lesson", text +
                        "<form method=\"GET\" action=\"" + linkTo(methodOn(StudentController.class).
                        approveCancelLessonOperation(bookedLesson.getId())) + "\">" +
                        "<input type=\"submit\" value=\"Cancel\"/></form>",
                bookedLesson.getStudent().getEmail());
    }

    public void approveCancelLessonOperation(Long lessonId) {
        BookedLesson bookedLesson = findById(lessonId);
        bookedLessonRepository.deleteById(lessonId);
        if (bookedLesson.isAproved()) {
            List<FreeTime> existFreeTime = freeTimeRepository.findByUserIdAndDateAndTimeFromEqualsOrTimeToEquals(
                    bookedLesson.getTeacher().getId(), bookedLesson.getDate(), bookedLesson.getTimeTo(), bookedLesson.getTimeFrom());
            if (!existFreeTime.isEmpty()) {
                if (existFreeTime.size() == 2) {
                    FreeTime f1 = existFreeTime.stream().sorted(Comparator.comparing(FreeTime::getTimeFrom)).findFirst().get();
                    FreeTime f2 = existFreeTime.stream().sorted(Comparator.comparing(FreeTime::getTimeFrom)).skip(1).findFirst().get();
                    freeTimeService.save(new FreeTimeDto(bookedLesson.getDate(),
                                    f1.getTimeFrom(), f2.getTimeTo()),
                            bookedLesson.getTeacher().getEmail());
                    freeTimeService.delete(f1.getId());
                    freeTimeService.delete(f2.getId());

                } else {
                    FreeTime existTime = existFreeTime.get(0);
                    if (existTime.getTimeFrom().equals(bookedLesson.getTimeTo())) {
                        freeTimeService.save(new FreeTimeDto(bookedLesson.getDate(),
                                bookedLesson.getTimeFrom(), existTime.getTimeTo()), bookedLesson.getTeacher().getEmail());
                    } else {
                        freeTimeService.save(new FreeTimeDto(bookedLesson.getDate(),
                                existTime.getTimeFrom(), bookedLesson.getTimeTo()), bookedLesson.getTeacher().getEmail());

                    }
                    freeTimeService.delete(existTime.getId());
                }
            } else {
                freeTimeService.save(new FreeTimeDto(bookedLesson.getDate(), bookedLesson.getTimeFrom(),
                        bookedLesson.getTimeTo()), bookedLesson.getTeacher().getEmail());
            }
        }
    }

    // private FreeTimeDto joinFreeTimeIfNeeded(BookedLesson canceledLesson) {
//        List<FreeTimeDto> allFreeHoursInThatDay = freeTimeService.
//                getTeacherFreeTimeByData(canceledLesson.getTeacher().getId(), canceledLesson.getDate());
//        return allFreeHoursInThatDay.stream().sorted(Comparator.comparing(FreeTimeDto::getTimeFrom)).filter(
//                x -> x.getTimeFrom().equals(canceledLesson.getTimeTo()) ||
//                        x.getTimeTo().equals(canceledLesson.getTimeFrom())).findFirst().get();

    //}

    private BookedLesson findById(Long id) {
        return bookedLessonRepository.findById(id).
                orElseThrow(() -> new NotFoundException
                        (String.format("Lesson with %d id was not found", id)));
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<BookedLessonDto> getByStudentId(Long id) {
        List<BookedLesson> bookedLesson = bookedLessonRepository.getByStudentId(id);
        return checkIfEmptyAndMapToDto("The student has no booked lesson", bookedLesson);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public List<BookedLessonDto> getByTeacherId(Long id) {
        List<BookedLesson> bookedLesson = bookedLessonRepository.getByTeacherId(id);
        return checkIfEmptyAndMapToDto("The teacher has no booked lesson", bookedLesson);
    }

    private List<BookedLessonDto> checkIfEmptyAndMapToDto(String errorMessage, List<BookedLesson> bookedLesson) {
        if (bookedLesson.isEmpty()) {
            throw new NotFoundException(errorMessage);
        }
        return bookedLesson.stream().
                map(x -> modelMapper.map(x, BookedLessonDto.class)).collect(Collectors.toList());
    }

}
