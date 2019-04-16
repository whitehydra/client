package com.fadeev.bgtu.client.file;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.preference.ListPreference;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class OpenFileDialog extends AlertDialog.Builder {
    private String currentPath = Environment.getExternalStorageDirectory().getPath();
    private List<File> files = new ArrayList<>();
    private TextView title;
    private ListView listView;
    private FilenameFilter filenameFilter;
    private int selectedIndex = -1;
    private OpenDialogListener openDialogListener;
    private Drawable folderIcon;
    private Drawable fileIcon;
    private String accessDeniedMessage;
    private boolean isOnlyFoldersFilter;



    public OpenFileDialog(Context context) {
        super(context);
        isOnlyFoldersFilter = false;
        title = createTitle(context);
        changeTitle();
        LinearLayout linearLayout = createMainLayout(context);
        linearLayout.addView(createBackItem(context));
        listView = createListView(context);
        linearLayout.addView(listView);
        setCustomTitle(title)
                .setView(linearLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selectedIndex > -1 && openDialogListener != null){
                            openDialogListener.OnSelectedFile(listView.getItemAtPosition(selectedIndex).toString());
                          //  File kek = listView.getItemAtPosition(0);
                        }
                        if(openDialogListener != null && isOnlyFoldersFilter){
                            openDialogListener.OnSelectedFile(currentPath);
                        }
                    }
                })
                .setNegativeButton("CANCEL",null);

    }

    @Override
    public AlertDialog show() {
        files.addAll(getFiles(currentPath));
        listView.setAdapter(new FileAdapter(getContext(),files));
        return super.show();
    }




    private int getItemHeight(Context context){
        TypedValue value = new TypedValue();
        DisplayMetrics metrics = new DisplayMetrics();
        context.getTheme().resolveAttribute(android.R.attr.rowHeight, value, true);
        getDefaultDisplay(context).getMetrics(metrics);
        return (int)TypedValue.complexToDimension(value.data, metrics);
    }


    private TextView createTitle(Context context){
        TextView textView = createTextView(context, android.R.style.TextAppearance_DeviceDefault_DialogWindowTitle);
        return textView;
    }


    private static Display getDefaultDisplay(Context context){
        return ((WindowManager) Objects.requireNonNull(context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
    }

    private static Point getScreenSize(Context context){
        Point screenSize = new Point();
        getDefaultDisplay(context).getSize(screenSize);
        return screenSize;
    }

    private static int getLinearLayoutMinHeight(Context context){
        return getScreenSize(context).y;
    }

    public int getTextWidth(String text, Paint paint){
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(),bounds);
        return bounds.left + bounds.width() + 80;
    }


    private  void changeTitle(){
        String titleText = currentPath;
        int screenWidth = getScreenSize(getContext()).x;
        int maxWidth = (int)(screenWidth * 0.99);
        if(getTextWidth(titleText, title.getPaint())>maxWidth){
            while(getTextWidth("..." + titleText, title.getPaint()) > maxWidth){
                int start = titleText.indexOf("/",2);
                if (start > 0)
                    titleText = titleText.substring(start);
                else
                    titleText = titleText.substring(2);
            }
            title.setText("..." + titleText);
        } else {
            title.setText(titleText);
        }
    }


    private  TextView createTextView (Context context, int style){
        TextView textView = new TextView(context);
        textView.setTextAppearance(context, style);
        int itemHeight = getItemHeight(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
        textView.setMinHeight(itemHeight);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(15, 0, 0,0);
        return textView;
    }

    private TextView createBackItem(Context context){
        TextView textView = createTextView(context, android.R.style.TextAppearance_DeviceDefault_Small);

        Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(),android.R.drawable.ic_menu_directions,null );
        drawable.setBounds(0, 0, 60, 60);
        textView.setCompoundDrawables(drawable, null, null, null);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(currentPath);
                File parentDirectory = file.getParentFile();
                if(parentDirectory != null){
                    currentPath = parentDirectory.getPath();
                    RebuildFiles((FileAdapter)listView.getAdapter());
                }
            }
        });
       // Drawable drawable = getContext().getResources().getDrawable(android.R.drawable.ic_menu_directions
        return  textView;
    }



    private LinearLayout createMainLayout(Context context){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setMinimumHeight(getLinearLayoutMinHeight(context));
        return linearLayout;
    }



    private void RebuildFiles(ArrayAdapter<File> adapter){

        try {
            List<File> fileList = getFiles(currentPath);
            files.clear();
            selectedIndex = -1;
            files.addAll(fileList);
            adapter.notifyDataSetChanged();
            changeTitle();
        } catch (NullPointerException e){
            String message = getContext().getResources().getString(android.R.string.unknownName);
            if(!accessDeniedMessage.equals("")) message = accessDeniedMessage;

            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public OpenFileDialog setFilter(final String filter){
        filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String fileName) {
                File tempFile = new File(String.format("%s/%s", file.getPath(),fileName));
                if (tempFile.isFile())return tempFile.getName().matches(filter);
                return true;
            }
        };
        return this;
    }

    public OpenFileDialog setOnluFoldersFilter(){
        isOnlyFoldersFilter = true;
        filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String fileName) {
                File tempFile = new File(String.format("%s/%s", file.getPath(),fileName));
                return tempFile.isDirectory();
            }
        };
        return this;
    }


    public OpenFileDialog setOpenDialogListener(OpenDialogListener listener){
        this.openDialogListener = listener;
        return this;
    }

    public OpenFileDialog setFolderIcon(Drawable drawable){
        this.folderIcon = drawable;
        return this;
    }

    public OpenFileDialog setFileIcon(Drawable drawable){
        this.fileIcon = drawable;
        return this;
    }

    public OpenFileDialog setAccessDeniedMessage(String accessDeniedMessage) {
        this.accessDeniedMessage = accessDeniedMessage;
        return this;
    }

    private ListView createListView(Context context){
        ListView listView = new ListView(context);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                final ArrayAdapter<File> adapter = (FileAdapter) adapterView.getAdapter();
                File file = adapter.getItem(index);
                if (file.isDirectory()){
                    currentPath = file.getPath();
                    RebuildFiles(adapter);
                } else {
                    if (index != selectedIndex)selectedIndex = index;
                    else selectedIndex = -1;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return listView;
    }



    private List<File> getFiles(String directoryPath){
        File directory = new File(directoryPath);
        List<File> fileList = Arrays.asList(directory.listFiles(filenameFilter));
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                if(file1.isDirectory() && file2.isFile())return -1;
                else if (file1.isFile() && file2.isDirectory()) return 1;
                else return file1.getPath().compareTo(file2.getPath());
            }
        });

        return fileList;
    }


    public interface OpenDialogListener{
        void OnSelectedFile(String fileName);
    }



    public class FileAdapter extends ArrayAdapter<File> {
        public FileAdapter(Context context, List<File> files) {
            super(context, android.R.layout.simple_list_item_1,files);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            TextView view = (TextView)super.getView(position, convertView, parent);
            File file = getItem(position);

            view.setText(file.getName());

            if(file.isDirectory()){
                setDrawable(view, folderIcon);
            } else {
                setDrawable(view, fileIcon);
                if(selectedIndex == position)
                    view.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
                else
                    view.setBackgroundColor(getContext().getResources().getColor(android.R.color.background_light));
            }
            return view;
        }


        private void setDrawable(TextView view, Drawable drawable){
            if(view != null){
                if(drawable != null){
                    drawable.setBounds(0,0,60,60);
                    view.setCompoundDrawables(drawable, null, null, null);
                } else {
                    view.setCompoundDrawables(null, null, null, null);
                }
            }
        }
    }
}
