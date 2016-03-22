package qianjun.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * ViewPager在某种程度上有点类似AdapterView(ListView的超类)。
 * AdapterView需借助于Adapter才能提供视图，同样的ViewPager也需要PagerAdapter的支持
 *
 * ViewPager默认只显示PagerAdapter的第一个列表项
 * ViewPager可以实现左右滑动
 *
 * Created by john on 2016/3/14.
 */
public class CrimePagerActivity extends FragmentActivity {

    public static final String EXTRA_POSITION_SHOW = "qianjun.android.criminalintent.position_show";
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.getInstance(this).getCrimes();

        FragmentManager fm = getSupportFragmentManager();
        //----------------------------------------------------
        // FragmentStatePagerAdapter是我们的代理，负责管理与ViewPager的对话并协同工作。
        // 代理需首先将getItem(int)方法返回的fragment添加给activity，然后才能使用fragment完成自己的工作。
        // 这也是创建代理的时候，需要穿FragmentManager的原因所在
        //----------------------------------------------------
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                setPositionResult(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        // ViewPager默认只显示PagerAdapter的第一个列表项
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);

        for (int i = 0 ; i < mCrimes.size(); i++){
            if (mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        // 标题栏
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Crime crime = mCrimes.get(position);
                if (crime.getTitle() != null){
                    setTitle(crime.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPositionResult(int position ){
        Intent data = new Intent();
        data.putExtra(EXTRA_POSITION_SHOW, position);
        setResult(RESULT_OK, data);
    }

}
