package ma.ensaj.gestioncompteksoap2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.ksoap2.serialization.SoapObject;
import java.util.ArrayList;
import java.util.List;

import ma.ensaj.gestioncompteksoap2.MainActivity;
import ma.ensaj.gestioncompteksoap2.R;
import ma.ensaj.gestioncompteksoap2.beans.Compte;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {
    private List<Compte> comptesList;
   private Context context;


    public CompteAdapter(List<Compte> comptesList, Context context) {
        this.comptesList = comptesList;
        this.context = context;
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptesList.get(position);

        holder.textNumero.setText(String.valueOf(compte.getId()));
        holder.textSolde.setText(String.format("%s €", compte.getSolde()));
        holder.textType.setText(compte.getType());
        holder.textDate.setText(compte.getDateCreation());
        // ajouter date

        holder.buttonUpdate.setOnClickListener(v ->{
                    compte.setSolde(compte.getSolde());
                    compte.setType(compte.getType());
                ((MainActivity)context).showUpdateCompteDialog(compte);
                }

               );

        holder.buttonDelete.setOnClickListener(v ->{
                    ((MainActivity)context).deleteCompte(compte.getId());
                }
              );
    }

    @Override
    public int getItemCount() {

        return comptesList != null ? comptesList.size() : 0;
    }


    static class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView textNumero, textSolde, textType,textDate;
        ImageButton buttonUpdate, buttonDelete;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);

            textNumero = itemView.findViewById(R.id.textNumero);
            textSolde = itemView.findViewById(R.id.textSolde);
            textType = itemView.findViewById(R.id.textType);
            textDate = itemView.findViewById(R.id.textDate);

            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
    public void updateComptes(List<Compte> newComptes) {
        this.comptesList = newComptes != null ? newComptes : new ArrayList<>();
        notifyDataSetChanged();
    }
}