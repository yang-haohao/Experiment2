package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements View.OnClickListener {

    private int[]data = new int[100];
    int hasData = 0;
    final int PROGRESS_DIALOG = 0x112;
    int progressStatus = 0;
    ProgressDialog pd;
    Handler handler;

    @BindView(R.id.text1)
    TextView text;
    @BindView(R.id.btn6)
    Button btn6;

    @OnClick(R.id.btn6)
    public void onclick(View view){
        text.setText("使用ButterKnife作为监控器");
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Button btn1=(Button)findViewById(R.id.btn1);
        Button btn2=(Button)findViewById(R.id.btn2);
        Button btn4=(Button)findViewById(R.id.btn4);
        Button btn5=(Button)findViewById(R.id.btn5);
        Button btn7=(Button)findViewById(R.id.btn7);
        Button btn8=(Button)findViewById(R.id.btn8);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView text1=(TextView)findViewById(R.id.text1);
                text1.setText("使用匿名内部类作为监听器");
            }
        });
        MyButton listener=new MyButton();
        btn4.setOnClickListener(listener);
        btn5.setOnClickListener(new MyButtonListener("使用外部类作为监听器"));

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Activity1.class);
                startActivity(intent);
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(PROGRESS_DIALOG);
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what==1)
                {
                    pd.setProgress(progressStatus);
                }
            }
        };
    }
    @Override
    public Dialog onCreateDialog(int id,Bundle status){
        System.out.println("-----create-----");
        switch (id){
            case PROGRESS_DIALOG:
                pd=new ProgressDialog(this);
                pd.setMax(100);
                pd.setTitle("任务完成百分比");
                pd.setMessage("耗时任务的完成百分比");
                pd.setCancelable(false);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setIndeterminate(false);
                break;
        }
        return pd;
    }
    @Override
    public void onPrepareDialog(int id,Dialog dialog){
        System.out.println("-----prepare-----");
        super.onPrepareDialog(id, dialog);
        switch (id){
            case PROGRESS_DIALOG:
                pd.incrementProgressBy(-pd.getProgress());
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        while (progressStatus<100)
                        {
                            progressStatus=doWork();
                            handler.sendEmptyMessage(1);
                        }
                        if(progressStatus>=100)
                        {
                            pd.dismiss();
                        }
                    }
                }.start();
                break;
        }
    }

    public int doWork(){
        data[hasData++]=(int)(Math.random()*100);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return hasData;
    }

    public void onClick(View view) {
        TextView text1=(TextView)findViewById(R.id.text1);
        text1.setText("使用Activity作为监听器");
    }
    public void clickHandler(View source){
        TextView text1=(TextView)findViewById(R.id.text1);
        text1.setText("使用绑定到标签方式实现监听");
    }
    class MyButton implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            TextView text1=(TextView)findViewById(R.id.text1);
            text1.setText("使用内部类作为监听器");
        }
    }
    public class MyButtonListener implements View.OnClickListener{
        private String str;
        public MyButtonListener(String str){
            super();
            this.str=str;
        }
        @Override
        public void onClick(View view) {
            TextView text1=(TextView)findViewById(R.id.text1);
            text1.setText(str);
        }
    }
}
