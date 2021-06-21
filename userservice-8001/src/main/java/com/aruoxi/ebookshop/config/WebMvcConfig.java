package com.aruoxi.ebookshop.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedHeaders("Content-Type","X-Requested-With","accept,Origin","Access-Control-Request-Method","Access-Control-Request-Headers","token","Authorization")
//                .allowedMethods("*")
//                .allowedOriginPatterns("*");
////                .allowCredentials(true);
//    }
//}
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
public class WebMvcConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //1、配置跨域
        corsConfiguration.addAllowedHeader("*");//  支持哪些请求头跨域
        corsConfiguration.addAllowedMethod("*");//  支持哪些方法跨域
        corsConfiguration.addAllowedOrigin("*");//  支持哪些来源跨域，需要跨域的地址  注意这里的 127.0.0.1 != localhost
        corsConfiguration.setAllowCredentials(true);//跨域请求默认不包含cookie，设置true时可包含cookie

        //Aecess-Control-Allow-Origin       支持哪些来源跨域，需要跨域的地址  注意这里的 127.0.0.1 != localhost
        //Access-Control-Allow-Methods      支持哪些方法跨域
        //Access-Control-Allow-Credentials  跨域请求默认不包含cookie，设置true时可包含cookie
        //Access-Control-Expose-Headers     支持哪些请求头跨域
        //Access-control-max-age            表明该响应时间为多少秒，在有效时间内，
        //浏览器无需为同一请求在次发起预检请求，请注意，浏览器自己维护了最大有效时间，
        // 如果该字段的值超过了最大有效时间，将不会生效
        /**corsConfiguration.exposedHeaders（"access-control-max-age"）
         * CORS请求时，XMLHttpRequest对象的getResponseHeader()方法只能拿到6个基本字段
         * Cache-Control、Content-Language、Content-Type、Expires、Last-Modified、Pragma。
         * 如果先得到其他字段必须在Access-Control-Expose-Headers中指定
         */


        //注册跨域配置，/**代表任意路径
        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(source);
    }
}