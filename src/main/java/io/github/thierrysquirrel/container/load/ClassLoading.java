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

package io.github.thierrysquirrel.container.load;

import io.github.thierrysquirrel.container.load.check.ClassLoadingCheck;
import io.github.thierrysquirrel.container.load.constant.ClassLoadingConstant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: ClassLoading
 * Description:
 * Date:2025/1/21
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ClassLoading {

    private static final Logger logger = Logger.getLogger(ClassLoading.class.getName());

    private ClassLoading() {
    }

    public static List<Object> resourceLoad() {

        List<Object> objectList = new ArrayList<>();
        try (InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(ClassLoadingConstant.RESOURCE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            Object thisObject = null;
            String line = reader.readLine();

            while (line != null) {
                boolean checkEqualSign = ClassLoadingCheck.checkEqualSign(line);
                if (!checkEqualSign) {
                    line = reader.readLine();
                    continue;
                }
                String[] dateKeyAndValue = line.split(ClassLoadingConstant.SPLIT_KEY_AND_VALUE);
                String dateKey = dateKeyAndValue[0];
                String dateValue = dateKeyAndValue[1];

                boolean checkClassForName = ClassLoadingCheck.checkClassForName(dateKey);
                if (checkClassForName) {
                    if (thisObject != null) {
                        objectList.add(thisObject);
                    }
                    Class<?> objectClass = Class.forName(dateValue);
                    thisObject = objectClass.getDeclaredConstructor().newInstance();

                    line = reader.readLine();
                    continue;
                }

                boolean checkMethod = ClassLoadingCheck.checkMethod(dateKey);
                if (!checkMethod) {
                    line = reader.readLine();
                    continue;
                }

                String[] methodParameterTypes = dateKey.split(ClassLoadingConstant.SPLIT_METHOD_TYPE);

                int methodTypeSize = methodParameterTypes.length - 2;

                Class<?>[] methodType = new Class[methodTypeSize];
                String[] methodValue = dateValue.split(ClassLoadingConstant.SPLIT_METHOD_VALUE);
                Object[] methodValueObjects = new Object[methodValue.length];

                objectMethodLoad(methodParameterTypes, methodType, methodValue, methodValueObjects);

                String methodName = methodParameterTypes[1];
                Method thisMethod = thisObject.getClass().getMethod(methodName, methodType);
                thisMethod.setAccessible(true);
                thisMethod.invoke(thisObject, methodValueObjects);
                line = reader.readLine();

            }

            if (thisObject != null) {
                objectList.add(thisObject);

            }

        } catch (Exception e) {
            String logMsg = "resourceLoad Error";
            logger.log(Level.WARNING, logMsg, e);
        }

        return objectList;

    }

    private static void objectMethodLoad(String[] methodParameterTypes, Class<?>[] methodType, String[] methodValue, Object[] methodValueObjects) {

        for (int i = 0; i < methodValue.length; i++) {

            int methodTypeIndex = i + 2;

            String thisMethodTypeString = methodParameterTypes[methodTypeIndex];
            String thisValue = methodValue[i];

            switch (thisMethodTypeString) {

                case "byte":
                    methodType[i] = byte.class;
                    methodValueObjects[i] = Byte.valueOf(thisValue);
                    break;
                case "Byte":
                    methodType[i] = Byte.class;
                    methodValueObjects[i] = Byte.valueOf(thisValue);
                    break;
                case "short":
                    methodType[i] = short.class;
                    methodValueObjects[i] = Short.valueOf(thisValue);
                    break;
                case "Short":
                    methodType[i] = Short.class;
                    methodValueObjects[i] = Short.valueOf(thisValue);
                    break;
                case "int":
                    methodType[i] = int.class;
                    methodValueObjects[i] = Integer.valueOf(thisValue);
                    break;
                case "Integer":
                    methodType[i] = Integer.class;
                    methodValueObjects[i] = Integer.valueOf(thisValue);
                    break;
                case "long":
                    methodType[i] = long.class;
                    methodValueObjects[i] = Long.valueOf(thisValue);
                    break;
                case "Long":
                    methodType[i] = Long.class;
                    methodValueObjects[i] = Long.valueOf(thisValue);
                    break;
                case "float":
                    methodType[i] = float.class;
                    methodValueObjects[i] = Float.valueOf(thisValue);
                    break;
                case "Float":
                    methodType[i] = Float.class;
                    methodValueObjects[i] = Float.valueOf(thisValue);
                    break;
                case "double":
                    methodType[i] = double.class;
                    methodValueObjects[i] = Double.valueOf(thisValue);
                    break;
                case "Double":
                    methodType[i] = Double.class;
                    methodValueObjects[i] = Double.valueOf(thisValue);
                    break;
                case "boolean":
                    methodType[i] = boolean.class;
                    methodValueObjects[i] = Boolean.valueOf(thisValue);
                    break;
                case "Boolean":
                    methodType[i] = Boolean.class;
                    methodValueObjects[i] = Boolean.valueOf(thisValue);
                    break;
                case "char":
                    methodType[i] = char.class;
                    methodValueObjects[i] = thisValue.charAt(0);
                    break;
                case "Character":
                    methodType[i] = Character.class;
                    methodValueObjects[i] = thisValue.charAt(0);
                    break;
                case "String":
                    methodType[i] = String.class;
                    methodValueObjects[i] = thisValue;
                    break;
            }
        }

    }

}
