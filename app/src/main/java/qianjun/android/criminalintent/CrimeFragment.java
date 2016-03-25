package qianjun.android.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 *  模型及视图对象交互的控制器，用于显示特定crime的明细信息，并在用户修改这些信息后立即进行内容更新
 * Created by john on 2016/2/17.
 */
public class CrimeFragment  extends Fragment{
    public static final String EXTRA_CRIME_ID = "qianjun.android.criminalintent.crime_id";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mCreateDateButton;
    private CheckBox mSolvedCheckBox;


    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    /**
     * 完成fragment实例及bundle对象的创建，然后将argument放入bundle中，最后附加给fragment
     * @param crimeId
     * @return
     */
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        // 必须在fragment创建后、添加给activity前完成
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        mCrime = new Crime();
//        UUID  crimeId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }

    /**
     * 创建和配置fragment视图，Activity的视图是在onCreate()中创建和配置的
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @TargetApi(11)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        // 启用向上导航按钮,仅仅将应用图标变成按钮，出现向左的图标而已，点击没用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if (NavUtils.getParentActivityName(getActivity()) != null) { // 没有父activity，不用显示
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCreateDateButton = (Button) view.findViewById(R.id.crime_date);
//        mCreateDateButton.setText(mCrime.getCreateDate().toLocaleString());
        updateDate();
//        mCreateDateButton.setEnabled(false); // 禁用按钮
        mCreateDateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
//                DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getCreateDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });


        // 处理CheckBox组件，更新Crime的mSolved变量值
        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSovled());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSovled(isChecked);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK ) {
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setCreateDate(date);
            updateDate();
//            mCreateDateButton.setText(mCrime.getCreateDate().toString());
        }
    }

    /**
     * 使用NavUtils类要好于手动启动activity
     *  1，NavUtils类实现代码既简洁又优雅
     *  2，NavUtils类也可以实现在manifest配置文件中统一管理activity间关系
     *  3，NavUtils类还可以保持层级关系处理与fragment的代码分离
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :{
                if (NavUtils.getParentActivityName(getActivity()) != null){
                    // 导航至父activity界面
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).saveCrimes();
    }

    private void updateDate(){
        mCreateDateButton.setText(mCrime.getCreateDate().toLocaleString());
    }
    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK, null);
    }
}
