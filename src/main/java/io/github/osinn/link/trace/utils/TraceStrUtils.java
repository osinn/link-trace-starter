package io.github.osinn.link.trace.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 字符串工具类
 *
 * @author wency_cai
 **/
@Slf4j
public class TraceStrUtils {

    /**
     * 判断是否不为空
     *
     * @param obj 要判断空的对象
     * @return 空返回true, 否则返回false
     */
    public static boolean isEmpty(@Nullable Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        return false;
    }


}
