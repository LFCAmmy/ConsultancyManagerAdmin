package susankyatech.com.consultancymanageradmin.FileTransfer;


import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import susankyatech.com.consultancymanageradmin.R;

import static android.app.Activity.RESULT_OK;
import static susankyatech.com.consultancymanageradmin.Generic.FileURI.isDownloadsDocument;
import static susankyatech.com.consultancymanageradmin.Generic.FileURI.isExternalStorageDocument;
import static susankyatech.com.consultancymanageradmin.Generic.FileURI.isGooglePhotosUri;
import static susankyatech.com.consultancymanageradmin.Generic.FileURI.isMediaDocument;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileTransferFragment extends Fragment {

    @BindView(R.id.uploadFile)
    ImageView uploadFile;
    @BindView(R.id.file_remark)
    EditText fileRemark;
    @BindView(R.id.send_button)
    FancyButton sendBtn;

    private static final int RESULT_LOAD_FILES = 1;
    private static final int REQUEST_WRITE_PERMISSION = 786;

    private File file;
    private String selectedFilePath;

    public FileTransferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_transfer, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile();
                openFilePicker();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String remark = fileRemark.getText().toString().trim();

                if (selectedFilePath != null){
                    Toast.makeText(getContext(), "uploaded", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void selectFile() {
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_FILES);
            } else {
                openFilePicker();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent();
        intent.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "image/*", "application/pdf"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), RESULT_LOAD_FILES);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_FILES && resultCode == RESULT_OK){
            if (data.getData() != null){
                Uri fileUri = data.getData();
                selectedFilePath = getPath(data.getData());
                file = new File(getPath(data.getData()));

            }
        }
    }

    public String getPath(Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(getActivity(), uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(getActivity(), contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(getActivity(), contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(getActivity(), uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
