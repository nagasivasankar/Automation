package fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appsee.Appsee;

import java.io.File;

import co.legion.client.R;
import helpers.BitmapHelper;
import interfaces.ImagePickerListener;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 9/1/2016.
 */
public class CameraGalleryFragment extends DialogFragment implements Legion_Constants {

    private static final int CONSTANT                             = 1;
    private static final int CLICK_PHOTO_REQUEST_CODE             = 1 + CONSTANT;
    private static final int PICK_PHOTO_REQUEST_CODE              = 2 + CONSTANT;
    private static final int PERMISSIONS_REQUEST_GALLERY_CONSTANT = 3 + CONSTANT;
    private static final int PERMISSIONS_REQUEST_CAMERA_CONSTANT  = 4 + CONSTANT;
    private static int imagePosition;
    private Dialog dialog;
    private String clickedPhotoPath;
    public static MyProfileFragment profileFragment;

    public CameraGalleryFragment() {
    }

    public static CameraGalleryFragment newInstance(MyProfileFragment profileFragment, boolean isProfilePicSet, int position) {
        CameraGalleryFragment.profileFragment = profileFragment;
        imagePosition = position;
        return new CameraGalleryFragment();
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        this.dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) rootView.findViewById(R.id.parentLayout));
        final LinearLayout cameraLayout = (LinearLayout) rootView.findViewById(R.id.cameraLayout);
        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CAMERA_CONSTANT);
                } else {
                    if (isSDPresent()) {
                        openCameraApp();
                    } else {
                        showToast("SD card is not mounted in this device");
                    }
                }
            }
        });
        final LinearLayout cancelLayout = (LinearLayout) rootView.findViewById(R.id.cancelLayout);
        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final LinearLayout galleryLayout = (LinearLayout) rootView.findViewById(R.id.galleryLayout);
        galleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the SDK version and whether the permission is already granted or not.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_GALLERY_CONSTANT);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {
                    if (isSDPresent()) {
                        openGalleryApp();
                    } else {
                        showToast("SD card is not mounted in this device");
                    }
                }
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    /* @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        imagePickerListener.onImagePickerDismiss(dialog);
       Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) parentFragment).onDismiss(dialog);
        }else {
            final Activity activity = getActivity();
            if ((activity != null) && (activity instanceof DialogInterface.OnDismissListener)) {
                ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
            }
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_camera_gallery, container, false);
    }

    public void openCameraApp() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            clickedPhotoPath = LegionUtils.getClickedPhotoPath(getActivity().getApplicationContext());
            if (clickedPhotoPath == null) {
                showToast("Something went wrong.");
                return;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(clickedPhotoPath)));
            if(LegionUtils.isFeatureEnabled(getActivity(), "feature.appsee", "")) {
                Appsee.pause();
            }
            startActivityForResult(intent, CLICK_PHOTO_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Camera app not found.");
        }
    }

    public void openGalleryApp() {
        try {
            clickedPhotoPath = null;
            if(LegionUtils.isFeatureEnabled(getActivity(), "feature.appsee", "")) {
                Appsee.pause();
            }
            startActivityForResult(LegionUtils.getGalleryIntent(), PICK_PHOTO_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            showToast("Gallery app not found.");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(getActivity() == null){
            return;
        }
        if(LegionUtils.isFeatureEnabled(getActivity(), "feature.appsee", "")) {
            Appsee.resume();
        }
        if (profileFragment == null) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_PHOTO_REQUEST_CODE) {
                Uri imageUri = data.getData();
                String imageFilePath = LegionUtils.getPathOfExternalPhotoFromUri(getActivity().getApplicationContext(), imageUri);
                if (imageUri != null && imageFilePath != null && !imageFilePath.startsWith("http")) {
                    Bitmap bmp = BitmapHelper.decodeBitmapFromPath(imageFilePath, PROFILE_PIC_BITMAP_WIDTH_HEIGHT, PROFILE_PIC_BITMAP_WIDTH_HEIGHT, LegionUtils.getPhotoOrientation(imageFilePath));
                    profileFragment.onImagePick(imageFilePath, bmp, dialog, imagePosition);
                }
            } else if (requestCode == CLICK_PHOTO_REQUEST_CODE) {
                Bitmap bmp = BitmapHelper.decodeBitmapFromPath(clickedPhotoPath, PROFILE_PIC_BITMAP_WIDTH_HEIGHT, PROFILE_PIC_BITMAP_WIDTH_HEIGHT, LegionUtils.getPhotoOrientation(clickedPhotoPath));
                profileFragment.onImagePick(clickedPhotoPath, bmp, dialog, imagePosition);
            } else {
                profileFragment.onImagePick(null, null, dialog, imagePosition);
            }
        } else {
            profileFragment.onImagePick(null, null, dialog, imagePosition);
        }
    }

    protected void showToast(final String msg) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_GALLERY_CONSTANT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted
                    if (isSDPresent()) {
                        openGalleryApp();
                    } else {
                        showToast("SD card is not mounted in this device");
                    }
                } else {
                    showToast("Until you grant the permissions, You cannot pick photos");
                }
                break;

            case PERMISSIONS_REQUEST_CAMERA_CONSTANT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Now user should be able to use camera
                    if (isSDPresent()) {
                        openCameraApp();
                    } else {
                        showToast("SD card is not mounted in this device");
                    }
                } else {
                    // Your app will not have this permission. Turn off all functions
                    // that require this permission or it will force close like your
                    // original question
                    showToast("Until you grant the permissions, You cannot take photos");
                }
                break;
        }
    }

    public static boolean isSDPresent() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
}