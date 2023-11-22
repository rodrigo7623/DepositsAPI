package cavapy.api.py.datasource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

public class ApiDatasource {


    private final JndiDataSourceLookup lookup = new JndiDataSourceLookup();

    public DataSource getDataSource(
                                    String jndiName,
                                    String url,
                                    String username,
                                    String password,
                                    String driverClassName) {
        try {
            return lookup.getDataSource(jndiName);
        }catch (DataSourceLookupFailureException e) {
            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.url(url);
            dataSourceBuilder.username(username);
            dataSourceBuilder.password(password);
            dataSourceBuilder.driverClassName(driverClassName);
            return dataSourceBuilder.build();
        }
    }

}
