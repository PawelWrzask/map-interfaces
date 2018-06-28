package jm.maps.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jm.maps.R;


public class FragmentDiscover extends Fragment {

    Context context;
    private TableLayout mainTable;
    private static List<Integer> animalsList;
    private static List<ImageView> images;
    private static List<ImageView> undiscoveredImages;

    private static boolean initialized = false;

    private int rows = 6;
    private int columns = 12;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        mainTable = view.findViewById(R.id.TableRow02);
        if(!initialized){
            undiscoveredImages = new ArrayList<>();
            images = new ArrayList<>();
            initialized = true;
            initialize();
        }else{
            for(int i = 0 ; i < rows ; i++){
                TableRow tableRow = new TableRow(getActivity());
                for(int j = 0 ; j < columns ; j++){
                    ImageView image = images.get(i*columns+j);
                    ((ViewGroup)image.getParent()).removeView(image);
                    tableRow.addView(image);
                }
                mainTable.addView(tableRow);
            }
        }
        return view;
    }
    private void initialize(){
        context = mainTable.getContext();

        loadAnimals();


        for(int i = 0 ; i < rows ; i++){
            TableRow tableRow = new TableRow(getActivity());
            for(int j = 0 ; j < columns ; j++){
                ImageView newImage = new ImageView(getActivity());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                options.inJustDecodeBounds = false;

                Bitmap originalBmp = BitmapFactory.decodeResource(context.getResources(), animalsList.get(i*columns+j), options);


                int imageSize = originalBmp.getWidth() > originalBmp.getHeight() ? originalBmp.getHeight() : originalBmp.getWidth();
                Bitmap croppedBmp = Bitmap.createBitmap(originalBmp, 0, 0, imageSize, imageSize);


                newImage.setImageBitmap(croppedBmp);
                newImage.setMaxHeight(250);
                newImage.setMaxWidth(250);
                newImage.setAdjustViewBounds(true);
                newImage.setVisibility(View.INVISIBLE);
                undiscoveredImages.add(newImage);
                images.add(newImage);
                tableRow.addView(newImage);
            }
            mainTable.addView(tableRow);
        }

        for(int i = 0 ; i< 10 ; i++){
            discoverRandomImage();
        }
    }

    private void loadAnimals(){
        animalsList = new ArrayList<>();

        animalsList.add(R.drawable.barczatka_kataks);
        animalsList.add(R.drawable.bocian_bialy);
        animalsList.add(R.drawable.bogatek_wspanialy);
        animalsList.add(R.drawable.czerwonczyk_fioletek);
        animalsList.add(R.drawable.dymowka);
        animalsList.add(R.drawable.dzierzba_rudoglowa);
        animalsList.add(R.drawable.foka_pospolita);
        animalsList.add(R.drawable.gagol);
        animalsList.add(R.drawable.ggniewosz_plamisty);
        animalsList.add(R.drawable.gluszec_zwyczajny);
        animalsList.add(R.drawable.gorowka_sudecka);
        animalsList.add(R.drawable.iglica_mala);
        animalsList.add(R.drawable.iglicznia);
        animalsList.add(R.drawable.jaszczurka_zielona);
        animalsList.add(R.drawable.karliczka_zwyczajna);
        animalsList.add(R.drawable.kozica_polnocna);
        animalsList.add(R.drawable.krakwa);
        animalsList.add(R.drawable.kraska_zwyczajna);
        animalsList.add(R.drawable.krasopani_hera);
        animalsList.add(R.drawable.kreslinek_nizinny);
        animalsList.add(R.drawable.kukulka_czubata);
        animalsList.add(R.drawable.modliszka_zwyczajna);
        animalsList.add(R.drawable.modraszek_arion);
        animalsList.add(R.drawable.modraszek_eros);
        animalsList.add(R.drawable.modraszek_gniady);
        animalsList.add(R.drawable.modraszek_nausitous);
        animalsList.add(R.drawable.modraszek_telejus);
        animalsList.add(R.drawable.nerpa_obraczkowana);
        animalsList.add(R.drawable.niedzwiedz_brunatny);
        animalsList.add(R.drawable.niepylak_apollo);
        animalsList.add(R.drawable.niepylak_mnemozyna);
        animalsList.add(R.drawable.nocek_brandta);
        animalsList.add(R.drawable.nocek_duzy);
        animalsList.add(R.drawable.norka_europejska);
        animalsList.add(R.drawable.nur_czarnoszyi);
        animalsList.add(R.drawable.nurzyk_polarny);
        animalsList.add(R.drawable.plaskonos_zwyczajny);
        animalsList.add(R.drawable.pliszka_siwa);
        animalsList.add(R.drawable.plywak_szerokobrzezek);
        animalsList.add(R.drawable.podkowiec_duzy);
        animalsList.add(R.drawable.przeplatka_aurinia);
        animalsList.add(R.drawable.przeplatka_maturna);
        animalsList.add(R.drawable.rys_euroazjatycki);
        animalsList.add(R.drawable.rzekotka_drzewna);
        animalsList.add(R.drawable.sep_plowy);
        animalsList.add(R.drawable.sikora_lazurowa);
        animalsList.add(R.drawable.slepowron_zwyczajny);
        animalsList.add(R.drawable.smuzka_lesna);
        animalsList.add(R.drawable.smuzka_stepowa);
        animalsList.add(R.drawable.strzepotek_edypus);
        animalsList.add(R.drawable.strzepotek_hero);
        animalsList.add(R.drawable.susel_moregowany);
        animalsList.add(R.drawable.susel_perelkowany);
        animalsList.add(R.drawable.swistak_tatrzanski);
        animalsList.add(R.drawable.swistun_zwyczajny);
        animalsList.add(R.drawable.swistun_zwyczajny);
        animalsList.add(R.drawable.szarytka_morska);
        animalsList.add(R.drawable.tecznik_ziarenkowaty);
        animalsList.add(R.drawable.traszka_grzebieniasta);
        animalsList.add(R.drawable.traszka_karpacka);
        animalsList.add(R.drawable.trzepla_zielona);
        animalsList.add(R.drawable.warzecha_zwyczajna);
        animalsList.add(R.drawable.wasatka);
        animalsList.add(R.drawable.wilk_szary);
        animalsList.add(R.drawable.wwaz_eskulapa);
        animalsList.add(R.drawable.zaba_moczarowa);
        animalsList.add(R.drawable.zadrzechnia_czarnoroga);
        animalsList.add(R.drawable.zagnica_zielona);
        animalsList.add(R.drawable.zajac_bielak);
        animalsList.add(R.drawable.zalotka_bialoczelna);
        animalsList.add(R.drawable.zbik_europejski);
        animalsList.add(R.drawable.zimorodek_zwyczajny);
        animalsList.add(R.drawable.zolednica_europejska);
        animalsList.add(R.drawable.zolna_zwyczajna);
        animalsList.add(R.drawable.zolw_blotny);
        animalsList.add(R.drawable.zubr_europejski);
    }

    public void discoverRandomImage(){
        if(!initialized) return;
        if(undiscoveredImages.size()<1) return;

        Random random = new Random();
        int index = random.nextInt(undiscoveredImages.size());
        ImageView image = undiscoveredImages.get(index);
        undiscoveredImages.remove(index);
        image.setVisibility(View.VISIBLE);
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