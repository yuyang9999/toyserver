package stock.components.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yangyu on 15/3/18.
 */
@Configuration
@PropertySource({"classpath:persistence.properties"})
public class DataSourceConfig {
    @Value("classpath:schema.sql")
    private Resource schemaScript;

    @Value("classpath:data.sql")
    private Resource dataScript;

    @Value("classpath:sp500_short.csv")
    private Resource sp500Data;

    @Autowired
    private Environment env;

    @Autowired
    public DataSourceInitializer dataSourceInitializer() {
        DataSource dataSource = dataSource();
        try {
            Connection connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        initializer.afterPropertiesSet();


        populateSp500Data(dataSource);

        return initializer;
    }

    private void populateSp500Data(final DataSource dataSource) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();

            BufferedReader reader = new BufferedReader(new InputStreamReader(sp500Data.getInputStream()));
            int cnt = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                cnt ++;
                if (cnt == 1) {
                    //skip the first line
                    continue;
                }

                String[] parts = line.split("\t");
                if (parts.length != 4) {
                    continue;
                }

                String ticker = parts[0];
                String sector = parts[1];
                String subSector = parts[2];
                String name = parts[3];

                String sql = String.format("insert into sp500_symbols (symbol, sector, subsector) values (\"%s\", \"%s\", \"%s\")",  ticker, sector, subSector);
                statement.execute(sql);
            }
            connection.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
//        populator.addScript(dataScript);

        return populator;
    }

    private DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));
        return dataSource;
    }


//    public static void main(String[] args) throws Exception {
//        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//        ctx.register(DataSourceConfig.class);
//        ctx.refresh();
//    }
}
