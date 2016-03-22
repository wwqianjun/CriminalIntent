package qianjun.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by john on 2016/2/17.
 */
public class Crime {

    private UUID mId;
    private String mTitle;
    /**crime发生时间**/
    private Date mCreateDate;
    /**问题是否得到解决**/
    private boolean mSovled;

    public Crime() {
        mId = UUID.randomUUID();
        // TODO 将来修正，目前用当前日期代替
        mCreateDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(Date createDate) {
        mCreateDate = createDate;
    }

    public boolean isSovled() {
        return mSovled;
    }

    public void setSovled(boolean sovled) {
        mSovled = sovled;
    }

    @Override
    public String toString() {
        return  mTitle;
    }
}
