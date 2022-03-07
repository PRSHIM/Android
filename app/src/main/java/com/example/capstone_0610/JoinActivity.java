package com.example.capstone_0610;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {
    EditText et_id,et_pw,et_pw_chk,et_City,et_Age,et_spe;
    //TextView tv_spe;
    String sId,sPw,sPw_chk,sAge,sCity,sSpe;
    Button Cancel;
    String spnSpe;
    Integer snSpe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        setTitle("기후알림 서비스");

        Cancel = (Button) findViewById(R.id.bt_Join_Cancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainActivity();
            }
        });


        et_id = (EditText) findViewById(R.id.et_Id);
        et_pw = (EditText) findViewById(R.id.et_Password);
        et_pw_chk = (EditText)findViewById(R.id.et_Password_chk);
        et_City = (EditText)findViewById(R.id.et_City);
        et_Age = (EditText)findViewById(R.id.et_Age);
        et_spe = (EditText) findViewById(R.id.et_spe);

        sId = et_id.getText().toString().trim();
        sPw = et_pw.getText().toString().trim();
        sPw_chk = et_pw_chk.getText().toString().trim();
        sCity = et_City.getText().toString().trim();
        sAge = et_Age.getText().toString();
        sSpe = et_spe.getText().toString();

        final RadioGroup rg01 = (RadioGroup)findViewById(R.id.rg01);
        rg01.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group,int checkedId)
            {
                RadioButton rb = (RadioButton)findViewById(checkedId);
                EditText et_spe = (EditText) findViewById(R.id.et_spe);

                if(checkedId==R.id.spe0)
                {
                    et_spe.setText(rb.getTag().toString());
                }
                else  if(checkedId == R.id.spe1)
                {
                    et_spe.setText(rb.getTag().toString());
                }
                else  if(checkedId == R.id.spe2)
                {
                    et_spe.setText(rb.getTag().toString());
                }
            }
        });


    }

    public void bt_Join(View view)
    {
        sId = et_id.getText().toString().trim();
        sPw = et_pw.getText().toString().trim();
        sPw_chk = et_pw_chk.getText().toString().trim();
        sCity = et_City.getText().toString().trim();
        sAge = et_Age.getText().toString();
        sSpe = et_spe.getText().toString();
        /*
        if(sSpe == "없음")
        {
            spnSpe = "0";
        }
        else if(sSpe == "농업")
        {
            spnSpe = "1";
        }
        else if(sSpe == "실외 작업")
        {
            spnSpe = "2";
        }
        */
        //라디오 버튼에서 String 값 받기

        if(sPw.equals(sPw_chk))
        {
            registDB rdb = new registDB();
            rdb.execute();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("가입 완료").setMessage("로그인 해주세요");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("가입 실패").setMessage("비밀번호를 확인해주세요");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    public class registDB extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            //String param = "u_id=" + sId + "&u_pw=" + sPw + "&u_city" + sCity + "&u_age" + sAge + "&u_spe" + sSpe + "";
           //Mysql String param = "jdata={\"method\":\"newAccount\",\"p_id\":\"" + sId + "\",\"p_pw\":\"" + sPw + "\",\"p_age\":"+ sAge +",\"p_city\":\"" + sCity +"\",\"p_spe\":" + snSpe + "}";
            String param = "method=newAccount&p_id="+ sId +"&p_pw="+ sPw +"&p_age="+ sAge +"&p_city="+ sCity +"&p_spe="+sSpe+"";
            Log.e("POST", param);
            try {
                /* 서버연결 */
                String strUrl = "http://web022caps.cafe24.com/Climate/connect.jsp";
                Log.e("strUrl",strUrl);
                URL url = new URL(strUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is      = null;
                BufferedReader in   = null;
                String data         = "";

                is  = conn.getInputStream();
                in  = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff   = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data    = buff.toString().trim();
                Log.e("RECV DATA",data);
                if(data.equals("insert success"))
                {
                    Log.e("Result","성공적으로 처리되었습니다");
                }
                else
                {
                    Log.e("RESULT","에러 발생 ERRCODE =" + data);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
    public void goMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

