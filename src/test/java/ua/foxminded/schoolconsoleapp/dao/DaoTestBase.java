package ua.foxminded.schoolconsoleapp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    StudentDao.class, GroupDao.class, CourseDao.class}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = {"classpath:db/sql/test_tables.sql",
    "classpath:db/sql/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Testcontainers
public abstract class DaoTestBase {

  @Container
  protected static final PostgreSQLContainer<?> postgresqlContainer =
      new PostgreSQLContainer<>("postgres:15")
          .withDatabaseName("test-db")
          .withUsername("root")
          .withPassword("test");

  @PersistenceContext
  protected EntityManager entityManager;

  @Autowired
  protected StudentDao studentDao;

  @Autowired
  protected GroupDao groupDao;

  @Autowired
  protected CourseDao courseDao;

}
