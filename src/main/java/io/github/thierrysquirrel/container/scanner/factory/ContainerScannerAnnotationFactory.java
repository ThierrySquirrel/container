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

import io.github.thierrysquirrel.container.scanner.annotation.Registration;
import io.github.thierrysquirrel.container.scanner.annotation.ScannerPackage;
import io.github.thierrysquirrel.container.scanner.registration.InterfaceManualRegistration;
import io.github.thierrysquirrel.container.scanner.registration.ManualRegistration;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: ContainerScannerAnnotationFactory
 * Description:
 * Date:2025/1/21
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ContainerScannerAnnotationFactory {
    private ContainerScannerAnnotationFactory() {
    }

    private static final Logger logger = Logger.getLogger(ContainerScannerAnnotationFactory.class.getName());


    public static void scannerAnnotation(Map<Class<?>, Object> registrationMap, Map<ScannerPackage, InterfaceManualRegistration> sacnnerRegistrationMap, Class<?> scannerClass) {
        Registration registration = scannerClass.getAnnotation(Registration.class);
        if (!Objects.isNull(registration)) {
            Object classNew = newInstance(scannerClass);
            registrationMap.put(scannerClass, classNew);
        }

        Class<?>[] interfaces = scannerClass.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            if (interfaceClass.equals(ManualRegistration.class)) {
                ManualRegistration classNewInstance = (ManualRegistration) newInstance(scannerClass);
                Object classNew = classNewInstance.manualRegistration(classNewInstance);

                registrationMap.put(scannerClass, classNew);
            }
        }

        ScannerPackage scannerPackage = scannerClass.getAnnotation(ScannerPackage.class);
        if (!Objects.isNull(scannerPackage)) {
            Object classNew = newInstance(scannerClass);
            sacnnerRegistrationMap.put(scannerPackage, (InterfaceManualRegistration) classNew);
        }
    }

    private static Object newInstance(Class<?> thisClass) {
        Object classNew = null;
        try {
            classNew = thisClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            String logMsg = "newInstance Error";
            logger.log(Level.WARNING, logMsg, e);
        }
        return classNew;
    }
}
