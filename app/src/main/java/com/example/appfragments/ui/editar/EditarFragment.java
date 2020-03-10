package com.example.appfragments.ui.editar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.appfragments.R;
import com.example.appfragments.SQL.SQLite;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditarFragment extends Fragment {

    private EditarViewModel editarViewModel;

    private int dia, mes, anio;
    private static final int REQUEST_TAKE_PHOTO=1;
    private Spinner sp_area, sp_doctor, sp_sexo;
    private EditText et_idPaciente, et_id_buscarPaciente, et_nombre, et_fecha, et_edad, et_estatura, et_peso;
    private String strArea, strDoctor, strSexo, pathImage = "", currentPhotoPath="";
    private Button btn_buscar, btn_modificar, btn_limpiar, btn_fecha;
    private ImageView iv_foto;
    Uri photoURI;
    SQLite sqLite;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editarViewModel =
                ViewModelProviders.of(this).get(EditarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_editar, container, false);

        // Enlazamos interfaz con el modelo
        sp_area = root.findViewById(R.id.editar_sp_areaDeIngreso);
        sp_doctor = root.findViewById(R.id.editar_sp_doctorCrear);
        sp_sexo = root.findViewById(R.id.editar_sp_sexo);

        et_id_buscarPaciente = root.findViewById(R.id.editar_et_id_buscarPaciente);
        et_idPaciente = root.findViewById(R.id.editar_et_idPaciente);
        et_nombre = root.findViewById(R.id.editar_et_nombrePaciente);
        et_fecha = root.findViewById(R.id.editar_et_fechaIngreso);
        et_edad = root.findViewById(R.id.editar_et_edadPaciente);
        et_estatura = root.findViewById(R.id.editar_et_estaturaPaciente);
        et_peso = root.findViewById(R.id.editar_et_pesoPaciente);

        btn_fecha = root.findViewById(R.id.editar_btn_fechaIngreso);
        btn_limpiar = root.findViewById(R.id.editar_btn_limpiar);
        btn_modificar = root.findViewById(R.id.editar_btn_modificar);
        btn_buscar = root.findViewById(R.id.editar_btn_buscar);
        iv_foto = root.findViewById(R.id.editar_iv_foto);

        sp_area.setVisibility(View.INVISIBLE);
        sp_doctor.setVisibility(View.INVISIBLE);
        et_nombre.setVisibility(View.INVISIBLE);
        sp_sexo.setVisibility(View.INVISIBLE);
        et_peso.setVisibility(View.INVISIBLE);
        et_fecha.setVisibility(View.INVISIBLE);
        et_edad.setVisibility(View.INVISIBLE);
        et_estatura.setVisibility(View.INVISIBLE);
        iv_foto.setVisibility(View.GONE);
        btn_modificar.setVisibility(View.INVISIBLE);
        btn_fecha.setVisibility(View.INVISIBLE);

        // Conexión con base de datos
        sqLite = new SQLite(getContext());
        sqLite.abrir();

        configureSpinnerAreaDoctor();
        configureSpinnerSexo();
        configureFecha();

        configureTomarFoto();
        configureBtnModificar();
        configureBtnLimpiar();
        configureBtnBuscar();

        return root;
    }

    private void configureSpinnerAreaDoctor(){

        final ArrayAdapter<CharSequence> areaAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.opciones,
                android.R.layout.simple_spinner_item
        );
        // Preparando los spinner del área y doctor. El spinner del doctor depende del área que seleccionó el usuario
        sp_area.setAdapter(areaAdapter);
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String opcion = String.valueOf(sp_area.getSelectedItemId());
                int op = Integer.parseInt(opcion);
                System.out.println(opcion);
                if (op==0){
                }else if (op ==1){
                    final ArrayAdapter<CharSequence> doctorAdapter =
                            ArrayAdapter.createFromResource(
                                    getContext(),
                                    R.array.o1,
                                    android.R.layout.simple_spinner_item);

                    sp_doctor.setAdapter(doctorAdapter);
                    strArea = areaAdapter.getItem(1).toString();
                    sp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String opcion = String.valueOf(sp_doctor.getSelectedItemId());
                            int op = Integer.parseInt(opcion);
                            System.out.println(opcion);
                            if (op==0){
                            }else if (op==1){
                                strDoctor=doctorAdapter.getItem(1).toString();
                            }else if(op==2){
                                strDoctor=doctorAdapter.getItem(2).toString();
                            }else if(op==3){
                                strDoctor=doctorAdapter.getItem(3).toString();
                            }else if(op==4){
                                strDoctor=doctorAdapter.getItem(4).toString();
                            }
                            if(op!=0) Toast.makeText(getContext(),strArea+" "+strDoctor, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }//Primera selección
                else if (op ==2){
                    final ArrayAdapter<CharSequence> doctorAdapter =
                            ArrayAdapter.createFromResource(
                                    getContext(),
                                    R.array.o2, //Aereos
                                    android.R.layout.simple_spinner_item);
                    sp_doctor.setAdapter(doctorAdapter);
                    strArea = areaAdapter.getItem(2).toString(); //Clasificacion 2 - Aereos
                    sp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String opcion = String.valueOf(sp_doctor.getSelectedItemId());
                            int op = Integer.parseInt(opcion);
                            System.out.println(opcion);
                            if (op==0){
                            }else if (op==1){
                                strDoctor=doctorAdapter.getItem(1).toString();
                            }else if(op==2){
                                strDoctor=doctorAdapter.getItem(2).toString();
                            }else if(op==3){
                                strDoctor=doctorAdapter.getItem(3).toString();
                            }else if(op==4){
                                strDoctor=doctorAdapter.getItem(4).toString();
                            }
                            if(op!=0) Toast.makeText(getContext(),strArea+" "+strDoctor, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }//Segunda selección
                else if (op ==3){
                    final ArrayAdapter<CharSequence> doctorAdapter =
                            ArrayAdapter.createFromResource(
                                    getContext(),
                                    R.array.o3, //Terrestres
                                    android.R.layout.simple_spinner_item);

                    sp_doctor.setAdapter(doctorAdapter);
                    strArea = areaAdapter.getItem(3).toString(); //Clasificacion 3 - Terrestres
                    sp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String opcion = String.valueOf(sp_doctor.getSelectedItemId());
                            int op = Integer.parseInt(opcion);
                            System.out.println(opcion);
                            if (op==0){
                            }else if (op==1){
                                strDoctor=doctorAdapter.getItem(1).toString();
                            }else if(op==2){
                                strDoctor=doctorAdapter.getItem(2).toString();
                            }else if(op==3){
                                strDoctor=doctorAdapter.getItem(3).toString();
                            }else if(op==4){
                                strDoctor=doctorAdapter.getItem(4).toString();
                            }
                            if(op!=0) Toast.makeText(getContext(),strArea+" "+strDoctor, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }//Tercera selección
                else if (op ==4){
                    final ArrayAdapter<CharSequence> doctorAdapter =
                            ArrayAdapter.createFromResource(
                                    getContext(),
                                    R.array.o4, //Acuaticos
                                    android.R.layout.simple_spinner_item);

                    sp_doctor.setAdapter(doctorAdapter);
                    strArea = areaAdapter.getItem(4).toString(); //Clasificacion 4 - Acuaticos
                    sp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String opcion = String.valueOf(sp_doctor.getSelectedItemId());
                            int op = Integer.parseInt(opcion);
                            System.out.println(opcion);
                            if (op==0){
                            }else if (op==1){
                                strDoctor=doctorAdapter.getItem(1).toString();
                            }else if(op==2){
                                strDoctor=doctorAdapter.getItem(2).toString();
                            }else if(op==3){
                                strDoctor=doctorAdapter.getItem(3).toString();
                            }else if(op==4){
                                strDoctor=doctorAdapter.getItem(4).toString();
                            }
                            if(op!=0) Toast.makeText(getContext(),strArea+" "+strDoctor, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }//Cuarta selección
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void configureSpinnerSexo() {
        //Spinner de SEXO
        final ArrayAdapter<CharSequence> sexoAdapter =
                ArrayAdapter.createFromResource(
                        getContext(),
                        R.array.sx,
                        android.R.layout.simple_spinner_item);
        sp_sexo.setAdapter(sexoAdapter);
        sp_sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String opcion = String.valueOf(sp_sexo.getSelectedItemId());
                int op = Integer.parseInt(opcion);
                System.out.println(opcion);
                if (op == 0){
                }else if (op == 1){
                    strSexo = "MASCULINO";
                }else if(op==2){
                    strSexo = "FEMENINO";
                }
            }
            //Fin spinner sexo
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void configureFecha(){
        // Configurando la fecha - datepicker
        btn_fecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final Calendar c= Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear , int dayOfMonth) {
                        //     sFecha=Integer.toString(2019-year);
                        et_fecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });
    }

    private void configureTomarFoto(){
        //Tomar fotografia
        iv_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tomarfoto=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Se comprueba que se encontro una actividad para genera la foto
                if(tomarfoto.resolveActivity(getActivity().getPackageManager())!=null){
                    //se crea el archivo donde se guardara la imagen
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex){
                        Toast.makeText(getContext(), "Ocurrio un error mientras se generaba el archivo", Toast.LENGTH_SHORT).show();
                    }
                    //se comprueba que la imagen fue creada correctamente
                    if(photoFile != null){
                        photoURI = FileProvider.getUriForFile(getContext(),"com.example.appfragments",photoFile);
                        tomarfoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(tomarfoto,REQUEST_TAKE_PHOTO);
                    }
                }
            }
        });
    }

    //Crear archivo de imagen
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void configureBtnModificar(){

        //Modificar registro
        btn_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !et_idPaciente.getText().toString().equals("") &&
                        !et_nombre.getText().toString().equals("") &&
                        !et_edad.getText().toString().equals("") &&
                        !et_estatura.getText().toString().equals("") &&
                        !et_peso.getText().toString().equals("") &&
                        !et_fecha.getText().toString().equals("") &&
                        !sp_area.equals("") &&
                        sp_doctor.getSelectedItemPosition()!=0 &&
                        !strSexo.equals("") &&
                        !pathImage.equals("")) {
                    //dentro de if
                    Toast.makeText(getContext(), strArea + " " + strDoctor + " " +
                            et_nombre.getText().toString().toUpperCase() + " " +
                            strSexo + " " +
                            et_edad.getText().toString().toUpperCase() + " " +
                            et_peso.getText().toString().toUpperCase() + " " +
                            et_estatura.getText().toString().toUpperCase() + " " +
                            et_fecha.getText().toString(), Toast.LENGTH_SHORT).show();
                    sqLite.updatePaciente(
                            Integer.parseInt(et_idPaciente.getText().toString()),
                            strArea,
                            strDoctor,
                            et_nombre.getText().toString().toUpperCase(),
                            strSexo,
                            et_fecha.getText().toString(),
                            et_edad.getText().toString().toUpperCase(),
                            et_estatura.getText().toString().toUpperCase(),
                            et_peso.getText().toString().toUpperCase(),
                            pathImage
                    );
                    //Dentro if agregar registro
                    Toast.makeText(getContext(), "REGISTRO AÑADIDO", Toast.LENGTH_SHORT).show();
                    limpiarCampos();

                }else {
                    Toast.makeText(getContext(),
                            "Error: no puede haber campos vacios",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void configureBtnLimpiar(){
        btn_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });
    }

    private void limpiarCampos(){
        //Limpiar campos
        et_idPaciente.setText("");
        et_nombre.setText("");
        et_fecha.setText("");
        et_edad.setText("");
        et_estatura.setText("");
        et_peso.setText("");
        sp_area.setId(0);
        sp_doctor.setId(0);
        sp_sexo.setId(0);
    }

    private void configureBtnBuscar(){
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_id_buscarPaciente.getText().toString().equals("")) {
                    if (sqLite.getCant(Integer.parseInt(et_id_buscarPaciente.getText().toString())).getCount() == 1) {
                        sp_area.setVisibility(View.VISIBLE);
                        sp_doctor.setVisibility(View.VISIBLE);
                        et_nombre.setVisibility(View.VISIBLE);
                        sp_sexo.setVisibility(View.VISIBLE);
                        et_fecha.setVisibility(View.VISIBLE);
                        et_edad.setVisibility(View.VISIBLE);
                        et_estatura.setVisibility(View.VISIBLE);
                        et_peso.setVisibility(View.VISIBLE);
                        iv_foto.setVisibility(View.VISIBLE);

                        btn_modificar.setVisibility(View.VISIBLE);
                        et_fecha.setVisibility(View.VISIBLE);
                        btn_fecha.setVisibility(View.VISIBLE);

                        int f = Integer.parseInt(et_id_buscarPaciente.getText().toString());
                        Cursor cursor = sqLite.getCant(f);
                        String g1 = null, g2 = null, g3 = null, g4 = null, g5 = null, g6 = null, g7 = null, g8 = null, g9 = null;
                        if (cursor.moveToFirst()) {
                            do {
                                g1 = cursor.getString(1);
                                g2 = cursor.getString(2);
                                g3 = cursor.getString(3);
                                g4 = cursor.getString(4);
                                g5 = cursor.getString(5);
                                g6 = cursor.getString(6);
                                g7 = cursor.getString(7);
                                g8 = cursor.getString(8);
                                g9 = cursor.getString(9);
                            } while (cursor.moveToNext());
                        }
                        et_idPaciente.setText(et_id_buscarPaciente.getText().toString());
                        et_nombre.setText(g3.toString());
                        et_fecha.setText(g5.toString());
                        et_edad.setText(g6.toString());
                        et_estatura.setText(g7.toString());
                        et_peso.setText(g8.toString());
                        pathImage=g9;
                        cargarImagen();
                        int posArea=buscarPosicion(R.array.opciones,g1);
                        if(posArea!=-1){
                            sp_area.setSelection(posArea);
                        }
                        System.out.println(g4);
                        int posSexo= buscarPosicion(R.array.sx,g4);
                        System.out.println(posSexo);
                        if(posSexo!=-1) {
                            sp_sexo.setSelection(posSexo);
                        }
                    } else
                        Toast.makeText(getContext(), "Error: No existe ese ID" +
                                "", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error: No has puesto un ID" +
                            "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //cargar imagen
    public void cargarImagen(){
        try{
            File filePhoto=new File(pathImage);
            photoURI = FileProvider.getUriForFile(getContext(),"com.example.appfragments",filePhoto);
            iv_foto.setImageURI(photoURI);
        }catch (Exception ex){
            Toast.makeText(getContext(), "Ocurrio un error al cargar la imagen", Toast.LENGTH_SHORT).show();
            Log.d("Cargar Imagen","Error al cargar imagen "+pathImage+"\nMensaje: "+ex.getMessage()+"\nCausa: "+ex.getCause());
            pathImage="";
        }
    }

    //buscar elemento en arreglo
    public int buscarPosicion(int arreglo,String elemento){
        int posicion=-1;
        elemento = elemento.toUpperCase();
        String tem;
        String elementos[]=getResources().getStringArray(arreglo);
        for (int i=0; i<elementos.length;i++){
            tem=elementos[i].toUpperCase();
            if(tem.equals(elemento)){
                posicion=i;
                break;
            }
        }
        return posicion;
    }

}