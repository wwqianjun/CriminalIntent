package qianjun.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 数据集中存储池,用来存储Crime对象
 * Created by john on 2016/2/17.
 */
public class CrimeLab {
    private ArrayList<Crime> mCrimes;

    private static CrimeLab sCrimeLab ;
    /**
     * 使用Context参数，单例可完成启动Activity、获取项目资源、查找应用的私有存储空间等任务
     */
    private Context mAppContext;

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null){
            // 只要是应用层面的单例，就一定要使用application context
            // Context除了是Activity也可能是Service，getApplicationContext()可以转化成application context
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        }
        return sCrimeLab;
    }

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mCrimes = new ArrayList<>();

        initData(mCrimes);
        // TODO 目前是手动创建
//        for (int i = 0; i < 100; i++) {
//            Crime c = new Crime();
//            c.setTitle("Crime #" + i);
//            c.setSovled(i % 3 == 0);
//            mCrimes.add(c);
//        }
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID uuid) {
        for (Crime c: mCrimes) {
            if (c.getId().equals(uuid)){
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    private void initData(List list){
//        Crime crime1 = new Crime();
//        crime1.setTitle("CriminalIntent");
//        crime1.setSovled(true);
//        list.add(crime1);
//
//        Crime crime2 = new Crime();
//        crime2.setTitle("潘小美，啦啦啦");
//        list.add(crime2);
//
//        Crime crime3 = new Crime();
//        crime3.setTitle("Android编程");
//        crime3.setSovled(true);
//        list.add(crime3);
    }
}
