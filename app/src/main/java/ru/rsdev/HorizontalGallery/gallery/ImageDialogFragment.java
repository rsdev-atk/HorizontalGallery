package ru.rsdev.HorizontalGallery.gallery;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.rsdev.files.R;

public class ImageDialogFragment extends DialogFragment implements OnClickListener {

    public static final String ISLOCKED_ARG = "isLocked";
    public ViewPager mViewPager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /*
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.image_dialog, null);
        PhotoView photoView = (PhotoView)v.findViewById(R.id.iv_photo);
        Bundle bundle = this.getArguments();
        ArrayList<String> dayList = bundle.getStringArrayList("dayList");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(),dayList);
        photoView.setImageURI(Uri.parse("/storage/emulated/0/DCIM/Camera/20151023_150901.jpg"));
        return v;
        */



        //getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.image_dialog, null);
        Bundle bundle = this.getArguments();
        ArrayList<String> dayList = bundle.getStringArrayList("dayList");
        //Удаляем лишнее слово File в начале строки
        int dayListSize = dayList.size();
        for(int i=0;i<dayListSize;i++){
            String newString = dayList.get(i);
            StringBuilder sb = new StringBuilder(newString);
            sb.delete(0,6);
            dayList.remove(i);
            dayList.add(0,sb.toString());
        }


        mViewPager = (HackyViewPager)v.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new ViewPagerAdapter(getActivity(),dayList));

        return v;


    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v) {

    }
}
