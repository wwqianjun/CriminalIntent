package qianjun.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by john on 2016/2/17.
 */
public class Crime {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";

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

    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE)){
            mTitle = json.getString(JSON_TITLE);
        }
        mSovled = json.getBoolean(JSON_SOLVED);
        mCreateDate = new Date(json.getString(JSON_DATE));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSovled);
        json.put(JSON_DATE, mCreateDate);
        return json;
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
