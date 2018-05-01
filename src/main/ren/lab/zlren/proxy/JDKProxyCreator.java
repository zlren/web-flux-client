package lab.zlren.proxy;

import lab.zlren.ApiServer;
import lab.zlren.beans.MethodInfo;
import lab.zlren.beans.ServerInfo;
import lab.zlren.handlers.WebClientRestHandler;
import lab.zlren.interfaces.ProxyCreator;
import lab.zlren.interfaces.RestHandler;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于JDK动态代理实现代理类
 *
 * @author zlren
 * @date 2018-05-01
 */
@Component
public class JDKProxyCreator implements ProxyCreator {

    @Override
    public Object createProxy(Class<?> type) {

        // 服务器信息
        ServerInfo serverInfo = extractServerInfo(type);

        // 给每一个代理类一个实现
        RestHandler handler = new WebClientRestHandler();
        // 初始化服务器信息（webclient）
        handler.init(serverInfo);

        return Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{type},
                (proxy, method, args) -> {
                    // 根据方法和参数得到调用信息
                    MethodInfo methodInfo = extractMethodInfo(method, args);
                    // 调用rest
                    return handler.invokeRest(methodInfo);
                });
    }

    private MethodInfo extractMethodInfo(Method method, Object[] args) {
        MethodInfo methodInfo = new MethodInfo();
        extractUrlMethod(methodInfo, method);
        extractRequestParamAndBody(method, args, methodInfo);
        extractReturnInfo(methodInfo, method);
        return methodInfo;
    }

    /**
     * 提取返回对象信息
     *
     * @param methodInfo
     * @param method
     */
    private void extractReturnInfo(MethodInfo methodInfo, Method method) {
        boolean isFlux = method.getReturnType().isAssignableFrom(Flux.class);
        methodInfo.setReturnFlux(isFlux);

        Class<?> elementType = extractElementType(method.getGenericReturnType());
        methodInfo.setReturnElementType(elementType);
    }

    private Class<?> extractElementType(Type genericReturnType) {
        Type[] types = ((ParameterizedType) genericReturnType).getActualTypeArguments();
        return (Class<?>) types[0];
    }

    /**
     * 得到请求的param和body
     *
     * @param method
     * @param args
     * @param methodInfo
     */
    private void extractRequestParamAndBody(Method method, Object[] args, MethodInfo methodInfo) {
        Parameter[] parameters = method.getParameters();
        Map<String, Object> params = new LinkedHashMap<>();
        methodInfo.setParams(params);
        for (int i = 0; i < parameters.length; i++) {
            // 是否带@PathVariable
            PathVariable annoPath = parameters[i].getAnnotation(PathVariable.class);
            if (annoPath != null) {
                params.put(annoPath.value(), args[i]);
            }

            // 是否带了RequestBody
            RequestBody annoBody = parameters[i].getAnnotation(RequestBody.class);
            if (annoBody != null) {
                methodInfo.setBody((Mono<?>) args[i]);
            }
        }
    }

    /**
     * 得到请求的url和method
     *
     * @param methodInfo
     * @param method
     * @return
     */
    private MethodInfo extractUrlMethod(MethodInfo methodInfo, Method method) {

        for (Annotation annotation : method.getAnnotations()) {
            // GET
            if (annotation instanceof GetMapping) {
                GetMapping a = (GetMapping) annotation;

                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.GET);
            }
            // POST
            else if (annotation instanceof PostMapping) {
                PostMapping a = (PostMapping) annotation;

                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.POST);
            }
            // DELETE
            else if (annotation instanceof DeleteMapping) {
                DeleteMapping a = (DeleteMapping) annotation;

                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.DELETE);
            }
        }
        return methodInfo;
    }

    private ServerInfo extractServerInfo(Class<?> type) {
        return new ServerInfo().setUrl(type.getAnnotation(ApiServer.class).value());
    }
}
