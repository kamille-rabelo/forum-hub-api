package forum.hub.api.domain.course;

import lombok.Getter;

@Getter
public enum CourseCategory {

    PROGRAMMING("Programming"),
    DATASCIENCE("Data Science"),
    DESIGN("Design"),
    MARKETING("Marketing"),
    JAVA("Java"),
    PYTHON("Python"),
    C("C"),
    CPLUSPLUS("C++"),
    CSHARP("C#"),
    RUBY("Ruby"),
    SWIFT("Swift"),
    JAVASCRIPT("JavaScript"),
    PHP("PHP"),
    HTML("HTML"),
    CSS("CSS"),
    SQL("SQL"),
    KOTLIN("Kotlin"),
    GO("Go"),
    R("R"),
    RUST("Rust"),
    LUA("Lua");

    private final String value;

    CourseCategory(String value) {
        this.value = value;
    }

}
