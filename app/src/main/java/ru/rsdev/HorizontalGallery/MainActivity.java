package ru.rsdev.HorizontalGallery;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;

import ru.rsdev.HorizontalGallery.gallery.ImageDialogFragment;
import ru.rsdev.files.R;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;


public class MainActivity extends FragmentActivity {

    private String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath(); // путь для корневого элемента
    private String sdDIR = Environment.getExternalStorageDirectory().getParent();// путь для SD карты
    final static int RQS_OPEN_AUDIO_MP3 = 1;

    GridView gridView;
    HorizontalListView horizontalListView;
    TextView textDay,textView2,textView;


    ArrayList<String> fileList = new ArrayList<String>();//Список найденных файлов
    ArrayList<String> fileProperty = new ArrayList<String>();//Список дат к найденным файлам
    ArrayList<String> unicDateList = new ArrayList<String>();// Список дат для отображения
    ArrayList<String> dayList = new ArrayList<String>();//Список материалов для выбранной даты
//    ArrayList<String> dateCalendar = new ArrayList<String>();

    FileUtil fileUtil = new FileUtil();
    DateUtil dateUtil = new DateUtil();
    SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil();

    String puthFile;
    int dayNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        //Установка альбомной ориентации экрана
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Привязка контекстного меню
        registerForContextMenu(gridView);
        registerForContextMenu(horizontalListView);

        //Поиск всех файлов
        ArrayList<ArrayList<String>> arry = fileUtil.getDir(rootDir);
        fileList = arry.get(0);
        fileProperty = arry.get(1);


        //Добавление 30 дат от текущей даты
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy:MM:dd");
        Calendar calendar;
        calendar=Calendar.getInstance();
        String firstDayStart = currentDate.format(Calendar.getInstance().getTime());
        textView.setText(dateUtil.getDateWithoutTime(firstDayStart));


        int dayOfMonth = calendar.getTime().getDate();
        for(int i=1;i<31;i++){

            String firstDayNumber = currentDate.format(calendar.getTime());
            firstDayNumber = dateUtil.getDateWithoutTime(firstDayNumber);
            unicDateList.add(firstDayNumber);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

//        setImageInDateList();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Показываем путь к изображению
                //Toast.makeText(getApplication(), fileList.get(position), Toast.LENGTH_SHORT).show();

                //Показ выбранного изображения
                ImageDialogFragment imageDialogFragment = new ImageDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("dayList",dayList);
                imageDialogFragment.setArguments(bundle);
                imageDialogFragment.show(getFragmentManager(), "imageDialogFragment");

            }
        });

        //Поиск уникальных значений дат
        //getUnicDate();


        horizontalListView.setOnItemClickListener(itemClickListener);
        dayList = getAllImageInDay(0);
        showImage(dayList);
        setImageInDateList();


        if(dayList.isEmpty()){
            textView2.setText("Материалов не найдено");
            textView2.setHeight(60);
        }
        else {
            textView2.setText("");
            textView2.setHeight(0);
        }




        String value = sharedPreferencesUtil.getData(getApplication(), "photo_"+ unicDateList.get(dayNumber));
        textDay.setText(value);
    }

    private void getUnicDate(){
        //Поиск уникальных значений дат
        LinkedHashSet dateList = new LinkedHashSet();
        for(int i=0;i<fileProperty.size();i++) {
            dateList.addAll(fileProperty);
        }
        unicDateList.addAll(dateList);
        //Запрос описаний для дней
        //dayInfoList =
    }


    public void showImage(ArrayList<String> list){
        //Адаптер для GridView
        GridAdapter adapterGridView = new GridAdapter(this, list);
        gridView.setAdapter(adapterGridView);
    }

    //Сортировка изображений по выбранному дню
    public ArrayList<String> getAllImageInDay(int pos){
        String date = unicDateList.get(pos);
        ArrayList<Integer> numberList = new ArrayList<Integer>();
        for (int i=0; i<fileProperty.size();i++){
            if(fileProperty.get(i).equals(date))
                numberList.add(i);
        }
        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<numberList.size();i++){
            int position = numberList.get(i);
            list.add("File:/" + fileList.get(position));
        }
        return list;
    }

    //Слушатель для ListView с датами
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            dayNumber = position;

            ListAdapter.positionNumber = dayNumber;

            horizontalListView.setItemChecked(position, true);
            horizontalListView.setSelected(true);

            dayList = getAllImageInDay(dayNumber);
            showImage(dayList);

            //Вывод сообщения об отсутствии данных, или отображение материалов
            if(dayList.isEmpty()){
                textView2.setText("Материалов не найдено");
                textView2.setHeight(60);
            }
            else {
                textView2.setText("");
                textView2.setHeight(0);
            }
            String value = sharedPreferencesUtil.getData(getApplication(), "photo_"+ unicDateList.get(dayNumber));
            textDay.setText(value);
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_grid, menu);
    }

    //Показ календаря для выбора новой даты изображения
    private void showDatePickerEditDateImage() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(YEAR));
        args.putInt("month", calender.get(MONTH));
        args.putInt("day", calender.get(DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    private void showDatePickerChooseFirstDate() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(YEAR));
        args.putInt("month", calender.get(MONTH));
        args.putInt("day", calender.get(DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onDateFirst);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    //Слушатель для отлова выбора новой даты в календаре для изменения даты изображения
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(year)).append(":").append(String.valueOf(monthOfYear+1)).append(":").append(String.valueOf(dayOfMonth));
            fileUtil.setNewDateFile(puthFile, sb.toString());

            fileList.clear();
            fileProperty.clear();
            unicDateList.clear();
            dayList.clear();

            fileUtil.getDir(rootDir);
            //fileUtil.getDir(sdDIR);
//            getUnicDate();
            dayList = getAllImageInDay(0);
            setImageInDateList();
            showImage(dayList);
        }
    };

    //Слушатель для отлова выбора новой первой даты
    DatePickerDialog.OnDateSetListener onDateFirst = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(year)).append(":").append(String.valueOf(monthOfYear+1)).append(":").append(String.valueOf(dayOfMonth));

            //Toast.makeText(getApplicationContext(),sb.toString(),Toast.LENGTH_SHORT).show();

            Calendar calendar;
            calendar=Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat month_date = new SimpleDateFormat("yyyy:MM:dd");

            unicDateList.clear();

            for(int i=1;i<31;i++){
                /*
                String firstDate = month_date.format(calendar.getTime());
                unicDateList.add(firstDate);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth - i);
                */

                String firstDayNumber = month_date.format(calendar.getTime());
                firstDayNumber = dateUtil.getDateWithoutTime(firstDayNumber);
                unicDateList.add(firstDayNumber);
                calendar.add(Calendar.DAY_OF_MONTH, -1);



            }
//            setImageInDateList();
            //textView.setText(dateUtil.getDateWithoutTime(unicDateList.get(0)));
            textView.setText(unicDateList.get(0));

            dayList = getAllImageInDay(0);
            showImage(dayList);
            //Вывод сообщения об отсутствии данных, или отображение материалов
            if(dayList.isEmpty()){
                textView2.setText("Материалов не найдено");
                textView2.setHeight(60);
            }
            else {
                textView2.setText("");
                textView2.setHeight(0);
            }




/*

            fileUtil.setNewDateFile(puthFile, sb.toString());

            fileList.clear();
            fileProperty.clear();
            unicDateList.clear();
            dayList.clear();

            fileUtil.getDir(rootDir);
            //fileUtil.getDir(sdDIR);
            getUnicDate();
            dayList = getAllImageInDay(0);
//            setImageInDateList();
            showImage(dayList);
            */
        }
    };








    //Ищем изображения для обложек
    private void setImageInDateList(){

        ///////TEST//////наполняем тестовыми данными для отображения

        ArrayList<String> coverList = new ArrayList<>();
        for(int i=0;i<unicDateList.size();i++){
            ArrayList<String> dayListItem = getAllImageInDay(i);
            if(dayListItem.isEmpty())
                //coverList.add(null);
                coverList.add("null");

            else {
                String inputDate = dayListItem.get(0);
                StringBuilder sb = new StringBuilder(inputDate);
                sb.delete(0,6);
                coverList.add(sb.toString());
            }
        }


/*
        ArrayList<String> coverList = new ArrayList<>();
        //Map coverMap = new HashMap<String,String>();
        //

        for(int i=0;i<unicDateList.size();i++){

            for (int j=0;j<fileList.size();j++){
                String param1 = fileProperty.get(j);
                String param2 = unicDateList.get(i);

                if(param1.equals(param2)){
                    coverList.add(fileList.get(j));
                    //coverMap.put(param1,fileList.get(j));
                    break;
                }
            }
        }


        //TEST/////////////////////Обложки для дней
        ArrayList<String> coverList = new ArrayList<String>();
        for (int i=0;i<30;i++){
            coverList.add("/storage/emulated/0/DCIM/Camera/20151023_150901.jpg");
        }
        */

        ListAdapter adapterListView = new ListAdapter(this, unicDateList, coverList);
        horizontalListView.setAdapter(adapterListView);


    }

    //Обработчик нажатий по контекстному меню в GridView
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case R.id.item1://Изменение даты
                puthFile = dayList.get(info.position);
                showDatePickerEditDateImage();
                break;

            case R.id.item2://Изменение обложки

                break;

            case R.id.item3://Изменение описание
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Введите описание дня");
                final EditText input = new EditText(this);
                alert.setView(input);
                input.setText(sharedPreferencesUtil.getData(getApplication(), "photo_"+ unicDateList.get(dayNumber)));

                alert.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = String.valueOf(input.getText());
                        sharedPreferencesUtil.setData(getApplication(), "photo_"+ unicDateList.get(dayNumber), value);
                        textDay.setText(value);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
                break;

            case R.id.item4://Изменение звука
                Intent intent = new Intent();
                intent.setType("audio/mp3");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(
                intent, "Open Audio (mp3) file"), RQS_OPEN_AUDIO_MP3);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RQS_OPEN_AUDIO_MP3) {
                Uri audioFileUri = data.getData();

                String soundPath = audioFileUri.getPath();

                sharedPreferencesUtil.setData(getApplication(), "sound_"+ unicDateList.get(dayNumber), soundPath);

            }
        }
    }







    //Начальная иницилизация компонентов
    private void initComponents() {
        gridView = (GridView)findViewById(R.id.gridView);
        horizontalListView = (HorizontalListView)findViewById(R.id.horizontalListView);
        textDay = (TextView)findViewById(R.id.text_day);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView = (TextView) findViewById(R.id.textView);


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
    }

    //Обработчик клика по изображению с проигрыванием звука
    public void clickImage(View view) {

        String soundPath = sharedPreferencesUtil.getData(getApplication(), "sound_" + unicDateList.get(dayNumber));
        if(soundPath == null)
            Toast.makeText(this,soundPath,Toast.LENGTH_SHORT).show();
        else {
            //Воспроизведение аудио
        }

    }

    public void clickDateChoose(View view) {
        //Toast.makeText(this,"Выбор даты",Toast.LENGTH_SHORT).show();

        showDatePickerChooseFirstDate();

    }
}
