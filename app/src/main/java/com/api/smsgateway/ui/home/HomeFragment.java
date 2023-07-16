package com.api.smsgateway.ui.home;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.api.smsgateway.adapter.MessageAdapter;
import com.api.smsgateway.databinding.FragmentHomeBinding;
import com.api.smsgateway.model.RMessage;
import com.api.smsgateway.repo.aboutusrepo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private aboutusrepo aboutusrepo = new aboutusrepo();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        aboutusrepo.aboutUs().observe(getActivity(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
////                if (s.isEmpty()){
////                    binding.aboutus.setText(s);
////                }else{
////                    binding.aboutus.setText("we are sms gateway...");
////                }
//            }
//        });

        // Enable JavaScript (optional)
        WebSettings webSettings = binding.webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set a WebViewClient to handle page navigation and loading
        binding.webview.setWebViewClient(new WebViewClient());

        // Load a web page
        binding.webview.loadUrl("https://epaymentbd.com/about-us");

        return root;

}

}
