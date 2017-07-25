package com.example.android.tourguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAddArtist;
    Spinner spinnerGenre;
    ListView listViewArtists;


    //Database reference object
    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Parse "artists" node because if left blank firebase will return the root node
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");

        //Initialise views
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAddArtist = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenre = (Spinner) findViewById(R.id.spinnerGenres);


        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        //Add Artist to Firebase when button is clicked
        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addArtist();
            }
        });
    }

    //Attach value event listener to the database reference object
    @Override
    protected void onStart() {
        super.onStart();

        databaseArtists.addValueEventListener(new ValueEventListener() {
            //Executed everytime we change something inside the database
            //Use to read the values in the Firebase database as it will fetch all the values inside the specified reference
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }



            //Executed if there is a error
            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }


    /*
       * This method is saving a new artist to the
       * Firebase Realtime Database
       * */

    private void addArtist() {
        //getting the values to save
        //Everytime we add data to our database a new node is generated
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseArtists.push().getKey();

            //creating an Artist Object
            Artist artist = new Artist(id, name, genre);

            //Saving the Artist
            databaseArtists.child(id).setValue(artist);

            //setting editText to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }


}


