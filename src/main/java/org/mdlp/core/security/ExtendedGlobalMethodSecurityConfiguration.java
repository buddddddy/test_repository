package org.mdlp.core.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.annotation.Jsr250MethodSecurityMetadataSource;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.annotation.SecuredAnnotationSecurityMetadataSource;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

import java.util.ArrayList;
import java.util.List;

//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//@ConditionalOnClass({EnableGlobalMethodSecurity.class, MethodSecurityMetadataSource.class, AbstractFallbackMethodSecurityMetadataSource.class})
public class ExtendedGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private ApplicationContext context;

    private Jsr250MethodSecurityMetadataSource jsr250MethodSecurityMetadataSource;

    @NotNull
    @Override
    public MethodSecurityMetadataSource methodSecurityMetadataSource() {
        List<AbstractFallbackMethodSecurityMetadataSource> sources = new ArrayList<>();

        ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(getExpressionHandler());
        sources.add(new PrePostAnnotationFallbackSecurityMetadataSource(attributeFactory));

        sources.add(new SecuredAnnotationSecurityMetadataSource());

        GrantedAuthorityDefaults grantedAuthorityDefaults = getSingleBeanOrNull(GrantedAuthorityDefaults.class);
        if (grantedAuthorityDefaults != null) this.jsr250MethodSecurityMetadataSource.setDefaultRolePrefix(grantedAuthorityDefaults.getRolePrefix());
        sources.add(jsr250MethodSecurityMetadataSource);

        return new ExtendedMethodSecurityMetadataSource(sources);
    }

    @NotNull
    @Override
    protected AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();

        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());
        decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expressionAdvice));

        decisionVoters.add(new Jsr250Voter());

        RoleHierarchy roleHierarchy = getSingleBeanOrNull(RoleHierarchy.class);
        if (null != roleHierarchy) {
            decisionVoters.add(new RoleHierarchyVoter(roleHierarchy));
        } else {
            decisionVoters.add(new RoleVoter());
        }

        decisionVoters.add(new AuthenticatedVoter());

        return new UnanimousBased(decisionVoters);
    }

    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
        super.setApplicationContext(context);
    }

    @Override
    @Autowired
    public void setJsr250MethodSecurityMetadataSource(Jsr250MethodSecurityMetadataSource jsr250MethodSecurityMetadataSource) {
        this.jsr250MethodSecurityMetadataSource = jsr250MethodSecurityMetadataSource;
        super.setJsr250MethodSecurityMetadataSource(jsr250MethodSecurityMetadataSource);
    }

    private <T> T getSingleBeanOrNull(Class<T> type) {
        String[] beanNamesForType = this.context.getBeanNamesForType(type);
        if (beanNamesForType == null || beanNamesForType.length != 1) {
            return null;
        }
        return this.context.getBean(beanNamesForType[0], type);
    }

}
