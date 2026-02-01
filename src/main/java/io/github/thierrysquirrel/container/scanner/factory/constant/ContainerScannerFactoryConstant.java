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

package io.github.thierrysquirrel.container.scanner.factory.constant;

import java.io.File;

/**
 * Classname: ContainerScannerFactoryConstant
 * Description:
 * Date:2025/1/21
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ContainerScannerFactoryConstant {
    private ContainerScannerFactoryConstant() {
    }

    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String SEPARATOR = File.separator;

    public static final String COMPILE_OUTPUT_DIRECTORY = "classes.";
    public static final String COMPILE_FILE_SUFFIX = ".class";
    public static final String EMPTY = "";

    public static final String JAR = ".jar";

    public static final String MAVEN_FILE = "file:";

}
