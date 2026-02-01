
# Container

容器

[English](./README.md)

支持功能:
- [x] 加载Java.ClassLoading文件
- [x] Class注册
- [x] Class写入
- [x] Class自定义扫描
- [x] Class自定义注册
- [x] 自定义初始化class(已成功注册的class)

# 加载Java.ClassLoading文件:
加载Java.ClassLoading文件,位于resources目录中
支持的数据类型:byte,short,int,long,float,double,boolean,char,String

# Class注册:
将Class注册到容器中

# Class写入:
使用已成功注册的Class

# Class自定义扫描:
扫描指定范围

# Class自定义注册:
在指定范围内注册

# 自定义初始化已注册的Class:
自定义初始化已注册的Class

## Quick Start

```xml
<!--Adding dependencies to pom. XML-->
        <dependency>
            <groupId>io.github.thierrysquirrel</groupId>
            <artifactId>container</artifactId>
            <version>1.0.0.1-RELEASE</version>
        </dependency>
```
# 加载Java.ClassLoading文件:

 ```properties
 ## Java.ClassLoading

####ClassName
Class.forName=io.demo.HelloLoading
####Method
Method.setNameAndAge.String.int=HelloWorld#123
Method.setAge.int=456

Class.forName=io.demo.WorldLoading
Method.setNameAndAge.String.int=World#123
Method.setAge.int=456

 ```

# 加载Java.ClassLoading文件:
# Class自定义扫描:
# Class自定义注册:
```java
package io.demo;
@ScannerPackage(packageName = "io.demo")
public class HelloLoading implements InterfaceManualRegistration {
    private  String name;
    private  int age;

    public void setNameAndAge(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "HelloLoading{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public void scannerAll(List<Class<?>> scannerClassList, Map<Class<?>, Object> registrationMap) {
        
    }
}
```

# Class注册:
# Class写入:

```java
package io.demo;
@Registration
public class Hello {

    @Set
    private HelloLoading helloLoading;

    public void helloWorld() {
        System.out.println("Hello");
        System.out.println(helloLoading.toString());
    }

    @Override
    public String toString() {
        return "Hello{}";
    }
}
```

# Class注册:
# Class写入:
# Class自定义注册:
# 自定义初始化已注册的Class:
```java
package io.demo;
@Registration
public class World implements ManualRegistration, RegistrationInit {

    @Set
    private Hello hello;
    @Set
    private HelloLoading helloLoading;

    public void helloWorld() {
        System.out.println("World");
        hello.helloWorld();
    }
    @Override
    public String toString() {
        return "World{}";
    }

    @Override
    public Object manualRegistration(ManualRegistration thisClassObject) {
        if (thisClassObject instanceof World) {
            World world = (World) thisClassObject;
            // thisClassObject = new World
        }
        return thisClassObject;
    }

    @Override
    public void init(Map<Class<?>, Object> registrationMap) {
        System.out.println("init");
        System.out.println("World::"+helloLoading.toString());
    }
}
```

# Class自定义扫描:
# Class自定义注册:
# 自定义初始化已注册的Class:

```java
package io.demo.manual;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Demo {
}
```

```java
package io.demo.manual;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DemoSelect {
    String value();
}
```

```java
package io.demo.manual;

@Demo
public interface DemoSelectInterface {
    @DemoSelect("select * form demo")
    String selectAll();
}
```

```java
package io.demo.manual;

public class DemoAgent implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before ");
        DemoSelect demoSelect = method.getAnnotation(DemoSelect.class);
        if(!Objects.isNull(demoSelect)){
            System.out.println(demoSelect.value());
        }
//        Object invokeValue = method.invoke(proxy, args);
        System.out.println("After ");
        return null;
    }
}
```

```java
package io.demo.manual;

@ScannerPackage(packageName = "io.demo.manual")
public class DemoInterfaceManualRegistration implements InterfaceManualRegistration {
    @Override
    public void scannerAll(List<Class<?>> list, Map<Class<?>, Object> map) {
        System.out.println("scannerAll");
        for (Class<?> type : list) {
            Demo annotation = type.getAnnotation(Demo.class);
            if(!Objects.isNull(annotation)){
                Object object = Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new DemoAgent());
                map.put(type,object);

            }
        }
    }
}
```

```java
package io.demo;

public class Main {
    public static void main(String[] args) {

        ContainerScanner scanner=new ContainerScanner();
        Map<Class<?>, Object> registrationMap=scanner.scannerAll(Main.class);

        Hello hello = (Hello) registrationMap.get(Hello.class);
        hello.helloWorld();
        System.out.println("==============");

        World world = (World) registrationMap.get(World.class);
        world.helloWorld();
        System.out.println("==============");

        DemoSelectInterface demoSelectInterface = (DemoSelectInterface) registrationMap.get(DemoSelectInterface.class);
        demoSelectInterface.selectAll();
        System.out.println("==============");
    }
}
```