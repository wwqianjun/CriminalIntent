package qianjun.android.criminalintent;

import android.content.Context;

import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

    public void saveCrimes(List<Crime> crimes) throws IOException {
        // Build an array in JSON
        JSONArray array = new JSONArray();
        for (Crime c: crimes) {
//            array.put(c.toJSON());
        }

        // Write the file to disk
        Writer writer = null ;
        try {
            // --------------------------------------------------------
            // 1,openFileOutput会自动将传入的文件名附加到应用沙盒文件目录路径之后，形成一个新路径
            // 然后再新路径下创建并打开文件，等待数据写入
            // 2,如选择手动获取私有文件目录并在其下
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
