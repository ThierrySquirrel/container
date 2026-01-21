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

package io.github.thierrysquirrel.container.load.check;

import io.github.thierrysquirrel.container.load.check.constant.ClassLoadingCheckConstant;

/**
 * Classname: ClassLoadingCheck
 * Description:
 * Date:2025/1/21
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ClassLoadingCheck {

    private ClassLoadingCheck() {
    }

    public static boolean checkEqualSign(String value) {
        int indexRead = value.indexOf(ClassLoadingCheckConstant.EQUAL_SIGN);
        return indexRead != -1;
    }

    public static boolean checkClassForName(String dateKey) {
        return dateKey.equals(ClassLoadingCheckConstant.CLASS_FOR_NAME);

    }

    public static boolean checkMethod(String dateKey) {
        int indexRead = dateKey.indexOf(ClassLoadingCheckConstant.METHOD);
        return indexRead != -1;
    }

}
