package com.dhunter.getr.r;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author illidantao
 * @date 2016/5/8 19:19
 */
public class R {

    private static Context app;

    private static String RName;
    //打包release后的styleable文件 xxx.xxx.R$styleable
    private static String RNameStyleable;
    //打包release后的styleable文件 xxx.xxx.R$style
    private static String RNameStyle;
    private static String pkgName;

    /**
     * Must init in Application or other place before activity starting
     * @param context
     */
    public static void init(Context context) {
        if (app == null && context != null) {
            app = context.getApplicationContext();
            pkgName = context.getPackageName();
        }
        pkgName = app.getPackageName();
        if (RName == null || "".equals(RName)) {
            setRjavaPkgName(pkgName);
        }else{
            checkRNameIsOK();
        }
    }

    /**
     * Must set in Application or other place before activity starting
     */
    public static void setRjavaPkgName(String RfilePath) {
        RName = RfilePath + ".R";
        RNameStyleable = RName + "$styleable";
        RNameStyle = RName + "$style";
        checkRNameIsOK();
    }

    private static void checkRNameIsOK() {
        if (RName != null && !"".equals(RName)) {
            try {
                Class.forName(RName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                // 找不到R文件
                if (pkgName != null && !RName.startsWith(pkgName)) {
                    RName = pkgName + ".R";
                    RNameStyleable = RName + "$styleable";
                    RNameStyle = RName + "$style";
                }
            }
        }
    }

    public static int getIdentifier(String name, String defType) {
        return app.getResources().getIdentifier(name, defType, pkgName);
    }

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

    private static int[] getStyleableArraysR$xx(String styleableName) {
        try {
            Field[] fields2 = Class.forName(RNameStyleable).getFields();
            for (Field f : fields2) {
                if (f.getName().equals(styleableName)) {
                    // return as int array
                    int[] ret = (int[]) f.get(null);
                    return ret;
                }
            }
        } catch (Exception t) {
        }
        return null;
    }

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

    public static int getStyle(String name) {
        int val = 0;
        try {
            val = getStyle_R$xx(name, RNameStyle);
        } catch (Exception e) {

        }
        if (val == 0) {
            val = ((Integer) getResourceId(name, "style")).intValue();
        }
        return val;
    }

    public static int getStyleable(String name) {
        int val = 0;
        try {
            val = getStyle_R$xx(name, RNameStyleable);
        } catch (Exception e) {

        }
        if (val == 0) {
            val = ((Integer) getResourceId(name, "styleable")).intValue();
        }
        return val;
    }

    public static int[] getStyleableArray(String name) {
        int[] val;
        try {
            val = getStyleableArraysR$xx(name);
        } catch (Exception e) {
            return new int[0];
        }
        if (val == null || val.length == 0) {
            val = (int[]) getResourceId(name, "styleable");
        }
        return val;
    }

    public static final class layout{
        public static final int activity_main = getIdentifier("activity_main", "layout");
    }

    public static final class attr {
        public static final int barColor = getIdentifier("epaysdk_barColor", "attr");
        public static final int fadeDelay = getIdentifier("epaysdk_fadeDelay", "attr");
    }

    public static final class color {
        public static final int common_bg = getIdentifier("common_bg", "color");
        public static final int divier_color = getIdentifier("divier_color", "color");
    }

    public static final class drawable {
        public static final int bg_black_dialog = getIdentifier("epaysdk_bg_black_dialog", "drawable");
        public static final int bg_checked = getIdentifier("epaysdk_bg_checked", "drawable");
    }

    public static final class id {
        public static final int IDCard = getIdentifier("tvCard", "id");
        public static final int btnTitleBack = getIdentifier("btnTitleBack", "id");
    }

    public static final class styleable {
        public static final int[] AdjustTextSizeTextView = getStyleableArray("AdjustTextSizeTextView");
        public static final int AdjustTextSizeTextView_epaysdk_maxTextSize = getStyleable(
                "AdjustTextSizeTextView_epaysdk_maxTextSize");
    }

    public static final class style {
        public static final int AppBaseTheme = getStyle("AppBaseTheme");
        public static final int GridPasswordView = getStyle("GridPasswordView");
    }

}
