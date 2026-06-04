package jenkins.jenkins.student;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentManagerTest {

    private static StudentManager manager;

    @BeforeAll
    static void setUp() {
        manager = new StudentManager();
    }

    // 학생 추가
    @Test
    @Order(1)
    void addStudent_shouldMakeStudentPresent() {
        manager.addStudent("Alice");
        assertTrue(manager.hasStudent("Alice"));
    }

    // 학생 제거
    @Test
    @Order(2)
    void removeStudent_shouldMakeStudentAbsent() {
        manager.removeStudent("Alice");
        assertFalse(manager.hasStudent("Alice"));
    }

    // 중복 추가 예외 처리
    @Test
    @Order(3)
    void addStudent_duplicateShouldThrow() {
        manager.addStudent("Alice");
        assertThrows(IllegalArgumentException.class, () -> manager.addStudent("Alice"));
    }

    // 존재하지 않는 학생 제거 예외 처리
    @Test
    @Order(4)
    void removeStudent_nonexistentShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> manager.removeStudent("Bob"));
    }
}
