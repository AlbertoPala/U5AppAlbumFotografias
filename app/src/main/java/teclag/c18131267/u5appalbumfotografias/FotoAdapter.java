package teclag.c18131267.u5appalbumfotografias;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.ViewHolder> {

    //se guardara la lista de las fotos
    private List<Bitmap> lista;
    private Context context;

    public FotoAdapter(List<Bitmap> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_foto,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
// rellenar la foto, recoger de la lista la foto
        Bitmap bit = lista.get(position);
        // aqui se visualiza
        holder.image.setImageBitmap(bit);
    }

    @Override
    public int getItemCount() {
        //en el numero de los elmentos devuelve el tamaño de lalista
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.foto);
        }
    }
}
