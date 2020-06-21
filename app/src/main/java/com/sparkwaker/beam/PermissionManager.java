package com.sparkwaker.beam;

import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

        private static RequestPermissionListener mRequestPermissionListener;
        private static int mRequestCode;

        public interface RequestPermissionListener {
            void onPermissionGranted();
            void onPermissionNotGranted();
        }

        public static void requestPermission(Fragment fragment, @NonNull String[] permissions, int requestCode, RequestPermissionListener requestPermissionListener) {
            mRequestPermissionListener = requestPermissionListener;
            mRequestCode = requestCode;
            if (!needRequestRuntimePermissions()) {
                mRequestPermissionListener.onPermissionGranted();
                return;
            }
            requestUnGrantedPermissions(fragment, permissions, requestCode);
        }

        private static boolean needRequestRuntimePermissions() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        }

        private static void requestUnGrantedPermissions(Fragment fragment, String[] permissions, int requestCode) {
            String[] unGrantedPermissions = findUnGrantedPermissions(fragment, permissions);
            if (unGrantedPermissions.length == 0) {
                mRequestPermissionListener.onPermissionGranted();
                return;
            }
            fragment.requestPermissions(unGrantedPermissions, requestCode);
        }

        private static boolean isPermissionGranted(Fragment fragment, String permission) {
            return ContextCompat.checkSelfPermission(fragment.requireContext(),permission)
                    == PackageManager.PERMISSION_GRANTED;
        }

        private static String[] findUnGrantedPermissions(Fragment fragment, String[] permissions) {
            List<String> unGrantedPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (!isPermissionGranted(fragment, permission)) {
                    unGrantedPermissionList.add(permission);
                }
            }
            return unGrantedPermissionList.toArray(new String[0]);
        }

        public static void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
            if (requestCode == mRequestCode) {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mRequestPermissionListener.onPermissionNotGranted();
                            return;
                        }
                    }
                    mRequestPermissionListener.onPermissionGranted();
                } else {
                    mRequestPermissionListener.onPermissionNotGranted();
                }
            }
        }
}
