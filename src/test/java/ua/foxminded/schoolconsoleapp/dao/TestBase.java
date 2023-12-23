package ua.foxminded.schoolconsoleapp.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.schoolconsoleapp.dao.CourseDao;
import ua.foxminded.schoolconsoleapp.dao.GroupDao;
import ua.foxminded.schoolconsoleapp.dao.StudentDao;
import ua.foxminded.schoolconsoleapp.dao.impl.CourseDaoImpl;
import ua.foxminded.schoolconsoleapp.dao.impl.GroupDaoImpl;
import ua.foxminded.schoolconsoleapp.dao.impl.StudentDaoImpl;
import ua.foxminded.schoolconsoleapp.dao.mappers.CourseMapper;
import ua.foxminded.schoolconsoleapp.dao.mappers.GroupMapper;
import ua.foxminded.schoolconsoleapp.dao.mappers.StudentMapper;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class TestBase {

  @Container
  protected static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(
      "postgres:15")
      .withDatabaseName("test-db")
      .withUsername("root")
      .withPassword("test");

  protected static HikariDataSource dataSource;
  protected static JdbcTemplate jdbcTemplate;

  protected StudentDao studentDao;
  protected CourseDao courseDao;
  protected GroupDao groupDao;

  @Mock
  protected JdbcTemplate mockJdbcTemplate;

  @Mock
  protected StudentMapper mockStudentMapper;

  @Mock
  protected CourseMapper mockCourseMapper;

  @Mock
  protected GroupMapper mockGroupMapper;

  @InjectMocks
  protected StudentDaoImpl mockStudentDao;

  @InjectMocks
  protected CourseDaoImpl mockCourseDao;

  @InjectMocks
  protected GroupDaoImpl mockGroupDao;

  @BeforeAll
  public static void initialize() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(postgresqlContainer.getDriverClassName());
    hikariConfig.setJdbcUrl(postgresqlContainer.getJdbcUrl());
    hikariConfig.setUsername(postgresqlContainer.getUsername());
    hikariConfig.setPassword(postgresqlContainer.getPassword());

    dataSource = new HikariDataSource(hikariConfig);
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @BeforeEach
  @Tag("database")
  protected void setUp() {
    StudentMapper studentMapper = new StudentMapper();
    CourseMapper courseMapper = new CourseMapper();
    GroupMapper groupMapper = new GroupMapper();

    studentDao = new StudentDaoImpl(jdbcTemplate, studentMapper);
    courseDao = new CourseDaoImpl(jdbcTemplate, courseMapper);
    groupDao = new GroupDaoImpl(jdbcTemplate, groupMapper);

    String sqlStatements = readSqlStatements("src/test/resources/db/sql/test_tables.sql");
    jdbcTemplate.execute(sqlStatements);

    sqlStatements = readSqlStatements("src/test/resources/db/sql/testing_data.sql");
    jdbcTemplate.execute(sqlStatements);
  }

  private String readSqlStatements(String filePath) {
    try {
      return Files.lines(Paths.get(filePath)).collect(Collectors.joining(System.lineSeparator()));
    } catch (IOException e) {
      throw new RuntimeException("Failed to read SQL file", e);
    }
  }

  @AfterAll
  public static void cleanup() {
    if (dataSource != null) {
      dataSource.close();
    }
  }

}
