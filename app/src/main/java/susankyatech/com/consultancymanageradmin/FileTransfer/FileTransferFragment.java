package susankyatech.com.consultancymanageradmin.FileTransfer;


import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import susankyatech.com.consultancymanageradmin.API.StudentAPI;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Generic.FileUtils;
import susankyatech.com.consultancymanageradmin.Generic.Keys;
import susankyatech.com.consultancymanageradmin.Model.Login;
import susankyatech.com.consultancymanageradmin.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
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
    @BindView(R.id.file_name)
    EditText fileName;
    @BindView(R.id.file_remark)
    EditText fileRemark;
    @BindView(R.id.send_button)
    FancyButton sendBtn;

    private ProgressDialog progressDialog;

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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("File Transfer");
        init();
        return view;
    }

    private void init() {
        progressDialog = new ProgressDialog(getContext());
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
                String name = fileName.getText().toString().trim();

                if (selectedFilePath == null) {
                    Toast.makeText(getContext(), "please choose file first", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name)) {
                    fileName.setError("Enter File Name");
                    fileName.requestFocus();
                } else {
                    progressDialog.setTitle("Uploading your Document");
                    progressDialog.setMessage("Please wait, while we are uploading your document.");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    uploadDocument(remark, name);
                }

            }
        });
    }

    private void uploadDocument(String remark, String name) {
        RequestBody fileBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("document", file.getName(), fileBody);

        RequestBody group = RequestBody.create(MediaType.parse("text/plain"), "From Student");
        RequestBody file_remark = RequestBody.create(MediaType.parse("text/plain"), remark);
        final RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), name);

        String studentId = String.valueOf(App.db().getInt(Keys.USER_ID));
        RequestBody fileId = RequestBody.create(MediaType.parse("text/plain"), studentId);

        StudentAPI studentAPI = App.consultancyRetrofit().create(StudentAPI.class);
        studentAPI.addFiles(group, file_remark, file_name, fileToUpload, fileId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        selectedFilePath = null;
                        fileName.setText("");
                        fileRemark.setText("");
                        MDToast mdToast = MDToast.makeText(getContext(), "File uploaded successfully", Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                        mdToast.show();
                    }
                } else {
                    try {
                        Log.d("client", "onResponse: error" + response.errorBody().string());
                        MDToast mdToast = MDToast.makeText(getContext(), "There was something wrong while uploading your file. Please try again!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                        mdToast.show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                MDToast mdToast = MDToast.makeText(getActivity(), "There is no internet connection. Please try again later!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                mdToast.show();
            }
        });


    }

    private void selectFile() {
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_FILES);

                return;
            } else {
//                openFilePicker();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_FILES && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri fileUri = data.getData();
                selectedFilePath = FileUtils.getPath(getContext(), data.getData());
                file = new File(selectedFilePath);
            }
        }
    }

}
