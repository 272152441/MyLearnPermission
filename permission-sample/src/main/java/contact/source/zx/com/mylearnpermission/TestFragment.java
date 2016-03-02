package contact.source.zx.com.mylearnpermission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zx.mypermission.MyPermission;
import com.zx.permissionannomation.MyPermissionDenied;
import com.zx.permissionannomation.MyPermissionGrant;

/**
 * Description
 *
 * @version 1.0
 *          time 13:58 2016/3/2.
 * @auther zhangxiao
 */
public class TestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.id_btn_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.requirePermission(TestFragment.this, 4, Manifest.permission.WRITE_CONTACTS);
            }
        });

    }

    @MyPermissionGrant(4)
    public void requestContactSuccess() {
        Log.i("call permission" , "fragment Contact Success");

    }

    @MyPermissionDenied(4)
    public void requestContactFailed() {
        Log.i("call permission" , "fragment Contact failed");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MyPermission.onRequestPermissionResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
