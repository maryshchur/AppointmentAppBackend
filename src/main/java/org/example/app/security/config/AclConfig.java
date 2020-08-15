package org.example.app.security.config;

import org.example.app.constants.AclQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;

import javax.sql.DataSource;

@Configuration
public class AclConfig {

    private final DataSource dataSource;

    /**
     * Constructor
     */
    @Autowired
    public AclConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Bean {@link MethodSecurityExpressionHandler} is used by Spring Security to evaluate security
     * expressions
     */
    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        AclPermissionEvaluator aclPermissionEvaluator = new AclPermissionEvaluator(aclService());
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator);
        return expressionHandler;
    }

    @Bean
    public MutableAclService aclService() {
        JdbcMutableAclService jdbcMutableAclService =
                new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());

        jdbcMutableAclService.setClassIdentityQuery(AclQueries.CLASS_IDENTITY_QUERY);
        jdbcMutableAclService.setSidIdentityQuery(AclQueries.SID_IDENTITY_QUERY);

        jdbcMutableAclService.setObjectIdentityPrimaryKeyQuery(AclQueries.OBJECT_IDENTITY_PRIMARY_KEY_QUERY);
        jdbcMutableAclService.setFindChildrenQuery(AclQueries.FIND_CHILDREN_QUERY);

        return jdbcMutableAclService;
    }
    /**
     * Bean {@link EhCacheBasedAclCache} that uses for caching ACLs
     */
    @Bean
    public EhCacheBasedAclCache aclCache() {
        return new EhCacheBasedAclCache(
                aclEhCacheFactoryBean().getObject(),
                permissionGrantingStrategy(),
                aclAuthorizationStrategy()
        );
    }
    /**
     * Bean {@link PermissionGrantingStrategy} is responsible for granting or denying access to secure objects
     * depending on the permissions assigned to SIDs
     */
    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }
    /**
     * Bean{@link LookupStrategy} is responsible for looking up ACL information
     */
    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(dataSource,
                aclCache(),
                aclAuthorizationStrategy(),
                new ConsoleAuditLogger());
    }
    /**
     * Bean {@link AclAuthorizationStrategy} represents the strategy to
     * determine if a SID has the permissions to perform administrative actions on the
     * ACL entries of a domain object instance
     */
    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(() -> "ROLE_ADMIN");
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }
    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        return new EhCacheManagerFactoryBean();
    }


}
