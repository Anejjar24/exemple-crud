package ma.ensaj.gestioncompteksoap2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ma.ensaj.gestioncompteksoap2.R;
import ma.ensaj.gestioncompteksoap2.adapters.CompteAdapter;
import ma.ensaj.gestioncompteksoap2.beans.Compte;
import ma.ensaj.gestioncompteksoap2.webservices.SoapWebService;

public class MainActivity extends AppCompatActivity  {
    private RecyclerView recyclerView;
    private CompteAdapter compteAdapter;

    private FloatingActionButton fabAddCompte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialiser l'adapter avec une liste vide
        compteAdapter = new CompteAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(compteAdapter);
        fabAddCompte = findViewById(R.id.addCompte);





        fabAddCompte.setOnClickListener(v -> showAddCompteDialog());

        // Fetch initial list of comptes
        loadComptes();
    }

    private void loadComptes() {
        new Thread(() -> {
            List<Compte> comptes = new SoapWebService().getComptes();

            runOnUiThread(() -> {
                compteAdapter.updateComptes(comptes);
            });
        }).start();
    }


    // Remplace AddCompteTask
    private void addCompte(double solde, String type) {
        new Thread(() -> {
            boolean ok = new SoapWebService().createCompte(solde,type);
            runOnUiThread(() -> {
                if (ok) {
                    Toast.makeText(MainActivity.this,
                            "Account added successfully",
                            Toast.LENGTH_SHORT).show();
                    loadComptes();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Failed to add account",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }


    public void updateCompte(Long id, double solde, String type) {
        new Thread(() -> {
            boolean ok = new SoapWebService().updateCompte(id,solde,type);
            runOnUiThread(() -> {
                if (ok) {
                    Toast.makeText(MainActivity.this,
                            "Account updated successfully",
                            Toast.LENGTH_SHORT).show();
                    loadComptes();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Failed to update account",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

    }



    // Remplace DeleteCompteTask
    public void deleteCompte(Long id) {
       new Thread(
               ()->{

                       boolean ok = new SoapWebService().deleteCompte(id);
                   runOnUiThread(() -> {
                   if (ok) {
                       Toast.makeText(MainActivity.this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
                       loadComptes();
                   } else {
                       Toast.makeText(MainActivity.this, "Error lors de la suppression", Toast.LENGTH_SHORT).show();
                   }
                   });
               }).start();
    }

    // Méthodes de l'interface OnCompteActionListener




    public void showAddCompteDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);
        TextInputEditText editSolde = dialogView.findViewById(R.id.solde);
        RadioButton radioCourant = dialogView.findViewById(R.id.radioCourant);
        RadioButton radioEpargne = dialogView.findViewById(R.id.radioEpargne);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Ajouter un Compte")
                .setView(dialogView)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    double solde = Double.parseDouble(editSolde.getText().toString().trim());


                    if (!radioCourant.isChecked() && !radioEpargne.isChecked()) {
                        Toast.makeText(this, "Veuillez sélectionner un type de compte", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String type = radioCourant.isChecked() ? "COURANT" : "EPARGNE";

                    // TO DO
                    addCompte(solde, type);
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }



    public void showUpdateCompteDialog(Compte compte) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);
        TextInputEditText editSolde = dialogView.findViewById(R.id.solde);
        RadioButton radioCourant = dialogView.findViewById(R.id.radioCourant);
        RadioButton radioEpargne = dialogView.findViewById(R.id.radioEpargne);

        // Pre-fill existing data
        editSolde.setText(String.valueOf(compte.getSolde()));
        if (compte.getType().equals("COURANT")) {
            radioCourant.setChecked(true);
        } else {
            radioEpargne.setChecked(true);
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("Modifier un Compte")
                .setView(dialogView)
                .setPositiveButton("Modifier", (dialog, which) -> {

                    double solde = Double.parseDouble(editSolde.getText().toString().trim());


                    String type = radioCourant.isChecked() ? "COURANT" : "EPARGNE";
                    updateCompte(compte.getId(), solde, type);
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }


}