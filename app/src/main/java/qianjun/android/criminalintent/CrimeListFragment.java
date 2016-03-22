package qianjun.android.criminalintent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * ListView组成 内置列表显示功能
 * Created by john on 2016/2/17.
 */
public class CrimeListFragment extends ListFragment {

    private static final int REQUEST_CRIME = 1;

    private static final String TAG = "CrimeListFragment";

    private ArrayList<Crime> mCrimes;

    private static CrimeAdapter adapter ;

    private static final String EXTRA_IS_SHOW_SUB_TITLE = "qianjun.android.criminalintent.is_show_sub_title";
    private boolean mIsShowSubTitle ;

    private ListView mListView;

    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null)
//        mIsShowSubTitle = savedInstanceState.getBoolean(EXTRA_IS_SHOW_SUB_TITLE);

        // ----------------------------------------
        // 当在onCreate()方法中调用了setRetainInstance(true)后，
        // Fragment恢复时会跳过onCreate()和onDestroy()方法，因此不能在onCreate()中放置一些初始化逻辑，切忌！
        // ----------------------------------------
        setRetainInstance(true);
        mIsShowSubTitle = false;
        //-----------------------------------------
        //  onCreateOptionMenu()方法是由FragmentManager负责调用
        //  so 当activity接收到OS的onCreateOptionMenu()方法回调请求时，需告诉FragmentManager：
        //      其管理的fragment应接受onCreateOptionMenu()方法调动指令，现在调用下面方法
        //-----------------------------------------
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crimes_title);

        mCrimes = CrimeLab.getInstance(getActivity()).getCrimes();

//        ArrayAdapter<Crime> adapter =
//                new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1,
//                                        mCrimes);
        adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);


    }
    // 无需覆盖onCreateView(...)方法或为CrimeListFragment生成布局，
    // ListFragment类默认实现方法已经生成了一个全屏的ListView,没有任何内容，以一个圆形进度条展示

    /**
     * ListView 并非是将所有的view列表展示，只是类似分页查询，一次性加载完所有会导致严重的系统性能问题及内存占用问题
     * 如何做到分页查询（申请视图对象），需要adapter
     * adapter负责：
     *  1，创建必要的视图对象；
     *  2，用模型层数据填充视图对象
     *  3，将准备好的视图
     */

    private class CrimeAdapter extends ArrayAdapter<Crime>{

        private int selectedItem = -1;

        public void setSelectedItem(int position) {
            selectedItem = position;
        }

        public CrimeAdapter(ArrayList<Crime> crimes) {
            this(0, crimes);
        }

        public CrimeAdapter(int index,ArrayList<Crime> crimes) {
            super(getActivity(), index ,crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren`t given a view, inflate one
            if (convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime, null);
            }

            Crime c = getItem(position);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            TextView dateTextView =
                    (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getCreateDate().toLocaleString());

            CheckBox solvedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSovled());

            return convertView;
        }
    }

    /**
     *  显示子标题内容
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = super.onCreateView(inflater, container, savedInstanceState);
        View view ;
        if (mCrimes == null || mCrimes.size()==0) {
            view = inflater.inflate(R.layout.list_all_crime, container, false);
            mTextView = (TextView) view.findViewById(android.R.id.empty);
            mTextView.setText(R.string.list_empty);
        }else{
            view = super.onCreateView(inflater, container, savedInstanceState);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if (mIsShowSubTitle){
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }
        return view;
    }
//    @Override
//    public void onViewCreated (View view, Bundle savedInstanceState) {
//
//    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        Crime crime = (Crime)(getListAdapter()).getItem(position);
        Crime crime = ((CrimeAdapter)getListAdapter()).getItem(position);
        Log.d(TAG, crime.getTitle() + " was clicked");
        // Start CrimeActivity；Fragment 获取Context用getActivity，不是this
//        Intent intent = new Intent(getActivity(), CrimeActivity.class);

        Log.d(TAG, crime.getId() + " was clicked");
        // Start CrimePagerActivity；
        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
//        startActivity(intent);
        startActivityForResult(intent, REQUEST_CRIME);
    }

    /**
     * Fragment能从activity中接受返回结果，但其自身无法产生返回结果，只有activity拥有返回结果
     * so: Fragment 有自己的startActivityForResult 和 onActivityResult，但没有setResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CRIME && data != null){
            // Handle result
           final int position =  data.getIntExtra(CrimePagerActivity.EXTRA_POSITION_SHOW, 0);

            getListView().clearFocus();
            getListView().post(new Runnable() {
                @Override
                public void run() {
                    getListView().setSelection(position);
                }
            });
            // ------------------------------------------------------------------------------
            // 如果简单地通过setSelection(position)指定ListView的显示项，无效
            // 由于View还没有创建，因此实际无效。
            // onActivityResult()-->CrimeAdapter.getView()，view尚未创建，
            // ------------------------------------------------------------------------------
//            getListView().setSelection(position);
//            ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
        }
    }
    /**
     * CrimeListActivity 恢复运行状态后,操作系统会像它发出调用onResume()生命周期方法的指令。
     * CrimeListActivity 接到指令后，它的FragmentManager会调用当前被activity托管的fragment的onResume()方法
     *
     * 覆盖onResume()方法 《立即》刷新 显示列表项，不能使用onStart():
     *     前面的activity是透明的，我们的activity可能只会被暂停，onStart()方法更新代码是起不到作用的
     * 一般来说：要保证fragment视图得到刷新，在onResume()方法内更新代码是最安全的选择
     */
    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    /**
     *  菜单栏的展示
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_lsit, menu);

        MenuItem showSubTitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mIsShowSubTitle && showSubTitle != null){
            showSubTitle.setTitle(R.string.hide_subtitle);
        }
    }

    /**
     *  点击菜单栏中菜单项时
     * @param item
     * @return
     */
    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime :{
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);

                Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(intent, 0);
                return true;
            }
            case R.id.menu_item_show_subtitle : {
                if (getActivity().getActionBar().getSubtitle() == null) { // 点击show_subtitle
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    mIsShowSubTitle = true;
                } else { // 点击hide_subtitle
                    getActivity().getActionBar().setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    mIsShowSubTitle = false;
                }
                return true;
            }
             default:{
                 return super.onOptionsItemSelected(item);
             }
        }// End switch
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putBoolean(EXTRA_IS_SHOW_SUB_TITLE, mIsShowSubTitle);
        super.onSaveInstanceState(outState);
    }
}

