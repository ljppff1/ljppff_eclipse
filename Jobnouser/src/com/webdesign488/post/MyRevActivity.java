package com.webdesign488.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.webdesign488.post.model.ApplyInfo;
import com.webdesign488.post.model.ForInfo;
import com.webdesign488.post.model.Info;
import com.webdesign488.post.net.AsyncTaskUtil;
import com.webdesign488.post.util.Content;
import com.webdesign488.post.util.Util;
import com.webdesign488.post.view.ProgressDialog;
import com.webdesign488.post.view.XListView;
import com.webdesign488.post.view.XListView.IXListViewListener;

public class MyRevActivity extends BaseActivity implements IXListViewListener {
     private XListView mLvnew1;
     private List<ApplyInfo> listApplyInfo =new ArrayList<ApplyInfo>();
	private String IMEI;
     private boolean flag =false;
     private String ID;
 	private View layout;
 	private Button mBTW1success;
 	private Button mBTW1next;
 	private PopupWindow mPop;
	private TextView mTVw1name;
	private TextView mTVw1show;
	private TextView mTVw1tell;
	private TextView mTVw1Id;
	private TextView mTVw1title;
	private TextView mTVw1content;
	private TextView mTVw1area;
	private TextView mTVw1local;

 	private int mPosition =0;
	private String Iftitle;
	private String Ifcontent;
	private String Ifcategory;
	private String Iflocation;
	private String Ifaid;
	private String Ifusername;
	private String Ifusermobile;
	private String Ifuserintro;
	private String locationone;
	private String locationtwo;
	private String locationthree;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.myrev);
    	initView();
    	initData();
    }


   @Override
protected void onResume() {
	if(flag&&listApplyInfo.size()<=0){
		initData();
	}
	super.onResume();
}
   @Override
protected void onPause() {
	   flag =true;
	super.onPause();
}
 private void initData() {
		IMEI =getSharedPreferences("AREA", Activity.MODE_PRIVATE).getString("IMEI", "");
	     List<String> list =new ArrayList<String>();
	     List<String> list1 =new ArrayList<String>();
	     list.add("IMEI");
	     list1.add(IMEI);
	     mPosition =0;
	      AsyncTaskUtil.getJson(list, list1,handler, dialog, Content.MYREVLIST, MyRevActivity.this, 3);
	}
 private InfoAdapter adapter;

private Handler handler =new Handler(){

	private String Etitle;

	public void handleMessage(android.os.Message msg) {
		   switch (msg.what) {
		   case 1:
			   Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast11), 1).show();
			   break;
		case 3:
			String json =(String) msg.obj;
			if(!TextUtils.isEmpty(json)){
				try {
					JSONObject jRoot = new JSONObject(json);
					int code = jRoot.getInt("code");
					if(code ==1){
						JSONArray data =jRoot.getJSONArray("data");
						listApplyInfo.clear();
						for(int i=0;i<data.length();i++){
							Integer id = data.getJSONObject(i).getInt("id");
      						String title =  data.getJSONObject(i).getString("title");
      						String date =  data.getJSONObject(i).getString("date");
							//Integer status = data.getJSONObject(i).getInt("status");
                           String status = data.getJSONObject(i).getString("status");
                         ApplyInfo info =new ApplyInfo();
                         info.setId(id+"");
                         info.setDate(date);
                         info.setStatus(status+"");
                         info.setTitle(title);
                         listApplyInfo.add(info);
						}
					adapter =new InfoAdapter();
					mLvnew1.setAdapter(adapter);
					}else if(code==0){
						String msg1 =jRoot.getString("msg");
		                Toast.makeText(getApplicationContext(), msg1, 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (dialog.isShow()) {
				dialog.cancel();
			}
			break;
		case 4:
			String json1 =(String) msg.obj;
			if (!TextUtils.isEmpty(json1)) {
				try {
					JSONObject jRoot = new JSONObject(json1);
					int code = jRoot.getInt("code");
					if (code == 1) {
						JSONObject data = jRoot.getJSONObject("data");
						Ifusername = data.getString("username");
						Ifusermobile = data.getString("usermobile");
						Ifuserintro = data.getString("userintro");
						Iftitle = data.getString("title");
						Ifcontent = data.getString("content");
						String categoryone = data.getString("categoryone");
						String categorytwo = data.getString("categorytwo");
						String categorythree = data
								.getString("categorythree");
						 locationone = data.getString("locationone");
						 locationtwo = data.getString("locationtwo");
						 locationthree = data
								.getString("locationthree");
						if (Util.isEmpty(locationtwo)) {
							Iflocation = locationone;
						} else if (Util.isEmpty(locationthree)) {
							Iflocation = locationone + ">" + locationtwo;
						} else {
							Iflocation = locationone + ">" + locationtwo
									+ ">" + locationthree;
						}
						if (Util.isEmpty(categorytwo)) {
							Ifcategory = categoryone;
						} else if (Util.isEmpty(categorythree)) {
							Ifcategory = categoryone + ">" + categorytwo;
						} else {
							Ifcategory = categoryone + ">" + categorytwo
									+ ">" + categorythree;
						}
						showDialog1();
					    
						
					}else{
				
                          Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast10), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			if (dialog.isShow()) {
				dialog.cancel();
			}
			break;

		default:
			break;
		}
	   }  
	   };
private ProgressDialog dialog;

	private void initView() {
		dialog =new com.webdesign488.post.view.ProgressDialog(MyRevActivity.this);

		mLvnew1 =(XListView)this.findViewById(R.id.mLvnew1);
		mLvnew1.setCacheColorHint(0);
		mLvnew1.setPullLoadEnable(true);
		mLvnew1.setXListViewListener(this);
		mLvnew1.setHeaderDividersEnabled(false);
		mLvnew1.setFooterDividersEnabled(false);
		mLvnew1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

					ID=listApplyInfo.get(position-1).getId();
					initData1(listApplyInfo.get(position-1).getId());

			}
		});
	}

	@Override
	public void onRefresh() {
		initData();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		onLoad();
		}
	private void onLoad() {
		mLvnew1.stopRefresh();
		mLvnew1.stopLoadMore();
		mLvnew1.setRefreshTime(new Date().toLocaleString());
	}

	class InfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listApplyInfo.size();
		}

		@Override
		public Object getItem(int position) {
			return listApplyInfo.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHodler holder;
			View view = convertView;
			if (view == null) {
				view = View.inflate(getApplicationContext(), R.layout.item_myrev, null);
				holder = new ViewHodler();
				holder.mTVInfoTitle = (TextView) view.findViewById(R.id.mTVInfoTitle);
				holder.mTVMyrevstatus = (TextView) view.findViewById(R.id.mTVMyrevstatus);
				holder.mTVMyrevdate = (TextView) view.findViewById(R.id.mTVMyrevdate);
				view.setTag(holder);
			} else {
				holder = (ViewHodler) view.getTag();
			}
			holder.mTVInfoTitle.setText(listApplyInfo.get(position).getTitle());
			if(!TextUtils.isEmpty(listApplyInfo.get(position).getStatus())){
			if(listApplyInfo.get(position).getStatus().equals("0")){
				holder.mTVMyrevstatus.setText("未被查閱");
			}
			if(listApplyInfo.get(position).getStatus().equals("1")){
				holder.mTVMyrevstatus.setText("已被查閱");
			}
			if(listApplyInfo.get(position).getStatus().equals("2")){
				holder.mTVMyrevstatus.setText("已被錄取");
			}
			}

			holder.mTVMyrevdate.setText(listApplyInfo.get(position).getDate());
			

			
			return view;
		}
	
	
			   

		 class ViewHodler {
			private TextView mTVInfoTitle,mTVMyrevstatus,mTVMyrevdate;
			private Button mBTlistInfo1,mBTlistInfo2;
		}


	}
	OnClickListener listener =new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
		 switch (v.getId()) {
		case R.id.mTVw1Id:
			 flag =false;
			if (mPop != null && mPop.isShowing()) {
				mPop.dismiss();
				mPop = null;
				
			}
			break;
		case R.id.mBTW1next:
			 flag =false;
			if (mPop != null && mPop.isShowing()) {
				mPop.dismiss();
				mPop = null;
				
			}
			break;
		default:
			break;
		}
		}
	};
	private TextView mTVw1time;
	private TextView mTVw1area1cc;
	private TextView mTVw1area1aa;
	private TextView mTVw1area1bb;

	private void showDialog1() {
		/*
		 * LayoutInflater mInflater =
		 * LayoutInflater.from(ExecuteOrderActivity.this); layout =
		 * mInflater.inflate(R.layout.window, null);
		 */
		if(mPop==null){

			
		layout = View.inflate(MyRevActivity.this, R.layout.window2a, null);
		layout.setFocusable(true); // 这个很重要
		layout.setFocusableInTouchMode(true);
		mPop = new PopupWindow(layout, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		mPop.setFocusable(true);

		mPop.showAtLocation(
				MyRevActivity.this.findViewById(R.id.mLlPubInfo1),
				Gravity.CENTER, 0, 0);// 在屏幕顶部|居右，带偏移
       layout.findViewById(R.id.mBTgocancel).setVisibility(View.GONE);
		mBTW1next = (Button) layout.findViewById(R.id.mBTW1next);
		mBTW1next.setText(getResources().getString(R.string.sure));
		mBTW1next.setOnClickListener(listener);
		mTVw1name = (TextView) layout.findViewById(R.id.mTVw1name);
		mTVw1show = (TextView) layout.findViewById(R.id.mTVw1show);
		mTVw1tell = (TextView) layout.findViewById(R.id.mTVw1tell);
		mTVw1title = (TextView) layout.findViewById(R.id.mTVw1title);
		mTVw1content = (TextView) layout.findViewById(R.id.mTVw1content);
		mTVw1area1cc = (TextView) layout.findViewById(R.id.mTVw1area1cc);
		mTVw1area1aa = (TextView) layout.findViewById(R.id.mTVw1area1aa);
		mTVw1area1bb = (TextView) layout.findViewById(R.id.mTVw1area1bb);
		mTVw1local = (TextView) layout.findViewById(R.id.mTVw1local);
		mTVw1name.setText(Ifusername);
		mTVw1show.setText(Ifuserintro);
		mTVw1tell.setText(Ifusermobile);
		mTVw1title.setText(Iftitle);
		mTVw1content.setText(Ifcontent);
		mTVw1area1cc.setText(locationone);
		mTVw1area1aa.setText(locationtwo);
		mTVw1area1bb.setText(locationthree);
		mTVw1local.setText(Ifcategory);
		mTVw1Id = (TextView) layout.findViewById(R.id.mTVw1Id);
		mTVw1Id.setOnClickListener(listener);
		}
		// mPop.setFocusable(true);
		// mPop.setOutsideTouchable(true);
		layout.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					flag = false;
					if (mPop != null && mPop.isShowing()) {
						mPop.dismiss();
						mPop = null;

						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				/*
				 * if (mPop != null && mPop.isShowing()) { mPop.dismiss();
				 * mPop = null; }
				 */
				return true;
			}
		});

	}
	
	
	 private void initData1(String id) {
			IMEI =getSharedPreferences("AREA", Activity.MODE_PRIVATE).getString("IMEI", "");
		
		     List<String> list =new ArrayList<String>();
		     List<String> list1 =new ArrayList<String>();
		     list.add("id");
		     list1.add(id);
		     AsyncTaskUtil.getJson(list, list1,handler, dialog, Content.MYREVDETAIL, MyRevActivity.this, 4);
		}
}
