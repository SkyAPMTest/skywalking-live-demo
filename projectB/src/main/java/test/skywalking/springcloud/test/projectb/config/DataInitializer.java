package test.skywalking.springcloud.test.projectb.config;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final DataSource dataSource;

    public DataInitializer(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... strings) throws Exception {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS `user`");
        statement.execute("CREATE TABLE `user` (\n" + "  `id`   INT(20)     NOT NULL AUTO_INCREMENT,\n"
                + "  `name` VARCHAR(50) NOT NULL,\n" + "  PRIMARY KEY (`id`)\n" + ");");

        statement.close();
        connection.close();
    }
}
