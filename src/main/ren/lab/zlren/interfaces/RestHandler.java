package lab.zlren.interfaces;

import lab.zlren.beans.MethodInfo;
import lab.zlren.beans.ServerInfo;

public interface RestHandler {
    void init(ServerInfo serverInfo);

    Object invokeRest(MethodInfo methodInfo);
}
