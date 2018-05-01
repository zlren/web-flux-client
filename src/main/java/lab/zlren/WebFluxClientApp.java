package lab.zlren;

import lab.zlren.client.service.IUserApi;
import lab.zlren.interfaces.ProxyCreator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author zlren
 * @date 2018-05-01
 */
@SpringBootApplication
public class WebFluxClientApp {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxClientApp.class, args);
    }

    @Bean
    FactoryBean<IUserApi> userApi(ProxyCreator creator) {
        return new FactoryBean<IUserApi>() {

            /**
             * 返回代理对象
             * @return
             * @throws Exception
             */
            @Override
            public IUserApi getObject() throws Exception {
                return (IUserApi) creator.createProxy(getObjectType());
            }

            @Override
            public Class<?> getObjectType() {
                return IUserApi.class;
            }
        };
    }
}
