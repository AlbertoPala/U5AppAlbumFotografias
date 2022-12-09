package teclag.c18131267.u5appalbumfotografias;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import teclag.c18131267.u5appalbumfotografias.databinding.FragmentImagenesBinding;

public class ImagenesFragment extends Fragment {

    private FragmentImagenesBinding binding;
    private RecyclerView listaFotos;
    private FotoAdapter adapter;

    private List<Bitmap> fotos = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        binding = FragmentImagenesBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        cargarFotos();

        listaFotos=binding.listaFotos;
        adapter=new FotoAdapter(fotos, getContext());

        listaFotos.setLayoutManager(new GridLayoutManager(getContext(),4));
        listaFotos.setAdapter(adapter);

        FloatingActionButton btn=binding.fotografiar;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hacerFoto();
            }
        });
        return root;
    }

    private File archivo;
    public void hacerFoto() {
        try {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            archivo = crearFichero();
            Uri foto = FileProvider.getUriForFile(getContext(),"com.able.u5appalbumfotografias.fileprovider",archivo );
            i.putExtra(MediaStore.EXTRA_OUTPUT,foto);
            startActivityForResult(i, 1);
        }
    catch (IOException e){
        Toast.makeText(getContext(),"Error" +e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public void cargarFotos(){
        File directorio = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] fichero = directorio.listFiles();
        for (File i: fichero){
            if(i.getName().endsWith(".jpg")) {
                Bitmap image = BitmapFactory.decodeFile(i.getAbsolutePath());
                fotos.add(image);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
      if(requestCode==1){
          if(resultCode== Activity.RESULT_OK){
              Bitmap image = BitmapFactory.decodeFile(archivo.getAbsolutePath());
              fotos.add(image);
              adapter.notifyDataSetChanged();
          }else{
              archivo.delete();
          }
      }else{
          super.onActivityResult(requestCode,resultCode,data);
      }
    }

    private File crearFichero() throws IOException {
        String pre="foto_";
        File directorio = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(pre+ UUID.randomUUID().toString(),"jpg",directorio);
        return img;
    }

    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
