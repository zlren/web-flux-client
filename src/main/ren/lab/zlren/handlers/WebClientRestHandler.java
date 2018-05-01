package lab.zlren.handlers;

import lab.zlren.beans.MethodInfo;
import lab.zlren.beans.ServerInfo;
import lab.zlren.interfaces.RestHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author zlren
 * @date 2018-05-01
 */
@Component
public class WebClientRestHandler implements RestHandler {

    private WebClient webClient;

    @Override
    public void init(ServerInfo serverInfo) {
        this.webClient = WebClient.create(serverInfo.getUrl());
    }

    /**
     * 处理rest请求
     *
     * @param methodInfo
     * @return
     */
    @Override
    public Object invokeRest(MethodInfo methodInfo) {

        Object result = null;
        WebClient.ResponseSpec request = this.webClient.method(methodInfo.getMethod()).uri(methodInfo.getUrl())
                .accept(MediaType.APPLICATION_JSON).retrieve();

        if (methodInfo.isReturnFlux()) {
            result = request.bodyToFlux(methodInfo.getReturnElementType());
        } else {
            result = request.bodyToMono(methodInfo.getReturnElementType());
        }

        return result;
    }
}
