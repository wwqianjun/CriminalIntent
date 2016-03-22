package qianjun.android.criminalintent;

import android.support.v4.app.Fragment;

import java.util.UUID;

@Deprecated
public class CrimeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        // 为了CrimeListFragment能够被任意Activity使用，将Intent参数解耦,不直接new CrimeListFragment()
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

/***
 * 代码抽象出去SingleFragmentActivity
 * @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        // CrimeActivity托管CrimeFragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if ( fragment == null ){
            fragment = new CrimeFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }*/
}
