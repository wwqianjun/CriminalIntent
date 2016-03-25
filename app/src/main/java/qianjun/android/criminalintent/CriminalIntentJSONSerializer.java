package qianjun.android.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建和解析由Crime转化的JSON数据(对象的序列化), 处理当前的Crime类
 * Created by john on 2016/3/21.
 */
public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFileName;

    public CriminalIntentJSONSerializer(Context context, String fileName) {
        mContext = context;
        mFileName = fileName;
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<>();
        BufferedReader reader = null;
        try{
            // open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ( (line = reader.readLine()) != null ){
                // Line break are omitted and irrelevant
                jsonString.append(line);
            }
            // Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            // Build the array of crimes from JSONTObjects
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e){
            // Ignore this one ; it happens when starting fresh
        } finally {
            if ( reader != null ){
                reader.close();
            }
        }
        return crimes;
    }

    public void saveCrimes(List<Crime> crimes) throws IOException, JSONException {
        // Build an array in JSON
        JSONArray array = new JSONArray();
        for (Crime c: crimes) {
            array.put(c.toJSON());
        }

        // Write the file to disk
        Writer writer = null ;
        try {
            // --------------------------------------------------------
            // 1,openFileOutput会自动将传入的文件名附加到应用沙盒文件目录路径之后，形成一个新路径
            // 然后再新路径下创建并打开文件，等待数据写入
            // 2,如选择手动获取私有文件目录并在其下创建和打开文件，记得总是使用Context.getFileDir()替代方法。
            // 3,如需创建不同使用权限的文件，还是少不了要使用openFileOutput()
            // --------------------------------------------------------

            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null){
                writer.close();
            }
        }
    }
}
