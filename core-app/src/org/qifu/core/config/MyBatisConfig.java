/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.core.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages = {"org.qifu.core.mapper", "org.qifu.hillfog.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory")
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@PropertySource("classpath:db1-config.properties")
public class MyBatisConfig implements EnvironmentAware {
	
	@Autowired
	private Environment environment;
	
	//@Autowired
	private DataSource dataSource;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	@Primary
	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "db1.datasource")
	public DataSource dataSource() {
		
		System.out.println("===========================================");
		System.out.println(environment.getProperty("db1.datasource.jdbcUrl"));
		System.out.println(environment.getProperty("db1.datasource.jdbcUrl"));
		System.out.println(environment.getProperty("db1.datasource.jdbcUrl"));
		System.out.println("===========================================");
		
		this.dataSource = DataSourceBuilder.create().build();
		return this.dataSource;
	}	
	
	@Bean(name = "db1JdbcTemplate")
	@DependsOn("dataSource")
	public NamedParameterJdbcTemplate db1JdbcTemplate() {
		return new NamedParameterJdbcTemplate(this.dataSource);
	}
	
	@Bean(name = "sqlSessionFactory")
	@Primary
	@DependsOn("dataSource")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(this.dataSource);
        return sqlSessionFactoryBean.getObject();
    }
	
	@Bean("sqlSessionTemplate")
	@DependsOn("sqlSessionFactory")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}
	
    @Bean(name = "transactionManager")
    @DependsOn("dataSource")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
    	return new DataSourceTransactionManager(dataSource);
    }
    
}
