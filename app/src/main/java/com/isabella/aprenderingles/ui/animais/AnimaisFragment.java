package com.isabella.aprenderingles.ui.animais;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.isabella.aprenderingles.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;


public class AnimaisFragment extends Fragment {

    private AnimaisViewModel galleryViewModel;
    List<String> list = new ArrayList<String>();
    String palavra = "";
    int posicaoColunaDaPalavra = 0;
    String randomElement = "";
    String elementoAnterior = "";
    private TextToSpeech mTTS;
    private ImageView microfone;
    private TextView pIngles;
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_animais, container, false);
        Button b = (Button) view.findViewById(R.id.idBtnConhecer);
        b.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                //https://www.youtube.com/watch?v=9iTRAwCazw8&t=310s
                AssetManager am = getContext().getAssets();
                InputStream is = null;
                try {


                    is = am.open("palavras.xls");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Workbook wb = null;

                try {
                    WorkbookSettings conf = new WorkbookSettings();
                    conf.setEncoding("Cp1252"); //acentuacao
                    wb = Workbook.getWorkbook(is, conf);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BiffException e) {
                    e.printStackTrace();
                }
                Sheet s = wb.getSheet(0);
                int row = s.getRows();
                int col = s.getColumns();

                for (int i =0; i < s.getColumns(); i++){
                    Log.d("CREATION", "tot coluna: " + s.getColumns());
                    Cell pal = s.getCell(i , 0);
                    palavra = pal.getContents();

                    if (palavra.length() > 1){
                        list.add(list.size(), palavra);
                    }
                }


                Boolean repetido = true;

                while (repetido){

                    Random r = new Random();
                    int randomitem = r.nextInt(list.size());
                    randomElement = list.get(randomitem);

                    if (elementoAnterior.equals(randomElement)){
                        repetido = true;
                    }
                    else{
                        repetido = false;
                    }
                    Log.d("CREATION", "Anterior: " + elementoAnterior + " random: " + randomElement + "|" + repetido + list.size());

                    elementoAnterior = randomElement;
                }
                //Log.d("CREATION", "posicao" + randomElement + list.size());

                posicaoColunaDaPalavra = list.indexOf(randomElement);
                Log.d("CREATION", "posicao" + posicaoColunaDaPalavra);

                Cell trad = s.getCell(posicaoColunaDaPalavra , 1);
                String palavraTraduzida = trad.getContents();

               //display(randomElement, palavraTraduzida);

                pIngles = view.findViewById(R.id.idTvPalavraEmIngles);
                pIngles.setText(randomElement.toUpperCase().replaceAll("_", " "));

                TextView pPortugues = view.findViewById(R.id.idTvPalavraEmPortugues);
                pPortugues.setText(palavraTraduzida.substring(0,1).toUpperCase() + palavraTraduzida.substring(1).toLowerCase());

                Log.i("MyApp", palavraTraduzida);

                list.removeAll(list);

                ImageView iv = (ImageView) view.findViewById(R.id.idImImagem);

                String variableValue = randomElement;

                iv.setImageResource(getResources().getIdentifier(variableValue, "drawable", getContext().getPackageName()));
                //Log.i("MyApp",abc.getText().toString());

                microfone.setVisibility(view.VISIBLE);

            }
        });


      //  mButtonSpeak = view.findViewById(R.id.falar);
        microfone = view.findViewById(R.id.idIvMic);
        mTTS = new TextToSpeech(this.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                   int result = mTTS.setLanguage(Locale.ENGLISH);

                   if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                       Log.e("TTS", "Language not spported");
                    }
                   else{
                      // mButtonSpeak.setEnabled(true);
                   }

                }
                else{
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        microfone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speak();
            }
        });



       return view;
    }


    private void speak(){
        String text = pIngles.getText().toString();
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);

    }

    public void onDestroy() {
        if (mTTS != null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }



}