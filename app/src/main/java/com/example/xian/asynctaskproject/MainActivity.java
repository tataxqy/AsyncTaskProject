package com.example.xian.asynctaskproject;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;



/*
 *1.网络上请求数据：申请网络权限 读写SD卡的权限
 * 2.布局
 * 3.下载之前
 * 4.下载中
 * 5.下载后
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private ProgressBar mProgressBar;
    private Button mDownloadButton;
    private TextView mResultTextView;
    String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();

        //初始化UI数据
        setData();
    }

    private void setData()
    {
        mProgressBar.setProgress(0);
        mDownloadButton.setText("开始下载");
        mResultTextView.setText("准备下载");

    }

    private void setListener()
    {
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DownLoadAsyncTask askyncTask=new DownLoadAsyncTask();
               askyncTask.execute("https://www1.szu.edu.cn/szu.asp");


            }


        });
    }
    private void initView()
    {
        mProgressBar=(ProgressBar) findViewById(R.id.progressBar);
        mDownloadButton=(Button)findViewById(R.id.button);
        mResultTextView=(TextView) findViewById(R.id.textview);
    }

    public class  DownLoadAsyncTask extends AsyncTask<String ,Integer,Boolean>{
        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();
            mDownloadButton.setText("下载中");
            mResultTextView.setText("下载中");
            mProgressBar.setProgress(0);
        }
        //在另外一个事件中处理事件

        //String... strings表示可变参数
        @Override
        protected Boolean doInBackground(String... strings) {
            if(strings!=null&& strings.length>0)
            {
                String weburl=strings[0];
                try{
                    URL url=new URL(weburl);
                    URLConnection urlConnection=url.openConnection();
                    InputStream intputstream=urlConnection.getInputStream();
                    //获取总长度
                    int contentlength=urlConnection.getContentLength();


                    //获取下载地址
                    mFilePath= Environment.getExternalStorageDirectory()+ File.separator+"download";

                    //对下载地址进行处理
                    File webFile =new File (mFilePath);
                    if(webFile.exists())
                    {
                        boolean result =webFile.delete();
                        if(!result)
                        {
                            return false;
                        }
                    }

                    //记录已下载的大小
                    int downloadSize=0;
                    byte[] bytes =new byte[1024];
                    int length;
                    OutputStream outputStream=new FileOutputStream(mFilePath);

                    while((length=intputstream.read(bytes))!=-1)
                    {
                        outputStream.write(bytes,0,length);
                        downloadSize+=length;
                        publishProgress(downloadSize*100/contentlength);


                    }


                }catch(MalformedURLException e)
                {
                    e.printStackTrace();
                }catch(IOException e)
                {
                    e.printStackTrace();
                    return false;
                }
            }
            else{
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
           super.onPostExecute(result);
           mDownloadButton.setText("下载完成");
           mResultTextView.setText(result?"下载完成"+mFilePath:"下载失败");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // 收到进度，然后处理： 也是在UI线程中。
            if (values != null && values.length > 0) {
                mProgressBar.setProgress(values[0]);
            }
        }


    }
}
