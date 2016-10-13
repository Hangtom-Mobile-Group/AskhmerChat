package com.askhmer.chat.util;

import android.content.Context;
import android.os.AsyncTask;

import com.askhmer.chat.listener.SendAudioListener;

import java.io.File;
import java.util.List;

public class SaveUserAsyntaskAudio extends AsyncTask<String, Integer, String> {

    private SendAudioListener sendAudioListener;

    public SaveUserAsyntaskAudio(Context context){
       this.sendAudioListener= (SendAudioListener) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... pathFile) {
        String path = pathFile[0];
        String result="";
        File file;
        String url = "http://chat.askhmer.com/api/uploadfile/upload";
        try {
            MultipartUtilityAudio multipart = new MultipartUtilityAudio(url,"UTF-8","POST","Basic YWRtaW46MTIz");
            file = new File(path);
            multipart.addFilePart("file",file);
            multipart.addFormField("folder","audio");

            List<String> response = multipart.finish();
            for (String line : response) {
                if (line != null) {
                    result+=line;
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
//        Log.e("onGetResponse", result.toString());
        sendAudioListener.sendAudio(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }
}
