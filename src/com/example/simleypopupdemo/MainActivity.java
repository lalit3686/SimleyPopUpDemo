package com.example.simleypopupdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class MainActivity extends Activity {

	private HashMap<String, Integer> emoticons = new HashMap<String, Integer>();
	private ArrayList<String> arrayList = new ArrayList<String>();
	
	private void fillArrayList() {
		Iterator<Entry<String, Integer>> iterator = emoticons.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Integer> entry = iterator.next();
			arrayList.add(entry.getKey());
		}
	}
	
	PopupWindow popupWindow;
	EditText editText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		emoticons.put(":)", R.drawable.ic_launcher);
		emoticons.put(":P", R.drawable.smiley);
		emoticons.put(":D", R.drawable.smiley1);
		fillArrayList();

		editText = (EditText) findViewById(R.id.editText1);
		editText.setText(getSmiledText(this, "Hello :)"));
	}

	public Spannable getSmiledText(Context context, String text) {
  	  SpannableStringBuilder builder = new SpannableStringBuilder(text);
  	  int index;
  	  for (index = 0; index < builder.length(); index++) {
  	    for (Entry<String, Integer> entry : emoticons.entrySet()) {
  	      int length = entry.getKey().length();
  	      if (index + length > builder.length())
  	        continue;
  	      if (builder.subSequence(index, index + length).toString().equals(entry.getKey())) {
  	        builder.setSpan(new ImageSpan(context, entry.getValue()), index, index + length,
  	            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
  	        index += length - 1;
  	        break;
  	      }
  	    }
  	  }
  	  return builder;
  	}
    
    public void btn_click(View v) {
		 	View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup, null);
	        popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
	        popupWindow.showAtLocation(view, Gravity.TOP, 100, 200);
	        
	        final GridView gridView = (GridView) view.findViewById(R.id.gridView1);
	        
			gridView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
					
					String value = gridView.getAdapter().getItem(position).toString();
					Spannable spannable = getSmiledText(MainActivity.this, value);
					editText.setText(spannable);
					popupWindow.dismiss();
				}
			});
			gridView.setAdapter(new ImageAdapter());
	}
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	if(popupWindow != null && popupWindow.isShowing())
    	popupWindow.dismiss();
    }
    
    @Override
    public void onBackPressed() {
    	if(popupWindow != null && popupWindow.isShowing())
    		popupWindow.dismiss();
    	else
    		super.onBackPressed();
    }
    
    class ImageAdapter extends BaseAdapter {

		public int getCount() {
			return arrayList.size();
		}

		public Object getItem(int position) {
			return arrayList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.row, null);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
			imageView.setBackgroundResource(emoticons.get(arrayList.get(position)));
			
			return convertView;
		}}
}
