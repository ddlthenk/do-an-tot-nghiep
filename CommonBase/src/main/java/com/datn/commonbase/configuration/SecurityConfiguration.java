package com.datn.commonbase.configuration;

//@Configuration
//@EnableWebSecurity
public class SecurityConfiguration {
//    @Autowired
//    UserService userService;
//    @Autowired
//    UserDetailsService userDetailsService;
//    @Autowired
//    DataSource dataSource;
//
//    private static final Logger _log = LogManager.getLogger(SecurityConfiguration.class);
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .rememberMe(rememberMe -> rememberMe
//                        .tokenRepository(persistentTokenRepository())
//                        .tokenValiditySeconds(7 * 24 * 60 * 60));
//
//        return http.build();
//    }
//
//    public static class RefererRedirectionAuthenticationSuccessHandler
//            implements AuthenticationSuccessHandler {
//
//        @Override
//        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                            Authentication authentication) throws IOException, ServletException {
//            String refererUrl = request.getHeader("Referer");
//            response.sendRedirect(refererUrl != null ? refererUrl : "/home");
//        }
//    }
//
//    public class MySavedRequestAwareHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//        @Override
//        protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
//            SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
//            if (savedRequest != null) {
//                return savedRequest.getRedirectUrl();
//            }
//            return super.determineTargetUrl(request, response);
//        }
//    }
//
//    @Autowired
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // Set your configuration on the auth object
////        auth.jdbcAuthentication().dataSource(dataSource);
////                .usersByUsernameQuery("select email, password from tbl_user where email = ?");
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            UserDetailsService userDetailsService,
//            PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder);
//        return new ProviderManager(authenticationProvider);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        return bCryptPasswordEncoder;
//    }
//
//    @Bean
//    public JdbcTokenRepositoryImpl persistentTokenRepository() {
//        JdbcTokenRepositoryImpl memory = new JdbcTokenRepositoryImpl();
//        memory.setDataSource(dataSource);
//        _log.error("Persistent token repository created");
//        return memory;
//    }
}
