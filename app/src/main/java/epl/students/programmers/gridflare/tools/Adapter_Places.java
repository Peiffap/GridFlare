package epl.students.programmers.gridflare.tools;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import epl.students.programmers.gridflare.R;

public class Adapter_Places extends RecyclerView.Adapter<Adapter_Places.MyViewHolder>{

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView lv_room;

        MyViewHolder(View itemView) {
            super(itemView);
            lv_room = itemView.findViewById(R.id.lv_room_rooms_item);

        }

        void display(Place current_scan){
            assert current_scan != null;
            lv_room.setText(""+current_scan.toString());

        }
    }

    private ArrayList<Place> historic;

    public Adapter_Places(ArrayList<Place> historic){
        this.historic = historic;
    }

    @NonNull
    @Override
    public Adapter_Places.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rooms_item,parent,false);
        return new Adapter_Places.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Places.MyViewHolder holder, int position) {
        holder.display(historic.get(position));
    }

    @Override
    public int getItemCount() {
        return historic.size();
    }
}
