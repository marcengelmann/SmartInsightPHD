package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


/**
 * Pofil klasse zum hochladen eine Profilbildes
 * Anzeigen das push nachrichten akitviert sind
 * Und Name plus mail adresse
 */
public class ProfilFragment extends Fragment {
    private TextView txtName;
    private TextView txtEmail;
    private ImageButton btnUploadFoto;
    ImageView profileImage;
    UserLocalStore userLocalStore;
    private OnListFragmentInteractionListener mListener;
    public ProfilFragment() {
        // Required empty public constructor
    }

    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // holt die aktuellen gespeicherten User daten
        userLocalStore = new UserLocalStore(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        txtEmail = (TextView) view.findViewById(R.id.profileEmail);
        txtName = (TextView) view.findViewById(R.id.profileName);
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        // Button zum Bearbeiten des Profilbildes und soll die methode onClickUploadFoto aufrufen
        btnUploadFoto = (ImageButton) view.findViewById(R.id.btnUploadFoto);
        btnUploadFoto.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                onClickUploadFoto(v);

            }

        });
        //hol die aktuel user daten um email und name in das profil reinzuschreiben
        final User user = userLocalStore.getUserLogInUser();
        txtEmail.setText(user.getEmail());
        txtName.setText(user.getName());
        // methode um das aktuelle profilbild zu laden
        updateProfilPic();
        return view;
    }

    public void onClickUploadFoto(View view){
        final CharSequence[] options = { "Foto aufnehmen", "Foto aus Gallerie auswählen","Abbrechen" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Neues Profilbild hochladen!");
        // Es soll über die Kamera ein Bild gemacht werden können oder eiens aus der
        // Galerie geladen werden
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Foto aufnehmen"))
                {
                    // Camera aufnahmen
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Foto aus Gallerie auswählen"))
                {
                    // Bild über Galerie holen
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Abbrechen")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    // Methode wir aufgerufen, wenn ein Bild in der GAllerie oder über die Kamera ausgewähl/
    // gemacht wrude
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            userLocalStore.setUserStatusProfilPic(false);
            if (requestCode == 1) {
                // Kamera bild umwandlen und in UserLocalStore speicehrn
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        Uri uri = Uri.fromFile(f);
                        userLocalStore.setUserProfilPic(uri);
                        userLocalStore.setUserStatusProfilPic(true);
                        break;
                    }
                }
            } else if (requestCode == 2) {
                // Gallerie bild umwandlen und in UserLocalStore speicehrn
                Uri selectedImage = data.getData();
                userLocalStore.setUserProfilPic(selectedImage);
                userLocalStore.setUserStatusProfilPic(true);
            }

        }
        // Profilbild neu alden
        updateProfilPic();

    }
    //Diese Methode updatet das Profilbild auf der Profilseite
    private void updateProfilPic() {
        // sit ein Profilbild vorhanden, wird dies geladen
        if (userLocalStore.getUserStatusProfilPic()){
            User user = userLocalStore.getUserLogInUser();
            profileImage.setImageURI(userLocalStore.getUserProfilPic());
            mListener.onListFragmentUpdateProfilePic();
        }
        else {
            // es ist kein Profilbild vorhanden und daher wird ein dummy geladen
            profileImage.setImageResource(R.drawable.profile);
        }

    }

    public interface OnListFragmentInteractionListener {
        // Interface was auf der MainActivity klasse gealden wird um das profilbild in der
        // Navigationsbar zu erneuern
        void onListFragmentUpdateProfilePic();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
