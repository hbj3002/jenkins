package student;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentManagerTest2 {

    private static StudentManager manager;

    @BeforeAll
    static void setUp() {
        manager = new StudentManager();
    }

    // 학생 추가
    @Test
    void addStudent_shouldMakeStudentPresent() {
        manager.addStudent("Alice");
        assertTrue(manager.hasStudent("Alice"));
    }

    // 학생 제거
    @Test
    void removeStudent_shouldMakeStudentAbsent() {
        manager.addStudent("Bob");
        manager.removeStudent("Bob");
        assertFalse(manager.hasStudent("Bob"));
    }

    // 중복 추가 예외 처리
    @Test
    void addStudent_duplicateShouldThrow() {
        manager.addStudent("Carol");
        assertThrows(IllegalArgumentException.class, () -> manager.addStudent("Carol"));
    }

    // 존재하지 않는 학생 제거 예외 처리
    @Test
    void removeStudent_nonexistentShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> manager.removeStudent("Dave"));
    }
}
