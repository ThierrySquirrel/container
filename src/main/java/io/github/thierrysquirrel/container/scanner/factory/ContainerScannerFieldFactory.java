/**
 * Copyright 2025/1/21 ThierrySquirrel
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package io.github.thierrysquirrel.container.scanner.factory;

import io.github.thierrysquirrel.container.scanner.annotation.Set;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: ContainerScannerFieldFactory
 * Description:
 * Date:2025/1/21
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ContainerScannerFieldFactory {

    private ContainerScannerFieldFactory() {
    }

    private static final Logger logger = Logger.getLogger(ContainerScannerFieldFactory.class.getName());

    public static void fieldSet(Map<Class<?>, Object> registrationMap) {
        for (Map.Entry<Class<?>, Object> entry : registrationMap.entrySet()) {
            Class<?> key = entry.getKey();
            Object value = entry.getValue();
            for (Field field : key.getDeclaredFields()) {

                Set annotation = field.getAnnotation(Set.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    Object fieldObject = registrationMap.get(field.getType());
                    set(field, value, fieldObject);
                }
            }
        }
    }

    public static void set(Field field, Object thisObject, Object fieldObject) {
        try {
            field.set(thisObject, fieldObject);
        } catch (IllegalAccessException e) {
            String logMsg = "field set Error";
            logger.log(Level.WARNING, logMsg, e.getMessage());
        }
    }
}
