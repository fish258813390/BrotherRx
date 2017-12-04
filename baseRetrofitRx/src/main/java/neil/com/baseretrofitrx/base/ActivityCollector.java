package neil.com.baseretrofitrx.base;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by wy on 2016/5/19.
 */
public class ActivityCollector {
//    public static List<Activity> activityStack = new ArrayList<Activity>();

    public static Stack<Activity> activities = new Stack<>(); // 栈管理

    private Stack<Activity> deleteStack = new Stack<>();

    private static ActivityCollector instance;

    private ActivityCollector() {
    }

    public static synchronized ActivityCollector getInstance() {
        if (instance == null) {
            instance = new ActivityCollector();
        }
        return instance;
    }

    public Stack<Activity> getActivityStack() {
        return activities;
    }


    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void finishActivity(Class tClass) {
        for (Activity activity : activities) {
            if (activity.getClass().equals(tClass)) {
                activity.finish();
            }
        }
    }


    /**
     * 指定 activity 出栈
     * 一般在baseActivity的onDestroy里面加入
     *
     * @param ac
     */
    public void popActivity(Activity ac) {
        if (ac != null) {
            activities.remove(ac);
        }
        if (!ac.isFinishing()) {
            ac.finish();
            ac = null;
        }
    }

    /**
     * 指定 activity 出栈
     * 一般在baseActivity的onDestroy里面加入
     *
     * @param cls
     */
    public void popActivity(Class<?> cls) {
        Activity deleteActivity = null;
        if (activities == null) {
            activities = new Stack<>();
        }
        for (Activity activity : activities) {
            if (activity.getClass().equals(cls) && !activity.isFinishing()) {
                deleteActivity = activity;
                activity.finish();
            }
        }
        activities.remove(deleteActivity);
    }

    /**
     * 从栈顶往下移除 直到cls这个activity为止
     * 如： 现有ABCD popAllActivityUntilTargetOne(B.class)
     * 则： 还有AB存在
     * <p>
     * 注意此方法 会把自身也finish掉
     *
     * @param cls
     */
    public void popAllActivityUntilTargetOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }


    /**
     * 返回栈顶的activity
     *
     * @return
     */
    public Activity currentActivity() {
        if (activities.size() == 0) {
            return null;
        }
        Activity activity = activities.lastElement();
        return activity;
    }

}
