package com.niner.cigifydona;

import static com.rosaloves.bitlyj.Bitly.as;
import static com.rosaloves.bitlyj.Bitly.shorten;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rosaloves.bitlyj.Url;

public final class MainActivity extends Activity implements OnClickListener {

	static Button search, lucky, share;
	static EditText searchtext;
	static TextView link, shorten;
	static String linkf, shortu;
	static char[] tarr;
	static int flag = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
																				// to prevent screen from going into landscape mode
																				// and hence preventing the activity from restarting
		setContentView(R.layout.activity_main);
		search = (Button) findViewById(R.id.bsearch);
		share = (Button) findViewById(R.id.bshare);
		lucky = (Button) findViewById(R.id.blucky);
		link = (TextView) findViewById(R.id.tvlink);
		shorten = (TextView) findViewById(R.id.tvshort);
		searchtext = (EditText) findViewById(R.id.etsearch);
		search.setOnClickListener(this);
		share.setOnClickListener(this);
		lucky.setOnClickListener(this);
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.aboutme:
			Intent abt = new Intent(MainActivity.this, AboutMe.class);
			startActivity(abt);
			break;
		case R.id.exit:
			finish(); // if exit is selected from menu finish activity
			break;
		}
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getBaseContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) { // check
																							// if
																							// android
																							// version
																							// >3.0
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder() // required
																					// for
																					// generating
																					// bit.ly
																					// links
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		String test = searchtext.getText().toString();
		tarr = test.toCharArray();
		if (!test.matches("")) {
			switch (arg0.getId()) {
			case R.id.bsearch: // do when search button pressed
				imm.hideSoftInputFromWindow(searchtext.getWindowToken(), 0);
				makelink();
				flag = 0;
				link.setText("http://lmgtfy.com/?q=" + linkf);
				if (isOnline()) {
					try {
						Url url = as("9ninernine",
								"R_6602cf608d079310ec6da8a0e2802e51").call(
								shorten("http://lmgtfy.com/?q=" + linkf));
						linkf = url.getShortUrl();
						shorten.setText(linkf);
					} catch (Exception e) {

					}
					if (copy(true)) {
						Toast.makeText(getApplicationContext(), // show toast
								"Link Copied To Clipboard", Toast.LENGTH_SHORT)
								.show();
					}

					shortu = linkf;
				} else {
					copy(false);
					Toast.makeText(
							getApplicationContext(),
							"No Internet. Unable to generate Bit.ly link.\nNormal link copied to clipboard",
							Toast.LENGTH_LONG).show();
					shortu = link.getText().toString();
				}

				break;
			case R.id.blucky:
				imm.hideSoftInputFromWindow(searchtext.getWindowToken(), 0);
				makelink();
				flag = 1;
				link.setText("http://lmgtfy.com/?q=" + linkf + "&l=1");
				if (isOnline()) {
					try {
						Url urll = as("9ninernine",
								"R_6602cf608d079310ec6da8a0e2802e51").call(
								shorten("http://lmgtfy.com/?q=" + linkf
										+ "&l=1"));
						linkf = urll.getShortUrl();
						shorten.setText(linkf);
					} catch (Exception e) {

					}
					if (copy(true)) {
						Toast.makeText(getApplicationContext(), // show toast
								"Link Copied To Clipboard", Toast.LENGTH_SHORT)
								.show();
					}
					shortu = linkf;
				} else {
					Toast.makeText(
							getApplicationContext(),
							"No Internet. Unable to generate Bit.ly link.\nNormal link copied to clipboard",
							Toast.LENGTH_LONG).show();
					copy(false);
					shortu = link.getText().toString();
				}
				break;
			case R.id.bshare:
				imm.hideSoftInputFromWindow(searchtext.getWindowToken(), 0);
				Intent intent = new Intent(); // for the share button
				intent.setAction(android.content.Intent.ACTION_SEND);
				intent.setType("text/plain");
				if (flag == 0) {
					intent.putExtra(Intent.EXTRA_TEXT, shortu);
					startActivity(Intent.createChooser(intent, "Share to.."));
				} else if (flag == 1) {
					intent.putExtra(Intent.EXTRA_TEXT, shortu);
					startActivity(Intent.createChooser(intent, "Share to.."));
				} else
					Toast.makeText(getApplicationContext(),
							"First generate a link", Toast.LENGTH_SHORT).show();
				break;
			}
			Linkify.addLinks(link, Linkify.ALL);
			Linkify.addLinks(shorten, Linkify.ALL);
		} else {
			Toast.makeText(getApplicationContext(),
					"First enter a search query!", Toast.LENGTH_SHORT).show(); // if
																				// no
																				// search
																				// term
		}
	}

	public boolean copy(boolean net) {
		// TODO Auto-generated method stub
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) { // check
																							// if
																							// android
																							// version
																							// >3.0
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			android.content.ClipData clip;
			if (net) {
				clip = android.content.ClipData.newPlainText("text label",
						shorten.getText());
			} else {
				clip = android.content.ClipData.newPlainText("text label",
						link.getText());
			}
			clipboard.setPrimaryClip(clip); // copy link to clipboard
			return true;
		} else
			return false;

	}

	public static final void makelink() {
		// TODO Auto-generated method stub
		shorten.setText("");
		link.setText("");
		for (int i = 0; i < tarr.length; i++) {
			if (tarr[i] == ' ')
				tarr[i] = '+';
			linkf = new String(tarr);
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public final boolean onKeyDown(int keyCode) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { // finish when back key
													// pressed
			finish();
		}
		return super.onKeyDown(keyCode, null);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}
}