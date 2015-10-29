package ru.rsdev.HorizontalGallery;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

import ru.rsdev.files.R;

public class ListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> itemsName;
    ArrayList<String> itemsImage;

    LayoutInflater lInflater;


    ListAdapter(Context context, ArrayList<String> itemsName, ArrayList<String> itemsImage) {
        this.context=context;
        this.itemsName=itemsName;
        this.itemsImage=itemsImage;


        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemsName.size();
    }

    @Override
    public Object getItem(int arg0) {
        return itemsName.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.top_list_item, parent, false);
        }

        // заполняем View в пункте списка данными
        TextView textView = ((TextView) view.findViewById(R.id.text_date));
        ImageView imageView =  ((ImageView) view.findViewById(R.id.img));

        textView.setText(itemsName.get(position));


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
        imageLoader.displayImage("File:/" + itemsImage.get(position), imageView, options);

        final HorizontalListView lv = (HorizontalListView) parent;
        if(position == lv.getSelectedItemPosition()){
            // цвет выбранного элемента
            view.setBackgroundColor(0xFF0000FF);
        } else {
            // старая разметка, где работает только state_pressed
            view.setBackgroundColor(Color.BLUE);
        }


        return view;
    }
}

