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

package io.github.thierrysquirrel.container.scanner;

import io.github.thierrysquirrel.container.load.ClassLoading;
import io.github.thierrysquirrel.container.scanner.annotation.ScannerPackage;
import io.github.thierrysquirrel.container.scanner.factory.ContainerScannerAnnotationFactory;
import io.github.thierrysquirrel.container.scanner.factory.ContainerScannerFactory;
import io.github.thierrysquirrel.container.scanner.factory.ContainerScannerFieldFactory;
import io.github.thierrysquirrel.container.scanner.init.RegistrationInit;
import io.github.thierrysquirrel.container.scanner.registration.InterfaceManualRegistration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Classname: ContainerScanner
 * Description:
 * Date:2025/1/21
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ContainerScanner {
    private Map<Class<?>, Object> registrationMap = new HashMap<>();
    private Map<ScannerPackage, InterfaceManualRegistration> scannerRegistrationMap = new HashMap<>();


    public Map<Class<?>, Object> scannerAll(Class<?> applicationClass) {
        List<Object> resourcedList = ClassLoading.resourceLoad();
        for (Object resourcedObject : resourcedList) {
            Class<?> resourcedObjectClass = resourcedObject.getClass();
            registrationMap.put(resourcedObjectClass, resourcedObject);

            ScannerPackage scannerPackage = resourcedObjectClass.getAnnotation(ScannerPackage.class);
            if (!Objects.isNull(scannerPackage)) {
                String packageName = scannerPackage.packageName();
                scannerAllAndAnnotation(applicationClass, packageName);
            }
        }

        scannerAllAndAnnotation(applicationClass, applicationClass.getPackage().getName());

        for (Map.Entry<ScannerPackage, InterfaceManualRegistration> entry : scannerRegistrationMap.entrySet()) {
            ScannerPackage scannerPackage = entry.getKey();
            InterfaceManualRegistration interfaceManualRegistration = entry.getValue();

            List<Class<?>> sacnnerClassList = ContainerScannerFactory.scannerClassAndJar(applicationClass, scannerPackage.packageName());
            interfaceManualRegistration.scannerAll(sacnnerClassList, registrationMap);
        }

        ContainerScannerFieldFactory.fieldSet(registrationMap);

        for (Object value : registrationMap.values()) {
            if (value instanceof RegistrationInit registration) {
                registration.init(registrationMap);
            }
        }

        return this.registrationMap;
    }

    public void scannerAllAndAnnotation(Class<?> applicationClass, String packageName) {
        List<Class<?>> classList = ContainerScannerFactory.scannerClassAndJar(applicationClass, packageName);
        for (Class<?> type : classList) {
            ContainerScannerAnnotationFactory.scannerAnnotation(this.registrationMap, this.scannerRegistrationMap, type);
        }

    }

    public Map<Class<?>, Object> getRegistrationMap() {
        return registrationMap;
    }

    public void setRegistrationMap(Map<Class<?>, Object> registrationMap) {
        this.registrationMap = registrationMap;
    }

    public Map<ScannerPackage, InterfaceManualRegistration> getScannerRegistrationMap() {
        return scannerRegistrationMap;
    }
}
