package qianjun.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by john on 2016/2/17.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {

    /**
     * 动态创建要拖管的Fragment
     * @return
     */
    protected abstract Fragment createFragment();

    /**
     * Activity 托管 Fragment 的统一模板代码
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null){
            fragment = createFragment(); // 模板模式
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
