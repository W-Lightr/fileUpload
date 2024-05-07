package cn.lightr.fileupload.config;


import com.feiniaojin.gracefulresponse.EnableGracefulResponse;
import com.feiniaojin.gracefulresponse.ExceptionAliasRegister;
import com.feiniaojin.gracefulresponse.GracefulResponseException;
import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import com.feiniaojin.gracefulresponse.advice.GlobalExceptionAdvice;
import com.feiniaojin.gracefulresponse.api.ExceptionAliasFor;
import com.feiniaojin.gracefulresponse.api.ExceptionMapper;
import com.feiniaojin.gracefulresponse.api.ResponseFactory;
import com.feiniaojin.gracefulresponse.api.ResponseStatusFactory;
import com.feiniaojin.gracefulresponse.data.Response;
import com.feiniaojin.gracefulresponse.data.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * @author Lightr
 * @date 2024/1/17 16:16
 * @Description 优雅的响应
 */
@Configuration
@EnableGracefulResponse
public class ResponseConfig {
    @Bean
    @Order(1)
    public GlobalExceptionAdvice getGlobalExceptionAdvice2(){
        return new GlobalExceptionAdvice2();
    }

}
@Slf4j
class GlobalExceptionAdvice2 extends GlobalExceptionAdvice {
    @Value("${graceful-response.isReturnAllResponse}")
    private boolean isReturnAllResponse;
    @Resource
    private ResponseFactory responseFactory;
    @Resource
    private ResponseStatusFactory responseStatusFactory;
    @Resource
    private GracefulResponseProperties gracefulResponseProperties;
    private ExceptionAliasRegister exceptionAliasRegister;
    @Override
    public Response exceptionHandler(Throwable throwable) {
        //捕获全局异常
        if (gracefulResponseProperties.isPrintExceptionInGlobalAdvice()) {
            log.error("GlobalExceptionAdvice2捕获到异常,message=[{}]", throwable.getMessage(), throwable);
        }

        ResponseStatus statusLine;
        if (throwable instanceof GracefulResponseException) {
            statusLine = this.fromGracefulResponseExceptionInstance((GracefulResponseException)throwable);
        } else {
            statusLine = this.fromExceptionClass(throwable);
        }

        return this.responseFactory.newInstance(statusLine);
    }
    private ResponseStatus fromGracefulResponseExceptionInstance(GracefulResponseException exception) {
        return this.responseStatusFactory.newInstance(exception.getCode(), exception.getMsg());
    }

    private ResponseStatus fromExceptionClass(Throwable throwable) {
        Class<? extends Throwable> clazz = throwable.getClass();
        ExceptionMapper exceptionMapper = (ExceptionMapper)clazz.getAnnotation(ExceptionMapper.class);
        if (exceptionMapper != null) {
            return this.responseStatusFactory.newInstance(exceptionMapper.code(), exceptionMapper.msg());
        } else {
            if (this.exceptionAliasRegister != null) {
                ExceptionAliasFor exceptionAliasFor = this.exceptionAliasRegister.getExceptionAliasFor(clazz);
                if (exceptionAliasFor != null) {
                    return this.responseStatusFactory.newInstance(exceptionAliasFor.code(), exceptionAliasFor.msg());
                }
            }
            //是否返回所有异常详情内容
            if (isReturnAllResponse){
                try {
                    return this.responseStatusFactory.newInstance("500",throwable.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return this.responseStatusFactory.defaultError();
        }
    }
}
