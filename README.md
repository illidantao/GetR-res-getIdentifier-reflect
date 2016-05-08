# GetR-res-getIdentifier-reflect
get Android R res value through getIdentifier() and java reflect

卧槽，之前写了一篇博客不见了，我又得重新写（shit！????）!


![image](https://github.com/illidantao/HelloAndroidM/blob/master/oldDriver.jpg)
快上车，没时间解释了


有些场景我们无法在java代码中使用R.drawable.icon这样的资源id。例如在某些插件框架或者游戏框架中。

我司致力游戏事业，我们做了一个sdk供游戏平台接入，然后被告知，他们不能用我们的aar，只能使用jar和res文件导入。这样，jar中的资源导向会存在问题。因为游戏平台的特殊性在开发的过程中，没有R文件的感念，多数资源均在Asset中。

长话短说，简述下本项目的重点（直接从这里开始吧）：

### 1. android 给我们提供了一个反射资源的方法

 我们只需要将res的名称和类型传递到函数Resources.getIdentifier(String name, String defType, String defPackage) 就能得到该资源对应的型id，这个函数的返回类型当然是int
。


```
private static int getIdentifier(String name, String defType) {
        return app.getResources().getIdentifier(name, defType, pkgName);
    }
```


++高能！！++
然而这个函数是有坑的，style，styleable 这样的复合类型的资源是无法通过上述办法（getIdentifier）获取到res的，你拿到的永远是0。

### 2. 那就利用java的反射吧，亲测是可以的


```
/**
     *
     * 对于context.getResources().getIdentifier无法获取的数据,或者数组资源反射值
     * @param name
     * @param type
     * @return
     *
     */
    public static Object getResourceId(String name, String type) {
        try {
            Class cls = Class.forName(RName);
            for (Class childClass : cls.getClasses()) {
                String simple = childClass.getSimpleName();
                if (simple.equals(type)) {
                    for (Field field : childClass.getFields()) {
                        String fieldName = field.getName();
                        if (fieldName.equals(name)) {
                            return field.get(null);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
            Log.w("epaysdk", "找不到系统R文件，请检查R文件路径, error R File:" + RName);
        } catch (Exception e) {

        }
        return null;
    }
```
name是资源的名称，type是类型，这里类型就是style或者styleable了，其他的类型请用方法1。注意RName的赋值，是“com.xxx.xxxx.R”这样的R full Class Name。


不要以为这样就万事大吉了，在接入方打包混淆编译后又TM的出错了crash了，资源又找不到了



在release后，R文件内部的类如layout 、id 、style之类的是以com.xxx.xxxx.R$layout、com.xxx.xxxx.R$id、 com.xxx.xxxx.R$style 这样的形式存在于代码文件中，而办法2的反射是反射到R的内部去，导致反射不到资源。然而，debug run的时候又可以反射到。

最终又兼容了 release后的模式，带$的R资源反射


As：

```
private static int getStyle_R$xx(String styleableName, String RClassName) {
        try {
            Field[] fields2 = Class.forName(RClassName).getFields();
            for (Field f : fields2) {
                if (f.getName().equals(styleableName)) {
                    return (int) f.get(null);
                }
            }
        } catch (Exception t) {
        }
        return 0;
    }
```
这个RClassName就是“com.xxx.xxxx.R$style” 
这样就能找到被$后的资源了

##### 注意：

这里提醒一下，利用java反射的时候需要填写正确的R文件全路径，R文件的包名区别于app的包名（applicationId），自己去体会~~


detail see this file in project:com.dhunter.getr.r.R.java



