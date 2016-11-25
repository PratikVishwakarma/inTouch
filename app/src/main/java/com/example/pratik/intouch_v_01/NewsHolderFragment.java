package com.example.pratik.intouch_v_01;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pratik.intouch_v_01.model.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * Created by prati on 24-Nov-16.
 */


public class NewsHolderFragment extends Fragment {

    public static News mnews;
    TextView text_view_news_headline, text_view_news_content;
    ImageView image_view_new_image;

    private StorageReference mStorageRef;

    private int mPageNumber;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public NewsHolderFragment() {
    }
    public static NewsHolderFragment newInstance(int sectionNumber, News news) {
        NewsHolderFragment fragment = new NewsHolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        mnews = news;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        text_view_news_headline = (TextView) rootView.findViewById(R.id.text_view_news_headline);
        text_view_news_content = (TextView) rootView.findViewById(R.id.text_view_news_content);
        image_view_new_image = (ImageView) rootView.findViewById(R.id.image_view_news_image);

        StorageReference filePathRef = mStorageRef.child(getString(R.string.firebase_storage_newsImages)).child(mnews.getImage());

        text_view_news_headline.setText(mnews.getHeadline());
        text_view_news_content.setText(mnews.getNews_content());

        filePathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext())
                        .load(uri)
                        .error(R.drawable.no_image_available)
                        .into(image_view_new_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
}
