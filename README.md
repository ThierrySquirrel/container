
# Container

Container

[中文](./README_zh_CN.md)

Support function:
- [x] Load Java.ClassLoading file
- [x] Class registration
- [x] Class writing
- [x] Class Custom Scan
- [x] Class Custom Registration
- [x] Customize initialization class (successfully registered class)

# Load Java.ClassLoading file:
Load Java.ClassLoading file, located in the resources directory
Supported data types:byte,short,int,long,float,double,boolean,char,String

# Class registration:
Register the Class into the container

# Class writing:
Use a successfully registered class

# Class Custom Scan:
Scan the specified range

# Class Custom Registration:
Register within the designated scope

# Customize initialization class (successfully registered class):
Customize initialization class (successfully registered class)

## Quick Start

```xml
<!--Adding dependencies to pom. XML-->
        <dependency>
            <groupId>io.github.thierrysquirrel</groupId>
            <artifactId>container</artifactId>
            <version>1.0.0.1-RELEASE</version>
        </dependency>
```
# Load Java.ClassLoading file:

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

# Load Java.ClassLoading file:
# Class Custom Scan:
# Class Custom Registration:
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

# Class registration:
# Class writing:

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

# Class registration:
# Class writing:
# Class Custom Registration:
# Customize initialization class (successfully registered class):
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

# Class Custom Scan:
# Class Custom Registration:
# Customize initialization class (successfully registered class):

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