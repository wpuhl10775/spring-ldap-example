package hello;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${spring.ldap.base}")
  String ldapBase;

  @Value("${spring.ldap.dnPattern}")
  String ldapDnPattern;

	@Value("${spring.ldap.group.search.base}")
	String ldapGroupSearchBase;

  @Value("${spring.ldap.password}")
  String userPassword;

  @Value("${spring.ldap.url}")
  String ldapUrl;

  @Value("${spring.ldap.role.attribute}")
  String ldapRoleAttribute;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().fullyAuthenticated()
				.and()
			.formLogin();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.ldapAuthentication()
				.userDnPatterns(ldapDnPattern)
				.groupSearchBase(ldapGroupSearchBase)
				.contextSource(contextSource())
				.passwordCompare()
					.passwordEncoder(new LdapShaPasswordEncoder())
					.passwordAttribute(userPassword);

    if( !ldapRoleAttribute.isEmpty()) {
      auth.ldapAuthentication()
        .groupRoleAttribute(ldapRoleAttribute);
    }
	}

	@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		return  new DefaultSpringSecurityContextSource(Arrays.asList(ldapUrl), ldapBase);
	}
}