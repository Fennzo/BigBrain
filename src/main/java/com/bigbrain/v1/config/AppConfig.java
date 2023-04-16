package com.bigbrain.v1.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.HeaderWriterFilter;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig {

	//TODO handle logout
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/", "/registration", "/css/**", "/fonts/**", "/images/**", "/js/**", "/layui/**").permitAll()  // allows any request to the root URL ("/") and the registration URL ("/registration") without authentication
						.requestMatchers("/user/**").hasAnyAuthority("Homeowner", "Manager") // only users with homeowner role
						.requestMatchers("/admin/**").hasAuthority("Manager") // only users with manager role
						.requestMatchers("/mainteenance/**").hasAnyAuthority("Maintenance")
						.anyRequest().authenticated()
				)
				.oauth2Login()
				.defaultSuccessUrl("/backend", true)
				.loginPage("/");

		HeaderWriter headerWriter = new HeaderWriter() {
			@Override
			public void writeHeaders(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
				httpServletResponse.setHeader("X-Frame-Options","SAMEORIGIN");
			}
		};
		HeaderWriterFilter filter = new HeaderWriterFilter(Collections.singletonList(headerWriter));
		http.addFilter(filter);

		return http.build();
	}


	@Bean
	public DataSource getDatasource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		datasource.setUrl("jdbc:sqlserver://localhost:1433;database=CapstoneDatabase;encrypt=true;trustServerCertificate=true;integratedSecurity=true");
	//	datasource.setUrl("jdbc:sqlserver://localhost:1433;database=CapstoneDatabase;encrypt=false");
//		datasource.setUsername("jyj123");
	//	datasource.setUsername("sa");
//		datasource.setPassword("jpoo1234");
	//	datasource.setPassword("hhy960918");
		return datasource;
	}

	@Bean("jdbcTemplate")
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
