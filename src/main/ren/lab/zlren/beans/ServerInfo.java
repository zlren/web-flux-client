package lab.zlren.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 服务器信息
 *
 * @author zlren
 * @date 2018-05-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ServerInfo {
    /**
     * 服务器url
     */
    private String url;
}
