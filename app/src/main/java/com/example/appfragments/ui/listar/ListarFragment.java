package com.example.appfragments.ui.listar;

import android.app.AlertDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.appfragments.model.Paciente;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListarFragment extends Fragment {

    private ListarViewModel listarViewModel;
    ArrayList<String> reg;
    ArrayList<String> imagenes;
    SQLite sqlite;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listarViewModel =
                ViewModelProviders.of(this).get(ListarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listar, container, false);

        ListView lv_listaPacientes= root.findViewById(R.id.listar_lv_listaPacientes) ;

        //base de datos
        sqlite = new SQLite(getContext());
        sqlite.abrir();
        Cursor cursor = sqlite.getRegistro();

        //List<Paciente> pacientes = getPacientes(cursor);

        reg = sqlite.getPaciente(cursor);
        imagenes = sqlite.getImagenes(cursor);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,reg);
        lv_listaPacientes.setAdapter(adaptador);
        lv_listaPacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_paciente,null);
                ((TextView)dialogView.findViewById(R.id.dialog_paciente_tv_datos)).setText(reg.get(i));
                ImageView iVImagen=dialogView.findViewById(R.id.dialog_paciente_iv_picture);
                cargarImagen(imagenes.get(i),iVImagen);
                AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
                dialogo.setTitle("Paciente");
                dialogo.setView(dialogView);
                dialogo.setPositiveButton("Aceptar",null);
                dialogo.show();

            }
        });
        return root;
    }

    private List<Paciente> getPacientes(Cursor cursor){
        List<Paciente> listData = new ArrayList<>();
        Paciente paciente = new Paciente();
        int index;
        while(cursor.moveToNext()) {

            index = cursor.getColumnIndexOrThrow("ID_PACIENTE");
            paciente.setId(cursor.getLong(index));

            index = cursor.getColumnIndexOrThrow("NOMBRE");
            paciente.setNombre(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("SEXO");
            paciente.setSexo(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("EDAD");
            paciente.setEdad(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("ESTATURA");
            paciente.setEstatura(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("PESO");
            paciente.setPeso(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("AREA");
            paciente.setArea(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("DOCTOR");
            paciente.setDoctor(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("FECHA_ING");
            paciente.setFecha_ingreso(cursor.getString(index));

            System.out.println(paciente);
            listData.add(paciente);

            paciente = new Paciente();
        }
        return listData;
    }

    //cargar imagen
    public void cargarImagen(String imagen, ImageView iv){
        try{
            File filePhoto=new File(imagen);
            Uri uriPhoto = FileProvider.getUriForFile(getContext(),"com.example.appfragments",filePhoto);
            iv.setImageURI(uriPhoto);
        }catch (Exception ex){
            Toast.makeText(getContext(), "Ocurrio un error al cargar la imagen", Toast.LENGTH_SHORT).show();
            Log.d("Cargar Imagen","Error al cargar imagen: "+imagen+"\nMensaje: "+ex.getMessage()+"\nCausa: "+ex.getCause());
        }
    }
}