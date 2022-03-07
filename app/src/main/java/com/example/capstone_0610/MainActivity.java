package com.example.capstone_0610;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
// {"method":"loginAccount","p_id":"test01","p_pw":"pass01"}
public class MainActivity extends AppCompatActivity {

    EditText et_lid,et_lpw;
    String slId,slPw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("기후알림 서비스");

        Button joinBtn = (Button) findViewById(R.id.login_main_sign);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
        et_lid = (EditText) findViewById(R.id.et_lid);
        et_lpw = (EditText) findViewById(R.id.et_lpw);

        slId = et_lid.getText().toString();
        slPw = et_lpw.getText().toString();
    }
    public void bt_Login(View v)
    {
        slId = et_lid.getText().toString();
        slPw = et_lpw.getText().toString();
        String emptyText = "";

        try{
            slId = et_lid.getText().toString();
        } catch (NullPointerException e)
        {
            Log.e("err",e.getMessage());
        }
        //sPw.equals(sPw_chk)
        if(slId.equals(emptyText))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("로그인 실패").setMessage("아이디를 입력해주세요");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(slPw.equals(emptyText))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("로그인 실패").setMessage("비밀번호를 압력해주세요");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        {
            loginDB lDB = new loginDB();
            lDB.execute();

            //Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            //intent.putExtra("id",slId);
            //startActivity(intent);
        }
    }
    public class loginDB extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            //jdata={"method":"loginAccount","p_id":"test01","p_pw":"pass01"}
            // "jdata={'method':'loginAccount','p_id':'test01','p_pw':'pass01'}"
            //String param = "p_id=" + slId + "&p_pw=" + slPw + "";
            //오라클 String param = "jdata={\"method\":\"loginAccount\",\"p_id\":\"" + slId + "\",\"p_pw\":\"" + slPw + "\"}";
            String param = "method=loginAccount&p_id=" + slId + "&p_pw="+ slPw+"";
            Log.e("POST",param);
            try {
                /* 서버연결 */

                String strUrl = "http://web022caps.cafe24.com/Climate/connect.jsp";
                //URL url = new URL(strUrl);

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


                /* 서버에서 응답 */
                Log.e("RECV DATA",data);

                if(data.equals("1"))
                {
                    Log.e("RESULT","성공적으로 처리되었습니다!");
                    //Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("id",slId);
                    startActivity(intent);


                }
                else
                {
                    Log.e("RESULT","에러 발생! ERRCODE = " + data);
                    Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
    public void bt_temp(View v)
    {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        onPause();
    }
}