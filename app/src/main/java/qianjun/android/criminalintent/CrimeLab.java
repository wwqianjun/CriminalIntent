package qianjun.android.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 数据集中存储池,用来存储Crime对象
 * Created by john on 2016/2/17.
 */
public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILE_NAME = "crimes.json";

    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;

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
//        mCrimes = new ArrayList<>();
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILE_NAME);

        try {
            mCrimes = mSerializer.loadCrimes();
        } catch (Exception e){
            mCrimes = new ArrayList<>();
            Log.e(TAG, "Error loading crimes：", e);
        }
//        initData(mCrimes);

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

    public void deleteCrime(Crime c) {
        mCrimes.remove(c);
    }
    public boolean saveCrimes(){
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e){
            Log.e(TAG, "Error saving crimes：", e);
            return false;
        }
    }

    private void initData(List list){
        Crime crime1 = new Crime();
        crime1.setTitle("CriminalIntent");
        crime1.setSovled(true);
        list.add(crime1);

        Crime crime2 = new Crime();
        crime2.setTitle("潘小美，啦啦啦");
        list.add(crime2);

        Crime crime3 = new Crime();
        crime3.setTitle("Android编程");
        crime3.setSovled(true);
        list.add(crime3);
    }
}
