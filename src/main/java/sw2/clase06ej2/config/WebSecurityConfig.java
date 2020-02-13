package sw2.clase06ej2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/processLogin")
                .defaultSuccessUrl("/redirectByRole", true);

        http.logout()
                .logoutSuccessUrl("/");

        http.authorizeRequests()
                .antMatchers("/employee", "/employee/**").hasAnyAuthority("admin", "logistica")
                .antMatchers("/shipper", "/shipper/**").hasAuthority("admin")
                .anyRequest().permitAll();
    }

    @Autowired
    DataSource datasource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .dataSource(datasource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("SELECT email, pwd, activo FROM usuario WHERE email = ?")
                .authoritiesByUsernameQuery("SELECT u.email, r.nombre FROM usuario u INNER JOIN " +
                        "rol r ON (u.idrol = r.idrol) WHERE u.email = ? and u.activo = 1");
    }

}
