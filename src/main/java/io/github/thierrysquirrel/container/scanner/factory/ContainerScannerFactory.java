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

import io.github.thierrysquirrel.container.scanner.factory.constant.ContainerScannerFactoryConstant;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Classname: ContainerScannerFactory
 * Description:
 * Date:2025/1/21
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ContainerScannerFactory {

    private ContainerScannerFactory() {
    }

    private static final Logger logger = Logger.getLogger(ContainerScannerFactory.class.getName());

    public static List<Class<?>> scannerClassAndJar(Class<?> applicationClass, String packageName) {
        boolean isJar = scannerIsJar(applicationClass);
        if (isJar) {
            return scannerJar(applicationClass, packageName);
        } else {
            return scannerClass(packageName);
        }
    }

    public static List<Class<?>> scannerClass(String packageName) {
        List<Class<?>> classList = new ArrayList<>();

        String fileName = packageName.replace(ContainerScannerFactoryConstant.DOT, ContainerScannerFactoryConstant.SLASH);
        String resourceUrl = ClassLoader.getSystemClassLoader().getResource(fileName).getFile();

        int indexOfJar = resourceUrl.indexOf(ContainerScannerFactoryConstant.JAR);
        if (indexOfJar != -1) {
            int beginIndex = ContainerScannerFactoryConstant.MAVEN_FILE.length();
            int endIndex = indexOfJar + ContainerScannerFactoryConstant.JAR.length();
            String jarUrl = resourceUrl.substring(beginIndex, endIndex);

            String packagePath = packageName.replace(ContainerScannerFactoryConstant.DOT, ContainerScannerFactoryConstant.SLASH);
            return scannerJar(packagePath, jarUrl);
        }

        File file = new File(resourceUrl);

        Stream<Path> walkStream = createWalkStream(file.toPath());
        walkStream.forEach(path -> addClassLoader(classList, path));
        return classList;
    }

    public static List<Class<?>> scannerJar(Class<?> applicationClass, String packageName) {
        ProtectionDomain protectionDomain = applicationClass.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URL location = codeSource.getLocation();

        String path = location.getPath();
        String packagePath = packageName.replace(ContainerScannerFactoryConstant.DOT, ContainerScannerFactoryConstant.SLASH);
        return scannerJar(packagePath, path);
    }

    public static boolean scannerIsJar(Class<?> applicationClass) {

        ProtectionDomain protectionDomain = applicationClass.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URL location = codeSource.getLocation();

        String path = location.getPath();
        if (path.endsWith(ContainerScannerFactoryConstant.JAR)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;

    }

    private static List<Class<?>> scannerJar(String packagePath, String path) {
        List<Class<?>> classeList = new ArrayList<>();
        URL pathUrl = fileToUrl(new File(path));

        try (JarFile jarFile = new JarFile(path);
             URLClassLoader classLoader = new URLClassLoader(new URL[]{pathUrl})) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.startsWith(packagePath) && entryName.endsWith(ContainerScannerFactoryConstant.COMPILE_FILE_SUFFIX)) {
                    String className = entryName.replace(ContainerScannerFactoryConstant.SLASH, ContainerScannerFactoryConstant.DOT)
                            .replace(ContainerScannerFactoryConstant.COMPILE_FILE_SUFFIX, ContainerScannerFactoryConstant.EMPTY);

                    Class<?> clazz = classLoader.loadClass(className);
                    classeList.add(clazz);
                }
            }

        } catch (Exception e) {
            String logMsg = "JarFile Error";
            logger.log(Level.WARNING, logMsg, e.getMessage());
        }

        return classeList;
    }


    private static Stream<Path> createWalkStream(Path path) {
        Stream<Path> walkStream = null;
        try {
            walkStream = Files.walk(path);
        } catch (IOException e) {
            String logMsg = "createWalkStream Error";
            logger.log(Level.WARNING, logMsg, e.getMessage());
        }
        return walkStream;
    }

    private static void addClassLoader(List<Class<?>> classList, Path path) {
        boolean directory = Files.isDirectory(path);
        if (directory) {
            return;
        }

        String pathString = path.toString();

        pathString = pathString.replace(ContainerScannerFactoryConstant.SEPARATOR, ContainerScannerFactoryConstant.DOT);

        String[] allPath = pathString.split(ContainerScannerFactoryConstant.COMPILE_OUTPUT_DIRECTORY);
        String classPath = allPath[1];
        classPath = classPath.replace(ContainerScannerFactoryConstant.COMPILE_FILE_SUFFIX, ContainerScannerFactoryConstant.EMPTY);
        try {
            Class<?> classLoader = ClassLoader.getSystemClassLoader().loadClass(classPath);
            classList.add(classLoader);
        } catch (ClassNotFoundException e) {
            String logMsg = "addClassLoader Error";
            logger.log(Level.WARNING, logMsg, e.getMessage());
        }
    }

    private static URL fileToUrl(File file) {
        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            String logMsg = "fileToUrl Error";
            logger.log(Level.WARNING, logMsg, e);
        }
        return url;
    }
}
