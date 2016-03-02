package contact.source.zx.com.mylearnpermission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zx.mypermission.MyPermission;
import com.zx.permissionannomation.MyPermissionDenied;
import com.zx.permissionannomation.MyPermissionGrant;

public class MainActivity extends AppCompatActivity {

    private static final int REQUECT_CODE_SDCARD = 2;
    private static final int REQUECT_CODE_CALL_PHONE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mBtnSdcard = (Button) findViewById(R.id.id_btn_sdcard);
        Button mBtnCallPhone = (Button) findViewById(R.id.id_btn_callphone);

        mBtnSdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.requirePermission(MainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });

        mBtnCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.requirePermission(MainActivity.this, REQUECT_CODE_CALL_PHONE, Manifest.permission.CALL_PHONE);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        MyPermission.onRequestPermissionResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @MyPermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcartSMyPermissionGrant(){
        Log.i("call permission" , "sdcard grant");
    }

    @MyPermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcartSMyPermissionDenied(){
        Log.i("call permission" , "sdcard denied");
    }

    @MyPermissionGrant(REQUECT_CODE_CALL_PHONE)
    public void requestCallPhonePermissionGrant(){
        Log.i("call permission" , "call phone grant");
    }

    @MyPermissionDenied(REQUECT_CODE_CALL_PHONE)
    public void requestCallPhonePermissionDenied(){
        Log.i("call permission" , "call phone denied");
    }

}
