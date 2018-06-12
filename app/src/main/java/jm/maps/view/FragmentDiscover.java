package jm.maps.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import jm.maps.R;


public class FragmentDiscover extends Fragment {

    Context context;
    private LinearLayout mainTable;
    List<Integer> animalsList;

    static final String[] EXTENSIONS = new String[]{
            "jpg", "png", "bmp"
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        mainTable = view.findViewById(R.id.TableRow02);
        context = mainTable.getContext();

        loadAnimals();



        for(int i = 0 ; i < 6 ; i++){
            ImageView newImage = new ImageView(getActivity());

            Bitmap originalBmp = BitmapFactory.decodeResource(context.getResources(), animalsList.get(0));
            int imageSize = originalBmp.getWidth() > originalBmp.getWidth() ? originalBmp.getWidth() : originalBmp.getWidth();
            Bitmap croppedBmp = Bitmap.createBitmap(originalBmp, 0, 0, imageSize, imageSize);


            newImage.setImageBitmap(croppedBmp);
            newImage.setMaxHeight(250);
            newImage.setMaxWidth(250);
            newImage.layout(150,150,150,150);
            newImage.setAdjustViewBounds(true);
            mainTable.addView(newImage);
        }


        return view;
    }

    private void loadAnimals() {
        File dir = new File("res");
        File[] filelist = dir.listFiles(IMAGE_FILTER);
        animalsList = new ArrayList();

        for (File f : filelist) { // do your stuff here }
            //animalsList.add(Drawable.createFromPath(f.getPath()));
        }
    }

        static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                for (final String ext : EXTENSIONS) {
                    if (name.endsWith("." + ext)) {
                        return (true);
                    }
                }
                return (false);
            }
        };


    private TableRow createRow(int y) {
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);

        for (int x = 0; x < 5; x++) {
            row.addView(createImageButton(x, y));
        }
        return row;
    }

    private View createImageButton(int x, int y) {
        Button button = new Button(context);
        //button.setBackgroundDrawable(backImage);
        button.setId(100 * x + y);
        //button.setOnClickListener(buttonListener);
        return button;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}