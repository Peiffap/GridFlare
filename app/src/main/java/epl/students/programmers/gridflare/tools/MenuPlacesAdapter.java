package epl.students.programmers.gridflare.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.R;

/*
Le plus simple ca va etre de faire deux adapter et de mettre dans le template de place un recycler view pour que de lui meme il aille cherhcer les pieces
Ca va pas etre simple mais ca sera le mieux je pense
 */

public class MenuPlacesAdapter extends RecyclerView.Adapter<MenuPlacesAdapter.LocalViewHolder> {

    class LocalViewHolder extends RecyclerView.ViewHolder{

        View place_template;
        TextView place_name;
        RecyclerView rv;
        //Map<Place, MenuRoomsAdapter> map = new HashMap<>();

        DatabaseManager dm;

        Context ctx;

        LocalViewHolder(View itemView, Context context) {
            super(itemView);
            dm = new DatabaseManager(context);
            place_template = itemView;
            place_name = place_template.findViewById(R.id.d_place_name);
            rv = place_template.findViewById(R.id.d_place_rv);
            ctx = context;
        }

        void display(Place place){
            assert place != null;
            place_template.setTag(place.getIdPlace());
            place_name.setText(place.getPlace_name());

            MenuRoomsAdapter adapter = new MenuRoomsAdapter(dm.readRoom(place));
            //map.put(place, adapter);
            rv.setLayoutManager(new LinearLayoutManager(ctx, LinearLayout.VERTICAL,false));
            rv.setAdapter(adapter);
        }
    }

    private ArrayList<Place> places;

    public MenuPlacesAdapter(ArrayList<Place> places){
        this.places = places;
    }

    @NonNull
    @Override
    public MenuPlacesAdapter.LocalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View templates = layoutInflater.inflate(R.layout.template_places,parent,false);
        return new MenuPlacesAdapter.LocalViewHolder(templates, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull MenuPlacesAdapter.LocalViewHolder holder, int position) {
        holder.display(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void newPlaceAdded(){
        this.notifyItemInserted(places.size() - 1);
    }

    public void roomUpdate(Place p){
        this.notifyItemChanged(places.indexOf(p));
    }

    public void placeRemoved(int p){
        this.notifyItemRemoved(p);
    }
}
