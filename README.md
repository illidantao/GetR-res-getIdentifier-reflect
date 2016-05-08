# GetR-res-getIdentifier-reflect
get Android R res value through getIdentifier() and java reflect

�Բۣ�֮ǰд��һƪ���Ͳ����ˣ����ֵ�����д��shit��????��!

![image](http://img4.imgtn.bdimg.com/it/u=2340496989,162859093&fm=21&gp=0.jpg)

��Щ���������޷���java������ʹ��R.drawable.icon��������Դid��������ĳЩ�����ܻ�����Ϸ����С�

��˾������Ϸ��ҵ����������һ��sdk����Ϸƽ̨���룬Ȼ�󱻸�֪�����ǲ��������ǵ�aar��ֻ��ʹ��jar��res�ļ����롣������jar�е���Դ�����������⡣��Ϊ��Ϸƽ̨���������ڿ����Ĺ����У�û��R�ļ��ĸ��������Դ����Asset�С�

������˵�������±���Ŀ���ص㣨ֱ�Ӵ����￪ʼ�ɣ���

### 1. android �������ṩ��һ��������Դ�ķ���

 ����ֻ��Ҫ��res�����ƺ����ʹ��ݵ�����Resources.getIdentifier(String name, String defType, String defPackage) ���ܵõ�����Դ��Ӧ����id����������ķ������͵�Ȼ��int
��


```
private static int getIdentifier(String name, String defType) {
        return app.getResources().getIdentifier(name, defType, pkgName);
    }
```


++���ܣ���++
Ȼ������������пӵģ�style��styleable �����ĸ������͵���Դ���޷�ͨ�������취��getIdentifier����ȡ��res�ģ����õ�����Զ��0��

### 2. �Ǿ�����java�ķ���ɣ��ײ��ǿ��Ե�


```
/**
     *
     * ����context.getResources().getIdentifier�޷���ȡ������,����������Դ����ֵ
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
            Log.w("epaysdk", "�Ҳ���ϵͳR�ļ�������R�ļ�·��, error R File:" + RName);
        } catch (Exception e) {

        }
        return null;
    }
```
name����Դ�����ƣ�type�����ͣ��������;���style����styleable�ˣ��������������÷���1��ע��RName�ĸ�ֵ���ǡ�com.xxx.xxxx.R��������R full Class Name��


��Ҫ��Ϊ���������´��ˣ��ڽ��뷽��������������TM�ĳ�����crash�ˣ���Դ���Ҳ�����


![image](http://img5.imgtn.bdimg.com/it/u=3438884000,1950465061&fm=21&gp=0.jpg)


��release��R�ļ��ڲ�������layout ��id ��style֮�������com.xxx.xxxx.R$layout��com.xxx.xxxx.R$id�� com.xxx.xxxx.R$style ��������ʽ�����ڴ����ļ��У����취2�ķ����Ƿ��䵽R���ڲ�ȥ�����·��䲻����Դ��Ȼ����debug run��ʱ���ֿ��Է��䵽��

�����ּ����� release���ģʽ����$��R��Դ����


As��

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
���RClassName���ǡ�com.xxx.xxxx.R$style�� 
���������ҵ���$�����Դ��

##### ע�⣺

��������һ�£�����java�����ʱ����Ҫ��д��ȷ��R�ļ�ȫ·����R�ļ��İ���������app�İ�����applicationId�����Լ�ȥ���~~


detail see this file in project:com.dhunter.getr.r.R.java



