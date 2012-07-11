package com.ca.logworkhour;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        
        TextView t = (TextView)findViewById(R.id.tv_bkName);
        t.setText("the fetched backlog name.");
        
        Button bt = (Button)findViewById(R.id.button1);
        bt.setOnClickListener(new View.OnClickListener() 
        { 
            public void onClick(View v) 
            { 
            	new LongRunningGetIO().execute();
            	 
            } 
        }); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void logHour(View view) {
        // Do something in response to button click
    	System.out.println("loghour");
    	int hour = 0;
    	EditText et = (EditText)findViewById(R.id.ET_hours);
    	String hourString = et.getText().toString();
    	if (hourString.length() > 0)
    		hour = Integer.parseInt(hourString);
    	System.out.println("the hour logged is " + hour);
    }
    
    public void getREST(){
    	HttpClient hc = new DefaultHttpClient();
    	HttpContext localContext = new BasicHttpContext();
    	HttpGet httpGet = new HttpGet("");
    	
    }
    
    private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
    	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
    	InputStream in = entity.getContent();
    	StringBuffer out = new StringBuffer();
    	int n = 1;
    	byte[] b = new byte[2048];
    	while (n>0) {
    	n =  in.read(b);
    	if (n>0) out.append(new String(b, 0, n));
    	}
    	return out.toString();
    	}
    	@Override
    	protected String doInBackground(Void... params) {
    	HttpClient httpClient = new DefaultHttpClient();
    	HttpContext localContext = new BasicHttpContext();
    	HttpGet httpGet = new HttpGet("http://www.cheesejedi.com/rest_services/get_big_cheese.php?puzzle=1");
    	String text = null;
    	try {
    	HttpResponse response = httpClient.execute(httpGet, localContext);
    	HttpEntity entity = response.getEntity();
    	System.out.println(entity.toString());
    	text = getASCIIContentFromEntity(entity);
    	System.out.println(text.toString());
    	} catch (Exception e) {
    	return e.getLocalizedMessage();
    	}
    	return text;
    	}
    	protected void onPostExecute(String results) {
    	if (results!=null) {
    	TextView et = (TextView)findViewById(R.id.tv_bkName);
    	System.out.println(results);
    	et.setText(results);
    	}
    	Button b = (Button)findViewById(R.id.button1);
    	b.setClickable(true);
    	}
    	}
}
