package test.skywalking.springcloud.test.projectb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.skywalking.springcloud.test.projectb.service.ServiceController;

import static test.skywalking.springcloud.test.projectb.config.Constant.SAMPLE_RATE;

@Component
public class DatabaseOperateDao {

    private static final Logger logger = LogManager.getLogger(DatabaseOperateDao.class);

    private AtomicLong counter = new AtomicLong(0);

    @Autowired
    private DataSource dataSource;

    @Trace
    public void saveUser(String name) {
        if (counter.getAndIncrement() % SAMPLE_RATE == 0) {
            logger.debug("Save user {}", name);
        }
        ActiveSpan.tag("user.name", name);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO user(name) VALUES(?)");
            preparedStatement.setString(1, name == null ? "" : name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    @Trace(operationName = "selectUser")
    public void selectUser(String name) {
        if (counter.getAndIncrement() % SAMPLE_RATE == 0) {
            logger.debug("select user {}", name);
        }
        ActiveSpan.tag("user.name", name);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE name =?");
            preparedStatement.setString(1, name == null ? "" : name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
    }

}
