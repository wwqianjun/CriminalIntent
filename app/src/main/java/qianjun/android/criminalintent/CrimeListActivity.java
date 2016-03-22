package qianjun.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by john on 2016/2/17.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
