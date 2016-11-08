package inspector.ded.ajman.ajmaninspector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.ganesh.iarabic.arabic864;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static inspector.ded.ajman.ajmaninspector.Constants.MESSAGE_DEVICE_NAME;
import static inspector.ded.ajman.ajmaninspector.Constants.MESSAGE_STATE_CHANGE;
import static inspector.ded.ajman.ajmaninspector.Constants.MESSAGE_TOAST;
import static inspector.ded.ajman.ajmaninspector.Constants.MESSAGE_WRITE;

public class MainActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    //    public static AppBarLayout appBar;
//    public static Toolbar toolbar;
    public static WebView mWebView;


    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    EditText message;

    private boolean isLoaded = false;
    private boolean mEnablingBT;

    /**
     * Set to true to add debugging code and logging.
     */
    public static final boolean DEBUG = true;


    final Activity activity = this;

    private static final int FILECHOOSER_RESULTCODE = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUMA;
    private String mCM;
    private Uri mCapturedImageURI = null;

    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;

    private int DEVICE_LIST;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private BluetoothAdapter mBluetoothAdapter = null;
    private static BluetoothPrintService mSerialService = null;
    //declaration for in main class

    public arabic864 araconvert = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //        startActivity(new Intent(this, ShowWebView.class));

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //instance for Arabic under onCreate
        araconvert = new arabic864();
        mWebView = (WebView) findViewById(R.id.webView);

        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        }else {
            initWebView();
        }

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        mSerialService = new BluetoothPrintService(this, mHandlerBT);
//        setSupportActionBar(toolbar);
//        appBar.setVisibility(View.GONE);
//        toolbar.setVisibility(View.GONE);
//        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        ImageView menuIcon = (ImageView) findViewById(R.id.drawerIcon);
//        menuIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.openDrawer(GravityCompat.END);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {   // replaced in onCreate
        super.onRestoreInstanceState(savedInstanceState);
        mWebView.restoreState(savedInstanceState);
    }

    public void initWebView() {
        isLoaded = false;

        JavaScriptInterface jsInterface = new JavaScriptInterface(this);

        // Javascript inabled on webview
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.addJavascriptInterface(jsInterface, "JSInterface");

        // Other webview options
        mWebView.getSettings().setLoadWithOverviewMode(true);

        // Define url that will open in webview
        mWebView.loadUrl(PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .getString(getString(R.string.pref_url_key), getString(R.string.pref_url_default)));

        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.setWebViewClient(new MyWebViewClient() {
//            ProgressDialog progressDialog;

            //If you will not use this method url links are open in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Check if Url contains ExternalLinks string in url
                // then open url in new browser
                // else all webview links will open in webview browser
                if (url.contains("google")) {

                    // Could be cleverer and use a regex
                    //Open links in new browser
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    // Here we can open new activity

                    return true;

                } else {

                    // Stay within this webview and load url
                    view.loadUrl(url);
                    return true;
                }

            }


            //Show loader on url load
            public void onLoadResource(WebView view, String url) {

                // show progress  Dialog
//                if (progressDialog == null) {
//                    progressDialog = new ProgressDialog(MainActivity.this);
//                    progressDialog.setMessage("Loading...");
//                    progressDialog.show();
//                }
            }

            // Called when all page resources loaded
            public void onPageFinished(WebView view, String url) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoaded = true;
                    }
                }, 5000);

                try {
                    // Close progressDialog
//                    if (progressDialog != null && progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                        progressDialog = null;
//                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setWebChromeClient(new WebChromeClient() {

            // openFileChooser for Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                // Update message
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

//                try {
//
//                    // Create AndroidExampleFolder at sdcard
//
//                    File imageStorageDir = new File(
//                            Environment.getExternalStoragePublicDirectory(
//                                    Environment.DIRECTORY_PICTURES)
//                            , "AndroidExampleFolder");
//
//                    if (!imageStorageDir.exists()) {
//                        // Create AndroidExampleFolder at sdcard
//                        imageStorageDir.mkdirs();
//                    }
//
//                    // Create camera captured image file path and name
//                    File file = new File(
//                            imageStorageDir + File.separator + "IMG_"
//                                    + String.valueOf(System.currentTimeMillis())
//                                    + ".jpg");
//
//                    mCapturedImageURI = Uri.fromFile(file);
//
//                    // Camera capture image intent
//                    final Intent captureIntent = new Intent(
//                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
//
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType("image/*");
//
//                    // Create file chooser intent
//                    Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
//
//                    // Set camera intent to file chooser
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
//                            , new Parcelable[]{captureIntent});
//
//                    // On select image call onActivityResult method of activity
//                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
//
//                } catch (Exception e) {
//                    Toast.makeText(getBaseContext(), "Exception:" + e,
//                            Toast.LENGTH_LONG).show();
//                }

            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                openFileChooser(uploadMsg, "");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MainActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //openFileChooser for other Android versions
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType,
                                        String capture) {

//                openFileChooser(uploadMsg, acceptType);
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"),
                        MainActivity.FILECHOOSER_RESULTCODE);

            }
            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams){
                if(mUMA != null){
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null){
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    }catch(IOException ex){
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if(photoFile != null){
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    }else{
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray;
                if(takePictureIntent != null){
                    intentArray = new Intent[]{takePictureIntent};
                }else{
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                return true;
            }

            private File createImageFile() throws IOException{
                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "img_"+timeStamp+"_";
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                return File.createTempFile(imageFileName,".jpg",storageDir);
            }


            // The webPage has 2 filechoosers and will send a
            // console message informing what action to perform,
            // taking a photo or updating the file

            public boolean onConsoleMessage(ConsoleMessage cm) {

                onConsoleMessage(cm.message(), cm.lineNumber(), cm.sourceId());
                return true;
            }

            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                //Log.d("androidruntime", "Show console messages, Used for debugging: " + message);

            }

            public void onProgressChanged(WebView view, int progress) {
                activity.setTitle("Loading...");
                activity.setProgress(progress * 100);

                if (progress == 100)
                    activity.setTitle(R.string.app_name);
            }
        });
    }

    private void btPrint() {
        if (!mEnablingBT) { // If we are turning on the BT we cannot check if it's enable
            if ((mBluetoothAdapter != null) && (!mBluetoothAdapter.isEnabled())) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.alert_dialog_turn_on_bt)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.alert_dialog_warning_title)
                        .setCancelable(false)
                        .setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mEnablingBT = true;
                                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                            }
                        })
                        .setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finishDialogNoBluetooth();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            if (mSerialService != null) {
                // Only if the state is STATE_NONE, do we know that we haven't started already
                if (mSerialService.getState() == BluetoothPrintService.STATE_NONE) {
                    // Start the Bluetooth chat services
                    mSerialService.start();
                }
            }

//            if (mBluetoothAdapter != null) {
//                readPrefs();
//                updatePrefs();
//
//                mEmulatorView.onResume();
//            }
        }
    }

    public void finishDialogNoBluetooth() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_no_bt)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.app_name)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedHttpAuthRequest(WebView view,
                                              HttpAuthHandler handler, String host, String realm) {

//            handler.proceed("inspector1", "Abc12345");
            handler.proceed(PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext())
                            .getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default)),

                    PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext())
                            .getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default)));

        }
    }

    public class JavaScriptInterface {
        private Activity activity;

        public JavaScriptInterface(Activity activiy) {
            this.activity = activiy;
        }

        @JavascriptInterface
        public void startPrint(String printText){
            if (getConnectionState() == BluetoothPrintService.STATE_NONE) {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(activity, BTDeviceList.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            } else if (getConnectionState() == BluetoothPrintService.STATE_CONNECTED) {
                mSerialService.write(araconvert.Convert(printText, true));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //For Hardware Buttons
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_MENU:
                   SettingAction();
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {   //For Software Buttons " Huawei "
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_BACK:
                    SettingAction();
                    return true;
//                case KeyEvent.KEYCODE_APP_SWITCH:
//                    SettingAction();
//                    return true;
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }


//    @Override
//    protected void onUserLeaveHint() {  // Use secured button - not recommended
//        super.onUserLeaveHint();
//        SettingAction();
//    }


    private void SettingAction(){
    try{
        FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();
        dialog.show(getFragmentManager(), "NoticeDialogFragment");
    }catch(Exception E) {
        E.printStackTrace ();
    }
}

    // The Handler that gets information back from the BluetoothService
    private final Handler mHandlerBT = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (DEBUG) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothPrintService.STATE_CONNECTED:
                            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothPrintService.STATE_CONNECTING:
                            Toast.makeText(MainActivity.this, "Connecting", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothPrintService.STATE_LISTEN:
                            Toast.makeText(MainActivity.this, "Listening", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothPrintService.STATE_NONE:
                            Toast.makeText(MainActivity.this, "None", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
/*
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                mEmulatorView.write(readBuf, msg.arg1);

                break;
*/
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up butt  on, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_print_txt) {
//            startActivity(new Intent(this, BlueToothPrinterApp.class));
//            connect();
            return true;
        }

        if (id == R.id.action_print) {
//            Intent serverIntent = new Intent(this, BTDeviceList.class);
//            startActivityForResult(serverIntent, DEVICE_LIST);
//            connect();
            if (getConnectionState() == BluetoothPrintService.STATE_NONE) {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, BTDeviceList.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            } else if (getConnectionState() == BluetoothPrintService.STATE_CONNECTED) {
                mSerialService.stop();
                mSerialService.start();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int getConnectionState() {
        return mSerialService.getState();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == FILECHOOSER_RESULTCODE) {
            if (Build.VERSION.SDK_INT >= 21) {
                Uri[] results = null;
                //Check if response is positive
                if (resultCode == Activity.RESULT_OK) {

                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
                mUMA.onReceiveValue(results);
                mUMA = null;
            } else {
                if (requestCode == FILECHOOSER_RESULTCODE) {
                    if (null == mUploadMessage) return;
                    Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
            }
        }

        if (requestCode == DEVICE_LIST) {
            if (resultCode == Activity.RESULT_OK) {
//                connectDevice(data, false);
                if (isLoaded) {
//                    PrintActivity activity = new PrintActivity();
//                    Intent in = new Intent(MainActivity.this, activity.getClass());
////                    in.putExtra("data", siteToImage());
//                    assert data != null;
//                    in.putExtra("address", data.getExtras()
//                            .getString(BTDeviceList.EXTRA_DEVICE_ADDRESS));
//                    startActivity(in);
                    // Get the device MAC address
                    if (intent != null) {
                        String address = intent.getExtras()
                                .getString(BTDeviceList.EXTRA_DEVICE_ADDRESS);
                        // Get the BLuetoothDevice object
                        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                        // Attempt to connect to the device
                        mSerialService.connect(device);
                    } else {
                        Toast.makeText(MainActivity.this, "some error with device", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Page not Loaded yet please wait", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


//    private void print_bt() {
//        try {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            btoutputstream = btsocket.getOutputStream();
//
//            byte[] printformat = {0x1B, 0x21, FONT_TYPE};
//            btoutputstream.write(printformat);
//            String msg = message.getText().toString();
//            btoutputstream.write(msg.getBytes());
//            btoutputstream.write(0x0D);
//            btoutputstream.write(0x0D);
//            btoutputstream.write(0x0D);
//            btoutputstream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    // Open previous opened link from history on webview when back button pressed

//    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        } else {
//            super.onBackPressed();
//        }
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
            // Let the system handle the back button
            super.onBackPressed();
        }
    }
}
