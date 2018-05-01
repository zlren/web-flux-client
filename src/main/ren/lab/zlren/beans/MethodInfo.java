package lab.zlren.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 方法调用信息类
 *
 * @author zlren
 * @date 2018-05-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MethodInfo {
    private String url;
    private HttpMethod method;
    private Map<String, Object> params;

    private Mono<?> body;
    /**
     * Flux还是Mono
     */
    private boolean returnFlux;

    /**
     * 返回对象的类型
     */
    private Class<?> returnElementType;
}
