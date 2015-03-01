package com.pain.log.painlog.negocio;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.pain.log.painlog.R;

/**
 * Created by Rulo on 01/03/2015.
 */
public class DolActivity extends BaseActivity {

    private int clave;
    private String servicio;
    private SeekBar seekBar;
    private Button btnNext;
    private TextView textDolor;
    private EditText textFecha, textNotas;
    private ActionBar actionBar;
private LinearLayout viewLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dolencia);

        cargaDatos();


        if (servicio.equals("INS")) {
            actionBar.setTitle(R.string.addLog);
        } else if (servicio.equals("UPD")) {
            actionBar.setTitle(R.string.editarTittle);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            switch (progress){
                case 0:
                    viewLay.setBackgroundColor(getResources().getColor(R.color.color_b));
                    textDolor.setText(R.string.dolor1);
                    break;
                case 1:
                    viewLay.setBackgroundColor(getResources().getColor(R.color.color_f));
                    textDolor.setText(R.string.dolor2);
                    break;
                case 2:
                    viewLay.setBackgroundColor(getResources().getColor(R.color.color_j));
                    textDolor.setText(R.string.dolor3);
                    break;


            }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    private void cargaDatos() {

        Bundle extras = getIntent().getExtras();
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        textFecha = (EditText) findViewById(R.id.textFecha);
        textNotas = (EditText) findViewById(R.id.textNotas);
        textDolor = (TextView) findViewById(R.id.textDolor);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btnNext = (Button) findViewById(R.id.btnNext);
        viewLay = (LinearLayout) findViewById(R.id.viewLay);

        clave = extras.getInt("clave");
        servicio = extras.getString("SERVICIO");




    }

}
