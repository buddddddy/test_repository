package org.mdlp.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 18.08.2017
 * @package org.mdlp.utils
 */
public interface ApplicationConstants {

    public static final Map<Integer, String> ERRORS_DESCRIPTION = new HashMap<Integer, String>(){{
        put(1, "Указанная сущность зарегистрирована ранее");
        put(2, "Указанная сущность не может быть идентифицирована (не зарегистрирована)");
        put(3, "Некорректная операция (ошибка отмены операции)");
        put(4, "Некорректная операция (операция не может быть выполнена для указанных реквизитов)");
        put(100, "Частичное завершение");
    }};
}
